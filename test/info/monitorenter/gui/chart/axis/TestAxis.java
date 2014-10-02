/*
 *  TestAxis.java of project jchart2d, Junit test case for 
 *  IAxis implementations.
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
package info.monitorenter.gui.chart.axis;

import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.IAxisScalePolicy;
import info.monitorenter.gui.chart.IRangePolicy;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.test.ATestJChart2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.util.Range;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Junit test for <code>{@link info.monitorenter.gui.chart.IAxis}</code>
 * implementations.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 */
public class TestAxis extends ATestJChart2D {
 

  /**
   * Constructor with the test name.
   * <p>
   * 
   * @param testname
   *          the test name.
   */
  public TestAxis(final String testname) {
    super(testname);
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#createAxisX()
   */
  @Override
  protected AAxis<?> createAxisX() {
    return new AxisLinear<IAxisScalePolicy>();
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#createAxisY()
   */
  @Override
  protected AAxis<?> createAxisY() {
    return this.createAxisX();
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#createTraces()
   */
  @Override
  protected ITrace2D[] createTraces() {
    return new ITrace2D[] {new Trace2DSimple()};
  }

  /**
   * @see info.monitorenter.gui.chart.test.ATestJChart2D#fillTrace(info.monitorenter.gui.chart.ITrace2D)
   */
  @Override
  protected void fillTrace(final ITrace2D trace2D) {
    for (int i = 0; i < 101; i++) {
      trace2D.addPoint(i, i);
    }
  }

  /**
   * Tests <code>{@link AAxis#setRangePolicy(IRangePolicy)}</code> with a <code>
   * {@link RangePolicyFixedViewport}</code>.
   * <p>
   */
  @Test
  public void testSetRangePolicyFixedViewPort() {
    Range range = new Range(1, 2);
    IRangePolicy rangePolicy = new RangePolicyFixedViewport(range);
    this.m_axisX.setRangePolicy(rangePolicy);
    Range axisRange = this.m_axisX.getRangePolicy().getRange();
    Assert.assertEquals(range.getMin(), axisRange.getMin(), 0);
    Assert.assertSame(range, axisRange);
    this.m_axisY.setRangePolicy(rangePolicy);
    axisRange = this.m_axisY.getRangePolicy().getRange();
    Assert.assertEquals(range.getMin(), axisRange.getMin(), 0);
    Assert.assertSame(range, axisRange);
  }

  /**
   * Tests the method {@link AAxis.AChart2DDataAccessor#translatePxToValue(int)}
   * .
   * <p>
   */
  @Test
  public void testTransformPxToValue() {
    // X-axis
    int pixel = 100;
    double value = this.m_axisX.translatePxToValue(pixel);
    int retransform = this.m_axisX.m_accessor.translateValueToPx(value);
    Assert.assertEquals(pixel, retransform);

    pixel = 222;
    value = this.m_axisX.m_accessor.translatePxToValue(pixel);
    retransform = this.m_axisX.m_accessor.translateValueToPx(value);
    Assert.assertEquals(pixel, retransform);

    pixel = 399;
    value = this.m_axisX.m_accessor.translatePxToValue(pixel);
    retransform = this.m_axisX.m_accessor.translateValueToPx(value);
    Assert.assertEquals(pixel, retransform);

    // direction test: higher x px have to be transformed to lower px:
    pixel = 400;
    double higherValue = this.m_axisX.m_accessor.translatePxToValue(pixel);
    Assert.assertTrue(higherValue > value);

    // Y-axis
    pixel = 100;
    value = this.m_axisY.m_accessor.translatePxToValue(pixel);
    retransform = this.m_axisY.m_accessor.translateValueToPx(value);
    Assert.assertEquals(pixel, retransform);

    pixel = 222;
    value = this.m_axisY.m_accessor.translatePxToValue(pixel);
    retransform = this.m_axisY.m_accessor.translateValueToPx(value);
    Assert.assertEquals(pixel, retransform);

    // Direction test: higher y px have to be transformed to lower values,
    // as y starts from top in awt:
    pixel = 300;
    higherValue = this.m_axisY.m_accessor.translatePxToValue(pixel);
    Assert.assertTrue(higherValue < value);

  }

  /**
   * Tests the method {@link AAxis.AChart2DDataAccessor#translatePxToValue(int)}
   * .
   * <p>
   */
  @Test
  public void testTransformValueToPx() {
    double value = 50;
    int pixel = this.m_axisX.m_accessor.translateValueToPx(value);
    double retransform = this.m_axisX.m_accessor.translatePxToValue(pixel);
    Assert.assertEquals(value, retransform, 3);

    // the first transformation value to px will cause a change (int rounding)
    // the 2nd try should be exact as the value matches an exact px:
    value = retransform;
    pixel = this.m_axisX.m_accessor.translateValueToPx(value);
    retransform = this.m_axisX.m_accessor.translatePxToValue(pixel);
    Assert.assertEquals(value, retransform, 0);

    // y Axis
    value = 50;
    pixel = this.m_axisY.m_accessor.translateValueToPx(value);
    retransform = this.m_axisY.m_accessor.translatePxToValue(pixel);
    Assert.assertEquals(value, retransform, 3);

    // the first transformation value to px will cause a change (int rounding)
    // the 2nd try should be exact as the value matches an exact px:
    value = retransform;
    pixel = this.m_axisY.m_accessor.translateValueToPx(value);
    retransform = this.m_axisY.m_accessor.translatePxToValue(pixel);
    Assert.assertEquals(value, retransform, 0);
  }
  
  /**
   * Tests axis dimensions with {@link IAxis#setPaintScale(boolean)} set 
   * to false.
   * <p>
   */
  @Test
  public void testPaintScaleFalse() {
    this.m_axisY.setPaintScale(false);
    BufferedImage image=new BufferedImage(400,400,BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics=(Graphics2D)image.getGraphics();
    int axisYWidth = this.m_axisY.getWidth(graphics);
    assertEquals("Y axis without paintScale is too wide.",13, axisYWidth);
    this.m_axisX.setPaintScale(false);
    int axisXHeight = this.m_axisX.getHeight(graphics);
    assertEquals("X axis without paintScale is too tall.",14, axisXHeight);
    
  }

  /**
   * Tests axis dimensions with {@link IAxis#setPaintScale(boolean)} set 
   * to true.
   * <p>
   */
  @Test
  public void testPaintScaleTrue() {
    BufferedImage image=new BufferedImage(400,400,BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics=(Graphics2D)image.getGraphics();
    int axisYWidth = this.m_axisY.getWidth(graphics);
    assertEquals("Y axis without paintScale is too small.",34, axisYWidth);
    int axisXHeight = this.m_axisX.getHeight(graphics);
    assertEquals("X axis without paintScale is too small.",13, axisXHeight);
  }

}
