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

package org.restcomm.protocols.ss7.cap.service.gprs;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSResponse;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class EntityReleasedGPRSResponseImpl extends GprsMessageImpl implements EntityReleasedGPRSResponse {
	private static final long serialVersionUID = 1L;

	@Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.entityReleasedGPRS_Response;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.entityReleasedGPRS;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EntityReleasedGPRSResponse [");
        this.addInvokeIdInfo(sb);

        sb.append("]");

        return sb.toString();
    }
}
