/*
 *
 *  RangePolicyUnbounded.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 10:30:29
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package aw.gui.chart;

import aw.util.Range;

/**
 * A dummy IRangePolicy implementation that reflects the bounds of the connected
 * Chart2D instance.
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.4 $
 */
public final class RangePolicyUnbounded implements IRangePolicy {

  /**
   * @see aw.gui.chart.IRangePolicy#getMax(double,double)
   */
  public double getMax(final double chartMin, final double chartMax) {
    return chartMax;
  }

  /**
   * @see aw.gui.chart.IRangePolicy#getMin(double, double)
   */
  public double getMin(final double chartMin, final double chartMax) {
    return chartMin;
  }

  /**
   * @see aw.gui.chart.IRangePolicy#getRange()
   */
  public Range getRange() {
    return Range.RANGE_UNBOUNDED;
  }

  /**
   * This implementation has no effect: A RangePolicyUnbounded always ignores
   * any ranges.
   * <p>
   *
   * @see aw.gui.chart.IRangePolicy#setRange(aw.util.Range)
   */
  public void setRange(final Range range) {
    // nop
  }
}
