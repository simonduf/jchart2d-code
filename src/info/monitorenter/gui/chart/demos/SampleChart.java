/*
 * SampleChart, a test for memory leak contributed by Martin Rojo.
 * Copyright (C) 2002  Achim Westermann, Achim.Westermann@gmx.de
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
 */
package info.monitorenter.gui.chart.demos;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.io.ADataCollector;
import info.monitorenter.gui.chart.io.RandomDataCollectorOffset;
import info.monitorenter.gui.chart.traces.Trace2DLtdReplacing;
import info.monitorenter.gui.chart.views.ChartPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * <p>
 * Sample chart from Java 2D open source package.
 * </p>
 *
 * <p>
 * Copyright: sample code taken from http://jchart2d.sourceforge.net/usage.shtml
 * </p>
 *
 * <p>
 * Company: Infotility
 * </p>
 *
 * @author Martin Rojo
 *
 * @author Achim Westermann (modified)
 *
 * @version $Revision: 1.2 $
 */
public class SampleChart extends JPanel {
  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3257009847668192306L;

  /**
   * Main entry.
   * <p>
   *
   * @param args
   *          ignored.
   *          <p>
   */
  public static void main(final String[] args) {
    for (int i = 0; i < 1; i++) {
      JFrame frame = new JFrame("SampleChart");
      frame.getContentPane().add(new SampleChart());
      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(final WindowEvent e) {
          System.exit(0);
        }
      });
      frame.setSize(600, 300);
      frame.setLocation(i % 3 * 200, i / 3 * 100);
      frame.setVisible(true);
    }
  }

  /**
   * Defcon.
   * <p>
   */
  public SampleChart() {
    this.setLayout(new BorderLayout());
    this.setMaximumSize(new Dimension(600, 600));
    this.setPreferredSize(new Dimension(500, 600));
    Chart2D chart = new Chart2D();
    // Create an ITrace:
    // Note that dynamic charts need limited amount of values!!!
    // ITrace2D trace = new Trace2DLtd(200);
    // 3/11/-5 , let's try something else too:
    ITrace2D trace = new Trace2DLtdReplacing(100);
    trace.setColor(Color.RED);

    // Add the trace to the chart:
    chart.addTrace(trace);

    // Make it visible:
    this.add(new ChartPanel(chart), BorderLayout.CENTER);

    /**
     * ** removing this for now, potential memory leak ? 3/11/05
     */

    // Enable the termination button [cross on the upperright edge]:
    // Every 50 milliseconds a new value is collected.
    ADataCollector collector = new RandomDataCollectorOffset(trace, 100);
    // Start a Thread that adds the values:
    collector.start();
    /** ********** potential memory leak ? 3/1//05 */
  }
}
