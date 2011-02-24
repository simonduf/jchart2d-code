/**
 * RunningChart, a test for the Chart2D.
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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.Trace2DLtd;
import aw.reflection.ObjRecorder2Trace2DAdapter;
import aw.util.Range;
/**
 *
 *  A test for the <code>Chart2D</code> that constantly adds new tracepoints
 *  to a <code> Trace2DLtd</code>. Mainly the runtime- scaling is interesting.<br>
 *  <br>
 *  Furthermore this is an example on how to connect other components to the
 *  <code>Chart2D</code> using an adaptor- class. It is "hidden" in the package
 *  <i>aw.reflection</i> and called <code>ObjRecorder2Trace2DAdaptor</code> (5
 *  letters under the limit!).
 *
 * @author  <a href='mailto:Achim.Westermann@gmx.de'> Achim Westermann</a>
 * @version 1.1
 */
public class RunningChart extends JFrame {
    protected Chart2D chart = null;
    
    public RunningChart(Chart2D chart, String Label) {
        super(Label);
        this.chart = chart;
        addWindowListener(
        new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        }
        );
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this.chart,BorderLayout.CENTER);
    }
    
    public void addTrace(ITrace2D data){
        this.chart.addTrace(data);
    }
    public void removeTrace(ITrace2D data){
        this.chart.removeTrace(data);
    }
    
    public static void main(String[] args){
        System.out.println("10e3: "+10e3);
        Chart2D chart = new Chart2D();
        ITrace2D data = new Trace2DLtd(300);
        data.setColor(Color.RED);
        data.setName("random");
        data.setPhysicalUnits("hertz","ms");
        chart.addTrace(data);
        RunningChart wnd = new RunningChart(chart,"RunningChart");
        chart.setScaleX(true);
        chart.setGridX(true);
        chart.setDecimalsX(0);
        chart.setScaleY(true);
        chart.setGridY(true);
        // force ranges:
        chart.setForceYRange(new Range(-1e4,+1e4));
        //chart.setFont(new Font(null,0,12));
        wnd.setLocation(200, 300);
        wnd.setSize(700, 210);
        wnd.setResizable(true);
        wnd.setVisible(true);
        ObjRecorder2Trace2DAdapter adapter =
        new ObjRecorder2Trace2DAdapter(data,new RandomBumper(0.5,1000),"number",40);
    }
    
    static class RandomBumper extends Thread{
        protected double number=0;
        protected java.util.Random randomizer = new java.util.Random();
        protected int plus =1;
        protected double plusminus = 0.5;
        protected double factor;
        
        public RandomBumper(double plusminus,int factor){
            if(plusminus<0 || plusminus>1)
                System.out.println(this.getClass().getName()+" ignores constructor-passed value. Must be between 0.0 and 1.0!");
            else this.plusminus=plusminus;
            this.factor = factor;
            this.start();
        }
        
        public void run(){
            while(true){
                double rand = this.randomizer.nextDouble();
                if(rand<this.plusminus){
                    this.number+=this.randomizer.nextDouble()*this.factor;
                }
                else{
                    this.number-=this.randomizer.nextDouble()*this.factor;
                }
                
                try{
                    sleep(40);
                }catch(InterruptedException e){}
                
                
            }
        }
    }
    
}
