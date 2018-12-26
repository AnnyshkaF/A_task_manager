package servlets;

import model.Date;
import model.Task;
import model.TaskBase;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.servlet.*;
import javax.servlet.http.*;

public class ProfileServlet extends HttpServlet {

    private TaskBase taskBase = TaskBase.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        taskBase.clear();

        java.util.Date dateNow = new java.util.Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy");
        //String year = formatForDateNow.format(dateNow);

        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String user = session.getAttribute("name").toString();
        String uri = request.getRequestURI();

        out.println("<html><body>");

        /*if (uri.equals("/ProfileServlet/change")) {
            String tmp_year = request.getParameter("year");
            if (tmp_year != null) {
                year = tmp_year;
            }
            out.println("<script>alert(\"Does smt\");</script>");
            request.getRequestDispatcher("ProfileServlet").forward(request, response);
            return;
            //request.setAttribute("year", year);

        }*/
        if (uri.equals("/A_task_man/ProfileServlet/change")) {

            String tmp_year = request.getParameter("year");
            if (tmp_year != null) {
                taskBase.year = tmp_year;
            }else{

            }
            response.sendRedirect("/A_task_man/ProfileServlet");
            return;
            //response.sendRedirect("/A_task_man/ProfileServlet/change?year=" + year);
        }

            if (session != null) {
                request.getRequestDispatcher("auth_links.html").include(request, response); //can't find this
                String name = (String) session.getAttribute("name");
                out.print("User:" + name);
            } else {
                out.print("Please login first");
                request.getRequestDispatcher("links.html").include(request, response);
                request.getRequestDispatcher("login.html").include(request, response);
            }
            out.println(getTable(user, taskBase.year));
            out.println("</html></body>");
            taskBase.year = formatForDateNow.format(dateNow);
            out.close();

    }
    private String getTable(String user, String year) {
        StringBuilder sb = new StringBuilder();
        try {
            taskBase.loadTaskBase();
        } catch (Exception e) {
            sb.append("Error reading from file");
            sb.append("</body></html>");
            return sb.toString();
        }
        TreeMap<String, ArrayList<Task>> done = taskBase.calculateDoneStatistics(user, year);
        TreeMap<String, ArrayList<Task>> undone = taskBase.calculateNotDoneStatistics(user, year);
        TreeMap<String, ArrayList<Task>> todo = taskBase.calculateToDoStatistics(user, year);
        sb.append("<h1>").append(year).append("<h1>\n");
        sb.append("<form method=\"GET\" action=\"/A_task_man/ProfileServlet/change\"><br>\n");
        sb.append(" Year: <input type=\"number\" min=\"1900\" max=\"2099\" step=\"1\" value=\"").append(year).append("\" name=\"year\"/>\n");
        sb.append("<button type=\"submit\">Submit</button>");
        sb.append("</form>");

        sb.append("<table>\n");
        sb.append("<tr>\n");
        for (int j = 0; j < 12; j++) {
            sb.append("<th>").append(Date.months.get(j)).append("</th>\n");
        }
        sb.append("</tr>\n");
        for (int i = 0; i < 5; i++) {
            sb.append("<tr>\n");
            for (int j = 0; j < 12; j++) {
                if(done.get(index(i,j)).size() > undone.get(index(i,j)).size() && done.get(index(i,j)).size() > todo.get(index(i,j)).size()){
                    sb.append("<td color=\"").append("Done").append("\"");
                }
                else if(undone.get(index(i,j)).size() > 0){
                    sb.append("<td color=\"").append("Undone").append("\"");
                }
                else if(todo.get(index(i,j)).size() > done.get(index(i,j)).size() && todo.get(index(i,j)).size() > undone.get(index(i,j)).size()){
                    sb.append("<td color=\"").append("Todo").append("\"");
                }
                else {
                    sb.append("<td color=\"").append("Free").append("\"");
                }
                sb.append(" title=\"Done = ").append(done.get(index(i,j)).size());
                sb.append(" Undone = ").append(undone.get(index(i,j)).size());
                sb.append(" Todo = ").append(todo.get(index(i,j)).size()).append("\">");
                sb.append("<a href=\"TasksPerPeriod/period?").append("r=").append(i).append("&c=").append(j).append("&year").append(year).append("\"></a>");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }
    private static String index(int i, int j){
        return String.valueOf(i + "-" + j);
    }

    public static void main(String[] args) {
        java.util.Date dateNow = new java.util.Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy");
        String currentDate = formatForDateNow.format(dateNow);
        System.out.println(currentDate);
    }
}