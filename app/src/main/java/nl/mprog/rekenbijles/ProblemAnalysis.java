package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 23/01/15.
 */
public class ProblemAnalysis extends Analysis{

    public ProblemAnalysis(int manipulation_in, int[][] problems_in)
    {
        super(manipulation_in, problems_in);
    }

    public void runAnalysis()
    {
        int[] nullAnswer = new int[] {0, 0, 0};
        int amount = problems.length;
        int[][][] allBugs = new int[amount][][];

        totalAmountBugs = 0;
        for (int i = 0; i < amount; i++)
        {
            int[] problem = new int[2];
            problem = problems[i];
            SubtractionAnalysis subAnalysis = new SubtractionAnalysis(problem, nullAnswer);
            subAnalysis.runAnalysis(3, false);
            bugs = subAnalysis.getBugs();
            handleBugs(bugs);
        }
    }
}
