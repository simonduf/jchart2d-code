/**
 * Trace2DLtdSorting, a TreeSet based implementation of a ITrace2D, which
 *  has a maximum amount of TracePoints (fifo) and performs an insertion sort
 *  of the TracePoint2D- instances.
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
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;

/**
 *  Additional to the <code>Trace2DLtdReplacing</code> all tracepoints will be
 *  sorted by their x- value.<br>
 *  Performance is slower compared to the class named above. Internally a
 *  <code>TreeSet </code> is used (instead of <code>RingBufferArrayFast</code>)
 *  to keep the comparable <code>TracePoint2D</code>
 *  - instances sorted. Internally all tracepoints are <code>TracePoint2D</code>
 *  -instances.
 *
 *
 * @author  Achim Westermann <a href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de</A>
 * @version 1.0
 */
public class Trace2DLtdSorted extends Trace2DSorted{
    protected int maxsize;
    /**
     *  Constructs a <code>Trace2DLtedSorted</code> with a default buffer size of
     *  100.
     */
    public Trace2DLtdSorted() {
        this(100);
    }
    /**
     *  Constructs a <code>Trace2DLtedSorted</code> with a buffer size of
     *  maxsize.
     */
    public Trace2DLtdSorted(int maxsize) {
        this.maxsize = maxsize;
    }
    
    /**
     *  In case p has an x- value already contained, the old tracepoint with that
     *  value will be replaced by the new one.
     *  Else the new tracepoint will be added at an index in order to keep the
     *  ascending order of
     *  tracepoints with a higher x- value are contained.
     * <br>
     * If p takes additional space (it's x- value is not already contained) and
     * maxsize is reached, the first element (with lowest x- value) will be
     * removed.
     **/
    public void addPoint(TracePoint2D p) {
        boolean rem = false;
        double tmpx,tmpy;
        synchronized(this.renderer){
          synchronized(this){
        if(firsttime){
            this.maxX = p.getX();
            this.minX = this.maxX;
            this.maxY = p.getY();
            this.minY = this.maxY;
            firsttime = false;
        }
        else{
            if((tmpx=p.getX())>this.maxX)this.maxX = tmpx;
            else if(tmpx<this.minX)this.minX = tmpx;
            if((tmpy=p.getY())>this.maxY)this.maxY = tmpy;
            else if(tmpy<this.minY)this.minY = tmpy;
        }
        if(this.points.remove(p)){
            rem = true;
        }
        this.points.add(p);
        if(!rem){
            if(this.points.size()>maxsize){
                TracePoint2D removed = (TracePoint2D)this.points.last();
                this.points.remove(removed);
                
                tmpx = removed.getX();
                tmpy = removed.getY();
                //System.out.println("Trace2DLtd.addPoint() removed point!");
                if(tmpx>=this.maxX){
                    this.maxXSearch();
                }
                else if(tmpx<=minX){
                    this.minXSearch();
                }
                if(tmpy>=this.maxY){
                    this.maxYSearch();
                }
                else if(tmpy<=this.minY){
                    this.minYSearch();
                }
                
            }
            else{
                //researching for the point that was removed due to occurance of same x- value.
                tmpx = p.getX();
                //System.out.println("Trace2DLtd.addPoint() removed point!");
                if(tmpx>=this.maxX){
                    this.maxXSearch();
                }
                else if(tmpx<=minX){
                    this.minXSearch();
                }
                // hardcore as not knowable which y- value.
                this.maxYSearch();
                this.minYSearch();
            }
            
        }
        fireTraceChanged(p);
          }
        }
    }
    
    private void maxXSearch(){
        synchronized(this){
            double ret = Double.MIN_VALUE;
            TracePoint2D tmpoint = null;
            double tmp;
            Iterator it = this.points.iterator();
            while(it.hasNext()){
                tmpoint = (TracePoint2D)it.next();
                if((tmp=tmpoint.getX())>ret) ret = tmp;
            }
            if(ret == Double.MIN_VALUE)
                this.maxX = 0d;
            else
                this.maxX = ret;
        }
    }
    
    private void minXSearch(){
        synchronized(this){
            double ret = Double.MAX_VALUE;
            TracePoint2D tmpoint = null;
            double tmp;
            Iterator it = this.points.iterator();
            while(it.hasNext()){
                tmpoint = (TracePoint2D)it.next();
                if((tmp=tmpoint.getX())<ret) ret = tmp;
            }
            if(ret == Double.MAX_VALUE){
                this.minX = 0d;
            }
            else
                this.minX = ret;
        }
    }
    
    private void maxYSearch(){
        synchronized(this){
            double ret = Double.MIN_VALUE;
            TracePoint2D tmpoint = null;
            double tmp;
            Iterator it = this.points.iterator();
            while(it.hasNext()){
                tmpoint = (TracePoint2D)it.next();
                if((tmp=tmpoint.getY())>ret) ret = tmp;
            }
            if(ret == Double.MIN_VALUE)
                this.maxY = 0d;
            else
                this.maxY = ret;
        }
    }
    private void minYSearch(){
        synchronized(this){
            double ret = Double.MAX_VALUE;
            TracePoint2D tmpoint = null;
            double tmp;
            Iterator it = this.points.iterator();
            while(it.hasNext()){
                tmpoint = (TracePoint2D)it.next();
                if((tmp=tmpoint.getY())<ret) ret = tmp;
            }
            if(ret == Double.MAX_VALUE){
                this.minY = 0d;
                
            }
            else
                this.minY = ret;
        }
    }
    
    
    public void addPoint(double x,double y) {
        TracePoint2D p= new TracePoint2D(this,x,y);
        this.addPoint(p);
    }
    
}
