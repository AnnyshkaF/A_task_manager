package model;

import io.TaskIO;
import java.util.*;

public class TaskBase {
    private static TaskBase _instance = null;
    public String year = "2018";
    private List<Task> taskBase = new ArrayList<Task>();

    public synchronized static TaskBase getInstance() {
        if (_instance == null)
            _instance = new TaskBase();
        return _instance;
    }

    public TaskBase() {
    }

    public List<Task> getTaskBase() {
        return taskBase;
    }

    public void addTask(Task task) {
        taskBase.add(task);
    }

    // make it done
    public void deleteTask(Task task) {
        Task tmp = task;
        tmp.setCondition(true);
        taskBase.remove(task);
        taskBase.add(tmp);
    }

    public void removeTask(Task task) {
        taskBase.remove(task);
    }

    public ArrayList<String> getGroups(String username) {
        ArrayList<String> groups = new ArrayList<>();
        for (int i = 0; i < taskBase.size(); i++) {
            Task tmp = taskBase.get(i);
            if (tmp.getUser().equals(username) && !tmp.getCondition()) {
                if (!groups.contains(tmp.getGroup())) {
                    groups.add(tmp.getGroup());
                }
            }
        }
        return groups;
    }

    public ArrayList<Task> getTasks(String name, String group) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < taskBase.size(); i++) {
            if (containsUser(taskBase.get(i).getUser(), name) && taskBase.get(i).getGroup().equals(group) && taskBase.get(i).getCondition() == false) {
                tasks.add(taskBase.get(i));
            }
        }
        return tasks;
    }

    public Task getTask(String hash) {
        for (int i = 0; i < taskBase.size(); i++) {
            if (taskBase.get(i).getHash().equals(hash)) {
                return taskBase.get(i);
            }
        }
        return null;
    }

    public void clear() {
        taskBase.clear();
    }


    private boolean containsUser(String users, String user) {
        String[] name = users.split(" ");
        for (int i = 0; i < name.length; i++) {
            if (name[i].equals(user)) {
                return true;
            }
        }
        return false;
    }

    public TreeMap<String, ArrayList<Task>> calculateNotDoneStatistics(String name, String year) {
        TreeMap<String, ArrayList<Task>> tasks = new TreeMap<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                tasks.put(index(i, j), new ArrayList<>());
            }
        }
        for (int i = 0; i < taskBase.size(); i++) {
            Task task = taskBase.get(i);
            if (task.getUser().equals(name) && task.getOutcomeDate().getYear().equals(year)) {
                if (task.isExpired() && !task.getCondition()) {
                    int row = Integer.parseInt(task.getOutcomeDate().getDay()) / 6;
                    if (row > 4) row = 4;
                    tasks.get(index(row, (Integer.parseInt(task.getOutcomeDate().getMonth()) - 1))).add(task);
                }
            }
        }
        return tasks;
    }

    public TreeMap<String, ArrayList<Task>> calculateToDoStatistics(String name, String year) {
        TreeMap<String, ArrayList<Task>> tasks = new TreeMap<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                tasks.put(index(i, j), new ArrayList<>());
            }
        }
        for (int i = 0; i < taskBase.size(); i++) {
            Task task = taskBase.get(i);
            if (task.getUser().equals(name) && task.getOutcomeDate().getYear().equals(year)) {
                if (!task.isExpired() && !task.getCondition()) {
                    int row = Integer.parseInt(task.getOutcomeDate().getDay()) / 6;
                    if (row > 4) row = 4;
                    tasks.get(index(row, (Integer.parseInt(task.getOutcomeDate().getMonth()) - 1))).add(task);
                }
            }
        }
        return tasks;
    }

    public TreeMap<String, ArrayList<Task>> calculateDoneStatistics(String name, String year) {
        TreeMap<String, ArrayList<Task>> tasks = new TreeMap<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                tasks.put(index(i, j), new ArrayList<>());
            }
        }
        for (int i = 0; i < taskBase.size(); i++) {
            Task task = taskBase.get(i);

            if (task.getUser().equals(name) && task.getOutcomeDate().getYear().equals(year)) {
                if (task.getCondition()) {
                    int row = Integer.parseInt(task.getOutcomeDate().getDay()) / 6;
                    if (row > 4) row = 4;
                    tasks.get(index(row, (Integer.parseInt(task.getOutcomeDate().getMonth()) - 1))).add(task);
                }
            }
        }
        return tasks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < taskBase.size(); i++) {
            sb.append(taskBase.get(i));
        }
        return sb.toString();
    }

    private static String index(int i, int j) {
        return String.valueOf(i + "-" + j);
    }

    public void loadTaskBase() {
        try {
            new TaskIO().loadTasksFromFile("tasks.xml", this);
        } catch (Exception e) {
        }
    }

    public void saveTaskBase() {
        try {
            new TaskIO().saveTasksToFile("tasks.xml", this);
        } catch (Exception e) {
        }
    }
}

