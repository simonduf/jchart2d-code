/*
 *  Chart2DActionSetGridX, action for enabling / disabling display of the x grid of the chart.
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

import aw.gui.chart.Chart2D;

/**
 * Performs the action of setting the painting of x-gridlines (
 * {@link aw.gui.chart.Chart2D#setGridX(boolean)}} of a <code>Chart2D</code>
 * with the constructor given boolean.
 * <p>
 * 
 * <h2>Caution</h2>
 * This implementation only works if assigned to a
 * {@link javax.swing.JCheckBoxMenuItem}: It assumes that the source instance
 * given to {@link #actionPerformed(ActionEvent)} within the action event is of
 * that type as the state information (turn grid visible or turn grid invisible)
 * is needed.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.5 $
 */
public class Chart2DActionSetGridX extends AChart2DAction {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3906085667778803254L;

  /**
   * Create an <code>Action</code> that accesses the chart and identifies
   * itself with the given action String.
   * 
   * @param chart
   *          the target the action will work on.
   * 
   * @param description
   *          the descriptive <code>String</code> that will be displayed by
   *          {@link  javax.swing.AbstractButton} subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link  javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   * 
   * 
   */
  public Chart2DActionSetGridX(final Chart2D chart, final String description) {
    super(chart, description);
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
    boolean state = item.getState();
    this.m_chart.setGridX(state);
  }
}
