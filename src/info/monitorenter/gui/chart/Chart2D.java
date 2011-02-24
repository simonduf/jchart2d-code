/*
 *  Chart2D, a component for displaying ITrace2D instances.
 *  Copyright (C) 2007  Achim Westermann, Achim.Westermann@gmx.de
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
package info.monitorenter.gui.chart;

import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.axis.AxisLinear;
import info.monitorenter.gui.chart.axistickpainters.AxisTickPainterDefault;
import info.monitorenter.util.Range;
import info.monitorenter.util.collections.TreeSetGreedy;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.Timer;

/**
 * <code> Chart2D</code> is a component for displaying the data contained in a
 * <code>ITrace2D</code>. It inherits many features from
 * <code>javax.swing.JPanel</code> and allows specific configuration. <br>
 * In order to simplify the use of it, the scaling, labeling and choosing of
 * display- range is done automatically which flattens the free configuration.
 * <p>
 * There are several default settings that may be changed in
 * <code>Chart2D</code><br>
 * <ul>
 * <li>The display range is chosen always big enough to show every
 * <code>TracePoint2D</code> contained in the all <code>ITrace2d</code>
 * instances connected. This is because the
 * {@link info.monitorenter.gui.chart.IAxis} of the chart (for x and y) use by
 * default a
 * {@link info.monitorenter.gui.chart.rangepolicies.RangePolicyUnbounded}. To
 * change this, get the axis of the chart to change (via {@link #getAxisX()},
 * {@link #getAxisY()}) and invoke
 * {@link info.monitorenter.gui.chart.IAxis#setRangePolicy(IRangePolicy)} with
 * the desired viewport behavior.
 * <li>During the <code>paint()</code> operation every
 * <code>TracePoint2D</code> is taken from the <code>ITrace2D</code>-
 * instance exactly in the order, it's iterator returns them. From every
 * <code>TracePoint2D</code> then a line is drawn to the next. <br>
 * Unordered traces may cause a weird display. Choose the right implementation
 * of <code>ITrace2D</code> to avoid this. To change this line painting
 * behaviour you can use custom renderers at the level of traces via
 * {@link info.monitorenter.gui.chart.ITrace2D#addTracePainter(ITracePainter)}
 * or
 * {@link info.monitorenter.gui.chart.ITrace2D#setTracePainter(ITracePainter)}.
 * <li>If no scaling is choosen, no grids will be painted. See:
 * <code>{@link IAxis#setPaintScale(boolean)}</code> This allows saving of
 * many computations.
 * <li>The distance of the scalepoints is always big enough to display the
 * labels fully without overwriting each ohter.</li>
 * </ul>
 * <p>
 * <h3>Demo- code:</h3>
 * 
 * <pre>
 *       
 *   ...
 *   Chart2D test = new Chart2D();
 *   JFrame frame = new JFrame(&quot;Chart2D- Debug&quot;);
 *            
 *   frame.setSize(400,200);
 *   frame.setVisible(true);
 *   ITrace2D atrace = new Trace2DLtd(100);
 *   ...
 *   &lt;further configuration of trace&gt;
 *   ...
 *   test.addTrace(atrace);
 *   ....
 *   while(expression){
 *     atrace.addPoint(adouble,bdouble);
 *     ....
 *   }
 * </pre>
 * 
 * <p>
 * <h3>PropertyChangeEvents</h3>
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
 * <table width="100%">
 * <tr>
 * <th ><code>getPropertyName()</code></th>
 * <th><code>getSource()</code></th>
 * <th><code>getOldValue()</code></th>
 * <th><code>getNewValue()</code></th>
 * <th>occurence</th>
 * </tr>
 * <tr>
 * <td>{@link #PROPERTY_ADD_REMOVE_TRACE}</td>
 * <td>{@link Chart2D}</td>
 * <td>null</td>
 * <td>{@link info.monitorenter.gui.chart.ITrace2D}</td>
 * <td>if a new instance is added.</td>
 * </tr>
 * <tr>
 * <td>{@link #PROPERTY_ADD_REMOVE_TRACE}</td>
 * <td>{@link Chart2D}</td>
 * <td>{@link info.monitorenter.gui.chart.ITrace2D}</td>
 * <td>null</td>
 * <td>if an instance is deleted.</td>
 * </tr>
 * <tr>
 * <td>{@link #PROPERTY_BACKGROUND_COLOR}</td>
 * <td>{@link Chart2D}</td>
 * <td>{@link java.awt.Color}</td>
 * <td>{@link java.awt.Color}</td>
 * <td>if a change of the background color occurs.</td>
 * </tr>
 * <tr>
 * <td>{@link #PROPERTY_AXIS_X}</td>
 * <td>{@link Chart2D}</td>
 * <td>{@link info.monitorenter.gui.chart.IAxis}</td>
 * <td>{@link info.monitorenter.gui.chart.IAxis}</td>
 * <td>if a new axis is set in x dimension.</td>
 * </tr>
 * <tr>
 * <td>{@link #PROPERTY_AXIS_Y}</td>
 * <td>{@link Chart2D}</td>
 * <td>{@link info.monitorenter.gui.chart.IAxis}</td>
 * <td>{@link info.monitorenter.gui.chart.IAxis}</td>
 * <td>if a new axis is set in y dimension.</td>
 * </tr>
 * <tr>
 * <td>{@link #PROPERTY_GRID_COLOR}</td>
 * <td>{@link Chart2D}</td>
 * <td>{@link java.awt.Color}</td>
 * <td>{@link java.awt.Color}</td>
 * <td>if a change of the grid color occurs.</td>
 * </tr>
 * <tr>
 * <td>{@link #PROPERTY_PAINTLABELS}</td>
 * <td>{@link Chart2D}</td>
 * <td>{@link java.lang.Boolean}</td>
 * <td>{@link java.lang.Boolean}</td>
 * <td>if a change of the paint labels flag occurs.</td>
 * </tr>
 * </table>
 * <p>
 * 
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 * 
 * @version $Revision: 1.79 $
 */

public class Chart2D
    extends JPanel implements PropertyChangeListener {

  /** Speaking names for axis constants - used for debugging only. */
  public static final String[] AXIX_CONSTANT_NAMES = new String[] {"dummy", "X", "Y", "X,Y" };

  /**
   * A package wide switch for debugging problems with scaling. Set to false the
   * compiler will remove the debugging statements.
   */
  protected static final boolean DEBUG_SCALING = false;

  /**
   * A package wide switch for debugging problems with multithreading. Set to
   * false the compiler will remove the debugging statements.
   */
  public static final boolean DEBUG_THREADING = false;

  /**
   * The bean property <code>constant</code> identifying a change of the
   * internal set of <code>{@link ITrace2D}</code> instances.
   * <p>
   * 
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * <p>
   * See the class description for property change events fired.
   */
  public static final String PROPERTY_ADD_REMOVE_TRACE = "Chart2D.PROPERTY_ADD_REMOVE_TRACE";

  /**
   * The bean property <code>constant</code> identifying a change of the
   * internal <code>{@link IAxis}</code> instance for the x dimension.
   * <p>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * <p>
   * See the class description for property change events fired.
   * <p>
   */
  public static final String PROPERTY_AXIS_X = "Chart2D.PROPERTY_AXIS_X";

  /**
   * The bean property <code>constant</code> identifying a change of the
   * internal <code>{@link IAxis}</code> instance for the y dimension.
   * <p>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * <p>
   * See the class description for property change events fired.
   * <p>
   */
  public static final String PROPERTY_AXIS_Y = "Chart2D.PROPERTY_AXIS_Y";

  /**
   * The bean property <code>constant</code> identifying a change of the
   * background color. <br>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * <p>
   * The property change events for this change are constructed and fired by the
   * superclass {@link java.awt.Container} so this constant is just for
   * clarification of the String that is related to that property.
   * <p>
   */
  public static final String PROPERTY_BACKGROUND_COLOR = "Chart2D.PROPERTY_BACKGROUND_COLOR";

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
  public static final String PROPERTY_FONT = "Chart2D.PROPERTY_FONT";

  /**
   * The bean property <code>constant</code> identifying a change of the
   * foreground color. <br>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * <p>
   * The property change events for this change are constructed and fired by the
   * superclass {@link java.awt.Container} so this constant is just for
   * clarification of the String that is related to that property.
   * <p>
   */
  public static final String PROPERTY_FOREGROUND_COLOR = "Chart2D.PROPERTY_FOREGROUND_COLOR";

  /**
   * The bean property <code>constant</code> identifying a change of the grid
   * color.
   * <p>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * <p>
   */
  public static final String PROPERTY_GRID_COLOR = "Chart2D.PROPERTY_GRID_COLOR";

  /**
   * The bean property <code>constant</code> identifying a change of the paint
   * labels flag.
   * <p>
   * Use this constant to register a {@link java.beans.PropertyChangeListener}
   * with the <code>Chart2D</code>.
   * <p>
   */
  public static final String PROPERTY_PAINTLABELS = "Chart2D.PROPERTY_PAINTLABELS";

  /** Generated <code>serial version UID</code>. */
  private static final long serialVersionUID = 3978425840633852978L;

  /** Constant describing the x axis (needed for scaling). */
  public static final int X = 1;

  /** Constant describing the x and y axis (needed for scaling). */
  public static final int X_Y = 3;

  /** Constant describing the y axis (needed for scaling). */
  public static final int Y = 2;

  /** The internal label painter for this chart. */
  private IAxisTickPainter m_axisTickPainter;

  /** The x axis instance. */
  private AAxis m_axisX;

  /** The y axis instance. */
  private AAxis m_axisY;

  /** The grid color. */
  private Color m_gridcolor = Color.lightGray;

  /**
   * Chart - wide setting for the ms to give a repaint operation time for
   * collecting several repaint requests into one (performance vs. update
   * speed).
   * <p>
   */
  private int m_minPaintLatency = 50;

  /**
   * Flag that decides whether labels for traces are painted below the chart.
   */
  private boolean m_paintLabels = true;

  /**
   * Internal timer for repaint control with guarantee that the interval between
   * two frames will not be lower than
   * <code>{@link Chart2D#m_minPaintLatency}</code> ms.
   * <p>
   */
  private Timer m_repainter;

  /**
   * Internal flag that stores a request for a repaint that guarantees that two
   * invocations of <code></code> will always have at least have an interval of
   * <code>{@link Chart2D#m_minPaintLatency}</code> ms.
   * <p>
   * Access to it has to be synchronized!
   */
  private boolean m_requestedRepaint;

  /**
   * Flag to remember whether this chart has synchronized it's x start
   * coordinates with another chart.
   */
  private boolean m_synchronizedXStart = false;

  /** A chart this chart will synchronize it's start coordinates in x dimension. */
  private Chart2D m_synchronizedXStartChart;

  /** Flag for showing coordinates as tool tips. */
  private boolean m_toolTipCoords = true;

  /**
   * The internal <code>TreeSetGreedy</code> use to store the different
   * <code>ITrace2d</code> instanes to paint.
   */
  private TreeSetGreedy m_traces = new TreeSetGreedy();

  /**
   * The end x pixel coordinate of the chart.
   */
  private int m_xChartEnd;

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
   * The x range used for scaling in the previous paint operation.
   * <p>
   * This is used for detection of dirty scaling.
   * <p>
   */
  private Range m_xRangePreviousScaling = new Range(0, 0);

  /**
   * The y coordinate of the upper edge of the chart's display area in px.
   * <p>
   * 
   * The px coordinates in awt / swing start from top and increase towards the
   * bottom.
   * <p>
   * 
   */
  private int m_yChartEnd;

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
   * The y range used for scaling in the previous paint operation.
   * <p>
   * This is used for detection of dirty scaling.
   * <p>
   */
  private Range m_yRangePreviousScaling = new Range(0, 0);

  /**
   * Creates a new chart.
   * <p>
   */
  public Chart2D() {
    AAxis axisX = new AxisLinear();
    // add me as listener, later on this state will be copied in setAxis..
    axisX.addPropertyChangeListener(IAxis.PROPERTY_LABELFORMATTER, this);
    axisX.addPropertyChangeListener(IAxis.PROPERTY_PAINTGRID, this);
    axisX.addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY, this);
    axisX.addPropertyChangeListener(IAxis.PROPERTY_TITLE, this);
    axisX.addPropertyChangeListener(IAxis.PROPERTY_TITLEFONT, this);
    axisX.addPropertyChangeListener(IAxis.PROPERTY_TITLEPAINTER, this);
    this.setAxisX(axisX);

    AAxis axisY = new AxisLinear();
    // add me as listener, later on this state will be copied in setAxis..
    axisY.addPropertyChangeListener(IAxis.PROPERTY_LABELFORMATTER, this);
    axisY.addPropertyChangeListener(IAxis.PROPERTY_PAINTGRID, this);
    axisY.addPropertyChangeListener(IAxis.PROPERTY_RANGEPOLICY, this);
    axisY.addPropertyChangeListener(IAxis.PROPERTY_TITLE, this);
    axisY.addPropertyChangeListener(IAxis.PROPERTY_TITLEFONT, this);
    axisY.addPropertyChangeListener(IAxis.PROPERTY_TITLEPAINTER, this);
    this.setAxisY(axisY);

    this.setAxisTickPainter(new AxisTickPainterDefault());

    Font dflt = this.getFont();
    if (dflt != null) {
      this.setFont(new Font(dflt.getFontName(), dflt.getStyle(), 10));
    }
    this.getBackground();
    this.setBackground(Color.white);
    // one initial call to paint for side effect computations
    // potentially needed from outside (m_XstartChart...):
    this.setRequestedRepaint(true);

    // set a custom cursor:
    this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

    // turn on tooltip coordinates at mouse cursor:
    this.setToolTipCoords(true);
    this.m_repainter = new Timer(this.m_minPaintLatency, new ActionListener() {

      /**
       * Repaints the Chart if dirty.
       * <p>
       * 
       * @param e
       *            invoked by the timer to trigger the action.
       */
      public void actionPerformed(final ActionEvent e) {
        synchronized (Chart2D.this) {
          if (Chart2D.this.isRequestedRepaint()) {
            // Only here this deprecated call may be done:
            Chart2D.this.repaint(Chart2D.this.m_minPaintLatency);
            Chart2D.this.setRequestedRepaint(false);
          }
        }
      }

    });
    Timer.setLogTimers(false);
    this.m_repainter.setRepeats(true);
    this.m_repainter.setCoalesce(true);
    this.m_repainter.start();
  }

  /**
   * Adds the trace to this chart. It will be painted (if it's
   * {@link ITrace2D#isVisible()} returns true) in this chart.
   * <p>
   * This method will trigger a {@link java.beans.PropertyChangeEvent} being
   * fired on all instances registered by
   * {@link javax.swing.JComponent#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)}
   * (registered with <code>String</code> argument
   * {@link #PROPERTY_ADD_REMOVE_TRACE}).
   * <p>
   * 
   * @param points
   *            the trace to add.
   * 
   * @see Chart2D#PROPERTY_ADD_REMOVE_TRACE
   */
  public final void addTrace(final ITrace2D points) {
    if (Chart2D.DEBUG_THREADING) {
      System.out.println("chart.addTrace (" + Thread.currentThread().getName() + "), 0 locks");
    }
    synchronized (this) {
      if (Chart2D.DEBUG_THREADING) {
        System.out.println("chart.addTrace(" + Thread.currentThread().getName() + "), 1 lock");
      }
      synchronized (points) {
        if (Chart2D.DEBUG_THREADING) {
          System.out.println("chart.addTrace(" + Thread.currentThread().getName() + "), 2 locks");
        }
        this.m_traces.add(points);
        // listen to bound changes and more
        points.addPropertyChangeListener(ITrace2D.PROPERTY_MAX_X, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_MIN_X, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_MAX_Y, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_MIN_Y, this);
        // These are repaint candidates:
        points.addPropertyChangeListener(ITrace2D.PROPERTY_COLOR, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_STROKE, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_VISIBLE, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_ZINDEX, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_PAINTERS, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_ERRORBARPOLICY, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_ERRORBARPOLICY_CONFIGURATION, this);
        points.addPropertyChangeListener(ITrace2D.PROPERTY_NAME, this);
        // listen to newly added points
        // this is needed for scaling at point level.
        // else every bound change would force to rescale all traces!
        points.addPropertyChangeListener(ITrace2D.PROPERTY_TRACEPOINT, this);
        // listen to changed points whose location was changed:
        points.addPropertyChangeListener(ITrace2D.PROPERTY_POINT_CHANGED, this);

        // for static traces (all points added) we won't get events.
        // so update here:
        double maxX = points.getMaxX();
        if (maxX > this.m_xmax) {
          this.m_xmax = maxX;
        }
        double maxY = points.getMaxY();
        if (maxY > this.m_ymax) {
          this.m_ymax = maxY;
        }
        double minX = points.getMinX();
        if (minX < this.m_xmin) {
          this.m_xmin = minX;
        }
        double minY = points.getMinY();
        if (minY < this.m_ymin) {
          this.m_ymin = minY;
        }
        // special case: first trace added:
        if (this.m_traces.size() == 1) {
          this.m_ymin = minY;
          this.m_ymax = maxY;
          this.m_xmin = minX;
          this.m_xmax = maxX;
        }

        points.setRenderer(this);
        // unconditionally scale the trace as we don't know which
        // bounds it was related to before.
        this.m_axisX.scaleTrace(points);
        this.m_axisY.scaleTrace(points);

      }
      if (Chart2D.DEBUG_THREADING) {
        System.out.println("chart.addTrace(" + Thread.currentThread().getName()
            + "), left 1 lock: 1 remaining");
      }
    }
    if (Chart2D.DEBUG_THREADING) {
      System.out.println("chart.addTrace(" + Thread.currentThread().getName()
          + "), left 1 lock:  0 remaining");
    }
    // A deadlock occurs if a listener triggers paint.
    // This was the case with ChartPanel.
    this.firePropertyChange(Chart2D.PROPERTY_ADD_REMOVE_TRACE, null, points);
  }

  /**
   * Calculates the end x coordinate (right bound) in pixel of the chart to
   * draw.
   * <p>
   * This value depends on the current <code>{@link FontMetrics}</code> used
   * to paint the x labels and the maximum amount of characters that are used
   * for the x labels (<code>{@link IAxisLabelFormatter#getMaxAmountChars()}</code>)
   * because an x label may occur on the right edge of the chart and should not
   * be clipped.
   * <p>
   * 
   * @param g2d
   *            needed for size informations.
   * 
   * @return the end x coordinate (right bound) in pixel of the chart to draw.
   */
  private int calculateXChartEnd(final Graphics2D g2d) {
    return (int) getSize().getWidth() - this.getAxisX().getWidth(g2d);
  }

  /**
   * Calculates the start x coordinate (left bound) in pixel of the chart to
   * draw.
   * <p>
   * This value depends on the current <code>{@link FontMetrics}</code> used
   * to paint the y labels and the maximum amount of characters that are used
   * for the y labels (<code>{@link IAxisLabelFormatter#getMaxAmountChars()}</code>).
   * <p>
   * 
   * @param g2d
   *            needed for size information.
   * 
   * @return the start x coordinate (left bound) in pixel of the chart to draw.
   */
  private int calculateXChartStart(final Graphics2D g2d) {
    return this.getAxisY().getWidth(g2d);
  }

  /**
   * @see javax.swing.JComponent#createToolTip()
   */
  public JToolTip createToolTip() {
    /*
     * If desired return here a HTMLToolTip that transforms the text given to
     * setTipText into a View (with BasicHTML) and sets it as the
     * putClientProperty BasicHtml.html of itself.
     */
    JToolTip result = super.createToolTip();
    return result;
  }

  /**
   * Destroys the chart.
   * <p>
   * This method is only of interest if you have an application that dynamically
   * adds and removes charts. So if you use the same Chart2D object(s) during
   * the applications lifetime there is no need to use this method.
   * <p>
   */
  public void destroy() {
    if (Chart2D.DEBUG_THREADING) {
      System.out.println("destroy, 0 locks");
    }
    synchronized (this) {
      if (Chart2D.DEBUG_THREADING) {
        System.out.println("destroy, 1 lock");
      }
      this.m_axisX = null;
      this.m_axisY = null;
      this.m_traces = null;
    }
  }

  /**
   * Cleanup when this instance is dropped.
   * <p>
   * The internal painter thread is stoppped.
   * <p>
   * 
   * @see java.lang.Object#finalize()
   * @throws Throwable
   *             if a finalizer of a superclass fails.
   */
  protected void finalize() throws Throwable {
    super.finalize();
    this.destroy();
  }

  /**
   * Searches for the maximum x value of all contained ITraces.
   * <p>
   * This method is triggered when a trace fired a property change for property
   * <code>{@link ITrace2D#PROPERTY_MAX_X}</code> with a value lower than the
   * internal stored maximum x.
   * <p>
   * Performance breakdown is avoided because all <code>ITrace2D</code>
   * implementations cache their max and min values.
   * <p>
   * Note that the <code>Chart2D</code> itself does not use this value for
   * painting. It uses {@link AAxis#getRange()} which itself accesses this value
   * by accessors and additionally filters the value by it's assigned internal
   * {@link info.monitorenter.gui.chart.rangepolicies.ARangePolicy}.
   * <p>
   * 
   * @return the maximum x value of all traces.
   */
  protected final double findMaxX() {
    double max = -Double.MAX_VALUE;
    double tmp;
    Iterator it = this.m_traces.iterator();
    ITrace2D trace;
    while (it.hasNext()) {
      trace = (ITrace2D) it.next();
      if (trace.isVisible()) {
        if (trace.getSize() > 0) {
          tmp = trace.getMaxX();
          if (tmp > max) {
            max = tmp;
          }
        }
      }
    }
    if (max == -Double.MAX_VALUE) {
      max = 10;
    }
    return max;
  }

  /**
   * Searches for the maximum y value of all contained ITraces.
   * <p>
   * This method is triggered when a trace fired a property change for property
   * <code>{@link ITrace2D#PROPERTY_MAX_Y}</code> with a value lower than the
   * internal stored maximum x.
   * <p>
   * Note that the <code>Chart2D</code> itself does not use this value for
   * painting. It uses {@link AAxis#getRange()} which itself accesses this value
   * by accessors and additionally filters the value by it's assigned internal
   * {@link info.monitorenter.gui.chart.rangepolicies.ARangePolicy}.
   * <p>
   * 
   * @return the maximum y value of all traces.
   */
  protected final double findMaxY() {
    double max = -Double.MAX_VALUE;
    double tmp;
    Iterator it = this.m_traces.iterator();
    ITrace2D trace;
    while (it.hasNext()) {
      trace = (ITrace2D) it.next();
      if (trace.isVisible()) {
        if (trace.getSize() > 0) {
          tmp = trace.getMaxY();
          if (tmp > max) {
            max = tmp;
          }
        }
      }
    }
    if (max == -Double.MAX_VALUE) {
      max = 10;
    }
    return max;
  }

  /**
   * Searches for the minimum x value of all contained ITraces.
   * <p>
   * This method is triggered when a trace fired a property change for property
   * <code>{@link ITrace2D#PROPERTY_MIN_Y}</code> with a value lower than the
   * internal stored maximum x.
   * <p>
   * Note that the <code>Chart2D</code> itself does not use this value for
   * painting. It uses {@link AAxis#getRange()} which itself accesses this value
   * by accessors and additionally filters the value by it's assigned internal
   * {@link info.monitorenter.gui.chart.rangepolicies.ARangePolicy}.
   * <p>
   * 
   * @return the minimum x value of all traces.
   */

  protected final double findMinX() {
    double min = Double.MAX_VALUE;
    double tmp;
    Iterator it = this.m_traces.iterator();
    ITrace2D trace;
    while (it.hasNext()) {
      trace = (ITrace2D) it.next();
      if (trace.isVisible()) {
        if (trace.getSize() > 0) {
          tmp = trace.getMinX();
          if (tmp < min) {
            min = tmp;
          }
        }
      }
    }
    if (min == Double.MAX_VALUE) {
      min = 0;
    }
    return min;
  }

  /**
   * Searches for the minimum y value of all contained ITraces.
   * <p>
   * This method is triggered when a trace fired a property change for property
   * <code>{@link ITrace2D#PROPERTY_MIN_Y}</code> with a value lower than the
   * internal stored maximum x.
   * <p>
   * Note that the <code>Chart2D</code> itself does not use this value for
   * painting. It uses {@link AAxis#getRange()} which itself accesses this value
   * by accessors and additionally filters the value by it's assigned internal
   * {@link info.monitorenter.gui.chart.rangepolicies.ARangePolicy}.
   * <p>
   * 
   * @return the minimum y value of all traces.
   */

  protected final double findMinY() {
    double min = Double.MAX_VALUE;
    double tmp;
    Iterator it = this.m_traces.iterator();
    ITrace2D trace;
    while (it.hasNext()) {
      trace = (ITrace2D) it.next();
      if (trace.isVisible()) {
        if (trace.getSize() > 0) {
          tmp = trace.getMinY();
          if (tmp < min) {
            min = tmp;
          }
        }
      }
    }
    if (min == Double.MAX_VALUE) {
      min = 0;
    }
    return min;
  }

  /**
   * Returns the painter for the ticks of the axis.
   * <p>
   * 
   * @return Returns the painter for the ticks of the axis.
   */
  public IAxisTickPainter getAxisTickPainter() {
    return this.m_axisTickPainter;
  }

  /**
   * Returns the axis for the x dimension.
   * <p>
   * 
   * @return the axis for the x dimension.
   */
  public final IAxis getAxisX() {
    return this.m_axisX;
  }

  /**
   * Returns the axis for the y dimension.
   * <p>
   * 
   * @return the axis for the y dimension.
   */
  public final IAxis getAxisY() {
    return this.m_axisY;
  }

  /**
   * Returns the color of the grid.
   * <p>
   * 
   * @return the color of the grid.
   */
  public final Color getGridColor() {
    return this.m_gridcolor;
  }

  /**
   * Returns the maximum x-value of all contained <code>{@link ITrace2D}</code>
   * instances.
   * <p>
   * 
   * @return the maximum x-value of all contained <code>{@link ITrace2D}</code>
   *         instances.
   */
  public final double getMaxX() {
    return this.m_xmax;
  }

  /**
   * Returns the maximum y-value of all contained <code>{@link ITrace2D}</code>
   * instances.
   * <p>
   * 
   * @return the maximum y-value of all contained <code>{@link ITrace2D}</code>
   *         instances.
   */
  public final double getMaxY() {
    return this.m_ymax;
  }

  /**
   * Returns the chart - wide setting for the ms to give a repaint operation
   * time for collecting several repaint requests into one (performance vs.
   * update speed).
   * <p>
   * 
   * @return the setting for the ms to give a repaint operation time for
   *         collecting several repaint requests into one (performance vs.
   *         update speed).
   */
  public synchronized int getMinPaintLatency() {
    return this.m_minPaintLatency;
  }

  /**
   * Returns the minimum x-value of all contained <code>{@link ITrace2D}</code>
   * instances.
   * <p>
   * 
   * @return the minimum x-value of all contained <code>{@link ITrace2D}</code>
   *         instances.
   * @see #getOffsetX()
   */
  public final double getMinX() {
    return this.m_xmin;
  }

  /**
   * Returns the minimum y-value of all contained <code>{@link ITrace2D}</code>
   * instances.
   * <p>
   * 
   * @return the minimum y-value of all contained <code>{@link ITrace2D}</code>
   *         instances.
   */
  public final double getMinY() {
    return this.m_ymin;
  }

  /**
   * Returns the minimum x-value displayed by this chart.
   * <p>
   * This value is not neccessarily the minimum x-value of all internal
   * <code>{@link ITrace2D}</code> instances. Rounding or forced bounds on the
   * internal <code>X - {@link AAxis}</code> may have an effect on this value.
   * <p>
   * 
   * @return the minimum x-value displayed by this chart.
   * @see #getMinX()
   */
  public final double getOffsetX() {
    return this.m_axisX.getRange().getMin();
  }

  /**
   * Returns the chart that will be synchronized for finding the start
   * coordinate of this chart to draw in x dimension (<code>{@link #getXChartStart()}</code>).
   * <p>
   * 
   * This feature is used to allow two separate charts to be painted stacked in
   * y dimension (one below the other) that have different x start coordinates
   * (because of different y labels that shift that value) with an equal
   * starting x value (thus be comparable visually if their x values match).
   * <p>
   * 
   * @return the chart that will be synchronized for finding the start
   *         coordinate of this chart to draw in x dimension (<code>{@link #getXChartStart()}</code>).
   */
  public Chart2D getSynchronizedXStartChart() {
    return this.m_synchronizedXStartChart;
  }

  /**
   * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
   */
  public String getToolTipText(final MouseEvent event) {
    if (this.m_toolTipCoords) {
      TracePoint2D tracePoint = this.translateMousePosition(event);
      StringBuffer result = new StringBuffer("X: ");
      result.append(this.getAxisX().getFormatter().format(tracePoint.getX())).append(" ");
      result.append("Y: ");
      result.append(this.getAxisY().getFormatter().format(tracePoint.getY()));
      return result.toString();
    } else {
      return super.getToolTipText(event);
    }
  }

  /**
   * Returns the internal set of traces that are currently rendered by this
   * instance.
   * <p>
   * Caution: the original internal set is returned. Modifications on the
   * returned set could cause problems.
   * <p>
   * 
   * @return the internal set of traces that are currently rendered by this
   *         instance.
   */
  public final Set getTraces() {
    return this.m_traces;
  }

  /**
   * Returns the x coordinate of the chart's right edge in px.
   * <p>
   * 
   * @return the x coordinate of the chart's right edge in px.
   */
  public final synchronized int getXChartEnd() {
    return this.m_xChartEnd;
  }

  /**
   * Returns the x coordinate of the chart's left edge in px.
   * <p>
   * 
   * @return Returns the x coordinate of the chart's left edge in px.
   */
  public synchronized int getXChartStart() {
    return this.m_xChartStart;
  }

  /**
   * Returns the y coordinate of the upper edge of the chart's display area in
   * px.
   * <p>
   * Pixel coordinates in awt / swing start from top and increase towards the
   * bottom.
   * <p>
   * 
   * @return The y coordinate of the upper edge of the chart's display area in
   *         px.
   */
  public final synchronized int getYChartEnd() {
    return this.m_yChartEnd;
  }

  /**
   * Returns the y coordinate of the chart's lower edge in px.
   * <p>
   * 
   * Pixel coordinates in awt / swing start from top and increase towards the
   * bottom.
   * <p>
   * 
   * @return Returns the y coordinate of the chart's lower edge in px.
   */
  public synchronized int getYChartStart() {
    return this.m_yChartStart;
  }

  /**
   * Interpolates (linear) the two neighbouring points.
   * <p>
   * Calling this method only makes sense if argument visible is a visible point
   * and argument invisible is an invisible point.
   * <p>
   * Visibility is determined only by their internally normalized coordinates
   * that are withen [0.0,1.0] for visible points.
   * <p>
   * 
   * @param visible
   *            the visible point.
   * @param invisible
   *            the invisible point.
   * @return the interpolation towards the exceeded bound.
   */
  private TracePoint2D interpolateVisible(final TracePoint2D invisible, final TracePoint2D visible) {

    // in the first call invisible is null because it is the previous point
    // (there
    // was no previous point: just return the new point:
    if (invisible == null) {
      return visible;
    }

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
      xInterpolate = (1.0 - invisible.m_scaledY) * (visible.m_scaledX - invisible.m_scaledX)
          / (visible.m_scaledY - invisible.m_scaledY) + invisible.m_scaledX;
    } else {
      // lower y bound
      yInterpolate = 0.0;
      xInterpolate = -invisible.m_scaledY * (visible.m_scaledX - invisible.m_scaledX)
          / (visible.m_scaledY - invisible.m_scaledY) + invisible.m_scaledX;
    }
    // TODO: do we have to compute and set the unscaled real values too?
    TracePoint2D result = new TracePoint2D(0.0, 0.0);
    result.m_scaledOnce = true;
    result.m_scaledX = xInterpolate;
    result.m_scaledY = yInterpolate;
    return result;
  }

  /**
   * Returns true if the bounds of all {@link TracePoint2D} instances of all
   * internal {@link ITrace2D} instances have changed since all points have been
   * normalized to a value between 0 and 1.
   * <p>
   * 
   * @return true if the bounds of all {@link TracePoint2D} instances of all
   *         internal {@link ITrace2D} instances have changed since all points
   *         have been normalized to a value between 0 and 1.
   */
  protected final boolean isDirtyScaling() {
    return isDirtyScaling(Chart2D.X) || isDirtyScaling(Chart2D.Y);
  }

  /**
   * Returns true if the bounds in the given dimension of all
   * {@link TracePoint2D} instances of all internal {@link ITrace2D} instances
   * have changed since all points have been normalized to a value between 0 and
   * 1.
   * <p>
   * 
   * @param axis
   *            one of {@link Chart2D#X} or {@link Chart2D#Y}.
   * @return true if the bounds of all {@link TracePoint2D} instances of all
   *         internal {@link ITrace2D} instances have changed since all points
   *         have been normalized to a value between 0 and 1.
   */
  protected final boolean isDirtyScaling(final int axis) {
    boolean result = false;
    Range range;
    if (axis == Chart2D.Y) {
      range = this.getAxisY().getRange();
      result = !range.equals(this.m_yRangePreviousScaling);
      result |= (this.m_ymax != this.m_ymaxold) || (this.m_ymin != this.m_yminold);

    } else if (axis == Chart2D.X) {
      range = this.getAxisX().getRange();
      result = !range.equals(this.m_xRangePreviousScaling);
      result |= (this.m_xmax != this.m_xmaxold) || (this.m_xmin != this.m_xminold);

    }
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
   * Returns the requestedRepaint.
   * <p>
   * 
   * @return the requestedRepaint
   */
  protected synchronized boolean isRequestedRepaint() {
    return this.m_requestedRepaint;
  }

  /**
   * Returns true if chart coordinates are drawn as tool tips.
   * <p>
   * 
   * @return true if chart coordinates are drawn as tool tips.
   */
  public final boolean isToolTipCoords() {
    return this.m_toolTipCoords;
  }

  /**
   * Returns true if the given point is in the visible drawing area of the
   * Chart2D.
   * <p>
   * If the point is null false will be returned.
   * <p>
   * This only works if the point argument has been scaled already.
   * <p>
   * 
   * @param point
   *            the point to test.
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
   */
  public final Iterator iterator() {
    return this.m_traces.iterator();
  }

  /**
   * Internally sets the value of <code>{@link #getXChartStart()}</code> and
   * <code>{@link #getXChartEnd()}</code> with respect to another chart
   * synchronized to the same value.
   * <p>
   * 
   * @param g2d
   *            needed for size information.
   * 
   * @see #calculateXChartStart(Graphics2D)
   * @see #calculateXChartEnd(Graphics2D)
   */
  private void negociateXChart(final Graphics2D g2d) {
    if (this.m_synchronizedXStartChart != null) {
      this.m_xChartStart = Math.max(this.calculateXChartStart(g2d), this.m_synchronizedXStartChart
          .calculateXChartStart(g2d));
      this.m_xChartEnd = Math.max(calculateXChartEnd(g2d), this.m_synchronizedXStartChart
          .calculateXChartEnd(g2d));
      this.m_synchronizedXStartChart.m_xChartStart = this.m_xChartStart;
      this.m_synchronizedXStartChart.m_xChartEnd = this.m_xChartEnd;
    } else {
      if (!this.m_synchronizedXStart) {
        this.m_xChartStart = this.calculateXChartStart(g2d);
        this.m_xChartEnd = this.calculateXChartEnd(g2d);
      }
    }
  }

  /**
   * A basic rule of a JComponent is: <br>
   * <b>Never invoke this method directly. </b> <br>
   * See the description of
   * {@link javax.swing.JComponent#paint(java.awt.Graphics)} for details.
   * <p>
   * If you do invoke this method you may encounter performance issues,
   * flickering UI and even deadlocks.
   * <p>
   * 
   * @param g
   *            the graphics context to use.
   */
  public synchronized void paint(final Graphics g) {

    if (Chart2D.DEBUG_THREADING) {
      System.out.println("paint, 1 lock");
    }
    super.paint(g);
    /*
     * If a modal dialog or other component hides parts of the chart and is
     * moved / disappears paint is invoked. Update scaling will signal that
     * scaling is not needed any more and the Painter Thread will think it does
     * not have to repaint (e.g. for trace.removeAllPoints()). Problem: Only a
     * part has been repainted then. Moving updateScaling call to Painter will
     * not work as Painter uses repaint() which will execute paint by another
     * Threads -> locks get lost and new points may be added between repaint and
     * paint invocation which leads to draw unscaled zero points.
     * 
     * So we detect here if only a partial repaint is triggered and remain
     * dirtyScaling for that case.
     */
    Rectangle clip = g.getClipBounds();
    if (clip == null) {
      this.updateScaling(false);
    } else {
      int width = this.getWidth();
      int height = this.getHeight();
      if (clip.getWidth() == width && clip.getHeight() == height) {
        this.updateScaling(false);
      }
    }
    // Some operations (e.g. stroke) need Graphics2d
    Graphics2D g2d = (Graphics2D) g;
    // will be used in several iterations.
    ITrace2D tmpdata;
    Iterator traceIt;
    Dimension d = this.getSize();
    int height = (int) d.getHeight();
    int axisXHeight = this.getAxisX().getHeight(g2d);
    // painting trace labels
    negociateXChart(g2d);
    int labelheight = this.paintTraceLabels(g2d);
    // finding startpoint of coordinate System.
    // -4 is for showing colons of x - labels that are below the baseline

    this.m_yChartStart = height - axisXHeight - labelheight - 4;
    this.m_yChartEnd = this.getAxisY().getHeight(g2d);

    int rangex = this.m_xChartEnd - this.m_xChartStart;
    int rangey = this.m_yChartStart - this.m_yChartEnd;
    this.paintCoordinateSystem(g2d);
    // paint Traces.
    int tmpx = 0;
    int oldtmpx;
    int tmpy = 0;
    int oldtmpy;
    TracePoint2D oldpoint = null;
    TracePoint2D newpoint = null;
    TracePoint2D tmppt = null;
    traceIt = this.m_traces.iterator();
    Stroke backupStroke = g2d.getStroke();
    int count = 0;
    Iterator itTracePainters;
    Iterator itTraceErrorBarPolicies;
    ITracePainter tracePainter;
    IErrorBarPolicy errorBarPolicy;

    while (traceIt.hasNext()) {
      oldpoint = null;
      newpoint = null;
      count++;
      tmpdata = (ITrace2D) traceIt.next();
      if (tmpdata.isVisible()) {
        g2d.setStroke(tmpdata.getStroke());
        g2d.setColor(tmpdata.getColor());
        synchronized (tmpdata) {
          if (Chart2D.DEBUG_THREADING) {
            System.out.println("paint(" + Thread.currentThread().getName()
                + "), 2 locks (lock on trace " + tmpdata.getName() + ")");
          }
          Set tracePainters = tmpdata.getTracePainters();
          itTracePainters = tracePainters.iterator();
          tracePainter = null;
          while (itTracePainters.hasNext()) {
            tracePainter = (ITracePainter) itTracePainters.next();
            tracePainter.startPaintIteration(g2d);
          }
          errorBarPolicy = null;
          Set errorBarPolicies = tmpdata.getErrorBarPolicies();
          itTraceErrorBarPolicies = errorBarPolicies.iterator();
          while (itTraceErrorBarPolicies.hasNext()) {
            errorBarPolicy = (IErrorBarPolicy) itTraceErrorBarPolicies.next();
            errorBarPolicy.startPaintIteration(g2d);
          }
          Iterator pointIt = tmpdata.iterator();
          boolean newpointVisible = false;
          boolean oldpointVisible = false;
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
              // to new point
              oldpoint = interpolateVisible(oldpoint, newpoint);
              tmpx = this.m_xChartStart + (int) Math.round(newpoint.m_scaledX * rangex);
              tmpy = this.m_yChartStart - (int) Math.round(newpoint.m_scaledY * rangey);
              oldtmpx = this.m_xChartStart + (int) Math.round(oldpoint.m_scaledX * rangex);
              oldtmpy = this.m_yChartStart - (int) Math.round(oldpoint.m_scaledY * rangey);
              this.paintPoint(oldtmpx, oldtmpy, tmpx, tmpy, false, tmpdata, g2d, newpoint);
            } else if (!newpointVisible && oldpointVisible) {
              // leaving the visible bounds:
              tmppt = (TracePoint2D) newpoint.clone();
              newpoint = interpolateVisible(newpoint, oldpoint);
              tmpx = this.m_xChartStart + (int) Math.round(newpoint.m_scaledX * rangex);
              tmpy = this.m_yChartStart - (int) Math.round(newpoint.m_scaledY * rangey);
              itTracePainters = tmpdata.getTracePainters().iterator();
              this.paintPoint(oldtmpx, oldtmpy, tmpx, tmpy, true, tmpdata, g2d, newpoint);
              // restore for next loop start:
              newpoint = tmppt;
            } else {
              // staying in the visible bounds: just paint
              tmpx = this.m_xChartStart + (int) Math.round(newpoint.m_scaledX * rangex);
              tmpy = this.m_yChartStart - (int) Math.round(newpoint.m_scaledY * rangey);
              this.paintPoint(oldtmpx, oldtmpy, tmpx, tmpy, false, tmpdata, g2d, newpoint);
            }
          }
          itTracePainters = tmpdata.getTracePainters().iterator();
          while (itTracePainters.hasNext()) {
            tracePainter = (ITracePainter) itTracePainters.next();
            tracePainter.endPaintIteration(g2d);
          }
          itTraceErrorBarPolicies = tmpdata.getErrorBarPolicies().iterator();
          while (itTraceErrorBarPolicies.hasNext()) {
            errorBarPolicy = (IErrorBarPolicy) itTraceErrorBarPolicies.next();
            errorBarPolicy.endPaintIteration(g2d);
          }
        }
      }
      if (Chart2D.DEBUG_THREADING) {
        System.out.println("paint(" + Thread.currentThread().getName() + "), left lock on trace "
            + tmpdata.getName());
      }
    }
    g2d.setStroke(backupStroke);
  }

  /**
   * Paints the axis, the scales and the labels for the chart.
   * <p>
   * <b>Caution</b> This is highly coupled code and only factored out for
   * better overview. This method may only be called by {@link #paint(Graphics)}
   * and the order of this invocation there must not be changed.
   * <p>
   * 
   * TODO: Move this to axis implementations, at least the title painting.
   * 
   * @param g2d
   *            the graphics context to use.
   */
  private void paintCoordinateSystem(final Graphics2D g2d) {
    IAxisTickPainter tickPainter = this.getAxisTickPainter();
    int rangexPx = this.m_xChartEnd - this.m_xChartStart;
    int rangeyPx = this.m_yChartStart - this.m_yChartEnd;
    // finding the font- dimensions in px
    FontMetrics fontdim = g2d.getFontMetrics();
    int fontheight = fontdim.getHeight();

    // drawing the axis:
    g2d.setColor(this.getForeground());
    g2d.drawLine(this.m_xChartStart, this.m_yChartStart, this.m_xChartEnd, this.m_yChartStart);
    g2d.drawLine(this.m_xChartStart, this.m_yChartStart, this.m_xChartStart, this.m_yChartEnd);

    // drawing the x title :
    this.m_axisX.paintTitle(g2d);

    // drawing the y title :
    int titleWidthY = this.m_axisY.paintTitle(g2d);

    // drawing the labels:
    int tmp;
    LabeledValue[] labels;
    if (this.m_axisX.isPaintScale()) {
      // first for x- angle.
      tmp = 0;
      labels = this.m_axisX.getScaleValues(g2d);
      for (int i = 0; i < labels.length; i++) {
        tmp = this.m_xChartStart + (int) (labels[i].getValue() * rangexPx);
        tickPainter.paintXTick(tmp, this.m_yChartStart, labels[i].isMajorTick(), g2d);
        tickPainter.paintXLabel(tmp, this.m_yChartStart + fontheight, labels[i].getLabel(), g2d);
        if (this.m_axisX.isPaintGrid()) {
          if ((i != 0) || (tmp != this.m_xChartStart)) {
            g2d.setColor(this.m_gridcolor);
            g2d.drawLine(tmp, this.m_yChartStart - 1, tmp, this.m_yChartEnd);
            g2d.setColor(this.getForeground());

          }
        }
      }
      // unit-labeling
      g2d.drawString(this.m_axisX.getFormatter().getUnit().getUnitName(), this.m_xChartEnd - 20,
          this.m_yChartStart - 5);
    }
    if (this.m_axisY.isPaintScale()) {
      // then for y- angle.
      labels = this.m_axisY.getScaleValues(g2d);
      for (int i = 0; i < labels.length; i++) {
        tmp = this.m_yChartStart - (int) (labels[i].getValue() * rangeyPx);
        tickPainter.paintYTick(this.m_xChartStart, tmp, labels[i].isMajorTick(), g2d);
        tickPainter.paintYLabel(titleWidthY, tmp, labels[i].getLabel(), g2d);
        if (this.m_axisY.isPaintGrid()) {
          if ((i != 0) || (tmp != this.m_yChartStart)) {
            g2d.setColor(this.m_gridcolor);
            g2d.drawLine(this.m_xChartStart + 1, tmp, this.m_xChartEnd, tmp);
            g2d.setColor(this.getForeground());
          }
        }
      }
      // unit-labeling
      g2d.drawString(this.m_axisY.getFormatter().getUnit().getUnitName(), this.m_xChartStart,
          this.m_yChartEnd - 5);
    }

  }

  /**
   * Internally renders the error bars for the given point for the given trace.
   * <p>
   * 
   * The current point to render in px is defined by the first two arguments,
   * the next point to render in px is defined by the 2nd two arguments.
   * <p>
   * 
   * @param trace
   *            needed to get the {@link IErrorBarPolicy} instances to use.
   * 
   * @param oldtmpx
   *            the x coordinate of the original point to render an error bar
   *            for.
   * 
   * @param oldtmpy
   *            the y coordinate of the original point to render an error bar
   *            for.
   * 
   * @param tmpx
   *            the x coordinate of the original next point to render an error
   *            bar for.
   * 
   * @param tmpy
   *            the y coordinate of the original next point to render an error
   *            bar for.
   * 
   * @param g2d
   *            the graphics context to use.
   * 
   * @param discontinue
   *            if a discontinuation has been taken place and all potential
   *            cached points by an <code>{@link ITracePainter}</code> (done
   *            for polyline performance boost) have to be drawn immediately
   *            before starting a new point caching.
   * 
   * @param original
   *            intended for information only, should nor be needed to paint the
   *            point neither be changed in any way!
   * 
   */
  private void paintErrorBars(final ITrace2D trace, final int oldtmpx, final int oldtmpy,
      final int tmpx, final int tmpy, final Graphics2D g2d, final boolean discontinue,
      final TracePoint2D original) {
    IErrorBarPolicy errorBarPolicy;
    Iterator itTraceErrorBarPolicies = trace.getErrorBarPolicies().iterator();
    while (itTraceErrorBarPolicies.hasNext()) {
      errorBarPolicy = (IErrorBarPolicy) itTraceErrorBarPolicies.next();
      errorBarPolicy.paintPoint(oldtmpx, oldtmpy, tmpx, tmpy, g2d, original);
      if (discontinue) {
        errorBarPolicy.discontinue(g2d);
      }
    }
  }

  /**
   * Internally paints the point with respect to trace painters ({@link ITracePainter})
   * and error bar painter ({@link IErrorBarPolicy}) of the trace.
   * <p>
   * 
   * This method must not be called directly as it does not support
   * interpolation of visibility bounds (discontinuations).
   * <p>
   * 
   * @param xPxOld
   *            the x coordinate of the previous point to render in px
   *            (potentially an interpolation of it if the old point was not
   *            visible and the new point is).
   * 
   * @param yPxOld
   *            the y coordinate of the previous point to render in px
   *            (potentially an interpolation of it if the old point was not
   *            visible and the new point is).
   * 
   * @param xPxNew
   *            the x coordinate of the point to render in px (potentially an
   *            interpolation of it if the old point was visible and the new
   *            point is not).
   * 
   * @param yPxNew
   *            the y coordinate of the point to render in px (potentially an
   *            interpolation of it if the old point was visible and the new
   *            point is not).
   * @param trace
   *            needed for obtaining trace painters and error bar painters.
   * 
   * @param g2d
   *            the graphics context to use.
   * 
   * @param discontinue
   *            if a discontinuation has been taken place and all potential
   *            cached points by an <code>{@link ITracePainter}</code> (done
   *            for polyline performance boost) have to be drawn immediately
   *            before starting a new point caching.
   * 
   * @param original
   *            intended for information only, should nor be needed to paint the
   *            point neither be changed in any way!
   */
  private final void paintPoint(final int xPxOld, final int yPxOld, final int xPxNew,
      final int yPxNew, final boolean discontinue, final ITrace2D trace, final Graphics2D g2d,
      final TracePoint2D original) {
    Iterator itTracePainters;
    ITracePainter tracePainter;
    itTracePainters = trace.getTracePainters().iterator();
    while (itTracePainters.hasNext()) {
      tracePainter = (ITracePainter) itTracePainters.next();
      tracePainter.paintPoint(xPxOld, yPxOld, xPxNew, yPxNew, g2d, original);
      if (discontinue) {
        tracePainter.discontinue(g2d);
      }
    }
    this.paintErrorBars(trace, xPxOld, yPxOld, xPxNew, yPxNew, g2d, discontinue, original);
  }

  /**
   * Internally paints the labels for the traces below the chart.
   * <p>
   * 
   * @param g2d
   *            the graphic context to use.
   * @return the amount of vertical (y) px used for the labels.
   */

  private int paintTraceLabels(final Graphics2D g2d) {
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
      // includes leading space
      int fontheight = fontdim.getHeight();

      if (traceIt.hasNext()) {
        labelheight += fontheight;
      }
      while (traceIt.hasNext()) {
        trace = (ITrace2D) traceIt.next();
        if (trace.isVisible()) {
          tmplabel = trace.getLabel();
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
   * Called from
   * {@link info.monitorenter.gui.chart.traces.ATrace2D#setZIndex(Integer)} to
   * show that property {@link ITrace2D#PROPERTY_ZINDEX} has changed.
   * </p>
   * <p>
   * This class adds itself as a {@link PropertyChangeListener} to the
   * {@link info.monitorenter.gui.chart.traces.ATrace2D} in method
   * {@link Chart2D#addTrace(ITrace2D)}.
   * </p>
   * <p>
   * Also used for properties <code>{@link ITrace2D#PROPERTY_MAX_X}</code>,
   * <code>{@link ITrace2D#PROPERTY_MAX_Y}</code>.
   * <code>{@link ITrace2D#PROPERTY_MIN_X}</code> and
   * <code>{@link ITrace2D#PROPERTY_MIN_Y}</code> to adapt to bound changes.
   * </p>
   * 
   * @param evt
   *            the property change event that was fired.
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(final PropertyChangeEvent evt) {
    if (Chart2D.DEBUG_THREADING) {
      System.out
          .println("chart.propertyChange (" + Thread.currentThread().getName() + "), 0 locks");
    }
    synchronized (this) {
      if (Chart2D.DEBUG_THREADING) {
        System.out.println("propertyChange (" + Thread.currentThread().getName() + "), 1 lock");
      }
      String property = evt.getPropertyName();
      // System.out.println(Chart2D.class.getName() + "propertyChange(" +
      // property + ")");
      double value;
      // first the bound changes:
      if (property.equals(ITrace2D.PROPERTY_MAX_X)) {
        if (Chart2D.DEBUG_SCALING) {
          System.out.println("pc-Xmax");
        }
        value = ((Double) evt.getNewValue()).doubleValue();
        if (value > this.m_xmax) {
          ITrace2D trace = (ITrace2D) evt.getSource();
          if (trace.isVisible()) {
            this.m_xmax = value;
          }
        } else if (value < this.m_xmax) {
          this.m_xmax = this.findMaxX();
        }
      } else if (property.equals(ITrace2D.PROPERTY_MIN_X)) {
        if (Chart2D.DEBUG_SCALING) {
          System.out.println("pc-Xmin");
        }
        value = ((Double) evt.getNewValue()).doubleValue();
        if (value < this.m_xmin) {
          ITrace2D trace = (ITrace2D) evt.getSource();
          if (trace.isVisible()) {
            this.m_xmin = value;
          }
        } else if (value > this.m_xmin) {
          this.m_xmin = this.findMinX();
        }
      } else if (property.equals(ITrace2D.PROPERTY_MAX_Y)) {
        if (Chart2D.DEBUG_SCALING) {
          System.out.println("pc-Ymax");
        }
        value = ((Double) evt.getNewValue()).doubleValue();
        if (value > this.m_ymax) {
          ITrace2D trace = (ITrace2D) evt.getSource();
          if (trace.isVisible()) {
            this.m_ymax = value;
          }
        } else if (value < this.m_ymax) {
          this.m_ymax = this.findMaxY();
        }
      } else if (property.equals(ITrace2D.PROPERTY_MIN_Y)) {
        if (Chart2D.DEBUG_SCALING) {
          System.out.println("pc-Ymin");
        }
        value = ((Double) evt.getNewValue()).doubleValue();
        if (value < this.m_ymin) {
          ITrace2D trace = (ITrace2D) evt.getSource();
          if (trace.isVisible()) {
            this.m_ymin = value;
          }
        } else if (value > this.m_ymin) {
          this.m_ymin = this.findMinY();
        }
      } else if (property.equals(IRangePolicy.PROPERTY_RANGE)) {
        // nop
      } else if (property.equals(IRangePolicy.PROPERTY_RANGE_MAX)) {
        // nop
      } else if (property.equals(IRangePolicy.PROPERTY_RANGE_MIN)) {
        // nop
      } else if (property.equals(ITrace2D.PROPERTY_TRACEPOINT)) {
        // now points added or removed -> rescale!
        if (Chart2D.DEBUG_SCALING) {
          System.out.println("pc-tp");
        }
        TracePoint2D oldPt = (TracePoint2D) evt.getOldValue();
        TracePoint2D newPt = (TracePoint2D) evt.getNewValue();
        // added or removed?
        // we only care about added points (rescaling is our task)
        if (oldPt == null) {
          this.scalePoint(newPt, Chart2D.X_Y);
        }
      } else if (property.equals(ITrace2D.PROPERTY_VISIBLE)) {
        // invisible traces don't count for max and min, so
        // expensive search has to be started:
        // TODO: Do performance: Get the trace of the event and check only
        // it's bounds here!!!
        this.m_xmax = this.findMaxX();
        this.m_xmin = this.findMinX();
        this.m_ymax = this.findMaxY();
        this.m_ymin = this.findMinY();
        // if the trace that became visible does not exceed bounds
        // it will not cause a "dirty Scaling" -> updateScaling and
        // repainting (in Painter Thread).
        ITrace2D trace = (ITrace2D) evt.getSource();
        this.m_axisX.scaleTrace(trace);
        this.m_axisY.scaleTrace(trace);

      } else if (property.equals(ITrace2D.PROPERTY_STROKE)) {
        // TODO: perhaps react more fine grained for the following events:
        // just repaint the trace without all the paint code (scaling,
        // axis,...).
        // But: These property changes are triggered by humans and occur
        // very seldom. Huge work non-l&f performance improvement.
      } else if (property.equals(ITrace2D.PROPERTY_PAINTERS)) {
        // nop
      } else if (property.equals(ITrace2D.PROPERTY_COLOR)) {
        // nop
      } else if (property.equals(ITrace2D.PROPERTY_NAME)) {
        // nop
      } else if (property.equals(ITrace2D.PROPERTY_ERRORBARPOLICY)) {
        // nop
      } else if (property.equals(ITrace2D.PROPERTY_ERRORBARPOLICY_CONFIGURATION)) {
        // nop
      } else if (property.equals(IAxis.PROPERTY_LABELFORMATTER)) {
        // TODO: Maybe only repaint the axis? Much complicated work vs.
        // occassional user interaction.
      } else if (property.equals(IAxisLabelFormatter.PROPERTY_FORMATCHANGE)) {
        // nop
      } else if (property.equals(ITrace2D.PROPERTY_POINT_CHANGED)) {
        TracePoint2D changed = (TracePoint2D) evt.getNewValue();
        this.scalePoint(changed, Chart2D.X_Y);
      } else if (property.equals(IAxis.PROPERTY_LABELFORMATTER)) {
        // nop
      } else if (property.equals(IAxis.PROPERTY_PAINTGRID)) {
        // nop
      } else if (property.equals(IAxis.PROPERTY_RANGEPOLICY)) {
        // nop
      } else if (property.equals(IAxis.PROPERTY_TITLE)) {
        // nop
      } else if (property.equals(IAxis.PROPERTY_TITLEFONT)) {
        // nop
      } else if (property.equals(IAxis.PROPERTY_TITLEPAINTER)) {
        // nop
      }
      this.setRequestedRepaint(true);
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
   *            the trace to remove.
   * @see Chart2D#PROPERTY_ADD_REMOVE_TRACE
   */
  public final void removeTrace(final ITrace2D points) {
    if (Chart2D.DEBUG_THREADING) {
      System.out.println("removeTrace, 0 locks");
    }
    synchronized (this) {
      if (Chart2D.DEBUG_THREADING) {
        System.out.println("removeTrace, 1 lock");
      }
      this.m_traces.remove(points);
      // TODO: stick with addTrace to explicitly remove chart as a listener for
      // all properties!
      points.removePropertyChangeListener(ITrace2D.PROPERTY_MAX_X, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_MIN_X, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_MAX_Y, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_MIN_Y, this);
      // These are repaint candidates:
      points.removePropertyChangeListener(ITrace2D.PROPERTY_COLOR, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_STROKE, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_VISIBLE, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_ZINDEX, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_PAINTERS, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_NAME, this);
      // rescale and repaint candidates:
      points.removePropertyChangeListener(ITrace2D.PROPERTY_TRACEPOINT, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_ERRORBARPOLICY, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_ERRORBARPOLICY_CONFIGURATION, this);
      points.removePropertyChangeListener(ITrace2D.PROPERTY_POINT_CHANGED, this);

      // update bounds:
      this.m_xmax = this.findMaxX();
      this.m_xmin = this.findMinX();
      this.m_ymax = this.findMaxY();
      this.m_ymin = this.findMinY();

    }
    this.firePropertyChange(Chart2D.PROPERTY_ADD_REMOVE_TRACE, points, null);
  }

  /**
   * @deprecated use {@link #setRequestedRepaint(boolean)}.
   * @see java.awt.Component#repaint()
   */
  public void repaint() {
    super.repaint();
  }

  /**
   * @deprecated use {@link #setRequestedRepaint(boolean)}.
   * 
   * @see java.awt.Component#repaint(int, int, int, int)
   */
  public void repaint(final int x,final  int y,final  int width,final  int height) {
    super.repaint(x, y, width, height);
  }

  /**
   * @deprecated use {@link #setRequestedRepaint(boolean)}.
   * @see java.awt.Component#repaint(long)
   */
  public void repaint(long tm) {
    super.repaint(tm);
  }

  /**
   * @deprecated use {@link #setRequestedRepaint(boolean)}.
   * @see javax.swing.JComponent#repaint(long, int, int, int, int)
   */
  public void repaint(long tm, int x, int y, int width, int height) {
    super.repaint(tm, x, y, width, height);
  }

  /**
   * @deprecated use {@link #setRequestedRepaint(boolean)}.
   * @see javax.swing.JComponent#repaint(java.awt.Rectangle)
   */
  public void repaint(Rectangle r) {
    super.repaint(r);
  }

  /**
   * Internally rescales the given <code>{@link TracePoint2D}</code> in both
   * dimensions by using the bounds provided by the internal
   * <code>{@link AAxis}</code> instances.
   * <p>
   * 
   * @param point
   *            the point to scale (between 0.0 and 1.0) according to the
   *            internal bounds.
   * 
   * @param axis
   *            one of {@link Chart2D#X}, {@link Chart2D#Y},
   *            {@link Chart2D#X_Y}.
   * 
   */
  private final void scalePoint(final TracePoint2D point, final int axis) {
    if (axis == Chart2D.X_Y) {
      point.m_scaledX = this.m_axisX.getScaledValue(point.getX());
      point.m_scaledY = this.m_axisY.getScaledValue(point.getY());
    } else if (axis == Chart2D.X) {
      point.m_scaledX = this.m_axisX.getScaledValue(point.getX());

    } else if (axis == Chart2D.Y) {
      point.m_scaledY = this.m_axisY.getScaledValue(point.getY());

    } else {
      throw new IllegalArgumentException("Illegal argument, use constants.");
    }
    if (Chart2D.DEBUG_SCALING) {
      // This is ok for fixed viewports that zoom!
      if ((point.m_scaledX > 1.0) || (point.m_scaledX < 0.0) || (point.m_scaledY > 1.0)
          || (point.m_scaledY < 0.0)) {
        System.out.println("Scaled Point " + point + " to [" + point.m_scaledX + ","
            + point.m_scaledY + "]");
      }
    }
  }

  /**
   * Sets the axis tick painter.
   * <p>
   * 
   * @param tickPainter
   *            The axis tick painter to set.
   */
  public synchronized void setAxisTickPainter(final IAxisTickPainter tickPainter) {
    this.m_axisTickPainter = tickPainter;
  }

  /**
   * Set the x axis to use.
   * <p>
   * 
   * @param axisX
   *            The axisX to set.
   */
  public void setAxisX(final AAxis axisX) {
    IAxis old = this.m_axisX;
    // steal the listeners:
    axisX.replace(old);
    this.m_axisX = axisX;
    axisX.setChart(this, Chart2D.X);

    this.firePropertyChange(Chart2D.PROPERTY_AXIS_X, old, this.m_axisX);
    this.setRequestedRepaint(true);
  }

  /**
   * Set the y axis to use.
   * <p>
   * 
   * @param axisY
   *            The axisY to set.
   */
  public void setAxisY(final AAxis axisY) {
    IAxis old = this.m_axisY;
    // steal the listeners:
    axisY.replace(old);
    this.m_axisY = axisY;
    axisY.setChart(this, Chart2D.Y);
    this.firePropertyChange(Chart2D.PROPERTY_AXIS_Y, old, this.m_axisY);
    this.setRequestedRepaint(true);
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
   *            the grid color to use.
   */
  public final void setGridColor(final Color gridclr) {
    if (gridclr != null) {
      Color old = this.m_gridcolor;
      this.m_gridcolor = gridclr;
      if (!old.equals(this.m_gridcolor)) {
        this.firePropertyChange(Chart2D.PROPERTY_GRID_COLOR, old, this.m_gridcolor);
      }
      this.setRequestedRepaint(true);
    }
  }

  /**
   * Sets the ms to give a repaint operation time for collecting several repaint
   * requests into one (performance vs. update speed).
   * <p>
   * 
   * @param minPaintLatency
   *            the setting for the ms to give a repaint operation time for
   *            collecting several repaint requests into one (performance vs.
   *            update speed).
   */
  public synchronized void setMinPaintLatency(final int minPaintLatency) {
    this.m_minPaintLatency = minPaintLatency;
    this.m_repainter.setDelay(this.m_minPaintLatency);
  }

  /**
   * <p>
   * Decide wether labels for each chart are painted below it. If set to true
   * this will be done, else labels will be ommited.
   * </p>
   * 
   * @param paintLabels
   *            the value for paintLabels to set.
   */
  public void setPaintLabels(final boolean paintLabels) {
    final boolean change = this.m_paintLabels != paintLabels;
    this.m_paintLabels = paintLabels;
    if (change) {
      this.firePropertyChange(Chart2D.PROPERTY_PAINTLABELS, new Boolean(!paintLabels), new Boolean(
          paintLabels));
      this.setRequestedRepaint(true);
    }
  }

  /**
   * Sets the requestedRepaint.
   * <p>
   * 
   * Internal method to request a repaint that guarantees that two invocations
   * of <code></code> will always have at least have an interval of
   * <code>{@link Chart2D#setMinPaintLatency(int)}</code> ms.
   * <p>
   * 
   * Methods <code>{@link Chart2D#repaint()}, {@link Chart2D#repaint(long)}, 
   * {@link Chart2D#repaint(Rectangle)}, {@link Chart2D#repaint(int, int, int, int)} 
   * and {@link Chart2D#repaint(long, int, int, int, int)}</code>
   * must not be called from application code that has to inform the UI to
   * update the chart directly or a performance problem may arise as java awt /
   * swing implementation does not guarantee to collapse several repaint
   * requests into a single one but prefers to issue many paint invocations
   * causing a high CPU load in realtime scenarios (adding several 100 points
   * per second to a chart).
   * <p>
   * Only the internal timer may invoke the methods mentioned above.
   * <p>
   * 
   * @param requestedRepaint
   *            the requestedRepaint to set.
   */
  public final synchronized void setRequestedRepaint(final boolean requestedRepaint) {
    this.m_requestedRepaint = requestedRepaint;
  }

  /**
   * Sets the chart that will be synchronized for finding the start coordinate
   * of this chart to draw in x dimension (<code>{@link #getXChartStart()}</code>).
   * <p>
   * 
   * This feature is used to allow two separate charts to be painted stacked in
   * y dimension (one below the other) that have different x start coordinates
   * (because of different y labels that shift that value) with an equal
   * starting x value (thus be comparable visually if their x values match).
   * <p>
   * 
   * @param synchronizedXStartChart
   *            the chart that will be synchronized for finding the start
   *            coordinate of this chart to draw in x dimension (<code>{@link #getXChartStart()}</code>).
   */
  public void setSynchronizedXStartChart(final Chart2D synchronizedXStartChart) {
    this.m_synchronizedXStartChart = synchronizedXStartChart;
    this.m_synchronizedXStart = false;
    synchronizedXStartChart.m_synchronizedXStart = true;
  }

  /**
   * Set wether this component should display the chart coordinates as a tool
   * tip.
   * <p>
   * 
   * This turns on tool tip support (like
   * {@link javax.swing.JComponent#setToolTipText(java.lang.String)}) if
   * neccessary.
   * <p>
   * 
   * @param toolTipCoords
   *            The toolTipCoords to set.
   */
  public final void setToolTipCoords(final boolean toolTipCoords) {
    if (toolTipCoords) {
      // this turns on tooltips.
      if (this.getToolTipText() == null) {
        this.setToolTipText("turnOn");
      }
    } else {
      // this is the hidden "unregister for tooltips trick".
      this.setToolTipText(null);
    }
    this.m_toolTipCoords = toolTipCoords;
  }

  /**
   * Returns a BufferedImage with the current width and height of the chart
   * filled with the Chart2D's graphics that may be written to a file or
   * OutputStream by using:
   * {@link javax.imageio.ImageIO#write(java.awt.image.RenderedImage, java.lang.String, java.io.File)}.
   * <p>
   * 
   * If the width and height of this chart is zero (this happens when the chart
   * has not been {@link javax.swing.JComponent#setVisible(boolean)}, the chart
   * was not integrated into layout correctly or the chart's dimenision was set
   * to this value, a default of width 600 and height 400 will temporarily be
   * set (syncrhonized), the image will be rendered, the old dimension will be
   * reset and the image will be returned.<br/> If you want to paint offscreen
   * images (without displayed chart) prefer invoke {@link #snapShot(int, int)}
   * instead.
   * <p>
   * 
   * @return a BufferedImage of the Chart2D's graphics that may be written to a
   *         file or OutputStream.
   * 
   * @since 1.03 - please download versions equal or greater than
   *        jchart2d-1.03.jar.
   */
  public BufferedImage snapShot() {
    int width = this.getWidth();
    int height = this.getWidth();
    if (width <= 0 && height <= 0) {
      width = 600;
      height = 400;
    }
    return this.snapShot(width, height);
  }

  /**
   * Returns a BufferedImage with the given width and height that is filled with
   * tChart2D's graphics that may be written to a file or OutputStream by using:
   * {@link javax.imageio.ImageIO#write(java.awt.image.RenderedImage, java.lang.String, java.io.File)}.
   * <p>
   * 
   * @param width
   *            the width of the image to create.
   * 
   * @param height
   *            the height of the image to create.
   * 
   * @return a BufferedImage of the Chart2D's graphics that may be written to a
   *         file or OutputStream.
   * @since 1.03 - please download versions equal or greater than
   *        jchart2d-1.03.jar.
   */
  public BufferedImage snapShot(final int width, final int height) {

    synchronized (this) {
      Dimension dsave = new Dimension(this.getWidth(), this.getHeight());
      this.setSize(new Dimension(width, height));
      BufferedImage img;
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2d = (Graphics2D) img.getGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      this.paint(g2d);
      this.setSize(dsave);
      return img;
    }
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    if (Chart2D.DEBUG_THREADING) {
      System.out.println("toString(), " + Thread.currentThread().getName() + ", 0 locks");
    }
    synchronized (this) {
      return super.toString();
    }
  }

  /**
   * Returns the translation of the mouse event coordinates of the given mouse
   * event to the value within the chart.
   * <p>
   * Note that the mouse event has to be an event fired on this component!
   * <p>
   * 
   * @param mouseEvent
   *            a mouse event that has been fired on this component.
   * @return the translation of the mouse event coordinates of the given mouse
   *         event to the value within the chart or null if no calculations
   *         could be performed as the chart was not painted before.
   * @throws IllegalArgumentException
   *             if the given mouse event does not belong to this component.
   */
  public TracePoint2D translateMousePosition(final MouseEvent mouseEvent)
      throws IllegalArgumentException {
    if (mouseEvent.getSource() != this) {
      throw new IllegalArgumentException(
          "The given mouse event does not belong to this chart but to: " + mouseEvent.getSource());
    }
    TracePoint2D result = null;
    double valueX = this.m_axisX.translateMousePosition(mouseEvent);
    double valueY = this.m_axisY.translateMousePosition(mouseEvent);
    result = new TracePoint2D(valueX, valueY);

    return result;
  }

  /**
   * Compares wether the bounds since last invocation have changed and
   * conditionally rescales the internal <code>{@link TracePoint2D}</code>
   * instances.
   * <p>
   * Must only be called from <code>{@link #paint(Graphics)}</code>.
   * <p>
   * The old recorded values for the bounds are set to the actual values
   * afterwards to allow detection of future changes again.
   * <p>
   * The force argument allows to enforce rescaling even if no change of data
   * bounds took place since the last scaling. This is useful if e.g. the view
   * upon the data is changed by a constraint (e.g.
   * {@link info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport}).
   * <p>
   * 
   * @param force
   *            if true no detection of changes of the data bounds as described
   *            above are performed: Rescaling is done unconditional.
   */
  private synchronized void updateScaling(final boolean force) {

    if (this.m_axisX != null && this.m_axisY != null) {
      this.m_axisX.initPaintIteration();
      this.m_axisY.initPaintIteration();
      boolean xChanged = isDirtyScaling(Chart2D.X);
      boolean yChanged = isDirtyScaling(Chart2D.Y);
      if (force || (xChanged && yChanged)) {
        this.m_axisX.scale();
        this.m_axisY.scale();
      } else if (xChanged) {
        this.m_axisX.scale();
      } else if (yChanged) {
        this.m_axisY.scale();
      } else {
        if (Chart2D.DEBUG_SCALING) {
          System.out.println("updateScaling: No scaling was performend.");
        }
      }
    }

    // reset equality
    this.m_xRangePreviousScaling.mimic(this.getAxisX().getRange());
    this.m_yRangePreviousScaling.mimic(this.getAxisY().getRange());
    this.m_xmaxold = this.m_xmax;
    this.m_ymaxold = this.m_ymax;
    this.m_xminold = this.m_xmin;
    this.m_yminold = this.m_ymin;

  }

}
