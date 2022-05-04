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

import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * This interface represents a SCCP connection refused message for connection-oriented protocol classes 2 and 3.
 * @author yulianoifa
 *
 */
public interface SccpConnCrefMessage extends SccpConnMessage {
    LocalReference getDestinationLocalReferenceNumber();
    void setDestinationLocalReferenceNumber(LocalReference number);

    SccpAddress getCalledPartyAddress();
    void setCalledPartyAddress(SccpAddress address);

    ByteBuf getUserData();
    void setUserData(ByteBuf data);

    RefusalCause getRefusalCause();
    void setRefusalCause(RefusalCause value);

    Importance getImportance();
    void setImportance(Importance importance);
}
