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

/**
 * Start time:15:44:56 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * @author yulianoifa
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CCNRPossibleIndicator;

/**
 * Start time:15:44:56 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CCNRPossibleIndicatorImpl extends AbstractISUPParameter implements CCNRPossibleIndicator {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private boolean ccnrPossible = false;

    public CCNRPossibleIndicatorImpl() {
        super();

    }

    public CCNRPossibleIndicatorImpl(boolean ccnrPossible) {
        super();
        this.ccnrPossible = ccnrPossible;
    }

    public CCNRPossibleIndicatorImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("buffer must not be null and length must be 1");
        }

        this.ccnrPossible = (b.readByte() & 0x01) == _TURN_ON;
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        buffer.writeByte((byte) (this.ccnrPossible ? _TURN_ON : _TURN_OFF));
    }

    public boolean isCcnrPossible() {
        return ccnrPossible;
    }

    public void setCcnrPossible(boolean ccnrPossible) {
        this.ccnrPossible = ccnrPossible;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
