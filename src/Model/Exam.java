/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PC
 */
public class Exam implements Serializable{
    private String Id;
    private Integer Highscore;
    private ArrayList<MultiChoiceQuestion> Questions;

    public Exam(String Id, ArrayList<MultiChoiceQuestion> Questions) {
        this.Id = Id;
        this.Questions = Questions;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public ArrayList<MultiChoiceQuestion> getQuestions() {
        return Questions;
    }

    public void setQuestions(ArrayList<MultiChoiceQuestion> Questions) {
        this.Questions = Questions;
    }
    
    public Integer getHighscore() {
        return Highscore;
    }

    public void setHighscore(Integer Highscore) {
        this.Highscore = Highscore;
    }
    
    
}
