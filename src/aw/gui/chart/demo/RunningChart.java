/**
 * RunningChart, a test for the Chart2D.
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
import aw.gui.chart.Trace2DLtd;
import aw.gui.chart.layout.ChartPanel;
import aw.reflection.ObjRecorder2Trace2DAdapter;
import aw.util.Range;

/**
 *
 * A test for the <code>Chart2D</code> that constantly adds new tracepoints to
 * a <code> Trace2DLtd</code>. Mainly the runtime- scaling is interesting.
 * <p>
 * Furthermore this is an example on how to connect other components to the
 * <code>Chart2D</code> using an adaptor- class. It is "hidden" in the package
 * <i>aw.reflection </i> and called <code>ObjRecorder2Trace2DAdaptor</code>(5
 * letters under the limit!).
 * <p>
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'> Achim Westermann </a>
 *
 * @version $Revision: 1.10 $
 */
public class RunningChart extends JFrame {
  /**
   * Helper class that holds an internal number that is randomly modified by a
   * Thread.
   * <p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   *
   * @version $Revision: 1.10 $
   */
  static class RandomBumper extends Thread {
    /** Streches or compresses the grade of jumping of the internal number. */
    protected double m_factor;

    /** The bumping number. */
    protected double m_number = 0;

    /** The propability of an increase versus a decrease of the bumped number. */
    protected double m_plusminus = 0.5;

    /** Needed for randomization of bumping the number. */
    protected java.util.Random m_randomizer = new java.util.Random();

    /**
     * Creates an instance.
     * <p>
     *
     * @param plusminus
     *          probability to increase or decrease the number each step.
     *
     * @param factor
     *          affects the amplitude of the number (severity of jumps).
     */
    public RandomBumper(final double plusminus, final int factor) {

      if (plusminus < 0 || plusminus > 1) {
        System.out.println(this.getClass().getName()
            + " ignores constructor-passed value. Must be between 0.0 and 1.0!");
      } else {
        this.m_plusminus = plusminus;
      }
      this.m_factor = factor;
      this.start();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {

      while (true) {
        double rand = this.m_randomizer.nextDouble();
        if (rand < this.m_plusminus) {
          this.m_number += this.m_randomizer.nextDouble() * this.m_factor;
        } else {
          this.m_number -= this.m_randomizer.nextDouble() * this.m_factor;
        }

        try {
          sleep(40);
        } catch (InterruptedException e) {
          // nop
        }

      }
    }
  }

  /**
   * Generated for <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3545231432038627123L;

  /**
   * Main entry.
   * <p>
   *
   * @param args
   *          ignored.
   */
  public static void main(final String[] args) {

    Chart2D chart = new Chart2D();
    ITrace2D data = new Trace2DLtd(300);
    data.setColor(Color.RED);
    data.setName("random");
    data.setPhysicalUnits("hertz", "ms");
    chart.addTrace(data);
    RunningChart wnd = new RunningChart(chart, "RunningChart");
    chart.setScaleX(true);
    chart.setGridX(true);

    chart.setScaleY(true);
    chart.setGridY(true);

    // force ranges:
    chart.getAxisY().setRangePolicy(new RangePolicyMinimumViewport(new Range(-1e4, +1e4)));
    // chart.setFont(new Font(null,0,12));
    wnd.setLocation(200, 300);
    wnd.setSize(700, 210);
    wnd.setResizable(true);
    wnd.setVisible(true);
    ObjRecorder2Trace2DAdapter adapter = new ObjRecorder2Trace2DAdapter(data, new RandomBumper(0.5,
        1000), "m_number", 100);
  }

  /** The chart to use. */
  protected Chart2D m_chart = null;

  /**
   * Creates an instance that will dynamically paint on the chart to a trace
   * with the given label.
   * <p>
   *
   * @param chart
   *          the chart to use.
   *
   * @param label
   *          the name of the trace too display.
   */
  public RunningChart(final Chart2D chart, final String label) {

    super(label);
    this.m_chart = chart;
    addWindowListener(new WindowAdapter() {

      public void windowClosing(final WindowEvent e) {

        System.exit(0);
      }
    });
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(new ChartPanel(this.m_chart), BorderLayout.CENTER);
  }
}
