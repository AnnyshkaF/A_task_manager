package model;

import io.TaskIO;

import java.util.ArrayList;
import java.util.List;

public class TaskBase {
    private List<Task> taskBase = new ArrayList<Task>();

    public TaskBase() {
    }

    public List<Task> getTaskBase() {
        return taskBase;
    }

    public void addTask(Task task) {
        taskBase.add(task);
    }

    public void deleteTask(Task task) {
        Task tmp = task;
        tmp.setCondition(true);
        taskBase.remove(task);
        taskBase.add(tmp);
    }

    public void removeTask(Task task){
        taskBase.remove(task);
    }

    public ArrayList<String> getGroups() {
        ArrayList<String> groups = new ArrayList<>();
        for (int i = 0; i < taskBase.size(); i++) {
            String tmpGroup = taskBase.get(i).getGroup();
            if (!groups.contains(tmpGroup)) {
                groups.add(tmpGroup);
            }
        }
        return groups;
    }

    public ArrayList<Task> getTasks(String name, String group) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < taskBase.size(); i++) {
            if(containsUser(taskBase.get(i).getUser(), name) && taskBase.get(i).getGroup().equals(group) && taskBase.get(i).getCondition() == false){
                tasks.add(taskBase.get(i));
            }
        }
        return tasks;
    }

    public Task getTask(String hash){
        for (int i = 0; i < taskBase.size(); i++) {
            if(taskBase.get(i).getHash().equals(hash)) {
                return taskBase.get(i);
            }
        }
        return null;
    }

    public void clear() {
        taskBase.clear();
    }


    private boolean containsUser(String users, String user){
        String[] name = users.split(" ");
        for (int i = 0; i < name.length; i++) {
            if(name[i].equals(user)){
                return true;
            }
        }
        return false;
    }

    public int[][] calculateNotDoneStatistics(String name) {
        int[][] s = new int[5][12];
        for (int i = 0; i < taskBase.size(); i++) {
            Task task = taskBase.get(i);
            if (task.getUser().equals(name)) {
                if (task.isExpired() && !task.getCondition()) {
                    int row = task.getOutcomeDate().getDay() / 6;
                    if (row > 4) row = 4;
                    s[row][task.getOutcomeDate().getMonth() - 1]++;
                }
            }
        }
        return s;
    }

    public int[][] calculateToDoStatistics(String name) {
        int[][] s = new int[5][12];
        for (int i = 0; i < taskBase.size(); i++) {
            Task task = taskBase.get(i);
            if (task.getUser().equals(name)) {
                if (!task.isExpired() && !task.getCondition()) {
                    int row = task.getOutcomeDate().getDay() / 6;
                    if (row > 4) row = 4;
                    s[row][task.getOutcomeDate().getMonth() - 1]++;
                }
            }
        }
        return s;
    }

    public int[][] calculateDoneStatistics(String name) {
        int[][] s = new int[5][12];
        for (int i = 0; i < taskBase.size(); i++) {
            Task task = taskBase.get(i);
            if (task.getUser().equals(name)) {
                if (task.getCondition()) {
                    int row = task.getOutcomeDate().getDay() / 6;
                    if (row > 4) row = 4;
                    s[row][task.getOutcomeDate().getMonth() - 1]++;
                }
            }
        }
        return s;
    }

    public int calculateSum(int[][] stat){
        int sum = 0;
        for (int i = 0; i < stat.length; i++) {
            for (int j = 0; j < stat[i].length; j++) {
                sum = sum + stat[i][j];
            }
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < taskBase.size(); i++) {
            sb.append(taskBase.get(i));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        TaskBase taskBase = new TaskBase();
        StringBuilder sb = new StringBuilder();
        new TaskIO().loadTasksFromFile("C:/Users/Anna/Desktop/calc.xml", taskBase);
        int[][] done = taskBase.calculateDoneStatistics("Anna");
        int[][] undone = taskBase.calculateNotDoneStatistics("Anna");
        int[][] todo = taskBase.calculateToDoStatistics("Anna");


        sb.append("<table>\n");
        sb.append("<tr>\n");
        for (int j = 0; j < 12; j++) {
            sb.append("<th>").append(Date.months.get(j)).append("</th>\n");
        }
        sb.append("</tr>\n");
        for (int i = 0; i < 5; i++) {
            sb.append("<tr>\n");
            for (int j = 0; j < 12; j++) {
 
                if(done[i][j] > undone[i][j] && done[i][j] > todo[i][j]){
                    sb.append("<td color=\"").append("Done").append("\"></td>\n");
                }
                else if(undone[i][j] > done[i][j] && undone[i][j] > todo[i][j]){
                    sb.append("<td color=\"").append("Undone").append("\"></td>\n");
                }
                else
                    sb.append("<td color=\"").append("Todo").append("\"></td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        System.out.println(sb.toString());
    }
}


