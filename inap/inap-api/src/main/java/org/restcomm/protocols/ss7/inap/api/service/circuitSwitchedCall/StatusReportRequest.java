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

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ReportCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceStatus;

/**
 *
<code>
<p>
StatusReport ::= OPERATION
ARGUMENT StatusReportArg
-- Direction: SSF –> SCF, Timer: Tsrp
-- This operation is used as a response to RequestFirstStatusMatchReport or
-- RequestEveryStatusChangeReport operations.
-- Direction: SCF –> SSF, Timer: Trfs
-- This operation is used to request the SSF to report the first change busy/idle to the specified status of
-- a physical termination resource.

StatusReportArg ::= SEQUENCE {
	resourceStatus [0] ResourceStatus OPTIONAL,
	correlationID [1] CorrelationID OPTIONAL,
	resourceID [2] ResourceID OPTIONAL,
	extensions [3] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	reportCondition [4] ReportCondition OPTIONAL
-- ...
}
-- For correlationID, OPTIONAL denotes network operator optional.
-- resourceID is required when the SSF sends a report as an answer to a previous request when the
-- correlationID was present.
</code>
 *
 * @author yulian.oifa
 *
 */
public interface StatusReportRequest extends CircuitSwitchedCallMessage {
	ResourceStatus getResourceStatus();
	
	DigitsIsup getCorrelationID();
	
	ResourceID getResourceID();
	
	CAPINAPExtensions getExtensions();	
	
	ReportCondition getReportCondition();
}