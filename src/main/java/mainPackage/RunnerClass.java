package mainPackage;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RunnerClass 
{
	public static String[][] pendingRenewalLeases; 
    public static String company;
    public static String buildingAbbreviation;
    public static String ownerName;
    public static WebDriver driver;
    public static Alert alert;
    
	public static ChromeOptions options;
	public static Actions actions;
	public static JavascriptExecutor js;
	public static WebDriverWait wait;
	public static String[][] pendingBuildingList;
	public static int updateStatus;
	public static String failedReason ="";
	public static ArrayList<String> successBuildings = new ArrayList<String>();
	public static ArrayList<String> failedBuildings = new ArrayList<String>();
	public static String[][] autoCharges;
	public static String[][] moveInCharges;
	public static String [] statusList;
	public static String currentDate = "";
	public static HashMap<String,String> failedReaonsList= new HashMap<String,String>();
	public static String leaseStatuses[][];
	public static String UWStatuses[][];
	public static String downloadFilePath;
	public static String monthlyRent;
	public static String startDate;
	public static String monthlyRentInPW;
	public static String startDateInPW;
	public static String endDateInPW;
	public static String portfolioType;
	public static String portfolioName;
	public static boolean published;
	public static boolean listingAgent;
	public static String currentTime;
	public static int statusID;
	public static String completeBuildingAbbreviation;
	public static String arizonaCityFromBuildingAddress = "";
	public static String arizonaRentCode = "";
	public static boolean arizonaCodeAvailable = false;
	
	// All fields required for Late Fee Rule
	    public static String lateFeeRuleType;
		public static String lateFeeType ="";
		public static String PDFFormatType= "";
		// Initial Fee + Per Day Fee
		public static String dueDay_initialFee="";
		public static String initialFeeAmount="";
		public static String initialFeeDropdown="";
		public static String perDayFeeAmount ="";
		public static String perDayFeeDropdown ="";
		public static String maximumDropdown1 ="";
		public static String maximumAmount ="";
		public static String maximumDropdown2 ="";
		public static String minimumDue ="";
		public static String additionalLateChargesLimit ="";
		
		// Greater of Flat Fee or Percentage
		public static String dueDay_GreaterOf="";
		public static String flatFee = "";
		public static String percentage = "";
		public static String maximumDropdown1_GreaterOf ="";
		public static String maximumAmount_GreaterOf ="";
		public static String maximumDropdown2_GreaterOf ="";
		public static String minimumDue_GreaterOf ="";
	
	public static void main(String[] args) throws Exception 
	{
		
		//Get In Progress Leases
		//Company,BuildingAbbreviation, LeaseNae
		DataBase.getBuildingsList();
		for(int i=0;i<pendingRenewalLeases.length;i++)
		{
		  
		  System.out.println(" Record -- "+(i+1));
		  company = pendingRenewalLeases[i][0];
		  buildingAbbreviation = pendingRenewalLeases[i][1];
		  ownerName = pendingRenewalLeases[i][2];
		  statusID =0;
		  failedReason = "";
		  RunnerClass.portfolioType = "";
		  portfolioName = "";
		  RunnerClass.PDFFormatType = "";
		  PDFReader.RCDetails= "";
		  arizonaCityFromBuildingAddress = "";
		  arizonaRentCode = "";
		  arizonaCodeAvailable = false;
		  
		  
		  //Delete files in the folder before starting a lease
		  try
			{
			FileUtils.cleanDirectory(new File(AppConfig.downloadFilePath));
			}
			catch(Exception e) {}
		  
		  
		  if(company.contains("Austin")||company.contains("California")||company.contains("Chattanooga")||company.contains("Chicago")||company.contains("Colorado")||company.contains("Kansas City")||company.contains("Houston")||company.contains("Maine")||company.contains("Savannah")||company.contains("North Carolina")||company.contains("Alabama")||company.contains("Arkansas")||company.contains("Dallas/Fort Worth")||company.contains("Indiana")||company.contains("Little Rock")||company.contains("San Antonio")||company.contains("Tulsa")||company.contains("Georgia")||company.contains("OKC")||company.contains("South Carolina")||company.contains("Tennessee")||company.contains("Florida")||company.contains("New Mexico")||company.contains("Ohio")||company.contains("Pennsylvania")||company.contains("Lake Havasu")||company.contains("Columbia - St Louis")||company.contains("Maryland")||company.contains("Virginia")||company.contains("Boise")||company.contains("Idaho Falls")||company.contains("Utah")||company.contains("Spokane")||company.contains("Washington DC")||company.contains("Hawaii")||company.contains("Arizona")||company.contains("New Jersey")||company.contains("Montana")||company.contains("Delaware")) 
	    {
		  //Change the Status of the Lease to Started so that it won't run again in the Jenkins scheduling Process
		           DataBase.insertData(buildingAbbreviation,"Started",6);
		            completeBuildingAbbreviation = buildingAbbreviation;  //This will be used when Building not found in first attempt
		           try
		           {
		            String a = buildingAbbreviation;
		            a = a.replace(" " , "");
		            int b = a.length()-1;
		           char c =  a.charAt(a.indexOf('-')+1);
		            if(a.indexOf('-')>=1&&a.indexOf('-')==(b-1))
						buildingAbbreviation = buildingAbbreviation;
					else
						if(a.indexOf('-')>=1&&a.charAt(a.indexOf('-')+1)=='(')
		            buildingAbbreviation = buildingAbbreviation.split("-")[0].trim();
						else buildingAbbreviation = buildingAbbreviation;
		           }
		           catch(Exception e) {}
          // Login to the PropertyWare		  
		  try
		  {
		  //Login 
		  if( PropertyWare.login()==true)
		  {
		  //Search building in property Ware
		   if(PropertyWare.searchBuilding(company, buildingAbbreviation)==true)
			{
				if(PropertyWare.downloadLeaseAgreement(buildingAbbreviation, ownerName)==true)
				{
					
					if(PDFReader.readPDFPerMarket(company)==true)
					{
						PropertyWare_updateValues.configureValues();
						PropertyWare_MoveInCharges.addMoveInCharges();
						PropertyWare_AutoCharges.addingAutoCharges();
						PropertyWare_OtherInformation.addOtherInformation();
						
						//Update Completed Status
						if(failedReason=="")
							failedReason="";
						else if(failedReason.charAt(0)==',')
							failedReason = failedReason.substring(1);
						String updateSuccessStatus ="";
						if(statusID==0)
							updateSuccessStatus = "Update [Automation].LeaseInfo Set Status ='Completed', StatusID=4,NotAutomatedFields='"+failedReason+"',LeaseCompletionDate= getDate() where BuildingName like '%"+buildingAbbreviation+"%'";
						else 
							updateSuccessStatus = "Update [Automation].LeaseInfo Set Status ='Review', StatusID=5,NotAutomatedFields='"+failedReason+"',LeaseCompletionDate= getDate() where BuildingName like '%"+buildingAbbreviation+"%'";
					    	DataBase.updateTable(updateSuccessStatus);
					}
					else 
					{
						if(failedReason.charAt(0)==',')
						failedReason = failedReason.substring(1);
						String updateSuccessStatus = "Update [Automation].LeaseInfo Set Status ='Failed', StatusID=3,NotAutomatedFields='"+failedReason+"',LeaseCompletionDate= getDate() where BuildingName like '%"+buildingAbbreviation+"%'";
				    	DataBase.updateTable(updateSuccessStatus);
					}
					
				}
				else 
				{
					if(PropertyWare.selectBuilding(company, completeBuildingAbbreviation)==true)
			    	{
			    		RunnerClass.processAfterBuildingIsSelected();
			    	}
			    	else
			    	{
					if(failedReason.charAt(0)==',')
						failedReason = failedReason.substring(1);
					String updateSuccessStatus = "Update [Automation].LeaseInfo Set Status ='Failed', StatusID=3,NotAutomatedFields='"+failedReason+"',LeaseCompletionDate= getDate() where BuildingName like '%"+buildingAbbreviation+"%'";
			    	DataBase.updateTable(updateSuccessStatus);
			    	}
				}
			}
		    else 
		    {
		    	if(PropertyWare.selectBuilding(company, completeBuildingAbbreviation)==true)
		    	{
		    		RunnerClass.processAfterBuildingIsSelected();
		    	}
		    	else
		    	{
		    	if(failedReason.charAt(0)==',')
					failedReason = failedReason.substring(1);
 		    	String updateSuccessStatus = "Update [Automation].LeaseInfo Set Status ='Failed', StatusID=3,NotAutomatedFields='"+failedReason+"',LeaseCompletionDate= getDate() where BuildingName like '%"+buildingAbbreviation+"%'";
		    	DataBase.updateTable(updateSuccessStatus);
		    	}
		    }
		}
		else 
		{
			if(failedReason.charAt(0)==',')
				failedReason = failedReason.substring(1);
			String updateSuccessStatus = "Update [Automation].LeaseInfo Set Status ='Failed', StatusID=3,NotAutomatedFields='"+failedReason+"',LeaseCompletionDate= getDate() where BuildingName like '%"+buildingAbbreviation+"%'";
	    	DataBase.updateTable(updateSuccessStatus);
		}
		  try
		  {
			  driver.quit();
		  }
		  catch(Exception e) {}
		  } 
		  catch(Exception e)
		  {
			  e.printStackTrace();
		  }
		}
		}
	}   

	public static File getLastModified() throws Exception
	{
		
	    File directory = new File(AppConfig.downloadFilePath);
	    File[] files = directory.listFiles(File::isFile);
	    long lastModifiedTime = Long.MIN_VALUE;
	    File chosenFile = null;

	    if (files != null)
	    { 
	        for (File file : files)
	        {
	            if (file.lastModified() > lastModifiedTime)
	            {
	                chosenFile = file;
	                lastModifiedTime = file.lastModified();
	            }
	        }
	    }

	    return chosenFile;
	}
	
	public static String convertDate(String dateRaw) throws Exception
	{
		try
		{
		SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd, yyyy");
	    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
	    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
	    System.out.println(format2.format(date));
		return format2.format(date).toString();
		}
		catch(Exception e)
		{
			try
			{
			SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd yyyy");
		    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
		    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
		    System.out.println(format2.format(date));
			return format2.format(date).toString();
			}
			catch(Exception e2)
			{
			  if(dateRaw.trim().replaceAll(" +", " ").split(" ")[1].contains("st")||dateRaw.trim().replaceAll(" +", " ").split(" ")[1].contains("nd")||dateRaw.trim().replaceAll(" +", " ").split(" ")[1].contains("th"))
				  dateRaw = dateRaw.trim().replaceAll(" +", " ").replace("st", "").replace("nd", "").replace("th", "");
			  try
				{
				SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd yyyy");
			    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
			    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
			    System.out.println(format2.format(date));
				return format2.format(date).toString();
				}
				catch(Exception e3)
				{
					try
					{
					SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd,yyyy");
				    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
				    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
				    System.out.println(format2.format(date));
					return format2.format(date).toString();
					}
					catch(Exception e4)
					{
						try
						{
						SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd. yyyy");
					    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
					    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
					    System.out.println(format2.format(date));
						return format2.format(date).toString();
						}
						catch(Exception e5)
						{
							try
							{
							SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd ,yyyy");
						    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
						    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
						    System.out.println(format2.format(date));
							return format2.format(date).toString();
							}
							catch(Exception e6)
							{
							return "";
							}
						}
						
					}
				}
			}
		}
	}
	
	    public static String firstDayOfMonth(String date,int month) throws Exception 
	    {
	    	//String string = "02/05/2014"; //assuming input
	        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	        Date dt = sdf .parse(date);
	        Calendar c = Calendar.getInstance();
	        c.setTime(dt);
	        //if(portfolioType=="MCH")
	        c.add(Calendar.MONTH, month);  //adding a month directly - gives the start of next month.
	        //else c.add(Calendar.MONTH, 2);
	        c.set(Calendar.DAY_OF_MONTH, 01);
	        String firstDate = sdf.format(c.getTime());
	        System.out.println(firstDate);
	        return firstDate;
	    }
	    public static String getCurrentDateTime()
	    {
	    	currentTime ="";
	    	 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
			 LocalDateTime now = LocalDateTime.now();  
			// System.out.println(dtf.format(now));
			 currentTime = dtf.format(now);
			 return currentTime;
	    }
	    public static String lastDateOfTheMonth(String date) throws Exception
	    {
	    	//String date =RunnerClass.convertDate("January 1, 2023");
	    	LocalDate lastDayOfMonth = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
	    	       .with(TemporalAdjusters.lastDayOfMonth());
	    	String newDate = new SimpleDateFormat("MM/dd/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(lastDayOfMonth.toString()));
	    	return newDate;
	    }
	    public static String monthDifference(String date1, String date2) throws Exception
	    {
	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
	        Date firstDate = sdf.parse(date1);
	        Date secondDate = sdf.parse(date2);

	        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
	                .appendPattern("MM/dd/yyyy")
	                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
	                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
	               // .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
	                .toFormatter();
	        
           String x =  Duration.between( LocalDate.parse(date1,formatter),  LocalDate.parse(date2,formatter)).toString();
			return "";
	    }
	    public static String getCurrentDate()
	    {
	    	currentTime ="";
	    	 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
			 LocalDateTime now = LocalDateTime.now();  
			// System.out.println(dtf.format(now));
			 currentTime = dtf.format(now);
			 return currentTime;
	    }
	    public static boolean onlyDigits(String str)
	    {
			str = str.replace(",", "").replace(".", "").trim();
			if(str=="")
				return false;
			int numberCount =0;
	        for (int i = 0; i < str.length(); i++) 
	        {
	            if (Character.isDigit(str.charAt(i))) 
	            {
	            	numberCount++;
	            	//return true;
	            }
	        }
	        if(numberCount==str.length())
	        return true;
	        else
	        return false;
	    }

	    public static int nthOccurrence(String str1, String str2, int n) 
	    {
	    	    
	            String tempStr = str1;
	            int tempIndex = -1;
	            int finalIndex = 0;
	            for(int occurrence = 0; occurrence < n ; ++occurrence)
	            {
	                tempIndex = tempStr.indexOf(str2);
	                if(tempIndex==-1){
	                    finalIndex = 0;
	                    break;
	                }
	                tempStr = tempStr.substring(++tempIndex);
	                finalIndex+=tempIndex;
	            }
	            return --finalIndex;
	      }
	    public static String extractNumber(String str) 
		{
		    //String str = "26.23,for";
		    StringBuilder myNumbers = new StringBuilder();
		    for (int i = 0; i < str.length(); i++) 
		    {
		    	char c = str.charAt(i);
		    	
		        if (Character.isDigit(str.charAt(i))||(String.valueOf(c).equals(".")&&i!=str.length()-1)) 
		        {
		            myNumbers.append(str.charAt(i));
		            //System.out.println(str.charAt(i) + " is a digit.");
		        } else {
		            //System.out.println(str.charAt(i) + " not a digit.");
		        }
		    }
		   // System.out.println("Your numbers: " + myNumbers.toString());
		    return myNumbers.toString();
		}
	    	public static double round(double value, int places) 
	    	{
	    	    if (places < 0) throw new IllegalArgumentException();

	    	    BigDecimal bd = BigDecimal.valueOf(value);
	    	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    	    return bd.doubleValue();
	    	}
	    
	    public static void processAfterBuildingIsSelected() throws Exception
	    {
	    	if(PropertyWare.downloadLeaseAgreement(buildingAbbreviation, ownerName)==true)
			{
				
				if(PDFReader.readPDFPerMarket(company)==true)
				{
					PropertyWare_updateValues.configureValues();
					PropertyWare_MoveInCharges.addMoveInCharges();
					PropertyWare_AutoCharges.addingAutoCharges();
					PropertyWare_OtherInformation.addOtherInformation();
					
					//Update Completed Status
					if(failedReason=="")
						failedReason="";
					else if(failedReason.charAt(0)==',')
						failedReason = failedReason.substring(1);
					String updateSuccessStatus ="";
					if(statusID==0)
						updateSuccessStatus = "Update [Automation].LeaseInfo Set Status ='Completed', StatusID=4,NotAutomatedFields='"+failedReason+"',LeaseCompletionDate= getDate() where BuildingName like '%"+buildingAbbreviation+"%'";
					else 
						updateSuccessStatus = "Update [Automation].LeaseInfo Set Status ='Review', StatusID=5,NotAutomatedFields='"+failedReason+"',LeaseCompletionDate= getDate() where BuildingName like '%"+buildingAbbreviation+"%'";
				    	DataBase.updateTable(updateSuccessStatus);
				}
				else 
				{
					if(failedReason.charAt(0)==',')
					failedReason = failedReason.substring(1);
					String updateSuccessStatus = "Update [Automation].LeaseInfo Set Status ='Failed', StatusID=3,NotAutomatedFields='"+failedReason+"',LeaseCompletionDate= getDate() where BuildingName like '%"+buildingAbbreviation+"%'";
			    	DataBase.updateTable(updateSuccessStatus);
				}
				
			}
			else 
			{
				if(failedReason.charAt(0)==',')
					failedReason = failedReason.substring(1);
				String updateSuccessStatus = "Update [Automation].LeaseInfo Set Status ='Failed', StatusID=3,NotAutomatedFields='"+failedReason+"',LeaseCompletionDate= getDate() where BuildingName like '%"+buildingAbbreviation+"%'";
		    	DataBase.updateTable(updateSuccessStatus);
			}
	    }
		

}
