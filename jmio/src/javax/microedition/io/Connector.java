/* (C) Copyright 2001 Sun Microsystems, Inc. 
 * (C) Copyright 2001 Open Services Gateway Initiative, Inc. (the OSGi alliance)
 */

/* $Header: /cvshome/repository/ee/foundation/javax/microedition/io/Connector.java,v 1.2 2003/03/12 03:17:39 hargrave Exp $ */

package javax.microedition.io;
public class Connector {
    private Connector() { }
    public static javax.microedition.io.Connection open(java.lang.String var0) throws java.io.IOException { return null; }
    public static javax.microedition.io.Connection open(java.lang.String var0, int var1) throws java.io.IOException { return null; }
    public static javax.microedition.io.Connection open(java.lang.String var0, int var1, boolean var2) throws java.io.IOException { return null; }
    public static java.io.DataInputStream openDataInputStream(java.lang.String var0) throws java.io.IOException { return null; }
    public static java.io.DataOutputStream openDataOutputStream(java.lang.String var0) throws java.io.IOException { return null; }
    public static java.io.InputStream openInputStream(java.lang.String var0) throws java.io.IOException { return null; }
    public static java.io.OutputStream openOutputStream(java.lang.String var0) throws java.io.IOException { return null; }
    public final static int READ = 1;
    public final static int WRITE = 2;
    public final static int READ_WRITE = 3;
}

