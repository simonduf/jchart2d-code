/*
 *  ComparatorToString.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 30.05.2005, 20:14:59
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package aw.util.collections;

import java.util.Comparator;

/**
 * A <code>Comparator</code> that compares the given Objects by their
 * {@link java.lang.Object#toString()}value.
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *
 * @version $Revision: 1.3 $
 */
public final class ComparatorToString implements Comparator {

  /**
   * Compares both Objects by their {@link Object#toString()}presentation.
   * <p> * @version $Revision: 1.3 $

   *
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(final Object o1, final Object o2) throws IllegalArgumentException {
    if (o1 == null) {
      throw new IllegalArgumentException("Argument 1 must not be null");
    }
    if (o2 == null) {
      throw new IllegalArgumentException("Argument 2 must not be null");
    }
    return o1.toString().compareTo(o2.toString());
  }
}
