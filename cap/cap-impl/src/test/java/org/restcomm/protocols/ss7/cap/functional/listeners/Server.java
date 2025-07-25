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

package org.restcomm.protocols.ss7.cap.functional.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParameterFactory;
import org.restcomm.protocols.ss7.cap.api.CAPProvider;
import org.restcomm.protocols.ss7.cap.api.CAPStack;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.RequestReportBCSMEventRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.RequestReportGPRSEventRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSEventImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class Server extends EventTestHarness {

	private static Logger logger = LogManager.getLogger(Server.class);

	public CAPStack capStack;
	public CAPProvider capProvider;

	protected CAPParameterFactory capParameterFactory;
	protected CAPErrorMessageFactory capErrorMessageFactory;
	protected ISUPParameterFactory isupParameterFactory;

	protected CAPDialog serverCscDialog;

	// private boolean _S_recievedDialogRequest;
	// private boolean _S_recievedInitialDp;
	//
	// private int dialogStep;
	// private long savedInvokeId;
	// private String unexpected = "";
	//
	// private FunctionalTestScenario step;

	public Server(CAPStack capStack, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super(logger);
		this.capStack = capStack;
		this.capProvider = this.capStack.getProvider();

		this.capParameterFactory = this.capProvider.getCAPParameterFactory();
		this.capErrorMessageFactory = this.capProvider.getCAPErrorMessageFactory();
		this.isupParameterFactory = this.capProvider.getISUPParameterFactory();

		this.capProvider.addCAPDialogListener(UUID.randomUUID(), this);
		this.capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);
		this.capProvider.getCAPServiceGprs().addCAPServiceListener(this);
		this.capProvider.getCAPServiceSms().addCAPServiceListener(this);

		this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
		this.capProvider.getCAPServiceGprs().acivate();
		this.capProvider.getCAPServiceSms().acivate();
	}

	public RequestReportBCSMEventRequest getRequestReportBCSMEventRequest() {

		List<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
		BCSMEvent ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.routeSelectFailure,
				MonitorMode.notifyAndContinue, null, null, false);
		bcsmEventList.add(ev);
		ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oCalledPartyBusy, MonitorMode.interrupted, null,
				null, false);
		bcsmEventList.add(ev);
		ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oNoAnswer, MonitorMode.interrupted, null, null,
				false);
		bcsmEventList.add(ev);
		ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oAnswer, MonitorMode.notifyAndContinue, null, null,
				false);
		bcsmEventList.add(ev);
		LegID legId = this.capParameterFactory.createLegID(null, LegType.leg1);
		ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oDisconnect, MonitorMode.notifyAndContinue, legId,
				null, false);
		bcsmEventList.add(ev);
		legId = this.capParameterFactory.createLegID(null, LegType.leg2);
		ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oDisconnect, MonitorMode.interrupted, legId, null,
				false);
		bcsmEventList.add(ev);
		ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oAbandon, MonitorMode.notifyAndContinue, null, null,
				false);
		bcsmEventList.add(ev);

		RequestReportBCSMEventRequestImpl res = new RequestReportBCSMEventRequestImpl(bcsmEventList, null);

		return res;
	}

	public RequestReportGPRSEventRequest getRequestReportGPRSEventRequest() {
		List<GPRSEvent> gprsEvent = new ArrayList<GPRSEvent>();
		GPRSEventImpl event = new GPRSEventImpl(GPRSEventType.attachChangeOfPosition, MonitorMode.notifyAndContinue);
		gprsEvent.add(event);
		PDPIDImpl pdpID = new PDPIDImpl(2);

		RequestReportGPRSEventRequest res = new RequestReportGPRSEventRequestImpl(gprsEvent, pdpID);
		return res;
	}

	@Override
	public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		super.onDialogRequest(capDialog, capGprsReferenceNumber);
		serverCscDialog = capDialog;
	}

	public void sendAccept() {
		try {
			serverCscDialog.send(new TaskCallback<Exception>() {
				@Override
				public void onSuccess() {
				}

				@Override
				public void onError(Exception exception) {
				}
			});
		} catch (CAPException e) {
			this.error("Error while trying to send/close() Dialog", e);
		}
	}

	public void debug(String message) {
		logger.debug(message);
	}

	public void error(String message, Exception e) {
		logger.error(message, e);
	}
}
