package com.WebsiteBuilder;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import com.ExtensionManager.Extension;
import com.ExtensionManager.ExtensionFolder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

public class WebsiteManager {

    protected String folder_path;
    protected String old_folder_path;
    protected Map<String, Website> websites;
    private String[] parts;


    public WebsiteManager(String folder_path, String old_folder_path) {
        this.folder_path = folder_path;
        this.old_folder_path = old_folder_path;
        this.websites = new HashMap<String, Website>();
        File directory = new File(folder_path);
        File[] subdirs = directory.listFiles();
        if (subdirs != null) {
            for (File file : subdirs) {
                if (file.isDirectory()) {
                    Website website = new Website(folder_path + "/" + file.getName());
                    websites.put(file.getName(), website);
                }
            }
        }
    }

    public void createWebsite(String websiteName, String domain, JSONObject connectInfos) {
        File dir = new File(folder_path + "/" + websiteName);
        dir.mkdir();

        try {
            File file = new File(folder_path + "/" + websiteName + "/connect.json");
            file.createNewFile();
            FileWriter connectFile = new FileWriter(file);
            connectFile.write(connectInfos.toString());
            connectFile.flush();
            connectFile.close();
            URL logoUrl = new URL("https://logo.clearbit.com/" + domain + "?size=128&format=png");
            ReadableByteChannel rbc = Channels.newChannel(logoUrl.openStream());
            FileOutputStream fos = new FileOutputStream(folder_path + "/" + websiteName + "/logo.png");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectInfosGenerated(String domain, JSONObject connectInfos) {
        String[] parts = domain.split("\\.");
        System.out.println(parts.length);
        String websiteName = parts[parts.length - 2];
        Website website;
        if ((website = websites.get(websiteName)) == null) {
            this.createWebsite(websiteName, domain, connectInfos);
            website = new Website(folder_path + "/" + websiteName);
            websites.put(websiteName, website);
        } else {
            //website.appendConnectInfos(connectInfos);
        }
    }

    public JSONObject msgHandler(Extension extension, JSONObject action, String order, JSONObject data) {
        System.out.println(order);
        JSONObject resp = new JSONObject();
        resp.put("context","action");
        resp.put("tabSingleId", action.get("tabSingleId"));
        resp.put("windowSingleId", action.get("windowSingleId"));
        resp.put("actionName", action.get("actionName"));
        switch (order) {
            case "getConnectJSON": {
                Website website = websites.get(data.get("websiteName"));
                JSONObject respData = new JSONObject();
                if (website == null) {
                    respData.put("json", this.getOldJSON((String) data.get("websiteName")));
                    resp.put("order", "connectJSONRetro");
                } else {
                    respData.put("json", website.getJSON());
                    resp.put("order", "connectJSON");
                }
                respData.put("login", "easetester1@gmail.com");
                respData.put("password", "Dunkerque.10");
                respData.put("websiteName", data.get("websiteName"));

                resp.put("data", respData);
            }
            break;
        }
        return resp;
    }

    public JSONObject getOldJSON(String websiteName) {
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(new FileReader(old_folder_path + "/" + websiteName + "/" + "connect.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
