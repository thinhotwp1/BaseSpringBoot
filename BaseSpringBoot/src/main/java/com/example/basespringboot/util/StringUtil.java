package com.example.basespringboot.util;


import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static boolean isBlank(String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < strLen; ++i) {
            if (!(Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static String getEmpIfNull(String source) {
        return Objects.nonNull(source) ? source : "";
    }

    public static boolean isContainSpecialCharacter(String inputStr) {

        if (inputStr == null) {
            return false;
        }
        inputStr = inputStr.replace(" ", "");
        if ("".equals(inputStr)) {
            return false;
        }

        Pattern p = Pattern.compile("[~!@#$%^&*()-=+_?><,|]");
        Matcher m = p.matcher(inputStr);

        return m.find();
    }

    public static String replaceAllSpaces(String str, String replaceBy) {
        if (isNullOrEmpty(str)) {
            return "";
        }
        return str.replaceAll("\\s+", replaceBy);
    }


    public static boolean isNullOrEmpty(String str) {
        return ((str == null) || (str.trim().isEmpty()));
    }

    public static boolean isNotNullOrEmpty(String str) {
        return (!(isNullOrEmpty(str)));
    }

    public static boolean isNotBlank(String str) {
        return (!(isBlank(str)));
    }

}