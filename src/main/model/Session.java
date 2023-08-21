package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// Class for a session, represents a session with solve list, time list, interesting solves list, name,
// mean time of the session, best solve of the session, and the most recent average of 5
public class Session implements Writable {
    private List<Solve> solveList;
    private List<Double> timeList;
    private List<Solve> interestingSolves;
    private String name;
    private double mean;
    private Solve bestSolve;
    private double currentAverageOfFive;

    // EFFECTS: creates a new solve session with mean and average set to 0, empty solveList
    //          and empty timeList, no best solve, and given name
    public Session(String name) {
        solveList = new ArrayList<>();
        timeList = new ArrayList<>();
        interestingSolves = new ArrayList<>();
        this.name = name;
        mean = 0.00;
        bestSolve = new Solve(0.00, "you currently have no solves in this session", 0);
        currentAverageOfFive = 0.00;
    }

    // MODIFIES: this
    // EFFECTS: adds solve to solveList and the time to the timeList
    public void addSolve(Solve solve) {
        solveList.add(solve);
        EventLog.getInstance().logEvent(new Event("Added a new solve to session. Time: " + solve.getTime()));
    }

    // REQUIRES: index >= 0, index < solveList.size()
    // MODIFIES: this
    // EFFECTS: removes the solve at given index from solveList, removes the corresponding
    //          time from timeList
    public void removeSolve(int index) {
        solveList.remove(index);
        EventLog.getInstance().logEvent(new Event("Removed solve " + (index + 1)
                + " from session"));
    }

    // https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    // MODIFIES: this
    // EFFECTS: calculates the mean time of the solves in this session
    public void calculateMean() {
        double sum = 0;
        if (solveList.size() == 0) {
            mean = 0;
        } else {
            for (Solve solve:solveList) {
                sum += solve.getTime();
            }
            double result = sum / solveList.size();
            DecimalFormat df = new DecimalFormat("#.##");
            result = Double.valueOf(df.format(result));
            mean = result;
        }
    }

    // MODIFIES: this
    // EFFECTS: determines the best solve of this session based on the time of the solve
    public void calculateBestSolve() {
        if (solveList.size() == 0) {
            bestSolve = new Solve(0.00, "you currently have no solves in this session", 0);
        }
        if (solveList.size() == 1) {
            bestSolve = solveList.get(0);
        } else if (solveList.size() != 0) {
            Solve bestSoFar = solveList.get(0);
            for (Solve solve:solveList) {
                if (solve.getTime() <= bestSoFar.getTime()) {
                    bestSoFar = solve;
                }
            }
            bestSolve = bestSoFar;
        }
    }

    // https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    // MODIFIES: this
    // EFFECTS: takes the 5 most recent solves, taking away the fastest and slowest solves,
    //          calculates the mean of the 3 solves that remain
    public void calculateAverage() {
        int size = solveList.size();
        if (size >= 5) {
            double slowest = solveList.get(size - 1).getTime();
            int slowestIndex = 1;
            double fastest = solveList.get(size - 1).getTime();
            int fastestIndex = 1;
            for (int i = 1; i <= 5; i += 1) {
                if (solveList.get(size - i).getTime() >= slowest) {
                    slowest = solveList.get(size - i).getTime();
                    slowestIndex = i;
                } else if (solveList.get(size - i).getTime() <= fastest) {
                    fastest = solveList.get(size - i).getTime();
                    fastestIndex = i;
                }
            }
            currentAverageOfFive = averageHelper(slowestIndex, fastestIndex, size);
        } else {
            currentAverageOfFive = 0.00;
        }
    }

    // Helper method for calculateAverage()
    // EFFECTS: adds up the middle 3 solves and dividing the sum by 3, return the result
    public Double averageHelper(int slowestIndex, int fastestIndex, int size) {
        double sum = 0;
        for (int i = 1; i <= 5; i += 1) {
            if ((i != slowestIndex) && (i != fastestIndex)) {
                sum += solveList.get(size - i).getTime();
            }
        }
        double result = (sum / 3);
        DecimalFormat df = new DecimalFormat("#.##");
        result = Double.valueOf(df.format(result));
        return result;
    }

    // MODIFIES: this
    // EFFECTS: organizes all interesting solves in a list
    public void filterInteresting() {
        List<Solve> interesting = new ArrayList<>();
        for (Solve solve:solveList) {
            if (solve.getIsInteresting()) {
                interesting.add(solve);
            }
        }
        interestingSolves = interesting;
    }

    // MODIFIES: this
    // EFFECTS: takes the time of each solve and stores all times in timeList
    public void convertSolveTimes() {
        List<Double> times = new ArrayList<>();
        for (Solve solve:solveList) {
            times.add(solve.getTime());
        }
        timeList = times;
    }

    // MODIFIES: solves in solveList
    // EFFECTS: updates the id of solves based on their index in solveList
    public void refreshId() {
        for (int i = 0; i < solveList.size(); i += 1) {
            solveList.get(i).setId(i + 1);
        }
    }

    // getters
    public List<Solve> getSolveList() {
        return solveList;
    }

    public List<Double> getTimeList() {
        return timeList;
    }

    public List<Solve> getInterestingSolves() {
        return interestingSolves;
    }

    public String getName() {
        return name;
    }

    public double getMean() {
        return mean;
    }

    public Solve getBestSolve() {
        return bestSolve;
    }

    public double getAverageOfFive() {
        return currentAverageOfFive;
    }

    // setters
    public void setMean(Double mean) {
        this.mean = mean;
    }

    public void setAverageOfFive(Double average) {
        this.currentAverageOfFive = average;
    }

    public void setName(String name) {
        this.name = name;
        EventLog.getInstance().logEvent(new Event("Name of the session was set to "
                + name));
    }

    // Written with reference to JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("solveList", solveListToJson());
        json.put("timeList", timeList);
        json.put("interestingSolves", interestingSolvesToJson());
        json.put("name", name);
        json.put("mean", mean);
        json.put("bestSolve", bestSolve.toJson());
        json.put("currentAverageOfFive", currentAverageOfFive);
        return json;
    }

    // EFFECTS: returns solves in this session as a JSON array
    private JSONArray solveListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Solve s : solveList) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }


    // EFFECTS: returns solves in the interesting solves as a JSON array
    private JSONArray interestingSolvesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Solve s : interestingSolves) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }


}
