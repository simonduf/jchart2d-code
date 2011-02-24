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

import aw.gui.chart.AbstractDataCollector;
import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.RandomDataCollectorOffset;
import aw.gui.chart.Trace2DLtd;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Demonstrates minimal effort to create a dynamic chart.
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 */
public final class MinimalDynamicChart {

  /**
   * Main entry.
   * <p>
   *
   * @param args
   *          ignored
   */
  public static void main(final String[] args) {
    // Create a chart:
    Chart2D chart = new Chart2D();
    // Create an ITrace:
    // Note that dynamic charts need limited amount of values!!!
    ITrace2D trace = new Trace2DLtd(200);
    trace.setColor(Color.RED);

    // Add the trace to the chart:
    chart.addTrace(trace);

    // Make it visible:
    // Create a frame.
    JFrame frame = new JFrame("MinimalDynamicChart");
    // add the chart to the frame:
    frame.getContentPane().add(chart);
    frame.setSize(400, 300);
    // Enable the termination button [cross on the upper right edge]:
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });
    frame.show();
    // Every 50 milliseconds a new value is collected.
    AbstractDataCollector collector = new RandomDataCollectorOffset(trace, 50);
    collector.start();
  }

  /** Defcon. */
  private MinimalDynamicChart() {
    // nop
  }
}
