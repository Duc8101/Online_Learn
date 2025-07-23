package online_learn.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class UserUtil {

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashPw = digest.digest(password.getBytes());

        StringBuilder builder = new StringBuilder();
        for (byte b : hashPw) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String randomPassword()
    {
        Random random = new Random();
        // password contain both alphabets and numbers
        String format = "abcdefghijklmnopqrstuvwxyz0123456789QWERTYUIOPASDFGHJKLZXCVBNM!#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ ";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++)
        {
            // get random index character
            int index = random.nextInt(format.length());
            builder.append(format.charAt(index));
        }
        return builder.toString();
    }

    public static String bodyEmailForForgotPassword(String password)
    {
        return "<h1>Mật khẩu mới</h1>\n" +
                "<p>Mật khẩu mới là: " + password + "</p>\n" +
                "<p>Không nên chia sẻ mật khẩu của bạn với người khác.</p>";
    }

    public static void sendEmail(String subject, String body, String to) throws MessagingException {
        // Cấu hình SMTP server
        final String username = "fptu.sep490.g33@gmail.com";
        final String password = "nvpzbnsqxbjxoqea";
        final String host = "smtp.gmail.com";
        final int port = 587;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // dùng TLS
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));

        // Tạo session với Authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Tạo email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");

        // Gửi email
        Transport.send(message);
    }
}
