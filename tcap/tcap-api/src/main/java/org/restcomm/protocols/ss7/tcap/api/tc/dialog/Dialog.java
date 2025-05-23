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

package org.restcomm.protocols.ss7.tcap.api.tc.dialog;

import java.io.Externalizable;
import java.io.Serializable;

import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCListener;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * Interface for class representing Dialog/Transaction.
 *
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface Dialog extends Serializable {

    /**
     * returns this dialog ID. It MUST be unique at any given time in local stack.
     *
     * @return
     */
    Long getLocalDialogId();

    /**
     * return the remote Dialog Id. This will be null if Dialog is locally originated and not confirmed yet
     *
     * @return
     */
    Long getRemoteDialogId();

    /**
     * Gets local sccp address
     *
     * @return
     */
    SccpAddress getLocalAddress();

    /**
     * Sets local Sccp Address.
     *
     * @param localAddress
     */
    void setLocalAddress(SccpAddress localAddress);

    /**
     * Gets remote sccp address
     *
     * @return
     */
    SccpAddress getRemoteAddress();

    /**
     * Sets remote Sccp Address
     *
     * @param remoteAddress
     */
    void setRemoteAddress(SccpAddress remoteAddress);

    /**
     * Gets ssn dialog value
     *
     * @return
     */
    int getLocalSsn();

    /**
     * Last sent/received ACN
     *
     * @return the acn
     */
    ApplicationContextName getApplicationContextName();

    /**
     * Last sent/received UI
     *
     * @return the ui
     */
    UserInformation getUserInformation();

    /**
     * returns new, unique for this dialog, invocation id to be used in TC_INVOKE. If there is no free invoke id, it returns
     * null. Invoke ID is freed once operation using it is canceled, timeouts or simply returns final value.
     *
     * @return
     */
    Integer getNewInvokeId() throws TCAPException;

    /**
     * @return NetworkId to which virtual network Dialog belongs to
     */
    int getNetworkId();

    /**
     * @param networkId
     *            NetworkId to which virtual network Dialog belongs to
     */
    void setNetworkId(int networkId);

    /**
     * Option responsible for presence of the protocol version in
     * this dialogue portion.
     *
     * @return boolean true if protocol version must be omitted,
     * false when it should be included and null if not defined at the
     * dialog level and global option should be used.
     */
    Boolean isDoNotSendProtcolVersion();

    /**
     * Modifies option responsible for presence of the protocol version in
     * this dialogue portion.
     *
     * @param doNotSendProtocolVersion
     * boolean true if protocol version must be omitted,
     * false when it should be included and null if not defined at the
     * dialog level and global option should be used.
     */
    void setDoNotSendProtocolVersion(Boolean doNotSendProtocolVersion);

    /**
     * Cancels INVOKE pending to be sent. It is equivalent to TC-U-CANCEL.
     *
     * @return <ul>
     *         <li><b>true</b> - if operation has been success and invoke id has been return to pool of available ids.</li>
     *         <li><b>false</b> -</li>
     *         </ul>
     * @throws TCAPException - thrown if passed invoke id is wrong
     */
    boolean cancelInvocation(Integer invokeId) throws TCAPException;

    /**
     *
     * @return <ul>
     *         <li><b>true </b></li> - if dialog is established(at least one TC_CONTINUE has been sent/received.)
     *         <li><b>false</b></li> - no TC_CONTINUE sent/received
     *         </ul>
     */
    boolean isEstabilished();

    /**
     *
     * @return <ul>
     *         <li><b>true </b></li> - if dialog is structured - its created with TC_BEGIN not TC_UNI
     *         <li><b>false</b></li> - otherwise
     *         </ul>
     */
    boolean isStructured();

    // //////////////////
    // Sender methods //
    // //////////////////
    /**
     * Schedules component for sending. All components on list are queued. Components are sent once message primitive is issued.
     *
     * @param componentRequest
     * @throws TCAPSendException
     */
    Integer sendData(Integer invokeId,Integer linkedId,InvokeClass invokeClass,Long customTimeout,OperationCode operationCode,Object param,Boolean isRequest,Boolean isLastResponse) throws TCAPSendException,TCAPException;

    /**
     * Schedules component for sending. All components on list are queued. Components are sent once message primitive is issued.
     *
     * @param componentRequest
     * @throws TCAPSendException
     */
    void sendReject(Integer invokeId,Problem project) throws TCAPSendException;

    /**
     * Schedules component for sending. All components on list are queued. Components are sent once message primitive is issued.
     *
     * @param componentRequest
     * @throws TCAPSendException
     */
    void sendError(Integer invokeId,ErrorCode errorCode,Object param) throws TCAPSendException;

    /**
     * If a TCAP user will not answer to an incoming Invoke with Response, Error or Reject components it should invoke this
     * method to remove the incoming Invoke from a pending incoming Invokes list
     *
     * @param invokeId
     */
    void processInvokeWithoutAnswer(Integer invokeId);

    /**
     * Helper function to retrieve operation code from Invoke
     *
     * @param invokeId
     */
    OperationCode getOperationCodeFromInvoke(Integer invokeId);
        
    /**
     * Send initial primitive for Structured dialog.
     *
     * @param event
     * @throws TCAPSendException - thrown if dialog is in bad state, ie. Being has already been sent or dialog has been removed.
     */
    void send(TCBeginRequest event, TaskCallback<Exception> callback);

    /**
     * Sends intermediate primitive for Structured dialog.
     *
     * @param event
     * @throws TCAPSendException - thrown if dialog is in bad state, ie. Begin has not been sent or dialog has been removed.
     */
    void send(TCContinueRequest event, TaskCallback<Exception> callback);

    /**
     * Sends dialog end request.
     *
     * @param event
     * @throws TCAPSendException - thrown if dialog is in bad state, ie. Begin has not been sent or dialog has been removed.
     */
    void send(TCEndRequest event, TaskCallback<Exception> callback);

    /**
     * Sends Abort primitive with indication to user as source of termination.
     *
     * @param event
     * @throws TCAPSendException
     */
    void send(TCUserAbortRequest event, TaskCallback<Exception> callback);

    /**
     * Sends unstructured dialog primitive. After this method returns dialog is expunged from stack as its life cycle reaches
     * end.
     *
     * @param event
     * @throws TCAPSendException
     */
    void send(TCUniRequest event, TaskCallback<Exception> callback);

    /**
     * Return the TCAP message length (in bytes) that will be after encoding This value must not exceed getMaxUserDataLength()
     * value
     *
     * @param event
     * @return
     */
    int getDataLength(TCBeginRequest event) throws TCAPSendException;

    /**
     * Return the TCAP message length (in bytes) that will be after encoding This value must not exceed getMaxUserDataLength()
     * value
     *
     * @param event
     * @return
     */
    int getDataLength(TCContinueRequest event) throws TCAPSendException;

    /**
     * Return the TCAP message length (in bytes) that will be after encoding This value must not exceed getMaxUserDataLength()
     * value
     *
     * @param event
     * @return
     */
    int getDataLength(TCEndRequest event) throws TCAPSendException;

    /**
     * Return the TCAP message length (in bytes) that will be after encoding This value must not exceed getMaxUserDataLength()
     * value
     *
     * @param event
     * @return
     */
    int getDataLength(TCUniRequest event) throws TCAPSendException;
    
    /**
     * Programmer hook to release.
     */
    void release();

    /**
     * Resets timeout timer for particular operation.
     *
     * @param invokeId
     * @throws TCAPException
     */
    void resetTimer(Integer invokeId) throws TCAPException;

    /**
     * This method can be called on timeout of dialog, inside {@link TCListener#onDialogTimeout(Dialog)} callback. If its
     * called, dialog wont be removed in case application does not perform 'send'.
     */
    void keepAlive();

    /**
     * Returns the state of this Dialog
     *
     * @return
     */
    TRPseudoState getState();

    /**
     * Return the maximum TCAP message length (in bytes) that are allowed for this dialog
     *
     * @return
     */
    int getMaxUserDataLength();    

    /**
     * Getting from the Dialog a user-defined object to save relating to the Dialog information
     *
     * @return
     */
    Externalizable getUserObject();

    /**
     * Store in the Dialog a user-defined object to save relating to the Dialog information
     *
     * @param userObject
     */
    void setUserObject(Externalizable userObject);

    /**
    *
    * @return IdleTaskTimeout value in milliseconds
    */
   long getIdleTaskTimeout();

    /**
     * Set IdleTaskTimeout in milliseconds.
     */
    void setIdleTaskTimeout(long idleTaskTimeoutMs);

    /**
     * Return the dialog start time epoch timestamp in milliseconds
     *
     * @return
     */
    long getStartTimeDialog();

    public void operationEnded(int invokeId);

    public void operationTimedOut(InvokeClass invokeClass, int invokeId);    
}