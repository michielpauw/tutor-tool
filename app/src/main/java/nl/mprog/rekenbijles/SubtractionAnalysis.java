package nl.mprog.rekenbijles;

import java.util.Arrays;

/**
 * Created by michielpauw on 19/01/15.
 */
public class SubtractionAnalysis extends FindBugs {

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

    public SubtractionAnalysis(int[] problem_in, int[] answer_in)
    {
        super(problem_in, answer_in);
        comparerBuggy = false;
        borrowerFirstBuggy = false;
        zeroerFirstBuggy = false;
    }

    public void runAnalysis(int maxBugLength, boolean remove)
    {
        boolean continueAnalysis = true;
        continueAnalysis = setupAnalysis(maxBugLength, remove);

        while (continueAnalysis)
        {
            // get the boolean values of the gates, if true a gate is buggy
            bugsProbe = getProbes();
            if (bugsProbe.length == 0)
            {
                continueAnalysis = false;
                break;
            }
            continueAnalysis = analyseSteps(subtractionSteps(bugsProbe));
        }
    }

    public boolean subtractionSteps(boolean[] bugsProbe)
    {
        // doing subtraction step by step, to get expected outcome of each gate
        comparerOne = comparer(digitsProblemOne[lengthOne], digitsProblemTwo[lengthTwo],
                bugsProbe[0]);
        borrowerFirstValueOne = borrowerFirstValue(comparerOne,
                bugsProbe[1]);
        borrowerSecondValueOne = borrowerSecondValue(comparerOne,
                digitsProblemOne[lengthOne], bugsProbe[2]);
        differencerFour = differencer(borrowerSecondValueOne, digitsProblemTwo[lengthTwo],
                bugsProbe[3]);
        int[] buggyAnswer = new int[3];
        buggyAnswer[2] = differencerFour;
        zeroerFirstValueOne = zeroerFirstValue(borrowerFirstValueOne,
                digitsProblemOne[lengthOne - 1], bugsProbe[4]);
        zeroerSecondValueOne = zeroerSecondValue(borrowerFirstValueOne,
                digitsProblemOne[lengthOne - 1], bugsProbe[5]);
        comparerTwo = comparer(zeroerSecondValueOne, digitsProblemTwo[lengthTwo - 1], bugsProbe[6]);
        borrowerFirstValueTwo = borrowerFirstValue(comparerTwo, bugsProbe[7]);
        borrowerSecondValueTwo = borrowerSecondValue(comparerTwo, zeroerSecondValueOne,
                bugsProbe[8]);
        differencerThree = differencer(borrowerSecondValueTwo, digitsProblemTwo[lengthTwo - 1],
                bugsProbe[9]);
        buggyAnswer[1] = differencerThree;
        orOne = orOperator(zeroerFirstValueOne, borrowerFirstValueTwo, bugsProbe[10]);
        differencerOne = differencer(digitsProblemOne[lengthOne - 2], orOne, bugsProbe[11]);
        differencerTwo = differencer(differencerOne, digitsProblemTwo[lengthTwo - 2],
                bugsProbe[12]);
        buggyAnswer[0] = differencerTwo;

        boolean[] rightDigitsAnalysis = new boolean[3];

        for (int i = 0; i < 3; i++)
        {
            if (buggyAnswer[i] == digitsCorrectAnswer[i])
            {
                rightDigitsAnalysis[i] = true;
            } else
            {
                rightDigitsAnalysis[i] = false;
            }
        }

        // if this configuration of bugs gives wrong digits at the same places as the answer given,
        // then we say the configuration can be responsible for the wrong answer.
        boolean[] allRight = new boolean[] {true, true, true};
        if (Arrays.equals(rightDigitsAnalysis, rightDigit) && !Arrays.equals(rightDigit, allRight))
        {
            return true;
        } else
        {
            return false;
        }
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
        } else
        {
            comparerBuggy = false;
        }

        // return the correct value if the program is not buggy, otherwise return opposite.
        if ((Math.abs(digitOne) >= Math.abs(digitTwo) && !buggy) || (Math.abs(digitOne) < Math
                .abs(digitTwo) && buggy))
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
                } else
                {
                    comparer = true;
                }
            }
        } else
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
                } else
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
        } else
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
        } else
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
            } else
            {
                return -9;
            }
        } else if (borrow == 1)
        {
            if (!buggy)
            {
                return digit - 1;
            } else
            {
                return -digit + 1;
            }
        } else
        {
            if (!buggy)
            {
                return digit;
            } else
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
