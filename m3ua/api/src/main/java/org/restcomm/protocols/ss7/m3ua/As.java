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
package org.restcomm.protocols.ss7.m3ua;

import java.util.Collection;

import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * <p>
 * Application Server (AS) is a logical entity serving a specific Routing Key. An example of an Application Server is a virtual
 * switch element handling all call processing for a signalling relation, identified by an SS7 DPC/OPC. Another example is a
 * virtual database element, handling all HLR transactions for a particular SS7 SIO/DPC/OPC combination. The AS contains a set
 * of one or more unique Application Server Processes, of which one or more is normally actively processing traffic
 * </p>
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */

public interface As {

    /**
     * Each As in M3UA stack is uniquely identified by its name
     *
     * @return name of this As
     */
    String getName();

    /**
     * Returns true if atleast one {@link Asp} in this As is ACTIVE and exchanging payload
     *
     * @return
     */
    boolean isConnected();

    /**
     * Returns true if state of this As is ACTIVE
     *
     * @return
     */
    boolean isUp();

    /**
     * {@link RoutingContext} associated with this As. Configuring routing context is optional and can return null if not
     * configured. Note that there is a 1:1 relationship between an AS and a Routing Key
     *
     * @return Routing Key
     */
    RoutingContext getRoutingContext();

    /**
     * The {@link Functionality} of this As
     *
     * @return
     */
    Functionality getFunctionality();

    /**
     * {@link ExchangeType} for this As
     *
     * @return
     */
    ExchangeType getExchangeType();

    /**
     * {@link IPSPType} for this As. This is useful only if {@link Functionality} for this As is IPSP
     *
     * @return
     */
    IPSPType getIpspType();

    /**
     * {@link NetworkAppearance} for this As
     *
     * @return
     */
    NetworkAppearance getNetworkAppearance();

    /**
     * {@link TrafficModeType} for this As
     *
     * @return
     */
    TrafficModeType getTrafficModeType();

    /**
     * Default {@link TrafficModeType} that this As will assume if TrafficModeType is not negotiated or not set when defining
     * this As
     *
     * @return
     */
    TrafficModeType getDefaultTrafficModeType();

    /**
     * The minimu number of {@link Asp} that should be ACTIVE before this As becomes ACTIVE. This value is useful only if
     * {@link TrafficModeType} is loadshare
     *
     * @return
     */
    int getMinAspActiveForLb();

    /**
     * Number of {@link Asp} assigned to this As
     *
     * @return
     */
    Collection<Asp> getAspList();

    /**
     * Current state of this As
     *
     * @return
     */
    State getState();

}
