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
	
    public static String securityDeposit="";
    public static String leaseStartDate_PW="";
    public static String leaseEndDate_PW="";

    public static String lateFeeType ="";
    public static String flatFeeAmount ="";
    public static String lateFeePercentage="";
    public static boolean HVACFilterOptOutAddendum = false;
    public static String leaseRenewalFee = "";
    public static String endDate = "";
    public static String previousMonthlyRent = "";
	public static String petSecurityDeposit ="";
	public static String lateFeeRuleType ="";
	public static String lateChargeDay = "";
	public static String lateChargeFee ="";
	public static String lateFeeChargePerDay = "";
	public static String additionalLateChargesLimit = "";
	public static String additionalLateCharges = "";
	
	//Other Fields
	public static String RCDetails = "";
	

	
	public static boolean floridaLiquidizedAddendumOption1Check =  false;
	
	private static ThreadLocal<String> prorateRentGETThreadLocal = new ThreadLocal<>();
	
	public static void setProrateRentGET(String prorateRentGET) {
		prorateRentGETThreadLocal.set(prorateRentGET);
	}
	
	public static String getProrateRentGET() {
		 return prorateRentGETThreadLocal.get();
	}
	
	
	
		public static boolean readPDFPerMarket(String company) throws Exception  
		{
			//Initialize all PDF data variables
		    leaseRenewalFee = "";
		    previousMonthlyRent = "";
		    petSecurityDeposit ="";
		    lateFeeRuleType ="";
		    lateChargeDay = "";
		    lateChargeFee ="";
		    lateFeeChargePerDay = "";
		    additionalLateChargesLimit = "";
		    additionalLateCharges = "";
			floridaLiquidizedAddendumOption1Check =  false;
			HVACFilterOptOutAddendum = false;
			
		
		    
		    ReadingLeaseAgreements.dataRead(RunnerClass.getFileName());
		    	
		    String prorateRent = "";
		    String monthlyRent = "";
			
			//Converting amounts in proper format if they have more than one dot
			try
			{
				if(RunnerClass.getTotalMonthlyRentWithTax().replace(",", "").matches(".*\\..*\\..*"))
					RunnerClass.setTotalMonthlyRentWithTax( RunnerClass.getTotalMonthlyRentWithTax().replace(",", ""));
				if(RunnerClass.getTotalMonthlyRentWithTax().substring(RunnerClass.getTotalMonthlyRentWithTax().length()-1).equals("."))
					RunnerClass.setTotalMonthlyRentWithTax(RunnerClass.getTotalMonthlyRentWithTax().substring(0,RunnerClass.getTotalMonthlyRentWithTax().length()-1));
				
			}
			catch(Exception e)
			{}
			try
			{
				monthlyRent = RunnerClass.getMonthlyRent();
				if(monthlyRent.replace(",", "").matches(".*\\..*\\..*"))
					monthlyRent = monthlyRent.replace(",", "").replaceFirst("[.]", "");
					RunnerClass.setMonthlyRent(monthlyRent);
			}
			catch(Exception e)
			{}
			try
			{
				prorateRent = RunnerClass.getProrateRent();
				if(prorateRent.replace(",", "").matches(".*\\..*\\..*"))
					prorateRent = prorateRent.replace(",", "").replaceFirst("[.]", "");
				RunnerClass.setProrateRent(prorateRent);
			}
			catch(Exception e)
			{}
			try
			{
				if(RunnerClass.getPetRent().replace(",", "").matches(".*\\..*\\..*"))
					RunnerClass.setPetRent(RunnerClass.getPetRent().replace(",", "").replaceFirst("[.]", ""));
			}
			catch(Exception e)
			{}
			try
			{
				if(RunnerClass.getproratedPetRent().replace(",", "").matches(".*\\..*\\..*"))
					RunnerClass.setproratedPetRent(RunnerClass.getproratedPetRent().replace(",", "").replaceFirst("[.]", ""));
			}
			catch(Exception e)
			{}
			
			//Prepayment charge 
			if(((company.equals("Alabama")||company.equals("Hawaii")||company.equals("Arizona"))&&RunnerClass.getMonthlyRentTaxFlag()==true))
			{
				try
				{
					RunnerClass.setprepaymentCharge(String.valueOf(RunnerClass.round((Double.parseDouble(RunnerClass.getTotalMonthlyRentWithTax().replace(",", "")) - Double.parseDouble(RunnerClass.getProrateRent().replace(",", ""))),2))); 
				}
				catch(Exception e)
				{
					RunnerClass.setprepaymentCharge("Error");
				}
				
				//Prorate Rent when Taxes available in Alabama and Hawaii
				if(!RunnerClass.getProrateRent().equalsIgnoreCase("n/a")||!RunnerClass.getProrateRent().equalsIgnoreCase("na")||!RunnerClass.getProrateRent().equalsIgnoreCase("n/a.")||!RunnerClass.getProrateRent().equalsIgnoreCase("0.00"))
				try
				{
					double rent = Double.parseDouble(monthlyRent.replace(",", ""));
					double proratedRent = Double.parseDouble(RunnerClass.getProrateRent().replace(",", ""));
					double  totalMonthlyRentWithTax= Double.parseDouble(RunnerClass.getTotalMonthlyRentWithTax().replace(",", ""));
					double prorateRentCalculated = RunnerClass.round(((rent*proratedRent)/totalMonthlyRentWithTax),2);
					//For Hawaii Prorate Rent GET
					setProrateRentGET( String.valueOf(RunnerClass.round((proratedRent - prorateRentCalculated),2)));
					prorateRent =String.valueOf(RunnerClass.round(prorateRentCalculated,2)); 
					RunnerClass.setProrateRent(prorateRent);
				}
				catch(Exception e)
				{
					prorateRent ="Error";
					RunnerClass.setProrateRent(prorateRent);
				}
			}
			else
			{
			try
			{
				RunnerClass.setprepaymentCharge(String.valueOf(RunnerClass.round((Double.parseDouble(monthlyRent.replace(",", "")) - Double.parseDouble(RunnerClass.getProrateRent().replace(",", ""))),2))); 
			}
			catch(Exception e)
			{
				RunnerClass.setprepaymentCharge("Error");
			}
			}
			System.out.println("Prepayment Charge = "+RunnerClass.getprepaymentCharge());
			
			//Prorate pet Rent when Taxes available in Alabama and Hawaii
			if(((company.equals("Alabama")||company.equals("Hawaii")||company.equals("Arizona"))&&RunnerClass.getPetRentTaxFlag()==true))
			{
			if(!RunnerClass.getproratedPetRent().equalsIgnoreCase("n/a")||!RunnerClass.getproratedPetRent().equalsIgnoreCase("na")||!RunnerClass.getproratedPetRent().equalsIgnoreCase("n/a.")||!RunnerClass.getproratedPetRent().equalsIgnoreCase("0.00"))
			try
			{
				double petRent = Double.parseDouble(RunnerClass.getPetRent().replace(",", ""));
				double proratePetRent = Double.parseDouble(RunnerClass.getproratedPetRent().replace(",", ""));
				double  totalPetRentWithTax= Double.parseDouble(RunnerClass.getTotalPetRentWithTax().replace(",", ""));
				double prorateRentCalculated = (petRent*proratePetRent)/totalPetRentWithTax;
				RunnerClass.setproratedPetRent(String.valueOf(RunnerClass.round(prorateRentCalculated,2))); 
			}
			catch(Exception e)
			{
				RunnerClass.setproratedPetRent("Error");
			}
			}
			
			//Increased Rent New Start Date
			
			try
			{
				RunnerClass.setIncreasedRent_newStartDate(RunnerClass.convertDate(RunnerClass.getIncreasedRent_newStartDate()));
			}
			catch(Exception e)
			{
				RunnerClass.setIncreasedRent_newStartDate("Error");
			}
			System.out.println("Increased Rent New Start Date = "+RunnerClass.getIncreasedRent_newStartDate());
			
			// 1% of Monthly rent
			try
			{
				double monthRent = Double.parseDouble(monthlyRent.replace(",", ""));
				double onePercentOfRent = monthRent*0.01;
				RunnerClass.setOnePercentOfRentAmount(String.valueOf(RunnerClass.round( onePercentOfRent,2)));
			}
			catch(Exception e)
			{
				RunnerClass.setOnePercentOfRentAmount("Error");
			}
			// 1% of Prorate rent
			try
			{
				double proratedRent = Double.parseDouble(RunnerClass.getProrateRent().replace(",", ""));
				double onePercentOfProrateRent = proratedRent*0.01;
				RunnerClass.setOnePercentOfProrateRentAmount(String.valueOf(RunnerClass.round(onePercentOfProrateRent,2)));
			}
			catch(Exception e)
			{
				RunnerClass.setOnePercentOfProrateRentAmount("Error");
			}
			// 1% of Pet rent
			try
			{
				double petRent = Double.parseDouble(RunnerClass.getPetRent().replace(",", ""));
				double onePercentOfPetRent = petRent*0.01;
				RunnerClass.setOnePercentOfPetRentAmount(String.valueOf(RunnerClass.round(onePercentOfPetRent,2)));
			}
			catch(Exception e)
			{
				RunnerClass.setOnePercentOfPetRentAmount("Error");
			}
			// 1% of Prorate Pet rent
			try
			{
				double proratePetRent2 = Double.parseDouble(RunnerClass.getproratedPetRent().replace(",", ""));
				double onePercentOfProratedPetRent = proratePetRent2*0.01;
				RunnerClass.setOnePercentOfProratePetRentAmount(String.valueOf(RunnerClass.round(onePercentOfProratedPetRent,2)));
			}
			catch(Exception e)
			{
				RunnerClass.setOnePercentOfProratePetRentAmount("Error");
		    }
			System.out.println("1% of Monthly Rent = "+RunnerClass.getOnePercentOfRentAmount());
			System.out.println("1% of Prorate Rent = "+RunnerClass.getOnePercentOfProrateRentAmount());
			System.out.println("1% of Pet Rent = "+RunnerClass.getOnePercentOfPetRentAmount());
			System.out.println("1% of Prorate Pet Rent = "+RunnerClass.getOnePercentOfProratePetRentAmount());
			
			//Splitting RBP Amounts when it has taxes for only Montana
			if(RunnerClass.getResidentBenefitsPackageTaxAvailabilityCheck()==true&&company.equals("Montana"))
			{
				try
				{
					double a = Double.parseDouble(RunnerClass.getresidentBenefitsPackage().replace("$", "").trim());
					double b = Double.parseDouble(RunnerClass.getResidentBenefitsPackageTaxAmount().replace("$", "").trim());
					double c = a-b;
					RunnerClass.setresidentBenefitsPackage(String.valueOf(c));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					RunnerClass.setresidentBenefitsPackage("Error");
					RunnerClass.setResidentBenefitsPackageTaxAmount("Error");
				}
				
			}
			
			return true;
			
		}
		

	  


}
