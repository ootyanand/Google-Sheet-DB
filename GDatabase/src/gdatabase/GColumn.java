package gdatabase;

import gsheet.GSheet;

public class GColumn {
	int index;
	String name;
	String a1Notation;
	public GColumn(int index, String name) {
		super();
		this.index = index;
		this.name = name;
		this.a1Notation = GSheet.indexToLetter(index+1);
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getA1Notation() {
		return a1Notation;
	}
	public void setA1Notation(String a1Notation) {
		this.a1Notation = a1Notation;
	}
	
	public String toString(){
		return name;
	}
}
