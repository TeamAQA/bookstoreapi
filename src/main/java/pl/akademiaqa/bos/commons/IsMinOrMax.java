package pl.akademiaqa.bos.commons;

public class IsMinOrMax {

    public static boolean isBelowMinOrAboveMax(String value) {
        if (value.length() < 3 || value.length() > 128) {
            return true;
        }
        return false;
    }
}
