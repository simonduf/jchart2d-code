/*
 *  CandleStick.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2012, Achim Westermann, created on Oct 9, 2012
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
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
 *
 * File   : $Source: /cvsroot/jchart2d/jchart2d/codetemplates.xml,v $
 * Date   : $Date: 2009/02/24 16:45:41 $
 * Version: $Revision: 1.2 $
 */

package info.monitorenter.gui.chart.tracepoints;

/**
 * Faked tracepoint that adds the properties to contain all data for a
 * candlestick.
 * <p>
 * 
 * See <a target="_blank" href="http://en.wikipedia.org/wiki/Candlestick_chart"
 * >http://en.wikipedia.org/wiki/Candlestick_chart</a>
 * <p>
 * 
 * This implementation only works correctly with a special candlestick point
 * painter.
 * <p>
 * The original {@link #getY()} method is mapped to {@link #getStart()}. The
 * original {@link #getX()} method is sufficient to be related to all other y
 * values as a candlestick has a single point in time (x value).
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public class CandleStick extends TracePoint2D {

  /**
   * Constructor with every argument needed.
   * <p>
   * See <a target="_blank"
   * href="http://en.wikipedia.org/wiki/Candlestick_chart"
   * >http://en.wikipedia.org/wiki/Candlestick_chart</a>
   * <p>
   * 
   * @param xValue
   *          the x coordinate.
   * 
   * @param startY
   *          the start y value.
   * 
   * @param endY
   *          the end y value.
   * 
   * @param highY
   *          the high y value.
   * 
   * @param lowY
   *          the low y value.
   */
  public CandleStick(final double xValue, final double startY, final double endY, final double highY, final double lowY) {
    super(xValue, startY);
    if (startY > highY) {
      throw new IllegalArgumentException("start Y must not be higher than high Y.");
    }
    if (startY < lowY) {
      throw new IllegalArgumentException("start Y must not be lower than low Y.");
    }
    if (endY > highY) {
      throw new IllegalArgumentException("end Y must not be higher than high Y.");

    }
    if (endY < lowY) {
      throw new IllegalArgumentException("endY must not be lower than low Y.");
    }
    this.m_end = endY;
    this.m_high = highY;
    this.m_low = lowY;
  }

  /** Generated <code>serialVersionUID</code>. **/
  private static final long serialVersionUID = -135311007801611830L;

  /** The high y value. **/
  private double m_high;

  /**
   * Returns the high y value.
   * <p>
   * 
   * @return the high y value.
   */
  public double getHigh() {
    return this.m_high;
  }

  /**
   * Returns the low y value.
   * <p>
   * 
   * @return the low y value.
   */
  public double getLow() {
    return this.m_low;
  }

  /**
   * Returns the end y value.
   * <p>
   * 
   * @return the end y value.
   */
  public double getEnd() {
    return this.m_end;
  }

  /**
   * Returns the start y value.
   * <p>
   * 
   * Note: this is the reused inherited {@link #getY()}.
   * <p>
   * 
   * @return the start y value.
   */
  public double getStart() {
    return this.getY();
  }

  /** The low y value. **/
  private double m_low;

  /** The end y value. **/
  private double m_end;

}
