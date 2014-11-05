package rayservice;

import java.sql.SQLException;
import java.util.List;

public class RayService {

    public static String findMessage(Message m) {
        final String logName = "findMessage ";
        try {
            if (RayDao.findMessage(m)) {
                return RayStatus.FIND.getValue();
            } else {
                return RayStatus.UNFIND.getValue();
            }
        } catch (Exception e) {
            if (e instanceof SQLException) {
                return RayStatus.SQL_FAILURE.getValue() + logName
                        + ((SQLException) e).getSQLState() + " "
                        + ((SQLException) e).getMessage();
            } else {
                return RayStatus.ERROR.getValue() + logName + e.getMessage();
            }
        }
    }

    public static String insertMessage(Message m) {
        final String logName = "insertMessage ";
        try {
            RayDao.insertMessage(m);
            return RayStatus.OK.getValue();
        } catch (Exception e) {
            if (e instanceof SQLException) {
                return RayStatus.SQL_FAILURE.getValue() + logName
                        + ((SQLException) e).getSQLState() + " "
                        + ((SQLException) e).getMessage();
            } else {
                return RayStatus.ERROR.getValue() + logName + e.getMessage();
            }
        }
    }

    public static List<Message> getMessage(String sender, String receiver) {
        try {
            return RayDao.getMessage(sender, receiver, 0, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<Message> getMessage(String sender, String receiver, int limit) {
        try {
            return RayDao.getMessage(sender, receiver, limit, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<Message> getUnreadMessage(String sender, String receiver) {
        try {
            return RayDao.getMessage(sender, receiver, 0, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String testDB() {
        return RayDao.testDB();
    }
}