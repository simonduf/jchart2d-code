package info.monitorenter.gui.chart.traces.iterators;

import info.monitorenter.gui.chart.IAccumulationFunction;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.util.Range;

import java.util.Iterator;

/**
 * {@link Iterator} that decorates an iterator by the feature of accumulation
 * of points.
 * <p>
 * Contract:
 * <ul>
 * <li>
 * No point with an x value of <code>NaN</code> must be accumulated. Those are
 * discontinuations and must be preserved.</li>
 * <li>
 * The first call to {@link #next()} has to return the same as the first point
 * in the given / wrapped iterator in case that the visible range does not
 * exclude it.</li>
 * <li>
 * The last call to {@link #next()} has to return the same as the last point
 * in the given / wrapped iterator in case that the visible range does not
 * exclude it.</li>
 * <li>
 * The visible range may be ignored.</li>
 * <li>
 * If the visible range is not ignored and excludes the first point(s) of the
 * wrapped iterator then the first point returned from {@link #next()} has to
 * be an interpolated point at the exact coordinate of the lower bound of the
 * range.</li>
 * <li>
 * If the visible range is not ignored and excludes the last point(s) of the
 * wrapped iterator then the last point returned from {@link #next()} has to
 * be an interpolated point at the exact coordinate of the upper bound of the
 * range.</li>
 * </ul>
 * <p>
 * 
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public abstract class AAccumulationIterator implements Iterator<ITracePoint2D> {

  /**
   * Internally stored stateful iterator. 
   */
  private Iterator<ITracePoint2D> m_originalIterator;

  /** The accumulation function being used. */
  private final IAccumulationFunction m_accumulationFunction;

  /**
   * The amount of visible points to accumulate. This will be used in case we
   * do accumulate <code>amountOfPoints</code> consecutive points without
   * caring for data density. In case of density - based accumulation this is
   * used to compute the range segments to accumulate values within. However
   * then the total amount of returned points may be smaller than this value
   * as some segments might not contain any point.
   */
  private final int m_amountOfVisiblePoints;

  /**
   * The original iterator that is decorated with the point accumulation
   * feature.
   */
  private final ITrace2D m_originalTrace;

  /**
   * Constructor with all that is needed for accumulating points.
   * <p>
   * 
   * @param originalTrace
   *          the iterator to decorate with the feature of accumulating
   *          points.
   * 
   * @param accumulationFunction
   *          the function to use for point - accumulation.
   * 
   * @param amountOfVisiblePoints
   *          The amount of visible points to accumulate. This will be used in
   *          case we do accumulate <code>amountOfPoints</code> consecutive
   *          points without caring for data density. In case of density -
   *          based accumulation this is used to compute the range segments to
   *          accumulate values within. However then the total amount of
   *          returned points may be smaller than this value as some segments
   *          might not contain any point.
   * 
   */
  public AAccumulationIterator(final ITrace2D originalTrace, final IAccumulationFunction accumulationFunction, final int amountOfVisiblePoints) {
    this.m_originalTrace = originalTrace;
    this.m_accumulationFunction = accumulationFunction;
    this.m_amountOfVisiblePoints = amountOfVisiblePoints;
    this.m_originalIterator = this.m_originalTrace.iterator();
  }

  /**
   * Returns the accumulationFunction.
   * <p>
   * 
   * @return the accumulationFunction
   */
  protected IAccumulationFunction getAccumulationFunction() {
    return this.m_accumulationFunction;
  }

  /**
   * Returns the amountOfVisiblePoints.
   * <p>
   * 
   * @return the amountOfVisiblePoints
   */
  protected int getAmountOfVisiblePoints() {
    return this.m_amountOfVisiblePoints;
  }

  /**
   * Returns the original trace.
   * <p>
   * 
   * @return the original trace.
   */
  protected ITrace2D getOriginalTrace() {
    return this.m_originalTrace;
  }

  /**
   * Returns the stateful iterator of the original trace.
   * <p>
   * 
   * @return the stateful iterator of the original trace.
   */
  protected Iterator<ITracePoint2D> getOriginalIterator() {
    return this.m_originalIterator;
  }
}