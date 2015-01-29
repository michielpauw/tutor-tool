package nl.mprog.rekenbijles;

/**
 * Created by Michiel Pauw on 08/01/15.
 * An activity that shows an intuitive UI for solving arithmetic problems, and when they're solved
 * it shows a view providing information about the bugs that occurred.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProblemActivity extends ActionBarActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, View.OnTouchListener {

    private Button numberButton;
    private Button continueButton;
    private int numberClicked = 0;
    private TextView answerView;
    private int[] currentAnswer;
    private int currentProblem = 0;
    private int[][] problems;
    private ArrayList<int[]> allProblems;
    private int manipulation;
    private ProblemUICreator interfaceCreator;
    private AnswerAnalysis analyze;
    private int problemAmount = 10;
    private int blockAmount = 3;
    private int[][] bugs;
    private float[] ratio;
    private String[] bugsString;
    private int highlighted;
    private int amountOfSpecificBug;
    private int[] occurrencesSorted;
    private TextView currentlySelected;
    private RelativeLayout histogram;
    private float xDown;
    private float xUp;
    private RelativeLayout moreInfo;
    private ArrayList<ArrayList<Integer>> occurrencesPerProblem;
    private ProblemGenerator generator;
    private boolean histogramVisible;
    private AdapterView adapterView;
    private ArrayAdapter<String> adapterHelp;
    private ArrayAdapter<String> adapterBugs;
    private boolean bugListVisible;
    private TextView amountBug;
    MoreInfoUICreator moreInfoUICreator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reken);

        // get the intent from PuzzleClicked
        Intent intent = getIntent();

        // get the manipulation that was clicked
        manipulation = Integer.parseInt(intent.getStringExtra("manipulation"));

        // create an interfaceCreator which will create the interface dynamically
        interfaceCreator = new ProblemUICreator(this.getApplicationContext(), this);

        // generate (ten) problems with the manipulation that was clicked
        generator = new ProblemGenerator(manipulation, problemAmount);
        problems = generator.generateNumbers();
        allProblems = generator.getAllProblems();
        // create an AnalyzeAnswers object, so I can analyze the answers
        analyze = new AnswerAnalysis(manipulation, problemAmount, problems, this);

        histogramVisible = true;
        bugListVisible = true;

        createInterface();
    }

    @Override
    public void onClick(View v)
    {
        // all views that can be clicked have tags
        int clicked = (Integer) v.getTag();

        // between 0 and 10 means a number button was clicked
        if (clicked >= 0 && clicked < 10)
        {
            // click on a number Button
            numberClicked = clicked;
            currentlySelected.setText(Integer.toString(clicked));
        }
        // the AnswerViews have a tag between 10 and 13
        else if (clicked < 30)
        {
            // click on an answer View
            answerView = (TextView) v.findViewById(clicked);
            answerView.setText(Integer.toString(numberClicked));
            int digit = clicked % 10;
            currentAnswer[digit] = numberClicked;
            // reset the answer View so text becomes visible
            v.invalidate();
        }
        // the back button was pressed in the hypothesis screen
        else if (clicked == 101)
        {
            // go back to main
            super.onBackPressed();
        }
        // the 'verder' button was pressed in the hypothesis screen
        else if (clicked == 102)
        {
            currentProblem = 0;
            // create new, bug specific problems
            problems = generator.createSpecificProblems(ratio, bugs);
            analyze.clearAnswerList();
            analyze.setProblems(problems);
            // reset the interface to the problems
            createInterface();

        }
        // if the 'verder' button was clicked and there are new problems left, we show a problem
        else if (currentProblem < problemAmount - 1)
        {
            // click on 'verder' button (get a new problem)
            analyze.enterAnswer(currentAnswer);
            currentProblem++;

            createInterface();
        }
        // if all problems have been shown, we can start the analysis
        else
        {
            analyze.enterAnswer(currentAnswer);
            startAnalysis();
        }
    }

    // do analysis and obtain more information about the bugs that were encountered
    private void startAnalysis()
    {
        allProblems = generator.getAllProblems();
        // run the analysis with the answers that were provided
        analyze.runAnalysis();
        // get the ratio of the bugs that occurred
        ratio = analyze.getRatio();
        // get the bugs that occurred, sorted by occurrence
        bugs = analyze.getSortedBugs();
        // get a string describing the bugs
        bugsString = analyze.getBugsString(bugs);
        // get the occurrences per bug
        occurrencesSorted = analyze.getOccurrencesSorted();
        occurrencesPerProblem = analyze.getOccurrencesPerProblemSorted();
        // if bugs occurred, create a View which shows the analysis
        if (ratio.length > 0)
        {
            createHypothesisInterface();
        }
        // otherwise we generate new problems and show those
        else
        {
            currentProblem = 0;
            problems = generator.generateNumbers();
            analyze.clearAnswerList();
            analyze.setProblems(problems);

            createInterface();
        }
    }

    // a method that creates the entire UI by calling methods from UICreator.
    private void createInterface()
    {
        RelativeLayout currentLayout = (RelativeLayout) this.findViewById(R.id.root_layout);
        currentLayout.removeAllViews();

        interfaceCreator.createLayouts();

        // create all the number buttons
        for (int i = 0; i < 10; i++)
        {
            numberButton = interfaceCreator.addNumberButton(i);
            numberButton.setTag(i);
            numberButton.setOnClickListener(this);
        }

        // create the continue button
        RelativeLayout topLayout = interfaceCreator.getTopLayout();
        continueButton = interfaceCreator.createButton("Verder", topLayout, 1);
        continueButton.setTag(30);
        continueButton.setOnClickListener(this);

        // create the textView showing the problem
        interfaceCreator.createTextView(blockAmount, problems[currentProblem][0],
                problems[currentProblem][1], manipulation);

        // add a TextView which shows which number is currently selected
        currentlySelected = interfaceCreator.addCurrentlySelected();
        currentlySelected.setText(Integer.toString(numberClicked));

        // add the circles to which the answers can be added
        currentAnswer = new int[blockAmount];
        interfaceCreator.addAnswerCircles(blockAmount);
        for (int i = 0; i < blockAmount; i++)
        {
            answerView = interfaceCreator.createAnswerView(blockAmount, i);
            answerView.setTag(i + 10);
            answerView.setId(i + 10);
            answerView.setOnClickListener(this);
        }
    }

    //Creates the entire interface that will be shown after all answers are provided.
    private void createHypothesisInterface()
    {
        RelativeLayout currentLayout = (RelativeLayout) this.findViewById(R.id.root_layout);
        currentLayout.removeAllViews();

        moreInfoUICreator = new MoreInfoUICreator(this.getApplicationContext(),
                this);

        createTopHypothesis();
        createBottomHypothesis();
    }

    private void createTopHypothesis()
    {
        // create a layout in which more info can be shown
        moreInfo = moreInfoUICreator.addMoreInfoLayout();
        moreInfo.setOnTouchListener(this);

        // create a layout to which a histogram of the occurrence of each bug can be shown
        histogram = moreInfoUICreator.addMoreInfoLayout();
        histogram.setOnTouchListener(this);

        // make sure that when histogram view is in the foreground, it stays there
        if (histogramVisible)
        {
            moreInfo.setVisibility(View.INVISIBLE);
            histogram.setVisibility(View.VISIBLE);
        } else
        {
            moreInfo.setVisibility(View.VISIBLE);
            histogram.setVisibility(View.INVISIBLE);
        }

        moreInfoUICreator.addHistogram(ratio, highlighted);
        amountBug = moreInfoUICreator.addAmountBug(occurrencesSorted[highlighted]);

        ArrayList<int[]> allAnswers = analyze.getAllAnswers();
        moreInfoUICreator.addMoreInfo(allProblems, occurrencesPerProblem, highlighted, bugs,
                moreInfo, allAnswers);
    }

    private void createBottomHypothesis()
    {
        // create a list of Strings describing all the bugs
        moreInfoUICreator.addBugLayout();
        adapterBugs = new ArrayAdapter<String>(this, R.layout.bug_list_item,
                bugsString);
        adapterHelp = new ArrayAdapter<String>(this, R.layout.bug_list_item,
                analyze.getSuggestionStrings());
        if (bugListVisible)
        {
            adapterView = moreInfoUICreator.addListView(adapterBugs);
        } else
        {
            adapterView = moreInfoUICreator.addListView(adapterHelp);
        }
        adapterView.setOnItemClickListener(this);
        adapterView.setOnTouchListener(this);

        // create two navigation buttons
        moreInfoUICreator.addBottomLayout();
        RelativeLayout bottomLayout = moreInfoUICreator.getBottomLayout();
        Button backButton = moreInfoUICreator.createButton("Terug", bottomLayout, 0);
        Button continueButton = moreInfoUICreator.createButton("Verder", bottomLayout, 1);

        backButton.setTag(101);
        continueButton.setTag(102);

        backButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);

    }

    // highlight a bar in the histogram after its corresponding bug was clicked
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        highlighted = i;
        amountOfSpecificBug = occurrencesSorted[i];
        histogram.removeAllViews();
        moreInfo.removeAllViews();
        createTopHypothesis();
//        createHypothesisInterface();
    }

    // implement a swipe function on the histogram / more info view
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            // get the x value of where the screen was clicked first
            xDown = motionEvent.getX();
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            // get the x value of where the screen was released
            xUp = motionEvent.getX();
            // if the difference is greater than 100: change view
            if (Math.abs(xUp - xDown) > 100)
            {
                // if x is smaller at the end, the current view is swiped to the left
                if (xUp < xDown)
                {
                    // check whether it was the histogram View or the ListView that was swiped
                    if (view instanceof RelativeLayout)
                    {
                        goMoreInfo();
                    } else
                    {
                        goHelpList();
                    }
                } else
                {
                    if (view instanceof RelativeLayout)
                    {
                        goHistogram();
                    } else
                    {
                        goBugsDescription();
                    }
                }
            }

        }
        if (view instanceof RelativeLayout)
        {
            return true;
        } else
        {
            return false;
        }
    }

    // go to the histogram
    private void goHistogram()
    {
        histogram.setVisibility(View.VISIBLE);
        moreInfo.setVisibility(View.INVISIBLE);
        histogramVisible = true;
    }

    // go to the more info screen
    private void goMoreInfo()
    {
        histogram.setVisibility(View.INVISIBLE);
        moreInfo.setVisibility(View.VISIBLE);
        histogramVisible = false;
    }

    // go to the bugs description
    private void goBugsDescription()
    {
        adapterView.setAdapter(adapterBugs);
        bugListVisible = true;
    }

    // go to the help list
    private void goHelpList()
    {
        adapterView.setAdapter(adapterHelp);
        bugListVisible = false;
    }
}
