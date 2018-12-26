package model;

import java.util.ArrayList;

public class Date {
    public static ArrayList<String> months = new ArrayList<String>() {{
        add("Jan");
        add("Feb");
        add("Mar");
        add("Apr");
        add("May");
        add("Jun");
        add("Jul");
        add("Aug");
        add("Sep");
        add("Oct");
        add("Nov");
        add("Dec");
    }};
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String year;

    public Date() {
    };

    public Date(String date, String time) {
        String[] d = date.split("-");
        this.day = d[2];
        this.month = d[1];
        this.year = d[0];
        String[] t = time.split(":");
        this.hour = t[0];
        this.minute = t[1];
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getYear() {
        return year;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append("-");
        sb.append(month);
        sb.append("-");
        sb.append(day);
        sb.append(" ");
        sb.append(hour);
        sb.append(":");
        sb.append(minute);
        return sb.toString();
    }

    public boolean compareFirstIsMore(Date date1, Date date2) {
        if (Integer.parseInt(date1.year) > Integer.parseInt(date2.year)) return true;
        if (Integer.parseInt(date1.year) < Integer.parseInt(date2.year)) return false;
        if (Integer.parseInt(date1.month) > Integer.parseInt(date2.month)) return true;
        if (Integer.parseInt(date1.month) < Integer.parseInt(date2.month)) return false;
        if (Integer.parseInt(date1.day) > Integer.parseInt(date2.day)) return true;
        if (Integer.parseInt(date1.day) < Integer.parseInt(date2.day)) return false;
        if (Integer.parseInt(date1.hour) > Integer.parseInt(date2.hour)) return true;
        if (Integer.parseInt(date1.hour) < Integer.parseInt(date2.hour)) return false;
        if (Integer.parseInt(date1.minute) > Integer.parseInt(date2.minute)) return true;
        if (Integer.parseInt(date1.minute) < Integer.parseInt(date2.minute)) return false;
        return false;
    }
}

