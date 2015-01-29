package nl.mprog.rekenbijles;

import java.util.ArrayList;

/**
 * Created by Michiel Pauw on 23/01/15.
 * This class has only one goal at the moment: it runs an analysis for one specific problem,
 * checking the result with a specific bug. The runAnalysis method will be used in a later stage,
 * but is irrelevant for now. It can be used to improve the analytic quality of the application
 * by comparing the amount of times a specific bug could occur and the amount of times a bug
 * actually occurs.
 */
public class ProblemAnalysis extends Analysis {

    private ArrayList<int[]> bugs;

    public ProblemAnalysis(int manipulation_in, int[][] problems_in)
    {
        super(manipulation_in, problems_in);
    }

    // see which bugs will create a buggy answer with a specific problem
    public void runAnalysis()
    {
        int[] nullAnswer = new int[] {0, 0, 0};
        int amount = problems.length;
        int[][][] allBugs = new int[amount][][];

        // run the analysis for every problem
        for (int i = 0; i < amount; i++)
        {
            int[] problem = new int[2];
            problem = problems[i];
            SubtractionAnalysis subAnalysis = new SubtractionAnalysis(problem, nullAnswer, 3, false);
            subAnalysis.runAnalysis();
            // get all the bugs that could occur in this specific problem
            bugs = subAnalysis.getBugs();
            // add the bugs to a bug list
            handleBugs(bugs, i);
        }
    }

    // check whether a specific bug makes the result of a problem buggy
    public boolean runSpecificAnalysis(int[] bug)
    {
        int[] nullAnswer = new int[] {0, 0, 0};
        int[] problem = problems[0];
        SubtractionAnalysis subAnalysis = new SubtractionAnalysis(problem, nullAnswer, 1, false);
        boolean toReturn = subAnalysis.runSpecificAnalysis(bug);
        return toReturn;
    }
}
