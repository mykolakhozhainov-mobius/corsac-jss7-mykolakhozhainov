package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

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
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

/**
 *
 * @author yulianoifa
 *
 */
public class ASNControlTypeImpl extends ASNEnumerated {
	public ASNControlTypeImpl() {
		super("ControlType",0,1,false);
	}
	
	public ASNControlTypeImpl(ControlType t) {
		super(t.getCode(),"ControlType",0,1,false);
	}
	
	public ControlType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ControlType.getInstance(realValue);
	}
}
