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

package org.restcomm.protocols.ss7.inap;

import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPMessage;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

/**
 *
 * @author yulian.oifa
 *
 */
public abstract class MessageImpl implements INAPMessage {
	private static final long serialVersionUID = 1L;

	private int invokeId;
    private INAPDialog inapDialog;
    private ByteBuf originalBuffer;

    public int getInvokeId() {
        return this.invokeId;
    }

    public INAPDialog getINAPDialog() {
        return this.inapDialog;
    }

    public void setInvokeId(int invokeId) {
        this.invokeId = invokeId;
    }

    public void setINAPDialog(INAPDialog inapDialog) {
        this.inapDialog = inapDialog;
    }

    protected void addInvokeIdInfo(StringBuilder sb) {
        sb.append("InvokeId=");
        sb.append(this.invokeId);
    }

    public void setOriginalBuffer(ByteBuf buffer) {
    	this.originalBuffer=buffer;
    }
    
    public ByteBuf getOriginalBuffer() {
    	return this.originalBuffer;
    }    
    
    public void retain() {
    	if(originalBuffer!=null)
    		ReferenceCountUtil.retain(originalBuffer);
    }
    
    public void release() {
    	if(originalBuffer!=null && originalBuffer.refCnt()>0)
    		ReferenceCountUtil.release(originalBuffer, originalBuffer.refCnt());
    }
}