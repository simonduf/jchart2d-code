/*
 *  TestRingBufferArrayFast.java of project jchart2d, <purpose>
 *  Copyright 2006 (C) Achim Westermann, created on 06:29:12.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA*
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.util.collections;

import info.monitorenter.gui.chart.layout.TestChartPanelMemoryLeak;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Testcase for {@link info.monitorenter.util.collections.RingBufferArrayFast}.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.2 $
 */
public class TestRingBufferArrayFast extends TestCase {

  /**
   * Test method for {@link info.monitorenter.util.collections.RingBufferArrayFast.add(Object)}.
   * <p>
   */
  public void testAdd() {
    RingBufferArrayFast ringBuffer = new RingBufferArrayFast(1);
    System.out.println("Adding 1 element to a buffer of size 1.");
    ringBuffer.add(new Integer(0));
    assertEquals(1, ringBuffer.size());

  }

  /**
   * Test method for {@link info.monitorenter.util.collections.RingBufferArrayFast#size()}.
   * <p>
   */
  public void testSize() {

    RingBufferArrayFast ringBuffer = new RingBufferArrayFast(100);
    System.out
        .println("Adding 100 elements to a buffer with capacity of 100 with size assertions.");
    for (int i = 0; i < 100; i++) {
      assertEquals(i, ringBuffer.size());
      ringBuffer.add(new Integer(i));
    }
    System.out
        .println("Adding 10 elements to a full buffer with capacity of 100 with size assertions.");
    for (int i = 0; i < 10; i++) {
      ringBuffer.add(new Integer(i));
      assertEquals(100, ringBuffer.size());
    }

  }

  /**
   * Test method for
   * {@link info.monitorenter.util.collections.RingBufferArrayFast#iteratorL2F()}.
   * <p>
   */
  public void testIteratorL2F() {
    IRingBuffer ringBuffer = new RingBufferArrayFast(10);
    System.out.println("Adding 2 elements to a buffer of size 10");
    for (int i = 0; i < 2; i++) {
      ringBuffer.add(new Integer(i));
    }
    assertEquals(2, ringBuffer.size());
    Iterator it = ringBuffer.iteratorL2F();
    assertTrue(it.hasNext());
    int value = 0;
    Integer removed;
    while (it.hasNext()) {
      removed = (Integer) it.next();
      assertNotNull("Element no. " + value + " is null.", removed);
      // tests the order of the iterator:
      assertEquals(removed.intValue(), value);
      value++;
    }

    ringBuffer.clear();
    assertEquals(0, ringBuffer.size());
    value = 0;
    System.out.println("Adding 1 element to a buffer of size 10");
    for (int i = 0; i < 1; i++) {
      ringBuffer.add(new Integer(i));
    }
    assertEquals(1, ringBuffer.size());
    it = ringBuffer.iteratorL2F();
    assertTrue(it.hasNext());
    while (it.hasNext()) {
      removed = (Integer) it.next();
      assertNotNull("Element no. " + value + " is null.", removed);
      // tests the order of the iterator:
      assertEquals(removed.intValue(), value);
      value++;
    }

    ringBuffer.clear();
    assertEquals(0, ringBuffer.size());
    value = 0;
    System.out.println("Adding 10 elements to a buffer of size 10");
    for (int i = 0; i < 10; i++) {
      ringBuffer.add(new Integer(i));
    }
    assertEquals(10, ringBuffer.size());
    it = ringBuffer.iteratorL2F();
    while (it.hasNext()) {
      removed = (Integer) it.next();
      assertNotNull("Element no. " + value + " is null.", removed);
      // tests the order of the iterator:
      assertEquals(removed.intValue(), value);
      value++;
    }

    ringBuffer.clear();
    assertEquals(0, ringBuffer.size());
    value = 2;
    System.out.println("Adding 12 elements to a buffer of size 10");
    for (int i = 0; i < 12; i++) {
      ringBuffer.add(new Integer(i));
    }
    assertEquals(10, ringBuffer.size());
    it = ringBuffer.iteratorL2F();
    assertTrue(it.hasNext());
    while (it.hasNext()) {
      removed = (Integer) it.next();
      assertNotNull("Element no. " + value + " is null.", removed);
      // tests the order of the iterator:
      assertEquals(value, removed.intValue());
      value++;
    }

    System.out.println("Testing for side effects of hasNext()...");
    it = ringBuffer.iteratorL2F();
    for (int i = 0; i < 100; i++) {
      assertTrue(it.hasNext());
    }

    System.out.println("Testing hasNext() with iterator on empty buffer...");
    ringBuffer.clear();
    assertEquals(0, ringBuffer.size());
    assertTrue(ringBuffer.isEmpty());
    it = ringBuffer.iteratorL2F();
    assertFalse(it.hasNext());
  }

  /**
   * Test method for
   * {@link info.monitorenter.util.collections.RingBufferArrayFast#iteratorF2L()}.
   * <p>
   */
  public void testIteratorF2L() {
    IRingBuffer ringBuffer = new RingBufferArrayFast(10);
    System.out.println("Adding 2 elements to a buffer of size 10");
    for (int i = 0; i < 2; i++) {
      ringBuffer.add(new Integer(i));
    }
    assertEquals(2, ringBuffer.size());
    Iterator it = ringBuffer.iteratorF2L();
    int value = 1;
    Integer removed;
    while (it.hasNext()) {
      removed = (Integer) it.next();
      assertNotNull("Element no. " + value + " is null.", removed);
      // tests the order of the iterator:
      assertEquals(removed.intValue(), value);
      value--;
    }

    ringBuffer.clear();
    assertEquals(0, ringBuffer.size());
    value = 0;
    System.out.println("Adding 1 element to a buffer of size 10");
    for (int i = 0; i < 1; i++) {
      ringBuffer.add(new Integer(i));
    }
    assertEquals(1, ringBuffer.size());
    it = ringBuffer.iteratorF2L();
    while (it.hasNext()) {
      removed = (Integer) it.next();
      assertNotNull("Element no. " + value + " is null.", removed);
      // tests the order of the iterator:
      assertEquals(removed.intValue(), value);
      value--;
    }

    ringBuffer.clear();
    assertEquals(0, ringBuffer.size());
    value = 9;
    System.out.println("Adding 10 elements to a buffer of size 10");
    for (int i = 0; i < 10; i++) {
      ringBuffer.add(new Integer(i));
    }
    assertEquals(10, ringBuffer.size());
    it = ringBuffer.iteratorF2L();
    while (it.hasNext()) {
      removed = (Integer) it.next();
      assertNotNull("Element no. " + value + " is null.", removed);
      // tests the order of the iterator:
      assertEquals(removed.intValue(), value);
      value--;
    }

    ringBuffer.clear();
    assertEquals(0, ringBuffer.size());
    value = 11;
    System.out.println("Adding 12 elements to a buffer of size 10");
    for (int i = 0; i < 12; i++) {
      ringBuffer.add(new Integer(i));
    }
    assertEquals(10, ringBuffer.size());
    it = ringBuffer.iteratorF2L();
    while (it.hasNext()) {
      removed = (Integer) it.next();
      assertNotNull("Element no. " + value + " is null.", removed);
      // tests the order of the iterator:
      assertEquals(removed.intValue(), value);
      value--;
    }

    System.out.println("Testing for side effects of hasNext()...");
    it = ringBuffer.iteratorF2L();
    for (int i = 0; i < 100; i++) {
      assertTrue(it.hasNext());
    }

    System.out.println("Testing hasNext() with iterator f2l on empty buffer...");
    ringBuffer.clear();
    assertEquals(0, ringBuffer.size());
    assertTrue(ringBuffer.isEmpty());
    it = ringBuffer.iteratorF2L();
    assertFalse(it.hasNext());

    System.out.println("Testing hasNext() with iterator l2f on empty buffer...");
    ringBuffer.clear();
    assertEquals(0, ringBuffer.size());
    assertTrue(ringBuffer.isEmpty());
    it = ringBuffer.iteratorL2F();
    assertFalse(it.hasNext());
  }

  /**
   * Test method for
   * {@link info.monitorenter.util.collections.RingBufferArrayFast#setBufferSize(int)}.
   * <p>
   * 
   */
  public void testSetBufferSize() {
    IRingBuffer buffer = new RingBufferArrayFast(3);
    assertEquals(3, buffer.getBufferSize());
    for (int i = 0; i < 3; i++) {
      buffer.add(new Integer(i));
    }
    assertEquals(3, buffer.size());
    System.out.println("before setting size from 3 to 4: " + buffer.toString());
    buffer.setBufferSize(4);
    System.out.println("after setting size from 3 to 4: " + buffer.toString());
    assertEquals(3, buffer.size());
    assertEquals(4, buffer.getBufferSize());

    buffer.setBufferSize(2);
    assertEquals(2, buffer.size());
    assertEquals(2, buffer.getBufferSize());
    Iterator it = buffer.iteratorL2F();
    assertEquals(1, ((Integer) it.next()).intValue());
    assertEquals(2, ((Integer) it.next()).intValue());
    assertFalse(it.hasNext());
  }
  
  /**
   * Test suite for this test class.
   * <p>
   * 
   * @return the test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    suite.setName(TestRingBufferArrayFast.class.getName());

    suite.addTest(new TestRingBufferArrayFast("testAdd"));
    suite.addTest(new TestRingBufferArrayFast("testIteratorF2L"));
    suite.addTest(new TestRingBufferArrayFast("testIteratorL2F"));
    suite.addTest(new TestRingBufferArrayFast("testSetBufferSize"));
    suite.addTest(new TestRingBufferArrayFast("testSize"));

    return suite;
  }

  /**
   * @param arg0
   */
  public TestRingBufferArrayFast(String arg0) {
    super(arg0);
  }

}
