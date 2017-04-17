package com.example.sqlitedemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;

public class MainActivity extends Activity {

	private List<People> list = new ArrayList<People>();
	// 定义ListView对象变量---View
	private ListView listview;
	// 存放数据的List<String>对象---Model
	private List<Map<String, Object>> showlist;

	private People people;
	
	private DBAdapter dbAdapter;
	
	private EditText etName;
	private EditText etAge;
	private EditText etHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listview = (ListView) findViewById(R.id.work_3);
		showlist = new ArrayList<Map<String, Object>>();

		etName = (EditText) findViewById(R.id.et_name);
		etAge = (EditText) findViewById(R.id.et_age);
		etHeight = (EditText) findViewById(R.id.et_height);

		Button btAdd = (Button) findViewById(R.id.add);
		Button btModify = (Button) findViewById(R.id.modify);
		Button btDelete = (Button) findViewById(R.id.delete);
		Button btQuery = (Button) findViewById(R.id.query);

		dbAdapter = new DBAdapter(getApplicationContext());
		
		btAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addUser();
				showAll();
			}
		});

		btModify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				update();
				showAll();
			}
		});
		btDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteUser();
				showAll();
			}
		});
		btQuery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSome();
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Map<String, Object> map =  (Map<String, Object>)listview.getItemAtPosition(position); 
				people = new People();
				people.setID((Integer)map.get("id"));
				people.setName((String)map.get("name"));
				people.setAge((Integer)map.get("age"));
				people.setHeight((Float)map.get("height"));
				
				etName.setText(people.getName());
				etAge.setText(people.getAge()+"");
				etHeight.setText(people.getHeight()+"");
			}
		});
	}
	@SuppressLint("ShowToast") private void showSome() {
		int age = -1; 
		StringBuffer str = new StringBuffer("查询结果：");
		if(!etAge.getText().toString().equals("")){
			age = Integer.parseInt(etAge.getText().toString());
		}
		Log.d("showSome",age+"");
		
		if(age<0){	return ;}
		
		dbAdapter.open();
		People[] peoples = dbAdapter.querySome(age);
		dbAdapter.close();
        if(peoples!=null){
        	for(int i = 0 ; i < peoples.length; i ++){
        		str.append("\n"+peoples[i].toString());
        	}
        }
        Log.d("showSome",str.toString());
		Toast.makeText(MainActivity.this, str.toString() , Toast.LENGTH_SHORT).show();
	}

	private void update(){
		float height = 0;
		int age = 0; 
		if(!etHeight.getText().toString().equals("")){
			height = Float.parseFloat(etHeight.getText().toString());
		}
		if(!etAge.getText().toString().equals("")){
			age = Integer.parseInt(etAge.getText().toString());
		}
		People user = new People();
		user.setName(etName.getText().toString());
		user.setHeight(height);
		user.setAge(age);
		
		Log.d("updateUser",user.toString());
		
		if(user.getName().equals("")){
			return ;
		}
		
		dbAdapter.open();
		dbAdapter.updateOneData(user);
		dbAdapter.close();
	}
	
	private boolean deleteUser() {
		boolean flag = false;
		dbAdapter.open();
		if(people!=null&&dbAdapter.deleteOneData(people.getID())==0){
			flag = true;
		}
		dbAdapter.close();
		return flag;
	}

	private void addUser() {
		
		float height = 0;
		int age = 0; 
		if(!etHeight.getText().toString().equals("")){
			height = Float.parseFloat(etHeight.getText().toString());
		}
		if(!etAge.getText().toString().equals("")){
			age = Integer.parseInt(etAge.getText().toString());
		}
		People user = new People();
		user.setName(etName.getText().toString());
		user.setHeight(height);
		user.setAge(age);
		
		Log.d("addUser",user.toString());
		
		if(user.getName().equals("")){
			return ;
		}
		
		dbAdapter.open();
		dbAdapter.insert(user);
		dbAdapter.close();

	}

	private void showAll() {
		People user;
		
		list.clear();
		
		dbAdapter.open();
		arrayToList(dbAdapter.queryAllData(),list);
		dbAdapter.close();
		
		showlist.clear();
		for (int i = 0; i < list.size(); i++) {
			user = list.get(i);
			Map<String, Object> map;

			map = new HashMap<String, Object>();
			map.put("name", user.getName());
			map.put("height", user.getHeight());
			map.put("age", user.getAge());
			map.put("id", user.getID());
			showlist.add(map);
		}
		// 创建Adapter对象
		SimpleAdapter adapter = new SimpleAdapter(this, // 上下文对象
				showlist, // 存放数据的List对象
				R.layout.item, // 每行的布局
				new String[] { "id", "name", "age", "height" }, // 数据对象Map中的列名--键
				new int[] { R.id.id, R.id.name, R.id.age, R.id.height }); // 列内容--Listview中的控件ID，对应控件用于显示Map对象中的值

		listview.setAdapter(adapter);
	}
	
	private void arrayToList(People[] o, List<People> list){
		if(o==null) return;
		for(int i = 0; i < o.length; i ++){
			list.add(o[i]);
		}
	}
}
