/*
 * LabelFormatterAutoUnits.java,  <enter purpose here>.
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
package aw.gui.chart;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import aw.util.Range;
import aw.util.units.Unit;
import aw.util.units.UnitFactory;
import aw.util.units.UnitSystemSI;

/**
 * A formatter that adds a "unit-functionality" to a given
 * {@link aw.gui.chart.ILabelFormatter}.
 * <p>
 *
 * The formatted Strings will be divided by a factor according to the automatic
 * chosen Unit.
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.5 $
 *
 */
public class LabelFormatterAutoUnits extends AbstractLabelFormatter {

  /**
   * Performance improvement: Maps the units to use to the powers of 10 of their
   * factor <nobr>(1*10^x = unit.factor) </nobr>.
   * <p>
   *
   * This is used to modifiy the result of {@link #getMaxAmountChars()}as this
   * unit factor will increase or decrease the characters to display.
   * <p>
   *
   */
  private static final Map UNITS_2_POWER = new HashMap();

  static {
    Iterator itUnits = UnitFactory.getInstance().getUnits(UnitSystemSI.getInstance()).iterator();
    Unit unit;
    double factor = 0;
    int power;
    while (itUnits.hasNext()) {
      power = 0;
      unit = (Unit) itUnits.next();
      factor = unit.getFactor();
      if (factor > 1) {
        while (factor > 1) {
          factor /= 10;
          power++;
        }

      } else if (factor < 1) {
        while (factor < 1) {
          factor *= 10;
          power--;
        }
      }
      LabelFormatterAutoUnits.UNITS_2_POWER.put(unit, new Integer(power));
    }
  }

  /**
   * The decorated instance.
   */
  private AbstractLabelFormatter m_delegate;

  /**
   * The internal unit.
   * <p>
   *
   * In this implementation it is only used for finding labels that match the
   * ticks.
   * <p>
   *
   * @see #setMajorTickSpacing(double)
   * @see #setMinorTickSpacing(double)
   */
  private Unit m_unit = UNIT_UNCHANGED;

  /**
   * Creates an instance that will add "unit-functionality" to the given
   * formatter.
   * <p>
   *
   * @param delegate
   *          the formatter that will be decorated with units.
   */
  public LabelFormatterAutoUnits(final AbstractLabelFormatter delegate) {
    super();
    this.m_delegate = delegate;
  }

  /**
   * Internally sets the correct <code>{@link Unit}</code> corresponding to
   * the range of this axis.
   * <p>
   *
   * This is used in this implementations for calculation of the labels.
   * <p>
   *
   * @param min
   *          the minimum value of the axis.
   * @param max
   *          the maximum value of the axis.
   *
   * @see #getScaleValues()
   * @see #setMajorTickSpacing(double)
   * @see #setMinorTickSpacing(double)
   */
  private final void chooseUnit(final double min, final double max) {
    double range = max - min;
    if (range == 0) {
      range = 1;
    }
    this.m_unit = UnitFactory.getInstance().getUnit(range, UnitSystemSI.getInstance());
    if (range / this.m_unit.getFactor() < 3) {
      this.m_unit = this.m_unit.getNexLowerUnit();
    }
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(final Object obj) {
    return this.m_delegate.equals(obj);
  }

  /**
   * @see ILabelFormatter#format(double)
   */
  public String format(final double value) {
    double tmp = value / this.m_unit.getFactor();
    return this.m_delegate.format(tmp);
  }

  /**
   * @see aw.gui.chart.AbstractLabelFormatter#getAxis()
   */
  protected Axis getAxis() {
    return this.m_delegate.getAxis();
  }

  /**
   * @see aw.gui.chart.AbstractLabelFormatter#getMaxAmountChars()
   */
  public int getMaxAmountChars() {
    // find the fractions by using range information:
    int fractionDigits = 0;
    Range range = this.getAxis().getRange();
    double dRange = range.getExtent() / this.m_unit.getFactor();
    if (dRange < 1) {
      if (dRange == 0) {
        fractionDigits = 1;
      } else {
        if (dRange == 0) {
          fractionDigits = 1;
        } else {
          // find the power
          while (dRange < 1) {
            dRange *= 10;
            fractionDigits++;
          }
        }
      }
    } else {
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
    double max = range.getMax() / (this.m_unit.getFactor());
    double min = Math.abs(range.getMin()) / (this.m_unit.getFactor());
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
    // check if the interna numberformat would cut values and cause rendering
    // errors:
    if (this.m_delegate instanceof LabelFormatterNumber) {

      NumberFormat nf = ((LabelFormatterNumber) this.m_delegate).getNumberFormat();
      if (integerDigits > nf.getMaximumIntegerDigits()) {
        nf.setMaximumIntegerDigits(integerDigits);
      }
      if (fractionDigits > nf.getMaximumFractionDigits()) {
        nf.setMaximumFractionDigits(fractionDigits);
      }
    }
    // <sign> integerDigits <dot> fractionDigits:
    return 1 + integerDigits + 1 + fractionDigits;

  }

  /**
   * @see ILabelFormatter#getMaxAmountChars()
   */
  public double getMinimumValueShiftForChange() {
    return this.m_delegate.getMinimumValueShiftForChange() * this.m_unit.getFactor();
  }

  /**
   * @see ILabelFormatter#getNextEvenValue(double, boolean)
   */
  public double getNextEvenValue(final double value, final boolean ceiling) {
    return this.m_delegate.getNextEvenValue(value, ceiling);
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#getUnit()
   */
  public Unit getUnit() {
    return this.m_unit;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return this.m_delegate.hashCode();
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#initPaintIteration()
   */
  public void initPaintIteration() {
    this.chooseUnit(this.m_delegate.getAxis().getMin(), this.m_delegate.getAxis().getMax());
  }

  /**
   * @see ILabelFormatter#parse(String)
   */
  public Number parse(final String formatted) throws NumberFormatException {
    double parsed = this.m_delegate.parse(formatted).doubleValue();
    parsed *= this.m_unit.getFactor();
    return new Double(parsed);
  }

  /**
   * @see aw.gui.chart.AbstractLabelFormatter#setAxis(aw.gui.chart.Axis)
   */
  protected void setAxis(final Axis axis) {

    this.m_delegate.setAxis(axis);
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return this.m_delegate.toString();
  }
}
