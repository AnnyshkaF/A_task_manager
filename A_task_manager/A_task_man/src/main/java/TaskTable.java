
import io.TaskIO;
import model.Task;
import model.TaskBase;

import java.io.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;

public class TaskTable extends HttpServlet {
    TaskBase taskBase = new TaskBase();

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        taskBase.clear();
        response.setContentType("text/html");
        String uri = request.getRequestURI();
        PrintWriter out = response.getWriter();
        if(uri.equals("/A_task_man/TaskTable/delete")) {
            //String res = "/TaskDeleting/" + request.getParameter("hash");
            //response.sendRedirect(res);
            request.getRequestDispatcher("/TaskDeleting").forward(request, response);
            //response.sendRedirect("/A_task_man/TaskDeleting");
            return;
            //out.println("<h1>COME</h1>");
        }

        out.println("<html><head>");
        //out.println("<link rel=\"stylesheet\" href=\"style2.css\"/>");
        String name = "";
        out.println("</head><body>");
        HttpSession session = request.getSession(false);

        if (session != null) {
            request.getRequestDispatcher("auth_links.html").include(request, response);
            name = (String) session.getAttribute("name");
            out.println("Hello, " + name);
        } else {
            request.getRequestDispatcher("links.html").include(request, response);
            request.getRequestDispatcher("login.html").include(request, response);
            out.println("Login first");
            out.println("</body></html>");
            out.close();
            return;
        }
        try {
            new TaskIO().loadTasksFromFile("tasks.xml", taskBase);
        } catch (Exception e) {
            out.println("Error reading from file");
            out.println("</body></html>");
            out.close();
            return;
        }
        out.println(getList(name));
        out.println("</body></html>");
    }

    public String getList(String name) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> groups = taskBase.getGroups();
        sb.append("<form method=\"GET\" action=\"/A_task_man/TaskTable/delete\">\n");
        sb.append("<ol class=\"list\" id = \"tasks\">\n");
        for (int j = 0; j < groups.size(); j++) {
            ArrayList<Task> groupTasks = taskBase.getTasks(name, groups.get(j));
            if (groupTasks.size() != 0) {
                sb.append("<li class = \"");
                sb.append("Group");
                sb.append("\" id = \"");
                sb.append(groups.get(j));
                sb.append("\" onclick=\"open_close(event)\">");
                sb.append(groups.get(j));
                sb.append("<br>");
                for (int k = 0; k < groupTasks.size(); k++) {
                    if (!groupTasks.get(k).getCondition()) {
                        sb.append("\n<input type=\"checkbox\"");
                        sb.append(" name=\"").append(groups.get(j)).append("_checkbox").append("\"");
                        sb.append(" value=\"").append(groupTasks.get(k).getHash()).append("\">");
                        sb.append("<a href=\"InfoServlet/").append("info?hash=").append(groupTasks.get(k).getHash()).append("\"");
                        sb.append(" name=\"").append(groups.get(j)).append("_reference").append("\"");
                        sb.append(" expired=\"").append(groupTasks.get(k).isExpired()).append("\">");
                        sb.append(groupTasks.get(k).getName());
                        sb.append("</a>");
                        sb.append("<br>");
                    }
                }
                sb.append("</li>\n");
            }
        }
        sb.append("</ol>");
        sb.append("<input type=\"submit\" value=\"delete\">\n");
        sb.append("<script async src=\"script.js\"></script>\n");
        sb.append("</form>");
        taskBase.clear();
        return sb.toString();
    }
}

