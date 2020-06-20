package com.revisedu.revised.request;

import java.util.ArrayList;
import java.util.List;

public class SearchRequestModel {

    String city;
    String location;
    String mclass;
    String subject;

    public SearchRequestModel(String city, String location, String mclass, String subject) {
        this.city = city;
        this.location = location;
        this.mclass = mclass;
        this.subject = subject;
    }

    public String getCity() {
        return city;
    }

    public String getLocation() {
        return location;
    }

    public String getMclass() {
        return mclass;
    }

    public String getSubject() {
        return subject;
    }

    public String getShowText(){
        List<String> list = new ArrayList<>();
        if(!city.isEmpty()) {
            list.add(city);
        }
        if(!location.isEmpty()) {
            list.add(location);
        }
        if(!mclass.isEmpty()) {
            list.add(mclass);
        }
        if(!subject.isEmpty()) {
            list.add(subject);
        }
        String response = "";
        for (int i = 0; i < list.size(); i++) {
            if(i==0){
                response+=list.get(i);
            }
            else {
                response += ", "+list.get(i);
            }
        }

        return response;
    }
}
