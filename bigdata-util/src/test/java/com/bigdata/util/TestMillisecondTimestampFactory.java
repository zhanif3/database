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
/*
 * Created on Oct 26, 2006
 */

package com.bigdata.util;

import java.util.Date;

import junit.framework.TestCase;

/**
 * Test suite for {@link MillisecondTimestampFactory}.
 * 
 * @author <a href="mailto:thompsonbry@users.sourceforge.net">Bryan Thompson</a>
 * @version $Id$
 */
public class TestMillisecondTimestampFactory extends TestCase {

    public TestMillisecondTimestampFactory() {
        
    }

    public TestMillisecondTimestampFactory(String arg0) {

        super(arg0);
        
    }

    /**
     * This test simply displays some timestamps where I observed time going
     * backward on a Fedora core 6 host using Sun JDK <code>1.6.0_03</code>.
     * The first timestamp was the previous value assigned by the factory as
     * reported by {@link System#currentTimeMillis()}. The second timestamp was
     * the next distinct value reported by {@link System#currentTimeMillis()}.
     * As you can see, the 2nd timestamp was actually earlier than the first
     * timestamp. This event lead to a refactor of the
     * {@link MillisecondTimestampFactory} to introduce a failback
     * "auto-increment" mode for handling precisely such bizarre events.
     * 
     * <pre>
     * lastTimestamp = Tue Apr 22 21:12:16 EDT 2008
     * millisTime    = Tue Apr 22 21:12:14 EDT 2008
     * </pre>
     * 
     * The actual long integer values reported were:
     * 
     * <pre>
     * lastTimestamp = 1208913136715
     * millisTime    = 1208913134964
     * </pre>
     */
    public void test_showTime() {
        
        System.err.println("lastTimestamp = " + new Date(1208913136715L));

        System.err.println("millisTime    = " + new Date(1208913134964L));
        
    }
    
    /**
     * Test determines whether or not millisecond timestamps are always distinct
     * from the last generated timestamp (as assigned by
     * {@link System#currentTimeMillis()}.
     * <p>
     * Note: This test is NOT designed to pass/fail but simply to test determine
     * a characteristic of the platform on which it is executing.
     */
    public void test_nextTimestamp1() {

        final int limit = 3000;
        
        long lastTimestamp = System.currentTimeMillis();
        long timestamp;
        long minDiff = Long.MAX_VALUE;
        
        for( int i=0; i<limit; i++ ) {

            timestamp = System.currentTimeMillis();
            
            if( timestamp == lastTimestamp ) {
                
                System.err
                        .println("This platform can generate identical timestamps with millisecond resolution");

                return;
                
            }

            long diff = timestamp - lastTimestamp;
            
            if( diff < 0 ) diff = -diff;
            
            if( diff < minDiff ) minDiff = diff;
            
            lastTimestamp = timestamp;
            
        }
        
        System.err.println("Millisecond times appear to be distinct on this platorm.");

        System.err.println("The minimum difference in milliseconds is "
                + minDiff + " over " + limit + " trials");
        
    }
    
    /**
     * Test verifies that timestamps are always distinct from the last generated
     * timestamp as generated by
     * {@link MillisecondTimestampFactory#nextMillis()}.
     */
    public void test_nextTimestamp2() {

        final int limit = 1000;
        
        long lastTimestamp = System.currentTimeMillis() - 1;
        final long begin = lastTimestamp;
        long timestamp;
        long minDiff = Long.MAX_VALUE;
        
        for( int i=0; i<limit; i++ ) {

            timestamp = MillisecondTimestampFactory.nextMillis();
            
            if( timestamp == lastTimestamp ) fail("Same timestamp?");

            if( timestamp < lastTimestamp ) fail("Time goes backwards?");

            long diff = timestamp - lastTimestamp;
            
            if( diff < 0 ) diff = -diff;
            
            if( diff < minDiff ) minDiff = diff;
            
            lastTimestamp = timestamp;
            
        }
        
        long elapsed = lastTimestamp - begin;
        
        System.err.println("Minimum time difference is " + minDiff
                + " milliseconds over " + limit + " trials and "+elapsed+" milliseconds");
        
    }
    
}
