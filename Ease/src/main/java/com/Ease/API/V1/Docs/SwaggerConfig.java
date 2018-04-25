package com.Ease.API.V1.Docs;


import com.google.gson.Gson;
import io.swagger.jaxrs.config.BeanConfig;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/api/v1/docs/swagger.json")
public class SwaggerConfig extends HttpServlet {

    static final String HOST = "localhost:8080";
    static final String PATH_BASE = "/api/v1";
    static final String PACKAGE_TO_SCAN = "com.Ease.API";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost(HOST);
        beanConfig.setBasePath(PATH_BASE);
        beanConfig.setResourcePackage(PACKAGE_TO_SCAN);
        beanConfig.setScan(true);
        beanConfig.setPrettyPrint(true);

        JSONObject res = new JSONObject();

        // return swagger json on demand
        Gson gson = new Gson();
        String swaggerJson = gson.toJson(beanConfig.getSwagger());
        response.setContentType("application/json");
        response.getWriter().write(swaggerJson);
    }

}
