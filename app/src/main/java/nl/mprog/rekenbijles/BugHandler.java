package nl.mprog.rekenbijles;

/**
 * Created by Michiel Pauw on 12/01/15.
 * This class creates a list of booleans which tells which gates should be probed in the next
 * iteration.
 */

import java.util.ArrayList;

public class BugHandler {

    private int[] problems;
    protected int amountGates;
    private ArrayList<int[]> bugs;


    public int lengthOne;
    public int lengthTwo;
    public int[] digitsProblemOne;
    public int[] digitsProblemTwo;
    public int[] digitsCorrectAnswer;
    public int[] answerProvided;
    public int[] answerManipulated;
    public boolean[] rightDigit;

    public boolean[] bugsProbe;

    private int[] currentGatesProbed;
    private boolean continueAnalysis = true;

    private int maxBugLength;
    private boolean answerAnalysis;

    private IterationGates iteration;


    public BugHandler(int[] problem_in, int[] answer_in, int amountGatesIn)
    {
        problems = problem_in;
        digitsProblemOne = Utilities.numberBreaker(problems[0], 3);
        digitsProblemTwo = Utilities.numberBreaker(problems[1], 3);
        digitsCorrectAnswer = Utilities.numberBreaker(problems[0] - problems[1], 3);
        answerProvided = answer_in;
        lengthOne = digitsProblemOne.length - 1;
        lengthTwo = digitsProblemTwo.length - 1;
        amountGates = amountGatesIn;
    }

    public ArrayList<int[]> getBugs()
    {
        return bugs;
    }

    // setup the analysis with initial values
    public boolean setupAnalysis(int maxBugLengthIn, boolean analysisIn)
    {
        // check whether there should be analysis, if an answer is correct, analysis is useless
        int answerLength = answerProvided.length;
        boolean notAllTrue = false;
        rightDigit = new boolean[3];
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
        if (!notAllTrue)
        {
            return false;
        }

        // check whether it's a problemAnalysis or an answerAnalysis
        answerAnalysis = analysisIn;
        maxBugLength = maxBugLengthIn;

        answerManipulated = new int[answerLength];
        continueAnalysis = true;

        bugs = new ArrayList<int[]>();
        bugsProbe = new boolean[amountGates];
        iteration = new IterationGates(amountGates, maxBugLength);
        currentGatesProbed = new int[] {-1};
        return notAllTrue;
    }

    // if a configuration is buggy, the tuple of gates will be removed from the to test list
    public boolean analyseSteps(boolean buggy)
    {
        // the chance of finding five problemBugsTotal responsible for a wrong answer is small
        if (!continueAnalysis)
        {
            return false;
        }

        // if a configuration can be held responsible for the wrong answer...
        if (buggy)
        {
            // a buggy tuple will be removed from the to test list, but not when we are testing
            // for potential bugs in problems
            if (answerAnalysis)
            {
                iteration.remove(currentGatesProbed);
            }
            // the buggy tuple will be added to the bugs list
            int[] copyOfGates = new int[currentGatesProbed.length];
            System.arraycopy(currentGatesProbed, 0, copyOfGates, 0, currentGatesProbed.length);
            bugs.add(copyOfGates);
        }
        // return whether the analysis should continue
        return continueAnalysis;
    }

    // returns an array of booleans corresponding to gates which will be true if they are buggy
    public boolean[] getProbes()
    {
        boolean[] probeArray = new boolean[amountGates];

        currentGatesProbed = iteration.nextToProbe();

        // this means that there are no new configurations to test
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
}