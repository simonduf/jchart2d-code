/*
 *
 *  TestAxisAutoUnit.java  jchart2d
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

import junit.framework.Test;
import junit.framework.TestSuite;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.TestChart2D;
import info.monitorenter.gui.chart.TestTrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 */
public class TestAxis extends ATestAxis {
  /**
   * @param arg0
   */
  public TestAxis(String arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }
  /**
   * Test suite for this test class.
   * <p>
   * 
   * @return the test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    suite.setName(TestAxis.class.getName());

    suite.addTest(new TestAxis("testSetRangePolicyFixedViewPort"));
    suite.addTest(new TestAxis("testTransformPxToValue"));
    suite.addTest(new TestAxis("testTransformValueToPx"));

    return suite;
  }
  /* (non-Javadoc)
   * @see info.monitorenter.gui.chart.TestAxis#createAxisY()
   */
  protected AAxis createAxisY() {
    // TODO Auto-generated method stub
    return this.createAxisX();
  }

  /* (non-Javadoc)
   * @see info.monitorenter.gui.chart.TestAxis#createAxisX()
   */
  protected AAxis createAxisX() {
    return new AxisLinear();
  }

  /* (non-Javadoc)
   * @see info.monitorenter.gui.chart.TestAxis#createTrace()
   */
  protected ITrace2D createTrace() {
    return new Trace2DSimple();
  }

}
