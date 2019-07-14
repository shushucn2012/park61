package com.park61.widget.wheel.weekcalendar.eventbus;
import org.joda.time.DateTime;

/**
 * Created by nor on 12/5/2015.
 */
public class Event {
    public static class OnDateClickEvent {
        public OnDateClickEvent(DateTime dateTime,int pPosition) {
            this.dateTime = dateTime;
            postion =pPosition;
        }

        private DateTime dateTime;
        private int postion;

        public DateTime getDateTime() {
            return dateTime;
        }

		public int getPostion() {
			return postion;
		}

		public void setPostion(int postion) {
			this.postion = postion;
		}
    }

    public static class InvalidateEvent {
    }

    public static class UpdateSelectedDateEvent {
        /***
         * Direction -1 for backgroun and 1 for forward
         *
         * @param direction
         */
        public UpdateSelectedDateEvent(int direction) {
            this.direction = direction;
        }

        public int getDirection() {
            return direction;
        }

        private int direction;
    }

    public static class SetCurrentPageEvent {
        public int getDirection() {
            return direction;
        }

        public SetCurrentPageEvent(int direction) {

            this.direction = direction;
        }

        private int direction;
    }

    public static class ResetEvent {
    }

    public static class SetSelectedDateEvent {
        public SetSelectedDateEvent(DateTime selectedDate) {
            this.selectedDate = selectedDate;
        }

        public DateTime getSelectedDate() {
            return selectedDate;
        }

        private DateTime selectedDate;
    }

    public static class SetStartDateEvent {


        public SetStartDateEvent(DateTime startDate) {
            this.startDate = startDate;
        }

        public DateTime getStartDate() {
            return startDate;
        }

        private DateTime startDate;
    }

}
