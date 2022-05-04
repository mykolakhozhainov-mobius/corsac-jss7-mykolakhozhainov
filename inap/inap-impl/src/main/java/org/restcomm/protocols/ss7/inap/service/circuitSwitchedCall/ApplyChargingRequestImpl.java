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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.AChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.AChBillingChargingCharacteristicsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ApplyChargingRequestImpl extends CircuitSwitchedCallMessageImpl implements ApplyChargingRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = AChBillingChargingCharacteristicsImpl.class)
    private AChBillingChargingCharacteristics aChBillingChargingCharacteristics;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNBoolean sendCalculationToSCPIndication;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private LegIDWrapperImpl partyToCharge;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public ApplyChargingRequestImpl() {
    }

    public ApplyChargingRequestImpl(AChBillingChargingCharacteristics aChBillingChargingCharacteristics,
    		Boolean sendCalculationToSCPIndication, LegID partyToCharge, CAPINAPExtensions extensions) {
        this.aChBillingChargingCharacteristics = aChBillingChargingCharacteristics;
        
        if(sendCalculationToSCPIndication!=null)
        	this.sendCalculationToSCPIndication=new ASNBoolean(sendCalculationToSCPIndication,"SendCalculationToSCPIndication",true,false);
        	
        
        if(partyToCharge!=null)
        	this.partyToCharge = new LegIDWrapperImpl(partyToCharge);
        
        this.extensions = extensions;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.applyCharging_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.applyCharging;
    }

    @Override
    public AChBillingChargingCharacteristics getAChBillingChargingCharacteristics() {
        return aChBillingChargingCharacteristics;
    }

    @Override
    public Boolean getSendCalculationToSCPIndication() {
    	if(sendCalculationToSCPIndication==null || sendCalculationToSCPIndication.getValue()==null)
    		return false;
    	
        return sendCalculationToSCPIndication.getValue();
    }

    @Override
    public LegID getPartyToCharge() {
    	if(partyToCharge==null || partyToCharge.getLegID()==null)
    		return null;
    	
        return partyToCharge.getLegID();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ApplyChargingRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.aChBillingChargingCharacteristics != null) {
            sb.append(", aChBillingChargingCharacteristics=");
            sb.append(aChBillingChargingCharacteristics.toString());
        }
        
        if (this.sendCalculationToSCPIndication != null && sendCalculationToSCPIndication.getValue()!=null) {
            sb.append(", sendCalculationToSCPIndication=");
            sb.append(sendCalculationToSCPIndication.getValue());
        }
        
        if (this.partyToCharge != null && this.partyToCharge.getLegID()!=null) {
            sb.append(", partyToCharge=");
            sb.append(partyToCharge.getLegID());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(aChBillingChargingCharacteristics==null)
			throw new ASNParsingComponentException("ach billing charging characteristics should be set for apply charging request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
