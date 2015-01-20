package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 08/01/15.
 * A class which delegates each manipulation to its supposed analysis.
 */
public class AnalyzeAnswers {

    private static int manipulation;
    private static int[][] answers;
    private static int[][] problems;
    private static int[] bugs;

    public AnalyzeAnswers(int manipulation_in, int answer_amount, int[][] problems_in)
    {
        manipulation = manipulation_in;
        answers = new int[answer_amount][];
        problems = problems_in;
    }

    // enter an answer array into the answers array, so I keep track of all answers
    public void enterAnswer(int[] answer, int position)
    {
        answers[position] = answer;
    }

    // a method which calls my test case subtraction analysis
    public int[][] testAnalysis()
    {
        int[] problem = new int[2];
        problem = problems[0];
        SubtractionAnalysis subAnalysis = new SubtractionAnalysis(problem, answers[0]);
        subAnalysis.runAnalysis();
        return subAnalysis.getBugs();
    }

    public int[][][] analysis()
    {
        int amount = answers.length;
        int[][][] allBugs = new int[amount][][];
        for (int i = 0; i < amount; i++)
        {
            int[] problem = new int[2];
            problem = problems[i];
            SubtractionAnalysis subAnalysis = new SubtractionAnalysis(problem, answers[i]);
            subAnalysis.runAnalysis();
            allBugs[i] = subAnalysis.getBugs();
        }
        return allBugs;
    }
}
