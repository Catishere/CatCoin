package utils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpUtils {
    private HttpServletResponse response;
    private HttpServletRequest request;

    public HttpUtils(HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        this.request = request;
    }

    public <T> void respond(T message) {
        try {
            response.getWriter().print(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dispatch(String url) {
        RequestDispatcher view = request.getRequestDispatcher("/mine.html");
        try {
            view.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
