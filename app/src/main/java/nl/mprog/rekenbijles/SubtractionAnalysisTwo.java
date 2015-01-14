package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 12/01/15.
 */

/**
 *
 *
 * WORK IN PROGRESS
 *
 *
 */
public class SubtractionAnalysisTwo {

    private static int[] problems;
    private static int[][] answers;
    private static String[] bugs = new String[20];
    private static int lengthOne;
    private static int lengthTwo;
    private static int lengthAnswer;

    private static int[] digitsProblemOne;
    private static int[] digitsProblemTwo;
    private static int[] digitsCorrectAnswer;
    private static int[] answerProvided;

    private static boolean comparerOne;
    private static int[] borrowerOne;
    private static int differencerFour;
    private static int[] zeroerOne;
    private static boolean comparerTwo;
    private static int[] borrowerTwo;
    private static int differencerThree;
    private static int orOne;
    private static int differencerOne;
    private static int differencerTwo;

    public SubtractionAnalysisTwo(int[] problems_in, int[][] answers_in)
    {
        problems = problems_in;
        answers = answers_in;
    }

//    public int[] analyze()
//    {
//        return bugs;
//    }

    public int[] process()
    {
        for (int i = 0; i < answers.length; i++) {
            digitsProblemOne = Tools.numberBreaker(problems[2 * i]);
            digitsProblemTwo = Tools.numberBreaker(problems[2 * i + 1]);
            digitsCorrectAnswer = Tools.numberBreaker(problems[2 * i] - problems[2 * i + 1]);
            answerProvided = answers[i];
            lengthOne = digitsProblemOne.length - 1;
            lengthTwo = digitsProblemTwo.length - 1;

            // doing subtraction step by step, to get expected outcome of each gate
            comparerOne = comparer(digitsProblemOne[lengthOne], digitsProblemTwo[lengthTwo]);
            borrowerOne = borrower(comparerOne, digitsProblemOne[lengthOne]);
            differencerFour = differencer(borrowerOne[1], digitsProblemTwo[lengthTwo]);
            int[] answerAlgorithm = new int[3];
            answerAlgorithm[2] = differencerFour;
            zeroerOne = zeroer(borrowerOne[0], digitsProblemOne[lengthOne - 1]);
            comparerTwo = comparer(zeroerOne[1], digitsProblemTwo[lengthTwo - 1]);
            borrowerTwo = borrower(comparerTwo, zeroerOne[1]);
            differencerThree = differencer(borrowerTwo[1], digitsProblemTwo[lengthTwo - 1]);
            answerAlgorithm[1] = differencerThree;
            orOne = orOperator(zeroerOne[0], borrowerTwo[0]);
            differencerOne = differencer(digitsProblemOne[lengthOne - 2], orOne);
            differencerTwo = differencer(differencerOne, digitsProblemTwo[lengthTwo - 2]);
            answerAlgorithm[0] = differencerTwo;
            return answerAlgorithm;
        }
        return answers[0];
    }

    public void backwardAnalysis()
    {
        int bugIndex = 0;
        if (answerProvided[1] != digitsCorrectAnswer[1]) {
            bugs[bugIndex] = "d_3";
            bugIndex += 1;
            int[][] difThreePossibleInput = supposeDifferencerCorrect(borrowerTwo[1],
                    digitsProblemTwo[lengthTwo - 1]);

        }
    }

    public int[][] supposeDifferencerCorrect(int correctInputOne, int correctInputTwo)
    {
        int[][] possibleWrongInput = {{correctInputOne, -1}, {-1, correctInputTwo}, {-1, -1}};
        return possibleWrongInput;
    }

    public int supposeDifferencerWrong()
    {
        return -1;
    }

    public int[][] supposeBorrowerCorrect(boolean correctInputOne, int correctInputTwo,
                                          int correctOutputOne, int correctOutputTwo)
    {
        if (correctOutputOne == 0) {
            int[][] possibleWrongInput = {{0, correctInputTwo}, {1, -1}, {0, -1}};
            return possibleWrongInput;
        } else {
            int[][] possibleWrongInput = {{1, correctInputTwo}, {0, -1}, {1, -1}};
            return possibleWrongInput;
        }
    }

    public int[][] supposeComparerCorrect(int correctInputOne, int correctInputTwo,
                                          boolean correctOutput)
    {
        if (correctOutput == true) {
            int[][] possibleWrongInput = new int[correctInputTwo][];
            for (int i = 0; i < correctInputTwo; i++) {
                int[] wrongInput = {i, correctInputTwo};
                possibleWrongInput[i] = wrongInput;
            }
            return possibleWrongInput;
        } else {
            int amount = 9 - correctInputTwo;
            int[][] possibleWrongInput = new int[amount][];
            for (int i = 0; i < amount; i++) {
                int[] wrongInput = {9 - i, correctInputTwo};
                possibleWrongInput[i] = wrongInput;
            }
            return possibleWrongInput;
        }
    }

    public boolean comparer(int digitOne, int digitTwo)
    {
        if (digitOne >= digitTwo) {
            return true;
        } else {
            return false;
        }
    }

    public int[] borrower(boolean comparer, int digitOne)
    {
        int[] toReturn = new int[2];
        if (comparer) {
            toReturn[0] = 0;
            toReturn[1] = digitOne;
            return toReturn;
        } else {
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
        int[] toReturn = new int[2];
        if (borrow == 1 && digit == 0) {
            toReturn[0] = 1;
            toReturn[1] = 9;
            return toReturn;
        } else if (borrow == 1) {
            toReturn[0] = 0;
            toReturn[1] = digit - 1;
            return toReturn;
        } else {
            toReturn[0] = 0;
            toReturn[1] = digit;
            return toReturn;
        }
    }

    public int orOperator(int orBooleanOne, int orBooleanTwo)
    {
        if (orBooleanOne == 1 || orBooleanTwo == 1) {
            return 1;
        } else {
            return 0;
        }
    }
}
