/*
 *
 *  TestTrace2DLtd.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 23.04.2005, 08:21:12
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package aw.gui.chart;


import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 */
public class TestTrace2D extends TestCase {

  /**
   * <p>
   *  Adds and removes a tracepoint to a trace and asserts that zero
   *  listeners are in the tracepoint afterwards.
   * </p>
   *
   */
  public void testMemoryLeakTrace2PointDListeners(){
    ITrace2D trace = new Trace2DSimple();
    TracePoint2D point = new TracePoint2D(1,1);

  }

  /**
   * <p>
   * Register <code>PropertyChangeListener</code> instances on a for
   * different properties on a <code>Char2D</code>, fire property changes a
   * check for <code>PropertyChangeEvent</code> instances being fired or
   * not if they should not be fired.
   * </p>
   *
   */
  public void testPropertyChange(){
    Chart2D chart = new Chart2D();
    class PropertyChangeDetector implements PropertyChangeListener{
      private PropertyChangeEvent event = null;
      public void  propertyChange(PropertyChangeEvent evt){
        event = evt;
      }
      public PropertyChangeEvent consumeEvent(){
        PropertyChangeEvent ret = this.event;
        this.event = null;
        return ret;
      }
    }


    // test
    // font
    // trigger a "font" change
    PropertyChangeDetector fontListener = new PropertyChangeDetector();
    chart.addPropertyChangeListener("font", fontListener);
    chart.setFont(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()[0]);
    assertNotNull("setFont(Font) on Chart2D did not trigger a PropertyChange for property \"font\". ", fontListener.consumeEvent());
    // trigger a different change:
    chart.setBackground(Color.GREEN);
    assertNull("setColor(Color) on Chart2D did trigger a PropertyChange for property \"font\".", fontListener.consumeEvent());

    // addTrace
    PropertyChangeDetector traceListener = new PropertyChangeDetector();
    chart.addPropertyChangeListener(Chart2D.PROPERTY_ADD_REMOVE_TRACE, traceListener);
    ITrace2D trace = new Trace2DSimple();
    chart.addTrace(trace);
    PropertyChangeEvent event = traceListener.consumeEvent();
    assertNotNull("addTrace(ITrace2D) on Chart2D did not trigger a PropertyChange for property \""+ Chart2D.PROPERTY_ADD_REMOVE_TRACE  +"\". ", event);
    assertNull("addTrace(ITrace2D) on Chart2D did trigger a PropertyChangeEvent with a not-null oldValue. ", event.getOldValue());
    assertNotNull("addTrace(ITrace2D) on Chart2D did trigger a PropertyChangeEvent with a null newValue. ", event.getNewValue());
    // trigger a different change:
    chart.setBackground(Color.GREEN);
    assertNull("addTrace(ITrace2D) on Chart2D did trigger a PropertyChange for property \""+ Chart2D.PROPERTY_ADD_REMOVE_TRACE  +"\". ", traceListener.consumeEvent());

    // removeTrace
    chart.removeTrace(trace);
    event = traceListener.consumeEvent();
    assertNotNull("removeTrace(ITrace2D) on Chart2D did not trigger a PropertyChange for property \""+ Chart2D.PROPERTY_ADD_REMOVE_TRACE  +"\". ", event);
    assertNotNull("removeTrace(ITrace2D) on Chart2D did trigger a PropertyChangeEvent with a null oldValue. ", event.getOldValue());
    assertNull("removeTrace(ITrace2D) on Chart2D did trigger a PropertyChangeEvent with a non-null newValue. ", event.getNewValue());
  }

  /**
   * <p>
   *  Add several <code>Trace2DSimple</code> instances and remove them (procedure
   *  two times) and check for zero traces remaining within the chart.
   * </p>
   *
   */
  public void testAddRemoveManyTrace2DSimple(){
    Chart2D chart = new Chart2D();
    ArrayList traces = new ArrayList(5);

    for(int i =0;i<5;i++){
      traces.add(new Trace2DSimple("Trace "+i));
    }

    for(int i =0;i<5;i++){
      chart.addTrace((ITrace2D)traces.get(i));
    }


    Iterator tracesIt = chart.getTraces().iterator();
    StringBuffer msg = new StringBuffer("[");
    while(tracesIt.hasNext()){
      msg.append(((ITrace2D)tracesIt.next()).getName());
      if(tracesIt.hasNext()){
        msg.append(',');
      }
    }
    msg.append(']');
    assertEquals("Wrong number of traces contained: "+msg.toString(), 5, chart.getTraces().size());
    for(int i =0;i<5;i++){
      chart.removeTrace((ITrace2D)traces.get(i));
    }

    tracesIt = chart.getTraces().iterator();
    msg = new StringBuffer("[");
    while(tracesIt.hasNext()){
      msg.append(((ITrace2D)tracesIt.next()).getName());
      if(tracesIt.hasNext()){
        msg.append(',');
      }
    }
    msg.append(']');
    assertEquals("Wrong number of traces contained: "+msg.toString(), 0, chart.getTraces().size());

    for(int i =0;i<5;i++){
      chart.addTrace((ITrace2D)traces.get(i));
    }

    tracesIt = chart.getTraces().iterator();
    msg = new StringBuffer("[");
    while(tracesIt.hasNext()){
      msg.append(((ITrace2D)tracesIt.next()).getName());
      if(tracesIt.hasNext()){
        msg.append(',');
      }
    }
    msg.append(']');
    assertEquals("Wrong number of traces contained: "+msg.toString(), 5, chart.getTraces().size());
    for(int i =0;i<5;i++){
      chart.removeTrace((ITrace2D)traces.get(i));
    }

    tracesIt = chart.getTraces().iterator();
    msg = new StringBuffer("[");
    while(tracesIt.hasNext()){
      msg.append(((ITrace2D)tracesIt.next()).getName());
      if(tracesIt.hasNext()){
        msg.append(',');
      }
    }
    msg.append(']');

    assertEquals("Wrong number of traces contained: "+msg.toString(), 0, chart.getTraces().size());


  }

  /**
   * <p>
   *  Add several <code>Trace2DLtd</code> instances and remove them (procedure
   *  two times) and check for zero traces remaining within the chart.
   * </p>
   *
   */
  public void testAddRemoveManyTrace2DLtd(){
    Chart2D chart = new Chart2D();
    ArrayList traces = new ArrayList(5);

    for(int i =0;i<5;i++){
      traces.add(new Trace2DLtd("Trace "+i));
    }

    for(int i =0;i<5;i++){
      chart.addTrace((ITrace2D)traces.get(i));
    }


    Iterator tracesIt = chart.getTraces().iterator();
    StringBuffer msg = new StringBuffer("[");
    while(tracesIt.hasNext()){
      msg.append(((ITrace2D)tracesIt.next()).getName());
      if(tracesIt.hasNext()){
        msg.append(',');
      }
    }
    msg.append(']');
    assertEquals("Wrong number of traces contained: "+msg.toString(), 5, chart.getTraces().size());
    for(int i =0;i<5;i++){
      chart.removeTrace((ITrace2D)traces.get(i));
    }

    tracesIt = chart.getTraces().iterator();
    msg = new StringBuffer("[");
    while(tracesIt.hasNext()){
      msg.append(((ITrace2D)tracesIt.next()).getName());
      if(tracesIt.hasNext()){
        msg.append(',');
      }
    }
    msg.append(']');
    assertEquals("Wrong number of traces contained: "+msg.toString(), 0, chart.getTraces().size());

    for(int i =0;i<5;i++){
      chart.addTrace((ITrace2D)traces.get(i));
    }

    tracesIt = chart.getTraces().iterator();
    msg = new StringBuffer("[");
    while(tracesIt.hasNext()){
      msg.append(((ITrace2D)tracesIt.next()).getName());
      if(tracesIt.hasNext()){
        msg.append(',');
      }
    }
    msg.append(']');
    assertEquals("Wrong number of traces contained: "+msg.toString(), 5, chart.getTraces().size());
    for(int i =0;i<5;i++){
      chart.removeTrace((ITrace2D)traces.get(i));
    }

    tracesIt = chart.getTraces().iterator();
    msg = new StringBuffer("[");
    while(tracesIt.hasNext()){
      msg.append(((ITrace2D)tracesIt.next()).getName());
      if(tracesIt.hasNext()){
        msg.append(',');
      }
    }
    msg.append(']');

    assertEquals("Wrong number of traces contained: "+msg.toString(), 0, chart.getTraces().size());


  }
}
