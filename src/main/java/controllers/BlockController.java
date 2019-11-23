package controllers;

import blockchain.Block;
import blockchain.BlockChain;
import com.google.gson.Gson;
import utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        Gson gson = new Gson();
        Block block = gson.fromJson(request.getParameter("block"), Block.class);
        if (BlockChain.getInstance().addBlock(block))
            hu.respond("Successfully added to blockchain");
        else
            hu.respond("Failed to add to blockchain");
    }

    public void processGet() {
        List<Block> blockList = BlockChain.getInstance().getBlockChain();
        hu.processGetList(blockList);
    }
}
