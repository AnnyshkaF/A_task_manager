
import io.*;
import model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class TaskAdding extends HttpServlet {
    TaskBase taskBase = new TaskBase();
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
        String task_giver = session.getAttribute("name").toString();

        try {
            new TaskIO().loadTasksFromFile("tasks.xml", taskBase);
        } catch (Exception e) {
            out.println("<html>\n<body>\n");
            request.getRequestDispatcher("auth_links.html").include(request, response);
            out.println("<h2>Error loading from file</h2>");
            out.println("</body>\n</html>");
            return;
        }
        if (uri.equals("/A_task_man/TaskAdding/add")) {
            //request.getRequestDispatcher().include(request, response);
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String[] users = request.getParameterValues("user");

            java.util.Date dateNow = new java.util.Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String currentDate = formatForDateNow.format(dateNow);

            Date income_date = new Date(currentDate.split(" ")[0], currentDate.split(" ")[1]);
            Date outcome_date = new Date(request.getParameter("outcome_date"), request.getParameter("time"));
            String isVisibleToOthers = request.getParameter("is_visible_to_others");
            String group = request.getParameter("group");
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
                new TaskIO().saveTasksToFile("tasks.xml", taskBase);
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
        groups = taskBase.getGroups();
        sb.append("<form method=\"GET\" action=\"/A_task_man/TaskAdding/add\">\n");

        sb.append("Task name: <input type=\"text\" name=\"name\">\n");
        sb.append("Task Description: <input type=\"text\" name=\"description\">\n");
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
            sb.append("<option value=\"");
            sb.append(groups.get(i));
            sb.append("\">\n");
            sb.append(groups.get(i));
            sb.append("</option>");
        }
        sb.append("</select>");
        sb.append("Create new group: <input type=\"text\" name=\"new_group\"><br>");

        sb.append("Deadline: <input type=\"date\" name=\"outcome_date\">\n");//2018-15-12
        sb.append("Time: <input type=\"time\" name=\"time\">"); //
        sb.append("<input type=\"submit\" value=\"add\">\n");
        sb.append("</form>");

        sb.append("<form method=\"GET\" action=\"/A_task_man/TaskAdding/cancel\">\n");
        sb.append("<input type=\"submit\" value=\"cancel\">\n");
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

    public static String parseDate(String date){
        //2018-01-02
        String res = "";
        String[] s = date.split("-");
        if(s.length == 3) {
             res = s[1] + "-" + s[2] + "-" + s[0];
        }
        return res;
        //01-02-2018
    }

    public static void main(String[] args) {
        System.out.println(parseDate("2019-11-01"));
        System.out.println(parseDate(java.util.Date.from(Instant.now()).toString()));
        java.util.Date dateNow = new java.util.Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String currentDate = formatForDateNow.format(dateNow);
        System.out.println(currentDate);
    }
}
