package persistence;

import model.Solve;
import model.Session;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Written with reference to JsonSerializationDemo
// Represents a reader that reads session from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads session from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Session read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkRoom(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses session from JSON object and returns it
    private Session parseWorkRoom(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Double mean = jsonObject.getDouble("mean");
        Double currentAverageOfFive = jsonObject.getDouble("currentAverageOfFive");

        Session session = new Session(name);
        session.setMean(mean);
        session.setAverageOfFive(currentAverageOfFive);
        addSolves(session, jsonObject);
        session.convertSolveTimes();
        session.filterInteresting();
        session.calculateBestSolve();
        return session;
    }

    // MODIFIES: session
    // EFFECTS: parses solves from JSON object and adds them to session
    private void addSolves(Session session, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("solveList");
        for (Object json : jsonArray) {
            JSONObject next = (JSONObject) json;
            addSolve(session, next);
        }
    }

    // MODIFIES: session
    // EFFECTS: parses solve from JSON object and adds it to session
    private void addSolve(Session session, JSONObject jsonObject) {
        Double time = jsonObject.getDouble("time");
        String scramble = jsonObject.getString("scramble");
        Boolean penalty = jsonObject.getBoolean("penalty");
        Boolean isInteresting = jsonObject.getBoolean("isInteresting");
        String reconstruction = jsonObject.getString("reconstruction");
        int id = jsonObject.getInt("id");
        Solve solve = new Solve(time, scramble, id);
        solve.reconstruct(reconstruction);
        solve.setPenalty(penalty);
        solve.setInteresting(isInteresting);
        session.addSolve(solve);
    }


}
