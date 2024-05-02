package mainPackage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import DataReader.ReadingLeaseAgreements;

public class PropertyWare_updateValues 
{
	
	private static ThreadLocal<String> startDate_MoveInChargeThreadLocal = new ThreadLocal<>();
	private static ThreadLocal<String> endDate_ProrateRentThreadLocal = new ThreadLocal<>();
	private static ThreadLocal<String> startDate_AutoChargeThreadLocal = new ThreadLocal<>();
	private static ThreadLocal<String> autoCharge_startDate_MonthlyRentThreadLocal = new ThreadLocal<>();  //For other portfolios, it should be added as second full month in Auto Charges 
	private static ThreadLocal<String> endDate_MonthlyRent_WhenIncreasedRentAvailableThreadLocal = new ThreadLocal<>();
	
	
	private static ThreadLocal<Boolean> checkifMoveInDateIsLessThan5DaysToEOMThreadLocal = new ThreadLocal<>();
	private static ThreadLocal<Boolean> petInspectionFeeFlagThreadLocal = new ThreadLocal<>();
	private static ThreadLocal<Boolean> petSecurityDepositFlagThreadLocal = new ThreadLocal<>();
	
	
	public static String increasedRent_previousRentStartDate ="";
	
	public static boolean getPetSecurityDepositFlag() {
		if(petSecurityDepositFlagThreadLocal.get()==null)
			return false;
		else
		 return petSecurityDepositFlagThreadLocal.get();
	}

	public static void setPetSecurityDepositFlag(boolean petSecurityDepositFlag) {
		petSecurityDepositFlagThreadLocal.set(petSecurityDepositFlag);
	}
	
	
	public static boolean getCheckIfMoveInDateIsLessThan5DaysToEOM() {
		if(checkifMoveInDateIsLessThan5DaysToEOMThreadLocal.get()==null)
			return false;
		else
		 return checkifMoveInDateIsLessThan5DaysToEOMThreadLocal.get();
	}

	public static void setCheckIfMoveInDateIsLessThan5DaysToEOM(boolean checkifMoveInDateIsLessThan5DaysToEOM) {
		checkifMoveInDateIsLessThan5DaysToEOMThreadLocal.set(checkifMoveInDateIsLessThan5DaysToEOM);
	}
	
	public static boolean getPetInspectionFeeFlag() {
		if(petInspectionFeeFlagThreadLocal.get()==null)
			return false;
		else
		 return petInspectionFeeFlagThreadLocal.get();
	}

	public static void setPetInspectionFeeFlag(boolean petInspectionFeeFlag) {
		petInspectionFeeFlagThreadLocal.set(petInspectionFeeFlag);
	}
	
	public static String getStartDate_MoveInCharge() {
		if(startDate_MoveInChargeThreadLocal.get()==null)
			return "Error";
		else
		 return startDate_MoveInChargeThreadLocal.get();
	}

	public static void setStartDate_MoveInCharge(String startDate) {
		startDate_MoveInChargeThreadLocal.set(startDate);
	}
	
	public static String getEndDate_ProrateRent() {
		if(endDate_ProrateRentThreadLocal.get()==null)
			return "Error";
		else
		 return endDate_ProrateRentThreadLocal.get();
	}

	public static void setEndDate_ProrateRent(String endDate) {
		endDate_ProrateRentThreadLocal.set(endDate);
	}
	
	public static String getstartDate_AutoCharge() {
		if(startDate_AutoChargeThreadLocal.get()==null)
			return "Error";
		else
		 return startDate_AutoChargeThreadLocal.get();
	}

	public static void setstartDate_AutoCharge(String startDate) {
		startDate_AutoChargeThreadLocal.set(startDate);
	}
	
	public static String getautoCharge_startDate_MonthlyRent() {
		if(autoCharge_startDate_MonthlyRentThreadLocal.get()==null)
			return "Error";
		else
		 return autoCharge_startDate_MonthlyRentThreadLocal.get();
	}

	public static void setautoCharge_startDate_MonthlyRent(String startDate_MonthlyRent) {
		autoCharge_startDate_MonthlyRentThreadLocal.set(startDate_MonthlyRent);
	}
	
	public static String getendDate_MonthlyRent_WhenIncreasedRentAvailable() {
		if(endDate_MonthlyRent_WhenIncreasedRentAvailableThreadLocal.get()==null)
			return "Error";
		else
		 return endDate_MonthlyRent_WhenIncreasedRentAvailableThreadLocal.get();
	}

	public static void setendDate_MonthlyRent_WhenIncreasedRentAvailable(String endDate_MonthlyRent_WhenIncreasedRent) {
		endDate_MonthlyRent_WhenIncreasedRentAvailableThreadLocal.set(endDate_MonthlyRent_WhenIncreasedRent);
	}
	
	
	//ConfigureValues
		public static boolean configureValues(WebDriver driver,String company,String buildingAbbreviation,String SNo) throws Exception
		{
			//Drop the table if exists
			try
			{
				String tableName = "automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo;
				String query = "Drop table if exists "+tableName;
				DataBase.updateTable(query);
			}
			catch(Exception e)
			{}
			
			String failedReason ="";
			//For Arizona - To get Rent Charge codes
			if(company.equals("Arizona"))
			PropertyWare_updateValues.getRentCodeForArizona(driver);
			
			//Create table for the lease with it's SNO
			try {
				String query = "Select * into automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" from automation.LeaseCloseOutsChargeChargesConfiguration";
				DataBase.updateTable(query);
			}
			catch(Exception e) {}
			
			//Clear all values Configuration table first 
			String query1 = "update  automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set Amount=NULL, StartDate=NUll, EndDate=NUll, MoveInCharge=NULL, AutoCharge=NULL, autoCharge_StartDate=NULL";
			DataBase.updateTable(query1);
			
			//If Concession Addendum Available, mention that in the comments
			if(RunnerClass.getconcessionAddendumFlag() == true) 
			{
				failedReason = failedReason+",Concession Addendum is available";
				RunnerClass.setFailedReason(failedReason);
				//DataBase.notAutomatedFields(buildingAbbreviation, "Consession Addendum is available"+'\n');
			}
					
			
			//Compare Start and end Dates in PW with Lease Agreement
			try
			{
				if(RunnerClass.getStartDate().trim().equals(RunnerClass.getStartDateInPW().trim()))
				System.out.println("Start is matched");
				else 
				{
					System.out.println("Start is not matched");
					failedReason = failedReason+",Start is not matched";
					RunnerClass.setFailedReason(failedReason);
				}
				
				if(RunnerClass.getEndDate().trim().equals(RunnerClass.getEndDateInPW().trim()))
					System.out.println("End is matched");
					else 
					{
						System.out.println("End is not matched");
						failedReason = failedReason+",End is not matched";
						RunnerClass.setFailedReason(failedReason);
					}
			}
			catch(Exception e)
			{}
			
			//Update dates as per Move and Auto Charges
			PropertyWare_updateValues.updateDates(company);
			PropertyWare_updateValues.decideMoveInAndAutoCharges(company,buildingAbbreviation,SNo);
			PropertyWare_updateValues.addingValuesToTable(company,buildingAbbreviation,SNo);
			return true;
			}

		public static void updateDates(String company) throws Exception
		{
			String StartDate_MoveInCharge= "";
			String endDate_ProrateRent = "";
			String startDate_AutoCharge = "";
			String autoCharge_startDate_MonthlyRent = "";
			boolean checkifMoveInDateIsLessThan5DaysToEOM=false;
			
			
			//Get all Required dates converted
			String lastDayOfTheStartDate = RunnerClass.lastDateOfTheMonth(RunnerClass.getStartDate());
			String firstFullMonth = RunnerClass.firstDayOfMonth(RunnerClass.getStartDate(),1);
			String secondFullMonth = RunnerClass.firstDayOfMonth(RunnerClass.getStartDate(),2);
			
			//Check if Move In Date is less than 5 days to the End Of the month, if yes, remove prepayment charge from IAG portfolios
			try
			{
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				LocalDate date1 = LocalDate.parse(lastDayOfTheStartDate, dtf);
			    LocalDate date2 = LocalDate.parse( RunnerClass.getStartDate(), dtf);
				long daysBetween = ChronoUnit.DAYS.between(date2, date1);
				if(daysBetween<=5)
					checkifMoveInDateIsLessThan5DaysToEOM = true;
			}
			catch(Exception e)
			{
				checkifMoveInDateIsLessThan5DaysToEOM = false;
			}
			setCheckIfMoveInDateIsLessThan5DaysToEOM(checkifMoveInDateIsLessThan5DaysToEOM);
			StartDate_MoveInCharge  = RunnerClass.getStartDate();
			setStartDate_MoveInCharge(StartDate_MoveInCharge);
			endDate_ProrateRent =  RunnerClass.lastDateOfTheMonth(firstFullMonth);
			setEndDate_ProrateRent(endDate_ProrateRent);
			startDate_AutoCharge = firstFullMonth;
			setstartDate_AutoCharge(startDate_AutoCharge);
			
			if((RunnerClass.getPortfolioType()=="MCH"||RunnerClass.getProrateRent().trim().equals("0.00")||RunnerClass.getProrateRent().trim().equals("Error")||RunnerClass.getProrateRent().trim().equals("0.0"))) //&&PDFReader.checkifMoveInDateIsLessThan5DaysToEOM==true)
			{
				autoCharge_startDate_MonthlyRent = firstFullMonth;
				setautoCharge_startDate_MonthlyRent(autoCharge_startDate_MonthlyRent);
			}
			else {
				autoCharge_startDate_MonthlyRent = secondFullMonth;
				setautoCharge_startDate_MonthlyRent(autoCharge_startDate_MonthlyRent);
			}
			//For Montana
			if(company.equals("Montana"))
			{
			if((!RunnerClass.getProrateRent().trim().equals("0.00")||!RunnerClass.getProrateRent().trim().equals("Error")||!RunnerClass.getProrateRent().trim().equals("0.0"))&&(RunnerClass.getStartDate().split("/")[0].equals("01")||RunnerClass.getStartDate().split("/")[0].equals("1")))
				autoCharge_startDate_MonthlyRent = secondFullMonth;
			else 
				autoCharge_startDate_MonthlyRent = firstFullMonth;
			}
			if(RunnerClass.getIncrementRentFlag()==true)
			{
				setendDate_MonthlyRent_WhenIncreasedRentAvailable(RunnerClass.convertDate(RunnerClass.getIncreasedRent_previousRentEndDate()));
			}
			setautoCharge_startDate_MonthlyRent(autoCharge_startDate_MonthlyRent);
		}
		
		public static boolean addingValuesToTable(String company,String buildingAbbreviation,String SNo)
		{
			String failedReason ="";
			if(getendDate_MonthlyRent_WhenIncreasedRentAvailable() == null) {
				setendDate_MonthlyRent_WhenIncreasedRentAvailable("");
			}
			try
			{
			String query =null;
			for(int i=1;i<=28;i++)
			{
				switch(i)
				{
				case 1:
					query = "Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getProrateRentChargeCode(company)+"',Amount = '"+RunnerClass.getProrateRent()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getEndDate_ProrateRent()+"',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=1";
					break;
				case 2:
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentChargeCode(company)+"',Amount = '"+RunnerClass.getMonthlyRent()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getendDate_MonthlyRent_WhenIncreasedRentAvailable()+"',AutoCharge_StartDate='"+getautoCharge_startDate_MonthlyRent()+"' where ID=2";
					break;
				case 3:
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getTenentAdminReveueChargeCode(company)+"',Amount = '"+RunnerClass.getAdminFee()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getEndDate_ProrateRent()+"',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=3";
					break;
				case 4: 
					String chargeCode=AppConfig.getPetRentChargeCode(company);
					String description = "";
					if(company.equals("Idaho Falls")||company.equals("Utah"))
					{
						if(getPetInspectionFeeFlag()==true)
						{
						chargeCode = AppConfig.getPetRentChargeCode(company).split(",")[1];
						description = "Pet Inspection Fee";
						}
						else
						{
						chargeCode = AppConfig.getPetRentChargeCode(company).split(",")[0];
						description = "Prorate Pet Rent";
						}
					}
					else description = "Prorate Pet Rent";
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+chargeCode+"',Amount = '"+RunnerClass.getproratedPetRent()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getEndDate_ProrateRent()+"',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"',Description = '"+description+"' where ID=4";
					break;
					//query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration Set ChargeCode = '"+AppConfig.getProratePetRentChargeCode(company)+"',Amount = '"+PDFReader.proratedPetRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',Description = '"+PDFReader.proratePetRentDescription+"' where ID=4";	
					//break;
				case 5:
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getpetSecurityDepositChargeCode(company)+"',Amount = '"+RunnerClass.getPetSecurityDeposit()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=5";
					break;
				case 6:
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getpetOneTimeNonRefundableChargeCode(company)+"',Amount = '"+RunnerClass.getPetOneTimeNonRefundableFee()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=6";
					break;
				case 7: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getHVACAirFilterFeeChargeCode(company)+"',Amount = '"+RunnerClass.getairFilterFee()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=7";
					break;
				case 8: 
					String chargeCode2=AppConfig.getPetRentChargeCode(company);
					String description2 = "";
					if(company.equals("Idaho Falls")||company.equals("Utah"))
					{
						if(getPetInspectionFeeFlag()==true)
						{
						chargeCode2 = AppConfig.getPetRentChargeCode(company).split(",")[1];
						description2 = "Pet Inspection Fee";
						}
						else
						{
						chargeCode2 = AppConfig.getPetRentChargeCode(company).split(",")[0];
						description2 = "Pet Rent";
						}
					}
					else description2 = "Pet Rent";
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+chargeCode2+"',Amount = '"+RunnerClass.getPetRent()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"',Description = '"+description2+"' where ID=8";
					break;
				case 9: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getPrepaymentChargeCode(company)+"',Amount = '"+RunnerClass.getprepaymentCharge()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=9";
					break;
				case 10: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getIncreasedRentChargeCode(company)+"',Amount = '"+RunnerClass.getIncreasedRent_amount()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+RunnerClass.getIncreasedRent_newStartDate()+"' where ID=10";
					break;
				case 11: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getResidentBenefitsPackageChargeCode(company)+"',Amount = '"+RunnerClass.getresidentBenefitsPackage()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=11";
					break;
				case 12: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getPrepaymentChargeCode(company)+"',Amount = '"+RunnerClass.getMonthlyRent()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=12";
					break;	
				case 13: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getResidentUtilityBillChargeCode(company)+"',Amount = '"+RunnerClass.getProrateRUBS()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=13";
					break;
				case 14: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getResidentUtilityBillChargeCode(company)+"',Amount = '"+RunnerClass.getRUBS()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=14";
				    break;
				case 15: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentTaxCode(company)+"',Amount = '"+RunnerClass.getProrateRent()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=15";
					break;
				case 16: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentTaxCode(company)+"',Amount = '"+RunnerClass.getMonthlyRent()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=16";
					break;
				case 17: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getPetRentTaxCode(company)+"',Amount = '"+RunnerClass.getproratedPetRent()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=17";
					break;
				case 18: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getProratePetRentTaxCode(company)+"',Amount = '"+RunnerClass.getPetRent()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=18";
					break;
				case 19: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getProrateRentGETCode(company)+"',Amount = '"+PDFReader.getProrateRentGET()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getEndDate_ProrateRent()+"',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=19";
					break;
				case 20: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentGETCode(company)+"',Amount = '"+RunnerClass.getMonthlyRentTaxAmount()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getautoCharge_startDate_MonthlyRent()+"' where ID=20";
					break;
				case 21: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentTaxCode(company)+"',Amount = '"+RunnerClass.getOnePercentOfRentAmount()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getautoCharge_startDate_MonthlyRent()+"' where ID=21";
					break;
				case 22: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentTaxCode(company)+"',Amount = '"+RunnerClass.getOnePercentOfProrateRentAmount()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getEndDate_ProrateRent()+"',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=22";
					break;
				case 23: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getPetRentTaxCode(company)+"',Amount = '"+RunnerClass.getOnePercentOfPetRentAmount()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=23";
					break;
				case 24: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getPetRentTaxCode(company)+"',Amount = '"+RunnerClass.getOnePercentOfProratePetRentAmount()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=24";
					break;
				case 25: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getSmartHomeAgreementCode(company)+"',Amount = '"+RunnerClass.getSmartHomeAgreementFee()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=25";
					break;
				case 26: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getCaptiveInsurenceATXChargeCode(company)+"',Amount = '"+RunnerClass.getCaptiveInsurenceATXFeeThreadLocal()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=26";
					break;
				case 27: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getResidentBenefitsPackageTaxChargeCode(company)+"',Amount = '"+RunnerClass.getResidentBenefitsPackageTaxAmount()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=27";
					break;
				case 28: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getResidentBenefitsPackageChargeCode(company)+"',Amount = '"+RunnerClass.getProrateResidentBenefitPackage()+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=28";
					break;
				}
			}
			DataBase.updateTable(query);
			return true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("Issue in adding values to Auto charges table");
				failedReason =  failedReason+","+"Internal Error - consolidating auto charges";
				RunnerClass.setFailedReason(failedReason);
				return false;
			}
		}
		
		public static void decideMoveInAndAutoCharges(String company,String buildingAbbreviation,String SNo)
		{
			String moveInCharges ="";
			String autoCharges = "";
			String proratePetRentDescription = "";
			boolean proratedRentDateIsInMoveInMonthFlag = false;
			
			//Decide Prepayment Charge
			proratePetRentDescription = "Prorate Pet Rent";
			String prepaymentChargeOrMonthlyRent;
			if(PropertyWare_updateValues.checkProratedRentDateIsInMoveInMonth()==true)
			{
				proratedRentDateIsInMoveInMonthFlag =true;
				if(RunnerClass.getProrateRentDate().equalsIgnoreCase("n/a")||RunnerClass.getProrateRentDate().equalsIgnoreCase("na")||RunnerClass.getProrateRentDate().equalsIgnoreCase("N/A")||RunnerClass.getProrateRentDate().equalsIgnoreCase("NA")||RunnerClass.getProrateRentDate().equalsIgnoreCase("n/a.")||RunnerClass.getProrateRentDate().equalsIgnoreCase("Error"))
				{
					prepaymentChargeOrMonthlyRent = "2";
					RunnerClass.setproratedPetRent(RunnerClass.getPetRent());
					proratePetRentDescription = "Pet Rent";
					RunnerClass.setProrateRUBS(RunnerClass.getRUBS());
				}
				else
				{
					prepaymentChargeOrMonthlyRent = "12";
					proratePetRentDescription = "Prorate Pet Rent";
				}
				
			}
			else 
				prepaymentChargeOrMonthlyRent = "9";
			/*
			//If Market is Boise, Utah, Idaho falls
			if(company.equals("Boise"))
			PropertyWare_updateValues.specificMarketMoveInAndAutoChargesAssignment(moveInCharges, autoCharges, prepaymentChargeOrMonthlyRent);
			else
			{*/
			
			
			if(RunnerClass.getPortfolioType()=="MCH"||company.equals("Montana"))
			{
				
				if(RunnerClass.getpetFlag()==false)
				{
					if(RunnerClass.getresidentBenefitsPackageAvailabilityCheckFlag()==true)
					{
						moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3,11";
						if(RunnerClass.getIncrementRentFlag() == true)
						autoCharges = "2,11,10";
						else autoCharges = "2,11";
					}
					else
					{
					moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3";
					if(RunnerClass.getIncrementRentFlag() == true)
					autoCharges = "2,7,10";
					else autoCharges = "2,7";
					}
					//DataBase.assignChargeCodes(moveInCharges, autoCharges);	
				}
				else
				{
					if(RunnerClass.getpetFlag()==true&&getPetSecurityDepositFlag()==false)
					{
						if(RunnerClass.getresidentBenefitsPackageAvailabilityCheckFlag()==true)
						{
						moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3,4,6,11";
						if(RunnerClass.getIncrementRentFlag() == true)
						autoCharges = "2,11,8,10";
						else autoCharges = "2,11,8";
						}
						else
						{
							moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3,4,6";
							if(RunnerClass.getIncrementRentFlag() == true)
							autoCharges = "2,7,8,10";
							else autoCharges = "2,7,8";
						}
						//DataBase.assignChargeCodes(moveInCharges, autoCharges);
					}
				    else
				    {
						if(RunnerClass.getpetFlag()==true&&getPetSecurityDepositFlag()==true)
						{
							if(RunnerClass.getresidentBenefitsPackageAvailabilityCheckFlag()==true)
							{
								moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3,4,5,11";
								if(RunnerClass.getIncrementRentFlag() == true)
								autoCharges = "2,11,8,10";
								else autoCharges = "2,11,8";
							}
							else
							{
							moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3,4,5";
							if(RunnerClass.getIncrementRentFlag() == true)
							autoCharges = "2,7,8,10";
							else autoCharges = "2,7,8";
							}
							//DataBase.assignChargeCodes(moveInCharges, autoCharges);
						}
				    }
				}
				if(company.equals("Montana"))
				{
				//If Company is Montana, do not add Prepayment charge
				if(moveInCharges.contains("9"))
					moveInCharges = moveInCharges.replace(",9", "");
				if(moveInCharges.contains("12"))
				moveInCharges = moveInCharges.replace(",12", "");
				//if(moveInCharges.contains(",2,"))
					//moveInCharges = moveInCharges.replace(",2", "");
				}
			}
			//Other Portfolios
			else 
			{
				
				
				if(RunnerClass.getPortfolioType()=="Others"&&RunnerClass.getpetFlag()==false)
				{
					if(proratedRentDateIsInMoveInMonthFlag == true)
					{
						if(RunnerClass.getresidentBenefitsPackageAvailabilityCheckFlag()==true)
						{
							moveInCharges = "1,2,3,11";
							autoCharges = "2,11";	
						}
						else
						{
						moveInCharges = "1,2,3";
						autoCharges = "2,7";
						}
					}
					else
					{
						if(RunnerClass.getresidentBenefitsPackageAvailabilityCheckFlag()==true)
						{
					     moveInCharges = "2,3,11";
					     autoCharges = "1,2,11";
						}
						else
						{
							moveInCharges = "2,3";
						     autoCharges = "1,2,7";
						}
					}
					//DataBase.assignChargeCodes(moveInCharges, autoCharges);
					//DataBase.assignChargeCodes(moveInCharges, autoCharges);
				}
				else
				{
					if(RunnerClass.getpetFlag()==true&&getPetSecurityDepositFlag()==false)
					{
						if(proratedRentDateIsInMoveInMonthFlag == true)
						{
							if(RunnerClass.getresidentBenefitsPackageAvailabilityCheckFlag()==true)
							{
								moveInCharges = "1,2,3,4,6,11";
								autoCharges = "2,11,8";
							}
							else
							{
							moveInCharges = "1,2,3,4,6";
							autoCharges = "2,7,8";
							}
						}
						else
						{
							if(RunnerClass.getresidentBenefitsPackageAvailabilityCheckFlag()==true)
							{
								moveInCharges = "2,3,4,6,11";
								autoCharges = "1,2,11,8";
							}
							else
							{
						      moveInCharges = "2,3,4,6";
						      autoCharges = "1,2,7,8";
							}
						}
						//DataBase.assignChargeCodes(moveInCharges, autoCharges);
					}
					else//(PDFReader.portfolioType=="Others"&&PDFReader.petFlag==true&&PDFReader.petSecurityDepositFlag==true)
					{
						if(RunnerClass.getresidentBenefitsPackageAvailabilityCheckFlag()==true)
						{
							moveInCharges = "2,3,4,5,11";
							autoCharges = "1,2,11,8";
						}
						else
						{
						moveInCharges = "2,3,4,5";
						autoCharges = "1,2,7,8";
						}
						//DataBase.assignChargeCodes(moveInCharges, autoCharges);
					}
				}
				
			}
			//If RBP flag is false, HVAC flag should also be false as we are not adding HVAC value anymore
			if(RunnerClass.getresidentBenefitsPackageAvailabilityCheckFlag()==false&&!company.equals("Chicago"))
			{
				RunnerClass.setHVACFilterFlag(false);
				if(autoCharges.contains(",7"))
					autoCharges = autoCharges.replace(",7", "");
			}
			
			//If Company is Spokane, do not add Admin fee at Move In, add it in Auto charges with first full month and End Date
			if(company.equals("Spokane"))
			{
				if(moveInCharges.contains(",3"))
				{
					moveInCharges = moveInCharges.replace(",3", "");
				    autoCharges = autoCharges+",3";
				}
			}
			
			//If Move In Date is less than 5 days to the end of the month, remove prepayments charge from the move in charges
			if(RunnerClass.getPortfolioType()=="MCH"&&getCheckIfMoveInDateIsLessThan5DaysToEOM()==true)
			{
				if(moveInCharges.contains(",9"))
				moveInCharges = moveInCharges.replace(",9", "");
				if(moveInCharges.contains(",12"))
					moveInCharges = moveInCharges.replace(",12", "");
			}
			
			
			//If Company is Boise,Idaho Falls,Utah and California, add RUBS charge
			if((company.equals("Boise")||company.equals("Idaho Falls")||company.equals("Utah")||company.equals("Montana")||company.equals("California")||company.equals("California PFW"))&&RunnerClass.getResidentUtilityBillFlag()==true&&(!RunnerClass.getProrateRUBS().equals("Error")&&!RunnerClass.getRUBS().equals("Error")))
			{
				moveInCharges = moveInCharges+",13";
				autoCharges = autoCharges+",14";
			}
			//Alabama Monthly Rent tax Charge changing
			if(company.equals("Alabama")&&RunnerClass.getMonthlyRentTaxFlag()==true)
			{
				moveInCharges = moveInCharges.replace("1,", "15,").replace("2,", "16,");
				autoCharges = autoCharges.replace("2,", "16,");
			}
			
			//Alabama Pet Rent tax Charge changing
			if(company.equals("Alabama")&&RunnerClass.getpetFlag()==true&&RunnerClass.getPetRentTaxFlag()==true)
			{
				moveInCharges = moveInCharges.replace(",4", ",17");
				autoCharges = autoCharges.replace(",8", ",18");
			}
			
			//Hawaii Monthly Rent tax Charge changing
			if(company.equals("Hawaii")&&RunnerClass.getMonthlyRentTaxFlag()==true)
			{
				// Create a map to store replacement values for each number
		        Map<String, String> replacements = new HashMap<>();
		        replacements.put("1", "1,19");
		        replacements.put("2", "2,20");
		        //Move In Charges
		        String replacedString = replaceNumbers(moveInCharges, replacements);
		        moveInCharges = replacedString;
		        //Auto Charges
		        String replacedString2 = replaceNumbers(autoCharges, replacements);
		        autoCharges = replacedString2;
				
		
			}
			
			// Arizona
			if(company.equals("Arizona")&&RunnerClass.getMonthlyRentTaxFlag()==true)
			{
				
		        
		        // Create a map to store replacement values for each number
		        Map<String, String> replacements = new HashMap<>();
		        replacements.put("1", "1,22");
		        replacements.put("2", "2,21");
		        replacements.put("8", "8,23");
		        replacements.put("4", "4,24");
		        //Move In Charges
		        String replacedString = replaceNumbers(moveInCharges, replacements);
		        moveInCharges = replacedString;
		        //Auto Charges
		        String replacedString2 = replaceNumbers(autoCharges, replacements);
		        autoCharges = replacedString2;
		        
		        
			}
			
			//If Smart Home Agreement is available and Portfolio type is ATX, add that charge in both Move in Auto Charges
			if(RunnerClass.getSmartHomeAgreementCheck()==true&&RunnerClass.getPortfolioName().contains("ATX."))
			{
				moveInCharges = moveInCharges+",25";
				autoCharges = autoCharges+",25";
			}
			//If Option 1 is selected in RBP Lease Agreement, then add Captive Insurence ATX charge
			if(RunnerClass.getCaptiveInsurenceATXFlag()==true)
			{
				moveInCharges = moveInCharges+",26";
				autoCharges = autoCharges+",26";
			}
			
	/*		//If RBP Has tax amount, add RBP in two charges
			if(RunnerClass.getResidentBenefitsPackageTaxAvailabilityCheck()==true) 
			{
				 // Create a map to store replacement values for each number
		        Map<String, String> replacements = new HashMap<>();
		        replacements.put("11", "11,27");
		        //Move In Charges
		        String replacedString = replaceNumbers(moveInCharges, replacements);
		        moveInCharges = replacedString;
		        //Auto Charges
		        String replacedString2 = replaceNumbers(autoCharges, replacements);
		        autoCharges = replacedString2;
			} */
			
			// If RBP amount is 49.95, then we need to Add Prorate RBP amount in Move in charges
			if ((RunnerClass.getresidentBenefitsPackage().trim().contains("49.95")|| (RunnerClass.getPortfolioName().contains("ATX.")&& RunnerClass.getresidentBenefitsPackage().trim().contains("39.00")))&& RunnerClass.getresidentBenefitsPackageAvailabilityCheckFlag() == true||(company.equals("Montana")&&RunnerClass.getresidentBenefitsPackage().trim().contains("44.95"))) 
			{
				Map<String, String> replacements = new HashMap<>();
				replacements.put("11", "28");
				// Move In Charges
				String replacedString = replaceNumbers(moveInCharges, replacements);
				moveInCharges = replacedString;
			}
			
			//If RBP opt out addendum is available, do not add RBP charges at all.
			if(RunnerClass.getRBPOptOutAddendumCheck()==true)
			{
				Map<String, String> replacements = new HashMap<>();
		        replacements.put("11", "");
		        replacements.put("28", "");
		        //Move In Charges
		        String replacedString = replaceNumbers(moveInCharges, replacements);
		        moveInCharges = replacedString;
		        //Auto Charges
		        String replacedString2 = replaceNumbers(autoCharges, replacements);
		        autoCharges = replacedString2;
			}
			try
			{
				moveInCharges = RunnerClass.replaceConsecutiveCommas(moveInCharges);
				autoCharges = RunnerClass.replaceConsecutiveCommas(autoCharges);
			}
			catch(Exception e)
			{}
			
			DataBase.assignChargeCodes(moveInCharges, autoCharges,buildingAbbreviation,SNo);
		}
		
		
		
		public static boolean checkProratedRentDateIsInMoveInMonth()
		{
			try
			{
			if(RunnerClass.getProrateRentDate().equalsIgnoreCase("n/a")||RunnerClass.getProrateRentDate().equalsIgnoreCase("na")||RunnerClass.getProrateRentDate().equalsIgnoreCase("n/a.") ||RunnerClass.getProrateRentDate().equalsIgnoreCase("Error"))
				return true;
			//if(RunnerClass.getProrateRentDate()==null||RunnerClass.getProrateRentDate().equalsIgnoreCase("n/a")||RunnerClass.getProrateRentDate()=="Error")
				//return false;
			String proratedDate = RunnerClass.convertDate(RunnerClass.getProrateRentDate());
			String proratedMonth = proratedDate.split("/")[0];
			String moveInDate = RunnerClass.getStartDate();
			String moveInMonth = moveInDate.split("/")[0];
			if(proratedMonth.equalsIgnoreCase(moveInMonth)||Double.parseDouble(RunnerClass.getProrateRent())<=200.00)
			{
				return true;
			}
			else return false;
			}
			catch(Exception e)
			{
				return false;
			}
		}
		
		
		public static void getRentCodeForArizona(WebDriver driver) throws Exception
		{
			Actions actions = new Actions(driver);
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			driver.findElement(Locators.ledgerTab).click();
			Thread.sleep(2000);
			actions.sendKeys(Keys.ESCAPE).build().perform();
			driver.findElement(Locators.newCharge).click();
			Thread.sleep(2000);
			//Account code
			driver.findElement(Locators.accountDropdown).click();
			List<WebElement> chargeCodes = driver.findElements(Locators.chargeCodesList);
			for(int i=0;i<chargeCodes.size();i++)
			{
				String code = chargeCodes.get(i).getText();
				if(code.contains(RunnerClass.getArizonaCityFromBuildingAddress()))
				{
					RunnerClass.setArizonaRentCode(code);
					RunnerClass.setArizonaCodeAvailable(true);
					break;
					
				}
				
			}
			//Closing Dropdown
			driver.findElement(Locators.accountDropdown).click();
			driver.findElement(Locators.moveInChargeCancel).click();
			
		}
		
		 public static String replaceNumbers(String input, Map<String, String> replacements) 
		    {
		        // Split the input string by commas to get individual numbers
		        String[] numbers = input.split(",");
		        
		        // Initialize a StringBuilder to build the replaced string
		        StringBuilder replacedString = new StringBuilder();
		        
		        // Iterate through the numbers and replace them
		        for (String number : numbers) {
		            // Check if the number has a replacement in the map
		            if (replacements.containsKey(number)) {
		                replacedString.append(replacements.get(number));
		            } else {
		                // If no replacement is found, keep the original number
		                replacedString.append(number);
		            }
		            
		            // Append a comma to separate numbers
		            replacedString.append(",");
		        }
		        
		        // Remove the trailing comma
		        if (replacedString.length() > 0) {
		            replacedString.deleteCharAt(replacedString.length() - 1);
		        }
		        
		        return replacedString.toString();
		    }

		
}
