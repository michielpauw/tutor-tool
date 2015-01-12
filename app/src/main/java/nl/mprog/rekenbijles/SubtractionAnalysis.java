package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 12/01/15.
 */
public class SubtractionAnalysis {

    private static int[] problems;
    private static int[] answers;
    private static int[] bugs;
    private static int lengthOne;
    private static int lengthTwo;
    private static int lengthAnswer;

    public SubtractionAnalysis(int[] problems_in, int[] answers_in)
    {
        problems = problems_in;
        answers = answers_in;
    }

    public int[] analyze()
    {

        return bugs;
    }

    public void process()
    {
        for (int i = 0; i < answers.length; i++)
        {
            int[] digitsAnswer = Tools.numberBreaker(answers[i]);
            int[] digitsProblemOne = Tools.numberBreaker(problems[2 * i]);
            int[] digitsProblemTwo = Tools.numberBreaker(problems[2 * i + 1]);
            lengthOne = digitsProblemOne.length;
            lengthTwo = digitsProblemTwo.length;
            lengthAnswer = digitsAnswer.length;

            // doing subtraction step by step
            boolean comparerOne = comparer(digitsProblemOne[lengthOne], digitsProblemTwo[lengthTwo]);
            int[] borrowerOne = borrower(comparerOne, digitsProblemOne[lengthOne]);
            int differencerFour = differencer(digitsProblemTwo[lengthTwo], borrowerOne[1]);
            answers[lengthAnswer] = differencerFour;
            int[] zeroerOne = zeroer(borrowerOne[0], digitsProblemOne[lengthOne - 1]);
            boolean comparerTwo = comparer(zeroerOne[1], digitsProblemTwo[lengthTwo - 1]);
            int[] borrowerTwo = borrower(comparerTwo, zeroerOne[1]);
            int differencerThree = differencer(borrowerTwo[1], digitsProblemTwo[lengthTwo - 1]);
            answers[lengthAnswer - 1] = differencerThree;
            int orOne = orOperator(zeroerOne[0], borrowerTwo[0]);
            int differencerOne = differencer(digitsProblemOne[lengthOne - 2], orOne);
            int differencerTwo = differencer(differencerOne, digitsProblemTwo[lengthTwo - 2]);
            answers[lengthAnswer - 2] = differencerTwo;
        }
    }

    public boolean comparer(int digitOne, int digitTwo)
    {
        if (digitOne >= digitTwo)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public int[] borrower(boolean comparer, int digitOne)
    {
        int[] toReturn = new int[2];
        if (comparer)
        {
            toReturn[0] = 0;
            toReturn[1] = digitOne;
            return toReturn;
        }
        else
        {
            toReturn[0] = 1;
            toReturn[1] = digitOne + 10;
            return toReturn;
        }
    }

    public int differencer(int digitOne, int digitTwo)
    {
        return digitOne - digitTwo;
    }

    public int[] zeroer(int borrow, int digit)
    {
        int[] toReturn = new int[2]
        if (borrow == 1 && digit == 0)
        {
            toReturn[0] = 1;
            toReturn[1] = 9;
            return toReturn;
        }
        else if (borrow == 1)
        {
            toReturn[0] = 0;
            toReturn[1] = digit - 1;
            return toReturn;
        }
        else
        {
            toReturn[0] = 0;
            toReturn[1] = digit;
            return toReturn;
        }
    }

    public int orOperator(int orBooleanOne, int orBooleanTwo)
    {
        if (orBooleanOne == 1 || orBooleanTwo == 2)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
