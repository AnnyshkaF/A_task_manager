package model;

import model.Date;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DateTest {
    @Test
    public void compareTest() {
        //  "yyyy-MM-dd hh:mm"
        Date date1 = new Date("2019-12-21", "10:10");
        Date date2 = new Date("2019-12-21", "10:09");
        java.util.Date dateNow = new java.util.Date();
        assertTrue(date1.compareFirstIsMore(date1, date2));
    }
}
