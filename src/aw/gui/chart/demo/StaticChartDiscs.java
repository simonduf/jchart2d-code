/*
 *
 *  StaticChartDiscs.java, rendering demo of jchart2d.
 *  Copyright (C) Achim Westermann, created on 10.12.2004, 13:48:55
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
package aw.gui.chart.demo;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.Trace2DSimple;
import aw.gui.chart.TracePainterDisc;
import aw.gui.chart.layout.ChartPanel;

/**
 * A demo chart that uses a {@link aw.gui.chart.TracePainterDisc}.
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.4 $
 *
 */
public final class StaticChartDiscs {

  /**
   * Main entry.
   * <p>
   *
   * @param args
   *          ignored.
   */
  public static void main(final String[] args) {
    // Create a chart:
    Chart2D chart = new Chart2D();

    // Create an ITrace:
    ITrace2D trace = new Trace2DSimple();
    trace.setTracePainter(new TracePainterDisc());
    trace.setColor(Color.DARK_GRAY);
    // Add all points, as it is static:
    double count = 0;
    double value;
    double place = 0;
    for (int i = 120; i >= 0; i--) {
      count += 1.0;
      place += 1.0;
      value = Math.random() * count * 10;
      trace.addPoint(place, value);
    }
    // Add the trace to the chart:
    chart.addTrace(trace);

    // Make it visible:
    // Create a frame.
    JFrame frame = new JFrame("StaticChartDiscs");
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
  }

  /**
   * Defcon.
   * <p>
   */
  private StaticChartDiscs() {
    // nop
  }
}
