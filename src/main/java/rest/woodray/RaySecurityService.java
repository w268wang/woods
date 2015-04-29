package rest.woodray;

import rayservice.Security;

public class RaySecurityService {
    static String getMD5(String input) {
        return Security.getMD5(input);
    }
}
