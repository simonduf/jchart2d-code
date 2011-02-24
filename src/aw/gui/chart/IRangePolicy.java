/*
 *
 *  IRangePolicy.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 10:23:32
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
 * An interface that allows an Axis to be plugged with a range policy.
 * </p>
 * <p>
 * Implementations may limit the range of the underlying Chart2D's data
 * (clipping / zooming), increase it (void space offset), guarantee a minimum
 * viewport... .
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.4 $
 *
 * @see aw.gui.chart.Axis
 */
public interface IRangePolicy {

  /**
   * Define the upper bound of the Chart2D's value range. Depends on the
   * {@link Axis}this instance is bound to.
   *
   * @param chartMin
   *          the minimum value of the connected Chart2D that may / should be
   *          taken into account.
   *
   * @param chartMax
   *          the maximum value of the connected Chart2D that may / should be
   *          taken into account.
   *
   * @return the maximum value (upper bound) for the Chart2D to display.
   *
   */
  public double getMax(double chartMin, double chartMax);

  /**
   * Define the lower bound of the Chart2D's value range. Depends on the
   * {@link Axis}this instance is bound to.
   *
   * @param chartMin
   *          the minimum value of the connected Chart2D that may / should be
   *          taken into account.
   *
   * @param chartMax
   *          the maximum value of the connected Chart2D that may / should be
   *          taken into account.
   *
   * @return the minimum value (lower bound) for the Chart2D to display.
   *
   */
  public double getMin(double chartMin, double chartMax);

  /**
   * Get the range of this range policy.
   * <p>
   *
   * @return he range of this range policy
   */
  public Range getRange();

  /**
   * Set the range of this RangePolicy.
   * <p>
   *
   * @param range
   *          the Range for the range policy.
   */
  public void setRange(Range range);
}
