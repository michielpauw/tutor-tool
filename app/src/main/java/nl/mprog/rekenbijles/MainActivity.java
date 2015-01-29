package nl.mprog.rekenbijles;

/**
 * Created by Michiel Pauw on 08/01/15.
 * A very simple MainActivity with a very simple ListView.
 */

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create a ListView which shows a list with the possible manipulations
        String[] manipulations = this.getResources().getStringArray(R.array.manipulations);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item,
                manipulations);
        setListAdapter(adapter);
        ListView l = this.getListView();
        TextView text = (TextView) this.findViewById(R.id.textView);
        text.setText("Welkom! Welke vorm van rekenen wil je testen?");
        l.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        Intent intent = new Intent(this, ProblemActivity.class);
        String manipulation = Integer.toString(1);
        intent.putExtra("manipulation", manipulation);
        startActivity(intent);
    }
}
