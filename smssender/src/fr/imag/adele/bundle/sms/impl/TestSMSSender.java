/*
 * Simple SMS Sender
 * send SMS with a GSM phone connected to the gateway
 * this bundle wrap the SMS library from http://smsj.sourceforge.net
 *
 * Copyright (C) 2003  Didier Donsez
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Contact: Didier Donsez (Didier.Donsez@ieee.org)
 * Contributor(s):
 *
**/
package fr.imag.adele.bundle.sms.impl;

import java.io.*;
import java.util.*;
import org.marre.sms.*;
import org.marre.sms.transport.gsm.*;

/**
 * this class is for test purpose
 */ 
public class TestSMSSender {
	public static void main(String[] args) {

		String sender = args[0];
		String receiver = args[1];
		String message = args[2];

		fr.imag.adele.bundle.sms.SMSSender smssender=new SMSSenderImpl(SMSSenderImpl.DEFAULT_PROPERTIES_FILE);
		smssender.send(message,receiver,sender);
	}
}



