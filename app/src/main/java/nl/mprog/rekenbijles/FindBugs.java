package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 12/01/15.
 */

import java.util.Arrays;

public class FindBugs {

    private static int[] problems;
    private static int[][] answers;
    private static int bugAmount = 20;
    private static int amountGates = 13;
    private static boolean[] noBugs = new boolean[amountGates];
    private static int[][] bugs = new int[bugAmount][];


    public static int lengthOne;
    public static int lengthTwo;
    public static int[] digitsProblemOne;
    public static int[] digitsProblemTwo;
    public static int[] digitsCorrectAnswer;
    public static int[] answerProvided;
    public static int[] answerManipulated;
    public static boolean[] rightDigit;

    public static boolean[] bugsProbe;
    public static int[] stillToCheck;

    private static int currentAmountToTest;
    private static int toCheckLeft = amountGates;
    private static int[] currentGatesProbed;
    private static boolean continueAnalysis = true;

    private static int[] currentlyProbedIndex;
    private static int currentProbedAmount;
    private static int currentBug;


    public FindBugs(int[] problem_in, int[] answer_in)
    {
        problems = problem_in;
        digitsProblemOne = Tools.numberBreaker(problems[0], 3);
        digitsProblemTwo = Tools.numberBreaker(problems[1], 3);
        digitsCorrectAnswer = Tools.numberBreaker(problems[0] - problems[1], 3);
        answerProvided = answer_in;
        lengthOne = digitsProblemOne.length - 1;
        lengthTwo = digitsProblemTwo.length - 1;
        currentAmountToTest = 1;
    }

    public int[][] getBugs()
    {
        return bugs;
    }

    public boolean setupAnalysis()
    {
        int answerLength = answerProvided.length;

        answerManipulated = new int[answerLength];
        rightDigit = new boolean[3];
        boolean notAllTrue = false;
        for (int i = 0; i < answerLength; i++)
        {
            if (answerProvided[i] == digitsCorrectAnswer[i])
            {
                answerManipulated[i] = digitsCorrectAnswer[i];
                rightDigit[i] = true;
            } else
            {
                answerManipulated[i] = -digitsCorrectAnswer[i];
                notAllTrue = true;
                rightDigit[i] = false;
            }
        }

        bugsProbe = new boolean[amountGates];
        System.arraycopy(noBugs, 0, bugsProbe, 0, noBugs.length);
        toCheckLeft = amountGates;
        stillToCheck = stillToCheck(-1, new int[amountGates]);
        currentGatesProbed = new int[] {-1};
        currentBug = 0;
        currentlyProbedIndex = new int[] {0};
        currentProbedAmount = 0;
        return notAllTrue;
    }

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
            stillToCheck = removeGate(amountGatesBuggy, stillToCheck, bugsProbe);
            int[] copyOfGates = new int[currentGatesProbed.length];
            System.arraycopy(currentGatesProbed, 0, copyOfGates, 0, currentGatesProbed.length);
            // append the bug to the bugs array
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


    public boolean[] getProbes(int[] stillToCheck)
    {
        boolean[] probeArray = new boolean[amountGates];

        int[] toTest = new int[currentAmountToTest];

        boolean appendGate = false;
        boolean reachedMax = false;

        int i = currentlyProbedIndex.length - 1;
        // if the rightmost column has the highest remaining gate as entry, increase the gate
        // number of the column to its left.
        while (currentlyProbedIndex[i] > stillToCheck.length - 1)
        {
            // if there is a column to its left which does not have the highest gate number as an
            // entry, increase this value and set the value of the rightmost column to a low entry.
            if (i > 0)
            {
                appendGate = false;
                reachedMax = false;
                int lengthPrevious = currentlyProbedIndex[i - 1];

                int k = 0;
                while (lengthPrevious >= stillToCheck.length && k < i)
                {
                    lengthPrevious = currentlyProbedIndex[i - k];
                    k++;
                }

                int indexToStartFrom = lengthPrevious + k;
                if (indexToStartFrom >= stillToCheck.length)
                {
                    continueAnalysis = false;
                    break;
                }
                currentlyProbedIndex[i] = indexToStartFrom;
                currentlyProbedIndex[i - 1]++;

                // decrease i, to check whether the column to its left does not contain the highest
                // remaining gate already.
                i--;
            } else
            {
                // otherwise, we need to append a new gate to be probed.
                appendGate = true;
                break;
            }
        }

        // currentGatesProbed gets its values from the stillToCheck list. Keeping track which index
        // to use in the stillToCheck list is difficult, but very important.
        if (!appendGate && !reachedMax)
        {
            for (int j = 0; j < currentlyProbedIndex.length; j++)
            {
                currentGatesProbed[j] = stillToCheck[currentlyProbedIndex[j]];
            }
        }

        // consider testing an extra gate to find a bug
        else if (appendGate)
        {
            currentProbedAmount++;
            currentGatesProbed = Arrays.copyOf(currentGatesProbed, currentGatesProbed.length + 1);
            currentlyProbedIndex = Arrays.copyOf(currentlyProbedIndex,
                    currentlyProbedIndex.length + 1);
            for (int j = 0; j < currentGatesProbed.length; j++)
            {
                if (stillToCheck.length == j)
                {
                    continueAnalysis = false;
                    break;
                }
                currentlyProbedIndex[j] = j;
                currentGatesProbed[j] = stillToCheck[j];
            }

        }

        // gates 13, 14 and 15 indicate that the gates with two outputs are completely buggy, and
        // not just one of their outputs.
        for (int j = 0; j < currentGatesProbed.length; j++)
        {
            probeArray[currentGatesProbed[j]] = true;
//            if (currentGatesProbed[j] == 13)
//            {
//                probeArray[1] = true;
//                probeArray[2] = true;
//            } else if (currentGatesProbed[j] == 14)
//            {
//                probeArray[4] = true;
//                probeArray[5] = true;
//            } else if (currentGatesProbed[j] == 15)
//            {
//                probeArray[7] = true;
//                probeArray[8] = true;
//            }
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

    // because some gates have more than one output, I split the operations of that gate. Nonethe-
    // less, if a part of that gate turns out to be buggy, I should remove both operations from
    // that gate.
    public int[] removeGate(int amountGatesBuggy, int[] stillToCheck, boolean[] bugsProbe)
    {
        int length = bugsProbe.length;
        int removed = 0;
        boolean changedIndex = false;

        for (int i = length - 1; i >= 0; i--)
        {
            if (bugsProbe[i] && removed < amountGatesBuggy)
            {
                removed++;
                toCheckLeft--;
                changeIndex(amountGatesBuggy);
                stillToCheck = stillToCheck(i, stillToCheck);
            }
        }
        return stillToCheck;
    }
//
//    public void setOthersFalse(int gate)
//    {
//        switch (gate)
//        {
//            case 13:
//                bugsProbe[1] = false;
//                bugsProbe[2] = false;
//                bugsProbe[13] = false;
//                break;
//            case 14:
//                bugsProbe[4] = false;
//                bugsProbe[5] = false;
//                bugsProbe[14] = false;
//                break;
//            case 15:
//                bugsProbe[7] = false;
//                bugsProbe[8] = false;
//                bugsProbe[15] = false;
//                break;
//            default:
//                break;
//        }
//    }

//    switch (i)
//                {
//                    case 1:
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(2, stillToCheck);
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(13, stillToCheck);
//                        setOthersFalse(13);
//                        break;
//                    case 2:
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(1, stillToCheck);
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(13, stillToCheck);
//                        changeIndex(amountGatesBuggy, -1);
//                        setOthersFalse(13);
//                        break;
//                    case 4:
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(5, stillToCheck);
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(14, stillToCheck);
//                        setOthersFalse(14);
//                        break;
//                    case 5:
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(4, stillToCheck);
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(14, stillToCheck);
//                        setOthersFalse(14);
//                        changeIndex(amountGatesBuggy, -1);
//                        break;
//                    case 7:
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(8, stillToCheck);
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(15, stillToCheck);
//                        setOthersFalse(15);
//                        break;
//                    case 8:
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(7, stillToCheck);
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(15, stillToCheck);
//                        changeIndex(amountGatesBuggy, -1);
//                        setOthersFalse(15);
//                        break;
//                    case 13:
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(1, stillToCheck);
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(2, stillToCheck);
//                        changeIndex(amountGatesBuggy, -2);
//                        setOthersFalse(13);
//                        break;
//                    case 14:
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(4, stillToCheck);
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(5, stillToCheck);
//                        changeIndex(amountGatesBuggy, -2);
//                        setOthersFalse(14);
//                        break;
//                    case 15:
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(8, stillToCheck);
//                        toCheckLeft--;
//                        stillToCheck = stillToCheck(7, stillToCheck);
//                        changeIndex(amountGatesBuggy, -2);
//                        setOthersFalse(15);
//                        break;
//                    default:
//                        break;
//                }
}