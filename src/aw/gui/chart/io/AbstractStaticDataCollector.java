/*
 *  AbstractDataCollector.java  a configurable threaded collector for tracepoints in jchart2d.
 *  Copyright (C) Achim Westermann, created on 10.12.2004, 14:48:09
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package aw.gui.chart.io;

import java.io.IOException;

import aw.gui.chart.ITrace2D;

/**
 * <p>
 * Base class for data collectors that fill traces for static charts in one run.
 * </p>
 * <p>
 * Extend from this class and override the method {@link #collectData()}.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 */
public abstract class AbstractStaticDataCollector {

  /**
   * Target trace where all collected data is added.
   */
  protected ITrace2D m_trace;

  /**
   * Constructor with target trace.
   * <p>
   *
   * @param trace
   *          the trace collected points will be added to.
   */
  public AbstractStaticDataCollector(final ITrace2D trace) {
    super();
    this.m_trace = trace;
  }

  /**
   * Collects all <code>ITracePoint</code> instances from it's underlying
   * source and adds it to the internal trace.
   * <p>
   *
   * @throws IOException
   *           if parsing or IO operations fails.
   */
  public abstract void collectData() throws IOException;

  /**
   * Returns the trace data is added to.
   * <p>
   *
   * @return the trace data is added to.
   */
  public ITrace2D getTrace() {
    return this.m_trace;
  }
}
