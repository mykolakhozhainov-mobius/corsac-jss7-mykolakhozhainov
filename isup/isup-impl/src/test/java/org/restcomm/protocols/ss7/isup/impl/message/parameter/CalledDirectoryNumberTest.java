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
import org.restcomm.protocols.ss7.isup.message.parameter.CalledDirectoryNumber;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CalledDirectoryNumberTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public CalledDirectoryNumberTest() throws IOException {
        // super.badBodies.add(new byte[1]);

        // super.goodBodies.add(getBody1());
        // super.goodBodies.add(getBody2());
    }

    private ByteBuf getBody1() throws IOException {
    	ByteBuf bos = Unpooled.buffer();
        // we will use odd number of digits, so we leave zero as MSB

        bos.writeByte(CalledDirectoryNumber._NAI_SUBSCRIBER_NUMBER);
        int v = CalledDirectoryNumberImpl._INNI_RESERVED << 7;
        v |= CalledDirectoryNumberImpl._NPI_ISDN_NP << 4;
        bos.writeByte(v);
        bos.writeBytes(super.getSixDigits());
        return bos;
    }

    private ByteBuf getBody2() throws IOException {
    	ByteBuf bos = Unpooled.buffer();

        bos.writeByte(CalledDirectoryNumber._NAI_SUBSCRIBER_NUMBER | (0x01 << 7));
        int v = CalledDirectoryNumberImpl._INNI_RESERVED << 7;
        v |= CalledDirectoryNumberImpl._NPI_ISDN_NP << 4;
        bos.writeByte(v);
        bos.writeBytes(super.getFiveDigits());
        return bos;
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        CalledDirectoryNumberImpl bci = new CalledDirectoryNumberImpl(getBody1());
        bci.getNumberingPlanIndicator();
        String[] methodNames = { "getNumberingPlanIndicator", "getInternalNetworkNumberIndicator",
                "getNatureOfAddressIndicator", "isOddFlag", "getAddress" };
        Object[] expectedValues = { CalledDirectoryNumberImpl._NPI_ISDN_NP, CalledDirectoryNumberImpl._INNI_RESERVED,
                CalledDirectoryNumber._NAI_SUBSCRIBER_NUMBER, false, super.getSixDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        CalledDirectoryNumberImpl bci = new CalledDirectoryNumberImpl(getBody2());

        String[] methodNames = { "getNumberingPlanIndicator", "getInternalNetworkNumberIndicator",
                "getNatureOfAddressIndicator", "isOddFlag", "getAddress" };
        Object[] expectedValues = { CalledDirectoryNumberImpl._NPI_ISDN_NP, CalledDirectoryNumberImpl._INNI_RESERVED,
                CalledDirectoryNumber._NAI_SUBSCRIBER_NUMBER, true, super.getFiveDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new CalledDirectoryNumberImpl(0, "1", 1, 1);
    }

}
