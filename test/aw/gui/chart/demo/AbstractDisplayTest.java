/*
 * AbstractDisplayTest.java,  <enter purpose here>.
 * Copyright (C) 2006  Achim Westermann, Achim.Westermann@gmx.de
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 */
package aw.gui.chart.demo;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import junit.framework.TestCase;
import aw.gui.chart.Axis;
import aw.gui.chart.Chart2D;
import aw.gui.chart.ILabelFormatter;
import aw.gui.chart.ILabelPainter;
import aw.gui.chart.IRangePolicy;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.ITracePainter;
import aw.gui.chart.io.PropertyFileStaticDataCollector;
import aw.util.Range;
import aw.util.units.Unit;

/**
 * <p>
 * TODO Write a comment ending with '.'
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.2 $
 *
 */
public abstract class AbstractDisplayTest extends TestCase {

  /** The frame currently visible. */
  private JFrame m_frame;

  /**
   * Chart2D inistance that failed, set by UI Threads to trigger failure on the
   * junit testcase Thread.
   */
  protected Chart2D failure = null;

  public final void testDisplay() throws IOException, InterruptedException {
    InputStream in;
    ITrace2D trace;
    StaticCollectorChart chart;
    boolean foundData = false;
    int count = 1;
    do {
      in = AbstractDisplayTest.class.getResourceAsStream("test" + count + ".properties");
      if (in == null) {
        foundData = false;
      } else {
        foundData = true;
        trace = createTrace();
        chart = new StaticCollectorChart(new PropertyFileStaticDataCollector(trace, in));
        this.configure(chart);
        this.show(chart);
      }
      count++;
    } while (foundData);
  }

  public final void fail(Chart2D wrong) {
    this.failure = wrong;

  }

  protected abstract ITrace2D createTrace();

  protected abstract void configure(StaticCollectorChart chart);

  /**
   * Internal helper that shows the chart in a frame.
   * <p>
   *
   * @param chart
   *          the chart to display.
   * @throws InterruptedException
   */
  private final void show(final StaticCollectorChart chart) throws InterruptedException {

    this.m_frame = new JFrame(this.getClass().getName());
    Container content = this.m_frame.getContentPane();
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.add(chart);

    // description of the chart:
    JTextArea description = new JTextArea();
    description.setFont(new Font("Courier", Font.PLAIN, 12));
    description.setEditable(false);
    JScrollPane scrollpane = new JScrollPane(description);
    scrollpane.setMaximumSize(new Dimension(600, 100));
    scrollpane.setPreferredSize(new Dimension(600, 100));
    content.add(scrollpane);

    // buttons for fail and ok:
    JButton fail = new JButton("fail");
    fail.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        fail(chart.getChart());
      }
    });
    JButton ok = new JButton("Ok");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        AbstractDisplayTest.this.m_frame.setVisible(false);
        AbstractDisplayTest.this.m_frame.dispose();
      }
    });
    JPanel controls = new JPanel();
    controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
    controls.add(Box.createHorizontalGlue());
    controls.add(ok);
    controls.add(Box.createHorizontalGlue());
    controls.add(fail);
    controls.add(Box.createHorizontalGlue());
    content.add(controls);

    this.m_frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        AbstractDisplayTest.this.m_frame.setVisible(false);
        AbstractDisplayTest.this.m_frame.dispose();
      }
    });
    this.m_frame.setSize(600, 600);
    this.m_frame.setVisible(true);
    Thread.sleep(1000);
    description.setText(describe(chart));
    while (this.m_frame.isVisible()) {
      try {
        Thread.sleep(1000);
        if (failure != null) {
          this.m_frame.setVisible(false);
          this.m_frame.dispose();
          this.m_frame = null;

          fail("Display Test judged bad:\n" + describe(failure));
        }
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    }
    this.m_frame.setVisible(false);
    this.m_frame.dispose();
    this.m_frame = null;

  }

  /**
   * Internal helper that describes a StaticCollectorChart.
   * <p>
   *
   * @param collectorchart
   *          the instance to describe.
   *
   * @return the description of the given instance.
   */
  private final String describe(final StaticCollectorChart collectorchart) {
    return describe(collectorchart.getChart());
  }

  /**
   * Internal helper that describes a Chart2D.
   * <p>
   *
   * @param chart
   *          the instance to describe.
   *
   * @return the description of the given instance.
   */
  private final String describe(final Chart2D chart) {
    StringBuffer result = new StringBuffer();
    result.append("Chart2D\n");
    result.append("-------\n");

    ILabelPainter labelPainter = chart.getLabelPainter();
    result.append("LabelPainter: ").append(labelPainter.getClass().getName()).append("\n");

    // X axis
    Axis axis = chart.getAxisX();
    result.append("Axis x:\n");
    IRangePolicy rangePolicy = axis.getRangePolicy();
    result.append("  RangePolicy: " + rangePolicy.getClass().getName() + ":\n");
    Range range = rangePolicy.getRange();
    result.append("    min: " + range.getMin() + "\n");
    result.append("    max: " + range.getMax() + "\n");
    ILabelFormatter labelFormatter = axis.getFormatter();
    result.append("  LabelFormatter: ").append(labelFormatter.getClass().getName()).append("\n");
    Unit unit = labelFormatter.getUnit();
    result.append("  Unit: " + unit.getClass().getName()).append("\n");
    result.append("  Major tick spacing: ").append(axis.getMajorTickSpacing()).append("\n");
    result.append("  Minor tick spacing: ").append(axis.getMajorTickSpacing()).append("\n");

    // Y axis
    axis = chart.getAxisY();
    result.append("Axis y:\n");
    rangePolicy = axis.getRangePolicy();
    result.append("  RangePolicy: " + rangePolicy.getClass().getName() + ":\n");
    range = rangePolicy.getRange();
    result.append("    min: " + range.getMin() + "\n");
    result.append("    max: " + range.getMax() + "\n");
    labelFormatter = axis.getFormatter();
    result.append("  LabelFormatter: ").append(labelFormatter.getClass().getName()).append("\n");
    unit = labelFormatter.getUnit();
    result.append("  Unit: " + unit.getClass().getName()).append("\n");
    result.append("  Major tick spacing: ").append(axis.getMajorTickSpacing()).append("\n");
    result.append("  Minor tick spacing: ").append(axis.getMajorTickSpacing()).append("\n");

    // Traces
    result.append("  Traces:\n");
    Iterator itTraces = chart.getTraces().iterator();
    ITrace2D trace;
    ITracePainter tracePainter;
    Stroke stroke;
    while (itTraces.hasNext()) {
      trace = (ITrace2D) itTraces.next();
      result.append("    ").append(trace.getClass().getName()).append(":\n");
      result.append("      x-range: [").append(trace.getMinX()).append(",").append(trace.getMaxX())
          .append("]\n");
      result.append("      y-range: [").append(trace.getMinY()).append(",").append(trace.getMaxY())
          .append("]\n");
      result.append("      Color: ").append(trace.getColor()).append("\n");
      result.append("      Label: ").append(trace.getLable()).append("\n");
      result.append("      Visible: ").append(trace.getVisible()).append("\n");
      result.append("      Z-index: ").append(trace.getZIndex()).append("\n");
      tracePainter = trace.getTracePainter();
      result.append("      TracePainter: ").append(tracePainter.getClass().getName()).append("\n");
      stroke = trace.getStroke();
      result.append("       Stroke: ").append(stroke.getClass().getName()).append("\n");
    }

    return result.toString();
  }
}
