import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jxl.Cell;
import jxl.Sheet;

public class bullStrategy22 {
	
	final int quarterKCount=13;
	final double divideWeeklyrate=9;
	int test=0;
	
	public String strategyName()
	{
		return "monthline on quaterline+middle of the week";
	}
	public void analyzeStock(Sheet sweek,Sheet sday,ArrayList<String[]> allTimePoint,String filepath,String stockname)
	{	
		int isComputeReturn=0; //0:exit 1:enter
		double[] baseData=new double[7],contemp=new double[7];
		double currentHigh=0,currentLow=0,enterPoint=0;
		ArrayList<double[]> content=new ArrayList<double[]>();
		DecimalFormat df=new DecimalFormat("#.##");
		String buytime="";
		String[] tempdata=null;		
		int quarterLineRedK=0;
		
		double[] mmstate=new double[7]; //mm=0;isComputeReturnM=0;monthLineRedTypeM=0;currentHighM=0;currentLowM=0;enterPointM=0;testHighM=0;
		for (double element:mmstate)
			element=0;
		
		ArrayList<double[]> combineK=null;
		ArrayList<double[]> currentK=null;
		String[] buyday=null;
		
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

										mmstate[0]=1;									
									}
									else
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
						{								
							if (contemp[1]>currentHigh)
							{
								currentHigh=contemp[1];								
							}

							if (endComputeReturnQ(currentHigh,baseData,contemp,content))
							{							
								isComputeReturn=0;
								quarterLineRedK=0;

								mmstate[0]=0;
							}
						}
						
						buytime=sweek.getCell(0,temp).getContents();
						analyzeStockResultByQuarterLineMM(sday,allTimePoint,buytime,contemp,content,stockname,tempdata,mmstate,combineK,currentK,buyday);
					}
					
					temp++;
					content.add(contemp);			
				}
		}
		catch (Exception e)
		{
			System.out.print("\nanalyzeStock"+","+sweek.getName());
			e.printStackTrace();
		}
	}
	private void analyzeStockResultByQuarterLineMM(Sheet sday,ArrayList<String[]> allTimePoint,String buytime,double[] contemp,ArrayList<double[]> content,String stockname,String[] tempdata,double[] mmstate,ArrayList<double[]> combineK,ArrayList<double[]> currentK,String[] buyday)
	{	
		DecimalFormat df=new DecimalFormat("#.##");
			
		try {		
					if (content.size()>=2)
					{
						if (mmstate[1]==0&&mmstate[0]==1)
						{
							if (mmstate[2]==0)
							{
								if (content.get(content.size()-1)[4]<content.get(content.size()-2)[4])
								{
									if ((contemp[1]-contemp[3])/4+contemp[4]>content.get(content.size()-1)[4])
									{							
										mmstate[2]=1;							
									}
									
								}	
							}
							if (mmstate[2]==1)
							{
								int day=0;
								combineK=new ArrayList<double[]>();
								currentK=new ArrayList<double[]>();
								buyday=new String[7];
								
								computeDailyK(sday,buytime,combineK,contemp,buyday,currentK);

								if(combineK.size()!=0)
									do{		//System.out.println(buytime);
										if (conditionAnalyzeMM(content,combineK.get(day),combineK.get(day)[3]))
										{										
											mmstate[1]=1;									
											mmstate[3]=combineK.get(day)[3];
											mmstate[4]=combineK.get(day)[3];
											mmstate[5]=combineK.get(day)[3];
											double weeklyrate=(combineK.get(day)[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
											double lead=(combineK.get(day)[1]-combineK.get(day)[3])/combineK.get(day)[3]*100;
											double quantity=combineK.get(day)[6];

											//tempdata=new String[]{stockname,buytime,"","","","",quantity+"",df.format(weeklyrate),df.format(lead),"","","","","","","","","","1"};
											fillStockData(allTimePoint,stockname,buyday[day],""+quantity,""+df.format(weeklyrate),""+df.format(lead));		
											
											if(day+1<combineK.size())
											if (endComputeReturnM(mmstate,sday,stockname,buytime,day+1))
											{
												//tempdata[2]=""+df.format(100*(currentHighM-enterPointM)/enterPointM);
												//tempdata[9]=""+df.format(100*(enterPointM-currentLowM)/enterPointM);
												fillInData(allTimePoint,2,""+df.format(100*(mmstate[3]-mmstate[5])/mmstate[5]));						
												fillInData(allTimePoint,9,""+df.format(100*(mmstate[5]-mmstate[4])/mmstate[5]));
																				
												//allTimePoint.add(tempdata);
												
												mmstate[1]=0;
												mmstate[2]=0;
											}
											
											break;
										}
										day++;
									}
									while(day<combineK.size());

								mmstate[2]=0;
							}														
						}
						else if (mmstate[1]==1)
						{		//		System.out.println("pre end "+buytime);			
							if (endComputeReturnM(mmstate,sday,stockname,buytime,0))
							{//System.out.println("end "+buytime);
								//tempdata[2]=""+df.format(100*(currentHighM-enterPointM)/enterPointM);
								//tempdata[9]=""+df.format(100*(enterPointM-currentLowM)/enterPointM);
								fillInData(allTimePoint,2,""+df.format(100*(mmstate[3]-mmstate[5])/mmstate[5]));						
								fillInData(allTimePoint,9,""+df.format(100*(mmstate[5]-mmstate[4])/mmstate[5]));
																
								//allTimePoint.add(tempdata);
								
								mmstate[1]=0;
								mmstate[2]=0;
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
	private void fillStockData(ArrayList<String[]> allTimePoint,String stockNum,String buytime,String quantity,String weeklyrate,String lead)
	{	
		String[] temp={stockNum,buytime,"","","","",quantity+"",weeklyrate,lead,"","","","","","","","","","1"};
		allTimePoint.add(temp);
	}
	private void fillInData(ArrayList<String[]> allTimePoint,int location,String firstComputeReturn)
	{
		allTimePoint.get(allTimePoint.size()-1)[location]=firstComputeReturn;
	}
	private boolean conditionAnalyzeQ(ArrayList<double[]> base,double[] compare,double enterPoint)
	{			
		if(isTurnQuarterLine(base,compare,enterPoint))
			if(isTurnMonthLine(base,compare,enterPoint))
				return true;
								
		return false;
	}
	private boolean conditionAnalyzeMM(ArrayList<double[]> base,double[] compare,double enterpoint)
	{
		double[] p1,p2;
		
		p1=base.get(base.size()-1);
		p2=base.get(base.size()-2);
		
		double currentMline;
		double currentQline;

		currentQline=((enterpoint-compare[3])/13+compare[5]);
		currentMline=((enterpoint-compare[3])/4+compare[4]);
		
		if(true)
		if((currentQline>=p1[5])&&(p1[5]>=p2[5]))//季線連2紅
		{
			//if ((p4[1]-compare[2])/p4[1]<=0.1)
			if (enterpoint>=p1[3])//收漲
			if(enterpoint>currentMline)//收在月線上
			{
				if(currentMline>p1[4])
				{
					return true;			
				}
			}
		}
									
		return false;
	}
	private boolean isTurnQuarterLine(ArrayList<double[]> base,double[] compare,double enterPoint)
	{//System.out.print("\nisTurnQuarterLine");
		
		double currentQline;
		
		currentQline=(enterPoint-compare[3])/13+compare[5];

		if(enterPoint>currentQline)//站上季線
		if((enterPoint-currentQline)>=(currentQline-compare[0]))//k棒明顯突破季線
		{				
			if ((currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5]>=0)
			{
				return true;
			}
		}
				return false;
	}
	private boolean isTurnMonthLine(ArrayList<double[]> base,double[] compare,double enterPoint)
	{//System.out.print("\nisTurnMonthLine\n");
			
		double currentMline;
		//double currentQline;
		
		//currentQline=(enterPoint-compare[3])/13+compare[5];
		currentMline=(enterPoint-compare[3])/4+compare[4];
		
		if(enterPoint>currentMline)//站上月線
		//if((enterPoint-currentMline)>=(currentMline-compare[0]))//k棒明顯突破月線
		{
			if ((currentMline-base.get(base.size()-1)[4])/base.get(base.size()-1)[4]>=0)
			{
				return true;
			}
			/*if((currentMline-base.get(base.size()-1)[4])/base.get(base.size()-1)[4]>=(currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5])
			{
				return true;
			}*/
		}

			return false;
	}
	private boolean endComputeReturnQ(double currentHigh,double[] baseData,double[] contemp,ArrayList<double[]> content)
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
	private boolean endComputeReturnM(double[] mmstate,Sheet s,String name,String buytime,int day)
	{	
		int row;									
		double curtime=0;
		double[] contemp=new double[5];
		double enterPoint=mmstate[5];		
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
				contemp[4]=Double.parseDouble(s.getCell(7,row+day).getContents());

				if (contemp[0]>=contemp[3])
				{
					updateHigh(contemp[1],mmstate);
					
					if(stopLoss(enterPoint,contemp[2]))
						return true;
					
					if(stopCompute(contemp[4],Double.parseDouble(s.getCell(7,row+day-1).getContents()),mmstate[3],enterPoint))
						return true;
					
					updateLow(contemp[2],mmstate,enterPoint);
				}
				else
				{
					updateLow(contemp[2],mmstate,enterPoint);

					if(stopLoss(enterPoint,contemp[2]))
						return true;
					
					if(stopCompute(contemp[4],Double.parseDouble(s.getCell(7,row+day-1).getContents()),mmstate[3],enterPoint))
						return true;

					updateHigh(contemp[1],mmstate);
				}								
				day++;

				if ((row+day)<s.getRows())
					curtime=format.parse(s.getCell(0,row+day).getContents()).getTime();
			}
			catch(Exception e)
			{
				System.out.println("endComputeReturnM");
				e.printStackTrace();
				day++;
				continue;
			}
		}
		while((row+day)<s.getRows()&&curtime<sundayTime);
		
		return false;
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
	private void updateHigh(double high,double[] returnv)
	{
		if (high>returnv[3])
		{		
			returnv[3]=high;	
		}	
	}
	private void updateLow(double low,double[] returnv,double enterpoint)
	{					
		if (low<returnv[4])
		{
			if ((returnv[3]-enterpoint)/enterpoint<0.07)//高點不超過7%
			{
				returnv[4]=low;		
			}				
		}
		
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
				
				/*for (int i=0;i<9;i++)
				{
					if (!s.getCell(i+1,row+day).getContents().equals(""))
						currentk[i]=Double.parseDouble(s.getCell(i+1,row+day).getContents());
				}
				
				currentKlist.add(currentk);*/
			
			}
			while((row+day)<s.getRows()&&format.parse(s.getCell(0,row+day).getContents()).getTime()<sundayTime);
		}
		catch(Exception e)
		{
			System.out.println("computeDailyK "+" "+buytime);
			e.printStackTrace();
		}
	}
}


