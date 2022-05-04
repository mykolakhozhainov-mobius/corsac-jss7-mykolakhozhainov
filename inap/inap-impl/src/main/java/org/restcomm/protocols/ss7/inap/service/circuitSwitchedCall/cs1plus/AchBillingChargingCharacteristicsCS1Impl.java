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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.AchBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ReportCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.RequestedReportInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AchBillingChargingCharacteristicsCS1Impl implements AchBillingChargingCharacteristicsCS1 {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1)
    private ReportConditionWrapperImpl reportCondition;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1, defaultImplementation = RequestedReportInfoImpl.class)
    private RequestedReportInfo requestedReportInfo;

	public AchBillingChargingCharacteristicsCS1Impl() {
    }

    public AchBillingChargingCharacteristicsCS1Impl(ReportCondition reportCondition,RequestedReportInfo requestedReportInfo) {
    	if(reportCondition!=null)
    		this.reportCondition=new ReportConditionWrapperImpl(reportCondition);
    	
    	this.requestedReportInfo=requestedReportInfo;
    }

    public ReportCondition getReportCondition() {
    	if(reportCondition==null)
    		return null;
    	
    	return reportCondition.getReportCondition();
    }

    public RequestedReportInfo getRequestedReportInfo() {
    	return requestedReportInfo;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AchBillingChargingCharacteristicsCS1 [");

        if (this.reportCondition != null && this.reportCondition.getReportCondition()!=null) {
            sb.append(", reportCondition=");
            sb.append(reportCondition.getReportCondition());
        }
        
        if (this.requestedReportInfo != null) {
            sb.append(", requestedReportInfo=");
            sb.append(requestedReportInfo);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(reportCondition==null)
			throw new ASNParsingComponentException("report condition should be set for ach billing charging characteristics", ASNParsingComponentExceptionReason.MistypedParameter);

		if(requestedReportInfo==null)
			throw new ASNParsingComponentException("request report info should be set for ach billing charging characteristics", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}