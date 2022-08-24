package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerHelper;
import com.myproject.expo.expositions.controller.util.HallControllerUtil;
import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.exception.custom.HallException;
import com.myproject.expo.expositions.service.HallService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    private final HallService hallService;

    public HallController(HallControllerUtil hallUtilController, ControllerHelper controllerHelper, HallService hallService) {
        this.hallUtilController = hallUtilController;
        this.controllerHelper = controllerHelper;
        this.hallService = hallService;
    }

    @GetMapping("/halls")
    public String getHalls(Model model, @PageableDefault(sort = {ID_HALL}, page = 1,
            direction = Sort.Direction.DESC) Pageable pageable) {
        setUpDataToModel(model, pageable);
        try {
            model.addAttribute(HALLS, hallService.getHalls(controllerHelper.getPageableFromPageSize(pageable)));
        } catch (HallException e) {
            log.warn("Getting all halls was failed");
            model.addAttribute(ERR_MSG, e.getMessage());
            return URL.ADMIN_HOME_SLASH;
        }
        return URL.ADMIN_HOME_SLASH;
    }

    private void setUpDataToModel(Model model, Pageable pageable) {
        model.addAttribute(HALL_OBJ, new HallDto());
        model.addAttribute(NUMBER_OF_PAGES, countTotalNoOfRequiredPages(hallService.getAll(), pageable));
        model.addAttribute(PAGE, pageable);
    }

    public int countTotalNoOfRequiredPages(List<Hall> allHalls, Pageable pageable) {
        return controllerHelper.countNoOfRequiredPagesForPage(allHalls.size(), pageable.getPageSize());
    }

    @PostMapping("/halls")
    public String saveHall(@ModelAttribute(HALL_OBJ) @Valid HallDto hallDto,
                           BindingResult bindingResult, Model model,
                           @PageableDefault(sort = {ID_HALL}, page = 0,
                                   direction = Sort.Direction.DESC) Pageable pageable) {
        setUpDataToModel(hallDto, model, pageable);
        if (controllerHelper.inputHasErrors(bindingResult)) {
            log.warn("Cannot save the hall with name = {}. The hall with such name is already exists in the system.", hallDto.getName());
            return URL.ADMIN_HOME_SLASH;
        }
        try {
            saveHall(hallDto, model, controllerHelper.getResPageable(pageable, ID_HALL));
        } catch (HallException e) {
            model.addAttribute(ERR_MSG, e.getMessage());
            return URL.ADMIN_HALLS;
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

    public Hall saveHall(HallDto hallDto, Model model, Pageable pageable) {
        setPageableAndHallsToTheModel(model, pageable, hallDto);
        return hallService.save(hallDto);
    }

    private void setPageableAndHallsToTheModel(Model model, Pageable pageable, HallDto hallDto) {
        model.addAttribute(PAGE, pageable);
        setAllHallsToTheModel(model, pageable);
        model.addAttribute(HALL_OBJ, hallDto);
    }

    private void setAllHallsToTheModel(Model model, Pageable pageable) {
        model.addAttribute(HALLS, hallService.getHalls(pageable));
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
        return updateTheHall(id, hallDto, bindingResult,
                model, controllerHelper.getResPageable(pageable, ID_HALL));
    }

    public String updateTheHall(Long id, HallDto hallDto,
                                BindingResult bindingResult, Model model, Pageable pageable) {
        hallDto.setId(id);
        setPageableAndHallsToTheModel(model, pageable, hallDto);
        if (controllerHelper.inputHasErrors(bindingResult)) {
            return URL.ADMIN_HOME_SLASH;
        }
        try {
            hallService.update(hallDto);
        } catch (Exception e) {
            log.warn("Cannot update the hall with name {} and id {}. The hall with such name already exists.", hallDto.getName(), hallDto.getId());
            setAllHallsToTheModel(model, pageable);
            return controllerHelper.setErrMsgAndPathBack(model, "err.hall_update", URL.ADMIN_HALLS);
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

    @DeleteMapping("/halls/{id}")
    public String delete(@PathVariable(ID) Long id, Model model,
                         @PageableDefault(sort = {ID_HALL}, page = 0,
                                 direction = Sort.Direction.DESC) Pageable pageable) {
        return deleteTheHall(id, model, controllerHelper.getResPageable(pageable, ID_HALL));
    }

    public String deleteTheHall(Long id, Model model, Pageable pageable) {
        try {
            hallService.delete(id);
        } catch (Exception e) {
            log.warn("Cannot delete hall with id {}. The hall already occupied for the exposition", id);
            setPageableAndHallsToTheModel(model, pageable, new HallDto());
            return controllerHelper.setErrMsgAndPathBack(model, "hall_delete", URL.ADMIN_HOME_SLASH);
        }
        return URL.REDIRECT_ADMIN_HOME;
    }
}
