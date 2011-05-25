/* (C) Copyright 2001 Sun Microsystems, Inc. 
 * (C) Copyright 2001 Open Services Gateway Initiative, Inc. (the OSGi alliance)
 */

/* $Header: /cvshome/repository/ee/foundation/javax/microedition/io/StreamConnectionNotifier.java,v 1.2 2003/03/12 03:17:39 hargrave Exp $ */

package javax.microedition.io;
public abstract interface StreamConnectionNotifier extends javax.microedition.io.Connection {
    public abstract javax.microedition.io.StreamConnection acceptAndOpen() throws java.io.IOException;
}

