/* (C) Copyright 2001 Sun Microsystems, Inc. 
 * (C) Copyright 2001 Open Services Gateway Initiative, Inc. (the OSGi alliance)
 */

/* $Header: /cvshome/repository/ee/foundation/javax/microedition/io/DatagramConnection.java,v 1.2 2003/03/12 03:17:39 hargrave Exp $ */

package javax.microedition.io;
public abstract interface DatagramConnection extends javax.microedition.io.Connection {
    public abstract int getMaximumLength() throws java.io.IOException;
    public abstract int getNominalLength() throws java.io.IOException;
    public abstract javax.microedition.io.Datagram newDatagram(byte[] var0, int var1) throws java.io.IOException;
    public abstract javax.microedition.io.Datagram newDatagram(byte[] var0, int var1, java.lang.String var2) throws java.io.IOException;
    public abstract javax.microedition.io.Datagram newDatagram(int var0) throws java.io.IOException;
    public abstract javax.microedition.io.Datagram newDatagram(int var0, java.lang.String var1) throws java.io.IOException;
    public abstract void receive(javax.microedition.io.Datagram var0) throws java.io.IOException;
    public abstract void send(javax.microedition.io.Datagram var0) throws java.io.IOException;
}

