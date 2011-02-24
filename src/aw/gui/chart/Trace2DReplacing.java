/*
 * Trace2DReplacing, a list- based implementation of a ITrace2D.
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

/**
 * Has the behaviour of <code>Trace2DBijective</code> and an additional
 * features: <br>
 * <ul>
 * <li>All tracepoints that are added are stored unchanged in a LinkedList.
 * </li>
 * <li>All traceoints added whose x- values are not already contained are added
 * to the end.</li>
 * <li>If a tracepoint is inserted whose x - value already exists in the List,
 * the old tracepoint with that value will be replaced by the new tracepoint.
 * </li>
 * </UL>
 *
 * @see Trace2DBijective
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 *
 * @version $Revision: 1.6 $
 */
public class Trace2DReplacing extends Trace2DSimple {

  /**
   * Defcon.
   */
  public Trace2DReplacing() {
    // nop
  }

  /**
   * In case p has an x- value already contained, the old tracepoint with that
   * value will be replaced by the new one. Else the new tracepoint will be
   * added to the end, not caring wether tracepoints with a higher x- value are
   * contained.
   * <p>
   *
   * @param p
   *          the point to add.
   *
   * @return true if the point wathe maximum amount of points that will be
   *         showns successfully added.
   */
  public boolean addPointInternal(final TracePoint2D p) {
    boolean changed = true;

    int index = -1;
    double tmp = 0;
    TracePoint2D old;
    if ((index = this.m_points.indexOf(p)) != -1) {
      // already contained.
      old = (TracePoint2D) this.m_points.get(index);
      // fires property changes with bound checks
      old.setLocation(old.getX(), p.getY());
      // we don't need further bound checks and property change events from
      // calling
      // addPoint method.
      return false;
    } else {
      this.m_points.add(p);
      return true;
    }
  }
}
