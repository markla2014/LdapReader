package com.rofs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;

public class Main {
    private static String FILE_NAME=null;
    private static String LDAP_SEVER=null;
    private static String PATH=null;
    public static void readProperites() throws Exception{
        Properties prop = new Properties();

        //读取属性文件a.properties
        File directory = new File("");//参数为空
        String courseFile = directory.getCanonicalPath() ;
        PATH=courseFile+"//source//";
        String path=courseFile+"//source//conf.properties";
        InputStream in = new BufferedInputStream(new FileInputStream(path));
        prop.load(in);     ///加载属性列表
        FILE_NAME= PATH+prop.getProperty("filename");
        LDAP_SEVER=prop.getProperty("ldapip");
        in.close();
    }
    public static void main(String[] args) throws Exception {
	// write your code here
        CsvReader reader=new CsvReader();
        readProperites();
        LinkedList<Contact> temp = reader.read(FILE_NAME);
        IntoLdap ldap=new IntoLdap(LDAP_SEVER);
          ldap.createDomain();
        for(Contact a:temp){
            if(a.getRealName()!=""||a.getRealName()!=null){
            if(!ldap.addContact(a)){
                System.out.println(a.getRealName()+"not add in");
            }}
        }

    }
}
