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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * CamelInfo ::= SEQUENCE { supportedCamelPhases SupportedCamelPhases, suppress-T-CSI NULL OPTIONAL, extensionContainer
 * ExtensionContainer OPTIONAL, ... , offeredCamel4CSIs [0] OfferedCamel4CSIs OPTIONAL }
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,lengthIndefinite=false)
public interface CamelInfo {
	SupportedCamelPhases getSupportedCamelPhases();

    boolean getSuppressTCSI();

    MAPExtensionContainer getExtensionContainer();

    OfferedCamel4CSIs getOfferedCamel4CSIs();
}