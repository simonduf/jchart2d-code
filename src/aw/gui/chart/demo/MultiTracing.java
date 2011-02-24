/**
 * MultiTracing, a demo testing the thread- safetiness of the Chart2D.
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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.text.*;
import aw.gui.chart.*;
import aw.util.Range;
/**
 *  An example that tests the ability of multithreaded use of a single
 *  <code>Chart2D</code>. Six different Threads are painting subsequently
 *  a single point to the chart and go to a sleep. After having painted
 *  the whole trace, each Thread sleeps for a random time between 1000
 *  and 11000 ms, removes it's trace, sleeps for another random time
 *  between 1000 and 16000 ms and starts again. <br>
 *  To be true: the data for the <code>TracePoint</code> instances is computed
 *  a single time at startup.
 *  <p>
 *  This test may blow your CPU. I am currently workin on an AMD Athlon 1200,
 *  512 MB RAM so i did not get these problems. <br>
 *  Nevertheless there are problems to see:<br>
 *  <br>
 *  <i>
 *  The ColorDistributor works awful.<br>
 *  <br>
 *  To save computations the labels are assinged to the current color of the
 *      trace. This seems to cause troubles: Differnt labels may get the same color
 *      which does not match their corresponding trace.
 *  <br>
 *  </i>
 *  <br>
 *  Please report further problems.
 *  @version 1.1
 *  @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 **/


public class MultiTracing extends JFrame {
    protected Chart2D chart = null;
    static final boolean debug = false;
    
    public MultiTracing() {
        super("MultiTracing");
        this.chart = new Chart2D();
        chart.setGridX(true);
        chart.setGridY(true);
        chart.setBackground(Color.lightGray);
        chart.setGridColor(new Color(0xDD,0xDD,0xDD));
        //WindowListener hinzufügen
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
        /*
         *   [[xa 1,..,xa n],..,[xf 1,...,xf n]]
         **/
        final double[][]data = new double[6][150];
        final java.util.Random rand = new java.util.Random();
        final long speed = 100;
        // first traces data
        // recursive entry:
        data[0][0] = rand.nextDouble()*5;
        for(int i=1;i<data[0].length;i++){
            data[0][i]= (rand.nextDouble()<0.5)? data[0][i-1]  + rand.nextDouble()*5: data[0][i-1] - rand.nextDouble()*5;
        }
        //second trace
        double tmp;
        for(int i=0;i<data[0].length;i++){
            tmp = Math.pow(Math.E,((double)i)/60);
            data[1][i] = tmp;
        }
        //third trace
        for(int i=0;i<data[0].length;i++){
            data[2][i] = Math.pow(Math.cos(((double)i)/10)*5,2);
        }
        //fourth trace: numerical integration of fist trace
        // recursive entry:
        data[3][0] = data[0][0];
        tmp=0;
        for(int i=1;i<data[0].length;i++){
            for(int j=0;j<=i;j++)
                tmp+= data[0][j];
            data[3][i] = tmp/(((double)i)+1);
            tmp =0;
        }
        // fifth trace addition of second trace and third trace
        for(int i=0;i<data[0].length;i++){
            data[4][i] = data[1][i]+data[2][i]*(0.1*-data[0][i]);
        }
        //sixth trace: addition of first and second trace
        for(int i=0;i<data[0].length;i++){
            data[5][i] = data[0][i]+data[2][i];
        }
        
        final MultiTracing wnd = new MultiTracing();
        wnd.setForceXRange(new Range(0,data[0].length+10));
        wnd.setForceYRange(getRange(data));//
        wnd.setLocation(200, 300);
        wnd.setSize(500, 300);
        wnd.setResizable(true);
        wnd.setVisible(true);
        //first Thread:
        new Thread(){
            public void run(){
                while(true){
                    ITrace2D test = new Trace2DSimple();
                    test.setColor(Color.blue);
                    test.setName(this.getName());
                    wnd.addTrace(test);
                    for(int i=0;i<data[0].length;i++){
                        if(debug)
                            System.out.println(this.getName()+" adding point to "+test.getClass().getName()+Integer.toHexString(test.hashCode()));
                        test.addPoint(i,data[0][i]);
                        try{
                            Thread.sleep(speed);
                        }catch(InterruptedException e){e.printStackTrace(System.err);}
                        
                    }
                    try{
                        Thread.sleep((long)(rand.nextDouble()*10000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                    if(debug)
                        System.out.println(this.getName()+" removing trace.");
                    wnd.removeTrace(test);
                    try{
                        Thread.sleep((long)(rand.nextDouble()*15000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                }
            }
        }.start();
        //second Thread:
        new Thread(){
            public void run(){
                while(true){
                    ITrace2D test = new Trace2DSimple();
                    test.setColor(Color.red);
                    test.setName(this.getName());
                    wnd.addTrace(test);
                    for(int i=0;i<data[0].length;i++){
                        if(debug)
                            System.out.println(this.getName()+" adding point to "+test.getClass().getName()+Integer.toHexString(test.hashCode()));
                        test.addPoint(i,data[1][i]);
                        try{
                            Thread.sleep(speed+10);
                        }catch(InterruptedException e){e.printStackTrace(System.err);}
                        
                    }
                    try{
                        Thread.sleep((long)(rand.nextDouble()*10000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                    if(debug)
                        System.out.println(this.getName()+" removing trace.");
                    wnd.removeTrace(test);
                    try{
                        Thread.sleep((long)(rand.nextDouble()*15000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                }
            }
        }.start();
        //third Thread:
        new Thread(){
            public void run(){
                while(true){
                    ITrace2D test = new Trace2DSimple();
                    test.setColor(Color.green);
                    test.setName(this.getName());
                    wnd.addTrace(test);
                    for(int i=0;i<data[0].length;i++){
                        if(debug)
                            System.out.println(this.getName()+" adding point to "+test.getClass().getName()+Integer.toHexString(test.hashCode()));
                        test.addPoint(i,data[2][i]);
                        try{
                            Thread.sleep(speed+20);
                        }catch(InterruptedException e){e.printStackTrace(System.err);}
                        
                    }
                    try{
                        Thread.sleep((long)(rand.nextDouble()*10000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                    if(debug)
                        System.out.println(this.getName()+" removing trace.");
                    wnd.removeTrace(test);
                    try{
                        Thread.sleep((long)(rand.nextDouble()*15000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                }
            }
        }.start();
        
        //fourth Thread:
        new Thread(){
            public void run(){
                while(true){
                    ITrace2D test = new Trace2DSimple();
                    test.setColor(Color.cyan);
                    test.setName(this.getName());
                    wnd.addTrace(test);
                    for(int i=0;i<data[0].length;i++){
                        if(debug)
                            System.out.println(this.getName()+" adding point to "+test.getClass().getName()+Integer.toHexString(test.hashCode()));
                        test.addPoint(i,data[3][i]);
                        try{
                            Thread.sleep(speed +30);
                        }catch(InterruptedException e){e.printStackTrace(System.err);}
                        
                    }
                    try{
                        Thread.sleep((long)(rand.nextDouble()*10000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                    if(debug)
                        System.out.println(this.getName()+" removing trace.");
                    wnd.removeTrace(test);
                    try{
                        Thread.sleep((long)(rand.nextDouble()*15000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                }
            }
        }.start();
        //fifth Thread:
        new Thread(){
            public void run(){
                while(true){
                    ITrace2D test = new Trace2DSimple();
                    test.setColor(Color.magenta);
                    test.setName(this.getName());
                    wnd.addTrace(test);
                    for(int i=0;i<data[0].length;i++){
                        if(debug)
                            System.out.println(this.getName()+" adding point to "+test.getClass().getName()+Integer.toHexString(test.hashCode()));
                        test.addPoint(i,data[4][i]);
                        try{
                            Thread.sleep(speed+40);
                        }catch(InterruptedException e){e.printStackTrace(System.err);}
                        
                    }
                    try{
                        Thread.sleep((long)(rand.nextDouble()*10000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                    if(debug)
                        System.out.println(this.getName()+" removing trace.");
                    wnd.removeTrace(test);
                    try{
                        Thread.sleep((long)(rand.nextDouble()*15000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                }
            }
        }.start();
        //sixth Thread:
        new Thread(){
            public void run(){
                while(true){
                    ITrace2D test = new Trace2DSimple();
                    test.setColor(Color.white);
                    test.setName(this.getName());
                    wnd.addTrace(test);
                    for(int i=0;i<data[0].length;i++){
                        if(debug)
                            System.out.println(this.getName()+" adding point to "+test.getClass().getName()+Integer.toHexString(test.hashCode()));
                        test.addPoint(i,data[5][i]);
                        try{
                            Thread.sleep(speed+50);
                        }catch(InterruptedException e){e.printStackTrace(System.err);}
                        
                    }
                    try{
                        Thread.sleep((long)(rand.nextDouble()*10000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                    if(debug)
                        System.out.println(this.getName()+" removing trace.");
                    wnd.removeTrace(test);
                    try{
                        Thread.sleep((long)(rand.nextDouble()*15000+1000));
                    }catch(InterruptedException e){e.printStackTrace(System.err);}
                }
            }
        }.start();
        
    }
    // dump helper: 
    private static Range getRange(double[][]data){
      double min = Double.MAX_VALUE;
      double max = Double.MIN_VALUE;
      double tmp;
      for(int i=data.length-1;i>=0;i--){
        for(int j=data[i].length-1;j>=0;j--){
          tmp = data[i][j];
          if(tmp>max){
            max = tmp;
          }
          if(tmp< min){
            min = tmp;
          }
        }
      }
      System.out.println("MIN Y : "+min);
      System.out.println("MAX Y : "+max);
      
      return new Range(min,max);
    }
  /**
   * 
   */
  public void setForceXRange(Range forceXRange) {
    chart.setForceXRange(forceXRange);
  }
  /**
   * 
   */
  public void setForceYRange(Range forceYRange) {
    chart.setForceYRange(forceYRange);
  }
}
