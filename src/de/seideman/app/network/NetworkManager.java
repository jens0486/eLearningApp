package de.seideman.app.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkManager {

	private ConnectivityManager con;

	public NetworkManager(ConnectivityManager _con) {
		con = _con;
	}

	public boolean tryNetwork() {
		boolean bool = false;
		NetworkInfo inf = con.getActiveNetworkInfo();

		try {
			if (inf.isAvailable() && inf.isConnected()) {
				bool = true;
			}
		} catch (Exception ex) {
			bool = false;
		}

		return bool;

	}

	public JSONObject tryLogin(String email, String password) {

		JSONObject json = null;
		HttpClient cl = new DefaultHttpClient();
		HttpGet get = new HttpGet(
				"http://192.168.2.1:8080/PictureWeb/api/android/login/" + email
						+ "/" + password);
		try {

			HttpResponse resp = cl.execute(get);

			HttpEntity entity = resp.getEntity();

			json = readStream(resp.getEntity().getContent());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	public JSONArray getCategories() {

		JSONObject json = null;
		HttpClient cl = new DefaultHttpClient();
		HttpGet get = new HttpGet(
				"http://192.168.2.1:8080/PictureWeb/api/android/category/list");
		JSONArray resultList = null;
		
		try {

			HttpResponse resp = cl.execute(get);
			HttpEntity entity = resp.getEntity();

			json = readStream(resp.getEntity().getContent());
			resultList = json.getJSONArray("list");
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultList;
	}

	public JSONObject createCard(String term,String description,String image, String category,
			String ansWhat, String ansWho, String ansWhy, String email) {
		JSONObject json = null;
		
		HttpClient cl = new DefaultHttpClient();
		HttpGet get = new HttpGet(
				"http://192.168.2.1:8080/PictureWeb/api/android/card/new/"+ term+ "/" + description + "/12345"
						+ "/" + category + "/" + ansWhat+ "/" + ansWho + "/" + ansWhy + "/" + email);
		try {

			HttpResponse resp = cl.execute(get);

			HttpEntity entity = resp.getEntity();

			json = readStream(resp.getEntity().getContent());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		return json;
	}

	private JSONObject readStream(InputStream in) {
		JSONObject json = null;

		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder builder = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}
			in.close();
			json = new JSONObject(builder.toString());
			json.put("result", true);
		} catch (Exception ex) {
			json = new JSONObject();
			try {
				json.put("result", false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return json;
	}
}
