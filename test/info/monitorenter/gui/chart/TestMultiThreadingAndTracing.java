/*
 *
 *  TestMultiThreadingAndTracing.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 10.05.2005, 22:52:54
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

/**
 * <p>
 * Multiple <code>Producers</code> concurrently add points to an amount of
 * randomly shared <code>ITrace2D</code> instances.
 * </p>
 * <p>
 * One <code>Consumer</code> invokes paint on the <code>Chart2D</code> thus
 * allowing to drop pending changes stored in it.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 */
public class TestMultiThreadingAndTracing extends TestMultithreading {

  protected List traces;

  // configuration
  protected static final int TRACES_AMOUNT = 10;

  protected static final Class TRACE_CLASS = Trace2DLtd.class;

  /*
   * (non-Javadoc)
   *
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() throws Exception {
    this.trace = (ITrace2D) TRACE_CLASS.newInstance();
    this.chart = new Chart2D();
    this.weakMap = new WeakHashMap();
    this.producers = new LinkedList();
    for (int add = PRODUCER_AMOUNT; add > 0; add--) {
      this.producers.add(new Producer(PRODUCER_ADD_POINT_AMOUNT, PRODUCER_SLEEPRANGE));
    }

    this.traces = new LinkedList();
    ITrace2D tmpTrace;
    for (int add = TRACES_AMOUNT; add > 0; add--) {
      tmpTrace = (ITrace2D) TRACE_CLASS.newInstance();
      tmpTrace.setName("Trace-" + add);
      this.traces.add(tmpTrace);
    }

    assertTrue(this.chart.getTraces().size() == 0);
    // add all traces
    Iterator it = this.traces.iterator();
    while (it.hasNext()) {
      tmpTrace = (ITrace2D) it.next();
      this.chart.addTrace(tmpTrace);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.traces = null;
  }

  // ////////////////////////////
  // Helper methods
  // ////////////////////////////

  ITrace2D pickRandomTrace() {
    int index = (int) Math.round(Math.random() * TRACES_AMOUNT);
    return (ITrace2D) this.traces.get(index);
  }

  void report() {
    long keys = weakMap.size();
    System.out.println("Points remaining in the weakMap: " + keys);
    System.out.println("System.runFinalization()... ");
    System.runFinalization();
    System.out.println("System.gc()... ");
    System.gc();
    keys = weakMap.size();
    System.out.println("Points remaining in the weakMap: " + keys);
    keys = 0;
    Iterator it = weakMap.keySet().iterator();
    while (it.hasNext()) {
      keys++;
      System.out.println("Point " + it.next().toString() + " was not dropped.");
    }
    System.out.println("Points remaining in the weakMap: " + keys);
    assertFalse("There are " + keys + " TracePoint2D instances not deleted from the WeakHashMap.",
        keys > this.trace.getMaxSize());
  }

  // ////////////////////////////
  // Worker classes
  // ////////////////////////////

  class Producer extends TestMultithreading.Producer {
    /** The amount to add. */
    private long m_toAdd;

    /** The maximum of milliseconds between two add operations. */
    private long m_sleepRange;

    /** Flag to stop the producer. */
    private boolean m_stop = false;

    /**
     * <p>
     * Constructs a producer that will add <code>toAdd</code> points with
     * random breaks of milliseconds between <code>maxSleep</code> and zero.
     * </p>
     *
     * @param toAdd
     *          the amount of points to add
     * @param sleepRange
     *          the maxium time in milliseconds the Thread will sleep between
     *          two points added
     */
    Producer(final long toAdd, final long sleepRange) {
      super(toAdd, sleepRange);
    }

    public void run() {
      TracePoint2D point;
      ITrace2D tmpTrace;
      while (this.m_toAdd > 0 && !this.m_stop) {
        try {
          sleep((long) (Math.random() * this.m_sleepRange));
        } catch (InterruptedException e) {
          e.printStackTrace();
          this.m_stop = true;
        }
        tmpTrace = TestMultiThreadingAndTracing.this.pickRandomTrace();
        if (this.m_toAdd % 10 == 0) {
          System.out.println('[' + this.getName() + "] adding point to " + tmpTrace.getName()
              + "... " + this.m_toAdd + " to go...");
        }
        point = new TracePoint2D(m_toAdd, m_toAdd);
        TestMultiThreadingAndTracing.this.weakMap.put(point, point.toString());
        tmpTrace.addPoint(point);
        this.m_toAdd--;
      }
    }
  }

  class Consumer extends TestMultithreading.Consumer {
    /** The maximum of milliseconds between two add operations. */
    private long m_sleepRange;

    /**
     * Creates an instance that will take a random break between 0 and
     * <code>sleepRange</code> milliseconds between two add operations.
     * <p>
     *
     * @param sleepRange
     *          the maximum of milliseconds between two add operations.
     */
    Consumer(final long sleepRange) {
      super(sleepRange);
    }

    private boolean stop = false;

    public void run() {
      MockGraphics2D mockGraphics = new MockGraphics2D();
      while (!(this.stop || TestMultiThreadingAndTracing.this.isAllProducersFinished())) {
        try {
          sleep((long) (Math.random() * this.m_sleepRange));
        } catch (InterruptedException e) {
          e.printStackTrace();
          this.stop = true;
        }
        System.out.println('[' + this.getClass().getName() + "] painting...");
        chart.paint(mockGraphics);
      }
    }

  }

}
