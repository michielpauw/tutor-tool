package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 08/01/15.
 */
public class AnalyzeAnswers {

    private static int manipulation;
    private static int[][] answers;
    private static int[] problems;
    private static int[] bugs;

    public AnalyzeAnswers(int manipulation_in, int answer_amount, int[] problems_in)
    {
        manipulation = manipulation_in;
        answers = new int[answer_amount][];
        problems = problems_in;
    }

    public void enterAnswer(int[] answer, int position)
    {
        answers[position] = answer;
    }
}
