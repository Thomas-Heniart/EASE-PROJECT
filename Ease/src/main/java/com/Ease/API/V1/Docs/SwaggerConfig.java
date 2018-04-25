package com.Ease.API.V1.Docs;


import com.fasterxml.jackson.databind.ObjectMapper;
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
        beanConfig.setVersion("v1");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/rest");
        beanConfig.setResourcePackage("com.Ease");
        beanConfig.setScan(true);

        // return swagger json on demand
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(beanConfig.getSwagger());

        response.getWriter().write(jsonInString);
    }

}
