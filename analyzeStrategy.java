
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
		try{
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");			
			System.out.println("\ncompute start:"+sdFormat.format(new Date()));
			
			String filepath=drive+"software/sdata/15temp/";

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

			s1.fillInData(allTimePoint,new File(drive+"software/sdata/t8.xls"),20040301,0);
			
			System.out.println("\ncompute end:"+sdFormat.format(new Date()));

		}
		catch (Exception e)
		{
			System.out.println("main\n");
			e.printStackTrace();
		}
	}
}
