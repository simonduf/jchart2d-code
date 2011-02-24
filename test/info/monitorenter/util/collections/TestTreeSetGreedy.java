/*
 *
 *  TestTreeSetGreedy.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 16.05.2005, 18:58:11
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
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.util.collections;

import info.monitorenter.gui.chart.ITrace2D;

import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public class TestTreeSetGreedy extends TestCase {

  private TreeSetGreedy test;

  /**
   * @param arg0
   *          the name of the <code>TestCase</code>.
   */
  public TestTreeSetGreedy(String arg0) {
    super(arg0);
  }

  /*
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    this.test = new TreeSetGreedy();
  }

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.test = null;
  }

  /**
   * Add an element and ensure size is 1.
   * 
   */
  public void testAdd() {
    assertNotNull(this.test);

    this.test.add(new Element());
    assertEquals(1, this.test.size());
  }

  /**
   * Add two distinct {@link IComparableProperty} elements with equal comparable
   * and ensure size is 2.
   * 
   */
  public void testAddEqual2() {
    assertNotNull(this.test);
    IComparableProperty e1 = new Element();
    IComparableProperty e2 = new Element();

    this.test.add(e1);
    this.test.add(e2);
    System.out.println("testAddEqual2");
    System.out.println(test);
    assertEquals(this.test.size(), 2);
  }

  /**
   * Add 1,1,1 and test, wether all numbers are different.
   * 
   */
  public void testAddEqual3() {
    assertNotNull(this.test);
    IComparableProperty e1 = new Element();
    IComparableProperty e2 = new Element();
    IComparableProperty e3 = new Element();

    this.test.add(e1);
    this.test.add(e2);
    this.test.add(e3);

    System.out.println("testAddEqual3");
    System.out.println(test);
    assertEquals(3, this.test.size());
  }

  /**
   * Add 1,1,1,1,1 and test, wether all numbers are different.
   * 
   */
  public void testAddEqual5() {
    assertNotNull(this.test);
    IComparableProperty e1 = new Element();
    IComparableProperty e2 = new Element();
    IComparableProperty e3 = new Element();
    IComparableProperty e4 = new Element();
    IComparableProperty e5 = new Element();

    this.test.add(e1);
    this.test.add(e2);
    this.test.add(e3);
    this.test.add(e4);
    this.test.add(e5);

    System.out.println("testAddEqual5");
    System.out.println(test);
    assertEquals(5, this.test.size());
  }

  /**
   * Add 1,1,1,1,1 and test, wether all numbers are different.
   * 
   */
  public void testAddEqual10() {
    assertNotNull(this.test);
    IComparableProperty e1 = new Element();
    IComparableProperty e2 = new Element();
    IComparableProperty e3 = new Element();
    IComparableProperty e4 = new Element();
    IComparableProperty e5 = new Element();
    IComparableProperty e6 = new Element();
    IComparableProperty e7 = new Element();
    IComparableProperty e8 = new Element();
    IComparableProperty e9 = new Element();
    IComparableProperty e10 = new Element();

    this.test.add(e1);
    this.test.add(e2);
    this.test.add(e3);
    this.test.add(e4);
    this.test.add(e5);
    this.test.add(e6);
    this.test.add(e7);
    this.test.add(e8);
    this.test.add(e9);
    this.test.add(e10);

    System.out.println("testAddEqual10");
    System.out.println(test);
    assertEquals(10, this.test.size());
  }

  /**
   * Add two distinct {@link IComparableProperty} elements with equal
   * comparable, remove them and ensure size is 0.
   * 
   */
  public void testAddRemoveEqual2() {
    assertNotNull(this.test);
    IComparableProperty e1 = new Element();
    IComparableProperty e2 = new Element();

    // add
    this.test.add(e1);
    this.test.add(e2);

    // remove
    this.test.remove(e1);
    this.test.remove(e2);

    System.out.println("testAddRemoveEqual2");
    System.out.println(test);

    assertEquals("Unexpected remaining elements: " + test, this.test.size(), 0);
  }

  /**
   * Add 1,1,1, remove then and ensure that zero elements remain.
   * 
   */
  public void testAddRemoveEqual3() {
    assertNotNull(this.test);
    IComparableProperty e1 = new Element();
    IComparableProperty e2 = new Element();
    IComparableProperty e3 = new Element();

    // add
    this.test.add(e1);
    this.test.add(e2);
    this.test.add(e3);

    // remove
    this.test.remove(e1);
    this.test.remove(e2);
    this.test.remove(e3);

    System.out.println("testAddRemoveEqual3");
    System.out.println(test);

    assertEquals("Unexpected remaining elements: " + test, this.test.size(), 0);
  }

  /**
   * Add 1,1,1,1,1, remove them and ensure that no elements remain.
   * 
   */
  public void testAddRemoveEqual5() {
    assertNotNull(this.test);
    IComparableProperty e1 = new Element();
    IComparableProperty e2 = new Element();
    IComparableProperty e3 = new Element();
    IComparableProperty e4 = new Element();
    IComparableProperty e5 = new Element();

    // add
    this.test.add(e1);
    this.test.add(e2);
    this.test.add(e3);
    this.test.add(e4);
    this.test.add(e5);

    // remove
    this.test.remove(e1);
    this.test.remove(e2);
    this.test.remove(e3);
    this.test.remove(e4);
    this.test.remove(e5);

    System.out.println("testAddRemoveEqual5");
    System.out.println(test);

    assertEquals("Unexpected remaining elements: " + test, this.test.size(), 0);
  }

  /**
   * Add 1,1,1,1,1,1,1,1,1,1, remove them and ensure that no elements remain.
   * 
   */
  public void testAddRemoveEqual10() {
    assertNotNull(this.test);
    IComparableProperty e1 = new Element();
    IComparableProperty e2 = new Element();
    IComparableProperty e3 = new Element();
    IComparableProperty e4 = new Element();
    IComparableProperty e5 = new Element();
    IComparableProperty e6 = new Element();
    IComparableProperty e7 = new Element();
    IComparableProperty e8 = new Element();
    IComparableProperty e9 = new Element();
    IComparableProperty e10 = new Element();

    // add
    this.test.add(e1);
    this.test.add(e2);
    this.test.add(e3);
    this.test.add(e4);
    this.test.add(e5);
    this.test.add(e6);
    this.test.add(e7);
    this.test.add(e8);
    this.test.add(e9);
    this.test.add(e10);

    // remove
    this.test.remove(e1);
    this.test.remove(e2);
    this.test.remove(e3);
    this.test.remove(e4);
    this.test.remove(e5);
    this.test.remove(e6);
    this.test.remove(e7);
    this.test.remove(e8);
    this.test.remove(e9);
    this.test.remove(e10);

    System.out.println("testAddRemoveEqual10");
    System.out.println(test);

    assertEquals("Unexpected remaining elements: " + test, this.test.size(), 0);
  }

  /**
   * Add 1,1,1,1 and remove one element that is in the center of the order
   * (assuming that they will get different numbers due to correct add
   * mechanism).
   * 
   */
  public void testAdd10RemoveCenter() {
    assertNotNull(this.test);
    IComparableProperty e1 = new Element();
    IComparableProperty e2 = new Element();
    IComparableProperty e3 = new Element();
    IComparableProperty e4 = new Element();
    IComparableProperty e5 = new Element();
    IComparableProperty e6 = new Element();
    IComparableProperty e7 = new Element();
    IComparableProperty e8 = new Element();
    IComparableProperty e9 = new Element();
    IComparableProperty e10 = new Element();

    // add
    this.test.add(e1);
    this.test.add(e2);
    this.test.add(e3);
    this.test.add(e4);
    this.test.add(e5);
    this.test.add(e6);
    this.test.add(e7);
    this.test.add(e8);
    this.test.add(e9);
    this.test.add(e10);

    // remove

    this.test.remove(e6);

    System.out.println("testAdd10RemoveCenter");
    System.out.println(test);

    assertEquals("Unexpected size: " + test, this.test.size(), 9);
  }

  /**
   * Add two identical elements and ensure that the 2nd operation fails.
   * 
   */
  public void testAddIdentical2() {
    assertNotNull(this.test);
    IComparableProperty e1 = new Element();

    this.test.add(e1);
    boolean success = this.test.add(e1);
    System.out.println("testAddIdentical");
    System.out.println(test);
    assertFalse(success);

  }

  /**
   * Starting 20 Threads that will remove and add inital equal elements (with an
   * internal comparableProperty of {@link ITrace2D#ZINDEX_MAX}) each 50 times
   * with arbitrary sleep times. Each Thread will ensure that it's own
   * {@link IComparableProperty} will be removed from the {@link TreeSetGreedy}
   * after removing it and assert this by calling the remove call a 2nd time and
   * looking for the returned boolean. The main Thread will wait until all
   * Threads have terminated.
   * 
   * 
   */
  public void testMultiThreadingAddRemove() {

    class TreeSetGreedyAddRemover extends Thread {
      public static final int INSTANCES = 30;

      public static final int ITERATIONS = 20;

      public static final int MAX_SLEEP = 2000;

      private Exception failure = null;

      private IComparableProperty element = new Element();

      private boolean stop = false;

      public void run() {
        boolean success = false;
        long sleep;
        for (int i = 0; i < ITERATIONS && !this.stop; i++) {
          try {
            System.out.println(this.getName() + " adding: " + this.element);
            success = TestTreeSetGreedy.this.test.add(this.element);
            try {
              assertTrue("Add operation unsuccessful!", success);
            } catch (Exception fail) {
              fail.printStackTrace(System.err);
              this.failure = fail;
            }
            sleep = (long) (Math.random() * MAX_SLEEP);
            System.out.println(this.getName() + " sleeping for : " + sleep + " ms.");
            sleep(sleep);
            System.out.println(this.getName() + " removing: " + this.element);
            success = TestTreeSetGreedy.this.test.remove(this.element);
            try {
              assertTrue("Remove operation unsuccessful!", success);
            } catch (Exception fail) {
              fail.printStackTrace(System.err);
              this.failure = fail;
            }
            success = TestTreeSetGreedy.this.test.remove(this.element);
            try {
              // avoid ConcurrentModificationException for toString() of test:
              assertFalse("A remove operation of my own removed element was successful! ", success);
            } catch (Exception fail) {
              fail.printStackTrace(System.err);
              this.failure = fail;
            }
          } catch (InterruptedException e) {
            TestTreeSetGreedy.fail("Caught an InterruptedExcetpion: " + e.toString());
          } catch (Exception fail) {
            fail.printStackTrace(System.err);
            this.failure = fail;
          }

        }
      }
    }

    TreeSetGreedyAddRemover[] threads = new TreeSetGreedyAddRemover[TreeSetGreedyAddRemover.INSTANCES];
    for (int i = 0; i < TreeSetGreedyAddRemover.INSTANCES; i++) {
      threads[i] = new TreeSetGreedyAddRemover();
    }

    for (int i = 0; i < TreeSetGreedyAddRemover.INSTANCES; i++) {
      threads[i].setDaemon(true);
    }
    for (int i = 0; i < TreeSetGreedyAddRemover.INSTANCES; i++) {
      threads[i].start();
    }

    boolean allFinished = false;
    while (!allFinished) {
      allFinished = true;
      for (int i = 0; i < TreeSetGreedyAddRemover.INSTANCES; i++) {
        if (threads[i].failure != null) {
          // clean output: stop threads, wait for them to finish and then fail.
          Exception failure = threads[i].failure;
          for (int j = 0; j < TreeSetGreedyAddRemover.INSTANCES; j++) {
            threads[j].stop = true;
          }
          try {
            Thread.sleep(TreeSetGreedyAddRemover.MAX_SLEEP + 1000);
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }
          StringWriter trace = new StringWriter();
          PrintWriter tracePrint = new PrintWriter(trace);

          failure.printStackTrace(tracePrint);
          tracePrint.flush();
          tracePrint.close();
          fail(trace.toString());
        }
        allFinished &= !threads[i].isAlive();
      }
      try {
        Thread.sleep(400);
      } catch (InterruptedException e) {
        TestTreeSetGreedy.fail("Caught an InterruptedExcetpion: " + e.toString());
      }
    }
    System.out.println("testMultiThreadingAddRemove() finished.");

  }

  final class Element implements IComparableProperty {

    private Number compare = new Integer(ITrace2D.ZINDEX_MAX);

    /*
     * (non-Javadoc)
     * 
     * @see info.monitorenter.util.collections.IComparableProperty#getComparableProperty()
     */
    public Number getComparableProperty() {
      return this.compare;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.monitorenter.util.collections.IComparableProperty#setComparableProperty(java.lang.Number)
     */
    public void setComparableProperty(Number n) {
      this.compare = n;
    }

    public String toString() {
      StringBuffer ret = new StringBuffer(this.compare.toString());
      return ret.toString();
    }
  }
  /**
   * Test suite for this test class.
   * <p>
   * 
   * @return the test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    suite.setName(TestTreeSetGreedy.class.getName());

    suite.addTest(new TestTreeSetGreedy("testAdd"));
    suite.addTest(new TestTreeSetGreedy("testAdd10RemoveCenter"));
    suite.addTest(new TestTreeSetGreedy("testAddEqual10"));
    suite.addTest(new TestTreeSetGreedy("testAddEqual2"));
    suite.addTest(new TestTreeSetGreedy("testAddEqual3"));
    suite.addTest(new TestTreeSetGreedy("testAddEqual5"));
    suite.addTest(new TestTreeSetGreedy("testAddIdentical2"));
    suite.addTest(new TestTreeSetGreedy("testAddRemoveEqual10"));
    suite.addTest(new TestTreeSetGreedy("testAddRemoveEqual2"));
    suite.addTest(new TestTreeSetGreedy("testAddRemoveEqual3"));
    suite.addTest(new TestTreeSetGreedy("testAddRemoveEqual5"));
    suite.addTest(new TestTreeSetGreedy("testMultiThreadingAddRemove"));
  
    return suite;
  }
}
