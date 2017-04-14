package com.ExtensionManager;
/*
*   Class-name: ExtensionFolder
*   Author: debruy_p
*   Description: Is used to read the extension folder to send to extensions.
*   
*   Documentation-link: [The link here]
*/

import com.Servlet.Exception.ServletErrorException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ExtensionFolder {

    static String MANIFEST_FILE = "manifest.json";

    protected JSONObject parsed;
    protected String folder_path;
    protected String version;
    protected String magic;
    protected String background;
    protected String content;
    protected JSONObject actions;
    protected JSONObject files;
    protected JSONObject toSend;

    public ExtensionFolder(String folder_path) throws ServletErrorException {
        this.folder_path = folder_path;
        refresh();
    }

    public void refresh() throws ServletErrorException {
        JSONParser parser = new JSONParser();
        try {
            parsed = (JSONObject) parser.parse(new FileReader(folder_path + "/" + MANIFEST_FILE));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletErrorException("Error while opening extension's manifest.", e);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ServletErrorException("Error while parsing extension's manifest.", e);
        }

        version = (String) parsed.get("version");
        magic = (String) parsed.get("magic");

        try {
            background = readFile( folder_path + "/" + (String) parsed.get("background"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletErrorException("Error while opening extension's background file.", e);
        }

        try {
            content = readFile( folder_path + "/" + (String) parsed.get("content"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletErrorException("Error while opening extension's content file.", e);
        }

        actions = getFiles((JSONObject)parsed.get("actions"));

        files = getFiles((JSONObject)parsed.get("files"));

        toSend = new JSONObject();
        toSend.put("version", version);
        toSend.put("magic", magic);
        toSend.put("background", background);
        toSend.put("content", content);
        toSend.put("actions", actions);
        toSend.put("files", files);
    }

    public JSONObject getJson() {
        return toSend;
    }

    public String getVersion() {return version;};

    public void setVersion(String version) throws ServletErrorException {
        refresh();
        this.version = version;
        parsed.put("version", version);
        save();
    }

    private JSONObject getFiles(JSONObject obj) throws ServletErrorException {
        JSONObject ret = new JSONObject();
        for (Object o : obj.keySet()) {
            String key = (String) o;
            Object child = obj.get(key);
            if (child instanceof JSONObject) {
                ret.put(key, getFiles((JSONObject) child));
            } else if (child instanceof String) {
                try {
                    ret.put(key, readFile(folder_path + "/" + (String) child));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ServletErrorException("Error while reading file '" + folder_path + "/" + (String) child + "' from the extension manifest.", e);
                }
            }
        }
        return ret;
    }

    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    private void save() throws ServletErrorException {
        FileWriter file = null;
        try {
            file = new FileWriter(folder_path + "/" + MANIFEST_FILE);
            file.write(parsed.toString());
            file.flush();
            file.close();
            System.out.println("Manifest changed");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletErrorException("Error while saving extension's manifest.", e);
        }
    }
}