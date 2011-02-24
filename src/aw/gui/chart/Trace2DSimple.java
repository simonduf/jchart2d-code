/**
 * Trace2DSimple, a list- based simple implementation of a ITrace2D.
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/**
 *  A <code>Trace2D</code> wraps the points that represent Data for a trace
 * in a two - dimensional Chart.<br>
 *  In order to help to display the given Points in a Chart2D it has the following
 *  behaviour: <br>
 *  <ul>
 *      <li>
 *          All tracepoints that are added are stored unchanged in a LinkedList.
 *      </li>
 *      <li>
 *          All traceoints that are added are added
 *          to the end.
 *      </li>
 *      <li>
 *          If a tracepoint is inserted whose x - value already exists in the List,
 *          it is ok - the old point may remain. (no bijective assigement of X and Y)
 *      </li>
 *  </UL>
 *
 *  @author <A href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 *  @version 1.1
 **/
public class Trace2DSimple implements ITrace2D {
    protected LinkedList points = new LinkedList();
    protected Color color;
    protected List changeListeners=new LinkedList();
    protected String label="";
    double maxX;
    double minX;
    double maxY;
    double minY;
    protected boolean firsttime = true;
    protected String physunit = "";
    protected String name = "";
    
    Chart2D renderer;
    
    public Trace2DSimple() {
    }
    
    public void addPoint(TracePoint2D p) {
       synchronized(this.renderer){
        synchronized(this){
            if(firsttime){
                this.maxX = p.getX();
                this.minX = this.maxX;
                this.maxY = p.getY();
                this.minY = this.maxY;
                firsttime = false;
                this.points.add(p);
            }
            else{
                
                double tmp;
                if((tmp=p.getX())>this.maxX)this.maxX = tmp;
                if(tmp<this.minX)this.minX = tmp;
                if((tmp=p.getY())>this.maxY)this.maxY = tmp;
                if(tmp<this.minY)this.minY = tmp;
                this.points.add(p);
            }
            this.fireTraceChanged(p);
        }
       }
    }
    
    
    public void addPoint(double x,double y) {
        TracePoint2D p= new TracePoint2D(this,x,y);
        this.addPoint(p);
    }
    
   
    /**
     *  Returns the original maximum x- value ignoring the offsetX.
     **/
    public double getMaxX(){
        return this.maxX;
    }
    
    /**
     *  Returns the original maximum y- value ignoring the offsetY.
     **/
    
    public double getMaxY() {
        return this.maxY;
    }
    
    /**
     *  Returns the original minimum x- value ignoring the offsetX.
     **/
    public double getMinX() {
        return this.minX;
    }
    
    /**
     *  Returns the original minimum y- value ignoring the offsetY.
     **/
    public double getMinY() {
        return this.minY;
    }
    
    
    public Color getColor(){
        return this.color;
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public Iterator iterator(){
        return this.points.iterator();
    }
    
    public void addChangeListener(ITrace2D.Trace2DListener x){
        changeListeners.add(x);
        x.traceChanged(new Trace2DChangeEvent(this,ALL_POINTS_CHANGED)); // Aufruf des neuen ChangeListeners um zu aktualisieren.
    }
    
    public void removeChangeListener(ITrace2D.Trace2DListener x){
        changeListeners.remove(x);
    }
    
    public void setLabel(String label){
        this.label = label;
    }
    public String getLabel(){
        return this.label;
    }
    
    public void fireTraceChanged(TracePoint2D changed) {
        Trace2DChangeEvent fire = new Trace2DChangeEvent(this,changed);
        synchronized(this.points){
            Iterator it = this.changeListeners.iterator();
            while(it.hasNext()){
                ((ITrace2D.Trace2DListener)it.next()).traceChanged(fire);
            }
        }
    }
    
    public void pointChanged(TracePoint2D d){
        this.fireTraceChanged(d);
    }
    
    
    /**
     * Tell wether no tracepoints are avaiable.
     */
    public boolean isEmpty() {
        return this.points.size()==0;
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
     * displayed by the <code<Chart2D</code>
     */
    public void setName(String s) {
        this.name = s;
    }
    
    public void setPhysicalUnits(String xunit, String yunit) {
        if((xunit==null)&&(yunit==null))return;
        if((xunit==null) && (yunit!=null)){
            this.physunit = new StringBuffer("[x: , y: ").append(yunit).append("]").toString();
            return;
        }
        if((xunit!=null)&&(yunit==null)){
            this.physunit = new StringBuffer("[x: ").append(xunit).append(", y: ]").toString();
            return;
        }
        this.physunit = new StringBuffer("[x: ").append(xunit).append(", y: ").append(yunit).append("]").toString();
    }
    
    /**
     * @return Returns the renderer.
     */
    public final Chart2D getRenderer() {
        return renderer;
    }
    
    public final void setRenderer(Chart2D renderer) {
        this.renderer = renderer;
    }
}
