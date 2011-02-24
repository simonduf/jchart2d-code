/*
 *  IErrorBarValue.java of project jchart2d, interface for an error bar. 
 *  Copyright (c) 2007 Achim Westermann, created on 06.08.2006 13:42:31.
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
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA*
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.gui.chart;

/**
 * Interface for an error bar in the value domain (vs. pixel) for a single
 * {@link info.monitorenter.gui.chart.TracePoint2D}.
 * <p>
 * Errors returned from the getters represent not only the error part but the
 * absolute value.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * @version $Revision: 1.2 $
 */
public interface IErrorBarValue {

  /** Constant that identifies a resulting error to be non-existant. */
  public static final double ERROR_VALUE_NONE = Double.MAX_VALUE;

  /**
   * Returns the negative error (positive value) in X dimension as a value in
   * the value domain of the chart (vs. pixel domain) or
   * {@link #ERROR_VALUE_NONE}.
   * <p>
   * 
   * @return the negative error in X dimension or {@link #ERROR_VALUE_NONE}.
   */
  public double getNegativeXError();

  /**
   * Returns the negative error (absolute value) in Y dimension as a value
   * related to the value domain of the chart (vs. pixel domain) or
   * {@link #ERROR_VALUE_NONE}.
   * <p>
   * 
   * @return the negative error in Y dimension or {@link #ERROR_VALUE_NONE}.
   */
  public double getNegativeYError();

  /**
   * Returns the absolute value for the positive error in X dimension as a value
   * in the value domain of the chart (vs. pixel domain)
   * {@link #ERROR_VALUE_NONE}.
   * <p>
   * 
   * @return the positive error in X dimension or {@link #ERROR_VALUE_NONE}.
   */
  public double getPositiveXError();

  /**
   * Returns the absolute value for the positive error in Y dimension as a value in the value domain of
   * the chart (vs. pixel domain) {@link #ERROR_VALUE_NONE}.
   * <p>
   * 
   * @return the positive error in Y dimension or {@link #ERROR_VALUE_NONE}.
   */
  public double getPositiveYError();
}
