/*
 *
 *  LayoutFactory.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 19.05.2005, 20:26:00
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
package aw.gui.chart.layout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import aw.gui.chart.AbstractTrace2D;
import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.event.Trace2DActionSetCustomColor;
import aw.gui.chart.event.Trace2DActionSetName;
import aw.gui.chart.event.Chart2DActionSetCustomGridColor;
import aw.gui.chart.event.Chart2DActionSetGrid;
import aw.gui.chart.event.Chart2DActionSetGridColor;
import aw.gui.chart.event.JComponentActionSetBackground;
import aw.gui.chart.event.JComponentActionSetCustomBackground;
import aw.gui.chart.event.JComponentActionSetCustomForeground;
import aw.gui.chart.event.JComponentActionSetForeground;
import aw.gui.chart.event.PopupListener;
import aw.gui.chart.event.Trace2DActionSetColor;
import aw.gui.chart.event.Trace2DActionSetStroke;
import aw.gui.chart.event.Trace2DActionSetVisible;
import aw.gui.chart.event.Trace2DActionSetZindex;
import aw.gui.chart.event.Trace2DActionZindexDecrease;
import aw.gui.chart.event.Trace2DActionZindexIncrease;

/**
 * Factory that provides creational methods for adding popup menues to the
 * {@link aw.gui.chart.Chart2D}and obtaining popup menu decorated
 * {@link javax.swing.JLabel}instances for {@link aw.gui.chart.ITrace2D}
 * instances.
 * <p>
 *
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 */
public final class LayoutFactory {

  /**
   * <p>
   * Implementation for a <code>PropertyChangeListener</code> that adpapts a
   * wrapped <code>JComponent</code> to the following corporate UI properties.
   * <p>
   *
   * <ul>
   * <li>background color
   * <li>foreground color (text)
   * <li>font
   * </ul>
   * <p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   */
  private static class BasicPropertyAdaptSupport implements PropertyChangeListener {

    /** The component to adapt properties on. */
    private JComponent m_delegate;

    /**
     *
     * @param delegate
     *          The component to adapt the properties on.
     * @param adaptee
     *          The peer component delegate will be adapted to.
     */
    public BasicPropertyAdaptSupport(final JComponent delegate, final JComponent adaptee) {
      this.m_delegate = delegate;
      this.m_delegate.setFont(adaptee.getFont());
      this.m_delegate.setBackground(adaptee.getBackground());
      this.m_delegate.setForeground(adaptee.getForeground());
      adaptee.addPropertyChangeListener(this);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent evt) {
      String prop = evt.getPropertyName();
      if (prop.equals(Chart2D.PROPERTY_BACKGROUND_COLOR)) {
        Color color = (Color) evt.getNewValue();
        this.m_delegate.setBackground(color);
      } else if (prop.equals(Chart2D.PROPERTY_FONT)) {
        Font font = (Font) evt.getNewValue();
        this.m_delegate.setFont(font);
      } else if (prop.equals(Chart2D.PROPERTY_FOREGROUND_COLOR)) {
        Color color = (Color) evt.getNewValue();
        this.m_delegate.setForeground(color);
      }
    }
  }

  /**
   * A <code>JCheckBoxMenuItem</code> that listens for changes of background
   * color, foreground color and font of the given <code>JComponent</code> and
   * adapts it's own settings.
   * <p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   */
  private static class PropertyChangeCheckBoxMenuItem extends JCheckBoxMenuItem {

    /**
     * Generated <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3690196534012752439L;

    /**
     * Creates an instance with the given name that listens to the components
     * background color, foreground color and font.
     * <p>
     *
     * @param component
     *          The component to whose background color this item will adapt.
     *
     * @param action
     *          The <code>Action</code> to trigger when this item is clicked.
     */
    public PropertyChangeCheckBoxMenuItem(final JComponent component, final Action action) {
      super(action);
      new BasicPropertyAdaptSupport(this, component);
    }

  }

  /**
   * A <code>JMenu</code> that listens for changes of background color,
   * foreground color and font of the given <code>JComponent</code> and adapts
   * it's own settings.
   * <p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   */
  private static class PropertyChangeMenu extends JMenu {
    /**
     * Generated <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3256437027795973685L;

    /**
     * Creates an instance with the given name that listens to the components
     * background color, foreground color and font.
     * <p>
     *
     * @param name
     *          The name to display.
     *
     * @param component
     *          The component to whose background color this item will adapt.
     */
    public PropertyChangeMenu(final JComponent component, final String name) {
      super(name);
      new BasicPropertyAdaptSupport(this, component);
    }
  }

  /**
   * A <code>JMenuItem</code> that listens for changes of background color,
   * foreground color and font of the given <code>JComponent</code> and adapts
   * it's own settings.
   * <p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   */
  private static class PropertyChangeMenuItem extends JMenuItem {

    /**
     * Generated <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3690196534012752439L;

    /**
     * Creates an instance with the given name that listens to the components
     * background color, foreground color and font.
     * <p>
     *
     * @param component
     *          The component to whose background color this item will adapt.
     *
     * @param action
     *          The <code>Action</code> to trigger when this item is clicked.
     */
    public PropertyChangeMenuItem(final JComponent component, final Action action) {
      super(action);
      new BasicPropertyAdaptSupport(this, component);
    }
  }

  /**
   * <p>
   * A <code>JPopupMenu</code> that listens for changes of background color,
   * foreground color and font of the given <code>JComponent</code> and adapts
   * it's own settings.
   * </p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   */

  private static class PropertyChangePopupMenu extends JPopupMenu {

    /**
     * Generated <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3617013061525780016L;

    /**
     * @param component
     *          The component to whose background color this item will adapt.
     */
    public PropertyChangePopupMenu(final JComponent component) {
      new BasicPropertyAdaptSupport(this, component);
    }
  }

  /**
   * <p>
   * A <code>JLabel</code> that implements <code>ActionListener</code> in
   * order to change it's text color whenever the color of a corresponding
   * {@link ITrace2D}is changed.
   * </p>
   *
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   *
   */
  final class TraceJLabel extends JLabel implements PropertyChangeListener {

    /**
     * Generated <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3617290112636172342L;

    /**
     * Creates a label with the given name.
     * <p>
     *
     * @param name
     *          the name of the label.
     */
    public TraceJLabel(final String name) {
      super(name);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent evt) {
      String propertyName = evt.getPropertyName();
      if (propertyName.equals(ITrace2D.PROPERTY_COLOR)) {
        Color color = (Color) evt.getNewValue();
        this.setForeground(color);
      } else if (propertyName.equals(Chart2D.PROPERTY_BACKGROUND_COLOR)) {
        Color background = (Color) evt.getNewValue();
        this.setBackground(background);
      } else if (propertyName.equals(Chart2D.PROPERTY_FONT)) {
        Font font = (Font) evt.getNewValue();
        this.setFont(font);
      } else if (propertyName.equals(ITrace2D.PROPERTY_NAME)) {
        String name = (String) evt.getNewValue();
        this.setText(name);
      }
    }
  }

  /** The singleton instance of this factory. */
  private static LayoutFactory instance;

  /**
   * Singleton retrival method.
   * <p>
   *
   * @return the single instance of this factory within this VM.
   */
  public static LayoutFactory getInstance() {
    if (LayoutFactory.instance == null) {
      LayoutFactory.instance = new LayoutFactory();
    }
    return LayoutFactory.instance;
  }

  /**
   * Stroke names, quick hack - no "NamedStroke" subtype.
   */
  private String[] m_strokeNames;
  /**
   * Shared strokes.
   */
  private Stroke[] m_strokes;

  /**
   * Singleton constructor.
   * <p>
   */
  private LayoutFactory() {
    super();
    this.m_strokes = new Stroke[6];
    this.m_strokeNames = new String[6];
    this.m_strokes[0] = new BasicStroke();
    this.m_strokeNames[0] = "basic";
    this.m_strokes[1] = new BasicStroke(2);
    this.m_strokeNames[1] = "thick";
    this.m_strokes[2] = new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f,
        new float[] {0, 10f }, 0f);
    this.m_strokeNames[2] = "round caps";
    this.m_strokes[3] = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.5f,
        new float[] {5f, 5f }, 2.5f);
    this.m_strokeNames[3] = "dashed";
    this.m_strokes[4] = new BasicStroke(6, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 10.0f,
        new float[] {0, 10f }, 0f);
    this.m_strokeNames[4] = "square caps";
    this.m_strokes[5] = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.5f,
        new float[] {2f, 2f }, 1f);
    this.m_strokeNames[5] = "dashed thick";
  }

  /**
   * Adds a popup menu to the given chart that offers various controls over it
   * and it's {@link ITrace2D}instances.
   * <p>
   *
   * @param chart
   *          the chart to add the popup menue to.
   */
  public void createContextMenuLable(final Chart2D chart) {
    JPopupMenu popup = new PropertyChangePopupMenu(chart);
    // set the initial background color:
    Color background = chart.getBackground();
    // Background color menu
    JMenu bgColorMenu = new PropertyChangeMenu(chart, "Background color");
    JMenuItem item = new PropertyChangeMenuItem(chart, new JComponentActionSetBackground(chart,
        "White", Color.WHITE));
    item.setBackground(background);
    bgColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new JComponentActionSetBackground(chart, "Gray",
        Color.GRAY));
    bgColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new JComponentActionSetBackground(chart, "Light gray",
        Color.LIGHT_GRAY));
    bgColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new JComponentActionSetBackground(chart, "Black",
        Color.BLACK));
    bgColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new JComponentActionSetCustomBackground(chart,
        "Custom Color"));
    bgColorMenu.add(item);

    // Foreground color menu
    JMenu fgColorMenu = new PropertyChangeMenu(chart, "Foreground color");
    item = new PropertyChangeMenuItem(chart, new JComponentActionSetForeground(chart, "White",
        Color.WHITE));
    item.setBackground(background);
    fgColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new JComponentActionSetForeground(chart, "Gray",
        Color.GRAY));
    fgColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new JComponentActionSetForeground(chart, "Light gray",
        Color.LIGHT_GRAY));
    fgColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new JComponentActionSetForeground(chart, "Black",
        Color.BLACK));
    fgColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new JComponentActionSetCustomForeground(chart,
        "Custom Color"));
    fgColorMenu.add(item);

    // grid submenu
    JMenu gridMenu = new PropertyChangeMenu(chart, "Grid");
    gridMenu.setBackground(background);
    // show grid subsubmenu
    item = new PropertyChangeCheckBoxMenuItem(chart, new Chart2DActionSetGrid(chart, "Visible"));
    ((JCheckBoxMenuItem) item).setState(chart.getGridX() && chart.getGridY());

    gridMenu.add(item);
    // grid color subsubmenu
    JMenu gridColorMenu = new PropertyChangeMenu(chart, "Color");
    gridColorMenu.setBackground(background);
    item = new PropertyChangeMenuItem(chart, new Chart2DActionSetGridColor(chart, "Gray",
        Color.GRAY));
    gridColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Chart2DActionSetGridColor(chart, "Light gray",
        Color.LIGHT_GRAY));
    gridColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Chart2DActionSetGridColor(chart, "Black",
        Color.BLACK));
    gridColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Chart2DActionSetGridColor(chart, "White",
        Color.WHITE));
    gridColorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Chart2DActionSetCustomGridColor(chart, "Custom"));
    gridColorMenu.add(item);
    gridMenu.add(gridColorMenu);

    /*
     * Too many submenus, one is sufficient // gridx subsubmenu JMenu gridxMenu =
     * new JMenu("Grid X"); item = new JMenuItem(new
     * Chart2DActionSetGridX(chart, "on", true)); gridxMenu.add(item); item =
     * new JMenuItem(new Chart2DActionSetGridX(chart, "off", false));
     * gridxMenu.add(item); gridMenu.add(gridxMenu); // gridy subsubmenu JMenu
     * gridyMenu = new JMenu("Grid Y"); item = new JMenuItem(new
     * Chart2DActionSetGridY(chart, "on", true)); gridyMenu.add(item); item =
     * new JMenuItem(new Chart2DActionSetGridY(chart, "off", false));
     * gridyMenu.add(item); gridMenu.add(gridyMenu);
     */

    // fill top-level popup menu
    popup.add(bgColorMenu);
    popup.add(fgColorMenu);
    popup.add(gridMenu);
    chart.addMouseListener(new PopupListener(popup));
  }

  /**
   * <p>
   * Creates a <code>JLabel</code> that is capable of triggering a
   * <code>JPopupMenu</code> for the settings available for the
   * <code>ITrace2D</code>.
   * </p>
   *
   * @param chart
   *          The chart the given trace is a member of. This will be used for
   *          getting a <code>PopupMenu</code> that adapts to layout
   *          properties (such as background color).
   *
   * @param trace
   *          The trace on which the <code>JPopupMenu</code> of the
   *          <code>JLabel</code> will act.
   *
   * @return a label that offers a popup menue with controls for the given
   *         trace.
   */
  public JLabel createContextMenuLable(final Chart2D chart, final ITrace2D trace) {
    TraceJLabel ret = new TraceJLabel(trace.getLable());

    // ret.setSize(new Dimension(20, 100));
    JPopupMenu popup = new PropertyChangePopupMenu(chart);
    // set the initial background color:
    Color background = chart.getBackground();
    ret.setBackground(background);
    ret.setForeground(trace.getColor());

    // submenu for trace color
    JMenu colorMenu = new PropertyChangeMenu(chart, "Color");
    JMenuItem item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Red",
        Color.RED));
    colorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Green", Color.GREEN));
    colorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Blue", Color.BLUE));
    colorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Gray", Color.GRAY));
    colorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Magenta",
        Color.MAGENTA));
    colorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Pink", Color.PINK));
    colorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Black", Color.BLACK));
    colorMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionSetCustomColor(trace, "Custom", ret));
    colorMenu.add(item);

    // submenu for zIndex
    JMenu zIndexMenu = new PropertyChangeMenu(chart, "layer");
    zIndexMenu.setBackground(background);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionSetZindex(trace, "bring to front", 0));
    zIndexMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionSetZindex(trace, "send to back",
        ITrace2D.ZINDEX_MAX));
    zIndexMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionZindexDecrease(trace, "forward", 2));
    zIndexMenu.add(item);
    item = new PropertyChangeMenuItem(chart, new Trace2DActionZindexIncrease(trace, "backwards", 2));
    zIndexMenu.add(item);

    // item for setVisible
    item = new PropertyChangeCheckBoxMenuItem(chart, new Trace2DActionSetVisible(trace, "Visible"));
    ((JCheckBoxMenuItem) item).setState(trace.getVisible());
    popup.add(item);

    // item for setName
    item = new PropertyChangeMenuItem(chart, new Trace2DActionSetName(trace, "Name", chart));
    popup.add(item);

    // strokes
    JMenu strokesMenu = new PropertyChangeMenu(chart, "Stroke");
    for (int i = 0; i < this.m_strokes.length; i++) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetStroke(trace,
          this.m_strokeNames[i], this.m_strokes[i]));
      strokesMenu.add(item);
    }

    // add the submenus
    popup.add(colorMenu);
    popup.add(zIndexMenu);
    popup.add(strokesMenu);

    ret.addMouseListener(new PopupListener(popup));
    ret.setFont(chart.getFont());
    // set the initial background color:
    // ret.setBackground(chart.getBackground());
    // let the JLabel paint it's text in the color of the trace:
    trace.addPropertyChangeListener(AbstractTrace2D.PROPERTY_COLOR, ret);
    // let the JLabel adapt to display the name of the trace:
    trace.addPropertyChangeListener(ITrace2D.PROPERTY_NAME, ret);

    // let the Lable adapt to the background color of the chart:
    chart.addPropertyChangeListener(Chart2D.PROPERTY_BACKGROUND_COLOR, ret);
    // let the Lable adapt to the font of the chart.
    chart.addPropertyChangeListener(Chart2D.PROPERTY_FONT, ret);
    return ret;
  }
}
