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
	
	public static String autoCharge_startDate_MonthlyRent = ""; //For other portfolios, it should be added as second full month in Auto Charges 
	public static String increasedRent_previousRentStartDate ="";
	public static String endDate_MonthlyRent_WhenIncreasedRentAvailable = "";
	
	
	public static String getStartDate_MoveInCharge() {
		 return startDate_MoveInChargeThreadLocal.get();
	}

	public static void setStartDate_MoveInCharge(String startDate) {
		startDate_MoveInChargeThreadLocal.set(startDate);
	}
	
	public static String getEndDate_ProrateRent() {
		 return endDate_ProrateRentThreadLocal.get();
	}

	public static void setEndDate_ProrateRent(String endDate) {
		endDate_ProrateRentThreadLocal.set(endDate);
	}
	
	public static String getstartDate_AutoCharge() {
		 return startDate_AutoChargeThreadLocal.get();
	}

	public static void setstartDate_AutoCharge(String startDate) {
		startDate_AutoChargeThreadLocal.set(startDate);
	}
	
	
	//ConfigureValues
		public static boolean configureValues(WebDriver driver,String company,String buildingAbbreviation,String SNo) throws Exception
		{
			String failedReason ="";
			//For Arizona - To get Rent Charge codes
			if(company.equals("Arizona"))
			PropertyWare_updateValues.getRentCodeForArizona(driver);
			try {
				String query = "Select * into automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" from automation.LeaseCloseOutsChargeChargesConfiguration";
				DataBase.updateTable(query);
			}
			catch(Exception e) {}
			
			//Clear all values Configuration table first 
			String query1 = "update  automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set Amount=NULL, StartDate=NUll, EndDate=NUll, MoveInCharge=NULL, AutoCharge=NULL, autoCharge_StartDate=NULL";
			DataBase.updateTable(query1);
			
			//If Concession Addendum Available, mention that in the comments
			if(PDFReader.concessionAddendumFlag == true) 
			{
				failedReason = failedReason+",Concession Addendum is available";
				//DataBase.notAutomatedFields(buildingAbbreviation, "Consession Addendum is available"+'\n');
			}
					
			
			//Compare Start and end Dates in PW with Lease Agreement
			try
			{
				if(RunnerClass.getStartDate().trim().equals(RunnerClass.startDateInPW.trim()))
				System.out.println("Start is matched");
				else 
				{
					System.out.println("Start is not matched");
					failedReason = failedReason+",Start is not matched";
				}
				
				if(RunnerClass.getEndDate().trim().equals(RunnerClass.endDateInPW.trim()))
					System.out.println("End is matched");
					else 
					{
						System.out.println("End is not matched");
						failedReason = failedReason+",End is not matched";
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
					PDFReader.checkifMoveInDateIsLessThan5DaysToEOM = true;
			}
			catch(Exception e)
			{
				PDFReader.checkifMoveInDateIsLessThan5DaysToEOM = false;
			}
			
			StartDate_MoveInCharge  = RunnerClass.getStartDate();
			setStartDate_MoveInCharge(StartDate_MoveInCharge);
			endDate_ProrateRent =  RunnerClass.lastDateOfTheMonth(firstFullMonth);
			setEndDate_ProrateRent(endDate_ProrateRent);
			startDate_AutoCharge = firstFullMonth;
			setstartDate_AutoCharge(startDate_AutoCharge);
			if((RunnerClass.portfolioType=="MCH"||RunnerClass.getProrateRent().trim().equals("0.00")||RunnerClass.getProrateRent().trim().equals("Error")||RunnerClass.getProrateRent().trim().equals("0.0"))) //&&PDFReader.checkifMoveInDateIsLessThan5DaysToEOM==true)
				autoCharge_startDate_MonthlyRent = firstFullMonth;
			else 
				autoCharge_startDate_MonthlyRent = secondFullMonth;
			//For Montana
			if(company.equals("Montana"))
			{
			if((!RunnerClass.getProrateRent().trim().equals("0.00")||!RunnerClass.getProrateRent().trim().equals("Error")||!RunnerClass.getProrateRent().trim().equals("0.0"))&&(RunnerClass.getStartDate().split("/")[0].equals("01")||RunnerClass.getStartDate().split("/")[0].equals("1")))
				autoCharge_startDate_MonthlyRent = secondFullMonth;
			else 
				autoCharge_startDate_MonthlyRent = firstFullMonth;
			}
			if(PDFReader.incrementRentFlag==true)
			{
				endDate_MonthlyRent_WhenIncreasedRentAvailable = RunnerClass.convertDate(PDFReader.increasedRent_previousRentEndDate);
			}
			
		}
		
		public static boolean addingValuesToTable(String company,String buildingAbbreviation,String SNo)
		{
			String failedReason ="";
			try
			{
			String query =null;
			for(int i=1;i<=27;i++)
			{
				switch(i)
				{
				case 1:
					query = "Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getProrateRentChargeCode(company)+"',Amount = '"+PDFReader.proratedRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getEndDate_ProrateRent()+"',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=1";
					break;
				case 2:
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentChargeCode(company)+"',Amount = '"+PDFReader.monthlyRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+endDate_MonthlyRent_WhenIncreasedRentAvailable+"',AutoCharge_StartDate='"+autoCharge_startDate_MonthlyRent+"' where ID=2";
					break;
				case 3:
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getTenentAdminReveueChargeCode(company)+"',Amount = '"+PDFReader.adminFee+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getEndDate_ProrateRent()+"',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=3";
					break;
				case 4: 
					String chargeCode=AppConfig.getPetRentChargeCode(company);
					String description = "";
					if(company.equals("Idaho Falls")||company.equals("Utah"))
					{
						if(PDFReader.petInspectionFeeFlag==true)
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
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+chargeCode+"',Amount = '"+PDFReader.proratedPetRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getEndDate_ProrateRent()+"',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"',Description = '"+description+"' where ID=4";
					break;
					//query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration Set ChargeCode = '"+AppConfig.getProratePetRentChargeCode(company)+"',Amount = '"+PDFReader.proratedPetRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',Description = '"+PDFReader.proratePetRentDescription+"' where ID=4";	
					//break;
				case 5:
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getpetSecurityDepositChargeCode(company)+"',Amount = '"+PDFReader.petSecurityDeposit+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=5";
					break;
				case 6:
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getpetOneTimeNonRefundableChargeCode(company)+"',Amount = '"+PDFReader.petOneTimeNonRefundableFee+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=6";
					break;
				case 7: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration Set ChargeCode = '"+AppConfig.getHVACAirFilterFeeChargeCode(company)+"',Amount = '"+PDFReader.airFilterFee+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=7";
					break;
				case 8: 
					String chargeCode2=AppConfig.getPetRentChargeCode(company);
					String description2 = "";
					if(company.equals("Idaho Falls")||company.equals("Utah"))
					{
						if(PDFReader.petInspectionFeeFlag==true)
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
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+chargeCode2+"',Amount = '"+PDFReader.petRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"',Description = '"+description2+"' where ID=8";
					break;
				case 9: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getPrepaymentChargeCode(company)+"',Amount = '"+PDFReader.prepaymentCharge+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=9";
					break;
				case 10: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getIncreasedRentChargeCode(company)+"',Amount = '"+PDFReader.increasedRent_amount+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+PDFReader.increasedRent_newStartDate+"' where ID=10";
					break;
				case 11: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getResidentBenefitsPackageChargeCode(company)+"',Amount = '"+PDFReader.residentBenefitsPackage+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=11";
					break;
				case 12: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getPrepaymentChargeCode(company)+"',Amount = '"+PDFReader.monthlyRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=12";
					break;	
				case 13: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getResidentUtilityBillChargeCode(company)+"',Amount = '"+PDFReader.prorateRUBS+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=13";
					break;
				case 14: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getResidentUtilityBillChargeCode(company)+"',Amount = '"+PDFReader.RUBS+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=14";
				    break;
				case 15: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentTaxCode(company)+"',Amount = '"+PDFReader.proratedRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=15";
					break;
				case 16: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentTaxCode(company)+"',Amount = '"+PDFReader.monthlyRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=16";
					break;
				case 17: 
					query = query+"\n Update aautomation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getPetRentTaxCode(company)+"',Amount = '"+PDFReader.proratedPetRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=17";
					break;
				case 18: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getProratePetRentTaxCode(company)+"',Amount = '"+PDFReader.petRent+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=18";
					break;
				case 19: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getProrateRentGETCode(company)+"',Amount = '"+PDFReader.prorateRentGET+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getEndDate_ProrateRent()+"',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=19";
					break;
				case 20: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentGETCode(company)+"',Amount = '"+PDFReader.monthlyRentTaxAmount+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+autoCharge_startDate_MonthlyRent+"' where ID=20";
					break;
				case 21: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentTaxCode(company)+"',Amount = '"+PDFReader.OnePercentOfRentAmount+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+autoCharge_startDate_MonthlyRent+"' where ID=21";
					break;
				case 22: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getMonthlyRentTaxCode(company)+"',Amount = '"+PDFReader.OnePercentOfProrateRentAmount+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='"+getEndDate_ProrateRent()+"',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=22";
					break;
				case 23: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getPetRentTaxCode(company)+"',Amount = '"+PDFReader.OnePercentOfPetRentAmount+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=23";
					break;
				case 24: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getPetRentTaxCode(company)+"',Amount = '"+PDFReader.OnePercentOfProratePetRentAmount+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=24";
					break;
				case 25: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getSmartHomeAgreementCode(company)+"',Amount = '"+PDFReader.smartHomeAgreementFee+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=25";
					break;
				case 26: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getCaptiveInsurenceATXChargeCode(company)+"',Amount = '"+PDFReader.captiveInsurenceATXFee+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=26";
					break;
				case 27: 
					query = query+"\n Update automation.LeaseCloseOutsChargeChargesConfiguration_"+SNo+" Set ChargeCode = '"+AppConfig.getResidentBenefitsPackageTaxChargeCode(company)+"',Amount = '"+PDFReader.residentBenefitsPackageTaxAmount+"',StartDate='"+getStartDate_MoveInCharge()+"',EndDate='',AutoCharge_StartDate='"+getstartDate_AutoCharge()+"' where ID=27";
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
				return false;
			}
		}
		
		public static void decideMoveInAndAutoCharges(String company,String buildingAbbreviation,String SNo)
		{
			String moveInCharges ="";
			String autoCharges = "";
			
			//Decide Prepayment Charge
			PDFReader.proratePetRentDescription = "Prorate Pet Rent";
			String prepaymentChargeOrMonthlyRent;
			if(PropertyWare_updateValues.checkProratedRentDateIsInMoveInMonth()==true)
			{
				PDFReader.proratedRentDateIsInMoveInMonthFlag =true;
				if(PDFReader.proratedRentDate.equalsIgnoreCase("n/a")||PDFReader.proratedRentDate.equalsIgnoreCase("na")||PDFReader.proratedRentDate.equalsIgnoreCase("N/A")||PDFReader.proratedRentDate.equalsIgnoreCase("NA")||PDFReader.proratedRentDate.equalsIgnoreCase("n/a."))
				{
					prepaymentChargeOrMonthlyRent = "2";
					PDFReader.proratedPetRent = PDFReader.petRent;
					PDFReader.proratePetRentDescription = "Pet Rent";
					PDFReader.prorateRUBS = PDFReader.RUBS;
				}
				else
				{
					prepaymentChargeOrMonthlyRent = "12";
					PDFReader.proratePetRentDescription = "Prorate Pet Rent";
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
			if(RunnerClass.portfolioType=="MCH"||company.equals("Montana"))
			{
				
				if(PDFReader.petFlag==false)
				{
					if(PDFReader.residentBenefitsPackageAvailabilityCheck==true)
					{
						moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3,11";
						if(PDFReader.incrementRentFlag == true)
						autoCharges = "2,11,10";
						else autoCharges = "2,11";
					}
					else
					{
					moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3";
					if(PDFReader.incrementRentFlag == true)
					autoCharges = "2,7,10";
					else autoCharges = "2,7";
					}
					//DataBase.assignChargeCodes(moveInCharges, autoCharges);	
				}
				else
				{
					if(PDFReader.petFlag==true&&PDFReader.petSecurityDepositFlag==false)
					{
						if(PDFReader.residentBenefitsPackageAvailabilityCheck==true)
						{
						moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3,4,6,11";
						if(PDFReader.incrementRentFlag == true)
						autoCharges = "2,11,8,10";
						else autoCharges = "2,11,8";
						}
						else
						{
							moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3,4,6";
							if(PDFReader.incrementRentFlag == true)
							autoCharges = "2,7,8,10";
							else autoCharges = "2,7,8";
						}
						//DataBase.assignChargeCodes(moveInCharges, autoCharges);
					}
				    else
				    {
						if(PDFReader.petFlag==true&&PDFReader.petSecurityDepositFlag==true)
						{
							if(PDFReader.residentBenefitsPackageAvailabilityCheck==true)
							{
								moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3,4,5,11";
								if(PDFReader.incrementRentFlag == true)
								autoCharges = "2,11,8,10";
								else autoCharges = "2,11,8";
							}
							else
							{
							moveInCharges = "1,"+prepaymentChargeOrMonthlyRent+",3,4,5";
							if(PDFReader.incrementRentFlag == true)
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
				
				if(RunnerClass.portfolioType=="Others"&&PDFReader.petFlag==false)
				{
					if(PDFReader.proratedRentDateIsInMoveInMonthFlag == true)
					{
						if(PDFReader.residentBenefitsPackageAvailabilityCheck==true)
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
						if(PDFReader.residentBenefitsPackageAvailabilityCheck==true)
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
					if(PDFReader.petFlag==true&&PDFReader.petSecurityDepositFlag==false)
					{
						if(PDFReader.proratedRentDateIsInMoveInMonthFlag == true)
						{
							if(PDFReader.residentBenefitsPackageAvailabilityCheck==true)
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
							if(PDFReader.residentBenefitsPackageAvailabilityCheck==true)
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
						if(PDFReader.residentBenefitsPackageAvailabilityCheck==true)
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
			if(PDFReader.residentBenefitsPackageAvailabilityCheck==false&&!company.equals("Chicago"))
			{
				PDFReader.HVACFilterFlag = false;
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
			if(RunnerClass.portfolioType=="MCH"&&PDFReader.checkifMoveInDateIsLessThan5DaysToEOM==true)
			{
				if(moveInCharges.contains(",9"))
				moveInCharges = moveInCharges.replace(",9", "");
				if(moveInCharges.contains(",12"))
					moveInCharges = moveInCharges.replace(",12", "");
			}
			
			//If Company is Boise,Idaho Falls,Utah and California, add RUBS charge
			if((company.equals("Boise")||company.equals("Idaho Falls")||company.equals("Utah")||company.equals("Montana")||company.equals("California")||company.equals("California PFW"))&&PDFReader.residentUtilityBillFlag==true&&(!PDFReader.prorateRUBS.equals("Error")&&!PDFReader.RUBS.equals("Error")))
			{
				moveInCharges = moveInCharges+",13";
				autoCharges = autoCharges+",14";
			}
			//Alabama Monthly Rent tax Charge changing
			if(company.equals("Alabama")&&PDFReader.monthlyRentTaxFlag==true)
			{
				moveInCharges = moveInCharges.replace("1,", "15,").replace("2,", "16,");
				autoCharges = autoCharges.replace("2,", "16,");
			}
			
			//Alabama Pet Rent tax Charge changing
			if(company.equals("Alabama")&&PDFReader.petFlag==true&&PDFReader.petRentTaxFlag==true)
			{
				moveInCharges = moveInCharges.replace(",4", ",17");
				autoCharges = autoCharges.replace(",8", ",18");
			}
			
			//Hawaii Monthly Rent tax Charge changing
			if(company.equals("Hawaii")&&PDFReader.monthlyRentTaxFlag==true)
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
				
				/*
				
				String[] moveInCodes = moveInCharges.split(",");
				for(int i=0;i<moveInCodes.length;i++)
				{
					String code = moveInCodes[i];
					switch(code)
					{
					case "1":
						moveInCharges = moveInCharges.replace("1,", "1,19,");
						break;
					case "2":
						moveInCharges = moveInCharges.replace("2,", "2,20,");
					}
				}
				String[] autoCodes = autoCharges.split(",");
				for(int i=0;i<autoCodes.length;i++)
				{
					String code = autoCodes[i];
					switch(code)
					{
					case "1":
						autoCharges = autoCharges.replace("1,", "1,19,");
						break;
					case "2":
						autoCharges = autoCharges.replace("2,", "2,20,");
						break;
					}
				}
				*/
			}
			
			// Arizona
			if(company.equals("Arizona")&&PDFReader.monthlyRentTaxFlag==true)
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
			if(PDFReader.smartHomeAgreementCheck==true&&RunnerClass.portfolioName.contains("ATX."))
			{
				moveInCharges = moveInCharges+",25";
				autoCharges = autoCharges+",25";
			}
			//If Option 1 is selected in RBP Lease Agreement, then add Captive Insurence ATX charge
			if(PDFReader.captiveInsurenceATXFlag==true)
			{
				moveInCharges = moveInCharges+",26";
				autoCharges = autoCharges+",26";
			}
			
			//If RBP Has tax amount, add RBP in two charges
			if(PDFReader.residentBenefitsPackageTaxAvailabilityCheck==true) 
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
			}
			
			DataBase.assignChargeCodes(moveInCharges, autoCharges,buildingAbbreviation,SNo);
		}
		
		public static boolean checkProratedRentDateIsInMoveInMonth()
		{
			try
			{
			if(PDFReader.proratedRentDate.equalsIgnoreCase("n/a")||PDFReader.proratedRentDate.equalsIgnoreCase("na")||PDFReader.proratedRentDate.equalsIgnoreCase("n/a."))
				return true;
			if(PDFReader.proratedRentDate==null||PDFReader.proratedRentDate.equalsIgnoreCase("n/a")||PDFReader.proratedRentDate=="Error")
				return false;
			String proratedDate = RunnerClass.convertDate(PDFReader.proratedRentDate);
			String proratedMonth = proratedDate.split("/")[0];
			String moveInDate = RunnerClass.getStartDate();
			String moveInMonth = moveInDate.split("/")[0];
			if(proratedMonth.equalsIgnoreCase(moveInMonth)||Double.parseDouble(PDFReader.proratedRent)<=200.00)
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
				if(code.contains(RunnerClass.arizonaCityFromBuildingAddress))
				{
					RunnerClass.arizonaRentCode = code;
					RunnerClass.arizonaCodeAvailable = true;
					break;
					
				}
			}
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
