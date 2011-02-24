/*
 *
 *  LabelFormatterNumber.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 22:34:16
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

import java.text.NumberFormat;
import java.text.ParseException;

import aw.util.Range;

/**
 * <p>
 * An ILabelFormatter that is based on a {@link java.text.NumberFormat}
 * </p>
 * <p>
 * To avoid loss of precision please choose a sufficient resolution for your
 * constructor given NumberFormat. Example: If you add new
 * {@link aw.gui.chart.TracePoint2D}instances to the
 * {@link aw.gui.chart.Chart2D}every second, prefer using a NumberFormat that
 * at least formats the seconds like (e.g.):
 *
 * <pre>
 * NumberFormat format = new java.text.SimpleDateFormat(&quot;HH:mm:ss&quot;);
 * </pre>
 *
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.10 $
 */
public class LabelFormatterNumber extends AbstractLabelFormatter implements ILabelFormatter {

  /**
   * The internal cached minimum shift of value required to get to distinct
   * Strings from method <code>{@link #format(double)}</code>. This value is
   * computed once and cached because it's computation is expensive.
   */
  private double m_cachedMinValueShift = Double.MAX_VALUE;

  /** The number format to use. */
  protected NumberFormat m_nf;

  /**
   * Creates a label formatter that uses the given number format.
   * <p>
   *
   * @param numberFormat
   *          the number format to use.
   */
  public LabelFormatterNumber(final NumberFormat numberFormat) {
    super();
    if (numberFormat == null) {
      throw new IllegalArgumentException("Argument numberFormat must not be null.");
    }
    this.m_nf = numberFormat;
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#format(double)
   */
  public String format(final double value) {
    return this.m_nf.format(value);
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#getMaxAmountChars()
   */
  public int getMaxAmountChars() {
    // find the fractions by using range information:
    int fractionDigits = 0;
    Range range = this.getAxis().getRange();
    double dRange = range.getExtent();
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

    // check if the interna numberformat would cut values and cause rendering
    // errors:
    if (integerDigits > this.m_nf.getMaximumIntegerDigits()) {
      this.m_nf.setMaximumIntegerDigits(integerDigits);
    }
    if (fractionDigits > this.m_nf.getMaximumFractionDigits()) {
      this.m_nf.setMaximumFractionDigits(fractionDigits);
    }
    // <sign> integerDigits <dot> fractionDigits:
    return 1 + integerDigits + 1 + fractionDigits;

  }

  /**
   * @see aw.gui.chart.ILabelFormatter#getMinimumValueShiftForChange()
   */
  public double getMinimumValueShiftForChange() {
    if (this.m_cachedMinValueShift == Double.MAX_VALUE) {
      int fractionDigits = this.m_nf.getMaximumFractionDigits();
      this.m_cachedMinValueShift = 1 / Math.pow(10, fractionDigits);
    }
    return this.m_cachedMinValueShift;
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#getNextEvenValue(double, boolean)
   */
  public double getNextEvenValue(final double value, final boolean ceiling) {
    double divisor = Math.pow(10, this.m_nf.getMaximumFractionDigits());
    if (ceiling) {
      return Math.ceil(value * divisor) / divisor;
    } else {
      return Math.floor(value * divisor) / divisor;
    }
  }

  /**
   * Returns the interal <code>NumberFormat</code>.
   * <p>
   *
   * @return the interal <code>NumberFormat</code>.
   *
   */
  NumberFormat getNumberFormat() {
    return this.m_nf;
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#parse(java.lang.String)
   */
  public Number parse(final String formatted) throws NumberFormatException {
    try {
      return this.m_nf.parse(formatted);
    } catch (ParseException pe) {
      throw new NumberFormatException(pe.getMessage());
    }
  }
}
