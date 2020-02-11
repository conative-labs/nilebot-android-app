package com.soleeklab.nilebot.utils;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CredentialValidator {

    public static final Pattern PASS_PATTERN = Pattern.compile("^.{6,}$") ;
    public static final Pattern PHONE_PATTERN = Pattern.compile("^.{5,15}$") ;
    public static final Pattern CODE_PATTERN = Pattern.compile("^.{4,10}$") ;
    public static final Pattern EMPTY_PATTERN = Pattern.compile("^(?!\\s*$).+");
    public static final Pattern USER_NAME_PATTERN = Pattern.compile("^.{1,30}$");


    public static boolean isNameValid(final String username) {
        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(username);
        return matcher.find() && !TextUtils.isEmpty(username) && username.length() > 10;
    }

    public static boolean isEmpty(EditText editText){
        return TextUtils.isEmpty(editText.getText().toString());
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


}
