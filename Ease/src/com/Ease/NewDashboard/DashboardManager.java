package com.Ease.NewDashboard;

import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App.App;

import java.util.List;

/**
 * Created by thomas on 21/04/2017.
 */
public class DashboardManager {
    protected List<App> apps;
    protected List<Profile> profiles;

    public DashboardManager() {
        HibernateQuery query = new HibernateQuery();
        query.queryString("");
    }
}