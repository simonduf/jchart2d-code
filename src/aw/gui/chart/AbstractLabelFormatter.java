/*
 * AbstractLabelFormatter.java,  <enter purpose here>.
 * Copyright (C) 2005  Achim Westermann, Achim.Westermann@gmx.de
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
 */
package aw.gui.chart;

import aw.util.Range;
import aw.util.units.Unit;
import aw.util.units.UnitUnchanged;

/**
 * <p>
 * A label formatter that is aware of the {@link aw.gui.chart.Axis} it formats
 * label for.
 * </p>
 * <p>
 * This allows to compute the amount of fraction digits needed from the range to
 * display.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.7 $
 *
 */
public abstract class AbstractLabelFormatter implements ILabelFormatter {

  /**
   * The default unit with the factor 1 that is returned as the default for
   * {@link #getUnit()}.
   */
  public static final Unit UNIT_UNCHANGED = new UnitUnchanged();

  /** The corresponding axis to format for. */
  private Axis m_axis;

  /**
   * Default constructor.
   * <p>
   */
  protected AbstractLabelFormatter() {
    // nop
  }

  /**
   * Intended for {@link Axis} only.
   * <p>
   *
   * @return Returns the axis.
   */
  protected Axis getAxis() {
    return this.m_axis;
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#getMaxAmountChars()
   */
  public int getMaxAmountChars() {
    // find the fractions by using range information:
    int fractionDigits = 0;
    Range range = this.m_axis.getRange();
    double dRange = range.getExtent();
    if (dRange < 1) {
      if (dRange == 0) {
        fractionDigits = 1;
      } else {
        // find the power
        while (dRange < 1) {
          dRange *= 10;
          fractionDigits++;
        }
      }
    } else {
      if (dRange == 0) {
        dRange = 1;
      }
      if (dRange < 10) {
        fractionDigits = 2;
      } else if (dRange < 100) {
        fractionDigits = 1;
      } else {
        fractionDigits = 0;
      }
    }

    // find integer digits by using longest value:
    int integerDigits = 0;
    double max = range.getMax();
    double min = Math.abs(range.getMin());
    if (max == 0 && min == 0) {
      integerDigits = 1;
    } else if (max < min) {
      while (min > 1) {
        min /= 10;
        integerDigits++;
      }
    } else {
      while (max > 1) {
        max /= 10;
        integerDigits++;
      }
    }

    // <sign> integerDigits <dot> fractionDigits:
    return 1 + integerDigits + 1 + fractionDigits;
  }

  /**
   * Returns {@link #UNIT_UNCHANGED}.
   * <p>
   *
   * @return {@link #UNIT_UNCHANGED}
   *
   * @see aw.gui.chart.ILabelFormatter#getUnit()
   */
  public Unit getUnit() {

    return AbstractLabelFormatter.UNIT_UNCHANGED;
  }

  /**
   * Void adapter method implementation - optional to override.
   * <p>
   *
   * @see aw.gui.chart.ILabelFormatter#initPaintIteration()
   */
  public void initPaintIteration() {
    // nop adapter
  }

  /**
   * Intended for {@link Axis} only.
   * <p>
   *
   * @param axis
   *          The m_axis to set.
   */
  protected void setAxis(final Axis axis) {
    this.m_axis = axis;
  }

}
