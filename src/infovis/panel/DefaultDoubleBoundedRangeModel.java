/*****************************************************************************
 * Copyright (C) 2003-2005 Jean-Daniel Fekete and INRIA, France              *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the X11 Software License    *
 * a copy of which has been included with this distribution in the           *
 * license-infovis.txt file.                                                 *
 *****************************************************************************/
package infovis.panel;

import javax.swing.event.*;

/**
 * Caution this is a modified version of the infovis toolkit which strips down 
 * the minimal code to use the DoubleRangeSlider. Please find the orginial code at: 
 *  http://ivtk.sourceforge.net/.<p>
 * 
 * Defaut implementation of BoundedRangeModel for double.
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision: 1.1 $
 */
public class DefaultDoubleBoundedRangeModel implements DoubleBoundedRangeModel {
    protected transient ChangeEvent changeEvent;
    protected EventListenerList     listenerList = new EventListenerList();
    private double                  value        = 0;
    private double                  extent       = 0;
    private double                  min          = 0;
    private double                  max          = 100;
    private boolean                 isAdjusting  = false;

    /**
     * Creates a new DefaultDoubleBoundedRangeModel object.
     */
    public DefaultDoubleBoundedRangeModel() {
    }

    /**
     * Creates a new DefaultDoubleBoundedRangeModel object.
     * 
     * Maintains min &lt;= value &lt;= (value+extent) &lt;= max
     * 
     * @param value
     *            the current value
     * @param extent
     *            the current extent
     * @param min
     *            the minimum value
     * @param max
     *            the maximum value
     */
    public DefaultDoubleBoundedRangeModel(
            double value,
            double extent,
            double min,
            double max) {
        if ((max >= min) && (value >= min) && (value + extent) >= value
                && (value + extent) <= max) {
            this.value = value;
            this.extent = extent;
            this.min = min;
            this.max = max;
        }
        else {
            throw new IllegalArgumentException("invalid range properties");
        }
    }

    /**
     * {@inheritDoc}
     */
    public double getValueDouble() {
        return value;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getValue() {
    	return Math.round((float)getValueDouble());
        //return (int)getValueDouble();
    }

    /**
     * {@inheritDoc}
     */
    public double getExtentDouble() {
        return extent;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getExtent() {
    	return Math.round((float)getExtentDouble());
        //return (int)getExtentDouble();
    }

    /**
     * {@inheritDoc}
     */
    public double getMinimumDouble() {
        return min;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getMinimum() {
        return (int)getMinimumDouble();
    }

    /**
     * {@inheritDoc}
     */
    public double getMaximumDouble() {
        return max;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getMaximum() {
        return (int)getMaximumDouble();
    }

    /**
     * {@inheritDoc}
     */
    public void setValue(double n) {
        double newValue = Math.max(n, min);
        if ((newValue + extent) > max) {
            newValue = max - extent;
        }
        setRangeProperties(newValue, extent, min, max, isAdjusting);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setValue(int newValue) {
        setValue((double)newValue);
    }

    /**
     * {@inheritDoc}
     */
    public void setExtent(double n) {
        double newExtent = Math.max(0, n);
        if ((value + newExtent) > max) {
            newExtent = max - value;
        }
        setRangeProperties(value, newExtent, min, max, isAdjusting);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setExtent(int newExtent) {
        setExtent((double)newExtent);
    }

    /**
     * {@inheritDoc}
     */
    public void setMinimum(double n) {
        double newMax = Math.max(n, max);
        double newValue = Math.max(n, value);
        double newExtent = Math.min(newMax - newValue, extent);
        setRangeProperties(newValue, newExtent, n, newMax, isAdjusting);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setMinimum(int newMinimum) {
        setMinimum((double)newMinimum);
    }

    /**
     * {@inheritDoc}
     */
    public void setMaximum(double n) {
        double newMin = Math.min(n, min);
        double newExtent = Math.min(n - newMin, extent);
        double newValue = Math.min(n - newExtent, value);
        setRangeProperties(newValue, newExtent, newMin, n, isAdjusting);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setMaximum(int newMaximum) {
        setMaximum((double)newMaximum);
    }

    /**
     * {@inheritDoc}
     */
    public void setValueIsAdjusting(boolean b) {
        setRangeProperties(value, extent, min, max, b);
    }

    /**
     * {@inheritDoc}
     */
    public boolean getValueIsAdjusting() {
        return isAdjusting;
    }

    /**
     * {@inheritDoc}
     */
    public void setRangeProperties(
            double newValue,
            double newExtent,
            double newMin,
            double newMax,
            boolean adjusting) {
        if (newMin > newMax) {
            newMin = newMax;
        }
        if (newValue > newMax) {
            newMax = newValue;
        }
        if (newValue < newMin) {
            newMin = newValue;
        }
        if ((newExtent + newValue) > newMax) {
            newExtent = newMax - newValue;
        }
        if (newExtent < 0) {
            newExtent = 0;
        }
        if ((newValue != value) 
                || (newExtent != extent)
                || (newMin != min) 
                || (newMax != max)
                || (adjusting != isAdjusting)) {
            value = newValue;
            extent = newExtent;
            min = newMin;
            max = newMax;
            isAdjusting = adjusting;
            fireStateChanged();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void setRangeProperties(int value, int extent, int min, int max, boolean adjusting) {
        setRangeProperties((double)value, (double)extent, (double)min, (double)max, adjusting);
    }

    /**
     * {@inheritDoc}
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * Returns an array of all the change listeners registered on this
     * <code>DefaultDoubleBoundedRangeModel</code>.
     * 
     * @return all of this sizeModel's <code>ChangeListener</code>s or an
     *         empty array if no change listeners are currently registered
     * 
     * @see #addChangeListener
     * @see #removeChangeListener
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) listenerList
                .getListeners(ChangeListener.class);
    }

    /**
     * Runs each <code>ChangeListener</code>'s <code>stateChanged</code>
     * method.
     * 
     * @see #setRangeProperties
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
