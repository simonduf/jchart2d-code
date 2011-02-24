/*****************************************************************************
 * Copyright (C) 2003-2005 Jean-Daniel Fekete and INRIA, France              *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the X11 Software License    *
 * a copy of which has been included with this distribution in the           *
 * license-infovis.txt file.                                                 *
 *****************************************************************************/
package infovis.panel.dqinter;

import infovis.panel.DefaultDoubleBoundedRangeModel;
import infovis.panel.DoubleBoundedRangeModel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Caution this is a modified version of the infovis toolkit which strips down 
 * the minimal code to use the DoubleRangeSlider. Please find the orginial code at: 
 *  http://ivtk.sourceforge.net/.<p>
 * 
 * Implements a Swing-based Range slider, which allows the user to enter a
 * range-based value.
 * 
 * @author Ben B. Bederson, Jon Meyer and Jean-Daniel Fekete
 * @version $Revision: 1.1 $
 */
public class DoubleRangeSlider extends JComponent
    implements MouseListener, MouseMotionListener, MouseWheelListener, ChangeListener {
    private static int              PICK_WIDTH = 6;
    //  Size of the cutout corner on the range bar.
    static int                      SZ         = 6;

    // Event handling
    static final int                PICK_NONE  = 0;

    // Event handling
    static final int                PICK_MIN   = 1;

    // Event handling
    static final int                PICK_MAX   = 2;

    // Event handling
    static final int                PICK_MID   = 4;
    private DoubleBoundedRangeModel model;
    private boolean                 enabled    = false;

    // PAINT METHOD
    int[]                           xPts       = new int[7];

    // PAINT METHOD
    int[]                           yPts       = new int[7];
    int                             pick;
    double                          pickOffset;
    double                          mouseX;
    double                          scaleRatio = 1;
    Insets                          insets;
    boolean                         inside;
    int                             preferredHeight = 50;

    // PUBLIC API

    /**
     * Constructs a new range slider.
     * 
     * @param minimum -
     *            the minimum value of the range.
     * @param maximum -
     *            the maximum value of the range.
     * @param lowValue -
     *            the current low value shown by the range slider's bar.
     * @param highValue -
     *            the current high value shown by the range slider's bar.
     */
    public DoubleRangeSlider(
            double minimum,
            double maximum,
            double lowValue,
            double highValue) {
        this(new DefaultDoubleBoundedRangeModel(
                lowValue,
                highValue - lowValue,
                minimum,
                maximum));
    }

    /**
     * Creates a new RangeSlider object.
     * 
     * @param model
     *            the DoubleBoundedRangeModel
     */
    public DoubleRangeSlider(DoubleBoundedRangeModel model) {
        this.model = model;
        model.addChangeListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
 

    /**
     * Returns the current "low" value shown by the range slider's bar. The low
     * value meets the constraint minimum &lt;= lowValue &lt;= highValue &lt;=
     * maximum.
     * 
     * @return the current "low" value shown by the range slider's bar.
     */
    public double getLowValue() {
        return model.getValueDouble();
    }

    /**
     * Returns the current "high" value shown by the range slider's bar. The
     * high value meets the constraint minimum &lt;= lowValue &lt;= highValue
     * &lt;= maximum.
     * 
     * @return the current "high" value shown by the range slider's bar.
     */
    public double getHighValue() {
        return model.getValueDouble() + model.getExtentDouble();
    }

    /**
     * Returns the minimum possible value for either the low value or the high
     * value.
     * 
     * @return the minimum possible value for either the low value or the high
     *         value.
     */
    public double getMinimum() {
        return model.getMinimumDouble();
    }

    /**
     * Returns the maximum possible value for either the low value or the high
     * value.
     * 
     * @return the maximum possible value for either the low value or the high
     *         value.
     */
    public double getMaximum() {
        return model.getMaximumDouble();
    }

    /**
     * Returns true if the specified value is within the range indicated by this
     * range slider, i&dot;e&dot; lowValue 1 &lt;= v &lt;= highValue.
     * 
     * @param v
     *            value
     * 
     * @return true if the specified value is within the range indicated by this
     *         range slider.
     */
    public boolean contains(double v) {
        return (v >= getLowValue() && v <= getHighValue());
    }

    /**
     * Sets the low value shown by this range slider. This causes the range
     * slider to be repainted and a RangeEvent to be fired.
     * 
     * @param lowValue
     *            the low value shown by this range slider
     */
    public void setLowValue(double lowValue) {
        double high;
        if ((lowValue + model.getExtentDouble()) > getMaximum()) {
            high = getMaximum();
        }
        else {
            high = getHighValue();
        }
        double extent = high - lowValue;

        model.setRangeProperties(
                lowValue,
                extent,
                getMinimum(),
                getMaximum(),
                true);
    }

    /**
     * Sets the high value shown by this range slider. This causes the range
     * slider to be repainted and a RangeEvent to be fired.
     * 
     * @param highValue
     *            the high value shown by this range slider
     */
    public void setHighValue(double highValue) {
        model.setExtent(highValue - getLowValue());
    }

    /**
     * Sets the minimum value of the doubleBoundedRangeModel.
     * 
     * @param min
     *            the minimum value.
     */
    public void setMinimum(double min) {
        model.setMinimum(min);
    }

    /**
     * Sets the maximum value of the doubleBoundedRangeModel.
     * 
     * @param max
     *            the maximum value.
     */
    public void setMaximum(double max) {
        model.setMaximum(max);
    }

    Rectangle getInBounds() {
        Dimension sz = getSize();
        insets = getInsets();
        return new Rectangle(insets.left, insets.top, sz.width - insets.left
                - insets.right, sz.height - insets.top - insets.bottom);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics g) {
        g.setColor(Color.lightGray);

        // Dimension sz = getSize();
        Rectangle rect = getInBounds();
    
        int minX = toScreenX(getLowValue());
        int maxX = toScreenX(getHighValue());

        if ((maxX - minX) > 10) {
            xPts[0] = minX;
            yPts[0] = rect.y + SZ;
            xPts[1] = minX + SZ;
            yPts[1] = rect.y;
            xPts[2] = maxX;
            yPts[2] = rect.y;
            xPts[3] = maxX;
            yPts[3] = rect.y + rect.height - SZ;
            xPts[4] = maxX - SZ;
            yPts[4] = rect.y + rect.height;
            xPts[5] = minX;
            yPts[5] = rect.y + rect.height;
            xPts[6] = minX;
            yPts[6] = rect.y + SZ;
            g.setColor(Color.darkGray);
            g.drawPolygon(xPts, yPts, 7);

//            if (enabled == true) {
//                g.setColor(Color.lightGray);
//            }
//
//            g.fillPolygon(xPts, yPts, 7);

            g.setColor(Color.white);
            g.drawLine(xPts[0], yPts[0], xPts[1], yPts[1]);
            g.drawLine(xPts[1], yPts[1], xPts[2], yPts[2]);
            g.drawLine(xPts[5], yPts[5], xPts[6], yPts[6]);

            if ((maxX - minX) > 12) {
                // Draw the little dot pattern
                for (int y = rect.y + 3; y < (rect.y + rect.height - 3); y += 3) {
                    g.setColor(Color.lightGray);
                    g.fillRect(minX + 2, y + 2, 1, 1);
                    g.fillRect(minX + 5, y, 1, 1);
                    g.fillRect(minX + 8, y - 2, 1, 1);

                    g.fillRect(maxX - 3, y, 1, 1);
                    g.fillRect(maxX - 6, y + 2, 1, 1);
                    g.fillRect(maxX - 9, y + 4, 1, 1);

                    g.setColor(Color.darkGray);
                    g.fillRect(minX + 2, y + 3, 1, 1);
                    g.fillRect(minX + 5, y + 1, 1, 1);
                    g.fillRect(minX + 8, y - 1, 1, 1);

                    g.fillRect(maxX - 3, y + 1, 1, 1);
                    g.fillRect(maxX - 6, y + 3, 1, 1);
                    g.fillRect(maxX - 9, y + 5, 1, 1);
                }

                g.setColor(Color.gray);
                g.drawLine(minX + 10, rect.y + 2, minX + 10, rect.y
                        + rect.height - 2);
                g.drawLine(maxX - 11, rect.y + 2, maxX - 11, rect.y
                        + rect.height - 2);
                // pickWidth = PICK_WIDTH;
            }
            else {
                // Too small to draw the dot pattern - just draw a line down the
                // center
                g.setColor(Color.gray);
                g.drawLine(
                        (minX + maxX) / 3,
                        rect.y + 2,
                        (minX + maxX) / 3,
                        rect.y + rect.height - 2);

                g.drawLine(
                        (2 * (minX + maxX)) / 3,
                        rect.y + 2,
                        (2 * (minX + maxX)) / 3,
                        rect.y + rect.height - 2);
                // pickWidth = (int)((maxX - minX) / 3);
            }
        }
        else {
            // For very small ranges we just draw a tiny 3D rect
            if (enabled == true) {
                g.setColor(Color.lightGray);
            }
            else {
                g.setColor(Color.darkGray);
            }

            int w = maxX - minX;

            if (w < 10) {
                w = 10;
            }

            g.fill3DRect(minX, rect.y, w, rect.y + rect.height, true);
            g.setColor(Color.gray);
            g.drawLine((minX + maxX) / 3, rect.y + 2, (minX + maxX) / 3, rect.y
                    + rect.height - 2);
            g.drawLine(
                    (2 * (minX + maxX)) / 3,
                    rect.y + 2,
                    (2 * (minX + maxX)) / 3,
                    rect.y + rect.height - 2);
            // pickWidth = (int)((maxX - minX) / 3);
        }
//        setToolTipText("RangeSlider");
        if (model.getValueIsAdjusting() || inside) {
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            String s = format(getLowValue());
            float w = fm.stringWidth(s);
            g.clearRect(insets.left, insets.top, (int)w, fm.getHeight());
            g.drawString(s, insets.left, fm.getAscent() + insets.top);

            s = format(getHighValue());
            w = fm.stringWidth(s);
            g.clearRect(rect.x + rect.width - (int)w, insets.top, (int)w, fm.getHeight());
            g.drawString(s, rect.x + rect.width - (int)w, fm.getAscent() + insets.top);
        }
    }

    private double getScaleX() {
        int width = getWidth();
        double xScale = (width - insets.left - insets.right) / (getMaximum() - getMinimum());
        return xScale;
    }
    
    // Converts from screen coordinates to a range value.
    private double toLocalX(double x) {
        double xScale = getScaleX();

        return ((x - insets.left) / xScale) + getMinimum();
    }

    // Converts from a range value to screen coordinates.
    private int toScreenX(double x) {
        double xScale = getScaleX();

        return (int) ((x - getMinimum()) * xScale) + insets.left;
    }

    private int pickHandle(double x) {
        double minX = toScreenX(getLowValue());
        int maxX = toScreenX(getHighValue());

        int pick = 0;

        if (Math.abs(x - minX) < PICK_WIDTH) {
            pick |= PICK_MIN;
        }

        if (Math.abs(x - maxX) < PICK_WIDTH) {
            pick |= PICK_MAX;
        }

        if ((pick == 0) && (x > minX) && (x < maxX)) {
            pick = PICK_MID;
        }

        return pick;
    }

    private void offset(double dx) {
        model.setValue(getLowValue() + dx);
    }

    /**
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent e) {
        if (enabled == false) {
            return;
        }

        pick = pickHandle(e.getX());
        pickOffset = e.getX() - toScreenX(getLowValue());
        mouseX = e.getX();
        model.setValueIsAdjusting(true);
    }

    /**
     * {@inheritDoc}
     */
    public void mouseDragged(MouseEvent e) {
        if (enabled == false) {
            return;
        }
        double xpos = e.getX();

//        int dist = 0;
//        if (e.getY() < 0)
//            dist = -e.getY();
//        else if (e.getY() > getSize().height) {
//            dist = e.getY() - getSize().height;
//        }
//        if (dist != 0) {
//            xpos = mouseX + (xpos - mouseX) / (1 + dist);
//        }

        double x = toLocalX(xpos);

        if (x < getMinimum()) {
            x = getMinimum();
        }

        if (x > getMaximum()) {
            x = getMaximum();
        }

        if (pick == (PICK_MIN | PICK_MAX)) {
            if ((xpos - mouseX) > 2) {
                pick = PICK_MAX;
            }
            else if ((xpos - mouseX) < -2) {
                pick = PICK_MIN;
            }
            else {
                return;
            }
        }
        mouseX = xpos;

        switch (pick) {
        case PICK_MIN:
            if (x < getHighValue()) {
                setLowValue(x);
            }
            break;
        case PICK_MAX:
            if (x > getLowValue()) {
                setHighValue(x);
            }
            break;
        case PICK_MID:
            double dx = toLocalX(xpos - pickOffset) - getLowValue();
            if ((dx < 0) && ((getLowValue() + dx) < getMinimum())) {
                dx = getMinimum() - getLowValue();
            }

            if ((dx > 0) && ((getHighValue() + dx) > getMaximum())) {
                dx = getMaximum() - getHighValue();
            }

            if (dx != 0) {
                offset(dx);
            }

            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(MouseEvent e) {
        model.setValueIsAdjusting(false);
    }

    private void setCurs(int c) {
        Cursor cursor = Cursor.getPredefinedCursor(c);
        setCursor(cursor);
    }

    /**
     * {@inheritDoc}
     */
    public void mouseMoved(MouseEvent e) {
        if (enabled == false) {
            return;
        }

        switch (pickHandle(e.getX())) {
        case PICK_MIN:
        case PICK_MIN | PICK_MAX:
            setCurs(Cursor.W_RESIZE_CURSOR);
            break;
        case PICK_MAX:
            setCurs(Cursor.E_RESIZE_CURSOR);
            break;
        case PICK_MID:
            setCurs(Cursor.MOVE_CURSOR);
            break;
        case PICK_NONE:
            setCurs(Cursor.DEFAULT_CURSOR);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseClicked(MouseEvent e) {
        if (enabled == false) {
            return;
        }
        if (e.getClickCount() == 2) {
            model.setRangeProperties(
                    model.getMinimum(),
                    model.getMaximum() - model.getMinimum(),
                    model.getMinimum(),
                    model.getMaximum(),
                    false);
            repaint();
        }
    }
    


    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
        if (inside) return;
        setToolTipText("inside");
        inside = true;
         repaint();
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        setCurs(Cursor.DEFAULT_CURSOR);
        if (! inside) return;
        inside = false;
        repaint();
    }
    
    /**
     * {@inheritDoc}
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        
    }

    /**
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        return new Dimension(300, preferredHeight);
    }
    
    /**
     * @return the preferredHeight
     */
    public int getPreferredHeight() {
        return preferredHeight;
    }
    
    /**
     * @param preferredHeight the preferredHeight to set
     */
    public void setPreferredHeight(int preferredHeight) {
        if (this.preferredHeight == preferredHeight) {
            return;
        }
        this.preferredHeight = preferredHeight;
        invalidate();
    }

    /**
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    public void setEnabled(boolean v) {
        enabled = v;
        repaint();
    }

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        repaint();
    }

    /**
     * Returns the doubleBoundedRangeModel.
     * 
     * @return BoundedRangeModel
     */
    public DoubleBoundedRangeModel getModel() {
        return model;
    }

    /**
     * Sets the doubleBoundedRangeModel.
     * 
     * @param model
     *            The doubleBoundedRangeModel to set
     */
    public void setModel(DoubleBoundedRangeModel model) {
        this.model = model;
        repaint();
    }

    /**
     * Formats a range value.
     * @param x the value to format
     * @return a string representation of the value
     */
    public String format(double x) {
        return Double.toString(x);
    }

    /**
     * @see javax.swing.JComponent#getToolTipText(MouseEvent)
     */
    public String getToolTipText(MouseEvent event) {
        return format(getMinimum()) 
            + " [" 
            + format(getLowValue()) 
            + ".." 
            + format(getHighValue())
            + "] " 
            + format(getMaximum()); 
    }

}
