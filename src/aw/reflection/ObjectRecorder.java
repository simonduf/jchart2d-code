/**
 * ObjectRecorder, a class that takes records of an objects state using reflection.
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
import java.lang.reflect.*;
import java.util.LinkedList;
import javax.swing.event.*;
import javax.naming.directory.NoSuchAttributeException;
import aw.util.collections.*;
import aw.util.TimeStampedValue;

/**
 *   The <code>ObjectRecorder</code> takes records(inspections) of an objects state 
 *  using reflection and accessibility- framework. 
 *  It's strategy is to: <br>
 *  <pre>
 *  - try to set any field accessible. 
 *  - try to get the value of the field.
 *  - if not suceed: try to invoke a bean- conform getter.
 *  - if NoSuchMethod, it's useless (no implementation of MagicClazz here).
 *  </pre>
 *  Furthermore the <code>ObjectRecorder</code> has a history - size (buffer) 
 *  and an adjustable distance between each inspection.
 *  @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann</a>
 *  @version 1.1
 **/
public class ObjectRecorder extends Thread {
    /**
     *	The time - interval between to inspections of the Object.
     **/
    protected long interval;
    protected IRingBuffer buffer = new RingBufferArrayFast(100);
    protected Object toinspect;
    protected Field[] fields;
    protected EventListenerList changeListeners=new EventListenerList();
    
    protected static final boolean verbose = false;
    
    public ObjectRecorder(Object toinspect,long interval) {
        this.interval = interval;
        this.toinspect = toinspect;
        // getting the fieldnames.
        this.fields = toinspect.getClass().getDeclaredFields();
        this.start();
    }
    
    public Object getInspected() {
        return this.toinspect;
    }
    
    public void run() {
        while (true) {
            try {
                this.sleep(this.interval);
            }catch(InterruptedException e) {
            }
            this.inspect();
        }
    }
    
    /**
     *	Makes a record of the state of the object specified in the constructor.
     *	The new record is stored in a RingBuffer and contains all retrieveable values
     *	of the Object specified in the constructor.
     *	Reflection is used to get the values. If a field is private it's value is tried
     *	to be taken from the Object by invoking a getter - method conform with the bean - specification:
     *	The name of the method has to be "get" followed by the name of the field with first letter
     *	uppercase.
     **/
    public void inspect() {
        ObjectInspection newentry = new ObjectInspection(this);
        for(int i=0;i<this.fields.length;i++) {
            if(verbose)
                System.out.println(this.getClass().getName()+" inpspecting "+fields[i].getName()+" of "+this.toinspect.getClass().getName()+".");
            try {
                fields[i].setAccessible(true);
                newentry.add(fields[i].get(this.toinspect));
            }catch(IllegalAccessException e) {
                if(verbose)
                    System.err.println(this.getClass().getName()+".inspect(): No public access to "+fields[i].getName()+" of "+this.toinspect.getClass().getName());
                //	Try to invoke bean- conform getter method.
                String fieldname = this.fields[i].getName();
                char [] fieldnm = fieldname.toCharArray();
                fieldnm[0] = Character.toUpperCase(fieldnm[0]);
                fieldname = new String(fieldnm);
                String methodname = new StringBuffer("get").append(fieldname).toString();
                //name of method constructed. Now invoke it.
                try {
                    Method toinvoke = this.toinspect.getClass().getDeclaredMethod(methodname,new Class[]{});
                    newentry.add(toinvoke.invoke(this.toinspect,new Object[]{}));
                    
                    
                }catch(NoSuchMethodException f) {
                    if(verbose)
                        System.err.println(this.getClass().getName()+".inspect(): Failure at getting field "+fields[i].getName()+" by trying to invoke a method: "+methodname);
                }catch(SecurityException g) {
                    g.printStackTrace();
                }catch(IllegalAccessException h) {
                    h.printStackTrace();
                }catch(InvocationTargetException l) {
                    l.printStackTrace();
                }
            }
        }
        this.buffer.add(newentry);
        this.fireChange();
    }
    public String[] getAttributeNames(){
        String ret[] = new String[this.fields.length];
        for(int i=0;i<this.fields.length;i++)
            ret[i]=this.fields[i].getName();
        return ret;
        
    }
    /**
     *  The History returned by this Method represents the past of the
     *  field specified by attributeName. It starts from low index with the newest
     *  values taken from the inspected Object and ends with the oldest.
     * @return An array filled with TimeStampedValues that represent the past of the last
     *  inspections of the field with attributeName.
     **/
    public TimeStampedValue[] getAttributeHistory(String attributeName)throws NoSuchAttributeException {
        //search for the field
        int attribindex =-1;
        for(int i=this.fields.length-1;i>=0;i--) {
            if (this.fields[i].getName().equals(attributeName)) {
                attribindex = i;break;
            }
        }
        if(attribindex==-1)throw new NoSuchAttributeException("The Attribute with the name: "+attributeName+" does not exist in "+this.toinspect.getClass().getName());
        
        int stop = this.buffer.size();
        TimeStampedValue[] ret = new TimeStampedValue[stop];
        ObjectInspection tmp;
        synchronized(this.buffer){
            java.util.Iterator it = this.buffer.iteratorF2L();
            int i=0;
            while(it.hasNext()) {
                tmp =(ObjectInspection)it.next();
                ret[i++]=new TimeStampedValue(tmp.getTime(),tmp.get(attribindex));
            }
        }
        return ret;
    }
    
    public TimeStampedValue getLastValue(String fieldname)throws NoSuchAttributeException{
        //search for the field
        int attribindex =-1;
        for(int i=this.fields.length-1;i>=0;i--) {
            if (this.fields[i].getName().equals(fieldname)) {
                attribindex = i;break;
            }
        }
        if(attribindex==-1)throw new NoSuchAttributeException("The Attribute with the name: "+fieldname+" does not exist in "+this.toinspect.getClass().getName());
        ObjectInspection tmp =(ObjectInspection)this.buffer.getYoungest();
        return new TimeStampedValue(tmp.getTime(),tmp.get(attribindex));
    }
    /**
     *	Define the amount of recorded states of the Object to inspect that remain in memory.
     *	Default value is 100.
     **/
    public void setHistoryLength(int length) {
        this.buffer.setBufferSize(length);
    }
    
    public IRingBuffer getRingBuffer() {
        return this.buffer;
    }
    
    public String toString() {
        return this.buffer.toString();
    }
    
    public void addChangeListener(ChangeListener x){
        changeListeners.add(ChangeListener.class,x);
        //x.stateChanged(new ChangeEvent(this)); // Aufruf des neuen ChangeListeners um zu aktualisieren.
    }
    public void removeChangeListener(ChangeListener x){
        changeListeners.remove(ChangeListener.class,x);
    }
    protected void fireChange(){
        ChangeEvent ce = new ChangeEvent(this);
        Object[] listeners = changeListeners.getListenerList();
        for(int i = listeners.length-1; i>= 0;i-=2){
            ChangeListener cl = (ChangeListener)listeners[i];
            cl.stateChanged(ce);
        }
    }
    
    public void setInterval(long sleeptime){
        this.interval = sleeptime;
    }
    
    private class ObjectInspection {
        protected long time;
        LinkedList values;
        ObjectRecorder inspector;
        
        private ObjectInspection(ObjectRecorder inspector) {
            this.inspector = inspector;
            time = new java.util.Date().getTime();
            values = new LinkedList();
        }
        
        public void add(Object value) {
            values.add(value);
        }
        
        public void remove(Object value) {
            this.values.remove(value);
        }
        
        /**
         * Get the value for the attribute at the given index.
         **/
        public Object get(int index) {
            return this.values.get(index);
        }
        
        public long getTime() {
            return this.time;
        }
        
        
        public String toString() {
            StringBuffer ret = new StringBuffer("\nObjectInspection:\n");
            ret.append("-----------------\n");
            ret.append("Inspected: ").append(this.inspector.getInspected().toString()).append("\n");
            ret.append("time:      ").append(this.time).append("\n");
            Field[] field = this.inspector.fields;
            for(int i=fields.length-1;i>=0;i--) {
                ret.append(fields[i].getName()).append(": ").append(this.values.get(i).toString()).append("\n");
            }
            return ret.toString();
        }
    }
    
}