package com.example.cardformlib.utils;

import android.text.TextUtils;

import java.util.Calendar;


public class DateValidator {


    private static final int MAXIMUM_VALID_YEAR_DIFFERENCE = 20;
    private static final DateValidator INSTANCE = new DateValidator(Calendar.getInstance());

    private final Calendar mCalendar;


    protected DateValidator(Calendar calendar) {
        mCalendar = calendar;
    }

    public static boolean isValid(String month, String year) {
        return INSTANCE.isValidHelper(month, year);
    }

    protected boolean isValidHelper(String monthString, String yearString) {
        if (TextUtils.isEmpty(monthString)) {
            return false;
        }

        if (TextUtils.isEmpty(yearString)) {
            return false;
        }

        if (!TextUtils.isDigitsOnly(monthString) || !TextUtils.isDigitsOnly(yearString)) {
            return false;
        }

        int month = Integer.parseInt(monthString);
        if (month < 1 || month > 12) {
            return false;
        }

        int currentYear = getCurrentTwoDigitYear();
        int year;
        int yearLength = yearString.length();
        if (yearLength == 2) {
            year = Integer.parseInt(yearString);
        } else if (yearLength == 4) {
            year = Integer.parseInt(yearString.substring(2));
        } else {
            return false;
        }

        if (year == currentYear && month < getCurrentMonth()) {
            return false;
        }

        if (year < currentYear) {
            // account for century-overlapping in 2-digit year representations
            int adjustedYear = year + 100;
            if (adjustedYear - currentYear > MAXIMUM_VALID_YEAR_DIFFERENCE) {
                return false;
            }
        }

        if (year > currentYear + MAXIMUM_VALID_YEAR_DIFFERENCE) {
            return false;
        }

        return true;
    }


    private int getCurrentMonth() {
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    private int getCurrentTwoDigitYear() {
        return mCalendar.get(Calendar.YEAR) % 100;
    }
}
