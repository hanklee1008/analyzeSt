import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class bullStrategy2 {
	
	final int quarterKCount=13;
	final double divideWeeklyrate=9;
	static String drive="c:/";
	int test=0;
	double testbackpoint=0,ishead=0,lowpoint=0,lowkclose=0;
	
	public String strategyName()
	{
		return "monthline on quaterline+end of the week";
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
		
		double[] contemprecord=new double[7];
		ArrayList<double[]> contentrecord=new ArrayList<double[]>();
		int temprecord=0,tempstatus=0;
		
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
						/*if (mmstate[1]==1)
						{
							if(tempstatus==0)
							{
								System.arraycopy(contemp, 0, contemprecord, 0, contemp.length);
								contentrecord=(ArrayList<double[]>) content.clone();
								temprecord=temp;
								tempstatus=1;
							}
						}
						else*/
						{

							/*if(tempstatus==1)
							{
								System.arraycopy(contemprecord, 0, contemp, 0, contemprecord.length);
								content=contentrecord;
								temp=temprecord;
								tempstatus=0;
							}*/
							
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
						}
						
						if(mmstate[0]==1||mmstate[1]==1)
						{
							if (temp+1<sweek.getRows())
							buytime=sweek.getCell(0,temp+1).getContents();
						else
							buytime=sweek.getCell(0,temp).getContents();
													
						analyzeStockResultByQuarterLineMM(sday,sweek,allTimePoint,buytime,contemp,content,stockname,tempdata,mmstate);
						}					
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
	private void analyzeStockResultByQuarterLineMM(Sheet sday,Sheet sweek,ArrayList<String[]> allTimePoint,String buytime,double[] contemp,ArrayList<double[]> content,String stockname,String[] tempdata,double[] mmstate)
	{	
		DecimalFormat df=new DecimalFormat("#.##");
			
		try {		
					if (content.size()>=2)
					{
						//if (mmstate[1]==0&&mmstate[0]==1)
						if (mmstate[1]==0)
						{
							mmstate[6]=0;

							if (mmstate[2]==0)
							{//System.out.println(buytime+" "+content.get(content.size()-2)[4]+" "+content.get(content.size()-1)[4]+" "+contemp[4]);
								if (contemp[4]>=content.get(content.size()-1)[4]&&content.get(content.size()-2)[4]>content.get(content.size()-1)[4])
								{		//System.out.println(buytime+" "+content.get(content.size()-2)[4]+" "+content.get(content.size()-1)[4]+" "+contemp[4]);															
									mmstate[2]=1;
								}
							}
							if (mmstate[2]==1)
							{
								if(conditionAnalyzeMM(content,contemp))							
								{			//System.out.println("start "+buytime);							
									mmstate[1]=1;									
									mmstate[3]=contemp[3];
									mmstate[4]=contemp[3];
									mmstate[5]=contemp[3];
									double weeklyrate=(contemp[3]-content.get(content.size()-1)[3])/content.get(content.size()-1)[3]*100;
									double lead=(contemp[1]-contemp[3])/contemp[3]*100;
									double quantity=contemp[6];
									
									ishead=isHead(content,contemp);
									lowpoint=findlow(content,contemp);
									//tempdata=new String[]{stockname,buytime,"","","","",quantity+"",df.format(weeklyrate),df.format(lead),"","","","","","","","","","1"};
									fillStockData(allTimePoint,stockname,buytime,""+quantity,""+df.format(weeklyrate),""+df.format(lead));	
									
									
									//for (int i=0;i<contemp.length;i++)
									//fillInData(allTimePoint,11+i,""+contemp[i]);	

								}
								else
									mmstate[2]=0;
							}														
						}
						else
						{	
							//System.out.println(buytime+" "+mmstate[3]+" "+df.format(100*(mmstate[3]-mmstate[5])/mmstate[5]));
							//if (endComputeReturnMM(mmstate,sday,stockname,buytime,0))
							if (endComputeReturnM(mmstate,contemp,content,mmstate[5]))
							{//System.out.println("end "+buytime);
								//tempdata[2]=""+df.format(100*(currentHighM-enterPointM)/enterPointM);
								//tempdata[9]=""+df.format(100*(enterPointM-currentLowM)/enterPointM);
									
								{
									
									fillInData(allTimePoint,2,""+df.format(100*(mmstate[3]-mmstate[5])/mmstate[5]));
									fillInData(allTimePoint,9,""+df.format(100*(mmstate[5]-mmstate[4])/mmstate[5]));
									fillInData(allTimePoint,3,""+ishead);
									fillInData(allTimePoint,11,""+df.format(100*(mmstate[4]-lowpoint)/mmstate[5]));
									/*fillInData(allTimePoint,9,""+df.format(100*(mmstate[4]-lowpoint)/mmstate[5]));
									fillInData(allTimePoint,8,""+df.format(100*(lowkclose-mmstate[4])/lowkclose));
									fillInData(allTimePoint,5,""+df.format(100*(mmstate[5]-(lowpoint-mmstate[5]*0.035))/mmstate[5]));*/
									fillInData(allTimePoint,4,buytime);
								}
								
								
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
			//if(compare[0]<=compare[3])
			//if (compare[3]>=p1[3])//收漲
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
	private boolean endComputeReturnMM(double[] mmstate,Sheet sday,String name,String buytime,int day)
	{	
		int row;									
		double curtime=0;
		double[] contemp=new double[6];
		double enterPoint=mmstate[5];		
		long sundayTime;

		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd"); 		
		Calendar cal = Calendar.getInstance(); 
		try{
			Cell c=sday.findCell(buytime);
			if (c==null)
				return false;
			row=c.getRow();		

			cal.setTime(format.parse(sday.getCell(0,row).getContents()));  
			int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);			

			sundayTime=format.parse(sday.getCell(0,row).getContents()).getTime()+(8-dayOfWeek)*(24*60*60*1000);
		}
		catch(Exception e)
		{
			return false;
		}
			
		do
		{
			if (row+day>=sday.getRows())
				break;

			try{
				contemp[0]=Double.parseDouble(sday.getCell(1,row+day).getContents());
				contemp[1]=Double.parseDouble(sday.getCell(2,row+day).getContents());
				contemp[2]=Double.parseDouble(sday.getCell(3,row+day).getContents());
				contemp[3]=Double.parseDouble(sday.getCell(4,row+day).getContents());
				contemp[4]=Double.parseDouble(sday.getCell(7,row+day).getContents());
				
				if (contemp[0]>=contemp[3])
				{
					updateHigh(contemp[1],mmstate);
					
					if(stopLoss(enterPoint,contemp[2]))
						return true;
					
					if(stopCompute(contemp[4],Double.parseDouble(sday.getCell(7,row+day-1).getContents()),mmstate[3],enterPoint))
						return true;
					
					updateLow(contemp[2],mmstate,enterPoint);
				}
				else
				{
					updateLow(contemp[2],mmstate,enterPoint);

					if(stopLoss(enterPoint,contemp[2]))
						return true;
					
					if(stopCompute(contemp[4],Double.parseDouble(sday.getCell(7,row+day-1).getContents()),mmstate[3],enterPoint))
						return true;

					updateHigh(contemp[1],mmstate);
					
				}								
				day++;

				if ((row+day)<sday.getRows())
					curtime=format.parse(sday.getCell(0,row+day).getContents()).getTime();
			}
			catch(Exception e)
			{
				System.out.println("endComputeReturnM");
				e.printStackTrace();
				day++;
				continue;
			}
		}
		while((row+day)<sday.getRows()&&curtime<sundayTime);
		
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
	private boolean stopCompute(double curMonthline,double preMonthline,double high,double enterpoint)
	{
		if(curMonthline<preMonthline)//月線向下
		{
			if ((high-enterpoint)/enterpoint>=0.13)//高點漲超過10%
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
			if ((returnv[3]-enterpoint)/enterpoint<0.07)//高點不超過7%
			{
				returnv[4]=low;		
			}
	}
	public void computeReturnByReturnFile_day(File f,ArrayList<String[]> allTimePoint)
	{
		try{
			Workbook workBook=Workbook.getWorkbook(f);
			WritableWorkbook writeBook=Workbook.createWorkbook(f,workBook);
			WritableSheet sss=writeBook.getSheet(0);

			for (int i=1;i<sss.getRows();i++)
			{
				String stocknum=sss.getCell(0,i).getContents();
				String stockdate=sss.getCell(1,i).getContents();
				String stockenddate=sss.getCell(4,i).getContents();

				double[] basedata=new double[7];
				for (int j=0;j<basedata.length;j++)
					basedata[j]=Double.parseDouble(sss.getCell(11+j,i).getContents());

				double[] returnv=new double[2];
				returnv[0]=basedata[3];
				returnv[1]=basedata[3];

				String[] tempdata={"","","","","","","","","","","","","","","","","",""};
				DecimalFormat df=new DecimalFormat("#.##");

				Workbook stockbook=Workbook.getWorkbook(new File(drive+"software/sdata/15test999/"+stocknum));
				Sheet stocksh=stockbook.getSheet(0);

				Cell c=stocksh.findCell(stockdate);
				int row=c.getRow()-1;
				int nextrow=stocksh.getRows()-1;
				
				if(i!=sss.getRows()-1&&stocknum.equals(sss.getCell(0,i+1).getContents()))
					nextrow=stocksh.findCell(stockenddate).getRow()+4;
				
				//System.out.println(stocknum+":"+row+":"+nextrow);
				
				if (endComputeReturnByReturnFile_day(stocksh,row,nextrow,basedata,returnv,tempdata,basedata[3]))
				{
					//tempdata[2]=""+df.format(100*(returnv[0]-compoint)/compoint);	
					sss.addCell(new Number(3,i,Double.parseDouble(df.format(100*(returnv[0]-basedata[3])/basedata[3]))));
					sss.addCell(new Number(4,i,test));
				}

				//allTimePoint.add(tempdata);
				
			}
			writeBook.write();
			writeBook.close();
			workBook.close();
		}
		catch (Exception e)
		{
			System.out.print("\n computeReturnByReturnFile");
			e.printStackTrace();
		}		
	}
	private boolean endComputeReturnByReturnFile_day(Sheet s,int row,int nextrow,double[] basedata,double[] returnv,String[] tempdata,double compoint)
	{						
		String date="";
		int day=1,gg10=0,now10=0,endbenefit=0;
		double[] contemp=new double[9],previoustemp=new double[9];
		double[] previousreturnv=new double[2];

		try{
			for (int i=0;i<previoustemp.length;i++)
			{
				previoustemp[i]=Double.parseDouble(s.getCell(i+1,row).getContents());		
			}
		}
		catch (Exception e)
		{			
			e.printStackTrace();
			return false;
		}

		while (row+day<nextrow)
		{	
			now10=0;
			
			date=s.getCell(0,row+day).getContents();

			System.arraycopy(returnv, 0, previousreturnv, 0, returnv.length);

			try{
				for (int i=0;i<contemp.length;i++)
					contemp[i]=Double.parseDouble(s.getCell(i+1,row+day).getContents());		
				
				test=0;
				
				if(contemp[0]>contemp[3])
				{
					updateHigh(contemp,returnv,endbenefit);	

					if (gg10==0)
					{			
						if ((returnv[0]-compoint)/compoint>=0.1)
						{
							gg10=1;
							now10=1;
						}
					}

					//if (stopBenefit(gg10,contemp,previoustemp,basedata[3],returnv,previousreturnv))
					if (stopBenefit(gg10,now10,contemp,previoustemp,basedata[3],returnv,previousreturnv))
					{
						return true;
					}

					if (stopLoss(compoint,contemp[2]))
						return true;

					updateLow(contemp,returnv,compoint);					
				}
				else
				{							
					updateLow(contemp,returnv,compoint);	

					if (stopLoss(compoint,contemp[2]))
						return true;

					updateHigh(contemp,returnv,endbenefit);	

					if (gg10==0)
					{			
						if ((returnv[0]-compoint)/compoint>=0.1)
						{
							gg10=1;
							now10=1;
						}
					}

					//if (stopBenefit(gg10,contemp,previoustemp,basedata[3],returnv,previousreturnv))
					if (stopBenefit(gg10,now10,contemp,previoustemp,basedata[3],returnv,previousreturnv))
					{
						return true;
					}
				}

				/*if(stopCompute(returnv[0],compoint,contemp,s,gg10,row,day))					
				{
					return true;
				}	*/					

				System.arraycopy(contemp, 0, previoustemp, 0, contemp.length);
				day++;	
			}
			catch (Exception e)
			{
				System.out.print("\n endComputeReturnByReturnFile");
				e.printStackTrace();
			}
		}
		return false;
	}
	private void updateHigh(double[] contemp,double[] returnv,int endbenefit)
	{
		if (contemp[1]>returnv[0])
		{
			if (endbenefit==0)
				returnv[0]=contemp[1];
		}				
	}
	private void updateLow(double[] contemp,double[] returnv,double compoint)
	{					
		if (contemp[2]<returnv[1])	
			if ((returnv[0]-compoint)/compoint<0.07)
				returnv[1]=contemp[2];
	}
	private boolean stopCompute(double high,double enterpoint,double[] contemp,Sheet s,int gg10,int row,int day)
	{
		//if (gg10==1)//高點漲超過10%
		if (high>=enterpoint*1.13)
		{
			if(contemp[6]<Double.parseDouble(s.getCell(7,row+day-1).getContents())&&Double.parseDouble(s.getCell(7,row+day-1).getContents())<Double.parseDouble(s.getCell(7,row+day-2).getContents()))//月趨向下
			{
				return true;						
			}
			/*if(contemp[7]<Double.parseDouble(s.getCell(8,row+day-1).getContents()))//季線向下
			{
				return true;							
			}*/
		}

		return false;
	}
	private boolean stopBenefit(int gg10,int now10,double[] contemp,double[] previoustemp,double enterpoint,double[] returnv,double previousreturnv[])
	{
		//if (gg10==1)//高點漲超過10%		
		if (returnv[0]>=enterpoint*1.07)
		{
			/*if (returnv[0]>=enterpoint*1.2)
			{
				if (contemp[3]<contemp[5]&&contemp[3]<contemp[2]*1.02)
				{test=1;
					returnv[0]=contemp[3];
					return true;
				}
			}
			else*/
			{
				if (returnv[0]>=enterpoint*1.13)
				{
					//if (previoustemp[3]<previoustemp[5]&&previoustemp[3]<previoustemp[2]*1.02)
					if (previoustemp[3]<previoustemp[0])
					if (contemp[3]<contemp[5]&&contemp[3]<contemp[2]*1.02)
					{
						returnv[0]=contemp[3];
						return true;
					}
				}
			}
			
			/*if (contemp[2]<=enterpoint)
			{test=1;
				returnv[0]=enterpoint;
				return true;
			}*/
			
			
			
			/*if (contemp[0]>=previoustemp[3]*1.05)
			{
				if(contemp[2]<contemp[0]*0.95)
				{
					returnv[0]=contemp[0]*0.95;
					test=3;
					return true;
				} 
			}*/
		/*	if (contemp[0]>contemp[3])
				if (contemp[3]<contemp[1]*0.96)
				{test=3;
					returnv[0]=contemp[3];
					return true;
				}*/
			
			
			/*if (now10==1)
			{
				if (contemp[0]>=contemp[3])
				{
					if(contemp[2]<returnv[0]*0.97)
					{
						returnv[0]=returnv[0]*0.97;

						return true;
					}
				}
				else
				{
					if(contemp[3]<returnv[0]*0.97)
					{
						returnv[0]=contemp[3];

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

						return true;
					} 
					else
					{
						returnv[0]=previousreturnv[0]*0.97;

						return true;
					}
				}
			}	*/		
		}
		else
		{
			/*if (returnv[0]>=enterpoint*1.07)
			{
				if (contemp[2]<=enterpoint)
				{test=5;
					returnv[0]=enterpoint;
					return true;
				}
			}*/
		}
		return false;
	}
	public void computeReturnByReturnFile_week(File f,ArrayList<String[]> allTimePoint)
	{
		String stocknum="";
		try{
			Workbook workBook=Workbook.getWorkbook(f);
			WritableWorkbook writeBook=Workbook.createWorkbook(new File(drive+"software/sdata/t.xls"),workBook);
			WritableSheet sss=writeBook.getSheet(0);
			
			
			for (int i=1;i<sss.getRows();i++)
			{
				stocknum=sss.getCell(0,i).getContents();
				String stockdate=sss.getCell(1,i).getContents();

				double[] basedata=new double[7];
				for (int j=0;j<basedata.length;j++)
					basedata[j]=Double.parseDouble(sss.getCell(11+j,i).getContents());

				double[] returnv=new double[2];
				returnv[0]=basedata[3];
				returnv[1]=basedata[3];

				DecimalFormat df=new DecimalFormat("#.##");

				Workbook stockbook=Workbook.getWorkbook(new File(drive+"software/sdata/15test999/"+stocknum));
				Sheet stockweek=stockbook.getSheet(1);

				Cell c=stockweek.findCell(stockdate);
				int row=c.getRow()-1;
				int nextrow=stockweek.getRows()-1;

				if (endComputeReturnByReturnFile_week(stockweek,row,nextrow,basedata,returnv,basedata[3],stocknum))
				{
					//tempdata[2]=""+df.format(100*(returnv[0]-compoint)/compoint);	
					sss.addCell(new Number(3,i,Double.parseDouble(df.format(100*(returnv[0]-basedata[3])/basedata[3]))));
				}

				//allTimePoint.add(tempdata);
				
			}
			writeBook.write();
			writeBook.close();
			workBook.close();
		}
		catch (Exception e)
		{
			System.out.print("\n computeReturnByReturnFile ");
			e.printStackTrace();
		}		
	}
	private boolean endComputeReturnByReturnFile_week(Sheet stockweek,int row,int nextrow,double[] basedata,double[] returnv,double compoint,String stocknum)
	{						
		String date="";
		int day=1,gg10=0,now10=0,endbenefit=0;
		double[] contemp=new double[7],previoustemp=new double[7];
		double[] previousreturnv=new double[2];

		try{
			for (int i=0;i<previoustemp.length;i++)
			{
				if(!stockweek.getCell(i+1,row).getContents().equals(""))
					previoustemp[i]=Double.parseDouble(stockweek.getCell(i+1,row).getContents());		
				else
					previoustemp[i]=0;
			}
		}
		catch (Exception e)
		{			
			e.printStackTrace();
			return false;
		}

		while (row+day<nextrow)
		{		
			date=stockweek.getCell(0,row+day).getContents();

			System.arraycopy(returnv, 0, previousreturnv, 0, returnv.length);

			try{
				for (int i=0;i<contemp.length;i++)
				{
					if(!stockweek.getCell(i+1,row+day).getContents().equals(""))
						contemp[i]=Double.parseDouble(stockweek.getCell(i+1,row+day).getContents());		
					else
						contemp[i]=0;
				}

				if(contemp[0]>contemp[3])
				{
					updateHigh(contemp,returnv,endbenefit);	

					if (gg10==0)
					{			
						if ((returnv[0]-compoint)/compoint>=0.1)
						{
							gg10=1;
							now10=1;
						}
					}

					//if (stopBenefit(gg10,contemp,previoustemp,basedata[3],returnv,previousreturnv))
					if (stopBenefit(gg10,contemp,previoustemp,basedata[3],returnv))
					{
						return true;
					}

					if (stopLoss(compoint,contemp[2]))
						return true;

					updateLow(contemp,returnv,compoint);					
				}
				else
				{							
					updateLow(contemp,returnv,compoint);	

					if (stopLoss(compoint,contemp[2]))
						return true;

					updateHigh(contemp,returnv,endbenefit);	

					if (gg10==0)
					{			
						if ((returnv[0]-compoint)/compoint>=0.1)
						{
							gg10=1;
							now10=1;
						}
					}

					//if (stopBenefit(gg10,contemp,previoustemp,basedata[3],returnv,previousreturnv))
					if (stopBenefit(gg10,contemp,previoustemp,basedata[3],returnv))
					{
						return true;
					}
				}

				/*if(stopCompute(contemp,s,gg10,row,day))					
				{
					return true;
				}	*/					

				System.arraycopy(contemp, 0, previoustemp, 0, contemp.length);
				day++;	
			}
			catch (Exception e)
			{
				System.out.print("\n endComputeReturnByReturnFile "+stocknum);
				e.printStackTrace();
			}
		}
		return false;
	}
	private boolean stopBenefit(int gg10,double[] contemp,double[] previoustemp,double enterpoint,double[] returnv)
	{
		if (returnv[0]>=enterpoint*1.13)
		{
			if (contemp[2]<previoustemp[2])
			{
				returnv[0]=previoustemp[2];
				return true;
			}
		}
			

		return false;
	}
	//below test
	private int isHead(ArrayList<double[]> base,double[] compare)
	{
		if(base.size()>=8)
			if(base.get(base.size()-4)[1]>=base.get(base.size()-8)[0]*1.6)
				return 1;
		
		for (int i=base.size()-1;i>=base.size()-3;i--)
		{
			if(base.get(i)[4]<base.get(i-1)[4])
			if(base.get(i)[0]<base.get(i)[3]&&base.get(i)[3]>=base.get(i)[0]*1.1&&base.get(i)[3]>compare[3])
				return 2;
		}
		
		return 0;
	}
	private double findlow(ArrayList<double[]> base,double[] compare)
	{
		
		if(compare[3]>=compare[0]*1.07)
		{
			lowkclose=compare[3];
			return compare[2];
		}
		
		/*for (int i=base.size()-1;i>=base.size()-2;i--)
		{
			lowkclose=base.get(i)[3];
			if(base.get(i)[3]>=base.get(i)[0]*1.07)
				return base.get(i)[2];
		}*/
		
		return 999;
	}
}
