package nl.mprog.rekenbijles;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Michiel Pauw on 08/01/15.
 * A class which delegates each manipulation to its supposed analysis.
 */
public class Analysis {
    protected int[][] problems;
    protected ArrayList<int[]> uniqueBugs;
    protected ArrayList<Integer> occurrences;
    private int[] occurrencesSorted;
    protected int totalAmountBugs;
    private int[] indicesDecreasingRatio;
    protected ArrayList<ArrayList<Integer>> occurrencesPerProblem;
    protected int problemNumber;
    protected ArrayList<ArrayList<Integer>> occurrencesPerProblemSorted;


    public Analysis(int manipulation_in, int[][] problems_in)
    {
        totalAmountBugs = 0;
        problems = problems_in;
        uniqueBugs = new ArrayList<int[]>();
        occurrences = new ArrayList<Integer>();
        occurrencesPerProblem = new ArrayList<ArrayList<Integer>>();
    }

    public void setProblems(int[][] problems_in)
    {
        problems = problems_in;
    }


    // handleBugs will create a list of problemBugsTotal and how often they occurred
    protected void handleBugs(ArrayList<int[]> bugs, int problem)
    {
        int amountBugsProblem = bugs.size();
        for (int j = 0; j < amountBugsProblem; j++)
        {
            totalAmountBugs++;
            ArrayList<Integer> problemsForThisBug = new ArrayList<Integer>();
            int[] bug = bugs.get(j);
            // check whether a bug already exists in this list
            if (!alreadyAdded(bug))
            {
                // if not, add them
                uniqueBugs.add(bug);
                occurrences.add(1);
                problemsForThisBug.add(problemNumber);
                occurrencesPerProblem.add(problemsForThisBug);

            } else
            {
                // if so, increase the occurrence of that bug
                int index = getIndex(bug, uniqueBugs);
                int currentValue = occurrences.get(index);
                occurrences.set(index, currentValue + 1);
                problemsForThisBug = occurrencesPerProblem.get(index);
                problemsForThisBug.add(problemNumber);
                occurrencesPerProblem.set(index, problemsForThisBug);

            }
        }
    }

    // check whether a specific bug was already added to the list
    private boolean alreadyAdded(int[] bug)
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

    // get the ratio of bugs that occurred to the total amount of bugs
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
        // sort them in decreasing order
        for (int i = 0; i < length; i++)
        {
            toReturn[i] = ratio[indicesDecreasingRatio[i]];
        }
        return toReturn;
    }

    // sort the indices in such a way that the most occurring bugs show up first
    public void setIndicesDecreasingRatio(float[] ratio)
    {
        int length = occurrences.size();
        indicesDecreasingRatio = new int[length];
        boolean notSorted = true;
        int currentIndex = 0;
        float[] ratioCopy = new float[ratio.length];
        System.arraycopy(ratio, 0, ratioCopy, 0, ratio.length);
        // loop until the list is completely sorted
        while (notSorted)
        {
            int indexHighest = 0;
            float highest = 0;
            notSorted = false;
            // check whether there is a value that is higher than the currently highest value
            for (int i = 0; i < length; i++)
            {
                // if so, set that as the highest
                if (ratioCopy[i] > highest)
                {
                    notSorted = true;
                    highest = ratioCopy[i];
                    indexHighest = i;
                }
            }
            // add it to the list of ordered indices
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

    // get the index of a bug that already occurred previously
    private int getIndex(int[] bug, ArrayList<int[]> bugList)
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


    // sort the list of problemBugsTotal by how often they have occurred
    public int[][] getSortedBugs()
    {
        int length = uniqueBugs.size();
        int[][] sortedBugs = new int[length][];
        occurrencesPerProblemSorted = new ArrayList<ArrayList<Integer>>();
        occurrencesSorted = new int[occurrences.size()];

        for (int i = 0; i < length; i++)
        {
            occurrencesSorted[i] = occurrences.get(indicesDecreasingRatio[i]);
            sortedBugs[i] = uniqueBugs.get(indicesDecreasingRatio[i]);
            occurrencesPerProblemSorted.add(occurrencesPerProblem.get(indicesDecreasingRatio[i]));
        }
        return sortedBugs;
    }

    public int[] getOccurrencesSorted()
    {
        return occurrencesSorted;
    }

}
