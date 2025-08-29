package com.dailyvibeproj.daily_vibe.model;

public class User {

    // data identification
    private String id;
    private String name;
    private String language;
    // personal info
    // private String age;
    private String gender;
    // private String location;
    // private String time_zone;
    // emotions & motivation context
    // private List<String> mood;
    private String goals;
    // private List<String> interests;
    private String tone_preference;
    private String coach;

    // dummy data constructor
    public User() {
        this.id = "1";
        this.name = "JohnDoe";
        this.language = "en";
        // this.age = "21";
        this.gender = "M";
        // this.location = "India";
        // this.time_zone = "Asia/Kolkata";
        // this.mood = new ArrayList<>(List.of("optimistic", "ambitious"));
        this.goals = "career growth";
        // this.interests = new ArrayList<>(List.of("tech", "spirituality"));
        this.tone_preference = "uplifting, motivational";
        this.coach = "Kore";
    }

    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    // public String getAge() {
    // return age;
    // }

    // public void setAge(String age) {
    // this.age = age;
    // }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // public String getLocation() {
    // return location;
    // }

    // public void setLocation(String location) {
    // this.location = location;
    // }

    // public String getTime_zone() {
    // return time_zone;
    // }

    // public void setTime_zone(String time_zone) {
    // this.time_zone = time_zone;
    // }

    // public List<String> getMood() {
    // return mood;
    // }

    // public void setMood(List<String> mood) {
    // this.mood = mood;
    // }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    // public List<String> getInterests() {
    // return interests;
    // }

    // public void setInterests(List<String> interests) {
    // this.interests = interests;
    // }

    public String getTone_preference() {
        return tone_preference;
    }

    public void setTone_preference(String tone_preference) {
        this.tone_preference = tone_preference;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }
}
