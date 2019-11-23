package controllers;

import blockchain.BlockChain;
import blockchain.Transaction;
import com.google.gson.Gson;
import utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class TransactionController {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpUtils hu;

    public TransactionController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.hu = new HttpUtils(request, response);
    }

    public void processPost() {
        Gson gson = new Gson();
        Transaction transaction = gson.fromJson(request.getParameter("transaction"), Transaction.class);
        BlockChain.getInstance().addTransaction(transaction);
    }

    public void processGet() {
        List<Transaction> pool = BlockChain.getInstance().getTransactionPool();
        hu.processGetList(pool);
    }
}
