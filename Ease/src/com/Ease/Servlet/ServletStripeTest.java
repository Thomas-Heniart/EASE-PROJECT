package com.Ease.Servlet;

import com.Ease.Context.Variables;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.net.RequestOptions;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas on 02/06/2017.
 */
@WebServlet("/ServletStripeTest")
public class ServletStripeTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("stripeToken");
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            // Set your secret key: remember to change this to your live secret key in production
            // See your keys here: https://dashboard.stripe.com/account/apikeys
            Stripe.apiKey = "sk_test_4Qqw6xcv7VQDmXBS5CZ9rz5T";

            // Token is created using Stripe.js or Checkout!
            // Get the payment token submitted by the form:

            System.out.println("stripeToken: " + token);
            // Charge the user's card:
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("amount", 1000);
            params.put("currency", "eur");
            params.put("description", "Example charge");
            params.put("source", token);


            Charge charge = Charge.create(params);

            sm.setSuccess("Done");
        } catch (Exception e) {
            e.printStackTrace();
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
