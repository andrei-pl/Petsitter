package com.example.andrey.petsitter.Database;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.example.andrey.petsitter.Models.AnimalType;
import com.example.andrey.petsitter.Models.Classified;
import com.example.andrey.petsitter.R;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.widget.Toast;

public class UsersDB {

	private SQLiteDatabase userDb;
	//private static final String TABLE_USERS = "Users";
	private static final String TABLE_CLASSIFIEDS = "Classifieds";

	public UsersDB(SQLiteDatabase db) {
		this.userDb = db;
	}

//	public void registerUser(String username, String password,
//			String confirmPassword) throws Exception {
//
//		username = username.trim();
//
//		if (!password.equals(confirmPassword)) {
//			throw new Exception("Passsword and Confirm Password do not match!");
//		}
//		if (Character.isDigit(username.charAt(0))) {
//			throw new Exception("Username cannot start with digit!");
//		}
//		if (!Character.isLetter(username.charAt(0))) {
//			throw new Exception(
//					"The first symbol of username must be a letter!");
//		}
//		if (username.length() < 3) {
//			throw new Exception("Username must be at least 3 symbols long!");
//		}
//		if (password.length() < 3) {
//			throw new Exception("Password must be at least 3 symbols long!");
//		}
//		if (findId(username) > 0) {
//			throw new Exception("User with this name already exist.");
//		}
//
//		for (int i = 1; i < username.length(); i++) {
//			if (!(Character.isLetter(username.charAt(i)) || Character.isDigit(username.charAt(i)) || username.charAt(i) == '_' || username.charAt(i) == '-')) {
//				throw new Exception("Username must contains only digits, letters or \"_\" or \"-\"!");
//			}
//		}
//
//		ContentValues user = new ContentValues();
//		user.put("username", username);
//		user.put("password", password);
//		userDb.insert(TABLE_USERS, null, user);
//		userDb.close();
//	}

//	public boolean loginUser(String username, String password) throws Exception {
//		Cursor userCursor = null;
//
//		if (username != null && password != null) {
//			String sql = "SELECT * FROM " + TABLE_USERS + " WHERE username='"
//					+ username + "' " + "AND password ='" + password + "'";
//
//			userCursor = userDb.rawQuery(sql, null);
//			boolean isLoggedIn = userCursor.moveToFirst();
//			if (isLoggedIn) {
//				// my user exist in the database record
//				userCursor.close();
//				return true;
//			}
//
//		}
//		if (userCursor != null)
//			userCursor.close();
//		return false;
//	}

    public boolean addClassified(Classified classified) {

        String id = classified.getId();
        String name = classified.getAuthorName();
        String address = classified.getAddress();
        String title = classified.getTitle();
        String description = classified.getDescription();
        String phone = classified.getPhone();
        AnimalType animalType = classified.getAnimalType();
        Bitmap picture = classified.getImage();
        Location location = classified.getLocation();
        byte[] imgData = getBitmapAsByteArray(picture);

        try {
            String sql = "INSERT INTO "
                    + TABLE_CLASSIFIEDS
                    + "(_id,name,address,title,description,phone,picture) VALUES('"
                    + id + "','"
                    + name + "','"
                    + address + "','"
                    + title + "','"
                    + description + "','"
                    + phone + "','" +
                    imgData + "');";
            userDb.execSQL(sql);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public ArrayList<Classified> getAllBasketItems() {
		ArrayList<Classified> temp = new ArrayList<Classified>();

		String sql = "SELECT * FROM " + TABLE_CLASSIFIEDS;

		Cursor allItems = userDb.rawQuery(sql, null);

		while (allItems.moveToNext()) {
            String id = allItems.getString(0);
            String name = allItems.getString(1);
            String address = allItems.getString(2);
            String title = allItems.getString(3);
            String description = allItems.getString(4);
            String phone = allItems.getString(5);
            String animal = allItems.getString(6);
            double latitude = allItems.getDouble(8);
            double longitude = allItems.getDouble(9);
            byte[] image = allItems.getBlob(allItems.getColumnIndex("picture"));
            Bitmap bitmap = getImage(image);

            if(bitmap == null){
                bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher);
            }

			Classified baseItem = new Classified(title, description, name, phone, address, bitmap);
            baseItem.setId(id);

			temp.add(baseItem);
		}

		return temp;
	}

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private Bitmap getImage(byte[] imgByte){

        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

}