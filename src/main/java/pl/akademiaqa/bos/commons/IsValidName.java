package pl.akademiaqa.bos.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsValidName {

    private static final String TEXT_PATTERN = "^[\\p{L}][\\p{L}\\s-.]*[\\p{L}]$";
    private static final Pattern pattern = Pattern.compile(TEXT_PATTERN);

    public static boolean isValidName(String text) {
        if (text == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
