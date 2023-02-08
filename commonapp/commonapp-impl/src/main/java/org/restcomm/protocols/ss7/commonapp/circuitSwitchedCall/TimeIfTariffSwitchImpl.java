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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeIfTariffSwitch;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TimeIfTariffSwitchImpl implements TimeIfTariffSwitch {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger timeSinceTariffSwitch;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger tariffSwitchInterval;

    public TimeIfTariffSwitchImpl() {
    }

    public TimeIfTariffSwitchImpl(int timeSinceTariffSwitch, Integer tariffSwitchInterval) {
        this.timeSinceTariffSwitch = new ASNInteger(timeSinceTariffSwitch,"TimeSinceTariffSwitch",0,864000,false);
        
        if(tariffSwitchInterval!=null)
        	this.tariffSwitchInterval = new ASNInteger(tariffSwitchInterval,"TariffSwitchInterval",0,864000,false);        	
    }

    public int getTimeSinceTariffSwitch() {
    	if(timeSinceTariffSwitch==null || timeSinceTariffSwitch.getValue()==null)
    		return 0;
    	
        return timeSinceTariffSwitch.getIntValue();
    }

    public Integer getTariffSwitchInterval() {
    	if(tariffSwitchInterval==null)
    		return null;
    	
        return tariffSwitchInterval.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TimeIfTariffSwitch [");

        sb.append("timeSinceTariffSwitch=");
        sb.append(timeSinceTariffSwitch);
        if (this.tariffSwitchInterval != null) {
            sb.append(", tariffSwitchInterval=");
            sb.append(tariffSwitchInterval);
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(timeSinceTariffSwitch==null)
			throw new ASNParsingComponentException("time since tariff switch should be set for time if tariff switch", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
