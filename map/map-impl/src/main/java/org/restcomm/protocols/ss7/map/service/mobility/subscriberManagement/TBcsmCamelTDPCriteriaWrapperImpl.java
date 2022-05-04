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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.TBcsmCamelTdpCriteriaImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class TBcsmCamelTDPCriteriaWrapperImpl {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = TBcsmCamelTdpCriteriaImpl.class)
	private List<TBcsmCamelTdpCriteria> tBcsmCamelTDPCriteriaList;

    public TBcsmCamelTDPCriteriaWrapperImpl() {
    }

    public TBcsmCamelTDPCriteriaWrapperImpl(List<TBcsmCamelTdpCriteria> tBcsmCamelTDPCriteriaList) {
        this.tBcsmCamelTDPCriteriaList = tBcsmCamelTDPCriteriaList;
    }

    public List<TBcsmCamelTdpCriteria> getTBcsmCamelTDPCriteriaList() {
    	return tBcsmCamelTDPCriteriaList;
    }
}
