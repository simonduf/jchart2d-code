/*
 *  RangeSliderUI.java of project jchart2d, <enterpurposehere>. 
 *  Copyright (C) 2002 - 2013, Achim Westermann, created on Nov 1, 2011
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
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
 * This file is taken from https://github.com/ernieyu/Swing-range-slider (blog: http://ernienotes.wordpress.com/2010/12/27/creating-a-java-swing-range-slider/) 
 * and was originally licensed under the MIT license, http://www.opensource.org/licenses/mit-license.php. 
 * Visit the links for the freshest versions. 
 */

package info.monitorenter.gui.jcomponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * UI delegate for the RangeSlider component. RangeSliderUI paints two thumbs,
 * one for the lower value and one for the upper value.
 */
class RangeSliderUI extends BasicSliderUI {

  /**
   * Listener to handle model change events. This calculates the thumb locations
   * and repaints the slider if the value change is not caused by dragging a
   * thumb.
   */
  public class ChangeHandler implements ChangeListener {
    @Override
    public void stateChanged(final ChangeEvent arg0) {
      if (!RangeSliderUI.this.lowerDragging && !RangeSliderUI.this.upperDragging) {
        RangeSliderUI.this.calculateThumbLocation();
        RangeSliderUI.this.slider.repaint();
      }
    }
  }

  /**
   * Listener to handle mouse movements in the slider track.
   */
  public class RangeTrackListener extends TrackListener {

    @Override
    public void mouseDragged(final MouseEvent e) {
      if (!RangeSliderUI.this.slider.isEnabled()) {
        return;
      }

      this.currentMouseX = e.getX();
      this.currentMouseY = e.getY();

      if (RangeSliderUI.this.lowerDragging) {
        RangeSliderUI.this.slider.setValueIsAdjusting(true);
        this.moveLowerThumb();

      } else if (RangeSliderUI.this.upperDragging) {
        RangeSliderUI.this.slider.setValueIsAdjusting(true);
        this.moveUpperThumb();
      }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
      if (!RangeSliderUI.this.slider.isEnabled()) {
        return;
      }

      this.currentMouseX = e.getX();
      this.currentMouseY = e.getY();

      if (RangeSliderUI.this.slider.isRequestFocusEnabled()) {
        RangeSliderUI.this.slider.requestFocus();
      }

      // Determine which thumb is pressed. If the upper thumb is
      // selected (last one dragged), then check its position first;
      // otherwise check the position of the lower thumb first.
      boolean lowerPressed = false;
      boolean upperPressed = false;
      if (RangeSliderUI.this.upperThumbSelected) {
        if (RangeSliderUI.this.upperThumbRect.contains(this.currentMouseX, this.currentMouseY)) {
          upperPressed = true;
        } else if (RangeSliderUI.this.thumbRect.contains(this.currentMouseX, this.currentMouseY)) {
          lowerPressed = true;
        }
      } else {
        if (RangeSliderUI.this.thumbRect.contains(this.currentMouseX, this.currentMouseY)) {
          lowerPressed = true;
        } else if (RangeSliderUI.this.upperThumbRect.contains(this.currentMouseX, this.currentMouseY)) {
          upperPressed = true;
        }
      }

      // Handle lower thumb pressed.
      if (lowerPressed) {
        switch (RangeSliderUI.this.slider.getOrientation()) {
          case SwingConstants.VERTICAL:
            this.offset = this.currentMouseY - RangeSliderUI.this.thumbRect.y;
            break;
          case SwingConstants.HORIZONTAL:
            this.offset = this.currentMouseX - RangeSliderUI.this.thumbRect.x;
            break;
        }
        RangeSliderUI.this.upperThumbSelected = false;
        RangeSliderUI.this.lowerDragging = true;
        return;
      }
      RangeSliderUI.this.lowerDragging = false;

      // Handle upper thumb pressed.
      if (upperPressed) {
        switch (RangeSliderUI.this.slider.getOrientation()) {
          case SwingConstants.VERTICAL:
            this.offset = this.currentMouseY - RangeSliderUI.this.upperThumbRect.y;
            break;
          case SwingConstants.HORIZONTAL:
            this.offset = this.currentMouseX - RangeSliderUI.this.upperThumbRect.x;
            break;
        }
        RangeSliderUI.this.upperThumbSelected = true;
        RangeSliderUI.this.upperDragging = true;
        return;
      }
      RangeSliderUI.this.upperDragging = false;
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
      RangeSliderUI.this.lowerDragging = false;
      RangeSliderUI.this.upperDragging = false;
      RangeSliderUI.this.slider.setValueIsAdjusting(false);
      super.mouseReleased(e);
    }

    /**
     * Moves the location of the lower thumb, and sets its corresponding value
     * in the slider.
     */
    private void moveLowerThumb() {
      int thumbMiddle = 0;

      switch (RangeSliderUI.this.slider.getOrientation()) {
        case SwingConstants.VERTICAL:
          final int halfThumbHeight = RangeSliderUI.this.thumbRect.height / 2;
          int thumbTop = this.currentMouseY - this.offset;
          int trackTop = RangeSliderUI.this.trackRect.y;
          int trackBottom = RangeSliderUI.this.trackRect.y + (RangeSliderUI.this.trackRect.height - 1);
          final int vMax = RangeSliderUI.this.yPositionForValue(RangeSliderUI.this.slider.getValue() + RangeSliderUI.this.slider.getExtent());

          // Apply bounds to thumb position.
          if (RangeSliderUI.this.drawInverted()) {
            trackBottom = vMax;
          } else {
            trackTop = vMax;
          }
          thumbTop = Math.max(thumbTop, trackTop - halfThumbHeight);
          thumbTop = Math.min(thumbTop, trackBottom - halfThumbHeight);

          RangeSliderUI.this.setThumbLocation(RangeSliderUI.this.thumbRect.x, thumbTop);

          // Update slider value.
          thumbMiddle = thumbTop + halfThumbHeight;
          RangeSliderUI.this.slider.setValue(RangeSliderUI.this.valueForYPosition(thumbMiddle));
          break;

        case SwingConstants.HORIZONTAL:
          final int halfThumbWidth = RangeSliderUI.this.thumbRect.width / 2;
          int thumbLeft = this.currentMouseX - this.offset;
          int trackLeft = RangeSliderUI.this.trackRect.x;
          int trackRight = RangeSliderUI.this.trackRect.x + (RangeSliderUI.this.trackRect.width - 1);
          final int hMax = RangeSliderUI.this.xPositionForValue(RangeSliderUI.this.slider.getValue() + RangeSliderUI.this.slider.getExtent());

          // Apply bounds to thumb position.
          if (RangeSliderUI.this.drawInverted()) {
            trackLeft = hMax;
          } else {
            trackRight = hMax;
          }
          thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth);
          thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth);

          RangeSliderUI.this.setThumbLocation(thumbLeft, RangeSliderUI.this.thumbRect.y);

          // Update slider value.
          thumbMiddle = thumbLeft + halfThumbWidth;
          RangeSliderUI.this.slider.setValue(RangeSliderUI.this.valueForXPosition(thumbMiddle));
          break;

        default:
          return;
      }
    }

    /**
     * Moves the location of the upper thumb, and sets its corresponding value
     * in the slider.
     */
    private void moveUpperThumb() {
      int thumbMiddle = 0;

      switch (RangeSliderUI.this.slider.getOrientation()) {
        case SwingConstants.VERTICAL:
          final int halfThumbHeight = RangeSliderUI.this.thumbRect.height / 2;
          int thumbTop = this.currentMouseY - this.offset;
          int trackTop = RangeSliderUI.this.trackRect.y;
          int trackBottom = RangeSliderUI.this.trackRect.y + (RangeSliderUI.this.trackRect.height - 1);
          final int vMin = RangeSliderUI.this.yPositionForValue(RangeSliderUI.this.slider.getValue());

          // Apply bounds to thumb position.
          if (RangeSliderUI.this.drawInverted()) {
            trackTop = vMin;
          } else {
            trackBottom = vMin;
          }
          thumbTop = Math.max(thumbTop, trackTop - halfThumbHeight);
          thumbTop = Math.min(thumbTop, trackBottom - halfThumbHeight);

          RangeSliderUI.this.setUpperThumbLocation(RangeSliderUI.this.thumbRect.x, thumbTop);

          // Update slider extent.
          thumbMiddle = thumbTop + halfThumbHeight;
          RangeSliderUI.this.slider.setExtent(RangeSliderUI.this.valueForYPosition(thumbMiddle) - RangeSliderUI.this.slider.getValue());
          break;

        case SwingConstants.HORIZONTAL:
          final int halfThumbWidth = RangeSliderUI.this.thumbRect.width / 2;
          int thumbLeft = this.currentMouseX - this.offset;
          int trackLeft = RangeSliderUI.this.trackRect.x;
          int trackRight = RangeSliderUI.this.trackRect.x + (RangeSliderUI.this.trackRect.width - 1);
          final int hMin = RangeSliderUI.this.xPositionForValue(RangeSliderUI.this.slider.getValue());

          // Apply bounds to thumb position.
          if (RangeSliderUI.this.drawInverted()) {
            trackRight = hMin;
          } else {
            trackLeft = hMin;
          }
          thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth);
          thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth);

          RangeSliderUI.this.setUpperThumbLocation(thumbLeft, RangeSliderUI.this.thumbRect.y);

          // Update slider extent.
          thumbMiddle = thumbLeft + halfThumbWidth;
          RangeSliderUI.this.slider.setExtent(RangeSliderUI.this.valueForXPosition(thumbMiddle) - RangeSliderUI.this.slider.getValue());
          break;

        default:
          return;
      }
    }

    @Override
    public boolean shouldScroll(final int direction) {
      return false;
    }
  }

  /** Color of selected range. */
  private final Color rangeColor = Color.GREEN;

  /** Location and size of thumb for upper value. */
  private Rectangle upperThumbRect;

  /** Indicator that determines whether upper thumb is selected. */
  private boolean upperThumbSelected;

  /** Indicator that determines whether lower thumb is being dragged. */
  private transient boolean lowerDragging;

  /** Indicator that determines whether upper thumb is being dragged. */
  private transient boolean upperDragging;

  /**
   * Constructs a RangeSliderUI for the specified slider component.
   * 
   * @param b
   *          RangeSlider
   */
  public RangeSliderUI(final RangeSlider b) {
    super(b);
  }

  /**
   * Updates the locations for both thumbs.
   */
  @Override
  protected void calculateThumbLocation() {
    // Call superclass method for lower thumb location.
    super.calculateThumbLocation();

    // Adjust upper value to snap to ticks if necessary.
    if (this.slider.getSnapToTicks()) {
      final int upperValue = this.slider.getValue() + this.slider.getExtent();
      int snappedValue = upperValue;
      final int majorTickSpacing = this.slider.getMajorTickSpacing();
      final int minorTickSpacing = this.slider.getMinorTickSpacing();
      int tickSpacing = 0;

      if (minorTickSpacing > 0) {
        tickSpacing = minorTickSpacing;
      } else if (majorTickSpacing > 0) {
        tickSpacing = majorTickSpacing;
      }

      if (tickSpacing != 0) {
        // If it's not on a tick, change the value
        if (((upperValue - this.slider.getMinimum()) % tickSpacing) != 0) {
          final float temp = (float) (upperValue - this.slider.getMinimum()) / (float) tickSpacing;
          final int whichTick = Math.round(temp);
          snappedValue = this.slider.getMinimum() + (whichTick * tickSpacing);
        }

        if (snappedValue != upperValue) {
          this.slider.setExtent(snappedValue - this.slider.getValue());
        }
      }
    }

    // Calculate upper thumb location. The thumb is centered over its
    // value on the track.
    if (this.slider.getOrientation() == SwingConstants.HORIZONTAL) {
      final int upperPosition = this.xPositionForValue(this.slider.getValue() + this.slider.getExtent());
      this.upperThumbRect.x = upperPosition - (this.upperThumbRect.width / 2);
      this.upperThumbRect.y = this.trackRect.y;

    } else {
      final int upperPosition = this.yPositionForValue(this.slider.getValue() + this.slider.getExtent());
      this.upperThumbRect.x = this.trackRect.x;
      this.upperThumbRect.y = upperPosition - (this.upperThumbRect.height / 2);
    }
  }

  /**
   * Updates the dimensions for both thumbs.
   */
  @Override
  protected void calculateThumbSize() {
    // Call superclass method for lower thumb size.
    super.calculateThumbSize();

    // Set upper thumb size.
    this.upperThumbRect.setSize(this.thumbRect.width, this.thumbRect.height);
  }

  /**
   * Creates a listener to handle change events in the specified slider.
   */
  @Override
  protected ChangeListener createChangeListener(final JSlider slider) {
    return new ChangeHandler();
  }

  /**
   * Returns a Shape representing a thumb.
   */
  private Shape createThumbShape(final int width, final int height) {
    // Use circular shape.
    final Ellipse2D shape = new Ellipse2D.Double(0, 0, width, height);
    return shape;
  }

  /**
   * Creates a listener to handle track events in the specified slider.
   */
  @Override
  protected TrackListener createTrackListener(final JSlider slider) {
    return new RangeTrackListener();
  }

  /**
   * Returns the size of a thumb.
   */
  @Override
  protected Dimension getThumbSize() {
    return new Dimension(12, 12);
  }

  /**
   * Installs this UI delegate on the specified component.
   */
  @Override
  public void installUI(final JComponent c) {
    this.upperThumbRect = new Rectangle();
    super.installUI(c);
  }

  /**
   * Paints the slider. The selected thumb is always painted on top of the other
   * thumb.
   */
  @Override
  public void paint(final Graphics g, final JComponent c) {
    super.paint(g, c);

    final Rectangle clipRect = g.getClipBounds();
    if (this.upperThumbSelected) {
      // Paint lower thumb first, then upper thumb.
      if (clipRect.intersects(this.thumbRect)) {
        this.paintLowerThumb(g);
      }
      if (clipRect.intersects(this.upperThumbRect)) {
        this.paintUpperThumb(g);
      }

    } else {
      // Paint upper thumb first, then lower thumb.
      if (clipRect.intersects(this.upperThumbRect)) {
        this.paintUpperThumb(g);
      }
      if (clipRect.intersects(this.thumbRect)) {
        this.paintLowerThumb(g);
      }
    }
  }

  /**
   * Paints the thumb for the lower value using the specified graphics object.
   */
  private void paintLowerThumb(final Graphics g) {
    final Rectangle knobBounds = this.thumbRect;
    final int w = knobBounds.width;
    final int h = knobBounds.height;

    // Create graphics copy.
    final Graphics2D g2d = (Graphics2D) g.create();

    // Create default thumb shape.
    final Shape thumbShape = this.createThumbShape(w - 1, h - 1);

    // Draw thumb.
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.translate(knobBounds.x, knobBounds.y);

    g2d.setColor(Color.CYAN);
    g2d.fill(thumbShape);

    g2d.setColor(Color.BLUE);
    g2d.draw(thumbShape);

    // Dispose graphics.
    g2d.dispose();
  }

  /**
   * Overrides superclass method to do nothing. Thumb painting is handled within
   * the <code>paint()</code> method.
   */
  @Override
  public void paintThumb(final Graphics g) {
    // Do nothing.
  }

  /**
   * Paints the track.
   */
  @Override
  public void paintTrack(final Graphics g) {
    // Draw track.
    super.paintTrack(g);

    final Rectangle trackBounds = this.trackRect;

    if (this.slider.getOrientation() == SwingConstants.HORIZONTAL) {
      // Determine position of selected range by moving from the middle
      // of one thumb to the other.
      final int lowerX = this.thumbRect.x + (this.thumbRect.width / 2);
      final int upperX = this.upperThumbRect.x + (this.upperThumbRect.width / 2);

      // Determine track position.
      final int cy = (trackBounds.height / 2) - 2;

      // Save color and shift position.
      final Color oldColor = g.getColor();
      g.translate(trackBounds.x, trackBounds.y + cy);

      // Draw selected range.
      g.setColor(this.rangeColor);
      for (int y = 0; y <= 3; y++) {
        g.drawLine(lowerX - trackBounds.x, y, upperX - trackBounds.x, y);
      }

      // Restore position and color.
      g.translate(-trackBounds.x, -(trackBounds.y + cy));
      g.setColor(oldColor);

    } else {
      // Determine position of selected range by moving from the middle
      // of one thumb to the other.
      final int lowerY = this.thumbRect.x + (this.thumbRect.width / 2);
      final int upperY = this.upperThumbRect.x + (this.upperThumbRect.width / 2);

      // Determine track position.
      final int cx = (trackBounds.width / 2) - 2;

      // Save color and shift position.
      final Color oldColor = g.getColor();
      g.translate(trackBounds.x + cx, trackBounds.y);

      // Draw selected range.
      g.setColor(this.rangeColor);
      for (int x = 0; x <= 3; x++) {
        g.drawLine(x, lowerY - trackBounds.y, x, upperY - trackBounds.y);
      }

      // Restore position and color.
      g.translate(-(trackBounds.x + cx), -trackBounds.y);
      g.setColor(oldColor);
    }
  }

  /**
   * Paints the thumb for the upper value using the specified graphics object.
   */
  private void paintUpperThumb(final Graphics g) {
    final Rectangle knobBounds = this.upperThumbRect;
    final int w = knobBounds.width;
    final int h = knobBounds.height;

    // Create graphics copy.
    final Graphics2D g2d = (Graphics2D) g.create();

    // Create default thumb shape.
    final Shape thumbShape = this.createThumbShape(w - 1, h - 1);

    // Draw thumb.
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.translate(knobBounds.x, knobBounds.y);

    g2d.setColor(Color.PINK);
    g2d.fill(thumbShape);

    g2d.setColor(Color.RED);
    g2d.draw(thumbShape);

    // Dispose graphics.
    g2d.dispose();
  }

  /**
   * Moves the selected thumb in the specified direction by a block increment.
   * This method is called when the user presses the Page Up or Down keys.
   */
  @Override
  public void scrollByBlock(final int direction) {
    synchronized (this.slider) {
      int blockIncrement = (this.slider.getMaximum() - this.slider.getMinimum()) / 10;
      if ((blockIncrement <= 0) && (this.slider.getMaximum() > this.slider.getMinimum())) {
        blockIncrement = 1;
      }
      final int delta = blockIncrement * ((direction > 0) ? BasicSliderUI.POSITIVE_SCROLL : BasicSliderUI.NEGATIVE_SCROLL);

      if (this.upperThumbSelected) {
        final int oldValue = ((RangeSlider) this.slider).getUpperValue();
        ((RangeSlider) this.slider).setUpperValue(oldValue + delta);
      } else {
        final int oldValue = this.slider.getValue();
        this.slider.setValue(oldValue + delta);
      }
    }
  }

  /**
   * Moves the selected thumb in the specified direction by a unit increment.
   * This method is called when the user presses one of the arrow keys.
   */
  @Override
  public void scrollByUnit(final int direction) {
    synchronized (this.slider) {
      final int delta = 1 * ((direction > 0) ? BasicSliderUI.POSITIVE_SCROLL : BasicSliderUI.NEGATIVE_SCROLL);

      if (this.upperThumbSelected) {
        final int oldValue = ((RangeSlider) this.slider).getUpperValue();
        ((RangeSlider) this.slider).setUpperValue(oldValue + delta);
      } else {
        final int oldValue = this.slider.getValue();
        this.slider.setValue(oldValue + delta);
      }
    }
  }

  /**
   * Sets the location of the upper thumb, and repaints the slider. This is
   * called when the upper thumb is dragged to repaint the slider. The
   * <code>setThumbLocation()</code> method performs the same task for the lower
   * thumb.
   */
  private void setUpperThumbLocation(final int x, final int y) {
    final Rectangle upperUnionRect = new Rectangle();
    upperUnionRect.setBounds(this.upperThumbRect);

    this.upperThumbRect.setLocation(x, y);

    SwingUtilities.computeUnion(this.upperThumbRect.x, this.upperThumbRect.y, this.upperThumbRect.width, this.upperThumbRect.height, upperUnionRect);
    this.slider.repaint(upperUnionRect.x, upperUnionRect.y, upperUnionRect.width, upperUnionRect.height);
  }
}