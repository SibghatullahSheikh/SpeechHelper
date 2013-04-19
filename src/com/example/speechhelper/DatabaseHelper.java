package com.example.speechhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private final static int DATABASE_VERSION =1;
	private final static String DATABASE_NAME = "database";

	private final static String CREATE_PROJECT_TABLE = "CREATE TABLE ProjectTable (_id INTEGER PRIMARY KEY, project_name TEXT, project_time INTEGER)";
	private final static String CREATE_NOTE_TABLE="CREATE TABLE NoteTable (_id INTEGER PRIMARY KEY, note_content TEXT, start_time INTEGER, end_time INTEGER, project_id INTEGER)";
	//private final static String CREATE_VIDEO_TABLE;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_NOTE_TABLE); // create note table
		db.execSQL(CREATE_PROJECT_TABLE); //create project table
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS ProjectTable");
		db.execSQL("DROP TABLE IF EXISTS NoteTable");
		 onCreate( db);
	}

	public void addProjectData(String projectName, int projectTime){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("project_name", projectName);
		cv.put("project_time", projectTime);
		db.insert("ProjectTable", null, cv);
		db.close();
	}

	public void addNoteData(String content, int startTime, int endTime, int projectId){	
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("note_content", content);
		cv.put("start_time", startTime);
		cv.put("end_time", endTime);
		cv.put("project_id", projectId);
		
		db.insert("NoteTable", null, cv);
		db.close();
	}
	
	//update
	public void updateNote(String content, int startTime, int endTime, int id){
		SQLiteDatabase db = this.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        values.put("note_content",content );
        values.put("start_time", startTime );
        values.put("end_time", endTime);
 
        // updating row
         db.update("NoteTable", values, "_id=?",
                new String[] { String.valueOf(id) });
		
	}
	
	//delete data 
	public void deleteData(String table, int id){
		SQLiteDatabase db = getWritableDatabase();  
		db.delete(table, "_id=?", new String[]{Integer.toString(id)});
	}
	
//	public int getProjectId(String projectName){
//		SQLiteDatabase db = this.getWritableDatabase();
//		
//        Cursor c = db.query("ProjectTable", new String[]{"_id"}, "project_name=?", new String[]{projectName}, null, null,null, null);
//        
//        if (c != null)
//        c.moveToFirst();
//        
//        int columnIndex=c.getColumnIndex("_id");
//		int id = c.getInt(columnIndex);
//     
//		return id;
//	}
	
	public Cursor query(String tableName,int projectId){
		SQLiteDatabase db = getWritableDatabase(); 
		Log.d("tag5", Integer.toString(projectId)); //log5
		
		//Cursor c= db.rawQuery("SELECT * FROM "+ tableName +" WHERE project_id=?", new String[]{String.valueOf(projectId)});
		//Log.d("tag6", Integer.toString(c.getCount()));
		Cursor c = db.query(tableName,
			
			null,"project_id=?",new String[]{Integer.toString(projectId)},null, ///////
			null, "_id desc", null);
		return c;
		
	}
//	public Cursor query(String tableName){
//		SQLiteDatabase db = getWritableDatabase(); 
//		
//		Cursor c = db.query(tableName,
//				null,null,null,null,
//				null, null, null);
//		return c;
//		
//	}

}
