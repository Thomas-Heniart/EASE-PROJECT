package com.Ease.API.RestEasy.Resource;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Onboarding.OnboardingRoom;
import com.Ease.Utils.HttpServletException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/onboarding_room")
public class OnboardingRoomResource {

    @GET
    @Produces("application/json")
    @Path("/{id}")
    public OnboardingRoom get(@PathParam("id") Long id) {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            OnboardingRoom onboardingRoom = (OnboardingRoom) hibernateQuery.get(OnboardingRoom.class, id);
            hibernateQuery.commit();
            return onboardingRoom;
        } catch (HttpServletException e) {
            e.printStackTrace();
            hibernateQuery.rollback();
            return null;
        }
    }
}
