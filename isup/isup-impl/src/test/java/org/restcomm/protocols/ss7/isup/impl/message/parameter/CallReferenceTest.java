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
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CallReferenceTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public CallReferenceTest() throws IOException {
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[1]));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[2]));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[3]));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[4]));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[6]));

        super.goodBodies.add(Unpooled.wrappedBuffer(getBody2()));

    }

    private ByteBuf getBody1() throws IOException {

        // we will use odd number of digits, so we leave zero as MSB
        byte[] body = new byte[5];
        body[0] = 12;
        body[1] = 73;
        body[2] = 120;
        body[3] = 73;
        body[4] = 120;

        return Unpooled.wrappedBuffer(body);
    }

    private ByteBuf getBody2() throws IOException {

        // we will use odd number of digits, so we leave zero as MSB
        byte[] body = new byte[5];
        body[0] = 12;
        body[1] = 73;
        body[2] = 120;
        body[3] = 73;
        // one MSB will be ignored.
        body[4] = 120 & 0x3F;

        return Unpooled.wrappedBuffer(body);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        CallReferenceImpl cr = new CallReferenceImpl();
        cr.decode(getBody1());
        String[] methodNames = { "getCallIdentity", "getSignalingPointCode" };
        Object[] expectedValues = { 805240, 14409 };
        super.testValues(cr, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new CallReferenceImpl();
    }

}
