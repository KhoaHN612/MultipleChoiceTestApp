/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author PC
 */
public class MultiChoiceQuestion implements Serializable{
    private String qid;
    private String question;
    private String picture;
    private int RightAnswer;
    private String[] answer;

    public MultiChoiceQuestion(String qid, String question, String picture, int RightAnswer, String[] answer) {
        this.qid = qid;
        this.question = question;
        this.picture = picture;
        this.RightAnswer = RightAnswer;
        this.answer = answer;
    }

    public String[] getAnswer() {
        return answer;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }
    
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getRightAnswer() {
        return RightAnswer;
    }

    public void setRightAnswer(int RightAnswer) {
        this.RightAnswer = RightAnswer;
    }
    
    public boolean isCorrect(int i){
        return (i == RightAnswer);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) { 
            return true;
        }
        if (!(obj instanceof MultiChoiceQuestion)) { 
            return false;
        }
        MultiChoiceQuestion other = (MultiChoiceQuestion) obj; 
        return this.qid.equals(other.qid); 
    }
}
