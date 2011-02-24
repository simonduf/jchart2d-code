/*
 *  Trace2DActionSetStroke, action to set a Stroke on an ITrace2D.
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

import java.awt.Stroke;
import java.awt.event.ActionEvent;

import aw.gui.chart.ITrace2D;

/**
 * <p>
 * <code>Action</code> that sets a constructor-given zIndex to the
 * corresponding trace.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 */
public final class Trace2DActionSetStroke extends ATrace2DAction {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3978986583057707570L;

  /**
   * The stroke to set.
   */
  private Stroke m_stroke;

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
   * @param stroke
   *          the stroke property to set to the corresponding trace (see
   *          {@link ITrace2D#setStroke(Stroke)})
   */
  public Trace2DActionSetStroke(final ITrace2D trace, final String description, final Stroke stroke) {
    super(trace, description);
    this.m_stroke = stroke;
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    this.m_trace.setStroke(this.m_stroke);
  }
}
