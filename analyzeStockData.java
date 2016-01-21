import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class analyzeStockData {
public String getStockCapital(String stocknum)
{
	String url="http://www.cnyes.com/twstock/intro/"+stocknum+".htm";
	String s="";
	try{
		URL ur=new URL(url);
		//System.out.println("1 ");
		HttpURLConnection hc=(HttpURLConnection) ur.openConnection();
		//System.out.println("2 ");
		InputStream input=hc.getInputStream();
		//System.out.println("3 ");
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
		}
		while (idx!=-1);
		/*int p=s.indexOf("l015\">");
		int p1=s.indexOf("<", p);
		System.out.println(s.substring(p+6,p1).replaceAll(",", ""));*/
		fw.write(s);
		fw.close();
		input.close();
		hc.disconnect();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return s;
}
public ArrayList<String[]> getStockHistoryValue(String stocknum,String startdate)
{
	String url="http://www.cnyes.com/twstock/ps_historyprice.aspx?code="+stocknum+"&ctl00$ContentPlaceHolder1$startText="+startdate;
	//String url="http://www.cnyes.com/twstock/ps_historyprice.aspx?code=1333&ctl00$ContentPlaceHolder1$startText=2015/11/01";
	ArrayList<String[]> value=new ArrayList<String[]>();
	
	try {System.out.println("here1 "); 
        Document doc = Jsoup.connect(url).timeout(60000).get();
        System.out.println("here2 ");  
        Elements classtab = doc.getElementsByClass("tab");
        Element thisOne = null;
        if (classtab.size()>0)
        {
        	thisOne=classtab.first();
        }
        Elements tagtable = thisOne.getElementsByTag("table");
        if (tagtable.size()>0)
        {
        	thisOne=tagtable.first();
        }       
        Elements tagtr = thisOne.getElementsByTag("tr");
        if (tagtr.size()>0)
        {
        	for(int i=tagtr.size()-1;i>0;i--)
            {
                thisOne = tagtr.get(i);
                Elements tagtd = thisOne.getElementsByTag("td");
                if (tagtd.size()>0)
                {
                	//for(Iterator it = tagtd.iterator(); it.hasNext();)
                	
                	String[] temp=new String[6];
                	temp[0]=tagtd.get(0).html();
                	temp[1]=tagtd.get(1).html();
                	temp[2]=tagtd.get(2).html();
                	temp[3]=tagtd.get(3).html();
                	temp[4]=tagtd.get(4).html();
                	temp[5]=tagtd.get(7).html().replaceAll(",", "");
                	//System.out.println(temp[0]+" "+temp[1]+" "+temp[2]+" "+temp[3]+" "+temp[4]+" "+temp[5]);  
                	value.add(temp);
                }  
            }
        }      
        
    } catch (IOException e) {
        e.printStackTrace();
    }
	return value;
}
public static void main(String[] s)
{
	analyzeStockData asd=new analyzeStockData();
	//asd.getStockCapital("1333");
	ArrayList<String[]> as=asd.getStockHistoryValue("1333","2015/11/01");
	
	asd.updateExcel(as);
	
}
private void updateExcel(ArrayList<String[]> as)
{
	try{
		String drive="d:/";
		//Workbook workBook=Workbook.getWorkbook(new File(drive+"software/sdata/new/old/1333.xls"));
		WritableWorkbook writeBook=Workbook.createWorkbook(new File(drive+"software/sdata/new/new/1333.xls"));  
		WritableSheet sss=writeBook.createSheet("1333", 0);
		
		sss.addCell(new Label(0,0,"date"));
		sss.addCell(new Label(1,0,"open"));
		sss.addCell(new Label(2,0,"high"));
		sss.addCell(new Label(3,0,"low"));
		sss.addCell(new Label(4,0,"close"));
		sss.addCell(new Label(5,0,"SMA5"));
		sss.addCell(new Label(6,0,"SMA10"));
		sss.addCell(new Label(7,0,"SMA20"));
		sss.addCell(new Label(8,0,"SMA60"));
		sss.addCell(new Label(9,0,"quantity"));
		
		int initposition=1;
		
		for (String[] st:as)
		{
			sss.addCell(new Label(0,initposition,st[0]));
			sss.addCell(new Number(1,initposition,Double.parseDouble(st[1])));
			sss.addCell(new Number(2,initposition,Double.parseDouble(st[2])));
			sss.addCell(new Number(3,initposition,Double.parseDouble(st[3])));
			sss.addCell(new Number(4,initposition,Double.parseDouble(st[4])));
			sss.addCell(new Number(9,initposition,Double.parseDouble(st[5])));
			
			initposition++;
		}	
		
		double temp=0;
		for (int i=5;i<=initposition-1;i++)
		{
			if (i>=5)
			{
				temp=0;
				for (int j=i-4;j<=i;j++)
				{
					temp+=Double.parseDouble(sss.getCell(4,j).getContents());
					temp=new BigDecimal(temp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
				sss.addCell(new Number(5,i,new BigDecimal(temp/5).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
				
			}
			if (i>=10)
			{
				temp=0;
				for (int j=i-9;j<=i;j++)
				{
					temp+=Double.parseDouble(sss.getCell(4,j).getContents());
					temp=new BigDecimal(temp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}System.out.println(" "+temp/10);
				sss.addCell(new Number(6,i,new BigDecimal(temp/10).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
				
			}
			if (i>=20)
			{
				temp=0;
				for (int j=i-19;j<=i;j++)
				{
					temp+=Double.parseDouble(sss.getCell(4,j).getContents());
					temp=new BigDecimal(temp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
				sss.addCell(new Number(7,i,new BigDecimal(temp/20).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
			if (i>=60)
			{
				temp=0;
				for (int j=i-59;j<=i;j++)
				{
					temp+=Double.parseDouble(sss.getCell(4,j).getContents());
					temp=new BigDecimal(temp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
				sss.addCell(new Number(8,i,new BigDecimal(temp/60).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}

		}
		
		writeBook.write();
		writeBook.close();
	
	}
	catch (Exception e) {
        e.printStackTrace();
    }
}
}
