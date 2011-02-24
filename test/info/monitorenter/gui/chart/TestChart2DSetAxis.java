/*
 *  TestChart2DSetAxis.java of project jchart2d, Junit test to 
 *  ensure API contracts. 
 *  Copyright (c) 2007 Achim Westermann, created on 05.08.2006 17:14:47.
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

import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.axis.AxisLinear;
import info.monitorenter.gui.chart.labelformatters.LabelFormatterDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Junit test for ensuring some contracts of the API.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.11 $
 */
public class TestChart2DSetAxis extends TestCase {

  /**
   * Defcon.
   * <p>
   */
  public TestChart2DSetAxis() {
    // nop
  }

  /**
   * Constructor with test name.
   * <p>
   * 
   * @param testName
   *          the name of the test to create.
   */
  public TestChart2DSetAxis(final String testName) {
    super(testName);
  }

  /**
   * Tests the policy of adding axis to charts.
   * <p>
   * 
   * Checks the old formatter of the x axis and adds a new x axis with a different formatter: 
   * after the call the new axis should have the formatter of the previous axis due to the 
   * replace semantics of the {@link Chart2D#setAxisXBottom(AAxis, int)}.<p>
   * 
   */
  @org.junit.Test
  public void testSetAxis() {
    Chart2D chart = new Chart2D();
    IAxisLabelFormatter oldFormatter = chart.getAxisX().getFormatter();
    AAxis axis = new AxisLinear();
    IAxisLabelFormatter formatter = new LabelFormatterDate((SimpleDateFormat) DateFormat
        .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT));
    axis.setFormatter(formatter);
    chart.setAxisXBottom(axis, 0);
    Assert.assertSame(oldFormatter, chart.getAxisX().getFormatter());

  }
}
