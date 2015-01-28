package nl.mprog.rekenbijles;

import java.util.ArrayList;

/**
 * Created by michielpauw on 20/01/15.
 * <p/>
 * This class is responsible for finding tuples of gates which will be probed in the next
 * iteration.
 */
public class IterationGates {
    private ArrayList<Integer> indices;
    private boolean finish;
    private int currentN;
    private ArrayList<Integer> gatesArray;
    private int maxN;

    /**
     * @param amountGates is the amount of gates we are testing in total.
     */
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
    public void createGatesArray(int amountGates)
    {
        gatesArray = new ArrayList<Integer>();
        for (int i = 0; i < amountGates; i++)
        {
            gatesArray.add(i);
        }
    }

    /**
     * @return the next tuple of gates to be probed. If all combinations of gates (with a maximum
     * of 4-tuples) have been tested, it returns an array with the first entry -2, so the method
     * which calls it knows it's finished.
     */
    public int[] nextToProbe()
    {
        int[] toReturn = new int[currentN];
        if (!finish)
        {
            toReturn = getGates();
            increment();
            return toReturn;
        } else
        {
            toReturn[0] = -2;
            return toReturn;
        }
    }

    /**
     * @return an int[] of the gates to be probed, based on the current indices to probe.
     */
    public int[] getGates()
    {
        int[] toReturn = new int[currentN];
        for (int i = 0; i < currentN; i++)
        {
            toReturn[i] = gatesArray.get(indices.get(i));
        }
        return toReturn;
    }

    /**
     * Increments the indices in the correct way. If the last gate in GatesArray has been checked,
     * either an extra gate will be added, or the gate 'to the left' of this gate will be
     * incremented.
     */
    public void increment()
    {
        int currentIndex = indices.get(currentN - 1);
        if (currentIndex < gatesArray.size() - 1)
        {
            indices.set(currentN - 1, currentIndex + 1);
            return;
        }
        if (currentN > 1 && recIncrement(currentN - 2))
        {
            int indexLeft = indices.get(currentN - 2);
            indices.set(currentN - 1, indexLeft + 1);
        } else if (currentN < maxN)
        {
            currentN++;
            appendAndReset();
        } else
        {
            finish = true;
        }
    }

    /**
     * Adds another gate to be tested by adding an extra index. It resets all indices to their
     * supposed value.
     */
    public void appendAndReset()
    {
        indices.add(currentN - 1);
        for (int i = 0; i < currentN; i++)
        {
            indices.set(i, i);
        }
    }

    /**
     * @param currentIndex is the index in indices we are currently trying to increase.
     * @return if incrementing one of the existing indices has been successful, it will return true.
     * Otherwise a new index needs to be added, and the method will return false.
     * <p/>
     * Recursively tries to find an index which can be increased.
     */
    public boolean recIncrement(int currentIndex)
    {

        if (indices.get(currentIndex) < gatesArray.size() + currentIndex - currentN)
        {
            int currentValue = indices.get(currentIndex);
            indices.set(currentIndex, currentValue + 1);
            return true;
        }

        if (currentIndex > 0 && recIncrement(currentIndex - 1))
        {
            int indexLeft = indices.get(currentIndex - 1);
            indices.set(currentIndex, indexLeft + 1);
        } else
        {
            return false;
        }
        return true;
    }

    /**
     * @param toRemove is a gate tuple which turns out to be buggy and needs to be removed.
     *                 <p/>
     *                 Finds the index of a gate to be removed from the gatesArray.
     */
    public void remove(int[] toRemove)
    {
        if (currentN > gatesArray.size() - toRemove.length)
        {
            finish = true;
            return;
        }
        for (int i = 0; i < toRemove.length; i++)
        {
            int removeIndex = -1;
            int toRemoveValue = toRemove[i];
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

    /**
     * @param removeIndex the current index of the gate that needs to be removed.
     *                    <p/>
     *                    Remove an entry from gatesArray and change current indices accordingly.
     */
    public void removeOne(int removeIndex)
    {
        boolean check = true;
        while (check && !finish)
        {
            check = false;
            for (int i = 0; i < indices.size(); i++)
            {
                while (indices.get(i) == removeIndex)
                {
                    increment();
                    check = true;
                }
            }
        }
        if (finish)
        {
            return;
        }
        for (int i = 0; i < indices.size(); i++)
        {
            int index = indices.get(i);
            if (index > removeIndex)
            {
                indices.set(i, index - 1);
            }
        }
        gatesArray.remove(removeIndex);
        if (gatesArray.size() < maxN)
        {
            maxN = gatesArray.size();
        }
    }

}
