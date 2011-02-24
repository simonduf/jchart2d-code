/*
 *
 *  RangePolicyMinimumViewport.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 11:12:12
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
 * <p>
 * A <code>{@link aw.gui.chart.IRangePolicy}</code> implementation that
 * guarantees a minimum displayed range (viewport) but will stretch if values of
 * the corresponding <code>{@link aw.gui.chart.Chart2D}</code> exceeds these
 * constructor given bounds.
 * </p>
 * <p>
 * To sum up the policy of this implementation this
 * <code>{@link aw.gui.chart.IRangePolicy}</code>
 * <ol>
 * <li>Guarantees to always display the constructor given range.
 * <li>Guarantees to always display every value within the
 * <code>{@link aw.gui.chart.Chart2D}</code> (every
 * <code>{@link aw.gui.chart.TracePoint2D}</code> of the chart's
 * <code>{@link aw.gui.chart.ITrace2D}</code> instances).
 * </ol>
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.7 $
 */
public final class RangePolicyMinimumViewport extends AbstractRangePolicy {

  /**
   * <p>
   * Constructs an instance that will always ensure that the given range will be
   * displayed.
   * </p>
   *
   * @param range
   *          the range that always should be visible.
   */
  public RangePolicyMinimumViewport(final Range range) {
    super(range);
  }

  /**
   * Returns the maximum of the chart or of the internal range if greater.
   * <p>
   *
   * @param chartMin
   *          ignored.
   *
   * @param chartMax
   *          returned if greater than the value of the internal range.
   *
   * @return Math.max(this.range.getMax(), chartMax).
   *
   * @see aw.gui.chart.IRangePolicy#getMax(double, double)
   */
  public double getMax(final double chartMin, final double chartMax) {
    return Math.max(this.getRange().getMax(), chartMax);
  }

  /**
   * @see aw.gui.chart.IRangePolicy#getMin(double, double)
   */
  public double getMin(final double chartMin, final double chartMax) {
    return Math.min(this.getRange().getMin(), chartMin);
  }
}
