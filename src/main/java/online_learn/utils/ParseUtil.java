package online_learn.utils;

public class ParseUtil {

    public static boolean intTryParse(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean booleanTryParse(String input) {
        try {
            Boolean.parseBoolean(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
