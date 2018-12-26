package servlets;

import model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class InfoServlet extends HttpServlet {
    private TaskBase taskBase = TaskBase.getInstance();

    public void init(ServletConfig config) {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        taskBase.clear();

        String uri = request.getRequestURI();
        PrintWriter out = response.getWriter();
        out.println("<html>\n<body>\n");
        out.println("<link rel=\"stylesheet\" href=\"style2.css\"/>\n");
        if (uri.equals("/A_task_man/InfoServlet/cancel")) {
            response.sendRedirect("/A_task_man/TaskTable");
            return;
        }
        if (uri.equals("/A_task_man/InfoServlet/edit")) {
            String hash = request.getParameter("hash");
            String ur = "/A_task_man/TaskEditing/edit?hash=" + hash;
            response.sendRedirect(ur);
            return;
        }
        String hash = request.getParameter("hash");
        out.println(getTaskInfo(hash));


        out.println(getButtons(hash));
        out.println("</body>\n</html>");
        out.close();
    }

    private String getTaskInfo(String hash) {
        StringBuilder sb = new StringBuilder();
        try {
            taskBase.loadTaskBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Task task = taskBase.getTask(hash);
        sb.append("<p>").append(task.getName()).append("</p>\n");
        sb.append("<info>\n");
        sb.append("Description: <description>").append(task.getDescription()).append("</description>").append("<br>\n");
        sb.append("User: <user>").append(task.getUser()).append("</user>").append("<br>\n");
        sb.append("Head: <task_giver>").append(task.getTaskGiver()).append("</task_giver>").append("<br>\n");
        sb.append("Income date: <income_date>").append(task.getIncomeDate()).append("</income_date>").append("<br>\n");
        sb.append("Outcome date: <outcome_date>").append(task.getOutcomeDate()).append("</outcome_date>").append("<br>\n");
        sb.append("Group: <group>").append(task.getGroup()).append("</group>").append("<br>");
        sb.append("Condition: <condition>").append(task.getCondition()).append("</condition>").append("<br>");
        sb.append("</info>\n");
        return sb.toString();
    }

    private String getButtons(String hash){
        StringBuilder sb = new StringBuilder();
        sb.append("\n<form method=\"GET\" action=\"/A_task_man/InfoServlet/cancel\">\n");
        sb.append("<input type=\"submit\" value=\"cancel\">\n");
        sb.append("</form>\n");
        sb.append("<form method=\"GET\" action=\"/A_task_man/InfoServlet/edit\">\n");
        sb.append("<input type=\"submit\" value=\"edit\">\n");
        sb.append("<input style=\"visibility:hidden\" type=\"text\" name=\"hash\" value=\"").append(hash).append("\" readonly>\n");
        sb.append("</form><br>");
        return sb.toString();
    }
}
