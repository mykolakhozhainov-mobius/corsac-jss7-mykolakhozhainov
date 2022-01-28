package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNotificationToMSUser extends ASNEnumerated {
	public ASNNotificationToMSUser() {
		
	}
	
	public ASNNotificationToMSUser(NotificationToMSUser t) {
		super(t.getCode());
	}
	
	public NotificationToMSUser getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return NotificationToMSUser.getInstance(realValue);
	}
}