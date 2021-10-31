package comp5216.sydney.edu.au.checkme.utils;

public class CommonUtils {
    public static String phoneNumberToEmail(String phoneNumber) {
        return phoneNumber.substring(5).trim() + "@checkme.com";
    }
}
