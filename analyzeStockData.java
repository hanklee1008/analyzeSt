import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import jxl.Sheet;
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
                	temp[0]=tagtd.get(0).html().replaceAll(",", "");
                	temp[1]=tagtd.get(1).html().replaceAll(",", "");
                	temp[2]=tagtd.get(2).html().replaceAll(",", "");
                	temp[3]=tagtd.get(3).html().replaceAll(",", "");
                	temp[4]=tagtd.get(4).html().replaceAll(",", "");
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
	ArrayList<String[]> as=asd.getStockHistoryValue("3008","2015/09/01");
	
	asd.updateStockDailyKToExcel(as,"3008");
	
}
private void updateStockDailyKToExcel(ArrayList<String[]> as,String stocknum)
{
	try{
		String drive="d:/";
		WritableWorkbook writeBook=Workbook.createWorkbook(new File(drive+"software/sdata/new/new/"+stocknum+".xls"));  
		WritableSheet sss=writeBook.createSheet(stocknum, 0);
		
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
		BigDecimal b1,b2;
		for (int i=5;i<=initposition-1;i++)
		{
			if (i>=5)
			{
				temp=0;
				for (int j=i-4;j<=i;j++)
				{
					b1 = new BigDecimal(Double.toString(temp));
			        b2 = new BigDecimal(sss.getCell(4,j).getContents());
			        temp=b1.add(b2).doubleValue();
				}
				b1 = new BigDecimal(Double.toString(temp));
		        b2 = new BigDecimal("5");
				sss.addCell(new Number(5,i,b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
			if (i>=10)
			{
				temp=0;
				for (int j=i-9;j<=i;j++)
				{
					b1 = new BigDecimal(Double.toString(temp));
			        b2 = new BigDecimal(sss.getCell(4,j).getContents());
			        temp=b1.add(b2).doubleValue();
				}
				b1 = new BigDecimal(Double.toString(temp));
		        b2 = new BigDecimal("10");
				sss.addCell(new Number(6,i,b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
			if (i>=20)
			{
				temp=0;
				for (int j=i-19;j<=i;j++)
				{
					b1 = new BigDecimal(Double.toString(temp));
			        b2 = new BigDecimal(sss.getCell(4,j).getContents());
			        temp=b1.add(b2).doubleValue();
				}
				b1 = new BigDecimal(Double.toString(temp));
		        b2 = new BigDecimal("20");
				sss.addCell(new Number(7,i,b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
			if (i>=60)
			{
				temp=0;
				for (int j=i-59;j<=i;j++)
				{
					b1 = new BigDecimal(Double.toString(temp));
			        b2 = new BigDecimal(sss.getCell(4,j).getContents());
			        temp=b1.add(b2).doubleValue();
				}
				b1 = new BigDecimal(Double.toString(temp));
		        b2 = new BigDecimal("60");
				sss.addCell(new Number(8,i,b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue()));
			}

		}
		
		writeBook.write();
		writeBook.close();

		updateStockWeeklyKToExcel(new File(drive+"software/sdata/new/new/"+stocknum+".xls"),stocknum);
	}
	catch (Exception e) {
        e.printStackTrace();
    }
}
private void updateStockWeeklyKToExcel(File f,String stocknum)
{
	try{
		String drive="d:/";
		ArrayList<double[]> as=new ArrayList<double[]>();
		
		//Sheet dailysh=Workbook.getWorkbook(new File(drive+"software/sdata/new/new/"+stocknum+".xls")).getSheet(0);
		Workbook wb=Workbook.getWorkbook(f);
		WritableWorkbook writeBook=Workbook.createWorkbook(f,wb); 
		Sheet dailysh=wb.getSheet(0);
		WritableSheet weeklysh=writeBook.createSheet("999", 1);
		
		weeklysh.addCell(new Label(0,0,"date"));
		weeklysh.addCell(new Label(1,0,"open"));
		weeklysh.addCell(new Label(2,0,"high"));
		weeklysh.addCell(new Label(3,0,"low"));
		weeklysh.addCell(new Label(4,0,"close"));
		weeklysh.addCell(new Label(5,0,"SMA4"));
		weeklysh.addCell(new Label(6,0,"SMA13"));
		weeklysh.addCell(new Label(7,0,"quantity"));
		
		int initposition=1,initWeeklyPos=1;		
		long sundaytime;
		double open,high,low,close,quantity;
				
		while(initposition<dailysh.getRows())
		{
			open=Double.parseDouble(dailysh.getCell(1,initposition).getContents());
			high=Double.parseDouble(dailysh.getCell(2,initposition).getContents());
			low=Double.parseDouble(dailysh.getCell(3,initposition).getContents());
			close=Double.parseDouble(dailysh.getCell(4,initposition).getContents());
			quantity=Double.parseDouble(dailysh.getCell(9,initposition).getContents());
			
			sundaytime=sundayTime(dailysh.getCell(0, initposition).getContents());
			
			weeklysh.addCell(new Label(0,initWeeklyPos,dailysh.getCell(0,initposition).getContents()));
			weeklysh.addCell(new Label(1,initWeeklyPos,open+""));
			
			initposition++;
			
			while(initposition<dailysh.getRows()&&new SimpleDateFormat("yyyy/MM/dd").parse(dailysh.getCell(0,initposition).getContents()).getTime()<sundaytime)
			{
				double d;
				if (high<(d=Double.parseDouble(dailysh.getCell(2,initposition).getContents())))
				{
					high=d;
				}
				if (low>(d=Double.parseDouble(dailysh.getCell(3,initposition).getContents())))
				{
					low=d;
				}
				quantity+=Double.parseDouble(dailysh.getCell(9,initposition).getContents());
				
				initposition++;
			}
			close=Double.parseDouble(dailysh.getCell(4,initposition-1).getContents());
						
			weeklysh.addCell(new Label(2,initWeeklyPos,high+""));
			weeklysh.addCell(new Label(3,initWeeklyPos,low+""));
			weeklysh.addCell(new Label(4,initWeeklyPos,close+""));
			weeklysh.addCell(new Label(7,initWeeklyPos,quantity+""));
			
			initWeeklyPos++;
		}
				
		double temp=0;
		BigDecimal b1,b2;
		for (int i=4;i<=initWeeklyPos-1;i++)
		{
			if (i>=4)
			{
				temp=0;
				for (int j=i-3;j<=i;j++)
				{
					b1 = new BigDecimal(Double.toString(temp));
			        b2 = new BigDecimal(weeklysh.getCell(4,j).getContents());
			        temp=b1.add(b2).doubleValue();
				}
				b1 = new BigDecimal(Double.toString(temp));
		        b2 = new BigDecimal("4");
		        weeklysh.addCell(new Number(5,i,b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
			if (i>=13)
			{
				temp=0;
				for (int j=i-12;j<=i;j++)
				{
					b1 = new BigDecimal(Double.toString(temp));
			        b2 = new BigDecimal(weeklysh.getCell(4,j).getContents());
			        temp=b1.add(b2).doubleValue();
				}
				b1 = new BigDecimal(Double.toString(temp));
		        b2 = new BigDecimal("13");
		        weeklysh.addCell(new Number(6,i,b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
		}
		
		writeBook.write();
		writeBook.close();
	
	}
	catch (Exception e) {
        e.printStackTrace();
    }
}
private long sundayTime(String date)
{	
	Calendar cal = Calendar.getInstance(); 
	try{
		cal.setTime(new SimpleDateFormat("yyyy/MM/dd").parse(date));  
		
		return cal.getTimeInMillis()+(8-cal.get(Calendar.DAY_OF_WEEK))*(24*60*60*1000);

	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return 0;
}
}
