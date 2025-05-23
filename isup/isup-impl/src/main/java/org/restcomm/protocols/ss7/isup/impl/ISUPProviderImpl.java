/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 *
 */
package org.restcomm.protocols.ss7.isup.impl;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.isup.CircuitManager;
import org.restcomm.protocols.ss7.isup.ISUPEvent;
import org.restcomm.protocols.ss7.isup.ISUPListener;
import org.restcomm.protocols.ss7.isup.ISUPMessageFactory;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.ISUPProvider;
import org.restcomm.protocols.ss7.isup.ISUPTimeoutEvent;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.ISUPMessageFactoryImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.ISUPParameterFactoryImpl;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class ISUPProviderImpl implements ISUPProvider {
	private static final long serialVersionUID = 1L;

	protected static final Logger logger = LogManager.getLogger(ISUPProviderImpl.class);

	protected final ConcurrentHashMap<UUID, ISUPListener> listeners = new ConcurrentHashMap<UUID, ISUPListener>();

	protected final transient ISUPStackImpl stack;
	protected final transient ISUPMessageFactory messageFactory;
	protected final transient ISUPParameterFactory parameterFactory;

	protected final transient ConcurrentHashMap<Long, Circuit> cic2Circuit = new ConcurrentHashMap<Long, Circuit>();
	protected final int ni;
	protected final int localSpc;
	protected final boolean automaticTimerMessages;

	public ISUPProviderImpl(ISUPStackImpl isupStackImpl, int ni, int localSpc, boolean automaticTimerMessages) {
		this.stack = isupStackImpl;

		this.ni = ni;
		this.localSpc = localSpc;
		this.automaticTimerMessages = automaticTimerMessages;

		this.parameterFactory = new ISUPParameterFactoryImpl();
		this.messageFactory = new ISUPMessageFactoryImpl(this.parameterFactory);
	}

	@Override
	public int getNi() {
		return this.ni;
	}

	@Override
	public int getLocalSpc() {
		return this.localSpc;
	}

	public boolean isAutomaticTimerMessages() {
		return automaticTimerMessages;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.restcomm.isup.ISUPProvider#addListener(org.restcomm.isup.ISUPListener )
	 */
	@Override
	public void addListener(UUID key, ISUPListener listener) {
		if (listener == null)
			throw new NullPointerException("Listener must not be null!");
		if (this.listeners.containsKey(key))
			throw new IllegalArgumentException("Listener already present: " + listener + " !");
		else
			this.listeners.put(key, listener);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.restcomm.isup.ISUPProvider#removeListener(org.restcomm.isup.
	 * ISUPListener)
	 */
	@Override
	public void removeListener(UUID key) {
		if (key == null)
			throw new NullPointerException("Listener must not be null!");
		this.listeners.remove(key);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.isup.ISUPProvider#getMessageFactory()
	 */
	@Override
	public ISUPMessageFactory getMessageFactory() {
		return this.messageFactory;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.isup.ISUPProvider#getParameterFactory()
	 */
	@Override
	public ISUPParameterFactory getParameterFactory() {
		return this.parameterFactory;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.isup.ISUPProvider#sendMessage(org.restcomm.
	 * protocols.ss7.isup.message.ISUPMessage)
	 */

	@Override
	public void sendMessage(ISUPMessage msg, int dpc) throws ParameterException, IOException {
		if (!msg.hasAllMandatoryParameters())
			throw new ParameterException("Message does not have all required parameters!");
		sendOnCircuit(getCircuit(msg, dpc), msg);
	}

	void sendOnCircuit(Circuit c, ISUPMessage msg) throws ParameterException, IOException {
		if (c == null)
			throw new NullPointerException();
		if (msg == null)
			throw new NullPointerException();

		if (msg.getCircuitIdentificationCode() == null || msg.getCircuitIdentificationCode().getCIC() != c.getCic())
			throw new IllegalArgumentException();
		c.send(msg);
	}

	@Override
	public boolean cancelTimer(int cic, int dpc, int timerId) {
		long channelID = this.stack.getCircuitManager().getChannelID(cic, dpc);
		if (this.cic2Circuit.containsKey(channelID)) {
			Circuit c = this.cic2Circuit.get(channelID);
			return c.cancelTimer(timerId);
		}

		return false;
	}

	@Override
	public void cancelAllTimers(int cic, int dpc) {
		long channelID = this.stack.getCircuitManager().getChannelID(cic, dpc);
		if (this.cic2Circuit.containsKey(channelID)) {
			Circuit c = this.cic2Circuit.get(channelID);
			c.onStop();
		}
	}

	// ---------------------- non interface methods ----------------

	public void start() {
		CircuitManager cm = this.stack.getCircuitManager();
		long[] channelIDs = cm.getChannelIDs();
		this.cic2Circuit.clear();

		for (long channelID : channelIDs) {
			Circuit c = new Circuit(cm.getCIC(channelID), cm.getDPC(channelID), this,
					this.stack.getPeriodicQueuedTasks());
			cic2Circuit.put(channelID, c);
		}
	}

	public void stop() {
		Enumeration<Long> keys = cic2Circuit.keys();
		while (keys.hasMoreElements())
			try {
				cic2Circuit.remove(keys.nextElement()).onStop();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	}

	// --------- private methods and class defs.
	/**
	 * @param message
	 * @return
	 */
	void receive(ISUPMessage message, int dpc) {
		Circuit c = getCircuit(message, dpc);
		if (c != null)
			c.receive(message);
	}

	private Circuit getCircuit(ISUPMessage message, int dpc) {
		Circuit c = null;
		int cic = message.getCircuitIdentificationCode().getCIC();
		long channelID = this.stack.getCircuitManager().getChannelID(cic, dpc);
		if (!this.stack.getCircuitManager().isCircuitPresent(cic, dpc)) {
			if (this.cic2Circuit.containsKey(channelID))
				this.cic2Circuit.remove(channelID);

			// what for do we need to throw this error , lets simply add a circuit and
			// return it , we have all parameters anyway
			// throw new IllegalArgumentException("Curcuit not defined, no route definition
			// present!");
			this.stack.getCircuitManager().addCircuit(cic, dpc);
			c = new Circuit(cic, dpc, this, this.stack.getPeriodicQueuedTasks());
			cic2Circuit.put(channelID, c);
		} else
			c = this.cic2Circuit.get(channelID);
		return c;
	}

	void send(ISUPMessage message, Mtp3TransferPrimitive encoded, TaskCallback<Exception> callback) {
		this.stack.send(message, encoded, callback);
	}

	/**
	 * @param request
	 */
	public void deliver(ISUPEvent event) {
		Iterator<ISUPListener> iterator = listeners.values().iterator();
		while (iterator.hasNext())
			try {
				iterator.next().onEvent(event);
			} catch (Exception e) {
				if (logger.isErrorEnabled())
					logger.error("Exception thrown from listener.", e);
			}

	}

	/**
	 * @param timeoutEvent
	 */
	public void deliver(ISUPTimeoutEvent timeoutEvent) {
		Iterator<ISUPListener> iterator = listeners.values().iterator();
		while (iterator.hasNext())
			try {
				iterator.next().onTimeout(timeoutEvent);
			} catch (Exception e) {
				if (logger.isErrorEnabled())
					logger.error("Exception thrown from listener.", e);
			}
	}

	private long T1Timeout = ISUPTimeoutEvent.T1_DEFAULT;
	private long T5Timeout = ISUPTimeoutEvent.T5_DEFAULT;
	private long T7Timeout = ISUPTimeoutEvent.T7_DEFAULT;
	private long T12Timeout = ISUPTimeoutEvent.T12_DEFAULT;
	private long T13Timeout = ISUPTimeoutEvent.T13_DEFAULT;
	private long T14Timeout = ISUPTimeoutEvent.T14_DEFAULT;
	private long T15Timeout = ISUPTimeoutEvent.T15_DEFAULT;
	private long T16Timeout = ISUPTimeoutEvent.T16_DEFAULT;
	private long T17Timeout = ISUPTimeoutEvent.T17_DEFAULT;
	private long T18Timeout = ISUPTimeoutEvent.T18_DEFAULT;
	private long T19Timeout = ISUPTimeoutEvent.T19_DEFAULT;
	private long T20Timeout = ISUPTimeoutEvent.T20_DEFAULT;
	private long T21Timeout = ISUPTimeoutEvent.T21_DEFAULT;
	private long T22Timeout = ISUPTimeoutEvent.T22_DEFAULT;
	private long T23Timeout = ISUPTimeoutEvent.T23_DEFAULT;
	private long T28Timeout = ISUPTimeoutEvent.T28_DEFAULT;
	private long T33Timeout = ISUPTimeoutEvent.T33_DEFAULT;

	/**
	 * @return
	 */
	long getT1Timeout() {
		return T1Timeout;
	}

	/**
	 * @return
	 */
	long getT5Timeout() {
		return T5Timeout;
	}

	/**
	 * @return
	 */
	long getT7Timeout() {
		return T7Timeout;
	}

	/**
	 * @return
	 */
	long getT12Timeout() {
		return T12Timeout;
	}

	/**
	 * @return
	 */
	long getT13Timeout() {
		return T13Timeout;
	}

	/**
	 * @return
	 */
	long getT14Timeout() {
		return T14Timeout;
	}

	/**
	 * @return
	 */
	long getT15Timeout() {

		return T15Timeout;
	}

	/**
	 * @return
	 */
	long getT16Timeout() {

		return T16Timeout;
	}

	/**
	 * @return
	 */
	long getT17Timeout() {

		return T17Timeout;
	}

	/**
	 * @return
	 */
	long getT18Timeout() {

		return T18Timeout;
	}

	/**
	 * @return
	 */
	long getT19Timeout() {

		return T19Timeout;
	}

	/**
	 * @return
	 */
	long getT20Timeout() {

		return T20Timeout;
	}

	/**
	 * @return
	 */
	long getT21Timeout() {

		return T21Timeout;
	}

	/**
	 * @return
	 */
	long getT22Timeout() {

		return T22Timeout;
	}

	/**
	 * @return
	 */
	long getT23Timeout() {

		return T23Timeout;
	}

	/**
	 * @return
	 */
	long getT28Timeout() {

		return T28Timeout;
	}

	/**
	 * @return
	 */
	long getT33Timeout() {

		return T33Timeout;
	}

}
