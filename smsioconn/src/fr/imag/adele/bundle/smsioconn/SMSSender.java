/*
 *  Simple SMS Sender
 *  send SMS with a GSM phone connected to the gateway
 *  this bundle wrap the SMS library from http://smsj.sourceforge.net
 *
 *  Copyright (C) 2003  Didier Donsez
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *  Contact: Didier Donsez (Didier.Donsez@ieee.org)
 *  Contributor(s):
 *
 */
package fr.imag.adele.bundle.sms;

/**
 *  interface of services to send SMS which a GSM phone
 *
 *@author     donsez
 *@created    22/03/2003
 *@version    1.0 22/03/2003
 */
public interface SMSSender {
	/**
	 *  send a SMS (Short Message Service)
	 *
	 *@param  message   the message to send
	 *@param  receiver  the GSM phone number of the receiver in international format (eg +33675123456)
	 *@param  sender    the GSM phone number of the sender in international format (eg +33675123456)
	 *@return           true if the message is sent, false if an error occurs
	 */
	public boolean send(String message, String receiver, String sender);
}


