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

package org.restcomm.protocols.ss7.map.api;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MAPParsingComponentException extends Exception {
	private static final long serialVersionUID = 1L;

	private MAPParsingComponentExceptionReason reason;

    public MAPParsingComponentException() {
        // TODO Auto-generated constructor stub
    }

    public MAPParsingComponentException(String message, MAPParsingComponentExceptionReason reason) {
        super(message);

        this.reason = reason;
    }

    public MAPParsingComponentException(Throwable cause, MAPParsingComponentExceptionReason reason) {
        super(cause);

        this.reason = reason;
    }

    public MAPParsingComponentException(String message, Throwable cause, MAPParsingComponentExceptionReason reason) {
        super(message, cause);

        this.reason = reason;
    }

    public MAPParsingComponentExceptionReason getReason() {
        return this.reason;
    }
}
