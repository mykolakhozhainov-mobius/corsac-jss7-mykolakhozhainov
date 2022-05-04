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

package org.restcomm.protocols.ss7.map.api.service.mobility.handover;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V1:
 *
 * PerformSubsequentHandover ::= OPERATION --Timer m ARGUMENT performSubsequentHO-Arg PerformSubsequentHO-Arg RESULT
 * accessSignalInfo ExternalSignalInfo ERRORS { UnexpectedDataValue, UnknownBaseStation, UnknownMSC, InvalidTargetBaseStation,
 * SubsequentHandoverFailure}
 *
 * PerformSubsequentHO-Arg ::= SEQUENCE { targetCellId GlobalCellId, servingCellId GlobalCellId, targetMSC-Number
 * ISDN-AddressStringImpl, classmarkInfo [10] ClassmarkInfo OPTIONAL}
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface PerformSubsequentHandoverRequest extends MobilityMessage {

    GlobalCellId getTargetCellId();

    GlobalCellId getServingCellId();

    ISDNAddressString getTargetMSCNumber();

    ClassmarkInfo getClassmarkInfo();
}