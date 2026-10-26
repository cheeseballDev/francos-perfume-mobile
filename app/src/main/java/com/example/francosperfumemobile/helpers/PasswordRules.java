package com.example.francosperfumemobile.helpers;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.francosperfumemobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Password rules shared with the website (src/utils/validationUtils.js getPasswordConditions)
 * and the backend (PasswordPolicy.cs):
 *  - 12+ characters, upper case, lower case, a number, one of # . - ? ! @ $ % ^ &amp; *
 *  - must not contain the first or last name (spaces removed, any case)
 *  - optionally: must differ from the old / temporary password
 */
public final class PasswordRules {

    public static final String SPECIAL_CHARACTERS = "#.-?!@$%^&*";
    public static final int MIN_LENGTH = 12;

    private static final Pattern UPPER = Pattern.compile("[A-Z]");
    private static final Pattern LOWER = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL = Pattern.compile("[#.\\-?!@$%^&*]");

    private static final Pattern RULE =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#.\\-?!@$%^&*]).{12,}$");

    private PasswordRules() {
    }

    /** One line in the checklist. */
    public static final class Condition {
        public final String key;
        @StringRes
        public final int label;
        public final boolean met;

        Condition(String key, @StringRes int label, boolean met) {
            this.key = key;
            this.label = label;
            this.met = met;
        }
    }

    /**
     * @param oldPassword pass the temporary/current password to add "must be different"; null to skip
     */
    public static List<Condition> evaluate(@Nullable String password, @Nullable String firstName,
                                           @Nullable String lastName, @Nullable String oldPassword) {
        String pw = password == null ? "" : password;
        boolean hasValue = !pw.isEmpty();
        String compact = pw.toLowerCase(Locale.ROOT).replaceAll("\\s+", "");

        List<String> names = new ArrayList<>();
        for (String name : new String[]{firstName, lastName}) {
            if (name == null) continue;
            String n = name.toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
            if (n.length() >= 2) names.add(n);
        }

        List<Condition> conditions = new ArrayList<>();
        if (oldPassword != null) {
            conditions.add(new Condition("isNotOld", R.string.pw_rule_not_old, hasValue && !pw.equals(oldPassword)));
        }
        if (!names.isEmpty()) {
            boolean containsName = false;
            for (String n : names) {
                if (compact.contains(n)) {
                    containsName = true;
                    break;
                }
            }
            conditions.add(new Condition("hasNoName", R.string.pw_rule_no_name, hasValue && !containsName));
        }
        conditions.add(new Condition("hasMinLength", R.string.pw_rule_length, pw.length() >= MIN_LENGTH));
        conditions.add(new Condition("hasUpper", R.string.pw_rule_upper, UPPER.matcher(pw).find()));
        conditions.add(new Condition("hasLower", R.string.pw_rule_lower, LOWER.matcher(pw).find()));
        conditions.add(new Condition("hasNumber", R.string.pw_rule_number, DIGIT.matcher(pw).find()));
        conditions.add(new Condition("hasSpecial", R.string.pw_rule_special, SPECIAL.matcher(pw).find()));
        return conditions;
    }

    public static boolean allMet(List<Condition> conditions) {
        for (Condition c : conditions) {
            if (!c.met) return false;
        }
        return true;
    }

    /** Old one-shot check, kept for any caller that still uses it. */
    public static String check(String password, String confirm) {
        if (password == null || !RULE.matcher(password).matches())
            return "Use at least 12 characters with upper and lower case letters, a number and one of # . - ? ! @ $ % ^ & *";
        if (!password.equals(confirm)) return "Passwords do not match";
        return null;
    }
}
