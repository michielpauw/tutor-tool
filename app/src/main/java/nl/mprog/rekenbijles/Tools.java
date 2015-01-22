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

    public static int blockAmount(int first, int second, int manipulation)
    {
        double workWith;
        int result;
        double numberLength = 0.0;
        switch (manipulation)
        {
            case 0:
                result = first + second;
                break;
            case 1:
                result = first - second;
                break;
            case 2:
                result = first * second;
                break;
            case 3:
                result = first / second;
                break;
            default:
                result = first + second;
                break;
        }
        if (first > second)
        {
            workWith = first;
        } else
        {
            workWith = second;
        }
        if (result > workWith)
        {
            workWith = result;
        }
        while (workWith >= 1)
        {
            workWith = workWith / 10;
            numberLength += 1;
        }
        return (int) numberLength;
    }

    public static String getManipulationString(int manipulation)
    {
        String manipulationString;
        switch (manipulation)
        {
            case 0:
                manipulationString = "+";
                break;
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
