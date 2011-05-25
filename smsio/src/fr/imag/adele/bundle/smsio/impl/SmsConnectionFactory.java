/**
 * this is an ConnectionFactory for the "sms" IO Scheme
 * @see OSGi specification, Release 3, chapter 13
 */

package fr.imag.adele.bundle.smsio.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.microedition.io.Connection;

import org.marre.sms.SmsException;
import org.marre.sms.SmsSender;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.io.ConnectionFactory;

import fr.imag.adele.bundle.smsio.ConfigMBean;
import fr.imag.adele.bundle.util.config.Configuration;

public class SmsConnectionFactory
	implements BundleActivator, ConnectionFactory, ConfigMBean {

	private static final String CONFIGFILE = "/initialstate.properties";
	private static final String[] MY_SCHEMES = { "sms" };

	private BundleContext bundleContext;

	private Properties bundleprops;
	
	private boolean trace=false;

	static final String RECEIVER = "sms.receiver.number";
	static final String SENDER = "sms.sender.number";

	/**
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		this.bundleContext = bundleContext;

		// initializes the service with the initial configuration
		init();
		
		// register the service
		Hashtable servicesProperties = new Hashtable();
		servicesProperties.put(IO_SCHEME, MY_SCHEMES);
		bundleContext.registerService(
			ConnectionFactory.class.getName(),
			this,
			servicesProperties);
	}

	/**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
	}
	 
	 /* configure the service with the initial state
	  */
	 private void init() throws Exception {

		 // load properties
		 // Get the Config-Location value from the manifest

		 String configLocation = null;
		 bundleprops = null;
		 Dictionary dict = bundleContext.getBundle().getHeaders();
		 Enumeration enum = dict.keys();
		 while (enum.hasMoreElements()) {
			 Object nextKey = enum.nextElement();
			 Object nextElem = dict.get(nextKey);
			 if (nextKey.equals("Config-Location")) {
				 configLocation = nextElem.toString();
				 break;
			 }
		 }
		 if (configLocation == null) {
			 configLocation = CONFIGFILE;
		 }

		 // Load properties from configLocation file
		 // and create and register a service
		 InputStream sourcestream =
			 getClass().getResourceAsStream(configLocation);
		 bundleprops = Configuration.loadProperties(sourcestream);

		 if(trace) Configuration.printProperties(System.out, bundleprops);
	 }
	 
	 /**
	  * @see org.osgi.service.io.ConnectionFactory#createConnection(java.lang.String, int, boolean)
	  */
	public Connection createConnection(String uri, int mode, boolean timeouts)
		throws IOException {

		Hashtable headers = new Hashtable();

		// get the scheme
		int doubledotpos = uri.indexOf(":");
		if (doubledotpos == -1)
			throw new IOException("No scheme in the uri");
		String scheme = uri.substring(0, doubledotpos);

		// check if the scheme is one of my schemes !
		// this is normally done by the IO ConnectorService
		boolean found = false;
		for (int i = 0; i < MY_SCHEMES.length && !found; i++)
			if (scheme.equals(MY_SCHEMES[i]))
				found = true;
		if (!found)
			throw new IOException("Can not deal this scheme");

		// parse the uri and put the extra uri headers
		// headers are name=value pairs after the ?

		String uriWithoutScheme = uri.substring(scheme.length()+1);
		int sep = uriWithoutScheme.indexOf('?');
		String receiver_headerfield;
		if (sep == -1) {
			receiver_headerfield = uriWithoutScheme;
		} else {
			receiver_headerfield = uriWithoutScheme.substring(0, sep);
			String extra_headerfields = uriWithoutScheme.substring(sep + 1);

			StringTokenizer st = new StringTokenizer(extra_headerfields, "&");
			while (st.hasMoreTokens()) {
				String pairnamevalue = st.nextToken();
				StringTokenizer st2 = new StringTokenizer(pairnamevalue, "=");
				String name;
				String value;
				if (st2.hasMoreTokens()) {
					name = st2.nextToken();
					if (st2.hasMoreTokens()) {
						value = st2.nextToken();
						headers.put(name, URLDecoder.decode(value));
					}
				}
			}
		}

		// add the RECIEVER header
		headers.put(RECEIVER, receiver_headerfield);

		return new SmsConnection(
			headers,
			bundleprops);
	}

	/**
	 * @see fr.imag.adele.bundle.smsio.ConfigMBean#setSmtpPort(int)
	 */
	public void setSender(String senderNumber) {
		bundleprops.setProperty(SENDER,senderNumber);
	}

	/**
	 * @see fr.imag.adele.bundle.smsio.ConfigMBean#getSmtpPort()
	 */
	public String getSender() {
		return bundleprops.getProperty(SENDER);
	}


	/**
	 * @see fr.imag.adele.bundle.smsio.ConfigMBean#setTrace(boolean)
	 */
	public void setTrace(boolean trace) {
	}

	/**
	 * @see fr.imag.adele.bundle.smsio.ConfigMBean#getTrace()
	 */
	public boolean getTrace() {
		return false;
	}

	/**
	 * @see fr.imag.adele.bundle.smsio.ConfigMBean#reset()
	 */
	public void reset() {
	}
}

class SmsConnection implements javax.microedition.io.OutputConnection {

	private Properties properties;
	private Hashtable headers;
	private SmsOutputStream mos;

	public SmsConnection(Hashtable headers, Properties properties) {
		this.headers = headers;
		this.properties = properties;
		this.mos = null;
	}

	public DataOutputStream openDataOutputStream() throws IOException {
		return new DataOutputStream(openOutputStream());
	}

	public OutputStream openOutputStream() throws IOException {
		if (headers == null) {
			throw new IOException("No header for this sms");
		}
		mos = new SmsOutputStream(headers, properties);
		return (OutputStream) mos;
	}

	public void close() throws IOException {
		if (mos != null)
			mos.close();
		headers = null;
	}
}

class SmsOutputStream extends OutputStream {

	ByteArrayOutputStream baos = null;
	Properties properties = null;
	SmsSender smsSender = null;
	private static final int SEGMENT_MAXSIZE = 160;

	/**
	 * @param headers
	 */
	public SmsOutputStream(Hashtable headers, Properties properties)
		throws IOException {
		try {
			baos = new ByteArrayOutputStream();
			this.properties=properties;
			smsSender=new SmsSender(properties.getProperty("sms.transportclassname"), properties);
		} catch (SmsException e) {
			e.printStackTrace();
			throw new IOException(e.toString());
		}
	}

	public void close() throws IOException {
		try {
			String message=baos.toString();

			String sender=properties.getProperty(SmsConnectionFactory.SENDER);
			String receiver=properties.getProperty(SmsConnectionFactory.RECEIVER);

			int len=message.length();
			for(int s=0; s<len;){
				int e;
				if(len<s+SEGMENT_MAXSIZE){
					e=len;
				} else {
					e=s+SEGMENT_MAXSIZE;
				}
				String segment=message.substring(s,e);
				smsSender.sendTextSms(segment,receiver,sender);
				s=e;
			}			
			smsSender.close();
		} catch (SmsException e) {
			try {
				smsSender.close();
			} catch (SmsException e1) {
				throw new IOException(e1.toString());
			}
			throw new IOException(e.toString());
		}
	}

	public void flush() {

	}

	public void write(byte[] b) throws IOException {
		baos.write(b);
	}

	public void write(byte[] b, int off, int len) {
		baos.write(b, off, len);
	}

	public void write(int i) {
		baos.write(i);
	}
}
