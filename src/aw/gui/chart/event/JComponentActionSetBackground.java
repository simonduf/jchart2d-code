/*
 *  JComponentActionSetBackground, action to set the background color of a JComponent.
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

import javax.swing.JComponent;

/**
 * <p>
 * Performs the action of setting the background color
 * ({@link javax.swing.JComponent#setBackground(java.awt.Color)}}
 * of a <code>JComponent</code>.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 */
public final class JComponentActionSetBackground extends AJComponentAction {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3258131345116181297L;

  /** The background color to set. */
  private Color m_color;

  /**
   * Create an <code>Action</code> that accesses the <code>JComponent</code>
   * and identifies itself with the given action String.<p>
   *
   * @param component
   *          the target the action will work on.
   *
   * @param colorName
   *          the descriptive <code>String</code> that will be displayed by
   *          {@link javax.swing.AbstractButton} subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   *
   * @param color
   *          the <code>Color</code> to set as "background" of the internal
   *          <code>JComponent</code>.
   */
  public JComponentActionSetBackground(final JComponent component, final String colorName, final Color color) {
    super(component, colorName);
    this.m_color = color;
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    this.m_component.setBackground(this.m_color);
  }
}
