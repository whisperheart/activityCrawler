package editor;

import java.io.File;

import constents.FileConst;

public class ActFetcher {
	
	protected String[][] actContentList = new String[1000000][9];
	
	protected int Num;
	
	public ActFetcher(int Num) {
		this.Num = Num;
	}
	
	public String[][] fetchFromFile() {
		 for(int i = 0; i < Num; i++){
		     File file = new File(FileConst.getContFile(String.valueOf(i)));
			 actContentList[i] = FileWR.readFileAsAct(file);
			 FileWR.log("Activity " + i, " has been fetched");
//			 System.out.println(actContentList[i][8]);
		 }
		 return actContentList;
	}
}
