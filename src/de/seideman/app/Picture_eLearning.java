package de.seideman.app;

import org.json.JSONException;
import org.json.JSONObject;

import de.seideman.app.R;
import de.seideman.app.activities.CardCreator;
import de.seideman.app.activities.Player;
import de.seideman.app.network.NetworkManager;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Picture_eLearning extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private NetworkManager netMan;
	private EditText editTextUser;
	private EditText editTextPass;
	private Button btnLogin;
	private Button btnRegister;
	private Button btnPlay;
	private Button btnCard;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		editTextUser = (EditText) findViewById(R.id.EditText_User);
		editTextPass = (EditText) findViewById(R.id.EditText_Pass);
		btnLogin = (Button) findViewById(R.id.Button_Login);
		btnLogin.setOnClickListener(this);
		
		btnRegister = (Button) findViewById(R.id.Button_Register);
		btnRegister.setOnClickListener(this);

		netMan = new NetworkManager(
				(ConnectivityManager) this
						.getSystemService(CONNECTIVITY_SERVICE));

		// ueberprüft ob eine Netzwerkverbindung besteht, ansonsten wird
		// Programm beendet
		if (!netMan.tryNetwork()) {
			Toast.makeText(this,
					"Bitte stellen sie eine Netzwerkverbindung her!!!", 10)
					.show();
			this.finish();
		} else {
			Toast.makeText(this, "Netzwerkverbindung hergestellt", 100).show();
		}

	}

	@Override
	public void onClick(View v) {

		JSONObject json = null;
		String email = editTextUser.getText().toString();
		String password = editTextPass.getText().toString();

		if (v.equals((Button) btnLogin)) {

			if (email.equals("") || password.equals("") || password == null || email==null) {
				Toast.makeText(this, "Leere Eingabefelder!", 5).show();

			} else {
				json = netMan.tryLogin(email, password);
				try {
					if ((Boolean) json.get("result")) {
						setContentView(R.layout.main);
						
							Toast.makeText(this,
									"User: " + json.get("nickname"), 10).show();
						
						btnPlay = (Button) findViewById(R.id.Button_Play);
						btnPlay.setOnClickListener(this);

						btnCard = (Button) findViewById(R.id.Button_CreateCard);
						btnCard.setOnClickListener(this);
					} else {
						Toast.makeText(this,
								"Falsche eMail oder falsches Password!", 10).show();
						//editTextPass.setText("");
						//editTextUser.setText("");

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (v.equals((Button) btnPlay)) {
			Intent myIntent = new Intent(this, Player.class);

			try {
				this.startActivity(myIntent);
			} catch (Exception ex) {
				Toast.makeText(this, ex.getMessage().toString(), 10).show();
			}
		}
		if (v.equals((Button) btnCard)) {
			Intent myIntent = new Intent(this, CardCreator.class);
			
			myIntent.putExtra("email", email);
			try {
				this.startActivity(myIntent);
			} catch (Exception ex) {
				Toast.makeText(this, ex.getMessage().toString(), 10).show();
			}
		}
	}
}