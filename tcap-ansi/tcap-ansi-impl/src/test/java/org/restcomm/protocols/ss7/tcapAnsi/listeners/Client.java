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

package org.restcomm.protocols.ss7.tcapAnsi.listeners;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.ClientTestASN;
import org.restcomm.protocols.ss7.tcapAnsi.DialogImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.Unpooled;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class Client extends EventTestHarness {
	private static Logger logger = LogManager.getLogger(Client.class);

	public Client(final TCAPStack stack, final ParameterFactory parameterFactory, final SccpAddress thisAddress,
			final SccpAddress remoteAddress) {
		super(stack, parameterFactory, thisAddress, remoteAddress, logger);
	}

	@Override
	public void sendBegin() throws TCAPException, TCAPSendException {
		ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();

		// create some INVOKE
		Invoke invoke = cpFactory.createTCInvokeRequestNotLast(InvokeClass.Class1);
		invoke.setInvokeId(this.dialog.getNewInvokeId());
		OperationCode oc = TcapFactory.createPrivateOperationCode(12);
		invoke.setOperationCode(oc);
		// no parameter
		this.dialog.sendComponent(invoke);

		// create a second INVOKE for which we will test linkedId
		Invoke invokeLast = cpFactory.createTCInvokeRequestLast(InvokeClass.Class1);
		invokeLast.setInvokeId(this.dialog.getNewInvokeId());
		oc = TcapFactory.createPrivateOperationCode(13);
		invokeLast.setOperationCode(oc);
		// no parameter
		this.dialog.sendComponent(invokeLast);
		super.sendBegin();
	}

	public void sendBeginUnreachableAddress(boolean returnMessageOnError, TaskCallback<Exception> callback)
			throws TCAPException, TCAPSendException {
		ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
		// UI is optional!
		TCQueryRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createQuery(this.dialog, true);
		tcbr.setApplicationContext(acn);

		GlobalTitle gt = super.parameterFactory.createGlobalTitle("93702994006", 0, NumberingPlan.ISDN_TELEPHONY, null,
				NatureOfAddress.INTERNATIONAL);
		((DialogImpl) this.dialog).setRemoteAddress(
				super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 8));
		tcbr.setReturnMessageOnError(returnMessageOnError);

		this.dialog.send(tcbr, callback);
		super.handleSent(EventType.Begin, tcbr);
	}

	public void releaseDialog() {
		if (this.dialog != null) {
			this.dialog.release();
			this.dialog = null;
		}
	}

	public DialogImpl getCurDialog() {
		return (DialogImpl) this.dialog;
	}

	public Invoke createNewInvoke() {
		Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestNotLast();
		invoke.setInvokeId(12l);

		OperationCode oc = TcapFactory.createPrivateOperationCode(59);
		invoke.setOperationCode(oc);

		ASNOctetString p1 = new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 0x0F }), null, null, null, false);
		ASNOctetString p2 = new ASNOctetString(
				Unpooled.wrappedBuffer(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a,
						(byte) 0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 }),
				null, null, null, false);

		ClientTestASN pm = new ClientTestASN();
		pm.setO1(Arrays.asList(new ASNOctetString[] { p1, p2 }));
		invoke.setSeqParameter(pm);

		return invoke;
	}

	public void sendInvokeSet(Long[] lstInvokeId) throws Exception {
		for (Long invokeId : lstInvokeId) {
			Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestNotLast();
			invoke.setInvokeId(invokeId);
			OperationCode opCode = TcapFactory.createPrivateOperationCode(0);
			invoke.setOperationCode(opCode);
			this.dialog.sendComponent(invoke);
		}

		this.sendBegin();
	}
}
