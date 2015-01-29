package nl.mprog.rekenbijles;

import java.util.ArrayList;

/**
 * Created by Michiel Pauw on 20/01/15.
 * This class is responsible for finding tuples of gates which will be probed in the next
 * iteration.
 *
 */
public class IterationGates {
    private ArrayList<Integer> indices;
    private boolean finish;
    private int currentN;
    private ArrayList<Integer> gatesArray;
    private int maxN;

    // enter the amount of gates that should be probed in total and the max tuple generated
    public IterationGates(int amountGates, int maxNIn)
    {
        indices = new ArrayList<Integer>();
        indices.add(0);
        finish = false;
        currentN = 1;
        maxN = maxNIn;
        createGatesArray(amountGates);
    }

    // create an initial ArrayList of gates which still need to be probed.
    private void createGatesArray(int amountGates)
    {
        gatesArray = new ArrayList<Integer>();
        for (int i = 0; i < amountGates; i++)
        {
            gatesArray.add(i);
        }
    }

    // if not finished, return an array of the tuple that should be probed by the getProbes method
    public int[] nextToProbe()
    {
        int[] toReturn = new int[currentN];
        if (!finish)
        {
            // when not finished, get the current tuple to be probed and increment the indices
            toReturn = getGates();
            increment();
            return toReturn;
        } else
        {
            // when finished: return a tuple where the first entry is -2
            toReturn[0] = -2;
            return toReturn;
        }
    }

    // based on the current indices, return an array of the gates that should be probed
    private int[] getGates()
    {
        int[] toReturn = new int[currentN];
        for (int i = 0; i < currentN; i++)
        {
            toReturn[i] = gatesArray.get(indices.get(i));
        }
        return toReturn;
    }

     // increments the indices in the correct way
    private void increment()
    {
        int currentIndex = indices.get(currentN - 1);
        if (currentIndex < gatesArray.size() - 1)
        {
            // the right most index can still be incremented, so that will suffice
            indices.set(currentN - 1, currentIndex + 1);
            return;
        }
        if (currentN > 1 && recIncrement(currentN - 2))
        {
            // a different index (not the right most) can still be incremented
            int indexLeft = indices.get(currentN - 2);
            indices.set(currentN - 1, indexLeft + 1);
        } else if (currentN < maxN)
        {
            // all indices are at their max, a new index should be entered in the indices array
            currentN++;
            appendAndReset();
        } else
        {
            // all indices have been incremented to their max
            finish = true;
        }
    }

    // adds another gate to be tested by adding an extra index
    private void appendAndReset()
    {
        indices.add(currentN - 1);
        for (int i = 0; i < currentN; i++)
        {
            // reset all indices to a correct value
            indices.set(i, i);
        }
    }

    // checks whether any index can be incremented, if not: return false
    private boolean recIncrement(int currentIndex)
    {
        // check whether an entry of indices at currentIndex can be incremented
        if (indices.get(currentIndex) < gatesArray.size() + currentIndex - currentN)
        {
            int currentValue = indices.get(currentIndex);
            indices.set(currentIndex, currentValue + 1);
            // if so, increment and return true
            return true;
        }

        // otherwise, we decrease currentIndex and try again, as long as currentIndex is > 0
        if (currentIndex > 0 && recIncrement(currentIndex - 1))
        {
            int indexLeft = indices.get(currentIndex - 1);
            indices.set(currentIndex, indexLeft + 1);
        } else
        {
            // if this is not possible, the recursion is finished and we return false
            return false;
        }
        return true;
    }

    // removes a gate tuple from the gatesArray when it turned out to be buggy
    public void remove(int[] toRemove)
    {
        // if the size of probed array is larger than the gatesArray after removal, we are finished
        if (currentN > gatesArray.size() - toRemove.length)
        {
            finish = true;
            return;
        }
        // for each entry in the toRemove tuple, get the current index of that gate and remove it
        for (int i = 0; i < toRemove.length; i++)
        {
            int removeIndex = -1;
            int toRemoveValue = toRemove[i];
            // loop over the gatesArray to find the gate index
            for (int j = 0; j < gatesArray.size(); j++)
            {
                if (toRemoveValue == gatesArray.get(j))
                {
                    removeIndex = j;
                    break;
                }
            }
            removeOne(removeIndex);
        }
    }

    // remove one element from gatesArray and reset the indices to their correct value
    private void removeOne(int removeIndex)
    {
        boolean check = true;
        while (check && !finish)
        {
            check = false;
            for (int i = 0; i < indices.size(); i++)
            {
                while (indices.get(i) == removeIndex)
                {
                    // increment the indices until none of the removed values is still considered
                    increment();
                    check = true;
                }
            }
        }
        if (finish)
        {
            return;
        }
        // decrease the values of the indices for the indices that were higher than the ones removed
        for (int i = 0; i < indices.size(); i++)
        {
            int index = indices.get(i);
            if (index > removeIndex)
            {
                indices.set(i, index - 1);
            }
        }
        gatesArray.remove(removeIndex);
        // it is impossible to probe more gates than left in gatesArray, so reset maxN if fit
        if (gatesArray.size() < maxN)
        {
            maxN = gatesArray.size();
        }
    }

}
