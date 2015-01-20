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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class ProblemActivity extends ActionBarActivity implements View.OnClickListener {

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
    int blockAmount;
    AnalyzeAnswers analyze;
    int problemAmount = 3;

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
        analyze = new AnalyzeAnswers(manipulation, problemAmount, problems);

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
            currentProblem ++;
            RelativeLayout currentLayout = (RelativeLayout) this.findViewById(R.id.root_layout);
            currentLayout.removeAllViews();
            createInterface();
        } else
        {
            // if all problems have been shown
            analyze.enterAnswer(currentAnswer, currentProblem);
            int[][][] bugs = analyze.analysis();
            RelativeLayout currentLayout = (RelativeLayout) this.findViewById(R.id.root_layout);
            currentLayout.removeAllViews();
            TextView bugsView = new TextView(this);
            RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(400,
                    400);
            paramsText.topMargin = 50;
            paramsText.leftMargin = 50;
            currentLayout.addView(bugsView, paramsText);
            String bugsString = "";
            for (int i = 0; i < bugs.length; i++)
            {

                bugsString += Arrays.toString(bugs[i]) + "  ";

            }
            bugsView.setText(bugsString);
        }
    }

    // a method that creates the entire UI by calling methods from UICreator.
    public void createInterface()
    {
        blockAmount = interfaceCreator.blockAmount(problems[currentProblem][0],
                problems[currentProblem][1], manipulation);
        interfaceCreator.createProblemLayout();
        for (int i = 0; i < 10; i++)
        {
            numberButton = interfaceCreator.addNumberButton(i);
            numberButton.setTag(i);
            numberButton.setOnClickListener(this);
        }
        int widthScr = interfaceCreator.getDisplayWidth();
        continueButton = interfaceCreator.createButton("Verder", widthScr - 500, 0, 400, 150);
        continueButton.setTag(30);
        continueButton.setOnClickListener(this);
        interfaceCreator.createTextView(blockAmount, problems[currentProblem][0],
                problems[currentProblem][1]);

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
}
