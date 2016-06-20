import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.DateTime;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
//
public class analyzeStock {
	int monthLineRedTypeM;//0:black 1,2,3:red
	int isComputeReturnM; //0:exit 1:enter
	int mm;
	double[] baseDataM;
	double currentHighM,currentLowM,enterPointM,testHighM;
	
 int weeklyRateType,highType=0,quantityType=0,kType=0,quarterLineRedK=0,monthLineRedK=1;
 double turnQuarterLineDegree=0,turnMonthLineDegree=0,test=0;
 final int quarterKCount=14,findstock=0;
 final double divideWeeklyrate=9;
 static int oldOrNew=0,predict=1,qpredict=0; //0:old 1:new 0:no predict 1:predict
 static String drive="c:/";
 
/*public static void main(String[] s)
{		
	try{
		analyzeStock s1=new analyzeStock();	
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
					
		ArrayList<String[]> allTimePoint=new ArrayList<String[]>();
		int analyzeCondition=0;//0:
		int isPredict=1;

<<<<<<< HEAD
		String filepath=drive+"software/sdata/15test/";
=======
<<<<<<< HEAD
		String filepath=drive+"software/sdata/15test/";
=======
		String filepath=drive+"software/sdata/15foranalyze/";
>>>>>>> 03be8ecec943a218c05ab231db235056a83ce865
>>>>>>> fb6525c876535d111390ca57ded055a8e85a581a
		
		//analyzeStockData asd=new analyzeStockData();

		//asd.findstock(new File(drive+"software/sdata/low15.xls"),filepath);
		
		File[] temp=new File(filepath).listFiles();
		for (File f:temp)
		{
			s1.analyzeBullByFile(f,0,allTimePoint,isPredict,analyzeCondition,filepath);
		}
<<<<<<< HEAD
		s1.fillInData(allTimePoint,new File(drive+"software/sdata/c1.xls"),20040301,0);
=======
<<<<<<< HEAD
		s1.fillInData(allTimePoint,new File(drive+"software/sdata/c1.xls"),20040301,0);
=======
		s1.fillInData(allTimePoint,new File(drive+"software/sdata/herekkkkk.xls"),20040301,0);
>>>>>>> 03be8ecec943a218c05ab231db235056a83ce865
>>>>>>> fb6525c876535d111390ca57ded055a8e85a581a
		
		
		//s1.computeReturnByDailyExcel(filepath);
		//s1.computeReturnByWeeklyExcelByMonthline(filepath);
		//s1.computeReturnByWeeklyExcelByQuarterline(filepath);
		//s1.computeReturnByWeeklyExcelByMonthlineB(filepath); 
		//s1.computeReturnByWeeklyExcelByQuarterlineB(filepath);
		//s1.computeReturnByWeeklyExcelByMonthlineB(filepath);
		//s1.computeReturnByDailyExcelByQuarterlineB(filepath);
	}
	catch (Exception e)
	{
		System.out.println("main\n");
		e.printStackTrace();
	}
}*/
private void computeReturnByDailyExcel(String filepath)
{
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	
	System.out.println("\ncompute start:"+sdFormat.format(new Date()));
	
	ArrayList<String[]> allTimePoint=new ArrayList<String[]>();
	
	File[] temp=new File(filepath).listFiles();
	for (int i=0;i<temp.length;++i)
	{
		computeReturnQDay(temp[i],allTimePoint);
	}
	//fillInAllconditionBydaily(allTimePoint);
	fillInData(allTimePoint,new File(drive+"software/sdata/15Allquarterline.xls"),20040301,0);
	//computeResult(allTimePoint,20040301,0);
	
	System.out.println("\ncompute end:"+sdFormat.format(new Date()));
}
private void computeReturnByWeeklyExcelByMonthline(String filepath)
{
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	
	System.out.println("\ncompute start:"+sdFormat.format(new Date()));
	
	ArrayList<String[]> allTimePoint=new ArrayList<String[]>();
	
	File[] temp=new File(filepath).listFiles();
	for (File f:temp)
	{
		try{
			analyzeStockResultByQuarterLineM(Workbook.getWorkbook(f).getSheet(1),allTimePoint,f.getName());
			//computeReturnMDay(f,allTimePoint);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//fillInAllconditionBydaily(allTimePoint);
	fillInData(allTimePoint,new File(drive+"software/sdata/15Allmonthline.xls"),20040301,0);
	//computeResult(allTimePoint,20040301,0);
	
	System.out.println("\ncompute end:"+sdFormat.format(new Date()));
}
private void computeReturnByWeeklyExcelByQuarterline(String filepath)
{
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	
	System.out.println("\ncompute start:"+sdFormat.format(new Date()));
	
	ArrayList<String[]> allTimePoint=new ArrayList<String[]>();
	
	File[] temp=new File(filepath).listFiles();
	for (File f:temp)
	{
		try{
			analyzeStockResultByQuarterLine(Workbook.getWorkbook(f).getSheet(1),allTimePoint,f.getName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//fillInAllconditionBydaily(allTimePoint);
	fillInData(allTimePoint,new File(drive+"software/sdata/15Allqline.xls"),20040301,0);
	//computeResult(allTimePoint,20040301,0);
	
	System.out.println("\ncompute end:"+sdFormat.format(new Date()));
}
private void computeReturnByWeeklyExcelByQuarterlineB(String filepath)
{
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	
	System.out.println("\ncompute start:"+sdFormat.format(new Date()));
	
	ArrayList<String[]> allTimePoint=new ArrayList<String[]>();
	
	File[] temp=new File(filepath).listFiles();
	for (File f:temp)
	{
		try{
			analyzeStockResultBearByQuarterLine(Workbook.getWorkbook(f).getSheet(1),allTimePoint,f.getName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//fillInAllconditionBydaily(allTimePoint);
	fillInData(allTimePoint,new File(drive+"software/sdata/15AllqlineB.xls"),20040301,4);
	//computeResult(allTimePoint,20040301,0);
	
	System.out.println("\ncompute end:"+sdFormat.format(new Date()));
}
private void computeReturnByDailyExcelByQuarterlineB(String filepath)
{
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	
	System.out.println("\ncompute start:"+sdFormat.format(new Date()));
	
	ArrayList<String[]> allTimePoint=new ArrayList<String[]>();
	
	File[] temp=new File(filepath).listFiles();
	for (File f:temp)
	{
		try{
			analyzeStockResultBearByDailyKQuarterLine(Workbook.getWorkbook(f).getSheet(0),allTimePoint,f.getName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//fillInAllconditionBydaily(allTimePoint);
	fillInData(allTimePoint,new File(drive+"software/sdata/15AllDailyKqlineB.xls"),20040301,4);
	//computeResult(allTimePoint,20040301,0);
	
	System.out.println("\ncompute end:"+sdFormat.format(new Date()));
}
private void computeReturnByWeeklyExcelByMonthlineB(String filepath)
{
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	
	System.out.println("\ncompute start:"+sdFormat.format(new Date()));
	
	ArrayList<String[]> allTimePoint=new ArrayList<String[]>();
	
	File[] temp=new File(filepath).listFiles();
	for (File f:temp)
	{
		try{
			analyzeStockResultBearByTopMonthLine(Workbook.getWorkbook(f).getSheet(1),allTimePoint,f.getName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//fillInAllconditionBydaily(allTimePoint);
	fillInData(allTimePoint,new File(drive+"software/sdata/15AllqlineB.xls"),20040301,4);
	//computeResult(allTimePoint,20040301,0);
	
	System.out.println("\ncompute end:"+sdFormat.format(new Date()));
}
private void mergeDirData(File sourcedir,File destination)
{
	File[] temp=sourcedir.listFiles();
	
	ArrayList<String[]> as=new ArrayList<String[]>();//merge all stock file
	try{
	WritableWorkbook writeBook=Workbook.createWorkbook(destination);
	for (int i=0;i<temp.length;i++)
	{
		as=mergeData(temp[i]);
		fillDate(writeBook,as,temp[i],i);
	}
	writeBook.write();
	writeBook.close();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}
private ArrayList<String[]> mergeData(File f)
{
	ArrayList<String[]> content=new ArrayList<String[]>();
	String[] contemp;
	
	try {
		
		Workbook workbook = Workbook.getWorkbook(f);
		Sheet s;
		
		for (int i=0;i<workbook.getNumberOfSheets();i++)
		{			
			s=workbook.getSheet(i);
			
			int temp;
			
			if(i==0)
				temp=0;
			else
				temp=1;
			
			while (temp<s.getRows())
			{
				contemp=new String[12];
				
				for (int j=0;j<12;++j)
				{
					contemp[j]=s.getCell(j,temp).getContents();
				}

				temp++;
				content.add(contemp);
			}
		}
		
	}
	catch (Exception e)
	{
		System.out.print("\nanalyzeStockResultByQuarterLine"+","+f.getName());
		e.printStackTrace();
	}
	return content;
}
public void fillDate(WritableWorkbook writeBook,ArrayList<String[]> content,File ff,int sheetLocation)
{
	try{					
		WritableSheet sss=writeBook.createSheet(ff.getName(),sheetLocation);

		for (int j=0;j<12;j++)
		{
			sss.addCell(new Label(j,0,content.get(0)[j]));
		}

		for (int i=1;i<content.size();i++)	
		{
			for (int j=0;j<12;j++)
			{
				if (j==0)
					sss.addCell(new Label(j,i,content.get(i)[j]));
				else
				{	
					if (!content.get(i)[j].equals(""))
						sss.addCell(new Number(j,i,Double.parseDouble(content.get(i)[j])));
					//sss.addCell(new Label(j,i,content.get(i)[j]));
					else
						sss.addCell(new Label(j,i,content.get(i)[j]));
				}
			}
		}				
	}
	catch(Exception e)
	{
		System.out.print("\nfillInData");
		e.printStackTrace();
	}
}
public void analyzeBullByFile(File f,int filetype,ArrayList<String[]> allTimePoint,int isPredict,int analyzeCondition,String filepath)
{	
	predict=isPredict;
	oldOrNew=filetype;
	
	Workbook workbook;
	Sheet shd,shw;
	
	try{	
		workbook=Workbook.getWorkbook(f);
		shd=workbook.getSheet(0);
		shw=workbook.getSheet(1);
	}
	catch (Exception e)
	{
		System.out.print("\n analyze:"+" "+f.getName());
		e.printStackTrace();
		return;
	}
	
	if(analyzeCondition==0)
	{
		analyzeStockResultByQuarterLinePredict(shw,shd,allTimePoint,filepath,f.getName());
	}
	else
	{
		switch(analyzeCondition)
		{
		case 1:
			analyzeStockResultByQuarterLine(shw,allTimePoint,f.getName());
			break;
		case 2:
			analyzeStockResultByQuarterLineM(shw,allTimePoint,shw.getName());
			break;
		case 3:
			analyzeStockResultByMonthLine(shw,allTimePoint);
			break;
		default:
			System.out.println("no this condition");
		}
	}
}
public ArrayList<String[]> analyzeBull(Workbook workbook,int filetype,ArrayList<String[]> allTimePoint,int isPredict,int analyzeCondition,String filepath)
{	
	predict=isPredict;
	oldOrNew=filetype;
	Sheet sh=null;
	if(analyzeCondition==0)
	{
		File[] temp=new File(drive+"software/sdata/new/").listFiles();
		for (int i=0;i<workbook.getNumberOfSheets();i++)
		{
			sh=workbook.getSheet(i);
			for(int l=0;l<temp.length;l++)
			{
				if(sh.getName().equals(temp[l].getName()))
				{
					try{				
						analyzeStockResultByQuarterLinePredict(sh,Workbook.getWorkbook(new File(drive+"software/sdata/new/"+temp[l].getName())).getSheet(0),allTimePoint,filepath,sh.getName());
						break;
					}
					catch (Exception e)
					{
						System.out.print("\n analyze:"+i+" "+sh.getName());
						e.printStackTrace();
					}
				}
			}
		}
	}
	else
	{
		for (int i=0;i<workbook.getNumberOfSheets();i++)
		{
			switch(analyzeCondition)
			{
				case 1:
					analyzeStockResultByQuarterLine(workbook.getSheet(i),allTimePoint,sh.getName());
					break;
				case 2:
					analyzeStockResultByQuarterLineM(workbook.getSheet(i),allTimePoint,workbook.getSheet(i).getName());
					break;
				case 3:
					analyzeStockResultByMonthLine(workbook.getSheet(i),allTimePoint);
					break;
				default:
					System.out.println("no this condition");
			}
		}
	}
	return allTimePoint;
}
public ArrayList<String[]> analyzeBear(Workbook workbook,int filetype,ArrayList<String[]> allTimePoint,int isPredict,int analyzeCondition)
{	
	predict=isPredict;
	oldOrNew=filetype;
	
	for (int i=0;i<workbook.getNumberOfSheets();i++)
	{
		switch(analyzeCondition)
		{
		case 4:
			analyzeStockResultBearByQuarterLine(workbook.getSheet(i),allTimePoint,workbook.getSheet(i).getName());
			break;
		case 5:
			analyzeStockResultBearByMonthLine(workbook.getSheet(i),allTimePoint,workbook.getSheet(i).getName());
			break;
		default:
			System.out.println("no this condition");
		}
	}
		
	return allTimePoint;
}
public void analyzeStockResultByQuarterLine(Sheet s,ArrayList<String[]> allTimePoint,String name)
{	
	int isComputeReturn=0; //0:exit 1:enter
	double[] baseData=new double[7],contemp=new double[7];
	double currentHigh=0,currentLow=0,enterPoint=0;
	ArrayList<double[]> content=new ArrayList<double[]>();
	DecimalFormat df=new DecimalFormat("#.##");
	String buytime="";
	double weeklyrate,lead,quantity,kRate;
	
	monthLineRedK=1;
	int quarterLineRedK=0;	

	try {			
			int temp=1;
			while (temp<s.getRows())
			{
				contemp=new double[7];

				if (oldOrNew==0)
				{
					for (int j=0;j<7;++j)
					{
						String st=s.getCell(j+1,temp).getContents();
						if (!st.equals(""))
							contemp[j]=Double.parseDouble(st);
						else
							contemp[j]=0;
					}
				}
				else
				{
					for (int j=0;j<6;++j)
					{
						String st=s.getCell(j+1,temp).getContents();
						if (!st.equals(""))
							contemp[j]=Double.parseDouble(st);
						else
							contemp[j]=0;
					}
					contemp[6]=Double.parseDouble(s.getCell(11,temp).getContents());
				}
							
				if (content.size()>=quarterKCount-1)
				{
					if (isComputeReturn==0)
					{
						if (quarterLineRedK==0)
						{		
							if (content.get(content.size()-1)[5]<=content.get(content.size()-2)[5]&&contemp[5]>=content.get(content.size()-1)[5])
							{
								quarterLineRedK=1;
							}
						}
						if (quarterLineRedK>=1&&quarterLineRedK<=8)
						{
							if (contemp[5]>=content.get(content.size()-1)[5])
							{							
								if (conditionAnalyzeQ(content,contemp,contemp[3]))
								{
									isComputeReturn=1;
									baseData=contemp;										
									enterPoint=contemp[3];									
									currentHigh=contemp[3];
									currentLow=contemp[3];
									buytime=s.getCell(0,temp).getContents();
									weeklyrate=(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									lead=(contemp[1]-contemp[3])/contemp[3]*100;
									quantity=contemp[6];
									
									fillStockData(allTimePoint,name,buytime,quantity+"",df.format(weeklyrate),df.format(lead));										
								}
								else
								{
									quarterLineRedK++;
								}
							}
							else
							{
								quarterLineRedK=0;
							}
						}
						else if(quarterLineRedK>=9) 
						{
							if (contemp[5]>content.get(content.size()-1)[5])
								quarterLineRedK++;
							else
							{
								quarterLineRedK=0;
							}
						}															
					}
					else
					{	
						if (contemp[0]>=contemp[3])
						{
							if (contemp[1]>currentHigh)
							{
								currentHigh=contemp[1];								
							}
							if (contemp[2]<currentLow)
							{
								if (100*(currentHigh-enterPoint)/enterPoint<7)
									currentLow=contemp[2];
							}
						}
						else
						{
							if (contemp[2]<currentLow)
							{
								if (100*(currentHigh-enterPoint)/enterPoint<7)
									currentLow=contemp[2];
							}								
							if (contemp[1]>currentHigh)
							{
								if ((enterPoint-currentLow)/enterPoint<0.13)
									currentHigh=contemp[1];								
							}							
						}
						
						/*if(firstComputek==1)
						{
							firstComputeK(baseData,contemp);
							firstComputek=0;
						}*/
						
						if (endComputeReturnQ(currentHigh,baseData,contemp,content))
						{
							if (enterPoint!=9999)
							{										
								fillInData(allTimePoint,2,""+df.format(100*(currentHigh-enterPoint)/enterPoint));
								fillInData(allTimePoint,9,""+df.format(100*(enterPoint-currentLow)/enterPoint));
							}
							
							isComputeReturn=0;
							quarterLineRedK=0;
						}
					}
				}				
				temp++;
				content.add(contemp);			
			}	
	}
	catch (Exception e)
	{
		System.out.print("\nanalyzeStockResultByQuarterLine"+","+s.getName());
		e.printStackTrace();
	}
}
public void analyzeStockResultByQuarterLinePredict(Sheet s,Sheet shfile,ArrayList<String[]> allTimePoint,String filepath,String stockname)
{	
	int isComputeReturn=0; //0:exit 1:enter
	double[] baseData=new double[7],contemp=new double[7];
	double currentHigh=0,testHigh=0,testLow=0,currentLow=0,enterPoint=0;
	ArrayList<double[]> content=new ArrayList<double[]>();
	DecimalFormat df=new DecimalFormat("#.##");
	String buytime="";
	double weeklyrate,lead,quantity,testWeeklyrate=0,highWeeklyrate=0;
	int day=1;
	ArrayList<double[]> tt=new ArrayList<double[]>();
	String[] buyday=new String[7];
	double[] returnV=new double[2];
	String[] tempdata=null;
	ArrayList<String> as=new ArrayList<String>(),buytimeAll=new ArrayList<String>();
	ArrayList<double[]> mdata=new ArrayList<double[]>();
	
	int quarterLineRedK=0;	

	try {			
			int temp=1;
			while (temp<s.getRows())
			{
				contemp=new double[7];

				if (oldOrNew==0)
				{
					for (int j=0;j<7;++j)
					{
						String st=s.getCell(j+1,temp).getContents();
						if (!st.equals(""))
							contemp[j]=Double.parseDouble(st);
						else
							contemp[j]=0;
					}
				}
				else
				{
					for (int j=0;j<6;++j)
					{
						String st=s.getCell(j+1,temp).getContents();
						if (!st.equals(""))
							contemp[j]=Double.parseDouble(st);
						else
							contemp[j]=0;
					}
					contemp[6]=Double.parseDouble(s.getCell(11,temp).getContents());
				}
				
				
				if (content.size()>=quarterKCount-1)
				{
					if (isComputeReturn==0)
					{
						if (quarterLineRedK==0)
						{	
							if (content.get(content.size()-1)[5]<=content.get(content.size()-2)[5])
							if (contemp[5]>=content.get(content.size()-1)[5])
							{
								quarterLineRedK=1;
							}
							else
							{
								if ((contemp[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]>=0.09&&(contemp[1]-contemp[3])/13+contemp[5]>=content.get(content.size()-1)[5])
								{
									day=0;
									tt=new ArrayList<double[]>();
									computeDailyK(shfile,s.getName(),s.getCell(0,temp).getContents(),tt,contemp,buyday);
									if(tt.size()!=0)
									do{	//System.out.println(s.getCell(0,temp).getContents()+" "+day+" "+tt.get(day)[3]);									
										if (conditionAnalyzeQ(content,tt.get(day),tt.get(day)[3]))
										{								
											isComputeReturn=1;
											baseData=tt.get(day);									
											enterPoint=tt.get(day)[3];									
											currentHigh=tt.get(day)[3];
											currentLow=tt.get(day)[3];
											buytime=s.getCell(0,temp).getContents();
											weeklyrate=(tt.get(day)[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
											lead=(tt.get(day)[1]-tt.get(day)[3])/tt.get(day)[3]*100;
											quantity=tt.get(day)[6];

											/*if (predict==1)
											{
												highWeeklyrate=(tt.get(day)[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;

												if(weeklyrate>=6&&highWeeklyrate>=divideWeeklyrate)
												{													
													enterPoint=computeEnterPoint(content,tt.get(day));
													testWeeklyrate=(enterPoint-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
													currentLow=enterPoint;
												}
											}*/
											
											as.add(buyday[day]);
											buytimeAll.add(buytime);
											double[] dd=new double[7];
											dd[0]=tt.get(day)[0];
											dd[1]=tt.get(day)[1];
											dd[2]=tt.get(day)[2];
											dd[3]=tt.get(day)[3];
											dd[4]=tt.get(day)[6];
											dd[5]=enterPoint;
											dd[6]=weeklyrate;
											mdata.add(dd);
											
											//fillStockData(allTimePoint,s.getName(),buytime,quantity+"",df.format(weeklyrate),df.format(lead));	

											tempdata=new String[]{stockname,buytime,"",""+test,""+quarterLineRedK,""+highType,quantity+"",df.format(weeklyrate),df.format(lead),"","","","","","","","",""+kType,"1"};
											
											returnV[0]=currentHigh;
											returnV[1]=currentLow;
											//System.out.println(buytime+" "+buyday[day]+" "+day+" "+tt.size());	
											if(day+1<tt.size())
											{	
												if (endComputeReturnQDay(shfile,s.getName(),s.getCell(0,temp).getContents(),baseData,enterPoint,returnV,day+1))
												{
													/*fillInData(allTimePoint,2,""+df.format(100*(returnV[0]-enterPoint)/enterPoint));
													fillInData(allTimePoint,9,""+df.format(100*(enterPoint-returnV[1])/enterPoint));
													fillInData(allTimePoint,12,""+df.format(highWeeklyrate));
													fillInData(allTimePoint,13,""+df.format(testWeeklyrate));*/
													
													tempdata[2]=""+df.format(100*(returnV[0]-enterPoint)/enterPoint);
													tempdata[9]=""+df.format(100*(enterPoint-returnV[1])/enterPoint);
													tempdata[12]=""+df.format(highWeeklyrate);
													tempdata[13]=""+df.format(testWeeklyrate);
													
													allTimePoint.add(tempdata);
													
													isComputeReturn=0;
													quarterLineRedK=0;
													monthLineRedK=1;
												}
											}
											
											break;
										}
										day++;
									}
									while(day<tt.size());
								}
							}
						}

						if (quarterLineRedK>=1&&quarterLineRedK<=8)
						{
							if (contemp[5]>=content.get(content.size()-1)[5])
							{		
								if ((contemp[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]>=0.09)
								{
									day=0;
									tt=new ArrayList<double[]>();
									computeDailyK(shfile,s.getName(),s.getCell(0,temp).getContents(),tt,contemp,buyday);
									if(tt.size()!=0)
									do{										
										if (conditionAnalyzeQ(content,tt.get(day),tt.get(day)[3]))
										{
											isComputeReturn=1;
											baseData=tt.get(day);									
											enterPoint=tt.get(day)[3];									
											currentHigh=tt.get(day)[3];
											currentLow=tt.get(day)[3];
											buytime=s.getCell(0,temp).getContents();
											weeklyrate=(tt.get(day)[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
											lead=(tt.get(day)[1]-tt.get(day)[3])/tt.get(day)[3]*100;
											quantity=tt.get(day)[6];

										/*	if (predict==1)
											{
												highWeeklyrate=(tt.get(day)[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;

												if(weeklyrate>=6&&highWeeklyrate>=divideWeeklyrate)
												{													
													enterPoint=computeEnterPoint(content,tt.get(day));
													//baseData[3]=enterPoint;
													testWeeklyrate=(enterPoint-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
													currentLow=enterPoint;
												}
											}*/
											
											//System.out.println(buytime+":"+buyday[day]);
											as.add(buyday[day]);
											buytimeAll.add(buytime);
											double[] dd=new double[7];
											dd[0]=tt.get(day)[0];
											dd[1]=tt.get(day)[1];
											dd[2]=tt.get(day)[2];
											dd[3]=tt.get(day)[3];
											dd[4]=tt.get(day)[6];
											dd[5]=enterPoint;
											dd[6]=weeklyrate;
											mdata.add(dd);											
											
											//fillStockData(allTimePoint,s.getName(),buytime,quantity+"",df.format(weeklyrate),df.format(lead));	

											tempdata=new String[]{stockname,buytime,"",""+test,""+quarterLineRedK,""+highType,quantity+"",df.format(weeklyrate),df.format(lead),"","","","","","","","",""+kType,"1"};
											
											returnV[0]=currentHigh;
											returnV[1]=currentLow;
											//System.out.println(buytime+" "+buyday[day]+" "+day+" "+tt.size());	
											if(day+1<tt.size())
											{	//System.out.println("end1:"+buytime+":"+s.getCell(0,temp).getContents()+" "+returnV[0]+" "+enterPoint+" "+day+" "+tt.size());
												if (endComputeReturnQDay(shfile,s.getName(),s.getCell(0,temp).getContents(),baseData,enterPoint,returnV,day+1))
												{
													/*fillInData(allTimePoint,2,""+df.format(100*(returnV[0]-enterPoint)/enterPoint));
													fillInData(allTimePoint,9,""+df.format(100*(enterPoint-returnV[1])/enterPoint));
													fillInData(allTimePoint,12,""+df.format(highWeeklyrate));
													fillInData(allTimePoint,13,""+df.format(testWeeklyrate));*/
													
													tempdata[2]=""+df.format(100*(returnV[0]-enterPoint)/enterPoint);
													tempdata[9]=""+df.format(100*(enterPoint-returnV[1])/enterPoint);
													tempdata[12]=""+df.format(highWeeklyrate);
													tempdata[13]=""+df.format(testWeeklyrate);
													
													allTimePoint.add(tempdata);
													
													isComputeReturn=0;
													quarterLineRedK=0;
													
												}
											}
											
											break;
										}
										day++;
									}
									while(day<tt.size());
								}
									quarterLineRedK++;
							}
							else
							{
								quarterLineRedK=0;
								
							}
						}
						else if(quarterLineRedK>=9) 
						{
							if (contemp[5]>content.get(content.size()-1)[5])
								quarterLineRedK++;
							else
							{
								quarterLineRedK=0;
								
							}
						}															
					}
					else
					{		//System.out.println(s.getName()+":"+s.getCell(0,temp).getContents());															
						if (endComputeReturnQDay(shfile,s.getName(),s.getCell(0,temp).getContents(),baseData,enterPoint,returnV,0))
						{		//System.out.println("end2:"+buytime+":"+s.getCell(0,temp).getContents()+" "+returnV[0]+" "+enterPoint);

							//System.out.println("E2:"+s.getCell(0,temp).getContents()+" "+returnV[0]+":"+baseData[3]+":"+enterPoint);
							/*fillInData(allTimePoint,2,""+df.format(100*(returnV[0]-enterPoint)/enterPoint));
							fillInData(allTimePoint,9,""+df.format(100*(enterPoint-returnV[1])/enterPoint));
							fillInData(allTimePoint,12,""+df.format(highWeeklyrate));
							fillInData(allTimePoint,13,""+df.format(testWeeklyrate));*/
							
							tempdata[2]=""+df.format(100*(returnV[0]-enterPoint)/enterPoint);
							tempdata[9]=""+df.format(100*(enterPoint-returnV[1])/enterPoint);
							tempdata[12]=""+df.format(highWeeklyrate);
							tempdata[13]=""+df.format(testWeeklyrate);
							
							allTimePoint.add(tempdata);
							
							isComputeReturn=0;
							quarterLineRedK=0;
							
						}
					}
				}
				
				temp++;
				content.add(contemp);			
			}
			//locateStockDailyDate(filepath,shfile.getName(),as,mdata);
			//locateStockWeeklyDate(filepath,shfile.getName(),buytimeAll);
	}
	catch (Exception e)
	{
		System.out.print("\nanalyzeStockResultByQuarterLinePredict"+","+s.getName());
		e.printStackTrace();
	}
}
public void analyzeStockResultByQuarterLineM(Sheet s,ArrayList<String[]> allTimePoint,String name)
{
	
	int isComputeReturn=0; //0:exit 1:enter
	double[] previousData=new double[7],baseData=new double[7],contemp=new double[7];
	double currentHigh=0,twoHigh=0,threeHigh=0,fourHigh=0,currentLow=0,returnPoint=0,enterPoint=0;
	ArrayList<double[]> content=new ArrayList<double[]>();
	DecimalFormat df=new DecimalFormat("#.##");
	String buytime="";
	double weeklyrate,lead,quantity,kRate;
	
	monthLineRedK=1;
	quarterLineRedK=0;
		
	monthLineRedTypeM=0;//0:black 1,2,3:red
	isComputeReturnM=0; //0:exit 1:enter
	mm=0;
	baseDataM=new double[7];
	currentHighM=0;
	currentLowM=0;
	enterPointM=0;
		
	try {
			int temp=1;
			while (temp<s.getRows())
			{
				contemp=new double[7];
				
				if (oldOrNew==0)
				{
					try {
					for (int j=0;j<7;++j)
					{						
						contemp[j]=Double.parseDouble(s.getCell(j+1,temp).getContents());
					}					
					}
					catch(Exception e)
					{
						//e.printStackTrace();
						temp++;
						continue ;
					}
				}
				else
				{
					for (int j=0;j<6;++j)
					{
						contemp[j]=Double.parseDouble(s.getCell(j+1,temp).getContents());
					}
					contemp[6]=Double.parseDouble(s.getCell(11,temp).getContents());
				}
								
				if (content.size()>=quarterKCount-1)
				{
					if (isComputeReturn==0)
					{
						if (quarterLineRedK==0)
						{
							if (content.get(content.size()-1)[5]<=content.get(content.size()-2)[5]&&contemp[5]>=content.get(content.size()-1)[5])
							{
								quarterLineRedK=1;
							}
						}

						if (quarterLineRedK>=1&&quarterLineRedK<=8)
						{
							if (contemp[5]>=content.get(content.size()-1)[5])
							{
								if (conditionAnalyzeQ(content,contemp,contemp[3]))
								{
									isComputeReturn=1;
									baseData=contemp;
									previousData=content.get(content.size()-1);										
									enterPoint=contemp[3];									
									currentHigh=contemp[3];
									currentLow=contemp[3];
									buytime=s.getCell(0,temp).getContents();
									weeklyrate=(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									lead=(contemp[1]-contemp[3])/contemp[3]*100;
									quantity=contemp[6];
									kRate=(contemp[3]-contemp[0])/contemp[0]*100;

									mm=1;									
								}
								else
									quarterLineRedK++;
							}
							else
							{
								quarterLineRedK=0;
								monthLineRedK=1;
							}
						}
						else if(quarterLineRedK>=9) 
						{
							if (contemp[5]>content.get(content.size()-1)[5])
								quarterLineRedK++;
							else
							{
								quarterLineRedK=0;
								monthLineRedK=1;
							}
						}															
					}
					else
					{								
						if (contemp[1]>currentHigh)
						{
							currentHigh=contemp[1];								
						}

						if (endComputeReturnQ(currentHigh,baseData,contemp,content))
						{							
							isComputeReturn=0;
							quarterLineRedK=0;
							monthLineRedK=1;

							mm=0;
						}
					}
					
					buytime=s.getCell(0,temp).getContents();
					analyzeStockResultByQuarterLineMM(s,allTimePoint,buytime,contemp,content,name);
				}
				
				temp++;
				content.add(contemp);			
			}
	}
	catch (Exception e)
	{
		System.out.print("\nanalyzeStockResultByQuarterLine"+","+s.getName());
		e.printStackTrace();
	}
}
public void analyzeStockResultByQuarterLineMM(Sheet s,ArrayList<String[]> allTimePoint,String buytime,double[] contemp,ArrayList<double[]> content,String name)
{	
	DecimalFormat df=new DecimalFormat("#.##");
	
	try {		
				if (content.size()>=2)
				{
					if (isComputeReturnM==0&&mm==1)
					{
						testHighM=0;
						
							if (monthLineRedTypeM==0)
							{
								if (contemp[4]>content.get(content.size()-1)[4]&&content.get(content.size()-2)[4]>content.get(content.size()-1)[4])
								{																	
									monthLineRedTypeM=1;
								}
							}
							if (monthLineRedTypeM==1)
							{
								if(conditionAnalyzeMM(content,contemp))							
								{										
										isComputeReturnM=1;
										baseDataM=contemp;
										enterPointM=contemp[3];
										currentHighM=contemp[3];
										currentLowM=contemp[3];
										double weeklyrate=(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
										double kRate=(contemp[3]-contemp[2])/contemp[3]*100;
										double lead=(contemp[1]-contemp[3])/contemp[3]*100;
										double quantity=contemp[6];
										fillStockData(allTimePoint,name,buytime,""+quantity,""+df.format(weeklyrate),""+df.format(lead));	
										fillInData(allTimePoint,10,""+kRate);
										
										if (predict==1)
										{
											if(enterPointM>content.get(content.size()-4)[3]*1.03)
												enterPointM=content.get(content.size()-4)[3]*1.03;
											
											testHighM=contemp[1];
										}
										
										monthLineRedTypeM=0;
								}
								else
									monthLineRedTypeM=0;
							}														
					}
					else if (isComputeReturnM==1)
					{							
						if (contemp[0]>=contemp[3])
						{
							if (contemp[1]>currentHighM)
							{
								currentHighM=contemp[1];								
							}
							if (contemp[2]<currentLowM)
							{
								if ((currentHighM-baseDataM[3])/baseDataM[3]<0.07)
								{
									if ((currentHighM-enterPointM)/enterPointM<0.07&&(testHighM-enterPointM)/enterPointM<0.07)
										currentLowM=contemp[2];
									
								}
							}
						}
						else
						{
							if (contemp[2]<currentLowM)
							{
								if ((currentHighM-baseDataM[3])/baseDataM[3]<0.07)
								{
									if ((currentHighM-enterPointM)/enterPointM<0.07&&(testHighM-enterPointM)/enterPointM<0.07)
										currentLowM=contemp[2];
									
								}
							}								
							if (contemp[1]>currentHighM)
							{
								if ((enterPointM-currentLowM)/enterPointM<0.12)
									currentHighM=contemp[1];								
							}
							
						}
						
						if (endComputeReturnM(currentHighM,baseDataM,contemp,content,enterPointM))
						{
							if(testHighM>currentHighM)
								currentHighM=testHighM;
							
							fillInData(allTimePoint,2,""+df.format(100*(currentHighM-enterPointM)/enterPointM));						
							fillInData(allTimePoint,9,""+df.format(100*(baseDataM[3]-currentLowM)/baseDataM[3]));
							
							/*if((testHighM-enterPointM)/enterPointM>0.07)
							{
								//System.out.print("\nhere"+testHighM+" "+enterPointM+" "+(testHighM-enterPointM)/enterPointM);
								fillInData(allTimePoint,11,"1");
							}*/
							
							isComputeReturnM=0;
							monthLineRedTypeM=0;
						}
					}
				}							
	}
	catch (Exception e)
	{
		System.out.println("analyzeStockResultByQuarterLineMM");
		e.printStackTrace();
	}
}
public void analyzeStockResultByMonthLine(Sheet s,ArrayList<String[]> allTimePoint)
{	
	int isComputeReturn=0; //0:exit 1:enter
	double[] baseData=new double[7],contemp=new double[7];
	double currentHigh=0,currentLow=0,enterPoint=0,testHigh=0;
	ArrayList<double[]> content=new ArrayList<double[]>();
	DecimalFormat df=new DecimalFormat("#.##");
	
	try {					
		int temp=1;
		while (temp<s.getRows())
		{
			contemp=new double[7];
			
			if (oldOrNew==0)
			{
				for (int j=0;j<7;++j)
				{
					contemp[j]=Double.parseDouble(s.getCell(j+1,temp).getContents());
				}
			}
			else
			{
				for (int j=0;j<6;++j)
				{
					contemp[j]=Double.parseDouble(s.getCell(j+1,temp).getContents());
				}
				contemp[6]=Double.parseDouble(s.getCell(11,temp).getContents());
			}

			if (content.size()>=4)
			{
				if (isComputeReturn==0)
				{
					if (content.get(content.size()-2)[5]>=content.get(content.size()-3)[5]&&content.get(content.size()-1)[5]>=content.get(content.size()-2)[5]&&contemp[5]>=content.get(content.size()-1)[5])
					{
						if(conditionAnalyzeM(content,contemp))							
						{						
							String buytime=s.getCell(0,temp).getContents();
							isComputeReturn=1;
							baseData=contemp;
							enterPoint=contemp[3];
							currentHigh=contemp[3];
							currentLow=contemp[3];
							double weeklyrate=(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
							double lead=(contemp[1]-contemp[3])/contemp[3]*100;
							double quantity=contemp[6];
							fillStockData(allTimePoint,s.getName(),buytime,""+quantity,""+df.format(weeklyrate),""+df.format(lead));	

							if (predict==1)
							{
								testHigh=0;
								if(weeklyrate>=6&&(contemp[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100>=divideWeeklyrate)
								{
									if(enterPoint>content.get(content.size()-4)[3]*1.03)
										enterPoint=content.get(content.size()-4)[3]*1.03;
									
									testHigh=contemp[1];
								}
							}
							fillInData(allTimePoint,12,""+df.format((contemp[3]-contemp[4])));
						}
					}														
				}
				else 
				{						
					if (contemp[0]>=contemp[3])
					{
						if (contemp[1]>currentHigh)
						{
							currentHigh=contemp[1];								
						}
						if (contemp[2]<currentLow)
						{
							if (100*(currentHigh-baseData[3])/baseData[3]<7)
							{
								if (100*(currentHigh-enterPoint)/enterPoint<7&&100*(testHigh-enterPoint)/enterPoint<7)
									currentLow=contemp[2];								
							}
						}
					}
					else
					{
						if (contemp[2]<currentLow)
						{
							if (100*(currentHigh-baseData[3])/baseData[3]<7)
							{
								if (100*(currentHigh-enterPoint)/enterPoint<7&&100*(testHigh-enterPoint)/enterPoint<7)
									currentLow=contemp[2];								
							}
						}								
						if (contemp[1]>currentHigh)
						{
							if ((enterPoint-currentLow)/enterPoint<0.12)
								currentHigh=contemp[1];								
						}
						
					}
														
					if (endComputeReturnM(currentHigh,baseData,contemp,content,enterPoint))
					{
						if(testHigh>currentHigh)
							currentHigh=testHigh;
						
						fillInData(allTimePoint,2,""+df.format(100*(currentHigh-enterPoint)/enterPoint));
						fillInData(allTimePoint,9,""+df.format(100*(baseData[3]-currentLow)/baseData[3]));

						isComputeReturn=0;

					}
				}
			}							
			content.add(contemp);
			temp++;
		}
	}
	catch (Exception e)
	{
		System.out.print("\nanalyzeStockResultByMonthLine");
		e.printStackTrace();
	}
}
public void analyzeStockResultBearByQuarterLine(Sheet s,ArrayList<String[]> allTimePoint,String name)
{
	
	int isComputeReturn=0,isMonthLineDown=0; //0:exit 1:enter
	double[] previousData=new double[7],baseData=new double[7],contemp=new double[7];
	double currentHigh=0,currentLow=0,testLow=0,returnPoint=0,enterPoint=0;
	ArrayList<double[]> content=new ArrayList<double[]>();
	DecimalFormat df=new DecimalFormat("#.##");
	String buytime="";
	double weeklyrate,lead,quantity,kRate,testWeeklyrate=0,highWeeklyrate=0;
	
	monthLineRedK=1;
	quarterLineRedK=0;	

	try {			
			int temp=1;
			while (temp<s.getRows())
			{
				contemp=new double[7];

				if (oldOrNew==0)
				{
					for (int j=0;j<7;++j)
					{
						String st=s.getCell(j+1,temp).getContents();
						if (!st.equals(""))
							contemp[j]=Double.parseDouble(st);
						else
							contemp[j]=0;
					}
				}
				else
				{
					for (int j=0;j<6;++j)
					{
						contemp[j]=Double.parseDouble(s.getCell(j+1,temp).getContents());
					}
					contemp[6]=Double.parseDouble(s.getCell(11,temp).getContents());
				}
				
				
				if (content.size()>=quarterKCount-1)
				{
					if (isComputeReturn==0)
					{
						if (quarterLineRedK==0)
						{
							if (content.get(content.size()-1)[5]>=content.get(content.size()-2)[5]&&contemp[5]<content.get(content.size()-1)[5])
							{
								quarterLineRedK=1;
							}
						}

						if (quarterLineRedK>=3&&quarterLineRedK<=5)
						{
							if (contemp[5]<content.get(content.size()-1)[5])
							{//System.out.print("\n"+s.getCell(0,temp).getContents());
								if (conditionAnalyzeQB(content,contemp,contemp[3]))
								{
									isComputeReturn=1;
									baseData=contemp;
									previousData=content.get(content.size()-1);										
									enterPoint=contemp[3];									
									currentHigh=contemp[3];
									currentLow=contemp[3];
									buytime=s.getCell(0,temp).getContents();
									weeklyrate=-(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									lead=-(contemp[2]-contemp[3])/contemp[3]*100;
									quantity=contemp[6];
									
									/*highWeeklyrate=(contemp[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									testHigh=0;
									if(weeklyrate>=6&&(contemp[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100>=divideWeeklyrate)
									{
										//System.out.print("\n"+buytime);
										enterPoint=computeEnterPoint(content,contemp,weeklyrate);
										//currentHigh=contemp[1];
										currentLow=enterPoint;
										testHigh=contemp[1];
										testLow=enterPoint;
										testWeeklyrate=(enterPoint-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									}*/

									fillStockData(allTimePoint,name,buytime,quantity+"",df.format(weeklyrate),df.format(lead));	
									
								}
								else
									quarterLineRedK++;
							}
							else
							{
								quarterLineRedK=0;
								monthLineRedK=1;
							}
						}
						else if(quarterLineRedK>=6||quarterLineRedK==1||quarterLineRedK==2) 
						{
							if (contemp[5]<content.get(content.size()-1)[5])
								quarterLineRedK++;
							else
							{
								quarterLineRedK=0;
								monthLineRedK=1;
							}
						}															
					}
					else
					{	
						if (contemp[0]>=contemp[3])
						{
							if (contemp[1]>currentHigh)
							{		
								if ((currentLow-baseData[3])/baseData[3]>-0.07)
								{
									if ((currentLow-enterPoint)/enterPoint>-0.07)
										currentHigh=contemp[1];									
								}
							}
							if (contemp[2]<currentLow&&isMonthLineDown!=1)
							{
								if ((currentHigh-enterPoint)/enterPoint<0.13)
									currentLow=contemp[2];
							}							
						}
						else
						{
							if (contemp[2]<currentLow&&isMonthLineDown!=1)
							{
								currentLow=contemp[2];
							}
							if (contemp[1]>currentHigh)
							{
								if ((currentLow-baseData[3])/baseData[3]>-0.07)
								{
									if ((currentLow-enterPoint)/enterPoint>-0.07)
										currentHigh=contemp[1];	
									
								}														
							}																										
						}
											
						if ((currentLow-baseData[3])/baseData[3]<=-0.1)
						{
							if (contemp[4]>content.get(content.size()-1)[4])
							{
								isMonthLineDown=1;
							}
						}
						
						if (endComputeReturnQB(currentLow,previousData,baseData,contemp,content))
						{									
							fillInData(allTimePoint,2,""+df.format(100*(enterPoint-currentLow)/enterPoint));
							fillInData(allTimePoint,9,""+df.format(100*(currentHigh-enterPoint)/enterPoint));
							
							isComputeReturn=0;
							isMonthLineDown=0;
							quarterLineRedK=0;
							monthLineRedK=1;
						}
					}
				}
				
				temp++;
				content.add(contemp);			
			}
		
	}
	catch (Exception e)
	{
		System.out.print("\nanalyzeStockResultByQuarterLine"+","+s.getName());
		e.printStackTrace();
	}
}
public void analyzeStockResultBearByDailyKQuarterLine(Sheet s,ArrayList<String[]> allTimePoint,String name)
{
	
	int isComputeReturn=0,isMonthLineDown=0; //0:exit 1:enter
	double[] previousData=new double[7],baseData=new double[7],contemp=new double[7];
	double currentHigh=0,currentLow=0,testLow=0,returnPoint=0,enterPoint=0;
	ArrayList<double[]> content=new ArrayList<double[]>();
	DecimalFormat df=new DecimalFormat("#.##");
	String buytime="";
	double weeklyrate,lead,quantity,kRate,testWeeklyrate=0,highWeeklyrate=0;
	
	monthLineRedK=1;
	quarterLineRedK=0;	

	try {			
			int temp=1;
			while (temp<s.getRows())
			{
				contemp=new double[7];

				for (int j=0;j<4;++j)
				{
					String st=s.getCell(j+1,temp).getContents();
					if (!st.equals(""))
						contemp[j]=Double.parseDouble(st);
					else
						contemp[j]=0;
					
					if(j!=3)
					{
						st=s.getCell(j+7,temp).getContents();
						if (!st.equals(""))
							contemp[j+4]=Double.parseDouble(st);
						else
							contemp[j+4]=0;
					}
				}
							
				if (content.size()>=60)
				{
					if (isComputeReturn==0)
					{
						if (quarterLineRedK==0)
						{
							if (content.get(content.size()-1)[5]>=content.get(content.size()-2)[5]&&contemp[5]<content.get(content.size()-1)[5])
							{
								quarterLineRedK=1;
							}
						}

						if (quarterLineRedK>=3&&quarterLineRedK<=5)
						{
							if (contemp[5]<content.get(content.size()-1)[5])
							{
								if (conditionAnalyzeQBDailyK(content,contemp,contemp[3]))
								{//System.out.print("\n"+s.getCell(0,temp).getContents());
									isComputeReturn=1;
									baseData=contemp;
									previousData=content.get(content.size()-1);										
									enterPoint=contemp[3];									
									currentHigh=contemp[3];
									currentLow=contemp[3];
									buytime=s.getCell(0,temp).getContents();
									weeklyrate=-(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									lead=-(contemp[2]-contemp[3])/contemp[3]*100;
									quantity=contemp[6];
									
									/*highWeeklyrate=(contemp[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									testHigh=0;
									if(weeklyrate>=6&&(contemp[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100>=divideWeeklyrate)
									{
										//System.out.print("\n"+buytime);
										enterPoint=computeEnterPoint(content,contemp,weeklyrate);
										//currentHigh=contemp[1];
										currentLow=enterPoint;
										testHigh=contemp[1];
										testLow=enterPoint;
										testWeeklyrate=(enterPoint-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									}*/

									fillStockData(allTimePoint,name,buytime,quantity+"",df.format(weeklyrate),df.format(lead));	
									
								}
								else
									quarterLineRedK++;
							}
							else
							{
								quarterLineRedK=0;
								monthLineRedK=1;
							}
						}
						else if(quarterLineRedK>=6||quarterLineRedK==1||quarterLineRedK==2) 
						{
							if (contemp[5]<content.get(content.size()-1)[5])
								quarterLineRedK++;
							else
							{
								quarterLineRedK=0;
								monthLineRedK=1;
							}
						}															
					}
					else
					{	
						if (contemp[0]>=contemp[3])
						{
							if (contemp[1]>currentHigh)
							{		
								if ((currentLow-baseData[3])/baseData[3]>-0.07)
								{
									if ((currentLow-enterPoint)/enterPoint>-0.07)
										currentHigh=contemp[1];									
								}
							}
							if (contemp[2]<currentLow&&isMonthLineDown!=1)
							{
								if ((currentHigh-enterPoint)/enterPoint<0.13)
									currentLow=contemp[2];
							}							
						}
						else
						{
							if (contemp[2]<currentLow&&isMonthLineDown!=1)
							{
								currentLow=contemp[2];
							}
							if (contemp[1]>currentHigh)
							{
								if ((currentLow-baseData[3])/baseData[3]>-0.07)
								{
									if ((currentLow-enterPoint)/enterPoint>-0.07)
										currentHigh=contemp[1];	
									
								}														
							}																										
						}
											
						if ((currentLow-baseData[3])/baseData[3]<=-0.1)
						{
							if (contemp[4]>content.get(content.size()-1)[4])
							{
								isMonthLineDown=1;
							}
						}
						
						if (endComputeReturnQB(currentLow,previousData,baseData,contemp,content))
						{									
							fillInData(allTimePoint,2,""+df.format(100*(enterPoint-currentLow)/enterPoint));
							fillInData(allTimePoint,9,""+df.format(100*(currentHigh-enterPoint)/enterPoint));
							
							isComputeReturn=0;
							isMonthLineDown=0;
							quarterLineRedK=0;
							monthLineRedK=1;
						}
					}
				}
				
				temp++;
				content.add(contemp);			
			}
		
	}
	catch (Exception e)
	{
		System.out.print("\nanalyzeStockResultByQuarterLine"+","+s.getName());
		e.printStackTrace();
	}
}
public void analyzeStockResultBearByMonthLine(Sheet s,ArrayList<String[]> allTimePoint,String name)
{	
	int monthLineRedType=0;//0:black 1,2,3:red
	int isComputeReturn=0; //0:exit 1:enter
	int isMonthLineDown=0;
	double[] baseData=new double[7],contemp=new double[7];
	double currentHigh=0,currentLow=0,enterPoint=0;
	ArrayList<double[]> content=new ArrayList<double[]>();
	DecimalFormat df=new DecimalFormat("#.##");
	
	try {					
		int temp=1;
		while (temp<s.getRows())
		{
			contemp=new double[7];
			
			if (oldOrNew==0)
			{
				for (int j=0;j<7;++j)
				{
					String st=s.getCell(j+1,temp).getContents();
					if (!st.equals(""))
						contemp[j]=Double.parseDouble(st);
					else
						contemp[j]=0;
				}
			}
			else
			{
				for (int j=0;j<6;++j)
				{
					contemp[j]=Double.parseDouble(s.getCell(j+1,temp).getContents());
				}
				contemp[6]=Double.parseDouble(s.getCell(11,temp).getContents());
			}

			if (content.size()>=4)
			{
				if (isComputeReturn==0)
				{
					if (content.get(content.size()-2)[5]<content.get(content.size()-3)[5]&&content.get(content.size()-1)[5]<content.get(content.size()-2)[5]&&contemp[5]<content.get(content.size()-1)[5])
					{
						if(conditionAnalyzeMB(content,contemp))							
						{	
							isComputeReturn=1;
							String buytime=s.getCell(0,temp).getContents();
							baseData=contemp;
							enterPoint=contemp[3];
							currentHigh=contemp[3];
							currentLow=contemp[3];
							double weeklyrate=-(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
							double lead=-(contemp[2]-contemp[3])/contemp[3]*100;
							double quantity=contemp[6];

							fillStockData(allTimePoint,name,buytime,""+quantity,""+df.format(weeklyrate),""+df.format(lead));	
						}
					}					
				}
				else 
				{	
					if (contemp[0]>=contemp[3])
					{
						if (contemp[1]>currentHigh)
						{		
							if ((currentLow-baseData[3])/baseData[3]>-0.07)
							{
								if (100*(currentLow-enterPoint)/enterPoint>-7)
									currentHigh=contemp[1];									
							}
						}
						if (contemp[2]<currentLow&&isMonthLineDown!=1)
						{
							if ((currentHigh-enterPoint)/enterPoint<0.12)
								currentLow=contemp[2];
						}							
					}
					else
					{
						if (contemp[2]<currentLow&&isMonthLineDown!=1)
						{
							currentLow=contemp[2];
						}
						if (contemp[1]>currentHigh)
						{
							if (100*(currentLow-baseData[3])/baseData[3]>-7)
							{
								if (100*(currentLow-enterPoint)/enterPoint>-7)
									currentHigh=contemp[1];	
								
							}														
						}																										
					}

					if (endComputeReturnMB(currentLow,baseData,contemp,content))
					{
						fillInData(allTimePoint,2,""+df.format(100*(enterPoint-currentLow)/enterPoint));
						fillInData(allTimePoint,9,""+df.format(100*(currentHigh-enterPoint)/enterPoint));
						
						isComputeReturn=0;
					}
				}
			}							
			content.add(contemp);
			temp++;
		}
	}
	catch (Exception e)
	{
		System.out.print("\nanalyzeStockResultByMonthLine");
		e.printStackTrace();
	}
}
public void analyzeStockResultBearByTopMonthLine(Sheet s,ArrayList<String[]> allTimePoint,String name)
{	
	int monthLineRedType=0;//0:black 1,2,3:red
	int isComputeReturn=0; //0:exit 1:enter
	int isMonthLineDown=0;
	double[] baseData=new double[7],contemp=new double[7];
	double currentHigh=0,currentLow=0,enterPoint=0;
	ArrayList<double[]> content=new ArrayList<double[]>();
	DecimalFormat df=new DecimalFormat("#.##");
	
	try {					
		int temp=1;
		while (temp<s.getRows())
		{
			contemp=new double[7];
			
			if (oldOrNew==0)
			{
				for (int j=0;j<7;++j)
				{
					String st=s.getCell(j+1,temp).getContents();
					if (!st.equals(""))
						contemp[j]=Double.parseDouble(st);
					else
						contemp[j]=0;
				}
			}
			else
			{
				for (int j=0;j<6;++j)
				{
					contemp[j]=Double.parseDouble(s.getCell(j+1,temp).getContents());
				}
				contemp[6]=Double.parseDouble(s.getCell(11,temp).getContents());
			}

			if (content.size()>=14)
			{
				if (isComputeReturn==0)
				{
					if (contemp[5]>content.get(content.size()-1)[5])	 
					{
						if(conditionAnalyzeTopMB(content,contemp))							
						{	
							isComputeReturn=1;
							String buytime=s.getCell(0,temp).getContents();
							baseData=contemp;
							enterPoint=contemp[3];
							currentHigh=contemp[3];
							currentLow=contemp[3];
							double weeklyrate=-(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
							double lead=-(contemp[2]-contemp[3])/contemp[3]*100;
							double quantity=contemp[6];
							//System.out.println(buytime);
							fillStockData(allTimePoint,name,buytime,""+quantity,""+df.format(weeklyrate),""+df.format(lead));	
						}
					}					
				}
				else 
				{	
					if (contemp[0]>=contemp[3])
					{
						if (contemp[1]>currentHigh)
						{		
							if ((currentLow-baseData[3])/baseData[3]>-0.07)
							{
								if (100*(currentLow-enterPoint)/enterPoint>-7)
									currentHigh=contemp[1];									
							}
						}
						if (contemp[2]<currentLow&&isMonthLineDown!=1)
						{
							if ((currentHigh-enterPoint)/enterPoint<0.13)
								currentLow=contemp[2];
						}							
					}
					else
					{
						if (contemp[2]<currentLow&&isMonthLineDown!=1)
						{
							currentLow=contemp[2];
						}
						if (contemp[1]>currentHigh)
						{
							if (100*(currentLow-baseData[3])/baseData[3]>-7)
							{
								if (100*(currentLow-enterPoint)/enterPoint>-7)
									currentHigh=contemp[1];	
								
							}														
						}																										
					}

					if (endComputeReturnMB(currentLow,baseData,contemp,content))
					{
						fillInData(allTimePoint,2,""+df.format(100*(enterPoint-currentLow)/enterPoint));
						fillInData(allTimePoint,9,""+df.format(100*(currentHigh-enterPoint)/enterPoint));
						
						isComputeReturn=0;
					}
				}
			}							
			content.add(contemp);
			temp++;
		}
	}
	catch (Exception e)
	{
		System.out.print("\nanalyzeStockResultByMonthLine");
		e.printStackTrace();
	}
}
public void setTitle(WritableSheet sss)
{
	try{
		sss.addCell(new Label(0,0,"number"));
		sss.addCell(new Label(1,0,"time"));
		sss.addCell(new Label(2,0,"rate"));
		sss.addCell(new Label(3,0,"hightype"));
		sss.addCell(new Label(4,0,"turnQuarterLineType"));
		sss.addCell(new Label(5,0,"quantityType"));
		sss.addCell(new Label(6,0,"quantity"));
		sss.addCell(new Label(7,0,"weekly rate"));
		sss.addCell(new Label(8,0,"lead"));
		sss.addCell(new Label(9,0,"backPoint"));
		sss.addCell(new Label(10,0,"firstComputeKHigh"));
		sss.addCell(new Label(11,0,"firstComputeKType"));
		sss.addCell(new Label(12,0,"whichK"));
		sss.addCell(new Label(13,0,"allStockType"));
		sss.addCell(new Label(14,0,"allStockType"));
		sss.addCell(new Label(15,0,"allStockType"));
		sss.addCell(new Label(16,0,"allStockType"));
	}
	catch (Exception e)
	{
		System.out.print("\nsetTitle");
		//e.printStackTrace();
	}
}
public void fillStockData(ArrayList<String[]> allTimePoint,String stockNum,String buytime,String quantity,String weeklyrate,String lead)
{	
	String[] temp={stockNum,buytime,"",""+test,""+quarterLineRedK,""+highType,quantity+"",weeklyrate,lead,"","","","","","","","",""+kType,"1"};
	allTimePoint.add(temp);
}
public void fillInData(ArrayList<String[]> allTimePoint,int location,String firstComputeReturn)
{
	allTimePoint.get(allTimePoint.size()-1)[location]=firstComputeReturn;
}
public void fillInData(ArrayList<String[]> allTimePoint,File f,int baseDate,int condition)
{	
	try{	
	WritableWorkbook writeBook=Workbook.createWorkbook(f);
	WritableSheet sss=writeBook.createSheet("my",0);
	setTitle(sss);
	
	int row=1;
	
	for (int i=0;i<allTimePoint.size();i++)	
	{		
		if(setDataCondition(allTimePoint.get(i),baseDate,condition))
		{
			for (int j=0;j<allTimePoint.get(i).length;j++)
			{
				if (j==0)
					sss.addCell(new Label(j,row,allTimePoint.get(i)[j]));
				else if(j==1)
					//sss.addCell(new DateTime(j,row,new SimpleDateFormat("yyyy/MM/dd").parse(allTimePoint.get(i)[j])));
					sss.addCell(new Label(j,row,allTimePoint.get(i)[j]));
				else
				{	
					if (!allTimePoint.get(i)[j].equals("")&&testRegular.isfloatNum(allTimePoint.get(i)[j]))
						sss.addCell(new Number(j,row,Double.parseDouble(allTimePoint.get(i)[j])));
					else
						sss.addCell(new Label(j,row,allTimePoint.get(i)[j]));
				}
			}
			
			row++;
		}		
	}
	
	writeBook.write();
	writeBook.close();
	
	}
	catch(Exception e)
	{
		System.out.print("\nfillInData");
		e.printStackTrace();
	}
	
}
public void fillInAllcondition(ArrayList<String[]> allTimePoint)
{
	try{
	Workbook workbook=Workbook.getWorkbook(new File(drive+"software/sdata/allcondition.xls"));
	Sheet s=workbook.getSheet(0);
	
	for (int i=0;i<allTimePoint.size();i++)	
	{
		String[] tt=allTimePoint.get(i);
		String[] temp=allStockCondition(tt[0],tt[1],s,tt[11]);
		tt[10]=temp[3];
		tt[11]=temp[1];
		tt[12]=temp[2];
		tt[13]=temp[4];
	}
	}
	catch(Exception e)
	{
		System.out.print("\nfillInAllcondition");
		e.printStackTrace();
	}
}
public void fillInAllconditionBydaily(ArrayList<String[]> allTimePoint)
{
	try{
	Workbook workbook=Workbook.getWorkbook(new File(drive+"software/sdata/alldaily.xls"));
	Sheet s=workbook.getSheet(0);
	
	for (int i=0;i<allTimePoint.size();i++)	
	{
		String[] tt=allTimePoint.get(i);
		String[] temp=allStockConditionBydaily(tt[0],tt[1],s);
		
		tt[10]=temp[3];
		//tt[11]=temp[1];
		//tt[12]=temp[2];
		//tt[13]=temp[4];
	}
	}
	catch(Exception e)
	{
		System.out.print("\nfillInAllconditionBydaily");
		e.printStackTrace();
	}
}
public String[] allStockConditionBydaily(String Num,String buyTime,Sheet ss)
{
	String[] temp={"0","0","0","0","0","0"};
	
	try{
		
		Sheet s=ss;
		Cell c;
		int rowlocation;
		double currentDiff,previousDiff,previousTwoDiff;
		double[] currentdata=new double[8],previousdata=new double[8],previoustwodata=new double[8],previousthreedata=new double[8]; 
				
		c=s.findCell(buyTime);
		rowlocation=c.getRow();
		
		for (int i=1;i<8;i++)
		{
			previousthreedata[i]=Double.parseDouble(s.getCell(i,rowlocation-3).getContents());
			previoustwodata[i]=Double.parseDouble(s.getCell(i,rowlocation-2).getContents());
			previousdata[i]=Double.parseDouble(s.getCell(i,rowlocation-1).getContents());
			currentdata[i]=Double.parseDouble(s.getCell(i,rowlocation).getContents());
		}
				
		currentDiff=currentdata[5]-currentdata[6];
		previousDiff=previousdata[5]-previousdata[6];
		previousTwoDiff=previoustwodata[5]-previoustwodata[6];
		
		temp[3]=currentdata[4]+"";
		
		if (currentdata[6]<previousdata[6])
		{
			if (previousdata[6]<previoustwodata[6]&&previoustwodata[6]<previousthreedata[6])
			{	
				if(currentdata[5]<previousdata[5])//月向下
				{	
					if ((currentdata[4]>currentdata[1]))
						temp[2]="11";
					else
					{
						if ((currentdata[4]-currentdata[3])/currentdata[3]>0.005)
							temp[2]="121";
						else
							temp[2]="122";
					}
				}
				else
				{
					temp[2]="2";
				}	
			}
			else
			{
				if(currentdata[5]<previousdata[5])//月向下
				{	
					if (currentdata[4]>currentdata[1])
						temp[1]="11";
					else
						temp[1]="12";
				}
				else
				{
					temp[1]="2";
				}
			}
		}
		else
		{
			if(currentdata[5]<previousdata[5])//月向下
			{
				if ((currentdata[4]>currentdata[1]))
					temp[4]="11";
				else
				{	
					if ((currentdata[4]-currentdata[3])/currentdata[3]>0.005)
						temp[4]="121";
					else 
						temp[4]="122";
				}
			}
			else
			{
				temp[4]="2";
			}
		}		
	}
	catch (Exception e)
	{
		System.out.print("\nallStockCondition,"+Num+","+buyTime);
		e.printStackTrace();
	}
	
	return temp;
}
public String[] allStockCondition(String Num,String buyTime,Sheet ss,String cur)
{
	String[] temp={"0","0","0","0","0","0"};
	
	try{
		
		Sheet s=ss;
		Cell c;
		int rowlocation;
		double currentDiff,previousDiff,previousTwoDiff;
		double[] currentdata=new double[8],previousdata=new double[8],previoustwodata=new double[8],previousthreedata=new double[8]; 
				
		c=s.findCell(buyTime);
		if (cur.equals("1"))
		{
			rowlocation=c.getRow()-1;
		}
		else
		{
			rowlocation=c.getRow();
		}
		
		for (int i=1;i<8;i++)
		{
			previousthreedata[i]=Double.parseDouble(s.getCell(i,rowlocation-3).getContents());
			previoustwodata[i]=Double.parseDouble(s.getCell(i,rowlocation-2).getContents());
			previousdata[i]=Double.parseDouble(s.getCell(i,rowlocation-1).getContents());
			currentdata[i]=Double.parseDouble(s.getCell(i,rowlocation).getContents());
		}
				
		currentDiff=currentdata[5]-currentdata[6];
		previousDiff=previousdata[5]-previousdata[6];
		previousTwoDiff=previoustwodata[5]-previoustwodata[6];
		
		temp[3]=currentdata[4]+"";
		
		if (currentdata[6]<previousdata[6])
		{
			if (previousdata[6]<previoustwodata[6]&&previoustwodata[6]<previousthreedata[6])
			{					
				if(currentdata[5]<previousdata[5])//月向下
				{	
					if(currentdata[1]>currentdata[4])
					{
						if ((currentdata[4]-currentdata[3])/currentdata[3]<0.01)
							temp[1]="1";
					}
					else
					{
						if ((currentdata[4]-currentdata[3])/currentdata[3]<0.01)
							temp[1]="2";
					}
				}
				else
				{
					if ((currentdata[4]-currentdata[1])/currentdata[1]>0.025)
						temp[1]="3";
				}			
			}
			else
			{	
				if(currentdata[1]>currentdata[4])//黑k
					if(currentdata[5]<previousdata[5])//月向下
						temp[2]="1";
					else
						temp[2]="2";
					
			}	
			if(currentdata[5]<previousdata[5])//月向下
				temp[2]="3";
			else
				temp[2]="4";
		}
		else
		{
			if(currentdata[5]<previousdata[5])	
			{
				if(previoustwodata[5]>previousdata[5])
					temp[4]="2";
				else
					temp[4]="1";
			}
			else
			{
				temp[4]="3";
			}
		}
		/*if(currentdata[6]>=previousdata[6])	
		{					
			if(currentdata[4]>6500)
			{
				if (previousDiff>=275)
				{
					if (currentDiff>=275)
					{
						if (currentdata[1]>=currentdata[4])
						{
							if((currentdata[2]-currentdata[4])/currentdata[4]>0.02)
								temp[3]="1";
						}
						else
						{
							if ((currentdata[2]-currentdata[4])/currentdata[4]>0.0095)
							{
								if((currentdata[4]-currentdata[1])/currentdata[1]>0.012)
									temp[3]="1";
								else
								{
									if(currentdata[1]>previousdata[4])
										if((currentdata[4]-currentdata[1])>20)
											temp[3]="1";
								}
								
							}
						}
					}
				}		
			}			
		}*/
		if(currentdata[6]>=previousdata[6])	
		if (currentDiff>=200&&currentdata[4]>6500)//月季線距大於200,月線向上
		{
			/*if(currentdata[5]>=previousdata[5])
			{
				if (currentdata[1]>currentdata[4]&&(currentdata[2]-currentdata[4])/currentdata[4]>0.015)//大黑
				{
						temp[1]="1";
				}
				if (currentDiff<=275&&currentDiff-previousDiff>50)
				{
						temp[2]="1";
				}
				if(currentDiff-previousDiff<0)
						temp[3]="1";
			}
			else
			{
				if((currentdata[4]-currentdata[1])/currentdata[1]<0.01)
				{
					temp[4]="1";
				}
			}*/	
		}
		
	}
	catch (Exception e)
	{
		System.out.print("\nallStockCondition,"+Num+","+buyTime);
		e.printStackTrace();
	}
	
	return temp;
}
public void computeResult(ArrayList<String[]> allTimePoint,int baseDate,int condition)
{
	int r1=0,r2=0,r3=0,r11=0,r12=0,r13=0;
	
	for (int i=0;i<allTimePoint.size();i++)	
	{	
		try{
			if(setDataCondition(allTimePoint.get(i),baseDate,condition))
			{
				if(Double.parseDouble(allTimePoint.get(i)[7])>=divideWeeklyrate)
				{
					if (!allTimePoint.get(i)[2].equals(""))
					{
						if(Double.parseDouble(allTimePoint.get(i)[2])>=10)
						{
							r11++;
						}
						else if(Double.parseDouble(allTimePoint.get(i)[2])>=7&&Double.parseDouble(allTimePoint.get(i)[2])<10)
						{
							r12++;
						}
						else
						{
							r13++;
						}	
					}
				}
				else
				{
					if(Double.parseDouble(allTimePoint.get(i)[7])>=-6)
					{
						if (!allTimePoint.get(i)[2].equals(""))
						{
							if(Double.parseDouble(allTimePoint.get(i)[2])>=10)
							{
								r1++;
							}
							else if(Double.parseDouble(allTimePoint.get(i)[2])>=7&&Double.parseDouble(allTimePoint.get(i)[2])<10)
							{
								r2++;
							}
							else
							{
								r3++;
							}	
						}	
					}
					else
					{

					}
				}		
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	if((r1+r2+r3)!=0)
		System.out.print("\n"+(r1+r2+r3)+","+r1*1000/(r1+r2+r3)+","+r2*1000/(r1+r2+r3)+","+r3*1000/(r1+r2+r3));
	if((r11+r12+r13)!=0)
		System.out.print("\n"+(r11+r12+r13)+","+r11*1000/(r11+r12+r13)+","+r12*1000/(r11+r12+r13)+","+r13*1000/(r11+r12+r13));
	if((r1+r2+r3)!=0&&(r11+r12+r13)!=0)
		System.out.print("\n"+(r1+r2+r3+r11+r12+r13)+","+(r1+r11)*1000/(r1+r2+r3+r11+r12+r13)+","+(r2+r12)*1000/(r1+r2+r3+r11+r12+r13)+","+(r3+r13)*1000/(r1+r2+r3+r11+r12+r13));
}
public boolean setDataCondition(String[] data,int baseDate,int condition)
{
	if(data[1].lastIndexOf("/")-data[1].indexOf("/")==2)
		data[1]=data[1].substring(0, 5)+"0"+data[1].substring(5, data[1].length());
	if(data[1].length()-data[1].lastIndexOf("/")==2)
		data[1]=data[1].substring(0, data[1].lastIndexOf("/")+1)+"0"+data[1].substring(data[1].lastIndexOf("/")+1, data[1].length());
	
	if (data[1].equals(""))
		return false;
	if (Integer.parseInt(data[1].replaceAll("/", ""))<baseDate)
			return false;
	
	//if (data[2].equals(""))
		//return false;
	if (findstock==1&&!data[2].equals(""))
		return false;
	if (findstock==0&&data[2].equals(""))
		return false;
	
	if(Double.parseDouble(data[6])<50)
		return false;
	
	/*if(Double.parseDouble(data[7])>61)
		return false;*/
	
	try{
		if(condition==0)//多,季,週中
		{
			/*if (Double.parseDouble(data[8])==0&&Double.parseDouble(data[6])<=15000)
				return true;
			else if (Double.parseDouble(data[8])<=3.3&&Double.parseDouble(data[6])<=8000)
				return true;
			else if (Double.parseDouble(data[8])>3.3&&Double.parseDouble(data[6])<=5000)
				return true;
			else
				return false;*/

			//if(data[13].equals("")||Double.parseDouble(data[13])>14)
				//return false;
		}
		else if(condition==1)//多,季,週收
		{	
			if(predict!=1)
			{
				if(Double.parseDouble(data[7])>=divideWeeklyrate) //***量過高沒過較好
				{
					if(Double.parseDouble(data[8])>3)
						return false;
					if (Double.parseDouble(data[7])<=14)
						if(Double.parseDouble(data[6])>10000)
							return false;		
				}
				else 
				{
					if(Double.parseDouble(data[7])>=6)
					{
						if(Double.parseDouble(data[8])>2.5)  //*** 2.5較好
							return false;
						if(Double.parseDouble(data[6])>8000)//*** 6000較好
							return false;
					}
					else
					{
						if(Double.parseDouble(data[7])>=5)
						{
							if(Double.parseDouble(data[6])>6000)
								return false;
							if(Double.parseDouble(data[8])>0.5)
								return false;
						}
						else
						{
							if(Double.parseDouble(data[6])>5000)
								return false;
							if(Double.parseDouble(data[8])>0.5)
								return false;
						}
					}	
				}
			}		
		}
		else if(condition==2)//多,月,週收
		{

		}
		else if(condition==3)//
		{
			if(Double.parseDouble(data[7])>=divideWeeklyrate)
			{
				/*if(Double.parseDouble(data[8])>3)
					return false;*/
				/*if(Double.parseDouble(data[6])>8000) //*** 5000?
					return false;*/
			}
		}
		else if(condition==4)//空,季,週收
		{
			
		}
		else//空,月,週收
		{
			if(Double.parseDouble(data[7])>=divideWeeklyrate)
			{
				if(Double.parseDouble(data[8])>3)
					return false;
			}
			else 
			{
				if(Double.parseDouble(data[8])>1)
					return false;	
			}
		}
		
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	
	return true;
}
public boolean conditionAnalyzeQ(ArrayList<double[]> base,double[] compare,double enterPoint)
{	
	int weeklyRateType;
	//quantityType=isOverQuantity(base,compare);
	//highType=isOverHigh(base,compare);
	weeklyRateType=weeklyRateType(base,compare);
	
	if(isTurnQuarterLine(base,compare,enterPoint))
		if(isTurnMonthLine(base,compare,enterPoint))
			if(weeklyRateType!=0)
			{				
				//if ((quantityType>=1&&quantityType<=3)||highType!=0)
				if (kType(quantityType,base,compare,enterPoint))
				{
					
					return true;
				}
			}
							
	return false;
}
public boolean conditionAnalyzeQB(ArrayList<double[]> base,double[] compare,double enterPoint)
{
	int weeklyRateType;
	
	quantityType=isOverQuantityB(base,compare);
	highType=isOverHighB(base,compare);
	weeklyRateType=weeklyRateTypeB(base,compare);
	
	if(isTurnQuarterLineB(base,compare,enterPoint))
		if(isTurnMonthLineB(base,compare,enterPoint))
			if(weeklyRateType!=0)
			{				
				if (highType!=0)
				//if (kTypeB(quantityType,base,compare,enterPoint)) //***
					return true;
			}
							
	return false;
}
public boolean conditionAnalyzeQBDailyK(ArrayList<double[]> base,double[] compare,double enterPoint)
{
	int weeklyRateType;
	
	quantityType=isOverQuantityB(base,compare);
	highType=isOverHighB(base,compare);
	weeklyRateType=weeklyRateTypeB(base,compare);
	
	if(isTurnQuarterLineB(base,compare,enterPoint))
		if(isTurnMonthLineB(base,compare,enterPoint))
			if(weeklyRateType!=0)
			{				
				if (highType!=0)
				//if (kTypeB(quantityType,base,compare,enterPoint)) //***
					return true;
			}
							
	return false;
}
public boolean conditionAnalyzeM(ArrayList<double[]> base,double[] compare)
{
	double[] p1,p2,p3,p4;
	
	p1=base.get(base.size()-1);
	p2=base.get(base.size()-2);
	p3=base.get(base.size()-3);
	p4=base.get(base.size()-4);
	
	test=0;
	if(true)
	if((p1[4]<p2[4])&&(compare[4]>=p1[4]))//月線黑翻紅
	{
		//if ((p4[1]-compare[2])/p4[1]<=0.1)
		if (compare[3]>=p1[3])
		if(compare[3]>compare[4])
		{	
			if(compare[3]>compare[0])
				test=1;
			else
				test=2;
			
				return true;			
		}
		else
		{
			/*if(compare[3]>compare[0])
				if((compare[3]-compare[2])/compare[2]>=0.07)
					if((compare[4]-compare[3])/compare[4]<=0.002)
				{
					test=1;
					return true;
				}*/
		}
	}
								
	return false;
}
public boolean conditionAnalyzeMM(ArrayList<double[]> base,double[] compare)
{
	double[] p1,p2;
	
	p1=base.get(base.size()-1);
	p2=base.get(base.size()-2);
	
	test=0;
	if(true)
	if((compare[5]>=p1[5])&&(p1[5]>=p2[5]))//季線連2紅
	{
		//if ((p4[1]-compare[2])/p4[1]<=0.1)
		if (compare[3]>=p1[3])//收漲
		if(compare[3]>compare[4])//收在月線上
		{	
			if(compare[3]>compare[0])
				test=1;
			else
				test=2;
			
			return true;			
		}
	}
								
	return false;
}
public boolean conditionAnalyzeMB(ArrayList<double[]> base,double[] compare)
{
	
	double[] p1,p2,p3,p4;
	
	p1=base.get(base.size()-1);
	p2=base.get(base.size()-2);
	p3=base.get(base.size()-3);
	p4=base.get(base.size()-4);
	
	double m1,q1,q2,q3,q4;
	
	q1=(compare[5]-base.get(base.size()-1)[5])/base.get(base.size()-1)[5];//翻轉幅度
	m1=(compare[4]-base.get(base.size()-1)[4])/base.get(base.size()-1)[4];//翻轉幅度
	
	q2=base.get(base.size()-1)[5]-compare[5];//翻轉幅度
	q3=base.get(base.size()-2)[5]-base.get(base.size()-1)[5];//翻轉幅度
	q4=base.get(base.size()-3)[5]-base.get(base.size()-2)[5];//翻轉幅度
	
	if((compare[3]<=compare[4]))
		if(compare[4]<compare[5])//***
			if((base.get(base.size()-1)[3]-compare[3])/base.get(base.size()-1)[3]>=0.06)
				if (base.get(base.size()-1)[4]>=base.get(base.size()-2)[4]&&compare[4]<base.get(base.size()-1)[4])//月線紅翻黑
					//if (base.get(base.size()-2)[4]<base.get(base.size()-3)[4])//***

					return true;											
	
					
	return false;
}
public boolean conditionAnalyzeTopMB(ArrayList<double[]> base,double[] compare)
{
	
	double[] p1,p2,p3,p4;
	
	p1=base.get(base.size()-1);
	p2=base.get(base.size()-2);
	p3=base.get(base.size()-3);
	p4=base.get(base.size()-4);
	
	double m1,q1,q2,q3,q4;
	
	if (p1[4]>p2[4]&&compare[4]<p1[4])
	{//System.out.println("1");
		if ((p1[4]-p1[5])/p1[5]>0.1)
		{//System.out.println("2");
			if((compare[0]>compare[3]))
			{//System.out.println("3");
				for (int i=2;i<12;i++)
				{
					if (base.get(base.size()-i-1)[4]<base.get(base.size()-i-2)[4])
						if (base.get(base.size()-i)[4]>base.get(base.size()-i-1)[4])
						{//System.out.println("4:"+(base.get(base.size()-i-1)[5]-base.get(base.size()-i-1)[4])/base.get(base.size()-i-1)[5]);
							if ((base.get(base.size()-i-1)[5]-base.get(base.size()-i-1)[4])/base.get(base.size()-i-1)[5]>0.1)
							{//System.out.println("5:"+(base.get(base.size()-i-1)[5]-base.get(base.size()-i-1)[4])/base.get(base.size()-i-1)[5]);
								return true;
							}
							else
								return false;
						}
				}
			}
		}
			
	}
		
																
					
	return false;
}
public boolean isTurnQuarterLine(ArrayList<double[]> base,double[] compare,double enterPoint)
{//System.out.print("\nisTurnQuarterLine");
	//System.out.println("hhhh "+enterPoint);	
	double currentQline;
	
	currentQline=(enterPoint-compare[3])/13+compare[5];

	if(enterPoint>currentQline)//站上季線
	if((enterPoint-currentQline)>=(currentQline-compare[0]))//k棒明顯突破季線
	{			//System.out.println("hhhh1 "+enterPoint+" "+compare[3]+" "+compare[5]);	
		if ((currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5]>=0)
		{//System.out.println("hhhh2 "+enterPoint+" "+compare[3]+" "+compare[5]);
			return true;
		}
		
		//turnQuarterLineDegree=(currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5];//翻轉幅度
		
		/*if (turnQuarterLineDegree>=0.001)//翻轉超過0.001 ***
		{
			return true;
		}
		else
		{
			if (compare[3]==compare[1])//收最高
			{
				return true;
			}
		}	*/	
	}
			return false;
}
public boolean isTurnQuarterLineB(ArrayList<double[]> base,double[] compare,double enterPoint)
{//System.out.print("\nisTurnQuarterLine");
	double currentQline;
	
	currentQline=(enterPoint-compare[3])/13+compare[5];
	
	if(enterPoint<currentQline)//跌破季線
	if((enterPoint-currentQline)<=(currentQline-compare[0]))//k棒明顯跌破季線
	{				
		if ((currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5]<=0)
		{
			return true;
		}
	}
	else
	{
		
	}
			return false;
}
public boolean isTurnMonthLine(ArrayList<double[]> base,double[] compare,double enterPoint)
{//System.out.print("\nisTurnMonthLine\n");
	//System.out.println("qqqq "+enterPoint);	
	double currentMline;
	double currentQline;
	
	currentQline=(enterPoint-compare[3])/13+compare[5];
	currentMline=(enterPoint-compare[3])/4+compare[4];
	
	if(enterPoint>currentMline)//站上月線
	if((enterPoint-currentMline)>=(currentMline-compare[0]))//k棒明顯突破月線
	{
		//turnMonthLineDegree=(currentMline-base.get(base.size()-1)[4])/base.get(base.size()-1)[4];//翻轉幅度
		
		if((currentMline-base.get(base.size()-1)[4])/base.get(base.size()-1)[4]>=(currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5])
		{
			return true;
		}
	}

		return false;
}
public boolean isTurnMonthLineB(ArrayList<double[]> base,double[] compare,double enterPoint)
{//System.out.print("\nisTurnMonthLine\n");
	double currentMline;
	double currentQline;
	
	currentQline=(enterPoint-compare[3])/13+compare[5];
	currentMline=(enterPoint-compare[3])/4+compare[4];
	
	if(enterPoint<currentMline)//跌破月線
	if((enterPoint-currentMline)<=(currentMline-compare[0]))//k棒明顯跌破月線 ***
	{
		if((currentMline-base.get(base.size()-1)[4])/base.get(base.size()-1)[4]<=(currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5])
		{
			return true;
		}
	}
		return false;
}
public int weeklyRateType(ArrayList<double[]> base,double[] compare)
{	//System.out.print("\nisweeklyRateType\n");
	if (predict==1)
	{
		if ((compare[1]-base.get(base.size()-1)[3])/base.get(base.size()-1)[3]>=0.09)
			return 1;
	}
	else
	{
		if ((compare[3]-base.get(base.size()-1)[3])/base.get(base.size()-1)[3]*100>0)
		{		
			if ((compare[3]-base.get(base.size()-1)[3])/base.get(base.size()-1)[3]*100<6)
			{
				if ((compare[1]-compare[3])/compare[3]*100>1)
					return 0;
			}
			return 1;
		}
	}	
	
	return 0;
}
public int weeklyRateTypeB(ArrayList<double[]> base,double[] compare)
{		
	if ((compare[3]-base.get(base.size()-1)[3])/base.get(base.size()-1)[3]<=-0.06)
		if ((base.get(base.size()-1)[3]-base.get(base.size()-2)[3])/base.get(base.size()-2)[3]>-0.06) //***0.05 good	
			return 1;
	
	return 0;
}
public boolean kType(int quantityType,ArrayList<double[]> base,double[] compare,double enterPoint)
{
	if ((kType=isRedK(quantityType,base,compare,enterPoint))!=0)
		return true;		

	return false;	
}
public boolean kTypeB(int quantityType,ArrayList<double[]> base,double[] compare,double enterPoint)
{
	if ((kType=isRedKB(quantityType,base,compare,enterPoint))!=0)
		return true;		

	return false;	
}
public int isRedK(int quantityType,ArrayList<double[]> base,double[] compare,double enterPoint)
{//System.out.print("\nisRedK");
	
	if((compare[1] - base.get(base.size() - 1)[3]) / base.get(base.size() - 1)[3] >=0.09&&compare[1]==compare[3])
		return 9;
	
	if (base.get(base.size() - 1)[1]== base.get(base.size() - 1)[3]&&(enterPoint - base.get(base.size() - 1)[3]) / base.get(base.size() - 1)[3] >=0.05)
	{
		return 1;
	}
	else
	{
		if ((enterPoint-compare[0])/compare[0]*100>=5)//紅棒區域>=5%
		{
			return 2;
		}	
		else
		{
			if((enterPoint-compare[2])/compare[2]*100>=7)//從低點算>=7%
			{
				if ((enterPoint - compare[0]) / compare[0] > 0.02)// 大於2%紅棒
				{
					return 3;
				} 
				else 
				{
					if(enterPoint >= compare[0])// 紅k
						if (base.get(base.size() - 1)[3]>=base.get(base.size() - 1)[0])// 前一根紅k
							if (base.get(base.size() - 1)[1] - compare[2] > compare[1]- base.get(base.size() - 1)[1])// k棒多半落在前一k棒裡面
							{
								return 4;
							}
				}
			}
			else
			{
				return 0;
			}
		}	
	}	
		
		return 0;
}
public int isRedKB(int quantityType,ArrayList<double[]> base,double[] compare,double enterPoint)
{//System.out.print("\nisRedK");
	
	if (base.get(base.size() - 1)[3]==base.get(base.size() - 1)[2]&&(enterPoint - base.get(base.size() - 1)[3]) / base.get(base.size() - 1)[3] <=-0.05)
	{
		return 1;
	}
	else
	{
		if ((enterPoint-compare[0])/compare[0]<=-0.05)
		{
			return 2;
		}	
		else
		{
			if((enterPoint-compare[1])/compare[1]<=-0.07)
			{
				if ((enterPoint - compare[0]) / compare[0] < -0.02)// 大於2%黑棒
				{
					return 3;
				} 
				else 
				{
					if(enterPoint <= compare[0])// 黑k
						if (base.get(base.size() - 1)[3]<=base.get(base.size() - 1)[0])// 前一根黑k
							if (base.get(base.size() - 1)[2] - compare[1] < compare[2]- base.get(base.size() - 1)[2])// k棒多半落在前一k棒裡面
							{
								return 4;
							}
				}
			}
			else
			{
				return 0;
			}
		}	
	}	
		
		return 0;
}
public int isOverQuantity(ArrayList<double[]> base,double[] compare)
{//System.out.print("\nisOverQuantity\n");
	int isOverQuantity=1,preHighK=0,pre2ndHighK=0;
	double quantity,preHigh,pre2ndHigh,preLow,preHighQuantity,pre2ndHighQuantity,preLowQuantity,maxQuantity;
	
	/*if (base.get(base.size()-(quarterKCount-1))[1]>base.get(base.size()-(quarterKCount-2))[1])
	{
		maxHighK=base.size()-(quarterKCount-1);
		secndHighK=base.size()-(quarterKCount-2);
		preHigh=base.get(base.size()-(quarterKCount-1))[1];
		pre2ndHigh=base.get(base.size()-(quarterKCount-2))[1];
		preHighQuantity=base.get(base.size()-(quarterKCount-1))[6];
		pre2ndHighQuantity=base.get(base.size()-(quarterKCount-2))[6];
	}
	else
	{
		maxHighK=base.size()-(quarterKCount-2);
		secndHighK=base.size()-(quarterKCount-1);
		preHigh=base.get(base.size()-(quarterKCount-2))[1];
		pre2ndHigh=base.get(base.size()-(quarterKCount-1))[1];
		preHighQuantity=base.get(base.size()-(quarterKCount-2))[6];
		pre2ndHighQuantity=base.get(base.size()-(quarterKCount-1))[6];
	}
	
	if(base.get(base.size()-(quarterKCount-1))[2]>=base.get(base.size()-(quarterKCount-2))[2])
	{
		preLow=base.get(base.size()-(quarterKCount-2))[2];
		preLowQuantity=base.get(base.size()-(quarterKCount-2))[6];
	}
	else
	{
		preLow=base.get(base.size()-(quarterKCount-1))[2];
		preLowQuantity=base.get(base.size()-(quarterKCount-1))[6];
	}
	
	if (base.get(base.size()-(quarterKCount-1))[6]>base.get(base.size()-(quarterKCount-2))[6])
		maxQuantity=base.get(base.size()-(quarterKCount-1))[6];
	else
		maxQuantity=base.get(base.size()-(quarterKCount-2))[6];

	for (int i=base.size()-(quarterKCount-2);i<base.size()-1;i++)
	{
		if (preHigh<base.get(i+1)[1])
		{				
			secndHighK=maxHighK;
			pre2ndHigh=preHigh;
			pre2ndHighQuantity=preHighQuantity;
			maxHighK=i+1;
			preHigh=base.get(i+1)[1];
			preHighQuantity=base.get(i+1)[6];					
		}				
		else if (preHigh==base.get(i+1)[1])
		{
			if(preHighQuantity<base.get(i+1)[6])
			{
				secndHighK=maxHighK;
				pre2ndHighQuantity=preHighQuantity;
				preHighQuantity=base.get(i+1)[6];
				maxHighK=i+1;
			}
			else
			{
				secndHighK=i+1;
				pre2ndHighQuantity=base.get(i+1)[6];
			}
			pre2ndHigh=preHigh;
		}
		else
		{
			if(pre2ndHigh<base.get(i+1)[1])
			{
				secndHighK=i+1;
				pre2ndHigh=base.get(i+1)[1];
				pre2ndHighQuantity=base.get(i+1)[6];
			}
			if (pre2ndHigh==base.get(i+1)[1])
			{
				if(pre2ndHighQuantity<base.get(i+1)[6])
				{
					secndHighK=i+1;
					pre2ndHighQuantity=base.get(i+1)[6];
				}
			}
		}
		if (preLow>base.get(i+1)[2])
		{
			preLow=base.get(i+1)[2];									
			preLowQuantity=base.get(i+1)[6];						
		}
		if (preLow==base.get(i+1)[2])
		{
			if(preLowQuantity<base.get(i+1)[6])
				preLowQuantity=base.get(i+1)[6];
		}
		if (maxQuantity<base.get(i+1)[6])
		{
			maxQuantity=base.get(i+1)[6];
		}

	}*/	
		
	for (int i=base.size()-(quarterKCount-1);i<=base.size()-1;i++)
	{
		if (compare[6]<base.get(i)[6])
		{
			isOverQuantity=0;
			break;
		}
	}
	
	if (isOverQuantity==1)
	{
		return 1;//最大量
	}
	
	preHighK=base.size()-(quarterKCount-1);
		
	for (int i=base.size()-(quarterKCount-1-1);i<=base.size()-1;i++)
	{
		if (base.get(i)[1]>=base.get(preHighK)[1])
		{
			preHighK=i;		
		}
	}
	preHigh=base.get(preHighK)[1];
	preHighQuantity=base.get(preHighK)[6];
			
	quantity=compare[6];
	
	if (quantity>=preHighQuantity)
		return 2;

	if (base.get(base.size()-1)[3]>=base.get(base.size()-1)[0])
	{
		quantity+=base.get(base.size()-1)[6];//前一根收紅k,算組合量

		if (base.get(base.size()-2)[3]>=base.get(base.size()-2)[0])
		{
			quantity+=base.get(base.size()-2)[6];//前二根收紅k,算組合量
		}
	}

	if (quantity>=preHighQuantity)
		return 3;

	/*if (compare[3]>=base.get(maxHighK)[3])
			{
				return 7;
			}

			if (maxHighK==base.size()-1)//前一根k棒有最高點			
			{
				if((compare[3]-compare[0])/compare[0]>0.05)//超過5%的大紅棒
				{
					if((compare[0]-base.get(base.size()-1)[3])/base.get(base.size()-1)[3]<0.015)//開盤比前k棒收盤高不到1.5%
						//if (compare[6]>base.get(base.size()-1)[6]*0.9)//量超過前一根k棒的9成	
							return 4;
				}
			}

			if(maxHighK==base.size()-2)
			{
				if(base.get(base.size()-2)[6]>=pre2ndHighQuantity)
				{
					if((base.get(base.size()-2)[3]-base.get(base.size()-2)[0])/base.get(base.size()-2)[0]>=0.06)
					{
						if((compare[1]-compare[3])/compare[3]<0.07)
							return 5;
					}
				}
			}

			if(maxHighK==base.size()-3)
			{
				if(base.get(base.size()-3)[6]>=pre2ndHighQuantity)
				{
					if((base.get(base.size()-3)[3]-base.get(base.size()-3)[0])/base.get(base.size()-3)[0]>=0.06)
					{
						if((compare[1]-compare[3])/compare[3]<0.07)
							return 6;
					}
				}
			}			

			if (preLowQuantity==maxQuantity)//最低檔有最大量
				return 9;*/			
						
	return 0;
}
public int isOverQuantityB(ArrayList<double[]> base,double[] compare)
{//System.out.print("\nisOverQuantity\n");
	int isOverQuantity=1,preLowK=0,pre2ndLowK=0;
	double quantity,preLow,pre2ndLow,preHigh,preLowQuantity,pre2ndLowQuantity,preHighQuantity,maxQuantity;
		
	for (int i=base.size()-(quarterKCount-1);i<=base.size()-1;i++)
	{
		if (compare[6]<base.get(i)[6])
		{
			isOverQuantity=0;
			break;
		}
	}
	
	if (isOverQuantity==1)
	{
		return 1;//最大量
	}
	
	preLowK=base.size()-(quarterKCount-1);
		
	for (int i=base.size()-(quarterKCount-1-1);i<=base.size()-1;i++)
	{
		if (base.get(i)[2]<=base.get(preLowK)[2])
		{
			preLowK=i;		
		}
	}
	preLow=base.get(preLowK)[1];
	preLowQuantity=base.get(preLowK)[6];
			
	quantity=compare[6];
	
	if (quantity>=preLowQuantity)
		return 2;

	if (base.get(base.size()-1)[3]<=base.get(base.size()-1)[0])
	{
		quantity+=base.get(base.size()-1)[6];//前一根收黑k,算組合量

		if (base.get(base.size()-2)[3]<=base.get(base.size()-2)[0])
		{
			quantity+=base.get(base.size()-2)[6];//前二根收黑k,算組合量
		}
	}

	if (quantity>=preLowQuantity)
		return 3;
						
	return 0;
}
public int isOverHigh(ArrayList<double[]> base,double[] compare)
{//System.out.print("\nisOverHigh");

	int highType=1,kn=base.size()-1;//0:非最高 1:最高
	double highestPoint=base.get(base.size()-1)[1],highestKClose=base.get(base.size()-1)[3];
	
	for (int i=base.size()-(quarterKCount-1);i<base.size()-1;i++)
	{
		if (highestPoint<base.get(i)[1])//非最高
		{
			kn=i;
			highestPoint=base.get(i)[1];
			highestKClose=base.get(i)[3];
		}	
	}	
	
	if (compare[1]>highestPoint)
	{
		if (compare[3]>=highestPoint)
			return 1;
		else
			return 2;
	}
	else
	{		
		if (compare[3]==compare[1])//本身收最高
			if (compare[3]>=highestKClose)
			{
					return 3;
			}
	}
	
	return 0;
}
public int isOverHighB(ArrayList<double[]> base,double[] compare)
{//System.out.print("\nisOverHigh");

	int lowType=1,kn=base.size()-1;//0:非最高 1:最高
	double lowestPoint=base.get(base.size()-1)[1],lowestKClose=base.get(base.size()-1)[3];
	
	for (int i=base.size()-(quarterKCount-1);i<base.size()-1;i++)
	{
		if (lowestPoint>base.get(i)[1])//非最高
		{
			kn=i;
			lowestPoint=base.get(i)[2];
			lowestKClose=base.get(i)[3];
		}	
	}	
	
	if (compare[2]<lowestPoint)
	{
		if (compare[3]<=lowestPoint)
			return 1;
		else
			return 2;
	}
	else
	{		
		if (compare[3]==compare[2])//本身收最低
			if (compare[3]<=lowestKClose)
			{
					//return 3; //***
			}
	}
	
	return 0;
}
public boolean endComputeReturnQ(double currentHigh,double[] baseData,double[] contemp,ArrayList<double[]> content)
{	
	if ((baseData[3]-contemp[2])/baseData[3]>0.13)//拉回超過12%
	{
		return true;
	}
	else if(contemp[5]<content.get(content.size()-1)[5])//季線向下
	{
		if ((currentHigh-baseData[3])/baseData[3]>=0.1)//高點漲超過10%
		{
			return true;
		}							
	}
		
	return false;
}
public boolean endComputeReturnQDay(Sheet s,String name,String buytime,double[] baseData,double predictpoint,double[] returnV,int day)
{	
		
	
	int row=0;	
	double[] contemp=new double[5];
	double enterPoint=predictpoint;//baseData[3];									
	double curtime=0;
	long sundayTime;

	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd"); 		
	Calendar cal = Calendar.getInstance(); 
	try{
		Cell c=s.findCell(buytime);
		if (c==null)
			return false;
		row=c.getRow();		

		cal.setTime(format.parse(s.getCell(0,row).getContents()));  
		int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
		

		sundayTime=format.parse(s.getCell(0,row).getContents()).getTime()+(8-dayOfWeek)*(24*60*60*1000);
	}
	catch(Exception e)
	{
		return false;
	}
		
	do
	{
			if (row+day>=s.getRows())
				break;
			
			try{
			contemp[0]=Double.parseDouble(s.getCell(1,row+day).getContents());
			contemp[1]=Double.parseDouble(s.getCell(2,row+day).getContents());
			contemp[2]=Double.parseDouble(s.getCell(3,row+day).getContents());
			contemp[3]=Double.parseDouble(s.getCell(4,row+day).getContents());
			contemp[4]=Double.parseDouble(s.getCell(8,row+day).getContents());

			if (contemp[0]>=contemp[3])
			{
				if (contemp[1]>returnV[0])
				{		
					returnV[0]=contemp[1];	
				}

				if ((enterPoint-contemp[2])/enterPoint>0.13)//拉回超過13%
				{
					return true;
				}
				else if(contemp[4]<Double.parseDouble(s.getCell(8,row+day-1).getContents()))//季線向下
				{
					if ((returnV[0]-enterPoint)/enterPoint>=0.1)//高點漲超過10%
					{
						return true;
					}							
				}

				if (contemp[2]<returnV[1])
				{
					if ((returnV[0]-enterPoint)/enterPoint<0.07)//高點不超過7%
					{
						returnV[1]=contemp[2];		
					}				
				}
			}
			else
			{
				if (contemp[2]<returnV[1])
				{					
					if ((returnV[0]-enterPoint)/enterPoint<0.07)//高點不超過7%
					{
						returnV[1]=contemp[2];	
					}
				}

				if ((enterPoint-contemp[2])/enterPoint>0.13)//拉回超過13%
				{
					return true;
				}
				else if(contemp[4]<Double.parseDouble(s.getCell(8,row+day-1).getContents()))//季線向下
				{
					if ((returnV[0]-enterPoint)/enterPoint>=0.1)//高點漲超過10%
					{
						return true;
					}							
				}

				if (contemp[1]>returnV[0])
				{
					returnV[0]=contemp[1];		
				}
			}
			day++;
			
			if ((row+day)<s.getRows())
				curtime=format.parse(s.getCell(0,row+day).getContents()).getTime();
		}
		catch(Exception e)
		{
			System.out.println(row+" "+day+" "+s.getRows());
			System.out.println(s.getName()+" "+buytime);
			System.out.println("endComputeReturnQDay ");
			e.printStackTrace();
			day++;
			continue;
		}
	}
	while((row+day)<s.getRows()&&curtime<sundayTime);
	
	return false;
}
public boolean endComputeReturnQB(double currentLow,double[] previousData,double[] baseData,double[] contemp,ArrayList<double[]> content)
{	
	if ((baseData[3]-contemp[1])/baseData[3]<-0.13)//拉回超過13%
	{
		return true;
	}
	else if(contemp[5]>content.get(content.size()-1)[5])//季線向上
	{
		if ((currentLow-baseData[3])/baseData[3]<=-0.1)//報酬超過10%
		{
			return true;
		}							
	}	
		
	return false;
}
public boolean endComputeReturnM(double currentHigh,double[] baseData,double[] contemp,ArrayList<double[]> content,double enterPointM)
{
	if ((enterPointM-contemp[2])/enterPointM>0.12)//拉回超過12%
	{
		return true;
	}
	else if(contemp[4]<content.get(content.size()-1)[4])//月線向下
	{
		if ((currentHigh-enterPointM)/enterPointM>=0.1)//高點漲超過10%
		{
			return true;
		}
	}
	
	return false;
}
public boolean endComputeReturnMB(double currentLow,double[] baseData,double[] contemp,ArrayList<double[]> content)
{
	if ((baseData[3]-contemp[1])/baseData[3]<-0.13)//
	{
		return true;
	}
	else if(contemp[4]>content.get(content.size()-1)[4])//月線向上
	{
		if ((currentLow-baseData[3])/baseData[3]<=-0.07)//低點超過10%
		{
			return true;
		}
	}
	
	return false;
}
public void firstComputeK(double[] baseData,double[] contemp)
{
	int tt=0;
	
	if (contemp[3]>=baseData[3])
	{
		if (contemp[3]>=contemp[0])
		{
			tt=11;
		}
		else
		{
			tt=12;
		}
	}
	else
	{
		if (contemp[3]>=contemp[0])
		{
			tt=21;
		}
		else
		{
			if((baseData[3]-contemp[2])/baseData[3]<=0.035)
				tt=22;
			else if((baseData[3]-contemp[2])/baseData[3]<=0.07)
				tt=98;
			else
				tt=99;
		}
	}
}
public void computeDailyK(Sheet s,String name,String buytime,ArrayList<double[]> tt,double[] base,String[] buyday)
{	
	try{
		Cell c=s.findCell(buytime);
		if (c==null)
			return;
		int row=c.getRow();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd"); 
		int day=0;
		double[] ktype;
		
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(format.parse(s.getCell(0,row).getContents()));  
		int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
		long sundayTime;
		
		sundayTime=format.parse(s.getCell(0,row).getContents()).getTime()+(8-dayOfWeek)*(24*60*60*1000);
		
		do
		{
			buyday[day]=s.getCell(0,row+day).getContents();
			
			ktype=new double[7];
			ktype[0]=Double.parseDouble(s.getCell(1,row).getContents());
			if(day>=1)
			{
				if(Double.parseDouble(s.getCell(2,row+day).getContents())>tt.get(day-1)[1])
					ktype[1]=Double.parseDouble(s.getCell(2,row+day).getContents());
				else
					ktype[1]=tt.get(day-1)[1];
				if(Double.parseDouble(s.getCell(3,row+day).getContents())<tt.get(day-1)[2])
					ktype[2]=Double.parseDouble(s.getCell(3,row+day).getContents());
				else
					ktype[2]=tt.get(day-1)[2];
				
				ktype[6]=Double.parseDouble(s.getCell(9,row+day).getContents())+tt.get(day-1)[6];
			}
			else
			{
				ktype[1]=Double.parseDouble(s.getCell(2,row+day).getContents());
				ktype[2]=Double.parseDouble(s.getCell(3,row+day).getContents());
				
				ktype[6]=Double.parseDouble(s.getCell(9,row+day).getContents());
			}
			ktype[3]=Double.parseDouble(s.getCell(4,row+day).getContents());
			
			ktype[4]=(ktype[3]-base[3])/4+base[4];
			ktype[5]=(ktype[3]-base[3])/13+base[5];
			
			
			day++;
			tt.add(ktype);
		}
		while((row+day)<s.getRows()&&format.parse(s.getCell(0,row+day).getContents()).getTime()<sundayTime);
	}
	catch(Exception e)
	{
		System.out.println("computeDailyK "+name+" "+buytime);
		e.printStackTrace();
	}
}
public double computeEnterPoint(ArrayList<double[]> base,double[] compare)
{
	double keypoint;
	
	//keypoint=divideWeeklyrate/100*base.get(base.size()-1)[3]+base.get(base.size()-1)[3];
	keypoint=1.06*base.get(base.size()-1)[3];

	while(keypoint<compare[3])
	{
		if(isTurnQuarterLine(base,compare,keypoint))//站上季線		
		if(isTurnMonthLine(base,compare,keypoint))//站上月線		
		if (kType(0,base,compare,keypoint))
		{
			break;
		}
		keypoint=keypoint+stockPriceUnit(keypoint);
		
	}
	
	keypoint*=1.03;
	
	if(keypoint>compare[3])
		keypoint=compare[3];
	
	return keypoint;
}
public void locateStockDailyDate(String filepath,String name,ArrayList<String> as,ArrayList<double[]> mdata)
{
	int basePosition=15;
	int count=0;
	
	try{
		Workbook workBook=Workbook.getWorkbook(new File(filepath+name+".xls"));
		WritableWorkbook writeBook=Workbook.createWorkbook(new File(filepath+name+".xls"),workBook);
		WritableSheet sss=writeBook.getSheet(0);
		
		for (int i=0;i<as.size();i++)
		{
			//System.out.print("\n "+as.get(i));
			
			Cell c=workBook.getSheet(0).findCell(as.get(i));
			if (c!=null)
			{
				int row=c.getRow();
				//System.out.print("\n "+row);
				
				sss.addCell(new Number(basePosition,count+1,row));
				count++;
								
				sss.addCell(new Number(basePosition,row,mdata.get(i)[0]));
				sss.addCell(new Number(basePosition+1,row,mdata.get(i)[1]));
				sss.addCell(new Number(basePosition+2,row,mdata.get(i)[2]));
				sss.addCell(new Number(basePosition+3,row,mdata.get(i)[3]));
				sss.addCell(new Number(basePosition+4,row,mdata.get(i)[4]));
				sss.addCell(new Number(basePosition+5,row,mdata.get(i)[5]));
				sss.addCell(new Number(basePosition+6,row,mdata.get(i)[6]));
					
			}	
		}
		
		sss.addCell(new Number(basePosition,0,count));
		sss.addCell(new Number(basePosition,count+1,workBook.getSheet(0).getRows()));
		
		writeBook.write();
		writeBook.close();
		workBook.close();
	}
	catch (Exception e)
	{
		System.out.print("\n locateStockDate");
		e.printStackTrace();
	}
}
public void locateStockWeeklyDate(String filepath,String name,ArrayList<String> as)
{
	int basePosition=15;
	int count=0;
	
	try{
		Workbook workBook=Workbook.getWorkbook(new File(filepath+name+".xls"));
		WritableWorkbook writeBook=Workbook.createWorkbook(new File(filepath+name+".xls"),workBook);
		WritableSheet sss=writeBook.getSheet(1);
		
		for (int i=0;i<as.size();i++)
		{			
			Cell c=workBook.getSheet(1).findCell(as.get(i));
			if (c!=null)
			{
				int row=c.getRow();
				
				sss.addCell(new Number(basePosition,count+1,row));
				count++;					
			}	
		}
		
		sss.addCell(new Number(basePosition,0,count));
		sss.addCell(new Number(basePosition,count+1,workBook.getSheet(1).getRows()));
		
		writeBook.write();
		writeBook.close();
		workBook.close();
	}
	catch (Exception e)
	{
		System.out.print("\n locateStockDate");
		e.printStackTrace();
	}
}
public void computeReturnQDay(File f,ArrayList<String[]> allTimePoint)
{
	int basePosition=15;
	double[] basedata=new double[7];
	
	try{
		Workbook workBook=Workbook.getWorkbook(f);
		Sheet s=workBook.getSheet(0);		
		Cell c=s.getCell(basePosition,0);
		
		for (int i=0;i<Integer.parseInt(c.getContents());i++)
		{			
			int row=Integer.parseInt(s.getCell(basePosition,i+1).getContents());
			int nextrow=Integer.parseInt(s.getCell(basePosition,i+2).getContents());
			
			basedata[0]=Double.parseDouble(s.getCell(basePosition,row).getContents());
			basedata[1]=Double.parseDouble(s.getCell(basePosition+1,row).getContents());
			basedata[2]=Double.parseDouble(s.getCell(basePosition+2,row).getContents());
			basedata[3]=Double.parseDouble(s.getCell(basePosition+3,row).getContents());
			basedata[4]=Double.parseDouble(s.getCell(basePosition+4,row).getContents());
			basedata[5]=Double.parseDouble(s.getCell(basePosition+5,row).getContents());
			basedata[6]=Double.parseDouble(s.getCell(basePosition+6,row).getContents());
			
			double[] returnv=new double[3];
			returnv[0]=basedata[3];
			returnv[1]=basedata[3];
			returnv[2]=basedata[3];
			
			double predictpoint=basedata[5];
			String[] tempdata={"","","","","","","","","","","","","","","","","",""};
			DecimalFormat df=new DecimalFormat("#.##");
			
			double computepoint=predictpoint*1.03;
			if (computepoint>basedata[3])
				computepoint=basedata[3];
			//computepoint=predictpoint;
			
			returnv[0]=returnv[2]=basedata[1];
				
			if (endComputeReturnQDay(s,row,nextrow,basedata,predictpoint,returnv,tempdata,computepoint))
			{
				tempdata[2]=""+df.format(100*(returnv[2]-computepoint)/computepoint);
				tempdata[3]=""+df.format(100*(returnv[0]-computepoint)/computepoint);	
			}

			tempdata[0]=f.getName();
			tempdata[1]=s.getCell(0,row).getContents();
			tempdata[6]=""+basedata[4];
			tempdata[7]=""+df.format(basedata[6]);
			tempdata[8]=""+df.format(100*(basedata[1]-basedata[3])/basedata[3]);
			tempdata[9]=""+df.format(100*(computepoint-returnv[1])/computepoint);
			//tempdata[10]=""+predictpoint;
			//tempdata[10]=""+df.format(100*(Double.parseDouble(tempdata[15])-returnv[1])/Double.parseDouble(tempdata[15]));
			//tempdata[12]=""+df.format(basedata[6]-100*(predictpoint-100*basedata[3]/(basedata[6]+100))/(100*basedata[3]/(basedata[6]+100)));
			
			/*SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
			if (!tempdata[4].equals(""))
			tempdata[13]=""+(format.parse(tempdata[4]).getTime()-format.parse(tempdata[1]).getTime())/(24*60*60*1000);
			if (!tempdata[5].equals(""))
			tempdata[14]=""+(format.parse(tempdata[5]).getTime()-format.parse(tempdata[1]).getTime())/(24*60*60*1000);*/
			
			allTimePoint.add(tempdata);
		}
	}
	catch (Exception e)
	{
		System.out.print("\n computeReturnQDay");
		e.printStackTrace();
	}
	
	
}
public boolean endComputeReturnQDay(Sheet s,int row,int nextrow,double[] basedata,double predictpoint,double[] returnv,String[] tempdata,double compoint)
{						
	String date="";
	int day=1,now10=0,gg10=0,endbenefit=0;
	double[] contemp=new double[9],previoustemp=new double[9];
	double[] previousreturnv=new double[3];
		
	try{
		previoustemp[0]=Double.parseDouble(s.getCell(1,row).getContents());
		previoustemp[1]=Double.parseDouble(s.getCell(2,row).getContents());
		previoustemp[2]=Double.parseDouble(s.getCell(3,row).getContents());
		previoustemp[3]=Double.parseDouble(s.getCell(4,row).getContents());
		previoustemp[4]=Double.parseDouble(s.getCell(5,row).getContents());
		previoustemp[5]=Double.parseDouble(s.getCell(6,row).getContents());
		previoustemp[6]=Double.parseDouble(s.getCell(7,row).getContents());
		previoustemp[7]=Double.parseDouble(s.getCell(8,row).getContents());
		previoustemp[8]=Double.parseDouble(s.getCell(9,row).getContents());
		
		{
			tempdata[11]=previoustemp[8]/Double.parseDouble(s.getCell(9,row-1).getContents())+"";
		}
		
		}
		catch (Exception e)
		{			
			//System.out.print("\n computeReturnQDay");
			//e.printStackTrace();
			return false;
		}
	
	while (row+day<nextrow)
	{		
		date=s.getCell(0,row+day).getContents();
		
		System.arraycopy(returnv, 0, previousreturnv, 0, returnv.length);
		
		try{
		contemp[0]=Double.parseDouble(s.getCell(1,row+day).getContents());
		contemp[1]=Double.parseDouble(s.getCell(2,row+day).getContents());
		contemp[2]=Double.parseDouble(s.getCell(3,row+day).getContents());
		contemp[3]=Double.parseDouble(s.getCell(4,row+day).getContents());
		contemp[4]=Double.parseDouble(s.getCell(5,row+day).getContents());
		contemp[5]=Double.parseDouble(s.getCell(6,row+day).getContents());
		contemp[6]=Double.parseDouble(s.getCell(7,row+day).getContents());
		contemp[7]=Double.parseDouble(s.getCell(8,row+day).getContents());
		contemp[8]=Double.parseDouble(s.getCell(9,row+day).getContents());
		
		if (day==1)
		{
			tempdata[12]=contemp[8]/previoustemp[8]+"";
		}
		}
		catch (Exception e)
		{			
			//System.out.print("\n computeReturnQDay");
			//e.printStackTrace();
			break;
		}
				
		if(contemp[0]>contemp[3])
		{
			if (contemp[1]>returnv[0])
			{
				updateHigh(contemp,returnv,tempdata,date,endbenefit);	
			}
			if (contemp[2]<returnv[1])			
			{							
				updateLow(contemp,returnv,compoint);
			}	
			if (stopLoss(compoint,contemp[2]))
			{
				tempdata[9]="-1";
				tempdata[5]=date;
				
				return true;
			}
		}
		else
		{
			if (contemp[2]<returnv[1])			
			{							
				updateLow(contemp,returnv,compoint);
			}	
			if (stopLoss(compoint,contemp[2]))
			{
				tempdata[9]="-1";
				tempdata[5]=date;
				
				return true;
			}
			if (contemp[1]>returnv[0])
			{
				updateHigh(contemp,returnv,tempdata,date,endbenefit);		
			}			
		}
		
		if (gg10==0)
		{			
			if ((returnv[0]-compoint)/compoint>=0.1)
			{
				tempdata[4]=date;
				gg10=1;
				now10=1;
				
				if (tempdata[9].equals(""))
					tempdata[9]="1";
			}
		}
		
		if (endbenefit==0)
		if (stopBenefit(gg10,now10,contemp,previoustemp,returnv,previousreturnv,tempdata,date,basedata))
		{
			tempdata[5]=date;
			endbenefit=1;
			//return true;
		}
		
		if(stopCompute(contemp,s,tempdata,gg10,row,day))					
		{
			//tempdata[9]="0";
			
			return true;
		}							
		
		System.arraycopy(contemp, 0, previoustemp, 0, contemp.length);
		day++;		
	}		
	
	return false;
}
public void computeReturnMDay(File f,ArrayList<String[]> allTimePoint)
{
	int basePosition=15;
	double[] contemp=new double[7];
	ArrayList<double[]> content=new ArrayList<double[]>();
	DecimalFormat df=new DecimalFormat("#.##");
	int isComputeReturn=0; //0:exit 1:enter
	double[] baseData=new double[7];
	double enterPoint=0;
	double weeklyrate,lead,quantity;
	double[] returnv=new double[3];
	String buytime="";
	
	
	try{
		Workbook workBook=Workbook.getWorkbook(f);
		Sheet s=workBook.getSheet(1);		
		Cell c=s.getCell(basePosition,0);
		
		for (int i=0;i<Integer.parseInt(c.getContents());i++)
		{								
			int day=0;
			isComputeReturn=0;
			content=new ArrayList<double[]>();			
			String[] tempdata={"","","","","","","","","","","","","","","","","",""};
			int row=Integer.parseInt(s.getCell(basePosition,i+1).getContents());
			int nextrow=Integer.parseInt(s.getCell(basePosition,i+2).getContents());	
			
			
			contemp=new double[7];
			contemp[0]=Double.parseDouble(s.getCell(1,row-2).getContents());
			contemp[1]=Double.parseDouble(s.getCell(2,row-2).getContents());
			contemp[2]=Double.parseDouble(s.getCell(3,row-2).getContents());
			contemp[3]=Double.parseDouble(s.getCell(4,row-2).getContents());
			contemp[4]=Double.parseDouble(s.getCell(5,row-2).getContents());
			contemp[5]=Double.parseDouble(s.getCell(6,row-2).getContents());
			contemp[6]=Double.parseDouble(s.getCell(7,row-2).getContents());
			content.add(contemp);
			contemp=new double[7];
			contemp[0]=Double.parseDouble(s.getCell(1,row-1).getContents());
			contemp[1]=Double.parseDouble(s.getCell(2,row-1).getContents());
			contemp[2]=Double.parseDouble(s.getCell(3,row-1).getContents());
			contemp[3]=Double.parseDouble(s.getCell(4,row-1).getContents());
			contemp[4]=Double.parseDouble(s.getCell(5,row-1).getContents());
			contemp[5]=Double.parseDouble(s.getCell(6,row-1).getContents());
			contemp[6]=Double.parseDouble(s.getCell(7,row-1).getContents());
			content.add(contemp);
			
			
			while(row+day<nextrow)
			{
				contemp=new double[7];
				contemp[0]=Double.parseDouble(s.getCell(1,row+day).getContents());
				contemp[1]=Double.parseDouble(s.getCell(2,row+day).getContents());
				contemp[2]=Double.parseDouble(s.getCell(3,row+day).getContents());
				contemp[3]=Double.parseDouble(s.getCell(4,row+day).getContents());
				contemp[4]=Double.parseDouble(s.getCell(5,row+day).getContents());
				contemp[5]=Double.parseDouble(s.getCell(6,row+day).getContents());
				contemp[6]=Double.parseDouble(s.getCell(7,row+day).getContents());
					
				if (content.size()>=1)
				{
					try {	
						if (isComputeReturn==0)
						{						
							if (Double.parseDouble(s.getCell(5,row+day-2).getContents())>content.get(content.size()-1)[4]&&contemp[4]>=content.get(content.size()-1)[4])
							{
								if(conditionAnalyzeMM(content,contemp))							
								{										
									isComputeReturn=1;
									baseData=contemp;
									enterPoint=contemp[3];
									
									buytime=s.getCell(0,row+day).getContents();
									if (content.get(content.size()-1)[3]!=0)
									weeklyrate=(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									else
										weeklyrate=1;
									lead=(contemp[1]-contemp[3])/contemp[3]*100;
									quantity=contemp[6];	
									
									double pre4kcloseprice=Double.parseDouble(s.getCell(4,row+day-4).getContents());
									if (predict==1)
									{
										enterPoint=enterPoint>pre4kcloseprice*1.03?pre4kcloseprice*1.03:enterPoint;
									}
									returnv[0]=enterPoint;
									returnv[1]=enterPoint;
									returnv[2]=enterPoint;
									
									tempdata[0]=f.getName();
									tempdata[1]=buytime;
									tempdata[6]=""+quantity;
									tempdata[7]=""+df.format(weeklyrate);
									tempdata[8]=""+df.format(lead);
								}
							}														
						}
						else
						{							
							if (endComputeReturnMDay(content,contemp,returnv,enterPoint,tempdata,s.getCell(0,row+day).getContents()))
							{
								tempdata[2]=""+df.format(100*(returnv[2]-enterPoint)/enterPoint);
								tempdata[3]=""+df.format(100*(returnv[0]-enterPoint)/enterPoint);	
								
								allTimePoint.add(tempdata);
																								
								isComputeReturn=0;
															
								tempdata=new String[18];
								for (int l=0;l<tempdata.length;l++)
								{
									tempdata[l]="";
								}
							}	
							if(contemp[5]<content.get(content.size()-1)[5])//季線向下
								if (100*(returnv[0]-enterPoint)/enterPoint>=10)
									break;
						}
					}	
					catch (Exception e)
					{
						System.out.print("computeReturnMDay");
						e.printStackTrace();
					}
				}
								
				day++;
				content.add(contemp);
				
			}		
			
		}
	}
	catch (Exception e)
	{
		System.out.println("computeReturnMDay "+f.getName()+" "+buytime);
		e.printStackTrace();
	}	
}
public boolean endComputeReturnMDay(ArrayList<double[]> content,double[] contemp,double[] returnv,double compoint,String[] tempdata,String date)
{						
	int now10=0,gg10=0,endbenefit=0;
		
		//System.arraycopy(returnv, 0, previousreturnv, 0, returnv.length);
						
		if(contemp[0]>contemp[3])
		{
			if (contemp[1]>returnv[0])
			{
				updateHigh(contemp,returnv,tempdata,date,endbenefit);	
			}
			/*if (contemp[2]<returnv[1])			
			{							
				updateLow(contemp,returnv,compoint);
			}*/	
			if (stopLossM(compoint,contemp[2]))
			{
				tempdata[9]="-1";
				tempdata[5]=date;
				
				return true;
			}
		}
		else
		{
			/*if (contemp[2]<returnv[1])			
			{							
				updateLow(contemp,returnv,compoint);
			}	*/
			if (stopLossM(compoint,contemp[2]))
			{
				tempdata[9]="-1";
				tempdata[5]=date;
				
				return true;
			}
			if (contemp[1]>returnv[0])
			{
				updateHigh(contemp,returnv,tempdata,date,endbenefit);		
			}			
		}
		
		if (gg10==0)
		{			
			if ((returnv[0]-compoint)/compoint>=0.1)
			{
				if (tempdata[4].equals(""))
				tempdata[4]=date;
				gg10=1;
				now10=1;
				
				if (tempdata[9].equals(""))
					tempdata[9]="1";
			}
		}
		
		/*if (endbenefit==0)
		if (stopBenefit(gg10,now10,contemp,returnv,tempdata,date))
		{
			tempdata[5]=date;
			endbenefit=1;
			//return true;
		}*/
		
		if(stopComputeM(content,contemp,gg10))					
		{
			//tempdata[9]="0";
			tempdata[5]=date;
			return true;
		}							
			
	return false;
}
private void updateHigh(double[] contemp,double[] returnv,String[] tempdata,String date,int endbenefit)
{
	if (endbenefit==0)
	returnv[0]=contemp[1];

	returnv[2]=contemp[1]>returnv[2]?contemp[1]:returnv[2];			
}
private void updateLow(double[] contemp,double[] returnv,double compoint)
{					
	if ((returnv[2]-compoint)/compoint<0.1)
		returnv[1]=contemp[2];
	
}
private boolean stopLossM(double base,double comp)
{
	if ((base-comp)/base>0.12)//拉回超過12%
	{
		return true;
	}
	
	return false;
}
private boolean stopComputeM(ArrayList<double[]> content,double[] contemp,int gg10)
{
	if(contemp[4]<content.get(content.size()-1)[4])//月線向下
	{
		if (gg10==1)//高點漲超過10%
		{
			return true;
		}
	}
	
	return false;
}
private boolean stopLoss(double base,double comp)
{
	if ((base-comp)/base>0.13)//拉回超過13% 停損出場
		//if ((predictpoint-contemp[2])/predictpoint>0.1)
		{
			return true;
		}
	
	return false;
}
private boolean stopCompute(double[] contemp,Sheet s,String[] tempdata,int gg10,int row,int day)
{
	if (gg10==1)//高點漲超過10%
	{
		if(contemp[6]<Double.parseDouble(s.getCell(7,row+day-1).getContents())&&Double.parseDouble(s.getCell(7,row+day-1).getContents())<Double.parseDouble(s.getCell(7,row+day-2).getContents()))//月趨向下
		{
			return true;						
		}
		if(contemp[7]<Double.parseDouble(s.getCell(8,row+day-1).getContents()))//季線向下
		{
			return true;							
		}
	}
		
	return false;
}
private boolean stopBenefit(int gg10,int now10,double[] contemp,double[] previoustemp,double[] returnv,double previousreturnv[],String[] tempdata,String date,double[] basedata)
{
	if (gg10==1)//高點漲超過10%		
	{
		if (contemp[0]>=previoustemp[3]*1.03)
		{
			if(contemp[2]<contemp[0]*0.97)
			//if(contemp[2]<previoustemp[3])
			{
				//returnv[0]=previoustemp[3];
				returnv[0]=contemp[0]*0.97;
				tempdata[9]="82";
				
				return true;
			} 
		}

		if (now10==1)
		{
			if (contemp[0]>=contemp[3])
			{
				if(contemp[2]<returnv[0]*0.97)
				{
					returnv[0]=returnv[0]*0.97;
					tempdata[9]="111";
				
					return true;
				}
			}
			else
			{
				if(contemp[3]<returnv[0]*0.97)
				{
					returnv[0]=contemp[3];
					tempdata[9]="112";
				
					return true;
				}
			}
			
			now10=0;
		}
		else
		{
			if(contemp[2]<previousreturnv[0]*0.97)
			{
				if(contemp[0]<previousreturnv[0]*0.97)
				{
					returnv[0]=contemp[0];
					tempdata[9]="211";
					
					return true;
				} 
				else
				{
					returnv[0]=previousreturnv[0]*0.97;
					tempdata[9]="212";
				
					return true;
				}
			}
		}			
	}
	
	return false;
}
public double stockPriceUnit(double price)
{
	if (price<10)
		return 0.01;
	if (price>=10&&price<50)
		return 0.05;
	if (price>=50&&price<100)
		return 0.1;
	if (price>=100&&price<500)
		return 0.5;
	if (price>=500&&price<1000)
		return 1;
	if (price>=1000)
		return 5;
	
	return 99;
}
private void updateExcel(File dir)//update excel data format
{
	File[] temp=dir.listFiles();
	for (int k=0;k<temp.length;k++)
	{
		updateExcel(temp[k].getName());
	}
}
public void updateExcel(String name)
{
	try{
		Workbook workBook=Workbook.getWorkbook(new File(drive+"software/sdata/ttt/temp/"+name));
		Sheet basics=workBook.getSheet(0);
		WritableWorkbook writeBook=Workbook.createWorkbook(new File(drive+"software/sdata/tttnew/"+name),workBook);
		WritableSheet sss=writeBook.getSheet(0);

		int basicrow=sss.getColumn(0).length;

		for (int i=1;i<basicrow;i++)
		{	
			for (int j=0;j<8;j++)
			{
				sss.addCell(new Label(j, i, basics.getCell(j, basicrow-i).getContents()));
				
				if (j==7)
				{
					sss.addCell(new Label(j, i, basics.getCell(j, basicrow-i).getContents().replaceAll(",","")));
				}
			}
		}

		sss.removeColumn(9);
		sss.removeColumn(8);
		sss.removeColumn(6);
		sss.removeColumn(5);
		
		sss.insertColumn(5);
		sss.addCell(new Label(5,0,"SMA60"));
		sss.insertColumn(5);
		sss.addCell(new Label(5,0,"SMA20"));
		sss.insertColumn(5);
		sss.addCell(new Label(5,0,"SMA10"));
		sss.insertColumn(5);
		sss.addCell(new Label(5,0,"SMA5"));
		
		double temp=0;
		BigDecimal b1,b2;
		for (int i=5;i<basicrow;i++)
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
				//System.out.println(temp+" "+b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue());
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
		
		sss.setName(name);
		
		writeBook.write();
		writeBook.close();
		workBook.close();
	}
	catch (Exception e)
	{
		System.out.print("\n updateExcel"+" "+name);
		e.printStackTrace();
	}
}
}
class analyzeThread extends Thread {
	analyzeStock ss;
	Sheet sh;
	ArrayList<String[]> allTimePoint;
	int filetype;
	String name;
	
	public analyzeThread(String name,analyzeStock ss,Sheet sh,int filetype,ArrayList<String[]> allTimePoint) {
        this.name=name;
		this.ss=ss;
        this.sh=sh;
        this.allTimePoint=allTimePoint;
        this.filetype=filetype;
    }

    public void run() {
    	

    }
}
class testRegular {

	public static boolean isfloatNum(String inputStr)
	{
		Pattern ptn = Pattern.compile("^-?[0-9]+(\\.[0-9]+)?$");  
		Matcher matcher = ptn.matcher(inputStr);
		
		return matcher.find();
	}
}