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
public class OriginatingParticipatingServiceProviderTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public OriginatingParticipatingServiceProviderTest() throws IOException {

        super.badBodies.add(Unpooled.wrappedBuffer(getBody3()));

        super.goodBodies.add(Unpooled.wrappedBuffer(new byte[1]));
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody1()));

    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        OriginatingParticipatingServiceProviderImpl bci = new OriginatingParticipatingServiceProviderImpl(getBody1());

        String[] methodNames = { "isOddFlag", "getAddress", "getOpspLengthIndicator" };
        Object[] expectedValues = { false, super.getSixDigitsString(), 3 };
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        OriginatingParticipatingServiceProviderImpl bci = new OriginatingParticipatingServiceProviderImpl(getBody2());

        String[] methodNames = { "isOddFlag", "getAddress", "getOpspLengthIndicator" };
        Object[] expectedValues = { true, super.getFiveDigitsString(), 3 };
        super.testValues(bci, methodNames, expectedValues);
    }

    private ByteBuf getBody1() throws IOException {
    	ByteBuf bos = Unpooled.buffer();
        // we will use odd number of digits, so we leave zero as MSB

        bos.writeByte(3);
        bos.writeBytes(super.getSixDigits());
        return bos;
    }

    private ByteBuf getBody2() throws IOException {
    	ByteBuf bos = Unpooled.buffer();
        // we will use odd number of digits, so we leave zero as MSB

        bos.writeByte(3 | 0x80);
        bos.writeBytes(super.getFiveDigits());
        return bos;
    }

    private ByteBuf getBody3() throws IOException {
    	ByteBuf bos = Unpooled.buffer();
        // we will use odd number of digits, so we leave zero as MSB

        bos.writeByte(4);
        bos.writeBytes(super.getEightDigits());
        return bos;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new OriginatingParticipatingServiceProviderImpl("1234");
    }

}
