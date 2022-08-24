package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerHelper;
import com.myproject.expo.expositions.controller.util.ControllerUtil;
import com.myproject.expo.expositions.controller.util.HallControllerUtil;
import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.exception.custom.HallException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The HallController class do CRUD operations with Hall Dto
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class HallController {
    private static final Logger log = LogManager.getLogger(HallController.class);
    private final HallControllerUtil hallUtilController;
    private final ControllerHelper controllerHelper;

    public HallController(HallControllerUtil hallUtilController,ControllerHelper controllerHelper) {
        this.hallUtilController = hallUtilController;
        this.controllerHelper = controllerHelper;
    }

    @GetMapping("/halls")
    public String getHalls(Model model, @PageableDefault(sort = {ID_HALL}, page = 1,
            direction = Sort.Direction.DESC) Pageable pageable) {
        setUpDataToModel(model, pageable);
        try {
            model.addAttribute(HALLS, hallUtilController.getHalls(pageable));
        } catch (HallException e) {
            log.warn("Getting all halls was failed");
            model.addAttribute(ERR_MSG, e.getMessage());
            return URL.ADMIN_HOME_SLASH;
        }
        return URL.ADMIN_HOME_SLASH;
    }

    private void setUpDataToModel(Model model, Pageable pageable) {
        model.addAttribute(HALL_OBJ, new HallDto());
        model.addAttribute(NUMBER_OF_PAGES,
                hallUtilController.countTotalNoOfRequiredPages(hallUtilController.getAllHalls(), pageable));
        model.addAttribute(PAGE, pageable);
    }

    @PostMapping("/halls")
    public String saveHall(@ModelAttribute(HALL_OBJ) @Valid HallDto hallDto,
                           BindingResult bindingResult, Model model,
                           @PageableDefault(sort = {ID_HALL}, page = 0,
                                   direction = Sort.Direction.DESC) Pageable pageable) {
        setUpDataToModel(hallDto, model, pageable);
        if (controllerHelper.inputHasErrors(bindingResult)) {
            log.warn("Cannot save the hall with name = {}. The hall with such name is already exists in the system.",hallDto.getName());
            return URL.ADMIN_HOME_SLASH;
        }
        try{
            hallUtilController.saveHall(hallDto, model, controllerHelper.getResPageable(pageable, ID_HALL));
        }catch (HallException e){
            model.addAttribute(ERR_MSG,e.getMessage());
            return URL.ADMIN_HALLS;
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

    private void setUpDataToModel(HallDto hallDto, Model model, Pageable pageable) {
        model.addAttribute(HALLS, hallUtilController.getHalls(pageable));
        model.addAttribute(PAGE, pageable);
        model.addAttribute(HALL_OBJ, hallDto);
    }

    @PatchMapping(path = "/halls/{id}")
    public String updateHall(@PathVariable(ID) Long id,
                             @ModelAttribute(HALL_OBJ) HallDto hallDto,
                             BindingResult bindingResult, Model model,
                             @PageableDefault(sort = {ID_HALL}, page = 0,
                                     direction = Sort.Direction.DESC) Pageable pageable) {
        return hallUtilController.updateTheHall(id, hallDto, bindingResult,
                model, controllerHelper.getResPageable(pageable, ID_HALL));
    }

    @DeleteMapping("/halls/{id}")
    public String delete(@PathVariable(ID) Long id, Model model,
                         @PageableDefault(sort = {ID_HALL}, page = 0,
                                 direction = Sort.Direction.DESC) Pageable pageable) {
        return hallUtilController.deleteTheHall(id, model, controllerHelper.getResPageable(pageable, ID_HALL));
    }
}
