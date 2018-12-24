
import io.TaskIO;
import model.Date;
import model.TaskBase;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.*;
import javax.servlet.http.*;

public class ProfileServlet extends HttpServlet {
    public static Map<String, String> colors = new TreeMap<String, String>() {{
        put("Red", "#FF0000");
        put("Orange", "#FFA500");
        put("Blue", "#0000FF");
        put("Violet", "#6A5ACD");
        put("Green", "#3CB371");
        put("White", "#FFFFFF");

    }};
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
        int[][] done = taskBase.calculateDoneStatistics(user);
        int[][] undone = taskBase.calculateNotDoneStatistics(user);
        int[][] todo = taskBase.calculateToDoStatistics(user);

        sb.append("<table>\n");
        sb.append("<tr>\n");
        for (int j = 0; j < 12; j++) {
            sb.append("<th>").append(Date.months.get(j)).append("</th>\n");
        }
        sb.append("</tr>\n");
        for (int i = 0; i < 5; i++) {
            sb.append("<tr>\n");
            for (int j = 0; j < 12; j++) {
                //<td style="background:#C0C0C0"></td>
                if(done[i][j] > undone[i][j] && done[i][j] > todo[i][j]){
                    sb.append("<td color=\"").append("Done").append("\"");
                    sb.append(" title=\"Done = ").append(done[i][j]).append("\"");
                    sb.append("></td>\n");
                }
                else if(undone[i][j] > done[i][j] && undone[i][j] > todo[i][j]){
                    sb.append("<td color=\"").append("Undone").append("\"");
                    sb.append(" title=\"Undone = ").append(undone[i][j]).append("\"");
                    sb.append("></td>\n");
                }
                else {
                    sb.append("<td color=\"").append("Todo").append("\"");
                    sb.append(" title=\"Todo = ").append(todo[i][j]).append("\"");
                    sb.append("></td>\n");
                }
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }
}
