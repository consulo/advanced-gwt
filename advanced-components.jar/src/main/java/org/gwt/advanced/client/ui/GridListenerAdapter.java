/*
 * Copyright 2008-2012 Sergey Skladchikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gwt.advanced.client.ui;

import org.gwt.advanced.client.datamodel.GridDataModel;
import org.gwt.advanced.client.ui.widget.cell.HeaderCell;

/**
 * Adapter class for the data model listeners.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class GridListenerAdapter implements GridListener {
    /** {@inheritDoc} */
    public void onSort (HeaderCell cell, GridDataModel dataModel) {
    }

    /** {@inheritDoc} */
    public void onSave (GridDataModel dataModel) {
    }

    /** {@inheritDoc} */
    public void onClear (GridDataModel dataModel) {
    }
}
