/*
 *  AxisActionSetRange.java of project jchart2d
 *  Copyright 2006 (C) Achim Westermann, created on 20:30:06.
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
import info.monitorenter.gui.chart.dialogs.DialogRange;
import info.monitorenter.gui.chart.layout.controls.RangeChooserPanel;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

/**
 * <code>Action</code> that sets the range of an
 * {@link info.monitorenter.gui.chart.AAxis} of a chart ({@link info.monitorenter.gui.chart.Chart2D})
 * that will be used by it's viewport (
 * {@link info.monitorenter.gui.chart.AAxis#setRangePolicy(info.monitorenter.gui.chart.IRangePolicy)})
 * by showing a modal range chooser.
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * 
 * @version $Revision: 1.3 $
 */
public class AxisActionSetRange extends AAxisAction {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3258694286479406393L;

  /**
   * Create an <code>Action</code> that sets the range of the given axis.
   * <p>
   * 
   * @param axis
   *          the target the action will work on.
   * 
   * @param description
   *          the descriptive <code>String</code> that will be displayed by
   *          {@link javax.swing.AbstractButton} subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   */
  public AxisActionSetRange(final IAxis axis, final String description) {
    super(axis, description);
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {

    IAxis axis = this.getAxis();
    RangeChooserPanel rangePanel = new RangeChooserPanel(axis.getRangePolicy().getRange());
    DialogRange dialog = new DialogRange(axis.getAccessor().getChart(), "Choose a range",
        true, rangePanel);
    dialog.showDialog();
    axis.setRange(rangePanel.getRange());
  }

  /**
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(final PropertyChangeEvent evt) {
    // nop as this action will not be used
    // by checkbox or radio button menu items that have a state.
  }
}
