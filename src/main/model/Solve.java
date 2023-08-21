package model;

import org.json.JSONObject;
import persistence.Writable;

import java.text.DecimalFormat;

// class for solve, represents a solve with time, penalty, scramble, reconstruction, and whether or not
// it is marked as interesting
public class Solve implements Writable {
    private double time;
    private boolean penalty;
    private String scramble;
    private String reconstruction;
    private boolean isInteresting;
    private int id;

    // EFFECTS: creates a solve with recorded time, no time penalty, corresponding scramble,
    //          no reconstruction, and is set to not interesting by default
    public Solve(double time, String scramble, int id) {
        this.time = time;
        penalty = false;
        this.scramble = scramble;
        reconstruction = "";
        isInteresting = false;
        this.id = id;
    }

    // MODIFIES: this
    // EFFECTS: penalty false: add 2 seconds from time, make penalty true;
    //          penalty true: subtract 2 seconds from time, make penalty false
    public void timePenalty() {
        if (!penalty) {
            penalty = true;
            time += 2;
            EventLog.getInstance().logEvent(new Event("Added 2 seconds time penalty to solve " + id
                    + ", new time: " + time));
        } else {
            penalty = false;
            time -= 2;
            EventLog.getInstance().logEvent(new Event("Removed 2 seconds time penalty from solve " + id
                    + ", new time: " + time));
        }
        DecimalFormat df = new DecimalFormat("#.##");
        time = Double.valueOf(df.format(time));
    }

    // MODIFIES: this
    // EFFECTS: sets the reconstruction field to the reconstruction parameter
    public void reconstruct(String reconstruction) {
        this.reconstruction = reconstruction;
    }

    // MODIFIES: this
    // EFFECTS: sets isInteresting to false if it is true initially, sets it to true if false initially
    public void invertInteresting() {
        this.isInteresting = !isInteresting;
        if (isInteresting) {
            EventLog.getInstance().logEvent(new Event("Solve " + id + " has been bookmarked."));
        } else {
            EventLog.getInstance().logEvent(new Event("Removed bookmark from solve " + id + "."));
        }
    }

    // getters
    public double getTime() {
        return time;
    }

    public String getScramble() {
        return scramble;
    }

    public boolean getPenalty() {
        return penalty;
    }

    public String getReconstruction() {
        return reconstruction;
    }

    public boolean getIsInteresting() {
        return isInteresting;
    }

    public int getId() {
        return id;
    }

    // setters
    public void setPenalty(Boolean penalty) {
        this.penalty = penalty;
    }

    public void setInteresting(Boolean interesting) {
        this.isInteresting = interesting;
    }

    public void setId(int i) {
        this.id = i;
    }

    // Written with reference to JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("time", time);
        json.put("id", id);
        json.put("penalty", penalty);
        json.put("scramble", scramble);
        json.put("reconstruction", reconstruction);
        json.put("isInteresting", isInteresting);

        return json;
    }
}
