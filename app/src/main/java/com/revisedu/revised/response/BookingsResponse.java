package com.revisedu.revised.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookingsResponse extends CommonResponse {

    @SerializedName("bookingList")
    private List<BookingItem> mBookingList = new ArrayList<>();

    public List<BookingItem> getBookingList() {
        return mBookingList;
    }

    public class BookingItem {

        @SerializedName("title")
        private String mTitle;

        @SerializedName("status")
        private String mStatus;

        @SerializedName("date_of_booking")
        private String mDateOfBooking;

        @SerializedName("photoUrl")
        private String mPhotoUrl;

        public String getTitle() {
            return mTitle;
        }

        public String getStatus() {
            return mStatus;
        }

        public String getDateOfBooking() {
            return mDateOfBooking;
        }

        public String getPhotoUrl() {
            return mPhotoUrl;
        }
    }
}
