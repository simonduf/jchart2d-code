/**
 * ITrace2D, the interface for all traces used by the Chart2D.
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
import java.util.List;

/**
 *  An interface used by <code>Chart2D</code>.
 *  ITrace2D contains the values to display, the color for displaying and
 *  a suitable lable. It may be seen as a trace of the <code>Chart2D</code>
 *  that displays it. <br>
 *  Implementations may be optimized for different use- cases: <br>
 *  RingBuffers for fast changing data to keep the amount of tracepoints and
 *  consumption of memory constant,
 *  internal Lists for allowing the sorting the internal <code>TracePoint2D</code>- instances
 *  or Arrays for unordered Data (in the order of adding) for fast performance.
 *  Even an <code>ITrace2D</code> constructed by a "function- Object" may be
 *  thinkable.
 *  <p>
 *  There are various constraints for Traces:<br>
 *  - ordered by x-values<br>
 *  - ordered by the order of <code>addPoint</code> - invocation (control form outside)<br>
 *  - unique, single x- values <br>
 *  - limitation of tracepoints <br>
 *  - time- critical (fast- changing tracepoints) <br>
 *   <br>
 *  Therefore there are various <code>ITrace2D</code> - implementations. Read
 *  their description to find the one you need. Some may not have been written yet.
 *  </p>
 *
 * @author  Achim Westermann <a href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de</A>
 * @version 1.1
 */
public interface ITrace2D  {
    /**
     *  A static reference to a marker that sais to all listening
     *  <code>Chart2DDataListener</code> all points have changed.
     *  Invoke <cod>aChart2DDataListener.traceChanged(new Chart2DDataChangeEvent(this,Trace2D.ALL_POINTS_CHANGED))</code>
     *  on the listener. He will have the possiblity to recognize that all Points
     *  have changed in it' method <code> traceChanged(Chart2DDataEvent e)</code> by testing:
     *  <code> ... if(e.getPoint()==ITrace2D.ALL_POINTS_CHANGED){....</code>
     **/
    public static TracePoint2D ALL_POINTS_CHANGED = new TracePoint2D();
   
    /**
     *  Assingns a specific name to the <code>ITrace2D</code> which will be 
     *  displayed by the <code<Chart2D</code>
     **/
    public void setName(String s);
    /**
     *  @see #setName(String s)
     **/
    public String getName();
    /*
     *  Assings a specific String representing the physical unit to the <code> 
     *  ITrace2D</code> (e.g. Volt, Ohm, lux, ...) which will be displayed by the 
     *  <code>Chart2D</code>
     **/
    public void setPhysicalUnits(String xunit,String yunit);
    /**
     *  @see #setPhysicalUnits(String x,String y)
     **/
    public String getPhysicalUnits();
    /**
     *  Adds the given <code> TracePoint2D </code> to the internal data.
     *  Try to pass instances of <code>TracePoint2D</code> to increase performace.
     *  Else the given point has to be copied into such an instance.
     **/
    void addPoint(TracePoint2D p);
    /**
     *  Adds a tracepoint to the internal data.<br>
     *  @see #addPoint(TracePoint2D p)
     **/
    void addPoint(double x,double y);
    
    /**
     *  Returns the maximum value to be displayed on the x- axis of the Chart2D.
     *  Implementations should be synchronized for multithreaded use.
     *  No exception is thrown. In case of empty data (no tracepoints) 0 should
     *  be returned, to let the Chart2D know.
     *  <p>
     *  The <code>Chart2D </code> will often call this method. So try to cache 
     *  the value in implementation and only check on modifications of <code>TracePoint</code>
     *   instances or on <code>add</code>- invocations for changes.
     *  </p>
     *  @return the maximum value of the internal data for the x- dimension.
     **/
    double getMaxX();
    /**
     *  Returns the maximum value to be displayed on the y- axis of the Chart2D.
     *  Implementations should be synchronized for multithreaded use.
     *  No exception is thrown. In case of empty data (no tracepoints) 0 should
     *  be returned. (watch division with zero).
     *  @return the maximum value of the internal data for the y- dimension.
     **/
    double getMaxY();
    /**
     *  Returns the minimum value to be displayed on the x- axis of the Chart2D.
     *  Implementations should be synchronized for multithreaded use.
     *  No exception is thrown. In case of empty data (no tracepoints) 0 should
     *  be returned. (watch division with zero).
     *  <p>
     *  The <code>Chart2D </code> will often call this method. So try to cache 
     *  the value in implementation and only check on modifications of <code>TracePoint</code>
     *   instances or on <code>add</code>- invocations for changes.
     *  </p>
     *  @return the minimum value of the internal data for the x- dimension.
     **/
    
    double getMinX();
    /**
     *  Returns the minimum value to be displayed on the y- axis of the Chart2D.
     *  Implementations should be synchronized for multithreaded use.
     *  No exception is thrown. In case of empty data (no tracepoints) 0 should
     *  be returned. (watch division with zero).
     *  <p>
     *  The <code>Chart2D </code> will often call this method. So try to cache 
     *  the value in implementation and only check on modifications of <code>TracePoint</code>
     *   instances or on <code>add</code>- invocations for changes.
     *  </p>
     *  @return the minimum value of the internal data for the y- dimension.
     **/
    
    double getMinY();
    
    /**
     *  Because the color is data common to a trace of a <code>Chart2D</code>
     *  it is stored here. <br>
     *  On the other hand only the corresponding <code>Chart2D </code>may detect the same
     *  color chosen for different <code>IChart2D</code> instances to be displayed.
     *  Therefore it is allowed to return null. This is a message to the <code>Chart2D</code>
     *  to leave it the choice of the color. Then the <code>Chart2D</code> will chose
     *  a color not owned by another <code>ITrace2D</code> instance managed and
     *  assign it to the null- returning instance.
     *  <p>
     *  The <code>Chart2D </code> will often call this method. So try to cache 
     *  the value in implementation and only check on modifications of <code>TracePoint</code>
     *   instances or on <code>add</code>- invocations for changes.
     *  </p>
     *  @return The chosen java.awt.Color or null if the decision for the color should
     *  be made by the corresponding <code>Chart2D</code>.
     **/
    Color getColor();
    /**
     *  Assings a <code>java.awt.Color</code> to this trace.
     **/
    void setColor(Color color);
    /**
     *  Returns an Iterator over the internal <code>TracePoint2D</code> instances.
     *  Implementations should be synchronized.
     *  This method is meant to allow modifications of the intenal TracePoint2Ds,
     *  so the original Points should be returned.
     *  There is no guarantee that changes made to the contained tracepoints will be
     *  reflected in the display immediately. If you insist on this use the
     *  class <code> TracePoint2D</code>  in your implementations.
     *  Objects of this type will invoke <code>pointChanged(TracePoint2D e)</code>
     *  on the <code>ITrace2D</code> instance they belong to when they are modified.
     *  The last thing to do: cascade the call to all listeners (<code>Chart2D</code> -instances)
     *  registered here. <br>
     *  <br>
     *   The order the iterator returns the <code>TracePoint2D</code> instances
     *  decides how the <code>Chart2D</code> will paint the trace.
     **/
    java.util.Iterator iterator();
    /**
     *  Assings the optional label to be displayed for this trace.
     **/
    void setLabel(String label);
    /**
     *  @return null or a label to be displayed for this trace.
     **/
    String getLabel();
    /**
     *  Tell wether no tracepoints are avaiable.
     **/
    boolean isEmpty();
    /**
     *  Fires a <code>ITrace2D.Trace2DChangeEvent</code> to all
     *  listeners.
     **/
    void fireTraceChanged(TracePoint2D changed);
    /**
     *  Method invoked by <code>TracePoint2D</code> to notify
     *  instances of the change.
     **/
    void pointChanged(TracePoint2D d);
    
    public void addChangeListener(ITrace2D.Trace2DListener x);
    
    public void removeChangeListener(ITrace2D.Trace2DListener x);
    /**
     * @return Returns the renderer.
     */
    public Chart2D getRenderer();
    /**
     * This is a callback from {@link Chart2D#addTrace(ITrace2D)} 
     * and must not be invoked from elswhere (needed for synchronization).
     * Not the best design to put this to an interface, but Char2D 
     * should handle this interface only. 
     * 
     * @param renderer The renderer to set.
     */
    void setRenderer(Chart2D renderer);
    
    /**
     *  Specialized ChangeEvent which gives further information about the
     *  <code>TracePoint2D</code> that is changed. This may be an old
     *  tracepoint or a newly added one.
     **/
    class Trace2DChangeEvent extends javax.swing.event.ChangeEvent{
        private TracePoint2D point;
        Trace2DChangeEvent(ITrace2D source,TracePoint2D d){
            super(source);
            this.point = d;
        }
        
        public TracePoint2D getPoint(){
            return point;
        }
    }
    /**
     *  Interface for classes interested in ChangeEvents of <code>ITrace2D</code>
     *  instances. The events fired to the listeners are specialized: They know
     *  not only the source (<code>ITrace2D</code>) but the <code>TracePoint2D</code>
     *  responsible for the change.
     *  <br>
     *  Defined here to obtain a save namespace.
     *  @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
     **/
    public static interface Trace2DListener{
        void traceChanged(Trace2DChangeEvent e);
    }
}

