/* (C) Copyright 2001 Sun Microsystems, Inc. 
 * (C) Copyright 2001 Open Services Gateway Initiative, Inc. (the OSGi alliance)
 */

/* $Header: /cvshome/repository/ee/foundation/javax/microedition/io/Connection.java,v 1.2 2003/03/12 03:17:39 hargrave Exp $ */

package javax.microedition.io;
public abstract interface Connection {
    public abstract void close() throws java.io.IOException;
}

