/*
 *  Trace2DActionSetStroke, action to set a Stroke on an ITrace2D.
 *  Copyright (C) Achim Westermann, created on 10.12.2004, 13:48:55
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
 *
 */
package aw.gui.chart.event;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;

import aw.gui.chart.ITrace2D;
import aw.gui.chart.ITracePainter;

/**
 * <code>Action</code> that adds or removes constructor-given
 * {@link aw.gui.chart.ITracePainter} to the corresponding trace.
 * <p>
 * This action only works in combination with {@link java.awt.CheckboxMenuItem}
 * instances that send themselve as the event object to the
 * {@link java.awt.event.ActionEvent}(
 * {@link java.util.EventObject#getSource()}).
 * <p>
 *
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.3 $
 */
public final class Trace2DActionAddRemoveTracePainter extends ATrace2DAction {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3978986583057707570L;

  /**
   * The stroke to set.
   */
  private ITracePainter m_tracePainter;

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
   *          {@link javax.swing.AbstractButton} subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   *
   * @param painter
   *          the painter to add / remove from the trace.
   */
  public Trace2DActionAddRemoveTracePainter(final ITrace2D trace, final String description,
      final ITracePainter painter) {
    super(trace, description);
    this.m_tracePainter = painter;
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
    boolean state = item.getState();
    if (state) {
      this.m_trace.addTracePainter(this.m_tracePainter);
    } else {
      boolean success = this.m_trace.removeTracePainter(this.m_tracePainter);
      if (success) {
        // nop
      } else {
        // rewind state as this could not be done:
        // contract for ITrace2D is, that at least one renderer should be
        // defined!
        item.setState(false);
        item.invalidate();
        item.repaint();
      }
    }
  }
}
