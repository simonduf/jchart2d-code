/*
 *  ErrorBarPixel.java of project jchart2d, an implementation that 
 *  transform error bar values to pixels.
 *  Copyright (c) 2007 Achim Westermann, created on 02.10.2006 08:21:35.
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
import info.monitorenter.gui.chart.IErrorBarValue;
import info.monitorenter.gui.chart.ITrace2D;

/**
 * Implementation that works upon delegating getters to a wrapped
 * {@link info.monitorenter.gui.chart.IErrorBarValue} enriched by transformation
 * methods from value domain to pixel domain.
 * <p>
 * For performance reason it is not recommended to create a new instance for
 * each error bar value to transform but reuse this instance and invoke the
 * setter for the error bar value.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.2 $
 */
public class ErrorBarPixel implements IErrorBarPixel {

  /**
   * Creates a trace that transform the wrapped {@link IErrorBarValue} (which is
   * still null after this instantiation) based upon the chart of the given
   * trace.
   * <p>
   * 
   * @param trace
   *          required for transformation.
   * 
   * @see #setTrace(ITrace2D)
   * @see #setValue(IErrorBarValue)
   */
  protected ErrorBarPixel(final ITrace2D trace) {
    this.m_trace = trace;
  }

  /**
   * The trace the corresponding
   * {@link info.monitorenter.gui.chart.IErrorBarPolicy} is assigned to. This is
   * needed for the transformation. Although for the transformation the
   * underlying chart instance is neede the trace has to be stored as
   * reassigning the trace to another chart would break the chain and transform
   * values based upon the wrong chart.
   */
  private ITrace2D m_trace;

  /** The value to transform. */
  private IErrorBarValue m_value;

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPixel#getNegativeXErrorPixel()
   */
  public int getNegativeXErrorPixel() {
    int result = ERROR_PIXEL_NONE;
    double value = this.m_value.getNegativeXError();
    if (value != IErrorBarValue.ERROR_VALUE_NONE) {
      result = this.m_trace.getRenderer().getAxisX().translateValueToPx(value);
    }
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPixel#getNegativeYErrorPixel()
   */
  public int getNegativeYErrorPixel() {
    int result = ERROR_PIXEL_NONE;
    double value = this.m_value.getNegativeYError();
    if (value != IErrorBarValue.ERROR_VALUE_NONE) {
      result = this.m_trace.getRenderer().getAxisY().translateValueToPx(value);
    }
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPixel#getPositiveXErrorPixel()
   */
  public int getPositiveXErrorPixel() {
    int result = ERROR_PIXEL_NONE;
    double value = this.m_value.getPositiveXError();
    if (value != IErrorBarValue.ERROR_VALUE_NONE) {
      result = this.m_trace.getRenderer().getAxisX().translateValueToPx(value);
    }
    return result;
  }

  /**
   * @see info.monitorenter.gui.chart.IErrorBarPixel#getPositiveYErrorPixel()
   */
  public int getPositiveYErrorPixel() {
    int result = ERROR_PIXEL_NONE;
    double value = this.m_value.getPositiveYError();
    if (value != IErrorBarValue.ERROR_VALUE_NONE) {
      result = this.m_trace.getRenderer().getAxisY().translateValueToPx(value);
    }
    return result;
  }

  /**
   * @return the trace.
   */
  protected final ITrace2D getTrace() {
    return this.m_trace;
  }

  /**
   * @param trace
   *          The trace to set.
   */
  protected final void setTrace(final ITrace2D trace) {
    this.m_trace = trace;
  }

  /**
   * @return the value.
   */
  protected final IErrorBarValue getValue() {
    return this.m_value;
  }

  /**
   * @param value
   *          The value to set.
   */
  protected final void setValue(final IErrorBarValue value) {
    this.m_value = value;
  }
}
