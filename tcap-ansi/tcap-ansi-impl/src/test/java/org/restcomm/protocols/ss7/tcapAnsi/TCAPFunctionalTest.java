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

package org.restcomm.protocols.ss7.tcapAnsi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCListener;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Return;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.Client;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.EventType;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.Server;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.TestEvent;

/**
 * Test for call flow.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public class TCAPFunctionalTest extends SccpHarness {
	public static final long WAIT_TIME = 500;
	public static final long[] _ACN_ = new long[] { 0, 4, 0, 0, 1, 0, 19, 2 };
	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;
	private TCAPListenerWrapper tcapListenerWrapper;

	@Before
	public void beforeEach() throws Exception {
		super.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		super.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		tcapStack1 = new TCAPStackImpl("TCAPFunctionalTest_1", this.sccpProvider1, 8, workerPool);
		tcapStack2 = new TCAPStackImpl("TCAPFunctionalTest_2", this.sccpProvider2, 8, workerPool);

		tcapListenerWrapper = new TCAPListenerWrapper();
		tcapStack1.getProvider().addTCListener(tcapListenerWrapper);

		tcapStack1.start();
		tcapStack2.start();

		tcapStack1.setInvokeTimeout(0);
		tcapStack2.setInvokeTimeout(0);

		// create test classes
		client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);
		server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address);
	}

	@After
	public void afterEach() {
		if (tcapStack1 != null) {
			tcapStack1.stop();
			tcapStack1 = null;
		}

		if (tcapStack2 != null) {
			tcapStack2.stop();
			tcapStack2 = null;
		}

		super.tearDown();
	}

	@Test
	public void simpleTCWithDialogTest() throws Exception {
		long stamp = System.currentTimeMillis();

		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, 2, stamp + WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		// te = TestEvent.createReceivedEvent(EventType.DialogRelease, null,
		// 3,stamp+WAIT_TIME*2+_WAIT_REMOVE);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, 2, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		// te = TestEvent.createReceivedEvent(EventType.DialogRelease, null,
		// 3,stamp+WAIT_TIME*2+_WAIT_REMOVE);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);

		// ..............................
//        this.saveTrafficInFile();
		// ..............................

		client.startClientDialog();
		assertNotNull(client.dialog.getLocalAddress());
		assertNull(client.dialog.getRemoteDialogId());

		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue(false);
		assertNotNull(server.dialog.getLocalAddress());
		assertNotNull(server.dialog.getRemoteDialogId());

		client.awaitReceived(EventType.Continue);
		server.awaitSent(EventType.Continue);

		client.dialog.sendComponent(client.createNewInvoke());
		client.sendEnd(false);
		assertNotNull(client.dialog.getLocalAddress());
		assertNotNull(client.dialog.getRemoteDialogId());

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void uniMsgTest() throws Exception {
		long stamp = System.currentTimeMillis();

		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Uni, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Uni, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		serverExpectedEvents.add(te);

		client.startUniDialog();
		client.sendUni();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	private class TCAPListenerWrapper implements TCListener {
		@Override
		public void onTCUni(TCUniIndication ind) {
		}

		@Override
		public void onTCQuery(TCQueryIndication ind) {
		}

		@Override
		public void onTCConversation(TCConversationIndication ind) {
			assertEquals(ind.getComponents().getComponents().size(), 2);
			Return rrl = (Return) ind.getComponents().getComponents().get(0);
			Invoke inv = (InvokeImpl) ind.getComponents().getComponents().get(1);

			// operationCode is not sent via ReturnResultLast because it does not contain a
			// Parameter
			// so operationCode is taken from a sent Invoke
			assertEquals((long) rrl.getCorrelationId(), 0);
			assertEquals(rrl.getOperationCode().getPrivateOperationCode(), new Integer(12));

			// second Invoke has its own operationCode and it has linkedId to the second
			// sent Invoke
			assertEquals((long) inv.getInvokeId(), 0);
			assertEquals(inv.getOperationCode().getPrivateOperationCode(), new Integer(14));
			assertEquals((long) inv.getCorrelationId(), 1);

			// we should see operationCode of the second sent Invoke
			Invoke linkedInv = inv.getCorrelationInvoke();
			assertEquals(linkedInv.getOperationCode().getPrivateOperationCode(), new Integer(13));
		}

		@Override
		public void onTCResponse(TCResponseIndication ind) {
		}

		@Override
		public void onTCUserAbort(TCUserAbortIndication ind) {
		}

		@Override
		public void onTCPAbort(TCPAbortIndication ind) {
		}

		@Override
		public void onTCNotice(TCNoticeIndication ind) {
		}

		@Override
		public void onDialogReleased(Dialog d) {
		}

		@Override
		public void onInvokeTimeout(Invoke tcInvokeRequest) {
		}

		@Override
		public void onDialogTimeout(Dialog d) {
		}
	}
}
