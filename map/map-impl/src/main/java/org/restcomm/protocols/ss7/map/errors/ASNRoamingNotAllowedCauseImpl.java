package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.RoamingNotAllowedCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRoamingNotAllowedCauseImpl extends ASNEnumerated {
	public ASNRoamingNotAllowedCauseImpl() {
		
	}
	
	public ASNRoamingNotAllowedCauseImpl(RoamingNotAllowedCause t) {
		super(t.getCode());
	}
	
	public RoamingNotAllowedCause getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return RoamingNotAllowedCause.getInstance(realValue);
	}
}