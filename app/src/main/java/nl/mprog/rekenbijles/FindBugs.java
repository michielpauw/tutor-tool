package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 12/01/15.
 */

import java.util.ArrayList;
import java.util.Arrays;

public class FindBugs {

    private static int[] problems;
    private static int[][] answers;
    private static int bugAmount = 20;
    private static int amountGates = 13;
    private static boolean[] noBugs = new boolean[amountGates];
    private static int[][] bugs;


    public static int lengthOne;
    public static int lengthTwo;
    public static int[] digitsProblemOne;
    public static int[] digitsProblemTwo;
    public static int[] digitsCorrectAnswer;
    public static int[] answerProvided;
    public static int[] answerManipulated;
    public static boolean[] rightDigit;

    public static boolean[] bugsProbe;

    private static int toCheckLeft = amountGates;
    private static int[] currentGatesProbed;
    private static boolean continueAnalysis = true;

    private static int[] currentlyProbedIndex;
    private static int currentProbedAmount;
    private static int currentBug;

    private static IterationGates iteration;


    public FindBugs(int[] problem_in, int[] answer_in)
    {
        problems = problem_in;
        digitsProblemOne = Tools.numberBreaker(problems[0], 3);
        digitsProblemTwo = Tools.numberBreaker(problems[1], 3);
        digitsCorrectAnswer = Tools.numberBreaker(problems[0] - problems[1], 3);
        answerProvided = answer_in;
        lengthOne = digitsProblemOne.length - 1;
        lengthTwo = digitsProblemTwo.length - 1;
    }

    public int[][] getBugs()
    {
        return bugs;
    }

    public boolean setupAnalysis()
    {
        int answerLength = answerProvided.length;

        answerManipulated = new int[answerLength];
        continueAnalysis = true;
        rightDigit = new boolean[3];
        boolean notAllTrue = false;
        for (int i = 0; i < answerLength; i++)
        {
            if (answerProvided[i] == digitsCorrectAnswer[i])
            {
                rightDigit[i] = true;
            } else
            {
                notAllTrue = true;
                rightDigit[i] = false;
            }
        }
        bugs = new int[bugAmount][];
        bugsProbe = new boolean[amountGates];
        toCheckLeft = amountGates;
        iteration = new IterationGates(amountGates);
        currentGatesProbed = new int[] {-1};
        currentBug = 0;
        currentlyProbedIndex = new int[] {0};
        currentProbedAmount = 0;
        return notAllTrue;
    }

    /**
     * Handles a buggy configuration by removing gates from stillToCheck array.
     * @param buggy if true the answer can be explained by the current bug configuration.
     * @return whether analysis should be continued.
     */
    public boolean analyseSteps(boolean buggy)
    {
        // the chance of finding five bugs responsible for a wrong answer is small, and it saves
        // processing time to not consider bugs of five gates or more
        if (currentProbedAmount == 5 || !continueAnalysis)
        {
            return false;
        }

        // if a configuration can be held responsible for the wrong answer.
        if (buggy)
        {
            int amountGatesBuggy = currentGatesProbed.length;
            iteration.remove(currentGatesProbed);
            int[] copyOfGates = new int[currentGatesProbed.length];
            System.arraycopy(currentGatesProbed, 0, copyOfGates, 0, currentGatesProbed.length);
            bugs[currentBug] = copyOfGates;
            currentBug++;
        } else
        {
            currentlyProbedIndex[currentProbedAmount]++;
        }
        return continueAnalysis;
    }

    // change the index of which gate should be probed from the stillToCheck list
    public void changeIndex(int amountGates)
    {
        for (int i = 0; i < amountGates; i++)
        {
            currentlyProbedIndex[i] = currentlyProbedIndex[0] + i;
        }
    }


    public boolean[] getProbes()
    {
        boolean[] probeArray = new boolean[amountGates];

        currentGatesProbed = iteration.nextToProbe();

        if (currentGatesProbed[0] == -2)
        {
            boolean[] toReturn = new boolean[0];
            continueAnalysis = false;
            return toReturn;
        }
        for (int j = 0; j < currentGatesProbed.length; j++)
        {
            probeArray[currentGatesProbed[j]] = true;
        }
        // return the boolean values for each gate: true means it's buggy
        return probeArray;
    }

    // create a list of gates that have not yet been identified as buggy
    public int[] stillToCheck(int toRemove, int[] previousToCheck)
    {
        int[] toCheck = new int[toCheckLeft];

        int lengthPrevious = previousToCheck.length;
        int positionNew = 0;
        for (int i = 0; i < lengthPrevious; i++)
        {
            if (toRemove == -1)
            {
                toCheck[i] = i;
            } else if (previousToCheck[i] != toRemove)
            {
                toCheck[positionNew] = previousToCheck[i];
                positionNew++;
            }
        }
        return toCheck;
    }
}