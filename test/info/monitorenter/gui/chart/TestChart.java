/*
 *  TestChart.java - Junit test for info.monitorenter.chart.gui.Chart2D. 
 *  Copyright (C) Achim Westermann, created on 16.07.2005, 10:52:43
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

import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.axis.AxisLinear;
import info.monitorenter.gui.chart.test.ATestJChart2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Junit test for <code>{@link Chart2D}</code>.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 */
public class TestChart extends ATestJChart2D {
  /**
   * Tries to serialize a chart.
   * <p>
   * 
   * @throws IOException
   *             if something goes wrong.
   */
  public void testSerializeChart() throws IOException {
    for (int i = 0; i < 100; i++) {
      this.m_trace.addPoint(i, (Math.random() + 1.0) * i);
    }
    java.io.File tmpOut = File.createTempFile("chart", "ser", null);
    tmpOut.deleteOnExit();

    ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(tmpOut));
    objOut.writeObject(this.m_chart);
  }

  /**
   * Constructor with the test name.
   * <p>
   * 
   * @param testname
   *          the test name.
   */
  public TestChart(final String testname) {
    super(testname);
  }

  /**
   * Test suite for this test class.
   * <p>
   * 
   * @return the test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    suite.setName(TestChart.class.getName());

    suite.addTest(new TestChart("testAddInvisibleTrace"));
    suite.addTest(new TestChart("testSetTraceName"));
    suite.addTest(new TestChart("testSerializeChart"));

    return suite;
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#createAxisY()
   */
  @Override
  protected AAxis createAxisY() {
    return this.createAxisX();
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#createAxisX()
   */
  @Override
  protected AAxis createAxisX() {
    return new AxisLinear();
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#createTrace()
   */
  @Override
  protected ITrace2D createTrace() {
    return new Trace2DSimple();
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#fillTrace(info.monitorenter.gui.chart.ITrace2D)
   */
  @Override
  protected void fillTrace(final ITrace2D trace2D) {
    for (int i = 0; i < 101; i++) {
      this.m_trace.addPoint(i, i);
    }
  }

  /**
   * Tests adding an invisible trace for correct scaling.
   * <p>
   * Invisible traces should not be scaled. If they are turned visible
   * afterwards they should be scaled.
   * <p>
   * 
   * 
   * @throws InterruptedException
   *           if sth. goes wrong.
   */
  public void testAddInvisibleTrace() throws InterruptedException {
    ITrace2D trace = new Trace2DSimple();
    for (int i = 20; i < 40; i++) {
      trace.addPoint(i, 70 - i);
    }
    trace.setVisible(false);
    trace.setName("added invisible");
    trace.setColor(Color.BLUE);
    // should call repaint!
    this.m_chart.addTrace(trace);
    Thread.sleep(1000);
    // test all points:
    Iterator<TracePoint2D> itpoints = trace.iterator();
    TracePoint2D point;
    double forbidden = 0.0;
    boolean scaleXDetected = false;
    boolean scaleYDetected = false;

    while (itpoints.hasNext()) {
      point = itpoints.next();
      if (forbidden != point.getScaledX()) {
        scaleXDetected = true;
      }
      if (forbidden != point.getScaledY()) {
        scaleYDetected = true;
      }
      if (scaleXDetected && scaleYDetected) {
        break;
      }
    }
    Assert.assertFalse("Invisible trace was scaled in x dimension", scaleXDetected);
    Assert.assertFalse("Invisible trace was scaled in y dimension", scaleYDetected);

    // now set it visible:
    trace.setVisible(true);
    Thread.sleep(2000);
    itpoints = trace.iterator();
    scaleXDetected = false;
    scaleYDetected = false;

    while (itpoints.hasNext()) {
      point = itpoints.next();
      if (forbidden != point.getScaledX()) {
        scaleXDetected = true;
      }
      if (forbidden != point.getScaledY()) {
        scaleYDetected = true;
      }
      if (scaleXDetected && scaleYDetected) {
        break;
      }
    }
    Assert.assertTrue("Visible trace was not scaled in x dimension", scaleXDetected);
    Assert.assertTrue("Visible trace was not scaled in y dimension", scaleYDetected);

  }

  /**
   * Sets a new name to the trace.
   * <p>
   */
  public void testSetTraceName() {
    this.m_trace.setName("Iphigenie");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
