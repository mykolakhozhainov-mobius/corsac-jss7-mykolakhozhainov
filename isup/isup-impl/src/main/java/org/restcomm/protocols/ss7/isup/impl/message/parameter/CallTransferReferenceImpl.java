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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CallTransferReference;

/**
 * Start time:13:29:12 2009-04-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CallTransferReferenceImpl extends AbstractISUPParameter implements CallTransferReference {
	private int callTransferReference = 0;

    public CallTransferReferenceImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public CallTransferReferenceImpl() {
        super();

    }

    public CallTransferReferenceImpl(int callTransferReference) {
        super();
        this.callTransferReference = callTransferReference;
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("buffer must  not be null and length must  be 1");
        }
        this.callTransferReference = b.readByte() & 0xFF;        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        buffer.writeByte((byte) this.callTransferReference);
    }

    public int getCallTransferReference() {
        return callTransferReference;
    }

    public void setCallTransferReference(int callTransferReference) {
        this.callTransferReference = callTransferReference  & 0xFF;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
