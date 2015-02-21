package matthewwills.gainstracker;


import java.io.Serializable;
import java.util.Calendar;

public class Exercise implements Serializable, Comparable<Exercise> {

    private String date;
    private String lift;
    private int weight;
    private int reps;
    private Calendar calendar;



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLift() {
        return lift;
    }

    public void setLift(String lift) {
        this.lift = lift;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    @Override
    public String toString() {
        return this.date + "\n" + this.lift + " " + this.weight + " for " + this.reps +
                ". Calculated 1RM: " + this.weight*36/(37-this.reps);
    }


    public int getOneRepMax() {
        return this.weight*36/(37-this.reps);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendarDate(Calendar calendarDate) {
        this.calendar = calendarDate;
    }

    @Override
    public int compareTo(Exercise another) {
        return this.getCalendar().compareTo(another.getCalendar());
    }
}

