package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Onboarding.OnboardingRoom;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/api/v1/admin/OnboardingRooms")
public class ServletOnboardingRoom extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            String name = sm.getStringParam("name", true, false);
            if (!Regex.isValidRoomName(name))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            JSONArray website_ids = sm.getArrayParam("website_ids", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Set<Website> websiteSet = ConcurrentHashMap.newKeySet();
            for (int i = 0; i < website_ids.length(); i++) {
                Integer website_id = website_ids.getInt(i);
                Website website = catalog.getPublicWebsiteWithId(website_id, sm.getHibernateQuery(), new HashSet<>());
                websiteSet.add(website);
            }
            OnboardingRoom onboardingRoom = new OnboardingRoom(name, websiteSet);
            sm.saveOrUpdate(onboardingRoom);
            sm.setSuccess(onboardingRoom.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            JSONArray res = new JSONArray();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT r FROM OnboardingRoom r");
            List<OnboardingRoom> onboardingRooms = hibernateQuery.list();
            onboardingRooms.forEach(onboardingRoom -> res.put(onboardingRoom.getJson()));
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Long id = sm.getLongParam("id", true, false);
            OnboardingRoom onboardingRoom = (OnboardingRoom) sm.getHibernateQuery().get(OnboardingRoom.class, id);
            if (onboardingRoom == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This onboarding room does not exist");
            String name = sm.getStringParam("name", true, false);
            if (!Regex.isValidRoomName(name))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid room name");
            JSONArray website_ids = sm.getArrayParam("website_ids", true, false);
            Set<Website> websiteSet = ConcurrentHashMap.newKeySet();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            for (int i = 0; i < website_ids.length(); i++) {
                Website website = catalog.getPublicWebsiteWithId(website_ids.getInt(i), sm.getHibernateQuery(), new HashSet<>());
                websiteSet.add(website);
            }
            onboardingRoom.setName(name);
            onboardingRoom.setWebsiteSet(websiteSet);
            sm.saveOrUpdate(onboardingRoom);
            sm.setSuccess(onboardingRoom.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Long id = sm.getLongParam("id", true, false);
            OnboardingRoom onboardingRoom = (OnboardingRoom) sm.getHibernateQuery().get(OnboardingRoom.class, id);
            if (onboardingRoom == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such room");
            sm.deleteObject(onboardingRoom);
            sm.setSuccess("Onboarding room deleted");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }
}
