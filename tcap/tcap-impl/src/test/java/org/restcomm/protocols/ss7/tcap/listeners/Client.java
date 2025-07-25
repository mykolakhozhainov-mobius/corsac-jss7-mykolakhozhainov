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

package org.restcomm.protocols.ss7.tcap.listeners;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.DialogImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.listeners.events.EventType;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.Unpooled;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class Client extends TCAPTestHarness {
	private static Logger logger = LogManager.getLogger(Client.class);

	public Client(TCAPStack stack, ParameterFactory paramFactory, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super(stack, paramFactory, thisAddress, remoteAddress, logger);

		super.listenerName = "Client";
	}

	@Override
	public void sendBegin() throws TCAPException, TCAPSendException {
		// create some INVOKE
		OperationCode oc = TcapFactory.createLocalOperationCode(12);
		// no parameter
		this.dialog.sendData(null, null, InvokeClass.Class1, null, oc, null, true, false);

		// create a second INVOKE for which we will test linkedId
		oc = TcapFactory.createLocalOperationCode(13);
		// no parameter
		this.dialog.sendData(null, null, InvokeClass.Class1, null, oc, null, true, false);

		super.sendBegin();
	}

	public void sendBeginUnreachableAddress(boolean returnMessageOnError, TaskCallback<Exception> callback)
			throws TCAPException, TCAPSendException {
		ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
		// UI is optional!
		TCBeginRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createBegin(this.dialog);
		tcbr.setApplicationContextName(acn);

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

	public Invoke createNewInvoke() {
		Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
		invoke.setInvokeId(12);

		invoke.setOperationCode(59);

		ASNOctetString p1 = new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 0x0F }), null, null, null, false);
		ASNOctetString p2 = new ASNOctetString(
				Unpooled.wrappedBuffer(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a,
						(byte) 0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 }),
				null, null, null, false);

		CompoundParameter c1 = new CompoundParameter();
		c1.setO1(Arrays.asList(new ASNOctetString[] { p1, p2 }));
		invoke.setParameter(c1);

		return invoke;
	}

	@ASNTag(asnClass = ASNClass.UNIVERSAL, tag = 0x10, constructed = true, lengthIndefinite = false)
	private class CompoundParameter {
		List<ASNOctetString> o1;

		public void setO1(List<ASNOctetString> o1) {
			this.o1 = o1;
		}

		@SuppressWarnings("unused")
		public List<ASNOctetString> getO1() {
			return this.o1;
		}
	}
}
