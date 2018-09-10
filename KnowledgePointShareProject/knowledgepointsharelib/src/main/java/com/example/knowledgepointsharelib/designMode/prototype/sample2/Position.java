package com.example.knowledgepointsharelib.designMode.prototype.sample2;

/**
 * Created by sunjie on 2018/5/31.
 */
//            职位
public class Position implements Cloneable {
    private String jobTitle;

    @Override
    protected Position clone() {
        try {
            return (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Position(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "Position{" +
                "jobTitle='" + jobTitle + '\'' +
                '}';
    }

}
