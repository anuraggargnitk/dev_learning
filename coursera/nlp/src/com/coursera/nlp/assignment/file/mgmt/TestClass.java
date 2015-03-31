package com.coursera.nlp.assignment.file.mgmt;

public class TestClass {

	public static void main(String[] args) {
		FileReader fl = FileReader.getInstance();
		FileWriter fw = FileWriter.getInstance();
		
		String dDrive = "D:/work/CVS/branch/AAUI/auth/dbscripts/mssql/";
		//String dDrive = "D:/work/CVS/branch/AAUI/framework/dbscripts/mysql/";
		//String readFile = "c:/new2.txt";
		//String readFile = dDrive + "ca-db-create-for-aaui-8.0.sql";
		//String readFile = dDrive + "drop-aaui-8.0.sql";
		String readFile = dDrive + "ca-db-seed-for-auth-8.0.sql";
		//String writeFile = "create.sql";
		//String writeFile = "drop.sql";
		String writeFile = "ca-db-seed-for-auth-8.0.sql";
		fw.write("c:/mssql/" + writeFile, fl.read(readFile));
		
		
	}
	
}
