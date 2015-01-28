package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 08/01/15.
 * An activity that shows an intuitive UI for solving arithmetic problems.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ProblemActivity extends ActionBarActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, View.OnTouchListener {

    Spinner spinner;
    Button numberButton;
    Button continueButton;
    int numberClicked = 0;
    TextView answerView;
    int[] currentAnswer;
    int currentProblem = 0;
    int[][] problems;
    int manipulation;
    UICreator interfaceCreator;
    AnswerAnalysis analyze;
    int problemAmount = 1;
    int blockAmount = 3;
    int[][] bugs;
    float[] ratio;
    String[] bugsString;
    ArrayList<Integer> occurrences;
    int highlighted;
    int amountOfSpecificBug;
    int[] occurrencesSorted;
    int[] problemOccurrences;
    TextView currentlySelected;
    int[][] problemBugs;
    RelativeLayout histogram;
    float xDown;
    float xUp;
    RelativeLayout moreInfo;
    int[][] occurrencesPerProblem;

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
        interfaceCreator = new UICreator(this.getApplicationContext(), this);

        // generate (ten) problems with the manipulation that was clicked
        ProblemGenerator generator = new ProblemGenerator(manipulation, problemAmount);
        problems = generator.generateNumbers();
        // create an AnalyzeAnswers object, so I can analyze the answers
        analyze = new AnswerAnalysis(manipulation, problemAmount, problems);

        createInterface();
    }

    @Override
    // all views that can be clicked have tags
    public void onClick(View v)
    {
        int clicked = (Integer) v.getTag();

        if (clicked >= 0 && clicked < 10)
        {
            // click on a number button
            numberClicked = clicked;
            currentlySelected.setText(Integer.toString(clicked));
        } else if (clicked < 30)
        {
            // click on an answer view
            answerView = (TextView) v.findViewById(clicked);
            answerView.setText(Integer.toString(numberClicked));
            int digit = clicked % 10;
            currentAnswer[digit] = numberClicked;

            v.invalidate();
        } else if (clicked == 101)
        {
            // TODO: create intent and go back to main
            // go back to main
        } else if (clicked == 102)
        {
            currentProblem = 0;
            ProblemGenerator generator = new ProblemGenerator(manipulation, problemAmount);
            problems = generator.createSpecificProblems(ratio, bugs);
            analyze.clearAnswerList();
            analyze.setProblems(problems);

            createInterface();

        } else if (currentProblem < problemAmount - 1)
        {
            // click on 'verder' button (get a new problem)
            analyze.enterAnswer(currentAnswer);
            currentProblem++;

            createInterface();
        } else
        {
            // if all problems have been shown
            analyze.enterAnswer(currentAnswer);
            analyze.runAnalysis();
            ratio = analyze.getRatio();
            bugs = analyze.getSortedBugs();
            bugsString = analyze.getBugsString(bugs);
            occurrencesSorted = analyze.getOccurrencesSorted();
            createHypothesisInterface();
            int i = 0;
        }
    }

    // a method that creates the entire UI by calling methods from UICreator.
    public void createInterface()
    {
        RelativeLayout currentLayout = (RelativeLayout) this.findViewById(R.id.root_layout);
        currentLayout.removeAllViews();

        interfaceCreator.createProblemLayout();

        // create all the number buttons
        for (int i = 0; i < 10; i++)
        {
            numberButton = interfaceCreator.addNumberButton(i);
            numberButton.setTag(i);
            numberButton.setOnClickListener(this);
        }
        int widthScr = interfaceCreator.getDisplayWidth();

        // create the continue button
        continueButton = interfaceCreator.createButton("Verder", widthScr - 500, 80, 400, 150);
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

    /**
     * Creates the entire interface that will be shown after all answers are provided.
     */
    public void createHypothesisInterface()
    {
        RelativeLayout currentLayout = (RelativeLayout) this.findViewById(R.id.root_layout);
        currentLayout.removeAllViews();

        // create a layout in which more info can be shown
        moreInfo = interfaceCreator.addMoreInfoLayout();
        moreInfo.setOnTouchListener(this);
        moreInfo.setVisibility(View.INVISIBLE);

        // create a layout to which a histogram of the occurrence of each bug can be shown
        histogram = interfaceCreator.addMoreInfoLayout();
        histogram.setOnTouchListener(this);

        interfaceCreator.addHistogram(ratio, highlighted);
        interfaceCreator.addAmountBug(amountOfSpecificBug);
        interfaceCreator.addBugLayout();
//        interfaceCreator.addMoreInfo(problems);

        // create two navigation buttons
        int widthScr = interfaceCreator.getDisplayWidth();
        int heightScr = interfaceCreator.getDisplayHeight();
        int yPosition = heightScr - 300;
        int widthButton = widthScr / 2 - 100;

        Button backButton = interfaceCreator.createButton("Terug",  10, yPosition, widthButton, 150);
        Button continueButton = interfaceCreator.createButton("Verder",  widthButton + 110, yPosition, widthButton, 150);

        backButton.setTag(101);
        continueButton.setTag(102);

        backButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);

        // create a list of Strings describing all the bugs
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.bug_list_item,
                bugsString);
        AdapterView adapterView = interfaceCreator.addListView(adapter);
        adapterView.setOnItemClickListener(this);
    }

    // highlight a bar in the histogram after its corresponding bug was clicked
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        highlighted = i;
        amountOfSpecificBug = occurrencesSorted[i];
        //TODO: make sure that when in more info view, it does not automatically switch to histogram.
        createHypothesisInterface();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            // get the x value of where the screen was clicked first
            xDown = motionEvent.getX();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            // get the x value of where the screen was released
            xUp = motionEvent.getX();
            // if the difference is greater than 100: change view
            if (Math.abs(xUp - xDown) > 100)
            {
                if (xUp < xDown)
                {
                    goRight();
                }
                else
                {
                    goLeft();
                }
            }

        }
        return false;
    }

    public void goLeft()
    {
        histogram.setVisibility(View.VISIBLE);
        moreInfo.setVisibility(View.INVISIBLE);
    }

    public void goRight()
    {
        histogram.setVisibility(View.INVISIBLE);
        moreInfo.setVisibility(View.VISIBLE);
    }


    /**
     * I do not use these methods, therefore I put them in the bottom of my code.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reken, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
