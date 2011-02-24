/**
 * TraceTester, a test for the implementations of ITrace2D - implementations.
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

package aw.gui.chart.demo;
import aw.gui.chart.*;
import javax.swing.*;
import java.awt.event.*;

/**
 *  A testclass that steps through all <code>ITrace2D</code> implementations and 
 *  adds random or "half- random" <code>TracePoint2D</code> -instances. 
 *  <br>
 *  You may see, that not all <code>ITrace2D</code> implementations work as 
 *  proposed (Trace2DLimitedReplacing).
 *  This will be fixed. 
 *
 * @author  Achim Westermann <a href='mailto:Achim.Westermann@gmx.de'>Achim.Westermann@gmx.de</A>
 * @version 0.5
 */
public class TraceTester {
    
    /** Creates new TraceTester */
    public TraceTester() {
    }
    
    public static void main(String[]args){
        try{
            Class[] traces = new Class[]{
                Trace2DSimple.class,
                Trace2DBijective.class,
                Trace2DReplacing.class,
                Trace2DSorted.class,
                Trace2DLtd.class,
                Trace2DLtdReplacing.class,
                Trace2DLtdSorted.class
            };
            RandomPoints rand = new RandomPoints(0,3,0,3);
            
            Chart2D test = new Chart2D();
            test.setDecimalsX(4);
            test.setDecimalsY(4);
            JFrame frame = new JFrame("TraceTester");
            frame. addWindowListener(
            new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    System.exit(0);
                }
            }
            );
            
            frame.getContentPane().add(test);
            frame.setSize(600,500);
            frame.setLocation(200,200);
            frame.setVisible(true);
            ITrace2D current = null;
            for(int i=0;i<traces.length;i++){
                current = (ITrace2D)traces[i].newInstance();
                test.addTrace(current);
                frame.setTitle("TraceTester: full-random, current: "+traces[i].getName());
                
                for(int j=0;j<200;j++){
                    current.addPoint(rand.nextPoint(current));
                    Thread.sleep(50);
                }
                Thread.sleep(2000);
                test.removeTrace(current);
            }
            rand = new HalfRandomPoints(0,3,0,3);
            for(int i=0;i<traces.length;i++){
                current = (ITrace2D)traces[i].newInstance();
                test.addTrace(current);
                frame.setTitle("TraceTester: repeating x 10 times, current: "+traces[i].getName());
                
                for(int j=0;j<200;j++){
                    current.addPoint(rand.nextPoint(current));
                    Thread.sleep(50);
                }
                Thread.sleep(2000);
                test.removeTrace(current);
            }
            
                
            System.exit(0);
            
        }catch(Throwable f){
            f.printStackTrace();
            System.exit(0);
        }
    }
    
    
    static class RandomPoints{
        java.util.Random rand = new java.util.Random();
        double xmin,xrange,ymin,yrange;
        RandomPoints(int minx,int maxx,int miny,int maxy){
            if(minx>=maxx)throw new IllegalArgumentException("minx>=maxx!");
            if(miny>=maxy)throw new IllegalArgumentException("miny>=maxy!");
            this.xmin = minx;
            this.xrange = maxx-minx;
            this.ymin = miny;
            this.yrange = maxy-miny;
        }
        
        public TracePoint2D nextPoint(ITrace2D listener){
            return new TracePoint2D(
            listener,
            rand.nextDouble()*xrange+xmin,
            rand.nextDouble()*yrange+ymin
            );
        }
        
        
    }
    
    static class HalfRandomPoints extends RandomPoints{
        double samexcount =0;
        double oldx = 0;
        HalfRandomPoints(int minx,int maxx,int miny,int maxy){
            super(minx,maxx,miny,maxy);
            oldx = (maxx-minx)/2;
        }
        
        public TracePoint2D nextPoint(ITrace2D listener){
            if(samexcount ==10){
                samexcount = 0;
                oldx = rand.nextDouble()*xrange+xmin;
            }
            samexcount++;
            return new TracePoint2D(
            listener,
            oldx,
            rand.nextDouble()*yrange+ymin
            );
        }
    }
    
}
