package rayservice;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rest.woodray.RayAPI;

public class MailUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RayAPI.class);
    private static final String LOGNAME = "MailUtil: ";
    private static  String SMTP_HOST = "smtp.gmail.com";  
    private static String FROM_ADDRESS = "wood.wjwang@gmail.com";  
    private static String PASSWORD = "woodwjwang";  
    private static String FROM_NAME = "Weijie Wang";  
  
    public static String sendMail(String subject, String content, String to) {  
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        //props.put("mail.smtp.host", SMTP_HOST);
//        props.put("mail.smtp.socketFactory.port", "465");
        //props.put("mail.smtp.socketFactory.port", "587");
        //props.put("mail.smtp.socketFactory.class",
        //        "javax.net.ssl.SSLSocketFactory");
        //props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", "465");
        //props.put("mail.smtp.port", "587");
 
        Session session = Session.getDefaultInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_ADDRESS, PASSWORD);
                }
        });
 
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_ADDRESS, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);
 
            Transport.send(message);
            LOG.debug(LOGNAME + "message sent to " + to);
            return RayStatus.OK.getValue();
        } catch (MessagingException | UnsupportedEncodingException e) {
            RuntimeException re = new RuntimeException(e);
            LOG.debug(LOGNAME + RayStatus.ERROR.getValue() + re.fillInStackTrace()
                    .getMessage());
            return RayStatus.ERROR.getValue() + re.fillInStackTrace()
                    .getMessage();
        }
    }  
    public static void main(String[] args) {
        sendMail("Test", "Test", "w268wang@gmail.com");
    }

}
