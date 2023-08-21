package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// tests for the Session classs
public class SessionTest {
    private Session testSession;
    private Solve s1;
    private Solve s2;
    private Solve s3;
    private Solve s4;
    private Solve s5;
    private Solve s6;
    @BeforeEach
    void beforeEach() {
        testSession = new Session("Chinese Felik's session");
        s1 = new Solve(5.00, "R", 1);
        s2 = new Solve(4.00, "U", 2);
        s3 = new Solve(3.00, "D", 3);
        s4 = new Solve(2.00, "L", 4);
        s5 = new Solve(1.00, "F", 5);
        s6 = new Solve(14.00, "B", 6);
    }

    @Test
    void testConstructor() {
        assertEquals("Chinese Felik's session", testSession.getName());
        assertEquals(0, testSession.getTimeList().size());
        assertEquals(0, testSession.getSolveList().size());
        assertEquals(0, testSession.getInterestingSolves().size());
        assertEquals(0.00, testSession.getBestSolve().getTime());
        assertEquals(0.00, testSession.getMean());
        assertEquals(0.00, testSession.getAverageOfFive());
    }

    @Test
    void testAddSolve() {
        testSession.addSolve(s1);
        assertEquals(1, testSession.getSolveList().size());
        assertEquals(s1, testSession.getSolveList().get(0));

        testSession.addSolve(s2);
        assertEquals(2, testSession.getSolveList().size());
        assertEquals(s2, testSession.getSolveList().get(1));
    }

    @Test
    void testRemoveSolve() {
        testSession.addSolve(s1);
        testSession.addSolve(s2);

        testSession.removeSolve(1);
        assertEquals(1, testSession.getSolveList().size());
        assertEquals(s1, testSession.getSolveList().get(0));

        testSession.addSolve(s3);
        testSession.addSolve(s4);

        testSession.removeSolve(2);
        assertEquals(2, testSession.getSolveList().size());
        assertEquals(s1, testSession.getSolveList().get(0));
        assertEquals(s3, testSession.getSolveList().get(1));
    }

    @Test
    void testCalculateMean() {
        testSession.calculateMean();
        assertEquals(0.00, testSession.getMean());

        testSession.addSolve(s1);
        testSession.calculateMean();
        assertEquals(5.00, testSession.getMean());

        testSession.addSolve(s3);
        testSession.calculateMean();
        assertEquals(4.00, testSession.getMean());

        testSession.addSolve(s6);
        testSession.calculateMean();
        assertEquals(7.33, testSession.getMean());
    }

    @Test
    void testCalculateBestSolve() {
        testSession.calculateBestSolve();
        assertEquals(0.00, testSession.getBestSolve().getTime());

        testSession.addSolve(s2);
        testSession.calculateBestSolve();
        assertEquals(s2, testSession.getBestSolve());

        testSession.addSolve(s4);
        testSession.calculateBestSolve();
        assertEquals(s4, testSession.getBestSolve());

        testSession.addSolve(s1);
        testSession.calculateBestSolve();
        assertEquals(s4, testSession.getBestSolve());

        testSession.addSolve(s3);
        testSession.calculateBestSolve();
        assertEquals(s4, testSession.getBestSolve());

        testSession.addSolve(s6);
        testSession.calculateBestSolve();
        assertEquals(s4, testSession.getBestSolve());

        testSession.addSolve(s5);
        testSession.calculateBestSolve();
        assertEquals(s5, testSession.getBestSolve());
        s5.timePenalty();
        testSession.calculateBestSolve();
        assertEquals(s4, testSession.getBestSolve());
    }

    @Test
    void testCalculateAverage() {
        testSession.calculateAverage();
        assertEquals(0.00, testSession.getAverageOfFive());

        testSession.addSolve(s1);
        testSession.calculateAverage();
        assertEquals(0.00, testSession.getAverageOfFive());

        testSession.addSolve(s2);
        testSession.addSolve(s3);
        testSession.addSolve(s6);
        testSession.calculateAverage();
        assertEquals(0.00, testSession.getAverageOfFive());

        testSession.addSolve(s5);
        testSession.calculateAverage();
        assertEquals(4.00, testSession.getAverageOfFive());

        testSession.addSolve(s2);
        testSession.calculateAverage();
        assertEquals(3.67, testSession.getAverageOfFive());
    }

    @Test
    void testFilterInteresting() {
        testSession.addSolve(s1);
        testSession.addSolve(s2);
        testSession.addSolve(s3);
        testSession.addSolve(s4);
        testSession.addSolve(s5);
        testSession.addSolve(s6);
        testSession.filterInteresting();
        assertEquals(0, testSession.getInterestingSolves().size());

        s1.invertInteresting();
        testSession.filterInteresting();
        assertEquals(1, testSession.getInterestingSolves().size());
        assertEquals(s1, testSession.getInterestingSolves().get(0));
    }

    @Test
    void testConvertSolveTimes() {
        testSession.addSolve(s1);
        testSession.addSolve(s2);
        testSession.addSolve(s3);
        testSession.addSolve(s4);
        testSession.addSolve(s5);
        testSession.addSolve(s6);
        testSession.convertSolveTimes();
        List<Double> times = new ArrayList<>();
        times.add(5.00);
        times.add(4.00);
        times.add(3.00);
        times.add(2.00);
        times.add(1.00);
        times.add(14.00);
        assertEquals(times, testSession.getTimeList());
    }

    @Test
    void testSetName() {
        assertEquals("Chinese Felik's session", testSession.getName());
        testSession.setName("R");
        assertEquals("R", testSession.getName());
    }

    @Test
    void testRefreshId() {
        testSession.addSolve(s2);
        testSession.addSolve(s5);
        testSession.addSolve(s6);
        assertEquals(6, s6.getId());
        assertEquals(5, s5.getId());
        assertEquals(2, s2.getId());
        testSession.refreshId();
        assertEquals(3, s6.getId());
        assertEquals(2, s5.getId());
        assertEquals(1, s2.getId());
    }


}
