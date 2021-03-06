
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

public class bullStrategy11 {

	final int quarterKCount=13;
	final double divideWeeklyrate=0.09;
		
	public String strategyName()
	{
		return "quaterline+middle of the week";
	}
	public void analyzeStock(Sheet sweek,Sheet shfile,ArrayList<String[]> allTimePoint,String filepath,String stockname)
	{	
		int isComputeReturn=0; //0:exit 1:enter
		double[] contemp=new double[7];
		double currentHigh=0,currentLow=0,enterPoint=0;
		ArrayList<double[]> content=new ArrayList<double[]>();
		DecimalFormat df=new DecimalFormat("#.##");
		String buytime="";
		double weeklyrate,lead,quantity,highWeeklyrate=0;
		String[] buyday=new String[7];
		double[] returnV=new double[2];
		int[] kstate=new int[4]; //[0]:quarterline red count [1]:monthline red count [2]:if last day [3]:days of the week
		double[] entrypoint=new double[7];
		String[] tempdata=null;
		ArrayList<String> as=new ArrayList<String>(),buytimeAll=new ArrayList<String>();
		ArrayList<double[]> mdata=new ArrayList<double[]>();

		kstate[0]=0;

		try {			
			int temp=1;
			while (temp<sweek.getRows())
			{
				contemp=new double[7];
				
				for (int j=0;j<7;++j)
				{
					String st=sweek.getCell(j+1,temp).getContents();
					if (!st.equals(""))
						contemp[j]=Double.parseDouble(st);
					else
						contemp[j]=0;
				}

				if (content.size()>=quarterKCount)
				{
					if (isComputeReturn==0)
					{
						if(findEntryPoint(sweek,shfile,content,contemp,temp,kstate,entrypoint,buyday))
						{
							isComputeReturn=1;									
							enterPoint=entrypoint[3];									
							currentHigh=entrypoint[3];
							currentLow=entrypoint[3];
							buytime=sweek.getCell(0,temp).getContents();
							weeklyrate=(entrypoint[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
							lead=(entrypoint[1]-entrypoint[3])/entrypoint[3]*100;
							quantity=entrypoint[6];
							
							double testpoint=1;
							{
								//highWeeklyrate=(entrypoint[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
								
								//if(weeklyrate>=6&&highWeeklyrate>=divideWeeklyrate)
								{		
									//testpoint=computeEnterPoint(content,entrypoint);
									//enterPoint=computeEnterPoint(content,entrypoint);
									//currentLow=enterPoint;
									//currentHigh=entrypoint[1];;
								}
							}

							tempdata=new String[]{stockname,buyday[kstate[2]],"",""+(entrypoint[1]-testpoint)/testpoint,"","",quantity+"",df.format(weeklyrate),df.format(lead),"","","","","","","","","","1"};				

							returnV[0]=currentHigh;
							returnV[1]=currentLow;
							
							if(kstate[2]+1<kstate[3])
							{	
								if (endComputeReturn(shfile,sweek.getName(),sweek.getCell(0,temp).getContents(),enterPoint,returnV,kstate[2]+1))
								{
									tempdata[2]=""+df.format(100*(returnV[0]-enterPoint)/enterPoint);
									tempdata[9]=""+df.format(100*(enterPoint-returnV[1])/enterPoint);

									allTimePoint.add(tempdata);
									isComputeReturn=0;
									kstate[0]=0;
								}
							}
						}
					}
					else
					{							
						if (endComputeReturn(shfile,sweek.getName(),sweek.getCell(0,temp).getContents(),enterPoint,returnV,0))
						{	
							tempdata[2]=""+df.format(100*(returnV[0]-enterPoint)/enterPoint);
							tempdata[9]=""+df.format(100*(enterPoint-returnV[1])/enterPoint);

							allTimePoint.add(tempdata);
							isComputeReturn=0;
							kstate[0]=0;
						}
					}
				}

				temp++;
				content.add(contemp);			
			}
		}
		catch (Exception e)
		{
			System.out.print("\nanalyzeStockResult"+","+sweek.getName());
			e.printStackTrace();
		}
	}
	private boolean findEntryPoint(Sheet sweek,Sheet shfile,ArrayList<double[]> content,double[] contemp,int temp,int[] kstate,double[] entrypoint,String[] buyday)
	{		
		if (kstate[0]==0)
		{	
			if (content.get(content.size()-1)[5]<=content.get(content.size()-2)[5])
			{
				if (contemp[5]>=content.get(content.size()-1)[5])
				{
					kstate[0]=1;
				}
				else
				{
					if ((contemp[1]-contemp[3])/13+contemp[5]>=content.get(content.size()-1)[5])
						if ((contemp[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]>=divideWeeklyrate)
						{							
							if (isfindEntry(sweek,shfile,content,contemp,temp,kstate,entrypoint,buyday))
								return true;							
						}
				}
			}		
		}

		if (kstate[0]>=1&&kstate[0]<=8)
		{
			if (contemp[5]>=content.get(content.size()-1)[5])
			{		
				if ((contemp[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]>=divideWeeklyrate)
				{
					if (isfindEntry(sweek,shfile,content,contemp,temp,kstate,entrypoint,buyday))
						return true;
				}
				kstate[0]++;
			}
			else
			{
				if ((contemp[1]-contemp[3])/13+contemp[5]>=content.get(content.size()-1)[5])
				{
					if ((contemp[1]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]>=divideWeeklyrate)
					{							
						if (isfindEntry(sweek,shfile,content,contemp,temp,kstate,entrypoint,buyday))
							return true;							
					}
				}
					
				kstate[0]=0;
			}
		}
		else if(kstate[0]>=9) 
		{
			if (contemp[5]>content.get(content.size()-1)[5])
				kstate[0]++;
			else
				kstate[0]=0;
		}
		
		return false;
	}
	private boolean isfindEntry(Sheet sweek,Sheet shfile,ArrayList<double[]> content,double[] contemp,int temp,int[] kstate,double[] entrypoint,String[] buyday)
	{
		int day=0;
		ArrayList<double[]> combineK=new ArrayList<double[]>();
		ArrayList<double[]> currentK=new ArrayList<double[]>();
		
		computeDailyK(shfile,sweek.getCell(0,temp).getContents(),combineK,contemp,buyday,currentK);
		
		/*ArrayList<double[]> ttt=new ArrayList<double[]>();
		String[] buyday1=new String[7];
		ArrayList<double[]> currentKKK=new ArrayList<double[]>();
		computeDailyKKK(shfile,s.getCell(0,temp).getContents(),ttt,contemp,buyday1,currentKKK);
		double enterPoint=0;*/
		
		if(combineK.size()!=0)
			do{	
				/*if(conditionAnalyzeKKK(content,ttt.get(day),ttt.get(day)[3]))
				{
					enterPoint=computeEnterPointKKK(content,ttt.get(day));
					
					if(((ttt.get(day)[1]-enterPoint)/content.get(content.size()-1)[3])>=0.05)
					{
						if (day==0)
						{
							if (currentKKK.get(day)[0]<=content.get(content.size()-1)[3]*1.05)
							{
								test1++;
								System.out.println("1:"+buyday1[day]+" "+shfile.getName());
							}
						}
						else
						{
							if (currentKKK.get(day)[0]<=currentKKK.get(day-1)[3]*1.05)
							{
								test1++;
								System.out.println("1:"+buyday1[day]+" "+shfile.getName());
							}
						}
						
					}
				}*/
				if (conditionAnalyze(content,combineK.get(day),combineK.get(day)[3],currentK.get(day)))
				{
					System.arraycopy(combineK.get(day), 0, entrypoint, 0, entrypoint.length);
					
					/*if(((ttt.get(day)[1]-enterPoint)/content.get(content.size()-1)[3])>=0.05)
					{
						if (day==0)
						{
							if (currentKKK.get(day)[0]<=content.get(content.size()-1)[3]*1.05)
							{
								test1--;
								//System.out.println("2:"+buyday[day]+" "+shfile.getName());
								System.out.println("2:");
							}
						}
						else
						{
							if (currentKKK.get(day)[0]<=currentKKK.get(day-1)[3]*1.05)
							{
								test1--;
								//System.out.println("2:"+buyday[day]+" "+shfile.getName());
								System.out.println("2:");
							}
						}
						
					}*/
					
					/*kstate[1]=1;
					
					for (int i=content.size()-1;i>=1;i--)
					{
						if (content.get(i)[4]>content.get(i-1)[4])
							kstate[1]+=1;
						else
							break;
					}*/
					kstate[2]=day;
					kstate[3]=combineK.size();
					return true;									
				}
				day++;
			}
			while(day<combineK.size());
		
		return false;
	}
	private boolean conditionAnalyze(ArrayList<double[]> base,double[] compare,double enterPoint,double[] currentk)
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
					if (kType(0,base,compare,enterPoint))
						return true;
				}

		return false;
	}
	private boolean isTurnQuarterLine(ArrayList<double[]> base,double[] compare,double enterPoint)
	{//System.out.println("isTurnQuarterLine");
		double currentQline;

		currentQline=((enterPoint-compare[3])/13+compare[5]);

		if(enterPoint>currentQline)//站上季線
			if((enterPoint-currentQline)>=(currentQline-compare[0]))//k棒明顯突破季線
			{			
				if ((currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5]>=0.0)
				{
					return true;
				}

				/*if ((currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5]>=0.001)//翻轉超過0.001 ***
				{
					return true;
				}
				else
				{
					if (compare[3]==compare[1])//收最高
					{
						return true;
					}
				}*/	
			}
		return false;
	}
	private boolean isTurnMonthLine(ArrayList<double[]> base,double[] compare,double enterPoint)
	{//System.out.println("isTurnMonthLine");
		double currentMline;
		double currentQline;

		currentQline=((enterPoint-compare[3])/13+compare[5]);
		currentMline=((enterPoint-compare[3])/4+compare[4]);

		if(enterPoint>currentMline)//站上月線
			if((enterPoint-currentMline)>=(currentMline-compare[0]))//k棒明顯突破月線
			{
				if((currentMline-base.get(base.size()-1)[4])/base.get(base.size()-1)[4]>=(currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5])
				{
					return true;
				}
			}

		return false;
	}
	private int weeklyRateType(ArrayList<double[]> base,double[] compare)
	{	//System.out.println("isweeklyRateType");
		{
			if ((compare[1]-base.get(base.size()-1)[3])/base.get(base.size()-1)[3]>=divideWeeklyrate)
				return 1;
		}
			

		return 0;
	}
	private boolean kType(int quantityType,ArrayList<double[]> base,double[] compare,double enterPoint)
	{
		if ((isRedK(quantityType,base,compare,enterPoint))!=0)
			return true;		

		return false;	
	}
	private int isRedK(int quantityType,ArrayList<double[]> base,double[] compare,double enterPoint)
	{//System.out.print("\nisRedK");		
		if ((enterPoint-compare[0])/compare[0]*100>=5)//紅棒區域>=5%
		{
			return 1;
		}
		else
		{
			if((compare[1] - base.get(base.size() - 1)[3]) / base.get(base.size() - 1)[3] >=0.09&&compare[1]==compare[3])
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
				}
			}	
		}
			
			return 0;
	}
	private int isOverQuantity(ArrayList<double[]> base,double[] compare)
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
	private int isOverHigh(ArrayList<double[]> base,double[] compare)
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
	private boolean endComputeReturn(Sheet s,String name,String buytime,double predictpoint,double[] returnV,int day)
	{			
		int row;	
		double[] contemp=new double[5];
		double enterPoint=predictpoint;									
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
					updateHigh(contemp[1],returnV);
					
					if(stopLoss(enterPoint,contemp[2]))
						return true;
					
					if(stopCompute(contemp[4],Double.parseDouble(s.getCell(8,row+day-1).getContents()),returnV[0],enterPoint))
						return true;
					
					updateLow(contemp[2],returnV,enterPoint);
				}
				else
				{
					updateLow(contemp[2],returnV,enterPoint);

					if(stopLoss(enterPoint,contemp[2]))
						return true;
					
					if(stopCompute(contemp[4],Double.parseDouble(s.getCell(8,row+day-1).getContents()),returnV[0],enterPoint))
						return true;

					updateHigh(contemp[1],returnV);
				}								
				day++;

				if ((row+day)<s.getRows())
					curtime=format.parse(s.getCell(0,row+day).getContents()).getTime();
			}
			catch(Exception e)
			{
				//System.out.println(row+" "+day+" "+s.getRows());
				//System.out.println(s.getName()+" "+buytime);
				System.out.println("endComputeReturnQDay ");
				e.printStackTrace();
				day++;
				continue;
			}
		}
		while((row+day)<s.getRows()&&curtime<sundayTime);
		
		return false;
	}
	private void computeDailyK(Sheet s,String buytime,ArrayList<double[]> combineK,double[] base,String[] buyday,ArrayList<double[]> currentKlist)
	{	
		try{
			Cell c=s.findCell(buytime);
			if (c==null)
				return;
			
			int row=c.getRow();
			int day=0;
			double[] ktype,currentk;
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd"); 			
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(format.parse(s.getCell(0,row).getContents()));  
			int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
			long sundayTime;

			sundayTime=format.parse(s.getCell(0,row).getContents()).getTime()+(8-dayOfWeek)*(24*60*60*1000);

			do
			{
				buyday[day]=s.getCell(0,row+day).getContents();

				ktype=new double[7];
				currentk=new double[9];
				
				ktype[0]=Double.parseDouble(s.getCell(1,row).getContents());
				if(day>=1)
				{
					if(Double.parseDouble(s.getCell(2,row+day).getContents())>combineK.get(day-1)[1])
						ktype[1]=Double.parseDouble(s.getCell(2,row+day).getContents());
					else
						ktype[1]=combineK.get(day-1)[1];
					
					if(Double.parseDouble(s.getCell(3,row+day).getContents())<combineK.get(day-1)[2])
						ktype[2]=Double.parseDouble(s.getCell(3,row+day).getContents());
					else
						ktype[2]=combineK.get(day-1)[2];

					ktype[6]=Double.parseDouble(s.getCell(9,row+day).getContents())+combineK.get(day-1)[6];
				}
				else
				{
					ktype[1]=Double.parseDouble(s.getCell(2,row).getContents());
					ktype[2]=Double.parseDouble(s.getCell(3,row).getContents());

					ktype[6]=Double.parseDouble(s.getCell(9,row).getContents());
				}
				ktype[3]=Double.parseDouble(s.getCell(4,row+day).getContents());

				ktype[4]=(ktype[3]-base[3])/4+base[4];
				ktype[5]=(ktype[3]-base[3])/13+base[5];
				
				day++;
				combineK.add(ktype);
				
				for (int i=0;i<9;i++)
				{
					if (!s.getCell(i+1,row+day).getContents().equals(""))
						currentk[i]=Double.parseDouble(s.getCell(i+1,row+day).getContents());
				}
				
				currentKlist.add(currentk);
			
			}
			while((row+day)<s.getRows()&&format.parse(s.getCell(0,row+day).getContents()).getTime()<sundayTime);
		}
		catch(Exception e)
		{
			System.out.println("computeDailyK "+" "+buytime);
			e.printStackTrace();
		}
	}
	private double computeEnterPoint(ArrayList<double[]> base,double[] compare)
	{
		double keypoint;
		
		keypoint=compare[0]*1.05;
		//keypoint=base.get(base.size()-1)[3];


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

		//keypoint*=1.03;

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
	private double stockPriceUnit(double price)
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
	private void updateHigh(double high,double[] returnv)
	{
		if (high>returnv[0])
		{		
			returnv[0]=high;	
		}	
	}
	private void updateLow(double low,double[] returnv,double enterpoint)
	{					
		if (low<returnv[1])
		{
			if ((returnv[0]-enterpoint)/enterpoint<0.07)//高點不超過7%
			{
				returnv[1]=low;		
			}				
		}
		
	}
	private boolean stopLoss(double enterpoint,double low)
	{
		if ((enterpoint-low)/enterpoint>0.13)//拉回超過13%
		{
			return true;
		}
		
		return false;
	}
	private boolean stopCompute(double curMonthline,double preMonthline,double low,double enterpoint)
	{
		if(curMonthline<preMonthline)//月線向下
		{
			if ((low-enterpoint)/enterpoint>=0.1)//高點漲超過10%
			{
				return true;
			}							
		}
			
		return false;
	}
	private boolean conditionAnalyzeKKK(ArrayList<double[]> base,double[] compare,double enterPoint)
	{	
		int weeklyRateType;
		weeklyRateType=weeklyRateType(base,compare);

		if(isTurnQuarterLine(base,compare,enterPoint))
			if(isTurnMonthLine(base,compare,enterPoint))
				if(weeklyRateType!=0)
				{				
					if (kType(0,base,compare,enterPoint))
						return true;
				}

		return false;
	}
	private void computeDailyKKK(Sheet s,String buytime,ArrayList<double[]> combineK,double[] base,String[] buyday,ArrayList<double[]> currentKlist)
	{	
		try{
			Cell c=s.findCell(buytime);
			if (c==null)
				return;
			
			int row=c.getRow();
			int day=0;
			double[] ktype,currentk;
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd"); 			
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(format.parse(s.getCell(0,row).getContents()));  
			int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
			long sundayTime;

			sundayTime=format.parse(s.getCell(0,row).getContents()).getTime()+(8-dayOfWeek)*(24*60*60*1000);

			do
			{
				buyday[day]=s.getCell(0,row+day).getContents();

				ktype=new double[7];
				currentk=new double[9];
				
				ktype[0]=Double.parseDouble(s.getCell(1,row).getContents());
				if(day>=1)
				{
					if(Double.parseDouble(s.getCell(2,row+day).getContents())>combineK.get(day-1)[1])
						ktype[1]=Double.parseDouble(s.getCell(2,row+day).getContents());
					else
						ktype[1]=combineK.get(day-1)[1];
					
					if(Double.parseDouble(s.getCell(3,row+day).getContents())<combineK.get(day-1)[2])
						ktype[2]=Double.parseDouble(s.getCell(3,row+day).getContents());
					else
						ktype[2]=combineK.get(day-1)[2];

					ktype[6]=Double.parseDouble(s.getCell(9,row+day).getContents())+combineK.get(day-1)[6];
				}
				else
				{
					ktype[1]=Double.parseDouble(s.getCell(2,row).getContents());
					ktype[2]=Double.parseDouble(s.getCell(3,row).getContents());

					ktype[6]=Double.parseDouble(s.getCell(9,row).getContents());
				}

				if (Double.parseDouble(s.getCell(1,row+day).getContents())<=Double.parseDouble(s.getCell(4,row+day-1).getContents())*1.05)
					ktype[3]=Double.parseDouble(s.getCell(2,row+day).getContents());
				else
					ktype[3]=Double.parseDouble(s.getCell(4,row+day).getContents());

				ktype[4]=(ktype[3]-base[3])/4+base[4];
				ktype[5]=(ktype[3]-base[3])/13+base[5];

				for (int i=0;i<9;i++)
				{
					currentk[i]=Double.parseDouble(s.getCell(i+1,row+day).getContents());
				}
				
				day++;
				combineK.add(ktype);
				currentKlist.add(currentk);
			
			}
			while((row+day)<s.getRows()&&format.parse(s.getCell(0,row+day).getContents()).getTime()<sundayTime);
		}
		catch(Exception e)
		{
			System.out.println("computeDailyK "+" "+buytime);
			e.printStackTrace();
		}
	}
	private double computeEnterPointKKK(ArrayList<double[]> base,double[] compare)
	{
		double keypoint;
		
		keypoint=compare[0]*1.05;
		//keypoint=base.get(base.size()-1)[3];

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

		//keypoint*=1.03;

		if(keypoint>compare[3])
			keypoint=compare[3];

		return keypoint;
	}
}




