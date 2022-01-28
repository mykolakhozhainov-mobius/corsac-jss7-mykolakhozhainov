package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCancellationTypeImpl extends ASNEnumerated {
	public ASNCancellationTypeImpl() {
		
	}
	
	public ASNCancellationTypeImpl(CancellationType t) {
		super(t.getCode());
	}
	
	public CancellationType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CancellationType.getInstance(realValue);
	}
}
