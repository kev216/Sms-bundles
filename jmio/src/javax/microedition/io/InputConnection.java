/* (C) Copyright 2001 Sun Microsystems, Inc. 
 * (C) Copyright 2001 Open Services Gateway Initiative, Inc. (the OSGi alliance)
 */

/* $Header: /cvshome/repository/ee/foundation/javax/microedition/io/InputConnection.java,v 1.2 2003/03/12 03:17:39 hargrave Exp $ */

package javax.microedition.io;
public abstract interface InputConnection extends javax.microedition.io.Connection {
    public abstract java.io.DataInputStream openDataInputStream() throws java.io.IOException;
    public abstract java.io.InputStream openInputStream() throws java.io.IOException;
}

