package online_learn.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
