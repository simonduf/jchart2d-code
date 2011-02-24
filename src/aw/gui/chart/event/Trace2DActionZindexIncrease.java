/*
 *  Trace2DActionZindexIncrease, action for increasing the z-Index of an ITrac2D.
 *  Copyright (C) Achim Westermann, created on 10.12.2004, 13:48:55
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
package aw.gui.chart.event;

import java.awt.event.ActionEvent;

import aw.gui.chart.ITrace2D;

/**
 * <p>
 * <code>Action</code> that increases the <code>zIndex</code> of the
 * constructor-given <code>ITrace2D</code> by a constructor-given integer.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 */
public final class Trace2DActionZindexIncrease extends ATrace2DAction {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3978986583057707570L;

  /**
   * The increment to the trace's zIndex.
   */
  private int m_increase;

  /**
   * Create an <code>Action</code> that accesses the trace and identifies
   * itself with the given action String.
   * <p>
   *
   * @param trace
   *          the target the action will work on.
   *
   * @param description
   *          the descriptive <code>String</code> that will be displayed by
   *          {@link javax.swing.AbstractButton}subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   *
   * @param increase
   *          the increment to the trace's zIndex (see
   *          {@link ITrace2D#setZIndex(Integer)}).
   */
  public Trace2DActionZindexIncrease(final ITrace2D trace, final String description,
      final int increase) {
    super(trace, description);
    this.m_increase = increase;
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    int value = this.m_trace.getZIndex().intValue();
    this.m_trace.setZIndex(new Integer(value + this.m_increase));
  }
}
