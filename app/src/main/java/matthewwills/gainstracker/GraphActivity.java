package matthewwills.gainstracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;


public class GraphActivity extends ActionBarActivity {

    private ArrayList<Exercise> selectedExerciseArrayList = new ArrayList<>();
    private ArrayList<Exercise> exerciseArrayList;
    private Exercise selectedExerciseArray[];
    private String[] liftNames;
    private Spinner liftSelector;
    private String liftSelected;

    String FILENAME = "exercise_object_file";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //Set up and populate the drop down menu
        liftNames = getResources().getStringArray(R.array.exercise_name_list);
        liftSelector = (Spinner) findViewById(R.id.lift_name_spinner_graph_screen);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, liftNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        liftSelector.setAdapter(dataAdapter);

        //Open the Exercise ArrayList saved on internal storage and save it.
        FileInputStream fis;
        try {
            fis = openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            exerciseArrayList = (ArrayList) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Check for the user to click an exercise. Defaults to first item.
        liftSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Set the lift from the drop-down as the selected lift.
                liftSelected = (String)liftSelector.getSelectedItem();

                //Clear the ArrayList of any previous data.
                selectedExerciseArrayList.clear();

                //Populate the selectedArrayList with Exercises whose names match the selected lift.
                for (Exercise e : exerciseArrayList) {
                    if (e.getLift().equals(liftSelected)) {
                        selectedExerciseArrayList.add(e);
                    }
                }

                //Check if the ArrayList is empty. If so, display "No data" message. If not, make graph.
                if(selectedExerciseArrayList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "no data",
                            Toast.LENGTH_SHORT).show();
                    TextView noDataToDisplay = (TextView)findViewById(R.id.no_data_to_display_text);
                    GraphView noGraphToDisplay = (GraphView)findViewById(R.id.graph);
                    noDataToDisplay.setVisibility(View.VISIBLE);
                    noGraphToDisplay.setVisibility(View.GONE);
                }
                else {
                    //Convert the ArrayList to an Array to populate DataPoint array.
                    selectedExerciseArray = new Exercise[selectedExerciseArrayList.size()];
                    selectedExerciseArray = selectedExerciseArrayList.toArray(selectedExerciseArray);

                    //Create a DataPoint array to populate the graph
                    DataPoint[] graphData = new DataPoint[selectedExerciseArrayList.size()];

                    //Populate the DataPoint array with the selected Exercise array.
                    for (int i = 0; i < selectedExerciseArrayList.size(); i++) {
                        graphData[i] = new DataPoint(selectedExerciseArray[i].getCalendar().getTime(),
                                selectedExerciseArray[i].getOneRepMax());

                    }

                    //Create a new graph and adjust its visual properties
                    GraphView graph = (GraphView) findViewById(R.id.graph);
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(graphData);
                    graph.removeAllSeries();
                    graph.addSeries(series);
                    TextView noDataToDisplay = (TextView)findViewById(R.id.no_data_to_display_text);
                    noDataToDisplay.setVisibility(View.GONE);
                    graph.setVisibility(View.VISIBLE);

                    //Format the x-axis
                    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
                    graph.getGridLabelRenderer().setNumHorizontalLabels(selectedExerciseArrayList.size());

                    //Set the y-axis scaling.
                    int maxYValue = 0;
                    for (int i = 0; i < selectedExerciseArrayList.size(); i++) {
                        graphData[i] = new DataPoint(i, selectedExerciseArray[i].getOneRepMax());
                        if (maxYValue < selectedExerciseArray[i].getOneRepMax()) {
                            maxYValue = selectedExerciseArray[i].getOneRepMax();
                        }
                    }

                    //Set the x-axis start point
                    Date minX = selectedExerciseArray[0].getCalendar().getTime();

                    //Set the x-axis end point
                    Date maxXDate = selectedExerciseArray[selectedExerciseArrayList.size()-1].getCalendar().getTime();

                    //Adjust the visual properties of the graph.
                    graph.getViewport().setScalable(true);
                    graph.getViewport().setScrollable(true);
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setMinX(minX.getTime());
                    graph.getViewport().setMaxX(maxXDate.getTime());
                    graph.getViewport().setYAxisBoundsManual(true);
                    graph.getViewport().setMinY(0);
                    graph.getViewport().setMaxY(maxYValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TextView nothingToDisplay = (TextView) findViewById(R.id.no_data_to_display_text);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
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
