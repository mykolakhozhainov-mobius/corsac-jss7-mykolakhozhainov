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

package org.restcomm.protocols.ss7.map.api.service.mobility.handover;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V2-3:
 *
 * MAP V3: prepareSubsequentHandover OPERATION ::= { --Timer m ARGUMENT PrepareSubsequentHO-Arg RESULT PrepareSubsequentHO-Res
 * ERRORS { unexpectedDataValue | dataMissing | unknownMSC | subsequentHandoverFailure} CODE local:69 }
 *
 * MAP V2: PrepareSubsequentHandover ::= OPERATION --Timer m ARGUMENT prepareSubsequentHO-Arg PrepareSubsequentHO-Arg RESULT
 * bss-APDU ExternalSignalInfo 191 ERRORS { UnexpectedDataValue, DataMissing, UnknownMSC, SubsequentHandoverFailure}
 *
 * MAP V3: PrepareSubsequentHO-Arg ::= [3] SEQUENCE { targetCellId [0] GlobalCellId OPTIONAL, targetMSC-Number [1]
 * ISDN-AddressStringImpl, targetRNCId [2] RNCId OPTIONAL, an-APDU [3] AccessNetworkSignalInfo OPTIONAL, selectedRab-Id [4] RAB-Id
 * OPTIONAL, extensionContainer [5] ExtensionContainer OPTIONAL, ..., geran-classmark [6] GERAN-Classmark OPTIONAL,
 * rab-ConfigurationIndicator [7] NULL OPTIONAL }
 *
 * MAP V2: PrepareSubsequentHO-Arg ::= SEQUENCE { targetCellId GlobalCellId, targetMSC-Number ISDN-AddressStringImpl, bss-APDU
 * ExternalSignalInfo, ...}
 *
 * RAB-Id ::= INTEGER (1..255)
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface PrepareSubsequentHandoverRequest extends MobilityMessage {

    GlobalCellId getTargetCellId();

    ISDNAddressString getTargetMSCNumber();

    RNCId getTargetRNCId();

    AccessNetworkSignalInfo getAnAPDU();

    Integer getSelectedRabId();

    MAPExtensionContainer getExtensionContainer();

    GERANClassmark getGERANClassmark();

    boolean getRabConfigurationIndicator();

    // MAP V2
    ExternalSignalInfo getBssAPDU();

}
