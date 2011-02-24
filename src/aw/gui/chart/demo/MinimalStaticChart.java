package aw.gui.chart.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.Trace2DSimple;
import aw.gui.chart.layout.ChartPanel;

/**
 * <p>
 * Title: AdvancedStaticChart
 * </p>
 *
 * <p>
 * Description: A chart to test labels. This chart uses a
 * <code>{@link aw.gui.chart.LabelFormatterNumber}</code> that formats to
 * whole integer numbers. No same labels should appear.
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
 * @author Achim Westermann (original)
 *
 * @author Martin Rojo (modified)
 *
 * @version $Revision: 1.7 $
 */
public final class MinimalStaticChart extends JPanel {
  /**
   * Generated for <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3257009847668192306L;

  /**
   * Main entry.
   * <p>
   *
   * @param args
   *          ignored.
   */
  public static void main(final String[] args) {
    for (int i = 0; i < 1; i++) {
      JFrame frame = new JFrame("SampleChart");
      frame.getContentPane().add(new MinimalStaticChart());
      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(final WindowEvent e) {
          System.exit(0);
        }
      });
      frame.setSize(600, 600);
      frame.setLocation(i % 3 * 200, i / 3 * 100);
      frame.setVisible(true);
    }
  }

  /**
   * Defcon.
   */
  private MinimalStaticChart() {
    this.setLayout(new BorderLayout());
    Chart2D chart = new Chart2D();

    // Create an ITrace:
    // Note that dynamic charts need limited amount of values!!!
    // ITrace2D trace = new Trace2DLtd(200);
    // 3/11/-5 , let's try something else too:
    ITrace2D trace = new Trace2DSimple();
    trace.setColor(Color.RED);

    // Add all points, as it is static:
    double time = System.currentTimeMillis();
    for (int i = 0; i < 100; i++) {
      trace.addPoint(time + i, i * 1000);
    }
    // Add the trace to the chart:
    chart.addTrace(trace);

    // Make it visible:
    this.add(new ChartPanel(chart), BorderLayout.CENTER);

  }

}
