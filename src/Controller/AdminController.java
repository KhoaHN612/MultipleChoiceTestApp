/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Exam;
import Model.MultiChoiceQuestion;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import lib.XFile;

/**
 *
 * @author PC
 */
public class AdminController {
    Random generator;
    Integer sizeExam = 5;
    String filePath; 
    ArrayList<Exam> examList;
    ArrayList<MultiChoiceQuestion> quesList;
    ArrayList<MultiChoiceQuestion> quesFalseList;

    public AdminController() {
        generator = new Random();
        filePath = "./src/Data/";
        quesList = new ArrayList<>();
        quesFalseList = new ArrayList<>();
        examList = new ArrayList<>();
        boolean file = loadFile();
        if (file){
//            for (Exam exam : examList) {
//                ArrayList<MultiChoiceQuestion> lsQues = exam.getQuestions();
//                System.out.println(exam.getId());
//                for (MultiChoiceQuestion lsQue : lsQues) {
//                    System.out.println(lsQue.getQuestion());
//                }
//            }
//            for (MultiChoiceQuestion ques : quesList) {
//                System.out.println(ques.getQuestion());
//            }
        }else {
//            JOptionPane.showMessageDialog(this, "Data missing");
        }
    }

    private boolean loadFile(){
        if (XFile.readObject(filePath+"exam.dat")==null){
            return false;
        }
        examList = (ArrayList<Exam>) XFile.readObject(filePath+"exam.dat");
        
        if (XFile.readObject(filePath+"question.dat")==null){
            return false;
        }
        quesList = (ArrayList<MultiChoiceQuestion>) XFile.readObject(filePath+"question.dat");
        
        if (XFile.readObject(filePath+"questionfalse.dat")==null){
            return false;
        }
        quesFalseList = (ArrayList<MultiChoiceQuestion>) XFile.readObject(filePath+"questionfalse.dat");
        
        return true;  
    }
    
    public void saveFile(){
        XFile.writeObject(filePath+"exam.dat", examList);
        XFile.writeObject(filePath+"question.dat", quesList);
        XFile.writeObject(filePath+"questionfalse.dat", quesFalseList);
    }
    
    public boolean checkValidId(String id){
        for (MultiChoiceQuestion ques : quesList) {
            if (ques.getQid().equals(id)){
                return false;
            }
        }
        return true;
    }
    
    public void addQuestion(MultiChoiceQuestion ques){
        quesList.add(ques);
        saveFile();
    }
    
    public boolean saveImage(String imagePath, String imageName){
        Path sourcePath = Paths.get(imagePath);
        Path destPath = Paths.get("./src/img/" + imageName);

        try {
            Files.copy(sourcePath, destPath);
            System.out.println("Image file copied successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Failed to copy image file: " + e.getMessage());
            System.out.println(sourcePath);
            System.out.println(destPath);
            return false;
        }
    }
    
    public boolean delImage(String imageName){
        File file = new File("src/img/" + imageName);
        if (!file.exists()) {
            System.out.println("No Image to delete");
            return true;
        }
        if (file.delete()) {
            System.out.println(file.getName() + " is deleted!");
            return true;
        } else {
            System.out.println("Delete operation failed.");
            System.out.println(file.getAbsolutePath());
            return false;
        }
    }
    
    public String getAbsoluteImage(String imageName){
        File file = new File("src/img/" + imageName);
        return file.getAbsolutePath();
    }

    public ArrayList<MultiChoiceQuestion> getQuesList() {
        return quesList;
    }

    public ArrayList<Exam> getExamList() {
        return examList;
    }
    
    public void delQuesByQid(String qid){
        MultiChoiceQuestion aimQues = null;
        for (MultiChoiceQuestion ques : quesList){
            if (ques.getQid().equals(qid)){
                aimQues = ques;
            }
        }
        if (aimQues != null) quesList.remove(aimQues);
        saveFile();
    }
    
    public void delQuesAbsByQid(String qid){
        MultiChoiceQuestion aimQues = null;
        for (MultiChoiceQuestion ques : quesList){
            if (ques.getQid().equals(qid)){
                aimQues = ques;
            }
        }
        if (aimQues == null) return;
        for (Exam exam : examList) {
            exam.getQuestions().remove(aimQues);
        }
        quesList.remove(aimQues);
        saveFile();
    }
    
    public MultiChoiceQuestion getQuesById(Integer index){
        return quesList.get(index);
    }
    
    public boolean createNewExamCode(){
        if (sizeExam>quesList.size()) return false;
        int i;
        ArrayList<MultiChoiceQuestion> temp = new ArrayList<MultiChoiceQuestion>();
        temp.addAll(quesList);
        ArrayList<MultiChoiceQuestion> newList = new ArrayList<MultiChoiceQuestion>();
        while (newList.size()<sizeExam) {
            i = generator.nextInt(temp.size());
            newList.add(temp.get(i));
            temp.remove(i);
        }
        examList.add(new Exam("E" + String.valueOf(generator.nextInt(100)),newList));
        saveFile();
        return true;
    }
    
    public boolean addQuesToExam(Integer curRowExam, Integer curRowInStorage){
        MultiChoiceQuestion ques = quesList.get(curRowInStorage);
        Exam exam = examList.get(curRowExam);
        
        if (exam.getQuestions().contains(ques)) return false;
        
        exam.getQuestions().add(ques);
        saveFile();
        return true;
    }

    public boolean delQuesFromExam(Integer curRowExam, Integer curRowInExam) {
        Exam exam = examList.get(curRowExam);
        try {
            MultiChoiceQuestion ques = exam.getQuestions().get(curRowInExam); 
            exam.getQuestions().remove(ques);
        } catch (Exception e) {
            return false;
        }
              
        
        saveFile();
        return true;
    }

    public void deleteExamCode(Integer curRowExam) {
        Exam exam = examList.get(curRowExam);
        examList.remove(exam);
        saveFile();
    }
    
    public void updateQuesToExam(MultiChoiceQuestion ques){
//        System.out.println(ques.getQuestion());
        for (Exam exam : examList) {
            for (MultiChoiceQuestion question : exam.getQuestions()) {
                if (question.getQid().equals(ques.getQid())){
//                    System.out.println(exam.getId());
//                    System.out.println(ques.getQuestion());
//                    System.out.println(question.getQuestion());
                    question.setQuestion(ques.getQuestion());
                    question.setPicture(ques.getPicture());
                    question.setAnswer(ques.getAnswer());
                    question.setRightAnswer(ques.getRightAnswer());
                    System.out.println(question.getQuestion());
                }
            }
 
        }
    }
}
