/*
 *  TestChart2D.java of project jchart2d, junit tests for Chart2D.
 *  Copyright 2006 (C) Achim Westermann, created on 14:32:20.
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
package info.monitorenter.gui.chart;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import java.util.WeakHashMap;

import junit.framework.TestCase;

/**
 * Junit testcase for {@link info.monitorenter.gui.chart.Chart2D}.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.2 $
 */
public class TestChart2D extends TestCase {

  /**
   * Creates several charts, adds a trace to each of them, destroys the chart
   * and checks, if a memory leak occurs.
   * <p>
   */
  public void testMemoryLeak() {
    Chart2D chart;
    ITrace2D trace;
    WeakHashMap chartMap = new WeakHashMap();
    for (int i = 0; i < 50; i++) {
      chart = new Chart2D();
      trace = new Trace2DLtd(100000);
      chart.addTrace(trace);
      chartMap.put(chart, null);
      chart.destroy();
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    chart = null;
    trace = null;
    System.runFinalization();
    System.gc();
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.runFinalization();
    System.gc();
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertEquals(0, chartMap.size());

  }

  /**
   * Junit test ui runner.
   * <p>
   * 
   * @param args
   *          ignored.
   */
  public static void main(final String[] args) {
    junit.swingui.TestRunner.run(TestChart2D.class);
  }

}
