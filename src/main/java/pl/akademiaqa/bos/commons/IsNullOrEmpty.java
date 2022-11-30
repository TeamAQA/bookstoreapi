package pl.akademiaqa.bos.commons;

public class IsNullOrEmpty {

    public static boolean isNullOrEmpty(Object value) {
        if (value == null) {
            return true;
        } else {
            return String.valueOf(value).isBlank() || String.valueOf(value) == null;
        }
    }
}
