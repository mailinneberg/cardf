package com.example.cardformlib.utils;


public class CardUtils
{

    public static boolean isLuhnValid(String cardNumber)
    {
        final String reversed = new StringBuffer(cardNumber).reverse().toString();
        final int len = reversed.length();
        int oddSum = 0;
        int evenSum = 0;
        for (int i = 0; i < len; i++)
        {
            final char c = reversed.charAt(i);
            if (!Character.isDigit(c))
            {
                throw new IllegalArgumentException(String.format("Not a digit: '%s'", c));
            }
            final int digit = Character.digit(c, 10);
            if (i % 2 == 0)
            {
                oddSum += digit;
            }
            else
            {
                evenSum += digit / 5 + (2 * digit) % 10;
            }
        }
        return (oddSum + evenSum) % 10 == 0;
    }

    public static boolean isIsracardValid(String cardNumber)
    {
        int sum = 0;
        int len = cardNumber.length();

        // If the number is less than 9 digits
        if (len == 8)
        {
            cardNumber = "0" + cardNumber;
            len++;
        }

        for (int i = 0; i < len; i++)
        {
            final char c = cardNumber.charAt(i);
            if (!Character.isDigit(c))
            {
                throw new IllegalArgumentException(String.format("Not a digit: '%s'", c));
            }

            final int digit = Character.digit(c, 10);
            sum = sum + (digit * (9 - i));
        }
        return sum % 11 == 0;
    }

}
