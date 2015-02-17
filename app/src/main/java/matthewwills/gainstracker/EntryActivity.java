package matthewwills.gainstracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class EntryActivity extends ActionBarActivity implements View.OnClickListener{

    private DateFormat format = DateFormat.getDateInstance();
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendarToSend = Calendar.getInstance();
    private EditText dateBox;
    private String[] liftNames;
    private Spinner spinner;
    EditText repsTextField;
    EditText weightTextField;
    Spinner liftTextField;
    EditText dateTextField;
    String FILENAME = "exercise_object_file";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        dateBox = (EditText) findViewById(R.id.editTextDate);
        dateBox.setOnClickListener(this);
        updateDate();

        //Set up the drop-down for lift name (to add or remove lifts, go to strings.xml)
        liftNames = getResources().getStringArray(R.array.exercise_name_list);
        spinner = (Spinner) findViewById(R.id.lift_name_spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,liftNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry, menu);
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

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            calendarToSend = calendar;
            updateDate();


        }
    };

    public void updateDate(){
        dateBox.setText(format.format(calendar.getTime()));
    }

    public void setDate(){
        new DatePickerDialog(EntryActivity.this,d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    @Override
    public void onClick(View v) {
        setDate();
    }

    public void onSettingsButtonClick(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), SettingsPageActivity.class));
    }

    public void onLogItButtonClick(View view) {
        repsTextField = (EditText) findViewById(R.id.editTextReps);
        weightTextField = (EditText) findViewById(R.id.editTextWeight);
        liftTextField = (Spinner) findViewById(R.id.lift_name_spinner);
        dateTextField = (EditText) findViewById(R.id.editTextDate);

        //Check for valid user input
        if (weightTextField.getText().toString().equals("")){
            weightTextField.setText("0");
        }

        if (repsTextField.getText().toString().equals("")){
            repsTextField.setText("0");
        }

        int reps = Integer.parseInt(repsTextField.getText().toString());
        int weight = Integer.parseInt(weightTextField.getText().toString());

        if((reps < 1 )||
                reps > 20 ||
                weight < 1 ||
                weight > 2000 ||
                repsTextField.length() == 0 ||
                weightTextField.length() == 0)
        {
            Toast.makeText(getApplicationContext(), "Invalid Entry",
                    Toast.LENGTH_SHORT).show();
        }

        //If user input is valid, continue
        else{
            Exercise exercise = new Exercise();
            ArrayList<Exercise> exerciseEntriesFromFile = new ArrayList<>();


            //Open the Exercise ArrayList saved on internal storage.
            FileInputStream fis;
            try {
                fis = openFileInput(FILENAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                exerciseEntriesFromFile = (ArrayList) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Take the data the user entered and assign it to a new Exercise object
            exercise.setDate(dateTextField.getText().toString());
            exercise.setLift(liftTextField.getSelectedItem().toString());
            exercise.setWeight(Integer.parseInt(weightTextField.getText().toString()));
            exercise.setReps(Integer.parseInt(repsTextField.getText().toString()));
            exercise.setCalendarDate(calendarToSend);

            //Add the new Exercise to the Exercise array list.
            exerciseEntriesFromFile.add(exercise);

            //Sort the ArrayList to be in chronological order.
            Collections.sort(exerciseEntriesFromFile);

            //Save the updated ArrayList to internal storage.
            FileOutputStream fos;
            try {
                fos = openFileOutput(FILENAME,Context.MODE_PRIVATE);
                ObjectOutputStream ois = new ObjectOutputStream(fos);
                ois.writeObject(exerciseEntriesFromFile);
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Toast.makeText(getApplicationContext(), "Exercise logged.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
