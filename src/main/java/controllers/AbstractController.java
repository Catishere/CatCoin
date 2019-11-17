package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

abstract class AbstractController {

    HttpServletRequest request;
    HttpServletResponse response;

    <T> void respond(T response) {
        try {
            this.response.getWriter().print(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
