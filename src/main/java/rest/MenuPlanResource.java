package rest;

import com.google.gson.Gson;
import dto.MenuPlanDTO;
import dto.MenuPlansDTO;
import dto.RecipeDTO;
import errorhandling.NotFoundException;
import facades.MenuPlanFacade;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

@Path("plan")
public class MenuPlanResource {

    @Context
    SecurityContext securityContext;

    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);

    private final static Gson GSON = new Gson();
    private static MenuPlanFacade facade = MenuPlanFacade.getMenuPlanFacade(EMF);

    public MenuPlanResource() {
    }

    @GET
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserMenuPlans() {
        String username = securityContext.getUserPrincipal().getName();
        MenuPlansDTO result = facade.getMenuPlans(username);
        return GSON.toJson(result);
    }

    @GET
    @RolesAllowed("admin")
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllMenuPlans() {
        MenuPlansDTO result = facade.getAllMenuPlans();
        return GSON.toJson(result);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"user", "admin"})
    public void deleteMenuPlan(@PathParam("id") int id) {
        facade.DeleteMenuPlan(id);
    }

    @POST
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public String addNewMenuPlan(String input) {
        MenuPlanDTO newMenuPlan = GSON.fromJson(input, MenuPlanDTO.class);
        String username = securityContext.getUserPrincipal().getName();
        MenuPlanDTO result = facade.newMenuPlan(username, newMenuPlan.getMenuName());
        return GSON.toJson(result);
    }

    @PUT
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addNewDayPlan(String input, @QueryParam("servings") int servings,
            @QueryParam("menuPlanID") int id) throws NotFoundException {
        RecipeDTO recipe = GSON.fromJson(input, RecipeDTO.class);
        String username = securityContext.getUserPrincipal().getName();
        MenuPlanDTO result = facade.addDayPlan(id, servings, recipe);
        return GSON.toJson(result);
    }

}
