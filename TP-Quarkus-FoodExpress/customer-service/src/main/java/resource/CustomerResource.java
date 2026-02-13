package resource;

import entity.Customer;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.CustomerService;

import java.util.List;

@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    CustomerService customerService;

    @POST
    public Response create(@Valid Customer customer) {
        Customer created = customerService.create(customer);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<Customer> list() {
        return customerService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return customerService.findById(id)
                .map(c -> Response.ok(c).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid Customer customer) {
        Customer updated = customerService.update(id, customer);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = customerService.delete(id);
        return deleted ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/exists")
    public boolean exists(@PathParam("id") Long id) {
        return Customer.findByIdOptional(id).isPresent();
    }

    @GET
    @Path("/search")
    public List<Customer> searchByCity(@QueryParam("city") String city) {
        if (city == null || city.isBlank()) {
            return List.of();
        }
        return Customer.list("city", city);
    }
}