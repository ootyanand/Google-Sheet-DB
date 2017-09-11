package gdatabase;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import gsheet.GSheet;
import gsheet.GWorkSheetInfo;

public class GTable {
	
	private GDatabase db;
	private GWorkSheetInfo tableInfo;
	List<GColumn> columns = null; 
	
	public GTable(GDatabase db, String tableName) throws IOException {
		this.db = db; 
		List<GWorkSheetInfo> tables = db.sheet.getWorkSheets();
		for(GWorkSheetInfo table:tables){
			if(table.getName().equalsIgnoreCase(tableName))
				tableInfo = table;
		}
		
		if(tableInfo==null){
			System.out.println("Table Not Found!: " + tableName);
			throw new InvalidParameterException("Table Not Found!: " + tableName);
		}
	}
	
	public GTable(GDatabase db, GWorkSheetInfo wsi) throws IOException, InvalidParameterException {
		this.db = db; 
		tableInfo = wsi;
		if(tableInfo==null){
			throw new NullPointerException("Table Not Found!: GWorkSheetInfo is null");
		}
	}
	
	List<GColumn> setColumns(List<Object> columns) throws IOException {
		List<List<Object>> values = new ArrayList<>();
		values.add(columns);
		
		ValueRange value =  new ValueRange().setValues(values);
		String range = getName()+"!A1:Z1000";
		AppendValuesResponse response = db.sheet.addData(range, value);
		if (response == null ) return null;
		return getColumns();
	}
	
	public String getName(){
		return tableInfo.getName();
	}
	
	public int getId(){
		return (tableInfo!=null)?tableInfo.getId():-1;
	}
	
	public List<GColumn> getColumns() throws IOException {
		if (columns == null) {
			columns = new ArrayList<>();
			
			ValueRange response = db.sheet.getData(getName() + "!A1:Z1");
			List<List<Object>> values = response.getValues();
			if (values == null || values.size() == 0) {
				System.err.println("Column Not Found in Table " + getName());
				return columns; 
			}
			
			List<?> firstRow = values.get(0);
			for (int index = 0; index < firstRow.size(); index++) {
				columns.add(new GColumn(index, firstRow.get(index).toString()));
			}
		}
		return columns;
	}
	
	public List<GColumn> addColumn(String columnName) throws IOException{
		List<GColumn> columns = getColumns();
		for(GColumn col:columns){
			if(col.getName().equalsIgnoreCase(columnName)){
				System.err.println("Column Name: " + columnName + ", Already Exists!");
				return getColumns();
			}
		}
		
		try{
			db.sheet.insertEmpryColumn(tableInfo.getId(), columns.size()+1);
			List<List<Object>> values = Arrays.asList(Arrays.asList(columnName));
			ValueRange value =  new ValueRange().setValues(values);
			
			String range = getName()+"!"+GSheet.indexToLetter(columns.size()+1) + "1";
			
			db.sheet.setData(range, value);
			this.columns = null;
			
		} catch (IOException e) {
			throw e;
		}
		return getColumns();
	}
	
	public List<GColumn> deleteColumn(String columnName) throws IOException{
		List<GColumn> columns = getColumns();
		for (GColumn col : columns) {
			if (col.getName().equalsIgnoreCase(columnName)) {
				db.sheet.deleteColumn(tableInfo.getId(), col.getIndex() + 1);
				this.columns = null;
				return getColumns();
			}
		}
		System.err.println("Column Name: " + columnName + ", Not Found!");
		return getColumns();
	}
	
	public List<GRecord> getRecords() throws IOException{
		List<GRecord> records = new ArrayList<>();
		List<GColumn> columns = getColumns();
		
		String range = getName()+"!A2:" + GSheet.indexToLetter(columns.size()>0?columns.size():1);
		ValueRange response = db.sheet.getData(range);
		
		List<List<Object>> values = response.getValues();
		if (values != null && values.size() > 0) {
			for(int i=0; i<values.size(); i++ ){
				records.add(new GRecord(i, columns, values.get(i)));
			}
		}
		return records;
	}
	
	public int insert(GRecord record) throws IOException{
		List<List<Object>> values = new ArrayList<>();
		values.add(record.getValues());
		ValueRange value =  new ValueRange().setValues(values);
		
		String range = getName() + "!" + record.getRange();
		AppendValuesResponse response = db.sheet.addData(range, value);

		if (response == null ) {
			return -1;
		} 
		
		Integer cells = response.getUpdates().getUpdatedCells(); 
		return cells==null?-1:cells;
	}
	
	public int update(GRecord record) throws IOException{
		List<List<Object>> values = new ArrayList<>();
		values.add(record.getValues());
		ValueRange value =  new ValueRange().setValues(values);
		String range = getName() + "!" + record.getRange();
		UpdateValuesResponse response = db.sheet.setData(range, value);
		if (response == null ) {
			return -1;
		} 
		Integer cells = response.getUpdatedCells(); 
		return cells==null?-1:cells;
	}
	
	public int delete(GRecord record) throws IOException{
		db.sheet.deleteRow(tableInfo.getId(), record.getIndex()+2);
		return 1;
	}

	public boolean rename(String newName) throws IOException{
		return (db.sheet.renameSheet(tableInfo, newName)!=null);
	}
	
	public GRecord newRecord() throws IOException{
		return new GRecord(getColumns());
	}
	
	public String toString(){
		return tableInfo.getName();
	}
}
