package org.gwt.advanced.client.ui.widget;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.ui.CalendarListener;

import java.util.Date;

/**
 * This is a date picker widget.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 */
public class DatePicker extends TextButtonPanel implements CalendarListener {
    /** calendar popup panel */
    private PopupPanel calendarPanel;
    /** calendar widget */
    private Calendar calendar;
    /** open calendar event listener */
    private ClickListener openCalendarClickListener;
    /** the date */
    private Date date;
    /** time visibility flag */
    private boolean timeVisible = false;

    /**
     * Creates an instance of this class.
     *
     * @param initialDate is an initial date.
     */
    public DatePicker(Date initialDate) {
        this.date = initialDate;
    }

    /**
     * Getter for property 'timeVisible'.
     *
     * @return Value for property 'timeVisible'.
     */
    public boolean isTimeVisible() {
        return timeVisible;
    }

    /**
     * Setter for property 'timeVisible'.
     *
     * @param timeVisible Value to set for property 'timeVisible'.
     */
    public void setTimeVisible(boolean timeVisible) {
        this.timeVisible = timeVisible;
    }

    /** {@inheritDoc} */
    public void onChange(Widget sender, Date oldValue) {
        getCalendarPanel().hide();
        Date date = getCalendar().getDate();
        getSelectedValue().setText(getFormat().format(date));
        this.date = date; 
    }

    /** {@inheritDoc} */
    public void onCancel(Widget sender) {
        getCalendarPanel().hide();
    }

    /**
     * Getter for property 'date'.
     *
     * @return Value for property 'date'.
     */
    public Date getDate() {
        return date;
    }
    
    /** {@inheritDoc} */
    public void display() {
        super.display();
        getCalendar().setShowTime(isTimeVisible());
    }

    /**
     * Gets a textual representation of the date using format properties.
     *
     * @return textual representation.
     */
    public String getTextualDate() {
        return getFormat().format(getDate());
    }

    /** {@inheritDoc} */
    protected void prepareSelectedValue() {
        super.prepareSelectedValue();
        getSelectedValue().setReadOnly(true);
        Date date = getDate();
        if (date != null)
            getSelectedValue().setText(getFormat().format(date));
    }

    /** {@inheritDoc} */
    protected String getDefaultImageName() {
        return "calendar.gif";
    }

    /**
     * This method adds different listeners to elements of the widget.
     */
    protected void addComponentListeners() {
        if (openCalendarClickListener == null)
            openCalendarClickListener = new OpenCalendarClickListener();

        if (isChoiceButtonVisible()) {
            ToggleButton calendarButton = getChoiceButton();
            calendarButton.removeClickListener(openCalendarClickListener);
            calendarButton.addClickListener(openCalendarClickListener);
        } else {
            TextBox box = getSelectedValue();
            box.removeClickListener(openCalendarClickListener);
            box.addClickListener(openCalendarClickListener);
        }

        getCalendar().addCalendarListener(this);
    }

    /**
     * Getter for property 'calendar'.
     *
     * @return Value for property 'calendar'.
     */
    protected Calendar getCalendar () {
        if (calendar == null) {
            calendar = new Calendar();
            calendar.setSelectedDate(getDate());
        }

        return calendar;
    }

    /**
     * Getter for property 'calendarPanel'.
     *
     * @return Value for property 'calendarPanel'.
     */
    protected PopupPanel getCalendarPanel() {
        if (calendarPanel == null) {
            calendarPanel = new PopupPanel(false, true);
            calendarPanel.add(getCalendar());
        }
        return calendarPanel;
    }

    /**
     * Getter for property 'format'.
     *
     * @return Value for property 'format'.
     */
    protected DateTimeFormat getFormat() {
        DateTimeFormat format;
        if (isTimeVisible())
            format = DateTimeFormat.getLongDateTimeFormat();
        else
            format = DateTimeFormat.getLongDateFormat();
        return format;
    }

    /**
     * This is an open calendar evbent listener implementation.
     */
    protected class OpenCalendarClickListener implements ClickListener {
        /** {@inheritDoc} */
        public void onClick (Widget sender) {
            Calendar calendar = getCalendar();

            if (getDate() != null)
                calendar.setSelectedDate(getDate());
            else
                calendar.setSelectedDate(new Date());

            calendar.display();
            getCalendarPanel().show();

            int left = getAbsoluteLeft() + getSelectedValue().getOffsetWidth();
            if (left + getCalendar().getOffsetWidth() > Window.getClientWidth())
                left -= getCalendar().getOffsetWidth();

            getCalendarPanel().setPopupPosition(left, getAbsoluteTop());
            getChoiceButton().setDown(false);
        }
    }
}