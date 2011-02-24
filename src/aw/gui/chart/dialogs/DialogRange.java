/*
 *  DialogRange.java of project jchart2d
 *  Copyright 2006 (C) Achim Westermann, created on 09:31:15.
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
package aw.gui.chart.dialogs;

import java.awt.Component;

import aw.gui.chart.panels.RangeChooserPanel;
import aw.util.Range;

/**
 * A dialog for choosing a range.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * 
 * @version $Revision$
 */
public class DialogRange extends ADialog {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3256718472674621488L;

  /**
   * Creates and returns a new dialog containing the specified
   * <code>RangeChooserPanel</code> pane along with "OK", "Cancel", and
   * "Reset" buttons.
   * <p>
   * 
   * If the "OK" or "Cancel" buttons are pressed, the dialog is automatically
   * hidden (but not disposed). If the "Reset" button is pressed, the
   * color-chooser's color will be reset to the color which was set the last
   * time <code>show</code> was invoked on the dialog and the dialog will
   * remain showing.
   * 
   * @param c
   *          the parent component for the dialog.
   * 
   * @param title
   *          the title for the dialog.
   * 
   * @param modal
   *          if true, the remainder of the program is inactive until the dialog
   *          is closed.
   * 
   * @param range
   *          the bounds for the displayed range chooser: values within these
   *          bounds will be selectable.
   * 
   * @param chooserPane
   *          the range chooser to be placed inside the dialog: Result of choice
   *          has to be obtained there.
   * 
   * @return the dialog for selection of the range (undisplayed yet).
   */
  public static DialogRange createRangeDialog(final Component c, final Range range,
      final String title, final boolean modal, final RangeChooserPanel chooserPane) {

    DialogRange dialog = new DialogRange(c, title, modal, chooserPane);
    return dialog;
  }

  /**
   * Creates a range-chooser dialog.
   * <p>
   * 
   * @param component
   *          the parent <code>Component</code> for the dialog.
   * 
   * @param title
   *          the String containing the dialog's title.
   * 
   * @param modal
   *          if true this will be a modal dialog (blocking actions on component
   *          until closed.
   * 
   * @param chooserPane
   *          the UI control for choosing the range.
   */
  public DialogRange(final Component component, final String title, final boolean modal,
      final RangeChooserPanel chooserPane) {
    super(component, title, modal, chooserPane);

  }
}
