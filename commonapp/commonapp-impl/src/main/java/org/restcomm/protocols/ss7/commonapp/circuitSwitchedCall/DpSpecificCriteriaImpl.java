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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DpSpecificCriteria;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DpSpecificCriteriaAlt;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.MidCallControlInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DpSpecificCriteriaImpl implements DpSpecificCriteria {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger applicationTimer;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = MidCallControlInfoImpl.class)
    private MidCallControlInfo midCallControlInfo;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1, defaultImplementation = DpSpecificCriteriaAltImpl.class)
    private DpSpecificCriteriaAlt dpSpecificCriteriaAlt;

    public DpSpecificCriteriaImpl() {
    }

    public DpSpecificCriteriaImpl(Integer applicationTimer) {
    	if(applicationTimer!=null)
    		this.applicationTimer = new ASNInteger(applicationTimer,"ApplicationTimer",0,2047,false);    		
    }

    public DpSpecificCriteriaImpl(MidCallControlInfo midCallControlInfo) {
        this.midCallControlInfo = midCallControlInfo;
    }

    public DpSpecificCriteriaImpl(DpSpecificCriteriaAlt dpSpecificCriteriaAlt) {
        this.dpSpecificCriteriaAlt = dpSpecificCriteriaAlt;
    }

    public Integer getApplicationTimer() {
    	if(applicationTimer==null)
    		return null;
    	
        return applicationTimer.getIntValue();
    }

    public MidCallControlInfo getMidCallControlInfo() {    	
        return midCallControlInfo;
    }

    public DpSpecificCriteriaAlt getDpSpecificCriteriaAlt() {
        return dpSpecificCriteriaAlt;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DpSpecificCriteria [");
        if (this.applicationTimer != null) {
            sb.append("applicationTimer=");
            sb.append(applicationTimer.getValue());
        }
        if (this.midCallControlInfo != null) {
            sb.append("midCallControlInfo=");
            sb.append(midCallControlInfo);
        }
        if (this.dpSpecificCriteriaAlt != null) {
            sb.append("dpSpecificCriteriaAlt=");
            sb.append(dpSpecificCriteriaAlt);
        }
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(applicationTimer==null && midCallControlInfo==null && dpSpecificCriteriaAlt==null)
			throw new ASNParsingComponentException("application timer,midcall control info or dp specific criteria alt should be set for dp specific criteria", ASNParsingComponentExceptionReason.MistypedParameter);		    				
	}
}
