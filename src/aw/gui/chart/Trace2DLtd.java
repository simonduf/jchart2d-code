/**
 * Trace2DLtd, a RingBuffer- based fast implementation of a ITrace2D.
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

import java.util.*;
import java.awt.Color;
import javax.swing.event.*;
import aw.util.collections.IRingBuffer;
import aw.util.collections.RingBufferArrayFast;

/**
 * Additional to the Trace2DSimple the Trace2DLimited adds the following
 * functionality: <br>
 * <ul>
 * <li>The amount of internal tracepoints is limited to the maxsize, passed to
 * the constructor.</li>
 * <li>If a new tracepoint is inserted and the maxsize has been reached, the
 * tracepoint residing for the longest time in this trace is thrown away.</li>
 * </UL>
 * Take this implementation to display frequently changing data (nonstatic, time -
 * dependant values). You will avoid a huge growing amount of tracepoints that
 * would increase the time for scaling and painting until system hangs or
 * java.lang.OutOfMemoryError is thrown.
 * 
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 * @version 1.0
 */
public class Trace2DLtd implements ITrace2D {
  public final static boolean debug = true;

  protected IRingBuffer buffer;

  protected Color color;

  protected List changeListeners = new LinkedList();

  protected String label = "";

  double maxX;

  double minX;

  double maxY;

  double minY;

  private boolean firsttime = true;

  protected String name = "";

  protected String physunit = "";

  protected Chart2D renderer;

  /**
   * Constructs a <code>Trace2DLtd</code> with a default buffersize of 100.
   */
  public Trace2DLtd() {
    this(100);
  }

  /**
   * Constructs a <code>Trace2DLtd</code> with a buffersize of maxsize.
   */
  public Trace2DLtd(int maxsize) {
    this.buffer = new RingBufferArrayFast(maxsize);
  }

  public void addPoint(TracePoint2D p) {
    //            if(p==null)System.out.println("addPoint(): null was added!");
    synchronized (this.renderer) {
      synchronized (this) {

        if (firsttime) {
          this.maxX = p.getX();
          this.minX = this.maxX;
          this.maxY = p.getY();
          this.minY = this.maxY;
          firsttime = false;
          this.buffer.add(p);
        } else {
          TracePoint2D removed = (TracePoint2D) this.buffer.add(p);
          double tmpx, tmpy;
          if (removed != null) {
            tmpx = removed.getX();
            tmpy = removed.getY();
            //System.out.println("Trace2DLtd.addPoint() removed point!");
            if (tmpx >= this.maxX) {
              this.maxXSearch();
            } else if (tmpx <= minX) {
              this.minXSearch();
            }
            if (tmpy >= this.maxY) {
              this.maxYSearch();
            } else if (tmpy <= this.minY) {
              this.minYSearch();
            }
          }
          tmpx = p.getX();
          tmpy = p.getY();
          if (tmpx > this.maxX) {
            this.maxX = tmpx;
          } else if (tmpx < this.minX) {
            this.minX = tmpx;
          }
          if (tmpy > this.maxY) {
            this.maxY = tmpy;
          } else if (tmpy < this.minY) {
            this.minY = tmpy;
          }
        }
        this.fireTraceChanged(p);
      }
    }
  }

  /**
   * Adds a tracepoint to the internal data. <br>
   * 
   * @see #addPoint(TracePoint2D p)
   */
  public void addPoint(double x, double y) {
    this.addPoint(new TracePoint2D(this, x, y));
  }

  public Iterator iterator() {
    return this.buffer.iteratorF2L();
  }

  /**
   * Fires a <code>IChart2DData.Chart2DDataChangeEvent</code> to all
   * listeners.
   */
  public void fireTraceChanged(TracePoint2D changed) {
    Trace2DChangeEvent fire = new Trace2DChangeEvent(this, changed);
    synchronized (this.buffer) {
      Iterator it = this.changeListeners.iterator();
      while (it.hasNext()) {
        ((ITrace2D.Trace2DListener) it.next()).traceChanged(fire);
      }
    }
  }

  /**
   * Because the color is data common to a trace of a <code>Chart2D</code> it
   * is stored here. <br>
   * On the other hand only the corresponding <code>Chart2D </code> may detect
   * the same color chosen for different <code>IChart2D</code> instances to be
   * displayed. Therefore it is allowed to return null. This is a message to the
   * <code>Chart2D</code> to leave it the choice of the color. Then the
   * <code>Chart2D</code> will chose a color not owned by another
   * <code>IChart2DData</code> instance managed and assign it to the null-
   * returning instance.
   * 
   * @return The chosen java.awt.Color or null if the decision for the color
   *         should be made by the corresponding <code>Chart2D</code>.
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * @return null or a label to be displayed for this trace.
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Method invoked by <code>TracePoint2D</code> to notify instances of the
   * change.
   */
  public void pointChanged(TracePoint2D d) {
    fireTraceChanged(d);
  }

  /**
   * Assings a <code>java.awt.Color</code> to this trace.
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Assings the optional label to be displayed for this trace.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Returns the minimum value to be displayed on the y- axis of the Chart2D.
   * Implementations should be synchronized for multithreaded use. No exception
   * is thrown. In case of empty data (no tracepoints) 0 should be returned.
   * (watch division with zero).
   * 
   * @return the minimum value of the internal data for the y- dimension or 0 if
   *         no tracepoint contained.
   */
  public double getMinY() {
    return this.minY;
  }

  private void minYSearch() {
    synchronized (this) {
      double ret = Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.buffer.iteratorF2L();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getY()) < ret)
          ret = tmp;
      }
      if (ret == Double.MAX_VALUE) {
        this.minY = 0d;

      } else
        this.minY = ret;
    }
  }

  /**
   * Returns the maximum value to be displayed on the y- axis of the Chart2D.
   * Implementations should be synchronized for multithreaded use. No exception
   * is thrown. In case of empty data (no tracepoints) 0 should be returned.
   * (watch division with zero).
   * 
   * @return the maximum value of the internal data for the y- dimension.
   */
  public double getMaxY() {
    return this.maxY;
  }

  private void maxYSearch() {
    synchronized (this) {
      double ret = -Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.buffer.iteratorF2L();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getY()) > ret)
          ret = tmp;
      }
      if (ret == -Double.MAX_VALUE)
        this.maxY = 0d;
      else
        this.maxY = ret;
    }
  }

  /**
   * Returns the minimum value to be displayed on the x- axis of the Chart2D.
   * Implementations should be synchronized for multithreaded use. No exception
   * is thrown. In case of empty data (no tracepoints) 0 should be returned.
   * (watch division with zero).
   * 
   * @return the minimum value of the internal data for the x- dimension.
   */
  public double getMinX() {
    return this.minX;
  }

  private void minXSearch() {
    synchronized (this) {
      double ret = Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.buffer.iteratorF2L();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getX()) < ret)
          ret = tmp;
      }
      if (ret == Double.MAX_VALUE) {
        this.minX = 0d;
      } else
        this.minX = ret;
    }
  }

  /**
   * Returns the maximum value to be displayed on the x- axis of the Chart2D.
   * Implementations should be synchronized for multithreaded use. No exception
   * is thrown. In case of empty data (no tracepoints) 0 should be returned.
   * (watch division with zero).
   * 
   * @return the maximum value of the internal data for the x- dimension.
   */
  public double getMaxX() {
    return this.maxX;
  }

  private void maxXSearch() {
    synchronized (this) {
      double ret = -Double.MAX_VALUE;
      TracePoint2D tmpoint = null;
      double tmp;
      Iterator it = this.buffer.iteratorF2L();
      while (it.hasNext()) {
        tmpoint = (TracePoint2D) it.next();
        if ((tmp = tmpoint.getX()) > ret)
          ret = tmp;
      }
      if (ret == -Double.MAX_VALUE)
        this.maxX = 0d;
      else
        this.maxX = ret;
    }
  }

  /**
   * Tell wether no tracepoints are avaiable.
   */
  public boolean isEmpty() {
    return this.buffer.isEmpty();
  }

  public void addChangeListener(ITrace2D.Trace2DListener x) {
    changeListeners.add(x);
    x.traceChanged(new Trace2DChangeEvent(this, ALL_POINTS_CHANGED)); // Aufruf
    // des
    // neuen
    // ChangeListeners
    // um zu
    // aktualisieren.
  }

  public void removeChangeListener(ITrace2D.Trace2DListener x) {
    changeListeners.remove(x);
  }

  /*
   * prooves that synchronizing from outside is necessary for multithreaded use.
   */
  public static void main(String[] args) {
    try {
      final Trace2DLtd test = new Trace2DLtd(10);
      for (int i = 0; i < 10; i++) {
        test.addPoint(i, i);
      }
      System.out.println("Iterator- test: ");

      Iterator it = test.iterator();
      new Thread() {
        public void run() {
          while (true) {
            System.out.println(this.getName() + " removed: " + test.buffer.remove());
            try {
              Thread.sleep(100);
            } catch (InterruptedException e) {
            }
          }
        }
      }.start();
      while (it.hasNext()) {
        Thread.sleep(200);
        System.out.println(it.next());
      }
      System.out.println("buffer.getYoungest(): " + test.buffer.getYoungest());
      System.out.println("buffer.getOldest(): " + test.buffer.getOldest());

    } catch (Throwable f) {
      f.printStackTrace();
    }
  }

  /**
   * @see #setName(String s)
   */
  public String getName() {
    return this.name;
  }

  /**
   * @see #setPhysicalUnits(String x,String y)
   */
  public String getPhysicalUnits() {
    return this.physunit;
  }

  /**
   * Assingns a specific name to the <code>ITrace2D</code> which will be
   * displayed by the <code <Chart2D</code>
   */
  public void setName(String s) {
    this.name = s;
  }

  public void setPhysicalUnits(String xunit, String yunit) {
    if ((xunit == null) && (yunit == null))
      return;
    if ((xunit == null) && (yunit != null)) {
      this.physunit = new StringBuffer("[x: , y: ").append(yunit).append("]").toString();
      return;
    }
    if ((xunit != null) && (yunit == null)) {
      this.physunit = new StringBuffer("[x: ").append(xunit).append(", y: ]").toString();
      return;
    }
    this.physunit = new StringBuffer("[x: ").append(xunit).append(", y: ").append(yunit).append("]").toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see aw.gui.chart.ITrace2D#getRenderer()
   */
  public Chart2D getRenderer() {
    return this.renderer;
  }

  /*
   * (non-Javadoc)
   * 
   * @see aw.gui.chart.ITrace2D#setRenderer(aw.gui.chart.Chart2D)
   */
  public void setRenderer(Chart2D renderer) {
    this.renderer = renderer;

  }
}