package com.Ease.API.JAX;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Importation.ImportedAccount;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/importedAccounts")
@Produces({MediaType.APPLICATION_JSON})
public class ImportedAccountResource {

    private HibernateQuery hibernateQuery = new HibernateQuery();

    @GET
    @Path("/{id}")
    public Response getImportedAccount(@PathParam("id") Long id) throws HttpServletException {
        ImportedAccount importedAccount = (ImportedAccount) hibernateQuery.get(ImportedAccount.class, id);
        if (importedAccount == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such imported account");
        this.hibernateQuery.commit();
        return Response.status(Response.Status.OK).entity(importedAccount).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response putImportedAccount(ImportedAccount importedAccount) throws HttpServletException {
        ImportedAccount importedAccount1 = (ImportedAccount) this.hibernateQuery.get(ImportedAccount.class, importedAccount.getId());
        if (importedAccount1 == null)
            importedAccount1 = importedAccount;
        this.hibernateQuery.saveOrUpdateObject(importedAccount1);
        this.hibernateQuery.commit();
        return Response.status(Response.Status.OK).entity(importedAccount1).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response postImportedAccount(ImportedAccount importedAccount) throws HttpServletException {
        this.hibernateQuery.saveOrUpdateObject(importedAccount);
        this.hibernateQuery.commit();
        return Response.status(Response.Status.OK).entity(importedAccount).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteImportedAccount(@PathParam("id") Long id) throws HttpServletException {
        ImportedAccount importedAccount = (ImportedAccount) this.hibernateQuery.get(ImportedAccount.class, id);
        if (importedAccount == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such imported account");
        this.hibernateQuery.deleteObject(importedAccount);
        this.hibernateQuery.commit();
        return Response.status(Response.Status.OK).build();
    }
}
