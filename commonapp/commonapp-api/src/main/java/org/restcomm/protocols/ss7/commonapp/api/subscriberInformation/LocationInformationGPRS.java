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

package org.restcomm.protocols.ss7.commonapp.api.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.LSAIdentity;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
<code>
LocationInformationGPRS ::= SEQUENCE {
  cellGlobalIdOrServiceAreaIdOrLAI    [0] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
  routeingAreaIdentity                [1] RAIdentity OPTIONAL,
  geographicalInformation             [2] GeographicalInformation OPTIONAL,
  sgsn-Number                         [3] ISDN-AddressString OPTIONAL,
  selectedLSAIdentity                 [4] LSAIdentity OPTIONAL,
  extensionContainer                  [5] ExtensionContainer OPTIONAL,
  ...,
  sai-Present                         [6] NULL OPTIONAL,
  geodeticInformation                 [7] GeodeticInformation OPTIONAL,
  currentLocationRetrieved            [8] NULL OPTIONAL,
  ageOfLocationInformation            [9] AgeOfLocationInformation OPTIONAL
}
-- sai-Present indicates that the cellGlobalIdOrServiceAreaIdOrLAI parameter contains
-- a Service Area Identity.
-- currentLocationRetrieved shall be present if the location information
-- was retrieved after successful paging.
</code>
 *
 *
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface LocationInformationGPRS {

    CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI();

    RAIdentity getRouteingAreaIdentity();

    GeographicalInformation getGeographicalInformation();

    ISDNAddressString getSGSNNumber();

    LSAIdentity getLSAIdentity();

    MAPExtensionContainer getExtensionContainer();

    boolean isSaiPresent();

    GeodeticInformation getGeodeticInformation();

    boolean isCurrentLocationRetrieved();

    Integer getAgeOfLocationInformation();

}