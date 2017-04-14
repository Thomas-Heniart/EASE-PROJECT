package com.Listeners;
/*
*   Class-name: ContextListener
*   Author: debruy_p
*   Description: Va initialiser le contexte au lancement du serveur et va detruire celui-ci à la fermeture du serveur.
*   
*   Documentation-link: [The link here]
*/

import com.ExtensionManager.ExtensionFolderManager;
import com.Servlet.Exception.ServletErrorException;
import com.User.UserManager;
import com.WebsiteBuilder.WebsiteBuilderManager;
import com.WebsiteBuilder.WebsiteManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evt) {
        System.out.println("Context loading...");
        ServletContext context = evt.getServletContext();

        System.out.print("Loading userManager...");
        UserManager userManager = new UserManager();
        context.setAttribute("userManager", userManager);
        System.out.println("Success");

        try {
            System.out.print("Loading extension files...");
            ExtensionFolderManager extensionFolderManager = new ExtensionFolderManager("/home/debruy_p/projects/EASE-PROJECT/extension/extension/ease");
            context.setAttribute("extensionFolderManager", extensionFolderManager);
            System.out.println("Success");

            System.out.print("Loading websites...");
            WebsiteManager websiteManager = new WebsiteManager("/home/debruy_p/projects/EASE-PROJECT/extension/websites", "/home/debruy_p/projects/EASE-PROJECT/extension/oldWebsites");
            context.setAttribute("websiteManager", websiteManager);
            System.out.println("Success");

            System.out.print("Loading websiteBuilder...");
            context.setAttribute("websiteBuilderManager", new WebsiteBuilderManager(websiteManager));
            System.out.println("Success");


        } catch (ServletErrorException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent evt) {
        System.out.print("Destroying context...");

        System.out.println("Success");
    }
}
