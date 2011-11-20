package info.monitorenter.gui.chart.traces.iterators;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAccumulationFunction;
import info.monitorenter.gui.chart.ITracePoint2D;

import java.util.Iterator;

/**
 * An implementation that - without any care for order of x or y values and
 * without any care for densitiy-based accumulation accumulates n consecutive
 * points.
 * <p>
 * Invisible points will still be accumulated allowing the {@link Chart2D}
 * rendering the data to interpolate into visibility bounds. See the super class
 * description for additional contracts.
 * <p>
 * Points having a {@link ITracePoint2D#getX()} value of {@link Double#NaN} will
 * not be accumulated. Those are discontinuations that have to be preserved.
 * However consecutive <code>NaN</code> x-values will be accumulated into one
 * which decreases the possibility that no points get accumulated. However in
 * the worst case no accumulations may be performed: In the case that
 * alternating <code>NaN</code> x-values and non- <code>NaN</code> x values
 * occur.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public class AccumulatingIteratorConsecutivePoints extends AAccumulationIterator {

  /**
   * Computed points to accumulate per {@link #next()}. Kept as a member to save
   * computations proportional O(n) (n are amount of source points).
   */
  private int m_countPerNext;

  /**
   * Return the first point in case this is true, then switch to false.
   */
  private boolean m_firstCall2Next = true;

  /**
   * Store the last available point in case we found it but first have to return
   * an accumulation result.
   */
  private ITracePoint2D m_lastPoint;

  /**
   * Internal needed to store if we had to return an accumulated point while
   * seeing NaN which we then have to return next time.
   */
  private ITracePoint2D m_previousNaN;

  /**
   * Constructor with all that is needed for accumulating points.
   * <p>
   * 
   * @param originalIterator
   *          the iterator to decorate with the feature of accumulating points.
   * 
   * @param accumulationFunction
   *          the function to use for point - accumulation.
   * 
   * @param amountOfVisiblePoints
   *          The amount of visible points to accumulate. This will be used in
   *          case we do accumulate <code>amountOfPoints</code> consecutive
   *          points without caring for data density. In case of density - based
   *          accumulation this is used to compute the range segments to
   *          accumulate values within. However then the total amount of
   *          returned points may be smaller than this value as some segments
   *          might not contain any point.
   * 
   * @param totalPointsInSource
   *          The amount of point in the source iterator. *
   */
  public AccumulatingIteratorConsecutivePoints(final Iterator<ITracePoint2D> originalIterator,
      final IAccumulationFunction accumulationFunction, final int amountOfVisiblePoints, final int totalPointsInSource) {
    super(originalIterator, accumulationFunction, amountOfVisiblePoints, totalPointsInSource);
    /*
     * Compute the amount of points per next() to accumulate:
     */
    int targetCount = this.getAmountOfVisiblePoints();
    int sourceCount = this.getTotalPointsInSource();
    if ((targetCount != 0) && (sourceCount != 0)) {
      // -2 is for first and last point
      if ((sourceCount > 2) && (targetCount > 2)) {
        this.m_countPerNext = (int) Math.ceil((sourceCount - 2) / (targetCount - 2));
      } else {
        this.m_countPerNext = 1;
      }
    } else {
      this.m_countPerNext = 1;
    }
  }

  /**
   * @see java.util.Iterator#hasNext()
   */
  public boolean hasNext() {
    return (this.getOriginalIterator().hasNext()) || (this.m_lastPoint != null);
  }

  /**
   * @see java.util.Iterator#next()
   */
  public ITracePoint2D next() {
    /*
     * Work with the accumulation function here:
     */
    ITracePoint2D result = null;
    IAccumulationFunction accumulate = this.getAccumulationFunction();
    Iterator<ITracePoint2D> iterator = this.getOriginalIterator();
    ITracePoint2D point;
    int amountToAccumulate = this.m_countPerNext;
    if (this.m_firstCall2Next) {
      result = iterator.next();
      this.m_firstCall2Next = false;
    } else {

      if (!iterator.hasNext()) {
        /*
         * This is the case if in previous call [a] was hit. hasNext of this
         * implementation returns still true to have the stored last point be
         * returned unchanged.
         */
        result = this.m_lastPoint;
        this.m_lastPoint = null;
      } else {
        while ((amountToAccumulate > 0) && (iterator.hasNext())) {
          if (this.m_previousNaN != null) {
            /*
             * In case our previous call next returned an accumulated point but
             * kept in mind that NaN (discontinuation) was found: immediately
             * return the discontinuation.
             */
            result = this.m_previousNaN;
            this.m_previousNaN = null;
            break;
          } else {
            point = iterator.next();
            if (!point.isDiscontinuation()) {
              /*
               * [a] We must not blindly add this point to the accumulation
               * result: The contract is that the last point has to be returned
               * unchanged (as this implementation does not make use of the
               * given ranges). In case this is the last point: Store it for the
               * next invocation of next.
               */
              if (iterator.hasNext()) {
                accumulate.addPointToAccumulate(point);
              } else {
                this.m_lastPoint = point;
              }
              // wipe out potential previous NaN!
              this.m_previousNaN = null;
              amountToAccumulate--;
            } else {
              // point is NaN!
              if (this.m_previousNaN != null) {
                /*
                 * Don't care: our previous point was also NaN. So skip that NaN
                 * discontinuation.
                 */
              } else {

                result = accumulate.getAccumulatedPoint();
                if (result == null) {
                  /*
                   * We're good as this NaN point was the first to accumulate:
                   * Just return it!
                   */
                  result = point;
                  break;
                } else {
                  /*
                   * Bad luck: We found a NaN but also a previous accumulation
                   * result. We have to return the previous accumulation result
                   * but keep in mind that we next have a NaN to return.
                   */
                  this.m_previousNaN = point;
                  break;
                }
              }

            }
          }
        }
      }

      /*
       * We left the loop for several reasons:
       * 
       * 1. amountToAccumulate == 0 or iterator empty: get accumulated point and
       * return it.
       * 
       * 2. NaN found with no current accumulation result: return NaN
       * 
       * 3. NaN found but with current accumulation results: Return the
       * accumulated point, store NaN result.
       */

      // case 1:
      if (result == null) {
        result = accumulate.getAccumulatedPoint();
        if (result == null) {
          /*
           * special case: just after our accumulated point was cleared/returned
           * in previous call to next we found the last point (why our
           * accumulated point is null):
           */
          if (this.m_lastPoint != null) {
            result = this.m_lastPoint;
            this.m_lastPoint = null;
          } else {
            throw new IllegalStateException("This seems a programming error or unexpected state!");
          }
        }
      } else {
        // case 2 and 3.
      }
    }
    return result;

  }

  /**
   * @see java.util.Iterator#remove()
   */
  public void remove() {

    throw new UnsupportedOperationException("This is not supported for " + this.getClass().getName()
        + ". Data is not contained but computed (non 1-1) from an underlying source. ");
  }

}