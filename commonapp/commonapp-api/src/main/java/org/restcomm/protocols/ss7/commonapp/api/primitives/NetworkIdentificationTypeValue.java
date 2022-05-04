package org.restcomm.protocols.ss7.commonapp.api.primitives;
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
/**
 *
 1) Type of network identification 0 0 0 spare (no interpretation) 0 0 1 spare (no interpretation) 0 1 0 national network
 * identification 0 1 1 } to } spare (no interpretation) 1 1 1 }
 *
 * -- values are defined in ANSI T1.113.3.
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public enum NetworkIdentificationTypeValue {

    spare_1(0), spare_2(0x01), nationalNetworkIdentification(0x02), spare_3(0x03), spare_4(0x04), spare_5(0x05), spare_6(0x06), spare_7(
            0x07);

    private int code;

    private NetworkIdentificationTypeValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static NetworkIdentificationTypeValue getInstance(int code) {
        switch (code) {
            case 0:
                return NetworkIdentificationTypeValue.spare_1;
            case 0x01:
                return NetworkIdentificationTypeValue.spare_2;
            case 0x02:
                return NetworkIdentificationTypeValue.nationalNetworkIdentification;
            case 0x03:
                return NetworkIdentificationTypeValue.spare_3;
            case 0x04:
                return NetworkIdentificationTypeValue.spare_4;
            case 0x05:
                return NetworkIdentificationTypeValue.spare_5;
            case 0x06:
                return NetworkIdentificationTypeValue.spare_6;
            case 0x07:
                return NetworkIdentificationTypeValue.spare_7;
            default:
                return null;
        }
    }

}
