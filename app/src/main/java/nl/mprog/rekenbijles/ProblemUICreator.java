package nl.mprog.rekenbijles;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Michiel Pauw on 28/01/15.
 * This child of UICreator generates all the Layouts and Views that are used when problems
 * are shown.
 */
public class ProblemUICreator extends UICreator {

    private int blockAmount = 0;
    private int buttonHeight;
    private int buttonGap = 15;
    private int textViewHeight = 190;
    private int textViewWidth = 150;
    private int sdk = android.os.Build.VERSION.SDK_INT;
    private RelativeLayout problemLayout;
    private RelativeLayout topLayout;
    private RelativeLayout buttonLayout;
    private int position_hor_first;
    private int position_ver_first;

    public ProblemUICreator(Context context_in, Activity activity_in)
    {
        super(context_in, activity_in);
        createLayouts();
    }

    public RelativeLayout getTopLayout()
    {
        return topLayout;
    }

    public void createLayouts()
    {
        createTopLayout();
        createProblemLayout();
        createNumberButtonLayout();
    }

    // create the layout that is shown at the top of the screen
    private void createTopLayout()
    {
        topLayout = new RelativeLayout(activity);
        topLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary1));
        // set the properties for this layout
        int width = widthScr - separator;
        int height = heightScr / 6;
        RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams
                (width, height);
        topParams.topMargin = 0;
        topParams.leftMargin = 0;
        topLayout.setGravity(Gravity.CENTER_VERTICAL);
        // add the layout to the root layout
        root.addView(topLayout, topParams);
    }

    // creates a RelativeLayout on which the problems and answers can be shown
    private void createProblemLayout()
    {
        problemLayout = new RelativeLayout(activity);
        // set the properties for this layout
        problemLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary1));
        problemLayoutWidth = (widthScr / 3) * 2;
        problemLayoutHeight = heightScr / 2 - 2 * separator;
        RelativeLayout.LayoutParams problemParams = new RelativeLayout.LayoutParams
                (problemLayoutWidth, problemLayoutHeight);
        problemParams.topMargin = heightScr / 6 + separator;
        problemParams.leftMargin = widthScr / 8;
        problemLayout.setGravity(Gravity.CENTER_VERTICAL);
        // add the layout to the root layout
        root.addView(problemLayout, problemParams);
    }

    // creates the layout to which the number buttons will be added
    private void createNumberButtonLayout()
    {
        buttonLayout = new RelativeLayout(activity);
        // set the properties for this layout
        buttonLayout.setBackgroundColor(activity.getResources().getColor(R.color.primary1));
        int width = widthScr - separator;
        int height = heightScr / 3;
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams
                (width, height);
        buttonParams.topMargin = (heightScr / 3) * 2;
        buttonParams.leftMargin = 0;
        buttonLayout.setGravity(Gravity.CENTER_VERTICAL);
        // add the layout to the root layout
        root.addView(buttonLayout, buttonParams);
    }

    // create a TextView for each digit of the problem, so the digits are aligned in a nice way,
    // which makes the use more intuitive
    public void createTextView(int amount, int first, int second, int manipulation)
    {
        // blockAmount is currently fixed at three, but can also be determined by the amount of
        // digits
        blockAmount = amount;
        // break up the digits in arrays of length 3
        int[] firstNumberDigits = Utilities.numberBreaker(first, 0);
        int[] secondNumberDigits = Utilities.numberBreaker(second, 0);
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
            // create a new TextView for each digit
            TextView block = new TextView(activity);
            RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(width - 40,
                    height);
            paramsText.topMargin = position_ver_first + (i / amount) * height;
            paramsText.leftMargin = position_hor_first + (i % amount) * width;
            if (i < amount)
            {
                // create TextViews for the first number
                int length = firstNumberDigits.length;
                if (i + length >= amount)
                {
                    // enter each digit separately
                    block.setText(Integer.toString(firstNumberDigits[drawn_first]));
                    drawn_first++;
                }
            } else
            {
                // create TextViews for the second number
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
    private void createManipulationView(int width, int height, int amount, int manipulation)
    {
        // the separator is just a +-x/ sign, to let the user know which manipulation to use
        TextView separator = new TextView(activity);
        RelativeLayout.LayoutParams paramsTextSep = new RelativeLayout.LayoutParams(width, height);
        // align it correctly compared to the problem numbers shown
        paramsTextSep.topMargin = position_ver_first + height + 70;
        paramsTextSep.leftMargin = position_hor_first + width * amount;
        separator.setText(Utilities.getManipulationString(manipulation));
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
        // place these TextViews below the two problem numbers
        paramsText.topMargin = position_ver_first + 2 * textViewHeight;
        paramsText.leftMargin = position_hor_first + position * textViewWidth;
        answerView.setBackgroundColor(Color.TRANSPARENT);
        answerView.setGravity(Gravity.CENTER);
        answerView.setTextSize(50);
        answerView.setTypeface(null, Typeface.BOLD);
        answerView.setTextColor(activity.getResources().getColor(R.color.accent));

        // add the Views to the ProblemLayout
        problemLayout.addView(answerView, paramsText);
        return answerView;
    }

    // this method adds the background to the answer views, so that the alignment is correct
    public void addAnswerCircles(int amount)
    {
        DrawView circularButton = new DrawView(activity);
        // the type for a circle is 0
        circularButton.setType(0);
        // set all the parameters for a circle of the right size
        int radius = textViewWidth / 2;
        circularButton.setAmount(amount);
        circularButton.setWidth(textViewWidth);
        circularButton.setFirstX(position_hor_first + radius / 2 + 18);
        circularButton.setY(position_ver_first + 2 * textViewHeight + radius + 30);
        circularButton.setRadius(radius);
        problemLayout.addView(circularButton);
    }

    // creates a TextView in the top left corner which shows which number is currently selected
    public TextView addCurrentlySelected()
    {
        TextView currentlySelected = new TextView(activity);
        // set all the properties for this TextView
        RelativeLayout.LayoutParams currentlySelectedParams = new RelativeLayout.LayoutParams
                (textViewWidth, textViewHeight + 100);
        currentlySelectedParams.leftMargin = 50;
        currentlySelectedParams.topMargin = 0;
        currentlySelected.setTag("currentlySelected");
        currentlySelected.setText("0");
        currentlySelected.setTextSize(40);
        currentlySelected.setTypeface(null, Typeface.BOLD);
        currentlySelected.setTextColor(activity.getResources().getColor(R.color.accent));
        topLayout.addView(currentlySelected, currentlySelectedParams);
        // return the View so it can be changed from ProblemActivity
        return currentlySelected;
    }

    // adds the main control buttons, ten digits which can be clicked
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Button addNumberButton(int number)
    {
        buttonHeight = widthScr / 5 - 2 * buttonGap;
        RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(buttonHeight,
                buttonHeight);
        // we want numbers 1-5 on one line, 6-9 and 0 on the second
        int positionHorizontal = ((number + 9) % 5) * (buttonHeight + 10);
        int positionVertical = 0;
        if (number == 0 || number > 5)
        {
            positionVertical = buttonHeight + 10;
        }

        // create a circle to be placed as a background to the button
        GradientDrawable shape = new GradientDrawable();
        shape.setSize(buttonHeight, buttonHeight);
        shape.setCornerRadius(100);
        shape.setColor(activity.getResources().getColor(R.color.primary2));

        //set the properties for button
        paramsButton.topMargin = positionVertical;
        paramsButton.leftMargin = positionHorizontal;
        Button button = new Button(activity);
        button.setTextSize(35);
        button.setTypeface(null, Typeface.BOLD);
        button.setText(Integer.toString(number));
        button.setTextColor(activity.getResources().getColor(R.color.white));

        // place the circle as background (different for API levels below 8)
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
        {
            button.setBackgroundDrawable(shape);
        } else
        {
            button.setBackground(shape);
        }
        buttonLayout.addView(button, paramsButton);
        return button;
    }
}
