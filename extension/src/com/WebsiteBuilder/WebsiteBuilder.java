package com.WebsiteBuilder;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WebsiteBuilder {
    protected DomDescriber unconnectedDom;
    protected DomDescriber loggedOutDom;

    protected DomDescriber connectedDom;
    protected DomDescriber notConnectedDom;

    protected Map<String, List<String>> unconnectedDiffConnected;
    protected Map<String, List<String>> connectedDiffLoggedOut;


    protected Map<String, List<String>> connectedOnlyAttr;
    protected Map<String, List<String>> unconnectedOnlyAttr;

    protected String homePageUrl;
    protected String domain;

    protected JSONArray loginActions;
    protected JSONArray logoutActions;

    public WebsiteBuilder(JSONObject page) {
        domain = (String) page.get("domain");
        homePageUrl = (String) page.get("url");
        String dom = (String) page.get("dom");
        unconnectedDom = new DomDescriber(dom);

    }

    public void addConnectedDom(JSONObject page) {
        String domain = (String) page.get("domain");
        String dom = (String) page.get("dom");
        loginActions = (JSONArray) page.get("actions");
        connectedDom = new DomDescriber(dom);
    }

    public void addLoggedOutDom(JSONObject page) {
        String domain = (String) page.get("domain");
        String dom = (String) page.get("dom");
        logoutActions = (JSONArray) page.get("actions");
        loggedOutDom = new DomDescriber(dom);
    }

    public void build(WebsiteManager websiteManager) {
        notConnectedDom = unconnectedDom.getEgality(loggedOutDom);

        unconnectedDiffConnected = connectedDom.getAttrsNotIn(unconnectedDom);
        connectedDiffLoggedOut = connectedDom.getAttrsNotIn(loggedOutDom);

        unconnectedOnlyAttr = notConnectedDom.getAttrsNotIn(connectedDom);
        connectedOnlyAttr = this.getCommomAttrs(unconnectedDiffConnected, connectedDiffLoggedOut);

        System.out.println("lalaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaal");

        JSONObject connectInfos = new JSONObject();
        connectInfos.put("homePage", this.homePageUrl);
        connectInfos.put("checkConnected", this.getCheckConnected());
        connectInfos.put("checkUnconnected", this.getCheckUnconnected());
        connectInfos.put("login", this.loginActions);
        connectInfos.put("logout", this.logoutActions);

        websiteManager.connectInfosGenerated(this.domain, connectInfos);
    }

    private Map<String, List<String>> getCommomAttrs(Map<String, List<String>> a, Map<String, List<String>> b) {
        Map<String, List<String>> in = new HashMap<String, List<String>>();
        for (Map.Entry<String, List<String>> entry : a.entrySet()) {
            if (b.get(entry.getKey()) != null) {
                for (String value : entry.getValue()) {
                    if (b.get(entry.getKey()).contains(value)) {
                        in.computeIfAbsent(entry.getKey(), k -> new LinkedList<String>());
                        in.get(entry.getKey()).add(value);
                    }
                }
            }
        }
        return in;
    }

    private String generateCheck(Map<String, List<String>> attrs) {
        String ret = "";
        int cpt = 0;
        for (Map.Entry<String, List<String>> entry : attrs.entrySet()) {
            if (cpt > 0) {
                ret += ",";
            }
            for (String value : entry.getValue()) {
                ret += "[" + entry.getKey() + "='" + value + "']";
            }
            cpt++;
        }
        return ret;
    }

    private String getCheckConnected() {
        return this.generateCheck(connectedOnlyAttr);
    }

    private String getCheckUnconnected() {
        return this.generateCheck(unconnectedOnlyAttr);
    }
}
