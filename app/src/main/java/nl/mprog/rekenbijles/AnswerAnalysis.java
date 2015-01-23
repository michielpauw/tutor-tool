package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 23/01/15.
 */
public class AnswerAnalysis extends Analysis{

    private static int[][] answers;

    public AnswerAnalysis(int manipulation_in, int answer_amount, int[][] problems_in)
    {
        super(manipulation_in, problems_in);
        answers = new int[answer_amount][];
    }


    // enter an answer array into the answers array, so I keep track of all answers
    public void enterAnswer(int[] answer, int position)
    {
        answers[position] = answer;
    }

    public void runAnalysis()
    {
        int amount = answers.length;
        int[][][] allBugs = new int[amount][][];

        totalAmountBugs = 0;
        for (int i = 0; i < amount; i++)
        {
            int[] problem = new int[2];
            problem = problems[i];
            SubtractionAnalysis subAnalysis = new SubtractionAnalysis(problem, answers[i]);
            subAnalysis.runAnalysis(4, true);
            bugs = subAnalysis.getBugs();
            handleBugs(bugs);
        }
    }

    /**
     * Creates a list of Strings describing all the bugs that occurred.
     *
     * @param bugs the bugs that need to be explained in words
     * @return a list of Strings that describe the bugs that occurred.
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
                String toAdd = getBugPartString(bug[j], 1);
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

    public String getBugPartString(int bug, int manipulation)
    {
        String[] possibleSubtractionBugs = new String[]
                {"de eerste vergelijker", "de boolean in de eerste lener", "het optellen van tien" +
                        " in de eerste lener", "de eerste aftrekker", "de boolean in de eerste" +
                        " nuller", "het verminderen van de waarde in de nuller", "de tweede" +
                        " vergelijker", "de boolean in de tweede lener", "het optellen van tien" +
                        " in de tweede lener", "de tweede aftrekker", "de of-manipulatie",
                        "de derde aftrekker", "de vierde aftrekker"};
        return possibleSubtractionBugs[bug];
    }
}
