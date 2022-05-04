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

package org.restcomm.protocols.ss7.sccp.impl;

import org.restcomm.protocols.ss7.sccp.ConcernedSignalingPointCode;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author yulianoifa
 *
 */
public class ConcernedSignalingPointCodeImpl implements ConcernedSignalingPointCode {
    private int remoteSpc;

    public ConcernedSignalingPointCodeImpl() {
    }

    public ConcernedSignalingPointCodeImpl(int remoteSpc) {
        this.remoteSpc = remoteSpc;
    }

    public int getRemoteSpc() {
        return remoteSpc;
    }

    /**
     * @param remoteSpc the remoteSpc to set
     */
    protected void setRemoteSpc(int remoteSpc) {
        this.remoteSpc = remoteSpc;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("rsp=").append(this.remoteSpc);
        return sb.toString();
    }
}
