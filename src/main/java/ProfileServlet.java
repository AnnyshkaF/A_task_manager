import io.TaskIO;
import model.Date;
import model.Task;
import model.TaskBase;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.*;
import javax.servlet.http.*;

public class ProfileServlet extends HttpServlet {

    private TaskBase taskBase = new TaskBase();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        taskBase.clear();
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String user = session.getAttribute("name").toString();
        out.println("<html><body>");
        if (session != null) {
            request.getRequestDispatcher("auth_links.html").include(request, response);
            String name = (String) session.getAttribute("name");
            out.print("User:" + name);
        } else {
            out.print("Please login first");
            request.getRequestDispatcher("links.html").include(request, response);
            request.getRequestDispatcher("login.html").include(request, response);
        }
        try {
            new TaskIO().loadTasksFromFile("tasks.xml", taskBase);
        } catch (Exception e) {
            out.println("Error reading from file");
            out.println("</body></html>");
            out.close();
            return;
        }
        out.println(getTable(user));
        out.println("</html></body>");
        out.close();
    }

    private String getTable(String user) {
        StringBuilder sb = new StringBuilder();
        TreeMap<String, ArrayList<Task>> done = taskBase.calculateDoneStatistics(user);
        TreeMap<String, ArrayList<Task>> undone = taskBase.calculateNotDoneStatistics(user);
        TreeMap<String, ArrayList<Task>> todo = taskBase.calculateToDoStatistics(user);

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
                else if(undone.get(index(i,j)).size() > done.get(index(i,j)).size() && undone.get(index(i,j)).size() > todo.get(index(i,j)).size()){
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
                sb.append("<a href=\"TasksPerPeriod/period?").append("r=").append(i).append("&c=").append(j).append("\"></a>");
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

    private String getTasks(Map<String, ArrayList<Task>> map){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                ArrayList<Task> t = map.get(index(i, j));
                if(t.size() != 0){
                    for (int k = 0; k < t.size(); k++) {
                        sb.append(t.get(k).getName());
                        sb.append("\n");
                    }
                }
            }
        }
        return  sb.toString();
    }
}