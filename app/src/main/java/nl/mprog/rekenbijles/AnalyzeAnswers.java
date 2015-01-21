package nl.mprog.rekenbijles;

import java.util.ArrayList;

/**
 * Created by michielpauw on 08/01/15.
 * A class which delegates each manipulation to its supposed analysis.
 */
public class AnalyzeAnswers {

    private static int manipulation;
    private static int[][] answers;
    private static int[][] problems;
    private static int[][] bugs;
    private static int amountBugs = 13;
    private static ArrayList<int[]> uniqueBugs;
    private static ArrayList<Integer> occurrences;

    public AnalyzeAnswers(int manipulation_in, int answer_amount, int[][] problems_in)
    {
        manipulation = manipulation_in;
        answers = new int[answer_amount][];
        problems = problems_in;
        uniqueBugs = new ArrayList<int[]>();
        occurrences = new ArrayList<Integer>();
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
            bugs = subAnalysis.getBugs();
            amount = bugs.length;
            for (int j = 0; j < amount; j++)
            {
                int[] bug = bugs[j];
                if (!uniqueBugs.contains(bug))
                {
                    uniqueBugs.add(bug);
                    occurrences.add(1);
                }
                else
                {
                    int index = getIndex(bug, uniqueBugs);
                    int currentValue = occurrences.get(index);
                    occurrences.set(index, currentValue + 1);
                }
            }
        }

        return allBugs;
    }

    public ArrayList<String> getBugsProblem(int[][] bugs)
    {
        int amount = bugs.length;
        ArrayList<String> namesBugs = new ArrayList<String>();
        for (int i = 0; i < amount; i++)
        {
            int[] bug = bugs[i];
            int lengthBug = bug.length;
            String currentBugEntry;
            for (int j = 0; j < lengthBug; j++)
            {
                String currentBugPart;

                getBugPart(bug[i], 1);
            }
        }
        return namesBugs;
    }

    public String getBugPart(int bug, int manipulation)
    {
        String[] possibleSubtractionBugs = new String[]
                {"the first comparer", "the boolean in the first borrower", "the adding of ten" +
                        "in the first borrower", "the first differencer", "the boolean in the" +
                        "first zeroer", "the decreasing of the value in the first zeroer", "the" +
                        "second comparer", "the boolean in the second borrower", "the adding of ten" +
                        "in the second borrower", "the second differencer", "the or manipulation",
                "the third differencer", "the fourth differencer"};
        return possibleSubtractionBugs[bug];
    }

    public int getIndex(int[] bug, ArrayList<int[]> bugList)
    {
        for (int i = 0; i < bugList.size(); i++)
        {
            int[] currentBug = bugList.get(i);
            if (currentBug.equals(bug))
            {
                return i;
            }
        }

        return -1;
    }
}
