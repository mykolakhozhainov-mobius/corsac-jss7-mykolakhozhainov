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

package org.restcomm.protocols.ss7.cap.EsiGprs;

import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ASNPDPInitiationTypeImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.EndUserAddressImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class PDPContextEstablishmentSpecificInformationImpl implements PDPContextEstablishmentSpecificInformation  {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1,defaultImplementation = AccessPointNameImpl.class)
    private AccessPointName accessPointName;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = EndUserAddressImpl.class)
    private EndUserAddress endUserAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = QualityOfServiceImpl.class)
    private QualityOfService qualityOfService;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1, defaultImplementation = LocationInformationGPRSImpl.class)
    private LocationInformationGPRS locationInformationGPRS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1, defaultImplementation = TimeAndTimezoneImpl.class)
    private TimeAndTimezone timeAndTimezone;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1)
    private ASNPDPInitiationTypeImpl pdpInitiationType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1)
    private ASNNull secondaryPDPContext;

    public PDPContextEstablishmentSpecificInformationImpl() {
    }

    public PDPContextEstablishmentSpecificInformationImpl(AccessPointName accessPointName, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, LocationInformationGPRS locationInformationGPRS,
            TimeAndTimezone timeAndTimezone, PDPInitiationType pdpInitiationType, boolean secondaryPDPContext) {
        this.accessPointName = accessPointName;
        this.endUserAddress = endUserAddress;
        this.qualityOfService = qualityOfService;
        this.locationInformationGPRS = locationInformationGPRS;
        this.timeAndTimezone = timeAndTimezone;
        
        if(pdpInitiationType!=null)
        	this.pdpInitiationType = new ASNPDPInitiationTypeImpl(pdpInitiationType);
        	
        if(secondaryPDPContext)
        	this.secondaryPDPContext = new ASNNull();
    }

    public AccessPointName getAccessPointName() {
        return this.accessPointName;
    }

    public PDPInitiationType getPDPInitiationType() {
    	if(pdpInitiationType==null)
    		return null;
    	
        return this.pdpInitiationType.getType();
    }

    public boolean getSecondaryPDPContext() {
        return this.secondaryPDPContext!=null;
    }

    public LocationInformationGPRS getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    public EndUserAddress getEndUserAddress() {
        return this.endUserAddress;
    }

    public QualityOfService getQualityOfService() {
        return this.qualityOfService;
    }

    public TimeAndTimezone getTimeAndTimezone() {
        return this.timeAndTimezone;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PdpContextchangeOfPositionSpecificInformation [");

        if (this.accessPointName != null) {
            sb.append("accessPointName=");
            sb.append(this.accessPointName.toString());
            sb.append(", ");
        }

        if (this.locationInformationGPRS != null) {
            sb.append("locationInformationGPRS=");
            sb.append(this.locationInformationGPRS.toString());
            sb.append(", ");
        }

        if (this.endUserAddress != null) {
            sb.append("endUserAddress=");
            sb.append(this.endUserAddress.toString());
            sb.append(", ");
        }

        if (this.qualityOfService != null) {
            sb.append("qualityOfService=");
            sb.append(this.qualityOfService.toString());
            sb.append(", ");
        }

        if (this.timeAndTimezone != null) {
            sb.append("timeAndTimezone=");
            sb.append(this.timeAndTimezone.toString());
            sb.append(", ");
        }

        if (this.pdpInitiationType != null && this.pdpInitiationType.getType()!=null) {
            sb.append("pdpInitiationType=");
            sb.append(this.pdpInitiationType.getType().toString());
            sb.append(", ");
        }

        if (this.secondaryPDPContext!=null) {
            sb.append("secondaryPDPContext=");
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
