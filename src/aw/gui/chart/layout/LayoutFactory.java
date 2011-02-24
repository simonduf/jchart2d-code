/*
 *
 *  LayoutFactory.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 19.05.2005, 20:26:00
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
package aw.gui.chart.layout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import aw.gui.chart.AbstractTrace2D;
import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.ITracePainter;
import aw.gui.chart.RangePolicyFixedViewport;
import aw.gui.chart.RangePolicyForcedPoint;
import aw.gui.chart.RangePolicyHighestValues;
import aw.gui.chart.RangePolicyUnbounded;
import aw.gui.chart.TracePainterDisc;
import aw.gui.chart.TracePainterFill;
import aw.gui.chart.TracePainterPolyline;
import aw.gui.chart.event.AxisActionSetRange;
import aw.gui.chart.event.AxisActionSetRangePolicy;
import aw.gui.chart.event.Chart2DActionSetCustomGridColor;
import aw.gui.chart.event.Chart2DActionSetGridColor;
import aw.gui.chart.event.Chart2DActionSetGridX;
import aw.gui.chart.event.Chart2DActionSetGridY;
import aw.gui.chart.event.JComponentActionSetBackground;
import aw.gui.chart.event.JComponentActionSetCustomBackground;
import aw.gui.chart.event.JComponentActionSetCustomForeground;
import aw.gui.chart.event.JComponentActionSetForeground;
import aw.gui.chart.event.PopupListener;
import aw.gui.chart.event.Trace2DActionAddRemoveTracePainter;
import aw.gui.chart.event.Trace2DActionSetColor;
import aw.gui.chart.event.Trace2DActionSetCustomColor;
import aw.gui.chart.event.Trace2DActionSetName;
import aw.gui.chart.event.Trace2DActionSetStroke;
import aw.gui.chart.event.Trace2DActionSetVisible;
import aw.gui.chart.event.Trace2DActionSetZindex;
import aw.gui.chart.event.Trace2DActionZindexDecrease;
import aw.gui.chart.event.Trace2DActionZindexIncrease;
import aw.util.Range;

/**
 * Factory that provides creational methods for adding popup menues to the
 * {@link aw.gui.chart.Chart2D} and obtaining popup menu decorated
 * {@link javax.swing.JLabel} instances for {@link aw.gui.chart.ITrace2D}
 * instances.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.10 $
 */
public final class LayoutFactory {

  /**
   * <p>
   * Implementation for a <code>PropertyChangeListener</code> that adpapts a
   * wrapped <code>JComponent</code> to the following corporate UI properties.
   * <p>
   * 
   * <ul>
   * <li>background color</li>
   * <li>foreground color (text)</li>
   * <li>font</li>
   * </ul>
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  private static class BasicPropertyAdaptSupport implements PropertyChangeListener {

    /** The weak reference to the component to adapt properties on. */
    private WeakReference m_delegate;

    /** The component to whose properties the delegate adapts to. */
    private JComponent m_adaptee;

    /**
     * 
     * @param delegate
     *          The component to adapt the properties on.
     * @param adaptee
     *          The peer component delegate will be adapted to.
     */
    public BasicPropertyAdaptSupport(final JComponent delegate, final JComponent adaptee) {
      this.m_delegate = new WeakReference(delegate);
      this.m_adaptee = adaptee;
      delegate.setFont(adaptee.getFont());
      delegate.setBackground(adaptee.getBackground());
      delegate.setForeground(adaptee.getForeground());
      adaptee.addPropertyChangeListener(this);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent evt) {
      String prop = evt.getPropertyName();
      Object instance = this.m_delegate.get();
      if (instance != null) {
        JComponent component = (JComponent) instance;
        if (prop.equals(Chart2D.PROPERTY_BACKGROUND_COLOR)) {
          Color color = (Color) evt.getNewValue();

          component.setBackground(color);
        } else if (prop.equals(Chart2D.PROPERTY_FONT)) {
          Font font = (Font) evt.getNewValue();
          component.setFont(font);
        } else if (prop.equals(Chart2D.PROPERTY_FOREGROUND_COLOR)) {
          Color color = (Color) evt.getNewValue();
          component.setForeground(color);
        }
      } else {
        // if no more components to adapt, remove myself as a listener
        // to avoid mem-leak in listener list:
        this.m_adaptee.removePropertyChangeListener(this);
      }
    }
  }

  /**
   * A checkbox menu item that will change it's order in the known {@link JMenu}
   * it is contained in whenever it's state changes.
   * <p>
   * 
   * Whenever it is deselected it is put to the end, whenever it is selected it
   * will put itself to the top. Not very perfomant but close to minimal code.
   * <p>
   */
  private static class OrderingCheckBoxMenuItem extends
      LayoutFactory.PropertyChangeCheckBoxMenuItem {

    /**
     * Enriches a wrapped {@link Action} by the service of ordering it's
     * corresponding {@link JMenuItem} in it's {@link JMenu} according to the
     * description of {@link LayoutFactory.OrderingCheckBoxMenuItem}.
     * <p>
     * 
     * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
     * 
     * 
     * @version $Revision: 1.10 $
     */
    private final class JMenuOrderingAction extends AbstractAction {

      /**
       * Generated <code>serialVersionUID</code>.
       */
      private static final long serialVersionUID = 3835159462649672505L;

      /**
       * The action that is enriched by the service of ordering it's
       * corresponding {@link JMenuItem} in it's {@link JMenu} according to the
       * description of {@link LayoutFactory.OrderingCheckBoxMenuItem}.
       */
      private Action m_action;

      /**
       * Creates an instance delegating to the given action and adding the
       * ordering service described above.
       * <p>
       * 
       * @param delegate
       *          the action that is enriched by the service of ordering it's
       *          corresponding {@link JMenuItem} in it's {@link JMenu}
       *          according to the description of
       *          {@link LayoutFactory.OrderingCheckBoxMenuItem}.
       * 
       */
      private JMenuOrderingAction(final Action delegate) {
        this.m_action = delegate;
      }

      /**
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(final ActionEvent e) {
        this.m_action.actionPerformed(e);
        // my service:
        JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
        boolean state = item.getState();
        if (state) {
          LayoutFactory.OrderingCheckBoxMenuItem.this.m_menu
              .remove(LayoutFactory.OrderingCheckBoxMenuItem.this);
          LayoutFactory.OrderingCheckBoxMenuItem.this.m_menu.add(
              LayoutFactory.OrderingCheckBoxMenuItem.this, 0);
        } else {
          LayoutFactory.OrderingCheckBoxMenuItem.this.m_menu
              .remove(LayoutFactory.OrderingCheckBoxMenuItem.this);
          LayoutFactory.OrderingCheckBoxMenuItem.this.m_menu
              .add(LayoutFactory.OrderingCheckBoxMenuItem.this);
        }

      }

      /**
       * @see javax.swing.AbstractAction#addPropertyChangeListener(java.beans.PropertyChangeListener)
       */
      public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.m_action.addPropertyChangeListener(listener);
      }

      /**
       * @see java.lang.Object#equals(java.lang.Object)
       */
      public boolean equals(final Object obj) {
        return this.m_action.equals(obj);
      }

      /**
       * @see javax.swing.AbstractAction#getValue(java.lang.String)
       */
      public Object getValue(final String key) {
        return this.m_action.getValue(key);
      }

      /**
       * @see java.lang.Object#hashCode()
       */
      public int hashCode() {
        return this.m_action.hashCode();
      }

      /**
       * @see javax.swing.AbstractAction#isEnabled()
       */
      public boolean isEnabled() {
        return this.m_action.isEnabled();
      }

      /**
       * @see javax.swing.AbstractAction#putValue(java.lang.String,
       *      java.lang.Object)
       */
      public void putValue(final String key, final Object value) {
        this.m_action.putValue(key, value);
      }

      /**
       * @see javax.swing.AbstractAction#removePropertyChangeListener(java.beans.PropertyChangeListener)
       */
      public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.m_action.removePropertyChangeListener(listener);
      }

      /**
       * @see javax.swing.AbstractAction#setEnabled(boolean)
       */
      public void setEnabled(final boolean b) {
        this.m_action.setEnabled(b);
      }

      /**
       * @see java.lang.Object#toString()
       */
      public String toString() {
        return this.m_action.toString();
      }
    }

    /**
     * Generated <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3834870273894857017L;

    /** The menu to control this items order within. */
    private JMenu m_menu;

    /**
     * Creates an instance that will adapt it's own basic UI properties to the
     * given component, trigger the given action upon checkbox selection /
     * deselection and order itself in the given menu as described in the class
     * comment.
     * <p>
     * 
     * @param component
     *          the component to adapt basic UI properties to.
     * 
     * @param action
     *          the action to trigger.
     * 
     * @param container
     *          the instance this menu item is contained in.
     * 
     * @see LayoutFactory.PropertyChangeCheckBoxMenuItem
     */
    public OrderingCheckBoxMenuItem(final JComponent component, final Action action,
        final JMenu container) {
      super(component);
      this.m_menu = container;
      super.setAction(new JMenuOrderingAction(action));
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
     * Internal constructor that should not be used unless
     * {@link javax.swing.AbstractButton#setAction(javax.swing.Action)} is
     * invoked afterwards on this instance (else NPE!).
     * <p>
     * 
     * @param component
     *          The component to whose basic UI properties this item will adapt.
     */
    protected PropertyChangeCheckBoxMenuItem(final JComponent component) {
      this(component, null);
    }

    /**
     * Creates an instance with the given name that listens to the components
     * background color, foreground color and font.
     * <p>
     * 
     * The source of the {@link java.awt.event.ActionEvent} given to the
     * {@link Action} ({@link java.util.EventObject#getSource()}) will be of
     * type {@link JCheckBoxMenuItem}- the state (selected / deselected) may be
     * obtained from it.
     * <p>
     * 
     * @param component
     *          The component to whose basic UI properties this item will adapt.
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
   * A <code>JRadioButtonMenuItem</code> that listens for changes of
   * background color, foreground color and font of the given
   * <code>JComponent</code> and adapts it's own settings.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  private static class PropertyChangeJRadioButtonMenuItem extends JRadioButtonMenuItem {

    /**
     * Generated <code>serial version UID</code>.
     */
    private static final long serialVersionUID = 2047135529480385910L;

    /**
     * Internal constructor that should not be used unless
     * {@link javax.swing.AbstractButton#setAction(javax.swing.Action)} is
     * invoked afterwards on this instance (else NPE!).
     * <p>
     * 
     * @param component
     *          The component to whose basic UI properties this item will adapt.
     */
    protected PropertyChangeJRadioButtonMenuItem(final JComponent component) {
      this(component, null);
    }

    /**
     * Creates an instance with the given name that listens to the components
     * background color, foreground color and font.
     * <p>
     * 
     * The source of the {@link java.awt.event.ActionEvent} given to the
     * {@link Action} ({@link java.util.EventObject#getSource()}) will be of
     * type {@link JRadioButtonMenuItem}.
     * <p>
     * 
     * @param component
     *          The component to whose basic UI properties this item will adapt.
     * 
     * @param action
     *          The <code>Action</code> to trigger when this item is clicked.
     */
    public PropertyChangeJRadioButtonMenuItem(final JComponent component, final Action action) {
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
   * {@link ITrace2D} is changed.
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
   * and it's {@link ITrace2D} instances.
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

    // Axis submenu:
    JMenu axisMenu = new PropertyChangeMenu(chart, "Axis");

    // X axis submenu
    JMenuItem xAxisMenuItem = new PropertyChangeMenu(chart, "Axis X");
    xAxisMenuItem.setBackground(background);
    axisMenu.add(xAxisMenuItem);

    // Axis x -> Range policy submenu
    JMenuItem xAxisRangePolicy = new PropertyChangeMenu(chart, "Range policy");
    xAxisMenuItem.add(xAxisRangePolicy);
    // Use a button group to control unique selection state of radio buttons:
    ButtonGroup buttonGroup = new ButtonGroup();
    // check the default selected item:
    Class rangePolicyClass = chart.getAxisX().getRangePolicy().getClass();
    item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart
        .getAxisX(), "Fixed viewport", new RangePolicyFixedViewport(new Range(0, 100))));
    xAxisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyFixedViewport.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart
        .getAxisX(), "Non-clipping viewport", new RangePolicyUnbounded(new Range(0, 100))));
    xAxisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyUnbounded.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart
        .getAxisX(), "Ensure visible point", new RangePolicyForcedPoint(0)));
    item.setToolTipText("Only the minimum value of the axis' range will be ensured to be visible.");
    xAxisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyForcedPoint.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart
        .getAxisX(), "Highest points within max-50 to max.", new RangePolicyHighestValues(
        new Range(0, 100), 50)));
    item.setToolTipText("Shows the highest values from max-50 to max.");
    xAxisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyHighestValues.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    // Axis x -> Range menu
    item = new PropertyChangeMenuItem(chart, new AxisActionSetRange(chart.getAxisX(), "Range"));
    xAxisMenuItem.add(item);

    // Axis x -> show grid submenu
    item = new PropertyChangeCheckBoxMenuItem(chart, new Chart2DActionSetGridX(chart, "Grid"));
    xAxisMenuItem.add(item);

    // Y axis submenu
    JMenuItem yAxisMenuItem = new PropertyChangeMenu(chart, "Axis Y");
    yAxisMenuItem.setBackground(background);
    axisMenu.add(yAxisMenuItem);

    // Axis y -> Range policy submenu
    JMenuItem yAxisRangePolicy = new PropertyChangeMenu(chart, "Range policy");
    yAxisMenuItem.add(yAxisRangePolicy);

    // initial selection management
    rangePolicyClass = chart.getAxisY().getRangePolicy().getClass();
    // Use a button group to control unique selection state of radio buttons:
    buttonGroup = new ButtonGroup();
    item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart
        .getAxisY(), "Fixed viewport", new RangePolicyFixedViewport(new Range(0, 100))));
    yAxisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == AxisActionSetRangePolicy.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart
        .getAxisY(), "Non-clipping viewport", new RangePolicyUnbounded(new Range(0, 100))));
    yAxisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyUnbounded.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart
        .getAxisY(), "Ensure visible point", new RangePolicyForcedPoint(0)));
    item.setToolTipText("Only the minimum value of the axis' range will be ensured to be visible.");
    yAxisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyForcedPoint.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart
        .getAxisY(), "Highest points within max-50 to max.", new RangePolicyHighestValues(
        new Range(0, 100), 50)));
    item.setToolTipText("Shows the highest values from max-50 to max.");
    yAxisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyHighestValues.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }
    // Axis y -> Range menu
    item = new PropertyChangeMenuItem(chart, new AxisActionSetRange(chart.getAxisY(), "Range"));
    yAxisMenuItem.add(item);

    // Axis y -> show grid submenu
    item = new PropertyChangeCheckBoxMenuItem(chart, new Chart2DActionSetGridY(chart, "Grid"));
    yAxisMenuItem.add(item);

    // grid color subsubmenu for Axis submenu
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
    axisMenu.add(gridColorMenu);

    // fill top-level popup menu
    popup.add(bgColorMenu);
    popup.add(fgColorMenu);
    popup.add(axisMenu);
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

    // trace painters
    JMenu painterMenu = new PropertyChangeMenu(chart, "renderer");
    ITracePainter painter = new TracePainterDisc(4);
    item = new OrderingCheckBoxMenuItem(chart, new Trace2DActionAddRemoveTracePainter(trace,
        "discs", painter), painterMenu);
    if (trace.getTracePainters().contains(painter)) {
      item.setSelected(true);
    }
    painterMenu.add(item);

    painter = new TracePainterPolyline();
    item = new OrderingCheckBoxMenuItem(chart, new Trace2DActionAddRemoveTracePainter(trace,
        "line", painter), painterMenu);
    painterMenu.add(item);
    if (trace.getTracePainters().contains(painter)) {
      item.setSelected(true);
    }

    painter = new TracePainterFill(chart);
    item = new OrderingCheckBoxMenuItem(chart, new Trace2DActionAddRemoveTracePainter(trace,
        "fill", painter), painterMenu);
    painterMenu.add(item);
    if (trace.getTracePainters().contains(painter)) {
      item.setSelected(true);
    }

    // add the submenus
    popup.add(colorMenu);
    popup.add(zIndexMenu);
    popup.add(strokesMenu);
    popup.add(painterMenu);

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
