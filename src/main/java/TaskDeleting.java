
import io.TaskIO;
import model.TaskBase;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TaskDeleting extends HttpServlet {
    TaskBase taskBase = new TaskBase();

    public void init(ServletConfig config) {
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        taskBase.clear();

        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();
        PrintWriter out = response.getWriter();

        try {
            new TaskIO().loadTasksFromFile("tasks.xml", taskBase);
        } catch (Exception e) {
            out.println("<html>\n<body>\n");
            request.getRequestDispatcher("auth_links.html").include(request, response);
            out.println("<h2>Error loading from file</h2>");
            out.println("</body>\n</html>");
        }
      
        ArrayList<String> hash = new ArrayList<>();
        ArrayList<String> groups = taskBase.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            String curName = groups.get(i) + "_checkbox";
             if(request.getParameter(curName) != null) {
                hash.add(request.getParameter(curName));
             }
        }
        for (int i = 0; i < hash.size(); i++) {
            for (int j = 0; j < taskBase.getTaskBase().size(); j++) {
                if (taskBase.getTaskBase().get(j).getHash().equals(hash.get(i))) {
                    taskBase.deleteTask(taskBase.getTaskBase().get(j));
                }
            }
        }

        try {
            new TaskIO().saveTasksToFile("tasks.xml", taskBase);
        } catch (Exception e) {
            out.println("<html>\n<body>\n");
            request.getRequestDispatcher("auth_links.html").include(request, response);
            out.println("<h2>Error saving to file</h2>");
            out.println("</body>\n</html>");
        }
        response.sendRedirect("/A_task_man/TaskTable");
    }
}
