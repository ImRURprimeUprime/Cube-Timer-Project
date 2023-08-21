package persistence;


import model.Session;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Written with reference to JsonSerializationDemo
// Test class for JsonReader
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Session session = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptySession() {
        JsonReader reader = new JsonReader("./data/testReaderEmptySession.json");
        try {
            Session session = reader.read();
            assertEquals(0, session.getSolveList().size());
            assertEquals(0, session.getTimeList().size());
            assertEquals(0, session.getInterestingSolves().size());
            assertEquals(0, session.getAverageOfFive());
            assertEquals(0, session.getMean());
            assertEquals("empty session", session.getName());
            assertEquals(0, session.getBestSolve().getTime());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralSession() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralSession.json");
        try {
            Session session = reader.read();
            assertEquals(2, session.getSolveList().size());
            assertEquals(2, session.getTimeList().size());
            assertEquals(1, session.getInterestingSolves().size());
            assertEquals(0, session.getAverageOfFive());
            assertEquals(1.55, session.getMean());
            assertEquals("s", session.getName());
            assertEquals(0.82, session.getBestSolve().getTime());
            assertEquals(2.29, session.getTimeList().get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}