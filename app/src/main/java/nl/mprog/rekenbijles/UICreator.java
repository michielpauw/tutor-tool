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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by michielpauw on 06/01/15.
 * In this class we create the entire UI of some activities. If a view should be responsive we
 * return the view type so an listener can be created in the activity.
 */
public class UICreator {
    private static Context context;
    private static Activity activity;
    private static int widthScr;
    private static int heightScr;
    private static RelativeLayout root;
    private static int heightSpinner = 120;
    private static int topMargin = 30;
    private static int heightSeekBar = 60;
    private static int blockAmount = 0;
    private static String manipulationString;
    private static int buttonHeight;
    private static int buttonGap = 15;
    private static int buttonInitPos = 2 * buttonGap;
    private static int textViewHeight = 190;
    private static int textViewWidth = 150;
    private static int sdk = android.os.Build.VERSION.SDK_INT;
    private static RelativeLayout problemLayout;
    private static int problemLayoutWidth;
    private static int problemLayoutHeight;
    private static int position_hor_first;
    private static int position_ver_first;
    private static RelativeLayout histogramLayout;
    private static int heightHistogramView;
    private static int widthHistogramView;
    private static RelativeLayout bugLayout;
    private static int heightBugLayout;
    private static int widthBugLayout;
    private static int yBugLayout;
    private static int highlighted;


    public UICreator(Context context_in, Activity activity_in)
    {
        context = context_in;
        activity = activity_in;
        setDisplayMetrics();
        root = (RelativeLayout) activity.findViewById(R.id.root_layout);
        createProblemLayout();
        root.setBackgroundColor(activity.getResources().getColor(R.color.primary1));
    }

    // of course we need to know the metrics of the display
    public void setDisplayMetrics()
    {
        // get the display height and width
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        heightScr = metrics.heightPixels;
        widthScr = metrics.widthPixels;
    }

    // some getter methods which don't need much explanation
    public int getBlockAmount()
    {
        return blockAmount;
    }

    public int getDisplayWidth()
    {
        return widthScr;
    }

    public int getDisplayHeight()
    {
        return heightScr;
    }

    // creates a RelativeLayout on which the problems and answers can be shown
    public void createProblemLayout()
    {
        problemLayout = new RelativeLayout(activity);
        problemLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary1));
        problemLayoutWidth = (widthScr / 3) * 2;
        problemLayoutHeight = heightScr / 3;
        RelativeLayout.LayoutParams problemParams = new RelativeLayout.LayoutParams
                (problemLayoutWidth, problemLayoutHeight);
        problemParams.topMargin = heightScr / 4;
        problemParams.leftMargin = widthScr / 8;
        root.addView(problemLayout, problemParams);
    }

    // create a TextView for each digit of the problem, so the digits are aligned in a nice way,
    // which makes the use more intuitive
    public void createTextView(int amount, int first, int second, int manipulation)
    {
        blockAmount = amount;
        int[] firstNumberDigits = Tools.numberBreaker(first, 0);
        int[] secondNumberDigits = Tools.numberBreaker(second, 0);
        int width = textViewWidth;
        int height = textViewHeight;
        position_hor_first = (problemLayoutWidth - amount * width) / 2;
        position_ver_first = 0;
        int drawn_first = 0;
        int drawn_second = 0;

        createManipulationView(width, height, amount, manipulation);

        // draw the problems separately so they show up above each other
        for (int i = 0; i < 2 * amount; i++)
        {
            TextView block = new TextView(activity);
            RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(width - 40,
                    height);
            paramsText.topMargin = position_ver_first + (i / amount) * height;
            paramsText.leftMargin = position_hor_first + (i % amount) * width;
            if (i < amount)
            {
                int length = firstNumberDigits.length;
                if (i + length >= amount)
                {
                    block.setText(Integer.toString(firstNumberDigits[drawn_first]));
                    drawn_first++;
                }
            } else
            {
                int length = secondNumberDigits.length;
                if (i - amount + length >= amount)
                {
                    block.setText(Integer.toString(secondNumberDigits[drawn_second]));
                    drawn_second++;
                }
            }
            block.setTextSize(50);
            block.setTextColor(activity.getResources().getColor(R.color.white));
            block.setTypeface(null, Typeface.BOLD);
            block.setGravity(Gravity.CENTER);
            block.setId(i + 40);
            problemLayout.addView(block, paramsText);
        }
    }

    public void createManipulationView(int width, int height, int amount, int manipulation)
    {
        // the separator is just a +-x/ sign, to let the user know which manipulation to use
        TextView separator = new TextView(activity);
        RelativeLayout.LayoutParams paramsTextSep = new RelativeLayout.LayoutParams(width, height);
        paramsTextSep.topMargin = position_ver_first + height + 70;
        paramsTextSep.leftMargin = position_hor_first + width * amount;
        separator.setText(Tools.getManipulationString(manipulation));
        separator.setTextSize(70);
        separator.setTextColor(activity.getResources().getColor(R.color.accent));
        problemLayout.addView(separator, paramsTextSep);
    }

    // the AnswerView is a transparent TextView which can be updates by clicking on it
    public TextView createAnswerView(int amount, int position)
    {
        TextView answerView = new TextView(activity);
        RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(textViewWidth -
                40, textViewHeight);
        paramsText.topMargin = position_ver_first + 2 * textViewHeight;
        paramsText.leftMargin = position_hor_first + position * textViewWidth;
        answerView.setBackgroundColor(Color.TRANSPARENT);
        answerView.setGravity(Gravity.CENTER);
        answerView.setTextSize(50);
        answerView.setTypeface(null, Typeface.BOLD);
        answerView.setTextColor(activity.getResources().getColor(R.color.accent));

        problemLayout.addView(answerView, paramsText);
        return answerView;
    }

    // this method adds the background to the answer views, so that the alignment is correct
    public void addAnswerCircles(int amount)
    {
        DrawView circularButton = new DrawView(activity);
        circularButton.setType(0);
        int radius = textViewWidth / 2;
        circularButton.setAmount(amount);
        circularButton.setWidth(textViewWidth);
        circularButton.setFirstX(position_hor_first + radius / 2 + 18);
        circularButton.setY(position_ver_first + 2 * textViewHeight + radius + 30);
        circularButton.setRadius(radius);
        problemLayout.addView(circularButton);
    }

    // adds the main control buttons, ten digits which can be clicked
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Button addNumberButton(int number)
    {
        buttonHeight = widthScr / 5 - 2 * buttonGap;
        RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(buttonHeight,
                buttonHeight);
        int positionHorizontal = ((number + 9) % 5) * (buttonHeight + 10);
        int positionVertical = heightScr - 2 * buttonHeight;
        if (number > 0 && number < 6)
        {
            positionVertical = heightScr - 3 * buttonHeight - 10;
        }

        GradientDrawable shape = new GradientDrawable();
        shape.setSize(buttonHeight, buttonHeight);
        shape.setCornerRadius(100);
        shape.setColor(activity.getResources().getColor(R.color.primary2));

        paramsButton.topMargin = positionVertical;
        paramsButton.leftMargin = positionHorizontal;
        //set the properties for button
        Button button = new Button(activity);
        button.setTextSize(35);
        button.setTypeface(null, Typeface.BOLD);
        button.setText(Integer.toString(number));
        button.setId(number);
        button.setTextColor(activity.getResources().getColor(R.color.white));
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
        {
            button.setBackgroundDrawable(shape);
        } else
        {
            button.setBackground(shape);
        }
        root.addView(button, paramsButton);
        return button;
    }

    // create a general button
    public Button createButton(String string, int x, int y, int width, int height)
    {
        RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(width, height);
        paramsButton.topMargin = y;
        paramsButton.leftMargin = x;
        //set the properties for button
        Button button = new Button(activity);
        button.setText(string);
        root.addView(button, paramsButton);
        return button;
    }

    public void addHistogramView()
    {
        heightHistogramView = heightScr / 3;
        widthHistogramView = widthScr - 70;
        histogramLayout = new RelativeLayout(activity);
        histogramLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        RelativeLayout.LayoutParams problemParams = new RelativeLayout.LayoutParams
                (widthHistogramView, heightHistogramView);
        problemParams.leftMargin = 10;
        root.addView(histogramLayout, problemParams);
    }

    // this method adds the background to the answer views, so that the alignment is correct
    public void addHistogram(float[] ratio, int highlighted_in)
    {
        highlighted = -1;
        highlighted = highlighted_in;
        DrawView histogramBars = new DrawView(activity);
        histogramBars.setType(1);
        histogramBars.initializeRectangle(widthHistogramView, heightHistogramView + 20, ratio, highlighted);
        int barWidth = histogramBars.getRectangleWidth();
        createHistogramLegend(barWidth, ratio);
        histogramLayout.addView(histogramBars);
    }

    public void createHistogramLegend(int barWidth, float[] ratio)
    {

        int y = heightHistogramView - 100;
        int amount = ratio.length;
        for (int i = 0; i < amount; i++)
        {
            TextView number = new TextView(activity);
            RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(barWidth, 80);
            paramsText.topMargin = y;
            paramsText.leftMargin = 25 + barWidth * i;
            number.setTextSize(15);
            number.setText(Integer.toString(i + 1));
            number.setTextColor(activity.getResources().getColor(R.color.white));
            number.setGravity(Gravity.CENTER);
            histogramLayout.addView(number, paramsText);
        }
    }

    public void addBugLayout()
    {
        bugLayout = new RelativeLayout(activity);
        heightBugLayout = 2 * heightScr / 3 - 100;
        widthBugLayout = widthScr - 70;
        yBugLayout = heightHistogramView + 70;
        bugLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        RelativeLayout.LayoutParams problemParams = new RelativeLayout.LayoutParams
                (widthBugLayout, heightBugLayout);
        problemParams.leftMargin = 10;
        problemParams.topMargin = yBugLayout;
        root.addView(bugLayout, problemParams);
    }

    public AdapterView addListView(ArrayAdapter<String> adapter)
    {
        ListView bugList = new ListView(activity);
        bugList.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        bugList.setAdapter(adapter);
        RelativeLayout.LayoutParams problemParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        bugLayout.addView(bugList, problemParams);
        return bugList;
    }
}
