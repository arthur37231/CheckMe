package comp5216.sydney.edu.au.checkme.utils;

public class Constants {
    public static class CountryCode {
        static final String australia = "+61";
        static final String china = "+86";

        public static String getFullPhoneNumber(String phoneNumber) {
            if(phoneNumber.length() == 11) {
                return china + " " + phoneNumber;
            }
            return australia + " " + phoneNumber;
        }
    }
    public static final String NOTHING = "NaN";
    public static final String HIGH_RISK_DATA_COLLECTION = "high_risk_areas";
    public static final String HIGH_RISK_DATA_DATE_FORMAT = "yyyyMMdd";
}
