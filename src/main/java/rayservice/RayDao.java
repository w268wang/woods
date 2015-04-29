package rayservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RayDao {
    static final boolean DEBUG = false;
    static final String MYSQL_USERNAME
        = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
    static final String MYSQL_PASSWORD
        = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
    static final String MYSQL_DATABASE_HOST
        = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
    static final String MYSQL_DATABASE_PORT
        = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
    static final String MYSQL_DATABASE_NAME = "wood";
    static final String MYSQL_DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    static final String CHAT_HISTORY = "chat_history";
    static final String ACCOUNTS = "accounts";
    
    private static Connection con = null;

    static boolean findMessage(Message m) throws SQLException {
        String sender = m.getSender();
        String receiver = m.getReceiver();
        long time = m.getTime();
        String content = m.getContent();
        String query = "SELECT * FROM " + CHAT_HISTORY + " WHERE sender=\""
                + sender + "\" AND receiver=\"" + receiver + "\" AND"
                + " posttime='" + getSqlFormatTime(time) + "' AND"
                + " message=\"" + content + "\";";
        Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        boolean result = rs.next();
        finalizor();
        return result;
    }

    static void insertMessage(Message m) throws SQLException {
        String sender = m.getSender();
        String receiver = m.getReceiver();
        long time = m.getTime();
        String content = m.getContent();
        int timeZone = m.getTimeZone();
        String query = "INSERT INTO " + CHAT_HISTORY + "(sender,receiver,"
                + "posttime,message,time_zone,tag) VALUES (\"" + sender + "\","
                + " \"" + receiver + "\", '" + getSqlFormatTime(time) + "', \""
                + content + "\", " + timeZone + ", \"unread\");";
        Statement stmt = getConnection().createStatement();
        stmt.executeUpdate(query);
        finalizor();
    }

    static Map<String, List<Message>> getMessage(String sender, int limit,
            boolean unreadOnly) throws SQLException, WoodException {
        User user = getUser(sender, false);
        if (!user.isActivated()) {
            throw new WoodException(RayStatus.INACT, "getMessage");
        } //else if (user.getPassword() != password) {
//            throw new WoodException(RayStatus.UNMATCHED, "getMessage");
//        }
        Map<String, List<Message>> result =
                new HashMap<String, List<Message>>();
        String query = (unreadOnly)? "SELECT * FROM " + CHAT_HISTORY + " WHERE"
                + " sender=? AND tag NOT LIKE '% read%'" + " ORDER BY posttime"
                + " desc;"
        : "SELECT * FROM " + CHAT_HISTORY + " WHERE sender=? ORDER BY posttime"
                + " desc;";
        PreparedStatement pstmt = getConnection()
                .prepareStatement(query);
        pstmt.setString(1, sender);
        ResultSet rs = pstmt.executeQuery(query);
        while (rs.next()) {
            String receiver = rs.getString("receiver");
            String content = rs.getString("message");
            int timeZone = rs.getInt("time_zone");
            long time = rs.getTimestamp("posttime").getTime();
            Message message =
                    new Message(sender, receiver, content, timeZone, time);
            if (result.containsKey(receiver)) {
                if (limit > 0 && result.get(receiver).size() >= limit) {
                    continue;
                }
                result.get(receiver).add(message);
            } else {
                List<Message> messageList = new ArrayList<Message>();
                messageList.add(message);
                result.put(receiver, messageList);
            }
        }
        finalizor();
        return result;
    }

    static String insertUser(User u) throws SQLException {
        String email = u.getEmail();
        String hash = Security.getMD5(Security.getusername(email));
        String password = u.getPassword();
        password = (password.length() >= 30)? password : Security.getMD5(password);
        String gender = u.getGender();
        String phone = u.getPhone();
        String address = u.getAddress();
        String tag = u.getTag();
        String query = "INSERT INTO " + ACCOUNTS + "(username,email,"
                + "password,gender,phone,address,tag) VALUES (?,?,?,?,?,?,?);";
        PreparedStatement pstmt = getConnection()
                .prepareStatement(query);
        pstmt.setString(1, hash);
        pstmt.setString(2, email);
        pstmt.setString(3, password);
        pstmt.setString(4, gender);
        pstmt.setString(5, phone);
        pstmt.setString(6, address);
        pstmt.setString(7, tag);
        pstmt.executeUpdate();
        finalizor();
        return hash;
    }

    static User getUser(String str, boolean useUserName) throws SQLException {
        User result = null;
        String query = (useUserName)? "SELECT * FROM " + ACCOUNTS + " WHERE username=?;" :
            "SELECT * FROM " + ACCOUNTS + " WHERE email=?;";
        PreparedStatement pstmt = getConnection()
                .prepareStatement(query);
        pstmt.setString(1, str);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String email = rs.getNString("email");
            String password = rs.getNString("password");
            String gender = rs.getNString("gender");
            String phone = rs.getNString("phone");
            String address = rs.getNString("address");
            int activated = rs.getInt("activated");
            String tag = rs.getNString("tag");
            long lastTime = rs.getTimestamp("lasttime").getTime();
            result = new User(email, password, gender, phone, address, activated, tag, lastTime);
        }
        finalizor();
        return result;
    }

    static void activateUser(String str, boolean useUserName) throws SQLException {
        String query = (useUserName)? "UPDATE " + ACCOUNTS + " SET activated=1 WHERE username=?;" :
            "UPDATE " + ACCOUNTS + " SET activated=1 WHERE email=?;";
        PreparedStatement pstmt = getConnection()
                .prepareStatement(query);
        pstmt.setString(1, str);
        pstmt.executeUpdate();
        finalizor();
    }

    static String testDB() {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                String url = (DEBUG)? "jdbc:mysql://127.0.0.1:3306/"
                        + MYSQL_DATABASE_NAME :
                "jdbc:mysql://" + MYSQL_DATABASE_HOST + ":"
                        + MYSQL_DATABASE_PORT + "/" + MYSQL_DATABASE_NAME;
                Connection tempCon = (DEBUG)? DriverManager
                        .getConnection(url, "admindsi7Gw4", "HlDdAmBtJ1RC") :
                        DriverManager.getConnection(url, MYSQL_USERNAME, MYSQL_PASSWORD);
                tempCon.close();
                return "test ok";
            }
            return "con ok";
        } catch (SQLException se) {
            return se.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private static String getSqlFormatTime(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)
                + "-" + c.get(Calendar.DAY_OF_MONTH) + " "
                + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
                + ":" + c.get(Calendar.SECOND);
    }

    private static Connection getConnection() {
        if (con == null) {
            try {
                Class.forName(MYSQL_DATABASE_DRIVER);
                String url = (DEBUG)? "jdbc:mysql://127.0.0.1:3306/"
                        + MYSQL_DATABASE_NAME :
                "jdbc:mysql://" + MYSQL_DATABASE_HOST + ":"
                        + MYSQL_DATABASE_PORT + "/" + MYSQL_DATABASE_NAME;
                con = (DEBUG)? DriverManager
                        .getConnection(url, "admindsi7Gw4", "HlDdAmBtJ1RC") :
                        DriverManager.getConnection(url, MYSQL_USERNAME, MYSQL_PASSWORD);
            } catch (SQLException se) {
                se.printStackTrace();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
                con = null;
            }
        }
        return con;
    }

    private static void finalizor() throws SQLException {
        if (con != null) {
            con.close();
            con = null;
        }
    }
}