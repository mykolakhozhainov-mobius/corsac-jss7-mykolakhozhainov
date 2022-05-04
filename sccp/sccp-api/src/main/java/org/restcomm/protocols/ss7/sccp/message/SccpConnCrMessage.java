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

package org.restcomm.protocols.ss7.sccp.message;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.HopCounter;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * This interface represents a SCCP connection request message for connection-oriented protocol classes 2 and 3.
 * @author yulianoifa
 *
 */
public interface SccpConnCrMessage extends SccpConnMessage, SccpAddressedMessage {

    LocalReference getSourceLocalReferenceNumber();
    void setSourceLocalReferenceNumber(LocalReference number);

    SccpAddress getCalledPartyAddress();
    void setCalledPartyAddress(SccpAddress address);

    SccpAddress getCallingPartyAddress();
    void setCallingPartyAddress(SccpAddress address);

    ProtocolClass getProtocolClass();
    void setProtocolClass(ProtocolClass value);

    Credit getCredit();
    void setCredit(Credit value);

    ByteBuf getUserData();
    void setUserData(ByteBuf data);

    HopCounter getHopCounter();
    void setHopCounter(HopCounter counter);
    boolean reduceHopCounter();

    Importance getImportance();
    void setImportance(Importance importance);

    boolean getSccpCreatesSls();
}
