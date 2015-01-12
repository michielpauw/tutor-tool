package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 12/01/15.
 */
public class Tools {
    static int[] numberBreaker(int number)
    {
        double numberLength = 0.0;
        double numberDouble = (double) number;

        while (numberDouble >= 1)
        {
            numberDouble = numberDouble / 10;
            numberLength += 1;
        }
        numberDouble = (double) number;
        int[] digits = new int[(int) numberLength];
        int position = 0;
        for (int i = 0; i < numberLength; i++)
        {
            int digit = 0;
            double order = Math.pow(10.0, numberLength - 1 - i);
            while(numberDouble >= order)
            {
                numberDouble = numberDouble - order;
                digit += 1;
            }
            digits[position] = digit;
            position += 1;
        }
        return digits;
    }
}
