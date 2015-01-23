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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ProblemActivity extends ActionBarActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

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
    int problemAmount = 10;
    int blockAmount = 3;
    int[][] bugs;
    float[] ratio;
    String[] bugsString;
    ArrayList<Integer> occurrences;
    int highlighted;
    int moreInfo;
    int amountOfSpecificBug;
    int[] occurrencesSorted;
    TextView currentlySelected;

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
        } else if (currentProblem < problemAmount - 1)
        {
            // click on 'verder' button (get a new problem)
            analyze.enterAnswer(currentAnswer, currentProblem);
            currentProblem++;
            RelativeLayout currentLayout = (RelativeLayout) this.findViewById(R.id.root_layout);
            currentLayout.removeAllViews();
            createInterface();
        } else
        {
            // if all problems have been shown
            analyze.enterAnswer(currentAnswer, currentProblem);
            analyze.runAnalysis();
            ratio = analyze.getRatio();
            bugs = analyze.getSortedBugs();
            bugsString = analyze.getBugsString(bugs);
            occurrences = analyze.getOccurrences();
            occurrencesSorted = analyze.getOccurrencesSorted();
            createHypothesisInterface();
            int i = 0;
        }
    }

    // a method that creates the entire UI by calling methods from UICreator.
    public void createInterface()
    {
//        blockAmount = Tools.blockAmount(problems[currentProblem][0],
//                problems[currentProblem][1], manipulation);
        interfaceCreator.createProblemLayout();

        for (int i = 0; i < 10; i++)
        {
            numberButton = interfaceCreator.addNumberButton(i);
            numberButton.setTag(i);
            numberButton.setOnClickListener(this);
        }
        int widthScr = interfaceCreator.getDisplayWidth();
        continueButton = interfaceCreator.createButton("Verder", widthScr - 500, 80, 400, 150);
        continueButton.setTag(30);
        continueButton.setOnClickListener(this);
        interfaceCreator.createTextView(blockAmount, problems[currentProblem][0],
                problems[currentProblem][1], manipulation);

        currentAnswer = new int[blockAmount];
        interfaceCreator.addAnswerCircles(blockAmount);
        currentlySelected = interfaceCreator.addCurrentlySelected();
        currentlySelected.setText(Integer.toString(numberClicked));
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

        interfaceCreator.addHistogramLayout();
        interfaceCreator.addHistogram(ratio, highlighted);
        interfaceCreator.addAmountBug(amountOfSpecificBug);
        interfaceCreator.addBugLayout();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.bug_list_item,
                bugsString);
        AdapterView adapterView = interfaceCreator.addListView(adapter);
        adapterView.setOnItemClickListener(this);
        adapterView.setOnItemLongClickListener(this);
    }

    // highlight a bar in the histogram after its corresponding bug was clicked
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        highlighted = i;
        amountOfSpecificBug = occurrencesSorted[i];
        createHypothesisInterface();
    }

    // after a bug was clicked and held, a new view will be generated which provides more info
    // about the bug
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        moreInfo = i;
        createMoreInfoInterface();
        return false;
    }

    // the interface which will provide more info about a specific bug (still to be implemented)
    public void createMoreInfoInterface()
    {
        RelativeLayout currentLayout = (RelativeLayout) this.findViewById(R.id.root_layout);
        currentLayout.removeAllViews();
    }

}
