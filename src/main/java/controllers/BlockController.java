package controllers;

import utils.Block;
import utils.BlockChain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

public class BlockController extends AbstractController {

    public BlockController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public void processPost() {

    }

    public void processGet() {
        String[] uriPath = request.getPathInfo().split("/");
        BlockChain bc = BlockChain.getInstance();
        List<Block> blockList = new LinkedList<>();
        switch (uriPath[1])
        {
            case "queue":
                if (uriPath.length > 2)
                {
                    Block block = bc.findBlockInBlockQueue(uriPath[2]);
                    if (block != null)
                        blockList.add(block);
                    respond(bc.serializeBlockList(uriPath[1], blockList));
                } else
                    respond(bc.serializeBlockQueue());
                break;

            case "chain":
                if (uriPath.length > 2)
                {
                    Block block = bc.findBlockInBlockChain(uriPath[2]);
                    if (block != null)
                        blockList.add(block);
                    respond(bc.serializeBlockList(uriPath[1], blockList));
                } else
                    respond(bc.serializeBlockChain());
                break;
        }
    }
}
