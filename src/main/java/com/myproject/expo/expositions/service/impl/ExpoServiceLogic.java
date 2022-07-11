package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Statistic;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.exception.custom.ExpoException;
import com.myproject.expo.expositions.repository.ExpoRepo;
import com.myproject.expo.expositions.repository.StatisticRepo;
import com.myproject.expo.expositions.service.ExpoService;
import com.myproject.expo.expositions.service.ThemeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.myproject.expo.expositions.util.Constant.DATE;
import static com.myproject.expo.expositions.util.Constant.THEME;

/**
 * The ExpoServiceLogic class do transfer operations with Exposition Dto and entity. Do logic and transfer to the repository layer
 */
@Service
@Qualifier("userServiceLogic")
public class ExpoServiceLogic implements ExpoService {
    private static final Logger log = LogManager.getLogger(ExpoServiceLogic.class);
    private final ExpoRepo expoRepo;
    private final ThemeService themeService;
    private final StatisticRepo statisticRepo;
    private final Build<ExpoDto, Exposition> build;

    @Autowired
    public ExpoServiceLogic(ExpoRepo expoRepo, @Qualifier("expoBuild") Build<ExpoDto, Exposition> build,
                            ThemeService themeService, StatisticRepo statisticRepo) {
        this.expoRepo = expoRepo;
        this.themeService = themeService;
        this.statisticRepo = statisticRepo;
        this.build = build;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpoDto> getAll(Pageable pageable) {
        log.info("Into service expo");
        Page<Exposition> all = expoRepo.findAll(pageable);
        return all.map(build::toDto);
    }

    @Transactional
    @Override
    public Exposition addExpo(ExpoDto expoDto, List<Long> hallsIds) {
        setRequiredFieldsForExpo(expoDto);
        Exposition expo;
        try {
            expo = build.toModel(expoDto);
            expo = expoRepo.save(expo);
        } catch (Exception e) {
            log.warn("Cannot add the exposition {}.Some problem.Probably already exists", expoDto.getName());
            throw new RuntimeException("err.add_expo");
        }
        return expo;
    }

    private void setRequiredFieldsForExpo(ExpoDto expoDto) {
        setThemeToDto(expoDto);
        setStatisticToDto(expoDto, addStatisticFirst(expoDto));
    }

    private Statistic addStatisticFirst(ExpoDto expoDto) {
        return statisticRepo.save(expoDto.getStatistic());
    }

    private void setThemeToDto(ExpoDto expoDto) {
        Theme theme = themeService.getById(expoDto.getTheme().getIdTheme());
        expoDto.setTheme(theme);
    }

    private void setStatisticToDto(ExpoDto expoDto, Statistic savedStat) {
        expoDto.getStatistic().setId(savedStat.getId());
        expoDto.getStatistic().setSold(savedStat.getSold());
        expoDto.getStatistic().setTickets(savedStat.getTickets());
    }

    @Override
    public ExpoDto getById(Long id) {
        return build.toDto(expoRepo.getById(id));
    }

    @Transactional
    @Override
    public boolean update(Long id, Exposition expo) {
        expo.setIdExpo(id);
        Theme theme = themeService.getById(expo.getTheme().getIdTheme());
        expo.setTheme(theme);
        try {
            expoRepo.save(expo);
        } catch (Exception e) {
            throw new RuntimeException("err.update_expo");
        }
        return true;
    }

    @Transactional
    @Override
    public boolean changeStatus(Long id, Integer statusId) {
        int res = expoRepo.changeStatus(statusId, id);
        return res > 0;
    }

    @Override
    public List<Exposition> getAll() {
        return expoRepo.findAll();
    }

    @Override
    public List<ExpoDto> getSearchedExpos(String searchItem, String selected) {
        checkInputNotNull(searchItem);
        List<Exposition> resList = null;
        resList = getResList(searchItem, selected, resList);
        if (resList.size() == 0) {
            throw new ExpoException("err.nothing_found");
        }
        return resList.stream()
                .map(build::toDto)
                .collect(Collectors.toList());
    }

    private List<Exposition> getResList(String searchItem, String selected, List<Exposition> resList) {
        if (selected.equals(THEME)) {
            resList = getByThemeName(searchItem);
        } else if (selected.equals(DATE)) {
            resList = getByExpoDate(searchItem);
        }
        return resList;
    }

    private List<Exposition> getByExpoDate(String searchItem) {
        return expoRepo.findByExpoDate(parseStrToLocalDate(searchItem.trim()));
    }

    private List<Exposition> getByThemeName(String searchItem) {
        return expoRepo.findByThemeName(searchItem);

    }

    private void checkInputNotNull(String searchItem) {
        if (searchItem.isEmpty() || (searchItem == null)) {
            throw new ExpoException("err.search_input");
        }
    }

    private LocalDate parseStrToLocalDate(String date) {
        final DateTimeFormatter datePatternEng = DateTimeFormatter.ofPattern("M/d/yy");
        final DateTimeFormatter datePatternUkr = DateTimeFormatter.ofPattern("dd.MM.yy");
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date, Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{2}").matcher(date).matches()
                    ? datePatternEng : datePatternUkr);
        } catch (DateTimeParseException e) {
            log.warn("The date {} input was incorrect",date);
            throw new ExpoException("err.date_input");
        }
        return localDate;
    }
}
