/*
 *  ChartPanel.java, a decoration of a Chart2D that adds popup menues for traces and the chart.
 *  Copyright (C) Achim Westermann, created on 19.05.2005, 22:01:51
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
package info.monitorenter.gui.chart.views;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.controls.LayoutFactory;
import info.monitorenter.gui.chart.layouts.FlowLayoutCorrectMinimumSize;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A decoration for {@link info.monitorenter.gui.chart.Chart2D} that adds
 * various controls for a {@link info.monitorenter.gui.chart.Chart2D} and it's
 * {@link info.monitorenter.gui.chart.ITrace2D} instances in form of popup
 * menues.
 * <p>
 * <h2>Performance note</h2>
 * The context menu items register themselves with the chart to adapt their
 * basic UI properties (font, foreground color, background color) via
 * weak referenced instances of
 * {@link info.monitorenter.gui.chart.controls.LayoutFactory.BasicPropertyAdaptSupport}.
 * This ensures that dropping a complete menu tree from the UI makes them
 * garbage collectable without introduction of highly unstable and
 * unmaintainable active memory management code. A side effect is that these
 * listeners remain in the property change listener list of the chart unless
 * they are finalized.
 * <p>
 * Adding and removing many traces to / from charts that are wrapped in
 * {@link ChartPanel} without {@link java.lang.System#gc()} followed by
 * {@link java.lang.System#runFinalization()} in your code will leave a huge
 * amount of listeners for non-visible uncleaned menu items in the chart which
 * causes a high cpu throttle for increasing the listener list.
 * <p>
 * The reason seems to be the implementation of ({@link javax.swing.event.EventListenerList}
 * that is used by {@link javax.swing.event.SwingPropertyChangeSupport}). It is
 * based upon an array an grows only for the space of an additional listener by
 * using
 * {@link java.lang.System#arraycopy(java.lang.Object, int, java.lang.Object, int, int)}
 * (ouch, this should be changed).
 * <p>
 * 
 * Profiling a day with showed
 * that up to 2000 dead listeners remained in the list. The cpu load increased
 * after about 200 add / remove trace operations. Good news is that no memory
 * leak could be detected.
 * <p>
 * If those add and remove trace operations on {@link ChartPanel} - connected
 * charts are performed with intermediate UI action property change events on
 * dead listeners will let them remove themselves from the listener list thus
 * avoiding the cpu overhead. So UI / user - controlled applications will
 * unlikely suffer from this problem.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public class ChartPanel extends JPanel implements PropertyChangeListener {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3905801963714197560L;

  /**
   * Main enbtry for demo app.
   * <p>
   * 
   * @param args
   *          ignored.
   */
  public static void main(final String[] args) {
    // some data:
    double[] data = new double[100];
    for (int i = 0; i < 100; i++) {
      data[i] = Math.random() * i + 1;
    }
    JFrame frame = new JFrame("ChartPanel demo");
    Chart2D chart = new Chart2D();
    // trace 1
    ITrace2D trace1 = new Trace2DLtd(100);
    trace1.setName("Trace 1");

    // AbstractDataCollector collector1 = new
    // RandomDataCollectorOffset(trace1,500);
    // trace2
    ITrace2D trace2 = new Trace2DLtd(100);
    trace2.setName("Trace 2");
    // AbstractDataCollector collector2 = new
    // RandomDataCollectorOffset(trace2,500);
    for (int i = 0; i < 100; i++) {
      trace1.addPoint(i + 2, data[i]);
      trace2.addPoint(i + 2, 100 - data[i]);
    }

    // add to chart
    chart.addTrace(trace1);
    chart.addTrace(trace2);
    frame.getContentPane().add(new ChartPanel(chart));
    frame.setSize(new Dimension(400, 600));
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent w) {
        System.exit(0);
      }

    });
    frame.setJMenuBar(LayoutFactory.getInstance().createMenuBar(chart, false));
    frame.setVisible(true);
  }

  /** The decorated chart. */
  private Chart2D m_chart;

  /**
   * <p>
   * An internal panel for the labels of the traces that uses a
   * {@link FlowLayout}.
   * </p>
   * 
   */
  protected JPanel m_labelPanel;

  /**
   * Creates an instance that decorates the given chart with controls in form of
   * popup menues.
   * <p>
   * 
   * @param chart
   *          A configured Chart2D instance that will be displayed and
   *          controlled by this panel.
   */
  public ChartPanel(final Chart2D chart) {
    super();
    this.m_chart = chart;
    this.setBackground(chart.getBackground());
    // we paint our own labels
    chart.setPaintLabels(false);
    // get the layout factory for popup menues: 
    LayoutFactory factory = LayoutFactory.getInstance();
    
    factory.createPopupMenu(chart, true);

    // layouting
    this.setLayout(new BorderLayout());
    this.add(chart, BorderLayout.CENTER);
    Iterator itTraces = chart.iterator();
    ITrace2D trace;
    // initial Labels
    // put to a flow layout panel
    this.m_labelPanel = new JPanel();
    this.m_labelPanel.setFont(chart.getFont());
    this.m_labelPanel.setLayout(new FlowLayoutCorrectMinimumSize(FlowLayout.LEFT));
    this.m_labelPanel.setBackground(chart.getBackground());
    JLabel label;
    while (itTraces.hasNext()) {
      trace = (ITrace2D) itTraces.next();
      label = factory.createContextMenuLabel(chart, trace, true);
      this.m_labelPanel.add(label);
    }
    this.add(this.m_labelPanel, BorderLayout.SOUTH);
    chart.addPropertyChangeListener(Chart2D.PROPERTY_BACKGROUND_COLOR, this);
    // listen to new Charts and deleted ones.
    chart.addPropertyChangeListener(Chart2D.PROPERTY_ADD_REMOVE_TRACE, this);

  }

  /**
   * Listens for property "background" of the <code>Chart2D</code> instance
   * that is contained in this component and sets the background color.
   * <p>
   * 
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(final PropertyChangeEvent evt) {
    String prop = evt.getPropertyName();
    if (prop.equals(Chart2D.PROPERTY_BACKGROUND_COLOR)) {
      Color color = (Color) evt.getNewValue();
      this.setBackground(color);
      this.m_labelPanel.setBackground(color);
    } else if (prop.equals(Chart2D.PROPERTY_ADD_REMOVE_TRACE)) {
      ITrace2D oldTrace = (ITrace2D) evt.getOldValue();
      ITrace2D newTrace = (ITrace2D) evt.getNewValue();
      JLabel label;
      if (oldTrace == null && newTrace != null) {
        label = LayoutFactory.getInstance().createContextMenuLabel(this.m_chart, newTrace, true);
        this.m_labelPanel.add(label);
          this.invalidate();
          this.m_labelPanel.invalidate();
          this.validateTree();
        this.m_labelPanel.doLayout();
      } else if (oldTrace != null && newTrace == null) {
        // search for label:
        String labelName = oldTrace.getLabel();
        Component[] labels = (this.m_labelPanel.getComponents());
        for (int i = 0; i < labels.length; i++) {
          if (((JLabel) labels[i]).getText().equals(labelName)) {
            this.m_labelPanel.remove(labels[i]);
            this.m_chart.removePropertyChangeListener((PropertyChangeListener) labels[i]);
            oldTrace.removePropertyChangeListener((PropertyChangeListener) labels[i]);
            // clear the popup menu listeners too:
            MouseListener[] mouseListeners = labels[i].getMouseListeners();
            for (int j = 0; j < mouseListeners.length; j++) {
              labels[i].removeMouseListener(mouseListeners[j]);
            }
            this.m_labelPanel.doLayout();
            this.doLayout();
            break;
          }
        }
      } else {
        throw new IllegalArgumentException("Bad property change event for add / remove trace.");
      }
    }
  }

}
