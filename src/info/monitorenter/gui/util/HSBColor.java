/*
 *  HSBColor.java, translates RGB colors into Hue-Saturation-Brightness 
 *  colors.
 *  Copyright (C) 2008 Achim Westermann
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
package info.monitorenter.gui.util;

/**
 * Color that internally works with the Hue Saturation Luminance color space.
 * <p>
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.4 $
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
    // TODO: Fix alpha treatment!!!!
    int rgb = color.getRGB();
    int r = (rgb & 0xFF0000) >> 16;
    int g = (rgb & 0xFF00) >> 8;
    int b = (rgb & 0xFF);
    HSBColor ret = new HSBColor();

    int cmax = (r >= g) ? (r >= b) ? r : b : (g >= b) ? g : b;
    int cmin = (r <= g) ? (r <= b) ? r : b : (g <= b) ? g : b;
    ret.m_lum = (cmax) / 255f;
    if (cmax != cmin) {
      float difference = (cmax - cmin);
      ret.m_sat = difference / (cmax);
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
    ret.m_alpha = color.getAlpha();
    return ret;
  }

  /** Hue value between 0.0 and 1.0. */
  protected double m_hue;

  /** Luminance value between 0.0 and 1.0. */
  protected double m_lum;

  /** Saturation value between 0.0 and 1.0. */
  protected double m_sat;

  /**
   * The unused alpha channel between 0 and 255: stored here for allow
   * java.awt.Color instances to be transformed to instances of this class and
   * be re - transformed with preserving their alpha setting.
   */
  protected double m_alpha;

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
  HSBColor(final double hue, final double saturation, final double brightness) {
    this(hue, saturation, brightness, 255);
  }

  /**
   * Creates an instance with the given values for hue saturation, luminance and
   * alpha.
   * <p>
   * 
   * @param hue
   *          the hue component of the HSBColor
   * 
   * @param saturation
   *          the saturation component of the HSBColor
   * 
   * @param brightness
   *          the brightness component of the HSBColor
   * 
   * @param alpha
   *          the alpha channed between 0.0 and 1.0.
   */
  HSBColor(final double hue, final double saturation, final double brightness, final int alpha) {
    this.m_hue = hue;
    this.m_sat = saturation;
    this.m_lum = brightness;
    this.m_alpha = alpha;
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
    this.m_lum = (cmax) / 255f;
    if (cmax != cmin) {
      float difference = (cmax - cmin);
      this.m_sat = difference / (cmax);
      if (r == cmax) {
        this.m_hue = (g - b) / difference;
      } else if (g == cmax) {
        this.m_hue = (b - r) / difference + 2.0;
      } else {
        this.m_hue = (r - g) / difference + 4.0;
      }
      this.m_hue /= 6.0;
      if (this.m_hue < 0) {
        this.m_hue += 1.0;
      }
    } else {
      this.m_sat = 0;
      this.m_hue = 0;
    }

    this.m_alpha = rgbcolor.getAlpha();
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
   * </code> A deep copy of this HSBColor is returned.
   * <p>
   * 
   * @return an instance copied from this one.
   */
  @Override
  public Object clone() {
    HSBColor result = null;
    try {
      result = (HSBColor) super.clone();
      result.m_hue = this.m_hue;
      result.m_lum = this.m_lum;
      result.m_sat = this.m_sat;
      result.m_alpha = this.m_alpha;
    } catch (CloneNotSupportedException e) {
      result = new HSBColor((float) this.m_hue, (float) this.m_sat, (float) this.m_lum, (int) Math
          .round(this.m_alpha));
    }
    return result;
  }

  /**
   * Equals implementation.
   * <p>
   * 
   * Returns true if :<br>
   * <code>
   * <span style="white-space:nowrap;">
   * o.instanceof HSBColor && (this.hue==o.hue) && (this.sat == o.sat) && (this.lum == o.lum)
   * </span>
   * </code>
   * <p>
   * 
   * @param o
   *          the other {@link HSBColor} instance.
   * 
   * @return true if the colors are judged equal.
   */
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof HSBColor)) {
      return false;
    }
    HSBColor other = (HSBColor) o;
    return (this.m_hue == other.m_hue) && (this.m_sat == other.m_sat)
        && (this.m_lum == other.m_lum) && (this.m_alpha == other.m_alpha);
  }

  /**
   * Returns the transformation of this color to the rgb color.
   * <p>
   * 
   * @return the transformation of this color to the rgb color.
   */

  public java.awt.Color getRGBColor() {
    int rgb = java.awt.Color.HSBtoRGB((float) this.m_hue, (float) this.m_sat, (float) this.m_lum);
    // This does not work as it filters out the alpha channel!
    // return new java.awt.Color(rgb);
    int r = (rgb & 0xff0000) >> 16;
    int g = (rgb & 0x00ff00) >> 8;
    int b = (rgb & 0x0000ff);
    return new java.awt.Color(r, g, b, (int) Math.round(this.m_alpha));

  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {

    return (int) (this.m_hue * 10000 + this.m_sat * 1000 + this.m_lum * 100);
  }
}
