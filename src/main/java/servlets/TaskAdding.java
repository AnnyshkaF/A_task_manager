package servlets;

import io.*;
import model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class TaskAdding extends HttpServlet {
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
            request.getRequestDispatcher("auth_links.html").include(request, response);
            out.println("<h2>Error loading from file</h2>");
            out.println("</body>\n</html>");
            return;
        }
        if (uri.equals("/A_task_man/TaskAdding/add")) {
            //request.getRequestDispatcher().include(request, response);
            String name = request.getParameter(TaskFields.NAME);
            String description = request.getParameter(TaskFields.DESCRIPTION);
            String[] users = request.getParameterValues(TaskFields.USER);

            java.util.Date dateNow = new java.util.Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String currentDate = formatForDateNow.format(dateNow);

            Date income_date = new Date(currentDate.split(" ")[0], currentDate.split(" ")[1]);
            Date outcome_date = new Date(request.getParameter(TaskFields.OUTCOME_DATE), request.getParameter("time"));
            String isVisibleToOthers = request.getParameter("is_visible_to_others");
            String group = request.getParameter(TaskFields.GROUP);
            String new_group = request.getParameter("new_group");
            if(new_group != null && !(new_group.equals(""))){
                group = new_group;
            }
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
        if (uri.equals("/A_task_man/TaskAdding/cancel")) {
            response.sendRedirect("/A_task_man/TaskTable");
            return;
        }


        out.println("<html>\n<body>\n");
        request.getRequestDispatcher("auth_links.html").include(request, response);
        out.println("<h2>Add an advert:</h2>");
        out.println(getMainPage(task_giver));
        out.println("</body>\n</html>");
        out.close();
    }

    private String getMainPage(String username) {
        StringBuilder sb = new StringBuilder();
        new ReaderWriter().read("users.txt", map);
        groups = taskBase.getGroups(username);
        sb.append("<form method=\"GET\" action=\"/A_task_man/TaskAdding/add\">\n");
        sb.append("Task name: <input type=\"text\" name=\"name\"><br>\n");
        sb.append("Task Description: <input type=\"text\" name=\"description\"><br>\n");
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
        sb.append("Select group:");
        sb.append("<select name=\"group\">");
        for (int i = 0; i < groups.size(); i++) {
            sb.append("<option value=\"");
            sb.append(groups.get(i));
            sb.append("\">\n");
            sb.append(groups.get(i));
            sb.append("</option>");
        }
        sb.append("</select><br>");
        sb.append("Create new group: <input type=\"text\" name=\"new_group\"><br>\n");

        sb.append("Deadline: <input type=\"date\" name=\"outcome_date\">\n");//2018-15-12
        sb.append("Time: <input type=\"time\" name=\"time\"><br>\n"); //
        sb.append("<input type=\"submit\" value=\"add\"><br>\n");
        sb.append("</form>");

        sb.append("<form method=\"GET\" action=\"/A_task_man/TaskAdding/cancel\"><br>\n");
        sb.append("<input type=\"submit\" value=\"cancel\"><br>\n");
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
