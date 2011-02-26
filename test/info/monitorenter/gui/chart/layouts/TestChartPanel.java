/*
 *  TestChartPanelMemoryLeak.java of project jchart2d, a 
 *  test for a memory leak detected by Pieter-Jan Busschaert.
 *  Copyright (c) 2007 Achim Westermann, created on 23:52:11.
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
package info.monitorenter.gui.chart.layouts;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.views.ChartPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * TestChartPanelMemoryLeak.java of project jchart2d, a test for a memory leak
 * detected by Pieter-Jan Busschaert.
 * <p>
 * 
 * @author Pieter-Jan Busschaert
 * @author Holger Brandl
 * 
 * 
 * @version $Revision: 1.5 $
 */
public class TestChartPanel
    extends TestCase {
  /**
   * Main debug / profiler entry, do not drop - it's used from build xml to
   * profile.
   * <p>
   * 
   * @param args
   *            ignored.
   * 
   * @throws Exception
   *             if sth. goes wrong.
   */
  public static void main(final String[] args) throws Exception {
    TestChartPanel test = new TestChartPanel(TestChartPanel.class.getName());
    test.setUp();
    test.testMemoryLeak();
    test.tearDown();
  }

  /**
   * Test suite for this test class.
   * <p>
   * 
   * @return the test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    suite.setName(TestChartPanel.class.getName());

    suite.addTest(new TestChartPanel("testNoTraces"));
    suite.addTest(new TestChartPanel("testManyTraces"));
    suite.addTest(new TestChartPanel("testMemoryLeak"));

    return suite;
  }

  /**
   * Default constructor with test name.
   * <p>
   * 
   * @param testName
   *            the test name.
   */
  public TestChartPanel(final String testName) {
    super(testName);
  }

  /**
   * Creates a <code>{@link ChartPanel}</code> with a
   * <code>{@link Chart2D}</code> that has many traces and shows it for some
   * seconds.
   * <p>
   * Warning: For now this is only a visual test, especially for seeing if the
   * space for the labels works correctly. This test will never fail if sth. is
   * wrong. So watch it!
   * <p>
   * 
   */
  public void testManyTraces() {
    Chart2D chart = new Chart2D();

    // add many traces:
    ITrace2D trace;
    for (int i = 0; i < 10; i++) {
      trace = new Trace2DSimple();
      chart.addTrace(trace);
      for (int j = 0; j < 20; j++) {
        trace.addPoint(j, (Math.random() + 1) * j);
      }
      trace.setName("trace-" + i);
    }

    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(new ChartPanel(chart));

    JFrame frame = new JFrame();
    frame.getContentPane().add(p);

    // frame.validate();
    frame.setSize(new Dimension(400, 400));
    frame.setVisible(true);
    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    frame.setVisible(false);
    frame.dispose();
  }

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
    int before = propertyChangeListeners.length;
    System.out.println("Before chart.setBackground(Color):");
    System.out.println("chart.propertyChangeListeners().length: " + before);
    System.runFinalization();
    System.gc();
    chart.setBackground(Color.LIGHT_GRAY);
    propertyChangeListeners = chart.getPropertyChangeListeners();
    int after = propertyChangeListeners.length;
    System.out.println("After chart.setBackground(Color):");
    System.out.println("chart.propertyChangeListeners().length: " + propertyChangeListeners.length);
    Assert.assertTrue(after < before);

    // reporting / analysis
    Map<Class< ? >, Integer> classes2count = new HashMap<Class< ? >, Integer>();
    Map<String, Integer> props2count = new HashMap<String, Integer>();
    Integer count;
    Class< ? > clazz;
    String property;
    for (int i = propertyChangeListeners.length - 1; i >= 0; i--) {
      System.out.println(propertyChangeListeners[i].getClass().getName());
      clazz = propertyChangeListeners[i].getClass();
      // count the properties:
      if (clazz == PropertyChangeListenerProxy.class) {
        property = ((PropertyChangeListenerProxy) propertyChangeListeners[i]).getPropertyName();
        count = props2count.get(property);
        if (count == null) {
          count = new Integer(1);
        } else {
          count = new Integer(count.intValue() + 1);
        }
        props2count.put(property, count);
      }
      count = null;
      // count the listener classes:
      count = classes2count.get(clazz);
      if (count == null) {
        count = new Integer(1);
      } else {
        count = new Integer(count.intValue() + 1);
      }
      classes2count.put(clazz, count);
    }

    for (Map.Entry<Class< ? >, Integer> entry : classes2count.entrySet()) {
      System.out.println((entry.getKey()).getName() + " : " + entry.getValue());
    }
    System.out.println();
    for (Map.Entry<String, Integer> entry : props2count.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }
  }

  /**
   * Creates a <code>{@link ChartPanel}</code> with a
   * <code>{@link Chart2D}</code> that has no traces and shows it for some
   * seconds.
   * <p>
   * 
   */
  public void testNoTraces() {
    Chart2D chart = new Chart2D();

    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(new ChartPanel(chart));

    JFrame frame = new JFrame();
    frame.getContentPane().add(p);

    frame.validate();
    frame.setSize(new Dimension(400, 400));
    frame.setVisible(true);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    frame.setVisible(false);
    frame.dispose();
  }

}
