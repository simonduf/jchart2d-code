/**
 * TracePoint2D, a tuned Point2D.Double for use with ITrace2D- implementations.
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
import java.awt.geom.Point2D;
/**
 *  A specialized version of <code>java.awt.Point2D.Double </code>who carries two further
 *  values: <code> double scaledX</code> and <code>double scaledY</code> which
 *  allow the <code>Chart2D</code> to cache the scaled values (between 0.0 and 1.0)
 *  without having to keep a copy of the aggregators (<code>ITrace2D</code>)
 *  complete tracepoints. This avoids the necessarity to care for the correct
 *  order of a set of scaled tracepoints copied for caching purposes. Especially
 *  in the case of new <code>TracePoint2D</code> instances added to a
 *  <code>ITrace2D</code> instance managed by a <code>Chart2D</code> there
 *  remains no responsibility for sorting the cached copy. This allows that the
 *  managing <code>Chart2D</code> may just rescale the newly added tracepoint
 *  instead of searching for the correct order of the new tracepoint by value -
 *  comparisons of x and y: The <code>TracePoint2D</code> passed to the method
 *  <code>traceChanged(Chart2DDataChangeEvent e)<code> coded in the argument is
 *  the original. <br>
 *  <p>
 *  Why caching of scaled values for the coordinates?<br>
 *  This takes more RAM but else for every <code>repaint()</code> invocation of
 *  the <code>Chart2D</code> would force all tracepoints of all traces to be
 *  rescaled again.
 *  </p>
 *  <p>
 *   A TracePoint2D will inform it's listener of type <code>ITrace</code>
 *  on changes of the internal values.
 *  </p>
 * @author  Achim Westermann <a href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de</A>
 * @version 1.1
 */
public class TracePoint2D extends Point2D.Double implements Comparable,java.io.Serializable {
    /**
     * The reference to the listening <code>ITrace</code> who owns this point.
     *
     * A TracePoint2D.Double should be contained only in one trace!
     **/
    private ITrace2D listener;
    
    /**
     *  Accessible at package-level for the <code>Chart2D</code>.
     **/
    double scaledX;
    /**
     *  Accessible at package-level for the <code>Chart2D</code>.
     **/
    double scaledY;
    /** 
     * Flag for the Chart2D painter that allows it to render only 
     * instances he has processed before.
     **/
    boolean scaledOnce = false;

    /** 
     *  Allows <code>ITrace2D</code> to construct <code>ALL_POINTS_CHANGED</code>
     *  to call the no- arg cons only one time. 
     **/
    private static boolean inited;
    /**
     *  This constructor is reserved for <code>ITrace2D</code> for construction 
     *  of <code>ALL_POINTS_CHANGED</code>!
     *  @exception IllegalAccessException is thrown if anyone except the loading 
     *  of interface <code>ITrace2D</code> triggers this constructor. Needed a 
     *  RuntimeException even if this one is not meant for this purpose it works.
     **/
    TracePoint2D()throws SecurityException{
        if(inited == true)
            throw new SecurityException("This constructor is meant for ITrace2D only!");
        else
            inited = true;
    }
    /**
     *  Construct a TracePoint2D whose coords are initalized to (0.0,0.0).
     *  Modifications of this instances by <code>setLocations(double x,double y)</code>
     *  will cause of invocation of
     * <code>pointChanged(Point2D.Double e)</code>  on the instance this
     *  tracepoint belongs to.
     **/
    public TracePoint2D(ITrace2D listener){
        super();
        this.listener = listener;
    }
    /**
     *  Construct a TracePoint2D whose coords are initalized to (x,y).
     *  Modifications of this instances by <code>setLocations(double x,double y)</code>
     *  will cause of invocation of
     * <code>pointChanged(Point2D.Double e)</code>  on the instance this
     *  tracepoint belongs to.
     **/
    public TracePoint2D(ITrace2D listener,double x,double y){
        super(x,y);
        this.listener = listener;
    }
    
    /**
     *  This method overloads the method of <code>java.awt.geom.Point2D.Double</code> 
     *  to inform the listening <code>ITrace</code>
     **/
    public void setLocation(double x,double y){
        super.setLocation(x,y);
        if(this.listener!= null){
            this.listener.pointChanged(this);
        }
    }
    
    
    public int compareTo(java.lang.Object obj)throws ClassCastException {
        double othx = ((Point2D.Double)obj).getX(); //ClassCastException
        if(this.x<othx)return -1;
        if(this.x==othx)return 0;
        else return 1;
    }
    
    public boolean equals(Object o){
        return compareTo(o)==0;
    }
    
}
