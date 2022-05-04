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

package org.restcomm.protocols.ss7.commonapp.api.subscriberManagement;

/**
*
<code>
Traffic handling priority, octet 11 (see 3GPP TS 23.107 [81])
Bits
2 1
In MS to network direction:
0 0     Subscribed traffic handling priority
In network to MS direction:
0 0     Reserved
In MS to network direction and in network to MS direction:
0 1     Priority level 1
1 0     Priority level 2
1 1     Priority level 3
The Traffic handling priority value is ignored if the Traffic Class is Conversational class, Streaming class or Background class.
</code>
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public enum ExtQoSSubscribed_TrafficHandlingPriority {
    subscribedTrafficHandlingPriority_Reserved(0), priorityLevel_1(1), priorityLevel_2(2), priorityLevel_3(3);

    private int code;

    private ExtQoSSubscribed_TrafficHandlingPriority(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ExtQoSSubscribed_TrafficHandlingPriority getInstance(int code) {
        switch (code) {
        case 0:
            return ExtQoSSubscribed_TrafficHandlingPriority.subscribedTrafficHandlingPriority_Reserved;
        case 1:
            return ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_1;
        case 2:
            return ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_2;
        case 3:
            return ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3;
        default:
            return null;
        }
    }

}
