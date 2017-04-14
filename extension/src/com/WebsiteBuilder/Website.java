package com.WebsiteBuilder;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Website {
    protected String folder_path;
    protected JSONObject connection_infos;

    public Website(String folder_path) {
        this.folder_path = folder_path;
        JSONParser parser = new JSONParser();
        try {
            System.out.println("hellllloooooooo");
            connection_infos = (JSONObject) parser.parse(new FileReader(folder_path + "/" + "connect.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJSON() {
        return connection_infos;
    }
}
