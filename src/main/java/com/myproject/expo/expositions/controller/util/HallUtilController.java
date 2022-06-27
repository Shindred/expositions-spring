package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.service.HallService;
import com.myproject.expo.expositions.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
@Slf4j
public class HallUtilController implements ControllerUtils {
    private final HallService hallService;
    private final String URL_BACK = "/admin/halls";
   // private List<Hall> list;

    @Autowired
    public HallUtilController(HallService hallService) {
        this.hallService = hallService;
      //  this.list = list;
    }

    public Page<Hall> getHalls(Pageable pageable) {
//        try {
//            Page<Hall> hallList = hallService.getHalls(pageable);
//            list = hallService.getAll();
//            setAllDataToTheModelGetAllHallsMethod(model,pageable,hallList);
//        }catch (Exception e){
//            log.warn("Cannot get all halls");
//            return setErrMsgAndPathBack(model,"cant_get_halls","/admin/halls");
//        }
        return hallService.getHalls(getPageableFromPageSize(pageable));
    }

    public List<Hall> getAllHalls(){
        return hallService.getAll();
    }

    public int countTotalNoOfRequiredPages(List<Hall> allHalls,Pageable pageable){
        System.out.println("all halls size " + allHalls.size());
        return countNoOfRequiredPagesForPage(allHalls.size(),pageable.getPageSize());
    }

//    private void setAllDataToTheModelGetAllHallsMethod(Model model, Pageable pageable, Page<Hall> allHalls) {
//        model.addAttribute("numberOfPages",countNoOfRequiredPagesForPage(list.size(), pageable.getPageSize()));
//        model.addAttribute("halls", allHalls);
//        model.addAttribute("hallObj", new HallDto());
//        model.addAttribute("page", pageable);
//    }

    public Hall saveHall(HallDto hallDto,
                           BindingResult bindingResult, Model model,Pageable pageable) {
        setPageableAndHallsToTheModel(model, pageable,hallDto);
//        if (inputHasErrors(bindingResult)) {
//            return URL_BACK;
//        }
//        try {
//            hallService.save(hallDto);
//        } catch (Exception e) {
//            log.warn("Cannot save the hall with name {}",hallDto.getName());
//            setAllHallsToTheModel(model, pageable);
//            return setErrMsgAndPathBack(model, "err.hall_exist",  URL_BACK);
//        }
        return hallService.save(hallDto);
    }

    public String updateTheHall(Long id, HallDto hallDto,
                                BindingResult bindingResult, Model model,Pageable pageable) {
        hallDto.setId(id);
        setPageableAndHallsToTheModel(model, pageable,hallDto);
        if (inputHasErrors(bindingResult)) {
            return "/admin/home";
        }
        try {
            hallService.update(hallDto);
        } catch (Exception e) {
            log.warn("Cannot update the hall with name {} with id {}",hallDto.getName(),hallDto.getId());
            setAllHallsToTheModel(model, pageable);
            return setErrMsgAndPathBack(model,"err.hall_update",URL_BACK);
        }
        return Constant.REDIRECT + "/admin/home";
    }

    private void setPageableAndHallsToTheModel(Model model, Pageable pageable,HallDto hallDto) {
        model.addAttribute("page", pageable);
        setAllHallsToTheModel(model, pageable);
        model.addAttribute("hallObj", hallDto);
    }

    private void setAllHallsToTheModel(Model model, Pageable pageable) {
        model.addAttribute("halls",hallService.getHalls(pageable));
    }

    public String deleteTheHall(Long id, Model model,Pageable pageable) {
        try {
            hallService.delete(id);
        } catch (Exception e) {
            log.warn("Cannot delete hall with id {}",id);
            setPageableAndHallsToTheModel(model,pageable,new HallDto());
            return setErrMsgAndPathBack(model, "hall_delete","/admin/home");
        }
        return "redirect:" + "/admin/home";
    }

}
