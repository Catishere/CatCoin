package utils;

import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HttpUtils {
    private HttpServletResponse response;
    private HttpServletRequest request;

    public HttpUtils(HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        this.request = request;
    }

    public <T> void respond(T message) {
        try {
            response.getWriter().print(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void error(String msg)
    {
        RequestDispatcher view = request.getRequestDispatcher("/mine.html");
        request.setAttribute("error", msg);
        try {
            view.forward(request, response);
        } catch (ServletException | IOException e) {
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

    public <T> void processGetList(List<T> list) {

        String[] uriPath;
        String pathInfo = request.getPathInfo();
        Gson gson = new Gson();

        if (pathInfo == null) {
            respond(gson.toJson(list));
            return;
        }
        uriPath = pathInfo.split("/");

        if (uriPath.length == 0) {
            respond(gson.toJson(list));
        } else if (uriPath.length == 2 && (uriPath[1].matches("\\d+"))) {
            int i = Integer.parseInt(uriPath[1]);
            if (i < list.size())
                respond(gson.toJson(list.get(i)));
        } else if ("count".equals(uriPath[1])) {
            if (uriPath.length == 2) {
                Integer size = list.size();
                respond(gson.toJson(size));
            } else if (uriPath.length == 3  && uriPath[2].matches("\\d+")) {
                int upperBound = Math.min(list.size() - 1, Integer.parseInt(uriPath[2]));
                respond(gson.toJson(list.subList(0, upperBound)));
            }
        }
    }

    public void home() {
        dispatch("/index.html");
    }
}
