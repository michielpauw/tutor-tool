package nl.mprog.rekenbijles;

/**
 * Created by Michiel Pauw on 08/01/15.
 * A class which generates problems, partly pseudo randomly, later also with information from
 * diagnosis.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ProblemGenerator {

    private int manipulation;
    private int amount_generated;
    private float[] ratioCumulative;
    private float randomFloat;
    private int currentIndex;
    private ArrayList<int[]> allProblems;

    public ProblemGenerator(int manipulation_in, int amount)
    {
        manipulation = manipulation_in;
        amount_generated = amount;
        allProblems = new ArrayList<int[]>();
    }

    // for the first set of problems, the problems can still be completely random
    public int[][] generateNumbers()
    {
        int[][] numbers = new int[amount_generated][2];
        for (int i = 0; i < amount_generated; i++)
        {
            int[] number = generateProblem();
            numbers[i] = number;
            // add the problem to allProblems for later reference
            allProblems.add(number);
        }
        return numbers;
    }

    // generate a single problem, based on the manipulation we want to do
    private int[] generateProblem()
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

    // after a number of bugs is found, specific problems will be created to test them
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
            int[] problemTestingBug;
            // create a number, which will determine for which bug a specific problem is generated
            Random rand = new Random();
            // I can suppose the cumulative ratio's add up to 1, but I don't take risks in rounding
            randomFloat = rand.nextFloat() * maxValue;
            currentIndex = 0;
            // get which bug we want to have the problem testing
            int bugToTestIndex = getBugToTest(leftBorder, rightBorder);
            int[] bugToTest = bugs[bugToTestIndex];
            bugToTestList[i] = bugToTest;
            // get a problem that tests this specific bug
            problemTestingBug = getProblemTestingBug(bugToTest, ratio);
            specificProblems[i] = problemTestingBug;
            allProblems.add(problemTestingBug);
        }
        return specificProblems;
    }

    public ArrayList<int[]> getAllProblems()
    {
        return allProblems;
    }

    // create an array of all the ratio's added up to all foregoing ratio's
    private void generateRatioCumulative(float[] ratio)
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

    private int getBugToTest(float leftBorder, float rightBorder)
    {
        // if the random float is in this interval: return the currentIndex
        // the intervals of bugs that occur more often are larges, because of a larger ratio
        if (randomFloat >= leftBorder && randomFloat < rightBorder)
        {
            return currentIndex;
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

    // create a random problem and test whether the bug we want to test would return a buggy answer
    private int[] getProblemTestingBug(int[] bug, float[] ratio)
    {
        int[][] problemList = new int[1][];

        boolean explanation = false;
        do {
            // loop until we found a problem which could test the bug
            problemList[0] = generateProblem();
            ProblemAnalysis analyzeThisProblem = new ProblemAnalysis(manipulation, problemList);
            explanation = analyzeThisProblem.runSpecificAnalysis(bug);
        } while (!explanation);

        return problemList[0];
    }
}
