/*
 *
 *  Showcase.java  jchart2d
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

import java.applet.Applet;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import aw.gui.chart.AbstractDataCollector;
import aw.gui.chart.Chart2D;
import aw.gui.chart.RandomDataCollectorOffset;
import aw.gui.chart.RangePolicyMinimumViewport;
import aw.gui.chart.Trace2DLtd;
import aw.gui.chart.layout.ChartPanel;
import aw.util.Range;

/**
 * Advanced demonstration applet for jchart2d.
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.11 $
 *
 */
public final class Showcase extends Applet {

  /**
   * Panel with controls for the chart.
   * <p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   *
   * @version $Revision: 1.11 $
   */
  final class ControlPanel extends JPanel {
    /**
     * Generated for <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3257005441048129846L;

    /** Slider for the maximum amount of points to show. */
    private JSlider m_amountPointsSlider;

    /** Button to clear data from the chart. */
    private JButton m_clear;

    /**
     * <p>
     * The <code>JComboBox</code> used to choose the color of the chart.
     * </p>
     */
    private JComboBox m_colorChooser;

    /**
     * <p>
     * The <code>JFileChooser</code> used to choose the location for saving
     * snapshot images.
     * </p>
     */
    private JFileChooser m_filechooser;

    /** The slider for choosing the speed of adding new points. */
    private JSlider m_latencyTimeSlider;

    /** Button for the action of capturing a snapshot image. */
    private JButton m_snapshot;

    /** Button to start or stop data collection. */
    private JButton m_startStop;

    /**
     * Defcon.
     * <p>
     */
    private ControlPanel() {
      // create the components:
      this.setBackground(Color.WHITE);
      this.createAmountPointSlider();
      this.createLatencySlider();
      this.createStartStopButton();
      this.createSnapShotButton();
      this.createClearButton();
      this.createColorChooserButton();

      // Layouting: Vertical Grid Layout for putting the sliders...
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      this.add(this.m_amountPointsSlider);
      this.add(this.m_latencyTimeSlider);
      // GridLayout stretches components (no respect for their preferred size):
      // Small trick by inserting another component with different Layout.
      JComponent stretch = new JPanel();
      stretch.setBackground(Color.WHITE);
      stretch.setLayout(new BoxLayout(stretch, BoxLayout.X_AXIS));
      stretch.add(Box.createHorizontalGlue());
      stretch.add(this.m_startStop);
      stretch.add(Box.createHorizontalGlue());
      stretch.add(this.m_clear);
      if (m_snapshot != null) {
        // for applet usage snapshot is null!
        stretch.add(Box.createHorizontalGlue());
        stretch.add(this.m_snapshot);
      }
      stretch.add(Box.createHorizontalGlue());
      stretch.add(this.m_colorChooser);
      stretch.add(Box.createHorizontalGlue());
      this.add(stretch);
    }

    /**
     * Helper to create a slider for maximum amount of points to show.
     * <p>
     */
    private void createAmountPointSlider() {
      // amountPointsSlider
      this.m_amountPointsSlider = new JSlider(10, 410);
      this.m_amountPointsSlider.setBackground(Color.WHITE);
      // find the value of max points:
      int maxPoints = Showcase.this.m_trace.getMaxSize();
      this.m_amountPointsSlider.setValue(maxPoints);
      this.m_amountPointsSlider.setMajorTickSpacing(40);
      this.m_amountPointsSlider.setMinorTickSpacing(20);
      this.m_amountPointsSlider.setSnapToTicks(true);
      this.m_amountPointsSlider.setPaintLabels(true);
      this.m_amountPointsSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory
          .createEtchedBorder(), "Amount of points.", TitledBorder.LEFT, TitledBorder.BELOW_TOP));
      this.m_amountPointsSlider.setPaintTicks(true);
      this.m_amountPointsSlider.addChangeListener(new ChangeListener() {
        public void stateChanged(final ChangeEvent e) {
          JSlider source = (JSlider) e.getSource();
          // Only if not currently dragged...
          if (!source.getValueIsAdjusting()) {
            int value = source.getValue();
            Showcase.this.m_trace.setMaxSize(value);
          }
        }
      });

    }

    /**
     * Helper to create a button for clearing data from the chart.
     * <p>
     */
    private void createClearButton() {
      // clear Button
      this.m_clear = new JButton("clear");
      this.m_clear.setBackground(Color.WHITE);
      this.m_clear.setBackground(Color.WHITE);
      this.m_clear.addActionListener(new ActionListener() {
        public void actionPerformed(final ActionEvent e) {
          Showcase.this.clearTrace();
        }
      });

    }

    /**
     * Helper to create a button for choosing trace colors.
     * <p>
     */
    private void createColorChooserButton() {
      // color chooser JComboBox
      this.m_colorChooser = new JComboBox();
      this.m_colorChooser.setBackground(Color.WHITE);

      /**
       * Color with a name.
       * <p>
       *
       * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
       * @version $Revision: 1.11 $
       */
      final class ColorItem extends Color {
        /**
         * Generated <code>serialVersionUID</code>.
         */
        private static final long serialVersionUID = 3257854281104568629L;

        /** The name of the color. */
        private String m_name;

        /**
         * Creates an instance with the given color and it's name.
         * <p>
         *
         * @param c
         *          the color to use.
         *
         * @param name
         *          the name of the color.
         */
        public ColorItem(final Color c, final String name) {
          super(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
          this.m_name = name;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
          return this.m_name;
        }
      }
      this.m_colorChooser.addItem(new ColorItem(Color.BLACK, "black"));
      this.m_colorChooser.addItem(new ColorItem(Color.BLUE, "blue"));
      this.m_colorChooser.addItem(new ColorItem(Color.CYAN, "cyan"));
      this.m_colorChooser.addItem(new ColorItem(Color.DARK_GRAY, "darg gray"));
      this.m_colorChooser.addItem(new ColorItem(Color.GRAY, "gray"));
      this.m_colorChooser.addItem(new ColorItem(Color.GREEN, "green"));
      this.m_colorChooser.addItem(new ColorItem(Color.LIGHT_GRAY, "light gray"));
      this.m_colorChooser.addItem(new ColorItem(Color.MAGENTA, "magenta"));
      this.m_colorChooser.addItem(new ColorItem(Color.ORANGE, "orange"));
      this.m_colorChooser.addItem(new ColorItem(Color.PINK, "pink"));
      this.m_colorChooser.addItem(new ColorItem(Color.RED, "red"));
      this.m_colorChooser.addItem(new ColorItem(Color.YELLOW, "yellow"));

      this.m_colorChooser.addActionListener(new ActionListener() {
        public void actionPerformed(final ActionEvent ae) {
          System.out.println("JComboBox colorChooser actionPerformed()");
          Color color = (Color) ((JComboBox) ae.getSource()).getSelectedItem();
          Showcase.this.m_trace.setColor(color);
        }
      });
      this.m_colorChooser.setSelectedIndex(10);
      this.m_colorChooser.setMaximumSize(new Dimension(200, this.m_clear.getMaximumSize().height));

    }

    /**
     * Helper to create a slider for speed of adding new points.
     * <p>
     */

    private void createLatencySlider() {
      // Latency slider:
      this.m_latencyTimeSlider = new JSlider(10, 210);
      this.m_latencyTimeSlider.setBackground(Color.WHITE);
      this.m_latencyTimeSlider.setValue((int) Showcase.this.m_collector.getLatency());
      this.m_latencyTimeSlider.setMajorTickSpacing(50);
      this.m_latencyTimeSlider.setMinorTickSpacing(10);
      this.m_latencyTimeSlider.setSnapToTicks(true);
      this.m_latencyTimeSlider.setPaintLabels(true);
      this.m_latencyTimeSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory
          .createEtchedBorder(), "Latency for adding points.", TitledBorder.LEFT,
          TitledBorder.BELOW_TOP));
      this.m_latencyTimeSlider.setPaintTicks(true);

      this.m_latencyTimeSlider.addChangeListener(new ChangeListener() {
        public void stateChanged(final ChangeEvent e) {
          JSlider source = (JSlider) e.getSource();
          // Only if not currently dragged...
          if (!source.getValueIsAdjusting()) {
            int value = source.getValue();
            Showcase.this.m_collector.setLatency(value);
          }
        }
      });
    }

    /**
     * Helper to create a button for taking snapshot images.
     * <p>
     */
    private void createSnapShotButton() {
      // the JFileChooser for saving snapshots:
      try {
        this.m_filechooser = new JFileChooser();
        this.m_filechooser.setAcceptAllFileFilterUsed(false);
        // the button for snapshot:
        this.m_snapshot = new JButton("snapshot");
        this.m_snapshot.setBackground(Color.WHITE);
        this.m_snapshot.addActionListener(new ActionListener() {
          public void actionPerformed(final ActionEvent e) {
            // Immediately get the image:
            BufferedImage img = Showcase.this.m_chart.snapShot();
            // clear file filters (uncool API)

            FileFilter[] farr = Showcase.ControlPanel.this.m_filechooser.getChoosableFileFilters();
            for (int i = 0; i < farr.length; i++) {
              Showcase.ControlPanel.this.m_filechooser.removeChoosableFileFilter(farr[i]);
            }
            // collect capable writers by format name (API even gets worse!)
            String[] encodings = ImageIO.getWriterFormatNames();
            Set writers = new TreeSet();
            ImageTypeSpecifier spec = ImageTypeSpecifier.createFromRenderedImage(img);
            Iterator itWriters;
            for (int i = 0; i < encodings.length; i++) {
              itWriters = ImageIO.getImageWriters(spec, encodings[i]);
              if (itWriters.hasNext()) {
                writers.add(encodings[i].toLowerCase());
              }
            }
            // add the file filters:
            itWriters = writers.iterator();
            String encoding;
            while (itWriters.hasNext()) {
              encoding = (String) itWriters.next();
              Showcase.ControlPanel.this.m_filechooser
                  .addChoosableFileFilter(new FileFilterExtensions(new String[] {encoding }));
            }

            int ret = Showcase.ControlPanel.this.m_filechooser.showSaveDialog(Showcase.this);
            if (ret == JFileChooser.APPROVE_OPTION) {
              File file = Showcase.ControlPanel.this.m_filechooser.getSelectedFile();
              // get the encoding
              encoding = Showcase.ControlPanel.this.m_filechooser.getFileFilter().getDescription()
                  .substring(2);
              try {
                ImageIO.write(img, encoding, new File(file.getAbsolutePath() + "." + encoding));
              } catch (IOException e1) {
                e1.printStackTrace();
              }
            }
          }
        });
      } catch (SecurityException se) {
        // applet context
        // grey out or leave ?
      }
    }

    /**
     * Helper to create a button to start and stop button for data collection.
     * <p>
     */
    private void createStartStopButton() {
      // Start stop Button
      this.m_startStop = new JButton("start");
      this.m_startStop.setBackground(Color.WHITE);
      this.m_startStop.addActionListener(new ActionListener() {
        public void actionPerformed(final ActionEvent e) {
          JButton source = (JButton) e.getSource();
          if (Showcase.this.m_collector.isRunning()) {
            Showcase.this.stopData();
            source.setText("start");
          } else {
            Showcase.this.startData();
            source.setText("stop");
          }
          source.invalidate();
          source.repaint();
        }
      });
    }
  }

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3904676068135678004L;

  /**
   * Main entry that uses the applet initialization.
   * <p>
   *
   * @param args
   *          ignored.
   *
   * @see #init()
   */
  public static void main(final String[] args) {
    JFrame frame = new JFrame("Showcase");
    Showcase showcase = new Showcase();
    showcase.init();
    frame.getContentPane().add(showcase);
    frame.setSize(400, 600);
    // Enable the termination button [cross on the upper right edge]:
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });
    frame.show();
  }

  /** The char to use. */
  Chart2D m_chart;

  /** The data collector to use. */
  AbstractDataCollector m_collector;

  /** The trace to use. */
  Trace2DLtd m_trace;

  /**
   * Defcon.
   *
   */
  public Showcase() {
    super();
  }

  /**
   * Clears the internal trace.
   * <p>
   */
  public synchronized void clearTrace() {
    this.m_trace.removeAllPoints();
  }

  /**
   * @see java.applet.Applet#init()
   */
  public void init() {
    super.init();
    this.m_chart = new Chart2D();
    this.setSize(new Dimension(400, 500));
    this.m_chart.setGridX(true);
    this.m_chart.setGridY(true);
    this.m_chart.getAxisY().setRangePolicy(new RangePolicyMinimumViewport(new Range(-20, +20)));
    this.m_chart.setGridColor(Color.LIGHT_GRAY);
    this.m_trace = new Trace2DLtd(100);
    this.m_trace.setName("random");
    this.m_trace.setPhysicalUnits("Milliseconds", "random value");
    this.m_trace.setColor(Color.RED);
    this.m_chart.addTrace(this.m_trace);
    this.m_collector = new RandomDataCollectorOffset(this.m_trace, 1000);
    Container content = this;
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    ChartPanel chartpanel = new ChartPanel(this.m_chart);
    content.add(chartpanel);
    content.addPropertyChangeListener(chartpanel);
    content.add(new ControlPanel());
    this.m_collector = new RandomDataCollectorOffset(this.m_trace, 50);
  }

  /**
   * Starts data collection.
   * <p>
   *
   */
  public synchronized void startData() {
    if (!this.m_collector.isRunning()) {
      this.m_collector.start();
    }
  }

  /**
   * Stops data collection.
   *
   * <p>
   */
  public synchronized void stopData() {
    if (this.m_collector.isRunning()) {
      this.m_collector.stop();
    }
  }
}
