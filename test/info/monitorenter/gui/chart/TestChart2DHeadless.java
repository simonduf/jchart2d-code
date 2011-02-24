/*
 *  TestChart2DHeadless.java of project jchart2d, junit tests for Chart2D
 *  in headless mode.
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

import info.monitorenter.gui.chart.events.Chart2DActionSaveImageSingleton;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.WeakHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Junit testcase for <code>{@link info.monitorenter.gui.chart.Chart2D}</code>
 * in headless (non-ui) mode.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * @version $Revision: 1.3 $
 */
public class TestChart2DHeadless extends TestCase {

  /**
   * Constructor with test name.
   * <p>
   * 
   * @param testName
   *          the name of the test.
   */
  public TestChart2DHeadless(final String testName) {
    super(testName);
  }

  /**
   * Test suite for this test class.
   * <p>
   * 
   * @return the test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    suite.setName(TestChart2DHeadless.class.getName());

    // suite.addTest(new TestChart2DHeadless("testMemoryLeak"));
    suite.addTest(new TestChart2DHeadless("testSnapshot"));

    return suite;
  }

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
   * Tests the method {@link Chart2D#snapShot()} method in non-UI mode by
   * creating an image of a chart that has not been painted (in UI) before.
   * <p>
   * 
   * @throws IOException
   *           if sth goes wrong in I/O (saving chart, deleting test file,...).
   * 
   */
  public void testSnapshot() throws IOException {
    Chart2D chart;
    ITrace2D trace;
    chart = new Chart2D();
    trace = new Trace2DSimple();
    for (int i = 0; i < 100; i++) {
      trace.addPoint(i, Math.random() + 1 * i);
    }
    chart.addTrace(trace);
    Chart2DActionSaveImageSingleton saver = Chart2DActionSaveImageSingleton.getInstance(chart,
        "BLUE");
    saver.actionPerformed(null);

    final BufferedImage img = chart.snapShot();

    JFrame frame = new JFrame("testShanpshot()");
    JPanel imgPanel = new JPanel() {
      /** serialVersionUID */
      private static final long serialVersionUID = -1171046385909150778L;

      public void paint(final Graphics g) {
        super.paint(g);
        g.drawImage(img, 0, 0, null);
      }
    };
    frame.getContentPane().add(imgPanel);
    frame.setSize(img.getWidth(), img.getHeight());
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
    while (frame.isVisible()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  /**
   * Junit test ui runner.
   * <p>
   * 
   * @param args
   *          ignored.
   */
  public static void main(final String[] args) {
    junit.swingui.TestRunner.run(TestChart2DHeadless.class);
  }

}
