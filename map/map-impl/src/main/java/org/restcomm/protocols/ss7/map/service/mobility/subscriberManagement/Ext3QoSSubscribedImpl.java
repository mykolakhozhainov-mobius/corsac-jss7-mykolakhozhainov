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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRateExtended;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class Ext3QoSSubscribedImpl extends ASNOctetString implements Ext3QoSSubscribed {
	public Ext3QoSSubscribedImpl() {
		super("Ext3QoSSubscribed",1,3,false);
    }

    public Ext3QoSSubscribedImpl(ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended,
            ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended) {
        super(translate(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended),"Ext3QoSSubscribed",1,3,false);
    }

    protected static ByteBuf translate(ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended, ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended) {
        ByteBuf value = Unpooled.buffer(2);
        value.writeByte((byte) (maximumBitRateForUplinkExtended != null ? maximumBitRateForUplinkExtended.getSourceData() : 0));
        value.writeByte((byte) (guaranteedBitRateForUplinkExtended != null ? guaranteedBitRateForUplinkExtended.getSourceData() : 0));
        return value;
    }

    public ExtQoSSubscribed_BitRateExtended getMaximumBitRateForUplinkExtended() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() < 1)
            return null;

        return new ExtQoSSubscribed_BitRateExtended(value.readByte() & 0xFF, true);
    }

    public ExtQoSSubscribed_BitRateExtended getGuaranteedBitRateForUplinkExtended() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() < 2)
            return null;

    	value.skipBytes(1);
        return new ExtQoSSubscribed_BitRateExtended(value.readByte() & 0xFF, true);
    }

    @Override
    public String toString() {
    	ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = getMaximumBitRateForUplinkExtended();
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = getGuaranteedBitRateForUplinkExtended();

        StringBuilder sb = new StringBuilder();
        sb.append("Ext3QoSSubscribed [");

        if (maximumBitRateForUplinkExtended != null) {
            sb.append("maximumBitRateForUplinkExtended=");
            sb.append(maximumBitRateForUplinkExtended);
            sb.append(", ");
        }
        if (guaranteedBitRateForUplinkExtended != null) {
            sb.append("guaranteedBitRateForUplinkExtended=");
            sb.append(guaranteedBitRateForUplinkExtended);
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }
}
