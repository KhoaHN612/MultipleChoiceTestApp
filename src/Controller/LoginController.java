/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Exam;
import Model.MultiChoiceQuestion;
import Model.User;
import java.util.ArrayList;
import java.util.List;
import lib.XFile;

/**
 *
 * @author PC
 */
public class LoginController {
    private String filePath; 
    private ArrayList<User> userList;

    public LoginController() {
        filePath = "./src/Data/user.dat";
        userList = new ArrayList<>();
        
        loadFile();
    }
    private boolean loadFile(){
        if (XFile.readObject(filePath)==null){
            return false;
        }
        userList = (ArrayList<User>) XFile.readObject(filePath);
        
        return true;  
    }
    
    public void saveFile(){
        XFile.writeObject(filePath, userList);
    }
    
    public void addUser(User user){
        userList.add(user);
        saveFile();
    }
    
    public String checkUser(User user){
        if (userList.contains(user)){
            User oUser = userList.get(userList.indexOf(user));
            return oUser.getRole();
        }
        return null;
    } 
}

