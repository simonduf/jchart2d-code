/*
 *  AxisLinear.java of project jchart2d, Axis implementation with linear display.
 *  Copyright (c) 2007 Achim Westermann, created on 20:33:13.
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
package info.monitorenter.gui.chart.axis;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxisLabelFormatter;
import info.monitorenter.util.Range;

/**
 * An <code>{@link AAxis}</code> with linear display of values.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.15 $
 */
public class AxisLinear
    extends AAxis {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = 4725336592625669661L;

  /**
   * Defcon.
   * <p>
   */
  public AxisLinear() {
    // nop
  }

  /**
   * Constructor that uses the given label formatter for formatting labels.
   * <p>
   * 
   * @param formatter
   *          needed for formatting labels of this axis.
   * 
   */
  public AxisLinear(final IAxisLabelFormatter formatter) {
    super(formatter);
  }

  /**
   * @see info.monitorenter.gui.chart.axis.AAxis#createAccessor(info.monitorenter.gui.chart.Chart2D,
   *      int)
   */
  protected AChart2DDataAccessor createAccessor(final Chart2D chart, final int dimension) {
    if (dimension == Chart2D.X) {
      return new AAxis.XDataAccessor(chart);
    } else if (dimension == Chart2D.Y) {
      return new AAxis.YDataAccessor(chart);
    } else {
      throw new IllegalArgumentException("Dimension has to be Chart2D.X or Chart2D.Y!");
    }

  }

  /**
   * @see info.monitorenter.gui.chart.IAxis#getScaledValue(double)
   */
  public double getScaledValue(final double absolute) {
    Range range = this.getRange();
    double scalerX = range.getExtent();
    double result = (absolute - range.getMin()) / scalerX;
    if (Double.isNaN(result) || Double.isInfinite(result)) {
      result = 0;
    }
    return result;
  }
}
