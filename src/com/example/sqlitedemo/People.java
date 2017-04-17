package com.example.sqlitedemo;

public class People {
    private int ID = -1;
	private String Name;
    private int Age;
    private float Height;
    
    public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getAge() {
		return Age;
	}
	public void setAge(int age) {
		Age = age;
	}
	public float getHeight() {
		return Height;
	}
	public void setHeight(float height) {
		Height = height;
	}
	public String toString(){
		return Name+" "+Age +" "+Height;
	}
}
