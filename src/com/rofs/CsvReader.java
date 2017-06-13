package com.rofs;

import com.opencsv.CSVReader;

import java.io.*;
import java.util.*;

/**
 * Created by markla on 2017/6/7.
 */
public class CsvReader {
 public String ecoding(String s){

//         if (s != null) {
//             try {
//                 s= new String(s.getBytes("ISO-8859-1"), "UTF-8");
//             } catch (UnsupportedEncodingException e) {
//                 s="";
//                 e.printStackTrace();
//             }
//         }
     return s;
 }
    public LinkedList<Contact> read(String fileName) throws Exception{
        LinkedList<Contact> list=new LinkedList<Contact>();
        File file = new File(fileName);
        FileReader fReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fReader);
        String[] line;
        while((line=csvReader.readNext())!=null&&line.length>0){
         Contact temp=new Contact();
         String mailTemp=ecoding(line[0]);
         String nameTemp=ecoding(line[1]);
         String departmentTemp=ecoding(line[2]);
         temp.setMail(mailTemp);
         if(mailTemp.contains("@")){
             temp.setName(mailTemp.split("@")[0]);
         }
         temp.setRealName(nameTemp);
         temp.setDepartment(departmentTemp);
         list.add(temp);
        }
        csvReader.close();
        return  list;
    }

//    public static void main(String[] args) {
//        CsvReader re=new CsvReader();
//        try {
//            List<Contact> temp=re.read("C:\\Users\\markla\\Desktop\\LdapReader\\source\\import.csv");
//            System.out.println(temp.size());
//            for(Contact a:temp){
//                System.out.println(a.toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
