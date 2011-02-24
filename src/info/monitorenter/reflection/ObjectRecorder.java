/*
 * ObjectRecorder, a class that takes records of an objects state using 
 * reflection.
 * Copyright (c) 2007  Achim Westermann, Achim.Westermann@gmx.de.
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
 */
package info.monitorenter.reflection;

import info.monitorenter.util.TimeStampedValue;
import info.monitorenter.util.collections.IRingBuffer;
import info.monitorenter.util.collections.RingBufferArrayFast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import javax.naming.directory.NoSuchAttributeException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;


/**
 * The <code>ObjectRecorder</code> takes records(inspections) of an objects
 * state using reflection and accessibility- framework.
 * <p>
 * 
 * It's strategy is to: <br/>
 * 
 * <pre>
 *  - try to set any field accessible.
 *  - try to get the value of the field.
 *  - if not suceed: try to invoke a bean- conform getter.
 *  - if NoSuchMethod, it's useless (no implementation of MagicClazz here).
 *  </pre>
 * 
 * <p>
 * 
 * Furthermore the <code>ObjectRecorder</code> has a history - size (buffer)
 * and an adjustable distance between each inspection.
 * <p>
 * 
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 * 
 * @version $Revision: 1.5 $
 */
public class ObjectRecorder extends Thread {

  /**
   * Data container for the inspection of the internal intance.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   * 
   * @version $Revision: 1.5 $
   */
  public final class ObjectInspection {
    /** Timestamp of the inspection. */
    protected long m_time;

    /** The values taken on the inspection. */
    private LinkedList m_values;

    /**
     * Creates an instance linked to the outer recorder.
     * <p>
     * 
     */
    private ObjectInspection() {
      this.m_time = new java.util.Date().getTime();
      this.m_values = new LinkedList();
    }

    /**
     * Adds an inspected value to this inpsection.
     * <p>
     * 
     * @param value
     *          an inspected value of this inpsection.
     */
    private void add(final Object value) {
      this.m_values.add(value);
    }

    /**
     * Get the value for the attribute at the given index.
     * <p>
     * 
     * @param index
     *          the index of the inspected value according to the order it was
     *          found on the instance by {@link Class#getDeclaredFields()}.
     *          <p>
     * 
     * @return the value for the attribute at the given index.
     */
    public Object get(final int index) {
      return this.m_values.get(index);
    }

    /**
     * Returns the timestamp in ms of this inspection.
     * <p>
     * 
     * @return the timestamp in ms of this inspection.
     */
    public long getTime() {
      return this.m_time;
    }

    /**
     * Removes the inspected value from this inspection.
     * <p>
     * 
     * The value is identified by means of
     * {@link Object#equals(java.lang.Object)}.
     * <p>
     * 
     * @param value
     *          the inspected value from this inspection.
     */
    protected void remove(final Object value) {
      this.m_values.remove(value);
    }

    /**
     * Returns a pretty print of this inspection.
     * <p>
     * 
     * @return a pretty print of this inspection.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
      StringBuffer ret = new StringBuffer("\nObjectInspection:\n");
      ret.append("-----------------\n");
      ret.append("Inspected: ").append(ObjectRecorder.this.getInspected().toString()).append("\n");
      ret.append("time:      ").append(this.m_time).append("\n");
      for (int i = ObjectRecorder.this.m_fields.length - 1; i >= 0; i--) {
        ret.append(ObjectRecorder.this.m_fields[i].getName()).append(": ").append(
            this.m_values.get(i).toString()).append("\n");
      }
      return ret.toString();
    }
  }

  /** Verbosity constant. */
  protected static final boolean VERBOSE = false;

  /** Fast buffer to store recorded fiels. */
  protected IRingBuffer m_buffer = new RingBufferArrayFast(100);

  /** The listeners on this recorder. */
  protected EventListenerList m_changeListeners = new EventListenerList();

  /** The fields to inspect on the instance. */
  protected Field[] m_fields;

  /**
   * The time - interval between to inspections of the Object.
   */
  protected long m_interval;

  /** The instance to instpec. */
  protected Object m_toinspect;

  /**
   * Creates an instance that will inspect the given Object in the given time
   * interval.
   * <p>
   * 
   * @param toinspect
   *          the instance to inspect.
   * 
   * @param interval
   *          the interval of inspection in ms.
   */
  public ObjectRecorder(final Object toinspect, final long interval) {
    this.m_interval = interval;
    this.m_toinspect = toinspect;
    this.setDaemon(true);
    // getting the fieldnames.
    this.m_fields = toinspect.getClass().getDeclaredFields();
    this.start();
  }

  /**
   * Adds a change listener that will be informed about new recordings of the
   * inpected instances.
   * <p>
   * 
   * @param x
   *          the change listener that will be informed about new recordings of
   *          the inpected instances.
   */
  public void addChangeListener(final ChangeListener x) {
    this.m_changeListeners.add(ChangeListener.class, x);
    // x.stateChanged(new ChangeEvent(this)); // Aufruf des neuen
    // ChangeListeners um zu aktualisieren.
  }

  /**
   * Informs the listeners about a change of this instance.
   * <p>
   * 
   */
  protected void fireChange() {
    ChangeEvent ce = new ChangeEvent(this);
    Object[] listeners = this.m_changeListeners.getListenerList();
    for (int i = listeners.length - 1; i >= 0; i -= 2) {
      ChangeListener cl = (ChangeListener) listeners[i];
      cl.stateChanged(ce);
    }
  }

  /**
   * The History returned by this Method represents the past of the field
   * specified by attributeName. It starts from low index with the newest values
   * taken from the inspected Object and ends with the oldest.
   * 
   * @param attributeName
   *          field name of the internal instance to inspect.
   * 
   * @return An array filled with TimeStampedValues that represent the past of
   *         the last inspections of the field with attributeName.
   * 
   * @throws NoSuchAttributeException
   *           if the attribute / field described by the given argument does not
   *           exist on the internal Object to instpect.
   * 
   * @see ObjectRecorder#getInspected()
   */
  public TimeStampedValue[] getAttributeHistory(final String attributeName)
      throws NoSuchAttributeException {
    // search for the field
    int attribindex = -1;
    for (int i = this.m_fields.length - 1; i >= 0; i--) {
      if (this.m_fields[i].getName().equals(attributeName)) {
        attribindex = i;
        break;
      }
    }
    if (attribindex == -1) {
      throw new NoSuchAttributeException("The Attribute with the name: " + attributeName
          + " does not exist in " + this.m_toinspect.getClass().getName());
    }
    int stop = this.m_buffer.size();
    TimeStampedValue[] ret = new TimeStampedValue[stop];
    ObjectInspection tmp;
    synchronized (this.m_buffer) {
      java.util.Iterator it = this.m_buffer.iteratorF2L();
      int i = 0;
      while (it.hasNext()) {
        tmp = (ObjectInspection) it.next();
        ret[i++] = new TimeStampedValue(tmp.getTime(), tmp.get(attribindex));
      }
    }
    return ret;
  }

  /**
   * Returns the names of the fields to inspect.
   * <p>
   * 
   * @return the names of the fields to inspect.
   */
  public String[] getAttributeNames() {
    String[] ret = new String[this.m_fields.length];
    for (int i = 0; i < this.m_fields.length; i++) {
      ret[i] = this.m_fields[i].getName();
    }
    return ret;

  }

  /**
   * Returns the inspected instance.
   * <p>
   * 
   * @return the inspected instance.
   */
  public Object getInspected() {
    return this.m_toinspect;
  }

  /**
   * Returns the last recoreded value taken from the given field along with the
   * timestamp identifying the time this value was recored.
   * <p>
   * 
   * @param fieldname
   *          the field whose value was recorded.
   * 
   * @return the last recoreded value taken from the given field along with the
   *         timestamp identifying the time this value was recored.
   * 
   * @throws NoSuchAttributeException
   *           if no such field exists on the Object to inspect.
   * 
   */
  public TimeStampedValue getLastValue(final String fieldname) throws NoSuchAttributeException {
    // search for the field
    int attribindex = -1;
    for (int i = this.m_fields.length - 1; i >= 0; i--) {
      if (this.m_fields[i].getName().equals(fieldname)) {
        attribindex = i;
        break;
      }
    }
    if (attribindex == -1) {
      throw new NoSuchAttributeException("The Attribute with the name: " + fieldname
          + " does not exist in " + this.m_toinspect.getClass().getName());
    }
    ObjectInspection tmp = (ObjectInspection) this.m_buffer.getYoungest();
    return new TimeStampedValue(tmp.getTime(), tmp.get(attribindex));
  }

  /**
   * Returns the internal fifo buffer that stores the
   * {@link ObjectRecorder.ObjectInspection} instances that have been done.
   * <p>
   * 
   * @return the internal fifo buffer that stores the
   *         {@link ObjectRecorder.ObjectInspection} instances that have been
   *         done.
   */
  public IRingBuffer getRingBuffer() {
    return this.m_buffer;
  }

  /**
   * Makes a record of the state of the object specified in the constructor. The
   * new record is stored in a RingBuffer and contains all retrieveable values
   * of the Object specified in the constructor. Reflection is used to get the
   * values. If a field is private it's value is tried to be taken from the
   * Object by invoking a getter - method conform with the bean - specification:
   * The name of the method has to be "get" followed by the name of the field
   * with first letter uppercase.
   */
  public void inspect() {
    ObjectInspection newentry = new ObjectInspection();
    for (int i = 0; i < this.m_fields.length; i++) {
      if (ObjectRecorder.VERBOSE) {
        System.out.println(this.getClass().getName() + " inpspecting " + this.m_fields[i].getName()
            + " of " + this.m_toinspect.getClass().getName() + ".");
      }
      try {
        this.m_fields[i].setAccessible(true);
        newentry.add(this.m_fields[i].get(this.m_toinspect));
      } catch (IllegalAccessException e) {
        if (ObjectRecorder.VERBOSE) {
          System.err.println(this.getClass().getName() + ".inspect(): No public access to "
              + this.m_fields[i].getName() + " of " + this.m_toinspect.getClass().getName());
        }
        // Try to invoke bean- conform getter method.
        String fieldname = this.m_fields[i].getName();
        char[] fieldnm = fieldname.toCharArray();
        fieldnm[0] = Character.toUpperCase(fieldnm[0]);
        fieldname = new String(fieldnm);
        String methodname = new StringBuffer("get").append(fieldname).toString();
        // name of method constructed. Now invoke it.
        try {
          Method toinvoke = this.m_toinspect.getClass().getDeclaredMethod(methodname,
              new Class[] {});
          newentry.add(toinvoke.invoke(this.m_toinspect, new Object[] {}));

        } catch (NoSuchMethodException f) {
          if (ObjectRecorder.VERBOSE) {
            System.err.println(this.getClass().getName() + ".inspect(): Failure at getting field "
                + this.m_fields[i].getName() + " by trying to invoke a method: " + methodname);
          }
        } catch (SecurityException g) {
          g.printStackTrace();
        } catch (IllegalAccessException h) {
          h.printStackTrace();
        } catch (InvocationTargetException l) {
          l.printStackTrace();
        }
      }
    }
    this.m_buffer.add(newentry);
    this.fireChange();
  }

  /**
   * Removes the given listener for changes of the inpsected instance.
   * <p>
   * 
   * @param x
   *          the listener to remove.
   */
  public void removeChangeListener(final ChangeListener x) {
    this.m_changeListeners.remove(ChangeListener.class, x);
  }

  /**
   * 
   * @see java.lang.Runnable#run()
   */
  public void run() {
    while (true) {
      try {
        Thread.sleep(this.m_interval);
      } catch (InterruptedException e) {
        // nop
      }
      this.inspect();
    }
  }

  /**
   * Define the amount of recorded states of the Object to inspect that remain
   * in memory.
   * <p>
   * 
   * Default value is 100.
   * <p>
   * 
   * @param length
   *          the amount of recorded states of the Object to inspect that remain
   *          in memory.
   */
  public void setHistoryLength(final int length) {
    this.m_buffer.setBufferSize(length);
  }

  /**
   * Sets the interval for inpection of the instance to inspect in ms.
   * <p>
   * 
   * @param sleeptime
   *          the interval for inpection of the instance to inspect in ms.
   * 
   * @see ObjectRecorder#ObjectRecorder(Object, long)
   */
  public void setInterval(final long sleeptime) {
    this.m_interval = sleeptime;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return this.m_buffer.toString();
  }
}
