package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.service.HallService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The HallUtilController class do transfer operations for Hall Dto entity. Validate data for the service layer
 */
@Component
public class HallUtilController implements ControllerUtils {
    private static final Logger log = LogManager.getLogger(HallUtilController.class);
    private final HallService hallService;

    @Autowired
    public HallUtilController(HallService hallService) {
        this.hallService = hallService;
    }

    public Page<Hall> getHalls(Pageable pageable) {
        return hallService.getHalls(getPageableFromPageSize(pageable));
    }

    public List<Hall> getAllHalls() {
        return hallService.getAll();
    }

    public int countTotalNoOfRequiredPages(List<Hall> allHalls, Pageable pageable) {
        System.out.println("all halls size " + allHalls.size());
        return countNoOfRequiredPagesForPage(allHalls.size(), pageable.getPageSize());
    }

    public Hall saveHall(HallDto hallDto, Model model, Pageable pageable) {
        setPageableAndHallsToTheModel(model, pageable, hallDto);
        return hallService.save(hallDto);
    }

    public String updateTheHall(Long id, HallDto hallDto,
                                BindingResult bindingResult, Model model, Pageable pageable) {
        hallDto.setId(id);
        setPageableAndHallsToTheModel(model, pageable, hallDto);
        if (inputHasErrors(bindingResult)) {
            return URL.ADMIN_HOME_SLASH;
        }
        try {
            hallService.update(hallDto);
        } catch (Exception e) {
            log.warn("Cannot update the hall with name {} with id {}", hallDto.getName(), hallDto.getId());
            setAllHallsToTheModel(model, pageable);
            return setErrMsgAndPathBack(model, "err.hall_update", URL.ADMIN_HALLS);
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

    private void setPageableAndHallsToTheModel(Model model, Pageable pageable, HallDto hallDto) {
        model.addAttribute(PAGE, pageable);
        setAllHallsToTheModel(model, pageable);
        model.addAttribute(HALL_OBJ, hallDto);
    }

    private void setAllHallsToTheModel(Model model, Pageable pageable) {
        model.addAttribute(HALLS, hallService.getHalls(pageable));
    }

    public String deleteTheHall(Long id, Model model, Pageable pageable) {
        try {
            hallService.delete(id);
        } catch (Exception e) {
            log.warn("Cannot delete hall with id {}", id);
            setPageableAndHallsToTheModel(model, pageable, new HallDto());
            return setErrMsgAndPathBack(model, "hall_delete", URL.ADMIN_HOME_SLASH);
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

}
