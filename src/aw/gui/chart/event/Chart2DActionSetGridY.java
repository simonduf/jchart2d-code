/*
 *  Chart2DActionSetGridY, action for enabling / disabling display of the y grid of the chart.
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

import aw.gui.chart.Chart2D;

/**
 * <p>
 * Performs the action of setting the painting of y-gridlines (
 * {@link aw.gui.chart.Chart2D#setGridX(boolean)}} of a <code>Chart2D</code>
 * with the constructor given boolean.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 */
public class Chart2DActionSetGridY extends AChart2DAction {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3545517326519972403L;
/** Decides wether the grid is turned on or off. */
  private boolean m_value;

  /**
   * Create an <code>Action</code> that accesses the chart and identifies
   * itself with the given action String.<p>
   *
   * @param chart
   *          the target the action will work on.
   *
   * @param description
   *          the descriptive <code>String</code> that will be displayed by
   *          {@link javax.swing.AbstractButton} subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   *
   * @param value
   *          decides wether to turn on or off painting gridlines.
   */
  public Chart2DActionSetGridY(final Chart2D chart, final String description, final boolean value) {
    super(chart, description);
    this.m_value = value;
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    this.m_chart.setGridX(this.m_value);
  }
}
