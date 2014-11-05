package rayservice;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class User {
    private String username;
    private String password;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private String nickname;
    private String tag;
    private long lastTime;

    @SuppressWarnings("unused")
    private User() {
        this.username = "";
        this.password = "";
        this.gender = "";
        this.email = "";
        this.phone = "";
        this.address = "";
        this.nickname = "";
        this.tag = "";
        this.lastTime = System.currentTimeMillis();
    }
    
    
    @JsonCreator
    public User(@JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("gender") String gender,
                @JsonProperty("email") String email,
                @JsonProperty("phone") String phone,
                @JsonProperty("address") String address,
                @JsonProperty("nickname") String nickname,
                @JsonProperty("tag") String tag,
                @JsonProperty("lastTime") long lastTime) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.nickname = nickname;
        this.tag = tag;
        this.lastTime = lastTime;
    }

    public User(String username, String password) {
        this(username, password, "", "", "", "", "", "",
                System.currentTimeMillis());
    }

    public String toString() {
        return this.hashCode() + "";
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
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
}
