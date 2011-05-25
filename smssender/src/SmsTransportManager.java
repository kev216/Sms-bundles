// Decompiled by DJ v3.0.0.63 Copyright 2002 Atanas Neshkov  Date: 27/03/2003 08:36:37
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SmsTransportManager.java

package org.marre.sms.transport;

import java.util.Properties;
import org.marre.sms.SmsException;

// Referenced classes of package org.marre.sms.transport:
//            SmsTransport

public class SmsTransportManager
{

    public static final SmsTransport getTransport(String theClassname, Properties theProps)
        throws SmsException
    {
        SmsTransport transport = null;
        try
        {
//            Class clazz = this.getClass().getClassLoader().forName(theClassname);
            Class clazz = SmsTransportManager.class.getClassLoader().loadClass(theClassname);
            Object obj = clazz.newInstance();
            transport = (SmsTransport)obj;
            transport.init(theProps);
            return (SmsTransport)obj;
        }
        catch(ClassCastException classcastexception)
        {
            throw new SmsException(theClassname + "is not an SmsTransport.");
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new SmsException("Couldn't find " + theClassname + ". Please check your classpath.");
        }
        catch(Exception exception)
        {
            throw new SmsException("Couldn't load " + theClassname + ".");
        }
    }

    private SmsTransportManager()
    {
    }
}