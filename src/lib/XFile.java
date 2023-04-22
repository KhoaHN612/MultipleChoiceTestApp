/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PC
 */
public class XFile {
    public static void writeFile(String path){
        File f = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            byte[] arr = {10,20,30};
            fos.write(arr);
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void readFile(String path){
        File f = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            int fileSize = (int) f.length();
            byte[] arr = new byte[fileSize];
            fis.read(arr);
            for (int i = 0; i < fileSize; i++) {
                System.out.println(arr[i]+ " ");
            }
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XFile.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    public static void writeDataFile(String path){
        try {
            FileOutputStream fos = new FileOutputStream(path);
            DataOutputStream dos = new DataOutputStream(fos);
            
            dos.writeInt(5);
            dos.writeUTF("Nhất Khoa");
            dos.writeDouble(8.7);
            
            dos.close();
        } catch (IOException e) {
            System.out.println("Failed");
        }
    }
 
    public static void writeBuffer(String path, String text){
        try{
            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(XFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String readBuffer(String path){
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String firstLine = br.readLine();
            while (firstLine!=null){
                sb.append(firstLine);
                firstLine = br.readLine();
                if (firstLine!=null) sb.append("\n");
            }
        } catch (IOException e) {
            System.out.println("Failed");
        }
        return sb.toString();
    }
    
    public static void readDataFile(String path){
        try {
            FileInputStream fis = new FileInputStream(path);
            DataInputStream dis = new DataInputStream(fis);
            
            System.out.println(dis.readInt());
            System.out.println(dis.readUTF());
            System.out.println(dis.readDouble());
            
            dis.close();
        } catch (IOException e) {
            System.out.println("Failed");
        }
    }
    
    public static void writeObject(String path, Object o){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(path)
            );
            oos.writeObject(o);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(XFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Object readObject(String path){
        try{
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(path)
            );
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (IOException ex) {
//            Logger.getLogger(XFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nothing to read");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(XFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void main(String[] args) {


    }
}
