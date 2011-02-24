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
package aw.gui.chart.layout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Stroke;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.Trace2DLtd;

/**
 * A decoration for {@link aw.gui.chart.Chart2D} that adds various controls for
 * a {@link aw.gui.chart.Chart2D} and it's {@link aw.gui.chart.ITrace2D}
 * instances in form of popup menues.
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
      data[i] = Math.random() * i;
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
      trace1.addPoint(i, data[i]);
      trace2.addPoint(i, data[i]);
    }
    // thick stroke
    Stroke stroke = new BasicStroke(2);
    trace1.setStroke(stroke);
    trace2.setStroke(stroke);

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
    LayoutFactory.getInstance().createContextMenuLable(chart);
    this.setBackground(chart.getBackground());
    // we paint our own labels
    chart.setPaintLabels(false);

    // layouting
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    this.setLayout(gridbag);
    c.fill = GridBagConstraints.BOTH;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 0.99;

    gridbag.setConstraints(chart, c);
    this.add(chart);
    Iterator itTraces = chart.iterator();
    ITrace2D trace;
    // initial Labels
    // put to a flow layout panel
    this.m_labelPanel = new JPanel();
    this.m_labelPanel.setFont(chart.getFont());
    this.m_labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    this.m_labelPanel.setBackground(chart.getBackground());
    JLabel label;
    while (itTraces.hasNext()) {
      trace = (ITrace2D) itTraces.next();
      label = LayoutFactory.getInstance().createContextMenuLable(chart, trace);
      this.m_labelPanel.add(label);
    }
    c.weighty = 0.01;
    gridbag.setConstraints(this.m_labelPanel, c);
    this.add(this.m_labelPanel);
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
        label = LayoutFactory.getInstance().createContextMenuLable(this.m_chart, newTrace);
        this.m_labelPanel.add(label);
        synchronized (this.m_chart) {
          // this.doLayout();
          // this.getLayout().layoutContainer(this);
          this.invalidate();
          this.m_labelPanel.invalidate();
          this.validateTree();
        }
        this.m_labelPanel.doLayout();
      } else if (oldTrace != null && newTrace == null) {
        // search for label:
        String labelName = oldTrace.getName();
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
