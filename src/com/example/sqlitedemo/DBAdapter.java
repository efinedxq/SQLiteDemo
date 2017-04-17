package com.example.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    private static final String DB_NAME = "people.db";
    private static final String DB_TABLE = "peopleinfo";
    private static final int DB_VERSION = 1;
    
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AGE = "age";
    public static final String KEY_HEIGHT = "height";
    
    private SQLiteDatabase db;
    private final Context context;
    private DBOpenHelpser dbOpenHalper;
    
    private static class DBOpenHelpser extends SQLiteOpenHelper{

		public DBOpenHelpser(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}
		
		private static final String DB_CREATE = "create table "+DB_TABLE+"("+KEY_ID+" integer primary key autoincrement,"+KEY_NAME+" text not null,"+KEY_AGE+" integer,"+KEY_HEIGHT+" float);";

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE);
			onCreate(db);
		}
    }
    
    public DBAdapter(Context _context){
    	context = _context;
    }
    
    public void open() throws SQLiteException{
    	dbOpenHalper = new DBOpenHelpser(context, DB_NAME, null, DB_VERSION);
    	try{
    		db = dbOpenHalper.getWritableDatabase();
    	}catch (Exception e) {
			// TODO: handle exception
    		db = dbOpenHalper.getReadableDatabase();
		}
    }
    public void close(){
    	if(db != null){
    		db.close();
    		db = null;
    	}
    }
    
    
    public long insert(People people){
    	ContentValues newValues = new ContentValues();
    	
    	newValues.put(KEY_NAME, people.getName());
    	newValues.put(KEY_AGE, people.getAge());
    	newValues.put(KEY_HEIGHT, people.getHeight());
    	
    	return db.insert(DB_TABLE, null, newValues);
    }
    
    public long deleteOneData(long id){
    	return db.delete(DB_TABLE, KEY_ID+"="+id,null);
    }
    
    public People[] queryAllData(){
    	Cursor results = db.query(DB_TABLE,new String[]{KEY_ID,KEY_NAME,KEY_AGE,KEY_HEIGHT},null,null,null,null,null);
    	return  ConvertToPeople(results);
    }
    
    public People[] querySome(int age){
    	Cursor results = db.query(DB_TABLE,new String[]{KEY_ID,KEY_NAME,KEY_AGE,KEY_HEIGHT},KEY_AGE+"="+age,null,null,null,null);
    	return  ConvertToPeople(results);
    }
    
    public long updateOneData(People people){
        ContentValues newValues = new ContentValues();
    	
    	newValues.put(KEY_NAME, people.getName());
    	newValues.put(KEY_AGE, people.getAge());
    	newValues.put(KEY_HEIGHT, people.getHeight());
    	
    	return db.update(DB_TABLE, newValues, KEY_ID+"="+people.getID(),null);
    }
    
    private People[] ConvertToPeople(Cursor cursor){
    	int resultCounts = cursor.getCount();
    	if(resultCounts == 0||!cursor.moveToFirst()){
    		return null;
    	}
    	People[] peoples = new People[resultCounts];
    	for(int i = 0; i < resultCounts ; i ++){
    		peoples[i] = new People();
    		peoples[i].setID(cursor.getInt(0));
    		peoples[i].setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
    		peoples[i].setAge(cursor.getInt(cursor.getColumnIndex(KEY_AGE)));
    		peoples[i].setHeight(cursor.getFloat(cursor.getColumnIndex(KEY_HEIGHT)));
    		cursor.moveToNext();
    	}
    	
    	return peoples;
    }
}
