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

package org.restcomm.protocols.ss7.sccp.impl.message;

import org.restcomm.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpAddressedMessage;
import org.restcomm.protocols.ss7.sccp.parameter.HopCounter;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
/**
 * 
 * @author yulianoifa
 *
 */
public abstract class SccpAddressedMessageImpl extends SccpMessageImpl implements SccpAddressedMessage {

    protected SccpAddress calledParty;
    protected SccpAddress callingParty;
    protected HopCounterImpl hopCounter;

    protected SccpAddressedMessageImpl(int maxDataLen, int type, int outgoingSls, int localSsn,
            SccpAddress calledParty, SccpAddress callingParty, HopCounter hopCounter) {
        super(maxDataLen, type, outgoingSls, localSsn);

        this.calledParty = calledParty;
        this.callingParty = callingParty;
        this.hopCounter = (HopCounterImpl) hopCounter;
    }

    protected SccpAddressedMessageImpl(int maxDataLen, int type, int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(maxDataLen,type, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    public SccpAddress getCalledPartyAddress() {
        return calledParty;
    }

    public void setCalledPartyAddress(SccpAddress calledParty) {
        this.calledParty = calledParty;
    }

    public SccpAddress getCallingPartyAddress() {
        return callingParty;
    }

    public void setCallingPartyAddress(SccpAddress callingParty) {
        this.callingParty = callingParty;
    }

    public HopCounter getHopCounter() {
        return hopCounter;
    }

    public void setHopCounter(HopCounter hopCounter) {
        this.hopCounter = (HopCounterImpl) hopCounter;
    }

    public boolean reduceHopCounter() {
        if (this.hopCounter != null) {
            int val = this.hopCounter.getValue();
            if (--val <= 0) {
                val = 0;
            }
            this.hopCounter.setValue(val);
            if (val == 0)
                return false;
        }
        return true;
    }
}
