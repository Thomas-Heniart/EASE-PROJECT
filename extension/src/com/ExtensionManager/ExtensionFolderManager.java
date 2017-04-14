package com.ExtensionManager;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import com.ExtensionManager.Exception.ExtensionClosedSocketException;
import com.ExtensionManager.Exception.ExtensionMessageNotSendedException;
import com.Servlet.Exception.ServletErrorException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExtensionFolderManager {

    static String DEFAULT_FILE = "default";

    protected String folder_path;
    protected Map<String, ExtensionFolder> folders;
    protected ExtensionFolder defaultFolder;

    public ExtensionFolderManager(String folder_path) throws ServletErrorException {
        folders = new HashMap<String, ExtensionFolder>();
        this.folder_path = folder_path;

        refresh();
    }

    public void refresh() throws ServletErrorException{

        Map<String, ExtensionFolder> newFolders = new HashMap<String, ExtensionFolder>();
        ExtensionFolder newDefaultFolder = null;

        File directory = new File(folder_path);
        File[] subdirs = directory.listFiles();
        for (File file : subdirs) {
            if (file.isDirectory()) {
                ExtensionFolder folder = new ExtensionFolder(folder_path + "/" + file.getName());
                newFolders.put(folder.getVersion(), folder);
            }
        }
        try {
            JSONParser parser = new JSONParser();
            JSONObject parsed = (JSONObject) parser.parse(new FileReader(folder_path + "/" + DEFAULT_FILE));
            String defaultVersion = (String) parsed.get("defaultVersion");
            newDefaultFolder = newFolders.get(defaultVersion);
            if (newDefaultFolder == null) {
                throw new ServletErrorException("Default extension folder not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletErrorException("Can't read 'default' file to define default extension");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ServletErrorException("Can't parse extension 'default' file.", e);
        }
        folders = newFolders;
        defaultFolder = newDefaultFolder;
    }

    public ExtensionFolder getFolder(String version) {
        return folders.get(version);
    }

    public ExtensionFolder getDefaultFolder() {
        return defaultFolder;
    }

    public void sendUpdateVersion(Extension extension) throws ExtensionClosedSocketException, ExtensionMessageNotSendedException {
        JSONObject data = new JSONObject();
        if (extension.getUser() != null) {
            ExtensionFolder folder = this.getFolder(extension.getUser().getExtensionVersion());
            if (folder != null) {
                data.put("version", folder.getVersion());
            } else {
                data.put("version", this.getDefaultFolder().getVersion());
            }
        } else {
            data.put("version", this.getDefaultFolder().getVersion());
        }
        extension.sendMessage("updater", "update", data);
    }
}