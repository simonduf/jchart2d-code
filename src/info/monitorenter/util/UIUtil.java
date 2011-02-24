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
import java.awt.Window;

/**
 * Utility class for UI / layout operations.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * @version $Revision: 1.2 $
 */
public final class UIUtil {
  /**
   * Utility class constructor.
   * <p>
   */
  private UIUtil() {
    // nop
  }

  /**
   * Returns the window the given component is contained in or null if this is not the case.
   * <p>
   * 
   * @param c
   *            the component to search the parent modal dialog of.
   * @return the window the given component is contained in or null if this is not the case.
   */
  public static Window findWindow(final Component c) {
    Window result = null;
    if (c instanceof Window) {
      result = (Window) c;
    } else {
      Component parent = c.getParent();
      if (parent != null) {
        result = UIUtil.findWindow(parent);
      }
    }
    return result;
  }
}
