package nl.mprog.rekenbijles;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Michiel Pauw on 23/01/15.
 * Child class of analysis, specialized in analysing the answers provided.
 */
public class AnswerAnalysis extends Analysis {

    private ArrayList<int[]> answers;
    private ArrayList<int[]> bugsSingle;
    private Activity activity;
    private String[] suggestionStrings;
    private ArrayList<int[]> allAnswers;

    public AnswerAnalysis(int manipulation_in, int answer_amount, int[][] problems_in,
                          Activity activityIn)
    {
        super(manipulation_in, problems_in);
        answers = new ArrayList<int[]>();
        allAnswers = new ArrayList<int[]>();
        activity = activityIn;
    }

    public void clearAnswerList()
    {
        answers = new ArrayList<int[]>();
    }

    public ArrayList<int[]> getAllAnswers()
    {
        return allAnswers;
    }

    // enter an answer that was provided
    public void enterAnswer(int[] answer)
    {
        answers.add(answer);
        allAnswers.add(answer);
    }

    // given the answers that were provided, apply analysis
    public void runAnalysis()
    {
        int amount = answers.size();
        int[][][] allBugs = new int[amount][][];

        for (int i = 0; i < amount; i++)
        {
            int[] problem = problems[i];
            // run an analysis for this specific problem
            SubtractionAnalysis subAnalysis = new SubtractionAnalysis(problem, answers.get(i), 4, true);
            subAnalysis.runAnalysis();
            bugsSingle = subAnalysis.getBugs();
            handleBugs(bugsSingle, i);
            problemNumber++;
        }
    }


    public ArrayList<ArrayList<Integer>> getOccurrencesPerProblemSorted()
    {
        return occurrencesPerProblemSorted;
    }

    // create a String with a short description of the bug that occurred
    public String[] getBugsString(int[][] bugs)
    {
        int amount = bugs.length;
        String[] namesBugs = new String[amount];
        suggestionStrings = new String[amount];
        // for each bug that occurred we have a separate string
        for (int i = 0; i < amount; i++)
        {
            int[] bug = bugs[i];
            int lengthBug = bug.length;
            String suggestionStringsEntry = "";
            String currentBugEntry = Integer.toString(i + 1) + ": ";
            for (int j = 0; j < lengthBug; j++)
            {
                // I create a list of Strings describing the bugs
                String toAddBug = getBugPartString(bug[j]);
                String tempBug = new String(currentBugEntry);
                currentBugEntry = tempBug + toAddBug;
                String tempBug2 = new String(currentBugEntry);

                // but also a list of Strings providing help to solving the bugs
                String toAddHelp = "- " + getSuggestionString(bug[j]) + "\n";
                String tempHelp = new String(suggestionStringsEntry);
                suggestionStringsEntry = tempHelp + toAddHelp;
                // if a bug consists of multiple buggy gates, separate each bug
                if (j == lengthBug - 2)
                {
                    currentBugEntry = tempBug2 + " en ";
                } else if (j < lengthBug - 2)
                {
                    currentBugEntry = tempBug2 + ", ";
                }
            }
            suggestionStrings[i] = suggestionStringsEntry;
            namesBugs[i] = currentBugEntry;
        }
        return namesBugs;
    }

    // get a String which briefly describes the bug that occurred
    private String getBugPartString(int bug)
    {
        String[] possibleSubtractionBugs = activity.getResources().getStringArray(R.array.bugs);

        return possibleSubtractionBugs[bug];
    }

    // get a String which briefly describes the way a bug can be solved
    public String getSuggestionString(int bug)
    {
        String[] possibleSubtractionSuggestions = activity.getResources().getStringArray(R.array
                .help);
        return possibleSubtractionSuggestions[bug];
    }

    public String[] getSuggestionStrings()
    {
        return suggestionStrings;
    }
}
