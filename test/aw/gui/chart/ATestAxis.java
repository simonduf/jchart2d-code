/*
 *
 *  TestTrace2DLtd.java  jchart2d
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
package aw.gui.chart;

import java.text.DecimalFormat;

import junit.framework.TestCase;
import aw.util.Range;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 */
public abstract class ATestAxis extends TestCase {

  protected Axis axisX;

  protected Axis axisY;

  protected ITrace2D trace;

  protected Chart2D chart;

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
  }

  /**
   *
   */
  protected void fillTrace(ITrace2D trace2D) {
    for (int i = 7; i < 107; i++) {
      trace.addPoint(i * 11, i * 13131);
    }
  }

  /**
   * Implement and return an instance of the type to test.
   * <p>
   */
  protected abstract Axis createAxisY();

  /**
   * Implement and return an instance of the type to test.
   * <p>
   */
  protected abstract Axis createAxisX();

  /**
   * Implement and return an instance of the type to test.
   * <p>
   */
  protected abstract ITrace2D createTrace();

  /**
   * Tests the method <code>{@link Axis#roundToTicks(double, boolean)}</code>.
   * <p>
   *
   */
  public void testRoundToTicks() {
    double xmin = this.axisX.getMin();
    double roundXmin = this.axisX.roundToTicks(this.trace.getMinX(), true, false).m_value;
    assertTrue("axis.getMin() :" + xmin
        + " has to return the same as axis.roundToTicks(trace.getMin(),true).value : " + roundXmin
        + ".", xmin == roundXmin);
    // several roundings should not modify a value (snap to the rounded
    // ticks).
    double roundXmin2 = this.axisX.roundToTicks(roundXmin, true, false).m_value;
    assertTrue("rounding a value that was rounded before should remain (snap to the tick).",
        roundXmin == roundXmin2);

    // compare with range:
    Range xRange = this.axisX.getRange();
    assertTrue("axis.getRange().getMin() has to be equal to axis.getMin().",
        xRange.getMin() == xmin);

    // runtime dynamic test:

    // (should follow here...)
  }

  public void testTicks() {
    // configure to the expected result
    this.axisX.setMajorTickSpacing(5);
    this.axisX.setMinorTickSpacing(1);
    this.axisX.setFormatter(new LabelFormatterNumber(new DecimalFormat("#")));
    LabeledValue[] labels = this.axisX.getScaleValues();
    // set points we expect
    this.trace.removeAllPoints();
    for (int i = 0; i < 100; i++) {
      this.trace.addPoint(i, i);
    }
    // test
    LabeledValue label;
    for (int i = 0; i < labels.length; i++) {
      label = labels[i];
      if (label.m_value % 5 == 0) {
        assertTrue("Label " + label.m_value + " should be a major tick.", label.m_isMajorTick);
      } else if (label.m_value % 1 == 0) {
        assertFalse("Label " + label.m_value + " should be a minor tick.", label.m_isMajorTick);

      } else {
        fail("Label " + label.m_value + " is not a multiple of majorTicks (5) or minorTicks (1).");
      }
    }

  }
}
