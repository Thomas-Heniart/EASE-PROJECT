package com.WebsiteBuilder;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import com.ExtensionManager.Extension;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebsiteBuilderManager {
    protected Map<String, WebsiteBuilder> builders;
    protected WebsiteManager websiteManager;

    public WebsiteBuilderManager(WebsiteManager websiteManager) {
        builders = new HashMap<String, WebsiteBuilder>();
        this.websiteManager = websiteManager;
    }

    private void unconnectedDom(String singleId, JSONObject page) {
        builders.put(singleId, new WebsiteBuilder(page));
    }

    private void connectedDom(String singleId, JSONObject page) {
        if (builders.get(singleId) != null)
            builders.get(singleId).addConnectedDom(page);
    }

    private void loggedOutDom(String singleId, JSONObject page) {
        if (builders.get(singleId) != null)  {
            builders.get(singleId).addLoggedOutDom(page);
            builders.get(singleId).build(websiteManager);
        }
    }



    public void msgHandler(Extension extension, JSONObject action, String order, JSONObject data) {
        System.out.println(order);
        switch (order) {
            case "unconnectedDom": {
                unconnectedDom(extension.getSingleId(), (JSONObject) data.get("dom"));
            }
            break;
            case "connectedDom": {
                connectedDom(extension.getSingleId(), (JSONObject) data.get("dom"));
            }
            break;
            case "loggedOutDom": {
                loggedOutDom(extension.getSingleId(), (JSONObject) data.get("dom"));
            }
            break;
        }
    }
}
