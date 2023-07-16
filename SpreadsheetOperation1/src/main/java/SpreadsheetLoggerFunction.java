import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.*;
import java.io.*;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.bigquery.model.JsonObject;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ThemeColorPair;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


//Main Class public class
public class SpreadsheetLoggerFunction {
	
	public static String ApplicationName = "Spreadsheet Logger Function";
	public static String ServiceAccount = "task-project-1-390309@appspot.gserviceaccount.com";
	public static String pKey = "D:\\eclipse project\\SpreadsheetOperation1\\src\\main\\webapp\\WEB-INF\\task-project-1-390309-a3e53fa67df6.p12";
	public static String sheetID = "1XqpnlCamocLppTlQyvRWkIC_7S78BFTLO7KPXZPP8ZU";
	public static String spreadsheetName = "Spreadsheet Data Access Project";
	
    // autohrizeSheet () method for obtaining the authorization to access the Google
	public static Sheets authorizeSheet() throws IOException, GeneralSecurityException 
	    {
		    // Input stream object
		    System.out.println("Initializing the object for the p12key");
		    FileInputStream p12key = new FileInputStream(pKey);
		    System.out.println("p12key found successfully");

		    // Set up the HTTP transport and JSON factor
		    System.out.println("Making API request");
		    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
		    System.out.println("API request connected");

		    // Create GoogleCredentials instance
		    System.out.println("Creating Google Credentials instance");
		    GoogleCredentials credentials = GoogleCredentials.fromStream(p12key)
		    		.createScoped(SheetsScopes.SPREADSHEETS);
		    System.out.println("Google credentials instance created successfully");
		    
		    Sheets sheetsService = new Sheets.Builder(httpTransport, jsonFactory, null)
		    		.setHttpRequestInitializer((HttpRequestInitializer) credentials)
		    		.setApplicationName(ApplicationName)
	                .setServicePath(pKey)
		    		.build();
		    return sheetsService;
		}
	
	// readsheet data method to read the data from the spreadsheet
	public static List<List<Object>> readSheetData(Sheets sheetsService, String sheetID, String spreadsheetName)
            throws IOException 
	{
		String range = spreadsheetName + "Logger Data!A1:F130";
	    System.out.println("get response");
        ValueRange response = sheetsService
        		.spreadsheets()
        		.values()
        		.get(sheetID, range)
        		.execute();
        System.out.println(response.getValues());
	    System.out.println("response found success");
        return response.getValues();
     }
		
    // main method
	public static void main(String[] args) 	
	{
		File p12KeyFile = new File(pKey);
		if (p12KeyFile.exists() && p12KeyFile.isFile() && p12KeyFile.canRead()) {
			System.out.println("main method is running, p12key file is initialized or found");
			try {
				Sheets sheetService = authorizeSheet();
				String spreadsheetId = sheetID;
				String sheetName = spreadsheetName;
				
				List<List<Object>> sheetData = readSheetData(sheetService, spreadsheetId, sheetName);
				System.out.println("Spreadsheet Name: " + (sheetService));
				System.out.println("Sheet Name: " + sheetName);
				System.out.println("Spreadsheet Data: " + sheetData);
				for (List<Object> row : sheetData){
					for (Object cell : row) {
						System.out.print(cell.toString() + " ");
					}
			      }
				}
			catch (GeneralSecurityException | IOException e) { 
				e.printStackTrace();
			}
		} 
		   else {
			System.out.println("P12 key file is missing, not a file, or cannot be read.");
	    }        
	}
}