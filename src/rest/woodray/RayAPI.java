package rest.woodray;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import rayservice.Message;
import rayservice.RayService;
import rayservice.RayStatus;

@Path("/ray")
public class RayAPI {
    static Map<String, String> tokens = new HashMap<String, String>();

    @GET
    @Path("/getmessage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMess(@QueryParam("sender") String sender,                                                                                      
                            @QueryParam("receiver") String receiver) {
        List<Message> messages = RayService.getMessage(sender, receiver);
        if (messages != null && messages.size() > 0) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
                mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
                String json = mapper.writeValueAsString(messages);
                return Response.ok(json, MediaType.APPLICATION_JSON).build();
            } catch (Exception e) {
                if (e instanceof JsonGenerationException) {}
                else if (e instanceof JsonMappingException) {}
                else if (e instanceof IOException) {}
                return Response.status(Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Status.NO_CONTENT).build();
        }
    }

    @GET
    @Path("/getunreadmessage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnreadMess(@QueryParam("sender") String sender,                                                                                      
                                  @QueryParam("receiver") String receiver) {
        List<Message> messages =
                RayService.getUnreadMessage(sender, receiver);
        if (messages != null && messages.size() > 0) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(messages);
                return Response.ok(json, MediaType.APPLICATION_JSON).build();
            } catch (Exception e) {
                if (e instanceof JsonGenerationException) {}
                else if (e instanceof JsonMappingException) {}
                else if (e instanceof IOException) {}
                return Response.status(Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Status.NO_CONTENT).build();
        }
    }

    @GET
    @Path("/getmessage/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMess(@QueryParam("sender") String sender,                                                                                      
                            @QueryParam("receiver") String receiver,
                            @PathParam("limit") String limitStr) {
        int limit = Integer.parseInt(limitStr);
        List<Message> messages =
                RayService.getMessage(sender, receiver, limit);
        if (messages != null && messages.size() > 0) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(messages);
                return Response.ok(json, MediaType.APPLICATION_JSON).build();
            } catch (Exception e) {
                if (e instanceof JsonGenerationException) {}
                else if (e instanceof JsonMappingException) {}
                else if (e instanceof IOException) {}
                return Response.status(Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Status.NO_CONTENT).build();
        }
    }

    /*
     * ClientConfig config = new DefaultClientConfig();
     * config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
     * Client client = Client.create(config);
     * WebResource service = client.resource(getBaseURI());
     * System.out.print(service.accept("application/json")
     *         .type("application/json").put(ClientResponse.class, new Message("a", "b", "message")));
     */
    @PUT
    @Path("/insertmessage")
    @Consumes(MediaType.APPLICATION_JSON) // Both text_plain
    //@Produces(MediaType.APPLICATION_JSON)
    public Response insertMessage(Message m) {
        try {
            String findStatus = RayService.findMessage(m);
            if (findStatus == RayStatus.FIND.getValue()) {
                return Response.ok("Message duplicated", MediaType.TEXT_PLAIN).build();
            } else if (findStatus == RayStatus.UNFIND.getValue()) {
                String insetStatus = RayService.insertMessage(m);
                if (insetStatus == RayStatus.OK.getValue()) {
                    return Response.ok(m.toString(), MediaType.TEXT_PLAIN).build();
                } else {
                    return Response.status(Status.INTERNAL_SERVER_ERROR).entity(insetStatus).build();
                }
            } else {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(findStatus).build();
            }
        } catch (Exception e) {
            if (e instanceof JsonMappingException) {
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("JsonMappingException: "
                        + ((JsonMappingException) e).getMessage()).build();
            } else if (e instanceof JsonParseException) {
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("JsonParseException: "
                        + ((JsonParseException) e).getMessage()).build();
            } else if (e instanceof IOException) {
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("IOException: "
                        + ((IOException) e).getMessage()).build();
            } else {
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("Unknow Exception: "
                        + e.getLocalizedMessage()).build();
            }
        }
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response signUp(@FormParam("username") String username,
                           @FormParam("password") String password) {
        String message = "u: " + username + "p: " + password;
        return Response.status(Status.OK).entity(message).build();
    }

    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response signIn(@FormParam("username") String username,
                           @FormParam("password") String password) {
        String message = "u: " + username + "p: " + password;
        return Response.status(Status.OK).entity(message).build();
    }

    @GET
    @Path("/testdb")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response testDB() {
        String result = RayService.testDB();
        if (result == "ok") {
            return Response.ok("DataBase works fine!", MediaType.TEXT_PLAIN).build();
        } else {
            return Response.ok("DataBase con error...\n" + result, MediaType.TEXT_PLAIN).build();
        }
    }
    
    private Response redirect(String url) {
        URI urlForRedirection = UriBuilder.fromUri(url).build();
        return Response.seeOther(urlForRedirection).build();
    }
}
