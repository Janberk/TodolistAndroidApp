package org.dieschnittstelle.mobile.android.todolist.db;

import org.dieschnittstelle.mobile.android.todolist.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserdataHandler extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "users.db";
	private static final int DATABASE_VERSION = 1;

	public static final String USER_TABLE = "usersdb";

	public static final String USER_ID = "_id";
	public static final String USER_NAME = "name";
	public static final String USER_SURNAME = "surname";
	public static final String USER_EMAIL = "email";
	public static final String USER_PASSWORD = "password";

	public UserdataHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createDB = "CREATE TABLE " + USER_TABLE + " (" + USER_ID
				+ " INTEGER PRIMARY KEY, " + USER_NAME + " TEXT, "
				+ USER_SURNAME + " TEXT, " + USER_EMAIL + " TEXT NOT NULL, "
				+ USER_PASSWORD + " TEXT NOT NULL)";

		db.execSQL(createDB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST " + USER_TABLE);
		onCreate(db);
	}

	public void deleteTable(String tableName) {
		String name = tableName;
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(name, null, null);
	}

	public long addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(USER_NAME, user.getName());
		values.put(USER_SURNAME, user.getSurname());
		values.put(USER_EMAIL, user.getEmail());
		values.put(USER_PASSWORD, user.getPassword());

		long id = db.insert(USER_TABLE, null, values);
		this.close();

		return id;
	}

	public boolean checkLogin(String mail, String pw) throws SQLException {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE
				+ " WHERE email = '" + mail + "' AND password = '" + pw + "'",
				null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				return true;
			}
		}
		this.close();

		return false;
	}

	public String getUserDataByEmail(String mail, String pw) {
		SQLiteDatabase db = this.getReadableDatabase();
		String user = "";

		Cursor cursor = db.query(USER_TABLE, null, USER_EMAIL + "=?" + " AND "
				+ USER_PASSWORD + "=?", new String[] { mail, pw }, null, null,
				null);

		if (cursor.moveToFirst()) {

			int nameIndex = cursor.getColumnIndex("name");
			int surnameIndex = cursor.getColumnIndex("surname");

			user = cursor.getString(nameIndex) + " "
					+ cursor.getString(surnameIndex);
		}
		this.close();

		return user;
	}

	public User cursorToUserdata(Cursor cursor) {
		User userData = new User();

		userData.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
		userData.setName(cursor.getString(cursor.getColumnIndex(USER_SURNAME)));
		userData.setName(cursor.getString(cursor.getColumnIndex(USER_EMAIL)));
		userData.setName(cursor.getString(cursor.getColumnIndex(USER_PASSWORD)));
		return userData;
	}

	public String getUserName(User user) {
		return user.toString();
	}

}
