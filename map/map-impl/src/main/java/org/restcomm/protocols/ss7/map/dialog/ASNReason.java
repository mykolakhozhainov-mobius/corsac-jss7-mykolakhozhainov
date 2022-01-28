package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.dialog.Reason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNReason extends ASNEnumerated {
	public ASNReason() {
		
	}
	
	public ASNReason(Reason t) {
		super(t.getCode());
	}
	
	public Reason getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return Reason.getReason(realValue);
	}
}