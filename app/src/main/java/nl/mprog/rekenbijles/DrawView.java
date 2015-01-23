package nl.mprog.rekenbijles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by michielpauw on 09/01/15.
 * A class which basically draws to circles where the answers should be entered. A TextView with
 * background would either make the text too small, or not aligned properly.
 */
public class DrawView extends View {
    private static int x;
    private static int y;
    private static int radius;
    private static int amount;
    private static int width;
    private static int xOriginal;
    private static int xCurrentlySelected;
    private static int yCurrentlySelected;
    private static int type;
    private static int widthView;
    private static int heightView;
    private static float[] ratio;
    private static int rectangleWidth;
    private static int widthFull;
    private static int heightFull;
    private static int amountBugs;
    private static int highlighted;

    public DrawView(Context context)
    {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (type == 0)
        {
            Paint paint = new Paint();
            // Use Color.parseColor to define HTML colors
            paint.setColor(getResources().getColor(R.color.primary2));
            x = xOriginal;
            for (int i = 0; i < amount; i++)
            {
                x = xOriginal + i * width;
                canvas.drawCircle(x, y, radius, paint);
            }
        } else if (type == 1)
        {
            Paint paint = new Paint();
            for (int i = 0; i < amountBugs; i++)
            {
                paint.setColor(getResources().getColor(R.color.accent));
                if (i == highlighted)
                {
                    paint.setColor(getResources().getColor(R.color.primary1));
                }
                int rectangleHeight = getRectangleHeight(ratio[i], heightFull);
                y = heightFull - rectangleHeight + 40;
                x = 25 + rectangleWidth * i;

                canvas.drawRect(x, y, x + rectangleWidth - 10, heightFull, paint);

            }
        }
    }

    public void initializeRectangle(int widthView, int heightView, float[] ratio_in,
                                    int highlighted_in)
    {
        amountBugs = ratio_in.length;
        ratio = ratio_in;
        heightFull = heightView - 100;
        widthFull = widthView - 20;
        highlighted = highlighted_in;
        setRectangleWidth(amountBugs, widthFull);
    }

    // some setter methods which don't need much explanation
    public void setRadius(int radiusIn)
    {
        radius = radiusIn;
    }

    public void setAmount(int amountIn)
    {
        amount = amountIn;
    }

    public void setWidth(int widthIn)
    {
        width = widthIn;
    }

    public void setFirstX(int xIn)
    {
        x = xIn;
        xOriginal = xIn;
    }

    public void setXCurrentlySelected(int xIn)
    {
        xCurrentlySelected = xIn;
    }

    public void setYCurrentlySelected(int yIn)
    {
        yCurrentlySelected = yIn;
    }

    public void setY(int yIn)
    {
        y = yIn;
    }

    public void setType(int type_in)
    {
        type = type_in;
    }

    public void setRectangleWidth(int amountBugs, int widthFull)
    {
        rectangleWidth = (widthFull - 50) / amountBugs;
    }

    public int getRectangleWidth()
    {
        return rectangleWidth;
    }

    public int getRectangleHeight(float singleRatio, int heightFull)
    {
        return (int) (singleRatio * heightFull / ratio[0]);
    }
}
