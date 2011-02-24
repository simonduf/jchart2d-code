/*
 *  AxisLinear.java of project jchart2d, Axis implementation with linear display.
 *  Copyright 2006 (C) Achim Westermann, created on 20:33:13.
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

import info.monitorenter.gui.chart.AAxis;
import info.monitorenter.util.Range;

import java.awt.event.MouseEvent;

/**
 * An {@link AAxis} with linear display of values.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.4 $
 */
public class AxisLinear
    extends AAxis {

  /**
   * Defcon.
   * <p>
   */
  public AxisLinear() {
    // nop
  }

  /**
   * @see info.monitorenter.gui.chart.AAxis#getScaledValue(double)
   */
  public double getScaledValue(final double absolute) {
    Range range = this.getRange();
    double scalerX = range.getExtent();
    double result = (absolute - range.getMin()) / scalerX;
    if (result == Double.NaN || Double.isInfinite(result)) {
      result = 0;
    }
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.AAxis#translateMousePosition(java.awt.event.MouseEvent)
   */
  protected double translateMousePosition(final MouseEvent mouseEvent)
      throws IllegalArgumentException {
    return this.getAccessor().translateMousePosition(mouseEvent);
  }
}
