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
package info.monitorenter.gui.chart.layout;

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
 * @version $Revision: 1.14 $
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
      extends LayoutFactory.PropertyChangeCheckBoxMenuItem {

    /**
     * Enriches a wrapped {@link Action} by the service of ordering it's
     * corresponding {@link JMenuItem} in it's {@link JMenu} according to the
     * description of {@link LayoutFactory.OrderingCheckBoxMenuItem}.
     * <p>
     * 
     * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
     * 
     * 
     * @version $Revision: 1.14 $
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
        final JMenu container, final boolean checked) {
      super(component, checked);
      this.m_menu = container;
      super.setAction(new JMenuOrderingAction(action));
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
   * Creates a {@link JMenuItem} that allows to trigger the features related to
   * {@link info.monitorenter.gui.chart.AAxis} features.
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
   *         {@link info.monitorenter.gui.chart.AAxis} features.
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

    axisMenuItem.add(createAxisTypeMenu(chart, axis, axisDimension, adaptUI2Chart));

    axisMenuItem.add(createAxisRangePolicyMenu(chart, axis, adaptUI2Chart));

    // Axis -> Range menu
    if (adaptUI2Chart) {
      item = new PropertyChangeMenuItem(chart, new AxisActionSetRange(axis, "Range"));
    } else {
      item = new JMenuItem(new AxisActionSetRange(axis, "Range"));
    }
    axisMenuItem.add(item);

    // Axis -> show grid submenu
    if (adaptUI2Chart) {
      item = new PropertyChangeCheckBoxMenuItem(chart, new AxisActionSetGrid(axis, "Grid"), axis
          .isPaintGrid());
    } else {
      item = new SelectionAdaptJCheckBoxMenuItem(new AxisActionSetGrid(axis, "Grid"), axis
          .isPaintGrid());
    }
    axisMenuItem.add(item);
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
      item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(axis,
          "Fixed viewport", new RangePolicyFixedViewport()));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new AxisActionSetRangePolicy(axis,
          "Fixed viewport", new RangePolicyFixedViewport()));
    }
    item
        .setToolTipText("Zooms or expands to the configured range without respect to the data to display. ");
    axisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyFixedViewport.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(axis,
          "Minimum viewport", new RangePolicyUnbounded()));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new AxisActionSetRangePolicy(axis,
          "Minimum viewport", new RangePolicyUnbounded()));
    }
    axisRangePolicy.add(item);
    item.setToolTipText("Ensures all data is shown with minimal bounds.");
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyUnbounded.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(axis,
          "Minimum viewport with range", new RangePolicyMinimumViewport(new Range(10, 10))));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new AxisActionSetRangePolicy(axis,
          "Minimum viewport with range", new RangePolicyMinimumViewport(new Range(10, 10))));
    }
    item.setToolTipText("Ensures that all data is shown and expands if range is higher. ");
    axisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyMinimumViewport.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(axis,
          "Ensure visible point", new RangePolicyForcedPoint(0)));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new AxisActionSetRangePolicy(axis,
          "Ensure visible point", new RangePolicyForcedPoint(0)));
    }
    item.setToolTipText("Only the minimum value of the axis' range will be ensured to be visible.");
    axisRangePolicy.add(item);
    buttonGroup.add(item);
    if (rangePolicyClass == RangePolicyForcedPoint.class) {
      buttonGroup.setSelected(item.getModel(), true);
    }

    if (adaptUI2Chart) {
      item = new PropertyChangeJRadioButtonMenuItem(chart, new AxisActionSetRangePolicy(axis,
          "Highest points within max-50 to max.", new RangePolicyHighestValues(50)));
    } else {
      item = new SelectionAdaptJRadioButtonMenuItem(new AxisActionSetRangePolicy(axis,
          "Highest points within max-50 to max.", new RangePolicyHighestValues(50)));
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
    item = new PropertyChangeCheckBoxMenuItem(chart, new Trace2DActionSetVisible(trace, "Visible"),
        trace.isVisible());
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
        "discs", painter), painterMenu, trace.containsTracePainter(painter));
    if (trace.getTracePainters().contains(painter)) {
      item.setSelected(true);
    }
    painterMenu.add(item);

    painter = new TracePainterPolyline();
    item = new OrderingCheckBoxMenuItem(chart, new Trace2DActionAddRemoveTracePainter(trace,
        "line", painter), painterMenu, trace.containsTracePainter(painter));
    painterMenu.add(item);
    if (trace.getTracePainters().contains(painter)) {
      item.setSelected(true);
    }

    painter = new TracePainterFill(chart);
    item = new OrderingCheckBoxMenuItem(chart, new Trace2DActionAddRemoveTracePainter(trace,
        "fill", painter), painterMenu, trace.containsTracePainter(painter));
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
    new BasicPropertyAdaptSupport(ret, chart);
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

    // Axis submenu:
    JMenu axisMenu;
    if (adaptUI2Chart) {
      axisMenu = new PropertyChangeMenu(chart, "Axis");
    } else {
      axisMenu = new JMenu("Axis");
    }
    // axisMenu.setBackground(background);

    // X axis submenu
    JMenuItem xAxisMenuItem = this.createAxisMenuItem(chart.getAxisX(), Chart2D.X, adaptUI2Chart);
    axisMenu.add(xAxisMenuItem);
    // Y axis submenu
    JMenuItem yAxisMenuItem = this.createAxisMenuItem(chart.getAxisY(), Chart2D.Y, adaptUI2Chart);
    axisMenu.add(yAxisMenuItem);

    // fill top-level popup menu
    chartMenu.add(createBackgroundColorMenu(chart, adaptUI2Chart));
    chartMenu.add(createForegroundColorMenu(chart, adaptUI2Chart));
    chartMenu.add(createGridColorMenu(chart, adaptUI2Chart));
    chartMenu.add(axisMenu);

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

    // Color background = chart.getBackground();

    // Axis submenu:
    JMenu axisMenu;
    if (adaptUI2Chart) {
      axisMenu = new PropertyChangeMenu(chart, "Axis");
    } else {
      axisMenu = new JMenu("Axis");
    }
    // axisMenu.setBackground(background);

    // X axis submenu
    JMenuItem xAxisMenuItem = this.createAxisMenuItem(chart.getAxisX(), Chart2D.X, adaptUI2Chart);
    axisMenu.add(xAxisMenuItem);
    // Y axis submenu
    JMenuItem yAxisMenuItem = this.createAxisMenuItem(chart.getAxisY(), Chart2D.Y, adaptUI2Chart);
    axisMenu.add(yAxisMenuItem);

    // fill top-level popup menu
    JPopupMenu popup;
    if (adaptUI2Chart) {
      popup = new PropertyChangePopupMenu(chart);
    } else {
      popup = new JPopupMenu();
    }
    popup.add(createBackgroundColorMenu(chart, adaptUI2Chart));
    popup.add(createForegroundColorMenu(chart, adaptUI2Chart));
    popup.add(createGridColorMenu(chart, adaptUI2Chart));
    popup.add(axisMenu);
    chart.addMouseListener(new PopupListener(popup));
  }
}
