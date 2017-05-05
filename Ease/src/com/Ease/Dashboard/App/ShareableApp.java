package com.Ease.Dashboard.App;

import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;

/**
 * Created by thomas on 03/05/2017.
 */
public interface ShareableApp {
    public SharedApp share(ServletManager sm);
    public void modifyShareable(ServletManager sm, JSONObject editJson, SharedApp sharedApp);
    public void deleteShareable(ServletManager sm, SharedApp sharedApp);
}
