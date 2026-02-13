package resource;

import entity.Dish;
import entity.Restaurant;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.RestaurantService;

import java.util.List;

@Path("/api/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantResource {

    @Inject
    RestaurantService restaurantService;

    @POST
    public Response create(@Valid Restaurant restaurant) {
        Restaurant created = restaurantService.create(restaurant);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<Restaurant> list() {
        return restaurantService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return restaurantService.findById(id)
                .map(r -> Response.ok(r).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid Restaurant restaurant) {
        Restaurant updated = restaurantService.update(id, restaurant);
        if (updated == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = restaurantService.delete(id);
        return deleted ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/dishes")
    public Response addDish(@PathParam("id") Long restaurantId, @Valid Dish dish) {
        Dish created = restaurantService.addDish(restaurantId, dish);
        if (created == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{id}/dishes/{dishId}")
    public Response removeDish(@PathParam("id") Long restaurantId,
                               @PathParam("dishId") Long dishId) {
        boolean removed = restaurantService.removeDish(restaurantId, dishId);
        return removed ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/dishes/{dishId}/exists")
    public boolean dishExists(@PathParam("id") Long restaurantId,
                              @PathParam("dishId") Long dishId) {
        return restaurantService.dishExists(restaurantId, dishId);
    }

    @GET
    @Path("/search")
    public List<Restaurant> search(@QueryParam("cuisine") String cuisine) {
        return restaurantService.searchByCuisine(cuisine);
    }

    @GET
    @Path("/{id}/dishes")
    public Response listDishes(@PathParam("id") Long restaurantId) {
        return restaurantService.findById(restaurantId)
                .map(r -> Response.ok(r.dishes).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/{id}/menu")
    @Produces(MediaType.APPLICATION_JSON)
    public Response detailedMenu(@PathParam("id") Long id) {
        return restaurantService.findById(id)
                .map(r -> Response.ok(r).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/{id}/menu")
    @Produces(MediaType.TEXT_PLAIN)
    public String simpleMenu(@PathParam("id") Long id) {
        return restaurantService.formatMenu(id);
    }
}