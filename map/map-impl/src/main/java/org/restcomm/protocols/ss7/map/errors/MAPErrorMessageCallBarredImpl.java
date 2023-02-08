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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.errors.CallBarringCause;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageCallBarredImpl extends MAPErrorMessageImpl implements MAPErrorMessageCallBarred {
	private ASNCallBaringCauseImpl callBarringCause;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull unauthorisedMessageOriginator;

    public MAPErrorMessageCallBarredImpl(CallBarringCause callBarringCause,
    		MAPExtensionContainer extensionContainer, Boolean unauthorisedMessageOriginator) {
        super(MAPErrorCode.callBarred);

        if(callBarringCause!=null)
        	this.callBarringCause = new ASNCallBaringCauseImpl(callBarringCause);
        	
        this.extensionContainer = extensionContainer;
        if(unauthorisedMessageOriginator!=null && unauthorisedMessageOriginator)
        	this.unauthorisedMessageOriginator = new ASNNull();
    }

    public MAPErrorMessageCallBarredImpl() {
        super(MAPErrorCode.callBarred);
    }

    public boolean isEmCallBarred() {
        return true;
    }

    public MAPErrorMessageCallBarred getEmCallBarred() {
        return this;
    }

    public CallBarringCause getCallBarringCause() {
    	if(this.callBarringCause==null)
    		return null;
    	
        return this.callBarringCause.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public Boolean getUnauthorisedMessageOriginator() {    	
        return this.unauthorisedMessageOriginator!=null;
    }

    public void setCallBarringCause(CallBarringCause callBarringCause) {
    	if(callBarringCause==null)
    		this.callBarringCause=null;
    	else
    		this.callBarringCause = new ASNCallBaringCauseImpl(callBarringCause);    		
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setUnauthorisedMessageOriginator(Boolean unauthorisedMessageOriginator) {
    	if(unauthorisedMessageOriginator!=null && unauthorisedMessageOriginator)
    		this.unauthorisedMessageOriginator = new ASNNull();
    	else
    		this.unauthorisedMessageOriginator=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageCallBarred [");
        if (this.callBarringCause != null)
            sb.append("callBarringCause=" + this.callBarringCause.toString());
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        if (this.unauthorisedMessageOriginator != null)
            sb.append(", unauthorisedMessageOriginator=true");
        sb.append("]");

        return sb.toString();
    }
}
