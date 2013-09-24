package org.dieschnittstelle.mobile.android.todolist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dieschnittstelle.mobile.android.todolist.client.TodolistRESTAccessorTest;
import org.dieschnittstelle.mobile.android.todolist.db.UserdataHandler;
import org.dieschnittstelle.mobile.android.todolist.handler.ColorHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	protected static final String logger = LoginActivity.class.getSimpleName();

	private EditText editEmail;
	private EditText editPassword;
	private Button btnLogin;
	private TextView linkToRegScreen;

	private UserdataHandler uDbHandler;

	private ColorHandler colorHandler = new ColorHandler();

	private TextWatcher watcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);

		uDbHandler = new UserdataHandler(this);

		watcher = new LocalTextWatcher();

		colorHandler
				.setActivityBackground(this, colorHandler.getMyColorBlack());

		editEmail = (EditText) findViewById(R.id.login_edit_email);
		editPassword = (EditText) findViewById(R.id.login_edit_password);

		btnLogin = (Button) findViewById(R.id.login_Btn);
		colorHandler.setButtonColor(btnLogin,
				colorHandler.getMyColorRoyalblue(),
				colorHandler.getMyColorWhite());

		linkToRegScreen = (TextView) findViewById(R.id.link_to_register);
		colorHandler.setTextColor(linkToRegScreen,
				colorHandler.getMyColorSkyblue());

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkLogin();
			}

		});

		linkToRegScreen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});

		editEmail.addTextChangedListener(watcher);
		editPassword.addTextChangedListener(watcher);

//		if (TodolistRESTAccessorTest.callWebapp()) {
//			Log.d(logger, "****** REST Server is available *");
//		} else {
//			Log.d(logger, "* REST Server is NOT available ******");
//		}

	}

	private void checkLogin() {

		String email = editEmail.getText().toString();
		String password = editPassword.getText().toString();

		try {

			String user = uDbHandler.getUserDataByEmail(email, password);

			if (uDbHandler.checkLogin(email, password)) {
				Toast.makeText(LoginActivity.this, "Welcome " + user,
						Toast.LENGTH_LONG).show();

				Log.i(logger, "onClick()" + user);

				Intent intent = new Intent(LoginActivity.this,
						TodolistActivity.class);
				startActivity(intent);
			} else if (confirmDelete()) {
				uDbHandler.deleteTable("usersdb");
				Toast.makeText(LoginActivity.this,
						"Table 'usersdb' successfully deleted!",
						Toast.LENGTH_LONG).show();
			} else {
				onResume();
				Toast.makeText(LoginActivity.this,
						"Invalid Username or Password!", Toast.LENGTH_LONG)
						.show();
			}

		} catch (Exception e) {
			String err = "Got Exception: " + e;
			Log.e(logger, err, e);
			Toast.makeText(LoginActivity.this, err, Toast.LENGTH_LONG).show();
		}

	}

	private class LocalTextWatcher implements TextWatcher {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			boolean validEmail = validateEmail(editEmail.getText().toString());
			boolean validPass = validatePassword(editPassword.getText()
					.toString());

			if (!validEmail && editTextNotEmpty(editEmail)) {
				editEmail.setError("Invalid Email!");
			}
			if (!validPass && editTextNotEmpty(editPassword)) {
				editPassword.setError("Only numbers. At least 6!");
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			enableSubmitIfReady();
		}
	}

	private void enableSubmitIfReady() {

		boolean validEmail = validateEmail(editEmail.getText().toString());
		boolean validPass = validatePassword(editPassword.getText().toString());
		boolean passwordReady = false;

		if (validPass) {
			passwordReady = true;
		}

		if (validEmail && passwordReady) {
			btnLogin.setEnabled(true);
		}

	}

	private boolean validateEmail(String email) {

		String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

		Pattern pattern = Pattern.compile(validEmail);
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}

	private boolean validatePassword(String password) {

		String validPass = "[0-9]+";

		Pattern pattern = Pattern.compile(validPass);
		Matcher matcher = pattern.matcher(password);

		if (password.length() > 5) {
			return matcher.matches();
		} else {
			return false;
		}
	}

	public boolean confirmDelete() {
		String email = editEmail.getText().toString();
		String password = editPassword.getText().toString();
		String deleteEmail = "deleteAllUsers@users.de";
		String deletePass = "000000";

		if ((deleteEmail.equals(email)) && (deletePass.equals(password))) {
			return true;
		}
		return false;
	}

	public boolean editTextNotEmpty(EditText edittext) {
		if (edittext.getText().toString().trim().length() < 1)
			return false;
		else
			return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.onCreate(null);
		editEmail.setText("");
		editPassword.setText("");
		btnLogin.setEnabled(false);
	}

}
