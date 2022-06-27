package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerUtils;
import com.myproject.expo.expositions.controller.util.HallUtilController;
import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.exception.custom.HallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class HallController implements ControllerUtils {
    private static final String ADMIN_HALLS_PAGE = "/admin/halls";
    private final HallUtilController hallUtilController;

    @Autowired
    public HallController(HallUtilController hallUtilController) {
        this.hallUtilController = hallUtilController;
    }

    @GetMapping("/halls")
    public String getHalls(Model model,
                           @PageableDefault(sort = {"idHall"},page = 1,direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("hallObj", new HallDto());
        model.addAttribute("numberOfPages",hallUtilController.countTotalNoOfRequiredPages(pageable));
        model.addAttribute("page", pageable);
        try{
            model.addAttribute("halls", hallUtilController.getHalls(pageable));
        }catch (HallException e){
            model.addAttribute("errMsg", e.getMessage());
            return "/admin/home";
        }
        return"/admin/home";
    }

    @PostMapping("/halls")
    public String saveHall(@ModelAttribute("hallObj") @Valid HallDto hallDto,
                           BindingResult bindingResult, Model model,
                           @PageableDefault(sort = {"idHall"},page = 0,
                                   direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("halls",hallUtilController.getHalls(pageable));
        model.addAttribute("page", pageable);
        model.addAttribute("hallObj", hallDto);
        if (inputHasErrors(bindingResult)) {
            return "/admin/home";
        }
        hallUtilController.saveHall(hallDto, bindingResult, model, getResPageable(pageable,"idHall"));
        return "redirect:/admin/home";
    }

    @PatchMapping(path = "/halls/{id}")
    public String updateHall(@PathVariable("id") Long id,
                             @ModelAttribute("hallObj") HallDto hallDto,
                             BindingResult bindingResult, Model model,
                             @PageableDefault(sort = {"idHall"},page = 0,
                                     direction = Sort.Direction.DESC) Pageable pageable) {
        return hallUtilController.updateTheHall(id, hallDto, bindingResult,
                model,getResPageable(pageable,"idHall"));
    }

    @DeleteMapping("/halls/{id}")
    public String delete(@PathVariable("id") Long id, Model model,
                         @PageableDefault(sort = {"idHall"},page = 0,
                                 direction = Sort.Direction.DESC) Pageable pageable) {
        return hallUtilController.deleteTheHall(id, model,getResPageable(pageable,"idHall"));
    }
}
