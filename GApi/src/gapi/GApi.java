package gapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GApi {

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * static initialize
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
	 * returns GoogleClientSecrets by parsing the json file.
	 */
	public static GoogleClientSecrets getClientSecretsFromFile(InputStream in) throws IOException {
		return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
	}

	/**
	 * returns GoogleClientSecrets by parsing the json file.
	 */
	public static GoogleClientSecrets getClientSecretsFromFile(String clientSecretsFilePath) throws IOException {
		return getClientSecretsFromFile(new FileInputStream(new File(clientSecretsFilePath)));
	}

	/**
	 * gets a refresh token by launching the browser and authenticating the
	 * user. Refresh tokens never expire, so once you get the refresh token you
	 * don't need to call this function each time. This token is valid for
	 * different types of Google Services like spreadsheet / google drive / etc
	 */
	public static String getRefreshToken(GoogleClientSecrets clientSecrets, List<String> scopes) throws IOException {
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, scopes).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		return credential.getRefreshToken();
	}

	/**
	 * get refresh token by reading client secret from file.
	 */
	public static String getRefreshToken(String clientSecretsJasonFilePath, List<String> scopes) throws IOException {
		return getRefreshToken(getClientSecretsFromFile(clientSecretsJasonFilePath), scopes);
	}

	/**
	 * Once you have the refresh token, you can use this function to retrieve
	 * Credentials which can be used in different google services like
	 * spreadsheet / google drive / etc
	 */
	public static Credential getCredential(GoogleClientSecrets clientSecrets, String refreshToken) throws IOException {
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
				.setJsonFactory(JSON_FACTORY).setClientSecrets(clientSecrets).build();
		credential.setRefreshToken(refreshToken);
		return credential;
	}

	/**
	 * Get Credential by reading client secret from file.
	 */
	public static Credential getCredential(String clientSecretsFilePath, String refreshToken) throws IOException {
		return getCredential(getClientSecretsFromFile(clientSecretsFilePath), refreshToken);
	}

	/**
	 * dummy main
	 */
	public static void main(String[] args) {
		System.out.println("GApi/0.1");
	}
}
