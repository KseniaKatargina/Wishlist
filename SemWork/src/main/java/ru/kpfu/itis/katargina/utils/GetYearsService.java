package ru.kpfu.itis.katargina.utils;

import java.time.LocalDate;

public class GetYearsService {
    static LocalDate currentDate = LocalDate.now();
    static int currentYear = currentDate.getYear();
    static int currentMonth = currentDate.getMonthValue();
    static int currentDay = currentDate.getDayOfMonth();
    public static int getYears(String date){
        int age =  currentYear - Integer.parseInt(date.substring(0, 4));
        if(currentMonth < Integer.parseInt(date.substring(5, 7))){
            age--;
            return age;
        } else if (currentDay < Integer.parseInt(date.substring(8, 10))){
            age--;
            return age;
        }
        return age;
    }
}
