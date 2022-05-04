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

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;

import io.netty.buffer.ByteBuf;

/**
 * Start time:13:44:22 2009-03-31<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class GenericNotificationIndicatorImpl extends AbstractISUPParameter implements GenericNotificationIndicator {
	private List<Integer> notificationIndicator;

    public GenericNotificationIndicatorImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public GenericNotificationIndicatorImpl() {
        super();

    }

    public GenericNotificationIndicatorImpl(List<Integer> notificationIndicator) {
        super();
        this.setNotificationIndicator(notificationIndicator);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() < 2) {
            throw new ParameterException("buffer must  not be null and length must be 1 or greater");
        }

        this.notificationIndicator = new ArrayList<Integer>(b.readableBytes());

        while(b.readableBytes()>0) {
        	byte curr=b.readByte();
            int extFlag = (curr >> 7) & 0x01;
            if (extFlag == 0x01 && (b.readableBytes()!=0)) {
                throw new ParameterException("Extenstion flag idnicates end of data, however buffer has more octets.");
            }
            this.notificationIndicator.add(curr & 0x7F);
        }
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        for (int index = 0; index < this.notificationIndicator.size()-1; index++) {
        	buffer.writeByte((byte) (this.notificationIndicator.get(index) & 0x7F));
        }

        // sets extension bit to show that we dont have more octets
        byte b=0;
        if(this.notificationIndicator.size()>0)
        	b=(byte) (this.notificationIndicator.get(this.notificationIndicator.size()-1) & 0x7F);
        
        b |= 1 << 7;
        buffer.writeByte(b);
    }

    public List<Integer> getNotificationIndicator() {
        return notificationIndicator;
    }

    public void setNotificationIndicator(List<Integer> notificationIndicator) {
        if (notificationIndicator == null) {
            throw new IllegalArgumentException("Notification indicator must not be null");
        }
        this.notificationIndicator = notificationIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
