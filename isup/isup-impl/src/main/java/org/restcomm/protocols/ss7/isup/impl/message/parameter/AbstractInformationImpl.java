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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.Information;
import org.restcomm.protocols.ss7.isup.message.parameter.InformationType;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public abstract class AbstractInformationImpl implements Information {
	protected final InformationType type;
    protected int tag;
    public AbstractInformationImpl(InformationType type) {
        super();
        this.type = type;
    }

    public InformationType getType(){
        return type;
    }
    public int getTag(){
        return tag;
    }

    void setTag(int tag){
        this.tag = tag;
    }

    abstract void encode(ByteBuf b) throws ParameterException;

    abstract void decode(ByteBuf b) throws ParameterException;
    
    public abstract int getLength();
}
