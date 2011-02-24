/*
 *  ATestJChart2D.java, base class for testing Axis functionality. 
 *  Copyright (C) Achim Westermann, created on 23.04.2005, 08:21:12
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
package info.monitorenter.gui.chart.test;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IRangePolicy;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.util.Range;

import javax.swing.JFrame;

import junit.framework.TestCase;

/**
 * Base class for testing JChart2D.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public abstract class ATestJChart2D
    extends TestCase {

  /** The x axis configured for the test chart. */
  protected AAxis m_axisX;

  /** The y axis configured for the test chart. */
  protected AAxis m_axisY;

  /** The trace configured for the test chart. */
  protected ITrace2D m_trace;

  /** The test chart. */
  protected Chart2D m_chart;

  /** The frame to show the test chart. */
  protected JFrame m_frame;

  /**
   * Sets up a chart and shows it in a frame.
   * <p>
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    this.m_axisX = this.createAxisX();
    this.m_axisY = this.createAxisY();
    this.m_trace = this.createTrace();
    this.fillTrace(this.m_trace);

    this.m_chart = new Chart2D();
    this.m_chart.setAxisX(this.m_axisX);
    this.m_chart.setAxisY(this.m_axisY);
    this.m_chart.addTrace(this.m_trace);
    assertNotSame(this.m_axisX, this.m_axisY);

    this.m_frame = new JFrame();
    this.m_frame.getContentPane().add(this.m_chart);
//    this.m_frame.add(new ChartPanel(this.m_chart));
    this.m_frame.setSize(400, 600);
    this.m_frame.setVisible(true);
    Thread.sleep(1000);
  }

  /**
   * Template method that fills the configured trace with data.
   * <p>
   * 
   * @param trace2D
   *          this class will use the internal configured trace for the test.
   */
  protected abstract void fillTrace(ITrace2D trace2D);

  /**
   * Implement and return an instance of the type to test.
   * <p>
   * 
   * @return the y axis to test.
   */
  protected abstract AAxis createAxisY();

  /**
   * Implement and return an instance of the type to test.
   * <p>
   * 
   * @return the <code>{@link AAxis}</code> to test.
   */
  protected abstract AAxis createAxisX();

  /**
   * Implement and return an instance of the type to test.
   * <p>
   * 
   * @return the trace to test.
   */
  protected abstract ITrace2D createTrace();

  /**
   * Creates a range policy with fixed viewport and a configured range, sets it
   * to the both axis and tests wether the configured range is modified in
   * bounds.
   * <p>
   * 
   */
  public void testSetRangePolicyFixedViewPort() {
    Range range = new Range(1, 2);
    IRangePolicy rangePolicy = new RangePolicyFixedViewport(range);
    this.m_axisX.setRangePolicy(rangePolicy);
    Range axisRange = this.m_axisX.getRangePolicy().getRange();
    assertEquals(range.getMin(), axisRange.getMin(), 0);
    assertSame(range, axisRange);
    this.m_axisY.setRangePolicy(rangePolicy);
    axisRange = this.m_axisY.getRangePolicy().getRange();
    assertEquals(range.getMin(), axisRange.getMin(), 0);
    assertSame(range, axisRange);
  }

  /**
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.m_frame.setVisible(false);
    this.m_frame.dispose();
  }

  /**
   * Constructor with the test name.
   * <p>
   * 
   * @param arg0
   *          the name of the test.
   */
  public ATestJChart2D(final String arg0) {
    super(arg0);
  }
}
