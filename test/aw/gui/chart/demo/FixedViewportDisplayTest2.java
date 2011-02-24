/*
 * DefaultDisplayTest.java,  <enter purpose here>.
 * Copyright (C) 2006  Achim Westermann, Achim.Westermann@gmx.de
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
package aw.gui.chart.demo;

import java.io.IOException;

import aw.gui.chart.Axis;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.ITracePainter;
import aw.gui.chart.RangePolicyFixedViewport;
import aw.gui.chart.Trace2DSimple;
import aw.gui.chart.TracePainterCompound;
import aw.gui.chart.TracePainterDisc;
import aw.gui.chart.TracePainterLine;
import aw.util.Range;

/**
 * <p>
 * TODO Write a comment ending with '.'
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.4 $
 *
 */
public class FixedViewportDisplayTest2 extends AbstractDisplayTest {

  /*
   * (non-Javadoc)
   *
   * @see aw.gui.chart.demo.AbstractDisplayTest#createTrace()
   */
  protected ITrace2D createTrace() {
    ITrace2D result = new Trace2DSimple();
    result.setTracePainter(new TracePainterCompound(new ITracePainter[] {
        new TracePainterDisc(), new TracePainterLine()}));
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see aw.gui.chart.demo.AbstractDisplayTest#configure(aw.gui.chart.demo.StaticCollectorChart)
   */
  protected void configure(StaticCollectorChart chart) {
    Axis axis = chart.getChart().getAxisX();
    axis.setRangePolicy(new RangePolicyFixedViewport(new Range(0, 7.5)));
    axis = chart.getChart().getAxisY();
    axis.setRangePolicy(new RangePolicyFixedViewport(new Range(0, 7.5)));
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    FixedViewportDisplayTest2 test = new FixedViewportDisplayTest2();
    try {
      test.testDisplay();
    } catch (Throwable f) {
      f.printStackTrace(System.err);
    }
  }
}
