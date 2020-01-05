package com.example.studycourse.Util;

public class Course {
    private int imageId;
    private String courseID;
    private String courseName;
    private String teacher;
    private String school;
    private String courseDate;
    private String courseDescription;
    private String imageURL;

    public Course(int imageId,String courseName,String teacher,String school){
        super();
        this.imageId=imageId;
        this.courseName=courseName;
        this.teacher=teacher;
        this.school=school;
    }

    public Course(String courseID,String courseName,String teacher,String school,String imageURL){
        super();
        this.courseID=courseID;
        this.courseName=courseName;
        this.teacher=teacher;
        this.school=school;
        this.imageURL=imageURL;
    }

    public Course(int imageId, String courseName, String teacher, String school,
                  String courseDate, String courseDescription){
        super();
        this.imageId=imageId;
        this.courseName=courseName;
        this.teacher=teacher;
        this.school=school;
        this.courseDate=courseDate;
        this.courseDescription=courseDescription;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getcourseName(){
        return courseName;
    }

    public void setcourseName(String courseName){
        this.courseName=courseName;
    }

    public String getTeacher(){
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getSchool(){
        return school;
    }

    public void setSchool(String school){
        this.school=school;
    }

    public String getCourseDate(){
        return courseDate;
    }

    public void setCourseDate(String courseDate){
        this.courseDate=courseDate;
    }

    public String getCourseDescription(){
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription){
        this.courseDescription=courseDescription;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
