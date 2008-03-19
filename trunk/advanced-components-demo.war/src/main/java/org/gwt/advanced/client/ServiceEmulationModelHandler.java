package org.gwt.advanced.client;

import org.gwt.advanced.client.datamodel.*;
import org.gwt.advanced.client.ui.widget.GridPanel;

import java.util.*;

import com.google.gwt.user.client.ui.ListBox;

/**
 * This is a demo model handler.<p>
 * In fact it must be remote service, but this sample just emulates remoting.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 */
public class ServiceEmulationModelHandler implements DataModelCallbackHandler {
    /** persistent data */
    private Object[][] data;
    /** a grid panel */
    private GridPanel panel;

    /**
     * Creates an instance of this class and initializes the internal field.
     *
     * @param data is an initial data.
     */
    public ServiceEmulationModelHandler(Object[][] data) {
        this.data = data;
    }

    /** {@inheritDoc} */
    public void synchronize(GridDataModel model) {
        if (panel != null)
            panel.lock();
        saveData(model);
        List rows = Arrays.asList(data);
        Collections.sort(rows, new DataComparator(model.getSortColumn(), model.isAscending()));

        ((LazyLoadable)model).setTotalRowCount(data.length);
        List result = new ArrayList();
        for (int i = model.getStartRow(); i < rows.size() && i < model.getStartRow() + model.getPageSize(); i++) {
            result.add(rows.get(i));
        }

        ((Editable)model).update((Object[][])result.toArray(new Object[result.size()][]));
        if (panel != null)
            panel.unlock();
    }

    /**
     * This method emulates data saving to the persistence storage.
     *
     * @param model is a grid data model to be saved.
     */
    private void saveData (GridDataModel model) {
        Object[][] modelData = model.getData();

        List dataList = new ArrayList(Arrays.asList(data));
        for (int j = 0; j < dataList.size(); j++) {
            Object[] persistentRow = (Object[]) dataList.get(j);
            Long persistentId = (Long) persistentRow[persistentRow.length - 1];
            for (int i = 0; i < modelData.length; i++) {
                Object[] row = modelData[i];
                Long id = (Long) row[row.length - 1];
                if (persistentId.equals(id)) {
                    dataList.set(j, row);
                    break;
                } else if (id == null) {
                    row[row.length - 1] = new Long(System.currentTimeMillis());
                    dataList.add(row);
                    break;
                }
            }
        }
        
        Object[][] removedRows = ((Editable) model).getRemovedRows();
        for (int i = 0; i < removedRows.length; i++) {
            Object[] row = removedRows[i];
            Long id = (Long) row[row.length - 1];
            for (int j = 0; j < dataList.size(); j++) {
                Object[] persistentRow = (Object[]) dataList.get(j);
                Long persistentId = (Long) persistentRow[persistentRow.length - 1];
                if (persistentId.equals(id)) {
                    dataList.remove(j);
                    break;
                }
            }
        }

        data = (Object[][]) dataList.toArray(new Object[dataList.size()][]);
    }

    /**
     * Getter for property 'panel'.
     *
     * @return Value for property 'panel'.
     */
    public GridPanel getPanel() {
        return panel;
    }

    /**
     * Setter for property 'panel'.
     *
     * @param panel Value to set for property 'panel'.
     */
    public void setPanel(GridPanel panel) {
        this.panel = panel;
    }

    /**
     * This is a data comparator to emulate server-side sorting.<p/>
     * In your applications you will use database sorting.
     */
    private static class DataComparator implements Comparator {
        /** sort column number */
        private int sortColumn;
        /** sort order */
        private boolean ascending;

        /**
         * This constructor initializes internal fields.
         *
         * @param sortRow is a sort column.
         * @param ascending is a sort order.
         */
        public DataComparator (int sortRow, boolean ascending) {
            this.sortColumn = sortRow;
            this.ascending = ascending;
        }

        /** {@inheritDoc} */
        public int compare (Object o1, Object o2) {
            Object[] row1 = (Object[]) o1;
            Object[] row2 = (Object[]) o2;

            int sign = ascending ? 1 : -1;

            if (row1[sortColumn] == null && row2[sortColumn] == null)
                return 0;
            else if (row2[sortColumn] != null && row1[sortColumn] == null)
                return sign;
            else if (row1[sortColumn] != null && row2[sortColumn] == null)
                return -sign;

            if (row1[sortColumn] instanceof Comparable)
                return sign * ((Comparable)row1[sortColumn]).compareTo(row2[sortColumn]);
            else if (row1[sortColumn] instanceof ListBox) {
                ListBox list1 = (ListBox) row1[sortColumn];
                ListBox list2 = (ListBox) row2[sortColumn];
                return sign * list1.getValue(list1.getSelectedIndex()).compareTo(list2.getValue(list2.getSelectedIndex()));
            } else
                return 0;
        }
    }
}