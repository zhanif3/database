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
 * Created on Jan 2, 2008
 */

package com.bigdata.rdf.store;

import java.util.NoSuchElementException;

import com.bigdata.rdf.model.BigdataStatement;

/**
 * Empty iterator.
 * 
 * @author <a href="mailto:thompsonbry@users.sourceforge.net">Bryan Thompson</a>
 * @version $Id$
 */
public class EmptyStatementIterator implements BigdataStatementIterator {

    public static transient final BigdataStatementIterator INSTANCE = new EmptyStatementIterator();
    
    private EmptyStatementIterator() {
        
    }

    public void close() throws RuntimeException {
        
    }

    public boolean hasNext() throws RuntimeException {
        
        return false;
        
    }

    public BigdataStatement next() throws RuntimeException {
        
        throw new NoSuchElementException();
        
    }

    public void remove() throws RuntimeException {
        
        throw new UnsupportedOperationException();
        
    }

}
