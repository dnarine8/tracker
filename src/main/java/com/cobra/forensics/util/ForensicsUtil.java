package com.cobra.forensics.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ForensicsUtil {

    public static String dateToString() {
        return dateToString(System.currentTimeMillis());
    }

    public static String dateToString(long timeInMillis) {
        Date currentDate = new Date(timeInMillis);
        DateFormat df = new SimpleDateFormat("dd_MM_yy_HH_mm_ss_SS");
        return df.format(currentDate);
    }

}
