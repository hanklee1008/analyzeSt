import java.text.DecimalFormat;
import java.util.ArrayList;

import jxl.Sheet;


public class BearStrategy2 {

	int test=0;
	
	public String strategyName()
	{
		return "monthline on quaterline+end of the week";
	}
	public void analyzeStock(Sheet sweek,ArrayList<String[]> allTimePoint,String stockname)
	{	
		int isComputeReturn=0; //0:exit 1:enter
		double[] baseData=new double[7],contemp=new double[7];
		double currentHigh=0,currentLow=0,enterPoint=0;
		ArrayList<double[]> content=new ArrayList<double[]>();
		DecimalFormat df=new DecimalFormat("#.##");
		String[] tempdata=null;	
		
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

				if (content.size()>=4)
				{
					if (isComputeReturn==0)
					{
						//if (content.get(content.size()-2)[5]<content.get(content.size()-3)[5]&&content.get(content.size()-1)[5]<content.get(content.size()-2)[5]&&contemp[5]<content.get(content.size()-1)[5])
						if (content.get(content.size()-1)[5]<content.get(content.size()-2)[5]&&contemp[5]<content.get(content.size()-1)[5])							
						{
							if(conditionAnalyzeMB(content,contemp))							
							{	
								isComputeReturn=1;
								String buytime=sweek.getCell(0,temp).getContents();
								baseData=contemp;
								enterPoint=contemp[3];
								currentHigh=contemp[3];
								currentLow=contemp[3];
								double weeklyrate=-(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
								double lead=-(contemp[2]-contemp[3])/contemp[3]*100;
								double quantity=contemp[6];

								tempdata=new String[]{stockname,buytime,"","","",""+test,quantity+"",df.format(weeklyrate),df.format(lead),"","","","","","","","","","1"};
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

						if (endComputeReturnMB(currentLow,baseData,contemp,content))
						{
							tempdata[2]=""+df.format(100*(enterPoint-currentLow)/enterPoint);
							tempdata[9]=""+df.format(100*(currentHigh-enterPoint)/enterPoint);								

							allTimePoint.add(tempdata);		
							
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
				if (base.get(base.size()-1)[4]>=base.get(base.size()-2)[4]&&compare[4]<base.get(base.size()-1)[4])//月線紅翻黑
					{test=1;
						return true;
					}
		//if((compare[3]<=compare[0]))
			if(compare[1]>compare[5]&&compare[3]<compare[5])
					{test=2;
				return true;											
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
			if ((currentLow-baseData[3])/baseData[3]<=-0.1)//低點超過10%
			{
				return true;
			}
		}
		
		return false;
	}
}
