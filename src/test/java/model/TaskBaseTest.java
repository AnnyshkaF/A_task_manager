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
    private TaskBase taskBase = TaskBase.getInstance();
    private String name = "name";
    private String description = "description";
    private String user = "user";
    private String task_giver = "task_giver";
    private String group = "group";
    private Date date1 = new Date("2017-12-21", "10:10");
    private Date date2 = new Date("2017-12-21", "10:09");
    private String year = "2017";
    private boolean condition = false;

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


