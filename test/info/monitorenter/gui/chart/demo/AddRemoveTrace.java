/*
 *  BlankChart.java of project jchart2d
 *  Copyright 2006 (C) Achim Westermann, created on 22:22:26.
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
package info.monitorenter.gui.chart.demo;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

/**
 * Contributed by zoola for bug report <a href=
 * "http://sourceforge.net/tracker/?func=detail&atid=459734&aid=1426461&group_id=50440"
 * target="_blank">#1426461 </a>.
 * <p>
 *
 * @author zoola
 *
 * @version $Revision: 1.2 $
 */
public final class AddRemoveTrace extends javax.swing.JFrame {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3689069560279873588L;

  /**
   * Creates an empty chart and displays it.
   * <p>
   *
   */
  public AddRemoveTrace() {
    Chart2D chart = new Chart2D();
    ITrace2D trace = new Trace2DLtd();
    chart.addTrace(trace);
    chart.removeTrace(trace);
    getContentPane().add(chart);
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    pack();
  }

  /**
   * Main entry.
   * <p>
   *
   * @param args
   *          ignored.
   */
  public static void main(final String[] args) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new AddRemoveTrace().setVisible(true);
      }
    });
  }
}
