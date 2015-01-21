package nl.mprog.rekenbijles;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by michielpauw on 20/01/15.
 */
public class IterationGates {
    private static ArrayList<Integer> indices;
    private static boolean finish;
    private static int currentN;
    private static ArrayList<Integer> gatesArray;
    private static int maxN = 4;

    public IterationGates(int amountGates)
    {
        indices = new ArrayList<Integer>();
        indices.add(0);
        finish = false;
        currentN = 1;
        createGatesArray(amountGates);
    }

    public void createGatesArray(int amountGates)
    {
        gatesArray = new ArrayList<Integer>();
        for (int i = 0; i < amountGates; i++)
        {
            gatesArray.add(i);
        }
    }

    public int[] nextToProbe()
    {
        int[] toReturn = new int[currentN];
        if (!finish)
        {
            toReturn = getGates();
            increment();
            return toReturn;
        }
        else
        {
            toReturn[0] = -2;
            return toReturn;
        }
    }

    public int[] getGates()
    {
        int[] toReturn = new int[currentN];
        for (int i = 0; i < currentN; i++)
        {
            toReturn[i] = gatesArray.get(indices.get(i));
        }
        return toReturn;
    }

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
        }
        else if (currentN < maxN)
        {
            currentN ++;
            appendAndReset();
        }
        else
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
        }
        else
        {
            return false;
        }
        return true;
    }

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
