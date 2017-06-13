package com.rofs;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by markla on 2017/6/7.
 */
public class IntoLdap {
    // private final String URL = "ldap://192.168.1.119:389/";
    private final String URL;
    private final String BASEDN = "dc=maxcrc,dc=com";  // 根据自己情况进行修改
    private final String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    private LdapContext ctx = null;
    private final Control[] connCtls = null;

    IntoLdap(String ip) {
        URL = "ldap://" + ip + ":389/";
    }

    private void LDAP_connect() {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
        env.put(Context.PROVIDER_URL, URL + BASEDN);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        String root = "cn=Manager,dc=maxcrc,dc=com";  //根据自己情况修改
        env.put(Context.SECURITY_PRINCIPAL, root);   // 管理员
        env.put(Context.SECURITY_CREDENTIALS, "secret");  // 管理员密码

        try {
            ctx = new InitialLdapContext(env, connCtls);
            System.out.println("连接成功");

        } catch (javax.naming.AuthenticationException e) {
            System.out.println("连接失败：");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("连接出错：");
            e.printStackTrace();
        }

    }

    private void closeContext() {
        if (ctx != null) {
            try {
                ctx.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }

        }
    }
 public boolean findDomain(String uid) {
     LDAP_connect();
     try {
         SearchControls constraints = new SearchControls();
         constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
         NamingEnumeration<SearchResult> en = ctx.search("", "ou=rofsmicro", constraints);
         if (en == null || !en.hasMoreElements()) {
             return false;
         }
         return true;
     } catch (Exception e) {
         return false;
     }finally {
         closeContext();
     }
 }
 public void deleteDomain(String uid){
     LDAP_connect();
     try {
         ctx.destroySubcontext("ou="+uid);

     } catch (NamingException e) {
         e.printStackTrace();
     }finally {
         closeContext();
     }
 }

    public boolean createDomain() {
        try {
            LDAP_connect();
            BasicAttributes attrsbu = new BasicAttributes();
            BasicAttribute objclassSet = new BasicAttribute("objectclass");
            objclassSet.add("organizationalUnit");
            attrsbu.put(objclassSet);
            ctx.createSubcontext("ou=rofsmicro", attrsbu);
            return true;
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        closeContext();
        return false;
    }

    public boolean addContact(Contact user) {
        try {
            LDAP_connect();
            BasicAttributes attrsbu = new BasicAttributes();
            BasicAttribute objclassSet = new BasicAttribute("objectclass");
            objclassSet.add("inetOrgPerson");
            attrsbu.put(objclassSet);
            if (user.getRealName().length() > 1) {
                attrsbu.put("cn", user.getRealName());
            }
            if (user.getDepartment().length() > 0) {
                attrsbu.put("o", user.getDepartment());
            }
            attrsbu.put("mail", user.getMail());
            ctx.createSubcontext("cn=" + user.getRealName() + ",ou=rofsmicro", attrsbu);
            return true;
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        closeContext();
        return false;
    }

    public void searchInformation(String base, String scope, String filter) {
        SearchControls sc = new SearchControls();
        LDAP_connect();
        if (scope.equals("base")) {
            sc.setSearchScope(SearchControls.OBJECT_SCOPE);
        } else if (scope.equals("one")) {
            sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        } else {
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        }

        NamingEnumeration ne = null;
        try {
            ne = ctx.search(base, filter, sc);
            // Use the NamingEnumeration object to cycle through
            // the result set.
            while (ne.hasMore()) {
                System.out.println();
                SearchResult sr = (SearchResult) ne.next();
                String name = sr.getName();
                if (base != null && !base.equals("")) {
                    System.out.println("entry: " + name + "," + base);
                } else {
                    System.out.println("entry: " + name);
                }

                Attributes at = sr.getAttributes();
                NamingEnumeration ane = at.getAll();

                while (ane.hasMore()) {
                    Attribute attr = (Attribute) ane.next();
                    String attrType = attr.getID();
                    NamingEnumeration values = attr.getAll();
                    Vector vals = new Vector();
                    // Another NamingEnumeration object, this time
                    // to iterate through attribute values.
                    while (values.hasMore()) {
                        Object oneVal = values.nextElement();
                        if (oneVal instanceof String) {
                            System.out.println(attrType + ": " + (String) oneVal);
                        } else {
                            System.out.println(attrType + ": " + new String((byte[]) oneVal));
                        }
                    }
                }
            }
        } catch (Exception nex) {
            System.err.println("Error: " + nex.getMessage());
            nex.printStackTrace();
        }finally {
            closeContext();
        }
    }
    public static void main(String[] args) {
        IntoLdap ldap = new IntoLdap("192.168.1.119");
//       ldap.findDomain("rofsmicro");
//        ldap.deleteDomain("rofsmicro");
        ldap.searchInformation("dc=maxcrc,dc=com","ou=rofsmicro","");
    }
}
