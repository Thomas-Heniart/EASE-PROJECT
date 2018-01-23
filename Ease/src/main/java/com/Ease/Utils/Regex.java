package com.Ease.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_EASE_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
    private static final Pattern VALID_COLOR_REGEX =
            Pattern.compile("^#[0-9a-fA-F]{6}$");
    private static final Pattern URL_REGEX =
            Pattern.compile("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})(/?)");
    private static final Pattern VALID_PHONE_NUMBER =
            Pattern.compile("^(\\+|[0-9])(?:[0-9] ?){5,13}[0-9]$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_USERNAME = Pattern.compile("^[a-z0-9._\\-]{3,22}$");
    private static final Pattern VALID_SIMPLE_STRING = Pattern.compile("^[a-zA-Z0-9]{4,20}$");
    private static final Pattern VALID_ROOM_NAME = Pattern.compile("^[a-z0-9_\\-]{1,21}$");
    private static final Pattern SIMPLE_URL = Pattern.compile("^(https?://)");
    private static final Pattern VALID_NAME = Pattern.compile("^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+");

    public static boolean isEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean isPassword(String passStr) {
        Matcher matcher = VALID_EASE_PASSWORD_REGEX.matcher(passStr);
        return matcher.find();
    }

    public static boolean isColor(String colorStr) {
        Matcher matcher = VALID_COLOR_REGEX.matcher(colorStr);
        return matcher.find();
    }

    public static boolean isValidLink(String urlStr) {
        Matcher matcher = URL_REGEX.matcher(urlStr);
        return matcher.find();
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        Matcher matcher = VALID_PHONE_NUMBER.matcher(phoneNumber);
        return matcher.find();
    }

    public static boolean isValidUsername(String username) {
        Matcher matcher = VALID_USERNAME.matcher(username);
        return matcher.find();
    }

    public static boolean isValidSimpleString(String s) {
        Matcher matcher = VALID_SIMPLE_STRING.matcher(s);
        return matcher.find();
    }

    public static boolean isValidRoomName(String s) {
        Matcher matcher = VALID_ROOM_NAME.matcher(s);
        return matcher.find();
    }

    public static boolean isSimpleUrl(String url) {
        Matcher matcher = SIMPLE_URL.matcher(url);
        return matcher.find();
    }

    public static boolean isValidName(String name) {
        Matcher matcher = VALID_NAME.matcher(name);
        return matcher.find();
    }
}
