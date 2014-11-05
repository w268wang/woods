package rayservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mysql.jdbc.Driver;

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

    static Connection getConnection() {
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

    static List<Message> getMessage(String sender, String receiver, int limit,
            boolean unreadOnly) throws SQLException {
        List<Message> result = new ArrayList<Message>();
        String query = (unreadOnly)? "SELECT * FROM " + CHAT_HISTORY + " WHERE"
                + " sender=\"" + sender + "\" AND receiver=\"" + receiver + "\" AND tag"
                + " NOT LIKE '% read%'" + " ORDER BY posttime desc;"
        : "SELECT * FROM " + CHAT_HISTORY + " WHERE sender=\"" + sender + "\" AND"
                + " receiver=\"" + receiver + "\" ORDER BY posttime desc;";
        Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (limit < 1) {
            while (rs.next()) {
                String content = rs.getString("message");
                int timeZone = rs.getInt("time_zone");
                long time = rs.getTimestamp("posttime").getTime();
                result.add(new Message(sender, receiver, content, timeZone, time));
            }
        } else {
            int acc = 0;
            while (rs.next() && acc < limit) {
                String content = rs.getString("message");
                int timeZone = rs.getInt("time_zone");
                long time = rs.getTimestamp("posttime").getTime();
                result.add(new Message(sender, receiver, content, timeZone, time));
                acc++;
            }
        }
        finalizor();
        return result;
    }

    static void insertUser(User u) throws SQLException {
        /*String query = "INSERT INTO " + CHAT_HISTORY + "(sender,receiver,"
                + "posttime,message,time_zone,tag) VALUES (\"" + sender + "\","
                + " \"" + receiver + "\", '" + getSqlFormatTime(time) + "', \""
                + content + "\", " + timeZone + ", \"unread\");";
        Statement stmt = getConnection().createStatement();
        stmt.executeUpdate(query);
        finalizor();*/
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

    private static void finalizor() throws SQLException {
        if (con != null) {
            con.close();
            con = null;
        }
    }
}