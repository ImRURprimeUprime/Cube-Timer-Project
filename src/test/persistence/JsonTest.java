package persistence;

import model.Solve;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkSolve(Double t, String scr, Boolean p, String r, Boolean i, int id, Solve solve) {
        assertEquals(t, solve.getTime());
        assertEquals(scr, solve.getScramble());
        assertEquals(p, solve.getPenalty());
        assertEquals(r, solve.getReconstruction());
        assertEquals(i, solve.getIsInteresting());
        assertEquals(id, solve.getId());
    }
}