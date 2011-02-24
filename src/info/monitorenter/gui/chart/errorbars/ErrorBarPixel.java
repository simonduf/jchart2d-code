/*
 *  ErrorBarPixel.java of project jchart2d, a plain data container implementation. 
 *  Copyright (c) 2007 - 2010 Achim Westermann, created on 02.10.2006 08:21:35.
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

import info.monitorenter.gui.chart.IErrorBarPixel;
import info.monitorenter.gui.chart.ITrace2D;

/**
 * Straight forward dumb data container implementation.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.9 $
 */
public class ErrorBarPixel implements IErrorBarPixel {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = -8982331911629960274L;

  /** The negative x error in pixel. */
  private int m_negativeXErrorPixel = ERROR_PIXEL_NONE;

  /** The negative y error in pixel. */
  private int m_negativeYErrorPixel = ERROR_PIXEL_NONE;

  /** The positive x error in pixel. */
  private int m_positiveXErrorPixel = ERROR_PIXEL_NONE;

  /** The positive y error in pixel. */
  private int m_positiveYErrorPixel = ERROR_PIXEL_NONE;

  /**
   * The trace the corresponding
   * {@link info.monitorenter.gui.chart.IErrorBarPolicy} is assigned to. This is
   * needed for the transformation. Although for the transformation the
   * underlying chart instance is needed the trace has to be stored as
   * reassigning the trace to another chart would break the chain and transform
   * values based upon the wrong chart.
   */
  private ITrace2D m_trace;

  /**
   * Creates an instance backed by the given trace.
   * <p>
   * 
   * @param trace
   *            required for transformation.
   */
  public ErrorBarPixel(final ITrace2D trace) {
    this.m_trace = trace;
  }

  /**
   * Convenience method for clearing this error bar making it available for new
   * configuration.
   * <p>
   * All errors are set to {@link #ERROR_PIXEL_NONE} afterwards.
   * <p>
   */
  public void clear() {
    this.m_negativeXErrorPixel = ERROR_PIXEL_NONE;
    this.m_negativeYErrorPixel = ERROR_PIXEL_NONE;
    this.m_positiveXErrorPixel = ERROR_PIXEL_NONE;
    this.m_positiveYErrorPixel = ERROR_PIXEL_NONE;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPixel#getNegativeXErrorPixel()
   */
  public int getNegativeXErrorPixel() {
    return this.m_negativeXErrorPixel;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPixel#getNegativeYErrorPixel()
   */
  public int getNegativeYErrorPixel() {
    return this.m_negativeYErrorPixel;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPixel#getPositiveXErrorPixel()
   */
  public int getPositiveXErrorPixel() {
    return this.m_positiveXErrorPixel;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPixel#getPositiveYErrorPixel()
   */
  public int getPositiveYErrorPixel() {
    return this.m_positiveYErrorPixel;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPixel#getTrace()
   */
  public final ITrace2D getTrace() {
    return this.m_trace;
  }

  /**
   * Intended for {@link AErrorBarPolicyConfigurable} only.
   * <p>
   * 
   * @param negativeXError
   *            The negativeXError in pixel to set.
   */
  protected final void setNegativeXErrorPixel(final int negativeXError) {
    this.m_negativeXErrorPixel = negativeXError;
  }

  /**
   * Intended for {@link AErrorBarPolicyConfigurable} only.
   * <p>
   * 
   * @param negativeYError
   *            The negativeYError in pixel to set.
   */
  protected final void setNegativeYErrorPixel(final int negativeYError) {
    this.m_negativeYErrorPixel = negativeYError;
  }

  /**
   * Intended for {@link AErrorBarPolicyConfigurable} only.
   * <p>
   * 
   * @param positiveXError
   *            The positiveXError in pixel to set.
   */
  protected final void setPositiveXErrorPixel(final int positiveXError) {
    this.m_positiveXErrorPixel = positiveXError;
  }

  /**
   * Intended for {@link AErrorBarPolicyConfigurable} only.
   * <p>
   * 
   * @param positiveYError
   *            The positiveYError in pixel to set.
   */
  protected final void setPositiveYErrorPixel(final int positiveYError) {
    this.m_positiveYErrorPixel = positiveYError;
  }

  /**
   * Sets the trace to use.
   * <p>
   * 
   * @param trace
   *            The trace to set.
   */
  protected final void setTrace(final ITrace2D trace) {
    this.m_trace = trace;
  }

}
