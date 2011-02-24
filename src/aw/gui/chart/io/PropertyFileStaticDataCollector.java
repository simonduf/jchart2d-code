/*
 * Created on 09.10.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package aw.gui.chart.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import aw.gui.chart.ITrace2D;
import aw.gui.chart.TracePoint2D;

/**
 * Data collector that collects data in form of
 * {@link aw.gui.chart.TracePoint2D}instances from a property file (
 * {@link java.util.Properties}).
 * <p>
 *
 * @author Achim Westermann
 */
public class PropertyFileStaticDataCollector extends AbstractStaticDataCollector {

  /**
   * The input stream in {@link java.util.Properties}format where
   * {@link aw.gui.chart.TracePoint2D}are parsed from.
   * <p>
   */
  private InputStream m_source;

  /**
   * Constructor with target trace and property file.
   * <p>
   *
   * @param trace
   *          the target trace to add data to.
   *
   * @param propertyFileStream
   *          the stream of the file in the {@link java.util.Properties}format
   *          where {@link aw.gui.chart.TracePoint2D}instances (key is x, value
   *          is y) is parsed from.
   */
  public PropertyFileStaticDataCollector(final ITrace2D trace, final InputStream propertyFileStream) {
    super(trace);
    this.m_source = propertyFileStream;
  }

  /**
   * @see aw.gui.chart.io.AbstractStaticDataCollector#collectData()
   */
  public void collectData() throws FileNotFoundException, IOException {
    Properties props = new Properties();
    props.load(this.m_source);
    Map.Entry entry;
    Iterator it = props.entrySet().iterator();
    List sortList = new LinkedList();
    while (it.hasNext()) {
      entry = (Map.Entry) it.next();
      sortList.add(new TracePoint2D(Double.parseDouble((String) entry.getKey()), Double
          .parseDouble((String) entry.getValue())));
    }
    Collections.sort(sortList);
    it = sortList.iterator();
    while (it.hasNext()) {
      m_trace.addPoint((TracePoint2D) it.next());
    }
  }
}
