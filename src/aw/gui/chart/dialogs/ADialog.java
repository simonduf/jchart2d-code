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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import aw.gui.chart.panels.RangeChooserPanel;

/**
 * A dialog for choosing a range.
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 *
 * @version $Revision$
 */
public abstract class ADialog extends JDialog {

  /** The ui controls and model to interact with. */
  private JComponent m_chooserPanel;

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
  public ADialog(final Component component, final String title, final boolean modal,
      final RangeChooserPanel chooserPane) {
    super(JOptionPane.getFrameForComponent(component), title, modal);
    this.m_chooserPanel = chooserPane;

    Container contentPane = this.getContentPane();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    contentPane.add(this.m_chooserPanel);

    // Window listeners:
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        Window w = e.getWindow();
        w.hide();
      }
    });
    this.addComponentListener(new ComponentAdapter() {
      public void componentHidden(final ComponentEvent e) {
        Window w = (Window) e.getComponent();
        w.dispose();
      }
    });

    // Chancel / OK Buttons.
    JPanel okChancelPanel = new JPanel();
    okChancelPanel.setLayout(new BoxLayout(okChancelPanel, BoxLayout.X_AXIS));
    okChancelPanel.add(Box.createHorizontalGlue());
    JButton ok = new JButton("OK");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        hide();
      }
    });
    okChancelPanel.add(ok);
    okChancelPanel.add(Box.createHorizontalGlue());
    JButton chancel = new JButton("Chancel");
    chancel.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        hide();
      }
    });
    okChancelPanel.add(chancel);
    okChancelPanel.add(Box.createHorizontalGlue());
    // add ok / chancel to ui:
    contentPane.add(okChancelPanel);
    this.setSize(new Dimension(300, 200));
  }

  /**
   * Shows a modal dialog and blocks until the dialog is hidden. If the user
   * presses the "OK" button, then this method hides/disposes the dialog and
   * returns the selected color. If the user presses the "Cancel" button or
   * closes the dialog without pressing "OK", then this method hides/disposes
   * the dialog and returns <code>null</code>.
   * <p>
   *
   *
   * @return the selected range or <code>null</code> if the user opted out.
   *
   * @exception HeadlessException
   *              if GraphicsEnvironment.isHeadless() returns true.
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public JComponent showDialog() throws HeadlessException {
    this.show(); // blocks until user brings dialog down...
    return this.m_chooserPanel;
  }

}
