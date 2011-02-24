/*
 *
 *  MinimalStaticChart.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 10.12.2004, 13:48:55
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package aw.gui.chart.demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;

import aw.gui.chart.AbstractDataCollector;
import aw.gui.chart.Axis;
import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.LabelFormatterDate;
import aw.gui.chart.LabelFormatterNumber;
import aw.gui.chart.RandomDataCollectorTimeStamped;
import aw.gui.chart.RangePolicyMinimumViewport;
import aw.gui.chart.Trace2DAxisSwap;
import aw.gui.chart.Trace2DLtd;
import aw.gui.chart.layout.ChartPanel;
import aw.util.Range;

/**
 * <p>
 * An example that introduces some more advanced features of jchart2d.
 * </p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.6 $
 *
 */
public final class AdvancedDynamicChart {

  /**
   * Creates a new JFrame and adds a chart that uses advanced features.
   * <p>
   *
   * @param args
   *          command line arguments, unused.
   */
  public static void main(final String[] args) {
    // Create a chart:
    Chart2D chart = new Chart2D();

    // We want to use a date format for the y axis.
    // Currently works only this way:
    Axis yAxis = new Axis();

    // Number formatter does not work for AxisAutoUnit.
    Axis xAxis = new Axis();

    // Set a date formatter:
    yAxis.setFormatter(new LabelFormatterDate(new SimpleDateFormat("HH:mm:ss")));
    chart.setAxisY(yAxis);

    // set a number formatter to get rid of the unnecessary ".0" prefixes for
    // the X-Axis:
    NumberFormat format = new DecimalFormat("#");
    // Important!
    // Or it will allow more than 100 integer digits and rendering will be
    // confused.
    // See the comment for java.text.DecimalFormat#applyPattern(String)
    format.setMaximumIntegerDigits(3);
    xAxis.setFormatter(new LabelFormatterNumber(format));

    chart.setAxisX(xAxis);
    // Try a range policy:
    // This must not be invoked before the Axis is connected to a Chart2D
    // (chart.setXAxis...)
    xAxis.setRangePolicy(new RangePolicyMinimumViewport(new Range(-10, +10)));
    // Create an ITrace:
    // Note that dynamic charts need limited amount of values!!!

    ITrace2D trace = new Trace2DLtd(20);
    trace.setColor(Color.RED);

    // set a stroke (pattern to render the trace)
    Stroke stroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f,
    // dash pattern, dash limit
        new float[]{15f, 10f }, 5f);
    trace.setStroke(stroke);

    // Add the trace to the chart:
    chart.addTrace(trace);

    // Make it visible:
    // Create a frame.
    JFrame frame = new JFrame("AdvancedDynamicChart");
    // add the chart to the frame:
    frame.getContentPane().add(new ChartPanel(chart));
    frame.setSize(400, 300);
    // Enable the termination button [cross on the upper right edge]:
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });
    frame.show();
    // Every 500 milliseconds a new value is collected.
    // The AxisSwap changes x and y data. Just a proof of concept.
    AbstractDataCollector collector = new RandomDataCollectorTimeStamped(
        new Trace2DAxisSwap(trace), 500);
    collector.start();
  }

  /** Private constructor. * */
  private AdvancedDynamicChart() {
    super();
  }
}
