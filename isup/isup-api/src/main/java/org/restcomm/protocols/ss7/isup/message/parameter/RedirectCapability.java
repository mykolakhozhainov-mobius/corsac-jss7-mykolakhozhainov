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

package org.restcomm.protocols.ss7.isup.message.parameter;

import io.netty.buffer.ByteBuf;

/**
 * Start time:13:54:36 2009-07-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public interface RedirectCapability extends ISUPParameter {

    int _PARAMETER_CODE = 0x4E;

    /**
     * See Q.763 3.96 Redirect possible indicator : not used
     */
    int _RPI_NOT_USED = 0;
    /**
     * See Q.763 3.96 Redirect possible indicator : redirect possible before ACM use)
     */
    int _RPI_RPB_ACM = 1;
    /**
     * See Q.763 3.96 Redirect possible indicator : redirect possible before ANM
     */
    int _RPI_RPB_ANM = 2;
    /**
     * See Q.763 3.96 Redirect possible indicator : redirect possible at any time during the call
     */
    int _RPI_RPANTDC = 3;

    ByteBuf getCapabilities();

    void setCapabilities(ByteBuf capabilities);

    int getCapability(byte b);
}
