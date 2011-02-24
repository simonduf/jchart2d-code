/*
 *  Chart2DActionSetCustomGridColor, action that sets a custom grid color to the chart.
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

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JColorChooser;

import aw.gui.chart.Chart2D;

/**
 * <p>
 * <code>Action</code> that sets a custom grid color to the corresponding
 * chart ({@link Chart2D#setGridColor(Color)}) by showing a modal color
 * chooser.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 */
public final class Chart2DActionSetCustomGridColor extends AChart2DAction {
  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3691034370412916788L;

  /**
   * Create an <code>Action</code> that accesses the trace and identifies
   * itself with the given action String.
   * <p>
   *
   * @param chart
   *          the target the action will work on
   * @param colorName
   *          the descriptive <code>String</code> that will be displayed by
   *          {@link javax.swing.AbstractButton}subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   */
  public Chart2DActionSetCustomGridColor(final Chart2D chart, final String colorName) {
    super(chart, colorName);
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    Color chosen = JColorChooser.showDialog(this.m_chart, "choose color for "
        + this.m_chart.getName(), this.m_chart.getGridColor());
    this.m_chart.setGridColor(chosen);
  }
}
