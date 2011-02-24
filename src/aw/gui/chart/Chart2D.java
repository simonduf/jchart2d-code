/*
 *  Chart2D, a component for displaying ITrace2D- instances.
 *  Copyright (C) 2002  Achim Westermann, Achim.Westermann@gmx.de
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
package aw.gui.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import aw.util.Range;
import aw.util.collections.TreeSetGreedy;

/**
 * <p>
 * <code> Chart2D</code> is a component for diplaying the data contained in a
 * <code>ITrace2D</code>. It inherits many features from
 * <code>javax.swing.JPanel</code> and allows specific configuration. <br>
 * In order to simplify the use of it, the scaling, labeling and choosing of
 * display- range is done automatical which flattens the free configuration.
 * </p>
 * <br>
 * <br>
 * <p>
 * There are several rules that are kept by the <code>Chart2D</code><br>
 * <ul>
 * <li>The display range is choosen always big enough to show every
 * <code>TracePoint2D</code> contained in the all <code>ITrace2d</code>
 * instances connected. <br>
 * Maybe future releases will give the "zooming" ability.
 * <li>During the <code>paint()</code> operation every
 * <code>TracePoint2D</code> is taken from the <code>ITrace2D</code>-
 * instance exactly in the order, it's iterator returns them. From every
 * <code>TracePoint2D</code> then a line is drawn to the next. <br>
 * Unordered traces may cause a weird display. Choose the right implementation
 * of <code>ITrace2D</code> to avoid this.
 * <li>If no scaling is choosen, no grids will be painted. See:
 * <code>setScalingX()</code>,<code>setScalingY()</code>. This allows
 * saving of many computations.
 * <li>Every scaling - point is set to the next even value of the chart
 * depending on the choosen decimals. If the first x- value in the chart is
 * 3.215 and decimalsX is 2, the first scalepoint of the x- axis will be shown
 * at position 3.22. This may cause problems, if the range of x -values does not
 * contain this last value!
 * <li>The distance of the scalepoints is always big enough to display the
 * labels fully without overwriting each ohter.</li>
 * </ul>
 * </p>
 * <p>
 * Problem: <br>
 * You called <code>setScalingX(true)</code>, but do not see any labels or
 * grids. <br>
 * Solution: <br>
 * Try setting the decimals to a higher value, try setting a smaller Font.
 * Especially, if high units are choosen (Tera) this problem may occur. It could
 * be solved by choosing the lowest value as first scalepoint, but then the
 * setting of decimals would force to round the displayed label so that it would
 * not show the exact value - I think this would turn this widget to a toy
 * rather than a usabel tool.
 * </p>
 * <p>
 * <h3>Demo- code:</h3>
 * 
 * <pre>
 *                            ...
 * Chart2D test = new Chart2D();
 * JFrame frame = new JFrame(&quot;Chart2D- Debug&quot;);
 *
 * frame.setSize(400,200);
 * frame.setVisible(true);
 * ITrace2D atrace = new Trace2DLtd(100);
 * ...
 * &lt;further configuration of trace&gt;
 * ...
 * test.addTrace(atrace);
 * ....
 * while(expression){
 *   atrace.addPoint(adouble,bdouble);
 *  ....
 * </pre>
 * 
 * </p>
 * 
 * <h3>PropertyChangeEvents</h3>
 * <p>
 * {@link java.beans.PropertyChangeListener} instances may be added via
 * {@link javax.swing.JComponent#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)}.
 * They inherit the properties to listen from
 * {@link java.awt.Container#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)}.
 * Additionally more <code>PropertyChangeEvents</code> are triggered.
 * <p>
 * As the set of traces inside this class is a collection (and no single
 * property) the {@link java.beans.PropertyChangeEvent} fired for a change of
 * properties property will contain a reference to the <code>Chart2D</code>
 * instance as well as the <code>ITrace2D</code> (if involved in the change).
 * <br>
 * 
 * <table >
 * <tr>
 * <th><code>property</code></th>
 * <th><code>oldValue</code></th>
 * <th><code>newValue</code></th>
 * <th>occurance</th>
 * </tr>
 * <tr>
 * <td>{@link #PROPERTY_ADD_REMOVE_TRACE}</td>
 * <td>null</td>
 * <td>&lt;ITrace2D instance&gt;</td>
 * <td>if a new instance is added.</td>
 * </tr>
 * <tr>
 * <td>{@link #PROPERTY_ADD_REMOVE_TRACE}</td>
 * <td>&lt;ITrace2D instance&gt;</td>
 * <td>null</td>
 * <td>if an instance is deleted.</td>
 * </tr>
 * <tr>
 * <td>{@link #PROPERTY_BACKGROUND_COLOR}</td>
 * <td>{@link java.awt.Color}</td>
 * <td>{@link java.awt.Color}</td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 * 
 * @version $Revision: 1.35 $
 * 
 */

public class Chart2D extends JPanel implements PropertyChangeListener {

  /**
   * <p>
   * The adapting paint Thread. It adapts its frequency of invoking
   * {@link java.awt.Component#repaint()} depending on the amount of points
   * added since it's last cycle (reported by
   * {@link Chart2D#traceChanged(Trace2DChangeEvent)}.
   * </p>
   * <p>
   * It also triggers (re-)scaling of (within {@link Chart2D#paint(Graphics)})
   * of the {@link TracePoint2D} instances in the contained {@link ITrace2D}
   * instances.
   * </p>
   * <p>
   * The speed adaption depends on the internal constants {@link #MIN_SLEEP} and
   * {@link #MAX_SLEEP}. Increasing speed is factorial (amount of new points
   * times 2), decreasing is fixed by 10 ms (if no new points are there).
   * </p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  class Painter extends Thread {
    /** The maximum sleep time between to paint invocations. */
    static final long MAX_SLEEP = 1000;

    /** The minimum sleep time between to paint invocations. */
    static final long MIN_SLEEP = 100;

    /**
     * Dynamically adapts to the update speed of data. Calculated in run().
     */
    private long m_sleepTime = Chart2D.Painter.MIN_SLEEP;

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
      try {
        while (!this.isInterrupted()) {
          sleep(this.m_sleepTime);
          // Calculation of sleeptime:
          // has to be done before repaint, as paint removes
          // pending changes! But after sleep, as in that time
          // most new points will be added.
          if (Chart2D.THREAD_DEBUG) {
            System.out.println("Painter, 0 locks");
          }

          synchronized (Chart2D.this) {
            if (Chart2D.THREAD_DEBUG) {
              System.out.println("Painter, 1 lock");
            }
            if (Chart2D.this.m_updates == 0) {
              // lazy slow down:
              if (this.m_sleepTime < Chart2D.Painter.MAX_SLEEP) {
                this.m_sleepTime += 10;
              }
            } else {
              // fast speed-up:
              this.m_sleepTime = Math.max(this.m_sleepTime - (Chart2D.this.m_updates * 2),
                  Chart2D.Painter.MIN_SLEEP);
            }
            repaint();
            Chart2D.this.m_updates = 0;
          }
          if (Chart2D.THREAD_DEBUG) {
            System.out.println("Painter, left lock");
          }
        }

      } catch (InterruptedException ie) {
        /*
         * This is the case, if call to interrupt() came while Thread was
         * sleeping.
         */

      }
    }
  }

  /**
   * <p>
   * The bean property <code>constant</code> identifying a change of the
   * internal set of <code>ITrace2D</code> instances. <br>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * </p>
   * <p>
   * As the set of traces inside this class is a collection (and no single
   * property) the {@link java.beans.PropertyChangeEvent} fired for a change of
   * this property will contain: <br>
   * <table border="0">
   * <tr>
   * <th><code>oldValue</code></th>
   * <th><code>newValue</code></th>
   * <th>occurance</th>
   * </tr>
   * <tr>
   * <td>null</td>
   * <td>&lt;ITrace2D instance&gt;</td>
   * <td>if a new instance is added.</td>
   * </tr>
   * <tr>
   * <td>&lt;ITrace2D instance&gt;</td>
   * <td>null</td>
   * <td>if an instance is deleted.</td>
   * </tr>
   * </table>
   */
  public static final String PROPERTY_ADD_REMOVE_TRACE = "addTrace";

  /**
   * <p>
   * The bean property <code>constant</code> identifying a change of the
   * background color. <br>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * </p>
   * <p>
   * The property change events for this change are constructed and fired by the
   * superclass {@link java.awt.Container} so this constant is just for
   * clarification of the String that is related to that property.
   * </p>
   */
  public static final String PROPERTY_BACKGROUND_COLOR = "background";

  /**
   * <p>
   * The bean property <code>constant</code> identifying a change of the font.
   * <br>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * </p>
   * <p>
   * The property change events for this change are constructed and fired by the
   * superclass {@link java.awt.Container} so this constant is just for
   * clarification of the String that is related to that property.
   * </p>
   */
  public static final String PROPERTY_FONT = "font";

  /**
   * <p>
   * The bean property <code>constant</code> identifying a change of the
   * foreground color. <br>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * </p>
   * <p>
   * The property change events for this change are constructed and fired by the
   * superclass {@link java.awt.Container} so this constant is just for
   * clarification of the String that is related to that property.
   * </p>
   */
  public static final String PROPERTY_FOREGROUND_COLOR = "foreground";

  /**
   * <p>
   * The bean property <code>constant</code> identifying a change of the grid
   * color. <br>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * </p>
   * 
   */
  public static final String PROPERTY_GRID_COLOR = "gridColor";

  /**
   * Generated serial version UID.
   */
  private static final long serialVersionUID = 3978425840633852978L;

  /**
   * A package wide switch for debugging problems with multithreading. Set to
   * false the compiler will remove the debugging statements.
   */
  protected static final boolean THREAD_DEBUG = false;

  /** Constant describing the x axis (needed for scaling). * */
  public static final int X = 0;

  /** Constant describing the x and y axis (needed for scaling). * */
  public static final int X_Y = 3;

  /** Constant describing the y axis (needed for scaling). * */
  public static final int Y = 1;

  /** The x axis insance. * */
  private Axis m_axisX;

  /** The y axis insance. * */
  private Axis m_axisY;

  /** The grid color. * */
  private Color m_gridcolor = Color.lightGray;

  /** Boolean switch for painting x gridlines. * */
  private boolean m_gridX = false;

  /** Boolean switch for painting y gridlines. * */
  private boolean m_gridY = false;

  /**
   * The internal label painter for this chart.
   */
  private ILabelPainter m_labelPainter;

  /** Internal Thread that manages adaptive painting speed. */
  private Painter m_painter = new Painter();

  /**
   * Flag that decides wether labels for traces are painted below the chart.
   */
  private boolean m_paintLabels = true;

  /** Boolean switch for painting x scale. * */
  private boolean m_scaleX = true;

  /** Boolean switch for painting y scale. * */
  private boolean m_scaleY = true;

  /**
   * The internal <code>TreeSetGreedy</code> use to store the different
   * <code>ITrace2d</code> instanes to paint.
   */
  private TreeSetGreedy m_traces = new TreeSetGreedy();

  /**
   * <p>
   * An internal counter that is increased for every bound property change event
   * received from traces.
   * </p>
   * <p>
   * It is reset whenever the painting Thread triggers a repaint and used to
   * calculate the new time it will sleep until starting the next paint
   * operation.
   * </p>
   */
  private int m_updates = 0;

  /**
   * The start x coordinate of the chart.
   */
  private int m_xChartStart;

  /** The current maximum x value for all points in all traces. */
  private double m_xmax;

  /**
   * The maximum x value for all points in all traces of the previous paint
   * operation.
   */
  private double m_xmaxold;

  /** The current minimum x value for all points in all traces. */
  private double m_xmin;

  /**
   * The minimum x value for all points in all traces of the previous paint
   * operation.
   */
  private double m_xminold;

  /**
   * The start y coordinate of the chart.
   */
  private int m_yChartStart;

  /** The current maximum y value for all points in all traces. */
  private double m_ymax;

  /**
   * The maximum y value for all points in all traces of the previous paint
   * operation.
   */
  private double m_ymaxold;

  /** The current minimum y value for all points in all traces. */
  private double m_ymin;

  /**
   * The minimum y value for all points in all traces of the previous paint
   * operation.
   */
  private double m_yminold;

  /**
   * Creates a new chart.
   * <p>
   */
  public Chart2D() {
    this.setAxisX(new Axis());
    this.setAxisY(new Axis());
    this.setLabelPainter(new LabelPainterDefault());

    Font dflt = this.getFont();
    if (dflt != null) {
      this.setFont(new Font(dflt.getFontName(), dflt.getStyle(), 10));
    }
    this.getBackground();
    this.setBackground(Color.white);
    // don't continue when application stops.
    this.m_painter.setDaemon(true);
    this.m_painter.start();
  }

  /**
   * <p>
   * Adds the trace to this chart. It will be painted (if it's
   * {@link ITrace2D#getVisible()} returns true) in this chart.
   * </p>
   * <p>
   * This method will trigger a {@link java.beans.PropertyChangeEvent} being
   * fired on all instances registered by
   * {@link javax.swing.JComponent#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)}
   * (registered with <code>String</code> argument
   * {@link #PROPERTY_ADD_REMOVE_TRACE}).
   * </p>
   * 
   * @see Chart2D#PROPERTY_ADD_REMOVE_TRACE
   */
  public final void addTrace(final ITrace2D points) {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("trace.addTrace, 0 locks");
    }
    synchronized (this) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("trace.addTrace, 1 lock");
      }
      synchronized (points) {
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("trace.addTrace, 2 locks");
        }

        points.setRenderer(this);
        this.m_traces.add(points);
        // listen to bound changes
        points.addPropertyChangeListener(ITrace2D.PROPERTY_MAX_X, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_MIN_X, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_MAX_Y, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_MIN_Y, this);
        // listen to newly added points
        // this is needed for scaling at point level.
        // else every bound change would force to rescale all traces!
        points.addPropertyChangeListener(ITrace2D.PROPERTY_TRACEPOINT, this);

        // for static traces (all points added) we won't get events.
        // so update here:
        boolean change = false;
        double maxX = points.getMaxX();
        if (maxX > this.m_xmax) {
          this.m_xmax = maxX;
          change = true;
        }
        double maxY = points.getMaxY();
        if (maxY > this.m_ymax) {
          this.m_ymax = maxY;
          change = true;
        }
        double minX = points.getMinX();
        if (minX < this.m_xmin) {
          this.m_xmin = minX;
          change = true;
        }
        double minY = points.getMinY();
        if (minY < this.m_ymin) {
          this.m_ymin = minY;
          change = true;
        }

        // initial scaling:
        // if a change in bounds was recorded here, scaling
        // will be done by the paint method. If not, new traces
        // (that could contain points already) have to be scaled here.
        if (!change) {
          this.scaleTrace(points, Chart2D.X_Y);
        }
        // special case: first trace added:
        if (this.m_traces.size() == 1) {
          this.m_ymin = minY;
          this.m_ymax = maxY;
          this.m_xmin = minX;
          this.m_xmax = maxX;
        }

      }
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("trace.addTrace, left 1 lock: 1 remaining");
      }
    }
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("trace.addTrace, left 1 lock:  0 remaining");
    }
    // A deadlock occurs if a listener triggers paint.
    // This was the case with ChartPanel.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Chart2D.this.firePropertyChange(Chart2D.PROPERTY_ADD_REMOVE_TRACE, null, points);
      }
    });
  }

  /**
   * Cleanup when this instance is dropped.
   * <p>
   * 
   * The internal painter thread is stoppped.
   * <p>
   * 
   * @see java.lang.Object#finalize()
   */
  public void finalize() {
    this.destroy();
  }

  /**
   * Destroys the chart by stopping the internal painter thread.
   * <p>
   * This method is only of interest if you have an application that dynamically
   * adds and removes charts. So if you use the same Chart2D object(s) during
   * the applications lifetime there is no need to use this method.
   * <p>
   */
  public void destroy() {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("destroy, 0 locks");
    }
    synchronized (this) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("destroy, 1 lock");
      }
      this.m_painter.interrupt();
    }
  }

  /**
   * <p>
   * Searches for the maximum x value of all contained ITraces.
   * </p>
   * <p>
   * This method is triggered when a trace fired a property change for property
   * <code>{@link ITrace2D#PROPERTY_MAX_X}</code> with a value lower than the
   * internal stored maximum x.
   * </p>
   * 
   * <p>
   * Performance breakdown is avoided because all <code>ITrace2D</code>
   * implementations cache their max and min values.
   * </p>
   * <p>
   * Note that the <code>Chart2D</code> itself does not use this value for
   * painting. It uses {@link Axis#getRange()} which itself accesses this value
   * by accessors and additionally filters the value by it's assigned internal
   * {@link AbstractRangePolicy}.
   * </p>
   * 
   * @return the maximum x value of all traces.
   */
  protected final double findMaxX() {
    double max = -Double.MAX_VALUE, tmp;
    Iterator it = this.m_traces.iterator();
    while (it.hasNext()) {
      tmp = ((ITrace2D) it.next()).getMaxX();
      if (tmp > max) {
        max = tmp;
      }
    }
    if (max == -Double.MAX_VALUE) {
      max = 10;
    }
    return max;
  }

  /**
   * <p>
   * Searches for the maximum y value of all contained ITraces.
   * </p>
   * <p>
   * This method is triggered when a trace fired a property change for property
   * <code>{@link ITrace2D#PROPERTY_MAX_Y}</code> with a value lower than the
   * internal stored maximum x.
   * </p>
   * <p>
   * Note that the <code>Chart2D</code> itself does not use this value for
   * painting. It uses {@link Axis#getRange()} which itself accesses this value
   * by accessors and additionally filters the value by it's assigned internal
   * {@link AbstractRangePolicy}.
   * </p>
   * 
   * @return the maximum y value of all traces.
   */
  protected final double findMaxY() {
    double max = -Double.MAX_VALUE, tmp;
    Iterator it = this.m_traces.iterator();
    while (it.hasNext()) {
      tmp = ((ITrace2D) it.next()).getMaxY();
      if (tmp > max) {
        max = tmp;
      }
    }
    if (max == -Double.MAX_VALUE) {
      max = 10;
    }
    return max;
  }

  /**
   * <p>
   * Searches for the minimum x value of all contained ITraces.
   * </p>
   * <p>
   * This method is triggered when a trace fired a property change for property
   * <code>{@link ITrace2D#PROPERTY_MIN_Y}</code> with a value lower than the
   * internal stored maximum x.
   * </p>
   * <p>
   * Note that the <code>Chart2D</code> itself does not use this value for
   * painting. It uses {@link Axis#getRange()} which itself accesses this value
   * by accessors and additionally filters the value by it's assigned internal
   * {@link AbstractRangePolicy}.
   * </p>
   * 
   * @return the minimum x value of all traces.
   * 
   */

  protected final double findMinX() {
    double min = Double.MAX_VALUE, tmp;
    Iterator it = this.m_traces.iterator();
    while (it.hasNext()) {
      tmp = ((ITrace2D) it.next()).getMinX();
      if (tmp < min) {
        min = tmp;
      }
    }
    if (min == Double.MAX_VALUE) {
      min = 0;
    }
    return min;
  }

  /**
   * <p>
   * Searches for the minimum y value of all contained ITraces.
   * </p>
   * <p>
   * This method is triggered when a trace fired a property change for property
   * <code>{@link ITrace2D#PROPERTY_MIN_Y}</code> with a value lower than the
   * internal stored maximum x.
   * </p>
   * <p>
   * Note that the <code>Chart2D</code> itself does not use this value for
   * painting. It uses {@link Axis#getRange()} which itself accesses this value
   * by accessors and additionally filters the value by it's assigned internal
   * {@link AbstractRangePolicy}.
   * </p>
   * 
   * @return the minimum y value of all traces.
   */

  protected final double findMinY() {
    double min = Double.MAX_VALUE, tmp;
    Iterator it = this.m_traces.iterator();
    while (it.hasNext()) {
      tmp = ((ITrace2D) it.next()).getMinY();
      if (tmp < min) {
        min = tmp;
      }
    }
    if (min == Double.MAX_VALUE) {
      min = 0;
    }
    return min;
  }

  /**
   * <p>
   * Returns the axis for the x dimension.
   * </p>
   * 
   * @return the axis for the x dimension.
   */
  public final Axis getAxisX() {
    return this.m_axisX;
  }

  /**
   * <p>
   * Returns the axis for the y dimension.
   * </p>
   * 
   * @return the axis for the y dimension.
   */
  public final Axis getAxisY() {
    return this.m_axisY;
  }

  /**
   * <p>
   * Returns the color of the grid.
   * </p>
   * 
   * @return the color of the grid.
   */
  public final Color getGridColor() {
    return this.m_gridcolor;
  }

  /**
   * <p>
   * Returns wether the x grid is painted or not.
   * </p>
   * 
   * @return wether the x grid is painted or not.
   * 
   */
  public final boolean getGridX() {
    return this.m_gridX;
  }

  /**
   * <p>
   * Returns wether the y grid is painted or not.
   * </p>
   * 
   * @return wether the y grid is painted or not.
   */
  public final boolean getGridY() {
    return this.m_gridY;
  }

  /**
   * Returns the painter for the labels.
   * <p>
   * 
   * @return Returns the painter for the labels.
   */
  public ILabelPainter getLabelPainter() {
    return this.m_labelPainter;
  }

  /**
   * <p>
   * Returns the maximum x-value of all contained <code>{@link ITrace2D}</code>
   * instances.
   * </p>
   * 
   * @return the maximum x-value of all contained <code>{@link ITrace2D}</code>
   *         instances.
   * 
   */
  public final double getMaxX() {
    return this.m_xmax;
  }

  /**
   * <p>
   * Returns the maximum y-value of all contained <code>{@link ITrace2D}</code>
   * instances.
   * </p>
   * 
   * @return the maximum y-value of all contained <code>{@link ITrace2D}</code>
   *         instances.
   * 
   */
  public final double getMaxY() {
    return this.m_ymax;
  }

  /**
   * <p>
   * Returns the minimum x-value of all contained <code>{@link ITrace2D}</code>
   * instances.
   * </p>
   * 
   * @return the minimum x-value of all contained <code>{@link ITrace2D}</code>
   *         instances.
   * 
   * @see #getOffsetX()
   */
  public final double getMinX() {
    return this.m_xmin;
  }

  /**
   * <p>
   * Returns the minimum y-value of all contained <code>{@link ITrace2D}</code>
   * instances.
   * </p>
   * 
   * @return the minimum y-value of all contained <code>{@link ITrace2D}</code>
   *         instances.
   * 
   * @see #getOffsetY()
   */
  public final double getMinY() {
    return this.m_ymin;
  }

  /**
   * <p>
   * Returns the minimum x-value displayed by this chart.
   * </p>
   * <p>
   * This value is not neccessarily the minimum x-value of all internal
   * <code>{@link ITrace2D}</code> instances. Rounding or forced bounds on the
   * internal <code>X - {@link Axis}</code> may have an effect on this value.
   * </p>
   * 
   * @return the minimum x-value displayed by this chart.
   * @see #getMinX()
   */
  public final double getOffsetX() {
    return this.m_axisX.getRange().getMin();
  }

  /**
   * <p>
   * Returns the minimum y-value displayed by this chart.
   * </p>
   * <p>
   * This value is not neccessarily the minimum y-value of all internal
   * <code>{@link ITrace2D}</code> instances. Rounding or forced bounds on the
   * internal <code>Y - {@link Axis}</code> may have an effect on this value.
   * </p>
   * 
   * @return the minimum y-value displayed by this chart.
   * @see #getMinY()
   */
  public final double getOffsetY() {
    return this.m_axisY.getMin();
  }

  /**
   * <p>
   * Scales the given absolute value into a value between 0 and 1.0 (if it is in
   * the x- range of the data).
   * </p>
   * <p>
   * The absoluteX is lowered by offsetX to relate it to the rest of the data of
   * the Points. If the given absoluteX is not in the display- range of the
   * Chart2DData (offsetX), negative values or values greater than 1.0 may
   * result.
   * </p>
   * 
   * @param absoluteX
   *          a value in the real value range of this chart.
   * @return a value between 0.0 and 1.0 that is mapped to a position within the
   *         chart.
   */
  public final double getScaledValueX(final double absoluteX) {
    Range range = this.m_axisX.getRange();
    double scalerX = range.getExtent();
    return (absoluteX - range.getMin()) / scalerX;
  }

  /**
   * <p>
   * Scales the given absolute value into a value between 0 and 1.0 (if it is in
   * the y- range of the data).
   * </p>
   * <p>
   * The absoluteValue is lowered by offsetY to relate it to the rest of the
   * data of the Points. If the given absoluteX is not in the display- range of
   * the Chart2DData (offsetX), negative values or values greater than 1.0 may
   * result.
   * </p>
   * 
   * @param absoluteY
   *          a value in the real value range of this chart.
   * @return a value between 0.0 and 1.0 that is mapped to a position within the
   *         chart.
   */

  public final double getScaledValueY(final double absoluteY) {
    double scalerY = this.m_ymax - this.m_ymin;
    return (absoluteY - this.m_ymin) / scalerY;
  }

  /**
   * <p>
   * Returns the internal set of traces that are currently rendered by this
   * instance.
   * </p>
   * 
   * <p>
   * Caution: the original internal set is returned. Modifications on the
   * returned set could cause problems.
   * </p>
   * 
   * @return the internal set of traces that are currently rendered by this
   *         instance.
   * 
   * 
   */
  public final Set getTraces() {
    return this.m_traces;
  }

  /**
   * Returns the x coordinate of the chart's upper left edge in px.
   * <p>
   * 
   * @return Returns the x coordinate of the chart's upper left edge in px.
   */
  protected int getXChartStart() {
    return this.m_xChartStart;
  }

  /**
   * Returns the x coordinate of the chart's upper left edge in px.
   * <p>
   * 
   * @return Returns the y coordinate of the chart's upper left edge in px.
   */
  protected int getYChartStart() {
    return this.m_yChartStart;
  }

  /**
   * Interpolates (linear) the two neighbouring points.
   * <p>
   * 
   * Calling this method only makes sense if argument visible is a visible point
   * and argument invisible is an invisible point.
   * <p>
   * 
   * Visibility is determined only by their internally normalized coordinates
   * that are withen [0.0,1.0] for visible points.
   * <p>
   * 
   * @param visible
   *          the visible point.
   * @param invisible
   *          the invisible point.
   * 
   * @return the interpolation towards the exceeded bound.
   */
  private TracePoint2D interpolateVisible(final TracePoint2D invisible, final TracePoint2D visible) {

    /*
     * Interpolation is done by the two point form:
     * 
     * (y - y1)/(x - x1) = (y2 - y1)/(x2 - x1)
     * 
     * solved to the missing value.
     */
    // interpolate
    double xInterpolate;
    double yInterpolate;
    // find the bound that has been exceeded:
    if (invisible.m_scaledX > 1.0) {
      // right x bound
      xInterpolate = 1.0;
      yInterpolate = (visible.m_scaledY - invisible.m_scaledY)
          / (visible.m_scaledX - invisible.m_scaledX) * (1.0 - invisible.m_scaledX)
          + invisible.m_scaledY;
    } else if (invisible.m_scaledX < 0.0) {
      // left x bound
      xInterpolate = 0.0;
      yInterpolate = (visible.m_scaledY - invisible.m_scaledY)
          / (visible.m_scaledX - invisible.m_scaledX) * -invisible.m_scaledX + invisible.m_scaledY;
    } else if (invisible.m_scaledY > 1.0) {
      // upper y bound, checked
      yInterpolate = 1.0;
      xInterpolate = (1.0 - invisible.m_scaledX);
      xInterpolate *= (visible.m_scaledX - invisible.m_scaledX);
      xInterpolate /= (visible.m_scaledY - invisible.m_scaledY);
      xInterpolate += visible.m_scaledX;
    } else {
      // lower y bound
      yInterpolate = 0.0;
      xInterpolate = -invisible.m_scaledX * (visible.m_scaledX - invisible.m_scaledX)
          / (visible.m_scaledY - invisible.m_scaledY) + visible.m_scaledX;
    }
    // TODO: do we have to compute and set the unscaled real values too?
    TracePoint2D result = new TracePoint2D(0.0, 0.0);
    result.m_scaledOnce = true;
    result.m_scaledX = xInterpolate;
    result.m_scaledY = yInterpolate;
    return result;
  }

  /**
   * <p>
   * Returns true if labels for each chart are painted below it, false else.
   * </p>
   * 
   * @return Returns if labels are painted.
   */
  public final boolean isPaintLabels() {
    return this.m_paintLabels;
  }

  /**
   * Returns true if the given point is in the visible drawing area of the
   * Chart2D.
   * <p>
   * If the point is null false will be returned.
   * <p>
   * 
   * This only works if the point argument has been scaled already.
   * <p>
   * 
   * @param point
   *          the point to test.
   * 
   * @return true if the given point is in the visible drawing area of the
   *         Chart2D.
   */
  private boolean isVisible(final TracePoint2D point) {
    if (point == null) {
      return false;
    }
    return !(point.m_scaledX > 1.0 || point.m_scaledX < 0.0 || point.m_scaledY > 1.0 || point.m_scaledY < 0.0);
  }

  /**
   * Returns an <code>Iterator</code> over the contained {@link ITrace2D}
   * instances.
   * 
   * @return an <code>Iterator</code> over the contained {@link ITrace2D}
   *         instances.
   * 
   */
  public final Iterator iterator() {
    return this.m_traces.iterator();
  }

  /**
   * <p>
   * A basic rule of a JComponent is: <br>
   * <b>Never invoke this method directly. </b> <br>
   * See the description of
   * {@link javax.swing.JComponent#paint(java.awt.Graphics)} for details.
   * </p>
   * <p>
   * If you do invoke this method you may encounter performance issues,
   * flickering UI and even deadlocks.
   * </p>
   * 
   * @param g
   *          the graphics context to use.
   * 
   */
  public synchronized void paint(final Graphics g) {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("paint, 1 lock");
    }
    super.paint(g);
    this.m_axisX.initPaintIteration();
    this.m_axisY.initPaintIteration();
    // update the scaling
    this.updateScaling();
    Dimension d = this.getSize();
    int width = (int) d.getWidth();
    int height = (int) d.getHeight();
    // finding the font- dimensions in px
    FontMetrics fontdim = g.getFontMetrics();
    int fontwidth = fontdim.charWidth('0');
    int fontheight = fontdim.getHeight();
    // includes leading space between Axis lines
    // one extra char for separation space
    this.m_xChartStart = (this.m_axisY.getFormatter().getMaxAmountChars()) * fontwidth;

    // will be used in several iterations.
    ITrace2D tmpdata;
    Iterator traceIt;
    ITracePainter tracePainter;
    ILabelPainter labelPainter = this.getLabelPainter();
    // Some operations (e.g. stroke) need Graphics2d
    Graphics2D g2d = (Graphics2D) g;

    // painting trace labels
    int labelheight = this.paintTraceLables(g2d);
    // finding startpoint of coordinate System.
    // -4 is for showing colons of x - labels that are below the baseline
    this.m_yChartStart = height - fontheight - labelheight - 4;

    int endx = width - 20;
    int endy = 20;
    int rangex = endx - this.m_xChartStart;
    int rangey = this.m_yChartStart - endy;
    // drawing the coordinate lines.
    g.setColor(this.getForeground());
    g.drawLine(this.m_xChartStart, this.m_yChartStart, endx, this.m_yChartStart);
    g.drawLine(this.m_xChartStart, this.m_yChartStart, this.m_xChartStart, endy);
    // drawing the labels.
    int tmp;
    LabeledValue[] labels;
    if (this.m_scaleX) {
      // first for x- angle.
      tmp = 0;
      labels = this.m_axisX.getScaleValues();
      for (int i = 0; i < labels.length; i++) {
        tmp = this.m_xChartStart + (int) (labels[i].getValue() * rangex);
        labelPainter.paintXTick(tmp, this.m_yChartStart, labels[i].isMajorTick(), g2d);
        labelPainter.paintXLabel(tmp, this.m_yChartStart + fontheight, labels[i].getLabel(), g2d);
        if (this.m_gridX) {
          if ((i != 0) || (tmp != this.m_xChartStart)) {
            g.setColor(this.m_gridcolor);
            g.drawLine(tmp, this.m_yChartStart - 1, tmp, endy);
            g.setColor(this.getForeground());

          }
        }
      }
      // unit-labeling
      g.drawString(this.m_axisX.getFormatter().getUnit().getUnitName(), endx - 20,
          this.m_yChartStart - 5);
    }
    if (this.m_scaleY) {
      // then for y- angle.
      labels = this.m_axisY.getScaleValues();
      for (int i = 0; i < labels.length; i++) {
        tmp = this.m_yChartStart - (int) (labels[i].getValue() * rangey);
        labelPainter.paintYTick(this.m_xChartStart, tmp, labels[i].isMajorTick(), g2d);
        labelPainter.paintYLabel(2, tmp, labels[i].getLabel(), g2d);
        if (this.m_gridY) {
          if ((i != 0) || (tmp != this.m_yChartStart)) {
            g.setColor(this.m_gridcolor);
            g.drawLine(this.m_xChartStart + 1, tmp, endx, tmp);
            g.setColor(this.getForeground());
          }
        }
      }
      // unit-labeling
      g.drawString(this.m_axisY.getFormatter().getUnit().getUnitName(), this.m_xChartStart,
          endy - 5);
    }
    // paint Traces.
    int tmpx, oldtmpx, tmpy, oldtmpy;
    TracePoint2D oldpoint = null, newpoint = null, tmppt = null;
    traceIt = this.m_traces.iterator();
    Stroke backupStroke = g2d.getStroke();
    int count = 0;
    Iterator itTracePainters;
    while (traceIt.hasNext()) {
      count++;
      tmpdata = (ITrace2D) traceIt.next();
      if (tmpdata.getVisible()) {

        g2d.setStroke(tmpdata.getStroke());
        g2d.setColor(tmpdata.getColor());
        itTracePainters = tmpdata.getTracePainters().iterator();
        synchronized (tmpdata) {
          if (Chart2D.THREAD_DEBUG) {
            System.out.println("paint, 2 locks");
          }
          while (itTracePainters.hasNext()) {
            tracePainter = (ITracePainter) itTracePainters.next();
            tracePainter.startPaintIteration();
            Iterator pointIt = tmpdata.iterator();
            boolean newpointVisible, oldpointVisible;
            // searching the first valid point, done as a wrapping loop to cope
            // with zero points.
            while (pointIt.hasNext()) {
              oldpoint = newpoint;
              newpoint = (TracePoint2D) pointIt.next();

              newpointVisible = isVisible(newpoint);
              oldpointVisible = isVisible(oldpoint);
              if (newpointVisible || oldpointVisible) {
                tmpx = this.m_xChartStart + (int) (newpoint.m_scaledX * rangex);
                tmpy = this.m_yChartStart - (int) (newpoint.m_scaledY * rangey);
                while (pointIt.hasNext()) {
                  oldpoint = newpoint;
                  oldtmpx = tmpx;
                  oldtmpy = tmpy;
                  newpoint = (TracePoint2D) pointIt.next();
                  newpointVisible = isVisible(newpoint);
                  oldpointVisible = isVisible(oldpoint);
                  if (!newpointVisible && !oldpointVisible) {
                    // nothing to paint...
                    continue;
                  } else if (newpointVisible && !oldpointVisible) {
                    // entering the visible bounds: interpolate from old point
                    // to
                    // new point
                    oldpoint = interpolateVisible(oldpoint, newpoint);
                    tmpx = this.m_xChartStart + (int) (newpoint.m_scaledX * rangex);
                    tmpy = this.m_yChartStart - (int) (newpoint.m_scaledY * rangey);
                    oldtmpx = this.m_xChartStart + (int) (oldpoint.m_scaledX * rangex);
                    oldtmpy = this.m_yChartStart - (int) (oldpoint.m_scaledY * rangey);
                    tracePainter.paintPoint(oldtmpx, oldtmpy, tmpx, tmpy, g2d);

                  } else if (!newpointVisible && oldpointVisible) {
                    // leaving the visible bounds:
                    tmppt = (TracePoint2D) newpoint.clone();
                    newpoint = interpolateVisible(newpoint, oldpoint);
                    tmpx = this.m_xChartStart + (int) (newpoint.m_scaledX * rangex);
                    tmpy = this.m_yChartStart - (int) (newpoint.m_scaledY * rangey);

                    tracePainter.paintPoint(oldtmpx, oldtmpy, tmpx, tmpy, g2d);
                    tracePainter.discontinue();
                    // restore for next loop start:
                    newpoint = tmppt;

                  } else {
                    // staying in the visible bounds: just paint
                    tmpx = this.m_xChartStart + (int) (newpoint.m_scaledX * rangex);
                    tmpy = this.m_yChartStart - (int) (newpoint.m_scaledY * rangey);

                    tracePainter.paintPoint(oldtmpx, oldtmpy, tmpx, tmpy, g2d);
                  }
                }
              }
            }
            tracePainter.endPaintIteration();
          }

        }
        if (Chart2D.THREAD_DEBUG) {

          System.out.println("paint, left lock");
        }
      }
    }
    g2d.setStroke(backupStroke);
  }

  /**
   * Internally paints the labels for the traces below the chart.
   * <p>
   * 
   * @param g2d
   *          the graphic context to use.
   * 
   * @return the amount of vertical (y) px used for the labels.
   * 
   */

  private int paintTraceLables(final Graphics2D g2d) {
    int labelheight = 0;
    Dimension d = this.getSize();
    if (this.m_paintLabels) {
      ITrace2D trace;
      Iterator traceIt = this.m_traces.iterator();
      int xtmpos = this.m_xChartStart;
      int ytmpos = (int) d.getHeight() - 2;
      int remwidth = (int) d.getWidth() - this.m_xChartStart;
      int allwidth = remwidth;
      int lblwidth = 0;
      String tmplabel;
      boolean crlfdone = false;
      // finding the font- dimensions in px
      FontMetrics fontdim = g2d.getFontMetrics();
      int fontheight = fontdim.getHeight(); // includes leading space

      if (traceIt.hasNext()) {
        labelheight += fontheight;
      }
      while (traceIt.hasNext()) {
        trace = (ITrace2D) traceIt.next();
        if (trace.getVisible()) {
          tmplabel = trace.getLable();
          lblwidth = fontdim.stringWidth(tmplabel) + 10;
          // conditional linebreak.
          // crlfdone avoids never doing linebreak if all
          // labels.length()>allwidth
          if (lblwidth > remwidth) {
            if (!(lblwidth > allwidth) || (!crlfdone)) {
              ytmpos -= fontheight;
              xtmpos = this.m_xChartStart;
              labelheight += fontheight;
              crlfdone = true;
              remwidth = (int) d.getWidth() - this.m_xChartStart;
            } else {
              crlfdone = false;
            }
          }
          remwidth -= lblwidth;
          g2d.setColor(trace.getColor());
          g2d.drawString(tmplabel, xtmpos, ytmpos);
          xtmpos += lblwidth;
        }
      }
    }
    return labelheight;
  }

  /**
   * <p>
   * Called from {@link AbstractTrace2D#setZIndex(Integer)} to show that
   * property {@link ITrace2D#PROPERTY_ZINDEX} has changed.
   * </p>
   * <p>
   * This class adds itself as a {@link PropertyChangeListener} to the
   * {@link AbstractTrace2D} in method {@link Chart2D#addTrace(ITrace2D)}.
   * </p>
   * <p>
   * Also used for properties <code>{@link ITrace2D#PROPERTY_MAX_X}</code>,
   * <code>{@link ITrace2D#PROPERTY_MAX_Y}</code>.
   * <code>{@link ITrace2D#PROPERTY_MIN_X}</code> and
   * <code>{@link ITrace2D#PROPERTY_MIN_Y}</code> to adapt to bound changes.
   * </p>
   * 
   * @param evt
   *          the property change event that was fired.
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(final PropertyChangeEvent evt) {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("propertyChange 0 locks");
    }
    synchronized (this) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("propertyChange 1 lock");
      }
      String property = evt.getPropertyName();
      double value;
      // first the bound changes:
      if (property.equals(ITrace2D.PROPERTY_MAX_X)) {
        this.m_updates++;
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("pc-Xmax");
        }
        value = ((Double) evt.getNewValue()).doubleValue();
        if (value > this.m_xmax) {
          this.m_xmax = value;
        } else if (value < this.m_xmax) {
          this.m_xmax = this.findMaxX();
        }
      } else if (property.equals(ITrace2D.PROPERTY_MIN_X)) {
        this.m_updates++;
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("pc-Xmin");
        }
        value = ((Double) evt.getNewValue()).doubleValue();
        if (value < this.m_xmin) {
          this.m_xmin = value;
        } else if (value > this.m_xmin) {
          this.m_xmin = this.findMinX();
        }
      } else if (property.equals(ITrace2D.PROPERTY_MAX_Y)) {
        this.m_updates++;
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("pc-Ymax");
        }
        value = ((Double) evt.getNewValue()).doubleValue();
        if (value > this.m_ymax) {
          this.m_ymax = value;
        } else if (value < this.m_ymax) {
          this.m_ymax = this.findMaxY();
        }
      } else if (property.equals(ITrace2D.PROPERTY_MIN_Y)) {
        this.m_updates++;
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("pc-Ymin");
        }
        value = ((Double) evt.getNewValue()).doubleValue();
        if (value < this.m_ymin) {
          this.m_ymin = value;
        } else if (value > this.m_ymin) {
          this.m_ymin = this.findMinY();
        }
      } else if (property.equals(AbstractRangePolicy.PROPERTY_RANGE)) {
        // TODO: Maybe be more precise for this property change: detect if this
        // range change has an effect on getMin/getMax of an axis, detect which
        // axis changed.
        this.m_updates++;
        this.scaleAll(X_Y);
      } else if (property.equals(AbstractRangePolicy.PROPERTY_RANGE_MAX)) {
        this.m_updates++;
        this.scaleAll(X_Y);
      } else if (property.equals(AbstractRangePolicy.PROPERTY_RANGE_MIN)) {
        this.m_updates++;
        this.scaleAll(X_Y);
      } else if (property.equals(ITrace2D.PROPERTY_TRACEPOINT)) {
        // now points added or removed -> rescale!
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("pc-tp");
        }
        TracePoint2D oldPt = (TracePoint2D) evt.getOldValue();
        TracePoint2D newPt = (TracePoint2D) evt.getNewValue();
        // added or removed?
        // we only care about added points (rescaling is our task)
        if (oldPt == null) {
          // this.m_axisX.initPaintIteration();
          // this.m_axisY.initPaintIteration();
          this.scalePoint(newPt);
        }
      }
    }
  }

  /**
   * <p>
   * Removes the given instance from this <code>Chart2D</code> if it is
   * contained.
   * </p>
   * <p>
   * This method will trigger a {@link java.beans.PropertyChangeEvent} being
   * fired on all instances registered by
   * {@link javax.swing.JComponent#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)}
   * (registered with <code>String</code> argument
   * {@link #PROPERTY_ADD_REMOVE_TRACE}).
   * </p>
   * 
   * @param points
   *          the trace to remove.
   * @see Chart2D#PROPERTY_ADD_REMOVE_TRACE
   */
  public final void removeTrace(final ITrace2D points) {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("removeTrace, 0 locks");
    }
    synchronized (this) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("removeTrace, 1 lock");
      }
      this.m_traces.remove(points);
      // all properties chart listens for
      points.removePropertyChangeListener(ITrace2D.PROPERTY_MAX_X, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_MIN_X, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_MAX_Y, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_MIN_Y, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_TRACEPOINT, this);

      // update bounds:
      this.m_xmax = this.findMaxX();
      this.m_xmin = this.findMinX();
      this.m_ymax = this.findMaxY();
      this.m_ymin = this.findMinY();

      this.scaleAll(Chart2D.X_Y);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          repaint();
        }
      });
    }
    this.firePropertyChange(Chart2D.PROPERTY_ADD_REMOVE_TRACE, points, null);
  }

  /**
   * <p>
   * Internally rescales all <code>{@link TracePoint2D}</code> instances by
   * using the bounds provided by the internal <code>{@link Axis}</code>.
   * </p>
   * 
   * @param axis
   *          one of the values <code>{@link #X}</code>,
   *          <code>{@link #Y}</code> or <code>{@link #X_Y}</code> to decide
   *          in which dimension scaling should be performed.
   * 
   */
  protected final void scaleAll(final int axis) {
    Iterator it = this.m_traces.iterator();
    while (it.hasNext()) {
      scaleTrace((ITrace2D) it.next(), axis);
    }
  }

  /**
   * <p>
   * Internally rescales the given <code>{@link TracePoint2D}</code> in both
   * dimensions by using the bounds provided by the internal
   * <code>{@link Axis}</code> instances.
   * </p>
   * 
   * @param point
   *          the point to scale (between 0.0 and 1.0) according to the internal
   *          bounds.
   */
  private final void scalePoint(final TracePoint2D point) {
    Range xrange = this.m_axisX.getRange();
    Range yrange = this.m_axisY.getRange();
    double scalerX = xrange.getExtent();
    double scalerY = yrange.getExtent();
    double tmpx, tmpy;

    tmpx = (point.getX() - xrange.getMin()) / scalerX;
    tmpy = (point.getY() - yrange.getMin()) / scalerY;
    if (tmpx == Double.NaN || Double.isInfinite(tmpx)) {
      tmpx = 0;
    }
    if (tmpy == Double.NaN || Double.isInfinite(tmpy)) {
      tmpy = tmpx;
    }
    point.m_scaledX = tmpx;
    point.m_scaledY = tmpy;
    if ((tmpx > 1.0) || (tmpx < 0.0) || (tmpy > 1.0) || (tmpy < 0.0)) {
      System.out.println("ScaledPoint to [" + tmpx + "," + tmpy + "]");
    }
  }

  /**
   * <p>
   * Internally rescales all <code>{@link TracePoint2D}</code> instances of
   * the trace by using the bounds provided by the internal
   * <code>{@link Axis}</code>.
   * </p>
   * 
   * @param axis
   *          one of the values <code>{@link #X}</code>,
   *          <code>{@link #Y}</code> or <code>{@link #X_Y}</code> to decide
   *          in which dimension scaling should be performed.
   * 
   * @param trace
   *          the trace to rescale.
   */
  private final void scaleTrace(final ITrace2D trace, final int axis) {
    TracePoint2D tmp;
    Range xrange = this.m_axisX.getRange();
    Range yrange = this.m_axisY.getRange();
    double scalerX = xrange.getExtent();
    double scalerY = yrange.getExtent();
    double tmpx, tmpy;
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("scaleTrace, 0 locks");
    }

    synchronized (this) {
      if (Chart2D.THREAD_DEBUG) {
        System.out.println("scaleTrace, 1 lock");
      }
      synchronized (trace) {
        if (Chart2D.THREAD_DEBUG) {
          System.out.println("scaleTrace, 2 locks");
        }

        Iterator it = trace.iterator();
        if (axis == Chart2D.X_Y) {
          while (it.hasNext()) {
            tmp = (TracePoint2D) it.next();
            tmpx = (tmp.getX() - xrange.getMin()) / scalerX;
            tmpy = (tmp.getY() - yrange.getMin()) / scalerY;
            if (tmpx == Double.NaN) {
              tmpx = 0;
            }
            if (tmpy == Double.NaN) {
              tmpy = 0;
            }
            tmp.m_scaledX = tmpx;
            tmp.m_scaledY = tmpy;
          }
        } else if (axis == Chart2D.X) {
          while (it.hasNext()) {
            tmp = (TracePoint2D) it.next();
            tmpx = (tmp.getX() - xrange.getMin()) / scalerX;
            if (tmpx == Double.NaN) {
              tmpx = 0;
            }
            tmp.m_scaledX = tmpx;
          }
        } else if (axis == Chart2D.Y) {
          while (it.hasNext()) {
            tmp = (TracePoint2D) it.next();
            tmpy = (tmp.getY() - yrange.getMin()) / scalerY;
            if (tmpy == Double.NaN) {
              tmpy = 0;
            }
            tmp.m_scaledY = tmpy;
          }
        }
      }
    }
  }

  /**
   * <p>
   * Set the x axis to use.
   * </p>
   * 
   * @param axisX
   *          The axisX to set.
   */
  public void setAxisX(final Axis axisX) {
    this.m_axisX = axisX;
    // constructor will register the accessor to the axis:
    axisX.new XDataAccessor(this);
  }

  /**
   * <p>
   * Set the y axis to use.
   * </p>
   * 
   * @param axisY
   *          The axisY to set.
   */
  public void setAxisY(final Axis axisY) {
    this.m_axisY = axisY;
    // constructor will register the accessor to the axis:
    axisY.new YDataAccessor(this);
  }

  /**
   * @see java.awt.Component#setBackground(java.awt.Color)
   */
  public void setBackground(final Color bg) {
    Color old = this.getBackground();
    super.setBackground(bg);
    this.firePropertyChange(Chart2D.PROPERTY_BACKGROUND_COLOR, old, bg);
  }

  /**
   * Set the amounts of fractions that will be displayed. This may additionally
   * affect the amount of labels that will be displayed. The policy of jchart2d
   * is only to show labels at the exact positions (no rounding) so a setting of
   * "0" only permits natural number labels. If the range of the values e.g. is
   * between 0.5 and 0.9 then no labels would be displayed at all. On the other
   * hand a high setting could also decrease the number of labels as the amount
   * of labels also depends on the fact that no label will overwrite the
   * following label (overdraw).
   * 
   * @deprecated Use {@link #getAxisX()} and
   *             {@link Axis#setFractionsDigits(int)} instead. This method may
   *             be removed in future versions due to a unprecise naming and the
   *             refactored Axis design.
   * 
   * @param decimals
   *          not used any more.
   * 
   */
  public final void setDecimalsX(final int decimals) {
    this.m_axisX.setFractionsDigits(decimals);
  }

  /**
   * 
   * @see #setDecimalsX(int)
   * 
   * @deprecated Use {@link #getAxisX()} and
   *             {@link Axis#setFractionsDigits(int)} instead. This method may
   *             be removed in future versions due to a unprecise naming and the
   *             refactored Axis design.
   */
  public final void setDecimalsY(final int decimals) {
    this.m_axisY.setFractionsDigits(decimals);
  }

  /**
   * @see java.awt.Component#setForeground(java.awt.Color)
   */
  public void setForeground(final Color fg) {
    Color old = this.getForeground();
    super.setForeground(fg);
    this.firePropertyChange(Chart2D.PROPERTY_FOREGROUND_COLOR, old, fg);
  }

  /**
   * <p>
   * Set the grid color to use.
   * </p>
   * 
   * @param gridclr
   *          the grid color to use.
   */
  public final void setGridColor(final Color gridclr) {
    if (gridclr != null) {
      Color old = this.m_gridcolor;
      this.m_gridcolor = gridclr;
      if (old.equals(this.m_gridcolor)) {
        this.firePropertyChange(Chart2D.PROPERTY_GRID_COLOR, old, this.m_gridcolor);
      }
    }
  }

  /**
   * <p>
   * Set wether the x grid should be painted or not.
   * </p>
   * 
   * @param gridx
   *          true if the x grid should be painted or false if not.
   */
  public final void setGridX(final boolean gridx) {
    this.m_gridX = gridx;
    if (gridx) {
      this.setScaleX(true);
    }
  }

  /**
   * <p>
   * Set wether the y grid should be painted or not.
   * </p>
   * 
   * @param gridy
   *          true if the y grid should be painted or false if not.
   */
  public final void setGridY(final boolean gridy) {
    this.m_gridY = gridy;
    if (gridy) {
      this.setScaleY(true);
    }
  }

  /**
   * Sets the label painter.
   * <p>
   * 
   * @param labelPainter
   *          The labelPainter to set.
   */
  public synchronized void setLabelPainter(final ILabelPainter labelPainter) {
    this.m_labelPainter = labelPainter;
  }

  /**
   * 
   * <p>
   * Decide wether labels for each chart are painted below it. If set to true
   * this will be done, else labels will be ommited.
   * </p>
   * 
   * 
   * @param paintLabels
   *          the value for paintLabels to set.
   */
  public void setPaintLabels(final boolean paintLabels) {
    this.m_paintLabels = paintLabels;
  }

  /**
   * Set if the scale on the x axis should be shown.
   * <p>
   * 
   * @param show
   *          true if the scale on the x axis should be shown, false else.
   */
  public final void setScaleX(final boolean show) {
    this.m_scaleX = show;
  }

  /**
   * Set if the scale on the y axis should be shown.
   * <p>
   * 
   * @param show
   *          true if the scale on the y axis should be shown, false else.
   */
  public final void setScaleY(final boolean show) {
    this.m_scaleY = show;
  }

  /**
   * Returns a BufferedImage of the Chart2D's graphics that may be written to a
   * file or OutputStream by using:
   * 
   * {@link javax.imageio.ImageIO#write(java.awt.image.RenderedImage, java.lang.String, java.io.File)}.
   * <p>
   * 
   * @return a BufferedImage of the Chart2D's graphics that may be written to a
   *         file or OutputStream.
   * 
   * @since 1.03 - please download versions equal or greater than
   *        jchart2d-1.03.jar.
   */
  public BufferedImage snapShot() {
    BufferedImage img;
    img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics g = img.getGraphics();
    // paint is synchronized:
    this.paint(g);
    return img;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    if (Chart2D.THREAD_DEBUG) {
      System.out.println("toString(), " + Thread.currentThread().getName() + ", 0 locks");
    }
    synchronized (this) {
      return super.toString();
    }
  }

  /**
   * <p>
   * Compares wether the bounds since last invocation have changed and
   * conditionally rescales the internal <code>{@link TracePoint2D}</code>
   * instances.
   * </p>
   * <p>
   * Must only be called from <code>{@link #paint(Graphics)}</code>.
   * </p>
   * <p>
   * The old values for the bounds are set to the actual values afterwards to
   * allow detection of future changes again.
   * </p>
   */
  private void updateScaling() {

    boolean xChanged = (this.m_xmax != this.m_xmaxold) || (this.m_xmin != this.m_xminold);
    boolean yChanged = (this.m_ymax != this.m_ymaxold) || (this.m_ymin != this.m_yminold);
    if (xChanged && yChanged) {
      this.scaleAll(Chart2D.X_Y);
    } else if (xChanged) {
      scaleAll(Chart2D.X);
    } else if (yChanged) {
      scaleAll(Chart2D.Y);
    }
    // reset equality
    this.m_xmaxold = this.m_xmax;
    this.m_ymaxold = this.m_ymax;
    this.m_xminold = this.m_xmin;
    this.m_yminold = this.m_ymin;

  }
}
