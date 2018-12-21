package model;

import java.text.SimpleDateFormat;

public class Task {
    private String name;
    private String description;
    private String user;
    private String task_giver;
    private Date income_date;
    private Date outcome_date;
    private String group;
    private boolean condition;
    private String hash;

    public Task(String name, String description, String user, String task_giver, Date income_date, Date outcome_date, String group, boolean condition) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.task_giver = task_giver;
        this.income_date = income_date;
        this.outcome_date = outcome_date;
        this.group = group;
        this.condition = condition;
        this.hash = generateHash(name + description + group);
    }

    private String generateHash(String str) {
        int hash = 7;
        for (int i = 0; i < str.length(); i++) {
            hash = hash * 31 + str.charAt(i);
        }
        return String.valueOf(hash);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUser() {
        return user;
    }

    public String getTaskGiver() {
        return task_giver;
    }

    public Date getIncomeDate() {
        return income_date;
    }

    public Date getOutcomeDate() {
        return outcome_date;
    }

    public String getGroup() {
        return group;
    }

    public boolean getCondition() {
        return condition;
    }

    public String getHash() {
        return hash;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public boolean isExpired(){
        java.util.Date dateNow = new java.util.Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String currentDate = formatForDateNow.format(dateNow);
        Date currDate = new Date(currentDate.split(" ")[0], currentDate.split(" ")[1]);

        if(currDate.compareFirstIsMore(currDate, outcome_date)){
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------");
        sb.append("\n");
        sb.append("Name: ").append(name);
        sb.append("\n");
        sb.append("Description: ").append(description);
        sb.append("\n");
        sb.append("User: ").append(user);
        sb.append("\n");
        sb.append("Task_giver: ").append(task_giver);
        sb.append("\n");
        sb.append("Income_date: ").append(income_date);
        sb.append("\n");
        sb.append("Outcome_date: ").append(outcome_date);
        sb.append("\n");
        sb.append("Group: ").append(group);
        sb.append("\n");
        sb.append("Condition: ").append(condition);
        sb.append("\n");
        return sb.toString();
    }
}
