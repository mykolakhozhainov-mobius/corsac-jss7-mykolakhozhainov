/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.restcomm.protocols.ss7.sccp.MaxConnectionCountReached;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SccpManagementEventListener;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpStack;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.MessageFactory;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class CreateDialogTest {

    private SccpHarnessPreview sccpProv = new SccpHarnessPreview();
    private TCAPStackImplWrapper tcapStack1;

    @BeforeClass
    public void setUpClass() {
        System.out.println("setUpClass");
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        System.out.println("tearDownClass");
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("setUp");

        this.tcapStack1 = new TCAPStackImplWrapper(this.sccpProv, 8, "CreateDialogTest",4);

        this.tcapStack1.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterMethod
    public void tearDown() {
        this.tcapStack1.stop();
    }

    @Test(groups = { "functional.flow" })
    public void createDialogTest() throws Exception {

        SccpAddress localAddress = new SccpAddressImpl();
        SccpAddress remoteAddress = new SccpAddressImpl();

        Dialog dlg1 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress);
        assertEquals((long) dlg1.getLocalDialogId(), 1L);

        try {
            this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress, 1L);
            fail("Must be failure because dialogID==1 is taken");
        } catch (Exception e) {
        }

        Dialog dlg3 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress, 2L);
        assertEquals((long) dlg3.getLocalDialogId(), 2L);

        Dialog dlg4 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress);
        assertEquals((long) dlg4.getLocalDialogId(), 3L);
    }

    private class SccpHarnessPreview implements SccpProvider {
		private static final long serialVersionUID = 1L;

		@Override
        public void deregisterSccpListener(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getMaxUserDataLength(SccpAddress arg0, SccpAddress arg1, int networkId) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public MessageFactory getMessageFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ParameterFactory getParameterFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void registerSccpListener(int arg0, SccpListener listener) {
        }

        @Override
        public void send(SccpDataMessage msg) throws IOException {
            // we check here that no messages go from TCAP previewMode

            fail("No message must go from TCAP previewMode");
        }

        @Override
        public void registerManagementEventListener(UUID key,SccpManagementEventListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public void deregisterManagementEventListener(UUID key) {
            // TODO Auto-generated method stub

        }

        @Override
        public void coordRequest(int ssn) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public SccpConnection newConnection(int localSsn, ProtocolClass protocolClass) throws MaxConnectionCountReached {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ConcurrentHashMap<Integer, SccpConnection> getConnections() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void send(SccpNoticeMessage message) throws IOException {
            // TODO Auto-generated method stub
            
        }

		@Override
		public SccpStack getSccpStack() {
			// TODO Auto-generated method stub
			return null;
		}
    }

}
