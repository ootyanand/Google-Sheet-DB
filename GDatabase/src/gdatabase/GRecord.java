package gdatabase;

import java.util.ArrayList;
import java.util.List;

import gsheet.GSheet;

public class GRecord {
	
	int recIndex;
	List<GColumn> columns;
	List<Object> values;
	
	public int getIndex(){
		return recIndex;
	}
	
	public List<GColumn> getColumn() {
		return columns;
	}

	public void setColumn(List<GColumn> columns) {
		this.columns = columns;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		if(values.size()<columns.size()){
			for(int i=values.size(); i<columns.size(); i++)
				values.add("");
		}
		this.values = values;
	}
	
	public GColumn getColumn(String colName){
		for(GColumn col:columns){
			if(col.getName().equalsIgnoreCase(colName))
				return col;
		}
		return null;
	}
	
	public GColumn getColumn(int index){
		if(index <0 || index >= columns.size()){
			throw new IndexOutOfBoundsException();
		}
		return columns.get(index);
	}
	
	public boolean setValue(String colName, Object value){
		GColumn col = getColumn(colName);
		if(col!=null){
			values.set(col.getIndex(), value);
			return true;
		}
		return false;
	}
	
	public void setValue(int index, Object value){
		if(index <0 || index >= columns.size()){
			throw new IndexOutOfBoundsException();
		}
		values.set(index, value);
	}
	
	public Object getValue(String colName){
		GColumn col = getColumn(colName);
		if(col!=null)
			return values.get(col.getIndex());
		return null;
	}
	
	public Object getValue(int index){
		if(index <0 || index >= columns.size()){
			throw new IndexOutOfBoundsException();
		}
		return values.get(index);
	}
	
	public GRecord(int recIndex, List<GColumn> columns, List<Object> values) {
		super();
		this.recIndex=recIndex;
		this.columns = columns;
		setValues(values);
	}
	
	public GRecord(List<GColumn> columns) {
		super();
		this.recIndex=-1;
		this.columns = columns;
		setValues(new ArrayList<>());
	}
	
	public String getRange(){
		if(recIndex==-1)
			return "A1";
		
		return "A" + (recIndex+2) + ":" + GSheet.indexToLetter(columns.size()) + (recIndex+2);
	}
}
