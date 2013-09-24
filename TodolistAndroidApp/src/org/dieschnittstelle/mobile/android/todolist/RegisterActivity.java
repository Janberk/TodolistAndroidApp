package org.dieschnittstelle.mobile.android.todolist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dieschnittstelle.mobile.android.todolist.db.UserdataHandler;
import org.dieschnittstelle.mobile.android.todolist.handler.ColorHandler;
import org.dieschnittstelle.mobile.android.todolist.model.User;

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
import android.widget.Toast;

public class RegisterActivity extends Activity {

	protected static final String logger = RegisterActivity.class
			.getSimpleName();

	private EditText editName;
	private EditText editSurname;
	private EditText editEmail;
	private EditText editPassword;
	private Button btnRegister;

	private UserdataHandler uDbHandler;

	private ColorHandler colorHandler = new ColorHandler();

	private TextWatcher watcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register);

		uDbHandler = new UserdataHandler(this);

		watcher = new LocalTextWatcher();

		colorHandler
				.setActivityBackground(this, colorHandler.getMyColorBlack());

		editName = (EditText) findViewById(R.id.regis_edit_name);
		editSurname = (EditText) findViewById(R.id.regis_edit_surname);
		editEmail = (EditText) findViewById(R.id.regis_edit_email);
		editPassword = (EditText) findViewById(R.id.regis_edit_password);

		btnRegister = (Button) findViewById(R.id.register_Btn);
		btnRegister.setEnabled(false);
		colorHandler.setButtonColor(btnRegister,
				colorHandler.getMyColorRoyalblue(),
				colorHandler.getMyColorWhite());

		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createUser();
			}
		});
		editEmail.addTextChangedListener(watcher);
		editPassword.addTextChangedListener(watcher);
	}

	private void createUser() {

		User newUser = new User();

		newUser.setName(editName.getText().toString());
		newUser.setSurname(editSurname.getText().toString());
		newUser.setEmail(editEmail.getText().toString());
		newUser.setPassword(editPassword.getText().toString());

		uDbHandler.addUser(newUser);
		// TodoRESTHandler.writeCurrentUserToRest(newUser);

		Log.i(logger, " onClick: " + newUser.getPersonalData());
		Toast.makeText(RegisterActivity.this, "User added successfully!",
				Toast.LENGTH_LONG).show();

		Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
		startActivity(intent);

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
				editPassword.setError("Invalid Password!");
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
			btnRegister.setEnabled(true);
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

	public boolean editTextNotEmpty(EditText edittext) {
		if (edittext.getText().toString().trim().length() < 1)
			return false;
		else
			return true;
	}

}
