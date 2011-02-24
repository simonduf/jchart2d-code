/*
 *  ErrorBarValue.java of project jchart2d, data model for an error bar 
 *  along with the contract of visiblity. 
 *  Copyright 2006 (C) Achim Westermann, created on 09.08.2006 23:56:48.
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

import info.monitorenter.gui.chart.IErrorBarValue;

/**
 * Default member based implementation for configuration by
 * {@link info.monitorenter.gui.chart.errorbars.AErrorBarPolicyConfigurable}.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.1 $
 */
public class ErrorBarValue implements IErrorBarValue {

  /** The negative x error. */
  private double m_negativeXError = ERROR_VALUE_NONE;

  /** The negative y error. */
  private double m_negativeYError = ERROR_VALUE_NONE;

  /** The positive x error. */
  private double m_positiveXError = ERROR_VALUE_NONE;

  /** The positive y error. */
  private double m_positiveYError = ERROR_VALUE_NONE;

 
  /**
   * Defcon.
   * <p>
   */
  public ErrorBarValue() {
    // nop
  }

  /**
   * Convenience method for clearing this error bar making it available for new
   * configuration.
   * <p>
   * All errors are set to {@link #ERROR_VALUE_NONE} afterwards.
   * <p>
   */
  public void clear() {
    this.m_negativeXError = ERROR_VALUE_NONE;
    this.m_negativeYError = ERROR_VALUE_NONE;
    this.m_positiveXError = ERROR_VALUE_NONE;
    this.m_positiveYError = ERROR_VALUE_NONE;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarValue#getNegativeXError()
   */
  public double getNegativeXError() {
    return this.m_negativeXError;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarValue#getNegativeYError()
   */
  public double getNegativeYError() {
    return this.m_negativeYError;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarValue#getPositiveXError()
   */
  public double getPositiveXError() {
    return this.m_positiveXError;
  }

  /**
   * Intended for {@link AErrorBarPolicyConfigurable} only.
   * <p>
   * 
   * @see info.monitorenter.gui.chart.IErrorBarValue#getPositiveYError()
   */
  public double getPositiveYError() {
    return this.m_positiveYError;
  }

  /**
   * Intended for {@link AErrorBarPolicyConfigurable} only.
   * <p>
   * 
   * @param negativeXError
   *          The negativeXError to set.
   */
  protected final void setNegativeXError(final double negativeXError) {
    this.m_negativeXError = negativeXError;
  }

  /**
   * Intended for {@link AErrorBarPolicyConfigurable} only.
   * <p>
   * 
   * @param negativeYError
   *          The negativeYError to set.
   */
  protected final void setNegativeYError(final double negativeYError) {
    this.m_negativeYError = negativeYError;
  }

  /**
   * Intended for {@link AErrorBarPolicyConfigurable} only.
   * <p>
   * 
   * @param positiveXError
   *          The positiveXError to set.
   */
  protected final void setPositiveXError(final double positiveXError) {
    this.m_positiveXError = positiveXError;
  }

  /**
   * Intended for {@link AErrorBarPolicyConfigurable} only.
   * <p>
   * 
   * @param positiveYError
   *          The positiveYError to set.
   */
  protected final void setPositiveYError(final double positiveYError) {
    this.m_positiveYError = positiveYError;
  }
}
