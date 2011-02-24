/*
 *  LabelFormatterDate.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 20.04.2005, 11:56:36
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
package aw.gui.chart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import aw.util.Range;
import aw.util.SimpleDateFormatAnalyzer;

/**
 * <p>
 * An ILabelFormatter instance that uses a {@link java.text.DateFormat} to
 * format the labels.
 * </p>
 * <p>
 * <b>Caution: <br>
 * It only makes sense to use this class if the data of the corresponding axis
 * may be interpreted as long number of milliseconds since the standard base
 * time known as "the epoch", namely January 1, 1970, 00:00:00 GMT. </b>
 * <p>
 * <p>
 * <b>Caution: <br>
 * This implentation is not completly conformat with the constraint: <code>
 * instance.parse(instance.format(value)) == value
 * </code>
 * </b> This only works for subsequent call: one call to format contains the
 * next value to return from parse to be the same as the format. That value is
 * cached as date / time formatting produces truncation of the internal value
 * (e.g. if no year is displayed). <br>
 * Use: <br>
 *
 * <pre>
 *
 *     Chart2D chart = new &lt;Constructor&gt;
 *     Axis axis = new AxisSimple();
 *     axis.setFormatter(new LabelFormatterDate(DateFormat.getDateInstance()));
 *     chart.setAxisX(axis);
 *
 * </pre>
 *
 * to use this class.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.12 $
 *
 * @see java.util.Date
 */
public class LabelFormatterDate extends AbstractLabelFormatter implements ILabelFormatter {
  /** The cached maximum amount of characters that will be used. */
  private int m_cachedMaxAmountChars = Integer.MAX_VALUE;

  /** The date formatter that is used. */
  private SimpleDateFormat m_df;

  /** The last value that was formatted - needed for the parse - format contract. */
  private double m_lastFormatted = 0;

  /**
   * Creates a formatter that uses the given date format.
   * <p>
   *
   * @param dateFormat
   *          the date format to use.
   */
  public LabelFormatterDate(final SimpleDateFormat dateFormat) {
    super();
    this.m_df = dateFormat;
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#format(double)
   */
  public String format(final double value) {
    this.m_lastFormatted = value;
    return this.m_df.format(new Date((long) value));
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#getMaxAmountChars()
   */
  public int getMaxAmountChars() {
    Range range = this.getAxis().getRange();
    double dRange = range.getExtent();
    if (this.m_cachedMaxAmountChars == Integer.MAX_VALUE) {
      this.m_cachedMaxAmountChars = this.m_df.format(new Date((long) dRange)).length();
    }
    return this.m_cachedMaxAmountChars;
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#getMinimumValueShiftForChange()
   */
  public double getMinimumValueShiftForChange() {
    double ret = 0;
    if (SimpleDateFormatAnalyzer.displaysMillisecond(this.m_df)) {
      ret = 1;
    } else if (SimpleDateFormatAnalyzer.displaysSecond(this.m_df)) {
      ret = 1000;
    } else if (SimpleDateFormatAnalyzer.displaysMinute(this.m_df)) {
      ret = 60000;
    } else if (SimpleDateFormatAnalyzer.displaysHour(this.m_df)) {
      ret = 360000;
    } else if (SimpleDateFormatAnalyzer.displaysDay(this.m_df)) {
      ret = 24 * 360000;
    } else if (SimpleDateFormatAnalyzer.displaysMonth(this.m_df)) {
      ret = 31 * 24 * 360000;
    } else {
      ret = 12 * 31 * 24 * 60000;
    }
    return ret;
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#getNextEvenValue(double, boolean)
   */
  public double getNextEvenValue(final double value, final boolean ceiling) {
    Date d = new Date((long) value);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d);
    if (ceiling) {
      if (!SimpleDateFormatAnalyzer.displaysMillisecond(this.m_df)) {
        calendar.set(Calendar.MILLISECOND, 0);
        if (!SimpleDateFormatAnalyzer.displaysSecond(this.m_df)) {
          calendar.set(Calendar.SECOND, 0);
          if (!SimpleDateFormatAnalyzer.displaysMinute(this.m_df)) {
            calendar.set(Calendar.MINUTE, 0);
            if (!SimpleDateFormatAnalyzer.displaysHour(this.m_df)) {
              calendar.set(Calendar.HOUR, 0);
              if (!SimpleDateFormatAnalyzer.displaysDay(this.m_df)) {
                calendar.set(Calendar.DAY_OF_YEAR, 0);
                if (!SimpleDateFormatAnalyzer.displaysMonth(this.m_df)) {
                  calendar.set(Calendar.MONTH, 0);
                  if (!SimpleDateFormatAnalyzer.displaysYear(this.m_df)) {
                    calendar.set(Calendar.YEAR, 0);
                  }
                }
              }
            }
          }
        }
      }
    } else {
      if (!SimpleDateFormatAnalyzer.displaysMillisecond(this.m_df)) {
        calendar.set(Calendar.MILLISECOND, 1000);
        if (!SimpleDateFormatAnalyzer.displaysSecond(this.m_df)) {
          calendar.set(Calendar.SECOND, 60);
          if (!SimpleDateFormatAnalyzer.displaysMinute(this.m_df)) {
            calendar.set(Calendar.MINUTE, 60);
            if (!SimpleDateFormatAnalyzer.displaysHour(this.m_df)) {
              calendar.set(Calendar.HOUR, 24);
              if (!SimpleDateFormatAnalyzer.displaysDay(this.m_df)) {
                calendar.set(Calendar.DAY_OF_YEAR, 365);
                if (!SimpleDateFormatAnalyzer.displaysMonth(this.m_df)) {
                  calendar.set(Calendar.MONTH, 12);
                  if (!SimpleDateFormatAnalyzer.displaysYear(this.m_df)) {
                    calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                  }
                }
              }
            }
          }
        }
      }
    }
    return calendar.getTimeInMillis();
  }

  /**
   * @see aw.gui.chart.ILabelFormatter#parse(java.lang.String)
   */
  public Number parse(final String formatted) throws NumberFormatException {
    return new Double(this.m_lastFormatted);
  }
}
