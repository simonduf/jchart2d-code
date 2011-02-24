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
import info.monitorenter.gui.chart.IRangePolicy;
import info.monitorenter.gui.chart.layout.LayoutFactory.PropertyChangeCheckBoxMenuItem;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

/**
 * Action that sets a constructor given
 * {@link info.monitorenter.gui.chart.IRangePolicy} to a constructor given
 * {@link info.monitorenter.gui.chart.AAxis}.
 * <p>
 * 
 * <h2>Warning</h2>
 * This <code>Action</code> currently is only intended to be
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.2 $
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
   * {@link info.monitorenter.gui.chart.IRangePolicy} to the axis upon
   * selection.
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
  public AxisActionSetRangePolicy(final IAxis axis, final String description,
      final IRangePolicy rangePolicy) {
    super(axis, description);
    this.m_rangePolicy = rangePolicy;
    axis.addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY, this);

  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    this.m_axis.setRangePolicy(this.m_rangePolicy);
  }

  /**
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(final PropertyChangeEvent evt) {
    String property = evt.getPropertyName();
    if (property.equals(IAxis.PROPERTY_RANGEPOLICY)) {
      Class rangepolicyClass = evt.getNewValue().getClass();
      Boolean oldValue, newValue;
      if (rangepolicyClass == this.m_rangePolicy.getClass()) {
        oldValue = new Boolean(false);
        newValue = new Boolean(true);
      } else {
        oldValue = new Boolean(true);
        newValue = new Boolean(false);
      }
      this.firePropertyChange(PropertyChangeCheckBoxMenuItem.PROPERTY_SELECTED, oldValue, newValue);
    }
  }
}
