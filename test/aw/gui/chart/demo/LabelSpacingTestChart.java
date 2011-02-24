/*
 *  LabelSpacingTestChart.java of project jchart2d
 *  Copyright 2006 (C) Achim Westermann, created on 22:22:26.
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

import java.awt.Color;
import java.awt.Dimension;

import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.Trace2DLtd;

/**
 * Contributed by zoola for bug report <a href=
 * "http://sourceforge.net/tracker/index.php?func=detail&aid=1427330&group_id=50440&atid=459734"
 * target="_blank">#1427330 </a>.
 * <p>
 *
 * @author zoola
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.1 $
 */
public final class LabelSpacingTestChart extends javax.swing.JFrame {

  /**
   * Generated <code>serialVersionUID</code>.
   */
 private static final long serialVersionUID = 3689069560279873588L;

  /**
   * Creates a chart with three traces and different labels and displays it.
   * <p>
   *
   */
  public LabelSpacingTestChart() {
    Chart2D chart = new Chart2D();
    ITrace2D trace1 = new Trace2DLtd();
    trace1.setName("lottolotto");
    trace1.setColor(Color.RED);
    chart.addTrace(trace1);

    ITrace2D trace2 = new Trace2DLtd();
    trace2.setName("bbb");
    trace2.setColor(Color.BLUE);
    chart.addTrace(trace2);

    ITrace2D trace3 = new Trace2DLtd();
    trace3.setColor(Color.BLACK);
    trace3.setName("ffollejolle");
    chart.addTrace(trace3);
    getContentPane().add(chart);
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    this.setSize(new Dimension(400, 300));

  }

  /**
   * Main entry.
   * <p>
   *
   * @param args
   *          ignored.
   */
  public static void main(final String [] args) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new LabelSpacingTestChart().setVisible(true);
      }
    });
  }
}
