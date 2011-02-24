/*
 *
 *  TestMultithreading.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 10.05.2005, 21:33:24
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
package info.monitorenter.gui.chart;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 */
public class TestMultithreading extends TestCase {

  protected Chart2D chart;

  protected ITrace2D trace;

  protected WeakHashMap weakMap;

  protected List producers;

  // test configuration
  protected final static int PRODUCER_AMOUNT = 10;

  protected final static int PRODUCER_SLEEPRANGE = 100;

  protected final static int PRODUCER_ADD_POINT_AMOUNT = 500;

  protected final static int CONSUMER_SLEEPRANGE = 1000;

  protected static final Class TRACE_CLASS = Trace2DLtd.class;

  /**
   *
   */
  public TestMultithreading() {
    super();
  }

  /*
   * (non-Javadoc)
   *
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() throws Exception {
    super.setUp();
    this.chart = new Chart2D();
    this.weakMap = new WeakHashMap();
    this.producers = new LinkedList();
    for (int add = PRODUCER_AMOUNT; add > 0; add--) {
      this.producers.add(new Producer(PRODUCER_ADD_POINT_AMOUNT, PRODUCER_SLEEPRANGE));
    }
    this.trace = (ITrace2D) TRACE_CLASS.newInstance();
    this.chart.addTrace(trace);
  }

  /*
   * (non-Javadoc)
   *
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.weakMap = null;
    this.chart = null;
    this.producers = null;
  }

  /**
   * @param arg0
   */
  public TestMultithreading(String arg0) {
    super(arg0);
  }

  // ////////////////////////////
  // Test methods
  // ////////////////////////////

  public void testTrace2DLtd() {

    this.startThreads();
  }

  // ////////////////////////////
  // Helper methods
  // ////////////////////////////
  protected boolean isAllProducersFinished() {
    boolean ret = true;
    Iterator it = this.producers.iterator();
    Producer producer;
    while (it.hasNext()) {
      producer = (Producer) it.next();
      if (!producer.isAlive()) {
        it.remove();
      }
      else {
        ret = false;
      }
    }
    return ret;
  }

  /**
   * Start the Producer Threads and one Consumer Thread and blocks until all
   * Threads are finished to avoid that teardown will be called and further
   * tests are executed at the same time the calling test method has initiated
   * the Threads for it's test.
   *
   */
  protected void startThreads() {
    Iterator it = this.producers.iterator();
    Thread producer;
    while (it.hasNext()) {
      producer = (Thread) it.next();
      producer.start();
    }
    Consumer consumer = new Consumer(CONSUMER_SLEEPRANGE);

    consumer.start();
    while (!this.isAllProducersFinished() || consumer.isAlive()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    this.report();
  }

  void report() {
    long keys = this.weakMap.size();
    System.out.println("Points remaining in the weakMap: " + keys);
    System.out.println("System.runFinalization()... ");
    System.runFinalization();
    System.out.println("System.gc()... ");
    System.gc();
    System.out.println("Points remaining in the weakMap: " + keys);
    keys = 0;
    Iterator it = this.weakMap.keySet().iterator();
    while (it.hasNext()) {
      keys++;
      System.out.println("Point " + it.next().toString() + " was not dropped.");
    }
    System.out.println("Points remaining in the weakMap: " + keys);
    assertFalse("There are " + keys + " TracePoint2D instances not deleted from the WeakHashMap.", keys > this.trace.getMaxSize());
  }

  // ////////////////////////////
  // Worker classes
  // ////////////////////////////

  /**
   * Thread implementation that adds random points to the trace of the outer
   * classes's chart and takes a random sleep time between 0 and a constructor
   * given value between two add operations.
   * <p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   */
  class Producer extends Thread {
    /** The amount of points to add before termination. */
    private long m_toAdd;

    /** The maximum sleep time between two add operations. */
    private long m_sleepRange;

    /** Flag to allow stopping this Thread from outside. */
    private boolean m_stop = false;

    /**
     * <p>
     * Constructs a producer that will add <code>toAdd</code> points with
     * random breaks of milliseconds between <code>maxSleep</code> and zero.
     * </p>
     *
     * @param toAdd
     *          the amount of points to add.
     *
     * @param sleepRange
     *          the maxium time in milliseconds the Thread will sleep between
     *          two points added.
     *
     */
    Producer(final long toAdd, final long sleepRange) {
      this.m_toAdd = toAdd;
      this.m_sleepRange = sleepRange;
    }

    /**
     * Does the job.
     * <p>
     */
    public void run() {
      TracePoint2D point;
      while (this.m_toAdd > 0 && !this.m_stop) {
        try {
          sleep((long) (Math.random() * this.m_sleepRange));
        } catch (InterruptedException e) {
          e.printStackTrace();
          this.m_stop = true;
        }
        if (this.m_toAdd % 10 == 0) {
          System.out.println('[' + this.getName() + "] adding point... " + this.m_toAdd + " to go...");
        }
        point = new TracePoint2D(this.m_toAdd, this.m_toAdd);
        TestMultithreading.this.weakMap.put(point, point.toString());
        TestMultithreading.this.trace.addPoint(point);
        this.m_toAdd--;
      }
    }
  }

  /**
   * Thread that invokes paint operations with a mock graphics context (thus
   * consumes pending unscaled points) interrupted by a sleep between 0 and a
   * configurable amount of milliseconds.
   * <p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   */
  class Consumer extends Thread {

    /** The maximum sleep time between two paint operations. */
    private long m_sleepRange;

    /**
     * Creates an instance that mock-paints the chart every
     * <code>0 ..  sleeprange</code> ms.
     * <p>
     *
     * @param sleepRange
     *          the maximum sleep range between two rendering operations.
     */
    Consumer(final long sleepRange) {
      this.m_sleepRange = sleepRange;
    }

    /** Flag to allow termination from outside. */
    private boolean m_stop = false;

    /**
     * Do the job.
     * <p>
     */
    public void run() {
      MockGraphics2D mockGraphics = new MockGraphics2D();
      while (!(this.m_stop || TestMultithreading.this.isAllProducersFinished())) {
        try {
          sleep((long) (Math.random() * this.m_sleepRange));
        } catch (InterruptedException e) {
          e.printStackTrace();
          this.m_stop = true;
        }
        System.out.println('[' + this.getClass().getName() + "] painting...");
        TestMultithreading.this.chart.paint(mockGraphics);
      }
    }

  }
}
