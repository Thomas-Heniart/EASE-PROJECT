package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Metrics.ClickOnApp;
import com.Ease.Metrics.EaseEvent;
import com.Ease.Metrics.EaseEventFactory;
import com.Ease.NewDashboard.App;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

@WebServlet("/api/v1/admin/ConvertMetricClickOnApp")
public class ServletConvertMetricClickOnApp extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT c FROM ClickOnApp c");
            List<ClickOnApp> clickOnAppList = hibernateQuery.list();
            Calendar calendar = Calendar.getInstance();
            HibernateQuery trackingHibernateQuery = sm.getTrackingHibernateQuery();
            for (ClickOnApp clickOnApp : clickOnAppList) {
                App app = (App) hibernateQuery.get(App.class, clickOnApp.getApp_id());
                calendar.set(Calendar.YEAR, clickOnApp.getYear());
                calendar.set(Calendar.WEEK_OF_YEAR, clickOnApp.getWeek_of_year());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                for (int i = 0; i < clickOnApp.getDay_seven(); i++) {
                    EaseEvent easeEvent;
                    if (app != null)
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", app);
                    else
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", clickOnApp.getApp_id());
                    easeEvent.setCreation_date(calendar);
                    trackingHibernateQuery.saveOrUpdateObject(easeEvent);
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                for (int i = 0; i < clickOnApp.getDay_one(); i++) {
                    EaseEvent easeEvent;
                    if (app != null)
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", app);
                    else
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", clickOnApp.getApp_id());
                    easeEvent.setCreation_date(calendar);
                    trackingHibernateQuery.saveOrUpdateObject(easeEvent);
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                for (int i = 0; i < clickOnApp.getDay_two(); i++) {
                    EaseEvent easeEvent;
                    if (app != null)
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", app);
                    else
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", clickOnApp.getApp_id());
                    easeEvent.setCreation_date(calendar);
                    trackingHibernateQuery.saveOrUpdateObject(easeEvent);
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                for (int i = 0; i < clickOnApp.getDay_three(); i++) {
                    EaseEvent easeEvent;
                    if (app != null)
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", app);
                    else
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", clickOnApp.getApp_id());
                    easeEvent.setCreation_date(calendar);
                    trackingHibernateQuery.saveOrUpdateObject(easeEvent);
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                for (int i = 0; i < clickOnApp.getDay_four(); i++) {
                    EaseEvent easeEvent;
                    if (app != null)
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", app);
                    else
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", clickOnApp.getApp_id());
                    easeEvent.setCreation_date(calendar);
                    trackingHibernateQuery.saveOrUpdateObject(easeEvent);
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                for (int i = 0; i < clickOnApp.getDay_five(); i++) {
                    EaseEvent easeEvent;
                    if (app != null)
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", app);
                    else
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", clickOnApp.getApp_id());
                    easeEvent.setCreation_date(calendar);
                    trackingHibernateQuery.saveOrUpdateObject(easeEvent);
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                for (int i = 0; i < clickOnApp.getDay_six(); i++) {
                    EaseEvent easeEvent;
                    if (app != null)
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", app);
                    else
                        easeEvent = EaseEventFactory.getInstance().createPasswordUsedEvent(clickOnApp.getUser_id(), "DashboardClick", clickOnApp.getApp_id());
                    easeEvent.setCreation_date(calendar);
                    trackingHibernateQuery.saveOrUpdateObject(easeEvent);
                }
            }
            sm.setSuccess("Success");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
