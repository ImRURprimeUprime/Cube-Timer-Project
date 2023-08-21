package persistence;

import model.Solve;
import model.Session;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Written with reference to JsonSerializationDemo
// Test class for JsonWriter
class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Session session = new Session("My work room");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptySession() {
        try {
            Session session = new Session("I'm the cube lord");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptySession.json");
            writer.open();
            writer.write(session);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptySession.json");
            session = reader.read();
            assertEquals("I'm the cube lord", session.getName());
            assertEquals(0, session.getSolveList().size());
            assertEquals(0, session.getTimeList().size());
            assertEquals(0, session.getInterestingSolves().size());
            assertEquals(0, session.getBestSolve().getTime());
            assertEquals(0, session.getMean());
            assertEquals(0, session.getAverageOfFive());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralSession() {
        try {
            Session session = new Session("My Session");
            session.addSolve(new Solve(4.73, "R", 1));
            Solve duYuSheng = new Solve(3.47, "U", 2);
            duYuSheng.invertInteresting();
            session.addSolve(duYuSheng);
            session.convertSolveTimes();
            session.filterInteresting();
            session.calculateBestSolve();
            session.calculateAverage();
            session.calculateMean();
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(session);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            session = reader.read();
            List<Solve> solves = session.getSolveList();
            assertEquals("My Session", session.getName());
            assertEquals(2, solves.size());
            assertEquals(2, session.getTimeList().size());
            assertEquals(1, session.getInterestingSolves().size());
            checkSolve(4.73, "R", false, "", false, 1, solves.get(0));
            checkSolve(3.47, "U", false, "", true, 2, solves.get(1));
            assertEquals(0, session.getAverageOfFive());
            assertEquals(4.1, session.getMean());
            assertEquals(3.47, session.getBestSolve().getTime());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}