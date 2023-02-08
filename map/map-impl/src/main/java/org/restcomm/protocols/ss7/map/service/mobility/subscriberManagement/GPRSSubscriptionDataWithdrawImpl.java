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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdraw;
import org.restcomm.protocols.ss7.map.primitives.ASNIntegerListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GPRSSubscriptionDataWithdrawImpl implements GPRSSubscriptionDataWithdraw {
	private ASNNull allGPRSData;
    private ASNIntegerListWrapperImpl contextIdList;

    public GPRSSubscriptionDataWithdrawImpl() {
    }

    public GPRSSubscriptionDataWithdrawImpl(boolean allGPRSData) {
    	if(allGPRSData)
    		this.allGPRSData = new ASNNull();
    }

    public GPRSSubscriptionDataWithdrawImpl(List<Integer> contextIdList) {
    	if(contextIdList!=null) {
    		List<ASNInteger> realData=new ArrayList<ASNInteger>();
    		for(Integer curr:contextIdList) {
    			ASNInteger currData=new ASNInteger(curr,"ContextId",1,50,false);
    			realData.add(currData);
    		}
    		this.contextIdList = new ASNIntegerListWrapperImpl(realData);
    	}
    }


    public boolean getAllGPRSData() {
        return allGPRSData!=null;
    }

    public List<Integer> getContextIdList() {
    	if(this.contextIdList==null || this.contextIdList.getIntegers()==null)
    		return null;
    	
    	List<Integer> realData=new ArrayList<Integer>();
		for(ASNInteger curr:contextIdList.getIntegers()) {
			realData.add(curr.getIntValue());
		}
        return realData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("GPRSSubscriptionDataWithdraw [");

        if (this.allGPRSData!=null) {
            sb.append("allGPRSData, ");
        }
        if (this.contextIdList != null && this.contextIdList.getIntegers()!=null) {
            sb.append("contextIdList=[");
            for (ASNInteger i1 : this.contextIdList.getIntegers()) {
                sb.append(i1.getValue());
                sb.append(", ");
            }
            sb.append("], ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(allGPRSData==null && (contextIdList==null || contextIdList.getIntegers()==null || contextIdList.getIntegers().size()==0))
			throw new ASNParsingComponentException("one pf child items should be set for gprs subscription data withdraw", ASNParsingComponentExceptionReason.MistypedParameter);

		if(contextIdList!=null && contextIdList.getIntegers()!=null && contextIdList.getIntegers().size()>50)
			throw new ASNParsingComponentException("context ID list size should be between 1 and 50 for gprs subscription data withdraw", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
