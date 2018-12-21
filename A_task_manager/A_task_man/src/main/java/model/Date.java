package model;

import java.time.Instant;
import java.util.ArrayList;

public class Date {
    public static ArrayList<String> days = new ArrayList<String>() {{
        add("Mon");
        add("Tue");
        add("Wed");
        add("Thu");
        add("Fri");
        add("Sat");
        add("Sun");
    }};
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
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int year;

    public Date() {
    }

    ;

    public Date(String date) {
        //06-12-2018 13:30
        String[] arr = date.split(" ");
        this.year = Integer.parseInt(arr[3]);
        this.month = months.indexOf(arr[1]) + 1;
        this.day = Integer.parseInt(arr[2]);
        String[] hms = arr[3].split(":");
        this.hour = Integer.parseInt(hms[0]);
        this.minute = Integer.parseInt(hms[1]);
    }

    //http://localhost:8080/TaskAdding/add?name=task4&description=description4&user=user1&outcome_date=2018-12-07&time=17%3A59
    public Date(String date, String time) {
        //06-12-2018 13:30
        String[] d = date.split("-");
        this.day = Integer.parseInt(d[2]);
        this.month = Integer.parseInt(d[1]);
        this.year = Integer.parseInt(d[0]);
        String[] t = time.split(":");
        this.hour = Integer.parseInt(t[0]);
        this.minute = Integer.parseInt(t[1]);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getYear() {
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
        if (date1.year > date2.year) return true;
        if (date1.year < date2.year) return false;
        if (date1.month > date2.month) return true;
        if (date1.month < date2.month) return false;
        if (date1.day > date2.day) return true;
        if (date1.day < date2.day) return false;
        if (date1.hour > date2.hour) return true;
        if (date1.hour < date2.hour) return false;
        if (date1.minute > date2.minute) return true;
        if (date1.minute < date2.minute) return false;
        return false;
    }

    public static void main(String[] args) {
        String[] d = {"Day of the week:", "Month:", "Date:", "Time:", "TimeRegion:", "Year:"};
        String date = java.util.Date.from(Instant.now()).toString();
        System.out.println(date);
        String[] dat = date.split(" ");

        Date date1 = new Date(java.util.Date.from(Instant.now()).toString());
        String date2 = java.util.Date.from(Instant.now()).toString();
        //System.out.println(day > date2);
        System.out.println(date1);
    }
}

