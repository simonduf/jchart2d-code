/*
 *  AxisActionSetRangePolicy.java of project jchart2d
 *  Copyright 2006 (C) Achim Westermann, created on 00:13:29.
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

import aw.gui.chart.Axis;
import aw.gui.chart.IRangePolicy;

/**
 * Action that sets a constructor given {@link aw.gui.chart.IRangePolicy} to a
 * constructor given {@link aw.gui.chart.Axis}.
 * <p>
 * 
 * <h2>Warning</h2>
 * This <code>Action</code> currently is only intended to be
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision$
 */
public class AxisActionSetRangePolicy extends AAxisAction {

  /**
   * Generated <code>serial version UID</code>.
   */
  private static final long serialVersionUID = -3093734349885438197L;
  /**
   * The range policy to set to the axis upon invocation of
   * {@link #actionPerformed(ActionEvent)}.
   */
  private IRangePolicy m_rangePolicy;

  /**
   * Create an <code>Action</code> that accesses the axis, identifies itself
   * with the given action String and sets the given
   * {@link aw.gui.chart.IRangePolicy} to the axis upon selection.
   * 
   * @param axis
   *          the target the action will work on.
   * 
   * @param description
   *          the descriptive <code>String</code> that will be displayed by
   *          {@link javax.swing.AbstractButton} subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   * 
   * @param rangePolicy
   *          The range policy to set to the axis upon invocation of
   *          {@link #actionPerformed(ActionEvent)}.
   */
  public AxisActionSetRangePolicy(Axis axis, String description, IRangePolicy rangePolicy) {
    super(axis, description);
    this.m_rangePolicy = rangePolicy;
  }

  public void actionPerformed(ActionEvent e) {
    this.m_axis.setRangePolicy(this.m_rangePolicy);
  }
}
