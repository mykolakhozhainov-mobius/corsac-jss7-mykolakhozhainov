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

package org.restcomm.protocols.ss7.m3ua.impl;

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSM;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSMState;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSMStateEventHandler;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;

/**
 * {@link AsStatePenTimeout#onEvent(FSMState)} is called when the pending timer
 * T(r) expires.
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class AsStatePenTimeout implements FSMStateEventHandler {

	private AsImpl asImpl;
	private FSM fsm;
	private static final Logger logger = LogManager.getLogger(AsStatePenTimeout.class);

	boolean inactive = false;

	public AsStatePenTimeout(AsImpl asImpl, FSM fsm) {
		this.asImpl = asImpl;
		this.fsm = fsm;
	}

	/**
	 * <p>
	 * An active ASP has transitioned to ASP-INACTIVE or ASP DOWN and it was the
	 * last remaining active ASP in the AS. A recovery timer T(r) SHOULD be started,
	 * and all incoming signalling messages SHOULD be queued by the SGP. If an ASP
	 * becomes ASP-ACTIVE before T(r) expires, the AS is moved to the AS-ACTIVE
	 * state, and all the queued messages will be sent to the ASP.
	 * </p>
	 * <p>
	 * If T(r) expires before an ASP becomes ASP-ACTIVE, and the SGP has no
	 * alternative, the SGP may stop queuing messages and discard all previously
	 * queued messages. The AS will move to the AS-INACTIVE state if at least one
	 * ASP is in ASP-INACTIVE; otherwise, it will move to AS-DOWN state.
	 * </p>
	 */
	@Override
	public void onEvent(FSMState state) {
		logger.warn(String.format("PENDING timedout for As=%s", this.asImpl.getName()));

		// Clear the Pending Queue for this As
		this.asImpl.clearPendingQueue();

		this.inactive = false;

		// check if there are any ASP's who are INACTIVE, transition to
		// INACTIVE else DOWN
		Iterator<Entry<String, Asp>> iterator = this.asImpl.appServerProcs.entrySet().iterator();
		while (iterator.hasNext()) {
			AspImpl aspImpl = (AspImpl) iterator.next().getValue();
			FSM aspLocalFSM = aspImpl.getLocalFSM();

			if (AspState.getState(aspLocalFSM.getState().getName()) == AspState.INACTIVE)
				try {
					this.fsm.signal(TransitionState.AS_INACTIVE);
					inactive = true;
					break;
				} catch (UnknownTransitionException e) {
					logger.error(e.getMessage(), e);
				}
		} // for

		if (!this.inactive)
			// else transition to DOWN
			try {
				this.fsm.signal(TransitionState.AS_DOWN);
				inactive = true;
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}

		// Now send MTP3 PAUSE
		Iterator<AsStateListener> asStateListeners = this.asImpl.getAsStateListeners().iterator();

		while (asStateListeners.hasNext()) {
			AsStateListener asAsStateListener = asStateListeners.next();
			try {

				asAsStateListener.onAsInActive(this.asImpl);
			} catch (Exception e) {
				logger.error(String.format("Error while calling AsStateListener=%s onAsInActive method for As=%s",
						asAsStateListener, this.asImpl));
			}
		}
	}

}
