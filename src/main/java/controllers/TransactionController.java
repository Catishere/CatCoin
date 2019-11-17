package controllers;

import blockchain.Transaction;
import utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String action = request.getParameter("action");
        String sender = request.getParameter("sender");
        String receiver = request.getParameter("receiver");
        String amount = request.getParameter("amount");
        Transaction transaction = new Transaction(sender, receiver, Long.parseLong(amount));

        switch (action)
        {
            case "transfer":
                hu.respond("logged");
                break;
        }
    }

    public void processGet() {

    }
}
