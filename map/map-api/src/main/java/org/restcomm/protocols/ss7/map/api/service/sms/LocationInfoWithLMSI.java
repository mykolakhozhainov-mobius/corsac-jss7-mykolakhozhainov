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

package org.restcomm.protocols.ss7.map.api.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
LocationInfoWithLMSI ::= SEQUENCE {
  networkNode-Number    [1] ISDN-AddressString,
  lmsi                  LMSI OPTIONAL,
  extensionContainer    ExtensionContainer OPTIONAL,
  ...,
  gprsNodeIndicator     [5] NULL OPTIONAL,
  -- gprsNodeIndicator is set only if the SGSN number is sent as the
  -- Network Node Number
  additional-Number     [6] Additional-Number OPTIONAL
  -- NetworkNode-number can be either msc-number or sgsn-number
Additional-Number ::= CHOICE {
    msc-Number          [0] ISDN-AddressString,
    sgsn-Number         [1] ISDN-AddressString
}
-- additional-number can be either msc-number or sgsn-number
-- if received networkNode-number is msc-number then the
-- additional number is sgsn-number
-- if received networkNode-number is sgsn-number then the
-- additional number is msc-number
}
</code>
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,lengthIndefinite=false)
public interface LocationInfoWithLMSI {

    ISDNAddressString getNetworkNodeNumber();

    LMSI getLMSI();

    MAPExtensionContainer getExtensionContainer();

    boolean getGprsNodeIndicator();

    AdditionalNumber getAdditionalNumber();

}