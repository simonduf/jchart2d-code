/**
 * Chart2D, a component for displaying ITrace2D- instances.
 * Copyright (C) 2002  Achim Westermann, Achim.Westermann@gmx.de
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import aw.gui.chart.ITrace2D.Trace2DChangeEvent;
import aw.util.Range;
import aw.util.units.Unit;
import aw.util.units.UnitFactory;
import aw.util.units.UnitSystemSI;

/**
 * <code> Char2D</code> is a component for diplaying the data contained in a
 * <code>ITrace2D</code>. It inherits many features from
 * <code>javax.swing.JPanel</code> and allows specific configuration. <br>
 * In order to simplify the use of it, the scaling, labeling and choosing of
 * display- range is done automatical which flattens the free configuration.
 * <br>
 * <br>
 * There are several rules that are kept by the <code>Chart2D</code><br>
 * <ul>
 * <li>The display range is choosen always big enough to show every
 * <code>TracePoint2D</code> contained in the all <code>ITrace2d</code>
 * instances connected. <br>
 * Maybe future releases will give the "zooming" ability.</li>
 * <li>During the <code>paint()</code> operation every
 * <code>TracePoint2D</code> is taken from the <code>ITrace2D</code>-
 * instance exactly in the order, it's iterator returns them. From every
 * <code>TracePoint2D</code> then a line is drawn to the next. <br>
 * Unordered traces may cause a weird display. Choose the right implementation
 * of <code>ITrace2D</code> to avoid this.</li>
 * <li>If no scaling is choosen, no grids will be painted. See:
 * <code>setScalingX()</code>,<code>setScalingY()</code>. This allows
 * saving of many computations.</li>
 * <li>Every scaling - point is set to the next even value of the chart
 * depending on the choosen decimals. If the first x- value in the chart is
 * 3.215 and decimalsX is 2, the first scalepoint of the x- axis will be shown
 * at position 3.22. This may cause problems, if the range of x -values does not
 * contain this last value!</li>
 * <li>The distance of the scalepoints is always big enough to display the
 * labels fully without overwriting each ohter.</li>
 * </ul>
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
 * 
 * Demo- code:
 * 
 * <pre>
 *                ...
 *                Chart2D test = new Chart2D();
 *                JFrame frame = new JFrame(&quot;Chart2D- Debug&quot;);
 *                frame.getContentPane().add(test);
 *                frame.setSize(400,200);
 *                frame.setVisible(true);
 *                ITrace2D atrace = new Trace2DLtd(100);
 *                ...
 *                &lt;further configuration of trace&gt;
 *                ...
 *                test.addTrace(atrace);
 *                ....
 *                while(expression){
 *                    atrace.addPoint(adouble,bdouble);
 *                }
 *                ....
 *        
 *      
 *     
 *    
 *   
 *  
 * </pre>
 * 
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 * @version 1.1
 */

public class Chart2D extends JPanel implements ITrace2D.Trace2DListener {
  /**
   * @see #DEBUG_LEVEL
   */
  static final int OFFSETS_DEBUG = 1;

  /**
   * @see #DEBUG_LEVEL
   */
  static final int MINMAX_DEBUG = 2;

  /**
   * @see #DEBUG_LEVEL
   */
  static final int TRACEPAINT_DEBUG = 4;

  /**
   * @see #DEBUG_LEVEL
   */
  static final int GRIDPAINT_DEBUG = 8;

  /**
   * @see #DEBUG_LEVEL
   */
  static final int LABELPAINT_DEBUG = 16;

  /**
   * @see #DEBUG_LEVEL
   */
  static final int COLORPAINT_DEBUG = 32;

  /**
   * @see #DEBUG_LEVEL
   */
  static final int SCALING_DEBUG = 64;

  /**
   * @see #DEBUG_LEVEL
   */
  static final int EVENT_DEBUG = 128;

  /**
   * @see #DEBUG_LEVEL
   */
  static final int MATH_DEBUG = 256; // round

  /**
   * @see #DEBUG_LEVEL
   */
  static final int SYNC_DEBUG = 512;

  /**
   * @see #DEBUG_LEVEL
   */
  static final int ALL_DEBUG = OFFSETS_DEBUG | MINMAX_DEBUG | TRACEPAINT_DEBUG | GRIDPAINT_DEBUG | LABELPAINT_DEBUG | COLORPAINT_DEBUG
      | SCALING_DEBUG | EVENT_DEBUG | MATH_DEBUG | SYNC_DEBUG;

  /**
   * @see #DEBUG_LEVEL
   */
  static final int EVENT_DEBUG_HARD = ALL_DEBUG ^ (GRIDPAINT_DEBUG | MATH_DEBUG | TRACEPAINT_DEBUG);

  /**
   * The embedded debugging code in the source does not bother the execution-
   * time if <code>DEBUG_LEVEL</code> is set to zero. The compiler will
   * compute static finals and get to know that all debugging code meets
   * conditions that are never true. You may see this by compiling with
   * different levels and watching the changing size of the classfile. So leave
   * it here for future works. <br>
   * Combine your custom debug level by assinging values: <br>
   * 
   * <pre>
   * static final int DEBUG_LEVEL = ALL_DEBUG &circ; (SYNC_DEBUG | MATH_DEBUG);
   * </pre>
   */
  static final int DEBUG_LEVEL = 0;//MATH_DEBUG | SCALING_DEBUG;

  public final static int X = 0;

  public final static int Y = 1;

  public final static int X_Y = 3;

  protected java.util.List traces = new LinkedList();

  protected Map labels = new HashMap();

  protected Unit unitY = UnitFactory.getInstance().getUnit(1, UnitSystemSI.getInstance());

  protected Unit unitX = unitY;

  protected Color gridcolor = Color.lightGray;

  protected int decimalsX = 1;

  protected int decimalsY = 1;

  protected boolean gridX = false;

  protected boolean gridY = false;

  protected boolean scaleX = true;

  protected boolean scaleY = true;

  protected boolean labeling = true;

  protected double xmax;

  protected double xmin;

  protected double ymax;

  protected double ymin;

  // needed for checking differences in bounds.
  protected double xmaxold;

  protected double xminold;

  protected double ymaxold;

  protected double yminold;
  /**
   * Type-safe instance to determine, that no 
   * constraints for display of ranges are given.
   */
  public static Range NO_RANGE_FORCED = new Range(0.0,0.0);
  /**
   *  Needed to define ranges that have to be displayed regardless 
   * of the minimum and maximum values found int the ITraces.
   */
  protected Range forceXRange = NO_RANGE_FORCED;
  /**
   * @see #forceXRange
   */
  protected Range forceYRange = NO_RANGE_FORCED;
  
  // for the painter Thread:
  protected Painter painter = new Painter();

  /**
   * A list of TracePoint2D instances that have been changed since the Painter
   * did his work.
   */
  protected List pendingChanges = new LinkedList();

  public Chart2D() {
    Font dflt = this.getFont();
    if (dflt != null) {
      this.setFont(new Font(dflt.getFontName(), dflt.getStyle(), 10));
    }
    this.setBackground(Color.white);
    this.painter.start();
  }

  public synchronized void paint(Graphics g) {
    if ((DEBUG_LEVEL & SYNC_DEBUG) != 0) {
      System.out.println(Thread.currentThread().getName() + " has lock on " + this.getClass().getName()
          + Integer.toHexString(this.hashCode()));
    }
    super.paint(g);
    this.updateScaling();
    Dimension d = this.getSize();
    int width = (int) d.getWidth();
    int height = (int) d.getHeight();
    //finding the font- dimensions in px
    FontMetrics fontdim = g.getFontMetrics();
    int fontwidth = fontdim.charWidth('9');
    int fontheight = fontdim.getHeight(); // includes leading space between
    // lines

    int rootx = (this.decimalsY + 5) * fontwidth;

    int labelheight = 0;
    //painting different labels
    if (labeling) {
      Collection entries = this.labels.values();
      Iterator it = entries.iterator();
      ColoredLabel current;
      String tmplabel;
      int xtmpos = rootx;
      int ytmpos = height - 2;
      int remwidth = width - rootx;
      int allwidth = remwidth;
      int lblwidth = 0;
      boolean crlfdone = false;
      if (it.hasNext()) {
        labelheight += fontheight;
      }
      while (it.hasNext()) {
        current = (ColoredLabel) it.next();
        tmplabel = current.label;
        lblwidth = tmplabel.length() * fontwidth;
        // conditional linebreak.
        // crlfdone avoids never doing linebreak if all
        // labels.length()>allwidth
        if (lblwidth > remwidth) {
          if (!(lblwidth > allwidth) || (!crlfdone)) {
            ytmpos -= fontheight;
            xtmpos = rootx;
            labelheight += fontheight;
            crlfdone = true;
            remwidth = width - rootx;
          } else {
            crlfdone = false;
          }
        }
        remwidth -= lblwidth;
        g.setColor(current.color);
        g.drawString(tmplabel, xtmpos, ytmpos);
        xtmpos += lblwidth;
      }
      if ((DEBUG_LEVEL & LABELPAINT_DEBUG) != 0) {
        System.out.println("paint(): Painting of labels took " + labelheight + " px of height.");
      }

    }
    //ColoredLabels[] labels = getLables();
    //finding startpoint of coordinate System.
    int rooty = height - fontheight - labelheight;
    int endx = width - 20;
    int endy = 20;
    int rangex = endx - rootx;
    int rangey = rooty - endy;
    if ((DEBUG_LEVEL & TRACEPAINT_DEBUG) != 0) {
      System.out.println("paint(): pxRangeX = " + rangex + " pxRangeY = " + rangey);
    }
    //drawing the coordinate lines.
    g.setColor(Color.black);
    g.drawLine(rootx, rooty, endx, rooty);
    g.drawLine(rootx, rooty, rootx, endy);
    //drawing the labels.
    int tmp;
    LabeledValue[] labels;
    if (this.scaleX) {
      // first for x- angle.
      tmp = 0;
      labels = getScaleValuesX(getScaleDistanceX((this.decimalsX + 4) * fontwidth));
      if ((DEBUG_LEVEL & GRIDPAINT_DEBUG) != 0) {
        System.out.println("paint(): painting " + labels.length + " x-grids. ");
      }
      for (int i = 0; i < labels.length; i++) {
        tmp = rootx + (int) (labels[i].getValue() * rangex);
        g.drawLine(tmp, rooty, tmp, rooty + 5);
        g.drawString(labels[i].getLabel(), tmp, rooty + fontheight);
        if (gridX) {
          if ((i != 0) || (tmp != rootx)) {
            g.setColor(this.gridcolor);
            g.drawLine(tmp, rooty - 1, tmp, endy);
            g.setColor(Color.black);
            if ((DEBUG_LEVEL & GRIDPAINT_DEBUG) != 0) {
              System.out.println("paint(): painting gridx nr " + i + " from " + tmp + "," + (rooty - 1) + " to " + tmp + "," + endy);
            }

          }
        }
      }
      //unit-labeling
      g.drawString(this.unitX.getLabel(), endx - 20, rooty - 5);
    }
    if (scaleY) {
      // then for y- angle.
      labels = getScaleValuesY(getScaleDistanceY(fontheight));
      if ((DEBUG_LEVEL & GRIDPAINT_DEBUG) != 0) {
        System.out.println("paint(): painting " + labels.length + " y-grids. ");
      }
      for (int i = 0; i < labels.length; i++) {
        tmp = rooty - (int) (labels[i].getValue() * rangey);
        g.drawLine(rootx, tmp, rootx - 5, tmp);
        g.drawString(labels[i].getLabel(), 2, tmp);
        if (gridY) {
          if ((i != 0) || (tmp != rooty)) {
            g.setColor(this.gridcolor);
            g.drawLine(rootx + 1, tmp, endx, tmp);
            g.setColor(Color.black);
            if ((DEBUG_LEVEL & GRIDPAINT_DEBUG) != 0) {
              System.out.println("paint(): painting gridy nr " + i + " from " + (rootx + 1) + "," + tmp + " to " + endx + "," + tmp);
            }
          }
        }
      }
      //unit-labeling
      g.drawString(this.unitY.getLabel(), rootx, endy - 5);
    }
    //paint Traces.
    int stop = this.traces.size(), tmpx, oldtmpx, tmpy, oldtmpy;
    LinkedList tmptrace;
    ITrace2D tmpdata;
    TracePoint2D tmppt;
    Iterator it = null;
    for (int i = 0; i < stop; i++) {
      tmpdata = (ITrace2D) traces.get(i);
      Color acolor = tmpdata.getColor();
      g.setColor(tmpdata.getColor());
      if ((DEBUG_LEVEL & SYNC_DEBUG) != 0) {
        System.out.println(Thread.currentThread().getName() + " ask for lock on " + tmpdata.getClass().getName()
            + Integer.toHexString(tmpdata.hashCode()));
      }
      synchronized (tmpdata) {
        if ((DEBUG_LEVEL & SYNC_DEBUG) != 0) {
          System.out.println(Thread.currentThread().getName() + " has lock on " + tmpdata.getClass().getName()
              + Integer.toHexString(tmpdata.hashCode()));
        }
        it = tmpdata.iterator();
        if (it.hasNext()) {
          tmppt = (TracePoint2D) it.next();
          oldtmpx = rootx + (int) (tmppt.scaledX * rangex);
          oldtmpy = rooty - (int) (rangey * tmppt.scaledY);
          while (it.hasNext()) {
            tmppt = (TracePoint2D) it.next();
            // Don't render points not processed by Painter Thread.
            tmpx = rootx + (int) (tmppt.scaledX * rangex);
            tmpy = rooty - (int) (tmppt.scaledY * rangey);
            g.drawLine(oldtmpx, oldtmpy, tmpx, tmpy);
            if ((DEBUG_LEVEL & TRACEPAINT_DEBUG) != 0) {
              System.out.println("Chart2D.paint(): from " + oldtmpx + "," + oldtmpy + " to " + tmpx + "," + tmpy);
              System.out.println("Chart2D.paint(): tmppt.scaledX = " + tmppt.scaledX + " tmppt.scaledY = " + tmppt.scaledY);
            }
            oldtmpx = tmpx;
            oldtmpy = tmpy;
          }
        }
      }
    }
  }

  /**
   *  
   */
  public final synchronized void addTrace(ITrace2D points) {
    if ((DEBUG_LEVEL & SYNC_DEBUG) != 0) {
      System.out.println(Thread.currentThread().getName() + " has lock on " + this.getClass().getName()
          + Integer.toHexString(this.hashCode()));
    }
    points.setRenderer(this);
    this.traces.add(points);
    points.addChangeListener(this);
    String name = points.getName();
    String physunit = points.getPhysicalUnits();
    if ((name != "") && (physunit != "")) {
      name = new StringBuffer(name).append(" ").append(physunit).toString();
    } else if (physunit != "") {
      name = new StringBuffer("unnamed").append(" ").append(physunit).toString();
    }
    // now name is the whole label
    if (name.length() != 0) {
      ColoredLabel label = new ColoredLabel(name, points.getColor());
      this.labels.put(points, label);
    }
    this.traceChanged(new ITrace2D.Trace2DChangeEvent(points, ITrace2D.ALL_POINTS_CHANGED));
  }

  public final synchronized void removeTrace(ITrace2D points) {
    this.traces.remove(points);
    this.labels.remove(points);
    this.adjustRangeX();
    this.adjustRangeY();
    this.scaleAll(X_Y);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        repaint();
      }
    });
  }

  public final java.util.List getTraces() {
    return this.traces;
  }

  /**
   * All values are manipulated in that way that the curve will start at the
   * zero - point of the Chart2D.
   */
  private final void adjustRangeX() {
    this.getMaxX();
    this.getMinX();
    if ((this.xmaxold != this.xmax) || (this.xminold != this.xmin))
      this.chooseUnitX();
    if (this.xmin == this.xmax)
      this.xmin -= this.unitX.factor;
  }

  private final void adjustRangeY() {
    this.getMaxY();
    this.getMinY();
    if ((this.ymaxold != this.ymax) || (this.yminold != this.ymin))
      this.chooseUnitY();
    if (this.ymin == this.ymax)
      this.ymin -= this.unitY.factor;
  }

  /**
   * @return Returns the {@link #forceXRange}.
   */
  public Range getForceXRange() {
    return forceXRange;
  }
  /**
   * Allows to define a range (or even just a point), that 
   * has to be displayed in the x-axis, regardless whether 
   * there is data to display in that region.
   * 
   * @param forceXRange The {@link #forceXRange} to set.
   */
  public void setForceXRange(Range forceXRange) {
    this.forceXRange = forceXRange;
  }
  /**
   * @return Returns the {@link #forceYRange}.
   */
  public Range getForceYRange() {
    return forceYRange;
  }
  /**
   * Allows to define a range (or even just a point), that 
   * has to be displayed in the y-axis, regardless whether 
   * there is data to display in that region.
   * 
   * @param forceYRange The {@link #forceYRange} to set.
   */
  public void setForceYRange(Range forceYRange) {
    this.forceYRange = forceYRange;
  }

  public double getOffsetX() {
    return this.xmin;
  }

 

  public final double getOffsetY() {
    return this.ymin;
  }

  /**
   * Internally 
   * <ol>
   *  <li>
   *  Searches for the maximum x value of all contained ITraces.
   *  <li>
   *  Checks, wether the internal range {@link #forceXRange} is 
   *  higher.
   *  <li>
   *  Internally assings Math.max({@link #forceXRange}.getMax(),&lt;searcedMaxX&gt;) 
   *  to the member {@link #xmax}.
   * </ol>
   *
   */
  protected final void getMaxX() {
    double max = -Double.MAX_VALUE, tmp;
    Iterator it = this.traces.iterator();
    while (it.hasNext()) {
      tmp = ((ITrace2D) it.next()).getMaxX();
      if (tmp > max)
        max = tmp;
    }
    if (max == -Double.MAX_VALUE)
      max = 10;
    this.xmaxold = this.xmax;
    if(this.forceXRange != NO_RANGE_FORCED){
      max = Math.max(this.forceXRange.getMax(),max);
    }
    this.xmax = max;
    if ((DEBUG_LEVEL & MINMAX_DEBUG) == MINMAX_DEBUG)
      System.out.println("getMaxX returns " + this.xmax);

  }
  /**
   * Internally 
   * <ol>
   *  <li>
   *  Searches for the minimum x value of all contained ITraces.
   *  <li>
   *  Checks, wether the internal range {@link #forceXRange} is 
   *  lower.
   *  <li>
   *  Internally assings Math.min({@link #forceXRange}.getMin(),&lt;searcedMaxX&gt;) 
   *  to the member {@link #xmin}.
   * </ol>
   *
   */

  protected final void getMinX() {
    double min = Double.MAX_VALUE, tmp;
    Iterator it = this.traces.iterator();
    while (it.hasNext()) {
      tmp = ((ITrace2D) it.next()).getMinX();
      if (tmp < min)
        min = tmp;
    }
    if (min == Double.MAX_VALUE)
      min = 0;
    this.xminold = this.xmin;
    if(this.forceXRange != NO_RANGE_FORCED){
      min = Math.min(this.forceXRange.getMin(),min);
    }
    this.xmin = min;
    if ((DEBUG_LEVEL & MINMAX_DEBUG) == MINMAX_DEBUG)
      System.out.println("getMinX returns " + this.xmin);

  }
  
  /**
   * Internally 
   * <ol>
   *  <li>
   *  Searches for the maximum y value of all contained ITraces.
   *  <li>
   *  Checks, wether the internal range {@link #forceYRange} is 
   *  higher.
   *  <li>
   *  Internally assings Math.max({@link #forceYRange}.getMax(),&lt;searcedMaxY&gt;) 
   *  to the member {@link #ymax}.
   * </ol>
   *
   */
  protected final void getMaxY() {
    double max = -Double.MAX_VALUE, tmp;
    for (int i = this.traces.size() - 1; i >= 0; i--) {
      tmp = ((ITrace2D) this.traces.get(i)).getMaxY();
      if (tmp > max)
        max = tmp;
    }
    if (max == -Double.MAX_VALUE) {
      max = 10;
    }
    this.ymaxold = this.ymax;
    if(this.forceYRange != NO_RANGE_FORCED){
      max = Math.max(this.forceYRange.getMax(),max);
    }

    this.ymax = max;
    if ((DEBUG_LEVEL & MINMAX_DEBUG) == MINMAX_DEBUG)
      System.out.println("getMaxY returns " + this.ymax);
  }
  /**
   * Internally 
   * <ol>
   *  <li>
   *  Searches for the minimum y value of all contained ITraces.
   *  <li>
   *  Checks, wether the internal range {@link #forceYRange} is 
   *  lower.
   *  <li>
   *  Internally assings Math.min({@link #forceYRange}.getMin(),&lt;searcedMaxY&gt;) 
   *  to the member {@link #xmin}.
   * </ol>
   *
   */

  protected final void getMinY() {
    double min = Double.MAX_VALUE, tmp;
    for (int i = this.traces.size() - 1; i >= 0; i--) {
      tmp = ((ITrace2D) this.traces.get(i)).getMinY();
      if (tmp < min)
        min = tmp;
    }
    if (min == Double.MAX_VALUE) {
      min = 0;
    }
    this.yminold = this.ymin;
    if(this.forceYRange != NO_RANGE_FORCED){
      min = Math.min(this.forceYRange.getMin(),min);
    }
    this.ymin = min;
    if ((DEBUG_LEVEL & MINMAX_DEBUG) == MINMAX_DEBUG)
      System.out.println("getMinY returns " + this.ymin);
  }

  protected final void chooseUnitX() {
    double max = Math.max(Math.abs(this.xmax), Math.abs(this.xmin));
    if (max == 0)
      max = 1;
    this.unitX = UnitFactory.getInstance().getUnit(max, UnitSystemSI.getInstance());
  }

  protected final void chooseUnitY() {
    double max = Math.max(Math.abs(this.ymax), Math.abs(this.ymin));
    if (max == 0)
      max = 1;
    this.unitY = UnitFactory.getInstance().getUnit(max, UnitSystemSI.getInstance());
  }

  /**
   * Scales the given absolute value into a value between 0 and 1.0 (if it is in
   * the x- range of the data). The absoluteX is lowered by offsetX to relate it
   * to the rest of the data of the Points. If the given absoluteX is not in the
   * display- range of the Chart2DData (offsetX), negative values or values
   * greater than 1.0 may result.
   */
  public final double getScaledValueX(double absoluteX) {
    double scalerX = xmax - xmin;
    return (absoluteX - xmin) / scalerX;
  }

  /**
   * Scales the given absolute value into a value between 0 and 1.0 (if it is in
   * the y- range of the data). The absoluteValue is lowered by offsetY to
   * relate it to the rest of the data of the Points. If the given absoluteX is
   * not in the display- range of the Chart2DData (offsetX), negative values or
   * values greater than 1.0 may result.
   */

  public final double getScaledValueY(double absoluteY) {
    double scalerY = ymax - ymin;
    return (absoluteY - ymin) / scalerY;
  }

  public final String getLabelAbsoluteX(double absolute) {
    if (this.unitX == null)
      this.chooseUnitX();
    return this.unitX.getLabel(absolute);
  }

  public final String getLabelAbsoluteY(double absolute) {
    if (this.unitY == null)
      this.chooseUnitY();
    return this.unitY.getLabel(absolute);
  }

  /**
   * Depending on the width of the actual Chart2D, different distances lie
   * between the labeled scalepoints. The labeling might overwrite itself, if
   * the window is too small. This method calculates (depending on the actual
   * painting area of the Chart2D, the necessary scale - resolution (distance
   * between 2 scale - lines in the actual unit) to guarantee the given distance
   * of pixels between two scalings. This method is used by the paint - method.
   * It's return value is used for getScalePointX(float res) to guarantee the
   * readability of the labeling.
   * 
   * @param pixel
   *          The desired amount of pixels between two labels on the x- axis.
   * @return a double representing the
   */
  public final double getScaleDistanceX(int pixel) {
    Dimension d = this.getSize();
    int pxrangex = (int) d.getWidth() - 60;
    if (pxrangex <= 0) {
      return -1d;
    }
    double valuerangex = this.xmax - this.xmin;
    double px_to_value = valuerangex / pxrangex;
    double ret = px_to_value * pixel;
    if ((DEBUG_LEVEL & SCALING_DEBUG) != 0)
      System.out.println("getScaleDistanceX() returns " + ret);
    return ret;
  }

  /**
   * Depending on the width of the actual Chart2D, different distances lie
   * between the labeled scalepoints. The labeling might overwrite itself, if
   * the window is too small. This method calculates (depending on the actual
   * painting area of the Chart2D, the necessary scale - resolution (distance
   * between 2 scale - lines in the actual unit) to guarantee the given distance
   * of pixels between two scalings. This method is used by the paint - method.
   * It's return value is used for getScalePointY(float res) to guarantee the
   * readability of the labeling. The return values are rounded to the next
   * value that allows the scaling to hit natural values for the unit (avoiding
   * 2.23453244 e.g.).
   * 
   * @param pixel
   *          The desired distance between to scalepoints of the x- axis in
   *          pixel.
   * @return a scaled (from pixel to internal value-range) and normed (to the
   *         factor of the current unit of the x- axis) value usable to
   *         calculate the x- coords for the scalepoints of the x- axis.
   */
  public final double getScaleDistanceY(int pixel) {
    Dimension d = this.getSize();
    int pxrangey = (int) d.getHeight() - 40;
    if (pxrangey <= 0) {
      return -1d;
    }
    double valuerangey = this.ymax - this.ymin;
    double px_to_value = valuerangey / pxrangey;
    double ret = px_to_value * pixel;
    if ((DEBUG_LEVEL & SCALING_DEBUG) != 0)
      System.out.println("getScaleDistanceY() valuerangey = " + valuerangey + " pxrangey = " + pxrangey + " return = " + ret);
    return ret;

  }

  /**
   * @see #getScaleValuesY(double resolution)
   */
  private final LabeledValue[] getScaleValuesX(double resolution) {
    if (resolution <= 0) {
      if ((DEBUG_LEVEL & SCALING_DEBUG) != 0) {
        System.out.println("getScaleValuesX(): resolution<=0! --> returning nothing!");
      }
      return new LabeledValue[] {};
    }
    LinkedList collect = new LinkedList();
    double rangex = this.xmax - this.xmin;
    String tmplabel;
    double add = 1 / this.unitX.factor;
    while (add < resolution) {
      add *= 10;
    }
    double tmp = round(Math.abs(this.xmin) / this.unitX.factor, this.decimalsX, false) * this.unitX.factor - Math.abs(this.xmin);
    double roundlabel = 0;
    if ((DEBUG_LEVEL & SCALING_DEBUG) != 0) {
      System.out.println("getScaleValuesX(): rangex = " + rangex + " add = " + add + " starting from " + tmp);
    }
    while (tmp <= rangex) {
      roundlabel = round((tmp + xmin) / this.unitX.factor, this.decimalsX, false);
      tmplabel = String.valueOf(roundlabel);
      collect.add(new LabeledValue(tmp / rangex, tmplabel));
      tmp += add;
    }
    int stop = collect.size();
    LabeledValue[] ret = new LabeledValue[stop];
    for (int i = 0; i < stop; i++) {
      ret[i] = (LabeledValue) collect.get(i);
    }
    return ret;
  }

  /**
   * Depending on the unitY.factor an amount of scaled y- values between 0 and
   * 1.0 are given back whose distance represent resolution units. If the range
   * of the Traces's y - values for example is 30 K[String baseUnit] the call to
   * this method with resolution = 0.5 will result in 60 doubles between 0 and
   * 1.0. <br>
   * <p>
   * Note that the argument <code>resolution</code> is just a lower bound for
   * the distance between the labels (guarantees the readability of labels). The
   * <code>Chart2D </code> is designed to choose scales that are common with
   * the decimal- system.
   * </p>
   * 
   * @see #getScaleValuesX(double resolution)
   */
  private final LabeledValue[] getScaleValuesY(double resolution) {
    if (resolution <= 0)
      return new LabeledValue[] {};
    LinkedList collect = new LinkedList();
    double rangey = this.ymax - this.ymin;
    String tmplabel;
    double add = 1 / this.unitY.factor;
    while (add < resolution) {
      add *= 10;
    }
    double tmp = round(Math.abs(this.ymin) / this.unitY.factor, this.decimalsY, false) * this.unitY.factor - Math.abs(this.ymin);
    double roundlabel = 0;
    if ((DEBUG_LEVEL & SCALING_DEBUG) != 0) {
      System.out.println("getScaleValuesY(): rangey = " + rangey + " add = " + add + " starting from " + tmp);
    }
    while (tmp <= rangey) {
      roundlabel = round((tmp + ymin) / this.unitY.factor, this.decimalsY, false);
      tmplabel = String.valueOf(roundlabel);
      collect.add(new LabeledValue(tmp / rangey, tmplabel));
      tmp += add;
    }
    int stop = collect.size();
    LabeledValue[] ret = new LabeledValue[stop];
    for (int i = 0; i < stop; i++) {
      ret[i] = (LabeledValue) collect.get(i);
    }
    return ret;
  }

  /**
   * Processes pending changes. Must (and only should) be called from within
   * paint or else painting will render illegal points (matter of
   * monitor-chain).
   */
  private synchronized void updateScaling() {
    adjustRangeX();
    adjustRangeY();
    while (!Chart2D.this.pendingChanges.isEmpty()) {
      Trace2DChangeEvent event = (Trace2DChangeEvent) Chart2D.this.pendingChanges.remove(0);
      TracePoint2D changed = event.getPoint();
      ITrace2D trace = (ITrace2D) event.getSource();
      double scalerX = xmax - xmin;
      double scalerY = ymax - ymin;
      double xnew = (changed.getX() - xmin) / scalerX;
      double ynew = (changed.getY() - ymin) / scalerY;
      if (ynew == Double.NaN)
        xnew = 0;
      if (ynew == Double.NaN)
        ynew = 0;
      changed.scaledX = xnew;
      changed.scaledY = ynew;
      changed.scaledOnce = true;
    }
    // Conditional rescaling of all points due to
    // changed bounds.
    boolean x = false, y = false;
    if ((xmaxold != xmax) || (xminold != xmin)) {
      x = true;
    }
    if ((ymaxold != ymax) || (yminold != ymin)) {
      y = true;
    }
    if ((DEBUG_LEVEL & EVENT_DEBUG) != 0) {
      System.out.println("traceChanged(): \nmaxx = " + xmax + "\nminx = " + xmin + "\nmaxy = " + ymax + "\nminy = " + ymin);
      if (x)
        System.out.println("traceChanged triggers x - scaling");
      if (y)
        System.out.println("traceChanged triggers y - scaling");
    }
    if (x || y) {
      // rescaling for the rest?
      if (x && y) {
        scaleAll(X_Y);
      } else if (y) {
        scaleAll(Y);
      } else {
        scaleAll(X);
      }
    }
  }

  private final void scaleTrace(ITrace2D trace, int axis) {
    LinkedList scaled = new LinkedList();
    TracePoint2D tmp;
    double scalerX = xmax - xmin;
    double scalerY = ymax - ymin;
    double tmpx, tmpy;
    if ((DEBUG_LEVEL & SCALING_DEBUG) != 0) {
      System.out.println("scaleTrace(): scalerX =  " + scalerX + " scalerY = " + scalerY);
    }
    if ((DEBUG_LEVEL & SYNC_DEBUG) != 0) {
      System.out.println(Thread.currentThread().getName() + " ask for lock on " + trace.getClass().getName()
          + Integer.toHexString(trace.hashCode()));
    }
    synchronized (trace) {
      Iterator it = trace.iterator();
      if (axis == X_Y) {
        while (it.hasNext()) {
          tmp = (TracePoint2D) it.next();
          tmpx = (tmp.getX() - xmin) / scalerX;
          tmpy = (tmp.getY() - ymin) / scalerY;
          if (tmpx == Double.NaN)
            tmpx = 0;
          if (tmpy == Double.NaN)
            tmpy = 0;
          tmp.scaledX = tmpx;
          tmp.scaledY = tmpy;
        }
      } else if (axis == X) {
        while (it.hasNext()) {
          tmp = (TracePoint2D) it.next();
          tmpx = (tmp.getX() - xmin) / scalerX;
          if (tmpx == Double.NaN)
            tmpx = 0;
          tmp.scaledX = tmpx;
        }
      } else if (axis == Y) {
        while (it.hasNext()) {
          tmp = (TracePoint2D) it.next();
          tmpy = (tmp.getY() - ymin) / scalerY;
          if (tmpy == Double.NaN)
            tmpy = 0;
          tmp.scaledY = tmpy;
        }
      }

    }
  }

  protected final void scaleAll(int axis) {
    //        adjustRangeX();
    //        adjustRangeY();
    for (int i = this.traces.size() - 1; i >= 0; i--)
      scaleTrace((ITrace2D) traces.get(i), axis);
  }

  /**
   * Rounds the given value to the amount of given decimals. The last digit of
   * internal result is put to the next even number in the choosen direction.
   */
  public final double round(double value, int decimals, boolean floor) {
    double tmp = Math.pow(10, decimals);
    double toround = value * tmp;
    if (floor) {
      toround = Math.floor(toround + 0.5);
      //while(toround%2!=0)
      //    toround--;
    } else {
      toround = Math.ceil(toround + 0.5);
      //while(toround%2!=0)
      //    toround++;
    }
    toround = toround / tmp;
    if ((DEBUG_LEVEL & MATH_DEBUG) != 0)
      System.out.println("round(): value= " + value + " decimals = " + decimals + " floor = " + floor + " return: " + toround);
    return toround;
  }

  public final void setDecimalsX(int decimals) {
    this.decimalsX = decimals;
  }

  public final void setDecimalsY(int decimals) {
    this.decimalsY = decimals;
  }

  public final void setGridX(boolean gridx) {
    this.gridX = gridx;
    if (gridx)
      this.setScaleX(true);
  }

  public final void setGridY(boolean gridy) {
    this.gridY = gridy;
    if (gridy)
      this.setScaleY(true);
  }

  public final void setScaleX(boolean show) {
    this.scaleX = show;
  }

  public final void setScaleY(boolean show) {
    this.scaleY = show;
  }

  public final void setGridColor(Color gridclr) {
    if (gridclr != null)
      this.gridcolor = gridclr;
  }

  public final Color getGridColor() {
    return this.gridcolor;
  }

  public synchronized void traceChanged(final ITrace2D.Trace2DChangeEvent e) {
    /*
     * Thread 1 calls addPoint() on trace1 and invokes indirectly traceChanged.
     * He owns the monitor of trace1. While doing the first computations Thread
     * 2 invokes addPoint() on trace 2 and runs into traceChanged(). He owns the
     * monitor of trace2. Thread 1 has to rescale all traces which has do be
     * done synchronized on each trace. He is blocked at trying to get the
     * monitor of trace 2. Then Thread 2 has to rescale all traces too. He is
     * blocked at trying to get the monitor of trace 1. Therefore this runnable
     * is created here. At least i hope a pooled Thread takes the runnables.
     * Maybe someone finds a synchronization- concept that is more efficient and
     * informs me?
     */
    this.pendingChanges.add(e);
    /*
     * Old working code befor use of Painter Thread.
     * SwingUtilities.invokeLater(new Runnable() { public void run() {
     * synchronized (Chart2D.this) { adjustRangeX(); adjustRangeY(); double
     * xnew, ynew; boolean x = false, y = false; // ITrace2D
     * trace=(ITrace2D)e.getSource(); TracePoint2D changed = e.getPoint();
     * double scalerX = xmax - xmin; double scalerY = ymax - ymin; xnew =
     * (changed.getX() - xmin) / scalerX; ynew = (changed.getY() - ymin) /
     * scalerY; if (ynew == Double.NaN) xnew = 0; if (ynew == Double.NaN) ynew =
     * 0; changed.scaledX = xnew; changed.scaledY = ynew;
     * 
     * if ((xmaxold != xmax) || (xminold != xmin)) { x = true; } if ((ymaxold !=
     * ymax) || (yminold != ymin)) { y = true; } if ((DEBUG_LEVEL & EVENT_DEBUG) !=
     * 0) { System.out.println("traceChanged(): \nmaxx = " + xmax + "\nminx = " +
     * xmin + "\nmaxy = " + ymax + "\nminy = " + ymin); if (x)
     * System.out.println("traceChanged triggers x - scaling"); if (y)
     * System.out.println("traceChanged triggers x - scaling"); } if (x || y) { //
     * rescaling for the rest? if (x && y) { scaleAll(X_Y); } else if (y) {
     * scaleAll(Y); } else { scaleAll(X); } } Graphics g =
     * Chart2D.this.getGraphics(); repaint(); //Chart2D.this.paint(g); } } });
     */
  }

  public class LabeledValue {
    String label;

    double value;

    LabeledValue(double value, String label) {
      this.value = value;
      this.label = label;
    }

    public String getLabel() {
      return this.label;
    }

    public double getValue() {
      return this.value;
    }

    public String toString() {
      return new StringBuffer().append(label).append(" : ").append(value).toString();
    }
  }

  class ColoredLabel {
    private String label;

    private Color color;

    private ColoredLabel(String label, Color color) {
      this.label = label;
      this.color = color;
    }
  }

  class Painter extends Thread {
    /**
     * Dynamically adapts to the update speed of data. Calculated in run().
     */
    long MAX_SLEEP = 1000;

    long MIN_SLEEP = 10;

    private long sleepTime = this.MIN_SLEEP;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
      try {
        int pendingSize;
        while (!this.isInterrupted()) {
          sleep(this.sleepTime);
          // Calculation of sleeptime:
          // has to be done before repaint, as paint removes
          // pending changes! But after sleep, as in that time
          // most new points will be added.
          synchronized (Chart2D.this) {
            pendingSize = Chart2D.this.pendingChanges.size();
            if (pendingSize == 0) {
              // lazy slow down:
              if (this.sleepTime < this.MAX_SLEEP) {
                this.sleepTime += 10;
              }
            } else {
              // fast speed-up:
              this.sleepTime = Math.max(this.sleepTime - (pendingSize * 10), this.MIN_SLEEP);
            }
          }
          repaint();
        }
      } catch (InterruptedException ie) {
        /*
         * This is the case, if call to interrupt() came while Thread was
         * sleeping.
         */
        if ((DEBUG_LEVEL & SYNC_DEBUG) != 0) {
          System.err.println(this.getName() + " caught an InterruptedException. Stopping paint.");
        }

      }
    }
  }

}