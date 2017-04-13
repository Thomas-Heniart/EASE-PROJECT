package com.ExtensionManager.Servlet;/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import com.ExtensionManager.ExtensionFolderManager;
import com.ExtensionManager.ExtensionManager;
import com.Servlet.Servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ExtensionRefresh")
public class ExtensionRefresh extends Servlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.needToBeConnected();
        this.needToBeAdmin();

        ExtensionFolderManager folders = (ExtensionFolderManager) request.getServletContext().getAttribute("extensionFolderManager");
        ExtensionManager.refresh(folders);

        response.getWriter().print("Extension's sources refreshed.");
    }
}