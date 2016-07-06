import java.text.DecimalFormat;
import java.util.ArrayList;

import jxl.Sheet;


public class BearStrategy1 {
	
	final int quarterKCount=13;
	final double divideWeeklyrate=0.09;
	
	
	
	public void analyzeStock(Sheet sweek,ArrayList<String[]> allTimePoint,String stockname)
	{		
		int isComputeReturn=0; //0:exit 1:enter
		double[] previousData=new double[7],baseData=new double[7],contemp=new double[7];
		double currentHigh=0,currentLow=0,enterPoint=0;
		ArrayList<double[]> content=new ArrayList<double[]>();
		DecimalFormat df=new DecimalFormat("#.##");
		String buytime="";
		double weeklyrate,lead,quantity;
		String[] tempdata=null;
		int quarterLineRedK=0;	

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
								if (content.get(content.size()-1)[5]>=content.get(content.size()-2)[5]&&contemp[5]<content.get(content.size()-1)[5])
								{
									quarterLineRedK=1;
								}
							}

							if (quarterLineRedK>=3&&quarterLineRedK<=8)
							{
								if (contemp[5]<content.get(content.size()-1)[5])
								{
									if (conditionAnalyzeQB(content,contemp,contemp[3]))
									{
										isComputeReturn=1;
										baseData=contemp;
										previousData=content.get(content.size()-1);										
										enterPoint=contemp[3];									
										currentHigh=contemp[3];
										currentLow=contemp[3];
										buytime=sweek.getCell(0,temp).getContents();
										weeklyrate=-(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
										lead=-(contemp[2]-contemp[3])/contemp[3]*100;
										quantity=contemp[6];
										
										tempdata=new String[]{stockname,buytime,"","","",""+quarterLineRedK,quantity+"",df.format(weeklyrate),df.format(lead),"","","","","","","","","","1"};									
									}
									else
										quarterLineRedK++;
								}
								else
								{
									quarterLineRedK=0;
								}
							}
							//else if(quarterLineRedK>=6||quarterLineRedK==1||quarterLineRedK==2) 
							else if(quarterLineRedK>=9||quarterLineRedK==1||quarterLineRedK==2) 
							{
								if (contemp[5]<content.get(content.size()-1)[5])
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
									if ((currentLow-enterPoint)/enterPoint>-0.07)
										currentHigh=contemp[1];									
								}
								if (contemp[2]<currentLow)
								{
									if ((currentHigh-enterPoint)/enterPoint<0.13)
										currentLow=contemp[2];
								}							
							}
							else
							{
								if (contemp[2]<currentLow)
								{
									currentLow=contemp[2];
								}
								if (contemp[1]>currentHigh)
								{
									if ((currentLow-enterPoint)/enterPoint>-0.07)
										currentHigh=contemp[1];															
								}																										
							}
							
							if (endComputeReturnQB(currentLow,previousData,baseData,contemp,content))
							{	
								tempdata[2]=""+df.format(100*(enterPoint-currentLow)/enterPoint);
								tempdata[9]=""+df.format(100*(currentHigh-enterPoint)/enterPoint);
								

								allTimePoint.add(tempdata);							
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
			System.out.print("\nanalyzeStockResultByQuarterLine"+","+sweek.getName());
			e.printStackTrace();
		}
	}
	public boolean conditionAnalyzeQB(ArrayList<double[]> base,double[] compare,double enterPoint)
	{
		int weeklyRateType,highType;
		
		highType=isOverHighB(base,compare);
		weeklyRateType=weeklyRateTypeB(base,compare);
		
		if(isTurnQuarterLineB(base,compare,enterPoint))
			if(isTurnMonthLineB(base,compare,enterPoint))
				if(weeklyRateType!=0)
				{				
					if (highType!=0)
						return true;
				}
								
		return false;
	}
	private boolean isTurnQuarterLineB(ArrayList<double[]> base,double[] compare,double enterPoint)
	{//System.out.print("\nisTurnQuarterLine");
		double currentQline;
		
		currentQline=(enterPoint-compare[3])/13+compare[5];
		
		if(enterPoint<currentQline)//禴瘆﹗絬
		if((enterPoint-currentQline)<=(currentQline-compare[0]))//k次陪禴瘆﹗絬
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
	private boolean isTurnMonthLineB(ArrayList<double[]> base,double[] compare,double enterPoint)
	{//System.out.print("\nisTurnMonthLine\n");
		double currentMline;
		double currentQline;
		
		currentQline=(enterPoint-compare[3])/13+compare[5];
		currentMline=(enterPoint-compare[3])/4+compare[4];
		
		if(enterPoint<currentMline)//禴瘆る絬
		if((enterPoint-currentMline)<=(currentMline-compare[0]))//k次陪禴瘆る絬 ***
		{
			if((currentMline-base.get(base.size()-1)[4])/base.get(base.size()-1)[4]<=(currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5])
			{
				return true;
			}
		}
			return false;
	}
	private int weeklyRateTypeB(ArrayList<double[]> base,double[] compare)
	{		
		if ((compare[3]-base.get(base.size()-1)[3])/base.get(base.size()-1)[3]<=-0.06)
			if ((base.get(base.size()-1)[3]-base.get(base.size()-2)[3])/base.get(base.size()-2)[3]>-0.06) //***0.05 good	
				return 1;
		
		return 0;
	}
	private int isOverHighB(ArrayList<double[]> base,double[] compare)
	{//System.out.print("\nisOverHigh");

		double lowestPoint=base.get(base.size()-1)[2],lowestKClose=base.get(base.size()-1)[3];
		
		for (int i=base.size()-(quarterKCount-1);i<base.size()-1;i++)
		{
			if (lowestPoint>base.get(i)[2])//獶程蔼
			{
				lowestPoint=base.get(i)[2];
				lowestKClose=base.get(i)[3];
			}	
		}	
		
		if (lowestPoint>compare[2])
		{
			if (compare[3]<=lowestPoint)
				return 1;
			else
				return 2;
		}
		else
		{		
			if (compare[3]==compare[2])//セōΜ程
				if (lowestKClose>=compare[3])
				{
					return 3; //***
				}
		}
		
		return 0;
	}
	private boolean endComputeReturnQB(double currentLow,double[] previousData,double[] baseData,double[] contemp,ArrayList<double[]> content)
	{	
		if ((baseData[3]-contemp[1])/baseData[3]<-0.13)//┰禬筁13%
		{
			return true;
		}
		else if(contemp[5]>content.get(content.size()-1)[5])//﹗絬
		{
			if ((currentLow-baseData[3])/baseData[3]<=-0.1)//厨筍禬筁10%
			{
				return true;
			}							
		}	
			
		return false;
	}
}
