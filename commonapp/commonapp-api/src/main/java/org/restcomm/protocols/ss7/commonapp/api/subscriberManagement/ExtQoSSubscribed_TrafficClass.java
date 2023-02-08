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

package org.restcomm.protocols.ss7.commonapp.api.subscriberManagement;

/**
*
<code>
Traffic class, octet 6 (see 3GPP TS 23.107 [81])
Bits
8 7 6
In MS to network direction:
0 0 0       Subscribed traffic class
In network to MS direction:
0 0 0       Reserved
In MS to network direction and in network to MS direction:
0 0 1       Conversational class
0 1 0       Streaming class
0 1 1       Interactive class
1 0 0       Background class
1 1 1       Reserved
The network shall map all other values not explicitly defined onto one of the values defined in this version of the protocol.
The network shall return a negotiated value which is explicitly defined in this version of this protocol.
</code>
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public enum ExtQoSSubscribed_TrafficClass {
    subscribedTrafficClass_Reserved(0), conversationalClass(1), streamingClass(2), interactiveClass(3), backgroundClass(4), reserved(7);

    private int code;

    private ExtQoSSubscribed_TrafficClass(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ExtQoSSubscribed_TrafficClass getInstance(int code) {
        switch (code) {
        case 0:
            return ExtQoSSubscribed_TrafficClass.subscribedTrafficClass_Reserved;
        case 1:
            return ExtQoSSubscribed_TrafficClass.conversationalClass;
        case 2:
            return ExtQoSSubscribed_TrafficClass.streamingClass;
        case 3:
            return ExtQoSSubscribed_TrafficClass.interactiveClass;
        case 4:
            return ExtQoSSubscribed_TrafficClass.backgroundClass;
        default:
            return ExtQoSSubscribed_TrafficClass.reserved;
        }
    }

}
