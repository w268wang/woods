package rayservice;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class RestClient {
    public static void main(String[] args) {
        try {
            ClientConfig config = new DefaultClientConfig();
            config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client = Client.create(config);
            WebResource service = client.resource(getBaseURI());
//            ClientResponse response = service.accept(MediaType.APPLICATION_JSON)
//                    .type(MediaType.APPLICATION_JSON).put(ClientResponse.class, new Message("a", "b", "message", 5, 1000));
//            System.out.println(response.getStatus() + " " + response.getStatusInfo());
//            System.out.println(response.getEntity(String.class));
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(new Message("a", "b", "message"));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  private static URI getBaseURI() {
      //return UriBuilder.fromUri("http://localhost:8080/wood/rest/ray/insertmessage").build();
	  return UriBuilder.fromUri("http://wood-wjwang.rhcloud.com/rest/ray/insertmessage").build();
  }

} 