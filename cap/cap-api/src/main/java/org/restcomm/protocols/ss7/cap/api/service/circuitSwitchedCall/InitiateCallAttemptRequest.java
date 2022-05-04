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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;

/**
 *
<code>
initiateCallAttempt {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT InitiateCallAttemptArg {bound}
  RESULT InitiateCallAttemptRes {bound}
  ERRORS {
    missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter
  }
  CODE opcode-initiateCallAttempt}
-- Direction: gsmSCF -> gsmSSF, Timer T ica
-- This operation is used to instruct the gsmSSF to create a new call to a call party using the
-- address information provided by the gsmSCF.

InitiateCallAttemptArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  destinationRoutingAddress  [0] DestinationRoutingAddress {bound},
  extensions                 [4] Extensions {bound} OPTIONAL,
  legToBeCreated             [5] LegID OPTIONAL,
  newCallSegment             [6] CallSegmentID {bound} OPTIONAL,
  callingPartyNumber         [30] CallingPartyNumber {bound} OPTIONAL,
  callReferenceNumber        [51] CallReferenceNumber OPTIONAL,
  gsmSCFAddress              [52] ISDN-AddressString OPTIONAL,
  suppress-T-CSI             [53] NULL OPTIONAL,
  ...
}

CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..127)
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface InitiateCallAttemptRequest extends CircuitSwitchedCallMessage {

    DestinationRoutingAddress getDestinationRoutingAddress();

    CAPINAPExtensions getExtensions();

    LegID getLegToBeCreated();

    Integer getNewCallSegment();

    CallingPartyNumberIsup getCallingPartyNumber();

    CallReferenceNumber getCallReferenceNumber();

    ISDNAddressString getGsmSCFAddress();

    boolean getSuppressTCsi();

}