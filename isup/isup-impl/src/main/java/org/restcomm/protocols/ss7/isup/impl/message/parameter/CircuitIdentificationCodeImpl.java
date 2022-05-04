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
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;

/**
 * Start time:14:49:25 2009-09-18<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CircuitIdentificationCodeImpl extends AbstractISUPParameter implements CircuitIdentificationCode {
	private static final long serialVersionUID = 1L;

	private int cic;

    /**
     *
     */
    public CircuitIdentificationCodeImpl() {

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.message.parameter.CircuitIdentificationCode#getCIC()
     */
    public int getCIC() {
        return this.cic;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.message.parameter.CircuitIdentificationCode#setCIC(long)
     */
    public void setCIC(int cic) {
        this.cic = cic & 0x0FFF; // Q.763 1.2

    }

    public int getCode() {
        // Its not a real parameter.
        throw new UnsupportedOperationException();
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 2) {
            throw new ParameterException("buffer must not be null or has size equal to 2.");
        }
        this.cic = (b.readByte() & 0xFF);
        this.cic |= (b.readByte() << 8);        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	buffer.writeByte((byte) this.cic);
    	buffer.writeByte((byte) ((this.cic >> 8) & 0x0F));        
    }
}