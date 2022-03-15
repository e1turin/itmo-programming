package com.github.e1turin.lab5.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

public class DateValidator implements IDateValidator {
    private final String dateFormat;

    public DateValidator(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean isValid(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        return sdf.parse(s, new ParsePosition(0)) != null;
    }
}