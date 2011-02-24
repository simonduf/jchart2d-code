/*
 *  HSBColor.java, translates RGB colors into Hue-Saturation-Brightness 
 *  colors.
 *  Copyright (C) Achim Westermann, created on 19.05.2005, 22:01:51
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
package aw.gui.util;

/**
 * Color that internally works with the Hue Saturation Luminance color space.
 * <p>
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.5 $
 */
public class HSBColor implements java.io.Serializable, Cloneable {

  /**
   * Generated <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = 3257288036910903863L;

  /**
   * Inspired by
   * <code>float[] java.awt.Color.RGBtoHSB(int r,int g, int b, float[]hsbvals)</code>
   * except that algorithm is tuned <br>
   * Testing results showed about 25% speed up. Therefore the sources have
   * become harder to understand.
   *
   * @param color
   *          the <code>java.awt.Color</code> (that follows the RGB model) and
   *          should be transformed to a color instance in the
   *          hue-saturation-luminance model.
   *
   * @return the transformed values of the RGB colors in that order:
   *         hue,saturation,brightness.
   */
  public static HSBColor rgbToHSB(final java.awt.Color color) {
    int rgb = color.getRGB();
    int r = (rgb & 0xFF0000) >> 16;
    int g = (rgb & 0xFF00) >> 8;
    int b = (rgb & 0xFF);
    HSBColor ret = new HSBColor();

    int cmax = (r >= g) ? (r >= b) ? r : b : (g >= b) ? g : b;
    int cmin = (r <= g) ? (r <= b) ? r : b : (g <= b) ? g : b;
    ret.m_lum = ((float) cmax) / 255f;
    if (cmax != cmin) {
      float difference = (cmax - cmin);
      ret.m_sat = difference / ((float) cmax);
      if (r == cmax) {
        ret.m_hue = (g - b) / difference;
      } else if (g == cmax) {
        ret.m_hue = (b - r) / difference + 2.0f;
      } else {
        ret.m_hue = (r - g) / difference + 4.0f;
      }
      ret.m_hue /= 6.0f;
      if (ret.m_hue < 0) {
        ret.m_hue += 1.0f;
      }
    } else {
      ret.m_sat = 0;
      ret.m_hue = 0;
    }
    return ret;
  }

  /** Hue value between 0.0 and 1.0. */
  protected float m_hue;

  /** Luminance value between 0.0 and 1.0. */
  protected float m_lum;

  /** Saturation value between 0.0 and 1.0. */
  protected float m_sat;

  /**
   * Constructor for internal use only.
   * <p>
   */
  private HSBColor() {
    // nop
  }

  /**
   * Creates an instance with the given values for hue saturation and luminance.
   * <p>
   *
   * @param hue
   *          the hue component of the HSBColor
   * @param saturation
   *          the saturation component of the HSBColor
   * @param brightness
   *          the brightness component of the HSBColor
   */
  HSBColor(final float hue, final float saturation, final float brightness) {
    this.m_hue = hue;
    this.m_sat = saturation;
    this.m_lum = brightness;
  }

  /**
   * Creates an instance transformed from the rgb color.
   * <p>
   *
   * @param rgbcolor
   *          standard java rgb color.
   */
  public HSBColor(final java.awt.Color rgbcolor) {
    int rgb = rgbcolor.getRGB();
    int r = (rgb & 0xFF0000) >> 16;
    int g = (rgb & 0xFF00) >> 8;
    int b = (rgb & 0xFF);
    int cmax = (r >= g) ? (r >= b) ? r : b : (g >= b) ? g : b;
    int cmin = (r <= g) ? (r <= b) ? r : b : (g <= b) ? g : b;
    this.m_lum = ((float) cmax) / 255f;
    if (cmax != cmin) {
      float difference = (cmax - cmin);
      this.m_sat = difference / ((float) cmax);
      if (r == cmax) {
        this.m_hue = (g - b) / difference;
      } else if (g == cmax) {
        this.m_hue = (b - r) / difference + 2.0f;
      } else {
        this.m_hue = (r - g) / difference + 4.0f;
      }
      this.m_hue /= 6.0f;
      if (this.m_hue < 0) {
        this.m_hue += 1.0f;
      }
    } else {
      this.m_sat = 0;
      this.m_hue = 0;
    }
  }

  /**
   * Clone implementation.
   * <p>
   *
   * Following statements are true: <br>
   * <code>
   *  x.clone() != x
   *  x.clone().getClass() == x.getClass()
   *  x.clone().equals(x)
   * </code>
   * A deep copy of this HSBColor is returned.
   * <p>
   *
   * @return an intance copied from this one.
   */
  public Object clone() {
    return new HSBColor(this.m_hue, this.m_sat, this.m_lum);
  }

  /**
   * Equals implementation.
   * <p>
   *
   * Returns true if :<br>
   * <code>
   * <nobr>
   * o.instanceof HSBColor && (this.hue==o.hue) && (this.sat == o.sat) && (this.lum == o.lum)
   * </nobr>
   * </code>
   * <p>
   *
   * @param o
   *          the other {@link HSBColor} instance.
   *
   * @return true if the colors are judged equal.
   */
  public boolean equals(final Object o) {
    if (!(o instanceof HSBColor)) {
      return false;
    }
    HSBColor other = (HSBColor) o;
    return (this.m_hue == other.m_hue) && (this.m_sat == other.m_sat)
        && (this.m_lum == other.m_lum);
  }

  /**
   * Returns the transformation of this color to the rgb color.
   * <p>
   *
   * @return the transformation of this color to the rgb color.
   */

  public java.awt.Color getRGBColor() {
    return new java.awt.Color(java.awt.Color.HSBtoRGB(this.m_hue, this.m_sat, this.m_lum));
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {

    return (int) (this.m_hue * 10000 + this.m_sat * 1000 + this.m_lum * 100);
  }
}
