package com.revisedu.revised.request;

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
        if(city.isEmpty()&&location.isEmpty()&&mclass.isEmpty()&&subject.isEmpty()){
            return "";
        }
        return city+", "+location+", "+mclass+", "+subject;
    }
}
