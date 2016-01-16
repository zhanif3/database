/**

Copyright (C) SYSTAP, LLC 2006-2016.  All rights reserved.

Contact:
     SYSTAP, LLC
     2501 Calvert ST NW #106
     Washington, DC 20008
     licenses@blazegraph.com

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; version 2 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package com.bigdata.util;

import java.util.UUID;

/**
 * An instance of this class is thrown if we observe that the timestamps
 * generated by two or more services violate a requirement for synchronized
 * clocks.
 * 
 * @author <a href="mailto:thompsonbry@users.sourceforge.net">Bryan Thompson</a>
 */
public class ClocksNotSynchronizedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ClocksNotSynchronizedException() {
        super();
    }

    public ClocksNotSynchronizedException(String msg) {
        super(msg);
    }

    /**
     * Assert that <code>t1</code> LT <code>t2</code>, where <code>t1</code> and
     * <code>t2</code> are timestamps obtain such that this relation will be
     * <code>true</code> if the clocks on the nodes are synchronized.
     * <p>
     * Note: Clock synchronization errors can arise across nodes if the nodes
     * are not using a common network time source.
     * <p>
     * Note: Synchronization errors can arise on a single node if the clock is
     * changed on that node - specifically if the clock is move backwards to
     * before the most recent commit timestamp. For example, if the timezone is
     * changed.
     * 
     * @param serviceId1
     *            The service that reported the timestamp <code>t1</code>.
     * @param serviceId2
     *            The service that reported the timestamp <code>t2</code>.
     * @param t1
     *            A timestamp from one service.
     * @param t2
     *            A timestamp from the another service.
     * @param maxSkew
     *            The maximum allowed clock skew (typically on the order of
     *            seconds).
     * 
     * @throws ClocksNotSynchronizedException
     */
    static public void assertBefore(final UUID serviceId1,
            final UUID serviceId2, final long t1, final long t2,
            final long maxSkew) throws ClocksNotSynchronizedException {

        if (t1 < t2) {

            /*
             * Strictly LT.
             * 
             * Note: There can be large latencies between t1 and t2. If t1 is
             * taken on the leader before t2 is taken on the follower, then a
             * full GC on either node before t2 is taken will result in a large
             * latency between t1 and t2. Our concern here is to identify skew
             * that violates the BEFORE semantics, not latency in which time
             * moves forward.
             */
            return;

        }
        
        final long delta = Math.abs(t1 - t2);

        if (delta <= maxSkew)
            return;

//        if (t2 >= min && t2 <= max) {
//            // The 2nd clock is within the allowed window.
//            return;
//        }

        throw new ClocksNotSynchronizedException("service1=" + serviceId1
                + ", serviceId2=" + serviceId2 + ", skew=" + delta
                + "ms exceeds maximumSkew=" + maxSkew + "ms.");

    }
    

}
