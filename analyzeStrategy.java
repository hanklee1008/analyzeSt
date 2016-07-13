
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Sheet;
import jxl.Workbook;

public class analyzeStrategy {
	
	static String drive="c:/";
	
	public static void main(String[] s)
	{				
		System.out.println("\nanalyze start:"+new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));
		
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");	
		
		String filepath=drive+"software/sdata/15now/"+sdFormat.format(new Date());
		String resultfilepath=filepath+"result/";
			
		analyzeStrategy as=new analyzeStrategy();
		//as.collectStock(filepath,resultfilepath);
		as.analyze();
		
		System.out.println("\nanalyze end:"+new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));
	}
	public void collectStock(String filepath,String resultfilepath)
	{
		new File(filepath).mkdirs();			
		new File(resultfilepath).mkdirs();	
		analyzeStockData asd=new analyzeStockData();
		asd.findstock(new File(drive+"software/sdata/low15.xls"),filepath+"/","2015/03/02");
		analyze1(filepath,resultfilepath);
		analyze2(filepath,resultfilepath);
		analyze3(filepath,resultfilepath);
		analyze4(filepath,resultfilepath);	
		analyzeB1(filepath,resultfilepath);
		analyzeB2(filepath,resultfilepath);
	}
	private void analyze()
	{		
		String filepath=drive+"software/sdata/15test100/";
		String resultfilepath=drive+"software/sdata/";
		//analyze1(filepath,resultfilepath);
		//analyze2(filepath,resultfilepath);
		analyze3(filepath,resultfilepath);
		//analyze4(filepath,resultfilepath);	
		//analyzeB1(filepath,resultfilepath);
		//analyzeB2(filepath,resultfilepath);
	}
	private void analyze1(String filepath,String resultfilepath)
	{
		try{
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");			
			System.out.println("\ncompute start:"+sdFormat.format(new Date()));

			ArrayList<String[]> allTimePoint=new ArrayList<String[]>();		
			bullStrategy1 st1=new bullStrategy1();
			Workbook workbook;
			Sheet shd,shw;		

			analyzeStock s1=new analyzeStock();	
			File[] temp=new File(filepath).listFiles();
			for (File f:temp)
			{									
				workbook=Workbook.getWorkbook(f);
				shd=workbook.getSheet(0);
				shw=workbook.getSheet(1);
				
				
				st1.analyzeStock(shw,shd,allTimePoint,filepath,f.getName());

				workbook.close();
				
			}			
			s1.fillInAllcondition(allTimePoint);
			s1.fillInData(allTimePoint,new File(resultfilepath+"q-eow.xls"),20040301,0);
			s1.computeResult(allTimePoint,20040301,0);
			
			System.out.println("\ncompute end:"+sdFormat.format(new Date()));
		}
		catch (Exception e)
		{
			System.out.println("analyze1\n");
			e.printStackTrace();
		}
	}
	private void analyze2(String filepath,String resultfilepath)
	{
		try{
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");			
			System.out.println("\ncompute start:"+sdFormat.format(new Date()));

			ArrayList<String[]> allTimePoint=new ArrayList<String[]>();		
			bullStrategy11 st1=new bullStrategy11();
			Workbook workbook;
			Sheet shd,shw;		

			analyzeStock s1=new analyzeStock();	
			File[] temp=new File(filepath).listFiles();
			for (File f:temp)
			{									
				workbook=Workbook.getWorkbook(f);
				shd=workbook.getSheet(0);
				shw=workbook.getSheet(1);
				
				
				st1.analyzeStock(shw,shd,allTimePoint,filepath,f.getName());

				workbook.close();
				
			}			
			s1.fillInAllcondition(allTimePoint);
			s1.fillInData(allTimePoint,new File(resultfilepath+"q-mow.xls"),20040301,0);
			s1.computeResult(allTimePoint,20040301,0);
			
			System.out.println("\ncompute end:"+sdFormat.format(new Date()));
		}
		catch (Exception e)
		{
			System.out.println("analyze1\n");
			e.printStackTrace();
		}
	}
	private void analyze3(String filepath,String resultfilepath)
	{
		try{
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");			
			System.out.println("\ncompute start:"+sdFormat.format(new Date()));
			
			ArrayList<String[]> allTimePoint=new ArrayList<String[]>();		
			bullStrategy2 st1=new bullStrategy2();
			Workbook workbook;
			Sheet shd,shw;					
			
			File[] temp=new File(filepath).listFiles();
			for (File f:temp)
			{									
				workbook=Workbook.getWorkbook(f);
				shd=workbook.getSheet(0);
				shw=workbook.getSheet(1);
				
				
				st1.analyzeStock(shw,shd,allTimePoint,filepath,f.getName());

				workbook.close();
				
			}			
			
			analyzeStock s1=new analyzeStock();	
			s1.fillInAllcondition(allTimePoint);
			s1.fillInData(allTimePoint,new File(resultfilepath+"m-eow.xls"),20040301,0);
			s1.computeResult(allTimePoint,20040301,0);
			
			System.out.println("\ncompute end:"+sdFormat.format(new Date()));
		}
		catch (Exception e)
		{
			System.out.println("analyze2\n");
			e.printStackTrace();
		}
	}
	private void analyze4(String filepath,String resultfilepath)
	{
		try{
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");			
			System.out.println("\ncompute start:"+sdFormat.format(new Date()));
			
			ArrayList<String[]> allTimePoint=new ArrayList<String[]>();		
			bullStrategy22 st1=new bullStrategy22();
			Workbook workbook;
			Sheet shd,shw;		

			analyzeStock s1=new analyzeStock();	
			File[] temp=new File(filepath).listFiles();
			for (File f:temp)
			{									
				workbook=Workbook.getWorkbook(f);
				shd=workbook.getSheet(0);
				shw=workbook.getSheet(1);
				
				
				st1.analyzeStock(shw,shd,allTimePoint,filepath,f.getName());

				workbook.close();
				
			}			
			s1.fillInAllcondition(allTimePoint);
			s1.fillInData(allTimePoint,new File(resultfilepath+"m-mow.xls"),20040301,0);
			s1.computeResult(allTimePoint,20040301,0);
			
			System.out.println("\ncompute end:"+sdFormat.format(new Date()));
		}
		catch (Exception e)
		{
			System.out.println("analyze3\n");
			e.printStackTrace();
		}
	}
	private void analyzeB1(String filepath,String resultfilepath)
	{
		try{
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");			
			System.out.println("\ncompute start:"+sdFormat.format(new Date()));
			
			ArrayList<String[]> allTimePoint=new ArrayList<String[]>();		
			BearStrategy1 st1=new BearStrategy1();
			Workbook workbook;
			Sheet shd,shw;			
			analyzeStock s1=new analyzeStock();	
			File[] temp=new File(filepath).listFiles();
			
			for (File f:temp)
			{
				workbook=Workbook.getWorkbook(f);
				shd=workbook.getSheet(0);
				shw=workbook.getSheet(1);
				st1.analyzeStock(shw,allTimePoint,f.getName());
			}
			s1.fillInAllcondition(allTimePoint);
			s1.fillInData(allTimePoint,new File(resultfilepath+"bq-eow.xls"),20040301,0);
			s1.computeResult(allTimePoint,20040301,0);
			
			System.out.println("\ncompute end:"+sdFormat.format(new Date()));
		}
		catch (Exception e)
		{
			System.out.println("analyze4\n");
			e.printStackTrace();
		}
	}
	private void analyzeB2(String filepath,String resultfilepath)
	{
		try{
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");			
			System.out.println("\ncompute start:"+sdFormat.format(new Date()));
			
			ArrayList<String[]> allTimePoint=new ArrayList<String[]>();		
			BearStrategy2 st1=new BearStrategy2();
			Workbook workbook;
			Sheet shd,shw;			
			analyzeStock s1=new analyzeStock();	
			File[] temp=new File(filepath).listFiles();
			
			for (File f:temp)
			{
				workbook=Workbook.getWorkbook(f);
				shd=workbook.getSheet(0);
				shw=workbook.getSheet(1);
				st1.analyzeStock(shw,allTimePoint,f.getName());
			}
			s1.fillInAllcondition(allTimePoint);
			s1.fillInData(allTimePoint,new File(resultfilepath+"bm-eow.xls"),20040301,0);
			s1.computeResult(allTimePoint,20040301,0);
			
			System.out.println("\ncompute end:"+sdFormat.format(new Date()));
		}
		catch (Exception e)
		{
			System.out.println("analyze4\n");
			e.printStackTrace();
		}
	}
	private void conditionFilter(String filepath)
	{
		try{
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");			
			System.out.println("\ncompute start:"+sdFormat.format(new Date()));
			
			Workbook workbook;
			Sheet shd;		

			workbook=Workbook.getWorkbook(new File(filepath));
			shd=workbook.getSheet(0);
			
			double count=1,count7=0,sum=0,sum7=0;
			String stock=shd.getCell(0, 1).getContents();
			if	(Double.parseDouble(shd.getCell(2, 1).getContents())<7)
			{
				count7=1;					
			}
			
			for (int i=2;i<shd.getRows();i++)
			{
				String temp=shd.getCell(0, i).getContents();
				
				if(temp.equals(stock))
				{				
					count++;
					if	(Double.parseDouble(shd.getCell(2, i).getContents())<7)
						count7++;
				}
				else
				{
					stock=temp;
					//System.out.println(stock);
					//if (count7/count<0.4||count<7)
					if (count7/count<=0.33&&count>=8)
					{
						sum+=count;
						sum7+=count7;						
					}

					count=1;
					if	(Double.parseDouble(shd.getCell(2, i).getContents())<7)
						count7=1;
					else
						count7=0;										
				}
				
			}
			//if (count7/count<0.4||count<7)
				if (count7/count<=0.33&&count>=8)
			{
				sum+=count;
				sum7+=count7;
			}

			workbook.close();
			
			System.out.println("\n"+sum+" "+sum7+" "+(100*sum7/sum));
			
			System.out.println("\ncompute end:"+sdFormat.format(new Date()));
		}
		catch (Exception e)
		{
			System.out.println("conditionFilter\n");
			e.printStackTrace();
		}
	}
}
