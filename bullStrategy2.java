import java.text.DecimalFormat;
import java.util.ArrayList;

import jxl.Sheet;

public class bullStrategy2 {
	
	final int quarterKCount=13;
	final double divideWeeklyrate=9;
	final int predict=1; //0:no predict 1:predict
	 
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
						analyzeStockResultByQuarterLineMM(sweek,allTimePoint,buytime,contemp,content,stockname,tempdata,mmstate);
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
	private void analyzeStockResultByQuarterLineMM(Sheet s,ArrayList<String[]> allTimePoint,String buytime,double[] contemp,ArrayList<double[]> content,String stockname,String[] tempdata,double[] mmstate)
	{	
		DecimalFormat df=new DecimalFormat("#.##");
			
		try {		
					if (content.size()>=2)
					{
						if (mmstate[1]==0&&mmstate[0]==1)
						{
							mmstate[6]=0;

							if (mmstate[2]==0)
							{
								if (contemp[4]>content.get(content.size()-1)[4]&&content.get(content.size()-2)[4]>content.get(content.size()-1)[4])
								{																	
									mmstate[2]=1;
								}
							}
							if (mmstate[2]==1)
							{
								if(conditionAnalyzeMM(content,contemp))							
								{										
									mmstate[1]=1;									
									mmstate[3]=contemp[3];
									mmstate[4]=contemp[3];
									mmstate[5]=contemp[3];
									double weeklyrate=(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									double lead=(contemp[1]-contemp[3])/contemp[3]*100;
									double quantity=contemp[6];

									//tempdata=new String[]{stockname,buytime,"","","","",quantity+"",df.format(weeklyrate),df.format(lead),"","","","","","","","","","1"};
									fillStockData(allTimePoint,stockname,buytime,""+quantity,""+df.format(weeklyrate),""+df.format(lead));											

									/*if (predict==1)
											{
												if(enterPointM>content.get(content.size()-4)[3]*1.03)
													enterPointM=content.get(content.size()-4)[3]*1.03;

												testHighM=contemp[1];
											}*/
								}
								else
									mmstate[2]=0;
							}														
						}
						else if (mmstate[1]==1)
						{							
							if (endComputeReturnM(mmstate,contemp,content,mmstate[5]))
							{
								//if(testHighM>currentHighM)
									//currentHighM=testHighM;

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
	private boolean conditionAnalyzeMM(ArrayList<double[]> base,double[] compare)
	{
		double[] p1,p2;
		
		p1=base.get(base.size()-1);
		p2=base.get(base.size()-2);
		
		if(true)
		if((compare[5]>=p1[5])&&(p1[5]>=p2[5]))//季線連2紅
		{
			//if ((p4[1]-compare[2])/p4[1]<=0.1)
			if (compare[3]>=p1[3])//收漲
			if(compare[3]>compare[4])//收在月線上
			{				
				return true;			
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
		double currentQline;
		
		currentQline=(enterPoint-compare[3])/13+compare[5];
		currentMline=(enterPoint-compare[3])/4+compare[4];
		
		if(enterPoint>currentMline)//站上月線
		if((enterPoint-currentMline)>=(currentMline-compare[0]))//k棒明顯突破月線
		{return true;
			/*if ((currentMline-base.get(base.size()-1)[4])/base.get(base.size()-1)[4]>0)
			{
				return true;
			}*/
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
	private boolean endComputeReturnM(double[] mmstate,double[] contemp,ArrayList<double[]> content,double enterPoint)
	{		
		if (contemp[0]>=contemp[3])
		{
			updateHigh(contemp[1],mmstate);
			
			if(stopLoss(enterPoint,contemp[2]))
				return true;
			
			if(stopCompute(contemp[4],content.get(content.size()-1)[4],mmstate[3],enterPoint))
				return true;
			
			updateLow(contemp[2],mmstate,enterPoint);
		}
		else
		{
			updateLow(contemp[2],mmstate,enterPoint);
				
			if(stopLoss(enterPoint,contemp[2]))
				return true;
			
			if(stopCompute(contemp[4],content.get(content.size()-1)[4],mmstate[3],enterPoint))
				return true;
			
			updateHigh(contemp[1],mmstate);
		}
			
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
}
