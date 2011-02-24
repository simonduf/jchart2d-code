/*
 *
 *  AbstractRangePolicy.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 10:34:08
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
 * A default superclass for IRangePolicy implementations that adds support for
 * setting and getting ranges.
 * </p>
 * <p>
 * Should be used by any implementation that really works on the data of ranges
 * (not unbounded ranges). Subclasses should access the internal member range or
 * use {@link #getRange()}.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * @version $Revision: 1.5 $
 *
 */
public abstract class AbstractRangePolicy implements IRangePolicy {
  /**
   * The internal range that may be taken into account for returning bounds from
   * {@link IRangePolicy#getMax(double, double)}and
   * {@link IRangePolicy#getMax(double, double)}.
   * <p>
   */
  private Range m_range;

  /**
   * Creates a range policy backed by the given range.
   * <p>
   *
   * @param range
   *          the range that may be used to decide about the policy of
   *          displaying the range.
   */
  public AbstractRangePolicy(final Range range) {
    this.m_range = range;
  }

  /**
   * Returns the internal range that is used to decide about the policy of
   * displaying the chart.
   * <p>
   *
   * @return the internal range that may be taken into account for returning
   *         bounds from {@link IRangePolicy#getMax(double, double)}and
   *         {@link IRangePolicy#getMax(double, double)}.
   *
   */
  public final Range getRange() {
    return this.m_range;
  }

  /**
   * Sets the internal range that is used to decide about the policy of
   * displaying the chart.
   * <p>
   *
   * @param range
   *          the internal range that may be taken into account for returning
   *          bounds from {@link IRangePolicy#getMax(double, double)}and
   *          {@link IRangePolicy#getMax(double, double)}.
   */
  public final void setRange(final Range range) {
    this.m_range = range;
  }

}
