package nl.mprog.rekenbijles;

import java.util.Arrays;

/**
 * Created by Michiel Pauw on 19/01/15.
 * This class implements the algorithm of subtraction. It can also accept a boolean array which
 * tells specific gates to perform buggy. It will check whether a buggy gate accounts for a buggy
 * answer entered by the user.
 */
public class SubtractionAnalysis extends BuggyHandler {

    private boolean comparerOne;
    private int borrowerFirstValueOne;
    private int borrowerSecondValueOne;
    private int differencerFour;
    private int zeroerFirstValueOne;
    private int zeroerSecondValueOne;
    private boolean comparerTwo;
    private int borrowerFirstValueTwo;
    private int orOne;
    private int borrowerSecondValueTwo;
    private int differencerThree;
    private int differencerOne;
    private int differencerTwo;

    private boolean comparerBuggy;
    private boolean borrowerFirstBuggy;
    private boolean zeroerFirstBuggy;

    public SubtractionAnalysis(int[] problem_in, int[] answer_in, int maxBugLengthIn, boolean answerAnalysisIn)
    {
        // the argument 21 is the amount of gates that this manipulation has to go through
        // (including some gates that can be buggy in more than one way)
        super(problem_in, answer_in, 21);
        maxBugLength = maxBugLengthIn;
        answerAnalysis = answerAnalysisIn;
        comparerBuggy = false;
        borrowerFirstBuggy = false;
        zeroerFirstBuggy = false;
    }

    public void runAnalysis()
    {
        boolean continueAnalysis = true;
        continueAnalysis = setupAnalysis();

        while (continueAnalysis)
        {
            // get the boolean values of the gates, if true a gate is buggy
            bugsProbe = getProbes();
            if (bugsProbe.length == 0)
            {
                break;
            }
            boolean buggy = subtractionAlgorithm(answerAnalysis);
            continueAnalysis = analyseSteps(buggy);
        }
    }

    // check whether a problem will return a buggy answer when a specific bug is true
    public boolean runSpecificAnalysis(int[] bug)
    {
        bugsProbe = new boolean[amountGates];
        int length = bug.length;
        for (int i = 0; i < length; i++)
        {
            int index = bug[i];
            bugsProbe[index] = true;
        }
        boolean buggy = subtractionAlgorithm(false);

        return buggy;
    }

    private boolean subtractionAlgorithm(boolean analysis)
    {
        int[] buggyAnswer = subtractionSteps();
        boolean[] allRight = new boolean[] {true, true, true};
        boolean[] rightDigitsAnalysis = getRightDigitsAnalysis(buggyAnswer);
        // if we do the AnswerAnalysis we compare the buggy result of the algorithm to the buggy
        // answer provided
        if (analysis)
        {
            // if this configuration of problemBugsTotal gives wrong digits at the same places as
            // the answer given,
            // then we say the configuration can be responsible for the wrong answer.
            if (Arrays.equals(rightDigitsAnalysis, rightDigit) && !Arrays.equals(rightDigit,
                    allRight))
            {
                return true;
            } else
            {
                return false;
            }
        }
        // in ProblemAnalysis, we just look whether a bug provides a faulty answer in any way
        else
        {
            if (!Arrays.equals(rightDigitsAnalysis, allRight))
            {
                return true;
            } else
            {
                return false;
            }
        }
    }

    private int[] subtractionSteps()
    {
        // doing subtraction step by step, to get expected outcome of each gate
        // the boolean values indicate whether (a part of) a gate is buggy
        comparerOne = comparer(digitsProblemOne[lengthOne], digitsProblemTwo[lengthTwo],
                bugsProbe[0], bugsProbe[13]);
        borrowerFirstValueOne = borrowerFirstValue(comparerOne,
                bugsProbe[1], bugsProbe[14]);
        borrowerSecondValueOne = borrowerSecondValue(comparerOne,
                digitsProblemOne[lengthOne], bugsProbe[2], bugsProbe[18]);
        differencerFour = differencer(borrowerSecondValueOne, digitsProblemTwo[lengthTwo],
                bugsProbe[3]);
        int[] buggyAnswer = new int[3];
        buggyAnswer[2] = differencerFour;
        zeroerFirstValueOne = zeroerFirstValue(borrowerFirstValueOne,
                digitsProblemOne[lengthOne - 1], bugsProbe[4], bugsProbe[15]);
        zeroerSecondValueOne = zeroerSecondValue(borrowerFirstValueOne,
                digitsProblemOne[lengthOne - 1], bugsProbe[5]);
        comparerTwo = comparer(zeroerSecondValueOne, digitsProblemTwo[lengthTwo - 1],
                bugsProbe[6], bugsProbe[16]);
        borrowerFirstValueTwo = borrowerFirstValue(comparerTwo, bugsProbe[7], bugsProbe[17]);
        borrowerSecondValueTwo = borrowerSecondValue(comparerTwo, zeroerSecondValueOne,
                bugsProbe[8], bugsProbe[19]);
        differencerThree = differencer(borrowerSecondValueTwo, digitsProblemTwo[lengthTwo - 1],
                bugsProbe[9]);
        buggyAnswer[1] = differencerThree;
        orOne = orOperator(zeroerFirstValueOne, borrowerFirstValueTwo, bugsProbe[10],
                bugsProbe[20]);
        differencerOne = differencer(digitsProblemOne[lengthOne - 2], orOne, bugsProbe[11]);
        differencerTwo = differencer(differencerOne, digitsProblemTwo[lengthTwo - 2],
                bugsProbe[12]);
        buggyAnswer[0] = differencerTwo;

        return buggyAnswer;
    }

    // create a boolean array with true if a digit from the buggy corresponds to the correct answer
    private boolean[] getRightDigitsAnalysis(int[] buggyAnswer)
    {
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
        return rightDigitsAnalysis;
    }

    // decide whether the number on top is greater or equal to the number below
    private boolean comparer(int digitOne, int digitTwo, boolean buggyAlwaysTrue,
                             boolean buggyAlwaysFalse)
    {
        // if gate buggy and input buggy, the output may not be buggy
        if (buggyAlwaysTrue || buggyAlwaysFalse)
        {
            comparerBuggy = true;
            // therefore we set all values to their non-buggy state
            if (digitOne < 0)
            {
                comparerBuggy = false;
                buggyAlwaysFalse = false;
                buggyAlwaysTrue = false;
                digitOne = -digitOne;
            }
            if (digitTwo < 0)
            {
                comparerBuggy = false;
                buggyAlwaysFalse = false;
                buggyAlwaysTrue = false;
                digitTwo = -digitTwo;
            }
        } else
        {
            comparerBuggy = false;
        }

        if (((Math.abs(digitOne) >= Math.abs(digitTwo)) || buggyAlwaysTrue) && !buggyAlwaysFalse)
        {
            return true;
        } else
        {
            return false;
        }
    }

    // if the top number is smaller, it needs to be incremented by ten (borrow)
    private int borrowerFirstValue(boolean comparer, boolean buggyAlwaysTrue,
                                   boolean buggyAlwaysFalse)
    {
        // if gate buggy and input buggy, the output may not be buggy
        if (buggyAlwaysTrue || buggyAlwaysFalse)
        {
            borrowerFirstBuggy = true;
            if (comparerBuggy)
            {
                buggyAlwaysFalse = false;
                buggyAlwaysTrue = false;
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
        if ((comparer || buggyAlwaysFalse) && !buggyAlwaysTrue)
        {
            return 0;
        } else
        {
            return 1;
        }
    }

    private int borrowerSecondValue(boolean comparer, int digitOne, boolean buggyNeverIncrement,
                                    boolean buggyAlwaysIncrement)
    {
        // if the input is faulty and the gate buggy, we can assume the output may be non-buggy.
        if (buggyNeverIncrement || buggyAlwaysIncrement)
        {
            if (digitOne < 0)
            {
                buggyNeverIncrement = false;
                buggyAlwaysIncrement = false;
                digitOne = -digitOne;
            }

            if (comparerBuggy)
            {
                buggyNeverIncrement = false;
                buggyAlwaysIncrement = false;
                if (comparer)
                {
                    comparer = false;
                } else
                {
                    comparer = true;
                }
            }
        }

        // when the borrower always increments, it always adds 10
        if (buggyAlwaysIncrement)
        {
            if (comparer)
            {
                // a negative digit represents a currently buggy digit
                return -digitOne - 10;
            } else
            {
                return digitOne + 10;
            }
        }
        // when the borrower never increments, it never adds 10
        if (buggyNeverIncrement)
        {
            if (!comparer)
            {
                return -digitOne;
            } else
            {
                return digitOne;
            }
        }

        // return the correct value if the program is not buggy, otherwise its negative for testing
        if (comparer)
        {
            return digitOne;
        } else
        {
            return digitOne + 10;
        }
    }

    private int differencer(int digitOne, int digitTwo, boolean buggy)
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

        // return the correct value if the program is not buggy, otherwise its negative for testing
        if (!buggy && !(digitOne < 0 || digitTwo < 0))
        {
            return Math.abs(digitOne) - Math.abs(digitTwo);
        } else
        {
            return -(Math.abs(digitOne) - Math.abs(digitTwo));
        }
    }

    private int zeroerFirstValue(int borrow, int digit, boolean buggyAlwaysTrue,
                                 boolean buggyAlwaysFalse)
    {
        // once again, if input is faulty and gate is buggy, it may return a correct value
        if (buggyAlwaysFalse || buggyAlwaysTrue)
        {
            zeroerFirstBuggy = true;
            if (borrowerFirstBuggy)
            {
                buggyAlwaysFalse = false;
                buggyAlwaysTrue = false;
                borrow = (borrow + 1) % 2;
            }
            if (digit < 0)
            {
                buggyAlwaysFalse = false;
                buggyAlwaysTrue = false;
                digit = -digit;
            }
        }

        // the first value of the zeroer can either be true or false, so buggy means: always true
        // or always false
        if (buggyAlwaysTrue)
        {
            return 1;
        }

        if (buggyAlwaysFalse)
        {
            return 0;
        }

        if (borrow == 1 && digit == 0)
        {
            zeroerFirstBuggy = false;
            return 1;
        } else
        {
            zeroerFirstBuggy = false;
            return 0;
        }
    }

    private int zeroerSecondValue(int borrow, int digit, boolean buggy)
    {
        // if input is faulty and gate is buggy, it may return a correct value
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

        // handle a bug (i.e. do not decrement if necessary)
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

    private int orOperator(int orBooleanOne, int orBooleanTwo, boolean buggyAlwaysTrue,
                           boolean buggyAlwaysFalse)
    {
        // if input is faulty and gate is buggy, it may return a correct value
        if (buggyAlwaysFalse || buggyAlwaysTrue)
        {
            if (zeroerFirstBuggy)
            {
                buggyAlwaysFalse = false;
                buggyAlwaysTrue = false;
                orBooleanOne = (orBooleanOne + 1) % 2;
            }
            if (borrowerFirstBuggy)
            {
                buggyAlwaysFalse = false;
                buggyAlwaysTrue = false;
                orBooleanTwo = (orBooleanTwo + 1) % 2;
            }
        }

        // handle bugs
        if (buggyAlwaysFalse)
        {
            return 0;
        }

        if (buggyAlwaysTrue)
        {
            return 1;
        }

        // return the correct value
        if (orBooleanOne == 1 || orBooleanTwo == 1)
        {
            return 1;
        } else
        {
            return 0;
        }
    }
}
