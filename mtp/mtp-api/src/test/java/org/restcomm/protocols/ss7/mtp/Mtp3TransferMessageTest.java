/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.mtp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class Mtp3TransferMessageTest {

    private ByteBuf getMsg() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 0x83, (byte) 232, 3, (byte) 244, (byte) 161, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
    }

    private ByteBuf getAnsiMsg(boolean is8Bit) {
        if(is8Bit)
            return Unpooled.wrappedBuffer(new byte[] { (byte) 0x83, (byte) 232, 3, 0,  (byte) 208, 7, 0, 33, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        else
            return Unpooled.wrappedBuffer(new byte[] { (byte) 0x83, (byte) 232, 3, 0,  (byte) 208, 7, 0, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
    }

    private ByteBuf getData() {
        return Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
    }

    // si = 3 (SCCP)
    // ni = 2
    // mp = 0
    // sio = (si + (ni << 6) + (mt << 4))
    // dpc = 1000
    // opc = 2000
    // sls = 10

    @Test
    public void testItuDecode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ITU);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(getMsg(),new AtomicBoolean(false));

        assertEquals(msg.getSi(), 3);
        assertEquals(msg.getNi(), 2);
        assertEquals(msg.getMp(), 0);
        assertEquals(msg.getDpc(), 1000);
        assertEquals(msg.getOpc(), 2000);
        assertEquals(msg.getSls(), 10);
        
        ByteBuf expectedBuf=this.getData();
        byte[] expected=new byte[expectedBuf.readableBytes()];
        expectedBuf.readBytes(expected);
        
        byte[] real=new byte[msg.getData().readableBytes()];
        msg.getData().readBytes(real);
        assertTrue(Arrays.equals(expected, real));

    }

    @Test
    public void testAnsiSls8BitDecode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ANSI_Sls8Bit);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(getAnsiMsg(true),new AtomicBoolean(false));

        assertEquals(msg.getSi(), 3);
        assertEquals(msg.getNi(), 2);
        assertEquals(msg.getMp(), 0);
        assertEquals(msg.getDpc(), 1000);
        assertEquals(msg.getOpc(), 2000);
        assertEquals(msg.getSls(), 33);
        
        ByteBuf expectedBuf=this.getData();
        byte[] expected=new byte[expectedBuf.readableBytes()];
        expectedBuf.readBytes(expected);
        
        byte[] real=new byte[msg.getData().readableBytes()];
        msg.getData().readBytes(real);
        assertTrue(Arrays.equals(expected, real));

    }

    @Test
    public void testAnsiSls5BitDecode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ANSI_Sls5Bit);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(getAnsiMsg(false),new AtomicBoolean(false));

        assertEquals(msg.getSi(), 3);
        assertEquals(msg.getNi(), 2);
        assertEquals(msg.getMp(), 0);
        assertEquals(msg.getDpc(), 1000);
        assertEquals(msg.getOpc(), 2000);
        assertEquals(msg.getSls(), 10);
        
        ByteBuf expectedBuf=this.getData();
        byte[] expected=new byte[expectedBuf.readableBytes()];
        expectedBuf.readBytes(expected);
        
        byte[] real=new byte[msg.getData().readableBytes()];
        msg.getData().readBytes(real);
        assertTrue(Arrays.equals(expected, real));
    }

    @Test
    public void testItuEncode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ITU);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(3, 2, 0, 2000, 1000, 10, this.getData());

        ByteBuf expectedBuf=this.getMsg();
        byte[] expected=new byte[expectedBuf.readableBytes()];
        expectedBuf.readBytes(expected);
        
        ByteBuf res = msg.encodeMtp3();
        byte[] real=new byte[res.readableBytes()];
        res.readBytes(real);
        assertTrue(Arrays.equals(expected, real));

    }
    
    @Test
    public void testAnsiSls8BitEncode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ANSI_Sls8Bit);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(3, 2, 0, 2000, 1000, 33, this.getData());

        ByteBuf expectedBuf=this.getAnsiMsg(true);
        byte[] expected=new byte[expectedBuf.readableBytes()];
        expectedBuf.readBytes(expected);
        
        ByteBuf res = msg.encodeMtp3();
        byte[] real=new byte[res.readableBytes()];
        res.readBytes(real);
        assertTrue(Arrays.equals(expected, real));
    }
  
    @Test
    public void testAnsiSls5BitEncode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ANSI_Sls5Bit);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(3, 2, 0, 2000, 1000, 10, this.getData());

        ByteBuf expectedBuf=this.getAnsiMsg(false);
        byte[] expected=new byte[expectedBuf.readableBytes()];
        expectedBuf.readBytes(expected);
        
        ByteBuf res = msg.encodeMtp3();
        byte[] real=new byte[res.readableBytes()];
        res.readBytes(real);
        assertTrue(Arrays.equals(expected, real));
  }
}