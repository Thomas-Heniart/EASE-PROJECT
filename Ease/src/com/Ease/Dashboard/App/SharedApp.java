package com.Ease.Dashboard.App;

import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;

/**
 * Created by thomas on 03/05/2017.
 */
public interface SharedApp {
    public void modifyShared(ServletManager sm, JSONObject editJson);
    public ShareableApp getHolder();
    public void deleteShared(ServletManager sm);
}
