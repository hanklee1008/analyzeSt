import java.text.DecimalFormat;
import java.util.ArrayList;

import jxl.Sheet;

public class bullStrategy2 {
	int monthLineRedTypeM;//0:black 1,2,3:red
	int isComputeReturnM; //0:exit 1:enter
	int mm;
	double[] baseDataM;
	double currentHighM,currentLowM,enterPointM,testHighM;
	
	int weeklyRateType,highType=0,quantityType=0,kType=0,quarterLineRedK=0,monthLineRedK=1;
	final int quarterKCount=13;
	final double divideWeeklyrate=9;
	static int predict=1; //0:no predict 1:predict
	static String drive="d:/";
	 
	public void analyzeStock(Sheet sweek,Sheet sday,ArrayList<String[]> allTimePoint,String filepath,String stockname)
	{	
		int isComputeReturn=0; //0:exit 1:enter
		double[] baseData=new double[7],contemp=new double[7];
		double currentHigh=0,currentLow=0,enterPoint=0;
		ArrayList<double[]> content=new ArrayList<double[]>();
		DecimalFormat df=new DecimalFormat("#.##");
		String buytime="";
		double weeklyrate,lead,quantity,kRate;
		String[] tempdata=null;
		
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
						
						buytime=sweek.getCell(0,temp).getContents();
						analyzeStockResultByQuarterLineMM(sweek,allTimePoint,buytime,contemp,content,stockname,tempdata);
					}
					
					temp++;
					content.add(contemp);			
				}
		}
		catch (Exception e)
		{
			System.out.print("\nanalyzeStockResultByQuarterLine"+","+sweek.getName());
			e.printStackTrace();
		}
	}
	private void analyzeStockResultByQuarterLineMM(Sheet s,ArrayList<String[]> allTimePoint,String buytime,double[] contemp,ArrayList<double[]> content,String stockname,String[] tempdata)
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

								//tempdata[2]=""+df.format(100*(currentHighM-enterPointM)/enterPointM);
								//tempdata[9]=""+df.format(100*(enterPointM-currentLowM)/enterPointM);
								fillInData(allTimePoint,2,""+df.format(100*(currentHighM-enterPointM)/enterPointM));						
								fillInData(allTimePoint,9,""+df.format(100*(baseDataM[3]-currentLowM)/baseDataM[3]));
								
								
								//allTimePoint.add(tempdata);
								
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
	private void fillStockData(ArrayList<String[]> allTimePoint,String stockNum,String buytime,String quantity,String weeklyrate,String lead)
	{	
		String[] temp={stockNum,buytime,"","","","",quantity+"",weeklyrate,lead,"","","","","","","","",""+kType,"1"};
		allTimePoint.add(temp);
	}
	private void fillInData(ArrayList<String[]> allTimePoint,int location,String firstComputeReturn)
	{
		allTimePoint.get(allTimePoint.size()-1)[location]=firstComputeReturn;
	}
	private boolean conditionAnalyzeQ(ArrayList<double[]> base,double[] compare,double enterPoint)
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
	private boolean isTurnMonthLine(ArrayList<double[]> base,double[] compare,double enterPoint)
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
	private int weeklyRateType(ArrayList<double[]> base,double[] compare)
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
	private boolean kType(int quantityType,ArrayList<double[]> base,double[] compare,double enterPoint)
	{
		if ((kType=isRedK(quantityType,base,compare,enterPoint))!=0)
			return true;		

		return false;	
	}
	private int isRedK(int quantityType,ArrayList<double[]> base,double[] compare,double enterPoint)
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
	private boolean endComputeReturnM(double currentHigh,double[] baseData,double[] contemp,ArrayList<double[]> content,double enterPointM)
	{
		if ((enterPointM-contemp[2])/enterPointM>0.13)//拉回超過12%
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
}
