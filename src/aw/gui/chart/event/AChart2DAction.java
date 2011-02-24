/*
 *  AChart2DAction, base for actions to trigger on charts.
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

import javax.swing.AbstractAction;

import aw.gui.chart.Chart2D;

/**
 * <p>
 * The base class that connects triggered actions with an
 * {@link aw.gui.chart.Chart2D} instance.
 * </p>
 * <p>
 * Every subclass may delegate it's constructor-given <code>Chart2D</code>
 * instance as protected member <code>chart</code>.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 *
 */
public abstract class AChart2DAction extends AbstractAction {

  /** The target of this action. */
  protected Chart2D m_chart;

  /**
   * Create an <code>Action</code> that accesses the chart and identifies
   * itself with the given action String.
   *
   * @param chart
   *          the target the action will work on
   * @param description
   *          the descriptive <code>String</code> that will be displayed by
   *          {@link javax.swing.AbstractButton}subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   */
  public AChart2DAction(final Chart2D chart, final String description) {
    super(description);
    this.m_chart = chart;
  }
}
