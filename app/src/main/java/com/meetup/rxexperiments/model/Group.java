package com.meetup.rxexperiments.model;

import java.util.List;

public class Group {
    public Category category;
    public String city;
    public String country;
    public long created;
    public String description;
    public PhotoBasics groupPhoto;
    public long id;
    public double lat, lon;
    public int members;
    public String name;
    public List<PhotoBasics> photos;
    public Self self;
    public String timezone;
    public String urlname;
    public String who;

    public static class Self {
        public List<String> actions;
        public String role;
        public String status;
        public long visited;
    }
}
