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
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class SettingsPageActivity extends ActionBarActivity {

    ArrayList<Exercise> exerciseEntriesFromFile = new ArrayList<>();
    String FILENAME = "exercise_object_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings_page, menu);
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

    public void onDeleteButtonClick(View view) {

        //Create "Are you sure?" dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage(getString(R.string.delete_saved_data_dialog))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Open and clear the Exercise ArrayList saved on internal storage.
                        FileInputStream fis;
                        try {
                            fis = openFileInput(FILENAME);
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            exerciseEntriesFromFile.clear();
                            ois.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //Save the updated ArrayList to internal storage.
                        FileOutputStream fos;
                        try {
                            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                            ObjectOutputStream ois = new ObjectOutputStream(fos);
                            ois.writeObject(exerciseEntriesFromFile);
                            ois.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(getApplicationContext(), "Saved Data Cleared.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
