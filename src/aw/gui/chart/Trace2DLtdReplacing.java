/**
 * Trace2DLtdReplcacing, an array- based fast implementation of an ITrace2D 
 * that only allows a single tracepoint with a certain x- value.
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
import java.util.Iterator;

/**
 *  In addition to the <code> Trace2DLtd</code> this class offers the guarantee 
 *  only to allow a single tracepoint with a certain x- value. If a new 
 *  tracepoint is added whose x- value is already contained, the new tracepoints 
 *  values will get assigned to the certain old tracepoint respecting the fact 
 *  that only an additional changed y- value occurs. 
 *  <br>
 *  The <code>add</code> methods increase complexity to factor n but some event - 
 *  handling may be saved (no change of x and y).
 * <br>
 *  Tracepoints with x- values not contained before will be appended to the end 
 *  of the internal data- structure. <br>
 * @author  Achim Westermann <a href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de</A>
 * @version 
 */
public class Trace2DLtdReplacing extends Trace2DLtd {
    /**
     *  Constructs a <code>Trace2DLtdReplacing</code> with a default buffer size of 
     *  100.
     */  
    public Trace2DLtdReplacing(){
        this(100);
    }
    /**
     *  Constructs a <code>Trace2DLtdReplacing</code> with a buffer size of 
     *  bufsize.
     */  
    public Trace2DLtdReplacing(int bufsize){
        super(bufsize);
    }
        
    public void addPoint(TracePoint2D p)
    {
        TracePoint2D tmp;
        double tmpx,tmpy;
        synchronized(this.renderer){
          synchronized(this){
            Iterator it = this.buffer.iteratorF2L();
            while(it.hasNext()){
                tmp = (TracePoint2D)it.next();
                if((tmpx=tmp.getX())==p.getX()){
                    if((tmpy=p.getY())==tmp.getY()){
                        tmp.setLocation(tmpx,tmpy);
                        this.fireTraceChanged(tmp);
                        return;
                    }
                }
            }
          }
            // using the complicated min/max- routines
            super.addPoint(p);
        }
    }    
}
