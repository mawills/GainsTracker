package matthewwills.gainstracker;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.FileInputStream;
import java.io.ObjectInputStream;



public class MainActivity extends ActionBarActivity{

    String FILENAME = "exercise_object_file";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Open and close an empty file for first-time users.
        FileInputStream fis;
        try {
            fis = openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onNewExerciseButtonClick(View view) {
        startActivity(new Intent(getApplicationContext(), EntryActivity.class));
    }

    public void onViewProgressButtonClick(View view) {
        startActivity(new Intent(getApplicationContext(), GraphActivity.class));
    }

    public void onViewEntriesButtonClick(View view) {
        startActivity(new Intent(getApplicationContext(), EntriesActivity.class));
    }

    public void onSettingsButtonClick(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), SettingsPageActivity.class));
    }
}
