package nl.mprog.rekenbijles;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Michiel Pauw on 28/01/15.
 * This class creates the entire interface the user gets to see when all problems ar solved.
 */
public class MoreInfoUICreator extends UICreator {

    private RelativeLayout moreInfoLayout;
    private int heightMoreInfoLayout;
    private int widthMoreInfoLayout;
    private RelativeLayout bugLayout;
    private int heightBugLayout;
    private int widthBugLayout;
    private int yBugLayout;
    private int highlighted;
    private RelativeLayout bottomLayout;

    public MoreInfoUICreator(Context context_in, Activity activity_in)
    {
        super(context_in, activity_in);
    }

    public RelativeLayout getBottomLayout()
    {
        return bottomLayout;
    }

    // create a layout to which we add a histogram and more information
    public RelativeLayout addMoreInfoLayout()
    {
        moreInfoLayout = new RelativeLayout(activity);
        // set the properties for this layout
        heightMoreInfoLayout = heightScr / 3;
        widthMoreInfoLayout = widthScr - 70;
        moreInfoLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        RelativeLayout.LayoutParams problemParams = new RelativeLayout.LayoutParams
                (widthMoreInfoLayout, heightMoreInfoLayout);
        problemParams.leftMargin = 10;
        // add the layout to the root layout and return it so it can handle a touch event
        root.addView(moreInfoLayout, problemParams);
        return moreInfoLayout;
    }

    /**
     * create a layout to add a ListView to, where the problemBugsTotal will be shown.
     */
    public void addBugLayout()
    {
        bugLayout = new RelativeLayout(activity);
        // set the properties for this layout
        heightBugLayout = (heightScr / 9) * 4 - separator;
        widthBugLayout = widthScr - separator;
        yBugLayout = heightMoreInfoLayout + separator;
        bugLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        RelativeLayout.LayoutParams problemParams = new RelativeLayout.LayoutParams
                (widthBugLayout, heightBugLayout);
        problemParams.leftMargin = 10;
        problemParams.topMargin = yBugLayout;
        bugLayout.setGravity(Gravity.CENTER_VERTICAL);
        // add the layout to the root layout
        root.addView(bugLayout, problemParams);
    }

    public void addBottomLayout()
    {
        bottomLayout = new RelativeLayout(activity);
        // set the properties for this layout
        int height = heightScr / 9;
        int width = widthScr - separator;
        bugLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary1));
        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams
                (width, height);
        bottomParams.leftMargin = 10;
        bottomParams.topMargin = yBugLayout + heightBugLayout + separator;
        bugLayout.setGravity(Gravity.CENTER_VERTICAL);
        // add the layout to the root layout
        root.addView(bottomLayout, bottomParams);
    }

    // this method adds the background to the answer views, so that the alignment is correct
    public void addHistogram(float[] ratio, int highlighted_in)
    {
        highlighted = highlighted_in;
        // create a DrawView object, so we can draw the histogram bars
        DrawView histogramBars = new DrawView(activity);
        // type 1 implies we want to draw bars, not circles
        histogramBars.setType(1);
        histogramBars.initializeRectangle(widthMoreInfoLayout, heightMoreInfoLayout + 20, ratio,
                highlighted);
        int barWidth = histogramBars.getRectangleWidth();
        createHistogramLegend(barWidth, ratio);
        // add the View to moreInfoLayout
        moreInfoLayout.addView(histogramBars);
    }


    // create the numbers below the histogram, so the user knows to which bug they correspond
    private void createHistogramLegend(int barWidth, float[] ratio)
    {

        int y = heightMoreInfoLayout - 100;
        int amount = ratio.length;
        // there are as many numbers as there are bugs
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
            moreInfoLayout.addView(number, paramsText);
        }
    }

    // add a list in which there is a description of all the bugs, or a description how they
    // can be solved
    public AdapterView addListView(ArrayAdapter<String> adapter)
    {
        ListView bugList = new ListView(activity);
        bugList.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        bugList.setAdapter(adapter);
        RelativeLayout.LayoutParams problemParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bugList.setDividerHeight(10);
        bugLayout.addView(bugList, problemParams);
        // return the list, so its items can be clicked and it can handle an onTouch event
        return bugList;
    }

    // adds a simple TextView which shows the amount a specific bug has occurred
    public void addAmountBug(int amount)
    {
        TextView bugAmountView = new TextView(activity);
        RelativeLayout.LayoutParams bugAmountParams = new RelativeLayout.LayoutParams
                (widthBugLayout, heightBugLayout);
        bugAmountParams.leftMargin = (widthMoreInfoLayout / 5) * 3;
        bugAmountParams.topMargin = 30;
        bugAmountView.setText("Amount: " + Integer.toString(amount));
        bugAmountView.setTextSize(15);
        bugAmountView.setTextColor(activity.getResources().getColor(R.color.white));
        moreInfoLayout.addView(bugAmountView, bugAmountParams);
    }

    // add a view which shows problems in which a specific bug occurred
    public void addMoreInfo(ArrayList<int[]> problems, ArrayList<ArrayList<Integer>> occurrences,
                            int highlighted, int[][] bugs, RelativeLayout layout, ArrayList<int[]> answers)
    {
        // get the problems which involved a bug that was clicked
        ArrayList<Integer> problemsWithBug = occurrences.get(highlighted);
        // don't show all the problems, show them with a maximum of three
        int amountToShow = Math.min(problemsWithBug.size(), 3);
        int[] bug = bugs[highlighted];
        createHeader(layout, amountToShow);
        // for each problem, create a small TextView
        for (int i = 0; i < amountToShow; i++)
        {
            int length = problemsWithBug.size();
            int indexToShow = problemsWithBug.get(length - 1 - i);
            int[] problemToShow = problems.get(indexToShow);
            int[] answerToShow = answers.get(indexToShow);
            createMoreInfo(problemToShow, bug, i, amountToShow, layout, answerToShow);
        }
    }

    // create a header explaining what the user sees
    private void createHeader(RelativeLayout layout, int amount)
    {
        TextView header = new TextView(activity);
        header.setWidth(widthMoreInfoLayout);
        header.setHeight(heightMoreInfoLayout / 5);
        if (amount == 1)
        {
            header.setText("Probleem met deze bug");
        } else
        {
            header.setText("Problemen met deze bug");
        }
        header.setTextSize(20);
        header.setTypeface(Typeface.DEFAULT_BOLD);
        header.setGravity(Gravity.CENTER);
        header.setTextColor(activity.getResources().getColor(R.color.accent));
        layout.addView(header);
    }

    // create a small RelativeLayout to which a TextView with the problem can be added
    public void createMoreInfo(int[] problem, int[] bug, int index, int total,
                               RelativeLayout layout, int[] answer)
    {
        RelativeLayout moreInfoProblem = new RelativeLayout(activity);
        int width = widthMoreInfoLayout / total - 10;
        int height = (heightMoreInfoLayout / 5) * 4;
        RelativeLayout.LayoutParams moreInfoParams = new RelativeLayout.LayoutParams
                (width - 20, height);
        moreInfoParams.leftMargin = index * width + 10;
        moreInfoParams.topMargin = heightMoreInfoLayout / 5;

        moreInfoProblem.setBackgroundColor(activity.getResources().getColor(R.color.primary2));
        moreInfoProblem.setGravity(Gravity.CENTER);
        layout.addView(moreInfoProblem, moreInfoParams);
        // create a TextView containing the problem
        TextView smallTextView = createSmallTextViews(problem, answer);
        // add the TextView to the correct RelativeLayout
        moreInfoProblem.addView(smallTextView);
    }

    // create a TextView with in simple text the problem we want to show
    private TextView createSmallTextViews(int[] problem, int[] answer)
    {
        String answerString = Utilities.arrayToString(answer);
        TextView problemText = new TextView(activity);
        problemText.setGravity(Gravity.RIGHT);
        problemText.setTextColor(activity.getResources().getColor(R.color.accent));
        problemText.setTypeface(Typeface.DEFAULT_BOLD);
        String problemString = "";
        for (int i = 0; i < 2; i++)
        {
            int problemPart = problem[i];
            problemString += Integer.toString(problemPart) + "<br>";
        }
        String toAdd = "<font color=3FF8A80>" + problemString + "</font> <font color=#ffffff>" + answerString + "</font>";
        problemText.setText(Html.fromHtml(toAdd));
        return problemText;
    }
}
