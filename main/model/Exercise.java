package model;

import org.json.JSONObject;

// Represents a single exercise performed in a workout, including reps, sets, weight, and time.
public class Exercise {

    private String name;
    private int reps;
    private int sets;
    private double weight;
    private int time;

    // EFFECTS: initializes the various fields of the class
    public Exercise(String name, int reps, int sets, double weight, int time) {
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.weight = weight;
        this.time = time;
    }

    // EFFECTS: Returns the choice of exercise
    public String getName() {
        return name;
    }

    // EFFECTS: Returns the amount of reps for an exercise
    public int getReps() {
        return reps;
    }

    // EFFECTS: Returns the amount of sets for an exercise
    public int getSets() {
        return sets;
    }

    // EFFECTS: Returns the weight used in the exercise
    public double getWeight() {
        return weight;
    }

    // EFFECTS: Returns the training volume of the user
    public double getVolumeTrained() {
        return (weight * sets * reps);
    }

    // EFFECTS: Returns the time taken to complete the exercise
    public int getTime() {
        return time;
    }

    // EFFECTS: Returns the JSON Object of the Exercise
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", name);
        jsonObject.put("reps", reps);
        jsonObject.put("sets", sets);
        jsonObject.put("weight", weight);
        jsonObject.put("time", time);

        return jsonObject;
    }

    // EFFECTS: Creates an exercise by restoring past information and returns it
    public static Exercise convertFromJson(JSONObject jsonObject) {
        String n = jsonObject.getString("name");
        int r = jsonObject.getInt("reps");
        int s = jsonObject.getInt("sets");
        double w = jsonObject.getDouble("weight");
        int t = jsonObject.getInt("time");

        return new Exercise(n, r, s, w, t);
    }

}
