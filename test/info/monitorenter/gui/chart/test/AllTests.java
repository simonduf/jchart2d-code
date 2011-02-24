/*
 * File   : $Source: /cvsroot/jchart2d/jchart2d/test/info/monitorenter/gui/chart/test/AllTests.java,v $
 * Date   : $Date: 2006/10/08 11:55:33 $
 * Version: $Revision: 1.1 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (c) 2005 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package info.monitorenter.gui.chart.test;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * OpenCms main test suite, executes the individual test suites of all core
 * packages.
 * <p>
 * 
 * @author Achim Westermann
 * @version $Revision: 1.1 $
 * 
 * @since 2.1.0
 */
public final class AllTests {
  /**
   * Hide constructor to prevent generation of class instances.
   * <p>
   */
  private AllTests() {

    // empty
  }

  /**
   * Creates the OpenCms JUnit test suite.
   * <p>
   * 
   * @return the OpenCms JUnit test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite("OpenCms complete tests");

    suite.addTest(info.monitorenter.gui.chart.AllTests.suite());
    suite.addTest(info.monitorenter.gui.chart.axis.AllTests.suite());
    suite.addTest(info.monitorenter.gui.chart.demo.AllTests.suite());
    suite.addTest(info.monitorenter.gui.chart.layout.AllTests.suite());
    suite.addTest(info.monitorenter.util.collections.AllTests.suite());

    TestSetup wrapper = new TestSetup(suite) {

      protected void setUp() {

        // oneTimeSetUp();
      }

      protected void tearDown() {

        // oneTimeTearDown();
      }
    };

    return wrapper;
  }
}
