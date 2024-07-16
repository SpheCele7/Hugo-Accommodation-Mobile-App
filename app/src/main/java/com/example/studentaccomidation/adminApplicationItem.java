package com.example.studentaccomidation;

public class adminApplicationItem {

    String date;
    String payer;
    Boolean requirements;
    String status;
    String resName;
    String residenceId;
    String fullName;
    String documentId;
    String student_email;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPayer() {
        return payer;
    }

    public Boolean getRequirements() {
        return requirements;
    }

    public String getStatus() {
        return status;
    }

    public String getResName() {
        return resName;
    }


    public String getResidenceId() {
        return residenceId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudent_email() {
        return student_email;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFullName() {
        return fullName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
