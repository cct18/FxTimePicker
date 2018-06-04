package net.cct18.timePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A Java FX time picker based on TextField
 * 
 * @author cct18
 *
 */
public class TimePicker extends TextField {

	private boolean wasZeroOneOrTwo = false;
	private boolean isZeroOneOrTwo = false;
	private String lastTime = null;

	/**
	 * Initialize variables.
	 * 
	 * Replace onKeyReleased listener by another one. If a listener was already set, it will be called after checking time
	 */
	public TimePicker() {
		super();
		this.setOnKeyReleased((KeyEvent event) -> {
			checkTime(event);
		});
	}

	/**
	 * Check time
	 * 
	 * @param event the released key event
	 */
	private void checkTime(KeyEvent event) {
		String text = this.getText();
		isZeroOneOrTwo = event.getCode() == KeyCode.DIGIT0 || event.getCode() == KeyCode.NUMPAD0
				|| event.getCode() == KeyCode.DIGIT1 || event.getCode() == KeyCode.NUMPAD1
				|| event.getCode() == KeyCode.DIGIT2 || event.getCode() == KeyCode.NUMPAD2;
		if (text != null && !text.isEmpty()) {
			boolean isDeleting = event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE;
			String time = text.replaceAll("[^:0-9]", "");
			String[] timeParts = time.split(":");
			String hours = timeParts[0];
			String minutes = null;
			if (timeParts.length > 1) {
				minutes = "";
				for (int i = 1; i < timeParts.length; i++) {
					minutes += timeParts[i];
				}
			} else if (time.contains(":")) {
				minutes = "";
			}
			if (isDeleting && minutes == null && lastTime != null && lastTime.contains(":")
					&& lastTime.length() - hours.length() == 1) {
				hours = hours.substring(0, 1);
			}
			int minutesInt = -1;
			int hoursInt = hours.isEmpty() || hours.length() > 9 ? 0 : Integer.parseInt(hours);
			if (minutes != null) {
				minutesInt = minutes.isEmpty() || minutes.length() > 9 ? 0 : Integer.parseInt(minutes);
			}
			String formattedTime = "";
			if (time.length() > 0 && !(isDeleting && hoursInt == 0 && minutesInt == -1)) {
				hoursInt = Math.min(hoursInt, 23);
				formattedTime = String.format("%02d", hoursInt);
				if (minutesInt >= 0) {
					formattedTime += ":";
					if ((!minutes.isEmpty() && !isDeleting) || (isDeleting && minutesInt > 0)) {
						formattedTime += String.format("%02d", Math.min(minutesInt, 59));
					}
				} else if (!isDeleting && (!(!wasZeroOneOrTwo && isZeroOneOrTwo && minutesInt < 0) || hoursInt >= 10)) {
					formattedTime += ":";
				}
			}
			if (!text.equals(formattedTime)) {
				this.setText(formattedTime);
				this.positionCaret(formattedTime.length());
				lastTime = formattedTime;
			}
		} else {
			lastTime = null;
		}
		wasZeroOneOrTwo = isZeroOneOrTwo;
	}

	/**
	 * Gets the hour of the selected time
	 * 
	 * @return the hours
	 */
	public int getHour() {
		String text = this.getText();
		int hours = 0;
		if (text != null) {
			String[] time = text.split(":");
			try {
				hours = Integer.parseInt(time[0]);
			} catch (NumberFormatException e) {
			}
		}
		return hours;
	}

	/**
	 * Gets the minute of the selected time
	 * 
	 * @return the minutes
	 */
	public int getMinute() {
		String text = this.getText();
		int minutes = 0;
		if (text != null) {
			String[] time = text.split(":");
			if (time.length > 0) {
				try {
					minutes = Integer.parseInt(time[1]);
				} catch (NumberFormatException e) {
				}
			}
		}
		return minutes;
	}

	/**
	 * Gets time at a specified date
	 * 
	 * @param date
	 *            the specified date
	 * 
	 * @return the selected time at the specified date
	 */
	public LocalDateTime getTimeAt(LocalDate date) {
		return date.atTime(getHour(), getMinute());
	}

	/**
	 * Gets selected time
	 * 
	 * @return the selected time
	 */
	public LocalTime getTime() {
		return LocalTime.of(getHour(), getMinute());
	}

	/**
	 * Sets the time
	 * 
	 * @param time
	 *            the time to set
	 */
	public void setTime(LocalTime time) {
		if (time == null) {
			setText("");
		} else {
			setText(String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()));
		}
	}

	/**
	 * Returns true only if the hour has been set
	 * 
	 * @return true if the hour has been set or false otherwise
	 */
	public boolean isHourFilled() {
		String text = this.getText();
		return text != null && !text.isEmpty();
	}

	/**
	 * Returns true only if the minute has been set
	 * 
	 * @return true if the minute has been set or false otherwise
	 */
	public boolean isMinuteFilled() {
		String text = this.getText();
		return text != null && !text.isEmpty() && text.split(":").length >= 2;
	}

	/**
	 * Returns true only if the complete time has been set
	 * 
	 * @return true if the complete time has been set or false otherwise
	 */
	public boolean isTimeFilled() {
		String text = this.getText();
		return text != null && !text.isEmpty() && text.split(":").length >= 2;
	}
}
