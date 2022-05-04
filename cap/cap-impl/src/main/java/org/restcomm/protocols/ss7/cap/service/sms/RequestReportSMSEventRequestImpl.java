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

package org.restcomm.protocols.ss7.cap.service.sms;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.sms.RequestReportSMSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.SMSEventWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class RequestReportSMSEventRequestImpl extends SmsMessageImpl implements RequestReportSMSEventRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private SMSEventWrapperImpl smsEvents;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public RequestReportSMSEventRequestImpl(List<SMSEvent> smsEvents, CAPINAPExtensions extensions) {
        super();
        
        if(smsEvents!=null)
        	this.smsEvents = new SMSEventWrapperImpl(smsEvents);
        
        this.extensions = extensions;
    }

    public RequestReportSMSEventRequestImpl() {
        super();
    }

    @Override
    public List<SMSEvent> getSMSEvents() {
    	if(this.smsEvents==null)
    		return null;
    	
        return this.smsEvents.getSMSEvents();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return this.extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.requestReportSMSEvent_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.requestReportSMSEvent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestReportSMSEventRequest [");
        this.addInvokeIdInfo(sb);

        if (this.smsEvents != null && this.smsEvents.getSMSEvents()!=null) {
            sb.append(", smsEvents=[");
            int i1 = 0;
            for (SMSEvent evt : this.smsEvents.getSMSEvents()) {
                if (i1 == 0)
                    i1 = 1;
                else
                    sb.append(", ");
                sb.append("smsEvent=");
                sb.append(evt.toString());
            }
            sb.append("]");
        }

        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(this.extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(smsEvents==null || smsEvents.getSMSEvents()==null || smsEvents.getSMSEvents().size()==0)
			throw new ASNParsingComponentException("sms events should be set for request report sms request", ASNParsingComponentExceptionReason.MistypedRootParameter);
		
		if(smsEvents.getSMSEvents().size()>10)
			throw new ASNParsingComponentException("sms events size should be between 1 and 10 for request report sms request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
