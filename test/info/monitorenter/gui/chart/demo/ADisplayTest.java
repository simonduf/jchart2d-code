/*
 * ADisplayTest.java,  <enter purpose here>.
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
package info.monitorenter.gui.chart.demo;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ILabelFormatter;
import info.monitorenter.gui.chart.ILabelPainter;
import info.monitorenter.gui.chart.IRangePolicy;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.demos.StaticCollectorChart;
import info.monitorenter.util.Range;
import info.monitorenter.util.units.AUnit;

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
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import junit.framework.TestCase;

/**
 * Basic junit test method hook that searches for files in this package with
 * name "textX.properties" (where "X" is a number that starts from 1) collects
 * the data in the files and shows a chart with a trace that contains this data
 * along with buttons for judging the display as right or wrong.
 * <p>
 * Note that the test files have to be named with ascending numbers to ensure
 * all are shown.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.3 $
 * 
 */
public abstract class ADisplayTest
    extends TestCase {

  /** The frame currently visible. */
  private JFrame m_frame;

  /**
   * Chart2D inistance that failed, set by UI Threads to trigger failure on the
   * junit testcase Thread.
   * <p>
   */
  protected Chart2D m_failure = null;

  /**
   * Basic junit test method hook that searches for files in this package with
   * name "textX.properties" (where "X" is a number that starts from 1) collects
   * the data in the files and shows a chart with a trace that contains this
   * data along with buttons for judging the display as right or wrong.
   * <p>
   * 
   * @throws IOException
   *           if sth. goes wrong.
   * 
   * @throws InterruptedException
   *           if sleeping is interrupted.
   */
  public final void testDisplay() throws IOException, InterruptedException {
    InputStream in;
    ITrace2D trace;
    StaticCollectorChart chart;
    boolean foundData = false;
    int count = 1;
    do {
      chart = this.getNextChart();
      this.configure(chart);
      this.show(chart);
    } while (foundData);
  }

  /**
   * Template method that returns the next chart to test or null if no further
   * chart is available.
   * <p>
   * 
   * @return the next chart to test or null if no further chart is available.
   * 
   * @throws IOException
   *           if sth. goes wrong.
   */
  protected abstract StaticCollectorChart getNextChart() throws IOException;

  /**
   * Marks this test as failure.
   * <p>
   * 
   * @param wrong
   *          the chart that failed.
   *          <p>
   */
  public final void fail(final Chart2D wrong) {
    this.m_failure = wrong;

  }

  /**
   * @param arg0
   */
  public ADisplayTest(String arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

  /**
   * Template method that has to return a trace that may be configured.
   * <p>
   * 
   * @return a trace that may be configured.
   */
  protected abstract ITrace2D createTrace();

  /**
   * Template method that allows to configure the chart to show.
   * <p>
   * 
   * @param chart
   *          the chart that will be shown.
   */
  protected abstract void configure(StaticCollectorChart chart);

  /**
   * Internal helper that shows the chart in a frame.
   * <p>
   * 
   * @param chart
   *          the chart to display.
   * 
   * @throws InterruptedException
   *           if sleeping fails.
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
      public void actionPerformed(final ActionEvent ae) {
        fail(chart.getChart());
      }
    });
    JButton ok = new JButton("Ok");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent ae) {
        ADisplayTest.this.m_frame.setVisible(false);
        ADisplayTest.this.m_frame.dispose();
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
        ADisplayTest.this.m_frame.setVisible(false);
        ADisplayTest.this.m_frame.dispose();
      }
    });
    this.m_frame.setSize(600, 600);
    this.m_frame.setVisible(true);
    Thread.sleep(1000);
    description.setText(describe(chart));
    while (this.m_frame.isVisible()) {
      try {
        Thread.sleep(1000);
        if (this.m_failure != null) {
          this.m_frame.setVisible(false);
          this.m_frame.dispose();
          this.m_frame = null;

          fail("Display Test judged bad:\n" + describe(this.m_failure));
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
    IAxis axis = chart.getAxisX();
    result.append("Axis x:\n");
    IRangePolicy rangePolicy = axis.getRangePolicy();
    result.append("  RangePolicy: " + rangePolicy.getClass().getName() + ":\n");
    Range range = rangePolicy.getRange();
    result.append("    min: " + range.getMin() + "\n");
    result.append("    max: " + range.getMax() + "\n");
    ILabelFormatter labelFormatter = axis.getFormatter();
    result.append("  LabelFormatter: ").append(labelFormatter.getClass().getName()).append("\n");
    AUnit unit = labelFormatter.getUnit();
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
    Stroke stroke;
    while (itTraces.hasNext()) {
      trace = (ITrace2D) itTraces.next();
      result.append("    ").append(trace.getClass().getName()).append(":\n");
      result.append("      amount of poijnts : ").append(trace.getSize()).append("\n");
      result.append("      x-range: [").append(trace.getMinX()).append(",").append(trace.getMaxX())
          .append("]\n");
      result.append("      y-range: [").append(trace.getMinY()).append(",").append(trace.getMaxY())
          .append("]\n");
      result.append("      Color: ").append(trace.getColor()).append("\n");
      result.append("      Label: ").append(trace.getLabel()).append("\n");
      result.append("      Visible: ").append(trace.isVisible()).append("\n");
      result.append("      Z-index: ").append(trace.getZIndex()).append("\n");
      result.append("      TracePainters: \n");
      Set painters = trace.getTracePainters();
      Iterator itPainters = painters.iterator();
      while (itPainters.hasNext()) {
        result.append("        ").append(itPainters.next().getClass().getName()).append("\n");
      }
      stroke = trace.getStroke();
      result.append("       Stroke: ").append(stroke.getClass().getName()).append("\n");
    }

    return result.toString();
  }
}
