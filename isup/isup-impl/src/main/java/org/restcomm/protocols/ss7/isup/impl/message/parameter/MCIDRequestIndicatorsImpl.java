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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.MCIDRequestIndicators;

/**
 * Start time:14:44:20 2009-04-01<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 *
 */
public class MCIDRequestIndicatorsImpl extends AbstractISUPParameter implements MCIDRequestIndicators {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private boolean mcidRequestIndicator;
    private boolean holdingIndicator;

    public MCIDRequestIndicatorsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public MCIDRequestIndicatorsImpl() {
        super();

    }

    public MCIDRequestIndicatorsImpl(boolean mcidRequest, boolean holdingRequested) {
        super();
        this.mcidRequestIndicator = mcidRequest;
        this.holdingIndicator = holdingRequested;
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("buffer must  not be null and length must  be 1");
        }

        byte curr=b.readByte();
        this.mcidRequestIndicator = (curr & 0x01) == _TURN_ON;
        this.holdingIndicator = ((curr >> 1) & 0x01) == _TURN_ON;        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        int b0 = 0;

        b0 |= (this.mcidRequestIndicator ? _TURN_ON : _TURN_OFF);
        b0 |= ((this.holdingIndicator ? _TURN_ON : _TURN_OFF)) << 1;

        buffer.writeByte((byte) b0);
    }

    public boolean isMcidRequestIndicator() {
        return mcidRequestIndicator;
    }

    public void setMcidRequestIndicator(boolean mcidRequestIndicator) {
        this.mcidRequestIndicator = mcidRequestIndicator;
    }

    public boolean isHoldingIndicator() {
        return holdingIndicator;
    }

    public void setHoldingIndicator(boolean holdingIndicator) {
        this.holdingIndicator = holdingIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
