import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

public class analyzeStrategy {
	
	static String drive="c:/";
	
	public static void main(String[] s)
	{
		try{
			String filepath=drive+"software/sdata/15foranalyze/";
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
			
			
			s1.fillInData(allTimePoint,new File(drive+"software/sdata/here99999.xls"),20040301,0);

		}
		catch (Exception e)
		{
			System.out.println("main\n");
			e.printStackTrace();
		}
	}
}
