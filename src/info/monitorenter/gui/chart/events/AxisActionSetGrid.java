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
package info.monitorenter.gui.chart.events;

import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.layout.LayoutFactory.PropertyChangeCheckBoxMenuItem;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBoxMenuItem;

/**
 * <code>Action</code> that invokes
 * {@link info.monitorenter.gui.chart.AAxis#setPaintGrid(boolean)} on a
 * constructor given {@link info.monitorenter.gui.chart.AAxis}.
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
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.4 $
 */
public class AxisActionSetGrid
    extends AAxisAction {

  /**
   * Generated <code>serial version UID</code>.
   * <p>
   */
  private static final long serialVersionUID = -5816028313134616682L;

  /**
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(final PropertyChangeEvent evt) {
    String property = evt.getPropertyName();
    if (property.equals(IAxis.PROPERTY_PAINTGRID)) {
      this.firePropertyChange(PropertyChangeCheckBoxMenuItem.PROPERTY_SELECTED, evt.getOldValue(),
          evt.getNewValue());
    }
  }

  /**
   * Create an <code>Action</code> that accesses the axis, identifies itself
   * with the given action String and invokes
   * {@link info.monitorenter.gui.chart.AAxis#setPaintGrid(boolean)} on the axis
   * upon selection.
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
   */
  public AxisActionSetGrid(final IAxis axis, final String description) {
    super(axis, description);
    axis.addPropertyChangeListener(IAxis.PROPERTY_PAINTGRID, this);
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
    boolean state = item.getState();
    this.getAxis().setPaintGrid(state);
  }
}
