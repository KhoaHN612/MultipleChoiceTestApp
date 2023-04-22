/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Exam;
import Model.MultiChoiceQuestion;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.ParagraphStyle;
import com.spire.doc.fields.DocPicture;
import com.spire.doc.fields.TextRange;
import com.spire.doc.formatting.CharacterFormat;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import lib.XFile;

/**
 *
 * @author PC
 */
public class HomeController {
    Random generator;
    String filePath; 
    ArrayList<Exam> examList;
    ArrayList<MultiChoiceQuestion> quesList;
    ArrayList<MultiChoiceQuestion> quesFalseList;
    Integer[] ans;
    Exam curExam;
    Integer idCurQues;
    Integer idCurFalseQues;
    Integer sizeExam = 5;
    Integer timeExam = 60;
    String status;

    public HomeController() {
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
    
    public void startExam(int index){
        curExam = examList.get(index);
        ans = new Integer[curExam.getQuestions().size()];
        idCurQues = 0;
        status = "doing";
    }
    public MultiChoiceQuestion CurQues(){
        return curExam.getQuestions().get(idCurQues);
    }
    public Integer CurAns(){
        return ans[idCurQues];
    }
    public Integer HighScore(int index){
        return examList.get(index).getHighscore();
    }
    public Integer SizeExam(int index){
        return examList.get(index).getQuestions().size();
    }
    public void chooseAnswer(int index){
        ans[idCurQues] = index;
    }
    public void goToNextQues(){
        idCurQues++;
    }
    public void backToPrevQues(){
        idCurQues--;
    }
    public void summary(){
        status = "preview";
        int score = 0;
        int sizeExam = curExam.getQuestions().size();
        for (int i = 0; i < sizeExam; i++) {
            if (ans[i] != null){
                if (curExam.getQuestions().get(i).getRightAnswer() == ans[i]){ 
                    score++;  
                    if (quesFalseList.contains(curExam.getQuestions().get(i))){
                        quesFalseList.remove(curExam.getQuestions().get(i));
                    }
                } else if (!quesFalseList.contains(curExam.getQuestions().get(i))){
                    quesFalseList.add(curExam.getQuestions().get(i));
                    quesFalseList.add(curExam.getQuestions().get(i));
                    quesFalseList.add(curExam.getQuestions().get(i));
                }
            }
        }
        curExam.setHighscore(score);
    }
    
    public void createNewExamCode(){
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
    }
    
    public MultiChoiceQuestion getCurFalseQues(){
        return quesFalseList.get(idCurFalseQues);
    }
    public void removeCurFalseQues(){
        quesFalseList.remove(idCurFalseQues);
    }
    public Integer getidRandomNextFalseQues(){
        return generator.nextInt(quesFalseList.size());
    }
    
    public void restoreData(){
        MultiChoiceQuestion ques1 = new MultiChoiceQuestion("Q001", "What part of the roadway is used for vehicular traffic?" , "", 1, new String[]{"Road surface and curb","The part of the driveway","Motor vehicle road part"});
        MultiChoiceQuestion ques2 = new MultiChoiceQuestion("Q002", "What is a “lane”?", "",1, new String[]{
            "As part of the part of the roadway that can be run along the length of the road, used for vehicles",
            "As part of a longitudinally divided portion of the roadway, having a width sufficient for safe driving",
            "As part of a longitudinally divided part of the roadway, with enough width for cars to run safely"
        });
        MultiChoiceQuestion ques3 = new MultiChoiceQuestion("Q003", "How is the concept of \"limited gauge of road\" understood?", "",0, new String[]{
            "A space with limited dimensions in terms of height and width of roads, bridges, ferry terminals, road tunnels for vehicles including goods loaded on vehicles to pass safely",
            "A space with limited dimensions on the width of roads, bridges, ferries, tunnels on roads for vehicles including goods loaded on vehicles to pass safely",
            "A space with limited size on the height of roads, bridges, ferries, tunnels on roads for vehicles to pass safely"
        });        
        MultiChoiceQuestion ques4 = new MultiChoiceQuestion("Q004", "In the following concepts, how is \"separator\" understood correctly?", "",2, new String[]{
            "Part of the road to separate vehicles from entering unauthorized places",
            "Is a part used to separate the roadway and traffic safety corridor",
            "Is a part of the road to divide the road surface into two separate driving directions or to divide the road part of motor vehicles and rudimentary vehicles"
        });  
        MultiChoiceQuestion ques5 = new MultiChoiceQuestion("Q005", "What are the types of “separators” on the road?", "",0, new String[]{
            "Separator of fixed type and mobile type",
            "Separators include noise walls, hard guardrails and soft guardrails",
            "The divider includes the price of the dragon gate and the road sign"
        });
        MultiChoiceQuestion ques6 = new MultiChoiceQuestion("Q006", "Is the act of bringing motor vehicles and special-use motorcycles that do not meet technical safety and environmental protection standards to participate in road traffic?", "",1, new String[]{
            "Not strictly prohibited",
            "Prohibited",
            "Prohibited depending on the route",
            "Prohibited depending on vehicle type"
        });
        MultiChoiceQuestion ques7 = new MultiChoiceQuestion("Q007", "Are you driving in front of an ambulance that is signaling priority, is it okay to overtake?", "",0, new String[]{
            "Do not pass",
            "Passing while on the bridge",
            "It is allowed to pass when passing an intersection with few vehicles participating in traffic",
            "Passing when it is safe"
        });
        MultiChoiceQuestion ques8 = new MultiChoiceQuestion("Q008", "The signboard has a circle shape, red border, white background, on the background is a drawing of numbers and black writing, which type of sign is below?", "prohibition-sign.png",1, new String[]{
            "Danger sign",
            "Prohibition sign",
            "Command signboard",
            "Signposts indicating"
        });
        MultiChoiceQuestion ques9 = new MultiChoiceQuestion("Q009", "Which of the following is a sign showing a circle with a blue background with white drawings?", "signs-indicating-orders-to-be-executed.png",2, new String[]{
            "Danger sign",
            "Prohibition sign",
            "Command signboard",
            "Signposts indicating"
        });        
        MultiChoiceQuestion ques10 = new MultiChoiceQuestion("Q010", "What is the correct order of traffic?", "police-car-first.png",0, new String[]{
            "Police cars, cars, trucks, passenger cars",
            "Police cars, passenger cars, cars, trucks",
            "Police cars, trucks, passenger cars, cars",
            "Cars, police cars, trucks, passenger cars"
        });
        
        quesList = new ArrayList<>();
        
        quesList.add(ques1);
        quesList.add(ques2);
        quesList.add(ques3);
        quesList.add(ques4);
        quesList.add(ques5);
        quesList.add(ques6);
        quesList.add(ques7);
        quesList.add(ques8);
        quesList.add(ques9);
        quesList.add(ques10);
              
        saveFile();
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

    public Integer getTimeExam() {
        return timeExam;
    }

    public void setTimeExam(Integer timeExam) {
        this.timeExam = timeExam;
    }

    public Random getGenerator() {
        return generator;
    }

    public void setGenerator(Random generator) {
        this.generator = generator;
    }

    public ArrayList<Exam> getExamList() {
        return examList;
    }

    public void setExamList(ArrayList<Exam> examList) {
        this.examList = examList;
    }

    public ArrayList<MultiChoiceQuestion> getQuesList() {
        return quesList;
    }

    public void setQuesList(ArrayList<MultiChoiceQuestion> quesList) {
        this.quesList = quesList;
    }

    public ArrayList<MultiChoiceQuestion> getQuesFalseList() {
        return quesFalseList;
    }

    public void setQuesFalseList(ArrayList<MultiChoiceQuestion> quesFalseList) {
        this.quesFalseList = quesFalseList;
    }

    public Integer[] getAns() {
        return ans;
    }

    public void setAns(Integer[] ans) {
        this.ans = ans;
    }

    public Exam getCurExam() {
        return curExam;
    }

    public void setCurExam(Exam curExam) {
        this.curExam = curExam;
    }

    public Integer getCurQues() {
        return idCurQues;
    }

    public void setCurQues(Integer curQues) {
        this.idCurQues = curQues;
    }

    public Integer getidCurFalseQues() {
        return idCurFalseQues;
    }

    public void setidCurFalseQues(Integer idCurFalseQues) {
        this.idCurFalseQues = idCurFalseQues;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public void exportExam(Exam exam, File fileToSave){
                Document document = new Document();
                Section section = document.addSection();
                
                // Add some paragraphs with different styles and alignments
                ParagraphStyle styleHeader = new ParagraphStyle(document);
                styleHeader.setName("Header");
                styleHeader.getCharacterFormat().setFontName("Arial");
                styleHeader.getCharacterFormat().setFontSize(18);
                styleHeader.getCharacterFormat().setTextColor(java.awt.Color.BLUE);
                styleHeader.getCharacterFormat().setBold(true);
                document.getStyles().add(styleHeader);

                Paragraph paraName = section.addParagraph();
                paraName.setText("Exam " + exam.getId());
                paraName.applyStyle(styleHeader.getName());
                paraName.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                
                Paragraph paraQuesNum;
                Paragraph paraQuestion;
                Paragraph paraPic;
                int i = 0;
                for (MultiChoiceQuestion ques: exam.getQuestions()) {
                    i++;
                    paraQuesNum = section.addParagraph();
                    TextRange tr = paraQuesNum.appendText("Question " + i + ":");
                    tr.getCharacterFormat().setBold(true);
                    
                    paraPic = section.addParagraph();
                    paraPic.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                    String imagePath = "./src/img/" + ques.getPicture();
                    File file = new File(imagePath);
                    try {
                        BufferedImage image = ImageIO.read(file);
                        if (image.getWidth()>500){
                            DocPicture picture = paraPic.appendPicture(imagePath);
                            picture.setWidth(300f);
                            picture.setHeight(200f);  
                        } else {
                            DocPicture picture = paraPic.appendPicture(imagePath);
                        }
                    } catch (IOException ex) {
//                        System.out.println("No Image");
                    }
                  

                    paraQuestion = section.addParagraph();
                    paraQuestion.setText(ques.getQuestion());
                    
                    section.addParagraph();
                    
                    int j = 0;
                    char c = 'A';
                    Paragraph paraAns;
                    for (String ans: ques.getAnswer()) {
                        paraAns = section.addParagraph();
                        paraAns.setText((char)(c+j) + ". " + ans);
                        j++;
                    }
                    section.addParagraph();
                }
                
                
//                Paragraph para1 = section.addParagraph();
//                para1.setText("This is the first paragraph.");
//                para1.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
//
//                Paragraph paraPic = section.addParagraph();
//
//                section.addParagraph();
//
//                Paragraph para2 = section.addParagraph();
//                para2.setText("This is the second paragraph.");
//                para2.applyStyle(styleHeader.getName());
//
//                // Add a picture from a file
//                String imagePath = "./src/img/left.png";
//                DocPicture picture = paraPic.appendPicture(imagePath);
//                picture.setWidth(200f);
//                picture.setHeight(150f);
//
//                Paragraph para3 = section.addParagraph();
//                para3.setText("This is the third paragraph.");
                
                try {
                    document.saveToFile(fileToSave.getAbsolutePath(), FileFormat.Doc);
                    System.out.println("File saved successfully.");
                } catch (Exception e) {
                    System.out.println("File saved failed. Maybe because file being open.");
                }

    }
}
