package controllers;

import blockchain.Block;
import blockchain.BlockChain;
import utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

public class BlockController {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpUtils hu;

    public BlockController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.hu = new HttpUtils(request, response);
    }

    public void processPost() {

    }

    public void processGet() {
        String[] uriPath = request.getPathInfo().split("/");
        BlockChain bc = BlockChain.getInstance();
        List<Block> blockList = new LinkedList<>();

        if (uriPath.length < 2)
        {
            hu.dispatch("/error.html");
            return;
        }

        switch (uriPath[1])
        {
            case "queue":
                if (uriPath.length > 2)
                {
                    Block block = bc.findBlockInBlockQueue(uriPath[2]);
                    if (block != null)
                        blockList.add(block);
                    hu.respond(bc.serializeBlockList(uriPath[1], blockList));
                } else
                    hu.respond(bc.serializeBlockQueue());
                break;

            case "chain":
                if (uriPath.length > 2)
                {
                    Block block = bc.findBlockInBlockChain(uriPath[2]);
                    if (block != null)
                        blockList.add(block);
                    hu.respond(bc.serializeBlockList(uriPath[1], blockList));
                } else
                    hu.respond(bc.serializeBlockChain());
                break;
        }
    }
}
