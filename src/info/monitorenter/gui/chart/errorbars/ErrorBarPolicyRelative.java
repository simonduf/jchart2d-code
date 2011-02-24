/*
 *  ErrorBarPolicyRelative.java of project jchart2d, configurable 
 *  info.monitorenter.gui.chart.IErrorBarPolicy that adds a 
 *  relative error to the points to render.
 *  Copyright 2006 (C) Achim Westermann, created on 10.08.2006 19:37:54.
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
package info.monitorenter.gui.chart.errorbars;

/**
 * Configurable <code>{@link info.monitorenter.gui.chart.IErrorBarPolicy}</code>
 * that adds a relative error (relative to the absolute values) to the points to
 * render.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.4 $
 */
public class ErrorBarPolicyRelative
    extends AErrorBarPolicyConfigurable {

  /** The relative error to render. */
  private double m_relativeError = 0.1;

  /**
   * Creates an instance with the given relative error.
   * <p>
   * 
   * The relative error is related to the absolut x and y values to render. It
   * has to be between 0.0 and 1.0.
   * <p>
   * 
   * @param relativeError
   *          a value between 0.0 and 1.0.
   * 
   * @throws IllegalArgumentException
   *           if the argument is not between 0.0 and 1.0.
   */
  public ErrorBarPolicyRelative(final double relativeError) throws IllegalArgumentException {
    this.setRelativeError(relativeError);
  }

  /**
   * @return the relativeError.
   */
  public final double getRelativeError() {
    return this.m_relativeError;
  }

  /**
   * @see info.monitorenter.gui.chart.errorbars.AErrorBarPolicyConfigurable#internalGetNegativeXError(double,
   *      double)
   */
  protected double internalGetNegativeXError(final double absoluteX, final double absoluteY) {
    return (1.0 - this.m_relativeError) * absoluteX;
  }

  /**
   * @see info.monitorenter.gui.chart.errorbars.AErrorBarPolicyConfigurable#internalGetNegativeYError(double,
   *      double)
   */
  protected double internalGetNegativeYError(final double absoluteX, final double absoluteY) {
    return (1.0 - this.m_relativeError) * absoluteY;
  }

  /**
   * @see info.monitorenter.gui.chart.errorbars.AErrorBarPolicyConfigurable#internalGetPositiveXError(double,
   *      double)
   */
  protected double internalGetPositiveXError(final double absoluteX, final double absoluteY) {
    return (1.0 + this.m_relativeError) * absoluteX;
  }

  /**
   * @see info.monitorenter.gui.chart.errorbars.AErrorBarPolicyConfigurable#internalGetPositiveYError(double,
   *      double)
   */
  protected double internalGetPositiveYError(final double absoluteX, final double absoluteY) {
    return (1.0 + this.m_relativeError) * absoluteY;
  }

  /**
   * Sets the relative error to add to each error bar.
   * <p>
   * 
   * The relative error is related to the absolut x and y values to render. It
   * has to be between 0.0 and 1.0.
   * <p>
   * 
   * @param relativeError
   *          a value between 0.0 and 1.0.
   * 
   * @throws IllegalArgumentException
   *           if the argument is not between 0.0 and 1.0.
   */
  public final void setRelativeError(final double relativeError) throws IllegalArgumentException {
    if (relativeError <= 0.0 || relativeError >= 1.0) {
      throw new IllegalArgumentException("Given relative error (" + relativeError
          + ")has to be between 0.0 and 1.0.");
    }
    boolean change = this.m_relativeError != relativeError;
    if (change) {
      this.m_relativeError = relativeError;
      this.firePropertyChange(PROPERTY_CONFIGURATION, null, null);
    }
  }
}
