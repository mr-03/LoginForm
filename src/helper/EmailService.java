package helper;


import dao.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileReader;
import java.util.*;

public class EmailService {

    public static String code = "";

    public static User currentUser = null;


    public static void setUser(User user) {
        currentUser = user;
    }

    public static List<String> getGmailData() {
        List<String> userData = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("src/json/gmaildata.json"));
            JSONObject jsonObject = (JSONObject) obj;
            userData.add((String) jsonObject.get("email"));
            userData.add((String) jsonObject.get("password"));

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Email/Password not set correctly (src/json/gmaildata.json)");
        }
        return userData;
    }


    public static void sendVerificationCode(String email) {
        Random random = new Random();
        code = String.valueOf(random.nextInt(899999)+100000);
        sendEmail(email, "Verification Code", "Your code is: "+code);

    }

    public static void sendEmail(String to, String header, String messagetxt) {

        List<String> userData = getGmailData();
        String username = userData.get(0);
        String password = userData.get(1);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(header);
            message.setText(messagetxt);

            Transport.send(message);

        }

        catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
