package rayservice;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rayutil.RayEntry;
import rest.woodray.RayAPI;

public class RayService {
    private static final Logger LOG = LoggerFactory.getLogger(RayAPI.class);
    private static String WEB_ADDRESS = "wjwang.me/rest/ray";

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

    public static Map<String, List<Message>> getMessage(String sender)
            throws WoodException {
        try {
            return RayDao.getMessage(sender, 0, false);
        } catch (WoodException we) {
            throw we;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<Message> getMessage(String sender,
            String receiver, int limit) throws WoodException {
        try {
            Map<String, List<Message>> data = RayDao.getMessage(sender, limit, false);
            return data.get(receiver);
        } catch (WoodException we) {
            throw we;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Message> getUnreadMessage(String sender,
            String receiver) throws WoodException {
        try {
            Map<String, List<Message>> data = RayDao.getMessage(sender, 0, true);
            return data.get(receiver);
        } catch (WoodException we) {
            throw we;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RayStatus signup(User u) throws SQLException, Exception {
        final String logName = "signup ";
        try {
            User user = RayDao.getUser(u.getEmail(), false);
            if (user != null) {
                return RayStatus.DUPLICATED;
            }
            String hash = RayDao.insertUser(u);
            MailUtil.sendMail("[wood team] Activate your account",
                    "Click on this link to activate your account "
            + WEB_ADDRESS + "/activate/" + hash, u.getEmail());
            return RayStatus.OK;
        } catch (Exception e) {
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw e;
            }
        }
    }

    public static RayStatus signin(User u) {
        String logName = "signin";
        try {
            String password = u.getPassword();
            password = (password.length() >= 30)? password : Security.getMD5(password);
            User user = RayDao.getUser(u.getEmail(), false);
            if (user == null) {
                return RayStatus.UNFIND;
            } else if (!user.isActivated()) {
                return RayStatus.INACT;
            } else if (user.getPassword().compareTo(password) != 0) {
                return RayStatus.UNMATCHED;
            }
            return RayStatus.OK;
        } catch (Exception e) {
            if (e instanceof SQLException) {
                return RayStatus.SQL_FAILURE;
            } else {
                return RayStatus.ERROR;
            }
        }
    }

    public static String activate(String hash) {
        String logName = "activate";
        try {
            if (RayDao.getUser(hash, true) == null) {
                return RayStatus.UNFIND.getValue();
            }
            RayDao.activateUser(hash, true);
            return RayStatus.OK.getValue();
        } catch (Exception e) {
            if (e instanceof SQLException) {
                LOG.error(RayStatus.SQL_FAILURE.getValue() + logName
                        + ((SQLException) e).getSQLState() + " "
                        + ((SQLException) e).getMessage());
                return RayStatus.SQL_FAILURE.getValue() + logName
                        + ((SQLException) e).getSQLState() + " "
                        + ((SQLException) e).getMessage();
            } else {
                LOG.error(RayStatus.ERROR.getValue() + logName + e.getMessage());
                return RayStatus.ERROR.getValue() + logName + e.getMessage();
            }
        }
    }

    public static String testDB() {
        return RayDao.testDB();
    }
}