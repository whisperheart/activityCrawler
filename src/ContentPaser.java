import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ContentPaser {
	protected static File inf = new File("E:\\","urls.txt");
	protected static File outf = new File("E:\\","activities.txt");
	
	protected static Collection actList = new ArrayList(3);
	
	public static void readFileByLines(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                if (!actList.contains(tempString))
                  	actList.add(tempString);           
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
	
	public static void writeFileByLines(File file) {
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			DataOutputStream dos = new DataOutputStream(fos);
		    Object list[] = actList.toArray();
		    for ( int i=0;(i<list.length);i++ )
		    {
			       dos.writeChars((String)list[i] + "\n");
			       System.out.println(((String)list[i]));
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	 static public void main(String args[]) throws IOException 
	  {
		readFileByLines(inf);
		writeFileByLines(outf);
		System.out.print("test"); 
	  }	 
}
