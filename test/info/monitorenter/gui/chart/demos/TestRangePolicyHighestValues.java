/*
 *  RangePolicyHighestValues.java  of projcect jchart2d, 
 *  Junit test for class RangePolicyHighestValues.
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
package info.monitorenter.gui.chart.demos;

import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyHighestValues;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.util.Range;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Junit test for class <code>{@link RangePolicyHighestValues}</code>.
 * <p>
 * 
 * @author Achim Westermann
 */
public class TestRangePolicyHighestValues extends ADisplayTestPropertyDataBased {
  /**
   * Test suite for this test class.
   * <p>
   * 
   * @return the test suite.
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    suite.setName(TestRangePolicyHighestValues.class.getName());

    suite.addTest(new TestRangePolicyHighestValues("testDisplay"));

    return suite;
  }

  /**
   * Creates a test case with the given name.
   * <p>
   * 
   * @param testName
   *          the name of the test case.
   */
  public TestRangePolicyHighestValues(final String testName) {
    super(testName);
  }

  /**
   * @see info.monitorenter.gui.chart.demos.ADisplayTest#configure(info.monitorenter.gui.chart.demos.StaticCollectorChart)
   */
  @Override
  protected void configure(final StaticCollectorChart chart) {
    IAxis axis = chart.getChart().getAxisX();
    axis.setRangePolicy(new RangePolicyHighestValues(new Range(2, 50), 6.0));
    axis = chart.getChart().getAxisY();
    axis.setRangePolicy(new RangePolicyHighestValues(new Range(2, 10), 6.0));
  }

  /**
   * @see info.monitorenter.gui.chart.demos.ADisplayTest#createTrace()
   */
  @Override
  protected ITrace2D createTrace() {
    return new Trace2DSimple();
  }

}
