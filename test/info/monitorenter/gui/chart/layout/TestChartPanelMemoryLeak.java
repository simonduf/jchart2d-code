/*
 *  TestChartPanelMemoryLeak.java of project jchart2d, a 
 *  test for a memory leak detected by Pieter-Jan Busschaert.
 *  Copyright 2006 (C) Achim Westermann, created on 23:52:11.
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
package info.monitorenter.gui.chart.layout;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

/**
 * 
 * TestChartPanelMemoryLeak.java of project jchart2d, a test for a memory leak
 * detected by Pieter-Jan Busschaert.
 * <p>
 * 
 * @author Pieter-Jan Busschaert
 * 
 * 
 * @version $Revision: 1.3 $
 */
public class TestChartPanelMemoryLeak extends TestCase {
  /**
   * Tests a memory leak that was found in jchart2d-1.1.0 and was related to
   * adding and removing traces with charts wrapped in a {@link ChartPanel}.
   * <p>
   * 
   */

  public void testMemoryLeak() {
    Chart2D chart = new Chart2D();
    ChartPanel chartPanel = new ChartPanel(chart);
    // this call is for fooling checkstyle (unused local variable):
    chartPanel.setEnabled(false);
    ITrace2D trace;
    for (int i = 0; i < 500; i++) {
      trace = new Trace2DLtd(16383);
      chart.addTrace(trace);
      try {
        if (i % 20 == 0) {
          System.gc();
          System.runFinalization();
        }
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace(System.err);
      }
      chart.removeTrace(trace);
    }
    

    PropertyChangeListener[] propertyChangeListeners = chart.getPropertyChangeListeners();
    System.out.println("Before chart.setBackground(Color):");
    System.out.println("chart.propertyChangeListeners().length: " + propertyChangeListeners.length);
    System.runFinalization();
    System.gc();
    chart.setBackground(Color.LIGHT_GRAY);
    propertyChangeListeners = chart.getPropertyChangeListeners();
    System.out.println("After chart.setBackground(Color):");
    System.out.println("chart.propertyChangeListeners().length: " + propertyChangeListeners.length);

    Map classes2count = new HashMap();
    Map props2count = new HashMap();
    Integer count;
    Class clazz;
    String property;
    for (int i = propertyChangeListeners.length - 1; i >= 0; i--) {
      clazz = propertyChangeListeners[i].getClass();
      // count the properties:
      if (clazz == PropertyChangeListenerProxy.class) {
        property = ((PropertyChangeListenerProxy) propertyChangeListeners[i]).getPropertyName();
        count = (Integer) props2count.get(property);
        if (count == null) {
          count = new Integer(1);
        } else {
          count = new Integer(count.intValue() + 1);
        }
        props2count.put(property, count);
      }
      count = null;
      // count the listener classes:
      count = (Integer) classes2count.get(clazz);
      if (count == null) {
        count = new Integer(1);
      } else {
        count = new Integer(count.intValue() + 1);
      }
      classes2count.put(clazz, count);
    }
    Map.Entry entry;

    for (Iterator it = classes2count.entrySet().iterator(); it.hasNext();) {
      entry = (Map.Entry) it.next();
      System.out.println(((Class) entry.getKey()).getName() + " : " + entry.getValue());
    }
    System.out.println();
    for (Iterator it = props2count.entrySet().iterator(); it.hasNext();) {
      entry = (Map.Entry) it.next();
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }
  }

  /**
   * Main debug / profiler entry, do not drop - it's used from build xml to
   * profile.
   * <p>
   * 
   * @param args
   *          ignored.
   * 
   * @throws Exception
   *           if sth. goes wrong.
   */
  public static void main(final String[] args) throws Exception {
    TestChartPanelMemoryLeak test = new TestChartPanelMemoryLeak();
    test.setUp();
    test.testMemoryLeak();
    test.tearDown();
  }
}
