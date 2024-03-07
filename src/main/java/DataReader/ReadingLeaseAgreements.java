package DataReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.RunnerClass;

public class ReadingLeaseAgreements {
	
	public static String text="";
	public static String commencementDate ="";
	public static String expirationDate ="";
	public static String proratedRentDate="";
	
	public static boolean monthlyRentTaxFlag;
	public static boolean incrementRentFlag;
	public static boolean residentBenefitsPackageAvailabilityCheck;
	public static boolean HVACFilterFlag;
	public static boolean petRentTaxFlag;
	public static boolean serviceAnimalFlag;
	public static boolean concessionAddendumFlag;
	public static boolean smartHomeAgreementCheck;
	public static boolean captiveInsurenceATXFlag;
	

	public static void dataRead() throws Exception{
		
		 
		try {
			File file = RunnerClass.getLastModified();
		
			//File file = new File("C:\\SantoshMurthyP\\Lease Audit Automation\\Lease_02.22_02.23_200_Doc_Johns_Dr_ATX_Smith (3).pdf");
			FileInputStream fis = new FileInputStream(file);
		
			PDDocument document= PDDocument.load(fis);
	  
			text = new PDFTextStripper().getText(document);
		
			text = text.replaceAll(System.lineSeparator(), " ");
			text = text.trim().replaceAll(" +", " ");
			text = text.toLowerCase();
			//System.out.println(text);
			System.out.println("------------------------------------------------------------------");
			monthlyRentTaxFlag = false;
		
			commencementDate = dataExtractionClass.getDates(text,"term:^shall commence on@term:^commencement date:@term^commences on");
	    
			//commencementDate = LeaseAgreementDownloadandGetData.getDates(getDataOf("commencementDateFromPDF"));
			System.out.println("Start date = "+ commencementDate);
			expirationDate = dataExtractionClass.getDates(text,"term:^location of the premises\\) on@term:^expiration date:@term^expires on");
			System.out.println("End date = "+ expirationDate);
			proratedRentDate = dataExtractionClass.getDates(text,"rent:^prorated rent\\, on or before@rent:^Prorated Rent: On or before");
			System.out.println("Prorated Rent Date = "+ proratedRentDate);
			monthlyRentTaxFlag =dataExtractionClass.getFlags(text,"rent:^plus the additional amount of $@rent:^plus applicable sales tax and administrative fees of $");
			System.out.println("Monthly Rent Tax Flag = "+ monthlyRentTaxFlag);
			incrementRentFlag= dataExtractionClass.getFlags(text,"rent:^*Per the Landlord\\, Monthly Rent");
			System.out.println("Increment Rent Flag = "+ incrementRentFlag);
			residentBenefitsPackageAvailabilityCheck = dataExtractionClass.getFlags(text,"rent:^Resident Benefits Package (“RBP”) Program and Fee:@rent:^Resident Benefits Package (RBP) Lease Addendum@rent:^Resident Benefits Package Opt\\-Out Addendum");
			System.out.println("resident benefit package Availability Flag = "+ residentBenefitsPackageAvailabilityCheck); 
			HVACFilterFlag = dataExtractionClass.getFlags(text, "rent:^HVAC FILTER MAINTENANCE PROGRAM OPT-OUT ADDENDUM@rent:^HVAC Filter Maintenance Program Fee of $");
			System.out.println("HVAC Filter Flag = "+ HVACFilterFlag);
			petRentTaxFlag = dataExtractionClass.getFlags(text, "rent:^THIS PET ADDENDUM (this@rent^PET AUTHORIZATION AND PET DESCRIPTION:");
			System.out.println("Pet Rent Tax Flag = "+ petRentTaxFlag);
			serviceAnimalFlag = dataExtractionClass.getFlags(text, "SERVICE/SUPPORT ANIMAL AGREEMENT^SERVICE/SUPPORT ANIMAL AUTHORIZATION");
			System.out.println("Service Animal Flag = "+ serviceAnimalFlag);
			concessionAddendumFlag = dataExtractionClass.getFlags(text, "rent:^This is a CONCESSION ADDENDUM to your Lease Agreement");
			System.out.println("Concession Addendum Flag = "+ concessionAddendumFlag);
			smartHomeAgreementCheck = dataExtractionClass.getFlags(text, "rent:^This Smart Home Agreement is subject");
			System.out.println("Smart Home Agreement Flag = "+ smartHomeAgreementCheck);
			//captiveInsurenceATXFlag;
			
			
			
			
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
}
