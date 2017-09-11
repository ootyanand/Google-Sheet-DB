package gdatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;

import gsheet.GSheet;
import gsheet.GWorkSheetInfo;

public class GDatabase {
	
	GSheet sheet;
	
	public static GDatabase createDatabase(String name, Credential credential) throws IOException{
			GDatabase db = new GDatabase();
			db.sheet = GSheet.create(name, credential);
			return db;
	}
	
	public GDatabase() {
	}
	
	public GDatabase(String spreadsheeId, Credential credential) throws IOException{
		sheet = new GSheet();
		sheet.Open(spreadsheeId, credential);
	}
	
	public boolean Open(String spreadsheeId, Credential credential) throws IOException{
		sheet = new GSheet();
		return sheet.Open(spreadsheeId, credential);
	}
	
	public boolean isOpen(){
		if(sheet!=null)
			return sheet.isOpen();
		return false;
	}
	
	public static List<String> getDefaultScope(){
		return GSheet.getDefaultScops();
	}
	
	public String getName() throws IOException{
		if(sheet!=null)
			return sheet.getName();
		return null;
	}
	
	public String getDatabaseId(){
		if(sheet!=null)
			return sheet.getSpredsheetId();
		return null;
	}
	
	public List<GTable> getTables() throws IOException{
		if(sheet!=null){
			List<GTable> tables = new ArrayList<>();
			for(GWorkSheetInfo ws:sheet.getWorkSheets())
				tables.add(new GTable(this, ws));
			
			return tables;
		}
		return null;
	}
	
	public GTable getTable(String tableName) throws IOException{
		List<GTable> tables = getTables();
		if(tables!=null){
			for(GTable table:tables){
				if(table.getName().equalsIgnoreCase(tableName))
					return table;
			}
		}
		return null;
	}
	
	public GTable createTable(String tableName, List<Object> columns) throws IOException{
		sheet.addSheet(tableName);
		GTable table = new GTable(this, tableName);
		table.setColumns(columns);
		return table;
	}
	
	public boolean dropTable(String tableName) throws IOException{
		return sheet.deleteSheet(tableName)!=null;
	}
	
	public static void main(String[] args) {
		System.out.println("GDatabase/0.1");
	}
}
