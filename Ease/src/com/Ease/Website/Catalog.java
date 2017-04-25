package com.Ease.Website;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.Website.Sso;
import com.Ease.Website.Website;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 24/04/2017.
 */
public class Catalog {
    protected List<Sso> ssoList;
    protected Map<Integer, Sso> ssoIdMap;
    protected List<Website> websites;
    protected Map<Integer, Website> websiteIdMap;
    protected List<Tag> tags;
    protected Map<Integer, Tag> tagIdMap;
    protected Map<Tag, List<Website>> tagWebsiteMap;
    protected Map<Website, List<Tag>> websiteTagMap;
    protected Map<Sso, List<Website>> ssoWebsiteMap;

    public Catalog() {
        this.ssoList = new LinkedList<Sso>();
        this.ssoIdMap = new HashMap<Integer, Sso>();
        this.websites = new LinkedList<Website>();
        this.websiteIdMap = new HashMap<Integer, Website>();
        this.tags = new LinkedList<Tag>();
        this.tagIdMap = new HashMap<Integer, Tag>();
        this.tagWebsiteMap = new HashMap<Tag, List<Website>>();
        this.websiteTagMap = new HashMap<Website, List<Tag>>();
        this.ssoWebsiteMap = new HashMap<Sso, List<Website>>();
    }

    public void populate() throws GeneralException {
        HibernateQuery query = new HibernateQuery();
        query.queryString("SELECT w FROM Website w INNER JOIN w.websiteAttributes wa ORDER BY wa.isNew, w.ratio");
        this.websites = query.list();
        for (Website website : this.websites) {
            this.websiteIdMap.put(website.getDb_id(), website);
            System.out.println("InformationList size: " + website.getWebsiteInformationList().size());
        }

        query.queryString("SELECT s FROM Sso s");
        this.ssoList = query.list();
        for (Sso sso : this.ssoList)
            this.ssoIdMap.put(sso.getDb_id(), sso);
        query.queryString("SELECT t FROM Tag t ORDER BY t.priority");
        this.tags = query.list();
        for (Tag tag : this.tags)
            this.tagIdMap.put(tag.getDb_id(), tag);
        query.querySQLString("SELECT tag_id, website_id FROM tagsAndSitesMap");
        List<Object[]> tagAndSiteList = query.list();
        for (Object[] tagAndSite : tagAndSiteList) {
            Integer tag_id = (Integer) tagAndSite[0];
            Integer website_id = (Integer) tagAndSite[1];
            this.addTagAndSiteWithIds(tag_id, website_id);
        }
        query.commit();
        for (Website website : this.websites) {
            Sso sso = website.getSso();
            if (sso == null)
                continue;
            List<Website> websiteList = this.ssoWebsiteMap.get(sso);
            if (websiteList == null) {
                websiteList = new LinkedList<Website>();
                this.ssoWebsiteMap.put(sso, websiteList);
            }
            websiteList.add(website);
        }
        System.out.println("Catalog websites size: " + this.websites.size());
        System.out.println("Catalog tags size: " + this.tags.size());
        System.out.println("Catalog ssoList size: " + this.ssoList.size());
    }

    public void addTagAndSiteWithIds(Integer tag_id, Integer website_id) throws GeneralException {
        Tag tag = this.getTag(tag_id);
        Website website = this.getWebsite(website_id);
        this.addTagAndSite(tag, website);
    }

    private void addTagAndSite(Tag tag, Website website) {
        List<Website> websiteList = this.tagWebsiteMap.get(tag);
        if (websiteList == null) {
            websiteList = new LinkedList<Website>();
            this.tagWebsiteMap.put(tag, websiteList);
        }
        List<Tag> tagList = this.websiteTagMap.get(website);
        if (tagList == null) {
            tagList = new LinkedList<Tag>();
            this.websiteTagMap.put(website, tagList);
        }
        websiteList.add(website);
        tagList.add(tag);
    }

    public List<Sso> getSsoList() {
        return ssoList;
    }

    public List<Website> getWebsites() {
        return websites;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Sso getSso(Integer sso_id) throws GeneralException {
        Sso sso = this.ssoIdMap.get(sso_id);
        if (sso == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This sso does not exist.");
        return sso;
    }

    public Website getWebsite(Integer website_id) throws GeneralException {
        Website website = this.websiteIdMap.get(website_id);
        if (website == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This website does not exist.");
        return website;
    }

    public Tag getTag(Integer tag_id) throws GeneralException {
        Tag tag = this.tagIdMap.get(tag_id);
        if (tag == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This tag does not exist.");
        return tag;
    }

    private List<Website> getWebsitesForTag(Tag tag) throws GeneralException {
        List<Website> websiteList = this.tagWebsiteMap.get(tag);
        if (websiteList == null)
            throw new GeneralException(ServletManager.Code.ClientError, "No mapping for this tag");
        return websiteList;
    }

    public List<Website> getWebsitesForTagId(Integer tag_id) throws GeneralException {
        Tag tag = this.getTag(tag_id);
        return this.getWebsitesForTag(tag);
    }

    private List<Tag> getTagsForWebsite(Website website) throws GeneralException {
        List<Tag> tagList = this.websiteTagMap.get(website);
        if (tagList == null)
            throw new GeneralException(ServletManager.Code.ClientError, "No mapping for this website");
        return tagList;
    }

    public List<Tag> getTagsForWebsiteId(Integer website_id) throws GeneralException {
        Website website = this.getWebsite(website_id);
        return this.getTagsForWebsite(website);
    }
}
