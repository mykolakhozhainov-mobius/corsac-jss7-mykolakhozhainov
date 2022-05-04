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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

/**
 *
CallInformationRequest ::= OPERATION
ARGUMENT CallInformationRequestArg
ERRORS {
	MissingParameter,
	ParameterOutOfRange,
	RequestedInfoError,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedParameter
}
-- Direction: SCF -> SSF, Timer: Tcirq -- This operation is used to request the SSF to record specific information about a single call
-- and report it to the SCF (with a callInformationReport operation).

CallInformationRequestArg ::= SEQUENCE {
	requestedInformationTypeList [0] RequestedInformationTypeList,
	extensions [2] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}

--- From Q.1218 CS1
CallInformationRequestArg ::= SEQUENCE {
	requestedInformationTypeList [0] RequestedInformationTypeList,
	correlationID [1] CorrelationID OPTIONAL,
	extensions [2] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
-- OPTIONAL denotes network operator optional.

--- From CS1+ Spec
CallInformationRequestArg ::= SEQUENCE {
	legID [PRIVATE 01] SendingSideID OPTIONAL,
	requestedInformationTypeList [00] RequestedInformationTypeList,
	extensions [02] SEQUENCE SIZE (1..7) OF ExtensionField1 OPTIONAL
‐‐ ...
}
 *
 * @author yulian.oifa
 *
 */
public interface CallInformationRequest extends CircuitSwitchedCallMessage {

	LegType getLegID();
	
    List<RequestedInformationType> getRequestedInformationTypeList();

    DigitsIsup getCorrelationID();
    
    CAPINAPExtensions getExtensions();
}