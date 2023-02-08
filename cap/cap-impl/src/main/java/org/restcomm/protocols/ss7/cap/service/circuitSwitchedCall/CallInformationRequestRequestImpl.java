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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.RequestedInformationTypeWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CallInformationRequestRequestImpl extends CircuitSwitchedCallMessageImpl implements CallInformationRequestRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private RequestedInformationTypeWrapperImpl requestedInformationTypeList;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private SendingLegIDWrapperImpl legID;

    public CallInformationRequestRequestImpl() {
    }

    public CallInformationRequestRequestImpl(List<RequestedInformationType> requestedInformationTypeList,
            CAPINAPExtensions extensions, LegType legID) {
    	
    	if(requestedInformationTypeList!=null)
    		this.requestedInformationTypeList = new RequestedInformationTypeWrapperImpl(requestedInformationTypeList);
    	
        this.extensions = extensions;
        
        if(legID!=null)
        	this.legID = new SendingLegIDWrapperImpl(new SendingLegIDImpl(legID));
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.callInformationRequest_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.callInformationRequest;
    }

    @Override
    public List<RequestedInformationType> getRequestedInformationTypeList() {
    	if(requestedInformationTypeList==null || requestedInformationTypeList.getRequestedInformationTypes()==null)
    		return null;
    	
    	return requestedInformationTypeList.getRequestedInformationTypes();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public LegType getLegID() {
    	if(legID==null || legID.getSendingLegID()==null)
    		return LegType.leg2;
    	
        return legID.getSendingLegID().getSendingSideID();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CallInformationRequestRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.requestedInformationTypeList != null && this.requestedInformationTypeList.getRequestedInformationTypes()!=null) {
            sb.append(", requestedInformationTypeList=[");
            boolean firstItem = true;
            for (RequestedInformationType ri : this.requestedInformationTypeList.getRequestedInformationTypes()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(ri.toString());
            }
            sb.append("]");
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        
        if (this.legID != null && this.legID.getSendingLegID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getSendingLegID());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(requestedInformationTypeList==null)
			throw new ASNParsingComponentException("requested information type list should be set for call information request request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
