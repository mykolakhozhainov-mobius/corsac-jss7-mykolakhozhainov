package org.restcomm.protocols.ss7.cap;
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

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
/**
*
* @author sergey vetyutnev
* @author yulianoifa
*/
public class Example {

    private static SccpAddress createLocalAddress() {
        return new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,  1, 146);
    }

    private static SccpAddress createRemoteAddress() {
        return new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 146);
    }

    public static void startCallSsf() throws Exception {
        CallSsfExample client = new CallSsfExample();

        client.start();

        // starting a call
        SccpAddress origAddress = createLocalAddress();
        SccpAddress destAddress = createRemoteAddress();

        int serviceKey = 1;

        CalledPartyNumber calledPartyNumber = client.getCAPProvider().getISUPParameterFactory().createCalledPartyNumber();
        calledPartyNumber.setAddress("552348762");
        calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
        calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
        CalledPartyNumberIsup calledPartyNumberCap = client.getCAPProvider().getCAPParameterFactory()
                .createCalledPartyNumber(calledPartyNumber);

        CallingPartyNumber callingPartyNumber = client.getCAPProvider().getISUPParameterFactory().createCallingPartyNumber();
        callingPartyNumber.setAddress("55998223");
        callingPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        callingPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
        callingPartyNumber.setAddressRepresentationREstrictedIndicator(CallingPartyNumber._APRI_ALLOWED);
        callingPartyNumber.setScreeningIndicator(CallingPartyNumber._SI_NETWORK_PROVIDED);
        CallingPartyNumberIsup callingPartyNumberCap = client.getCAPProvider().getCAPParameterFactory()
                .createCallingPartyNumber(callingPartyNumber);

        LocationNumber locationNumber = client.getCAPProvider().getISUPParameterFactory().createLocationNumber();
        locationNumber.setAddress("55200001");
        locationNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        locationNumber.setNumberingPlanIndicator(LocationNumber._NPI_ISDN);
        locationNumber.setAddressRepresentationRestrictedIndicator(LocationNumber._APRI_ALLOWED);
        locationNumber.setScreeningIndicator(LocationNumber._SI_NETWORK_PROVIDED);
        locationNumber.setInternalNetworkNumberIndicator(LocationNumber._INN_ROUTING_ALLOWED);
        LocationNumberIsup locationNumberCap = client.getCAPProvider().getCAPParameterFactory()
                .createLocationNumber(locationNumber);

        ISDNAddressString vlrNumber = client.getCAPProvider().getCAPParameterFactory()
                .createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "552000002");
        LocationInformation locationInformation = client
                .getCAPProvider()
                .getCAPParameterFactory()
                .createLocationInformation(10, null, vlrNumber, null, null, null, null, vlrNumber, null, false, false, null,
                        null);

        client.sendInitialDP(origAddress, destAddress, serviceKey, calledPartyNumberCap, callingPartyNumberCap,
                locationNumberCap, EventTypeBCSM.collectedInfo, locationInformation);

        // sending oAnswer in 5 sec
        Thread.sleep(5000);
        OAnswerSpecificInfo oAnswerSpecificInfo = client.getCAPProvider().getCAPParameterFactory()
                .createOAnswerSpecificInfo(null, false, false, null, null, null);        
        MiscCallInfo miscCallInfo = client.getCAPProvider().getCAPParameterFactory()
                .createMiscCallInfo(MiscCallInfoMessageType.notification, null);
        client.sendEventReportBCSM_OAnswer(oAnswerSpecificInfo, LegType.leg2, miscCallInfo);

        // sending oDisconnect in 20 sec
        Thread.sleep(20000);
        CauseIndicators causeIndicators = client.getCAPProvider().getISUPParameterFactory().createCauseIndicators();
        causeIndicators.setLocation(CauseIndicators._LOCATION_USER);
        causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
        causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
        CauseIsup releaseCause = client.getCAPProvider().getCAPParameterFactory().createCause(causeIndicators);
        ODisconnectSpecificInfo oDisconnectSpecificInfo = client.getCAPProvider().getCAPParameterFactory()
                .createODisconnectSpecificInfo(releaseCause);
        miscCallInfo = client.getCAPProvider().getCAPParameterFactory()
                .createMiscCallInfo(MiscCallInfoMessageType.notification, null);
        client.sendEventReportBCSM_ODisconnect(oDisconnectSpecificInfo, LegType.leg1, miscCallInfo);

        // wait for answer
        Thread.sleep(600000);

        client.stop();
    }

    public static void startCallScf() throws Exception {
        CallScfExample server = new CallScfExample();

        server.start();

        // wait for a request
        Thread.sleep(600000);

        server.stop();
    }
}
