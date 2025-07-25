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

package org.restcomm.protocols.ss7.cap.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.cap.CAPStackImpl;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.cap.api.errors.UnavailableNetworkResource;
import org.restcomm.protocols.ss7.cap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CollectInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionWithArgumentRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswer;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CAPDialogGprs;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ConnectGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ContinueGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EventReportGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EventReportGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.FurnishChargingInformationGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ReleaseGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ResetTimerGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.SendChargingInformationGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPDialogSms;
import org.restcomm.protocols.ss7.cap.api.service.sms.ConnectSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ContinueSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.EventReportSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.FurnishChargingInformationSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.InitialDPSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ReleaseSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.RequestReportSMSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ResetTimerSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.RPCause;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.restcomm.protocols.ss7.cap.functional.listeners.Client;
import org.restcomm.protocols.ss7.cap.functional.listeners.EventType;
import org.restcomm.protocols.ss7.cap.functional.listeners.Server;
import org.restcomm.protocols.ss7.cap.functional.listeners.TestEvent;
import org.restcomm.protocols.ss7.cap.functional.wrappers.CAPStackImplWrapper;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCallImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AOCSubsequentImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.AOCGPRSImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ChargingResultImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ElapsedTimeImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.FCIBCCCAMELSequence1GprsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.FreeFormatDataGprsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSEventSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAI_GSM0224;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedDigits;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.FCIBCCCAMELSequence1;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeDurationChargingResult;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeInformation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Tone;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingLevel;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CAI_GSM0224Impl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.FreeFormatDataImpl;
import org.restcomm.protocols.ss7.commonapp.gap.BasicGapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CalledAddressAndServiceImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapIndicatorsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPFunctionalTest extends SccpHarness {
	private static final int _TCAP_DIALOG_RELEASE_TIMEOUT = 0;

	private CAPStackImpl stack1;
	private CAPStackImpl stack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;

	@Override
	protected int getSSN() {
		return 146;
	}

	@Override
	protected int getSSN2() {
		return 146;
	}

	@Before
	public void beforeEach() throws Exception {
		this.sccpStack1Name = "CAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "CAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 146);
		peer2Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 146);

		this.stack1 = new CAPStackImplWrapper(this.sccpProvider1, 146, workerPool);
		this.stack2 = new CAPStackImplWrapper(this.sccpProvider2, 146, workerPool);

		this.stack1.start();
		this.stack2.start();
	}

	@After
	public void afterEach() {
		if (stack1 != null) {
			this.stack1.stop();
			this.stack1 = null;
		}

		if (stack2 != null) {
			this.stack2.stop();
			this.stack2 = null;
		}

		super.tearDown();
	}

	/**
	 * InitialDP + Error message SystemFailure ACN=CAP-v1-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest
	 * TC-END + Error message SystemFailure
	 * </pre>
	 */
	@Test
	public void testInitialDp_Error() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onErrorComponent(CAPDialog capDialog, Integer invokeId, CAPErrorMessage capErrorMessage) {
				super.onErrorComponent(capDialog, invokeId, capErrorMessage);

				assertTrue(capErrorMessage.isEmSystemFailure());
				CAPErrorMessageSystemFailure em = capErrorMessage.getEmSystemFailure();
				assertEquals(em.getUnavailableNetworkResource(), UnavailableNetworkResource.endUserFailure);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				super.handleSent(EventType.ErrorComponent, null);
				CAPErrorMessage capErrorMessage = this.capErrorMessageFactory
						.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.endUserFailure);
				try {
					ind.getCAPDialog().sendErrorComponent(ind.getInvokeId(), capErrorMessage);
				} catch (CAPException e) {
					this.error("Error while trying to send Response SystemFailure", e);
				}
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				try {
					capDialog.close(false, dummyCallback);
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ErrorComponent, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp(CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF);
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Circuit switch call simple messageflow 1 ACN=CAP-v2-gsmSSF-to-gsmSCF
	 * 
	 * <pre>
	 * TC-BEGIN + InitialDPRequest
	 * TC-CONTINUE + RequestReportBCSMEventRequest
	 * TC-CONTINUE + FurnishChargingInformationRequest
	 * TC-CONTINUE + ApplyChargingRequest + ConnectRequest
	 * TC-CONTINUE + ContinueRequest
	 * TC-CONTINUE + SendChargingInformationRequest
	 * TC-CONTINUE + EventReportBCSMRequest (OAnswer)
	 * TC-CONTINUE + ApplyChargingReportRequest <call... waiting till DialogTimeout>
	 * TC-CONTINUE + ActivityTestRequest
	 * TC-CONTINUE + ActivityTestResponse
	 * TC-CONTINUE + EventReportBCSMRequest (ODisconnect)
	 * TC-END (empty)
	 * </pre>
	 */
	@Test
	public void testCircuitCall1() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;
			private int activityTestInvokeId;

			@Override
			public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
				super.onRequestReportBCSMEventRequest(ind);

				this.checkRequestReportBCSMEventRequest(ind);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind) {
				super.onFurnishChargingInformationRequest(ind);

				byte[] freeFormatData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
				assertTrue(ByteBufUtil.equals(ind.getFCIBCCCAMELsequence1().getFreeFormatData().getValue(),
						Unpooled.wrappedBuffer(freeFormatData)));
				assertEquals(ind.getFCIBCCCAMELsequence1().getPartyToCharge(), LegType.leg1);
				assertEquals(ind.getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onApplyChargingRequest(ApplyChargingRequest ind) {
				super.onApplyChargingRequest(ind);

				assertEquals(ind.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration(), 1000);
				// we are sending V2 which does not have this property
				assertFalse(ind.getAChBillingChargingCharacteristics().getReleaseIfdurationExceeded());
				assertNull(ind.getAChBillingChargingCharacteristics().getTariffSwitchInterval());
				assertEquals(ind.getPartyToCharge(), LegType.leg1);
				assertNull(ind.getExtensions());
				assertNotNull(ind.getAChChargingAddress());
				assertNotNull(ind.getAChChargingAddress().getLegID());
				assertNotNull(ind.getAChChargingAddress().getLegID().getSendingSideID());
				assertNull(ind.getAChChargingAddress().getLegID().getReceivingSideID());
				assertEquals(ind.getAChChargingAddress().getLegID().getSendingSideID(), LegType.leg1);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onConnectRequest(ConnectRequest ind) {
				super.onConnectRequest(ind);

				try {
					assertEquals(ind.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
					CalledPartyNumber calledPartyNumber = ind.getDestinationRoutingAddress().getCalledPartyNumber()
							.get(0).getCalledPartyNumber();
					assertTrue(calledPartyNumber.getAddress().equals("5599999988"));
					assertEquals(calledPartyNumber.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(calledPartyNumber.getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);
					assertEquals(calledPartyNumber.getInternalNetworkNumberIndicator(),
							CalledPartyNumber._INN_ROUTING_ALLOWED);
				} catch (ASNParsingException e) {
					e.printStackTrace();
					fail("Exception while checking ConnectRequest imdication");
				}
				assertNull(ind.getAlertingPattern());
				assertNull(ind.getCallingPartysCategory());
				assertNull(ind.getChargeNumber());
				assertNull(ind.getGenericNumbers());
				assertNull(ind.getLegToBeConnected());
				assertNull(ind.getOriginalCalledPartyID());
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onActivityTestRequest(ActivityTestRequest ind) {
				super.onActivityTestRequest(ind);

				activityTestInvokeId = ind.getInvokeId();
				dialogStep = 2;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onContinueRequest(ContinueRequest ind) {
				super.onContinueRequest(ind);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onSendChargingInformationRequest(SendChargingInformationRequest ind) {
				super.onSendChargingInformationRequest(ind);

				CAI_GSM0224 aocInitial = ind.getSCIBillingChargingCharacteristics().getAOCBeforeAnswer()
						.getAOCInitial();
				assertEquals((int) aocInitial.getE1(), 1);
				assertEquals((int) aocInitial.getE2(), 2);
				assertEquals((int) aocInitial.getE3(), 3);
				assertNull(aocInitial.getE4());
				assertNull(aocInitial.getE5());
				assertNull(aocInitial.getE6());
				assertNull(aocInitial.getE7());
				assertNull(ind.getSCIBillingChargingCharacteristics().getAOCBeforeAnswer().getAOCSubsequent());
				assertNull(ind.getSCIBillingChargingCharacteristics().getAOCSubsequent());
				assertNull(ind.getSCIBillingChargingCharacteristics().getAOCExtension());
				assertEquals(ind.getPartyToCharge(), LegType.leg2);
				assertNull(ind.getExtensions());

				dialogStep = 1;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after ConnectRequest
						OAnswerSpecificInfo oAnswerSpecificInfo = this.capParameterFactory
								.createOAnswerSpecificInfo(null, false, false, null, null, null);
						MiscCallInfo miscCallInfo = this.capParameterFactory
								.createMiscCallInfo(MiscCallInfoMessageType.notification, null);
						EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capParameterFactory
								.createEventSpecificInformationBCSM(oAnswerSpecificInfo);
						dlg.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, eventSpecificInformationBCSM, LegType.leg2,
								miscCallInfo, null);
						super.handleSent(EventType.EventReportBCSMRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						TimeInformation timeInformation = this.capParameterFactory.createTimeInformation(2000);
						TimeDurationChargingResult timeDurationChargingResult = this.capParameterFactory
								.createTimeDurationChargingResult(LegType.leg1, timeInformation, true, false, null,
										null);
						dlg.addApplyChargingReportRequest(timeDurationChargingResult);
						super.handleSent(EventType.ApplyChargingReportRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;

					case 2: // after ActivityTestRequest
						dlg.addActivityTestResponse(activityTestInvokeId);
						super.handleSent(EventType.ActivityTestResponse, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;
			private boolean firstEventReportBCSMRequest = true;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
			}

			@Override
			public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
				super.onEventReportBCSMRequest(ind);

				if (firstEventReportBCSMRequest) {
					firstEventReportBCSMRequest = false;

					assertEquals(ind.getEventTypeBCSM(), EventTypeBCSM.oAnswer);
					assertNotNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo());
					assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getDestinationAddress());
					assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getChargeIndicator());
					assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getExtBasicServiceCode());
					assertNull(
							ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getExtBasicServiceCode2());
					assertFalse(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getForwardedCall());
					assertFalse(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getOrCall());
					assertEquals(ind.getLegID(), LegType.leg2);
					assertNull(ind.getExtensions());
				} else {
					try {
						assertEquals(ind.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
						assertNotNull(ind.getEventSpecificInformationBCSM().getODisconnectSpecificInfo());
						CauseIndicators ci = ind.getEventSpecificInformationBCSM().getODisconnectSpecificInfo()
								.getReleaseCause().getCauseIndicators();
						assertEquals(ci.getCauseValue(), CauseIndicators._CV_ALL_CLEAR);
						assertEquals(ci.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
						assertEquals(ci.getLocation(), CauseIndicators._LOCATION_USER);
						assertEquals(ind.getLegID(), LegType.leg1);
						assertEquals(ind.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.notification);
						assertNull(ind.getMiscCallInfo().getDpAssignment());
						assertNull(ind.getExtensions());
					} catch (ASNParsingException e) {
						this.error("Exception while checking EventReportBCSMRequest - the second message", e);
					}

					dialogStep = 2;
				}
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
				super.onApplyChargingReportRequest(ind);

				TimeDurationChargingResult tdr = ind.getTimeDurationChargingResult();
				assertEquals(tdr.getPartyToCharge(), LegType.leg1);
				assertEquals((int) tdr.getTimeInformation().getTimeIfNoTariffSwitch(), 2000);
				assertNotNull(tdr.getAChChargingAddress());
				assertNotNull(tdr.getAChChargingAddress().getLegID());
				assertNotNull(tdr.getAChChargingAddress().getLegID().getReceivingSideID());
				assertEquals(tdr.getAChChargingAddress().getLegID().getReceivingSideID(), LegType.leg1);
				assertFalse(tdr.getCallLegReleasedAtTcpExpiry());
				assertNull(tdr.getExtensions());
				assertTrue(tdr.getLegActive());
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp

						RequestReportBCSMEventRequest rrc = this.getRequestReportBCSMEventRequest();
						dlg.addRequestReportBCSMEventRequest(rrc.getBCSMEventList(), rrc.getExtensions());
						super.handleSent(EventType.RequestReportBCSMEventRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						byte[] freeFormatData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
						FreeFormatDataImpl ffd = new FreeFormatDataImpl(Unpooled.wrappedBuffer(freeFormatData));
						FCIBCCCAMELSequence1 FCIBCCCAMELsequence1 = this.capParameterFactory
								.createFCIBCCCAMELsequence1(ffd, LegType.leg1, AppendFreeFormatData.append);
						dlg.addFurnishChargingInformationRequest(FCIBCCCAMELsequence1);
						dlg.send(dummyCallback);
						super.handleSent(EventType.FurnishChargingInformationRequest, null);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						// Boolean tone, CAPExtensionsImpl extensions, Long tariffSwitchInterval
						CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics = this.capParameterFactory
								.createCAMELAChBillingChargingCharacteristics(1000, null, null, null);
						dlg.addApplyChargingRequest(aChBillingChargingCharacteristics, LegType.leg1, null, null);
						super.handleSent(EventType.ApplyChargingRequest, null);

						List<CalledPartyNumberIsup> calledPartyNumber = new ArrayList<CalledPartyNumberIsup>();
						CalledPartyNumber cpn = this.isupParameterFactory.createCalledPartyNumber();
						cpn.setAddress("5599999988");
						cpn.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
						cpn.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
						cpn.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
						CalledPartyNumberIsup cpnc = this.capParameterFactory.createCalledPartyNumber(cpn);
						calledPartyNumber.add(cpnc);
						DestinationRoutingAddress destinationRoutingAddress = this.capParameterFactory
								.createDestinationRoutingAddress(calledPartyNumber);
						dlg.addConnectRequest(destinationRoutingAddress, null, null, null, null, null, null, null, null,
								null, null, null, null, false, false, false, null, false, false);
						super.handleSent(EventType.ConnectRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						dlg.addContinueRequest();
						super.handleSent(EventType.ContinueRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						CAI_GSM0224 aocInitial = this.capParameterFactory.createCAI_GSM0224(1, 2, 3, null, null, null,
								null);
						AOCBeforeAnswer aocBeforeAnswer = this.capParameterFactory.createAOCBeforeAnswer(aocInitial,
								null);
						SCIBillingChargingCharacteristics sciBillingChargingCharacteristics = this.capParameterFactory
								.createSCIBillingChargingCharacteristics(aocBeforeAnswer);
						dlg.addSendChargingInformationRequest(sciBillingChargingCharacteristics, LegType.leg2, null);
						super.handleSent(EventType.SendChargingInformationRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;

					case 2: // after oDisconnect
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}

			@Override
			public void onDialogTimeout(CAPDialog capDialog) {
				super.onDialogTimeout(capDialog);

				capDialog.keepAlive();

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
				try {
					dlg.addActivityTestRequest(500);
					dlg.send(dummyCallback);
				} catch (CAPException e) {
					this.error("Error while trying to send ActivityTestRequest", e);
				}
				super.handleSent(EventType.ActivityTestRequest, null);
			}
		};

		long _DIALOG_TIMEOUT = 2000;
		long _SLEEP_BEFORE_ODISCONNECT = 3000;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.FurnishChargingInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ApplyChargingRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ConnectRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendChargingInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportBCSMRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ApplyChargingReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestResponse, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportBCSMRequest, null, count++,
				stamp + _SLEEP_BEFORE_ODISCONNECT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp + _SLEEP_BEFORE_ODISCONNECT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _SLEEP_BEFORE_ODISCONNECT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.FurnishChargingInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ApplyChargingRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ConnectRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SendChargingInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportBCSMRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ApplyChargingReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestResponse, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportBCSMRequest, null, count++,
				stamp + _SLEEP_BEFORE_ODISCONNECT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _SLEEP_BEFORE_ODISCONNECT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _SLEEP_BEFORE_ODISCONNECT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

//        this.saveTrafficInFile();

		// setting dialog timeout little interval to invoke onDialogTimeout on SCF side
		server.capStack.getTCAPStack().setInvokeTimeout(_DIALOG_TIMEOUT - 100);
		server.capStack.getTCAPStack().setDialogIdleTimeout(_DIALOG_TIMEOUT);
		client.capStack.getTCAPStack().setDialogIdleTimeout(60000);
		client.suppressInvokeTimeout();
		client.sendInitialDp(CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF);

		// waiting here for DialogTimeOut -> ActivityTest
		Thread.sleep(_SLEEP_BEFORE_ODISCONNECT);

		// sending an event of call finishing
		client.sendEventReportBCSMRequest_1();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		// Thread.currentThread().sleep(1000000);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 *
	 * Circuit switch call play announcement and disconnect ACN =
	 * capssf-scfGenericAC V3
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest
	 * TC-CONTINUE + RequestReportBCSMEventRequest
	 * TC-CONTINUE + ConnectToResourceRequest
	 * TC-CONTINUE + PlayAnnouncementRequest
	 * TC-CONTINUE + SpecializedResourceReportRequest
	 * TC-CONTINUE + DisconnectForwardConnectionRequest
	 * TC-END + ReleaseCallRequest
	 * </pre>
	 */
	@Test
	public void testPlayAnnouncment() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
				super.onRequestReportBCSMEventRequest(ind);

				this.checkRequestReportBCSMEventRequest(ind);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
				super.onConnectToResourceRequest(ind);

				try {
					CalledPartyNumber cpn = ind.getResourceAddress_IPRoutingAddress().getCalledPartyNumber();
					assertTrue(cpn.getAddress().equals("111222333"));
					assertEquals(cpn.getInternalNetworkNumberIndicator(), CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
					assertEquals(cpn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(cpn.getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);
				} catch (ASNParsingException e) {
					this.error("Error while checking ConnectToResourceRequest", e);
				}
				assertFalse(ind.getResourceAddress_Null());
				assertNull(ind.getCallSegmentID());
				assertNull(ind.getExtensions());
				assertNull(ind.getServiceInteractionIndicatorsTwo());
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			private int playAnnounsmentInvokeId;

			@Override
			public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
				super.onPlayAnnouncementRequest(ind);

				assertEquals(ind.getInformationToSend().getTone().getToneID(), 10);
				assertEquals((int) ind.getInformationToSend().getTone().getDuration(), 100);
				assertTrue(ind.getDisconnectFromIPForbidden());
				assertTrue(ind.getRequestAnnouncementCompleteNotification());
				assertFalse(ind.getRequestAnnouncementStartedNotification());
				assertNull(ind.getCallSegmentID());
				assertNull(ind.getExtensions());

				playAnnounsmentInvokeId = ind.getInvokeId();

				dialogStep = 1;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
				super.onDisconnectForwardConnectionRequest(ind);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onReleaseCallRequest(ReleaseCallRequest ind) {
				super.onReleaseCallRequest(ind);

				CauseIndicators ci;
				try {
					ci = ind.getCause().getCauseIndicators();
					assertEquals(ci.getCauseValue(), CauseIndicators._CV_SEND_SPECIAL_TONE);
					assertEquals(ci.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
					assertNull(ci.getDiagnostics());
					assertEquals(ci.getLocation(), CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);
				} catch (ASNParsingException e) {
					this.error("Error while checking ReleaseCallRequest", e);
				}
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after PlayAnnouncementRequest
						dlg.addSpecializedResourceReportRequest_CapV23(playAnnounsmentInvokeId);
						super.handleSent(EventType.SpecializedResourceReportRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
				super.onSpecializedResourceReportRequest(ind);

				assertFalse(ind.getFirstAnnouncementStarted());
				assertFalse(ind.getAllAnnouncementsComplete());

				dialogStep = 2;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp
						RequestReportBCSMEventRequest rrc = this.getRequestReportBCSMEventRequest();
						dlg.addRequestReportBCSMEventRequest(rrc.getBCSMEventList(), rrc.getExtensions());
						super.handleSent(EventType.RequestReportBCSMEventRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						CalledPartyNumber calledPartyNumber = this.isupParameterFactory.createCalledPartyNumber();
						calledPartyNumber.setAddress("111222333");
						calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
						calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
						calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
						CalledPartyNumberIsup resourceAddress_IPRoutingAddress = this.capParameterFactory
								.createCalledPartyNumber(calledPartyNumber);
						dlg.addConnectToResourceRequest(resourceAddress_IPRoutingAddress, false, null, null, null);
						super.handleSent(EventType.ConnectToResourceRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						Tone tone = this.capParameterFactory.createTone(10, 100);
						InformationToSend informationToSend = this.capParameterFactory.createInformationToSend(tone);

						dlg.addPlayAnnouncementRequest(informationToSend, true, true, null, null,
								invokeTimeoutSuppressed);
						super.handleSent(EventType.PlayAnnouncementRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;

					case 2: // after SpecializedResourceReportRequest
						dlg.addDisconnectForwardConnectionRequest();
						super.handleSent(EventType.DisconnectForwardConnectionRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
						causeIndicators.setCauseValue(CauseIndicators._CV_SEND_SPECIAL_TONE);
						causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
						causeIndicators.setDiagnostics(null);
						causeIndicators.setLocation(CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);
						CauseIsup cause = this.capParameterFactory.createCause(causeIndicators);
						dlg.addReleaseCallRequest(cause);
						super.handleSent(EventType.ReleaseCallRequest, null);
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ConnectToResourceRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PlayAnnouncementRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DisconnectForwardConnectionRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReleaseCallRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ConnectToResourceRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PlayAnnouncementRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DisconnectForwardConnectionRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Assist SSF dialog (V4) ACN = capssf-scfAssistHandoffAC V4
	 * 
	 * <pre>
	 * TC-BEGIN + AssistRequestInstructionsRequest
	 * TC-CONTINUE + ResetTimerRequest
	 * TC-CONTINUE + PromptAndCollectUserInformationRequest
	 * TC-CONTINUE + SpecializedResourceReportRequest
	 * TC-CONTINUE + PromptAndCollectUserInformationResponse
	 * TC-CONTINUE + CancelRequest
	 * TC-END + CancelRequest
	 * </pre>
	 */
	@Test
	public void testAssistSsf() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;
			private int promptAndCollectUserInformationInvokeId;

			@Override
			public void onResetTimerRequest(ResetTimerRequest ind) {
				super.onResetTimerRequest(ind);

				assertEquals(ind.getTimerID(), TimerID.tssf);
				assertEquals(ind.getTimerValue(), 1001);
				assertNull(ind.getCallSegmentID());
				assertNull(ind.getExtensions());
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
				super.onPromptAndCollectUserInformationRequest(ind);

				promptAndCollectUserInformationInvokeId = ind.getInvokeId();

				CollectedDigits cd = ind.getCollectedInfo().getCollectedDigits();
				assertEquals((int) cd.getMinimumNbOfDigits(), 1);
				assertEquals(cd.getMaximumNbOfDigits(), 11);
				assertNull(cd.getCancelDigit());
				assertNull(cd.getEndOfReplyDigit());
				assertNull(cd.getFirstDigitTimeOut());
				assertNull(cd.getStartDigit());
				assertTrue(ind.getDisconnectFromIPForbidden());
				assertNull(ind.getInformationToSend());
				assertNull(ind.getExtensions());
				assertNull(ind.getCallSegmentID());
				assertFalse(ind.getRequestAnnouncementStartedNotification());

				dialogStep = 1;
			}

			private boolean cancelRequestFirst = true;

			@Override
			public void onCancelRequest(CancelRequest ind) {
				super.onCancelRequest(ind);

				if (cancelRequestFirst) {
					cancelRequestFirst = false;
					assertTrue(ind.getAllRequests());
					assertNull(ind.getInvokeID());
					assertNull(ind.getCallSegmentToCancel());
				} else {
					assertFalse(ind.getAllRequests());
					assertEquals((int) ind.getInvokeID(), 10);
					assertNull(ind.getCallSegmentToCancel());
				}
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after PromptAndCollectUserInformationRequest
						dlg.addSpecializedResourceReportRequest_CapV4(promptAndCollectUserInformationInvokeId, false,
								true);
						super.handleSent(EventType.SpecializedResourceReportRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (Exception ex) {

						}

						GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
						genericNumber.setAddress("444422220000");
						genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
						genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_SUBSCRIBER_NUMBER);
						genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_DATA);
						genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLING_PARTY_NUMBER);
						genericNumber.setScreeningIndicator(GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
						DigitsIsup digitsResponse = this.capParameterFactory.createDigits_GenericNumber(genericNumber);
						dlg.addPromptAndCollectUserInformationResponse_DigitsResponse(
								promptAndCollectUserInformationInvokeId, digitsResponse);
						super.handleSent(EventType.PromptAndCollectUserInformationResponse, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind) {
				super.onAssistRequestInstructionsRequest(ind);

				try {
					// assertNull(ind.getCorrelationID().getGenericDigits());
					GenericNumber gn = ind.getCorrelationID().getGenericNumber();
					assertTrue(gn.getAddress().equals("333111222"));
					assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
					assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_ISDN);
					assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLED_NUMBER);
					assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_NETWORK_PROVIDED);

					IPSSPCapabilities ipc = ind.getIPSSPCapabilities();
					assertTrue(ipc.getIPRoutingAddressSupported());
					assertFalse(ipc.getVoiceBackSupported());
					assertTrue(ipc.getVoiceInformationSupportedViaSpeechRecognition());
					assertFalse(ipc.getVoiceInformationSupportedViaVoiceRecognition());
					assertFalse(ipc.getGenerationOfVoiceAnnouncementsFromTextSupported());
					assertNull(ipc.getExtraData());

					assertNull(ind.getExtensions());
				} catch (ASNParsingException e) {
					this.error("Error while checking AssistRequestInstructionsRequest", e);
				}

				dialogStep = 1;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind) {
				super.onPromptAndCollectUserInformationResponse(ind);

				try {
					DigitsIsup digits = ind.getDigitsResponse();
					digits.setIsGenericNumber();
					GenericNumber gn = digits.getGenericNumber();
					assertTrue(gn.getAddress().equals("444422220000"));
					assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
					assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_SUBSCRIBER_NUMBER);
					assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_DATA);
					assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLING_PARTY_NUMBER);
					assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
				} catch (ASNParsingException e) {
					this.error("Error while checking PromptAndCollectUserInformationResponse", e);
				}

				dialogStep = 2;
			}

			@Override
			public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
				super.onSpecializedResourceReportRequest(ind);

				assertFalse(ind.getAllAnnouncementsComplete());
				assertTrue(ind.getFirstAnnouncementStarted());

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after AssistRequestInstructionsRequest
						dlg.addResetTimerRequest(TimerID.tssf, 1001, null, null);
						super.handleSent(EventType.ResetTimerRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (Exception ex) {

						}

						CollectedDigits collectedDigits = this.capParameterFactory.createCollectedDigits(1, 11, null,
								null, null, null, null, null, null, null, null);
						CollectedInfo collectedInfo = this.capParameterFactory.createCollectedInfo(collectedDigits);
						dlg.addPromptAndCollectUserInformationRequest(collectedInfo, true, null, null, null, null);
						super.handleSent(EventType.PromptAndCollectUserInformationRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;

					case 2: // after SpecializedResourceReportRequest
						dlg.addCancelRequest_AllRequests();
						super.handleSent(EventType.CancelRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (Exception ex) {

						}

						dlg.addCancelRequest_InvokeId(10);
						super.handleSent(EventType.CancelRequest, null);
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.AssistRequestInstructionsRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ResetTimerRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.AssistRequestInstructionsRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ResetTimerRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendAssistRequestInstructionsRequest();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * ScfSsf test (V4) ACN = capscf-ssfGenericAC V4
	 * 
	 * <pre>
	 * TC-BEGIN + establishTemporaryConnection + callInformationRequest + collectInformationRequest 
	 * TC-END + callInformationReport
	 * </pre>
	 */
	@Test
	public void testScfSsf() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onCallInformationReportRequest(CallInformationReportRequest ind) {
				super.onCallInformationReportRequest(ind);

				List<RequestedInformation> al = ind.getRequestedInformationList();
				assertEquals(al.size(), 1);
				DateAndTime dt = al.get(0).getCallStopTimeValue();
				assertEquals(dt.getYear(), 2012);
				assertEquals(dt.getMonth(), 11);
				assertEquals(dt.getDay(), 30);
				assertEquals(dt.getHour(), 23);
				assertEquals(dt.getMinute(), 50);
				assertEquals(dt.getSecond(), 40);
				assertNull(ind.getExtensions());
				assertNotNull(ind.getLegID());
				assertEquals(ind.getLegID(), LegType.leg2);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind) {
				super.onEstablishTemporaryConnectionRequest(ind);

				try {
					GenericNumber gn = ind.getAssistingSSPIPRoutingAddress().getGenericNumber();
					assertTrue(gn.getAddress().equals("333111222"));
					assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
					assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_ISDN);
					assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLED_NUMBER);
					assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_NETWORK_PROVIDED);

					assertNull(ind.getCallingPartyNumber());
					assertNull(ind.getCallSegmentID());
					assertNull(ind.getCarrier());
					assertNull(ind.getChargeNumber());
					assertNull(ind.getCorrelationID());
					assertNull(ind.getExtensions());
					assertNull(ind.getNAOliInfo());
					assertNull(ind.getOriginalCalledPartyID());
					assertNull(ind.getScfID());
					assertNull(ind.getServiceInteractionIndicatorsTwo());
				} catch (ASNParsingException e) {
					this.error("Error while trying checking EstablishTemporaryConnectionRequest", e);
				}
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onCallInformationRequestRequest(CallInformationRequestRequest ind) {
				super.onCallInformationRequestRequest(ind);

				List<RequestedInformationType> al = ind.getRequestedInformationTypeList();
				assertEquals(al.size(), 1);
				assertEquals(al.get(0), RequestedInformationType.callStopTime);
				assertNull(ind.getExtensions());
				assertNotNull(ind.getLegID());
				assertEquals(ind.getLegID(), LegType.leg2);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onCollectInformationRequest(CollectInformationRequest ind) {
				super.onCollectInformationRequest(ind);

				dialogStep = 1;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after CallInformationRequestRequest
						List<RequestedInformation> requestedInformationList = new ArrayList<RequestedInformation>();
						DateAndTime dt = this.capParameterFactory.createDateAndTime(2012, 11, 30, 23, 50, 40);
						RequestedInformation ri = this.capParameterFactory.createRequestedInformation_CallStopTime(dt);
						requestedInformationList.add(ri);
						dlg.addCallInformationReportRequest(requestedInformationList, null, null);
						super.handleSent(EventType.CallInformationReportRequest, null);
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.EstablishTemporaryConnectionRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CallInformationRequestRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CollectInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CallInformationReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EstablishTemporaryConnectionRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CallInformationRequestRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CollectInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CallInformationReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendEstablishTemporaryConnectionRequest_CallInformationRequest();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Abnormal test ACN = CAP-v2-assist-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + ActivityTestRequest
	 * TC-CONTINUE <no ActivityTestResponse> resetInvokeTimer() before InvokeTimeout InvokeTimeout 
	 * TC-CONTINUE + CancelRequest + cancelInvocation() -> CancelRequest will not go to Server
	 * TC-CONTINUE + ResetTimerRequest reject ResetTimerRequest 
	 * DialogUserAbort: AbortReason=missing_reference
	 * </pre>
	 */
	@Test
	public void testAbnormal() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;
			private long resetTimerRequestInvokeId;

			@Override
			public void onInvokeTimeout(CAPDialog capDialog, Integer invokeId) {
				super.onInvokeTimeout(capDialog, invokeId);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					int invId = dlg.addCancelRequest_AllRequests();
					super.handleSent(EventType.CancelRequest, null);
					dlg.cancelInvocation(invId);
					dlg.send(dummyCallback);

					resetTimerRequestInvokeId = dlg.addResetTimerRequest(TimerID.tssf, 2222, null, null);
					super.handleSent(EventType.ResetTimerRequest, null);
					dlg.send(dummyCallback);
				} catch (CAPException e) {
					this.error("Error while checking CancelRequest or ResetTimerRequest", e);
				}
			}

			@Override
			public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(capDialog, invokeId, problem, isLocalOriginated);

				assertEquals(resetTimerRequestInvokeId, (long) invokeId);
				try {
					assertEquals(problem.getInvokeProblemType(), InvokeProblemType.MistypedParameter);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(isLocalOriginated);

				dialogStep = 1;
			}

			@Override
			public void onDialogRelease(CAPDialog capDialog) {
				super.onDialogRelease(capDialog);
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
				try {
					switch (dialogStep) {
					case 1: // after RejectComponent
						super.handleSent(EventType.DialogUserAbort, null);
						dlg.abort(CAPUserAbortReason.missing_reference, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onActivityTestRequest(ActivityTestRequest ind) {
				super.onActivityTestRequest(ind);

				dialogStep = 1;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason,
					CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);

				assertNull(generalReason);
				assertNull(userReason);
			}

			@Override
			public void onDialogRelease(CAPDialog capDialog) {
				super.onDialogRelease(capDialog);
			}

			int resetTimerRequestInvokeId;

			@Override
			public void onResetTimerRequest(ResetTimerRequest ind) {
				super.onResetTimerRequest(ind);

				resetTimerRequestInvokeId = ind.getInvokeId();

				dialogStep = 2;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after ActivityTestRequest
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;

					case 2: // after ResetTimerRequest
						ProblemImpl problem = new ProblemImpl();
						problem.setInvokeProblemType(InvokeProblemType.MistypedParameter);
						try {
							dlg.sendRejectComponent(resetTimerRequestInvokeId, problem);
							super.handleSent(EventType.RejectComponent, null);
						} catch (CAPException e) {
							this.error("Error while sending reject", e);
						}

						dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		int _ACTIVITY_TEST_INVOKE_TIMEOUT = 1000;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ResetTimerRequest, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DialogUserAbort, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ResetTimerRequest, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RejectComponent, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendActivityTestRequest(_ACTIVITY_TEST_INVOKE_TIMEOUT);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * DialogTimeout test ACN=CAP-v3-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest
	 * TC-CONTINUE empty (no answer - DialogTimeout at both sides)
	 * </pre>
	 */
	@Test
	public void testDialogTimeout() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address);

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			private int dialogStep;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDpRequest
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}

			@Override
			public void onDialogTimeout(CAPDialog capDialog) {
				super.onDialogTimeout(capDialog);
			}
		};

		long _DIALOG_TIMEOUT = 2000;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _DIALOG_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _DIALOG_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		// setting dialog timeout little interval to invoke onDialogTimeout on SCF side
		server.capStack.getTCAPStack().setInvokeTimeout(_DIALOG_TIMEOUT - 100);
		server.capStack.getTCAPStack().setDialogIdleTimeout(_DIALOG_TIMEOUT + 100);
		server.suppressInvokeTimeout();
		client.capStack.getTCAPStack().setInvokeTimeout(_DIALOG_TIMEOUT - 100);
		client.capStack.getTCAPStack().setDialogIdleTimeout(_DIALOG_TIMEOUT - 100);
		client.suppressInvokeTimeout();
		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);

		// waiting here for DialogTimeOut -> ActivityTest
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * ACNNotSuported test ACN=CAP-v3-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest (Server service is down -> ACN not supported)
	 * TC-ABORT + ACNNotSuported
	 * </pre>
	 */
	@Test
	public void testACNNotSuported() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason,
					CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);

				assertEquals(generalReason, CAPGeneralAbortReason.ACNNotSupported);
				assertNull(userReason);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.Abort);
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}

			@Override
			public void onDialogTimeout(CAPDialog capDialog) {
				super.onDialogTimeout(capDialog);
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		server.capProvider.getCAPServiceCircuitSwitchedCall().deactivate();

		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);

		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Bad data sending at TC-BEGIN test - no ACN
	 *
	 * <pre>
	 * TC-BEGIN + no ACN
	 * TC-ABORT + BadReceivedData
	 * </pre>
	 */
	@Test
	public void testBadDataSendingNoAcn() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason,
					CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);

				assertNull(generalReason);
				assertNull(userReason);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.Abort);
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}

			@Override
			public void onDialogTimeout(CAPDialog capDialog) {
				super.onDialogTimeout(capDialog);
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.sendBadDataNoAcn();
		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * TC-CONTINUE from Server after dialogRelease at Client
	 *
	 * <pre>
	 * TC-BEGIN + InitialDP relaseDialog 
	 * TC-CONTINUE ProviderAbort
	 * </pre>
	 */
	@Test
	public void testProviderAbort() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}

			@Override
			public void onDialogRelease(CAPDialog capDialog) {
				super.onDialogRelease(capDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			@Override
			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason,
					CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);
			}

			@Override
			public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
				super.onDialogProviderAbort(capDialog, abortCause);

				assertEquals(abortCause, PAbortCauseType.UnrecognizedTxID);
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}
		};

		long _DIALOG_RELEASE_DELAY = 100;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, stamp + _DIALOG_RELEASE_DELAY);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _DIALOG_RELEASE_DELAY + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp(CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF);
		client.releaseDialog();
		Thread.sleep(_DIALOG_RELEASE_DELAY);
		server.sendAccept();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * referensedNumber
	 *
	 * <pre>
	 * TC-BEGIN + referensedNumber
	 * TC-END + referensedNumber
	 * </pre>
	 */
	@Test
	public void testReferensedNumber() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogAccept(capDialog, capGprsReferenceNumber);

				assertEquals((int) capGprsReferenceNumber.getDestinationReference(), 10005);
				assertEquals((int) capGprsReferenceNumber.getOriginationReference(), 10006);
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}

			@Override
			public void onDialogRelease(CAPDialog capDialog) {
				super.onDialogRelease(capDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogSteps = 0;

			@Override
			public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogRequest(capDialog, capGprsReferenceNumber);

				assertEquals((int) capGprsReferenceNumber.getDestinationReference(), 1005);
				assertEquals((int) capGprsReferenceNumber.getOriginationReference(), 1006);

				dialogSteps = 1;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

				if (dialogSteps == 1) {
					dialogSteps = 0;

					try {
						CAPGprsReferenceNumber capGprsReferenceNumber = this.capParameterFactory
								.createCAPGprsReferenceNumber(10005, 10006);
						dlg.setGprsReferenceNumber(capGprsReferenceNumber);
						dlg.close(false, dummyCallback);
					} catch (CAPException e) {
						this.error("Error while trying to send/close() Dialog", e);
					}
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendReferensedNumber();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * <pre>
	 * TC-BEGIN + broken referensedNumber
	 * TC-ABORT
	 * </pre>
	 */
	@Test
	public void testMessageUserDataLength() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}

			@Override
			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason,
					CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.Abort);
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events

		client.testMessageUserDataLength();

		// client.awaitReceived(EventType.DialogRelease);server.awaitReceived(EventType.DialogRelease);
		//
		// client.compareEvents(clientExpectedEvents);
		// server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Some not real test for testing:
	 * <p>
	 * - sendDelayed() / closeDelayed()
	 * <p>
	 * - getTCAPMessageType()
	 * <p>
	 * - saving origReferense, destReference, extContainer in MAPDialog
	 * 
	 * <pre>
	 * TC-BEGIN + referensedNumber + initialDPRequest + initialDPRequest
	 * TC-CONTINUE + sendDelayed(ContinueRequest) + sendDelayed(ContinueRequest)
	 * TC-END + closeDelayed(CancelRequest) + sendDelayed(CancelRequest)
	 * </pre>
	 */
	@Test
	public void testDelayedSendClose() throws Exception {
		Client client = new Client(stack1, peer1Address, peer2Address) {

			int dialogStep = 0;

			@Override
			public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogAccept(capDialog, capGprsReferenceNumber);

				assertEquals((int) capGprsReferenceNumber.getDestinationReference(), 201);
				assertEquals((int) capGprsReferenceNumber.getOriginationReference(), 202);

				assertEquals(capDialog.getTCAPMessageType(), MessageType.Continue);
			}

			@Override
			public void onContinueRequest(ContinueRequest ind) {
				super.onContinueRequest(ind);

				CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();
				assertEquals(d.getTCAPMessageType(), MessageType.Continue);

				assertEquals((int) d.getReceivedGprsReferenceNumber().getDestinationReference(), 201);
				assertEquals((int) d.getReceivedGprsReferenceNumber().getOriginationReference(), 202);

				try {
					d.addCancelRequest_AllRequests();
					if (dialogStep == 0)
						d.closeDelayed(false, dummyCallback);
					else
						d.sendDelayed(dummyCallback);
					dialogStep++;
					super.handleSent(EventType.CancelRequest, null);
				} catch (CAPException e) {
					this.error("Error while adding CancelRequest/sending", e);
					fail("Error while adding CancelRequest/sending");
				}
			};
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			int dialogStep = 0;

			@Override
			public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogRequest(capDialog, capGprsReferenceNumber);

				assertEquals((int) capGprsReferenceNumber.getDestinationReference(), 101);
				assertEquals((int) capGprsReferenceNumber.getOriginationReference(), 102);

				CAPGprsReferenceNumber grn = this.capParameterFactory.createCAPGprsReferenceNumber(201, 202);
				capDialog.setGprsReferenceNumber(grn);
			}

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();

				assertTrue(Client.checkTestInitialDp(ind));

				assertEquals((int) d.getReceivedGprsReferenceNumber().getDestinationReference(), 101);
				assertEquals((int) d.getReceivedGprsReferenceNumber().getOriginationReference(), 102);

				if (dialogStep < 2) {
					assertEquals(d.getTCAPMessageType(), MessageType.Begin);

					try {
						d.addContinueRequest();
						d.sendDelayed(dummyCallback);
						super.handleSent(EventType.ContinueRequest, null);
					} catch (CAPException e) {
						this.error("Error while adding ContinueRequest/sending", e);
						fail("Error while adding ContinueRequest/sending");
					}
				} else
					assertEquals(d.getTCAPMessageType(), MessageType.End);

				dialogStep++;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueRequest, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueRequest, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp2();
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Some not real test for testing:
	 * <p>
	 * - closeDelayed(true)
	 * <p>
	 * - getTCAPMessageType()
	 * 
	 * <pre>
	 * TC-BEGIN + initialDPRequest + initialDPRequest 
	 * TC-END + Prearranged + [ContinueRequest + ContinueRequest]
	 * </pre>
	 */
	@Test
	public void testDelayedClosePrearranged() throws Exception {
		Client client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogAccept(capDialog, capGprsReferenceNumber);

				assertNull(capGprsReferenceNumber);

				assertEquals(capDialog.getTCAPMessageType(), MessageType.End);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			int dialogStep = 0;

			@Override
			public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogRequest(capDialog, capGprsReferenceNumber);

				assertNull(capGprsReferenceNumber);

				assertEquals(capDialog.getTCAPMessageType(), MessageType.Begin);
			}

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();

				assertTrue(Client.checkTestInitialDp(ind));

				assertNull(d.getReceivedGprsReferenceNumber());

				assertEquals(d.getTCAPMessageType(), MessageType.Begin);

				try {
					d.addContinueRequest();
					if (dialogStep == 0)
						d.sendDelayed(dummyCallback);
					else
						d.closeDelayed(true, dummyCallback);
					super.handleSent(EventType.ContinueRequest, null);
				} catch (CAPException e) {
					this.error("Error while adding ContinueRequest/sending", e);
					fail("Error while adding ContinueRequest/sending");
				}

				dialogStep++;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

//        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
//        clientExpectedEvents.add(te);
//
//        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
//        clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, stamp);
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, stamp);
		serverExpectedEvents.add(te);

//        this.saveTrafficInFile();
		client.sendInitialDp3();
		client.clientCscDialog.close(true, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Testing for some special error cases:
	 * <p>
	 * - linkedId to an operation that does not support linked operations
	 * <p>
	 * - linkedId to a missed operation
	 *
	 * <pre>
	 * TC-BEGIN + initialDPRequest + playAnnouncement 
	 * TC-CONTINUE + SpecializedResourceReportRequest to initialDPRequest (->
	 * LinkedResponseUnexpected) + SpecializedResourceReportRequest to a missed
	 * operation (linkedId==bad==50 -> UnrechognizedLinkedID) + ContinueRequest to a
	 * playAnnouncement operation (-> UnexpectedLinkedOperation) +
	 * SpecializedResourceReportRequest to a playAnnouncement operation (-> normal
	 * case)
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testBadInvokeLinkedId() throws Exception {
		Client client = new Client(stack1, peer1Address, peer2Address) {
			int dialogStep = 0;

			@Override
			public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(capDialog, invokeId, problem, isLocalOriginated);

				dialogStep++;

				try {
					switch (dialogStep) {
					case 1:
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.LinkedResponseUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 2:
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnrechognizedLinkedID);
						assertTrue(isLocalOriginated);
						break;
					case 3:
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnexpectedLinkedOperation);
						assertTrue(isLocalOriginated);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					dlg.close(false, dummyCallback);
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			int invokeId1;
			int invokeId2;
			int outInvokeId1;
			int outInvokeId2;
			int outInvokeId3;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				invokeId1 = ind.getInvokeId();
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
				super.onPlayAnnouncementRequest(ind);

				invokeId2 = ind.getInvokeId();
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(capDialog, invokeId, problem, isLocalOriginated);

				try {
					if (invokeId == outInvokeId1) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.LinkedResponseUnexpected);
						assertFalse(isLocalOriginated);
					} else if (invokeId == outInvokeId2) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnrechognizedLinkedID);
						assertFalse(isLocalOriginated);
					} else if (invokeId == outInvokeId3) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnexpectedLinkedOperation);
						assertFalse(isLocalOriginated);
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCallImpl dlg = (CAPDialogCircuitSwitchedCallImpl) capDialog;

				try {
					outInvokeId1 = dlg.addSpecializedResourceReportRequest_CapV23(invokeId1);
					outInvokeId2 = dlg.addSpecializedResourceReportRequest_CapV23(50);
					outInvokeId3 = dlg.sendDataComponent(null, invokeId2, null, 2000L, CAPOperationCode.continueCode,
							null, true, false);

					dlg.addSpecializedResourceReportRequest_CapV23(invokeId2);
					super.handleSent(EventType.SpecializedResourceReportRequest, null);
					super.handleSent(EventType.SpecializedResourceReportRequest, null);
					super.handleSent(EventType.ContinueRequest, null);
					super.handleSent(EventType.SpecializedResourceReportRequest, null);

					dlg.send(dummyCallback);
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PlayAnnouncementRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PlayAnnouncementRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp_playAnnouncement();
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * ReturnResultLast & ReturnError for operation classes 1, 2, 3, 4
	 *
	 * <pre>
	 * TC-BEGIN + initialDPRequest (class2, invokeId==1) + initialDPRequest (class2,
	 * invokeId==2) + promptAndCollectUserInformationRequest (class1, invokeId==3) +
	 * promptAndCollectUserInformationRequest (class1, invokeId==4) + +
	 * activityTestRequest (class3, invokeId==5) + activityTestRequest (class3,
	 * invokeId==6) + releaseCallRequest (class4, invokeId==7) + releaseCallRequest
	 * (class4, invokeId==7)
	 *
	 * TC-CONTINUE + ReturnResultLast (initialDP, invokeId==1 ->
	 * ReturnResultUnexpected) + SystemFailureError (initialDP, invokeId==2 -> OK) +
	 * promptAndCollectUserInformationResponse (invokeId==3 -> OK) +
	 * SystemFailureError (promptAndCollectUserInformation, invokeId==4 -> OK) +
	 * activityTestResponse (invokeId==5 -> OK) + SystemFailureError (activityTest,
	 * invokeId==6 -> ReturnErrorUnexpected) + ReturnResultLast (releaseCall,
	 * invokeId==7 -> ReturnResultUnexpected) + SystemFailureError
	 * (releaseCallRequest, invokeId==8 -> ReturnErrorUnexpected) TC-END + Reject
	 * (ReturnResultUnexpected) + Reject (ReturnErrorUnexpected) + Reject
	 * (ReturnResultUnexpected) + Reject (ReturnErrorUnexpected)
	 * </pre>
	 */
	@Test
	public void testUnexpectedResultError() throws Exception {
		Client client = new Client(stack1, peer1Address, peer2Address) {
			int rejectStep = 0;

			@Override
			public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(capDialog, invokeId, problem, isLocalOriginated);

				rejectStep++;

				try {
					switch (rejectStep) {
					case 1:
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 2:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 3:
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 4:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertTrue(isLocalOriginated);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					dlg.close(false, dummyCallback);
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			int dialogStep = 0;
			int rejectStep = 0;
			int invokeId1;
			int invokeId2;
			int invokeId3;
			int invokeId4;
			int invokeId5;
			int invokeId6;
			int invokeId7;
			int invokeId8;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				dialogStep++;

				switch (dialogStep) {
				case 1:
					invokeId1 = ind.getInvokeId();
					break;
				case 2:
					invokeId2 = ind.getInvokeId();
					break;
				}
			}

			@Override
			public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
				super.onPromptAndCollectUserInformationRequest(ind);

				dialogStep++;

				switch (dialogStep) {
				case 3:
					invokeId3 = ind.getInvokeId();
					break;
				case 4:
					invokeId4 = ind.getInvokeId();
					break;
				}
			}

			@Override
			public void onActivityTestRequest(ActivityTestRequest ind) {
				super.onActivityTestRequest(ind);

				dialogStep++;

				switch (dialogStep) {
				case 5:
					invokeId5 = ind.getInvokeId();
					break;
				case 6:
					invokeId6 = ind.getInvokeId();
					break;
				}
			}

			@Override
			public void onReleaseCallRequest(ReleaseCallRequest ind) {
				super.onReleaseCallRequest(ind);

				dialogStep++;

				switch (dialogStep) {
				case 7:
					invokeId7 = ind.getInvokeId();
					break;
				case 8:
					invokeId8 = ind.getInvokeId();
					break;
				}
			}

			@Override
			public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(capDialog, invokeId, problem, isLocalOriginated);

				rejectStep++;

				try {
					switch (rejectStep) {
					case 1:
						assertEquals((long) invokeId, invokeId1);
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 2:
						assertEquals((long) invokeId, invokeId6);
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 3:
						assertEquals((long) invokeId, invokeId7);
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 4:
						assertEquals((long) invokeId, invokeId8);
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertFalse(isLocalOriginated);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCallImpl dlg = (CAPDialogCircuitSwitchedCallImpl) capDialog;

				try {
					dlg.sendDataComponent(invokeId1, null, null, null, CAPOperationCode.initialDP, null, false, true);

					CAPErrorMessage mem = this.capErrorMessageFactory
							.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.endUserFailure);
					dlg.sendErrorComponent(invokeId2, mem);
					super.handleSent(EventType.ErrorComponent, null);

					GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
					genericNumber.setAddress("444422220000");
					genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
					genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_SUBSCRIBER_NUMBER);
					genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_DATA);
					genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLING_PARTY_NUMBER);
					genericNumber.setScreeningIndicator(GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
					DigitsIsup digitsResponse = this.capParameterFactory.createDigits_GenericNumber(genericNumber);
					dlg.addPromptAndCollectUserInformationResponse_DigitsResponse(invokeId3, digitsResponse);
					super.handleSent(EventType.PromptAndCollectUserInformationResponse, null);

					mem = this.capErrorMessageFactory
							.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
					dlg.sendErrorComponent(invokeId4, mem);
					super.handleSent(EventType.ErrorComponent, null);

					dlg.addActivityTestResponse(invokeId5);
					super.handleSent(EventType.ActivityTestResponse, null);

					mem = this.capErrorMessageFactory
							.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
					dlg.sendErrorComponent(invokeId6, mem);
					super.handleSent(EventType.ErrorComponent, null);

					dlg.sendDataComponent(invokeId7, null, null, null, CAPOperationCode.releaseCall, null, false, true);

					mem = this.capErrorMessageFactory
							.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
					dlg.sendErrorComponent(invokeId8, mem);
					super.handleSent(EventType.ErrorComponent, null);

					dlg.send(dummyCallback);
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ErrorComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationResponse, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ErrorComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestResponse, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReleaseCallRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReleaseCallRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInvokesForUnexpectedResultError();
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * <pre>
	 * TC-Message + bad UnrecognizedMessageType
	 * TC-ABORT UnrecognizedMessageType
	 * </pre>
	 */
	@Test
	public void testUnrecognizedMessageType() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
				super.onDialogProviderAbort(capDialog, abortCause);

				assertEquals(abortCause, PAbortCauseType.UnrecognizedMessageType);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		// sending a dummy message to a bad address for a dialog starting
		client.sendDummyMessage();

		// sending a badly formatted message
		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address,
				peer1Address, Unpooled.wrappedBuffer(getMessageBadTag()), 0, 0, false, null, null);

		this.sccpProvider1.send(message, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * <pre>
	 * TC-BEGIN + (bad sccp address + setReturnMessageOnError)
	 * TC-NOTICE
	 * </pre>
	 */
	@Test
	public void testTcNotice() throws Exception {
		Client client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
				super.onDialogNotice(capDialog, noticeProblemDiagnostic);

				assertEquals(noticeProblemDiagnostic, CAPNoticeProblemDiagnostic.MessageCannotBeDeliveredToThePeer);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogNotice, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.actionB();

		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	public static byte[] getMessageBadTag() {
		return new byte[] { 106, 6, 72, 1, 1, 73, 1, 1 };
	}

	/**
	 * GPSR messageflow 1 ACN=cap3-gprssf-scf
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPGPRSRequest + originationReference=1001
	 * TC-CONTINUE + requestReportGPRSEventRequest + destinationReference=1001 + originationReference=2001 
	 * TC-CONTINUE + furnishChargingInformationGPRSRequest
	 * TC-CONTINUE + eventReportGPRSRequest
	 * TC-CONTINUE + eventReportGPRSResponse
	 * TC-CONTINUE + resetTimerGPRSRequest
	 * TC-CONTINUE + applyChargingGPRSRequest + connectGPRSRequest 
	 * TC-CONTINUE + applyChargingReportGPRSRequest
	 * TC-END + applyChargingReportGPRSResponse
	 * </pre>
	 */
	@Test
	public void testGPRS1() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onApplyChargingReportGPRSResponse(ApplyChargingReportGPRSResponse ind) {
				super.onApplyChargingReportGPRSResponse(ind);
			}

			@Override
			public void onEventReportGPRSResponse(EventReportGPRSResponse ind) {
				super.onEventReportGPRSResponse(ind);
			}

			@Override
			public void onRequestReportGPRSEventRequest(RequestReportGPRSEventRequest ind) {
				super.onRequestReportGPRSEventRequest(ind);
				checkRequestReportGPRSEventRequest(ind);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onFurnishChargingInformationGPRSRequest(FurnishChargingInformationGPRSRequest ind) {
				super.onFurnishChargingInformationGPRSRequest(ind);

				byte[] bFreeFormatData = new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };
				assertTrue(ByteBufUtil.equals(ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1()
						.getFreeFormatData().getValue(), Unpooled.wrappedBuffer(bFreeFormatData)));
				assertEquals(
						ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getPDPID().getId(), 2);
				assertEquals(ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1()
						.getAppendFreeFormatData(), AppendFreeFormatData.append);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 1;
			}

			@Override
			public void onApplyChargingGPRSRequest(ApplyChargingGPRSRequest ind) {
				super.onApplyChargingGPRSRequest(ind);

				assertEquals(ind.getChargingCharacteristics().getMaxTransferredVolume(), 200L);
				assertEquals(ind.getTariffSwitchInterval().intValue(), 24);
				assertEquals(ind.getPDPID().getId(), 2);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onConnectGPRSRequest(ConnectGPRSRequest ind) {
				super.onConnectGPRSRequest(ind);
				assertTrue(ByteBufUtil.equals(ind.getAccessPointName().getValue(),
						Unpooled.wrappedBuffer(new byte[] { 52, 20, 30 })));
				assertEquals(ind.getPDPID().getId(), 2);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 2;
			}

			@Override
			public void onResetTimerGPRSRequest(ResetTimerGPRSRequest ind) {
				super.onResetTimerGPRSRequest(ind);
				assertEquals(ind.getTimerValue(), 12);
				assertEquals(ind.getTimerID(), TimerID.tssf);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after FurnishChargingInformationGPRSRequest
						GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
						MiscCallInfoImpl miscGPRSInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.notification,
								null);
						LAIFixedLengthImpl lai;
						try {
							lai = new LAIFixedLengthImpl(250, 1, 4444);
						} catch (ASNParsingException e) {
							throw new CAPException(e.getMessage(), e);
						}
						CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
						RAIdentityImpl ra = new RAIdentityImpl(
								Unpooled.wrappedBuffer(new byte[] { 11, 12, 13, 14, 15, 16 }));

						GeographicalInformationImpl ggi = null;
						try {
							ByteBuf geoBuffer = Unpooled.wrappedBuffer(new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 });
							ggi = new GeographicalInformationImpl(
									GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF),
									GeographicalInformationImpl.decodeLatitude(geoBuffer),
									GeographicalInformationImpl.decodeLongitude(geoBuffer),
									GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));
						} catch (ASNParsingException e) {
							throw new CAPException(e.getMessage(), e);
						}

						ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number,
								NumberingPlan.ISDN, "654321");
						LSAIdentityImpl lsa = new LSAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 91, 92, 93 }));

						GeodeticInformationImpl gdi = null;
						try {
							ByteBuf geodeticBuffer = Unpooled
									.wrappedBuffer(new byte[] { 1, 16, 3, 4, 5, 6, 7, 8, 9, 10 });
							gdi = new GeodeticInformationImpl(geodeticBuffer.readByte() & 0x0FF,
									GeographicalInformationImpl.decodeTypeOfShape(geodeticBuffer.readByte() & 0x0FF),
									GeographicalInformationImpl.decodeLatitude(geodeticBuffer),
									GeographicalInformationImpl.decodeLongitude(geodeticBuffer),
									GeographicalInformationImpl.decodeUncertainty(geodeticBuffer.readByte() & 0x0FF),
									geodeticBuffer.readByte() & 0x0FF);
						} catch (ASNParsingException e) {
							throw new CAPException(e.getMessage(), e);
						}

						LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra,
								ggi, sgsn, lsa, null, true, gdi, true, 13);
						GPRSEventSpecificInformationImpl gprsEventSpecificInformation = new GPRSEventSpecificInformationImpl(
								locationInformationGPRS);
						PDPIDImpl pdpID = new PDPIDImpl(1);
						dlg.addEventReportGPRSRequest(gprsEventType, miscGPRSInfo, gprsEventSpecificInformation, pdpID);

						super.handleSent(EventType.EventReportGPRSRequest, null);
						dlg.send(dummyCallback);
						dialogStep = 0;
						break;
					case 2: // after ConnectRequest
						ElapsedTimeImpl elapsedTime = new ElapsedTimeImpl(new Integer(5320));
						ChargingResultImpl chargingResult = new ChargingResultImpl(elapsedTime);
						boolean active = true;
						dlg.addApplyChargingReportGPRSRequest(chargingResult, null, active, null, null);
						super.handleSent(EventType.ApplyChargingReportGPRSRequest, null);
						dlg.send(dummyCallback);
						dialogStep = 0;

						break;

					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;
			private int applyChargingReportGPRSResponse;
			private int eventReportGPRSResponse;

			@Override
			public void onInitialDpGprsRequest(InitialDpGprsRequest ind) {
				super.onInitialDpGprsRequest(ind);
				assertTrue(Client.checkTestInitialDpGprsRequest(ind));
				dialogStep = 1;
			}

			@Override
			public void onEventReportGPRSRequest(EventReportGPRSRequest ind) {
				super.onEventReportGPRSRequest(ind);
				assertEquals(ind.getGPRSEventType(), GPRSEventType.attachChangeOfPosition);
				assertNotNull(ind.getMiscGPRSInfo().getMessageType());
				assertNull(ind.getMiscGPRSInfo().getDpAssignment());
				assertEquals(ind.getMiscGPRSInfo().getMessageType(), MiscCallInfoMessageType.notification);
				dialogStep = 2;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				eventReportGPRSResponse = ind.getInvokeId();

			}

			@Override
			public void onApplyChargingReportGPRSRequest(ApplyChargingReportGPRSRequest ind) {
				super.onApplyChargingReportGPRSRequest(ind);

				assertEquals(ind.getChargingResult().getElapsedTime().getTimeGPRSIfNoTariffSwitch().intValue(), 5320);
				assertNull(ind.getChargingResult().getTransferredVolume());
				assertNull(ind.getQualityOfService());
				assertTrue(ind.getActive());
				assertNull(ind.getPDPID());
				assertNull(ind.getChargingRollOver());

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				applyChargingReportGPRSResponse = ind.getInvokeId();

				dialogStep = 3;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp

						RequestReportGPRSEventRequest rrc = this.getRequestReportGPRSEventRequest();
						dlg.addRequestReportGPRSEventRequest(rrc.getGPRSEvent(), rrc.getPDPID());
						super.handleSent(EventType.RequestReportGPRSEventRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						byte[] bFreeFormatData = new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };

						FreeFormatDataGprsImpl freeFormatData = new FreeFormatDataGprsImpl(
								Unpooled.wrappedBuffer(bFreeFormatData));
						PDPIDImpl pdpID = new PDPIDImpl(2);
						FCIBCCCAMELSequence1GprsImpl fcIBCCCAMELsequence1 = new FCIBCCCAMELSequence1GprsImpl(
								freeFormatData, pdpID, AppendFreeFormatData.append);

						CAMELFCIGPRSBillingChargingCharacteristicsImpl fciGPRSBillingChargingCharacteristics = new CAMELFCIGPRSBillingChargingCharacteristicsImpl(
								fcIBCCCAMELsequence1);

						dlg.addFurnishChargingInformationGPRSRequest(fciGPRSBillingChargingCharacteristics);
						dlg.send(dummyCallback);
						super.handleSent(EventType.FurnishChargingInformationGPRSRequest, null);

						dialogStep = 0;

						break;

					case 2: // after eventReportGPRSRequest
						dlg.addEventReportGPRSResponse(eventReportGPRSResponse);
						super.handleSent(EventType.EventReportGPRSResponse, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						dlg.addResetTimerGPRSRequest(TimerID.tssf, 12);
						super.handleSent(EventType.ResetTimerGPRSRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(200L);

						Integer tariffSwitchInterval = new Integer(24);
						PDPIDImpl pdpIDApplyCharging = new PDPIDImpl(2);
						dlg.addApplyChargingGPRSRequest(chargingCharacteristics, tariffSwitchInterval,
								pdpIDApplyCharging);
						super.handleSent(EventType.ApplyChargingGPRSRequest, null);

						AccessPointNameImpl accessPointName = new AccessPointNameImpl(
								Unpooled.wrappedBuffer(new byte[] { 52, 20, 30 }));
						PDPIDImpl pdpIDConnectRequest = new PDPIDImpl(2);
						dlg.addConnectGPRSRequest(accessPointName, pdpIDConnectRequest);
						super.handleSent(EventType.ConnectGPRSRequest, null);
						dlg.send(dummyCallback);
						dialogStep = 0;
						break;
					case 3:
						dlg.addApplyChargingReportGPRSResponse(applyChargingReportGPRSResponse);
						super.handleSent(EventType.ApplyChargingReportGPRSResponse, null);
						dlg.close(false, dummyCallback);
						dialogStep = 0;
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpGprsRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RequestReportGPRSEventRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.FurnishChargingInformationGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportGPRSResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ResetTimerGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ApplyChargingGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ConnectGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ApplyChargingReportGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ApplyChargingReportGPRSResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpGprsRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RequestReportGPRSEventRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.FurnishChargingInformationGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportGPRSResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ResetTimerGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ApplyChargingGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ConnectGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ApplyChargingReportGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ApplyChargingReportGPRSResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.suppressInvokeTimeout();
		client.sendInitialDpGprs(CAPApplicationContext.CapV3_gprsSSF_gsmSCF);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * GPSR messageflow 2 ACN=cap3-gsmscf-gprsssf
	 *
	 * <pre>
	 * TC-BEGIN + activityTestGPRSSRequest + destinationReference=1001 + originationReference=2001 
	 * TC-CONTINUE + activityTestGPRSSResponse + destinationReference=2001 + originationReference=1001 
	 * TC-CONTINUE + furnishChargingInformationGPRSRequest + continueGPRSRequest 
	 * TC-CONTINUE + eventReportGPRSRequest 
	 * TC-END + eventReportGPRSResponse + cancelGPRSRequest
	 * </pre>
	 */
	@Test
	public void testGPRS2() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;
			private int eventReportGPRSResponse;

			@Override
			public void onActivityTestGPRSResponse(ActivityTestGPRSResponse ind) {
				super.onActivityTestGPRSResponse(ind);
				dialogStep = 1;
			}

			@Override
			public void onEventReportGPRSRequest(EventReportGPRSRequest ind) {
				super.onEventReportGPRSRequest(ind);
				assertEquals(ind.getGPRSEventType(), GPRSEventType.attachChangeOfPosition);
				assertNotNull(ind.getMiscGPRSInfo().getMessageType());
				assertNull(ind.getMiscGPRSInfo().getDpAssignment());
				assertEquals(ind.getMiscGPRSInfo().getMessageType(), MiscCallInfoMessageType.notification);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				eventReportGPRSResponse = ind.getInvokeId();
				dialogStep = 2;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

				try {
					switch (dialogStep) {
					case 1:
						byte[] bFreeFormatData = new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };
						FreeFormatDataGprsImpl freeFormatData = new FreeFormatDataGprsImpl(
								Unpooled.wrappedBuffer(bFreeFormatData));
						PDPIDImpl pdpID = new PDPIDImpl(2);
						FCIBCCCAMELSequence1GprsImpl fcIBCCCAMELsequence1 = new FCIBCCCAMELSequence1GprsImpl(
								freeFormatData, pdpID, AppendFreeFormatData.append);
						CAMELFCIGPRSBillingChargingCharacteristicsImpl fciGPRSBillingChargingCharacteristics = new CAMELFCIGPRSBillingChargingCharacteristicsImpl(
								fcIBCCCAMELsequence1);

						dlg.addFurnishChargingInformationGPRSRequest(fciGPRSBillingChargingCharacteristics);
						super.handleSent(EventType.FurnishChargingInformationGPRSRequest, null);

						dlg.addContinueGPRSRequest(pdpID);
						super.handleSent(EventType.ContinueGPRSRequest, null);

						dlg.send(dummyCallback);

						dialogStep = 0;
						break;
					case 2:
						dlg.addEventReportGPRSResponse(eventReportGPRSResponse);
						super.handleSent(EventType.EventReportGPRSResponse, null);

						PDPIDImpl pdpIDCancelGPRS = new PDPIDImpl(2);
						dlg.addCancelGPRSRequest(pdpIDCancelGPRS);
						super.handleSent(EventType.CancelGPRSRequest, null);
						dlg.close(false, dummyCallback);
						dialogStep = 0;

						break;

					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;
			private int activityTestGPRSRequest;

			@Override
			public void onActivityTestGPRSRequest(ActivityTestGPRSRequest ind) {
				super.onActivityTestGPRSRequest(ind);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				activityTestGPRSRequest = ind.getInvokeId();
				dialogStep = 1;
			}

			@Override
			public void onFurnishChargingInformationGPRSRequest(FurnishChargingInformationGPRSRequest ind) {
				super.onFurnishChargingInformationGPRSRequest(ind);

				byte[] bFreeFormatData = new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };
				assertTrue(ByteBufUtil.equals(ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1()
						.getFreeFormatData().getValue(), Unpooled.wrappedBuffer(bFreeFormatData)));
				assertEquals(
						ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getPDPID().getId(), 2);
				assertEquals(ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1()
						.getAppendFreeFormatData(), AppendFreeFormatData.append);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onContinueGPRSRequest(ContinueGPRSRequest ind) {
				super.onContinueGPRSRequest(ind);

				assertEquals(ind.getPDPID().getId(), 2);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 2;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after ActivityTestGPRS
						dlg.addActivityTestGPRSResponse(activityTestGPRSRequest);
						dlg.send(dummyCallback);
						super.handleSent(EventType.ActivityTestGPRSResponse, null);

						dialogStep = 0;

						break;

					case 2:
						GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
						MiscCallInfoImpl miscGPRSInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.notification,
								null);
						LAIFixedLengthImpl lai;
						try {
							lai = new LAIFixedLengthImpl(250, 1, 4444);
						} catch (ASNParsingException e) {
							throw new CAPException(e.getMessage(), e);
						}
						CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
						RAIdentityImpl ra = new RAIdentityImpl(
								Unpooled.wrappedBuffer(new byte[] { 11, 12, 13, 14, 15, 16 }));

						GeographicalInformationImpl ggi = null;
						try {
							ByteBuf geoBuffer = Unpooled.wrappedBuffer(new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 });
							ggi = new GeographicalInformationImpl(
									GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF),
									GeographicalInformationImpl.decodeLatitude(geoBuffer),
									GeographicalInformationImpl.decodeLongitude(geoBuffer),
									GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));
						} catch (ASNParsingException e) {
							throw new CAPException(e.getMessage(), e);
						}

						ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number,
								NumberingPlan.ISDN, "654321");
						LSAIdentityImpl lsa = new LSAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 91, 92, 93 }));

						GeodeticInformationImpl gdi = null;
						try {
							ByteBuf geodeticBuffer = Unpooled
									.wrappedBuffer(new byte[] { 1, 16, 3, 4, 5, 6, 7, 8, 9, 10 });
							gdi = new GeodeticInformationImpl(geodeticBuffer.readByte() & 0x0FF,
									GeographicalInformationImpl.decodeTypeOfShape(geodeticBuffer.readByte() & 0x0FF),
									GeographicalInformationImpl.decodeLatitude(geodeticBuffer),
									GeographicalInformationImpl.decodeLongitude(geodeticBuffer),
									GeographicalInformationImpl.decodeUncertainty(geodeticBuffer.readByte() & 0x0FF),
									geodeticBuffer.readByte() & 0x0FF);
						} catch (ASNParsingException e) {
							throw new CAPException(e.getMessage(), e);
						}

						LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra,
								ggi, sgsn, lsa, null, true, gdi, true, 13);
						GPRSEventSpecificInformationImpl gprsEventSpecificInformation = new GPRSEventSpecificInformationImpl(
								locationInformationGPRS);
						PDPIDImpl pdpID = new PDPIDImpl(1);
						dlg.addEventReportGPRSRequest(gprsEventType, miscGPRSInfo, gprsEventSpecificInformation, pdpID);

						super.handleSent(EventType.EventReportGPRSRequest, null);
						dlg.send(dummyCallback);
						dialogStep = 0;
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ActivityTestGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestGPRSResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.FurnishChargingInformationGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportGPRSResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestGPRSResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.FurnishChargingInformationGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportGPRSResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.suppressInvokeTimeout();
		client.sendActivityTestGPRSRequest(CAPApplicationContext.CapV3_gsmSCF_gprsSSF);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * GPSR messageflow 3 ACN=cap3-gprssf-scf
	 *
	 * <pre>
	 * TC-BEGIN + eventReportGPRSRequest + destinationReference=2001 + originationReference=1001 
	 * TC-END + eventReportGPRSResponse + connectGPRSRequest + sendChargingInformationGPRSRequest + destinationReference=1001 + originationReference=2001
	 * </pre>
	 */
	@Test
	public void testGPRS3() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onEventReportGPRSResponse(EventReportGPRSResponse ind) {
				super.onEventReportGPRSResponse(ind);
			}

			@Override
			public void onSendChargingInformationGPRSRequest(SendChargingInformationGPRSRequest ind) {
				super.onSendChargingInformationGPRSRequest(ind);

				assertEquals((int) ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCInitial().getE1(),
						1);
				assertNull(ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent()
						.getCAI_GSM0224().getE1());
				assertNull(ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent()
						.getCAI_GSM0224().getE2());
				assertNull(ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent()
						.getCAI_GSM0224().getE3());
				assertNull(ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent()
						.getCAI_GSM0224().getE6());
				assertNull(ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent()
						.getCAI_GSM0224().getE7());
				assertEquals((int) ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent()
						.getTariffSwitchInterval(), 222);
				assertEquals(ind.getSCIGPRSBillingChargingCharacteristics().getPDPID().getId(), 1);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onConnectGPRSRequest(ConnectGPRSRequest ind) {
				super.onConnectGPRSRequest(ind);
				assertTrue(ByteBufUtil.equals(ind.getAccessPointName().getValue(),
						Unpooled.wrappedBuffer(new byte[] { 52, 20, 30 })));
				assertEquals(ind.getPDPID().getId(), 2);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;
			private int eventReportGPRSResponse;

			@Override
			public void onEventReportGPRSRequest(EventReportGPRSRequest ind) {
				super.onEventReportGPRSRequest(ind);
				assertEquals(ind.getGPRSEventType(), GPRSEventType.attachChangeOfPosition);
				assertNotNull(ind.getMiscGPRSInfo().getMessageType());
				assertNull(ind.getMiscGPRSInfo().getDpAssignment());
				assertEquals(ind.getMiscGPRSInfo().getMessageType(), MiscCallInfoMessageType.notification);
				dialogStep = 1;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				eventReportGPRSResponse = ind.getInvokeId();

			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

				try {
					switch (dialogStep) {

					case 1:
						dlg.addEventReportGPRSResponse(eventReportGPRSResponse);
						super.handleSent(EventType.EventReportGPRSResponse, null);

						AccessPointNameImpl accessPointName = new AccessPointNameImpl(
								Unpooled.wrappedBuffer(new byte[] { 52, 20, 30 }));
						PDPIDImpl pdpIDConnectRequest = new PDPIDImpl(2);
						dlg.addConnectGPRSRequest(accessPointName, pdpIDConnectRequest);
						super.handleSent(EventType.ConnectGPRSRequest, null);

						CAI_GSM0224Impl aocInitial = new CAI_GSM0224Impl(1, 2, 3, 4, 5, 6, 7);
						CAI_GSM0224Impl cai_GSM0224 = new CAI_GSM0224Impl(null, null, null, 4, 5, null, null);
						AOCSubsequentImpl aocSubsequent = new AOCSubsequentImpl(cai_GSM0224, 222);
						AOCGPRSImpl aocGPRS = new AOCGPRSImpl(aocInitial, aocSubsequent);
						PDPIDImpl pdpID = new PDPIDImpl(1);
						CAMELSCIGPRSBillingChargingCharacteristicsImpl sciGPRSBillingChargingCharacteristics = new CAMELSCIGPRSBillingChargingCharacteristicsImpl(
								aocGPRS, pdpID);
						dlg.addSendChargingInformationGPRSRequest(sciGPRSBillingChargingCharacteristics);
						super.handleSent(EventType.SendChargingInformationGPRSRequest, null);

						dlg.close(false, dummyCallback);
						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();

		TestEvent te = TestEvent.createSentEvent(EventType.EventReportGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportGPRSResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ConnectGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendChargingInformationGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportGPRSResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ConnectGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SendChargingInformationGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.suppressInvokeTimeout();
		client.sendEventReportGPRSRequest(CAPApplicationContext.CapV3_gprsSSF_gsmSCF);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * GPSR messageflow 4 ACN=cap3-gsmscf-gprsssf
	 *
	 * <pre>
	 * TC-BEGIN + releaseGPRSRequest + destinationReference=1001 + originationReference=2001 
	 * TC-END + destinationReference=2001 + originationReference=1001
	 * </pre>
	 */
	@Test
	public void testGPRS4() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			private int dialogStep = 0;

			@Override
			public void onReleaseGPRSRequest(ReleaseGPRSRequest ind) {
				super.onReleaseGPRSRequest(ind);
				assertEquals(ind.getGPRSCause().getData(), 5);
				assertEquals(ind.getPDPID().getId(), 2);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 1;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

				try {
					switch (dialogStep) {
					case 1:
						dlg.close(false, dummyCallback);
						dialogStep = 0;
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ReleaseGPRSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReleaseGPRSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.suppressInvokeTimeout();
		client.sendReleaseGPRSRequest(CAPApplicationContext.CapV3_gsmSCF_gprsSSF);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	private byte[] freeFD = new byte[] { 1, 2, 3, 4, 5 };

	/**
	 * SMS test messageflow 1 ACN=CapV3_cap3_sms
	 * 
	 * <pre>
	 * -> initialDPSMSRequest
	 * <- resetTimerSMS + requestReportSMSEventRequest 
	 * -> eventReportSMSRequest 
	 * <- furnishChargingInformationSMS 
	 * <- connectSMS (TC-END)
	 * </pre>
	 */
	@Test
	public void testSMS1() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			private int dialogStep = 0;

			@Override
			public void onResetTimerSMSRequest(ResetTimerSMSRequest ind) {
				super.onResetTimerSMSRequest(ind);

				assertEquals(ind.getTimerID(), TimerID.tssf);
				assertEquals(ind.getTimerValue(), 3000);
				assertNull(ind.getExtensions());

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 1;
			}

			@Override
			public void onRequestReportSMSEventRequest(RequestReportSMSEventRequest ind) {
				super.onRequestReportSMSEventRequest(ind);

				assertEquals(ind.getSMSEvents().size(), 2);
				SMSEvent smsEvent = ind.getSMSEvents().get(0);
				assertEquals(smsEvent.getEventTypeSMS(), EventTypeSMS.tSmsDelivery);
				assertEquals(smsEvent.getMonitorMode(), MonitorMode.transparent);
				assertNull(ind.getExtensions());
				smsEvent = ind.getSMSEvents().get(1);
				assertEquals(smsEvent.getEventTypeSMS(), EventTypeSMS.oSmsFailure);
				assertEquals(smsEvent.getMonitorMode(), MonitorMode.notifyAndContinue);
				assertNull(ind.getExtensions());

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 1;
			}

			@Override
			public void onFurnishChargingInformationSMSRequest(FurnishChargingInformationSMSRequest ind) {
				super.onFurnishChargingInformationSMSRequest(ind);

				assertTrue(ByteBufUtil.equals(ind.getFCIBCCCAMELsequence1().getFreeFormatData().getValue(),
						Unpooled.wrappedBuffer(freeFD)));

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 2;
			}

			@Override
			public void onConnectSMSRequest(ConnectSMSRequest ind) {
				super.onConnectSMSRequest(ind);

				assertEquals(ind.getCallingPartysNumber().getAddressNature(), AddressNature.reserved);
				assertEquals(ind.getCallingPartysNumber().getNumberingPlan(), NumberingPlan.ISDN);
				assertEquals(ind.getCallingPartysNumber().getAddress(), "Drosd");
				assertEquals(ind.getDestinationSubscriberNumber().getAddress(), "1111144444");
				assertEquals(ind.getSMSCAddress().getAddress(), "1111155555");

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 2;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogSms dlg = (CAPDialogSms) capDialog;

				try {
					switch (dialogStep) {
					case 1:
						OSmsFailureSpecificInfo oSmsFailureSpecificInfo = this.capParameterFactory
								.createOSmsFailureSpecificInfo(MOSMSCause.releaseFromRadioInterface);
						EventSpecificInformationSMS eventSpecificInformationSMS = this.capParameterFactory
								.createEventSpecificInformationSMS(oSmsFailureSpecificInfo);
						dlg.addEventReportSMSRequest(EventTypeSMS.oSmsFailure, eventSpecificInformationSMS, null, null);
						super.handleSent(EventType.EventReportSMSRequest, null);

						dlg.send(dummyCallback);
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			private int dialogStep = 0;

			@Override
			public void onInitialDPSMSRequest(InitialDPSMSRequest ind) {
				super.onInitialDPSMSRequest(ind);

				assertEquals(ind.getServiceKey(), 15);
				assertEquals(ind.getDestinationSubscriberNumber().getAddress(), "123678");
				assertEquals(ind.getDestinationSubscriberNumber().getNumberingPlan(), NumberingPlan.ISDN);
				assertEquals(ind.getDestinationSubscriberNumber().getAddressNature(),
						AddressNature.international_number);
				assertEquals(ind.getCallingPartyNumber().getAddress(), "123999");
				assertEquals(ind.getImsi().getData(), "12345678901234");
				assertEquals(ind.getEventTypeSMS(), EventTypeSMS.smsDeliveryRequested);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 1;
			}

			@Override
			public void onEventReportSMSRequest(EventReportSMSRequest ind) {
				super.onEventReportSMSRequest(ind);

				assertEquals(ind.getEventTypeSMS(), EventTypeSMS.oSmsFailure);
				assertEquals(ind.getEventSpecificInformationSMS().getOSmsFailureSpecificInfo().getFailureCause(),
						MOSMSCause.releaseFromRadioInterface);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 2;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogSms dlg = (CAPDialogSms) capDialog;

				try {
					switch (dialogStep) {
					case 1:
						dlg.addResetTimerSMSRequest(TimerID.tssf, 3000, null);
						super.handleSent(EventType.ResetTimerSMSRequest, null);

						List<SMSEvent> smsEvents = new ArrayList<SMSEvent>();
						SMSEvent smsEvent = this.capParameterFactory.createSMSEvent(EventTypeSMS.tSmsDelivery,
								MonitorMode.transparent);
						smsEvents.add(smsEvent);
						smsEvent = this.capParameterFactory.createSMSEvent(EventTypeSMS.oSmsFailure,
								MonitorMode.notifyAndContinue);
						smsEvents.add(smsEvent);
						dlg.addRequestReportSMSEventRequest(smsEvents, null);
						super.handleSent(EventType.RequestReportSMSEventRequest, null);

						dlg.send(dummyCallback);
						break;

					case 2:
						FreeFormatDataSMS freeFormatData = this.capParameterFactory
								.createFreeFormatDataSMS(Unpooled.wrappedBuffer(freeFD));
						FCIBCCCAMELSequence1SMS fciBCCCAMELsequence1 = this.capParameterFactory
								.createFCIBCCCAMELsequence1(freeFormatData, null);
						dlg.addFurnishChargingInformationSMSRequest(fciBCCCAMELsequence1);
						super.handleSent(EventType.FurnishChargingInformationSMSRequest, null);

						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (Exception ex) {

						}

						SMSAddressString callingPartysNumber = this.capParameterFactory
								.createSMSAddressString(AddressNature.reserved, NumberingPlan.ISDN, "Drosd");
						CalledPartyBCDNumber destinationSubscriberNumber = this.capParameterFactory
								.createCalledPartyBCDNumber(AddressNature.international_number, NumberingPlan.ISDN,
										"1111144444");
						ISDNAddressString smscAddress = this.capParameterFactory.createISDNAddressString(
								AddressNature.international_number, NumberingPlan.ISDN, "1111155555");
						dlg.addConnectSMSRequest(callingPartysNumber, destinationSubscriberNumber, smscAddress, null);
						super.handleSent(EventType.ConnectSMSRequest, null);

						dlg.close(false, dummyCallback);
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDPSMSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ResetTimerSMSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RequestReportSMSEventRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportSMSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.FurnishChargingInformationSMSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ConnectSMSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDPSMSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ResetTimerSMSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RequestReportSMSEventRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportSMSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.FurnishChargingInformationSMSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ConnectSMSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.suppressInvokeTimeout();
		client.sendInitialDpSmsRequest(CAPApplicationContext.CapV3_cap3_sms);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * SMS test messageflow 2 ACN=CapV4_cap4_sms
	 * 
	 * <pre>
	 * -> initialDPSMSRequest
	 * <- continueSMS
	 * </pre>
	 */
	@Test
	public void testSMS2() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onContinueSMSRequest(ContinueSMSRequest ind) {
				super.onContinueSMSRequest(ind);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			private int dialogStep = 0;

			@Override
			public void onInitialDPSMSRequest(InitialDPSMSRequest ind) {
				super.onInitialDPSMSRequest(ind);

				assertEquals(ind.getServiceKey(), 15);
				assertEquals(ind.getDestinationSubscriberNumber().getAddress(), "123678");
				assertEquals(ind.getDestinationSubscriberNumber().getNumberingPlan(), NumberingPlan.ISDN);
				assertEquals(ind.getDestinationSubscriberNumber().getAddressNature(),
						AddressNature.international_number);
				assertEquals(ind.getCallingPartyNumber().getAddress(), "123999");
				assertEquals(ind.getImsi().getData(), "12345678901234");
				assertEquals(ind.getEventTypeSMS(), EventTypeSMS.smsDeliveryRequested);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 1;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogSms dlg = (CAPDialogSms) capDialog;

				try {
					switch (dialogStep) {
					case 1:
						dlg.addContinueSMSRequest();
						super.handleSent(EventType.ContinueSMSRequest, null);

						dlg.close(false, dummyCallback);
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDPSMSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueSMSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDPSMSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueSMSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.suppressInvokeTimeout();
		client.sendInitialDpSmsRequest(CAPApplicationContext.CapV4_cap4_sms);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * SMS test messageflow 3 ACN=CapV4_cap4_sms
	 * 
	 * <pre>
	 * -> initialDPSMSRequest
	 * <- releaseSMS
	 * </pre>
	 */
	@Test
	public void testSMS3() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onReleaseSMSRequest(ReleaseSMSRequest ind) {
				super.onReleaseSMSRequest(ind);

				assertEquals(ind.getRPCause().getData(), 8);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			private int dialogStep = 0;

			@Override
			public void onInitialDPSMSRequest(InitialDPSMSRequest ind) {
				super.onInitialDPSMSRequest(ind);

				assertEquals(ind.getServiceKey(), 15);
				assertEquals(ind.getDestinationSubscriberNumber().getAddress(), "123678");
				assertEquals(ind.getDestinationSubscriberNumber().getNumberingPlan(), NumberingPlan.ISDN);
				assertEquals(ind.getDestinationSubscriberNumber().getAddressNature(),
						AddressNature.international_number);
				assertEquals(ind.getCallingPartyNumber().getAddress(), "123999");
				assertEquals(ind.getImsi().getData(), "12345678901234");
				assertEquals(ind.getEventTypeSMS(), EventTypeSMS.smsDeliveryRequested);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
				dialogStep = 1;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogSms dlg = (CAPDialogSms) capDialog;

				try {
					switch (dialogStep) {
					case 1:
						RPCause rpCause = this.capParameterFactory.createRPCause(8);
						dlg.addReleaseSMSRequest(rpCause);
						super.handleSent(EventType.ReleaseSMSRequest, null);

						dlg.close(false, dummyCallback);
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDPSMSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReleaseSMSRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDPSMSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ReleaseSMSRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.suppressInvokeTimeout();
		client.sendInitialDpSmsRequest(CAPApplicationContext.CapV4_cap4_sms);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * ACN = capscf-ssfGenericAC V4
	 *
	 * <pre>
	 * TC-BEGIN + InitiateCallAttemptRequest
	 * TC-CONTINUE + InitiateCallAttemptResponse
	 * TC-CONTINUE + SplitLegRequest 
	 * TC-CONTINUE + SplitLegResponse 
	 * TC-CONTINUE + MoveLegRequest 
	 * TC-CONTINUE + MoveLegResponse
	 * TC-CONTINUE + DisconnectLegRequest
	 * TC-CONTINUE + DisconnectLegResponse
	 * TC-END + DisconnectForwardConnectionWithArgumentRequest
	 * </pre>
	 */
	@Test
	public void testInitiateCallAttempt() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onInitiateCallAttemptResponse(InitiateCallAttemptResponse ind) {
				super.onInitiateCallAttemptResponse(ind);

				assertTrue(ind.getSupportedCamelPhases().getPhase1Supported());
				assertTrue(ind.getSupportedCamelPhases().getPhase2Supported());
				assertTrue(ind.getSupportedCamelPhases().getPhase3Supported());
				assertFalse(ind.getSupportedCamelPhases().getPhase4Supported());

				dialogStep = 1;
			}

			@Override
			public void onSplitLegResponse(SplitLegResponse ind) {
				super.onSplitLegResponse(ind);

				dialogStep = 2;
			}

			@Override
			public void onMoveLegResponse(MoveLegResponse ind) {
				super.onMoveLegResponse(ind);

				dialogStep = 3;
			}

			@Override
			public void onDisconnectLegResponse(DisconnectLegResponse ind) {
				super.onDisconnectLegResponse(ind);

				dialogStep = 4;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitiateCallAttemptResponse
						LegID logIDToSplit = this.capParameterFactory.createLegID(LegType.leg1, null);
						dlg.addSplitLegRequest(logIDToSplit, 1, null);
						super.handleSent(EventType.SplitLegRequest, null);
						dlg.send(dummyCallback);
						break;

					case 2: // after SplitLegResponse
						LegID logIDToMove = this.capParameterFactory.createLegID(LegType.leg1, null);
						dlg.addMoveLegRequest(logIDToMove, null);
						super.handleSent(EventType.MoveLegRequest, null);
						dlg.send(dummyCallback);
						break;

					case 3: // after MoveLegResponse
						LegID logToBeReleased = this.capParameterFactory.createLegID(LegType.leg2, null);
						CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
						causeIndicators.setCauseValue(3);
						CauseIsup causeCap = this.capParameterFactory.createCause(causeIndicators);
						dlg.addDisconnectLegRequest(logToBeReleased, causeCap, null);
						super.handleSent(EventType.DisconnectLegRequest, null);
						dlg.send(dummyCallback);
						break;

					case 4: // after MoveLegResponse
						dlg.addDisconnectForwardConnectionWithArgumentRequest(15, null);
						super.handleSent(EventType.DisconnectForwardConnectionWithArgumentRequest, null);
						dlg.close(false, dummyCallback);
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;
			private int invokeIdInitiateCallAttempt;
			private int invokeIdSplitLeg;
			private int invokeIdMoveLeg;
			private int invokeIdDisconnectLeg;

			@Override
			public void onInitiateCallAttemptRequest(InitiateCallAttemptRequest ind) {
				super.onInitiateCallAttemptRequest(ind);

				invokeIdInitiateCallAttempt = ind.getInvokeId();
				try {
					assertEquals(ind.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
					assertEquals(ind.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
							.getAddress(), "1113330");
				} catch (ASNParsingException e) {
					e.printStackTrace();
				}

				dialogStep = 1;
			}

			@Override
			public void onSplitLegRequest(SplitLegRequest ind) {
				super.onSplitLegRequest(ind);

				invokeIdSplitLeg = ind.getInvokeId();
				assertEquals(ind.getLegToBeSplit().getReceivingSideID(), LegType.leg1);

				dialogStep = 2;
			}

			@Override
			public void onMoveLegRequest(MoveLegRequest ind) {
				super.onMoveLegRequest(ind);

				invokeIdMoveLeg = ind.getInvokeId();
				assertEquals(ind.getLegIDToMove().getReceivingSideID(), LegType.leg1);

				dialogStep = 3;
			}

			@Override
			public void onDisconnectLegRequest(DisconnectLegRequest ind) {
				super.onDisconnectLegRequest(ind);

				try {
					invokeIdDisconnectLeg = ind.getInvokeId();
					assertEquals(ind.getLegToBeReleased().getReceivingSideID(), LegType.leg2);
					assertEquals(ind.getReleaseCause().getCauseIndicators().getCauseValue(), 3);
				} catch (ASNParsingException e) {
					e.printStackTrace();
				}

				dialogStep = 4;
			}

			@Override
			public void onDisconnectForwardConnectionWithArgumentRequest(
					DisconnectForwardConnectionWithArgumentRequest ind) {
				super.onDisconnectForwardConnectionWithArgumentRequest(ind);

				invokeIdDisconnectLeg = ind.getInvokeId();
				assertEquals((int) ind.getCallSegmentID(), 15);

				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());

				dialogStep = 5;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitiateCallAttempt
						SupportedCamelPhases supportedCamelPhases = this.capParameterFactory
								.createSupportedCamelPhases(true, true, true, false);
						dlg.addInitiateCallAttemptResponse(invokeIdInitiateCallAttempt, supportedCamelPhases, null,
								null, false);
						super.handleSent(EventType.InitiateCallAttemptResponse, null);
						dlg.send(dummyCallback);

						dialogStep = 0;
						break;

					case 2: // after SplitLegRequest
						dlg.addSplitLegResponse(invokeIdSplitLeg);
						super.handleSent(EventType.SplitLegResponse, null);
						dlg.send(dummyCallback);

						dialogStep = 0;
						break;

					case 3: // after MoveLegRequest
						dlg.addMoveLegResponse(invokeIdMoveLeg);
						super.handleSent(EventType.MoveLegResponse, null);
						dlg.send(dummyCallback);

						dialogStep = 0;
						break;

					case 4: // after DisconnectLegRequest
						dlg.addDisconnectLegResponse(invokeIdDisconnectLeg);
						super.handleSent(EventType.DisconnectLegResponse, null);
						dlg.send(dummyCallback);

						dialogStep = 0;
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitiateCallAttemptRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitiateCallAttemptResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SplitLegRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SplitLegResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.MoveLegRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.MoveLegResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DisconnectLegRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DisconnectLegResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DisconnectForwardConnectionWithArgumentRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitiateCallAttemptRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.InitiateCallAttemptResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SplitLegRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SplitLegResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.MoveLegRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.MoveLegResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DisconnectLegRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DisconnectLegResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DisconnectForwardConnectionWithArgumentRequest, null, count++,
				stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitiateCallAttemptRequest();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * ACN = capscf-ssfGenericAC V4
	 *
	 * <pre>
	 * TC-BEGIN + InitiateDPRequest
	 * TC-CONTINUE + ContinueWithArgumentRequest
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testContinueWithArgument() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind) {
				super.onContinueWithArgumentRequest(ind);

				assertEquals(ind.getAlertingPattern().getAlertingLevel(), AlertingLevel.Level1);
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());

				dialogStep = 1;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after ContinueWithArgumentRequest
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp
						AlertingPatternImpl ap = new AlertingPatternImpl(AlertingLevel.Level1);
						dlg.addContinueWithArgumentRequest(ap, null, null, null, null, null, false, null, null, false,
								null, false, false, null);
						super.handleSent(EventType.ContinueWithArgumentRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueWithArgumentRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueWithArgumentRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * ACN = capscf-ssfGenericAC V4
	 *
	 * <pre>
	 * TC-BEGIN + InitiateDPRequest
	 * TC-CONTINUE + callGap
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testCallGap() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onCallGapRequest(CallGapRequest ind) {
				super.onCallGapRequest(ind);

				try {
					assertEquals(ind.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService()
							.getCalledAddressNumber().getGenericNumber().getAddress(), "501090500");
				} catch (ASNParsingException e) {
					fail("CAPException in onCallGapRequest: " + e);
				}
				assertEquals(ind.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(),
						100);
				assertEquals(ind.getGapIndicators().getDuration(), 60);
				assertEquals(ind.getGapIndicators().getGapInterval(), -1);

				dialogStep = 1;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after onCallGapRequest
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				dialogStep = 1;
				ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp
						GenericNumber genericNumber = capProvider.getISUPParameterFactory().createGenericNumber();
						genericNumber.setAddress("501090500");
						DigitsIsup digits = capProvider.getCAPParameterFactory()
								.createDigits_GenericNumber(genericNumber);

						CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits,
								100);
						BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
						GapCriteriaImpl gapCriteria = new GapCriteriaImpl(basicGapCriteria);
						GapIndicatorsImpl gapIndicators = new GapIndicatorsImpl(60, -1);

						dlg.addCallGapRequest(gapCriteria, gapIndicators, null, null, null);
						super.handleSent(EventType.CallGapRequest, null);
						dlg.send(dummyCallback);

//                            GenericNumber genericNumber = capProvider.getISUPParameterFactory().createGenericNumber();
//                            genericNumber.setAddress("501090500");
//                            Digits digits = capProvider.getCAPParameterFactory().createDigits_GenericNumber(genericNumber);
//
//                            CalledAddressAndService calledAddressAndService = new CalledAddressAndServiceImpl(digits, 100);
//                            GapOnService gapOnService = new GapOnServiceImpl(888);
////                            BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
////                            BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(digits);
//                            BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(gapOnService);
//                            ScfID scfId = new ScfIDImpl(new byte[] { 12, 32, 23, 56 });
//                            CompoundCriteria compoundCriteria = new CompoundCriteriaImpl(basicGapCriteria, scfId);
//                            GapCriteria gapCriteria = new GapCriteriaImpl(compoundCriteria);
//                            GapIndicators gapIndicators = new GapIndicatorsImpl(60, -1);
//
//                            MessageID messageID = new MessageIDImpl(11);
//                            InbandInfo inbandInfo = new InbandInfoImpl(messageID, 1, 2, 3);
//                            InformationToSend informationToSend = new InformationToSendImpl(inbandInfo);
//                            GapTreatment gapTreatment = new GapTreatmentImpl(informationToSend);
//
//                            dlg.addCallGapRequest(gapCriteria, gapIndicators, ControlType.sCPOverloaded, gapTreatment, null);
//                            super.handleSent(EventType.CallGapRequest, null);
//                            dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to send Response CallGapRequests", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;

		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CallGapRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CallGapRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

//        this.saveTrafficInFile();

		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}
}
