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

package org.restcomm.protocols.ss7.map.smstpdu;

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.commonapp.api.smstpdu.AbsoluteTimeStamp;
import org.restcomm.protocols.ss7.commonapp.smstpu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.restcomm.protocols.ss7.map.api.smstpdu.FailureCause;
import org.restcomm.protocols.ss7.map.api.smstpdu.ParameterIndicator;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsSubmitReportTpdu;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SmsSubmitReportTpduImpl extends SmsTpduImpl implements SmsSubmitReportTpdu {
	private boolean userDataHeaderIndicator;
    private FailureCause failureCause;
    private ParameterIndicator parameterIndicator;
    private AbsoluteTimeStamp serviceCentreTimeStamp;
    private ProtocolIdentifier protocolIdentifier;
    private DataCodingScheme dataCodingScheme;
    private int userDataLength;
    private UserData userData;

    private SmsSubmitReportTpduImpl() {
        this.tpduType = SmsTpduType.SMS_SUBMIT_REPORT;
        this.mobileOriginatedMessage = false;
    }

    public SmsSubmitReportTpduImpl(FailureCause failureCause, AbsoluteTimeStamp serviceCentreTimeStamp,
            ProtocolIdentifier protocolIdentifier, UserData userData) {
        this();

        this.failureCause = failureCause;
        this.serviceCentreTimeStamp = serviceCentreTimeStamp;
        this.protocolIdentifier = protocolIdentifier;
        this.userData = userData;
    }

    public SmsSubmitReportTpduImpl(ByteBuf stm, Charset gsm8Charset) throws MAPException {
        this();

        if (stm == null)
            throw new MAPException("Error creating a new SmsSubmitReportTpdu instance: data is empty");
        if (stm.readableBytes() < 1)
            throw new MAPException("Error creating a new SmsSubmitReportTpdu instance: data length is equal zero");

        int bt = stm.readByte() & 0x0FF;
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;

        bt = stm.readByte() & 0x0FF;
        if (bt == -1)
            throw new MAPException(
                    "Error creating a new SmsDeliverReportTpdu instance: Failure-Cause and Parameter-Indicator fields have not been found");
        if ((bt & 0x80) != 0) {
            // Failure-Cause exists
            this.failureCause = new FailureCauseImpl(bt);

            bt = stm.readByte() & 0x0FF;
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverReportTpdu instance: Parameter-Indicator field has not been found");
        }

        this.parameterIndicator = new ParameterIndicatorImpl(bt);

        try {
        	this.serviceCentreTimeStamp = AbsoluteTimeStampImpl.createMessage(stm);
        }
    	catch(ASNParsingException ex) {
    		throw new MAPException(ex.getMessage(),ex.getCause());
    	}
        
        if (this.parameterIndicator.getTP_PIDPresence()) {
            bt = stm.readByte() & 0x0FF;
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverTpduImpl instance: protocolIdentifier field has not been found");
            this.protocolIdentifier = new ProtocolIdentifierImpl(bt);
        }

        if (this.parameterIndicator.getTP_DCSPresence()) {
            bt = stm.readByte() & 0x0FF;
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverTpduImpl instance: dataCodingScheme field has not been found");
            this.dataCodingScheme = new DataCodingSchemeImpl(bt);
        }

        if (this.parameterIndicator.getTP_UDLPresence()) {
            this.userDataLength = stm.readByte() & 0x0FF;
            if (this.userDataLength == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverTpduImpl instance: userDataLength field has not been found");

            userData = new UserDataImpl(stm.readSlice(stm.readableBytes()), dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
        }
    }

    public boolean getUserDataHeaderIndicator() {
        return this.userDataHeaderIndicator;
    }

    public FailureCause getFailureCause() {
        return failureCause;
    }

    public ParameterIndicator getParameterIndicator() {
        return parameterIndicator;
    }

    public AbsoluteTimeStamp getServiceCentreTimeStamp() {
        return serviceCentreTimeStamp;
    }

    public ProtocolIdentifier getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public DataCodingScheme getDataCodingScheme() {
        return dataCodingScheme;
    }

    public int getUserDataLength() {
        return userDataLength;
    }

    public UserData getUserData() {
        return userData;
    }

    public void encodeData(ByteBuf buf) throws MAPException {

        if (this.serviceCentreTimeStamp == null)
            throw new MAPException("Parameter serviceCentreTimeStamp must not be null");

        if (this.userData != null) {
            this.userData.encode();
            this.userDataHeaderIndicator = this.userData.getEncodedUserDataHeaderIndicator();
            this.userDataLength = this.userData.getEncodedUserDataLength();
            this.dataCodingScheme = this.userData.getDataCodingScheme();

            if (this.userData.getEncodedData().readableBytes() > _UserDataSubmitReportLimit)
                throw new MAPException("User data field length may not increase " + _UserDataSubmitReportLimit);
        }

        // byte 0
        buf.writeByte(SmsTpduType.SMS_SUBMIT_REPORT.getEncodedValue() | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0));

        if (this.failureCause != null)
        	buf.writeByte(this.failureCause.getCode());

        this.parameterIndicator = new ParameterIndicatorImpl(this.userData != null, this.dataCodingScheme != null,
                this.protocolIdentifier != null);
        buf.writeByte(this.parameterIndicator.getCode());
        
        try {
        	this.serviceCentreTimeStamp.encodeData(buf);
        }
    	catch(ASNParsingException ex) {
    		throw new MAPException(ex.getMessage(),ex.getCause());
    	}
        
        if (this.protocolIdentifier != null) {
        	buf.writeByte(this.protocolIdentifier.getCode());
        }
        if (this.dataCodingScheme != null) {
        	buf.writeByte(this.dataCodingScheme.getCode());
        }

        if (this.userData != null) {
        	buf.writeByte(this.userDataLength);
        	buf.writeBytes(this.userData.getEncodedData());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SMS-SUBMIT-REPORT tpdu [");

        boolean started = false;
        if (this.userDataHeaderIndicator) {
            sb.append("userDataHeaderIndicator");
            started = true;
        }

        if (this.failureCause != null) {
            if (started)
                sb.append(", ");
            sb.append("failureCause=");
            sb.append(this.failureCause.toString());
            started = true;
        }
        if (this.parameterIndicator != null) {
            if (started)
                sb.append(", ");
            sb.append(this.parameterIndicator.toString());
            started = true;
        }
        if (this.serviceCentreTimeStamp != null) {
            if (started)
                sb.append(", ");
            sb.append("serviceCentreTimeStamp [");
            sb.append(this.serviceCentreTimeStamp.toString());
            sb.append("]");
            started = true;
        }
        if (this.protocolIdentifier != null) {
            if (started)
                sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
            started = true;
        }
        if (this.userData != null) {
            sb.append("\nMSG [");
            sb.append(this.userData.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
