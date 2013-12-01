/*
 *  RangeSlider.java of project jchart2d, <enterpurposehere>. 
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

import javax.swing.JSlider;

/**
 * An extension of JSlider to select a range of values using two thumb controls.
 * The thumb controls are used to select the lower and upper value of a range
 * with predetermined minimum and maximum values.
 * 
 * <p>
 * Note that RangeSlider makes use of the default BoundedRangeModel, which
 * supports an inner range defined by a value and an extent. The upper value
 * returned by RangeSlider is simply the lower value plus the extent.
 * </p>
 */
public class RangeSlider extends JSlider {

  /** Generated <code>serialVersionUID</code>. **/
  private static final long serialVersionUID = -2217157963496139352L;

  /**
   * Constructs a RangeSlider with default minimum and maximum values of 0 and
   * 100.
   */
  public RangeSlider() {
    initSlider();
  }

  /**
   * Constructs a RangeSlider with the specified default minimum and maximum
   * values.
   * <p>
   * 
   * @param min
   *          the minimum value of the slider.
   *          
   * @param max
   *          the maximum value of the slider.
   */
  public RangeSlider(final int min, final int max) {
    super(min, max);
    initSlider();
  }

  /**
   * Initializes the slider by setting default properties.
   */
  private void initSlider() {
    setOrientation(HORIZONTAL);
  }

  /**
   * Overrides the superclass method to install the UI delegate to draw two
   * thumbs.
   */
  @Override
  public void updateUI() {
    setUI(new RangeSliderUI(this));
    // Update UI for slider labels. This must be called after updating the
    // UI of the slider. Refer to JSlider.updateUI().
    updateLabelUIs();
  }

  /**
   * Returns the lower value in the range.
   */
  @Override
  public int getValue() {
    return super.getValue();
  }

  /**
   * Sets the lower value in the range.
   */
  @Override
  public void setValue(int value) {
    int oldValue = getValue();
    if (oldValue == value) {
      return;
    }

    // Compute new value and extent to maintain upper value.
    int oldExtent = getExtent();
    int newValue = Math.min(Math.max(getMinimum(), value), oldValue + oldExtent);
    int newExtent = oldExtent + oldValue - newValue;

    // Set new value and extent, and fire a single change event.
    getModel().setRangeProperties(newValue, newExtent, getMinimum(), getMaximum(), getValueIsAdjusting());
  }

  /**
   * Returns the upper value in the range.
   * 
   * @return the upper value in the range.
   */
  public int getUpperValue() {
    return getValue() + getExtent();
  }

  /**
   * Sets the upper value in the range.
   * 
   * @param value the upper value in the range.
   */
  public void setUpperValue(int value) {
    // Compute new extent.
    int lowerValue = getValue();
    int newExtent = Math.min(Math.max(0, value - lowerValue), getMaximum() - lowerValue);

    // Set extent to set upper value.
    setExtent(newExtent);
  }
}
