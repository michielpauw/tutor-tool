package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 08/01/15.
 * A class which generates problems, partly pseudo randomly, later also with information from
 * diagnosis.
 */

import java.util.Random;

public class ProblemGenerator {

    private static int manipulation;
    private static int amount_generated;

    public ProblemGenerator(int manipulation_in, int amount)
    {
        manipulation = manipulation_in;
        amount_generated = amount;
    }

    public int[][] generateNumbers()
    {
        int r_1;
        int r_2;
        Random r = new Random();
        int[][] numbers = new int[amount_generated][2];
        for (int i = 0; i < amount_generated; i++)
        {
            // make sure the fractions or subtractions are not trivial
            do
            {
                r_1 = r.nextInt(1000);
                r_2 = r.nextInt(1000);
//                r_1 = 377;
//                r_2 = 160;
            } while ((manipulation == 1 || manipulation == 3) && r_1 == r_2);
            // make sure the results will not be negative in case of subtraction
            if (manipulation == 1 && r_1 < r_2)
            {
                int temp = r_1;
                r_1 = r_2;
                r_2 = temp;
            }
            // make sure the fraction is smaller than one
            else if (manipulation == 3 && r_2 < r_1)
            {
                int temp = r_1;
                r_1 = r_2;
                r_2 = temp;
            }
            // save the problems in an array
            numbers[i][0] = r_1;
            numbers[i][1] = r_2;
        }
        return numbers;
    }
}
