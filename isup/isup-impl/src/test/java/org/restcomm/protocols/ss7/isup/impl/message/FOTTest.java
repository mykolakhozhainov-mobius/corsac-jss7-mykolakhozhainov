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
package org.restcomm.protocols.ss7.isup.impl.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.message.ForwardTransferMessage;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.CallReference;
import org.testng.annotations.Test;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: restcomm-isup-stack<br>
 * Test for ACM
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class FOTTest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Params() throws Exception {

    	ByteBuf message = getDefaultBody();

        ForwardTransferMessage msg = super.messageFactory.createFOT();
        ((AbstractISUPMessage) msg).decode(message, messageFactory, parameterFactory);

        assertNotNull(msg.getCallReference());
        assertEquals(msg.getCallReference().getCallIdentity(), 805240);
        assertEquals(msg.getCallReference().getSignalingPointCode(), 14409);
    }

    protected ByteBuf getDefaultBody() {
        byte[] message = {
                // CIC
                0x0C, (byte) 0x0B,
                ForwardTransferMessage.MESSAGE_CODE, 
                //pointer
                0x01,
                CallReference._PARAMETER_CODE,
                    //len
                    0x05,
                    12,
                    73,
                    120,
                    73,
                    120& 0x3F,
            0x00
                
        };
        return Unpooled.wrappedBuffer(message);
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createFOT();
    }
}
