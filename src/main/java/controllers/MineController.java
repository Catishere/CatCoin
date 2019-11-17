package controllers;

import utils.Block;
import utils.BlockChain;
import utils.UserUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class MineController extends AbstractController {

    public MineController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public void processPost() {
        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String blockId = request.getParameter("blockId");
        String proof = request.getParameter("proof");
        UserUtil userUtil = new UserUtil();
        switch (action)
        {
            case "mine":
                userUtil.addUser(username);
                respond("logged");
                break;
            case "mined":
                respond(BlockChain.getInstance()
                        .mineBlock(blockId,  Long.parseLong(proof)));
                break;
        }
    }

    public void processGet() {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "block":
                    Block block = new Block("Kurzacskawe");
                    BlockChain.getInstance().addBlock(block);
                    respond("{\"hash\":" + Arrays.toString(block.serialize()) + ",\"id\":\"" + block.getId() + "\"}");
                    break;
            }
        } else {
            RequestDispatcher view = request.getRequestDispatcher("/mine.html");
            try {
                view.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
