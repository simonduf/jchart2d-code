/*
 *
 *  TestStaticCollectorChart.java  jchart2d
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
package aw.gui.chart.demo;

import aw.gui.chart.Axis;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.RangePolicyHighestValues;
import aw.gui.chart.Trace2DSimple;
import aw.util.Range;

/**
 * Tests the {@link aw.gui.chart.RangePolicyHighestValues}.
 * <p>
 *
 * @author Achim Westermann
 */
public class TestHighestValueRangeViewportPolicy extends AbstractDisplayTest {
  /*
   * (non-Javadoc)
   *
   * @see aw.gui.chart.demo.AbstractDisplayTest#createTrace()
   */
  protected ITrace2D createTrace() {
    return new Trace2DSimple();
  }

  /*
   * (non-Javadoc)
   *
   * @see aw.gui.chart.demo.AbstractDisplayTest#configure(aw.gui.chart.demo.StaticCollectorChart)
   */
  protected void configure(StaticCollectorChart chart) {
    Axis axis = chart.getChart().getAxisX();
    axis.setRangePolicy(new RangePolicyHighestValues(new Range(2, 50), 6.0));
    axis = chart.getChart().getAxisY();
    axis.setRangePolicy(new RangePolicyHighestValues(new Range(2, 10), 6.0));
  }

}
