/*
 *  PopupListener, general purpose popup trigger that connects JPopupMenus to mouse events.
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

/**
 * A general purpose <code>PopupListener</code>.
 * <p>
 * It is used to connect <code>JPopupMenu</code> instances with the components
 * retrieved from the factory methods (e.g.
 * {@link aw.gui.chart.layout.LayoutFactory#createContextMenuLable(aw.gui.chart.Chart2D)}).
 * <p>
 *
 * Note that instances have to be registered as a listener on components via
 * {@link java.awt.Component#addMouseListener(java.awt.event.MouseListener)}to
 * make it working.
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.3 $
 */
public final class PopupListener extends MouseAdapter {
  /** The popup to open. */
  private JPopupMenu m_popup;

  /**
   * Creates an instance that will show the given popup upon a right mouse click
   * on a {@link javax.swing.JComponent}this instance will be registered as
   * listener to.
   * <p>
   *
   * @param popup
   *          the popup to show upon a right mouse click on a
   *          {@link javax.swing.JComponent}this instance will be registered as
   *          listener to.
   *
   * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener)
   */
  public PopupListener(final JPopupMenu popup) {
    this.m_popup = popup;
  }

  /**
   * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
   */
  public void mousePressed(final MouseEvent me) {
    this.maybeShopwPopup(me);
  }

  /**
   * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
   */
  public void mouseReleased(final MouseEvent me) {
    this.maybeShopwPopup(me);
  }

  /**
   * Helper that triggers the popup display in a system - dependant manner.
   * <p>
   *
   * On windows a right mouse click will trigger the popup display.
   * <p>
   *
   * @param me
   *          the mouse event fired.
   */
  private void maybeShopwPopup(final MouseEvent me) {
    if (me.isPopupTrigger()) {
      this.m_popup.show(me.getComponent(), me.getX(), me.getY());
    }
  }
}
