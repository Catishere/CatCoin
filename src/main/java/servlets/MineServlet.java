package servlets;

import controllers.MineController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MineServlet")
public class MineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        MineController controller = new MineController(request, response);
        controller.processPost();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        MineController controller = new MineController(request, response);
        controller.processGet();
    }
}
