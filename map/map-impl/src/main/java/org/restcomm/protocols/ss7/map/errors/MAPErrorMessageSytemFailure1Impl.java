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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.errors.AdditionalNetworkResource;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 */
public class MAPErrorMessageSytemFailure1Impl extends EnumeratedMAPErrorMessage1Impl implements
MAPErrorMessageSystemFailure {
	public MAPErrorMessageSytemFailure1Impl(NetworkResource networkResource) {
        super(MAPErrorCode.systemFailure,"SystemFailure",0,7);

        if(networkResource!=null)
        	setValue(networkResource.getCode());
    }

    public MAPErrorMessageSytemFailure1Impl() {
        super(MAPErrorCode.systemFailure,"SystemFailure",0,7);
    }

    public boolean isEmSystemFailure() {
        return true;
    }

    public MAPErrorMessageSystemFailure getEmSystemFailure() {
        return this;
    }

    @Override
    public NetworkResource getNetworkResource() {
    	Integer value=getValue();
    	if(value==null)
    		return null;
    	
    	return NetworkResource.getInstance(value.intValue());
    }

    @Override
    public void setNetworkResource(NetworkResource val) {
    	if(val!=null)
    		setValue(val.getCode());
    	else
    		setValue(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageSystemFailure [");

        NetworkResource networkResource=getNetworkResource();
        if (networkResource != null)
            sb.append("networkResource=" + networkResource.toString());
        
        sb.append("]");

        return sb.toString();
    }

	@Override
	public AdditionalNetworkResource getAdditionalNetworkResource() {
		return null;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return null;
	}

	@Override
	public void setAdditionalNetworkResource(AdditionalNetworkResource additionalNetworkResource) {
		
	}

	@Override
	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		
	}
}
