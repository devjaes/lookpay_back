package dev.jeep.Lookpay.utils;

import java.util.HashMap;

public class DateUtil {
    public static String transformWebDateToDBDate(String date) {
        // Tue Jan 31 2023 01:11:33 GMT-0500 (Ecuador Time) to 2023-01-31 01:11:33
        HashMap<String, String> months = new HashMap<>();
        months.put("Jan", "01");
        months.put("Feb", "02");
        months.put("Mar", "03");
        months.put("Apr", "04");
        months.put("May", "05");
        months.put("Jun", "06");
        months.put("Jul", "07");
        months.put("Aug", "08");
        months.put("Sep", "09");
        months.put("Oct", "10");
        months.put("Nov", "11");
        months.put("Dec", "12");

        String[] dateParts = date.split(" ");
        String year = dateParts[3];
        String month = months.get(dateParts[1]);
        String day = dateParts[2];
        String time = dateParts[4];
        String[] timeParts = time.split(":");
        String hour = timeParts[0];
        String minute = timeParts[1];
        String second = timeParts[2];
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }
}
