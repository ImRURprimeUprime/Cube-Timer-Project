package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

// Code from AlarmSystem
/**
 * Unit tests for the Event class
 */
public class EventTest {
	private Event e;
	private Date d;
	
	//NOTE: these tests might fail if time at which line (2) below is executed
	//is different from time that line (1) is executed.  Lines (1) and (2) must
	//run in same millisecond for this test to make sense and pass.
	
	@BeforeEach
	public void runBefore() {
		e = new Event("Sensor open at door");   // (1)
		d = Calendar.getInstance().getTime();   // (2)
	}
	
	@Test
	public void testEvent() {
        Event e1 = e;
		assertEquals("Sensor open at door", e.getDescription());
        assertTrue(e.equals(e));
        assertEquals(e.hashCode(), e1.hashCode());
		assertEquals(d, e.getDate());
	}

	@Test
	public void testToString() {
		assertEquals(d.toString() + "\n" + "Sensor open at door", e.toString());
	}

    @Test
    public void testEqualEvent() {
        assertFalse(e.equals(null));
        assertFalse(e.equals("String"));
    }
}
