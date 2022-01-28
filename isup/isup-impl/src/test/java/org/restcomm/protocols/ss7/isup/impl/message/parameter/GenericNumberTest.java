/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author sergey vetyutnev
 */
public class GenericNumberTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    private ByteBuf getData() {
        return Unpooled.wrappedBuffer(new byte[] { 5, (byte) 131, (byte) 194, 0x21, 0x43, 0x05 });
    }

    private ByteBuf getData2() {
        return Unpooled.wrappedBuffer(new byte[] { 8, 0, 11 });
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        GenericNumberImpl prim = new GenericNumberImpl();
        prim.decode(getData());

        assertEquals(prim.getNatureOfAddressIndicator(), GenericNumber._NAI_NATIONAL_SN);
        assertEquals(prim.getAddress(), "12345");
        assertEquals(prim.getNumberQualifierIndicator(), GenericNumber._NQIA_CONNECTED_NUMBER);
        assertEquals(prim.getNumberingPlanIndicator(), GenericNumber._NPI_TELEX);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
        assertEquals(prim.isNumberIncomplete(), GenericNumber._NI_INCOMPLETE);
        assertEquals(prim.getScreeningIndicator(), GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        assertTrue(prim.isOddFlag());

        prim = new GenericNumberImpl();
        prim.decode(getData2());

        assertEquals(prim.getNatureOfAddressIndicator(), 0);
        assertEquals(prim.getAddress(), "");
        assertEquals(prim.getNumberQualifierIndicator(), GenericNumber._NQIA_REDIRECTING_NUMBER);
        assertEquals(prim.getNumberingPlanIndicator(), 0);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_NOT_AVAILABLE);
        assertEquals(prim.isNumberIncomplete(), GenericNumber._NI_COMPLETE);
        assertEquals(prim.getScreeningIndicator(), GenericNumber._SI_NETWORK_PROVIDED);
        assertFalse(prim.isOddFlag());
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        GenericNumberImpl prim = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        // int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int
        // addressRepresentationREstrictedIndicator,
        // boolean numberIncomplete, int screeningIndicator

        ByteBuf data = getData();
        ByteBuf encodedData=Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        prim = new GenericNumberImpl(0, "", GenericNumber._NQIA_REDIRECTING_NUMBER, 0, GenericNumber._APRI_NOT_AVAILABLE,
                GenericNumber._NI_COMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        // int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int
        // addressRepresentationREstrictedIndicator,
        // boolean numberIncomplete, int screeningIndicator

        data = getData2();        
        encodedData = Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));
    }
}
