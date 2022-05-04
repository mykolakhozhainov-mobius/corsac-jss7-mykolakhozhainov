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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author daniel bichara
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GPRSSubscriptionDataImpl implements GPRSSubscriptionData {
	private ASNNull completeDataListIncluded = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private PDPContextListWrapperImpl gprsDataList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1,defaultImplementation = APNOIReplacementImpl.class)
    private APNOIReplacement apnOiReplacement = null;

    public GPRSSubscriptionDataImpl() {
    }

    public GPRSSubscriptionDataImpl(boolean completeDataListIncluded, List<PDPContext> gprsDataList,
            MAPExtensionContainer extensionContainer, APNOIReplacement apnOiReplacement) {
        if(completeDataListIncluded)
        	this.completeDataListIncluded = new ASNNull();
        
        if(gprsDataList!=null)
        	this.gprsDataList = new PDPContextListWrapperImpl(gprsDataList);
        
        this.extensionContainer = extensionContainer;
        this.apnOiReplacement = apnOiReplacement;
    }

    public boolean getCompleteDataListIncluded() {
        return this.completeDataListIncluded!=null;
    }

    public List<PDPContext> getGPRSDataList() {
    	if(this.gprsDataList==null)
    		return null;
    	
        return this.gprsDataList.getPDPContextList();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public APNOIReplacement getApnOiReplacement() {
        return this.apnOiReplacement;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GPRSSubscriptionData [");

        if (this.getCompleteDataListIncluded()) {
            sb.append("completeDataListIncluded, ");
        }

        if (this.gprsDataList != null && this.gprsDataList.getPDPContextList()!=null) {
            sb.append("gprsDataList=[");
            boolean firstItem = true;
            for (PDPContext be : this.gprsDataList.getPDPContextList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.apnOiReplacement != null) {
            sb.append("apnOiReplacement=");
            sb.append(this.apnOiReplacement.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(gprsDataList==null || gprsDataList.getPDPContextList()==null || gprsDataList.getPDPContextList().size()==0)
			throw new ASNParsingComponentException("gprs data list should be set for gprs subscription data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(gprsDataList.getPDPContextList().size()>50)
			throw new ASNParsingComponentException("gprs data list size should be between 1 and 50 for gprs subscription data", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
