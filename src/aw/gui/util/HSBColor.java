package aw.gui.util;

public class HSBColor implements java.io.Serializable,Cloneable{

	float hue;
	float sat;
	float lum;

	/** 
	 * Constructor for internal use only.
	 **/
	private HSBColor() {}
	/**
	* HSBColor constructor
	*
	* @param hue the hue component of the HSBColor
	* @param saturation the saturation component of the HSBColor
	* @param brightness the brightness component of the HSBColor
	*/
	HSBColor(float hue, float saturation, float brightness) {
		this.hue = hue;
		this.sat = saturation;
		this.lum = brightness;
	}

	public HSBColor(java.awt.Color rgbcolor) {
		int rgb = rgbcolor.getRGB();
		int r = (rgb & 0xFF0000) >> 16;
		int g = (rgb & 0xFF00) >> 8;
		int b = (rgb & 0xFF);
		int cmax = (r >= g) ? (r >= b) ? r : b : (g >= b) ? g : b;
		int cmin = (r <= g) ? (r <= b) ? r : b : (g <= b) ? g : b;
		this.lum = ((float) cmax) / 255f;
		if (cmax != cmin) {
			float difference = (cmax - cmin);
			this.sat = difference / ((float) cmax);
			if (r == cmax)
				this.hue = (g - b) / difference;
			else if (g == cmax)
				this.hue = (b - r) / difference + 2.0f;
			else
				this.hue = (r - g) / difference + 4.0f;
			this.hue /= 6.0f;
			if (this.hue < 0)
				this.hue += 1.0f;
		} else {
			this.sat = 0;
			this.hue = 0;
		}
	}

	/**
	 * Inspired by <code>float[] java.awt.Color.RGBtoHSB(int r,int g, int b, float[]hsbvals)</code>
	 * except that algorithm is tuned<br>
	 * Testing results showed about 25% speed up.
	 * Therefore the sources have become harder to understand.
	 * 
	 * @param int r the red value of RGB color model
	 * @param int g the green value of RGB color model
	 * @param int b the blue value of RGB color model
	 * @return the transformed values of the RGB colors in that order: hue,saturation,brightness.
	 **/
	public static HSBColor RGBtoHSB(java.awt.Color color) {
		int rgb = color.getRGB();
		int r = (rgb & 0xFF0000) >> 16;
		int g = (rgb & 0xFF00) >> 8;
		int b = (rgb & 0xFF);
		HSBColor ret = new HSBColor();

		int cmax = (r >= g) ? (r >= b) ? r : b : (g >= b) ? g : b;
		int cmin = (r <= g) ? (r <= b) ? r : b : (g <= b) ? g : b;
		ret.lum = ((float) cmax) / 255f;
		if (cmax != cmin) {
			float difference = (cmax - cmin);
			ret.sat = difference / ((float) cmax);
			if (r == cmax)
				ret.hue = (g - b) / difference;
			else if (g == cmax)
				ret.hue = (b - r) / difference + 2.0f;
			else
				ret.hue = (r - g) / difference + 4.0f;
			ret.hue /= 6.0f;
			if (ret.hue < 0)
				ret.hue += 1.0f;
		} else {
			ret.sat = 0;
			ret.hue = 0;
		}
		return ret;
	}
	/**
	 * Returns true if :<br>
	 * <code> 
	 * <nobr>
	 * o.instanceof HSBColor && (this.hue==o.hue) && (this.sat == o.sat) && (this.lum == o.lum)
	 * </nobr>
	 * </code>
	 **/
	public boolean equals(Object o) {
		if (!(o instanceof HSBColor))
			return false;
		HSBColor other = (HSBColor) o;
		return (this.hue == other.hue) && (this.sat == other.sat) && (this.lum == other.lum);
	}

	public java.awt.Color getRGBColor() {
		return new java.awt.Color(java.awt.Color.HSBtoRGB(this.hue,this.sat,this.lum));
	}
	/**
	 * Following statements are true:<br>
	 * <code>
	 *  x.clone() != x
	 *  x.clone().getClass() == x.getClass()
	 *  x.clone().equals(x)
	 * </code>
	 * A deep copy of this HSBColor is returned.
	 **/
	public Object clone(){
		return new HSBColor(this.hue,this.sat,this.lum);
	}
}

