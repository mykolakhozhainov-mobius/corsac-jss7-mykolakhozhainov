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

package org.restcomm.protocols.ss7.sccp.impl.message;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.restcomm.protocols.ss7.sccp.impl.User;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MessageReassemblyTest extends SccpHarness {
	private SccpAddress a1, a2;

	@Before
	public void setUpClass() throws Exception {
		this.sccpStack1Name = "MessageReassemblyTestStack1";
		this.sccpStack2Name = "MessageReassemblyTestStack2";
	}

	@Override
	protected void createStack1() {
		this.sccpStack1 = createStack(sccpStack1Name);
		this.sccpProvider1 = sccpStack1.getSccpProvider();
	}

	@Override
	protected void createStack2() {
		this.sccpStack2 = createStack(sccpStack2Name);
		this.sccpProvider2 = sccpStack2.getSccpProvider();
	}

	@Override
	protected SccpStackImpl createStack(String name) {
		SccpStackImpl stack = new SccpStackImplProxy(name);
		return stack;
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	@After
	public void tearDown() {
		super.tearDown();
	}

	public ByteBuf getDataXudt1() {
		return Unpooled.wrappedBuffer(new byte[] { 17, (byte) 129, 15, 4, 6, 10, 15, 2, 66, 8, 4, 67, 1, 0, 6, 5, 11,
				12, 13, 14, 15, 16, 4, (byte) 192, 100, 0, 0, 18, 1, 7, 0 });
	}

	@Test
	public void testReassembly() throws Exception {

		a1 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
		a2 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

		User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
		User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

		u1.register();
		u2.register();

		sccpStack1.setReassemblyTimerDelay(3000);

		// Receiving a chain of 3 XUDT segments -> success
		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);

		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm1());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm2());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
		assertEquals(u1.getMessages().size(), 0);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm3());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		assertEquals(u1.getMessages().size(), 1);
		SccpDataMessage dMsg = (SccpDataMessage) u1.getMessages().get(0);

		MessageSegmentationTest.assertByteBufs(dMsg.getData(), MessageSegmentationTest.getDataA());
		assertEquals(u2.getMessages().size(), 0);

		// Receiving a single XUDT message without segments -> success
		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(), getDataXudt1());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		assertEquals(u1.getMessages().size(), 2);
		dMsg = (SccpDataMessage) u1.getMessages().get(1);

		MessageSegmentationTest.assertByteBufs(dMsg.getData(), SccpDataMessageTest.getDataXudt1Src());
		assertEquals(u2.getMessages().size(), 0);

		// Receiving a chain of 3 XUDTS segments -> success
		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm1_S());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm2_S());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
		assertEquals(u1.getMessages().size(), 2);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm3_S());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		assertEquals(u1.getMessages().size(), 3);
		SccpNoticeMessage nMsg = (SccpNoticeMessage) u1.getMessages().get(2);

		MessageSegmentationTest.assertByteBufs(nMsg.getData(), MessageSegmentationTest.getDataA());
		assertEquals(u2.getMessages().size(), 0);

		// Receiving an only the first segment of 3--segmented chain of 3 XUDT segments
		// -> timeout
		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm1());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
		Thread.sleep(5000); // waiting for timeout - current timeout is 3 sec
		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		assertEquals(u2.getMessages().size(), 1);
		assertEquals(((SccpNoticeMessage) u2.getMessages().get(0)).getReturnCause().getValue(),
				ReturnCauseValue.CANNOT_REASEMBLE);

		// Receiving an only the second segment of 3--segmented chain of 3 XUDT segments
		// -> error
		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm2());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		assertEquals(u1.getMessages().size(), 3);
		assertEquals(u2.getMessages().size(), 1);

		// Receiving only th 1 and 3 message from a chain of 3 XUDTS segments -> error
		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm1_S());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm3_S());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		assertEquals(u1.getMessages().size(), 3);
		assertEquals(u2.getMessages().size(), 1); // no error for service messages

		// Receiving two chains of 3 XUDT and XUDTS segments -> success
		assertEquals(u1.getMessages().size(), 3);
		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm1());

		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm1_S());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 2);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm2());

		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm2_S());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 2);
		assertEquals(u1.getMessages().size(), 3);
		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm3());

		super.sendTransferMessageToLocalUser(super.mtp3UserPart1, getStack2PC(), getStack1PC(),
				MessageSegmentationTest.getDataSegm3_S());

		assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
		assertEquals(u1.getMessages().size(), 5);
		dMsg = (SccpDataMessage) u1.getMessages().get(3);
		nMsg = (SccpNoticeMessage) u1.getMessages().get(4);

		MessageSegmentationTest.assertByteBufs(dMsg.getData(), MessageSegmentationTest.getDataA());
		MessageSegmentationTest.assertByteBufs(nMsg.getData(), MessageSegmentationTest.getDataA());
		assertEquals(u2.getMessages().size(), 1);
	}
}
