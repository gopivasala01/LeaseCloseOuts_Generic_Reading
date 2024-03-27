package mainPackage;

import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import DataReader.ReadingLeaseAgreements;
import PDFAppConfig.PDFFormatDecider;


public class PDFReader 
{
	
    public static String proratedRent ="";
    public static String proratedRentDate ="";
    public static String monthlyRent="";
    public static String petRentWithTax="";
    public static String petRent="";
    public static String petFee;
    public static String pdfText="";
    public static String securityDeposit="";
    public static String leaseStartDate_PW="";
    public static String leaseEndDate_PW="";
    public static String prepaymentCharge;
    public static ArrayList<String> petType;
    public static ArrayList<String> petBreed;
    public static ArrayList<String> petWeight;
    public static Robot robot;
    public static boolean concessionAddendumFlag = false;
    public static boolean petSecurityDepositFlag = false;
    public static boolean petFlag = false;
    public static String portfolioType="";
    public static boolean incrementRentFlag = false;
    public static boolean proratedRentDateIsInMoveInMonthFlag=false;
    public static String increasedRent_previousRentStartDate ="";
    public static String increasedRent_previousRentEndDate ="";
    public static String increasedRent_amount ="";
    public static String increasedRent_newStartDate ="";
    public static boolean serviceAnimalFlag = false;
    public static ArrayList<String> serviceAnimalPetType;
    public static ArrayList<String> serviceAnimalPetBreed;
    public static ArrayList<String> serviceAnimalPetWeight;
    public static String lateFeeType ="";
    public static String flatFeeAmount ="";
    public static String lateFeePercentage="";
    public static boolean HVACFilterFlag = false;
    public static boolean HVACFilterOptOutAddendum = false;
    public static boolean residentBenefitsPackageAvailabilityCheck = false;
    public static String residentBenefitsPackage = "";
    public static String residentBenefitsPackageTaxAmount = "";
    public static boolean residentBenefitsPackageTaxAvailabilityCheck = false;
    public static String leaseRenewalFee = "";
    public static String endDate = "";
    public static String previousMonthlyRent = "";
	public static String adminFee ="";
	public static String airFilterFee="";
	public static String earlyTermination = "";
	public static String occupants = "";
	public static String petSecurityDeposit ="";
	public static String proratedPetRent = "";
	public static String petOneTimeNonRefundableFee = "";
	public static String lateFeeRuleType ="";
	public static String lateChargeDay = "";
	public static String lateChargeFee ="";
	public static String lateFeeChargePerDay = "";
	public static String additionalLateChargesLimit = "";
	public static String additionalLateCharges = "";
	public static String proratePetRentDescription = "";
	public static boolean residentUtilityBillFlag = false; 
	public static String prorateRUBS = "";
	public static String RUBS = "";
	
	public static boolean checkifMoveInDateIsLessThan5DaysToEOM =false;
	public static boolean petInspectionFeeFlag = false;
	//Other Fields
	public static String RCDetails = "";
	public static boolean monthlyRentTaxFlag = false;
	public static String monthlyRentTaxAmount = "";
	public static String totalMonthlyRentWithTax = "";
	
	public static boolean petRentTaxFlag = false;
	public static String petRentTaxAmount = "";
	public static String totalPetRentWithTax = "";
	
	public static String prorateRentGET = ""; //For Hawaii tax
	public static String monthlyRentGET = ""; //For Hawaii tax
	
	public static String OnePercentOfRentAmount = "";
	public static String OnePercentOfProrateRentAmount = "";
	public static String OnePercentOfPetRentAmount = "";
	public static String OnePercentOfProratePetRentAmount = "";
	
	public static boolean smartHomeAgreementCheck = false;
	public static String smartHomeAgreementFee = "";
	public static boolean captiveInsurenceATXFlag = false;
	public static String captiveInsurenceATXFee = "";
	public static boolean floridaLiquidizedAddendumOption1Check =  false;
	
		public static boolean readPDFPerMarket(String company) throws Exception  
		{
			//Initialize all PDF data variables
			monthlyRent="";
			HVACFilterFlag = false;
			residentBenefitsPackageAvailabilityCheck = false;
			residentBenefitsPackage = "";
			residentBenefitsPackageTaxAmount = "";
			residentBenefitsPackageTaxAvailabilityCheck = false;
			proratedRent ="";
		    proratedRentDate ="";
		    petFlag = false;
		    leaseRenewalFee = "";
		    airFilterFee = "";
		    petRent ="";
		    incrementRentFlag = false;
		    increasedRent_previousRentEndDate ="";
		    increasedRent_amount ="";
		    increasedRent_newStartDate ="";
		    previousMonthlyRent = "";
		    adminFee ="";
		    airFilterFee="";
		    portfolioType="";
		    petSecurityDeposit ="";
		    proratedPetRent = "";
		    petOneTimeNonRefundableFee = "";
		    lateFeeRuleType ="";
		    petType = new ArrayList();
		    petBreed = new ArrayList();
		    petWeight = new ArrayList();
		    lateChargeDay = "";
		    lateChargeFee ="";
		    lateFeeChargePerDay = "";
		    additionalLateChargesLimit = "";
		    additionalLateCharges = "";
		    proratePetRentDescription = "";
		    proratedRentDateIsInMoveInMonthFlag = false;
		    residentUtilityBillFlag = false; 
		    concessionAddendumFlag = false;
		    prorateRUBS = "";
			RUBS = "";
			checkifMoveInDateIsLessThan5DaysToEOM = false;
			petInspectionFeeFlag = false;
			monthlyRentTaxFlag = false;
			monthlyRentTaxAmount = "";
			totalMonthlyRentWithTax = "";
			petRentTaxFlag = false;
			petRentTaxAmount = "";
			totalPetRentWithTax = "";
			prorateRentGET = ""; 
			monthlyRentGET = ""; 
			OnePercentOfRentAmount = "";
			OnePercentOfProrateRentAmount = "";
			OnePercentOfPetRentAmount = "";
			OnePercentOfProratePetRentAmount = "";
			smartHomeAgreementCheck = false;
			smartHomeAgreementFee = "";
			captiveInsurenceATXFlag = false;
			floridaLiquidizedAddendumOption1Check =  false;
			HVACFilterOptOutAddendum = false;
			
		    //Runner Class Late Fee Variables
		 // All fields required for Late Fee Rule
		    RunnerClass.lateFeeRuleType = "";
		    RunnerClass.lateFeeType ="";
		    RunnerClass.PDFFormatType= "";
			// Initial Fee + Per Day Fee
		    RunnerClass.dueDay_initialFee="";
		    RunnerClass.initialFeeAmount="";
		    RunnerClass.initialFeeDropdown="";
		    RunnerClass.perDayFeeAmount ="";
		    RunnerClass.perDayFeeDropdown ="";
		    RunnerClass.maximumDropdown1 ="";
		    RunnerClass.maximumAmount ="";
		    RunnerClass.maximumDropdown2 ="";
		    RunnerClass.minimumDue ="";
		    RunnerClass.additionalLateChargesLimit ="";
			
			// Greater of Flat Fee or Percentage
		    RunnerClass.dueDay_GreaterOf="";
		    RunnerClass.flatFee = "";
		    RunnerClass.percentage = "";
		    RunnerClass.maximumDropdown1_GreaterOf ="";
		    RunnerClass.maximumAmount_GreaterOf ="";
		    RunnerClass.maximumDropdown2_GreaterOf ="";
		    RunnerClass.minimumDue_GreaterOf ="";
		    
		    //Other information
		    //RCDetails = "";
		    earlyTermination = "";
		    occupants = "";
		    serviceAnimalFlag = false;
		    serviceAnimalPetType = new ArrayList();
		    serviceAnimalPetBreed = new ArrayList();
		    serviceAnimalPetWeight = new ArrayList();
		    
		    ReadingLeaseAgreements.dataRead(RunnerClass.getFileName());
		    	
		    
			
			//Converting amounts in proper format if they have more than one dot
			try
			{
				if(PDFReader.totalMonthlyRentWithTax.replace(",", "").matches(".*\\..*\\..*"))
					PDFReader.totalMonthlyRentWithTax = PDFReader.totalMonthlyRentWithTax.replace(",", "");
				if(PDFReader.totalMonthlyRentWithTax.substring(PDFReader.totalMonthlyRentWithTax.length()-1).equals("."))
					PDFReader.totalMonthlyRentWithTax=PDFReader.totalMonthlyRentWithTax.substring(0,PDFReader.totalMonthlyRentWithTax.length()-1);
				
			}
			catch(Exception e)
			{}
			try
			{
				if(PDFReader.monthlyRent.replace(",", "").matches(".*\\..*\\..*"))
					PDFReader.monthlyRent = PDFReader.monthlyRent.replace(",", "").replaceFirst("[.]", "");
			}
			catch(Exception e)
			{}
			try
			{
				if(PDFReader.proratedRent.replace(",", "").matches(".*\\..*\\..*"))
					PDFReader.proratedRent = PDFReader.proratedRent.replace(",", "").replaceFirst("[.]", "");
			}
			catch(Exception e)
			{}
			try
			{
				if(PDFReader.petRent.replace(",", "").matches(".*\\..*\\..*"))
					PDFReader.petRent = PDFReader.petRent.replace(",", "").replaceFirst("[.]", "");
			}
			catch(Exception e)
			{}
			try
			{
				if(PDFReader.proratedPetRent.replace(",", "").matches(".*\\..*\\..*"))
					PDFReader.proratedPetRent = PDFReader.proratedPetRent.replace(",", "").replaceFirst("[.]", "");
			}
			catch(Exception e)
			{}
			
			//Prepayment charge 
			if(((company.equals("Alabama")||company.equals("Hawaii")||company.equals("Arizona"))&&PDFReader.monthlyRentTaxFlag==true))
			{
				try
				{
					PDFReader.prepaymentCharge =String.valueOf(RunnerClass.round((Double.parseDouble(PDFReader.totalMonthlyRentWithTax.replace(",", "")) - Double.parseDouble(PDFReader.proratedRent.replace(",", ""))),2)); 
				}
				catch(Exception e)
				{
					PDFReader.prepaymentCharge ="Error";
				}
				
				//Prorate Rent when Taxes available in Alabama and Hawaii
				if(!PDFReader.proratedRent.equalsIgnoreCase("n/a")||!PDFReader.proratedRent.equalsIgnoreCase("na")||!PDFReader.proratedRent.equalsIgnoreCase("n/a.")||!PDFReader.proratedRent.equalsIgnoreCase("0.00"))
				try
				{
					double rent = Double.parseDouble(PDFReader.monthlyRent.replace(",", ""));
					double prorateRent = Double.parseDouble(PDFReader.proratedRent.replace(",", ""));
					double  totalMonthlyRentWithTax= Double.parseDouble(PDFReader.totalMonthlyRentWithTax.replace(",", ""));
					double prorateRentCalculated = RunnerClass.round(((rent*prorateRent)/totalMonthlyRentWithTax),2);
					//For Hawaii Prorate Rent GET
					prorateRentGET = String.valueOf(RunnerClass.round((prorateRent - prorateRentCalculated),2));
					PDFReader.proratedRent =String.valueOf(RunnerClass.round(prorateRentCalculated,2)); 
				}
				catch(Exception e)
				{
					PDFReader.proratedRent ="Error";
				}
			}
			else
			{
			try
			{
				PDFReader.prepaymentCharge =String.valueOf(RunnerClass.round((Double.parseDouble(PDFReader.monthlyRent.replace(",", "")) - Double.parseDouble(PDFReader.proratedRent.replace(",", ""))),2)); 
			}
			catch(Exception e)
			{
				PDFReader.prepaymentCharge ="Error";
			}
			}
			System.out.println("Prepayment Charge = "+PDFReader.prepaymentCharge);
			
			//Prorate pet Rent when Taxes available in Alabama and Hawaii
			if(((company.equals("Alabama")||company.equals("Hawaii")||company.equals("Arizona"))&&PDFReader.petRentTaxFlag==true))
			{
			if(!PDFReader.proratedPetRent.equalsIgnoreCase("n/a")||!PDFReader.proratedPetRent.equalsIgnoreCase("na")||!PDFReader.proratedPetRent.equalsIgnoreCase("n/a.")||!PDFReader.proratedPetRent.equalsIgnoreCase("0.00"))
			try
			{
				double petRent = Double.parseDouble(PDFReader.petRent.replace(",", ""));
				double proratePetRent = Double.parseDouble(PDFReader.proratedPetRent.replace(",", ""));
				double  totalPetRentWithTax= Double.parseDouble(PDFReader.totalPetRentWithTax.replace(",", ""));
				double prorateRentCalculated = (petRent*proratePetRent)/totalPetRentWithTax;
				PDFReader.proratedPetRent =String.valueOf(RunnerClass.round(prorateRentCalculated,2)); 
			}
			catch(Exception e)
			{
				PDFReader.proratedPetRent ="Error";
			}
			}
			
			//Increased Rent New Start Date
			
			try
			{
				PDFReader.increasedRent_newStartDate = RunnerClass.convertDate(PDFReader.increasedRent_newStartDate);
			}
			catch(Exception e)
			{
				PDFReader.increasedRent_newStartDate = "Error";
			}
			System.out.println("Increased Rent New Start Date = "+PDFReader.increasedRent_newStartDate);
			
			// 1% of Monthly rent
			try
			{
				double monthlyRent = Double.parseDouble(PDFReader.monthlyRent.replace(",", ""));
				double onePercentOfRent = monthlyRent*0.01;
				PDFReader.OnePercentOfRentAmount =String.valueOf(RunnerClass.round( onePercentOfRent,2));
			}
			catch(Exception e)
			{
				PDFReader.OnePercentOfRentAmount = "Error";
			}
			// 1% of Prorate rent
			try
			{
				double proratedRent = Double.parseDouble(PDFReader.proratedRent.replace(",", ""));
				double onePercentOfProrateRent = proratedRent*0.01;
				PDFReader.OnePercentOfProrateRentAmount = String.valueOf(RunnerClass.round(onePercentOfProrateRent,2));
			}
			catch(Exception e)
			{
				PDFReader.OnePercentOfProrateRentAmount = "Error";
			}
			// 1% of Pet rent
			try
			{
				double petRent = Double.parseDouble(PDFReader.petRent.replace(",", ""));
				double onePercentOfPetRent = petRent*0.01;
				PDFReader.OnePercentOfPetRentAmount = String.valueOf(RunnerClass.round(onePercentOfPetRent,2));
			}
			catch(Exception e)
			{
				PDFReader.OnePercentOfPetRentAmount = "Error";
			}
			// 1% of Prorate Pet rent
			try
			{
				double proratePetRent2 = Double.parseDouble(PDFReader.proratedPetRent.replace(",", ""));
				double onePercentOfProratedPetRent = proratePetRent2*0.01;
				PDFReader.OnePercentOfProratePetRentAmount = String.valueOf(RunnerClass.round(onePercentOfProratedPetRent,2));
			}
			catch(Exception e)
			{
				PDFReader.OnePercentOfProratePetRentAmount = "Error";
		    }
			System.out.println("1% of Monthly Rent = "+OnePercentOfRentAmount);
			System.out.println("1% of Prorate Rent = "+OnePercentOfProrateRentAmount);
			System.out.println("1% of Pet Rent = "+OnePercentOfPetRentAmount);
			System.out.println("1% of Prorate Pet Rent = "+OnePercentOfProratePetRentAmount);
			
			//Splitting RBP Amounts when it has taxes for only Montana
			if(PDFReader.residentBenefitsPackageTaxAvailabilityCheck==true&&company.equals("Montana"))
			{
				try
				{
					double a = Double.parseDouble(PDFReader.residentBenefitsPackage.replace("$", "").trim());
					double b = Double.parseDouble(PDFReader.residentBenefitsPackageTaxAmount.replace("$", "").trim());
					double c = a-b;
					PDFReader.residentBenefitsPackage = String.valueOf(c);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					PDFReader.residentBenefitsPackage = "Error";
					PDFReader.residentBenefitsPackageTaxAmount = "Error";
				}
				
			}
			
			return true;
			
		}
		

	  


}
