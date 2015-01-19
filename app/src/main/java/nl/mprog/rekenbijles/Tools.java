package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 12/01/15.
 * A class for tools which I can call in a static way from different classes.
 */
public class Tools {
    // a method which takes an n-digit number and breaks it into an array of length n, each
    // entry containing a digit
    static int[] numberBreaker(int number, int expectedLength)
    {
        double numberLength = 0.0;
        double numberDouble = (double) number;
        int difference = 0;
        int[] digits;
        while (numberDouble >= 1)
        {
            numberDouble = numberDouble / 10;
            numberLength += 1;
        }
        numberDouble = (double) number;
        if (expectedLength == 0)
        {
            digits = new int[(int) numberLength];
        } else
        {
            digits = new int[expectedLength];
            difference = expectedLength - (int) numberLength;
            for (int i = 0; i < difference; i++)
            {
                digits[i] = 0;
            }
        }

        int position = 0;
        for (int i = 0; i < numberLength; i++)
        {
            int digit = 0;
            double order = Math.pow(10.0, numberLength - 1 - i);
            while (numberDouble >= order)
            {
                numberDouble = numberDouble - order;
                digit += 1;
            }
            digits[position + difference] = digit;
            position += 1;
        }
        return digits;
    }
}
