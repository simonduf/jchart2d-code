/*
 *  ATestAxis.java, base class for testing Axis functionality. 
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
package info.monitorenter.gui.chart.axis;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IRangePolicy;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.util.Range;

import javax.swing.JFrame;

import junit.framework.TestCase;

/**
 * Base class for testing Axis functionality.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public abstract class ATestAxis
    extends TestCase {

  protected AAxis axisX;

  protected AAxis axisY;

  protected ITrace2D trace;

  protected Chart2D chart;

  private JFrame m_frame;

  protected void setUp() throws Exception {
    super.setUp();
    this.axisX = this.createAxisX();
    this.axisY = this.createAxisY();
    this.trace = this.createTrace();
    this.fillTrace(this.trace);

    this.chart = new Chart2D();
    this.chart.setAxisX(this.axisX);
    this.chart.setAxisY(this.axisY);
    this.chart.addTrace(this.trace);
    assertNotSame(this.axisX, this.axisY);

    this.m_frame = new JFrame();
    this.m_frame.add(this.chart);
    this.m_frame.setSize(400, 600);
    this.m_frame.setVisible(true);
    Thread.sleep(1000);

  }

  /**
   * 
   */
  protected void fillTrace(ITrace2D trace2D) {
    for (int i = 0; i < 101; i++) {
      trace.addPoint(i, i);
    }
  }

  /**
   * Implement and return an instance of the type to test.
   * <p>
   */
  protected abstract AAxis createAxisY();

  /**
   * Implement and return an instance of the type to test.
   * <p>
   */
  protected abstract AAxis createAxisX();

  /**
   * Implement and return an instance of the type to test.
   * <p>
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
    this.axisX.setRangePolicy(rangePolicy);
    Range axisRange = this.axisX.getRangePolicy().getRange();
    assertEquals(range.getMin(), axisRange.getMin(), 0);
    assertSame(range, axisRange);
    this.axisY.setRangePolicy(rangePolicy);
    axisRange = this.axisY.getRangePolicy().getRange();
    assertEquals(range.getMin(), axisRange.getMin(), 0);
    assertSame(range, axisRange);
  }

  /**
   * Tests the method {@link AAxis.AChart2DDataAccessor#translatePxToValue(int)}.
   * <p>
   * 
   */
  public void testTransformPxToValue() {
    // X-axis
    int pixel = 100;
    double value = this.axisX.translatePxToValue(pixel);
    int retransform = this.axisX.m_accessor.translateValueToPx(value);
    assertEquals(pixel, retransform);

    pixel = 222;
    value = this.axisX.m_accessor.translatePxToValue(pixel);
    retransform = this.axisX.m_accessor.translateValueToPx(value);
    assertEquals(pixel, retransform);
    
    //  direction test: higher x px have to be transformed to lower px:
    pixel = 300;
    double higherValue = this.axisX.m_accessor.translatePxToValue(pixel);
    assertTrue(higherValue > value);
   
    // Y-axis
    pixel = 100;
    value = this.axisY.m_accessor.translatePxToValue(pixel);
    retransform = this.axisY.m_accessor.translateValueToPx(value);
    assertEquals(pixel, retransform);

    pixel = 222;
    value = this.axisY.m_accessor.translatePxToValue(pixel);
    retransform = this.axisY.m_accessor.translateValueToPx(value);
    assertEquals(pixel, retransform);
 
    // Direction test: higher y px have to be transformed to lower values, 
    // as y starts from top in awt:
    pixel = 300;
    higherValue = this.axisY.m_accessor.translatePxToValue(pixel);
    assertTrue(higherValue < value);

  }

  /**
   * Tests the method {@link AAxis.AChart2DDataAccessor#translatePxToValue(int)}.
   * <p>
   * 
   */
  public void testTransformValueToPx() {
    double value = 50;
    int pixel = this.axisX.m_accessor.translateValueToPx(value);
    double retransform = this.axisX.m_accessor.translatePxToValue(pixel);
    assertEquals(value, retransform, 3);
    
    // the first transformation value to px will cause a change (int rounding)
    // the 2nd try should be exact as the value matches an exact px: 
    value = retransform;
    pixel = this.axisX.m_accessor.translateValueToPx(value);
    retransform = this.axisX.m_accessor.translatePxToValue(pixel);
    assertEquals(value, retransform, 0);
    
    // y Axis
    value = 50;
    pixel = this.axisY.m_accessor.translateValueToPx(value);
    retransform = this.axisY.m_accessor.translatePxToValue(pixel);
    assertEquals(value, retransform, 3);
    
    // the first transformation value to px will cause a change (int rounding)
    // the 2nd try should be exact as the value matches an exact px: 
    value = retransform;
    pixel = this.axisY.m_accessor.translateValueToPx(value);
    retransform = this.axisY.m_accessor.translatePxToValue(pixel);
    assertEquals(value, retransform, 0);
    

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
   * @param arg0
   */
  public ATestAxis(String arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }
}
