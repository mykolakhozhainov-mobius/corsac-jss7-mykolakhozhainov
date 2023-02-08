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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.DpSpecificInfoAlt;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OServiceChangeSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TServiceChangeSpecificInfo;

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
public class DpSpecificInfoAltImpl implements DpSpecificInfoAlt {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 0, constructed = true,index = -1, defaultImplementation = OServiceChangeSpecificInfoImpl.class)
    private OServiceChangeSpecificInfo oServiceChangeSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 1, constructed = true,index = -1, defaultImplementation = TServiceChangeSpecificInfoImpl.class)
    private TServiceChangeSpecificInfo tServiceChangeSpecificInfo;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 2, constructed = true,index = -1, defaultImplementation = CollectedInfoSpecificInfoImpl.class)
    private CollectedInfoSpecificInfo collectedInfoSpecificInfo;
    
    public DpSpecificInfoAltImpl() {
    }

    public DpSpecificInfoAltImpl(OServiceChangeSpecificInfo oServiceChangeSpecificInfo, CollectedInfoSpecificInfo collectedInfoSpecificInfo,
            TServiceChangeSpecificInfo tServiceChangeSpecificInfo) {
        this.oServiceChangeSpecificInfo = oServiceChangeSpecificInfo;
        this.collectedInfoSpecificInfo = collectedInfoSpecificInfo;
        this.tServiceChangeSpecificInfo = tServiceChangeSpecificInfo;
    }

    public OServiceChangeSpecificInfo getOServiceChangeSpecificInfo() {
        return oServiceChangeSpecificInfo;
    }

    public CollectedInfoSpecificInfo getCollectedInfoSpecificInfo() {
        return collectedInfoSpecificInfo;
    }

    public TServiceChangeSpecificInfo getTServiceChangeSpecificInfo() {
        return tServiceChangeSpecificInfo;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DpSpecificInfoAlt [");

        if (this.oServiceChangeSpecificInfo != null) {
            sb.append("oServiceChangeSpecificInfo=");
            sb.append(oServiceChangeSpecificInfo);
            sb.append(", ");
        }
        if (this.tServiceChangeSpecificInfo != null) {
            sb.append("tServiceChangeSpecificInfo=");
            sb.append(tServiceChangeSpecificInfo);
            sb.append(", ");
        }
        if (this.collectedInfoSpecificInfo != null) {
            sb.append("collectedInfoSpecificInfo=");
            sb.append(collectedInfoSpecificInfo);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(oServiceChangeSpecificInfo==null)
			throw new ASNParsingComponentException("oservice change specific info should be set for dp specific info alt", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(tServiceChangeSpecificInfo==null)
			throw new ASNParsingComponentException("tservice change specific info should be set for dp specific info alt", ASNParsingComponentExceptionReason.MistypedParameter);			
	
		if(collectedInfoSpecificInfo==null)
			throw new ASNParsingComponentException("collected info specific info should be set for dp specific info alt", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
