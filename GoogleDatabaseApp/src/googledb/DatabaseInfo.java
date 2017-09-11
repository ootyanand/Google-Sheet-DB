/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

/**
 *
 * @author ootyanand
 */
public class DatabaseInfo{
    String id;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.trim();
    }

    public DatabaseInfo(String name, String id) {
        this.name = name;
        this.id = id;
    }
    
    public DatabaseInfo(){}
    
    @Override
    public String toString(){
        return "Name: " + name + ", Id: " + id ;
    }
}