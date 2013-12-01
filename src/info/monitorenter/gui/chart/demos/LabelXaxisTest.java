/*
 *  ZoomTest.java of project jchart2d, demonstration of a zoom-enabled
 *  chart.
 *  Copyright 2007 - 2013 (C) Achim Westermann, created on 23:59:21.
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
package info.monitorenter.gui.chart.demos;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ZoomableChart;
import info.monitorenter.gui.chart.axis.AxisLinear;
import info.monitorenter.gui.chart.labelformatters.LabelFormatterDate;
import info.monitorenter.gui.chart.pointpainters.PointPainterDisc;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.views.ScrollablePanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Demonstration of a zoom - enabled chart (
 * {@link info.monitorenter.gui.chart.ZoomableChart}).
 * <p>
 *
 * @author Alessio Sambarino (Contributor)
 * @version $Revision: 1.17 $
 */
public class LabelXaxisTest extends JFrame {

  /**
   * Action adapter for zoomAllButton.
   * <p>
   */
  class ZoomAllAdapter implements ActionListener {
    /** The zoomable chart to act upon. */
    private ZoomableChart m_zoomableChart;

    /**
     * Creates an instance that will reset zooming on the given zoomable chart
     * upon the triggered action.
     * <p>
     *
     * @param chart
     *          the target to reset zooming on.
     */
    public ZoomAllAdapter(final ZoomableChart chart) {
      this.m_zoomableChart = chart;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent event) {
      this.m_zoomableChart.zoomAll();
    }
  }

  /**
   * Generated <code>serial version UID</code>.
   * <p>
   */
  private static final long serialVersionUID = -2249660781499017221L;

  /**
   * Main startup method.
   * <p>
   *
   * @param args
   *          ignored.
   */
  public static void main(final String[] args) {

    LabelXaxisTest zoomTest = new LabelXaxisTest();
    // Show the frame
    zoomTest.setSize(640, 480);
    zoomTest.setVisible(true);

  }

  /**
   * Defcon.
   * <p>
   */
  public LabelXaxisTest() {

    super("ZoomTest");

    AxisLinear xAxis = new AxisLinear();
    AxisLinear yAxis = new AxisLinear();

    Container c = this.getContentPane();

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0, 0, 0 };
    gridBagLayout.rowHeights = new int[] {28, 245, 0 };
    gridBagLayout.columnWeights = new double[] {1.0, 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] {0.0, 1.0, Double.MIN_VALUE };
    getContentPane().setLayout(gridBagLayout);

    // Create a chart
    ZoomableChart chart = new ZoomableChart();

    chart.setAxisXBottom(xAxis, 0);
    chart.setAxisYLeft(yAxis, 0);
    chart.setGridStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.CAP_BUTT, 10.0f, new float[] {1.0f, 4.0f }, 0.0f));
    final ITrace2D trace = new Trace2DSimple("Trace");
    chart.addTrace(trace);
    trace.setColor(Color.RED);
    trace.setStroke(new BasicStroke(4));
    trace.setPointHighlighter(new PointPainterDisc(10));
    xAxis.setPaintGrid(true);
    yAxis.setPaintGrid(true);
    xAxis.setFormatter(new LabelFormatterDate(new SimpleDateFormat("HH'h'mm'm'ss.SSS's'  ")));
    // ITracePainter<?> painter = new TracePainterDisc(8);
    // trace.setTracePainter(painter);

    // Tool tips and highlighting: Both modes point out the neares trace point
    // to the cursor:
    chart.setToolTipType(Chart2D.ToolTipType.VALUE_SNAP_TO_TRACEPOINTS);
    chart.enablePointHighlighting(true);

    // Create the zoomAll button
    JButton zoomAllButton = new JButton("Zoom All");
    zoomAllButton.addActionListener(new ZoomAllAdapter(chart));

    // Add zoomAll button to the pane
    GridBagConstraints gbc_zoomAllButton = new GridBagConstraints();
    gbc_zoomAllButton.anchor = GridBagConstraints.NORTH;
    gbc_zoomAllButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_zoomAllButton.insets = new Insets(0, 0, 5, 5);
    gbc_zoomAllButton.gridx = 0;
    gbc_zoomAllButton.gridy = 0;
    c.add(zoomAllButton, gbc_zoomAllButton);

    final Random random = new Random();

    final Timer timerAddPoints = new Timer(10, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        trace.addPoint(System.currentTimeMillis(), random.nextDouble());
      }
    });

    final JButton btnStart = new JButton("Start adding points");
    btnStart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (timerAddPoints.isRunning()) {
          timerAddPoints.stop();
          btnStart.setText("Start adding points");
        } else {
          timerAddPoints.start();
          btnStart.setText("Stop adding points");
        }
      }
    });
    GridBagConstraints gbc_btnStart = new GridBagConstraints();
    gbc_btnStart.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnStart.insets = new Insets(0, 0, 5, 0);
    gbc_btnStart.gridx = 1;
    gbc_btnStart.gridy = 0;
    getContentPane().add(btnStart, gbc_btnStart);

    // Add chart to the pane
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 0;
    gbc.gridy = 1;
    ScrollablePanel scrollablePanel = new ScrollablePanel(chart);
    c.add(scrollablePanel, gbc);

    // Enable the termination button:
    this.addWindowListener(new WindowAdapter() {
      /**
       * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });

  }
}
