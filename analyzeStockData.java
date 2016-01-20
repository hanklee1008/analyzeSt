import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;




public class analyzeStockData {
public String getStockCapital(String url)
{
	String s="";
	try{
		URL ur=new URL(url);
		HttpURLConnection hc=(HttpURLConnection) ur.openConnection();
		hc.connect();
		//s=hc.getResponseMessage();
		InputStream input=hc.getInputStream();
		File f=new File("c:/tt.txt");
		FileWriter fw=new FileWriter(f);
		
		int idx;
		do{
		byte[] data = new byte[1024]; 
        idx = input.read(data);
        if (idx!=-1)
        {
        String str = new String(data, 0, idx); 
		s=s+str;
        }
		//System.out.print("1");
		//System.out.print(s);
		}
		while (idx!=-1);
		int p=s.indexOf("l015\">");
		int p1=s.indexOf("<", p);
		System.out.println(s.substring(p+6,p1).replaceAll(",", ""));
		fw.write(s);
		fw.close();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return s;
}
public static void main(String[] s)
{
	analyzeStockData asd=new analyzeStockData();
	asd.getStockCapital("http://www.cnyes.com/twstock/intro/2330.htm");
}
}
