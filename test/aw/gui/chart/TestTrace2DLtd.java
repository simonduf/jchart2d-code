/*
 *
 *  TestTrace2DLtd.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 23.04.2005, 08:21:12
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package aw.gui.chart;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 */
public class TestTrace2DLtd extends TestCase {

  public void testMemoryLeak(){
    int traceSize = 10;
    ITrace2D trace = new Trace2DLtd(traceSize);
    long max = 1000000;
    long percentModulo = max/20;
    System.out.println("Adding "+max+" points to a Trace2DLtd and a WeakHashMap...");
    TracePoint2D point;
    Map weakMap = new WeakHashMap();
    for(long i=0;i<max;i++){
      point = new TracePoint2D(i,i);
      trace.addPoint(point);
      weakMap.put(point,point.toString());
      if(i%percentModulo == 0){
        System.out.println((i*100/max) +" %");
      }
    }
    System.out.println("Dropping point reference (but holding trace)...");
    point = null;
    //trace=null;
    long keys = weakMap.size();
    System.out.println("Points remaining in the weakMap: "+keys);
    System.out.println("System.runFinalization()... ");
    System.runFinalization();
    System.out.println("System.gc()... ");
    System.gc();
    keys = 0;
    Iterator it = weakMap.keySet().iterator();
    while(it.hasNext()){
      keys++;
      System.out.println("Point "+it.next().toString()+" was not dropped.");
    }
    System.out.println("Points remaining in the weakMap: "+keys);
    assertFalse("There are "+keys+ " TracePoint2D instances not deleted from the WeakHashMap.",keys > traceSize);
  }
}
