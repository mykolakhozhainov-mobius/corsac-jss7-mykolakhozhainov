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

import java.io.Serializable;

import io.netty.buffer.ByteBuf;

/**
 *
 lassmarkInfo ::= octet STRING (SIZE (1..2)) -- classmark information element type 1 or 2 contents as defined -- in TS GSM
 * 08.08 (phase 1)
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ClassmarkInfo extends Serializable {

	ByteBuf getValue();
}
