package servlets;

import model.Task;
import model.TaskBase;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.servlet.*;
import javax.servlet.http.*;

public class TasksPerPeriod extends HttpServlet {
    static TaskBase taskBase = TaskBase.getInstance();

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        taskBase.clear();
        response.setContentType("text/html");
        String uri = request.getRequestURI();
        PrintWriter out = response.getWriter();

        if (uri.equals("/A_task_man/TasksPerPeriod/cancel")) {
            response.sendRedirect("/A_task_man/ProfileServlet");
            return;
        }
        out.println("<html><head>");
        out.println("</head><body>");
        out.println("<link rel=\"stylesheet\" href=\"style2.css\"/>\n");
        HttpSession session = request.getSession(false);
        String name = "";
        name = (String) session.getAttribute("name");

        out.println("<p>Tasks</p>");
        try {
            taskBase.loadTaskBase();
        } catch (Exception e) {
            out.println("Error reading from file");
            out.println("</body></html>");
            out.close();
            return;
        }
        int r = Integer.parseInt(request.getParameter("r"));
        int c = Integer.parseInt(request.getParameter("c"));
        String year = request.getParameter("year");
        out.println(getList(name, r, c, year));
        out.println("</body></html>");
    }

    public static String getList(String user, int r, int c, String year) {
        StringBuilder sb = new StringBuilder();
        sb.append("<form method=\"GET\" action=\"/A_task_man/TasksPerPeriod/cancel\">\n");
        sb.append("<input type=\"submit\" value=\"cancel\">\n");
        sb.append("</form>\n");

        TreeMap<String, ArrayList<Task>> values = new TreeMap<String, ArrayList<Task>>();
        for (int j = 0; j < 3; j++) {
            if (j == 0) {
                values = taskBase.calculateDoneStatistics(user, year);
                sb.append("<h1>Done</h1>\n");
            }
            if (j == 1) {
                values = taskBase.calculateNotDoneStatistics(user, year);
                sb.append("<h1>Undone</h1>\n");
            }
            if (j == 2) {
                values = taskBase.calculateToDoStatistics(user, year);
                sb.append("<h1>ToDo</h1>\n");
            }

            ArrayList<Task> tmp = values.get(index(r, c));
            if (tmp.size() > 0) {
                sb.append("<table>\n");
                sb.append("<tr>\n<th>Task</th>\n<th>Description</th>\n<th>User</th>\n<th>Head</th>\n<th>Income date</th>\n<th>Outcome date</th>\n<th>Group</th>\n</tr>\n");
                for (int i = 0; i < tmp.size(); i++) {
                    sb.append("<tr>\n");
                    sb.append("<td><name>" + tmp.get(i).getName() + "</name></td>\n");
                    sb.append("<td><description>" + tmp.get(i).getDescription() + "</description></td>\n");
                    sb.append("<td><username>" + tmp.get(i).getUser() + "</username></td>\n");
                    sb.append("<td><task_giver>" + tmp.get(i).getTaskGiver() + "</task_giver></td>\n");
                    sb.append("<td><income_date>" + tmp.get(i).getIncomeDate() + "</income_date></td>\n");
                    sb.append("<td><outcome_date>" + tmp.get(i).getOutcomeDate() + "</outcome_date></td>\n");
                    sb.append("<td><group>" + tmp.get(i).getGroup() + "</group></td>\n");
                    sb.append("</tr>\n");
                }
                sb.append("</table>\n");
            } else {
                sb.append("<div>Nothing</div>\n");
            }
        }
        taskBase.clear();
        return sb.toString();
    }

    private static String index(int i, int j) {
        return String.valueOf(i + "-" + j);
    }
}

