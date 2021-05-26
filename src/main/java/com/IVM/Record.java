package com.IVM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {
    private Date date;
    private SnackCategory category;
    public final DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");

    public Record(Date _date, SnackCategory snack) {
        date = _date;
        category = snack;
    }

    public String getSnackName() { return category.getName(); }
    public String getDateStr() { return date_format.format(date); }
}
