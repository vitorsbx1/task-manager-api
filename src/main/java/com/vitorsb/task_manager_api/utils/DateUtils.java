package com.vitorsb.task_manager_api.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtils {

    public static LocalDateTime toStartOfDay(LocalDate date){
        return date.atStartOfDay();
    }

    public static LocalDateTime toEndOfDay(LocalDate date){
        return date.atTime(LocalTime.MAX);
    }
}
