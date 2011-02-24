/**
 * ObjectRecorder2Trace2DAdpater, an adapter which enables drawing timestamped 
 *  values inspected by the ObjectRecorder on a Chart2D.
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

package aw.reflection;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.event.*;
import aw.gui.chart.*;
import aw.util.TimeStampedValue;


/**
 *  A simple adapter that allows displaying of timestamped values from 
 *  an inspection of the <code>ObjectRecorder</code> on a Chart2D. 
 *
 */
public class ObjRecorder2Trace2DAdapter implements ChangeListener {
    ITrace2D view;
    ObjectRecorder inspector;
    String fieldname;
    long start = System.currentTimeMillis();
    public ObjRecorder2Trace2DAdapter(ITrace2D view, Object toinspect, String fieldname, long interval) {
        this.view = view;
        this.fieldname = fieldname;
        this.view.setLabel(new StringBuffer(toinspect.getClass().getName()).append(toinspect.hashCode()).toString());
        this.inspector = new ObjectRecorder(toinspect,interval);
        this.inspector.addChangeListener(this);
    }
    
    public void stateChanged(ChangeEvent e){
        TimeStampedValue last;
        try{
            last = inspector.getLastValue(fieldname);
        }catch(Exception f){
            f.printStackTrace();
            return;
        }
        if(last!=null){
            double tmpx,tmpy;
            tmpx = last.getTime()-start;
            tmpy = Double.parseDouble(last.getValue().toString());
            this.view.addPoint(tmpx,tmpy);
        }
    }
    
    public void setInterval(long interval){
        this.inspector.setInterval(interval);
    
    }
}
