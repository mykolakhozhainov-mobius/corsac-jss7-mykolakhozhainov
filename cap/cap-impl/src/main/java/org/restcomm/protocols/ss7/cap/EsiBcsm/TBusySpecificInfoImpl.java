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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TBusySpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TBusySpecificInfoImpl implements TBusySpecificInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = CauseIsupImpl.class)
    private CauseIsup busyCause;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1)
    private ASNNull callForwarded;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = false,index = -1)
    private ASNNull routeNotPermitted;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = false,index = -1, defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup forwardingDestinationNumber;

    public TBusySpecificInfoImpl() {
    }

    public TBusySpecificInfoImpl(CauseIsup busyCause, boolean callForwarded, boolean routeNotPermitted,
            CalledPartyNumberIsup forwardingDestinationNumber) {
        this.busyCause = busyCause;
        if(callForwarded)
        	this.callForwarded = new ASNNull();
        
        if(routeNotPermitted)
        	this.routeNotPermitted = new ASNNull();
        
        this.forwardingDestinationNumber = forwardingDestinationNumber;
    }

    public CauseIsup getBusyCause() {
        return busyCause;
    }

    public boolean getCallForwarded() {
        return callForwarded!=null;
    }

    public boolean getRouteNotPermitted() {
        return routeNotPermitted!=null;
    }

    public CalledPartyNumberIsup getForwardingDestinationNumber() {
        return forwardingDestinationNumber;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TBusySpecificInfo [");

        if (this.busyCause != null) {
            sb.append("busyCause= [");
            sb.append(busyCause);
            sb.append("]");
        }
        if (this.callForwarded!=null) {
            sb.append(", callForwarded");
        }
        if (this.routeNotPermitted!=null) {
            sb.append(", routeNotPermitted");
        }
        if (this.forwardingDestinationNumber != null) {
            sb.append(", forwardingDestinationNumber= [");
            sb.append(forwardingDestinationNumber);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
