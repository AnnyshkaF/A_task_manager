package servlets;

import io.ReaderWriter;

import model.Date;
import model.Task;
import model.TaskBase;
import model.TaskFields;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class TaskEditing extends HttpServlet {
    TaskBase taskBase = TaskBase.getInstance();
    ArrayList<String> groups = new ArrayList<>();
    TreeMap<String, String> map = new TreeMap<String, String>();

    public void init(ServletConfig config) {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        taskBase.clear();

        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();
        PrintWriter out = response.getWriter();
        String task_giver = session.getAttribute(TaskFields.NAME).toString();
        try {
            taskBase.loadTaskBase();
        } catch (Exception e) {
            out.println("<html>\n<body>\n");
            out.println("<h2>Error loading from file</h2>");
            out.println("</body>\n</html>");
            return;
        }

        if (uri.equals("/A_task_man/TaskEditing/OK")) {
            String old_hash = request.getParameter("old_hash");
            Task old_task = taskBase.getTask(old_hash);
            String name = request.getParameter(TaskFields.NAME);
            String description = request.getParameter(TaskFields.DESCRIPTION);
            String[] users = request.getParameterValues(TaskFields.USER);

            Date income_date = old_task.getIncomeDate();
            Date outcome_date = new Date(request.getParameter(TaskFields.OUTCOME_DATE), request.getParameter("time"));
            String isVisibleToOthers = request.getParameter("is_visible_to_others");
            String group = request.getParameter(TaskFields.GROUP);
            String new_group = request.getParameter("new_group");
            if(new_group != null && !(new_group.equals(""))){
                group = new_group;
            }
            taskBase.removeTask(old_task);
            if (isVisibleToOthers == null) {
                for (int i = 0; i < users.length; i++) {
                    if (users[i].equals("Me")) {
                        users[i] = task_giver;
                    }
                    taskBase.addTask(new Task(name, description, users[i], task_giver, income_date, outcome_date, group, false));
                }
            } else {
                taskBase.addTask(new Task(name, description, makeString(users, task_giver), task_giver, income_date, outcome_date, group,false));
            }
            try {
                taskBase.saveTaskBase();
            } catch (Exception e) {
                out.println("<html>\n<body>\n");
                request.getRequestDispatcher("auth_links.html").include(request, response);
                out.println("<h2>Error saving to file</h2>");
                out.println("</body>\n</html>");
                return;
            }
            response.sendRedirect("/A_task_man/TaskTable");
            return;
        }
        if (uri.equals("/A_task_man/TaskEditing/cancel")) {
            String old_hash = request.getParameter("old_hash");
            //response.sendRedirect("/A_task_man/TaskTable");
            response.sendRedirect("/A_task_man/InfoServlet/" + "info?hash=" + old_hash);
            return;
        }

        String hash = request.getParameter("hash");
        out.println("<html>\n<body>\n");
        //request.getRequestDispatcher("auth_links.html").include(request, response);
        out.println(getMainPage(task_giver, hash));
        out.println("</body>\n</html>");
        out.close();

    }

    private String getMainPage(String username, String hash) {
        StringBuilder sb = new StringBuilder();
        Task task = taskBase.getTask(hash);
        new ReaderWriter().read("users.txt", map);
        groups = taskBase.getGroups();
        sb.append("<form method=\"GET\" action=\"/A_task_man/TaskEditing/OK\">\n");

        sb.append("<input style=\"visibility:hidden\" type=\"text\" name=\"old_hash\" value=\"").append(hash).append("\" readonly>\n");
        sb.append("Task name: <input type=\"text\" name=\"name\" value=\"").append(task.getName()).append("\">\n");
        sb.append("Task Description: <input type=\"text\" name=\"description\" value=\"").append(task.getDescription()).append("\">\n");
        sb.append("<br>Give a task to:<br>\n");    //select from users
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String user = entry.getKey();
            sb.append("<input type=\"checkbox\"");
            sb.append(" name=\"user\" value=\"");
            if (user.equals(username)) {
                user = "Me";
            }
            sb.append(user);
            sb.append("\">\n");
            sb.append(user);
            sb.append("<br>");
        }
        sb.append("<input type=\"checkbox\" name=\"is_visible_to_others\" value=\"visible\">Make visible to everyone<br>");
        sb.append("<select name=\"group\">");
        for (int i = 0; i < groups.size(); i++) {
            //<option value="volvo">Volvo</option>
            //selected
            sb.append("<option value=\"");
            sb.append(groups.get(i));
            sb.append("\"");
            if(groups.get(i).equals(task.getGroup())){sb.append(" selected ");}
            sb.append(">\n");
            sb.append(groups.get(i));
            sb.append("</option>");
        }

        sb.append("</select>");
        sb.append("Create new group: <input type=\"text\" name=\"new_group\"><br>");

        sb.append("Deadline: <input type=\"date\" name=\"outcome_date\" value=\"").append(task.getOutcomeDate().toString().split(" ")[0]).append("\">\n");
        sb.append("Time: <input type=\"time\" name=\"time\" value=\"").append(task.getOutcomeDate().toString().split(" ")[1]).append("\">\n");

        sb.append("<input type=\"submit\" value=\"OK\">\n");
        sb.append("</form>");

        sb.append("<form method=\"GET\" action=\"/A_task_man/TaskEditing/cancel\">\n");
        sb.append("<input type=\"submit\" value=\"Cancel\">\n");
        sb.append("<input style=\"visibility:hidden\" type=\"text\" name=\"old_hash\" value=\"").append(hash).append("\" readonly>\n");
        sb.append("</form>");

        return sb.toString();
    }

    private String makeString(String[] name, String task_giver) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length; i++) {
            if (name[i].equals("Me")) {
                name[i] = task_giver;
            }
            sb.append(name[i]);
            sb.append(" ");
        }
        return sb.toString();
    }
}

