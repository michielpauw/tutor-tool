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
    private Context context;
    private Activity activity;
    private int widthScr;
    private int heightScr;
    private RelativeLayout root;
    private int heightSpinner = 120;
    private int topMargin = 30;
    private int heightSeekBar = 60;
    private int blockAmount = 0;
    private String manipulationString;
    private int buttonHeight;
    private int buttonGap = 15;
    private int buttonInitPos = 2 * buttonGap;
    private int textViewHeight = 190;
    private int textViewWidth = 150;
    private int sdk = android.os.Build.VERSION.SDK_INT;
    private RelativeLayout problemLayout;
    private int problemLayoutWidth;
    private int problemLayoutHeight;
    private int position_hor_first;
    private int position_ver_first;
    private RelativeLayout histogramLayout;
    private int heightHistogramView;
    private int widthHistogramView;
    private RelativeLayout bugLayout;
    private int heightBugLayout;
    private int widthBugLayout;
    private int yBugLayout;
    private int highlighted;


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

    /**
     * Creates a +-/* sign, depending on which manipulation the user chooses to do.
     */
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

    // the AnswerView is a transparent TextView which can be updated by clicking on it
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

    /**
     * Adds a TextView which shows which number is currently selected.
     *
     * @return a TextView which will change if a number is clicked
     */
    public TextView addCurrentlySelected()
    {
        TextView currentlySelected = new TextView(activity);
        RelativeLayout.LayoutParams currentlySelectedParams = new RelativeLayout.LayoutParams
                (textViewWidth, textViewHeight + 100);
        currentlySelectedParams.leftMargin = 50;
        currentlySelectedParams.topMargin = 0;
        currentlySelected.setTag("currentlySelected");
        currentlySelected.setText("0");
        currentlySelected.setTextSize(70);
        currentlySelected.setTypeface(null, Typeface.BOLD);
        currentlySelected.setTextColor(activity.getResources().getColor(R.color.accent));
        root.addView(currentlySelected, currentlySelectedParams);
        return currentlySelected;
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
        button.setTextColor(activity.getResources().getColor(R.color.white));
        button.setTextSize(20);
        button.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        root.addView(button, paramsButton);

        return button;
    }

    /**
     * Create a layout to which we add a histogram.
     */
    public RelativeLayout addMoreInfoLayout()
    {
        heightHistogramView = heightScr / 3;
        widthHistogramView = widthScr - 70;
        histogramLayout = new RelativeLayout(activity);
        histogramLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        RelativeLayout.LayoutParams problemParams = new RelativeLayout.LayoutParams
                (widthHistogramView, heightHistogramView);
        problemParams.leftMargin = 10;
        root.addView(histogramLayout, problemParams);
        histogramLayout.setLongClickable(true);
        return histogramLayout;
    }

    // this method adds the background to the answer views, so that the alignment is correct
    public void addHistogram(float[] ratio, int highlighted_in)
    {
        highlighted = -1;
        highlighted = highlighted_in;
        DrawView histogramBars = new DrawView(activity);
        histogramBars.setType(1);
        histogramBars.initializeRectangle(widthHistogramView, heightHistogramView + 20, ratio,
                highlighted);
        int barWidth = histogramBars.getRectangleWidth();
        createHistogramLegend(barWidth, ratio);
        histogramLayout.addView(histogramBars);
    }


    /**
     * Create the numbers below the bars, so the user knows which bug they correspond with.
     *
     * @param barWidth the distance between each number
     * @param ratio    the relative height of each bar
     */
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
            number.setTextSize(10);
            number.setText(Integer.toString(i + 1));
            number.setTextColor(activity.getResources().getColor(R.color.white));
            number.setGravity(Gravity.CENTER);
            histogramLayout.addView(number, paramsText);
        }
    }

    /**
     * create a layout to add a ListView to, where the problemBugsTotal will be shown.
     */
    public void addBugLayout()
    {
        bugLayout = new RelativeLayout(activity);
        heightBugLayout = 2 * heightScr / 4 - 100;
        widthBugLayout = widthScr - 70;
        yBugLayout = heightHistogramView + 70;
        bugLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        RelativeLayout.LayoutParams problemParams = new RelativeLayout.LayoutParams
                (widthBugLayout, heightBugLayout);
        problemParams.leftMargin = 10;
        problemParams.topMargin = yBugLayout;
        root.addView(bugLayout, problemParams);
    }

    /**
     * Creates a list of all the problemBugsTotal that occurred.
     *
     * @param adapter the adapter containing all the Strings, describing all the problemBugsTotal
     * @return a ListView to which the activity can add an onItemClickListener
     */
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

    /**
     * Shows in the histogram how many times a specific bug occurred.
     *
     * @param amount is the amount a bug at index i occurred
     */
    public void addAmountBug(int amount)
    {
        TextView bugAmountView = new TextView(activity);
        RelativeLayout.LayoutParams bugAmountParams = new RelativeLayout.LayoutParams
                (widthBugLayout, heightBugLayout);
        bugAmountParams.leftMargin = (widthHistogramView / 5) * 3;
        bugAmountParams.topMargin = 30;
        bugAmountView.setText("Amount: " + Integer.toString(amount));
        bugAmountView.setTextSize(15);
        bugAmountView.setTextColor(activity.getResources().getColor(R.color.white));
        histogramLayout.addView(bugAmountView, bugAmountParams);
    }
}
