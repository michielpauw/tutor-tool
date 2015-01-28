package nl.mprog.rekenbijles;

import java.util.ArrayList;

/**
 * Created by michielpauw on 23/01/15.
 * Child class of analysis, specialized in analysing the answers provided.
 */
public class AnswerAnalysis extends Analysis {

    private ArrayList<int[]> answers;
    private ArrayList<int[]> bugsSingle;

    public AnswerAnalysis(int manipulation_in, int answer_amount, int[][] problems_in)
    {
        super(manipulation_in, problems_in);
        answers = new ArrayList<int[]>();
    }

    public void clearAnswerList()
    {
        answers = new ArrayList<int[]>();
    }

    /**
     * Enter a given answer to answers list.
     *
     * @param answer the answer to be added
     */
    public void enterAnswer(int[] answer)
    {
        answers.add(answer);
    }

    /**
     * Given answers to the problems, analyze which bugs occur.
     */
    public void runAnalysis()
    {
        int amount = answers.size();
        int[][][] allBugs = new int[amount][][];

        occurrencesPerProblem = new ArrayList<int[]>();
        for (int i = 0; i < amount; i++)
        {
            int[] problem = problems[i];
            SubtractionAnalysis subAnalysis = new SubtractionAnalysis(problem, answers.get(i));
            subAnalysis.runAnalysis(4, true);
            bugsSingle = subAnalysis.getBugs();
            handleBugs(bugsSingle, i);
        }
    }

    /**
     * Creates a list of Strings describing all the problemBugsTotal that occurred.
     *
     * @param bugs the problemBugsTotal that need to be explained in words
     * @return a list of Strings that describe the problemBugsTotal that occurred.
     */
    public String[] getBugsString(int[][] bugs)
    {
        int amount = bugs.length;
        String[] namesBugs = new String[amount];
        for (int i = 0; i < amount; i++)
        {
            int[] bug = bugs[i];
            int lengthBug = bug.length;
            String currentBugEntry = " " + Integer.toString(i + 1) + ": ";
            for (int j = 0; j < lengthBug; j++)
            {
                String toAdd = getBugPartString(bug[j]);
                String temp = new String(currentBugEntry);
                currentBugEntry = temp + toAdd;
                String temp2 = new String(currentBugEntry);
                if (j < lengthBug - 1)
                {
                    currentBugEntry = temp2 + " en ";
                }
            }
            namesBugs[i] = currentBugEntry;
        }
        return namesBugs;
    }

    /**
     * Get a bit of string which describes a possible bug.
     *
     * @param bug the bug we want to describe
     * @return a bit of string describing the bug
     */
    public String getBugPartString(int bug)
    {
        String[] possibleSubtractionBugs = new String[]
                {"C1 altijd waar", "B1 altijd onwaar", "B1 voegt nooit tien toe",
                        "de eerste aftrekker", "de nuller altijd onwaar",
                        "het verminderen van de waarde in de nuller", "C2 altijd waar",
                        "B2 altijd onwaar", "B2 voegt nooit tien toe",
                        "de tweede aftrekker", "de of-manipulatie is altijd waar",
                        "de derde aftrekker", "de vierde aftrekker", "C1 altijd onwaar",
                        "B1 altijd " +
                        "waar", "Z altijd waar", "C2 altijd onwaar",
                        "B2 altijd waar", "B1 voegt altijd tien toe",
                        "B1 voegt altijd tien toe", "de of-operator is" +
                        "nooit waar"};
        return possibleSubtractionBugs[bug];
    }
}
