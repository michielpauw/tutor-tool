package nl.mprog.rekenbijles;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by michielpauw on 06/01/15.
 * In this class we create the entire UI of some activities. If a view should be responsive we
 * return the view type so an listener can be created in the activity.
 */
public class UICreator {
    protected Context context;
    protected Activity activity;
    protected int widthScr;
    protected int heightScr;
    protected RelativeLayout root;
    protected int problemLayoutWidth;
    protected int problemLayoutHeight;
    protected int separator = 70;


    public UICreator(Context context_in, Activity activity_in)
    {
        context = context_in;
        activity = activity_in;
        setDisplayMetrics();
        root = (RelativeLayout) activity.findViewById(R.id.root_layout);

        root.setBackgroundColor(activity.getResources().getColor(R.color.primary1));
    }

    // of course we need to know the metrics of the display
    protected void setDisplayMetrics()
    {
        // get the display height and width
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        heightScr = metrics.heightPixels;
        widthScr = metrics.widthPixels;
    }

    // create a general button
    public Button createButton(String string, RelativeLayout layout, int alignment)
    {
        RelativeLayout.LayoutParams paramsTopButton = new RelativeLayout.LayoutParams(400, 150);
        if (alignment == 0)
        {
            paramsTopButton.leftMargin = 0;
        }
        else
        {
            paramsTopButton.leftMargin = widthScr - 500;
        }
        //set the properties for button
        Button button = new Button(activity);
        button.setText(string);
        button.setTextColor(activity.getResources().getColor(R.color.white));
        button.setTextSize(20);
        button.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        layout.addView(button, paramsTopButton);

        return button;
    }
}
