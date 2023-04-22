/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.HomeController;
import Model.Exam;
import Model.MultiChoiceQuestion;
import Model.User;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.ParagraphStyle;
import com.spire.doc.fields.DocPicture;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import lib.XFile;
import lib.XTimer;
import lib.XTimer.CountdownFinishedListener;

/**
 *
 * @author PC
 */
public class Home extends javax.swing.JFrame {
    
    CardLayout cardLayout;  
    DefaultComboBoxModel cbModel;
    XTimer clock;
    HomeController controller;
    Login loginView;
    Integer idExam = -1;
    
    public Home() {
        initComponents();
        this.setTitle("Main");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        
        cardLayout = (CardLayout) Content.getLayout();
        cardLayout.show(Content, "Home");
        
        lbWelcome.setText("Welcome, Guest");
        
        controller = new HomeController();

        loadComboExam();
        controller.saveFile();
    }
    
    public Home (User user, Login aThis){
        initComponents();
        this.setTitle("Application");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        
        cardLayout = (CardLayout) Content.getLayout();
        cardLayout.show(Content, "Home");
        
        controller = new HomeController();
        lbWelcome.setText("Welcome, " + user.getUsername());
        loginView = aThis;
        
        loadComboExam();
        controller.saveFile();
    }
    
    private void loadComboExam(){
        cbModel = new DefaultComboBoxModel();
        cbModel.addElement("- Choose exam code -");
        for (Exam exam : controller.getExamList()) {
            cbModel.addElement("Exam " + exam.getId());
        }
        cbExam.setModel(cbModel);
    }
    
    private void fillQuestion(MultiChoiceQuestion ques){
        questionPane.setText(null);
        grChoice.clearSelection();
        
        StyledDocument doc = questionPane.getStyledDocument();
        
        try {
            // Add text to the StyledDocument
            doc.insertString(doc.getLength(), "\n" + ques.getQuestion(), null);

            ImageIcon icon = new ImageIcon("src/img/" + ques.getPicture());

            // Get the original image from the ImageIcon
            Image originalImage = icon.getImage();

            // Scale the image to fit within a container
            int maxWidth = questionPane.getWidth()-10; // maximum width of the container
            int maxHeight = 150; // maximum height of the container
            Image scaledImage = originalImage.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
            
            if (originalImage.getWidth(panelAnswer)<questionPane.getWidth()-10){
                scaledImage = originalImage;
            }

            // Create a new ImageIcon from the scaled image
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            questionPane.setCaretPosition(0); // Set caret position to the beginning
            questionPane.insertIcon(scaledIcon); // Insert the image as an icon
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        String[] answer = ques.getAnswer();
        int nAnswer = answer.length;
        for (Component c: panelAnswer.getComponents()) {
            if (c instanceof JRadioButton){
                JRadioButton btn = (JRadioButton) c;
                btn.setForeground(Color.black);
                if (Integer.parseInt(btn.getActionCommand())<nAnswer){
                    c.setVisible(true);
                    btn.setText("<html>"+answer[Integer.parseInt(btn.getActionCommand())]+"</html>");
                }else{
                    c.setVisible(false);
                }
            }
        }
        
        lbQuesNum.setText("Question " + (controller.getCurQues()+1));
        if (controller.getCurQues() == 0) btnPrev.setEnabled(false);
            else btnPrev.setEnabled(true);
        if (controller.getCurQues() == controller.getCurExam().getQuestions().size()-1) btnNext.setEnabled(false);
            else btnNext.setEnabled(true);
        
        if (controller.getStatus() == "preview"){
            previewQuestion(ques);
        }
    }
    
    private void fillQuestion(MultiChoiceQuestion ques, int answer){
        fillQuestion(ques);
        for (Component c: panelAnswer.getComponents()) {
            if (c instanceof JRadioButton){
                JRadioButton btn = (JRadioButton) c;
                if (Integer.parseInt(btn.getActionCommand())==answer){
                    btn.setSelected(true);
                }
            }
        }
    }
    
    private void previewQuestion(MultiChoiceQuestion ques){
        int answer = controller.CurAns()==null?-1:controller.CurAns();
        for (Component c: panelAnswer.getComponents()) {
            if (c instanceof JRadioButton){
                JRadioButton btn = (JRadioButton) c;
                btn.setForeground(Color.black);
                if (Integer.parseInt(btn.getActionCommand())==answer){
                    btn.setSelected(true);
                    btn.setForeground(Color.red);
                }
                if (Integer.parseInt(btn.getActionCommand())==ques.getRightAnswer()){
                    btn.setForeground(Color.green);
                }
            }
        }
    }
   
//    private boolean loadFile(){
//        if (XFile.readObject(filePath+"exam.dat")==null){
//            return false;
//        }
//        examList = (ArrayList<Exam>) XFile.readObject(filePath+"exam.dat");
//        
//        if (XFile.readObject(filePath+"question.dat")==null){
//            return false;
//        }
//        quesList = (ArrayList<MultiChoiceQuestion>) XFile.readObject(filePath+"question.dat");
//        
//        if (XFile.readObject(filePath+"questionfalse.dat")==null){
//            return false;
//        }
//        quesFalseList = (ArrayList<MultiChoiceQuestion>) XFile.readObject(filePath+"questionfalse.dat");
//        
//        return true;  
//    }
//    
//    private void saveFile(){
//        XFile.writeObject(filePath+"exam.dat", examList);
//        XFile.writeObject(filePath+"question.dat", quesList);
//        XFile.writeObject(filePath+"questionfalse.dat", quesFalseList);
//    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grChoice = new javax.swing.ButtonGroup();
        grChoice1 = new javax.swing.ButtonGroup();
        MainPanel = new javax.swing.JPanel();
        NavBar = new javax.swing.JPanel();
        lbExit = new javax.swing.JLabel();
        lbHome = new javax.swing.JLabel();
        lbExam = new javax.swing.JLabel();
        lbMistake = new javax.swing.JLabel();
        Content = new javax.swing.JPanel();
        Question = new javax.swing.JPanel();
        Header = new javax.swing.JPanel();
        pnButton = new javax.swing.JPanel();
        btnSubmit = new javax.swing.JButton();
        btnReturn = new javax.swing.JButton();
        lbTime = new javax.swing.JLabel();
        Body = new javax.swing.JPanel();
        panelAnswer = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        questionPane = new javax.swing.JTextPane();
        Footer = new javax.swing.JPanel();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lbQuesNum = new javax.swing.JLabel();
        Home = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lbWelcome = new javax.swing.JLabel();
        Document = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        Exam = new javax.swing.JPanel();
        cbExam = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        txtScore = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        txtNumQues = new javax.swing.JLabel();
        btnStart = new javax.swing.JButton();
        btnCreateExamCode = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        Summary = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtScore1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        txtNumQues1 = new javax.swing.JLabel();
        btnPreview = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        Mistake = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnStartMistake = new javax.swing.JButton();
        FalseQuestion = new javax.swing.JPanel();
        Header1 = new javax.swing.JPanel();
        lbTime1 = new javax.swing.JLabel();
        Body1 = new javax.swing.JPanel();
        panelAnswer1 = new javax.swing.JPanel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        questionPane1 = new javax.swing.JTextPane();
        Footer1 = new javax.swing.JPanel();
        btnCheckNext = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        MainPanel.setLayout(new javax.swing.BoxLayout(MainPanel, javax.swing.BoxLayout.X_AXIS));

        NavBar.setBackground(new java.awt.Color(10, 77, 104));
        NavBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        NavBar.setMinimumSize(new java.awt.Dimension(150, 400));
        NavBar.setPreferredSize(new java.awt.Dimension(150, 400));

        lbExit.setBackground(new java.awt.Color(237, 198, 177));
        lbExit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbExit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout.png"))); // NOI18N
        lbExit.setText("Exit");
        lbExit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lbExit.setOpaque(true);
        lbExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbExitMousePressed(evt);
            }
        });

        lbHome.setBackground(new java.awt.Color(8, 131, 149));
        lbHome.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbHome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/home.png"))); // NOI18N
        lbHome.setText("HOME");
        lbHome.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbHome.setOpaque(true);
        lbHome.setPreferredSize(new java.awt.Dimension(100, 100));
        lbHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbHomeMousePressed(evt);
            }
        });

        lbExam.setBackground(new java.awt.Color(0, 255, 202));
        lbExam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbExam.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbExam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exam.png"))); // NOI18N
        lbExam.setText("EXAM");
        lbExam.setOpaque(true);
        lbExam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbExamMousePressed(evt);
            }
        });

        lbMistake.setBackground(new java.awt.Color(145, 127, 179));
        lbMistake.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbMistake.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbMistake.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/error.png"))); // NOI18N
        lbMistake.setText("MISTAKE");
        lbMistake.setOpaque(true);
        lbMistake.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbMistakeMousePressed(evt);
            }
        });

        javax.swing.GroupLayout NavBarLayout = new javax.swing.GroupLayout(NavBar);
        NavBar.setLayout(NavBarLayout);
        NavBarLayout.setHorizontalGroup(
            NavBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbHome, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
            .addComponent(lbExam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbMistake, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        NavBarLayout.setVerticalGroup(
            NavBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavBarLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(lbHome, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbMistake, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbExam, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 410, Short.MAX_VALUE)
                .addComponent(lbExit, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        MainPanel.add(NavBar);

        Content.setLayout(new java.awt.CardLayout());

        Question.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Question.setPreferredSize(new java.awt.Dimension(450, 400));
        Question.setLayout(new java.awt.BorderLayout());

        Header.setBackground(new java.awt.Color(102, 204, 255));
        Header.setPreferredSize(new java.awt.Dimension(450, 50));

        pnButton.setOpaque(false);

        btnSubmit.setText("Submit");
        btnSubmit.setPreferredSize(new java.awt.Dimension(72, 40));
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        pnButton.add(btnSubmit);

        btnReturn.setText("Return");
        btnReturn.setPreferredSize(new java.awt.Dimension(72, 40));
        btnReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnActionPerformed(evt);
            }
        });
        pnButton.add(btnReturn);

        lbTime.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeaderLayout.createSequentialGroup()
                .addGap(0, 216, Short.MAX_VALUE)
                .addComponent(lbTime, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(pnButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Question.add(Header, java.awt.BorderLayout.NORTH);

        Body.setBackground(new java.awt.Color(204, 255, 255));
        Body.setPreferredSize(new java.awt.Dimension(450, 300));

        panelAnswer.setOpaque(false);

        grChoice.add(jRadioButton1);
        jRadioButton1.setToolTipText("");
        jRadioButton1.setActionCommand("0");
        jRadioButton1.setPreferredSize(new java.awt.Dimension(3075, 80));

        grChoice.add(jRadioButton5);
        jRadioButton5.setToolTipText("");
        jRadioButton5.setActionCommand("1");
        jRadioButton5.setPreferredSize(new java.awt.Dimension(3075, 80));

        grChoice.add(jRadioButton6);
        jRadioButton6.setToolTipText("");
        jRadioButton6.setActionCommand("2");
        jRadioButton6.setPreferredSize(new java.awt.Dimension(3075, 80));

        grChoice.add(jRadioButton7);
        jRadioButton7.setToolTipText("");
        jRadioButton7.setActionCommand("3");
        jRadioButton7.setPreferredSize(new java.awt.Dimension(3075, 80));

        javax.swing.GroupLayout panelAnswerLayout = new javax.swing.GroupLayout(panelAnswer);
        panelAnswer.setLayout(panelAnswerLayout);
        panelAnswerLayout.setHorizontalGroup(
            panelAnswerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAnswerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAnswerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addComponent(jRadioButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addComponent(jRadioButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addComponent(jRadioButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelAnswerLayout.setVerticalGroup(
            panelAnswerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAnswerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        questionPane.setEditable(false);
        jScrollPane3.setViewportView(questionPane);

        javax.swing.GroupLayout BodyLayout = new javax.swing.GroupLayout(Body);
        Body.setLayout(BodyLayout);
        BodyLayout.setHorizontalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAnswer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        BodyLayout.setVerticalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelAnswer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Question.add(Body, java.awt.BorderLayout.CENTER);

        Footer.setBackground(new java.awt.Color(102, 204, 255));
        Footer.setPreferredSize(new java.awt.Dimension(450, 50));

        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/left.png"))); // NOI18N
        btnPrev.setText("Prev");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/right.png"))); // NOI18N
        btnNext.setText("Next");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        lbQuesNum.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbQuesNum.setText("Question");

        javax.swing.GroupLayout FooterLayout = new javax.swing.GroupLayout(Footer);
        Footer.setLayout(FooterLayout);
        FooterLayout.setHorizontalGroup(
            FooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FooterLayout.createSequentialGroup()
                .addContainerGap(217, Short.MAX_VALUE)
                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(lbQuesNum)
                .addGap(56, 56, 56)
                .addComponent(btnNext)
                .addGap(20, 20, 20))
        );
        FooterLayout.setVerticalGroup(
            FooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FooterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbQuesNum))
                .addGap(35, 35, 35))
        );

        Question.add(Footer, java.awt.BorderLayout.SOUTH);

        Content.add(Question, "Question");

        Home.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/car.gif"))); // NOI18N

        lbWelcome.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lbWelcome.setText("Welcome, ");

        javax.swing.GroupLayout HomeLayout = new javax.swing.GroupLayout(Home);
        Home.setLayout(HomeLayout);
        HomeLayout.setHorizontalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomeLayout.createSequentialGroup()
                .addContainerGap(142, Short.MAX_VALUE)
                .addGroup(HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HomeLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(172, 172, 172))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HomeLayout.createSequentialGroup()
                        .addComponent(lbWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75))))
        );
        HomeLayout.setVerticalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomeLayout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(lbWelcome)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addContainerGap(368, Short.MAX_VALUE))
        );

        Content.add(Home, "Home");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 538, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout DocumentLayout = new javax.swing.GroupLayout(Document);
        Document.setLayout(DocumentLayout);
        DocumentLayout.setHorizontalGroup(
            DocumentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DocumentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        DocumentLayout.setVerticalGroup(
            DocumentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DocumentLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(348, Short.MAX_VALUE))
        );

        Content.add(Document, "Document");

        cbExam.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbExam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbExamActionPerformed(evt);
            }
        });

        txtScore.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtScore.setForeground(new java.awt.Color(0, 255, 51));
        txtScore.setText("...");

        txtNumQues.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtNumQues.setForeground(new java.awt.Color(255, 51, 0));
        txtNumQues.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtNumQues.setText("...");
        txtNumQues.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(txtScore, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNumQues, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtScore)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtNumQues)
                .addGap(35, 35, 35))
        );

        btnStart.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        btnCreateExamCode.setText("Create new exam code");
        btnCreateExamCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateExamCodeActionPerformed(evt);
            }
        });

        btnExport.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnExport.setText("Export to Doc file");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ExamLayout = new javax.swing.GroupLayout(Exam);
        Exam.setLayout(ExamLayout);
        ExamLayout.setHorizontalGroup(
            ExamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamLayout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addGroup(ExamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ExamLayout.createSequentialGroup()
                        .addGroup(ExamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(ExamLayout.createSequentialGroup()
                                .addComponent(btnCreateExamCode)
                                .addGap(16, 16, 16))
                            .addComponent(btnExport))
                        .addGap(176, 176, 176))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ExamLayout.createSequentialGroup()
                        .addComponent(cbExam, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(144, 144, 144))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ExamLayout.createSequentialGroup()
                        .addComponent(btnStart)
                        .addGap(226, 226, 226))))
        );
        ExamLayout.setVerticalGroup(
            ExamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExamLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(btnCreateExamCode)
                .addGap(35, 35, 35)
                .addComponent(cbExam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(btnExport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
                .addComponent(btnStart)
                .addGap(90, 90, 90))
        );

        Content.add(Exam, "Exam");

        Summary.setPreferredSize(new java.awt.Dimension(550, 1000));

        jLabel3.setFont(new java.awt.Font("Sitka Display", 0, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Congratulations");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FIREWORKS.gif"))); // NOI18N

        txtScore1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtScore1.setForeground(new java.awt.Color(51, 255, 0));
        txtScore1.setText("a");

        txtNumQues1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtNumQues1.setForeground(new java.awt.Color(255, 51, 0));
        txtNumQues1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtNumQues1.setText("b");
        txtNumQues1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(txtScore1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNumQues1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtScore1)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtNumQues1)
                .addGap(35, 35, 35))
        );

        btnPreview.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnPreview.setText("Preview");
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Your score is");

        javax.swing.GroupLayout SummaryLayout = new javax.swing.GroupLayout(Summary);
        Summary.setLayout(SummaryLayout);
        SummaryLayout.setHorizontalGroup(
            SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SummaryLayout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addGroup(SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SummaryLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SummaryLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(197, 197, 197))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SummaryLayout.createSequentialGroup()
                        .addComponent(btnPreview)
                        .addGap(212, 212, 212))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SummaryLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(160, 160, 160))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SummaryLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(219, 219, 219))))
        );
        SummaryLayout.setVerticalGroup(
            SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SummaryLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(20, 20, 20)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnPreview)
                .addGap(439, 439, 439))
        );

        Content.add(Summary, "Summary");

        Mistake.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/true-false.gif"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Script MT Bold", 0, 24)); // NOI18N
        jLabel6.setText("CORRECT YOUR MISTAKE");

        btnStartMistake.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnStartMistake.setText("Start");
        btnStartMistake.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartMistakeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MistakeLayout = new javax.swing.GroupLayout(Mistake);
        Mistake.setLayout(MistakeLayout);
        MistakeLayout.setHorizontalGroup(
            MistakeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MistakeLayout.createSequentialGroup()
                .addContainerGap(122, Short.MAX_VALUE)
                .addGroup(MistakeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MistakeLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(202, 202, 202))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MistakeLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(118, 118, 118))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MistakeLayout.createSequentialGroup()
                        .addComponent(btnStartMistake)
                        .addGap(231, 231, 231))))
        );
        MistakeLayout.setVerticalGroup(
            MistakeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MistakeLayout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(80, 80, 80)
                .addComponent(btnStartMistake)
                .addContainerGap(247, Short.MAX_VALUE))
        );

        Content.add(Mistake, "Mistake");

        FalseQuestion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        FalseQuestion.setPreferredSize(new java.awt.Dimension(450, 400));
        FalseQuestion.setLayout(new java.awt.BorderLayout());

        Header1.setBackground(new java.awt.Color(102, 204, 255));
        Header1.setPreferredSize(new java.awt.Dimension(450, 50));

        lbTime1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbTime1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout Header1Layout = new javax.swing.GroupLayout(Header1);
        Header1.setLayout(Header1Layout);
        Header1Layout.setHorizontalGroup(
            Header1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Header1Layout.createSequentialGroup()
                .addGap(0, 365, Short.MAX_VALUE)
                .addComponent(lbTime1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94))
        );
        Header1Layout.setVerticalGroup(
            Header1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Header1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTime1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        FalseQuestion.add(Header1, java.awt.BorderLayout.NORTH);

        Body1.setBackground(new java.awt.Color(204, 255, 255));
        Body1.setPreferredSize(new java.awt.Dimension(450, 300));

        panelAnswer1.setOpaque(false);

        grChoice1.add(jRadioButton2);
        jRadioButton2.setToolTipText("");
        jRadioButton2.setActionCommand("0");
        jRadioButton2.setPreferredSize(new java.awt.Dimension(3075, 80));

        grChoice1.add(jRadioButton8);
        jRadioButton8.setToolTipText("");
        jRadioButton8.setActionCommand("1");
        jRadioButton8.setPreferredSize(new java.awt.Dimension(3075, 80));

        grChoice1.add(jRadioButton9);
        jRadioButton9.setToolTipText("");
        jRadioButton9.setActionCommand("2");
        jRadioButton9.setPreferredSize(new java.awt.Dimension(3075, 80));

        grChoice1.add(jRadioButton10);
        jRadioButton10.setToolTipText("");
        jRadioButton10.setActionCommand("3");
        jRadioButton10.setPreferredSize(new java.awt.Dimension(3075, 80));

        javax.swing.GroupLayout panelAnswer1Layout = new javax.swing.GroupLayout(panelAnswer1);
        panelAnswer1.setLayout(panelAnswer1Layout);
        panelAnswer1Layout.setHorizontalGroup(
            panelAnswer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAnswer1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAnswer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addComponent(jRadioButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addComponent(jRadioButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addComponent(jRadioButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelAnswer1Layout.setVerticalGroup(
            panelAnswer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAnswer1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        questionPane1.setEditable(false);
        jScrollPane4.setViewportView(questionPane1);

        javax.swing.GroupLayout Body1Layout = new javax.swing.GroupLayout(Body1);
        Body1.setLayout(Body1Layout);
        Body1Layout.setHorizontalGroup(
            Body1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Body1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Body1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAnswer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );
        Body1Layout.setVerticalGroup(
            Body1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Body1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelAnswer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        FalseQuestion.add(Body1, java.awt.BorderLayout.CENTER);

        Footer1.setBackground(new java.awt.Color(102, 204, 255));
        Footer1.setPreferredSize(new java.awt.Dimension(450, 50));

        btnCheckNext.setText("Check");
        btnCheckNext.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnCheckNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckNextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Footer1Layout = new javax.swing.GroupLayout(Footer1);
        Footer1.setLayout(Footer1Layout);
        Footer1Layout.setHorizontalGroup(
            Footer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Footer1Layout.createSequentialGroup()
                .addContainerGap(454, Short.MAX_VALUE)
                .addComponent(btnCheckNext)
                .addGap(20, 20, 20))
        );
        Footer1Layout.setVerticalGroup(
            Footer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Footer1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCheckNext, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        FalseQuestion.add(Footer1, java.awt.BorderLayout.SOUTH);

        Content.add(FalseQuestion, "FalseQuestion");

        MainPanel.add(Content);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lbExitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbExitMousePressed
        // TODO add your handling code here:
        controller.saveFile();
        if (JOptionPane.showConfirmDialog(this, "Are you sure wannna exit?", "Exit" , JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION)
            System.exit(0);
    }//GEN-LAST:event_lbExitMousePressed

    private void lbHomeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbHomeMousePressed
        // TODO add your handling code here:
        cardLayout.show(Content, "Home");
    }//GEN-LAST:event_lbHomeMousePressed

    private void lbExamMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbExamMousePressed
        // TODO add your handling code here:
        cardLayout.show(Content, "Exam");
    }//GEN-LAST:event_lbExamMousePressed

    private void btnCreateExamCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateExamCodeActionPerformed
        // TODO add your handling code here:
        controller.createNewExamCode();
        loadComboExam();
    }//GEN-LAST:event_btnCreateExamCodeActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        if (grChoice.getSelection() != null) controller.chooseAnswer(Integer.parseInt(grChoice.getSelection().getActionCommand()));
        controller.goToNextQues();
        if (controller.CurAns() == null){
            fillQuestion(controller.CurQues());
        } else {
            fillQuestion(controller.CurQues(),controller.CurAns());
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        if (grChoice.getSelection() != null) controller.chooseAnswer(Integer.parseInt(grChoice.getSelection().getActionCommand()));
        controller.backToPrevQues();
        if (controller.CurAns() == null){
            fillQuestion(controller.CurQues());
        } else {
            fillQuestion(controller.CurQues(),controller.CurAns());
        }
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        if (grChoice.getSelection() != null) controller.chooseAnswer(Integer.parseInt(grChoice.getSelection().getActionCommand()));
        if (JOptionPane.showConfirmDialog(this, "Are you sure want to submit?","Confirm",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            Submit();
            controller.saveFile();
        }
        clock.stop();
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void Submit(){
        
        btnSubmit.setEnabled(false);
        cardLayout.show(Content, "Summary");

        controller.summary();

        
        txtScore1.setText(controller.getCurExam().getHighscore()+ "");
        txtNumQues1.setText(controller.getCurExam().getQuestions().size()+ "");
    }
    
    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        // TODO add your handling code here:
        cardLayout.show(Content, "Question");
        controller.setCurQues(0);
        if (controller.CurAns() == null){
            fillQuestion(controller.CurQues());
        } else {
            fillQuestion(controller.CurQues(),controller.CurAns());
        }
    }//GEN-LAST:event_btnPreviewActionPerformed

    private void btnReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnActionPerformed
        // TODO add your handling code here:
       cardLayout.show(Content, "Exam");
       clock.stop();
    }//GEN-LAST:event_btnReturnActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        // TODO add your handling code here:
        if (cbExam.getSelectedIndex()>0){
            if (controller.getExamList().get(cbExam.getSelectedIndex()-1).getQuestions().isEmpty()){
                JOptionPane.showMessageDialog(this, "Cannot start empty exam. Contect admin for add more question to exam");
                return;
            }
            btnSubmit.setEnabled(true);
//            curExam = examList.get(cbExam.getSelectedIndex()-1);
            controller.startExam(cbExam.getSelectedIndex()-1);
//            ans = new Integer[curExam.getQuestions().size()];
            cardLayout.show(Content, "Question");
//            curQues = 0;
//            status = "doing";
            fillQuestion(controller.CurQues());
            
            clock = new XTimer(controller.getTimeExam(), lbTime, new CountdownFinishedListener() {
            @Override
                public void onCountdownFinished() {           
                    if (grChoice.getSelection() != null) controller.chooseAnswer(Integer.parseInt(grChoice.getSelection().getActionCommand()));
                    JOptionPane.showMessageDialog(Home, "Time's up!");
                    Submit();
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Please choose exam code");
        }
    }//GEN-LAST:event_btnStartActionPerformed

    private void cbExamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbExamActionPerformed
        // TODO add your handling code here:
        if(cbExam.getSelectedIndex()>0){
            txtScore.setText(controller.HighScore(cbExam.getSelectedIndex()-1)+"");
            txtNumQues.setText(controller.SizeExam(cbExam.getSelectedIndex()-1)+"");
            idExam = cbExam.getSelectedIndex()-1;
        }
    }//GEN-LAST:event_cbExamActionPerformed

    private void lbMistakeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbMistakeMousePressed
        // TODO add your handling code here:
        cardLayout.show(Content, "Mistake");
    }//GEN-LAST:event_lbMistakeMousePressed

    private void btnCheckNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckNextActionPerformed
        // TODO add your handling code here:
        boolean isRight = false;
        if (btnCheckNext.getText().equals("Check")){
            btnCheckNext.setText("Next");
            btnCheckNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/right.png")));
            isRight = checkFalseQuestion(controller.getCurFalseQues());
        } else {
            btnCheckNext.setText("Check");
            btnCheckNext.setIcon(null);
            if (isRight == true) controller.removeCurFalseQues();
            fillFalseQuestion(controller.getidRandomNextFalseQues());
        }
    }//GEN-LAST:event_btnCheckNextActionPerformed

    private void btnStartMistakeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartMistakeActionPerformed
        // TODO add your handling code here:
        cardLayout.show(Content, "FalseQuestion");
        fillFalseQuestion(controller.getidRandomNextFalseQues());
    }//GEN-LAST:event_btnStartMistakeActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
        if (idExam<0){
            JOptionPane.showMessageDialog(this, "Please choose one exam");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As...");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".doc")) {
                filePath += ".doc";
                fileToSave = new File(filePath);
            }        
            controller.exportExam(controller.getExamList().get(idExam),fileToSave);            
        } 

        
        
        
    }//GEN-LAST:event_btnExportActionPerformed

    private boolean checkFalseQuestion(MultiChoiceQuestion ques){
        int answer = -1;
        if (grChoice1.getSelection() != null) answer = Integer.parseInt(grChoice1.getSelection().getActionCommand());
        for (Component c: panelAnswer1.getComponents()) {
            if (c instanceof JRadioButton){
                JRadioButton btn = (JRadioButton) c;
                btn.setForeground(Color.black);
                if (Integer.parseInt(btn.getActionCommand())==answer){
                    btn.setSelected(true);
                    btn.setForeground(Color.red);
                }
                if (Integer.parseInt(btn.getActionCommand())==ques.getRightAnswer()){
                    btn.setForeground(Color.green);
                }
            }
        }
        return answer == ques.getRightAnswer();
    }
    
    private void fillFalseQuestion(int index){
        controller.setidCurFalseQues(index);
        MultiChoiceQuestion ques = controller.getCurFalseQues();
        questionPane1.setText(null);
        grChoice1.clearSelection();
        
        StyledDocument doc = questionPane1.getStyledDocument();
        
        try {
            // Add text to the StyledDocument
            doc.insertString(doc.getLength(), "\n" + ques.getQuestion(), null);

            ImageIcon icon = new ImageIcon("src/img/" + ques.getPicture());

            // Get the original image from the ImageIcon
            Image originalImage = icon.getImage();

            // Scale the image to fit within a container
            int maxWidth = questionPane1.getWidth()-10; // maximum width of the container
            int maxHeight = 150; // maximum height of the container
            Image scaledImage = originalImage.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);

            // Create a new ImageIcon from the scaled image
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            questionPane1.setCaretPosition(0); // Set caret position to the beginning
            questionPane1.insertIcon(scaledIcon); // Insert the image as an icon
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        String[] answer = ques.getAnswer();
        int nAnswer = answer.length;
        for (Component c: panelAnswer1.getComponents()) {
            if (c instanceof JRadioButton){
                JRadioButton btn = (JRadioButton) c;
                btn.setForeground(Color.black);
                if (Integer.parseInt(btn.getActionCommand())<nAnswer){
                    c.setVisible(true);
                    btn.setText("<html>"+answer[Integer.parseInt(btn.getActionCommand())]+"</html>");
                }else{
                    c.setVisible(false);
                }
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Body;
    private javax.swing.JPanel Body1;
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Document;
    private javax.swing.JPanel Exam;
    private javax.swing.JPanel FalseQuestion;
    private javax.swing.JPanel Footer;
    private javax.swing.JPanel Footer1;
    private javax.swing.JPanel Header;
    private javax.swing.JPanel Header1;
    private javax.swing.JPanel Home;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JPanel Mistake;
    private javax.swing.JPanel NavBar;
    private javax.swing.JPanel Question;
    private javax.swing.JPanel Summary;
    private javax.swing.JButton btnCheckNext;
    private javax.swing.JButton btnCreateExamCode;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnReturn;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnStartMistake;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> cbExam;
    private javax.swing.ButtonGroup grChoice;
    private javax.swing.ButtonGroup grChoice1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbExam;
    private javax.swing.JLabel lbExit;
    private javax.swing.JLabel lbHome;
    private javax.swing.JLabel lbMistake;
    private javax.swing.JLabel lbQuesNum;
    private javax.swing.JLabel lbTime;
    private javax.swing.JLabel lbTime1;
    private javax.swing.JLabel lbWelcome;
    private javax.swing.JPanel panelAnswer;
    private javax.swing.JPanel panelAnswer1;
    private javax.swing.JPanel pnButton;
    private javax.swing.JTextPane questionPane;
    private javax.swing.JTextPane questionPane1;
    private javax.swing.JLabel txtNumQues;
    private javax.swing.JLabel txtNumQues1;
    private javax.swing.JLabel txtScore;
    private javax.swing.JLabel txtScore1;
    // End of variables declaration//GEN-END:variables
}
