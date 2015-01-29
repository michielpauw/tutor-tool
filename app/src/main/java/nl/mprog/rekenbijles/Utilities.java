package nl.mprog.rekenbijles;

/**
 * Created by Michiel Pauw on 12/01/15.
 * A class for tools which I can call in a static way from different classes.
 */
public class Utilities {
    // a method which takes an n-digit number and breaks it into an array of length n, each
    // entry containing a digit
    public static int[] numberBreaker(int number, int expectedLength)
    {
        double numberLength = 0.0;
        double numberDouble = (double) number;
        int difference = 0;
        int[] digits;
        // determine the length of a number by dividing by ten until it is smaller than 1
        while (numberDouble >= 1)
        {
            numberDouble = numberDouble / 10;
            numberLength += 1;
        }
        // if the expectedLength is 0, the method will return an array with the length of the number
        numberDouble = (double) number;
        if (expectedLength == 0)
        {
            digits = new int[(int) numberLength];
        }
        // otherwise it will create an array with the expectedLength
        else
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
            // see how often you can subtract a certain order (i.e. 1, 10, 100)
            while (numberDouble >= order)
            {
                // the amount of times you can subtract it before it gets below that order, is the
                // digit
                numberDouble = numberDouble - order;
                digit += 1;
            }
            digits[position + difference] = digit;
            position += 1;
        }
        return digits;
    }

    // returns a string which shows which manipulation occurs
    public static String getManipulationString(int manipulation)
    {
        String manipulationString;
        switch (manipulation)
        {
            case 0:
                manipulationString = "+";
                break;
            // at this moment I only use subtraction, but the method will be used more extensively
            case 1:
                manipulationString = "-";
                break;
            case 2:
                manipulationString = "*";
                break;
            case 3:
                manipulationString = "/";
                break;
            default:
                manipulationString = "+";
                break;
        }
        return manipulationString;
    }
}
