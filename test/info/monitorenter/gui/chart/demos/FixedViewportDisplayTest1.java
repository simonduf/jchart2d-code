/*
 * FixedViewportDisplayTest1.java, Junit human display test.
 * Copyright (c) 2007  Achim Westermann, Achim.Westermann@gmx.de
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 */
package info.monitorenter.gui.chart.demos;

import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.util.Range;

import java.awt.Color;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test implementation that uses a chart with a {@link RangePolicyFixedViewport}
 * with a range from 2 to 5 for both axis and a
 * {@link info.monitorenter.gui.chart.Trace2DSimple}.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * @version $Revision: 1.2 $
 * 
 */
public class FixedViewportDisplayTest1
    extends ADisplayTestPropertyDataBased {

  /**
   * Test suite for this test class.
   * <p>
   * 
   * @return the test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    suite.setName(FixedViewportDisplayTest1.class.getName());

    suite.addTest(new FixedViewportDisplayTest1("testDisplay"));

    return suite;
  }

  /**
   * Creates a test case with the given name.
   * <p>
   * 
   * @param testName
   *          the name of the test case.
   */
  public FixedViewportDisplayTest1(final String testName) {
    super(testName);
  }

  /**
   * Sets up a {@link RangePolicyFixedViewport} with a range from 2 to 5 for
   * both axis.
   * <p>
   * 
   * @see info.monitorenter.gui.chart.demos.ADisplayTest#configure(info.monitorenter.gui.chart.demos.StaticCollectorChart)
   */
  protected void configure(final StaticCollectorChart chart) {
    IAxis axis = chart.getChart().getAxisX();
    axis.setRangePolicy(new RangePolicyFixedViewport());
    axis.setRange(new Range(2, 5));
    axis = chart.getChart().getAxisY();
    axis.setRangePolicy(new RangePolicyFixedViewport());
    axis.setRange(new Range(2, 5));
  }

  /**
   * Returns a {@link Trace2DSimple} that is configured with a custom
   * {@link ITrace2D#setColor(Color)}.
   * <p>
   * 
   * @return a {@link Trace2DSimple} that is configured with a custom
   *         {@link ITrace2D#setColor(Color)}.
   * 
   * @see info.monitorenter.gui.chart.demos.ADisplayTest#createTrace()
   */
  protected ITrace2D createTrace() {
    ITrace2D result = new Trace2DSimple();
    result.setColor(Color.RED);
    return result;
  }

}
