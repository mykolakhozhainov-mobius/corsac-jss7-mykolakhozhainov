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

package org.restcomm.protocols.ss7.tcap.asn.comp;

import org.restcomm.protocols.ss7.tcap.asn.ParseException;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public enum GeneralProblemType {

    /**
     * The component type is not recognized as being one of those defined in 3.1. (Invoke, ReturnResult, ReturnResultLast,
     * ReturnError, Reject) This code is generated by the TCAP layer.
     */
    UnrecognizedComponent(0),

    /**
     * The elemental structure of a component does not conform to the structure of that component as defined in 3.1/Q.773. This
     * code is generated by the TCAP layer.
     */
    MistypedComponent(1),

    /**
     * The contents of the component do not conform to the encoding rules defined in 4.1/Q.773. This code is generated by the
     * TCAP layer.
     */
    BadlyStructuredComponent(2);

    private int type = -1;

    GeneralProblemType(int l) {
        this.type = l;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    public static GeneralProblemType getFromInt(int t) throws ParseException {
        if (t == 0) {
            return UnrecognizedComponent;
        } else if (t == 1) {
            return MistypedComponent;
        } else if (t == 2) {
            return BadlyStructuredComponent;
        }

        throw new ParseException(null, GeneralProblemType.MistypedComponent, "Wrong value of type: " + t);
    }
}
