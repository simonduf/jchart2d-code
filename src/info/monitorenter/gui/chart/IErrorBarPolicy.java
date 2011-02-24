/*
 *  IErrorBarPolicy.java of project jchart2d, interface for a facade 
 *  towards painting error bars that adds a layer of configurability. 
 *  Copyright 2006 (C) Achim Westermann, created on 06.08.2006 13:40:03.
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
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA*
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.gui.chart;

import info.monitorenter.gui.chart.errorbars.ErrorBarValue;

import java.beans.PropertyChangeListener;
import java.util.Set;

/**
 * Interface for a facade towards painting error bars that adds a layer of
 * configurability.
 * <p>
 * 
 * It acts as a facade for an
 * <code>{@link info.monitorenter.gui.chart.ITracePainter}</code> with
 * configureable
 * <code>{@link info.monitorenter.gui.chart.IErrorBarPainter}</code> instances
 * that will be provided with configured
 * <code>{@link info.monitorenter.gui.chart.IErrorBarValue}</code> instances.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 * @version $Revision: 1.7 $
 */
public interface IErrorBarPolicy extends ITracePainter {
  /**
   * The property key defining the <code>color</code> property.
   * <p>
   * This is fired whenever the internal configuration of the error bar policy
   * changes. This internal configuration should be of no interest for clients
   * of this interface. What counts is that rendering of the error bars will
   * have changed whenever this event is fired. Subclasses might fire this event
   * to tell exactly this: "Rendering has changed. Please repaint."
   * <p>
   * As it is of no interest and knowledge which configuration has changed the
   * {@link javax.swing.event.PropertyChangeEvent#getNewValue()} and the
   * {@link java.beans.PropertyChangeEvent#getOldValue()} of the
   * {@link java.beans.PropertyChangeEvent} given to
   * {@link PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)}
   * of listeners should be null always.
   * <p>
   * 
   * Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * 
   */
  public static final String PROPERTY_CONFIGURATION = "errorbarpolicy.config";

  /**
   * Intended for {@link info.monitorenter.gui.chart.traces.ATrace2D} only that
   * will register itself to the instances added to it.
   * <p>
   * This is support for error bar policies that need information about the
   * whole trace (e.g. median value). It has nothing to do with the kind of
   * error bar policy to be used by a trace. See
   * {@link ITrace2D#setErrorBarPolicy(IErrorBarPolicy)} and
   * {@link ITrace2D#addErrorBarPolicy(IErrorBarPolicy)} for this feature
   * instead.
   * <p>
   * 
   * @param trace
   *          the trace error bars are rendered for.
   */
  public void setTrace(final ITrace2D trace);

  /**
   * Calculates the errors of the given errorbar according to the point to
   * render and the configuration of this instance.
   * <p>
   * 
   * @param absoluteX
   *          the x value to render an error bar for.
   * 
   * @param absoluteY
   *          the y value to render an error bar for.
   * 
   * @param errorBar
   *          an error bar to use: This is for design reasons as internally this
   *          method is used too with a reused instance.
   */
  public void calculateErrorBar(final double absoluteX, final double absoluteY, final ErrorBarValue errorBar);

  /**
   * The property key defining the <code>color</code> property.
   * <p>
   * This is fired whenever the internal set of error bar painters changes.
   * Namely from <code>{@link  #addErrorBarPainter(IErrorBarPainter)}</code>
   * and <code>{@link #setErrorBarPainter(IErrorBarPainter)}</code>.
   * <p>
   * Use in combination with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
   * 
   */
  public static final String PROPERTY_ERRORBARPAINTER = "errorbarpolicy.painter";

  /**
   * Adds the given error bar painter to the list of painters of this instance.
   * <p>
   * 
   * @param painter
   *          the painter to use.
   * 
   */
  public void addErrorBarPainter(IErrorBarPainter painter);

  /**
   * Registers a property change listener that will be informed about changes of
   * the property identified by the given <code>propertyName</code>.
   * <p>
   * 
   * @param propertyName
   *          the name of the property the listener is interested in
   * 
   * @param listener
   *          a listener that will only be informed if the property identified
   *          by the argument <code>propertyName</code> changes
   * 
   * 
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

  /**
   * Returns the set of {@link IErrorBarPainter} to use.
   * <p>
   * 
   * @return the set of {@link IErrorBarPainter} to use.
   */
  public Set getErrorBarPainters();

  /**
   * Returns true if negative errors in x dimension are shown.
   * <p>
   * 
   * @return true if negative errors in x dimension are shown.
   */
  public boolean isShowNegativeXErrors();

  /**
   * Returns true if negative errors in y dimension are shown.
   * <p>
   * 
   * @return true if negative errors in y dimension are shown.
   */
  public boolean isShowNegativeYErrors();

  /**
   * Returns true if positive errors in x dimension are shown.
   * <p>
   * 
   * @return true if positive errors in x dimension are shown.
   */
  public boolean isShowPositiveXErrors();

  /**
   * Returns true if positive errors in y dimension are shown.
   * <p>
   * 
   * @return true if positive errors in y dimension are shown.
   */
  public boolean isShowPositiveYErrors();

  /**
   * Deregisters a property change listener that has been registerd for
   * listening on all properties.
   * <p>
   * 
   * @param listener
   *          a listener that will only be informed if the property identified
   *          by the argument <code>propertyName</code> changes
   * 
   * 
   */
  public void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * Removes a property change listener for listening on the given property.
   * <p>
   * 
   * @param property
   *          one of the constants with teh <code>PROPERTY_</code> prefix
   *          defined in this class or subclasses.
   * 
   * @param listener
   *          the listener for this property change.
   */
  public void removePropertyChangeListener(String property, PropertyChangeListener listener);

  /**
   * Makes the given error bar painter the sole painter for error bars of this
   * instance.
   * <p>
   * 
   * @param painter
   *          the painter to use.
   */
  public void setErrorBarPainter(IErrorBarPainter painter);

  /**
   * Set whether negative errors in x dimension should be shown.
   * <p>
   * 
   * @param showNegativeXErrors
   *          if true negative errors in x dimension will be shown.
   */
  public void setShowNegativeXErrors(final boolean showNegativeXErrors);

  /**
   * Set whether negative errors in y dimension should be shown.
   * <p>
   * 
   * @param showNegativeYErrors
   *          if true negative errors in y dimension will be shown.
   */
  public void setShowNegativeYErrors(final boolean showNegativeYErrors);

  /**
   * Set whether positive errors in x dimension should be shown.
   * <p>
   * 
   * @param showPositiveXErrors
   *          if true positive errors in x dimension will be shown.
   */
  public void setShowPositiveXErrors(final boolean showPositiveXErrors);

  /**
   * Set whether positive errors in y dimension should be shown.
   * <p>
   * 
   * @param showPositiveYErrors
   *          if true positive errors in y dimension will be shown.
   */
  public void setShowPositiveYErrors(final boolean showPositiveYErrors);

}
