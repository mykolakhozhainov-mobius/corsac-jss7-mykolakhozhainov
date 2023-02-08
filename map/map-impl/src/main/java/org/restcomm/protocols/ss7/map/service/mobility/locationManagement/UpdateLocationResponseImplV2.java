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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class UpdateLocationResponseImplV2 extends MobilityMessageImpl implements UpdateLocationResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0,defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString hlrNumber;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
	private ASNNull addCapability;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull pagingAreaCapability;

    public UpdateLocationResponseImplV2() {
    }

    public UpdateLocationResponseImplV2(ISDNAddressString hlrNumber,
    		MAPExtensionContainer extensionContainer, boolean addCapability, boolean pagingAreaCapability) {
        this.hlrNumber = hlrNumber;
        this.extensionContainer = extensionContainer;
        
        if(addCapability)
        	this.addCapability = new ASNNull();
        
        if(pagingAreaCapability)
        	this.pagingAreaCapability = new ASNNull();
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.updateLocation_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.updateLocation;
    }

    public ISDNAddressString getHlrNumber() {
        return hlrNumber;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public boolean getAddCapability() {
        return addCapability!=null;
    }

    public boolean getPagingAreaCapability() {
        return pagingAreaCapability!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateLocationResponse [");

        if (this.hlrNumber != null) {
            sb.append("hlrNumber=");
            sb.append(hlrNumber.toString());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }
        if (this.addCapability!=null) {
            sb.append("addCapability, ");
        }
        if (this.pagingAreaCapability!=null) {
            sb.append("pagingAreaCapability, ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(hlrNumber==null)
			throw new ASNParsingComponentException("hlr number should be set for update location response", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
