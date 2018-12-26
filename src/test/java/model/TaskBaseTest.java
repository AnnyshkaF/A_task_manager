package model;

import model.Date;
import model.Task;
import model.TaskBase;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TaskBaseTest {
    TaskBase taskBase = TaskBase.getInstance();
    String name = "name";
    String description = "description";
    String user = "user";
    String task_giver = "task_giver";
    String group = "group";
    Date date1 = new Date("2017-12-21", "10:10");
    Date date2 = new Date("2017-12-21", "10:09");
    String year = "2017";
    boolean condition = false;

    @Test
    public void addTaskTest() {
        int N = 10;
        for (int i = 0; i < N; i++) {
            String val = String.valueOf("i");
            taskBase.addTask(new Task(val, val, user, task_giver, date1, date2, group, false));
        }
        assertEquals(N, taskBase.getTaskBase().size());
    }

    @Test
    public void deleteTaskTest() {
        Task task = new Task(name, description, user, task_giver, date1, date2, group, condition);
        taskBase.addTask(task);
        taskBase.deleteTask(task);
        assertTrue(taskBase.getTaskBase().get(0).getCondition());
    }

    @Test
    public void calculateNotDoneStatisticsTest() {
        int N = 10;
        for (int i = 0; i < N; i++) {
            String val = String.valueOf("i");
            taskBase.addTask(new Task(name, description, user, task_giver, date1, date2, group, condition));
        }
        TreeMap<String, ArrayList<Task>> stat = taskBase.calculateNotDoneStatistics(user, year);
        int sum = 0;
        for (Map.Entry<String, ArrayList<Task>> entry : stat.entrySet()) {
            ArrayList<Task> list = entry.getValue();
            sum = sum + list.size();
        }
        assertEquals(N, sum);
    }

    @Test
    public void calculateDoneStatisticsTest() {
        int N = 10;
        for (int i = 0; i < N; i++) {
            String val = String.valueOf("i");
            taskBase.addTask(new Task(name, description, user, task_giver, date1, date2, group, !condition));
        }
        TreeMap<String, ArrayList<Task>> stat = taskBase.calculateDoneStatistics(user, year);
        int sum = 0;
        for (Map.Entry<String, ArrayList<Task>> entry : stat.entrySet()) {
            ArrayList<Task> list = entry.getValue();
            sum = sum + list.size();
        }
        assertEquals(N, sum);
    }
}


