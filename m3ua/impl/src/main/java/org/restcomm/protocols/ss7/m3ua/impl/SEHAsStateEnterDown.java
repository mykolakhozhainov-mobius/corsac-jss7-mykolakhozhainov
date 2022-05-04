/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.restcomm.protocols.ss7.m3ua.impl;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.m3ua.M3UAManagementEventListener;
import org.restcomm.protocols.ss7.m3ua.State;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSMState;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSMStateEventHandler;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public abstract class SEHAsStateEnterDown implements FSMStateEventHandler {

    private static final Logger logger = LogManager.getLogger(SEHAsStateEnterDown.class);

    private AsImpl asImpl;

    public SEHAsStateEnterDown(AsImpl asImpl) {
        this.asImpl = asImpl;
    }

    @Override
    public void onEvent(FSMState state) {
        // Call listener and indicate of state change only if not already done
        if (!this.asImpl.state.getName().equals(State.STATE_DOWN)) {
            AsState oldState = AsState.getState(this.asImpl.state.getName());
            this.asImpl.state = AsState.DOWN;
            
            Iterator<M3UAManagementEventListener> managementEventListenersTmp = this.asImpl.m3UAManagementImpl.managementEventListeners.values().iterator();
            while(managementEventListenersTmp.hasNext()) {
                M3UAManagementEventListener m3uaManagementEventListener = managementEventListenersTmp.next();
                try {
                    m3uaManagementEventListener.onAsDown(this.asImpl, oldState);
                } catch (Throwable ee) {
                    logger.error("Exception while invoking onAsDown", ee);
                }
            }
        }
    }

}
