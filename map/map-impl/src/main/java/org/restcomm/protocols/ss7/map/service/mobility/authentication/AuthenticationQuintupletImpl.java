/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AuthenticationQuintupletImpl implements AuthenticationQuintuplet {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ASNOctetString2 rand;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
	private ASNOctetString2 xres;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2)
	private ASNOctetString2 ck;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=3)
	private ASNOctetString2 ik;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=4)
	private ASNOctetString2 autn;

    public AuthenticationQuintupletImpl() {
    }

    public AuthenticationQuintupletImpl(ByteBuf rand, ByteBuf xres, ByteBuf ck, ByteBuf ik, ByteBuf autn) {
    	
    	if(rand!=null)
    		this.rand = new ASNOctetString2(rand);
    	
    	if(xres!=null)
    		this.xres = new ASNOctetString2(xres);
    	
    	if(ck!=null)
    		this.ck = new ASNOctetString2(ck);
    	
    	if(ik!=null)
    		this.ik = new ASNOctetString2(ik);
    	
    	if(autn!=null)
    		this.autn = new ASNOctetString2(autn);    	
    }

    public ByteBuf getRand() {
    	if(this.rand==null)
    		return null;
    	
    	return this.rand.getValue();    	
    }

    public ByteBuf getXres() {
    	if(this.xres==null)
    		return null;
    	
    	return this.xres.getValue();    	
    }

    public ByteBuf getCk() {
    	if(this.ck==null)
    		return null;
    	
    	return this.ck.getValue();    	
    }

    public ByteBuf getIk() {
    	if(this.ik==null)
    		return null;
    	
    	return this.ik.getValue();    	
    }

    public ByteBuf getAutn() {
    	if(this.autn==null)
    		return null;
    	
    	return this.autn.getValue();    	
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthenticationQuintuplet [");

        if (this.rand != null) {
            sb.append("rand=[");
            sb.append(ASNOctetString.printDataArr(getRand()));
            sb.append("], ");
        }
        if (this.xres != null) {
            sb.append("xres=[");
            sb.append(ASNOctetString.printDataArr(getXres()));
            sb.append("], ");
        }
        if (this.ck != null) {
            sb.append("ck=[");
            sb.append(ASNOctetString.printDataArr(getCk()));
            sb.append("], ");
        }
        if (this.ik != null) {
            sb.append("ik=[");
            sb.append(ASNOctetString.printDataArr(getIk()));
            sb.append("], ");
        }
        if (this.autn != null) {
            sb.append("autn=[");
            sb.append(ASNOctetString.printDataArr(getAutn()));
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}