/*
 *  Trace2DLtdSorting, a TreeSet based implementation of a ITrace2D, which
 *  has a maximum amount of TracePoints (fifo) and performs an insertion sort
 *  of the TracePoint2D- instances.
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

package aw.gui.chart;

/**
 * Additional to the <code>Trace2DLtdReplacing</code> all tracepoints will be
 * sorted by their x- value. <br>
 * Performance is slower compared to the class named above. Internally a
 * <code>TreeSet </code> is used (instead of <code>RingBufferArrayFast</code>)
 * to keep the comparable <code>TracePoint2D</code>- instances sorted.
 * Internally all tracepoints are <code>TracePoint2D</code> -instances.
 *
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 *
 * @version $Revision: 1.8 $
 */
public class Trace2DLtdSorted extends Trace2DSorted {

  /** The maximum amount of points that will be shown. */
  protected int m_maxsize;

  /**
   * Constructs an instance with a default buffer size of 100.
   * <p>
   */
  public Trace2DLtdSorted() {

    this(100);
  }

  /**
   * Constructs an instance with a buffer size of maxsize.
   *
   * @param maxsize
   *          the maximum amount of points to show.
   */
  public Trace2DLtdSorted(final int maxsize) {
    this.m_maxsize = maxsize;
  }

  /**
   * In case point has an x- value already contained, the old tracepoint with
   * that value will be replaced by the new one. Else the new tracepoint will be
   * added at an index in order to keep the ascending order of tracepoints with
   * a higher x- value are contained.
   * <p>
   * If points takes additional space (it's x- value is not already contained)
   * and maxsize is reached, the first element (with lowest x- value) will be
   * removed.
   * <p>
   *
   * @param point
   *          the point to add.
   *
   * @return true if the point was successfully removed.
   */
  protected boolean addPointInternal(final TracePoint2D point) {

    boolean rem = this.removePoint(point);
    this.m_points.add(point);
    if (!rem) {
      if (this.m_points.size() > this.m_maxsize) {
        TracePoint2D remove = (TracePoint2D) this.m_points.last();
        this.removePoint(remove);
      }
    }
    return true;
  }

  /**
   * @see aw.gui.chart.ITrace2D#getMaxSize()
   */
  public final int getMaxSize() {

    return this.m_maxsize;
  }

  /**
   * Sets the maximum amount of points that will be shown.
   * <p>
   *
   * @param amount
   *          the maximum amount of points that will be shown.
   */
  public final void setMaxSize(final int amount) {

    synchronized (this) {
      this.m_maxsize = amount;
    }
  }
}
