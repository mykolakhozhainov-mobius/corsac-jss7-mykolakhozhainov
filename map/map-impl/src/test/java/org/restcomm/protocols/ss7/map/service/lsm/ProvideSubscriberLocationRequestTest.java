/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.lsm.Area;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;
import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class ProvideSubscriberLocationRequestTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public byte[] getEncodedData() {
        // The trace is from Brazilian operator
        return new byte[] { 0x30, 0x41, 0x30, 0x03, (byte) 0x80, 0x01, 0x00, 0x04, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70,
                (byte) 0xa0, 0x1b, (byte) 0x80, 0x01, 0x02, (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x13, (byte) 0x80, 0x01,
                0x0f, (byte) 0x82, 0x0e, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72,
                (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, (byte) 0x82, 0x08, 0x27, (byte) 0x94, (byte) 0x99, 0x09,
                0x00, 0x00, 0x00, (byte) 0xf7, (byte) 0x86, 0x01, 0x01, (byte) 0xa7, 0x05, (byte) 0xa3, 0x03, 0x0a, 0x01, 0x00,
                (byte) 0x89, 0x02, 0x01, (byte) 0xfe };
    }

    public byte[] getEncodedDataFull() {
        return new byte[] { 48, -127, -93, 48, 3, -128, 1, 0, 4, 5, -111, 85, 22, 9, 112, -96, 27, -128, 1, 2, -125, 1, 0, -92,
                19, -128, 1, 15, -126, 14, 110, 114, -5, 28, -122, -61, 101, 110, 114, -5, 28, -122, -61, 101, -127, 0, -126,
                8, 39, -108, -103, 9, 0, 0, 0, -9, -125, 6, -111, 103, 69, 35, 1, -16, -124, 4, 31, 32, 33, 34, -123, 8, 33,
                67, 101, -121, 9, 33, 67, 101, -122, 1, 1, -89, 5, -93, 3, 10, 1, 0, -119, 2, 1, -2, -118, 1, 5, -117, 1, 6,
                -84, 12, -128, 1, 15, -127, 7, 120, 124, 62, -97, -41, -21, 27, -83, 6, -128, 1, 1, -127, 1, 0, -82, 13, -96,
                11, -96, 9, 48, 7, -128, 1, 0, -127, 2, 82, -16, -113, 5, 4, 42, 43, 44, 45, -112, 0, -79, 7, 2, 2, 0, -56, 2,
                1, 100, -78, 9, -95, 7, 48, 5, -128, 3, 51, 52, 53 };
    }
    
    public byte[] getEncodedData2() {
        return new byte[] { 0x30,0x1c,0x04,0x0d,(byte)0xa0,0x1b,(byte)0xde,(byte)0xf1,0x1a,0x78,0x28,0x00,0x54,0x20,0x01,0x14,0x43,(byte)0x86,0x09,(byte)0x80,0x07,0x36,(byte)0xf4,0x70,0x00,0x21,(byte)0x97,0x7d,(byte)0x87,0x00 };
    }

    public byte[] getDataLmsi() {
        return new byte[] { 31, 32, 33, 34 };
    }

    public byte[] getDataHgmlcAddress() {
        return new byte[] { 42, 43, 44, 45 };
    }

    public byte[] getPlmnId() {
        return new byte[] { 51, 52, 53 };
    }

    @Test
    public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideSubscriberLocationRequestImpl.class);
    	
        byte[] data = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideSubscriberLocationRequestImpl);
        ProvideSubscriberLocationRequestImpl reqInd = (ProvideSubscriberLocationRequestImpl)result.getResult();

        LocationType locationType = reqInd.getLocationType();
        assertNotNull(locationType);
        assertEquals(locationType.getLocationEstimateType(), LocationEstimateType.currentLocation);

        ISDNAddressString mlcNumber = reqInd.getMlcNumber();
        assertNotNull(mlcNumber);
        assertEquals(mlcNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mlcNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mlcNumber.getAddress(), "55619007");

        LCSClientID lcsClientId = reqInd.getLCSClientID();
        assertNotNull(lcsClientId);
        assertEquals(lcsClientId.getLCSClientType(), LCSClientType.plmnOperatorServices);
        assertEquals(lcsClientId.getLCSClientInternalID(), LCSClientInternalID.broadcastService);
        LCSClientName lcsClientName = lcsClientId.getLCSClientName();
        assertNotNull(lcsClientName);
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        assertTrue(lcsClientName.getNameString().getString(null).equals("ndmgapp2ndmgapp2"));

        IMSI imsi = reqInd.getIMSI();
        assertNotNull(imsi);
        assertEquals(imsi.getData(), "724999900000007");

        assertEquals(reqInd.getLCSPriority(), LCSPriority.normalPriority);

        LCSQoS lcsQoS = reqInd.getLCSQoS();
        assertNotNull(lcsQoS);
        ResponseTime respTime = lcsQoS.getResponseTime();
        assertNotNull(respTime);
        assertEquals(respTime.getResponseTimeCategory(), ResponseTimeCategory.lowdelay);

        SupportedGADShapes suppGadShapes = reqInd.getSupportedGADShapes();
        assertNotNull(suppGadShapes);
        assertTrue(suppGadShapes.getEllipsoidArc());
        assertTrue(suppGadShapes.getEllipsoidPoint());
        assertTrue(suppGadShapes.getEllipsoidPointWithAltitude());
        assertTrue(suppGadShapes.getEllipsoidPointWithAltitudeAndUncertaintyElipsoid());

        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyCircle());
        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyEllipse());
        assertTrue(suppGadShapes.getPolygon());

        assertFalse(reqInd.getPrivacyOverride());
        assertNull(reqInd.getMSISDN());
        assertNull(reqInd.getLMSI());
        assertNull(reqInd.getIMEI());
        assertNull(reqInd.getExtensionContainer());
        assertNull(reqInd.getLCSReferenceNumber());
        assertNull(reqInd.getLCSServiceTypeID());
        assertNull(reqInd.getLCSCodeword());
        assertNull(reqInd.getLCSPrivacyCheck());
        assertNull(reqInd.getAreaEventInfo());
        assertNull(reqInd.getHGMLCAddress());
        assertFalse(reqInd.getMoLrShortCircuitIndicator());
        assertNull(reqInd.getPeriodicLDRInfo());
        assertNull(reqInd.getReportingPLMNList());

        data = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideSubscriberLocationRequestImpl);
        reqInd = (ProvideSubscriberLocationRequestImpl)result.getResult();

        locationType = reqInd.getLocationType();
        assertNotNull(locationType);
        assertEquals(locationType.getLocationEstimateType(), LocationEstimateType.currentLocation);

        mlcNumber = reqInd.getMlcNumber();
        assertNotNull(mlcNumber);
        assertEquals(mlcNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mlcNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mlcNumber.getAddress(), "55619007");

        lcsClientId = reqInd.getLCSClientID();
        assertNotNull(lcsClientId);
        assertEquals(lcsClientId.getLCSClientType(), LCSClientType.plmnOperatorServices);
        assertEquals(lcsClientId.getLCSClientInternalID(), LCSClientInternalID.broadcastService);
        lcsClientName = lcsClientId.getLCSClientName();
        assertNotNull(lcsClientName);
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        assertTrue(lcsClientName.getNameString().getString(null).equals("ndmgapp2ndmgapp2"));

        imsi = reqInd.getIMSI();
        assertNotNull(imsi);
        assertEquals(imsi.getData(), "724999900000007");

        assertEquals(reqInd.getLCSPriority(), LCSPriority.normalPriority);

        lcsQoS = reqInd.getLCSQoS();
        assertNotNull(lcsQoS);
        respTime = lcsQoS.getResponseTime();
        assertNotNull(respTime);
        assertEquals(respTime.getResponseTimeCategory(), ResponseTimeCategory.lowdelay);

        suppGadShapes = reqInd.getSupportedGADShapes();
        assertNotNull(suppGadShapes);
        assertTrue(suppGadShapes.getEllipsoidArc());
        assertTrue(suppGadShapes.getEllipsoidPoint());
        assertTrue(suppGadShapes.getEllipsoidPointWithAltitude());
        assertTrue(suppGadShapes.getEllipsoidPointWithAltitudeAndUncertaintyElipsoid());

        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyCircle());
        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyEllipse());
        assertTrue(suppGadShapes.getPolygon());

        assertTrue(reqInd.getPrivacyOverride());
        assertTrue(reqInd.getMSISDN().getAddress().equals("765432100"));
        assertTrue(ByteBufUtil.equals(reqInd.getLMSI().getValue(), Unpooled.wrappedBuffer(getDataLmsi())));
        assertTrue(reqInd.getIMEI().getIMEI().equals("1234567890123456"));
        assertNull(reqInd.getExtensionContainer());
        assertEquals((int) reqInd.getLCSReferenceNumber(), 5);
        assertEquals((int) reqInd.getLCSServiceTypeID(), 6);
        assertTrue(reqInd.getLCSCodeword().getLCSCodewordString().getString(null).equals("xxyyyzz"));
        assertEquals(reqInd.getLCSPrivacyCheck().getCallSessionUnrelated(), PrivacyCheckRelatedAction.allowedWithNotification);
        assertEquals(reqInd.getLCSPrivacyCheck().getCallSessionRelated(), PrivacyCheckRelatedAction.allowedWithoutNotification);

        assertEquals(reqInd.getAreaEventInfo().getAreaDefinition().getAreaList().size(), 1);
        assertEquals(reqInd.getAreaEventInfo().getAreaDefinition().getAreaList().get(0).getAreaIdentification().getMCC(), 250);

        assertEquals(reqInd.getHGMLCAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(reqInd.getHGMLCAddress().getGSNAddressData(),Unpooled.wrappedBuffer(getDataHgmlcAddress())));
        
        assertTrue(reqInd.getMoLrShortCircuitIndicator());
        assertEquals(reqInd.getPeriodicLDRInfo().getReportingAmount(), 200);
        assertEquals(reqInd.getPeriodicLDRInfo().getReportingInterval(), 100);
        assertEquals(reqInd.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMcc(), 334);
        assertEquals(reqInd.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMnc(), 533);
    }

    @Test
    public void testDecodeProvideSubscriberLocationResponseIndication() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideSubscriberLocationResponseImpl.class);
    	
        byte[] data = getEncodedData2();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideSubscriberLocationResponseImpl);        
    }
    
    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideSubscriberLocationRequestImpl.class);
    	
        LocationTypeImpl locationType = new LocationTypeImpl(LocationEstimateType.currentLocation, null);
        ISDNAddressStringImpl mlcNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "55619007");

        USSDString nameString = MAPParameterFactory.createUSSDString("ndmgapp2ndmgapp2");
        LCSClientNameImpl lcsClientName = new LCSClientNameImpl(new CBSDataCodingSchemeImpl(0x0f), nameString, null);

        LCSClientIDImpl lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null,
                LCSClientInternalID.broadcastService, lcsClientName, null, null, null);

        IMSI imsi = MAPParameterFactory.createIMSI("724999900000007");

        LCSQoSImpl lcsQoS = new LCSQoSImpl(null, null, false, new ResponseTimeImpl(ResponseTimeCategory.lowdelay), null);

        SupportedGADShapesImpl supportedGADShapes = new SupportedGADShapesImpl(true, true, true, true, true, true, true);

        ProvideSubscriberLocationRequestImpl reqInd = new ProvideSubscriberLocationRequestImpl(locationType, mlcNumber,
                lcsClientID, false, imsi, null, null, null, LCSPriority.normalPriority, lcsQoS, null, supportedGADShapes, null,
                null, null, null, null, null, false, null, null);

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(reqInd);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "765432100");
        LMSIImpl lmsi = new LMSIImpl(Unpooled.wrappedBuffer(getDataLmsi()));
        IMEIImpl imei = new IMEIImpl("1234567890123456");
        USSDString lcsCodewordString = MAPParameterFactory.createUSSDString("xxyyyzz");
        LCSCodewordImpl lcsCodeword = new LCSCodewordImpl(new CBSDataCodingSchemeImpl(0x0f), lcsCodewordString);
        LCSPrivacyCheckImpl lcsPrivacyCheck = new LCSPrivacyCheckImpl(PrivacyCheckRelatedAction.allowedWithNotification,
                PrivacyCheckRelatedAction.allowedWithoutNotification);
        List<Area> areaList = new ArrayList<Area>();
        AreaIdentificationImpl areaIdentification = new AreaIdentificationImpl(AreaType.countryCode, 250, 0, 0, 0);
        AreaImpl area = new AreaImpl(AreaType.countryCode, areaIdentification);
        areaList.add(area);
        AreaDefinition areaDefinition = new AreaDefinitionImpl(areaList);
        AreaEventInfoImpl areaEventInfo = new AreaEventInfoImpl(areaDefinition, null, null);
        GSNAddressImpl hgmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4,Unpooled.wrappedBuffer(getDataHgmlcAddress()));
        PeriodicLDRInfoImpl periodicLDRInfo = new PeriodicLDRInfoImpl(200, 100);
        List<ReportingPLMN> lstRplmn = new ArrayList<ReportingPLMN>();
        PlmnIdImpl plmnId = new PlmnIdImpl(334,533);
        ReportingPLMNImpl rplmn = new ReportingPLMNImpl(plmnId, null, false);
        lstRplmn.add(rplmn);
        ReportingPLMNList reportingPLMNList = new ReportingPLMNListImpl(false, lstRplmn);

        reqInd = new ProvideSubscriberLocationRequestImpl(locationType, mlcNumber, lcsClientID, true, imsi, msisdn, lmsi, imei,
                LCSPriority.normalPriority, lcsQoS, null, supportedGADShapes, 5, 6, lcsCodeword, lcsPrivacyCheck,
                areaEventInfo, hgmlcAddress, true, periodicLDRInfo, reportingPLMNList);

        data=getEncodedDataFull();
        buffer=parser.encode(reqInd);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}