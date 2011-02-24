/*
 *  ModalDialog.java, class for dialogs with ok and cancel buttons and 
 *  support for modality within jchart2d.
 *  Copyright (c) 2007 Achim Westermann, created on 09:31:15.
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
package info.monitorenter.gui.chart.dialogs;

import java.awt.Component;
import java.awt.Container;
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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * Class for modal dialogs with ok and cancel buttons.
 * <p>
 * This is a try for a better design approach to modal dialogs than offered in
 * the java development kit: <br>
 * The service of a modal dialog that offers cancel and ok is separated from the
 * retrieval of data of such a dialog. The component that queries the data from
 * this service is freely choosable. It may be passed to the contstructor and
 * will be returned from {@link #showDialog()}. The client code then is sure
 * that the modal dialog has been confirmed by the human interactor and may
 * query this component for input: it knows about the component that was used to
 * query inputs.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * 
 * @version $Revision: 1.8 $
 */
public class ModalDialog
    extends JDialog {
  
  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = 6915311633181971117L;

  /**
   * Finds the frame of the given component that may be contained in a
   * <code>{@link JDialog}</code> with support for modal dialogs that are
   * launched from <code>{@link JPopupMenu}</code> instances.
   * <p>
   * This also works for nested <code>{@link javax.swing.JMenu}</code> /
   * <code>{@link javax.swing.JMenuItem}</code> trees. 
   * <p>
   * 
   * @param component
   *          the component to find the master the JFrame of.
   *          
   * @return the frame of the given component that may be contained in a
   *         <code>{@link JDialog}</code> with support for modal dialogs that
   *         are launched from <code>{@link JPopupMenu}</code> instances.
   */
  public static JFrame getFrame(final Component component) {

    Component comp = component;
    if (comp instanceof JFrame) {
      return (JFrame) comp;
    }
    if (component instanceof JPopupMenu) {
      comp = ((JPopupMenu) component).getInvoker();
    } else {
      comp = component.getParent();
    }
    return getFrame(comp);
  }

  /** The ui controls and model to interact with. */
  private JComponent m_chooserPanel;

  /** Stores whether OK or Cancel was pressed. */
  private boolean m_ok;

  /**
   * Creates a modal dialog.
   * <p>
   * 
   * @param dialogParent
   *          the parent <code>Component</code> for the dialog.
   * 
   * @param title
   *          the String containing the dialog's title.
   * 
   * @param controlComponent
   *          the UI component that is additionally shown and returned from
   *          {@link #showDialog()}.
   */
  public ModalDialog(final Component dialogParent, final String title,
      final JComponent controlComponent) {
    super(getFrame(dialogParent), title, true);
    this.m_chooserPanel = controlComponent;

    // Frame parent = getFrame(dialogParent);
    // parent.addWindowStateListener(new WindowStateListener() {
    // public void windowStateChanged(final WindowEvent e) {
    // System.out.println("State of window " + e.getSource() + " changed: " +
    // e);
    //
    // }
    // }
    //
    // );
    Container contentPane = this.getContentPane();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    contentPane.add(this.m_chooserPanel);

    // Window listeners:
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        Window w = e.getWindow();
        w.setVisible(false);
      }
    });
    // Close this Modal dialog:
    this.addComponentListener(new ComponentAdapter() {
      public void componentHidden(final ComponentEvent e) {
        Window w = (Window) e.getComponent();
        w.dispose();
      }
    });

    // Cancel / OK Buttons.
    JPanel okCancelPanel = new JPanel();
    okCancelPanel.setLayout(new BoxLayout(okCancelPanel, BoxLayout.X_AXIS));
    okCancelPanel.add(Box.createHorizontalGlue());
    JButton ok = new JButton("OK");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        ModalDialog.this.m_ok = true;
        setVisible(false);
      }
    });
    okCancelPanel.add(ok);
    okCancelPanel.add(Box.createHorizontalGlue());
    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        ModalDialog.this.m_ok = false;
        setVisible(false);
      }
    });
    okCancelPanel.add(cancel);
    okCancelPanel.add(Box.createHorizontalGlue());
    // add ok / cancel to ui:
    contentPane.add(okCancelPanel);

    this.pack();
  }

  /**
   * Returns whether OK was pressed or not.
   * <p>
   * 
   * @return whether OK was pressed or not.
   */
  public final boolean isOk() {
    return this.m_ok;
  }

  /**
   * Shows a modal dialog and blocks until the dialog is hidden.
   * <p>
   * If the user presses the "OK" button, then this method hides/disposes the
   * dialog and returns the custom component that queries for user input. If the
   * user presses the "Cancel" button or closes the dialog without pressing
   * "OK", then this method hides/disposes the dialog and returns
   * <code>null</code>.
   * <p>
   * 
   * 
   * @return the custom component given to the constructor with it's new
   *         settings or <code>null</code> if the user opted out.
   * 
   * @exception HeadlessException
   *              if GraphicsEnvironment.isHeadless() returns true.
   * 
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public JComponent showDialog() throws HeadlessException {
    // if instance is reused (several showDialog calls) reset state:
    this.m_ok = false;
    // blocks until user brings dialog down...
    this.setVisible(true);
    return this.m_chooserPanel;
  }

}
