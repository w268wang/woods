package rest.woodray;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rayservice.Message;
import rayservice.RayService;
import rayservice.RayStatus;
import rayservice.User;
import rayservice.WoodException;
import rayutil.RayEntry;

/**
 * RayAPI is the class for all rest apis.
 * The idea here is that:
 *     For sign up:
 *         sign up -> email check to activate
 *     For sign in:
 *         sign in (hash as return, front end should save that as a cookie value)
 *     For communication:
 *         query with hash as header
 *     
 * @author david
 *
 */
@Path("/ray")
public class RayAPI {
    private static final Logger LOG = LoggerFactory.getLogger(RayAPI.class);
    private static final String LOGNAME = "rest.woodray.RayAPI ";
    private static final String WEB_ADDRESS = "http://wjwang.me";
    private static final String SPLIT = "|";
    
    //***************************************************
    //***************************************************
    private static List<String> androidNotification = new ArrayList<String>();

    @GET
    @Path("/notification")
    public Response receiveNotification(@QueryParam("content") String content) {
        androidNotification.add(content);
        LOG.debug("receiveNotification-receive " + content);
        return Response.status(Status.OK).build();
    }
    
    
    @GET
    @Path("/getnotification")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveNotification(@QueryParam("ps") String ps) {
        try {
            List<String> temp = androidNotification;
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
            mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
            String json = mapper.writeValueAsString(temp);
            androidNotification = new ArrayList<String>();
            LOG.debug("retrieveNotification-send " + json);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    //***************************************************
    //***************************************************s
    
    
    
    
    
    
    
    
    
    
    
    
    
    @GET
    @Path("/getmessage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMess(@QueryParam("sender") String sender,
                            @HeaderParam("hash") String hash) {
        LOG.debug(LOGNAME + "getMess(" + sender + ", password)");
        //try {
            
            Map<String, List<Message>> messages = new HashMap<String, List<Message>>();//RayService.getMessage(sender);
            ArrayList<Message> li = new ArrayList<Message>();
            li.add(new Message("abc", "abd", "qew"));
            messages.put("qwe", li);
            
            if (messages != null && messages.size() > 0) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.enable(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
                    mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
                    String json = mapper.writeValueAsString(messages);
                    LOG.debug(json);
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
//        } catch (WoodException we) {
//            if (we.getContent() == RayStatus.INACT) {
//                return Response.status(Status.BAD_REQUEST).entity("inactived").build();
//            } else if (we.getContent() == RayStatus.UNMATCHED) {
//                return Response.status(Status.BAD_REQUEST).entity("unmatched").build();
//            } else {
//                return Response.status(Status.BAD_REQUEST).entity("unknown").build();
//            }
//        }
    }

    @GET
    @Path("/getunreadmessage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnreadMess(@QueryParam("sender") String sender,
                                  @HeaderParam("hash") String hash) {
        LOG.debug(LOGNAME + "getUnreadMess(" + sender + ", password)");
        try {
            Map<String, List<Message>> messages =
                    RayService.getMessage(sender);
            if (messages != null && messages.size() > 0) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(messages);
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                } catch (Exception e) {
                    if (e instanceof JsonGenerationException) {}
                    else if (e instanceof JsonMappingException) {}
                    else if (e instanceof IOException) {}
                    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return Response.status(Status.NO_CONTENT).build();
            }
        } catch (WoodException we) {
            if (we.getContent() == RayStatus.INACT) {
                return Response.status(Status.BAD_REQUEST).entity("inactived").build();
            } else if (we.getContent() == RayStatus.UNMATCHED) {
                return Response.status(Status.BAD_REQUEST).entity("unmatched").build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("unknown").build();
            }
        }
    }

    @GET
    @Path("/getmessage/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMess(@QueryParam("sender") String sender,
                            @QueryParam("receiver") String receiver,
                            @HeaderParam("hash") String hash,
                            @PathParam("limit") String limitStr) {
        LOG.debug(LOGNAME + "getMess(" + sender + ", password, " +
                            limitStr + ")");
        try {
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
                    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return Response.status(Status.NO_CONTENT).build();
            }
        } catch (NumberFormatException nfe) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (WoodException we) {
            if (we.getContent() == RayStatus.INACT) {
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("inactived").build();
            } else if (we.getContent() == RayStatus.UNMATCHED) {
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("unmatched").build();
            } else {
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("unknown").build();
            }
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
    public Response insertMessage(Message m,
                                  @HeaderParam("sender") String sender,
                                  @HeaderParam("hash") String hash) {
        LOG.debug(LOGNAME + "insertMessage(" + m + ")");
        try {
            String findStatus = RayService.findMessage(m);
            if (findStatus == RayStatus.FIND.getValue()) {
                return Response.ok("Message duplicated", MediaType.TEXT_PLAIN).build();
            } else if (findStatus == RayStatus.UNFIND.getValue()) {
                String insetStatus = RayService.insertMessage(m);
                if (insetStatus == RayStatus.OK.getValue()) {
                    return Response.ok("ok", MediaType.TEXT_PLAIN).build();
                } else {
                    return Response.ok(insetStatus, MediaType.TEXT_PLAIN).build();
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
    public Response signUp(@FormParam("email") String email,
                           @FormParam("password") String password) {
        try {
            LOG.debug(LOGNAME + "signUp(" + email + ", password)");
            RayStatus status = RayService.signup(new User(email, password));
            LOG.debug(LOGNAME + "signUp complete.");
            if (status == RayStatus.OK) {
                return Response.status(Status.OK).entity("200").build();
            } else {
                return Response.status(Status.OK).entity("400" + SPLIT + "duplicated").build();
            }
        } catch (SQLException sqle) {
            LOG.error(sqle.getSQLState() + " " + sqle.getMessage());
            return Response.status(Status.OK).entity("500" + SPLIT + "sql").build();
        } catch (Exception e) {
            LOG.error(RayStatus.ERROR.getValue());
            return Response.status(Status.OK).entity("500" + SPLIT + "unknow").build();
        }
    }

    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response signIn(@FormParam("email") String email,
                           @FormParam("password") String password) {
        LOG.debug(LOGNAME + "signIn(" + email + ", password)");
        RayStatus status = RayService.signin(new User(email, password));
        LOG.debug(status.getValue());
        if (status == RayStatus.UNFIND || status == RayStatus.INACT ||
                status == RayStatus.UNMATCHED) {
            return Response.status(Status.OK).entity("400" + SPLIT + status.getValue()).build();
        } else if (status == RayStatus.OK) {
            LOG.debug(LOGNAME + "signIn complete.");
            return Response.status(Status.OK)
                    .entity("200" + SPLIT + RaySecurityService.getMD5(email)).build();
        } else {
            return Response.status(Status.OK).entity("500" + SPLIT + status.getValue()).build();
        }
    }

    @GET
    @Path("/activate/{hash}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activate(@PathParam("hash") String hash) {
        LOG.debug(LOGNAME + "activate(" + hash + ")");
        String response = RayService.activate(hash);
        if (response == RayStatus.UNFIND.getValue()) {
            LOG.debug(LOGNAME + "activate hash not found.");
            return Response.status(Status.NO_CONTENT).build();
        } else if (response == RayStatus.OK.getValue()) {
            LOG.debug(LOGNAME + "activate complete.");
            return redirect("http://wjwang.me/login.html");
        } else {
            LOG.debug(LOGNAME + "activate unknown error.");
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(response).build();
        }
    }

    @GET
    @Path("/hashcheck")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hashCheck(@QueryParam("email") String email,
                              @HeaderParam("hash") String hash) {
        LOG.debug(LOGNAME + "getMess(" + email + ", hash)");
        if (RaySecurityService.getMD5(email).compareTo(hash) == 0) {
            return Response.status(Status.OK).build();
        } else {
            return Response.status(Status.NOT_ACCEPTABLE).entity("unknown").build();
        }
    }

    
    @GET
    @Path("/testdb")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response testDB() {
        LOG.debug(LOGNAME + "Testing db");
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
