package nl.mprog.rekenbijles;

/**
 * Created by michielpauw on 08/01/15.
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

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] manipulations = this.getResources().getStringArray(R.array.manipulations);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item,
                manipulations);

        setListAdapter(adapter);
        ListView l = this.getListView();
        l.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        Intent intent = new Intent(this, ProblemActivity.class);
        String manipulation = Integer.toString(position);
        intent.putExtra("manipulation", manipulation);
        startActivity(intent);
        finish();
    }
}
