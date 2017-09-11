/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author ootyanand
 */
public class DBCredential {
    
    String name;
    String refreshToken;
    DBClientSecret installed = new DBClientSecret();

    public DBCredential() {
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public DBClientSecret getClientSecret() {
        return installed;
    }

    public void setClientSecret(DBClientSecret installed) {
        this.installed = installed;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public String getClientSecretJSON() throws IOException{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return "{\"installed\":" + gson.toJson(installed) + "}";
    }
    
    public static DBCredential getCredentials(String json){
        try (Reader reader = new StringReader(json)) {
            return getCredentials(reader);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static DBCredential getCredentials(Reader reader){
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(reader, DBCredential.class);
    }
    
    public static class DBClientSecret{
        String client_id;
        String project_id;
        String auth_uri;
        String token_uri;
        String auth_provider_x509_cert_url;
        String client_secret;
        String[] redirect_uris;

        public DBClientSecret() {
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public String getAuth_uri() {
            return auth_uri;
        }

        public void setAuth_uri(String auth_uri) {
            this.auth_uri = auth_uri;
        }

        public String getToken_uri() {
            return token_uri;
        }

        public void setToken_uri(String token_uri) {
            this.token_uri = token_uri;
        }

        public String getAuth_provider_x509_cert_url() {
            return auth_provider_x509_cert_url;
        }

        public void setAuth_provider_x509_cert_url(String auth_provider_x509_cert_url) {
            this.auth_provider_x509_cert_url = auth_provider_x509_cert_url;
        }

        public String getClient_secret() {
            return client_secret;
        }

        public void setClient_secret(String client_secret) {
            this.client_secret = client_secret;
        }

        public String[] getRedirect_uris() {
            return redirect_uris;
        }

        public void setRedirect_uris(String[] redirect_uris) {
            this.redirect_uris = redirect_uris;
        }
    }
}
