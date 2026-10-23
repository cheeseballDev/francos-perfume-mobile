package com.example.francosperfumemobile.helpers;

import java.util.regex.Pattern;

public class PasswordRules {
    private static final Pattern RULE =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#.\\-?!@$%^&*]).{12,}$");
    public static String check(String password, String confirm) {
        if (password == null || !RULE.matcher(password).matches())
            return "Use at least 12 characters with upper and lower case letters, a number and one of # . - ? ! @ $ % ^ & *";
        if (!password.equals(confirm)) return "Passwords do not match";
        return null;
    }
}
