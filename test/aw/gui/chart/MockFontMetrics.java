/*
 *
 *  MockFontMetrics.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 23.04.2005, 15:24:13
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
package aw.gui.chart;

import java.awt.Font;
import java.awt.FontMetrics;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 */
public class MockFontMetrics extends FontMetrics {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3905236827622486832L;
  /**
   */
  public MockFontMetrics(Font font) {
    super(font);
  }
  /* (non-Javadoc)
   * @see java.awt.FontMetrics#charsWidth(char[], int, int)
   */
  public int charsWidth(char[] data, int off, int len) {
    return len*this.getFont().getSize()/2;
  }
  /* (non-Javadoc)
   * @see java.awt.FontMetrics#charWidth(char)
   */
  public int charWidth(char ch) {
    return this.getFont().getSize();
  }
  /* (non-Javadoc)
   * @see java.awt.FontMetrics#getAscent()
   */
  public int getAscent() {
   return this.getFont().getSize();
  }
  /* (non-Javadoc)
   * @see java.awt.FontMetrics#getLeading()
   */
  public int getLeading() {
    return 2;
  }
  /* (non-Javadoc)
   * @see java.awt.FontMetrics#getMaxAdvance()
   */
  public int getMaxAdvance() {
    return this.getFont().getSize();
  }

}
