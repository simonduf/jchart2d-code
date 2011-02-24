/*
 *  LayoutFactory.java  jchart2d, factory for creating user interface 
 *  controls for charts and traces. 
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
package info.monitorenter.gui.chart.controls;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePainter;
import info.monitorenter.gui.chart.axis.AxisLinear;
import info.monitorenter.gui.chart.axis.AxisLog10;
import info.monitorenter.gui.chart.axis.AxisLogE;
import info.monitorenter.gui.chart.events.AxisActionSetGrid;
import info.monitorenter.gui.chart.events.AxisActionSetRange;
import info.monitorenter.gui.chart.events.AxisActionSetRangePolicy;
import info.monitorenter.gui.chart.events.Chart2DActionSaveImageSingleton;
import info.monitorenter.gui.chart.events.Chart2DActionSetAxis;
import info.monitorenter.gui.chart.events.Chart2DActionSetCustomGridColorSingleton;
import info.monitorenter.gui.chart.events.Chart2DActionSetGridColor;
import info.monitorenter.gui.chart.events.JComponentActionSetBackground;
import info.monitorenter.gui.chart.events.JComponentActionSetCustomBackgroundSingleton;
import info.monitorenter.gui.chart.events.JComponentActionSetCustomForegroundSingleton;
import info.monitorenter.gui.chart.events.JComponentActionSetForeground;
import info.monitorenter.gui.chart.events.PopupListener;
import info.monitorenter.gui.chart.events.Trace2DActionAddRemoveTracePainter;
import info.monitorenter.gui.chart.events.Trace2DActionSetColor;
import info.monitorenter.gui.chart.events.Trace2DActionSetCustomColor;
import info.monitorenter.gui.chart.events.Trace2DActionSetName;
import info.monitorenter.gui.chart.events.Trace2DActionSetStroke;
import info.monitorenter.gui.chart.events.Trace2DActionSetVisible;
import info.monitorenter.gui.chart.events.Trace2DActionSetZindex;
import info.monitorenter.gui.chart.events.Trace2DActionZindexDecrease;
import info.monitorenter.gui.chart.events.Trace2DActionZindexIncrease;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyForcedPoint;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyHighestValues;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyMinimumViewport;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyUnbounded;
import info.monitorenter.gui.chart.traces.painters.TracePainterDisc;
import info.monitorenter.gui.chart.traces.painters.TracePainterFill;
import info.monitorenter.gui.chart.traces.painters.TracePainterPolyline;
import info.monitorenter.gui.chart.traces.painters.TracePainterVerticalBar;
import info.monitorenter.util.Range;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

/**
 * Factory that provides creational methods for adding UI controls to
 * {@link info.monitorenter.gui.chart.Chart2D} instances and
 * {@link info.monitorenter.gui.chart.ITrace2D} instances.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.11 $
 */
public final class LayoutFactory {

  /**
   * Implementation for a <code>PropertyChangeListener</code> that adpapts a
   * wrapped <code>JComponent</code> to the following properties.
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
  public static class BasicPropertyAdaptSupport implements PropertyChangeListener {

    /**
     * The component to whose properties the delegate adapts to.
     * <p>
     * This is not needed to read the properties of because the fired change
     * events contain the re
     * 
     */
    private Component m_adaptee;

    /** The weak reference to the component to adapt properties on. */
    protected WeakReference m_delegate;

    /**
     * 
     * @param delegate
     *          The component to adapt the properties on.
     * @param adaptee
     *          The peer component delegate will be adapted to.
     */
    public BasicPropertyAdaptSupport(final Component delegate, final Component adaptee) {
      this.m_delegate = new WeakReference(delegate);
      this.m_adaptee = adaptee;
      delegate.setFont(adaptee.getFont());
      delegate.setBackground(adaptee.getBackground());
      delegate.setForeground(adaptee.getForeground());
      adaptee.addPropertyChangeListener(this);
    }

    /**
     * Removes the listener for basic property changes from the component to
     * adapt to.
     * <p>
     * 
     * @see java.lang.Object#finalize()
     * 
     * @throws Throwable
     *           if sth. goes wrong cleaning up.
     */
    public void finalize() throws Throwable {
      super.finalize();
      this.m_adaptee.removePropertyChangeListener(this);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent evt) {
      String prop = evt.getPropertyName();
      Object reference = this.m_delegate.get();
      if (reference != null) {
        Component component = (Component) reference;
        if (prop.equals(Chart2D.PROPERTY_BACKGROUND_COLOR)) {
          Color color = (Color) evt.getNewValue();
          Color foreground = component.getForeground();
          if (color.equals(foreground)) {
            component.setForeground(component.getBackground());
          }
          component.setBackground(color);
          component.repaint();
        } else if (prop.equals(Chart2D.PROPERTY_FONT)) {
          Font font = (Font) evt.getNewValue();
          component.setFont(font);
        } else if (prop.equals(Chart2D.PROPERTY_FOREGROUND_COLOR)) {
          Color color = (Color) evt.getNewValue();
          Color background = component.getBackground();
          if (color.equals(background)) {
            component.setBackground(component.getForeground());
          }
          component.setForeground(color);
        }
      } else {
        // if no more components to adapt, remove myself as a listener
        // to avoid mem-leak in listener list:
        ((Component) evt.getSource()).removePropertyChangeListener(this);
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
  private static class OrderingCheckBoxMenuItem
      extends JCheckBoxMenuItem {

    /**
     * Enriches a wrapped {@link Action} by the service of ordering it's
     * corresponding {@link JMenuItem} in it's {@link JMenu} according to the
     * description of {@link LayoutFactory.OrderingCheckBoxMenuItem}.
     * <p>
     * 
     * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
     * 
     * 
     * @version $Revision: 1.11 $
     */
    private final class JMenuOrderingAction
        extends AbstractAction {

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
     * Creates an instance that will trigger the given action upon checkbox
     * selection / deselection and order itself in the given menu as described
     * in the class comment.
     * <p>
     * 
     * @param action
     *          the action to trigger.
     * 
     * @param container
     *          the instance this menu item is contained in.
     * 
     * @see LayoutFactory.PropertyChangeCheckBoxMenuItem
     */
    public OrderingCheckBoxMenuItem(final Action action, final JMenu container,
        final boolean checked) {
      super();
      this.setSelected(checked);
      this.m_menu = container;
      if (action != null) {
        action.addPropertyChangeListener(new SelectionPropertyAdaptSupport(this));
      }
      super.setAction(new JMenuOrderingAction(action));
    }
  }

  /**
   * A checkbox menu item that will change it's order in the known {@link JMenu}
   * it is contained in whenever it's state changes (see superclass) and
   * additionally adapt basic UI propierties font, foreground color, background
   * color to the constructor given component.
   * <p>
   * 
   * Whenever it is deselected it is put to the end, whenever it is selected it
   * will put itself to the top. Not very perfomant but close to minimal code.
   * <p>
   */
  private static class OrderingCheckBoxPropertyChangeMenuItem
      extends LayoutFactory.OrderingCheckBoxMenuItem {

    /** Generated <code>serial version UID</code>. */
    private static final long serialVersionUID = 3889088574130596540L;

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
    public OrderingCheckBoxPropertyChangeMenuItem(final JComponent component, final Action action,
        final JMenu container, final boolean checked) {
      super(action, container, checked);
      new BasicPropertyAdaptSupport(this, component);
    }

  }

  /**
   * A <code>JCheckBoxMenuItem</code> that listens for changes of background
   * color, foreground color and font of the given <code>JComponent</code> and
   * adapts it's own settings.
   * <p>
   * Additionally - as this item has a state - it is possible to let the state
   * be changed from outside (unlike only changing it from the UI): Sth. that
   * seems to have been forgotten in the java implementation. It's state ({@link JCheckBoxMenuItem#setState(boolean)},
   * {@link javax.swing.AbstractButton#setSelected(boolean)}) listens on
   * property {@link #PROPERTY_SELECTED} for changes of the state. These events
   * are normally fired by the custom {@link Action} implementations like
   * {@link Chart2DActionSetAxis}.
   * <p>
   * Instances register themselves to receive events from the action given to
   * their constructor.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  public static class PropertyChangeCheckBoxMenuItem
      extends JCheckBoxMenuItem {

    /** The property identifying a change of selection. */
    public static final String PROPERTY_SELECTED = "";

    /**
     * Generated <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 3690196534012752439L;

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
     * 
     * @param checked
     *          the inital state of the checkbox.
     */
    public PropertyChangeCheckBoxMenuItem(final JComponent component, final Action action,
        final boolean checked) {
      super(action);
      this.setState(checked);
      new BasicPropertyAdaptSupport(this, component);
      if (action != null) {
        action.addPropertyChangeListener(new SelectionPropertyAdaptSupport(this));
      }
    }

    /**
     * Internal constructor that should not be used unless
     * {@link javax.swing.AbstractButton#setAction(javax.swing.Action)} is
     * invoked afterwards on this instance (else NPE!).
     * <p>
     * 
     * @param component
     *          The component to whose basic UI properties this item will adapt.
     * 
     * @param checked
     *          the inital state of the checkbox.
     * 
     */
    protected PropertyChangeCheckBoxMenuItem(final JComponent component, final boolean checked) {
      this(component, null, checked);
    }

    /**
     * @see javax.swing.AbstractButton#setAction(javax.swing.Action)
     */
    public void setAction(final Action a) {
      if (a != null) {
        super.setAction(a);
        a.addPropertyChangeListener(new SelectionPropertyAdaptSupport(this));
      }
    }
  }

  /**
   * A <code>JPopupMenu</code> that listens for changes of background color,
   * foreground color and font of the given <code>JComponent</code> and adapts
   * it's own settings.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */

  private static class PropertyChangeJMenuBar
      extends JMenuBar {

    /**
     * Generated <code>serial version UID</code>.
     * <p>
     */
    private static final long serialVersionUID = -332246962640911539L;

    /**
     * @param component
     *          The component to whose background color this item will adapt.
     */
    public PropertyChangeJMenuBar(final JComponent component) {
      new BasicPropertyAdaptSupport(this, component);
    }
  }

  /**
   * A <code>JRadioButtonMenuItem</code> that listens for changes of
   * background color, foreground color and font of the given
   * <code>JComponent</code> and adapts it's own settings.
   * <p>
   * 
   * Additionally - as this item has a state - it is possible to let the state
   * be changed from outside (unlike only changing it from the UI): Sth. that
   * seems to have been forgotten in the java implementation. It's state ({@link JCheckBoxMenuItem#setState(boolean)},
   * {@link javax.swing.AbstractButton#setSelected(boolean)}) listens on
   * property {@link #PROPERTY_SELECTED} for changes of the state. These events
   * are normally fired by the custom {@link Action} implementations like
   * {@link Chart2DActionSetAxis}.
   * <p>
   * Instances register themselves to receive events from the action given to
   * their constructor.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  private static class PropertyChangeJRadioButtonMenuItem
      extends JRadioButtonMenuItem {

    /**
     * Generated <code>serial version UID</code>.
     * <p>
     */
    private static final long serialVersionUID = 3933408706693522564L;

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
      if (action != null) {
        action.addPropertyChangeListener(new SelectionPropertyAdaptSupport(this));
      }

    }

    /**
     * @see javax.swing.AbstractButton#setAction(javax.swing.Action)
     */
    public void setAction(final Action a) {
      if (a != null) {
        super.setAction(a);
        a.addPropertyChangeListener(new SelectionPropertyAdaptSupport(this));
      }
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
  private static class PropertyChangeMenu
      extends JMenu {
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
      /*
       * For java 1.5 menus that are submenus don't use background color in
       * ocean theme: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5097866
       * 
       * workaround:
       */
      this.setOpaque(true);
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
  private static class PropertyChangeMenuItem
      extends JMenuItem {

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
   * A <code>JPopupMenu</code> that listens for changes of background color,
   * foreground color and font of the given <code>JComponent</code> and adapts
   * it's own settings.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */

  private static class PropertyChangePopupMenu
      extends JPopupMenu {

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
   * A <code>JCheckBoxMenuItem</code> that listens on it's assigned
   * <code>Action</code> for selection changes.
   * <p>
   * 
   * As this item has a state - it is possible to let the state be changed from
   * outside (unlike only changing it from the UI): Sth. that seems to have been
   * forgotten in the java implementation. It's state ({@link JCheckBoxMenuItem#setState(boolean)},
   * {@link javax.swing.AbstractButton#setSelected(boolean)}) listens on
   * property {@link #PROPERTY_SELECTED} for changes of the state. These events
   * are normally fired by the custom {@link Action} implementations like
   * {@link Chart2DActionSetAxis}.
   * <p>
   * Instances register themselves to receive events from the action given to
   * their constructor.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  private static class SelectionAdaptJCheckBoxMenuItem
      extends JCheckBoxMenuItem {

    /**
     * Generated <code>serial version UID</code>.
     * <p>
     */
    private static final long serialVersionUID = 5737559379056167605L;

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
     * @param action
     *          The <code>Action</code> to trigger when this item is clicked.
     * 
     * @param state
     *          the initial state of the checkbox.
     */
    public SelectionAdaptJCheckBoxMenuItem(final Action action, final boolean state) {
      super(action);
      this.setSelected(state);
      if (action != null) {
        action.addPropertyChangeListener(new SelectionPropertyAdaptSupport(this));
      }
    }

    /**
     * @see javax.swing.AbstractButton#setAction(javax.swing.Action)
     */
    public void setAction(final Action a) {
      if (a != null) {
        super.setAction(a);
        a.addPropertyChangeListener(new SelectionPropertyAdaptSupport(this));
      }
    }

  }

  /**
   * A <code>JRadioButtonMenuItem</code> that listens on it's assigned
   * <code>Action</code> for selection changes.
   * <p>
   * 
   * As this item has a state - it is possible to let the state be changed from
   * outside (unlike only changing it from the UI): Sth. that seems to have been
   * forgotten in the java implementation. It's state ({@link JCheckBoxMenuItem#setState(boolean)},
   * {@link javax.swing.AbstractButton#setSelected(boolean)}) listens on
   * property {@link #PROPERTY_SELECTED} for changes of the state. These events
   * are normally fired by the custom {@link Action} implementations like
   * {@link Chart2DActionSetAxis}.
   * <p>
   * Instances register themselves to receive events from the action given to
   * their constructor.
   * <p>
   * 
   * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
   * 
   */
  private static class SelectionAdaptJRadioButtonMenuItem
      extends JRadioButtonMenuItem {

    /**
     * Generated <code>serial version UID</code>.
     * <p>
     */
    private static final long serialVersionUID = 6949450166704804365L;

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
     * @param action
     *          The <code>Action</code> to trigger when this item is clicked.
     */
    public SelectionAdaptJRadioButtonMenuItem(final Action action) {
      super(action);
      if (action != null) {
        action.addPropertyChangeListener(new SelectionPropertyAdaptSupport(this));
      }
    }

    /**
     * @see javax.swing.AbstractButton#setAction(javax.swing.Action)
     */
    public void setAction(final Action a) {
      if (a != null) {
        super.setAction(a);
        a.addPropertyChangeListener(new SelectionPropertyAdaptSupport(this));
      }
    }

  }

  /**
   * <p>
   * Implementation for a <code>PropertyChangeListener</code> that adpapts a
   * wrapped <code>JComponent</code> to the following properties.
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
  public static class SelectionPropertyAdaptSupport implements PropertyChangeListener {

    /** The model to adapt selection upon. */
    protected WeakReference m_delegate;

    /**
     * 
     * @param delegate
     *          The component to adapt the properties on.
     */
    public SelectionPropertyAdaptSupport(final AbstractButton delegate) {
      this.m_delegate = new WeakReference(delegate);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent evt) {
      String prop = evt.getPropertyName();
      AbstractButton button = (AbstractButton) this.m_delegate.get();
      if (button != null) {
        if (prop.equals(LayoutFactory.PropertyChangeCheckBoxMenuItem.PROPERTY_SELECTED)) {
          boolean state = ((Boolean) evt.getNewValue()).booleanValue();
          button.setSelected(state);
        }
      } else {
        ((Component) evt.getSource()).removePropertyChangeListener(this);
      }
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
  final class TraceJLabel
      extends JLabel implements PropertyChangeListener {

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
        Color background = this.getBackground();
        if (color.equals(background)) {
          this.setBackground(this.getForeground());
        }
        this.setForeground(color);
      } else if (propertyName.equals(Chart2D.PROPERTY_BACKGROUND_COLOR)) {
        Color background = (Color) evt.getNewValue();
        Color foreground = this.getForeground();
        if (background.equals(foreground)) {
          this.setForeground(this.getBackground());
        }
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
   * Boolean flag that controls showing the show grid menu item for the x axis.
   */
  private boolean m_showAxisXGridMenu = true;

  /** Boolean flag that turns on showing the x axis menu. */
  private boolean m_showAxisXMenu = true;

  /**
   * Boolean flag that controls showing the range policy submenu and range menu
   * item on the x axis.
   */
  private boolean m_showAxisXRangePolicyMenu;

  /** Boolean flag that turns on showing the x axis type menu. */
  private boolean m_showAxisXTypeMenu = true;

  /**
   * Boolean flag that controls showing the show grid menu item for the y axis.
   */
  private boolean m_showAxisYGridMenu = true;

  /** Boolean flag that turns on showing the x axis menu. */
  private boolean m_showAxisYMenu = true;

  /**
   * Boolean flag that controls showing the range policy submenu and range menu
   * item on the y axis.
   */
  private boolean m_showAxisYRangePolicyMenu = true;

  /** Boolean flag that turns on showing the y axis type menu. */
  private boolean m_showAxisYTypeMenu = true;

  /** Boolean flag that turns on showing the chart background color menu. */
  private boolean m_showChartBackgroundMenu = true;

  /** Boolean flag that turns on showing the chart foreground color menu. */
  private boolean m_showChartForegroundMenu = true;

  /** Boolean flag that turns on showing the grid color menu. */
  private boolean m_showGridColorMenu = true;

  /** Boolean flag that controls showing the save to image menu item. */
  private boolean m_showSaveImageMenu = true;

  /** Boolean flag that controls showing the color menu for traces. */
  private boolean m_showTraceColorMenu = true;

  /** Boolean flag that controls showing the set name menu item for traces. */
  private boolean m_showTraceNameMenu = true;

  /** Boolean flag that controls showing the trace painter menu for traces. */
  private boolean m_showTracePainterMenu = true;

  /** Boolean flag that controls showing the stroke menu for traces. */
  private boolean m_showTraceStrokeMenu = true;

  /** Boolean flag that controls showing the set visible menu item for traces. */
  private boolean m_showTraceVisibleMenu = true;

  /** Boolean flag that controls showing the z-index menu for traces. */
  private boolean m_showTraceZindexMenu = true;

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
        new float[] {10f, 2f }, 1f);
    this.m_strokeNames[5] = "dashed thick";
  }

  /**
   * Creates a {@link JMenuItem} that allows to trigger the features related to
   * {@link info.monitorenter.gui.chart.axis.AAxis} features.
   * <p>
   * 
   * @param axis
   *          the axis to control.
   * 
   * @param axisDimension
   *          Identifies which dimension the axis controls in the chart: either
   *          {@link Chart2D#X} or {@link Chart2D#Y}
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * 
   * @return a {@link JMenuItem} that allows to trigger the features related to
   *         {@link info.monitorenter.gui.chart.axis.AAxis} features.
   */
  protected JMenuItem createAxisMenuItem(final IAxis axis, final int axisDimension,
      final boolean adaptUI2Chart) {
    final Chart2D chart = axis.getAccessor().getChart();
    JMenuItem item;

    // axis submenu
    JMenuItem axisMenuItem;
    if (adaptUI2Chart) {
      axisMenuItem = new PropertyChangeMenu(chart, "Axis" + axis.getAccessor().toString());
    } else {
      axisMenuItem = new JMenu("Axis" + axis.getAccessor().toString());
    }

    if ((this.m_showAxisXTypeMenu && axisDimension == Chart2D.X)
        || (this.m_showAxisYTypeMenu && axisDimension == Chart2D.Y)) {
      axisMenuItem.add(createAxisTypeMenu(chart, axis, axisDimension, adaptUI2Chart));
    }
    if ((this.m_showAxisXRangePolicyMenu && axisDimension == Chart2D.X)
        || this.m_showAxisYRangePolicyMenu && axisDimension == Chart2D.Y) {
      axisMenuItem.add(createAxisRangePolicyMenu(chart, axis, adaptUI2Chart));

      // Axis -> Range menu
      if (adaptUI2Chart) {
        item = new PropertyChangeMenuItem(chart, new AxisActionSetRange(chart, "Range",
            axisDimension));
      } else {
        item = new JMenuItem(new AxisActionSetRange(chart, "Range", axisDimension));
      }
      axisMenuItem.add(item);
    }

    if ((this.m_showAxisXGridMenu && axisDimension == Chart2D.X) || this.m_showAxisYGridMenu
        && axisDimension == Chart2D.Y) {
      // Axis -> show grid submenu
      if (adaptUI2Chart) {
        item = new PropertyChangeCheckBoxMenuItem(chart, new AxisActionSetGrid(chart, "Grid",
            axisDimension), axis.isPaintGrid());
      } else {
        item = new SelectionAdaptJCheckBoxMenuItem(new AxisActionSetGrid(chart, "Grid",
            axisDimension), axis.isPaintGrid());
      }
      axisMenuItem.add(item);
    }
    return axisMenuItem;
  }

  /**
   * Creates a radio button menu for choose one the available
   * {@link info.monitorenter.gui.chart.IRangePolicy} implementations to set to
   * it's axis identified by argument <code>axis</code>.
   * <p>
   * 
   * @param axis
   *          the axis to control.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * @return a radio button menu for choose one the available
   *         {@link info.monitorenter.gui.chart.IRangePolicy} implementations to
   *         set to it's axis identified by argument <code>axis</code>.
   */
  public JMenu createAxisRangePolicyMenu(final Chart2D chart, final IAxis axis,
      final boolean adaptUI2Chart) {
    JMenuItem item;
    // Axis -> Range policy submenu
    JMenu axisRangePolicy;
    if (adaptUI2Chart) {
      axisRangePolicy = new PropertyChangeMenu(chart, "Range policy");
    } else {
      axisRangePolicy = new JMenu("Range policy");
    }
    // Use a button group to control unique selection state of radio buttons:
    ButtonGroup buttonGroup = new ButtonGroup();
    // check the default selected item:
    Class rangePolicyClass = axis.getRangePolicy().getClass();
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart,
          "Fixed viewport", axis.getDimension(), new RangePolicyFixedViewport()));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new AxisActionSetRangePolicy(chart,
          "Fixed viewport", axis.getDimension(), new RangePolicyFixedViewport()));
    }
    item
        .setToolTipText("Zooms or expands to the configured range without respect to the data to display. ");
    axisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyFixedViewport.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart,
          "Minimum viewport", axis.getDimension(), new RangePolicyUnbounded()));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new AxisActionSetRangePolicy(chart,
          "Minimum viewport", axis.getDimension(), new RangePolicyUnbounded()));
    }
    axisRangePolicy.add(item);
    item.setToolTipText("Ensures all data is shown with minimal bounds.");
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyUnbounded.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart,
          "Minimum viewport with range", axis.getDimension(), new RangePolicyMinimumViewport(
              new Range(10, 10))));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new AxisActionSetRangePolicy(chart,
          "Minimum viewport with range", axis.getDimension(), new RangePolicyMinimumViewport(
              new Range(10, 10))));
    }
    item.setToolTipText("Ensures that all data is shown and expands if range is higher. ");
    axisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyMinimumViewport.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart,
          "Ensure visible point", axis.getDimension(), new RangePolicyForcedPoint(0)));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new AxisActionSetRangePolicy(chart,
          "Ensure visible point", axis.getDimension(), new RangePolicyForcedPoint(0)));
    }
    item.setToolTipText("Only the minimum value of the axis' range will be ensured to be visible.");
    axisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyForcedPoint.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(chart,
          "Highest points within max-50 to max.", axis.getDimension(),
          new RangePolicyHighestValues(50)));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new AxisActionSetRangePolicy(chart,
          "Highest points within max-50 to max.", axis.getDimension(),
          new RangePolicyHighestValues(50)));
    }
    item.setToolTipText("Shows the highest values from max-50 to max.");
    axisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyHighestValues.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }
    return axisRangePolicy;
  }

  /**
   * Creates a radio button menu for choose one the available axis types of the
   * given chart that will be set to it's axis identified by argument
   * <code>axisDimension</code>.
   * <p>
   * 
   * @param axis
   *          the axis to control.
   * 
   * @param axisDimension
   *          Identifies which dimension the axis controls in the chart: either
   *          {@link Chart2D#X} or {@link Chart2D#Y}
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * @return a radio button menu for choose one the available axis types of the
   *         given chart that will be set to it's axis identified by argument
   *         <code>axisDimension</code>.
   */
  public JMenu createAxisTypeMenu(final Chart2D chart, final IAxis axis, final int axisDimension,
      final boolean adaptUI2Chart) {
    // Axis -> Axis type
    JMenu axisType;
    if (adaptUI2Chart) {
      axisType = new PropertyChangeMenu(chart, "Type");
    } else {
      axisType = new JMenu("Type");
    }
    // Use a button group to control unique selection state of radio buttons:
    ButtonGroup buttonGroup = new ButtonGroup();
    // check the default selected item:
    Class typeClass = axis.getClass();
    JMenuItem item;

    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new Chart2DActionSetAxis(chart,
          new AxisLinear(), "Linear", axisDimension));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new Chart2DActionSetAxis(chart,
          new AxisLinear(), "Linear", axisDimension));
    }
    axisType.add(item);
    buttonGroup.add(item);
    if (typeClass == AxisLinear.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new Chart2DActionSetAxis(chart,
          new AxisLogE(), "Log E", axisDimension));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new Chart2DActionSetAxis(chart, new AxisLogE(),
          "Log E", axisDimension));
    }
    axisType.add(item);
    buttonGroup.add(item);
    if (typeClass == AxisLogE.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new Chart2DActionSetAxis(chart,
          new AxisLog10(), "Log 10", axisDimension));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new Chart2DActionSetAxis(chart,
          new AxisLog10(), "Log 10", axisDimension));
    }
    axisType.add(item);
    buttonGroup.add(item);
    if (typeClass == AxisLog10.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    return axisType;
  }

  /**
   * Creates a menu for choosing the background color of the given chart.
   * <p>
   * 
   * @param chart
   *          the chart to set the background color of by the menu to return.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * @return a menu for choosing the background color of the given chart.
   */
  public JMenu createBackgroundColorMenu(final Chart2D chart, final boolean adaptUI2Chart) {
    Color backgroundColor = chart.getBackground();
    boolean nonStandardColor = true;

    // Background color menu:
    JMenuItem item;
    JMenu bgColorMenu;
    if (adaptUI2Chart) {
      bgColorMenu = new PropertyChangeMenu(chart, "Background color");
    } else {
      bgColorMenu = new JMenu("Background color");
    }

    ButtonGroup buttonGroup = new ButtonGroup();
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new JComponentActionSetBackground(chart,
          "White", Color.WHITE));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new JComponentActionSetBackground(chart,
          "White", Color.WHITE));
    }
    if (backgroundColor.equals(Color.WHITE)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    bgColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new JComponentActionSetBackground(chart,
          "Gray", Color.GRAY));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new JComponentActionSetBackground(chart,
          "Gray", Color.GRAY));
    }
    if (backgroundColor.equals(Color.GRAY)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    bgColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new JComponentActionSetBackground(chart,
          "Light gray", Color.LIGHT_GRAY));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new JComponentActionSetBackground(chart,
          "Light gray", Color.LIGHT_GRAY));
    }
    if (backgroundColor.equals(Color.LIGHT_GRAY)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    bgColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new JComponentActionSetBackground(chart,
          "Black", Color.BLACK));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new JComponentActionSetBackground(chart,
          "Black", Color.BLACK));
    }
    if (backgroundColor.equals(Color.BLACK)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    bgColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart,
          JComponentActionSetCustomBackgroundSingleton.getInstance(chart, "Custom Color"));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(JComponentActionSetCustomBackgroundSingleton
          .getInstance(chart, "Custom Color"));
    }
    item.setSelected(nonStandardColor);
    buttonGroup.add(item);
    bgColorMenu.add(item);
    return bgColorMenu;
  }

  /**
   * Creates a <code>JLabel</code> that is capable of triggering a
   * <code>JPopupMenu</code> for the settings available for the
   * <code>ITrace2D</code>.
   * <p>
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
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * @return a label that offers a popup menue with controls for the given
   *         trace.
   */
  public JLabel createContextMenuLabel(final Chart2D chart, final ITrace2D trace,
      final boolean adaptUI2Chart) {
    TraceJLabel ret = new TraceJLabel(trace.getLabel());
    JMenuItem item;
    // ret.setSize(new Dimension(20, 100));
    JPopupMenu popup = new PropertyChangePopupMenu(chart);
    // set the initial background color:
    Color background = chart.getBackground();
    ret.setBackground(background);
    ret.setForeground(trace.getColor());

    // item for setVisible
    if (this.m_showTraceVisibleMenu) {
      item = new PropertyChangeCheckBoxMenuItem(chart,
          new Trace2DActionSetVisible(trace, "Visible"), trace.isVisible());
      popup.add(item);
    }

    // item for setName
    if (this.m_showTraceNameMenu) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetName(trace, "Name", chart));
      popup.add(item);
    }
    // add the submenus
    if (this.m_showTraceColorMenu) {
      popup.add(this.createTraceColorMenu(chart, trace, ret, adaptUI2Chart));
    }
    if (this.m_showTraceZindexMenu) {
      popup.add(this.createTraceZindexMenu(chart, trace, adaptUI2Chart));
    }
    if (this.m_showTraceStrokeMenu) {
      popup.add(this.createTraceStrokesMenu(chart, trace, adaptUI2Chart));
    }
    if (this.m_showTracePainterMenu) {
      popup.add(this.createTracePainterMenu(chart, trace, adaptUI2Chart));
    }
    ret.addMouseListener(new PopupListener(popup));
    trace.addPropertyChangeListener(ITrace2D.PROPERTY_COLOR, ret);
    trace.addPropertyChangeListener(ITrace2D.PROPERTY_NAME, ret);
    chart.addPropertyChangeListener(Chart2D.PROPERTY_FONT, ret);
    return ret;
  }

  /**
   * Creates a menu for choosing the foreground color of the given chart.
   * <p>
   * 
   * @param chart
   *          the chart to set the foreground color of by the menu to return.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * @return a menu for choosing the foreground color of the given chart.
   */
  public JMenuItem createForegroundColorMenu(final Chart2D chart, final boolean adaptUI2Chart) {

    Color foregroundColor = chart.getForeground();
    boolean nonStandardColor = true;
    ButtonGroup buttonGroup = new ButtonGroup();

    JMenuItem item;
    JMenu fgColorMenu;
    if (adaptUI2Chart) {
      fgColorMenu = new PropertyChangeMenu(chart, "Foreground color");
    } else {
      fgColorMenu = new JMenu("Foreground color");
    }
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new JComponentActionSetForeground(chart,
          "White", Color.WHITE));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new JComponentActionSetForeground(chart,
          "White", Color.WHITE));
    }
    if (foregroundColor.equals(Color.WHITE)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    fgColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new JComponentActionSetForeground(chart,
          "Gray", Color.GRAY));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new JComponentActionSetForeground(chart,
          "Gray", Color.GRAY));
    }
    if (foregroundColor.equals(Color.GRAY)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    fgColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new JComponentActionSetForeground(chart,
          "Light gray", Color.LIGHT_GRAY));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new JComponentActionSetForeground(chart,
          "Light gray", Color.LIGHT_GRAY));
    }
    if (foregroundColor.equals(Color.LIGHT_GRAY)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    fgColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new JComponentActionSetForeground(chart,
          "Black", Color.BLACK));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new JComponentActionSetForeground(chart,
          "Black", Color.BLACK));
    }
    if (foregroundColor.equals(Color.BLACK)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    fgColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart,
          JComponentActionSetCustomForegroundSingleton.getInstance(chart, "Custom Color"));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(JComponentActionSetCustomForegroundSingleton
          .getInstance(chart, "Custom Color"));
    }
    item.setSelected(nonStandardColor);
    buttonGroup.add(item);
    fgColorMenu.add(item);
    return fgColorMenu;
  }

  /**
   * Creates a menu for choosing the grid color of the given chart.
   * <p>
   * 
   * @param chart
   *          the chart to set the grid color of by the menu to return.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * @return a menu for choosing the grid color of the given chart.
   */
  public JMenu createGridColorMenu(final Chart2D chart, final boolean adaptUI2Chart) {
    JMenuItem item;
    Color gridColor = chart.getGridColor();
    boolean nonStandardColor = true;
    ButtonGroup buttonGroup = new ButtonGroup();

    JMenu gridColorMenu;
    if (adaptUI2Chart) {
      gridColorMenu = new PropertyChangeMenu(chart, "Grid color");
    } else {
      gridColorMenu = new JMenu("Grid color");
    }

    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new Chart2DActionSetGridColor(chart,
          "Gray", Color.GRAY));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new Chart2DActionSetGridColor(chart, "Gray",
          Color.GRAY));
    }
    if (gridColor.equals(Color.GRAY)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    gridColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new Chart2DActionSetGridColor(chart,
          "Light gray", Color.LIGHT_GRAY));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new Chart2DActionSetGridColor(chart,
          "Light gray", Color.LIGHT_GRAY));
    }
    if (gridColor.equals(Color.LIGHT_GRAY)) {
      item.setSelected(true);
      nonStandardColor = false;
    }

    buttonGroup.add(item);
    gridColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new Chart2DActionSetGridColor(chart,
          "Black", Color.BLACK));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new Chart2DActionSetGridColor(chart, "Black",
          Color.BLACK));
    }
    if (gridColor.equals(Color.BLACK)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    gridColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new Chart2DActionSetGridColor(chart,
          "White", Color.WHITE));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new Chart2DActionSetGridColor(chart, "White",
          Color.WHITE));
    }
    if (gridColor.equals(Color.WHITE)) {
      item.setSelected(true);
      nonStandardColor = false;
    }
    buttonGroup.add(item);
    gridColorMenu.add(item);
    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, Chart2DActionSetCustomGridColorSingleton
          .getInstance(chart, "Custom"));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(Chart2DActionSetCustomGridColorSingleton
          .getInstance(chart, "Custom"));
    }
    item.setSelected(nonStandardColor);
    buttonGroup.add(item);
    gridColorMenu.add(item);
    return gridColorMenu;
  }

  /**
   * Creates a menu that offers various controls over the given chart.
   * <p>
   * 
   * @param chart
   *          the chart to access.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   */
  public JMenu createMenu(final Chart2D chart, final boolean adaptUI2Chart) {

    JMenu chartMenu;
    if (adaptUI2Chart) {
      chartMenu = new PropertyChangeMenu(chart, "Chart");
    } else {
      chartMenu = new JMenu("Chart");
    }

    // fill top-level popup menu
    if (this.m_showChartBackgroundMenu) {
      chartMenu.add(createBackgroundColorMenu(chart, adaptUI2Chart));
    }
    if (this.m_showChartForegroundMenu) {
      chartMenu.add(createForegroundColorMenu(chart, adaptUI2Chart));
    }
    if (this.m_showGridColorMenu) {
      chartMenu.add(createGridColorMenu(chart, adaptUI2Chart));
    }
    JMenuItem item;
    if (this.m_showAxisXMenu || this.m_showAxisYMenu) {
      // Axis submenu:
      JMenu axisMenu;
      if (adaptUI2Chart) {
        axisMenu = new PropertyChangeMenu(chart, "Axis");
      } else {
        axisMenu = new JMenu("Axis");
      }

      // X axis submenu
      if (this.m_showAxisXMenu) {
        JMenuItem xAxisMenuItem = this.createAxisMenuItem(chart.getAxisX(), Chart2D.X,
            adaptUI2Chart);
        axisMenu.add(xAxisMenuItem);
      }
      // Y axis submenu
      if (this.m_showAxisYMenu) {
        JMenuItem yAxisMenuItem = this.createAxisMenuItem(chart.getAxisY(), Chart2D.Y,
            adaptUI2Chart);
        axisMenu.add(yAxisMenuItem);
      }
      chartMenu.add(axisMenu);
    }

    if (this.m_showSaveImageMenu) {
      if (adaptUI2Chart) {
        item = new PropertyChangeMenuItem(chart, Chart2DActionSaveImageSingleton.getInstance(chart,
            "Save image"));
      } else {
        item = new JMenuItem(Chart2DActionSaveImageSingleton.getInstance(chart, "Save image"));
      }
      chartMenu.add(item);
    }

    return chartMenu;
  }

  /**
   * Creates a menu bar that offers various controls over the given chart.
   * <p>
   * 
   * @param chart
   *          the chart to access.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   */
  public JMenuBar createMenuBar(final Chart2D chart, final boolean adaptUI2Chart) {

    JMenu chartMenu = createMenu(chart, adaptUI2Chart);
    JMenuBar menubar;
    if (adaptUI2Chart) {
      menubar = new PropertyChangeJMenuBar(chart);
    } else {
      menubar = new JMenuBar();
    }
    menubar.add(chartMenu);
    return menubar;
  }

  /**
   * Adds a popup menu to the given chart that offers various controls over it.
   * <p>
   * 
   * @param chart
   *          the chart to add the popup menue to.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   */
  public void createPopupMenu(final Chart2D chart, final boolean adaptUI2Chart) {

    // fill top-level popup menu
    JPopupMenu popup;
    if (adaptUI2Chart) {
      popup = new PropertyChangePopupMenu(chart);
    } else {
      popup = new JPopupMenu();
    }
    if (this.m_showChartBackgroundMenu) {
      popup.add(createBackgroundColorMenu(chart, adaptUI2Chart));
    }
    if (this.m_showChartForegroundMenu) {
      popup.add(createForegroundColorMenu(chart, adaptUI2Chart));
    }
    if (this.m_showGridColorMenu) {
      popup.add(createGridColorMenu(chart, adaptUI2Chart));
    }
    if (this.m_showAxisXMenu || this.m_showAxisYMenu) {
      // Axis submenu:
      JMenu axisMenu;
      if (adaptUI2Chart) {
        axisMenu = new PropertyChangeMenu(chart, "Axis");
      } else {
        axisMenu = new JMenu("Axis");
      }

      if (this.m_showAxisXMenu) {
        // X axis submenu
        JMenuItem xAxisMenuItem = this.createAxisMenuItem(chart.getAxisX(), Chart2D.X,
            adaptUI2Chart);
        axisMenu.add(xAxisMenuItem);
      }
      if (this.m_showAxisYMenu) {
        // Y axis submenu
        JMenuItem yAxisMenuItem = this.createAxisMenuItem(chart.getAxisY(), Chart2D.Y,
            adaptUI2Chart);
        axisMenu.add(yAxisMenuItem);
      }
      popup.add(axisMenu);
    }

    JMenuItem item;
    if (this.m_showSaveImageMenu) {
      if (adaptUI2Chart) {
        item = new PropertyChangeMenuItem(chart, Chart2DActionSaveImageSingleton.getInstance(chart,
            "Save image"));
      } else {
        item = new JMenuItem(Chart2DActionSaveImageSingleton.getInstance(chart, "Save image"));
      }
      popup.add(item);
    }
    chart.addMouseListener(new PopupListener(popup));
  }

  /**
   * Creates a menu for choosing the color of the given trace.
   * <p>
   * 
   * @param chart
   *          needed to adapt the basic ui properties to (font, foreground
   *          color, background color).
   * 
   * @param trace
   *          the trace to set the color of.
   * 
   * @param parent
   *          needed for a modal dialog for custom color as parent component.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * @return a menu for choosing the color of the given trace.
   */
  public JMenu createTraceColorMenu(final Chart2D chart, final ITrace2D trace,
      final JComponent parent, final boolean adaptUI2Chart) {
    // submenu for trace color
    JMenu colorMenu;
    if (adaptUI2Chart) {
      colorMenu = new PropertyChangeMenu(chart, "Color");
    } else {
      colorMenu = new JMenu("Color");
    }
    JMenuItem item;
    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Red", Color.RED));
    } else {
      item = new JMenuItem(new Trace2DActionSetColor(trace, "Red", Color.RED));
    }
    colorMenu.add(item);

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Green",
          Color.GREEN));
    } else {
      item = new JMenuItem(new Trace2DActionSetColor(trace, "Green", Color.GREEN));

    }
    colorMenu.add(item);

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Blue", Color.BLUE));
    } else {
      item = new JMenuItem(new Trace2DActionSetColor(trace, "Blue", Color.BLUE));
    }
    colorMenu.add(item);

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Gray", Color.GRAY));
    } else {
      item = new JMenuItem(new Trace2DActionSetColor(trace, "Gray", Color.GRAY));
    }
    colorMenu.add(item);

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Magenta",
          Color.MAGENTA));
    } else {
      item = new JMenuItem(new Trace2DActionSetColor(trace, "Magenta", Color.MAGENTA));
    }
    colorMenu.add(item);

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Pink", Color.PINK));
    } else {
      item = new JMenuItem(new Trace2DActionSetColor(trace, "Pink", Color.PINK));
    }
    colorMenu.add(item);

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetColor(trace, "Black",
          Color.BLACK));
    } else {
      item = new JMenuItem(new Trace2DActionSetColor(trace, "Black", Color.BLACK));
    }
    colorMenu.add(item);

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetCustomColor(trace, "Custom",
          parent));
    } else {
      item = new JMenuItem(new Trace2DActionSetCustomColor(trace, "Custom", parent));
    }
    colorMenu.add(item);
    return colorMenu;
  }

  /**
   * Creates a menu for choosing the {@link ITracePainter} of the given trace.
   * <p>
   * 
   * @param chart
   *          needed to adapt the basic ui properties to (font, foreground
   *          color, background color).
   * 
   * @param trace
   *          the trace to set the painter of.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * @return a menu for choosing the {@link ITracePainter} of the given trace.
   */
  public JMenu createTracePainterMenu(final Chart2D chart, final ITrace2D trace,
      final boolean adaptUI2Chart) {
    JMenuItem item;
    // trace painters
    JMenu painterMenu;
    if (adaptUI2Chart) {
      painterMenu = new PropertyChangeMenu(chart, "renderer");
    } else {
      painterMenu = new JMenu("renderer");
    }

    ITracePainter painter = new TracePainterDisc(4);
    if (adaptUI2Chart) {
      item = new OrderingCheckBoxPropertyChangeMenuItem(chart,
          new Trace2DActionAddRemoveTracePainter(trace, "discs", painter), painterMenu, trace
              .containsTracePainter(painter));
    } else {
      item = new OrderingCheckBoxMenuItem(new Trace2DActionAddRemoveTracePainter(trace, "discs",
          painter), painterMenu, trace.containsTracePainter(painter));
    }
    if (trace.getTracePainters().contains(painter)) {
      item.setSelected(true);
    }
    painterMenu.add(item);

    painter = new TracePainterPolyline();
    if (adaptUI2Chart) {
      item = new OrderingCheckBoxPropertyChangeMenuItem(chart,
          new Trace2DActionAddRemoveTracePainter(trace, "line", painter), painterMenu, trace
              .containsTracePainter(painter));
    } else {
      item = new OrderingCheckBoxMenuItem(new Trace2DActionAddRemoveTracePainter(trace, "line",
          painter), painterMenu, trace.containsTracePainter(painter));
    }
    painterMenu.add(item);
    if (trace.getTracePainters().contains(painter)) {
      item.setSelected(true);
    }

    painter = new TracePainterFill(chart);
    if (adaptUI2Chart) {
      item = new OrderingCheckBoxPropertyChangeMenuItem(chart,
          new Trace2DActionAddRemoveTracePainter(trace, "fill", painter), painterMenu, trace
              .containsTracePainter(painter));
    } else {
      item = new OrderingCheckBoxMenuItem(new Trace2DActionAddRemoveTracePainter(trace, "fill",
          painter), painterMenu, trace.containsTracePainter(painter));

    }
    painterMenu.add(item);
    if (trace.getTracePainters().contains(painter)) {
      item.setSelected(true);
    }
    painter = new TracePainterVerticalBar(chart);
    if (adaptUI2Chart) {
      item = new OrderingCheckBoxPropertyChangeMenuItem(chart,
          new Trace2DActionAddRemoveTracePainter(trace, "bar", painter), painterMenu, trace
              .containsTracePainter(painter));
    } else {
      item = new OrderingCheckBoxMenuItem(new Trace2DActionAddRemoveTracePainter(trace, "bar",
          painter), painterMenu, trace.containsTracePainter(painter));
    }
    painterMenu.add(item);
    if (trace.getTracePainters().contains(painter)) {
      item.setSelected(true);
    }

    return painterMenu;
  }

  /**
   * Creates a menu for choosing the {@link Stroke} of the given trace.
   * <p>
   * 
   * @param chart
   *          needed to adapt the basic ui properties to (font, foreground
   *          color, background color).
   * 
   * @param trace
   *          the trace to set the stroke of.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * @return a menu for choosing the stroke of the given trace.
   */
  public JMenu createTraceStrokesMenu(final Chart2D chart, final ITrace2D trace,
      final boolean adaptUI2Chart) {
    JMenuItem item;
    // strokes
    JMenu strokesMenu = new PropertyChangeMenu(chart, "Stroke");
    for (int i = 0; i < this.m_strokes.length; i++) {
      if (adaptUI2Chart) {
        item = new PropertyChangeMenuItem(chart, new Trace2DActionSetStroke(trace,
            this.m_strokeNames[i], this.m_strokes[i]));
      } else {
        item = new JMenuItem(new Trace2DActionSetStroke(trace, this.m_strokeNames[i],
            this.m_strokes[i]));
      }
      strokesMenu.add(item);
    }
    return strokesMenu;
  }

  /**
   * Creates a menu for choosing the z-index of the given trace.
   * <p>
   * 
   * @param chart
   *          needed to adapt the basic ui properties to (font, foreground
   *          color, background color).
   * 
   * @param trace
   *          the trace to set the z-index of.
   * 
   * @param adaptUI2Chart
   *          if true the menu will adapt it's basic UI properies (font,
   *          foreground and background color) to the given chart.
   * 
   * @return a menu for choosing the z-index of the given trace.
   */
  public JMenu createTraceZindexMenu(final Chart2D chart, final ITrace2D trace,
      final boolean adaptUI2Chart) {
    JMenuItem item;
    // submenu for zIndex
    JMenu zIndexMenu;
    if (adaptUI2Chart) {
      zIndexMenu = new PropertyChangeMenu(chart, "layer");
    } else {
      zIndexMenu = new JMenu("layer");
    }

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetZindex(trace, "bring to front",
          0));
    } else {
      item = new JMenuItem(new Trace2DActionSetZindex(trace, "bring to front", 0));
    }
    zIndexMenu.add(item);

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionSetZindex(trace, "send to back",
          ITrace2D.ZINDEX_MAX));
    } else {
      item = new JMenuItem(new Trace2DActionSetZindex(trace, "send to back", ITrace2D.ZINDEX_MAX));
    }
    zIndexMenu.add(item);

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionZindexDecrease(trace, "forward", 2));
    } else {
      item = new JMenuItem(new Trace2DActionZindexDecrease(trace, "forward", 2));
    }
    zIndexMenu.add(item);

    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new Trace2DActionZindexIncrease(trace, "backwards",
          2));
    } else {
      item = new JMenuItem(new Trace2DActionZindexIncrease(trace, "backwards", 2));
    }
    zIndexMenu.add(item);
    return zIndexMenu;
  }

  /**
   * Returns wether the chart show x grid menu should be created.
   * <p>
   * 
   * @return the showAxisXGridMenu.
   */
  public final boolean isShowAxisXGridMenu() {
    return this.m_showAxisXGridMenu;
  }

  /**
   * Returns whether the axis x menu is shown.
   * <p>
   * 
   * @return the showAxisXMenu.
   */
  public final boolean isShowAxisXMenu() {
    return this.m_showAxisXMenu;
  }

  /**
   * Returns whether the axis x range policy menu is shown.
   * <p>
   * 
   * @return the showAxisXRangePolicyMenu.
   */
  public final boolean isShowAxisXRangePolicyMenu() {
    return this.m_showAxisXRangePolicyMenu;
  }

  /**
   * Returns whether the axis x type menu is shown.
   * <p>
   * 
   * @return the showAxisXTypeMenu.
   */
  public final boolean isShowAxisXTypeMenu() {
    return this.m_showAxisXTypeMenu;
  }

  /**
   * Returns whether the axis y show grid menu is shown.
   * <p>
   * 
   * @return the showAxisYGridMenu.
   */
  public final boolean isShowAxisYGridMenu() {
    return this.m_showAxisYGridMenu;
  }

  /**
   * Returns whether the axis y menu is shown.
   * <p>
   * 
   * @return the showAxisYMenu.
   */
  public final boolean isShowAxisYMenu() {
    return this.m_showAxisYMenu;
  }

  /**
   * Returns whether the axis y range policy menu is shown.
   * <p>
   * 
   * @return the showAxisYRangePolicyMenu.
   */
  public final boolean isShowAxisYRangePolicyMenu() {
    return this.m_showAxisYRangePolicyMenu;
  }

  /**
   * Returns whether the axis y type menu is shown.
   * <p>
   * 
   * @return the showAxisYTypeMenu.
   */
  public final boolean isShowAxisYTypeMenu() {
    return this.m_showAxisYTypeMenu;
  }

  /**
   * Returns whether the chart set background color menu is shown.
   * <p>
   * 
   * @return the showChartBackgroundMenu.
   */
  public final boolean isShowChartBackgroundMenu() {
    return this.m_showChartBackgroundMenu;
  }

  /**
   * Returns whether the chart set foreground color menu is shown.
   * <p>
   * 
   * @return the showChartForegroundMenu.
   */
  public final boolean isShowChartForegroundMenu() {
    return this.m_showChartForegroundMenu;
  }

  /**
   * Returns whether the chart grid color menu is shown.
   * <p>
   * 
   * @return the showGridColorMenu.
   */
  public final boolean isShowGridColorMenu() {
    return this.m_showGridColorMenu;
  }

  /**
   * Returns whether the save image menu is shown.
   * <p>
   * 
   * @return the showSaveImageMenu.
   */
  public final boolean isShowSaveImageMenu() {
    return this.m_showSaveImageMenu;
  }

  /**
   * @return the showTraceColorMenu.
   */
  public final boolean isShowTraceColorMenu() {
    return this.m_showTraceColorMenu;
  }

  /**
   * @return the showTraceNameMenu.
   */
  public final boolean isShowTraceNameMenu() {
    return this.m_showTraceNameMenu;
  }

  /**
   * @return the showTracePainterMenu.
   */
  public final boolean isShowTracePainterMenu() {
    return this.m_showTracePainterMenu;
  }

  /**
   * @return the showTraceStrokeMenu.
   */
  public final boolean isShowTraceStrokeMenu() {
    return this.m_showTraceStrokeMenu;
  }

  /**
   * @return the showTraceVisibleMenu.
   */
  public final boolean isShowTraceVisibleMenu() {
    return this.m_showTraceVisibleMenu;
  }

  /**
   * @return the showTraceZindexMenu.
   */
  public final boolean isShowTraceZindexMenu() {
    return this.m_showTraceZindexMenu;
  }

  /**
   * Set wether the axis x show grid menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showAxisXGridMenu
   *          The showAxisXGridMenu to set.
   */
  public final void setShowAxisXGridMenu(final boolean showAxisXGridMenu) {
    this.m_showAxisXGridMenu = showAxisXGridMenu;
  }

  /**
   * Set wether the axis x menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showAxisXMenu
   *          The showAxisXMenu to set.
   */
  public final void setShowAxisXMenu(final boolean showAxisXMenu) {
    this.m_showAxisXMenu = showAxisXMenu;
  }

  /**
   * Set wether the axis x range policy menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showAxisXRangePolicyMenu
   *          The showAxisXRangePolicyMenu to set.
   */
  public final void setShowAxisXRangePolicyMenu(final boolean showAxisXRangePolicyMenu) {
    this.m_showAxisXRangePolicyMenu = showAxisXRangePolicyMenu;
  }

  /**
   * Set wether the axis x type menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showAxisXTypeMenu
   *          The showAxisXTypeMenu to set.
   */
  public final void setShowAxisXTypeMenu(final boolean showAxisXTypeMenu) {
    this.m_showAxisXTypeMenu = showAxisXTypeMenu;
  }

  /**
   * Set wether the axis y show grid menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showAxisYGridMenu
   *          The showAxisYGridMenu to set.
   */
  public final void setShowAxisYGridMenu(final boolean showAxisYGridMenu) {
    this.m_showAxisYGridMenu = showAxisYGridMenu;
  }

  /**
   * Set wether the axis y menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showAxisYMenu
   *          The showAxisYMenu to set.
   */
  public final void setShowAxisYMenu(final boolean showAxisYMenu) {
    this.m_showAxisYMenu = showAxisYMenu;
  }

  /**
   * Set wether the axis y range policy menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showAxisYRangePolicyMenu
   *          The showAxisYRangePolicyMenu to set.
   */
  public final void setShowAxisYRangePolicyMenu(final boolean showAxisYRangePolicyMenu) {
    this.m_showAxisYRangePolicyMenu = showAxisYRangePolicyMenu;
  }

  /**
   * Set wether the axis y type menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showAxisYTypeMenu
   *          The showAxisYTypeMenu to set.
   */
  public final void setShowAxisYTypeMenu(final boolean showAxisYTypeMenu) {
    this.m_showAxisYTypeMenu = showAxisYTypeMenu;
  }

  /**
   * Set wether the chart set background menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showChartBackgroundMenu
   *          The showChartBackgroundMenu to set.
   */
  public final void setShowChartBackgroundMenu(final boolean showChartBackgroundMenu) {
    this.m_showChartBackgroundMenu = showChartBackgroundMenu;
  }

  /**
   * Set wether the chart set foreground menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showChartForegroundMenu
   *          The showChartForegroundMenu to set.
   */
  public final void setShowChartForegroundMenu(final boolean showChartForegroundMenu) {
    this.m_showChartForegroundMenu = showChartForegroundMenu;
  }

  /**
   * Set wether the chart grid color menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showGridColorMenu
   *          The showGridColorMenu to set.
   */
  public final void setShowGridColorMenu(final boolean showGridColorMenu) {
    this.m_showGridColorMenu = showGridColorMenu;
  }

  /**
   * Set wether the save to image menu should be created.
   * <p>
   * Configure this before using any instance of
   * {@link info.monitorenter.gui.chart.views.ChartPanel} or it will be useless.
   * <p>
   * 
   * @param showSaveImageMenu
   *          The showSaveImageMenu to set.
   */
  public final void setShowSaveImageMenu(final boolean showSaveImageMenu) {
    this.m_showSaveImageMenu = showSaveImageMenu;
  }

  /**
   * @param showTraceColorMenu
   *          The showTraceColorMenu to set.
   */
  public final void setShowTraceColorMenu(final boolean showTraceColorMenu) {
    this.m_showTraceColorMenu = showTraceColorMenu;
  }

  /**
   * @param showTraceNameMenu
   *          The showTraceNameMenu to set.
   */
  public final void setShowTraceNameMenu(final boolean showTraceNameMenu) {
    this.m_showTraceNameMenu = showTraceNameMenu;
  }

  /**
   * @param showTracePainterMenu
   *          The showTracePainterMenu to set.
   */
  public final void setShowTracePainterMenu(final boolean showTracePainterMenu) {
    this.m_showTracePainterMenu = showTracePainterMenu;
  }

  /**
   * @param showTraceStrokeMenu
   *          The showTraceStrokeMenu to set.
   */
  public final void setShowTraceStrokeMenu(final boolean showTraceStrokeMenu) {
    this.m_showTraceStrokeMenu = showTraceStrokeMenu;
  }

  /**
   * @param showTraceVisibleMenu
   *          The showTraceVisibleMenu to set.
   */
  public final void setShowTraceVisibleMenu(final boolean showTraceVisibleMenu) {
    this.m_showTraceVisibleMenu = showTraceVisibleMenu;
  }

  /**
   * @param showTraceZindexMenu
   *          The showTraceZindexMenu to set.
   */
  public final void setShowTraceZindexMenu(final boolean showTraceZindexMenu) {
    this.m_showTraceZindexMenu = showTraceZindexMenu;
  }
}
