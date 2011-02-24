/*
 *  AAxisTransformation.java of project jchart2d, 
 *  base class for Axis implementations that transform the scale 
 *  for changed display.  
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
import info.monitorenter.gui.chart.labelformatters.ALabelFormatter;
import info.monitorenter.util.Range;

import java.awt.event.MouseEvent;

/**
 * Base class for Axis implementations that transform the scale for changed
 * display.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.2 $
 */
public abstract class AAxisTransformation extends AAxis {

  /**
   * Creates a default instance that will use a
   * {@link info.monitorenter.gui.chart.labelformatters.LabelFormatterAutoUnits}
   * for formatting labels.
   * <p>
   * 
   */
  public AAxisTransformation() {
    super();
  }

  /**
   * Creates an instance that will the given label formatter for formatting
   * labels.
   * <p>
   * 
   */
  public AAxisTransformation(final ALabelFormatter formatter) {
    super(formatter);
  }

  /**
   * @see info.monitorenter.gui.chart.AAxis#getScaledValue(double)
   */
  protected final double getScaledValue(final double absolute) {
    double result;
    Range range = this.getRange();
    double scaler = range.getExtent();

    result = (transform(absolute) - range.getMin());
    result = result / scaler;
    if (result == Double.NaN || Double.isInfinite(result)) {
      result = 0;
    }
    return result;
  }

  /**
   * Template method for performing the axis transformation.
   * <p>
   * 
   * The argument should not be negative, so only normalized values (no chart
   * values but their scaled values or pixel values) should be given here.
   * <p>
   * 
   * 
   * @param in
   *          the value to transform.
   * 
   * @return the transformed value.
   */
  protected abstract double transform(final double in);

  /**
   * Template method for performing the reverse axis transformation.
   * <p>
   * This is the counterpart to {@link #transform(double)}.
   * <p>
   * 
   * 
   * @param in
   *          the transformed value.
   * 
   * @return the normal value;
   */
  protected abstract double untransform(final double in);

  /**
   * @see info.monitorenter.gui.chart.AAxis#translateMousePosition(java.awt.event.MouseEvent)
   */
  protected final double translateMousePosition(final MouseEvent mouseEvent)
      throws IllegalArgumentException {
    return this.untransform(this.getAccessor().translateMousePosition(mouseEvent));
  }

  /**
   * @see info.monitorenter.gui.chart.AAxis#getMax()
   */
  public double getMax() {
    return this.transform(super.getMax());
  }

  /**
   * @see info.monitorenter.gui.chart.AAxis#getMin()
   */
  public double getMin() {
    return this.transform(super.getMin());
  }
}
