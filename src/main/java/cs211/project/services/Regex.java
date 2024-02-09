package cs211.project.services;

public class Regex {
    public static boolean checkNumberFormat(String number) {
        String format = "[0-9]+";
        if (number.matches(format)) {
            return true;
        }
        return false;
    }
    public static boolean checkTimeFormat(String time) {
        String format = "^(?:[01]\\d|2[0-3]):[0-5]\\d$";
        if (time.matches(format)) {
            return true;
        }
        return false;
    }
}
