/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

import gapi.GApi;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author ootyanand
 */
public class DBConfig {
    List<DatabaseInfo> recentDatabases = new ArrayList<>();
    List<DBCredential> allCredentials = new ArrayList<>();
    int curentCredentialIndex = -1;

    public DBConfig() {
    }

    public List<DatabaseInfo> getRecentDatabases() {
        return recentDatabases;
    }
    
    public List<String> getRecentDatabaseNames() {
        List<String> names = new ArrayList<>();
        recentDatabases.forEach((d) -> {
            names.add(d.getName());
        });
        return names;
    }
    
    public DatabaseInfo getDatabaseByID(String dbId){
        for (DatabaseInfo d : recentDatabases) {
            if(d.getId().equalsIgnoreCase(dbId)){
                return d;
            }
        }
        return null;
    }
    
    public DatabaseInfo getDatabaseByName(String name){
        for(DatabaseInfo d: recentDatabases){
            if(d.getName().equalsIgnoreCase(name)){
                return d;
            }
        }
        return null;
    }
    
    public DatabaseInfo getRecentDatabasesAt(int index) {
        if(recentDatabases!=null && index>=0 && index <recentDatabases.size()){
            return recentDatabases.get(index);
        }
        return null;
    }
    
    public DatabaseInfo getRecentDatabasesAtEnd() {
        if(recentDatabases!=null && recentDatabases.size()>0){
            return recentDatabases.get(recentDatabases.size()-1);
        }
        return null;
    }

    public void setRecentDatabases(List<DatabaseInfo> recentDatabases) {
        this.recentDatabases = recentDatabases;
    }

    public List<DBCredential> getAllCredentials() {
        return allCredentials;
    }

    public void setAllCredentials(List<DBCredential> allCredentials) {
        this.allCredentials = allCredentials;
    }

    public DBCredential getCurentCredential() {
        return (curentCredentialIndex == -1)? null : allCredentials.get(curentCredentialIndex);
    }

    public void setCurentCredential(DBCredential cr) {
        if(cr == null)   return;
        
        String crName = cr.getName();
        if(crName == null || crName.isEmpty()) return;
        
        for(int i = 0; i<allCredentials.size(); i++){
            if(allCredentials.get(i).getName().equalsIgnoreCase(crName)){
                setCurentCredential(i);
                return;
            }
        }
        allCredentials.add(cr);
        setCurentCredential(allCredentials.size()-1);
    }
    
    public void setCurentCredential(int index) {
        if(index>=0 && index < allCredentials.size())
            this.curentCredentialIndex = index;
    }
    
    public void addCredential(DBCredential cr){
        if(cr==null) return;
        
        String crName = cr.getName();
        if(crName==null || crName.isEmpty()) return;

        for(DBCredential c : allCredentials){
            if(c.getName().equalsIgnoreCase(crName))
                return;
        }
        allCredentials.add(cr);
    }
    
    public void addRecentDB(DatabaseInfo db){
        for(DatabaseInfo d : recentDatabases){
            if(d.getId().equalsIgnoreCase(db.getId()))
                return;
        }
        recentDatabases.add(db);
    }
    
    public void save(){
        try (Writer writer = new FileWriter("db-coinfig.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(this, writer);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static DBConfig load(){
        try (Reader reader = new FileReader("db-coinfig.json")) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(reader, DBConfig.class);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Credential getGoogleCredential(){
        try {
            DBCredential cr = getCurentCredential();
            if(cr==null) return null;
            
            String json = cr.getClientSecretJSON();
            InputStream in = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            Credential credentials = GApi.getCredential(GApi.getClientSecretsFromFile(in), cr.getRefreshToken());
            return credentials;
        } catch (IOException ex) {
            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
