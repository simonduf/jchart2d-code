/*
 *  Chart2DActionSetGridColor, action to set a color for the grid of the chart.
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

import java.awt.Color;
import java.awt.event.ActionEvent;

import aw.gui.chart.Chart2D;

/**
 * <p>
 * Performs the action of setting the color of gridlines (
 * {@link aw.gui.chart.Chart2D#setGridColor(Color)}) of a <code>Chart2D</code>
 * with the constructor given <code>Color</code>.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.4 $
 */
public class Chart2DActionSetGridColor extends AChart2DAction {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3689069560279937078L;

  /** The color to set. */
  private Color m_color;

  /**
   * Create an <code>Action</code> that accesses the chart and identifies
   * itself with the given action String.
   * <p>
   *
   * @param chart
   *          the target the action will work on
   * @param colorName
   *          the descriptive <code>String</code> that will be displayed by
   *          {@link javax.swing.AbstractButton} subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   * @param color
   *          the color of gridlines to set.
   *
   */
  public Chart2DActionSetGridColor(final Chart2D chart, final String colorName, final Color color) {
    super(chart, colorName);
    this.m_color = color;
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    this.m_chart.setGridColor(this.m_color);
  }
}
