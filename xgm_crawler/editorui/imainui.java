package editorUI;

public interface IMainUI {
	
	public void currentProcess(String rssMSG, String actMSG);
	
	public void succeedActCount();
	
	public void succeedRssCount();
	
	public void failedActCount();
	
	public void failedRssCount();
		
	public void currentLog(String msg);
}
