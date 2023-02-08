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
package org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events;

import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.DialogRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.EventType;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public abstract class DialogRequestImpl implements DialogRequest {
	private static final long serialVersionUID = 1L;

	private Dialog dialog;
    protected EventType type;
    protected boolean returnMessageOnError;

    protected DialogRequestImpl(EventType type) {
        super();
        this.type = type;
    }

    /**
     * @return the dialog
     */
    public Dialog getDialog() {
        return dialog;
    }

    /**
     * @param dialog the dialog to set
     */
    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    /**
     * @return the type
     */
    public EventType getType() {
        return type;
    }

    public boolean getReturnMessageOnError() {
        return returnMessageOnError;
    }

    public void setReturnMessageOnError(boolean val) {
        returnMessageOnError = val;
    }

}
