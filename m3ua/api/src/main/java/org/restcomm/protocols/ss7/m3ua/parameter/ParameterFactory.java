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

package org.restcomm.protocols.ss7.m3ua.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.m3ua.parameter.CongestedIndication.CongestionLevel;

/**
 * Constructs parameters.
 *
 * @author amit bhayani
 * @author kulikov
 * @author yulianoifa
 */
public interface ParameterFactory {
    /**
     * Constructs Protocol Data parameter.
     *
     * @param opc the origination point code
     * @param dpc the destination point code
     * @param si the service indicator
     * @param ni the network indicator
     * @param mp the message priority indicator
     * @param sls the signaling link selection
     * @param data message payload
     * @return Protocol data parameter
     */
    ProtocolData createProtocolData(int opc, int dpc, int si, int ni, int mp, int sls, ByteBuf data);

    ProtocolData createProtocolData(ByteBuf payloadData);

    NetworkAppearance createNetworkAppearance(long netApp);

    RoutingContext createRoutingContext(long[] routCntx);

    CorrelationId createCorrelationId(long corrId);

    AffectedPointCode createAffectedPointCode(int[] pc, short[] mask);

    DestinationPointCode createDestinationPointCode(int pc, short mask);

    InfoString createInfoString(String string);

    ConcernedDPC createConcernedDPC(int pointCode);

    CongestedIndication createCongestedIndication(CongestionLevel level);

    UserCause createUserCause(int user, int cause);

    ASPIdentifier createASPIdentifier(long aspId);

    LocalRKIdentifier createLocalRKIdentifier(long id);

    OPCList createOPCList(int[] pc, short[] mask);

    ServiceIndicators createServiceIndicators(short[] inds);

    TrafficModeType createTrafficModeType(int mode);

    RegistrationStatus createRegistrationStatus(int status);

    DiagnosticInfo createDiagnosticInfo(String info);

    RoutingKey createRoutingKey(LocalRKIdentifier localRkId, RoutingContext rc, TrafficModeType trafMdTy,
            NetworkAppearance netApp, DestinationPointCode[] dpc, ServiceIndicators[] servInds, OPCList[] opcList);

    RegistrationResult createRegistrationResult(LocalRKIdentifier localRkId, RegistrationStatus status, RoutingContext rc);

    DeregistrationStatus createDeregistrationStatus(int status);

    DeregistrationResult createDeregistrationResult(RoutingContext rc, DeregistrationStatus status);

    ErrorCode createErrorCode(int code);

    Status createStatus(int type, int info);

    HeartbeatData createHeartbeatData(ByteBuf data);

}
