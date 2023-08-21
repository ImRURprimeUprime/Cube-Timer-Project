package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for the Solve class
class SolveTest {

    private Solve testSolve;
    @BeforeEach
    void beforeEach() {
        testSolve = new Solve(1, "R", 1);
    }

    @Test
    void testConstructor() {
        assertEquals(1, testSolve.getTime());
        assertEquals("R", testSolve.getScramble());
        assertFalse(testSolve.getPenalty());
        assertEquals("", testSolve.getReconstruction());
        assertFalse(testSolve.getIsInteresting());
        assertEquals(1, testSolve.getId());
    }

    @Test
    void testTimePenalty() {
        testSolve.timePenalty();
        assertEquals(3, testSolve.getTime());
        assertTrue(testSolve.getPenalty());
        testSolve.timePenalty();
        assertEquals(1, testSolve.getTime());
        assertFalse(testSolve.getPenalty());
    }

    @Test
    void testReconstruct() {
        testSolve.reconstruct("Put white face bottom...");
        assertEquals("Put white face bottom...", testSolve.getReconstruction());
        testSolve.reconstruct("Put yellow face bottom...");
        assertEquals("Put yellow face bottom...", testSolve.getReconstruction());
    }

    @Test
    void testInvertInteresting() {
        testSolve.invertInteresting();
        assertTrue(testSolve.getIsInteresting());
        testSolve.invertInteresting();
        assertFalse(testSolve.getIsInteresting());
    }
}