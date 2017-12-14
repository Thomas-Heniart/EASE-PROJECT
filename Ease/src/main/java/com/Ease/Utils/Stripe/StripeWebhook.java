package com.Ease.Utils.Stripe;

import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.model.Event;
import com.stripe.net.APIResource;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/stripe/webhook")
public class StripeWebhook extends HttpServlet {

    private final static String webhook_key = "whsec_6gtoGZC12pTa3uHt9ZRK8MGhrwgz5Eh8";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Request reach");
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String header_key = request.getHeader("Stripe-Signature");
            System.out.println(header_key);
            System.out.println(sm.getBody());
            //Event event = Webhook.constructEvent(sm.getBody(), header_key, webhook_key);
            Event event = APIResource.GSON.fromJson(sm.getBody(), Event.class);
            System.out.println(event.toJson());
            sm.setSuccess("Ok");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Request reach");
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
