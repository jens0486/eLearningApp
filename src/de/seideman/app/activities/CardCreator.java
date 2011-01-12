package de.seideman.app.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.seideman.app.R;
import de.seideman.app.network.NetworkManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class CardCreator extends Activity implements OnClickListener,
		OnTouchListener {

	private Button btnPicture;
	private Button btnCreateCard;
	private EditText editTerm;
	private EditText editWhat;
	private EditText editWho;
	private EditText editWhy;
	private EditText editDesc;
	private String email;
	private byte[] pictureByte = {};
	private ImageView imageView;
	private NetworkManager netMan;
	private Spinner spinCategory;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_creator);

		netMan = new NetworkManager(
				(ConnectivityManager) this
						.getSystemService(CONNECTIVITY_SERVICE));

		btnPicture = (Button) findViewById(R.id.Button01);
		btnPicture.setOnClickListener(this);

		btnCreateCard = (Button) findViewById(R.id.CreateCard);
		btnCreateCard.setOnClickListener(this);

		editTerm = (EditText) findViewById(R.id.EditText07);
		editTerm.setOnTouchListener(this);

		editWhat = (EditText) findViewById(R.id.EditText01);
		editWhat.setOnTouchListener(this);
		editWho = (EditText) findViewById(R.id.EditText02);
		editWho.setOnTouchListener(this);
		editWhy = (EditText) findViewById(R.id.EditText03);
		editWhy.setOnTouchListener(this);

		editDesc = (EditText) findViewById(R.id.EditText06);
		editDesc.setOnTouchListener(this);

		email = this.getIntent().getStringExtra("email");

		spinCategory = (Spinner) findViewById(R.id.Spinner01);
		fillSpinner();

		imageView = (ImageView) findViewById(R.id.ImageView01);

	}

	public void onActivityResult(int i, int u, Intent intent) {

		BitmapFactory bmf = new BitmapFactory();

		
		pictureByte = intent.getByteArrayExtra("bit");
		
		
		Bitmap bm = bmf.decodeByteArray(pictureByte, 0, pictureByte.length);
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		
		Bitmap bmRotate = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
				bm.getHeight(), matrix, true);
		imageView.setImageBitmap(bmRotate);
	}

	public void fillSpinner() {

		String array[] = { "Fahrzeuge","Pflanzen","Sport","Tiere","Werkzeuge" };
//		String[] array = {};
//		JSONArray ja = null;
//		try {
//			ja = netMan.getCategories();
//
//			if (ja != null) {
//				Toast.makeText(this, ja.toString(), 5).show();
//				for (int i = 0; i < ja.length(); i++) {
//					array[i] = ja.getString(i);
//				}
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, array);

		spinCategory.setAdapter(adapter);

	}

	public void buildCard() {
		Boolean isReady = true;

		String term = editTerm.getText().toString();
		String ansWhat = editWhat.getText().toString();
		String ansWho = editWho.getText().toString();
		String ansWhy = editWhy.getText().toString();
		String description = editDesc.getText().toString();
		String category = (String) spinCategory.getSelectedItem().toString();

		if (term == null || term.matches("")) {
			Toast.makeText(this, "Bezeichnung fehlt!", 5).show();
			editWhat.setBackgroundColor(Color.RED);
			isReady = false;
		}

		if (ansWhat == null || ansWhat.matches("")) {
			Toast.makeText(this, "Antwort \"Was ist das?\" fehlt!", 5).show();
			editWhat.setBackgroundColor(Color.RED);
			isReady = false;
		}

		if (ansWhat == null || ansWhat.matches("")) {
			Toast.makeText(this, "Antwort \"Was ist das?\" fehlt!", 5).show();
			editWhat.setBackgroundColor(Color.RED);
			isReady = false;
		}

		if (ansWho == null || ansWho.matches("")) {
			Toast.makeText(this, "Antwort \"Wer benutzt es?\" fehlt!", 5)
					.show();
			editWhy.setBackgroundColor(Color.RED);
			isReady = false;
		}

		if (ansWhy == null || ansWhy.matches("")) {
			Toast.makeText(this, "Antwort \"Wozu benutzt man es?\" fehlt!", 5)
					.show();
			editWho.setBackgroundColor(Color.RED);
			isReady = false;
		}

		if (description == null || description.matches("")) {
			Toast.makeText(this, "Beschribung fehlt!", 5).show();
			editWho.setBackgroundColor(Color.RED);
			isReady = false;
		}

		// Picture Byte[] empty
		if (pictureByte.length == 0 || pictureByte == null) {
			Toast.makeText(this, "Das Bild fehlt!", 5).show();
			btnPicture.setBackgroundColor(Color.RED);
			isReady = false;
		}

		// call createCard in NetworkManager
		if (isReady) {
			JSONObject json = netMan.createCard(term, description, pictureByte.toString(),
					category, ansWhat, ansWho, ansWhy, email);
			try {
				if ((Boolean) json.get("result")) {
					Toast.makeText(this, "Karte erfolgreich angelegt!", 5)
							.show();
					finish();
				} else {
					Toast.makeText(this, "Karte konnte nicht angelegt werden!",
							5).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean onTouch(View v, MotionEvent event) {

		if (v.equals(editWhat)) {
			EditText edit = (EditText) v;
			editWhat.setBackgroundColor(Color.WHITE);
		}
		if (v.equals(editWho)) {
			editWho.setBackgroundColor(Color.WHITE);
		}
		if (v.equals(editWhy)) {
			editWhy.setBackgroundColor(Color.WHITE);
		}
		if (v.equals(editTerm)) {
			editTerm.setBackgroundColor(Color.WHITE);
		}
		if (v.equals(editDesc)) {
			editDesc.setBackgroundColor(Color.WHITE);
		}

		return false;
	}

	public void onClick(View v) {

		if (v.equals((Button) btnPicture)) {
			v.setBackgroundColor(Color.WHITE);
			Intent myIntent = new Intent(this, TakePicture.class);

			try {
				this.startActivityForResult(myIntent, 1);
			} catch (Exception ex) {
				Toast.makeText(this, ex.getMessage().toString(), 10).show();
			}
		}
		if (v.equals((Button) btnCreateCard)) {
			buildCard();
		}

	}
}
