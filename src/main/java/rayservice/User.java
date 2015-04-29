package rayservice;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class User {
    private String email;
    private String password;
    private String gender;
    private String phone;
    private String address;
    private String nickname;
    private String tag;
    private long lastTime;
    final int activated;

    @SuppressWarnings("unused")
    private User() {
        this.email = "";
        this.password = "";
        this.gender = "";
        this.phone = "";
        this.address = "";
        this.nickname = "";
        this.activated = 1;
        this.tag = "";
        this.lastTime = System.currentTimeMillis();
    }
    
    
    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("gender") String gender,
                @JsonProperty("phone") String phone,
                @JsonProperty("address") String address,
                @JsonProperty("nickname") String nickname,
                @JsonProperty("activated") int activated,
                @JsonProperty("tag") String tag,
                @JsonProperty("lastTime") long lastTime) {
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.nickname = nickname;
        this.activated = activated;
        this.tag = tag;
        this.lastTime = lastTime;
    }

    public User(String email, String password) {
        this(email, password, "", "", "", "", 1, "",
                System.currentTimeMillis());
    }
    
    public User(String email, String password,
            String gender, String phone,
            String address, int activated,
            String tag, long lastTime) {
        this(email, password, gender, phone, address, "",activated, tag,
                lastTime);
    }

    public String toString() {
        return this.hashCode() + "";
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getTag() {
        return tag;
    }

    public long getLastTime() {
        return lastTime;
    }
    
    public boolean isActivated() {
        return activated != 0;
    }
}
