package nl.mprog.rekenbijles;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by michielpauw on 08/01/15.
 * A class which delegates each manipulation to its supposed analysis.
 */
public class Analysis {

    private static int manipulation;

    protected static int[][] problems;
    protected static ArrayList<int[]> bugs;
    private static int amountBugs = 13;
    private static ArrayList<int[]> uniqueBugs;
    private static ArrayList<Integer> occurrences;
    private static int[] occurrencesSorted;
    protected static int totalAmountBugs;
    private static int[] indicesDecreasingRatio;


    public Analysis(int manipulation_in, int[][] problems_in)
    {
        manipulation = manipulation_in;
        problems = problems_in;
        uniqueBugs = new ArrayList<int[]>();
        occurrences = new ArrayList<Integer>();
    }




    public ArrayList<Integer> getOccurrences()
    {
        return occurrences;
    }

    /**
     * handleBugs will create a list of bugs and how often they occurred. It will call the Analysis
     * class corresponding to the manipulation.
     */
    public void handleBugs(ArrayList<int[]> bugs)
    {
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
            if (bug.length > 1)
            {
                for (int k = 0; k < bug.length; k++)
                {
                    int[] singleEntry = new int[1];
                    singleEntry[0] = bug[k];
                    if (!alreadyAdded(singleEntry))
                    {
                        uniqueBugs.add(singleEntry);
                        occurrences.add(1);
                    } else
                    {
                        int index = getIndex(singleEntry, uniqueBugs);
                        int currentValue = occurrences.get(index);
                        occurrences.set(index, currentValue + 1);
                    }
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
}
