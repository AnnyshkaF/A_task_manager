package model;

import model.Date;
import model.Task;
import org.junit.Test;

import static org.junit.Assert.*;

public class TaskTest {
    @Test
    public void isExpiredTest() {
        Date income1 = new Date("2018-12-20", "00:10");
        Date outcome1 = new Date("2018-12-20", "10:10");
        Task task1 = new Task("1", "1", "1", "2", income1, outcome1, "home", false);
        assertTrue(task1.isExpired());

        Date income2 = new Date("2019-12-21", "00:10");
        Date outcome2 = new Date("2019-12-22", "10:10");
        Task task2 = new Task("1", "1", "1", "2", income2, outcome2, "home", false);
        assertFalse(task2.isExpired());
    }
}
