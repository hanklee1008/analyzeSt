import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jxl.Cell;
import jxl.Sheet;

public class bullStrategy1 {

	final int quarterKCount=13;
	final double divideWeeklyrate=0.09;
	
	public String strategyName()
	{
		return "quaterline+end of the week";
	}
	public void analyzeStock(Sheet sweek,Sheet shfile,ArrayList<String[]> allTimePoint,String filepath,String stockname)
	{	
		int isComputeReturn=0; //0:exit 1:enter
		double[] baseData=new double[7],contemp=new double[7];
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
								if (content.get(content.size()-1)[5]<=content.get(content.size()-2)[5]&&contemp[5]>=content.get(content.size()-1)[5])
								{
									quarterLineRedK=1;
								}
							}
							if (quarterLineRedK>=1&&quarterLineRedK<=8)
							{
								if (contemp[5]>=content.get(content.size()-1)[5])
								{							
									if (conditionAnalyze(content,contemp,contemp[3]))
									{
										isComputeReturn=1;
										baseData=contemp;										
										enterPoint=contemp[3];									
										currentHigh=contemp[3];
										currentLow=contemp[3];
										buytime=sweek.getCell(0,temp).getContents();
										weeklyrate=(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
										lead=(contemp[1]-contemp[3])/contemp[3]*100;
										quantity=contemp[6];
											
										tempdata=new String[]{stockname,buytime,"","","","",quantity+"",df.format(weeklyrate),df.format(lead),"","","","","","","","","","1"};				

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
							
							if (endComputeReturn(currentHigh,baseData,contemp,content))
							{
								tempdata[2]=""+df.format(100*(currentHigh-enterPoint)/enterPoint);
								tempdata[9]=""+df.format(100*(enterPoint-currentLow)/enterPoint);

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
	public boolean conditionAnalyze(ArrayList<double[]> base,double[] compare,double enterPoint)
	{	
		int weeklyRateType;
		//quantityType=isOverQuantity(base,compare);
		//highType=isOverHigh(base,compare);
		weeklyRateType=weeklyRateType(base,compare);
		
		if(isTurnQuarterLine(base,compare,enterPoint))
			if(isTurnMonthLine(base,compare,enterPoint))
				if(weeklyRateType!=0)
				{				
					//if ((quantityTye>=1&&quantityType<=3)||highType!=0)
					if (kType(0,base,compare,enterPoint))
					{						
						return true;
					}
				}
								
		return false;
	}
	public boolean isTurnQuarterLine(ArrayList<double[]> base,double[] compare,double enterPoint)
	{//System.out.println("isTurnQuarterLine");
		double currentQline;

		currentQline=((enterPoint-compare[3])/13+compare[5]);

		if(enterPoint>currentQline)//���W�u�u
			if((enterPoint-currentQline)>=(currentQline-compare[0]))//k�Ω����}�u�u
			{			
				if ((currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5]>=0.0)
				{
					return true;
				}

				/*if ((currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5]>=0.001)//½��W�L0.001 ***
				{
					return true;
				}
				else
				{
					if (compare[3]==compare[1])//���̰�
					{
						return true;
					}
				}*/	
			}
		return false;
	}
	public boolean isTurnMonthLine(ArrayList<double[]> base,double[] compare,double enterPoint)
	{//System.out.println("isTurnMonthLine");
		double currentMline;
		double currentQline;

		currentQline=((enterPoint-compare[3])/13+compare[5]);
		currentMline=((enterPoint-compare[3])/4+compare[4]);

		if(enterPoint>currentMline)//���W��u
			if((enterPoint-currentMline)>=(currentMline-compare[0]))//k�Ω����}��u
			{
				if((currentMline-base.get(base.size()-1)[4])/base.get(base.size()-1)[4]>=(currentQline-base.get(base.size()-1)[5])/base.get(base.size()-1)[5])
				{
					return true;
				}
			}

		return false;
	}
	public int weeklyRateType(ArrayList<double[]> base,double[] compare)
	{	//System.out.println("isweeklyRateType");
		{
			if ((compare[1]-base.get(base.size()-1)[3])/base.get(base.size()-1)[3]>=divideWeeklyrate)
				return 1;
		}
			

		return 0;
	}
	public boolean kType(int quantityType,ArrayList<double[]> base,double[] compare,double enterPoint)
	{
		if ((isRedK(quantityType,base,compare,enterPoint))!=0)
			return true;		

		return false;	
	}
	public int isRedK(int quantityType,ArrayList<double[]> base,double[] compare,double enterPoint)
	{//System.out.print("\nisRedK");		
		if ((enterPoint-compare[0])/compare[0]*100>=5)//���ΰϰ�>=5%
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
				if((enterPoint-compare[2])/compare[2]*100>=7)//�q�C�I��>=7%
				{
					if ((enterPoint - compare[0]) / compare[0] > 0.02)// �j��2%����
					{
						return 3;
					} 
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
			return 1;//�̤j�q
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
			quantity+=base.get(base.size()-1)[6];//�e�@�ڦ���k,��զX�q

			if (base.get(base.size()-2)[3]>=base.get(base.size()-2)[0])
			{
				quantity+=base.get(base.size()-2)[6];//�e�G�ڦ���k,��զX�q
			}
		}

		if (quantity>=preHighQuantity)
			return 3;

		/*if (compare[3]>=base.get(maxHighK)[3])
				{
					return 7;
				}

				if (maxHighK==base.size()-1)//�e�@��k�Φ��̰��I			
				{
					if((compare[3]-compare[0])/compare[0]>0.05)//�W�L5%���j����
					{
						if((compare[0]-base.get(base.size()-1)[3])/base.get(base.size()-1)[3]<0.015)//�}�L��ek�Φ��L������1.5%
							//if (compare[6]>base.get(base.size()-1)[6]*0.9)//�q�W�L�e�@��k�Ϊ�9��	
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

				if (preLowQuantity==maxQuantity)//�̧C�ɦ��̤j�q
					return 9;*/			

		return 0;
	}
	public int isOverHigh(ArrayList<double[]> base,double[] compare)
	{//System.out.print("\nisOverHigh");

		int highType=1,kn=base.size()-1;//0:�D�̰� 1:�̰�
		double highestPoint=base.get(base.size()-1)[1],highestKClose=base.get(base.size()-1)[3];

		for (int i=base.size()-(quarterKCount-1);i<base.size()-1;i++)
		{
			if (highestPoint<base.get(i)[1])//�D�̰�
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
			if (compare[3]==compare[1])//�������̰�
				if (compare[3]>=highestKClose)
				{
					return 3;
				}
		}

		return 0;
	}
	public boolean endComputeReturn(double currentHigh,double[] baseData,double[] contemp,ArrayList<double[]> content)
	{	
		if ((baseData[3]-contemp[2])/baseData[3]>0.13)//�Ԧ^�W�L12%
		{
			return true;
		}
		else if(contemp[4]<content.get(content.size()-1)[4])//�u�u�V�U
		{
			if ((currentHigh-baseData[3])/baseData[3]>=0.13)//���I���W�L10%
			{
				return true;
			}							
		}
			
		return false;
	}
}
