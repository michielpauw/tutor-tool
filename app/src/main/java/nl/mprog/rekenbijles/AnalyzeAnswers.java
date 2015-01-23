package nl.mprog.rekenbijles;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by michielpauw on 08/01/15.
 * A class which delegates each manipulation to its supposed analysis.
 */
public class AnalyzeAnswers {

    private static int manipulation;
    private static int[][] answers;
    private static int[][] problems;
    private static ArrayList<int[]> bugs;
    private static int amountBugs = 13;
    private static ArrayList<int[]> uniqueBugs;
    private static ArrayList<Integer> occurrences;
    private static int[] occurrencesSorted;
    private static int totalAmountBugs;
    private static int[] indicesDecreasingRatio;


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


    /**
     * analysis will create a list of bugs and how often they occurred. It will call the Analysis
     * class corresponding to the manipulation.
     */
    public void analysis()
    {
        int amount = answers.length;
        int[][][] allBugs = new int[amount][][];

        totalAmountBugs = 0;
        for (int i = 0; i < amount; i++)
        {
            int[] problem = new int[2];
            problem = problems[i];
            SubtractionAnalysis subAnalysis = new SubtractionAnalysis(problem, answers[i]);
            subAnalysis.runAnalysis();
            bugs = subAnalysis.getBugs();
            int amountBugsProblem = bugs.size();
            for (int j = 0; j < amountBugsProblem; j++)
            {
                totalAmountBugs++;
                int[] bug = bugs.get(j);
                if (!alreadyAdded(bug))
                {
                    uniqueBugs.add(bug);
                    occurrences.add(1);
                } else
                {
                    int index = getIndex(bug, uniqueBugs);
                    int currentValue = occurrences.get(index);
                    occurrences.set(index, currentValue + 1);
                }
            }
        }
    }

    /**
     * Check whether a bug was already added to the uniqueBugs list.
     *
     * @param bug the bug that must be checked
     * @return true if the bug was already added, false if not
     */
    public boolean alreadyAdded(int[] bug)
    {
        boolean toReturn = false;
        int length = uniqueBugs.size();
        for (int i = 0; i < length; i++)
        {
            if (Arrays.equals(uniqueBugs.get(i), bug))
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<int[]> getBugsFound()
    {
        return uniqueBugs;
    }

    public ArrayList<Integer> getOccurrences()
    {
        return occurrences;
    }

    /**
     * @return a float array with the relative amount of times a bug occurred in the process.
     */
    public float[] getRatio()
    {
        int length = occurrences.size();
        float[] ratio = new float[length];
        for (int i = 0; i < length; i++)
        {
            ratio[i] = (float) occurrences.get(i) / totalAmountBugs;
        }

        setIndicesDecreasingRatio(ratio);
        float[] toReturn = new float[length];
        for (int i = 0; i < length; i++)
        {
            toReturn[i] = ratio[indicesDecreasingRatio[i]];
        }
        return toReturn;
    }

    /**
     * Gets the order of indices of the float array from high value to low value.
     *
     * @param ratio float array which needs to be sorted high to low
     */
    public void setIndicesDecreasingRatio(float[] ratio)
    {
        int length = occurrences.size();
        indicesDecreasingRatio = new int[length];
        boolean notSorted = true;
        int currentIndex = 0;
        float[] ratioCopy = new float[ratio.length];
        System.arraycopy(ratio, 0, ratioCopy, 0, ratio.length);
        while (notSorted)
        {
            int indexHighest = 0;
            float highest = 0;
            notSorted = false;
            for (int i = 0; i < length; i++)
            {
                if (ratioCopy[i] > highest)
                {
                    notSorted = true;
                    highest = ratioCopy[i];
                    indexHighest = i;
                }
            }
            if (notSorted)
            {
                indicesDecreasingRatio[currentIndex] = indexHighest;
                currentIndex++;
                ratioCopy[indexHighest] = 0;
            }

            if (currentIndex == length)
            {
                break;
            }
        }
        int i = 0;
    }

    /**
     * Sort the list of bugs by how often they have occurred.
     *
     * @return a sorted list of bugs
     */
    public int[][] getSortedBugs()
    {
        int length = uniqueBugs.size();
        int[][] sortedBugs = new int[length][];
        occurrencesSorted = new int[occurrences.size()];
        for (int i = 0; i < length; i++)
        {
            occurrencesSorted[i] = occurrences.get(indicesDecreasingRatio[i]);
            sortedBugs[i] = uniqueBugs.get(indicesDecreasingRatio[i]);
        }
        return sortedBugs;
    }

    public int[] getOccurrencesSorted()
    {
        return occurrencesSorted;
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

    // get the index of a specific bug that was already added.
    public int getIndex(int[] bug, ArrayList<int[]> bugList)
    {
        for (int i = 0; i < bugList.size(); i++)
        {
            int[] currentBug = bugList.get(i);
            if (Arrays.equals(currentBug, bug))
            {
                return i;
            }
        }

        return -1;
    }
}
