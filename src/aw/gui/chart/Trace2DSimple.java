/*
 * Trace2DSimple, a list- based simple implementation of a ITrace2D.
 * Copyright (C) 2002  Achim Westermann, Achim.Westermann@gmx.de
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 */

package aw.gui.chart;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * <p>
 * A basic <code>{@link aw.gui.chart.ITrace2D}</code> implementation that
 * stores the internal <code>{@link aw.gui.chart.TracePoint2D}</code>
 * instances in a <code>{@link java.util.List}</code>.
 * </p>
 * <p>
 * </p>
 * <p>
 * This class has the following behaviour: <br>
 * <ul>
 * <li>All tracepoints that are added are stored unchanged in a LinkedList.
 * </li>
 * <li>All traceoints that are added are added to the end.</li>
 * <li>If a tracepoint is inserted whose x - value already exists in the List,
 * it is ok - the old point may remain. (no bijective assigement of X and Y)
 * </li>
 * </ul>
 * </p>
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 *
 * @version $Revision: 1.12 $
 */
public class Trace2DSimple extends AbstractTrace2D implements ITrace2D {

  /** Internal List &lt;ITracePoint2D&gt;. */
  protected LinkedList m_points = new LinkedList();

  /**
   * Creates an empty trace.
   * <p>
   */
  public Trace2DSimple() {
    this(Trace2DSimple.class.getName() + "-" + AbstractTrace2D.getInstanceCount());
  }

  /**
   * Creates an emtpy trace with the given name.
   * <p>
   *
   * @param name
   *          the name that will be displayed below the chart.
   */
  public Trace2DSimple(final String name) {
    this.setName(name);
  }

  /**
   * @see aw.gui.chart.AbstractTrace2D#addPointInternal(aw.gui.chart.TracePoint2D)
   */
  protected boolean addPointInternal(final TracePoint2D p) {
    synchronized (this.m_renderer) {
      synchronized (this) {
        this.m_points.add(p);
        return true;
      }
    }
  }

  /**
   * @see aw.gui.chart.ITrace2D#getMaxSize()
   */
  public final int getMaxSize() {
    return Integer.MAX_VALUE;
  }

  /**
   * @see aw.gui.chart.ITrace2D#getSize()
   */
  public final int getSize() {
    return this.m_points.size();
  }

  /**
   * @see aw.gui.chart.ITrace2D#isEmpty()
   */
  public boolean isEmpty() {
    return this.m_points.size() == 0;
  }

  /**
   * @see aw.gui.chart.ITrace2D#iterator()
   */
  public Iterator iterator() {
    return this.m_points.iterator();
  }

  /**
   * @see aw.gui.chart.AbstractTrace2D#removeAllPointsInternal()
   */
  public final void removeAllPointsInternal() {
    synchronized (this.m_renderer) {
      synchronized (this.m_points) {

        this.m_points.clear();
      }
    }
  }

  /**
   * @see aw.gui.chart.AbstractTrace2D#removePointInternal(aw.gui.chart.TracePoint2D)
   */
  protected boolean removePointInternal(final TracePoint2D point) {
    return this.m_points.remove(point);
  }

}
