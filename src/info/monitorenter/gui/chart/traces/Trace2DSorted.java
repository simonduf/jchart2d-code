/*
 *  Trace2DSorted, a TreeSet- based implementation of a ITrace2D that performs
 *  insertion- sort of TracePoint2D - instances by their x- value.
 *  Copyright (C) 2002  Achim Westermann, Achim.Westermann@gmx.de
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
 */

package info.monitorenter.gui.chart.traces;

import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.TracePoint2D;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Has the behaviour of <code>Trace2DReplacing</code> and an additional
 * features: <br>
 * <ul>
 * <li>All traceoints added whose x- values are not already contained are added
 * to the internal Tree ordered by growing x-values. Therefore it is guaranteed
 * that the tracepoints will be sorted in ascending order of x- values at any
 * time.</li>
 * </UL>
 *
 * Because sorted insertion of a List causes n! index- operations (
 * <code>get(int i)</code>) additional to the comparisons this class does not
 * extend <code>Trace2DSimple</code> which uses a List. Instead a
 * <code>TreeSet </code> is used.
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 */
public class Trace2DSorted extends ATrace2D implements ITrace2D {
  /** The sorted set of points. */
  protected SortedSet m_points = new TreeSet();

  /**
   * Defcon.
   * <p>
   */
  public Trace2DSorted() {
    // nop
  }

  /**
   * In case p has an x- value already contained, the old tracepoint with that
   * value will be replaced by the new one. Else the new tracepoint will be
   * added at an index in order to keep the ascending order of tracepoints with
   * a higher x- value are contained.
   * <p>
   *
   * @param p
   *          the point to add.
   *
   * @return true if the given point was successfully added.
   */
  protected boolean addPointInternal(final TracePoint2D p) {
    // remove eventually contained to allow adding of new one
    this.removePoint(p);
    return this.m_points.add(p);
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getMaxSize()
   */
  public int getMaxSize() {
    return Integer.MAX_VALUE;
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#getSize()
   */
  public int getSize() {
    return this.m_points.size();
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#isEmpty()
   */
  public boolean isEmpty() {
    return this.m_points.size() == 0;
  }

  /**
   * @see info.monitorenter.gui.chart.ITrace2D#iterator()
   */
  public Iterator iterator() {
    return this.m_points.iterator();
  }

  /**
   * @see ATrace2D#addPointInternal(info.monitorenter.gui.chart.TracePoint2D)
   */
  protected void removeAllPointsInternal() {
    this.m_points.clear();
  }

  /**
   * @see ATrace2D#removePointInternal(info.monitorenter.gui.chart.TracePoint2D)
   */
  protected boolean removePointInternal(final TracePoint2D point) {
    return this.m_points.remove(point);
  }
}
