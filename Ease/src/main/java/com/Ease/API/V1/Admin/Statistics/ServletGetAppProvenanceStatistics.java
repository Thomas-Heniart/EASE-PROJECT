package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Metrics.EaseEvent;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/v1/admin/GetAppProvenanceStatistics")
public class ServletGetAppProvenanceStatistics extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery trackingHibernateQuery = sm.getTrackingHibernateQuery();
            JSONObject res = new JSONObject();
            JSONArray labels = new JSONArray();
            JSONArray single = new JSONArray();
            JSONArray enterprise = new JSONArray();
            JSONArray pro = new JSONArray();
            JSONArray perso = new JSONArray();
            JSONArray catalog = new JSONArray();
            JSONArray update = new JSONArray();
            JSONArray importation = new JSONArray();
            JSONArray classic = new JSONArray();
            JSONArray any = new JSONArray();
            JSONArray software = new JSONArray();
            JSONArray bookmark = new JSONArray();
            Calendar start_calendar = Calendar.getInstance();
            Calendar end_calendar = Calendar.getInstance();
            Long start_week_ms = sm.getLongParam("start_week_ms", true, true);
            if (start_week_ms == null)
                start_calendar.add(Calendar.WEEK_OF_YEAR, -4);
            else
                start_calendar.setTimeInMillis(start_week_ms);
            start_calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Long end_week_ms = sm.getLongParam("end_week_ms", true, true);
            if (end_week_ms != null) {
                if (start_week_ms != null && end_week_ms < start_week_ms)
                    throw new HttpServletException(HttpStatus.BadRequest, "End cannot be before start");
                end_calendar.setTimeInMillis(end_week_ms);
            }
            while (start_calendar.get(Calendar.YEAR) < end_calendar.get(Calendar.YEAR)) {
                trackWeek(trackingHibernateQuery, start_calendar, labels, single, enterprise, pro, perso, catalog, update, importation, classic, any, software, bookmark);
                start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            while (start_calendar.get(Calendar.WEEK_OF_YEAR) <= end_calendar.get(Calendar.WEEK_OF_YEAR)) {
                trackWeek(trackingHibernateQuery, start_calendar, labels, single, enterprise, pro, perso, catalog, update, importation, classic, any, software, bookmark);
                start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            res.put("labels", labels);
            res.put("single", single);
            res.put("pro", pro);
            res.put("perso", perso);
            res.put("enterprise", enterprise);
            res.put("catalog", catalog);
            res.put("importation", importation);
            res.put("update", update);
            res.put("classic", classic);
            res.put("any", any);
            res.put("software", software);
            res.put("bookmark", bookmark);
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void trackWeek(HibernateQuery trackingHibernateQuery, Calendar calendar, JSONArray labels, JSONArray single, JSONArray enterprise, JSONArray pro, JSONArray perso, JSONArray catalog, JSONArray updates, JSONArray importation, JSONArray classic, JSONArray any, JSONArray software, JSONArray bookmark) {
        trackingHibernateQuery.queryString("SELECT e FROM EaseEvent e WHERE (e.name LIKE 'CardAdded' OR e.name LIKE 'AppAdded') AND e.year = :year AND e.week_of_year = :week_of_year");
        trackingHibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
        trackingHibernateQuery.setParameter("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR));
        List<EaseEvent> addEvents = trackingHibernateQuery.list();
        addEvents = addEvents.stream().filter(easeEvent -> !easeEvent.getType().isEmpty()).collect(Collectors.toList());
        List<EaseEvent> addAppEvents = addEvents.stream().filter(easeEvent -> easeEvent.getName().equals("AppAdded")).collect(Collectors.toList());
        List<EaseEvent> addCardEvents = addEvents.stream().filter(easeEvent -> easeEvent.getName().equals("CardAdded")).collect(Collectors.toList());
        double total = addEvents.size();
        double proApps = addCardEvents.size();
        if (proApps == 0.) {
            single.put(0);
            enterprise.put(0);
        } else {
            single.put(addCardEvents.stream().filter(EaseEvent::isSingle).count() / proApps * 100);
            enterprise.put(addCardEvents.stream().filter(EaseEvent::isEnterprise).count() / proApps * 100);
        }
        double persoApps = addAppEvents.size();
        if (proApps == 0.)
            pro.put(0);
        else
            pro.put(proApps / (proApps + persoApps) * 100);
        if (persoApps == 0)
            perso.put(0);
        else
            perso.put(persoApps / (proApps + persoApps) * 100);
        if (total == 0.) {
            catalog.put(0);
            updates.put(0);
            importation.put(0);
            classic.put(0);
            any.put(0);
            software.put(0);
            bookmark.put(0);
        } else {
            catalog.put(addEvents.stream().filter(EaseEvent::isFromCatalog).count() / total * 100);
            updates.put(addEvents.stream().filter(EaseEvent::isFromUpdate).count() / total * 100);
            importation.put(addEvents.stream().filter(EaseEvent::isFromImportation).count() / total * 100);
            classic.put(addEvents.stream().filter(EaseEvent::isClassic).count() / total * 100);
            any.put(addEvents.stream().filter(EaseEvent::isAny).count() / total * 100);
            software.put(addEvents.stream().filter(EaseEvent::isSoftware).count() / total * 100);
            bookmark.put(addEvents.stream().filter(EaseEvent::isBookmark).count() / total * 100);
        }
        labels.put("Week " + calendar.get(Calendar.WEEK_OF_YEAR) + ", " + calendar.get(Calendar.YEAR));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    public static void main(String[] args) throws Exception {
        HibernateQuery trackingHibernateQuery = new HibernateQuery("tracking");
        HibernateQuery hibernateQuery = new HibernateQuery();
        trackingHibernateQuery.queryString("SELECT e FROM EaseEvent e WHERE e.name LIKE 'CardAdded'");
        List<EaseEvent> easeEvents = trackingHibernateQuery.list();
        for (EaseEvent easeEvent : easeEvents) {
            JSONObject data = easeEvent.getJsonData();
            int id = data.optInt("id", -1);
            hibernateQuery.queryString("SELECT t FROM TeamCard t WHERE t.id = :id");
            hibernateQuery.setParameter("id", id);
            TeamCard teamCard = (TeamCard) hibernateQuery.getSingleResult();
            if (teamCard == null)
                data.put("sub_type", "classic");
            else
                data.put("sub_type", teamCard.getSubtype());
            easeEvent.setData(data);
            trackingHibernateQuery.saveOrUpdateObject(easeEvent);
        }
        trackingHibernateQuery.commit();
        hibernateQuery.commit();
        return;
    }
}
