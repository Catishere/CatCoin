package controllers;

import blockchain.Block;
import blockchain.BlockChain;
import utils.HttpUtils;
import utils.UserUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class MineController {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpUtils hu;

    public MineController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.hu = new HttpUtils(request, response);
    }

    public void processPost() {
        String[] uriPath = request.getPathInfo().split("/");
        String username = request.getParameter("username");
        String blockId = request.getParameter("blockId");
        String proof = request.getParameter("proof");
        UserUtils userUtils = new UserUtils();

        switch (uriPath[1])
        {
            case "mine":
                userUtils.addUser(username);
                hu.respond("logged");
                break;
            case "mined":
                hu.respond(BlockChain.getInstance()
                        .mineBlock(blockId,  Long.parseLong(proof)));
                break;
        }
    }

    public void processGet() {
        String action = request.getParameter("action");
        BlockChain bc = BlockChain.getInstance();

        if (action != null) {
            switch (action) {
                case "block":
                    Block block = bc.getBlockQueue().peekFirst();
                    if (block != null)
                        hu.respond(bc.serializeBlock(block));
                    else
                        hu.dispatch("/noblocks.html");
                    break;
            }
        } else {
            hu.dispatch("/mine.html");
        }
    }
}
