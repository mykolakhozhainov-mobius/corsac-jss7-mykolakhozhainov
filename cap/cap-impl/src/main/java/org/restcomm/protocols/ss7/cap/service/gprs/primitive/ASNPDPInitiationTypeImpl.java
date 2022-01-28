package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNPDPInitiationTypeImpl extends ASNEnumerated {
	public ASNPDPInitiationTypeImpl() {
		
	}
	
	public ASNPDPInitiationTypeImpl(PDPInitiationType t) {
		super(t.getCode());
	}
	
	public PDPInitiationType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return PDPInitiationType.getInstance(realValue);
	}
}
