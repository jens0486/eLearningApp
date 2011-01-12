package de.seideman.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import de.seideman.app.R;

public class Player extends Activity {

	private WebView webView;
	
	public void onCreate(Bundle savedInstanceState) {
	    	
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.play);
	    	
	    WebChromeClient chrome = new WebChromeClient();
	    	
	    	
	    	webView = (WebView)findViewById(R.id.WebView01);
	    	webView.getSettings().setJavaScriptEnabled(true);
	    	webView.getSettings().setUseWideViewPort(true);
	    	webView.setInitialScale(58);
	    	
	    	
	    	webView.loadUrl("http://192.168.2.1:8080/PictureWeb/mobile_quiz.jsp");
	    	
	}

}
