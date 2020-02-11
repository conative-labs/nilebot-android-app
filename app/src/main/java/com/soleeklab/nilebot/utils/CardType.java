package com.soleeklab.nilebot.utils;

public class CardType {


    public static String detectCreditType(String number) {
        if (number.startsWith("4"))
            return "Visa";
        else if (number.startsWith("5"))
            return "MasterCard";
        else if (number.startsWith("6"))
            return "Discover";
        else if (number.startsWith("37"))
            return "American Express";
        else
            return "Unknown type";
    }
}
