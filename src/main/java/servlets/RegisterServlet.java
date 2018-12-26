package servlets;

import io.*;
import model.TaskFields;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.TreeMap;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String name = request.getParameter(TaskFields.NAME);
        String password = request.getParameter(TaskFields.PASSWORD);
        String hash = ReaderWriter.calculateHash(name, password);
        PrintWriter out = response.getWriter();
        out.println("<html><body>");

        TreeMap<String, String> map = new TreeMap<String, String>();
        new ReaderWriter().read("users.txt", map);
        if (map.get(name) == null) {
            map.put(name, hash);
            request.getRequestDispatcher("auth_links.html").include(request, response);
            out.print("Welcome, " + name);
            HttpSession session = request.getSession();
            session.setAttribute("name", name);
            new ReaderWriter().write("users.txt", map);
        } else {
            request.getRequestDispatcher("links.html").include(request, response);
            out.print("Sorry, user already exists.");
            request.getRequestDispatcher("login.html").include(request, response);
        }
        out.println("</html></body>");
        out.close();
    }
}
