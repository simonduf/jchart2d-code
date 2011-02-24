package aw.gui.util;

/**
 * An interface for distributing colors.
 * <p>
 *
 * Implementations are keeping track of the java.awt.Color - instances they gave
 * away during invocation of <code>getColor()</code>. The aim is to provide
 * an interface that allows to get unique colors from. As all colors given away
 * are remembered, it is possible to avoid lending the same color several times.
 * <p>
 * It will be tricky to implement this in a good way because of the fact that
 * the ColorLessor should not be limited to a maximum amount of colors, it does
 * not know how much different colors there will be asked for a priori, the
 * amount of colors given away should be the most visual different and the
 * background color should be especially different from all ohter colors given
 * away.
 * <p>
 *
 * @see aw.gui.chart.Chart2D
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 */

public interface IColorDistributor {

  /**
   * Puts the given color back into the color- space of free colors.
   * <p>
   * Do not forget to give your colors back if not used any more to allow the
   * biggest possible visual difference of the colors given away.
   * <p>
   *
   * @see #getColor()
   */
  public void freeColor(java.awt.Color color);

  /**
   * Returns a color, that has not been retrieved before from this instance.
   * <p>
   *
   * @return a color, that has not been retrieved before from this instance.
   */
  public java.awt.Color getColor();

  /**
   * Informs the IColorDistributor that a client already uses the given color.
   * <p>
   *
   * This ensures that the IColorDistributor does never return the given color
   * to clients. Furthermore implementations may use the given informations of
   * the color, e.g. an IColorDistributor that tries to return always the most
   * different colors (from each other).
   * <p>
   *
   * @param color
   *          the color to reserve.
   */
  void reserveColor(java.awt.Color color);

  /**
   * No other color returned from instances must come "too near" to this color
   * to guarantee a good visibility of the other colors returned.
   * <p>
   * Calls to this method are of course optional but should be regarded in the
   * computations of implemenatations.
   * <p>
   *
   * @param background
   *          the background color to keep a viewable distance to.
   */
  public void setBgColor(final java.awt.Color background);
}
