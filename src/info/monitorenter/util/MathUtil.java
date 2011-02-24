/*
 *  MathUtil, utility class for math operations.
 *  Copyright (C) Achim Westermann, created on 09.09.2004, 12:38:21
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
package info.monitorenter.util;

/**
 * Static helpers for working with numbers.
 * <p>
 * Maybe not always the fastest solution to call in here, but working. Also
 * usable for seeing examples and cutting code for manual inlining.
 * <p>
 * 
 * @author Achim.Westermann@gmx.de
 * 
 * @version $Revision: 1.2 $
 */
public final class MathUtil {
  /** Singleton instance. */
  private static MathUtil instance = null;

  /**
   * Returns the singleton instance of this class.
   * <p>
   * 
   * This method is useless for now as all methods are static. It may be used in
   * future if VM-global configuration will be put to the state of the instance.
   * <p>#
   * 
   * @return the singleton instance of this class.
   */
  public static MathUtil getInstance() {
    if (MathUtil.instance == null) {
      MathUtil.instance = new MathUtil();
    }
    return MathUtil.instance;
  }
  
  /**
   * Tests that the given double is not invalid for calculation.
   * <p>
   * It must not be one of:
   * <ul>
   * <li> {@link Double#NaN} </li>
   * <li> {@link Double#NEGATIVE_INFINITY} </li>
   * <li> {@link Double#POSITIVE_INFINITY} </li>
   * </ul>
   * <p>
   * 
   * @param d
   *            the double to test.
   * 
   * @return true if the given double is valid for calculation (not infinite or
   *         NaN).
   */
  public static boolean isDouble(final double d) {
    return !(Double.isInfinite(d) || Double.isNaN(d));
  }

  /**
   * Asserts that the given double is not invalid for calculation.
   * <p>
   * It must not be one of:
   * <ul>
   * <li> {@link Double#NaN} </li>
   * <li> {@link Double#NEGATIVE_INFINITY} </li>
   * <li> {@link Double#POSITIVE_INFINITY} </li>
   * </ul>
   * <p>
   * 
   * @param d
   *          the double to test.
   * 
   * @throws IllegalArgumentException
   *           if the assertion fails.
   */
  public static void assertDouble(final double d) throws IllegalArgumentException {
    if (Double.isInfinite(d) || Double.isNaN(d)) {
      throw new IllegalArgumentException(d + " is not valid.");
    }
  }

  /**
   * Avoids creation from outside for singleton support.
   * <p>
   * 
   */
  private MathUtil() {
    // nop
  }
}
