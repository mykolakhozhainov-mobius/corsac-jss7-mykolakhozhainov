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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CallResultCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CallResultReportCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.TariffInformation;
import org.restcomm.protocols.ss7.inap.primitives.DateAndTimeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CallResultCS1Impl implements CallResultCS1 {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNCallResultReportCondition callResultReportCondition;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1,defaultImplementation = DateAndTimeImpl.class)
    private DateAndTime timeStamp;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true, index=-1)
    private ReceivingLegIDWrapperImpl partyToCharge;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNInteger accumulatedCharge;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true, index=-1,defaultImplementation = TariffInformationImpl.class)
    private TariffInformation actualTariff;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false, index=-1)
    private ASNInteger chargeableDuration;
		
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false, index=-1,defaultImplementation = DateAndTimeImpl.class)
    private DateAndTime timeOfAnswer;

	public CallResultCS1Impl() {
    }

    public CallResultCS1Impl(CallResultReportCondition callResultReportCondition,DateAndTime timeStamp,
    		LegType partyToCharge,Integer accumulatedCharge,TariffInformation actualTariff,
    		Integer chargeableDuration,DateAndTime timeOfAnswer) {    	
    	if(callResultReportCondition!=null)
    		this.callResultReportCondition=new ASNCallResultReportCondition(callResultReportCondition);
    	
    	this.timeStamp=timeStamp;
    	if(partyToCharge!=null)
    		this.partyToCharge=new ReceivingLegIDWrapperImpl(new ReceivingLegIDImpl(partyToCharge));
    	
    	if(accumulatedCharge!=null)
    		this.accumulatedCharge=new ASNInteger(accumulatedCharge,"AccumulatedCharge",0,65535,false);    		
    	
    	this.actualTariff=actualTariff;
    	
    	if(chargeableDuration!=null)
    		this.chargeableDuration=new ASNInteger(chargeableDuration,"ChargeableDuration",0,Integer.MAX_VALUE,false);
    		
    	this.timeOfAnswer=timeOfAnswer;
    }

    public CallResultReportCondition getCallResultReportCondition() {
    	if(callResultReportCondition==null)
    		return null;
    	
    	return callResultReportCondition.getType();
    }

    public DateAndTime getTimeStamp() {
    	return timeStamp;
    }

    @Override
    public LegType getPartyToCharge() {
    	if(partyToCharge==null || partyToCharge.getReceivingLegID()==null)
    		return null;
    	
        return partyToCharge.getReceivingLegID().getReceivingSideID();
    }

    public Integer getAccumulatedCharge() {
    	if(accumulatedCharge==null)
    		return null;
    	
    	return accumulatedCharge.getIntValue();
    }

    public TariffInformation getTariffInformation() {
    	return actualTariff;
    }

    public Integer getChargeableDuration() {
    	if(chargeableDuration==null)
    		return null;
    	
    	return chargeableDuration.getIntValue();
    }

    public DateAndTime getTimeOfAnswer() {
    	return timeOfAnswer;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CallResultCS1 [");

        if (this.callResultReportCondition != null && this.callResultReportCondition.getType()!=null) {
            sb.append(", callResultReportCondition=");
            sb.append(callResultReportCondition.getType());
        }
        
        if (this.timeStamp != null) {
            sb.append(", timeStamp=");
            sb.append(timeStamp);
        }
        
        if (this.partyToCharge != null && this.partyToCharge.getReceivingLegID()!=null) {
            sb.append(", partyToCharge=");
            sb.append(partyToCharge.getReceivingLegID());
        }
        
        if (this.accumulatedCharge != null && this.accumulatedCharge.getValue()!=null) {
            sb.append(", accumulatedCharge=");
            sb.append(accumulatedCharge.getValue());
        }
        
        if (this.actualTariff != null) {
            sb.append(", actualTariff=");
            sb.append(actualTariff);
        }
        
        if (this.chargeableDuration != null && this.chargeableDuration.getValue()!=null) {
            sb.append(", chargeableDuration=");
            sb.append(chargeableDuration.getValue());
        }
        
        if (this.timeOfAnswer != null) {
            sb.append(", timeOfAnswer=");
            sb.append(timeOfAnswer);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(callResultReportCondition==null)
			throw new ASNParsingComponentException("call result report condition should be set for call result", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(timeOfAnswer==null)
			throw new ASNParsingComponentException("timestamp should be set for call result", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(partyToCharge==null)
			throw new ASNParsingComponentException("party to charge should be set for call result", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}