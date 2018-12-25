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
        out.println(getStyle());
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
    private String getStyle(){
        StringBuilder sb = new StringBuilder();
        sb.append("  <style>\n\n" +
                "info{\n" +
                "  font-size: 30px;\n" +
                "  font-weight: normal;\n" +
                "  cursor: default;\n" +
                "  text-shadow: 1px 1px white,\n" +
                "               2px 2px #777;\n" +
                "  color: #333;\n" +
                "  transition: all 1s;\n" +
                "}\n" +
                " info:hover {\n" +
                "    text-shadow: 1px 1px white,\n" +
                "                 2px 2px tomato;\n" +
                "    color: crimson;\n" +
                "    }\n" +
                "\n" +
                "name, description, user, task_giver, income_date, outcome_date , group, condition{\n" +
                "  font-size: 30px;\n" +
                "  color: black;\n" +
                "  background-color: white;\n" +
                "  padding-left: 2%;\n" +
                "\n" +
                "}\n" +
                "\n" +
                "p {\n" +
                "\tbackground: #1379FE;\n" +
                "\tpadding: 8px;\n" +
                "\tfont-size: 40px;\n" +
                "\tcolor: white;\n" +
                "\tborder-color: #1379FE;\n" +
                "}\n" +
                "\n" +
                "input {\n" +
                "  margin-left: auto;\n" +
                "  margin-right: auto;\n" +
                "  width: 300px;\n" +
                "  display: inline;\n" +
                "  padding: 10px 30px;\n" +
                "  margin: 10px 20px;\n" +
                "  position: relative;\n" +
                "  overflow: hidden;\n" +
                "  border: 2px solid #fe6637;\n" +
                "  border-radius: 8px;\n" +
                "  font-family: 'Montserrat', sans-serif; \n" +
                "  color: #fe6637;\n" +
                "  transition: .2s ease-in-out;\n" +
                "}\n" +
                "input:before {\n" +
                "  content: \"\";\n" +
                "  background: linear-gradient(90deg, rgba(255,255,255,.1), rgba(255,255,255,.5));\n" +
                "  height: 50px;\n" +
                "  width: 50px;\n" +
                "}\n" +
                "input:hover {\n" +
                "  background: grey;\n" +
                "  color: #fff;\n" +
                "}\n" +
                "input:hover:before {\n" +
                "  left: 150px;\n" +
                "  transition: .5s ease-in-out;\n" +
                "} \n" +
                "  \n</style>");
        return sb.toString();
    }
}
