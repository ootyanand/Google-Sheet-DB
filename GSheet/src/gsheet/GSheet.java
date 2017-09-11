package gsheet;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DeleteSheetRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.InsertDimensionRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateSheetPropertiesRequest;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;


public class GSheet {

	/** Application name. */
	private static final String APPLICATION_NAME = "GSheet/0.1";

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/** Default Scope for this Spread Sheet Application Read/Write */
	public static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);

	/** How the values should be stored in spreadsheet. */
	private static String valueInputOption = "USER_ENTERED";

	/** How values should be represented in the output. */
	private static String valueRenderOption = "FORMATTED_VALUE";

	/** How dates, times, and durations should be represented in the output. */
	private static String dateTimeRenderOption = "FORMATTED_STRING";

	/** API Key that points to the project / spreadsheet */
	private String apiKey;

	/** Sheets service instance */
	private Sheets sheetsService;

	/** Spread sheet id used in open */
	private String spreadsheetId;

	/** If public documents are to be opened set this to true */
	private boolean usingApiKey = false;

	private String name;

	/**
	 * Static initialization
	 */
	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Returns letter for a given column index, ex: 1=A, 2=B
	 */
	public static String indexToLetter(int column){
		String letter = "";
		int temp;
		while (column > 0){
			temp = (column - 1) % 26;
			letter = String.valueOf((char)(temp + 65)) + letter;
			column = (column - temp - 1) / 26;
		}
		return letter.isEmpty()?null:letter;
	}

	/**
	 * Returns column index for a given letter, ex: A=1, B=2
	 */
	public static int letterToIndex(String letter){
		
		if(letter.trim().isEmpty()){
			throw new InvalidParameterException("The parameter is empty!");
		}
		
		int column = 0, length = letter.length();
		for (int i = 0; i < length; i++){
			column += (letter.charAt(i) - 64) * Math.pow(26, length - i - 1);
		}
		return column;
	}

	/**
	 * default scope of this Application.
	 */
	public static List<String> getDefaultScops() {
		return SCOPES;
	}

	/**
	 * Use this function to google spread sheet service credentials. Credential
	 * can be null to access public documents
	 */
	private static Sheets getSheetsService(Credential credential) throws IOException {
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}

	/**
	 * Constructor
	 */
	public GSheet() {
		
	}
	
	/**
	 * Use this function to open private documents using goolge credentials.
	 */
	public boolean Open(String spreadsheetId, Credential credential) throws IOException {
		this.spreadsheetId = spreadsheetId;
		sheetsService = getSheetsService(credential);
		if(sheetsService != null){
			Spreadsheet response= sheetsService.spreadsheets().get(spreadsheetId).setIncludeGridData(false).execute();
			name = response.getProperties().getTitle();
		}
		return isOpen();
	}
	
	/**
	 * checks if spreadsheet is already open
	 * @return
	 */
	public boolean isOpen() {
		return sheetsService == null ? false
				: (spreadsheetId == null || spreadsheetId.trim().isEmpty()) ? false
						: usingApiKey ? (apiKey == null || apiKey.trim().isEmpty()) ? false : true : true;
	}
	
	/**
	 * returns the spreadsheet name
	 * @return
	 * @throws IOException
	 */
	public String getName() throws IOException{
		return (isOpen())? name:null;
	}
	
	/**
	 * returns the spreadsheet id, which is different from spreadsheet name
	 * @return
	 */
	public String getSpredsheetId(){
		return (isOpen())? spreadsheetId:null;
	}
	
	/**
	 * Get all work sheet in the spreadsheet
	 * @return
	 * @throws IOException
	 */
	public List<GWorkSheetInfo> getWorkSheets() throws IOException{
		
		if (!isOpen())
			return null;

		Spreadsheet response= sheetsService.spreadsheets().get(spreadsheetId).setIncludeGridData(false).execute();
		List<Sheet> workSheetList = response.getSheets();
		List<GWorkSheetInfo> workSheets=  new ArrayList<>();
		for (Sheet sheet : workSheetList) {
			workSheets.add(new GWorkSheetInfo(sheet.getProperties().getTitle(), sheet.getProperties().getSheetId()));
		    //System.out.println(sheet.getProperties().getTitle() + "[" + sheet.getProperties().getSheetId() +"]");
		    
		}
		return workSheets;
	}

	/**
	 * Use this function to open public documents using API key.
	 */
	public boolean Open(String spreadsheetId, String apiKey) throws IOException {
		this.spreadsheetId = spreadsheetId;
		this.usingApiKey = true;
		this.apiKey = apiKey;
		sheetsService = getSheetsService(null);
		return isOpen();
	}

	/**
	 * Get spreadsheet data for given range ex: A1=F10 or A1:B
	 */
	public ValueRange getData(String range) throws IOException {
	
		if (!isOpen())
			return null;

		Sheets.Spreadsheets.Values.Get request = sheetsService.spreadsheets().values().get(spreadsheetId, range);
		request.setValueRenderOption(valueRenderOption);
		request.setDateTimeRenderOption(dateTimeRenderOption);
		if (usingApiKey)
			request.setKey(apiKey);
		return request.execute();
	}

	/**
	 * Append spreadsheet data beginning from the given range.
	 */
	public AppendValuesResponse addData(String range, ValueRange data) throws IOException {
		
		if (!isOpen())
			return null;

		Sheets.Spreadsheets.Values.Append request = sheetsService.spreadsheets().values().append(spreadsheetId, range, data);
		request.setValueInputOption(valueInputOption);
		if (usingApiKey)
			request.setKey(apiKey);
		return request.execute();
	}

	/**
	 * Modifies a spreadsheet data at given range.
	 */
	public UpdateValuesResponse setData(String range, ValueRange data) throws IOException {
		
		if (!isOpen())
			return null;
		
		Sheets.Spreadsheets.Values.Update request = sheetsService.spreadsheets().values().update(spreadsheetId, range, data);
		request.setValueInputOption(valueInputOption);
		if (usingApiKey)
			request.setKey(apiKey);
		
		return request.execute();
	}
	
	/**
	 * Inserts empty row or columns
	 * @param sheetId
	 * @param dimention : ROWS/COLUMNS
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws IOException
	 */
	public BatchUpdateSpreadsheetResponse insertRowsOrColumns(int sheetId, String dimention, int startIndex, int endIndex) throws IOException {
		
		if (!isOpen())
			return null;
			
		List<Request> requests = new ArrayList<>();
		requests.add( new Request()
					.setInsertDimension( new InsertDimensionRequest()
				    .setRange(new DimensionRange()
				      .setSheetId(sheetId)
				      .setDimension(dimention)
				      .setStartIndex(startIndex)
				      .setEndIndex(endIndex)
				    )
				    .setInheritFromBefore(dimention.equalsIgnoreCase("COLUMNS") && startIndex>0 )
				  ));
		
		BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
	    requestBody.setRequests(requests);
	    
	    Sheets.Spreadsheets.BatchUpdate batchRequest =  sheetsService.spreadsheets().batchUpdate(spreadsheetId, requestBody);
	    if (usingApiKey)
	    	batchRequest.setKey(apiKey);
	    
	    return batchRequest.execute();
	}
	
	/**
	 * Deletes rows or columns in spreadsheet
	 * @param sheetId
	 * @param dimention : ROWS/COLUMNS
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws IOException
	 */
	public BatchUpdateSpreadsheetResponse deleteRowsOrColumns(int sheetId, String dimention, int startIndex, int endIndex) throws IOException {
		
		if (!isOpen())
			return null;
			
		List<Request> requests = new ArrayList<>();
		requests.add( new Request()
				  	.setDeleteDimension(new DeleteDimensionRequest()
				    .setRange(new DimensionRange()
				      .setSheetId(sheetId)
				      .setDimension(dimention)
				      .setStartIndex(startIndex)
				      .setEndIndex(endIndex)
				    )
				  ));
		
		BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
	    requestBody.setRequests(requests);
	    
	    Sheets.Spreadsheets.BatchUpdate batchRequest =  sheetsService.spreadsheets().batchUpdate(spreadsheetId, requestBody);
	    if (usingApiKey)
	    	batchRequest.setKey(apiKey);
	    
	    return batchRequest.execute();
	}
	
	/**
	 * Inserts a column in spreadsheet.
	 */
	public BatchUpdateSpreadsheetResponse insertEmpryColumn(int sheetId, int columnIndex) throws IOException {
		return insertRowsOrColumns(sheetId, "COLUMNS", columnIndex-1, columnIndex);
	}
	
	/**
	 * Inserts a row in spreadsheet. 
	 */
	public BatchUpdateSpreadsheetResponse insertEmptyRow(int sheetId, int rowIndex) throws IOException {
		return insertRowsOrColumns(sheetId, "ROWS", rowIndex-1, rowIndex);
	}
	
	/**
	 * Deletes a column from spreadsheet.
	 */
	public BatchUpdateSpreadsheetResponse deleteColumn(int sheetId, int columnIndex) throws IOException {
		return deleteRowsOrColumns(sheetId, "COLUMNS", columnIndex-1, columnIndex);
	}
	
	/**
	 * Deletes a row from spreadsheet. 
	 */
	public BatchUpdateSpreadsheetResponse deleteRow(int sheetId, int rowIndex) throws IOException {
		return deleteRowsOrColumns(sheetId, "ROWS", rowIndex-1, rowIndex);
	}
	
	/**
	 * Add a new work sheet to spreadsheet.
	 * @param title
	 * @return
	 * @throws IOException
	 */
	public BatchUpdateSpreadsheetResponse addSheet(String title) throws IOException{
		
		if (!isOpen())
			return null;
		
		List<Request> requests = new ArrayList<>();
		requests.add(new Request()
				.setAddSheet(new AddSheetRequest()
						.setProperties(new SheetProperties()
								.setTitle(title))));

		BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
	    requestBody.setRequests(requests);
	    
	    Sheets.Spreadsheets.BatchUpdate batchRequest =  sheetsService.spreadsheets().batchUpdate(spreadsheetId, requestBody);
	    if (usingApiKey)
	    	batchRequest.setKey(apiKey);
	    
	    return batchRequest.execute();
	}
	
	/**
	 * Rename a work sheet in a spreadsheet.
	 * @param oldName
	 * @param newName
	 * @return
	 * @throws IOException
	 */
	public BatchUpdateSpreadsheetResponse renameSheet(String oldName, String newName) throws IOException{
		
		if (!isOpen())
			return null;
		
		GWorkSheetInfo workSheet = null;
		List<GWorkSheetInfo> worSheets = getWorkSheets();
		for(GWorkSheetInfo wi : worSheets){
			if(wi.getName().equalsIgnoreCase(oldName)){
				workSheet = wi;
			}
		}
		
		if(workSheet == null){
			System.out.println("Worksheet Not found: " + oldName);
			return null;
		}
	    return renameSheet(workSheet, newName);
	}

	/**
	 * Rename a work sheet in a spreadsheet.
	 * @param workSheet
	 * @param newName
	 * @return
	 * @throws IOException
	 */
	public BatchUpdateSpreadsheetResponse renameSheet(GWorkSheetInfo workSheet, String newName) throws IOException{
		
		if (!isOpen())
			return null;
		
		List<Request> requests = new ArrayList<>();
		requests.add(new Request().setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
						.setProperties(new SheetProperties().setTitle(newName).setSheetId(workSheet.getId()))
						.setFields("title")));

		BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
	    requestBody.setRequests(requests);
	    
	    Sheets.Spreadsheets.BatchUpdate batchRequest =  sheetsService.spreadsheets().batchUpdate(spreadsheetId, requestBody);
	    if (usingApiKey)
	    	batchRequest.setKey(apiKey);
	    
	    return batchRequest.execute();
	}
	
	/**
	 * Delete a work sheet from spreadsheet.
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public BatchUpdateSpreadsheetResponse deleteSheet(String name) throws IOException{

		List<GWorkSheetInfo> workSheets = getWorkSheets();
		
		if(workSheets==null){
			return null;
		}
		
		GWorkSheetInfo workSheet = null;
		
		for(GWorkSheetInfo ws : workSheets){
			if(ws.getName().equalsIgnoreCase(name)){
				workSheet = ws;
			}
		}
		
		if(workSheet == null){
			System.out.println("Worksheet Not found: " + name);
			return null;
		}
		
		return deleteSheet(workSheet);
	}
	
	/**
	 * Delete a work sheet from spreadsheet.
	 * @param workSheet
	 * @return
	 * @throws IOException
	 */
	public BatchUpdateSpreadsheetResponse deleteSheet(GWorkSheetInfo workSheet) throws IOException{
		
		if (!isOpen())
			return null;
		
		List<Request> requests = new ArrayList<>();
		requests.add(new Request().setDeleteSheet(new DeleteSheetRequest().setSheetId(workSheet.getId())));

		BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
	    requestBody.setRequests(requests);
	    
	    Sheets.Spreadsheets.BatchUpdate batchRequest =  sheetsService.spreadsheets().batchUpdate(spreadsheetId, requestBody);
	    if (usingApiKey)
	    	batchRequest.setKey(apiKey);
	    
	    return batchRequest.execute();
	}
	
	/**
	 * Create a new spreadsheet in google drive.
	 * @param name
	 * @param credential
	 * @return
	 * @throws IOException
	 */
	public static GSheet create(String name, Credential credential) throws IOException {
		Sheets sheetsService = getSheetsService(credential);
		if (sheetsService != null) {
			Spreadsheet requestBody = new Spreadsheet();
			requestBody.setProperties(new SpreadsheetProperties().setTitle(name));
			
			
			Sheets.Spreadsheets.Create request = sheetsService.spreadsheets().create(requestBody);
			Spreadsheet sheet = request.execute();
			GSheet gsheet = new GSheet();
			gsheet.Open(sheet.getSpreadsheetId(), credential);
			return gsheet;
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println("GSheet/0.1");
	}
}
