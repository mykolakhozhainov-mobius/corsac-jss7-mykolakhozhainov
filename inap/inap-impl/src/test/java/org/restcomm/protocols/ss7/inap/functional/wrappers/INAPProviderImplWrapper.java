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

package org.restcomm.protocols.ss7.inap.functional.wrappers;

import org.restcomm.protocols.ss7.inap.INAPProviderImpl;
import org.restcomm.protocols.ss7.inap.INAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPProviderImplWrapper extends INAPProviderImpl {
	private static final long serialVersionUID = 1L;

	// private final MAPServiceSupplementary mapServiceSupplementaryTest = new MAPServiceSupplementaryImplWrapper(this);

    public INAPProviderImplWrapper(TCAPProvider tcapProvider,INAPStackImpl stack) {
        super("Test", stack, tcapProvider);

        // this.inapServices.add(this.mapServiceSupplementaryTest);
    }
}
