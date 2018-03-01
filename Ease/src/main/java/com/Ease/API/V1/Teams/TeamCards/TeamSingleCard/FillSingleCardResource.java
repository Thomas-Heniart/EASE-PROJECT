package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.API.RestEasy.Resource.RestResource;
import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Utils.HttpServletException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/fill")
public class FillSingleCardResource extends RestResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{card_id}/{uuid}")
    public String get(@PathParam("card_id") Integer card_id, @PathParam("uuid") String uuid) {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.queryString("SELECT TeamSingleCard t WHERE t.db_id = :id AND t.magicLink = :magicLink");
            hibernateQuery.setParameter("id", card_id);
            hibernateQuery.setParameter("magicLink", Variables.URL_PATH + "fill/" + card_id + "/" + uuid);
            TeamCard teamCard = (TeamCard) hibernateQuery.getSingleResult();
            if (teamCard == null) {
                hibernateQuery.queryString("SELECT TeamSoftwareSingleCard t WHERE t.db_id = :id AND t.magicLink = :magicLink");
                hibernateQuery.setParameter("id", card_id);
                hibernateQuery.setParameter("magicLink", Variables.URL_PATH + "fill/" + card_id + "/" + uuid);
                teamCard = (TeamCard) hibernateQuery.getSingleResult();
            }
            hibernateQuery.commit();
            if (teamCard == null)
                return "";
            else
                return teamCard.getJson().toString();
        } catch (HttpServletException e) {
            e.printStackTrace();
            hibernateQuery.rollback();
            return "";
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{card_id}/{uuid}")
    public String post(@PathParam("card_id") Integer card_id, @PathParam("uuid") String uuid) {
        return "";
    }
}
