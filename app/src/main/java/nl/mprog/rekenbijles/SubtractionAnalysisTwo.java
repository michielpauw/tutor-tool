package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 12/01/15.
 */

import java.util.Arrays;

/**
 * WORK IN PROGRESS
 */
public class SubtractionAnalysisTwo {

    private static int[] problems;
    private static int[][] answers;
    private static int bugAmount = 20;
    private static int amountGates = 13;
    private static boolean[] noBugs = new boolean[amountGates];
    private static int[][] bugs = new int[bugAmount][];
    private static int lengthOne;
    private static int lengthTwo;
    private static int lengthAnswer;

    private static int[] digitsProblemOne;
    private static int[] digitsProblemTwo;
    private static int[] digitsCorrectAnswer;
    private static int[] answerProvided;
    private static int[] answerManipulated;

    private static boolean comparerOne;
    private static int borrowerFirstValueOne;
    private static int borrowerSecondValueOne;
    private static int differencerFour;
    private static int zeroerFirstValueOne;
    private static int zeroerSecondValueOne;
    private static boolean comparerTwo;
    private static int borrowerFirstValueTwo;
    private static int orOne;
    private static int borrowerSecondValueTwo;
    private static int differencerThree;
    private static int differencerOne;
    private static int differencerTwo;

    private static boolean comparerBuggy;
    private static boolean borrowerFirstBuggy;
    private static boolean zeroerFirstBuggy;

    private static int currentAmountToTest;
    private static int toCheckLeft = amountGates;
    private static int[] currentGatesProbed;
    private static int currentlyProbedTracker;
    private static boolean continueAnalysis = true;
    private static int lengthToCheck;

    private static int[] currentlyProbedIndex;
    private static int currentProbedAmount;

    private static boolean[] rightDigit;

    public SubtractionAnalysisTwo(int[] problem_in, int[] answer_in)
    {
        problems = problem_in;
        digitsProblemOne = Tools.numberBreaker(problems[0]);
        digitsProblemTwo = Tools.numberBreaker(problems[1]);
        digitsCorrectAnswer = Tools.numberBreaker(problems[0] - problems[1]);
        answerProvided = answer_in;
        lengthOne = digitsProblemOne.length - 1;
        lengthTwo = digitsProblemTwo.length - 1;
        currentAmountToTest = 1;
    }

//    public int[] analyze()
//    {
//        return bugs;
//    }

    public int[][] getBugs()
    {
        return bugs;
    }

    public void setupAnalysis()
    {
        int answerLength = answerProvided.length;
        answerManipulated = new int[answerLength];
        rightDigit = new boolean[3];
        for (int i = 0; i < answerLength; i++)
        {
            if (answerProvided[i] == digitsCorrectAnswer[i])
            {
                answerManipulated[i] = digitsCorrectAnswer[i];
                rightDigit[i] = true;
            }
            else
            {
                answerManipulated[i] = -digitsCorrectAnswer[i];
                rightDigit[i] = false;
            }
        }


        boolean[] bugsProbe = new boolean[amountGates];
        boolean buggy;
        System.arraycopy(noBugs, 0, bugsProbe, 0, noBugs.length);
        int[] stillToCheck = stillToCheck(-1, new int[amountGates]);
        currentGatesProbed = new int[] {-1};
        int currentBug = 0;
        currentlyProbedTracker = 0;
        currentlyProbedIndex = new int[] {0};
        currentProbedAmount = 0;
        lengthToCheck = amountGates;
        int bugsFound = 0;
        while (continueAnalysis)
        {
            // TODO give currentGatesProbed as argument
            // TODO be frustrated that Java arrays aren't dynamical
            bugsProbe = getProbes(stillToCheck);
            if (runAnalysis(bugsProbe))
            {
                int amountGatesBuggy = currentGatesProbed.length;

                //TODO MAKE THIS PART MUCH NICER, VERY EASY WAY OUT! Make switch statement in
                //TODO separate method.
                for (int i = 0; i < amountGatesBuggy; i++)
                {
                    if (currentGatesProbed[i] == 7)
                    {
                        toCheckLeft --;
                        currentlyProbedIndex[currentProbedAmount] --;
                        stillToCheck = stillToCheck(8, stillToCheck);
                    }
                    else if (currentGatesProbed[i] == 8)
                    {
                        toCheckLeft --;
                        currentlyProbedIndex[currentProbedAmount] --;
                        stillToCheck = stillToCheck(7, stillToCheck);
                    }
                    else if (currentGatesProbed[i] == 2)
                    {
                        toCheckLeft --;
                        currentlyProbedIndex[currentProbedAmount] --;
                        stillToCheck = stillToCheck(1, stillToCheck);
                    }
                    else if (currentGatesProbed[i] == 1)
                    {
                        toCheckLeft --;
                        currentlyProbedIndex[currentProbedAmount] --;
                        stillToCheck = stillToCheck(2, stillToCheck);
                    }
                    else if (currentGatesProbed[i] == 4)
                    {
                        toCheckLeft --;
                        currentlyProbedIndex[currentProbedAmount] --;
                        stillToCheck = stillToCheck(5, stillToCheck);
                    }
                    else if (currentGatesProbed[i] == 5)
                    {
                        toCheckLeft --;
                        currentlyProbedIndex[currentProbedAmount] --;
                        stillToCheck = stillToCheck(4, stillToCheck);
                    }
                    toCheckLeft --;
                    stillToCheck = stillToCheck(currentGatesProbed[i], stillToCheck);
                }
                int[] copyOfGates = new int[currentGatesProbed.length];
                System.arraycopy(currentGatesProbed, 0, copyOfGates, 0, currentGatesProbed.length);
                bugs[currentBug] = copyOfGates;
                currentBug ++;
                bugsFound++;
            }
            else
            {
//                currentlyProbedTracker ++;
                currentlyProbedIndex[currentProbedAmount] ++;
            }
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
//                if (lengthPrevious < stillToCheck.length)
//                {
//                    int indexToStartFrom =  lengthPrevious + 1;
//                    currentlyProbedIndex[i] = indexToStartFrom;
//                    currentlyProbedIndex[i - 1] ++;
//                }
                int k = 0;
                while (lengthPrevious >= stillToCheck.length && k < i)
                {
                    lengthPrevious = currentlyProbedIndex[i - k];
                    k++;
                }

                int indexToStartFrom =  lengthPrevious + k;
                if (indexToStartFrom >= stillToCheck.length)
                {
                    continueAnalysis = false;
                    break;
                }
                currentlyProbedIndex[i] = indexToStartFrom;
                currentlyProbedIndex[i - 1] ++;

                // decrease i, to check whether the column to its left does not contain the highest
                // remaining gate already.
                i--;
            }
            else
            {
                // otherwise, we need to append a new gate to be probed.
                appendGate = true;
                lengthToCheck =  stillToCheck.length;
                break;
            }
        }
        if (!appendGate && !reachedMax)
        {
//            currentlyProbedIndex[i] ++;
            for (int j = 0; j < currentlyProbedIndex.length; j++)
            {
                currentGatesProbed[j] = stillToCheck[currentlyProbedIndex[j]];
            }
        }
        else if (appendGate)
        {
            currentProbedAmount ++;
            currentGatesProbed = Arrays.copyOf(currentGatesProbed, currentGatesProbed.length + 1);
            currentlyProbedIndex = Arrays.copyOf(currentlyProbedIndex, currentlyProbedIndex.length + 1);
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
        for (int j = 0; j < currentGatesProbed.length; j++)
        {
            probeArray[currentGatesProbed[j]] = true;
        }

        //TODO create a way to keep track which gates are probed at the moment

        return probeArray;
    }

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
            }
            else if (previousToCheck[i] != toRemove)
            {
                toCheck[positionNew] = previousToCheck[i];
                positionNew ++;
            }
        }
        return toCheck;
    }

    public boolean runAnalysis(boolean[] bugsProbe)
    {
        // doing subtraction step by step, to get expected outcome of each gate
        comparerOne = comparer(digitsProblemOne[lengthOne], digitsProblemTwo[lengthTwo], bugsProbe[0]);
        borrowerFirstValueOne = borrowerFirstValue(comparerOne, bugsProbe[1]);
        borrowerSecondValueOne = borrowerSecondValue(comparerOne, digitsProblemOne[lengthOne], bugsProbe[2]);
        differencerFour = differencer(borrowerSecondValueOne, digitsProblemTwo[lengthTwo], bugsProbe[3]);
        int[] buggyAnswer = new int[3];
        buggyAnswer[2] = differencerFour;
        zeroerFirstValueOne = zeroerFirstValue(borrowerFirstValueOne, digitsProblemOne[lengthOne - 1], bugsProbe[4]);
        zeroerSecondValueOne = zeroerSecondValue(borrowerFirstValueOne, digitsProblemOne[lengthOne - 1], bugsProbe[5]);
        comparerTwo = comparer(zeroerSecondValueOne, digitsProblemTwo[lengthTwo - 1], bugsProbe[6]);
        borrowerFirstValueTwo = borrowerFirstValue(comparerTwo, bugsProbe[7]);
        borrowerSecondValueTwo = borrowerSecondValue(comparerTwo, zeroerSecondValueOne, bugsProbe[8]);
        differencerThree = differencer(borrowerSecondValueTwo, digitsProblemTwo[lengthTwo - 1], bugsProbe[9]);
        buggyAnswer[1] = differencerThree;
        orOne = orOperator(zeroerFirstValueOne, borrowerFirstValueTwo, bugsProbe[10]);
        differencerOne = differencer(digitsProblemOne[lengthOne - 2], orOne, bugsProbe[11]);
        differencerTwo = differencer(differencerOne, digitsProblemTwo[lengthTwo - 2], bugsProbe[12]);
        buggyAnswer[0] = differencerTwo;
        boolean toReturn;

        // if a gate returning a boolean value (or 0 or 1) is buggy, the result of this algorithm will
        // be the answer given. If a gate returning an int value is buggy, the result of this algorithm
        // will have a negative number where a wrong number was entered.
        int length = buggyAnswer.length;

        boolean[] rightDigitsAnalysis = new boolean[3];
        for (int i = 0; i < length; i++)
        {
            int processing = buggyAnswer[i];
            if (processing < 0)
            {
                buggyAnswer[i] = -(-processing % 10);
            } else
            {
                buggyAnswer[i] = processing % 10;
            }
        }

        for (int i = 0; i < 3; i++)
        {
            if (buggyAnswer[i] == digitsCorrectAnswer[i])
            {
                rightDigitsAnalysis[i] = true;
            }
            else
            {
                rightDigitsAnalysis[i] = false;
            }
        }

        if (rightDigitsAnalysis == rightDigit)
        {
            return true;
        }

        if (Arrays.equals(buggyAnswer, answerManipulated))
        {
            toReturn = true;
        }
        else if (Arrays.equals(buggyAnswer, answerProvided))
        {
            toReturn = true;
        }
        else
        {
            toReturn = false;
        }
        return toReturn;
    }

    // decide whether the number on top is greater or equal to the number below
    public boolean comparer(int digitOne, int digitTwo, boolean buggy)
    {
        // if gate buggy and input buggy, the output may not be buggy
        if (buggy)
        {
            comparerBuggy = true;
            // therefore we set all values to their non-buggy state
            if (digitOne < 0)
            {
                comparerBuggy = false;
                buggy = false;
                digitOne = -digitOne;
            }
            if (digitTwo < 0)
            {
                comparerBuggy = false;
                buggy = false;
                digitTwo = -digitTwo;
            }
        }
        else
        {
            comparerBuggy = false;
        }

        // return the correct value if the program is not buggy, otherwise return opposite.
        if ((Math.abs(digitOne) >= Math.abs(digitTwo) && !buggy) || (Math.abs(digitOne) < Math.abs(digitTwo) && buggy))
        {
            return true;
        } else
        {
            return false;
        }
    }

    // if the top number is smaller, it needs to be incremented by ten (borrow)
    public int borrowerFirstValue(boolean comparer, boolean buggy)
    {
        if (buggy)
        {
            borrowerFirstBuggy = true;
            if (comparerBuggy)
            {
                buggy = false;
                borrowerFirstBuggy = false;
                if (comparer)
                {
                    comparer = false;
                }
                else
                {
                    comparer = true;
                }
            }
        }
        else
        {
            borrowerFirstBuggy = false;
        }
        // return the correct value if the program is not buggy, otherwise return opposite
        if ((comparer && !buggy) || (!comparer && buggy))
        {
            return 0;
        } else
        {
            return 1;
        }
    }

    public int borrowerSecondValue(boolean comparer, int digitOne, boolean buggy)
    {
        // if the input is faulty and the gate buggy, we can assume the output may be non-buggy.
        if (buggy)
        {
            if (digitOne < 0)
            {
                buggy = false;
                digitOne = -digitOne;
            }

            if (comparerBuggy)
            {
                buggy = false;
                if (comparer)
                {
                    comparer = false;
                }
                else
                {
                    comparer = true;
                }
            }
        }

        // return the correct value if the program is not buggy, otherwise its negative for testing
        if ((comparer && !buggy) || (!comparer && buggy))
        {
            return digitOne;
        } else if (buggy)
        {
            return -digitOne - 10;
        }
        else
        {
            return digitOne + 10;
        }
    }

    public int differencer(int digitOne, int digitTwo, boolean buggy)
    {
        // once again, if input is faulty and gate is buggy, it may return a correct value
        if (buggy)
        {
            if (digitOne < 0)
            {
                buggy = false;
                digitOne = -digitOne;
            }
            if (digitTwo < 0)
            {
                buggy = false;
                digitTwo = -digitTwo;
            }
        }

        if (!buggy && !(digitOne < 0 || digitTwo < 0))
        {
            return Math.abs(digitOne) - Math.abs(digitTwo);
        }
        else
        {
            return -(Math.abs(digitOne) - Math.abs(digitTwo));
        }
    }

    public int zeroerFirstValue(int borrow, int digit, boolean buggy)
    {
        if (buggy)
        {
            zeroerFirstBuggy = true;
            if (borrowerFirstBuggy)
            {
                buggy = false;
                borrow = (borrow + 1) % 2;
            }
            if (digit < 0)
            {
                buggy = false;
                digit = -digit;
            }
        }
        if (borrow == 1 && digit == 0 && !buggy)
        {
            zeroerFirstBuggy = false;
            return 1;
        } else if (buggy)
        {
            if (borrow == 1 && digit == 0)
            {
                return 0;
            } else
            {
                return 1;
            }
        } else
        {
            zeroerFirstBuggy = false;
            return 0;
        }
    }

    public int zeroerSecondValue(int borrow, int digit, boolean buggy)
    {
        if (buggy)
        {
            if (borrowerFirstBuggy)
            {
                buggy = false;
                borrow = (borrow + 1) % 2;
            }
            if (digit < 0)
            {
                buggy = false;
                digit = -digit;
            }
        }

        if (borrow == 1 && digit == 0)
        {
            if (!buggy)
            {
                return 9;
            }
            else
            {
                return -9;
            }
        } else if (borrow == 1)
        {
            if (!buggy)
            {
                return digit - 1;
            }
            else
            {
                return -digit + 1;
            }
        } else
        {
            if (!buggy)
            {
                return digit;
            }
            else
            {
                return -digit;
            }
        }
    }

    public int orOperator(int orBooleanOne, int orBooleanTwo, boolean buggy)
    {
        if (buggy)
        {
            if (zeroerFirstBuggy)
            {
                buggy = false;
                orBooleanOne = (orBooleanOne + 1) % 2;
            }
            if (borrowerFirstBuggy)
            {
                buggy = false;
                orBooleanTwo = (orBooleanTwo + 1) % 2;
            }
        }
        if (orBooleanOne == 1 || orBooleanTwo == 1 && !buggy)
        {
            return 1;
        } else if (buggy && orBooleanOne == 0 && orBooleanTwo == 0)
        {
            return 1;
        } else if (buggy && orBooleanOne == 1 || orBooleanTwo == 1)
        {
            return 0;
        }
        {
            return 0;
        }
    }
}

//    public void backwardAnalysis()
//    {
//        int bugIndex = 0;
//        if (answerProvided[1] != digitsCorrectAnswer[1])
//        {
//            bugs[bugIndex] = "d_3";
//            bugIndex += 1;
//            int[][] difThreePossibleInput = supposeDifferencerCorrect(borrowerTwo[1],
//                    digitsProblemTwo[lengthTwo - 1]);
//
//        }
////    }
//
//    public int[][] supposeDifferencerCorrect(int correctInputOne, int correctInputTwo)
//    {
//        int[][] possibleWrongInput = {{correctInputOne, -1}, {-1, correctInputTwo}, {-1, -1}};
//        return possibleWrongInput;
//    }
//
//    public int supposeDifferencerWrong()
//    {
//        return -1;
//    }
//
//    public int[][] supposeBorrowerCorrect(boolean correctInputOne, int correctInputTwo,
//                                          int correctOutputOne, int correctOutputTwo)
//    {
//        if (correctOutputOne == 0)
//        {
//            int[][] possibleWrongInput = {{0, correctInputTwo}, {1, -1}, {0, -1}};
//            return possibleWrongInput;
//        } else
//        {
//            int[][] possibleWrongInput = {{1, correctInputTwo}, {0, -1}, {1, -1}};
//            return possibleWrongInput;
//        }
//    }
//
//    public int[][] supposeComparerCorrect(int correctInputOne, int correctInputTwo,
//                                          boolean correctOutput)
//    {
//        if (correctOutput == true)
//        {
//            int[][] possibleWrongInput = new int[correctInputTwo][];
//            for (int i = 0; i < correctInputTwo; i++)
//            {
//                int[] wrongInput = {i, correctInputTwo};
//                possibleWrongInput[i] = wrongInput;
//            }
//            return possibleWrongInput;
//        } else
//        {
//            int amount = 9 - correctInputTwo;
//            int[][] possibleWrongInput = new int[amount][];
//            for (int i = 0; i < amount; i++)
//            {
//                int[] wrongInput = {9 - i, correctInputTwo};
//                possibleWrongInput[i] = wrongInput;
//            }
//            return possibleWrongInput;
//        }
//    }

//    public int[] processCorrect()
//    {
//        // doing subtraction step by step, to get expected outcome of each gate
//        comparerOne = comparer(digitsProblemOne[lengthOne], digitsProblemTwo[lengthTwo], false);
//        borrowerFirstValueOne = borrowerFirstValue(comparerOne, false);
//        borrowerSecondValueOne = borrowerSecondValue(comparerOne, digitsProblemOne[lengthOne], false);
//        differencerFour = differencer(borrowerSecondValueOne, digitsProblemTwo[lengthTwo], false);
//        int[] answerAlgorithm = new int[3];
//        answerAlgorithm[2] = differencerFour;
//        zeroerFirstValueOne = zeroerFirstValue(borrowerFirstValueOne, digitsProblemOne[lengthOne - 1], false);
//        zeroerSecondValueOne = zeroerSecondValue(borrowerFirstValueOne, digitsProblemOne[lengthOne - 1], false);
//        comparerTwo = comparer(zeroerSecondValueOne, digitsProblemTwo[lengthTwo - 1], false);
//        borrowerFirstValueTwo = borrowerFirstValue(comparerTwo, false);
//        borrowerSecondValueTwo = borrowerSecondValue(comparerTwo, zeroerSecondValueOne, false);
//        differencerThree = differencer(borrowerSecondValueTwo, digitsProblemTwo[lengthTwo - 1], false);
//        answerAlgorithm[1] = differencerThree;
//        orOne = orOperator(zeroerFirstValueOne, borrowerFirstValueTwo, false);
//        differencerOne = differencer(digitsProblemOne[lengthOne - 2], orOne, false);
//        differencerTwo = differencer(differencerOne, digitsProblemTwo[lengthTwo - 2], false);
//        answerAlgorithm[0] = differencerTwo;
//        return answerAlgorithm;
//    }