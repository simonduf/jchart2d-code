/**
 * MultiTracing, a demo testing the thread- safetiness of the Chart2D.
 * Copyright (C) 2002  Achim Westermann, Achim.Westermann@gmx.de
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.RangePolicyMinimumViewport;
import aw.gui.chart.Trace2DSimple;
import aw.gui.chart.layout.ChartPanel;
import aw.util.Range;

/**
 * <p>
 * An example that tests the ability of multithreaded use of a single
 * <code>Chart2D</code>. Six different Threads are painting subsequently a
 * single point to the chart and go to a sleep. After having painted the whole
 * trace, each Thread sleeps for a random time, removes it's trace, sleeps for
 * another random time and starts again. <br>
 * To be true: the data for the <code>TracePoint</code> instances is computed
 * a single time at startup.
 * </p>
 * <p>
 * This test may blow your CPU. I am currently working on an AMD Athlon 1200,
 * 512 MB RAM so I did not get these problems.
 * </p>
 *
 * @version $Revision: 1.7 $
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 */

public final class MultiTracing extends JFrame {
  /**
   * Thread that adds a trace to a chart, paints the points with configurable
   * sleep breaks and then removes it. It then goes to sleep and starts this
   * cycle anew.
   * <p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   *
   * @version $Revision: 1.7 $
   */
  static final class AddPaintRemoveThread extends Thread {

    /** The y values to paint. */
    private double[] m_data;

    /** the chart to use. */
    private Chart2D m_innnerChart;

    /** The break the Thread takes between painting two points. */
    private long m_sleep;

    /** The trace to paint to. */
    private ITrace2D m_trace;

    /**
     * Creates an instance that paints data to the trace that is added to the
     * chart.
     * <p>
     *
     * @param chart
     *          the chart to use.
     *
     * @param trace
     *          the trace to add points to.
     *
     * @param data
     *          the y values of the points to add.
     *
     * @param sleep
     *          the length of the sleep break between painting points in ms.
     */
    public AddPaintRemoveThread(final Chart2D chart, final ITrace2D trace, final double[] data,
        final long sleep) {
      this.m_innnerChart = chart;
      this.m_trace = trace;
      this.m_trace.setName(this.getName());
      this.m_data = data;
      this.m_sleep = sleep;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {

      while (true) {

        this.m_innnerChart.addTrace(this.m_trace);
        for (int i = 0; i < this.m_data.length; i++) {
          if (DEBUG) {
            System.out.println(this.getName() + " adding point to " + this.m_trace.getName());
          }
          this.m_trace.addPoint(i, this.m_data[i]);
          try {
            Thread.sleep(this.m_sleep);
          } catch (InterruptedException e) {
            e.printStackTrace(System.err);
          }

        }
        try {
          Thread.sleep((long) (Math.random() * this.m_sleep));
        } catch (InterruptedException e) {
          e.printStackTrace(System.err);
        }
        if (DEBUG) {
          System.out.println(this.getName() + " removing trace.");
        }
        this.m_innnerChart.removeTrace(this.m_trace);
        this.m_trace.removeAllPoints();

        try {
          Thread.sleep((long) (Math.random() * this.m_sleep));
        } catch (InterruptedException e) {
          e.printStackTrace(System.err);
        }
      }
    }
  }

  /** Debugging switch. */
  private static final boolean DEBUG = false;

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3256722879394820657L;

  /** Sleep break time between adding two points. */
  private static final int SLEEP = 100;

  /**
   * Helper method that generates random data for display.
   * <p>
   *
   * @param data
   *          will be filled with random y and ascending x values.
   *
   * @return the range of the generated data.
   */
  private static Range getRange(final double[][] data) {
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    double tmp;
    for (int i = data.length - 1; i >= 0; i--) {
      for (int j = data[i].length - 1; j >= 0; j--) {
        tmp = data[i][j];
        if (tmp > max) {
          max = tmp;
        }
        if (tmp < min) {
          min = tmp;
        }
      }
    }

    return new Range(min, max);
  }

  /**
   * Main entry.
   * <p>
   *
   * @param args
   *          ignored.
   */
  public static void main(final String[] args) {
    /*
     * [[xa 1,..,xa n],..,[xf 1,...,xf n]]
     */
    final double[][] data = new double[6][200];
    final java.util.Random rand = new java.util.Random();
    // first traces data
    // recursive entry:
    data[0][0] = rand.nextDouble() * 5;
    for (int i = 1; i < data[0].length; i++) {
      data[0][i] = (rand.nextDouble() < 0.5) ? data[0][i - 1] + rand.nextDouble() * 5
          : data[0][i - 1] - rand.nextDouble() * 5;
    }
    // second trace
    double tmp;
    for (int i = 0; i < data[0].length; i++) {
      tmp = Math.pow(Math.E, ((double) i) / 40) + (Math.random() < 0.5 ? data[0][i] : -data[0][i]);
      data[1][i] = tmp;
    }
    // third trace
    for (int i = 0; i < data[0].length; i++) {
      data[2][i] = Math.pow(Math.cos(((double) i) / 10) * 5, 2);
    }
    // fourth trace: numerical integration of fist trace's previous entries.
    // recursive entry:
    data[3][0] = data[0][0];
    tmp = 0;
    for (int i = 1; i < data[0].length; i++) {
      for (int j = Math.max(0, i - 10); j <= i; j++) {
        tmp += data[0][j];
      }
      data[3][i] = tmp / (((double) i) + 1);
      tmp = 0;
    }
    // fifth trace addition of second trace and third trace
    for (int i = 0; i < data[0].length; i++) {
      data[4][i] = data[1][i] + data[2][i] * (0.1 * -data[0][i]);
    }
    // sixth trace: addition of first and second trace
    for (int i = 0; i < data[0].length; i++) {
      data[5][i] = data[0][i] + data[2][i];
    }

    final MultiTracing wnd = new MultiTracing();
    wnd.setForceXRange(new Range(0, data[0].length + 10));
    wnd.setForceYRange(getRange(data));
    wnd.setLocation(100, 300);
    wnd.setSize(800, 300);
    wnd.setResizable(true);
    wnd.setVisible(true);

    ITrace2D trace;
    // first Thread:
    trace = new Trace2DSimple();
    trace.setColor(Color.red);
    new AddPaintRemoveThread(wnd.m_chart, trace, data[0], MultiTracing.SLEEP).start();
    // second Thread:
    trace = new Trace2DSimple();
    trace.setColor(Color.green);
    new AddPaintRemoveThread(wnd.m_chart, trace, data[1], (long) (MultiTracing.SLEEP * 1.5))
        .start();
    // third Thread:
    trace = new Trace2DSimple();
    trace.setColor(Color.blue);
    new AddPaintRemoveThread(wnd.m_chart, trace, data[2], (long) (MultiTracing.SLEEP * 2)).start();

    // fourth Thread:
    trace = new Trace2DSimple();
    trace.setColor(Color.cyan);
    new AddPaintRemoveThread(wnd.m_chart, trace, data[3], (long) (MultiTracing.SLEEP * 2.5))
        .start();
    // fifth Thread:
    trace = new Trace2DSimple();
    trace.setColor(Color.black);
    new AddPaintRemoveThread(wnd.m_chart, trace, data[4], (long) (MultiTracing.SLEEP * 3)).start();
    // sixth Thread:
    trace = new Trace2DSimple();
    trace.setColor(Color.white);
    new AddPaintRemoveThread(wnd.m_chart, trace, data[5], (long) (MultiTracing.SLEEP * 3.5))
        .start();

  }

  /** The chart to fill. */
  protected Chart2D m_chart = null;

  /** Defcon. */
  public MultiTracing() {
    super("MultiTracing");
    this.m_chart = new Chart2D();
    this.m_chart.setGridX(true);
    this.m_chart.setGridY(true);
    this.m_chart.setBackground(Color.lightGray);
    this.m_chart.setGridColor(new Color(0xDD, 0xDD, 0xDD));
    // add WindowListener
    addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(new ChartPanel(this.m_chart), BorderLayout.CENTER);
  }

  /**
   * Enforces to display a certain visible x range that will be expanded if
   * traces in the chart have higher or lower values.
   * <p>
   *
   * @param forceXRange
   *          the range that at least has to be kept visible.
   */
  public void setForceXRange(final Range forceXRange) {
    this.m_chart.getAxisX().setRangePolicy(new RangePolicyMinimumViewport(forceXRange));
  }

  /**
   * Enforces to display a certain visible x range that will be expanded if
   * traces in the chart have higher or lower values.
   * <p>
   *
   * @param forceYRange
   *          the range that at least has to be kept visible.
   */
  public void setForceYRange(final Range forceYRange) {
    this.m_chart.getAxisY().setRangePolicy(new RangePolicyMinimumViewport(forceYRange));
  }
}
