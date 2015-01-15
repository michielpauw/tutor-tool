package nl.mprog.rekenbijles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

    public DrawView(Context context)
    {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Paint paint = new Paint();
        // Use Color.parseColor to define HTML colors
        paint.setColor(Color.parseColor("#3F51B5"));
        x = xOriginal;
        for (int i = 0; i < amount; i++)
        {
            x = xOriginal + i * width;
            canvas.drawCircle(x, y, radius, paint);
        }

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

    public void setY(int yIn)
    {
        y = yIn;
    }
}
