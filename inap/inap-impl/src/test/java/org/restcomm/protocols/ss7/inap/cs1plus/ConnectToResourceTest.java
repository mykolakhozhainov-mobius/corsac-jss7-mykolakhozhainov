package org.restcomm.protocols.ss7.inap.cs1plus;
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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ConnectToResourceRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceInteractionIndicatorsImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class ConnectToResourceTest 
{
	protected final transient Logger logger=LogManager.getLogger(ConnectToResourceTest.class);

	private byte[] message1=new byte[] { 0x30,0x0a,(byte)0xa1,0x03,(byte)0x81,0x01,0x01,(byte)0xbe,
			0x03,(byte)0x82,0x01,0x01 };
	
	private byte[] serviceIndicatorsData=new byte[] { -126, 1, 1 };
	 
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(ConnectToResourceRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ConnectToResourceRequestImpl);
	        
		ConnectToResourceRequestImpl elem = (ConnectToResourceRequestImpl)result.getResult();
		assertNotNull(elem.getServiceInteractionIndicators());
		assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(serviceIndicatorsData), elem.getServiceInteractionIndicators().getValue()));
		assertNotNull(elem.getResourceAddress());
		assertNotNull(elem.getResourceAddress().getLegID());
		assertEquals(elem.getResourceAddress().getLegID(),LegType.leg1);
		logger.info(elem);		
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ConnectToResourceRequestImpl.class);
	    	
		ServiceInteractionIndicators serviceInteractionIndicators=new ServiceInteractionIndicatorsImpl(Unpooled.wrappedBuffer(serviceIndicatorsData));
		ConnectToResourceRequestImpl elem = new ConnectToResourceRequestImpl(LegType.leg1, null,serviceInteractionIndicators);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}