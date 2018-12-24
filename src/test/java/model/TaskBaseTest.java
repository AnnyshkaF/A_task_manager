package model;

import model.Date;
import model.Task;
import model.TaskBase;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TaskBaseTest {

    @BeforeClass
    public static void init(){

    }

    @Test
    public void addTaskTest() {
        TaskBase taskBase = new TaskBase();
        int N = 10;
        String user = "user";
        String task_giver = "task_giver";
        String group = "group";
        Date date1 = new Date("2019-12-21", "10:10");
        Date date2 = new Date("2019-12-21", "10:09");

        for (int i = 0; i < N; i++) {
            String val = String.valueOf("i");
            taskBase.addTask(new Task(val, val, user, task_giver, date1, date2, group, false));
        }
        assertEquals(N, taskBase.getTaskBase().size());
    }

    @Test
    public void deleteTaskTest() {
        TaskBase taskBase = new TaskBase();
        String name = "name";
        String description = "description";
        String user = "user";
        String task_giver = "task_giver";
        String group = "group";
        Date date1 = new Date("2019-12-21", "10:10");
        Date date2 = new Date("2019-12-21", "10:09");
        boolean condition = false;
        Task task = new Task(name, description, user, task_giver, date1, date2, group, condition);

        taskBase.addTask(task);
        taskBase.deleteTask(task);

        assertTrue(taskBase.getTaskBase().get(0).getCondition());
    }

    @Test
    public void calculateNotDoneStatisticsTest() {
        TaskBase taskBase = new TaskBase();
        int N = 10;
        String user = "user";
        String task_giver = "task_giver";
        String group = "group";
        Date date1 = new Date("2017-12-21", "10:10");
        Date date2 = new Date("2017-12-21", "10:09");
        boolean condition = false;

        for (int i = 0; i < N; i++) {
            String val = String.valueOf("i");
            taskBase.addTask(new Task(val, val, user, task_giver, date1, date2, group, condition));
        }
        //int[][] stat = taskBase.calculateNotDoneStatistics(user);

        //assertEquals(N, taskBase.calculateSum(stat));
    }

    @Test
    public void calculateToDoStatisticsTest() {
        TaskBase taskBase = new TaskBase();
        int N = 10;
        String user = "user";
        String task_giver = "task_giver";
        String group = "group";
        Date date1 = new Date("2019-12-21", "10:10");
        Date date2 = new Date("2019-12-21", "10:09");
        boolean condition = false;

        for (int i = 0; i < N/2; i++) {
            String val = String.valueOf("i");
            taskBase.addTask(new Task(val, val, user, task_giver, date1, date2, group, condition));
        }
        for (int i = N/2; i < N; i++) {
            String val = String.valueOf("i");
            taskBase.addTask(new Task(val, val, user, task_giver, date1, date2, group, !condition));
        }
        //int[][] stat = taskBase.calculateToDoStatistics(user);

        //assertEquals(N/2, taskBase.calculateSum(stat));
    }

    @Test
    public void calculateDoneStatisticsTest() {
        TaskBase taskBase = new TaskBase();
        int N = 10;
        String user = "user";
        String task_giver = "task_giver";
        String group = "group";
        Date date1 = new Date("2017-12-21", "10:10");
        Date date2 = new Date("2017-12-21", "10:09");
        boolean condition = false;

        for (int i = 0; i < N/2; i++) {
            String val = String.valueOf("i");
            taskBase.addTask(new Task(val, val, user, task_giver, date1, date2, group, condition));
        }
        for (int i = N/2; i < N; i++) {
            String val = String.valueOf("i");
            taskBase.addTask(new Task(val, val, user, task_giver, date1, date2, group, !condition));
        }
        //int[][] stat = taskBase.calculateDoneStatistics(user);

        //assertEquals(N/2, taskBase.calculateSum(stat));
    }

}


