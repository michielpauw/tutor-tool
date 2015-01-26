package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 08/01/15.
 * A class which generates problems, partly pseudo randomly, later also with information from
 * diagnosis.
 */

import java.util.Arrays;
import java.util.Random;

public class ProblemGenerator {

    private static int manipulation;
    private static int amount_generated;
    private static float[] ratioCumulative;
    private static float randomFloat;
    private static int currentIndex;
    private static int[][] bugToTestList;

    public ProblemGenerator(int manipulation_in, int amount)
    {
        manipulation = manipulation_in;
        amount_generated = amount;
    }

    public int[][] createSpecificProblems(float[] ratio, int[][] bugs)
    {
        generateRatioCumulative(ratio);
        int[][] specificProblems = new int[amount_generated][];
        int[][] bugToTestList = new int[amount_generated][];
        float leftBorder = 0;
        float rightBorder = ratioCumulative[0];
        float maxValue = ratio[ratio.length - 1];
        for (int i = 0; i < amount_generated; i++)
        {
            int[] problemTestingBug = new int[2];
            Random rand = new Random();
            // I could suppose the cumulative ratio's add up to 1, but I don't want to take risks
            // in rounding
            randomFloat = rand.nextFloat() * maxValue;
            currentIndex = 0;
            int bugToTestIndex = getBugToTest(leftBorder, rightBorder);
            int[] bugToTest = bugs[bugToTestIndex];
            bugToTestList[i] = bugToTest;
            problemTestingBug = getProblemTestingBug(bugToTest);
            specificProblems[i] = problemTestingBug;
        }
        return specificProblems;
    }

    private static int[][] getBugToTestList()
    {
        return bugToTestList;
    }

    public void generateRatioCumulative(float[] ratio)
    {
        int length = ratio.length;
        ratioCumulative = new float[length];
        float currentRatio = 0;
        for (int i = 0; i < length; i++)
        {
            ratioCumulative[i] = ratio[i] + currentRatio;
            currentRatio += ratio[i];
        }
    }

    public int getBugToTest(float leftBorder, float rightBorder)
    {
        // if the random float is in this interval: return the currentIndex
        if (randomFloat >= leftBorder && randomFloat < rightBorder)
        {
            return currentIndex;
        }
        // if there's an error (randomFloat outside of ratioCumulative): return -1
        else if (currentIndex >= ratioCumulative.length - 2)
        {
            return -1;
        }
        // else see if the randomFloat is in the next interval
        else
        {
            leftBorder = ratioCumulative[currentIndex];
            rightBorder = ratioCumulative[currentIndex + 1];
            currentIndex ++;
            return getBugToTest(leftBorder, rightBorder);
        }
    }

    public int[] getProblemTestingBug(int[] bug)
    {
        int[][] problemList = new int[1][];

        boolean problemNotFit = true;
        do {
            problemList[0] = generateProblem();
            ProblemAnalysis analyzeThisProblem = new ProblemAnalysis(manipulation, problemList);
            analyzeThisProblem.runAnalysis();
            int[][] bugs = analyzeThisProblem.getSortedBugs();
            if (checkIfBugPresent(bugs, bug))
            {
                problemNotFit = false;
            }
        } while (problemNotFit);

        return problemList[0];
    }

    public int[] generateProblem()
    {
        int r_1;
        int r_2;
        Random r = new Random();
        int[] numbers = new int[2];
        // make sure the fractions or subtractions are not trivial
        do
        {
            r_1 = r.nextInt(1000);
            r_2 = r.nextInt(1000);
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
        numbers[0] = r_1;
        numbers[1] = r_2;
        return numbers;
    }

    public boolean checkIfBugPresent(int[][] bugs, int[] bug)
    {
        boolean toReturn = false;
        int length = bugs.length;
        for (int i = 0; i < length; i++)
        {
            if (Arrays.equals(bugs[i], bug))
            {
                return true;
            }
        }
        return false;
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
