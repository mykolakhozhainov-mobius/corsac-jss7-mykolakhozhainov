package org.restcomm.protocols.ss7.tcap.asn;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

public class ASNResultType extends ASNInteger {
	public ASNResultType() {
		
	}
	
	public ASNResultType(ResultType t) {
		super(t.getType());
	}
	
	public ResultType getType() throws ParseException {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ResultType.getFromInt(realValue);
	}
}
