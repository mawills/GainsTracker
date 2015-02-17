package matthewwills.gainstracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class EntriesActivity extends ActionBarActivity {

    private ArrayList<Exercise> exerciseEntries = new ArrayList<>();
    ListView exerciseListView;
    String FILENAME = "exercise_object_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);


        //Open the Exercise ArrayList saved on internal storage.
        FileInputStream fis;
        try {
            fis = openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            exerciseEntries = (ArrayList) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Create a ListView and an adapter to give ArrayList to ListView
        exerciseListView = (ListView) findViewById(R.id.exerciseListViewXML);
        ArrayAdapter<Exercise> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, exerciseEntries);
        exerciseListView.setAdapter(adapter);


        //Set an OnItemClickListener to open a menu to delete or edit selected item from ListView.
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Create "Delete?" dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(EntriesActivity.this);
                builder
                        .setMessage("Delete entry?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Remove that item and save the updated ArrayList to internal storage.
                                exerciseEntries.remove(position);
                                FileOutputStream fos;
                                try {
                                    fos = openFileOutput(FILENAME,Context.MODE_PRIVATE);
                                    ObjectOutputStream ois = new ObjectOutputStream(fos);
                                    ois.writeObject(exerciseEntries);
                                    ois.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //Make the Activity refresh to show updated ListView
                                finish();
                                startActivity(getIntent());

                                Toast.makeText(getApplicationContext(), "Entry Deleted.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entries, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSettingsButtonClick(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), SettingsPageActivity.class));
    }
}