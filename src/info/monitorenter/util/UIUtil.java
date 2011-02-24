/*
 *  UIUtil.java of project jchart2d, utility class for UI / Layout 
 *  operations. 
 *  Copyright 2007 (C) Achim Westermann, created on 19.08.2007 16:16:59.
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
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA*
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.util;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;

/**
 * Utility class for UI / layout operations.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * 
 * @version $Revision: 1.3 $
 */
public final class UIUtil {
  
  /**
   * Finds the frame of the given component that may be contained in a
   * <code>{@link javax.swing.JDialog}</code> with support for modal dialogs
   * that are launched from <code>{@link JPopupMenu}</code> instances.
   * <p>
   * This also works for nested <code>{@link javax.swing.JMenu}</code> /
   * <code>{@link javax.swing.JMenuItem}</code> trees.
   * <p>
   * 
   * @param component
   *            the component to find the master the JFrame of.
   * 
   * @return the frame of the given component that may be contained in a
   *         <code>{@link javax.swing.JDialog}</code> with support for modal
   *         dialogs that are launched from <code>{@link JPopupMenu}</code>
   *         instances.
   */
  public static Frame findFrame(final Component component) {

    Frame result = null;
    Component comp = component;
    if (comp instanceof JFrame) {
      result = (JFrame) comp;
    } else {
      if (component instanceof JPopupMenu) {
        comp = ((JPopupMenu) component).getInvoker();
      } else {
        comp = component.getParent();
      }

      result = UIUtil.findFrame(comp);
    }
    return result;

  }

  /**
   * Utility class constructor.
   * <p>
   * 
   */
  private UIUtil() {
    // nop
  }
}
