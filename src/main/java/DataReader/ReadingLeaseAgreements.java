package DataReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.TimeoutException;

import PDFAppConfig.PDFFormatDecider;
import io.github.bonigarcia.wdm.WebDriverManager;
import mainPackage.AppConfig;
import mainPackage.PDFReader;
import mainPackage.PropertyWare;
import mainPackage.PropertyWare_updateValues;
import mainPackage.RunnerClass;
import mainPackage.TessaractTest;

public class ReadingLeaseAgreements {
	public static String format1Text = "The parties to this Lease are".toLowerCase();
	public static String format2Text = "THIS RESIDENTIAL LEASE AGREEMENT".toLowerCase();
	
	public static void dataRead(String fileName,String SNo,String company) throws Exception 
	{
	   
		String text="";
		String commencementDate ="";
		String expirationDate="";
		String proratedRent="";
		String proratedRentDate="";
		String increasedRent_previousRentEndDate="";
		String monthlyRent="";
		boolean monthlyRentTaxFlag=false;
		String monthlyRentTaxAmount="";
		String adminFee="";
		String occupants="";
		String residentBenefitsPackage="";
		boolean residentBenefitsPackageAvailabilityCheck=false;
		boolean HVACFilterFlag=false;
		boolean petFlag=false;
		boolean serviceAnimalFlag=false;
		boolean concessionAddendumFlag=false;
		String airFilterFee="";
		String prepaymentCharge="";
		String proratedPetRent="";
		String petRent="";
		String petRentTaxAmount="";
		String totalPetRentWithTax="";
		String petOneTimeNonRefundableFee="";
		String smartHomeAgreementFee="";
		boolean smartHomeAgreementCheck=false;
		String earlyTermination="";
		String totalMonthlyRentWithTax="";
		boolean residentBenefitsPackageTaxAvailabilityCheck=false;
		String residentBenefitsPackageTaxAmount="";
		String increasedRent_amount="";
		String increasedRent_newStartDate="";
		boolean incrementRentFlag=false;
		boolean petRentTaxFlag = false;
		String prorateRUBS="";
		String RUBS="";
		String captiveInsurenceATXFee = "";
		String petSecurityDeposit="";
		boolean residentUtilityBillFlag = false;
		boolean captiveInsurenceATXFlag = false;
		boolean petInspectionFeeFlag= false;
		boolean petSecurityDepositFlag = false;
		boolean HVACFilterOptOutAddendum =false;
		boolean RBPOptOutAddendumCheck = false;
		boolean floridaLiquidizedAddendumOption1Check = false;
		String PDFFormatType = "";
		
		List<String> allIncreasedRent_amounts=new ArrayList();
		
		ArrayList<String> petType = new ArrayList(); 
		ArrayList<String> petBreed = new ArrayList();
		ArrayList<String> petWeight = new ArrayList();
		
		ArrayList<String> serviceAnimalPetType;
	    ArrayList<String> serviceAnimalPetBreed;
	    ArrayList<String> serviceAnimalPetWeight;

         try {
        	 File file = RunnerClass.getLastModified(fileName);
 			//File file = new File("C:\\SantoshMurthyP\\Lease Audit Automation\\Lease_02.22_02.23_200_Doc_Johns_Dr_ATX_Smith (3).pdf");
 			FileInputStream fis = new FileInputStream(file);
 			PDDocument document = PDDocument.load(fis);
 		    text = new PDFTextStripper().getText(document);
 		    text = text.replaceAll(System.lineSeparator(), " ");
 		    text = text.trim().replaceAll(" +", " ");
 		    text = text.toLowerCase();
 		 
 		   if(text.contains(format1Text.toLowerCase())||text.contains(PDFFormatDecider.format1.toLowerCase())||text.contains(PDFFormatDecider.format1_2.toLowerCase()))
		    {
		    	PDFFormatType = "Format1";
		    	System.out.println("PDF Format Type  = "+PDFFormatType);
		    	
		    }
		    
		    else if(text.contains(format2Text.toLowerCase()))
		    {
		    	PDFFormatType = "Format2";
		    	System.out.println("PDF Format Type = "+PDFFormatType);
		    	
		    }
			RunnerClass.setPDFFormatType(PDFFormatType);
		       
		            
			//File file = new File("C:\\SantoshMurthyP\\Lease Audit Automation\\Lease_02.22_02.23_200_Doc_Johns_Dr_ATX_Smith (3).pdf");
		
			//System.out.println(text);
			System.out.println("------------------------------------------------------------------");
		
			commencementDate = dataExtractionClass.getDates(text,"term:^shall commence on@term:^commencement date:@term^commences on");
			System.out.println("Start date = "+ commencementDate);
			RunnerClass.setStartDate(RunnerClass.convertDate(commencementDate));
			expirationDate = dataExtractionClass.getDates(text,"term:^location of the premises\\) on@term:^expiration date:@term^expires on");
			System.out.println("End date = "+ expirationDate);
			RunnerClass.setEndDate(RunnerClass.convertDate(expirationDate));
			proratedRentDate = dataExtractionClass.getDates(text,"rent:^prorated rent\\, on or before@rent:^Prorated Rent: On or before");
			System.out.println("Prorated Rent Date = "+ proratedRentDate);
			RunnerClass.setProrateRentDate(proratedRentDate);
			//incrementRentFlag= dataExtractionClass.getFlags(text,"rent:^*Per the Landlord\\, Monthly Rent");
			
			concessionAddendumFlag = dataExtractionClass.getFlags(text, "rent:^This is a CONCESSION ADDENDUM to your Lease Agreement");
			System.out.println("Concession Addendum Flag = "+ concessionAddendumFlag);
			RunnerClass.setconcessionAddendumFlag(concessionAddendumFlag);
			
			monthlyRent =dataExtractionClass.getValues(text,"Monthly Rent:^Monthly Rent due in the amount of^@Monthly Rent:^Tenant will pay Landlord monthly rent in the amount of^@monthly installments,^on or before the 1st day of each month, in the amount of^@monthly installments,^Tenant will pay Landlord monthly rent in the amount of^");
			System.out.println("Monthly Rent Amount = "+ monthlyRent);
			RunnerClass.setMonthlyRent(monthlyRent);
			
			allIncreasedRent_amounts =dataExtractionClass.getMultipleValues(text, "Monthly Rent:^Monthly Rent due in the amount of^@Monthly Rent:^Tenant will pay Landlord monthly rent in the amount of^@monthly installments,^on or before the 1st day of each month, in the amount of^@monthly installments,^Tenant will pay Landlord monthly rent in the amount of^") ;
			if (allIncreasedRent_amounts.size() > 1) {
	            double firstValue = Double.parseDouble(allIncreasedRent_amounts.get(0));
	            for (int i = 1; i < allIncreasedRent_amounts.size(); i++) {
	                double currentValue = Double.parseDouble(allIncreasedRent_amounts.get(i));
	                if (currentValue > firstValue) {
	                	incrementRentFlag = true;
	                	System.out.println("Increment Rent Flag = "+ incrementRentFlag);
	                	RunnerClass.setIncrementRentFlag(incrementRentFlag);
	                	increasedRent_amount = String.valueOf(currentValue);
	                	System.out.println("Increment Rent Amount = "+ increasedRent_amount);
	                	RunnerClass.setIncreasedRent_amount(increasedRent_amount);
	                	increasedRent_newStartDate = dataExtractionClass.getSecondDate(text, "Monthly Rent:^Month");
	                	System.out.println("Increased Rent - New Rent Start date =  "+increasedRent_newStartDate);
	                	RunnerClass.setIncreasedRent_newStartDate(increasedRent_newStartDate);
	                    //System.out.println("Value " + increasedRent_amounts.get(i) + " is greater than the first value.");
	                    break;
	                }
	            }
	        }
			else {
				RunnerClass.setIncrementRentFlag(incrementRentFlag);
				RunnerClass.setIncreasedRent_amount("Error");
				RunnerClass.setIncreasedRent_newStartDate("Error");
			}
		
			increasedRent_previousRentEndDate =  dataExtractionClass.getDates(text,"Monthly Rent:^to Month");
			if(!increasedRent_previousRentEndDate.equalsIgnoreCase("Error")) {
				RunnerClass.setIncreasedRent_previousRentEndDate(increasedRent_previousRentEndDate);
			}
			else {
				RunnerClass.setIncreasedRent_previousRentEndDate("Error");
			}
			 
			monthlyRentTaxFlag =dataExtractionClass.getFlags(text,"rent:^plus the additional amount of $@rent:^plus applicable sales tax and administrative fees of $");
			System.out.println("Monthly Rent Tax Flag = "+ monthlyRentTaxFlag);
			RunnerClass.setMonthlyRentTaxFlag(monthlyRentTaxFlag);
			if(monthlyRentTaxFlag == true) {
				monthlyRentTaxAmount= dataExtractionClass.getValues(text, "Monthly Rent:^plus applicable sales tax and administrative fees of^@Monthly Rent:^plus the additional amount of^@monthly installments,^plus the additional amount of^");
				System.out.println("Monthly Rent Tax Amount = "+ monthlyRentTaxAmount);
				RunnerClass.setMonthlyRentTaxAmount(monthlyRentTaxAmount);
				if(RunnerClass.hasSpecialCharacters(monthlyRentTaxAmount.trim())==true||monthlyRentTaxAmount.trim().equalsIgnoreCase("0.00")||monthlyRentTaxAmount.trim().equalsIgnoreCase("N/A")||monthlyRentTaxAmount.trim().equalsIgnoreCase("n/a")||monthlyRentTaxAmount.trim().equalsIgnoreCase("na")||monthlyRentTaxAmount.trim().equalsIgnoreCase(""))
		    	{
		    		monthlyRentTaxAmount = "Error";
		    		
		    	}
		    	else
		    	{
		    		totalMonthlyRentWithTax = dataExtractionClass.getValues(text, "Monthly Rent:^for a total monthly Rent of^@Monthly Rent:^assessed, for a total of^@monthly installments,^assessed, for a total of@Monthly Rent:^for a total of");
		    		System.out.println("Total Monthly Rent With Tax Amount = "+ totalMonthlyRentWithTax);
		    		RunnerClass.setTotalMonthlyRentWithTax(totalMonthlyRentWithTax);
		    		
		    	}
				
			}
			else {
				RunnerClass.setMonthlyRentTaxAmount("Error");
			}
			
			adminFee = dataExtractionClass.getValues(text, "Lease Administrative Fee(s):^preparation fee in the amount of^@Lease Administrative Fee(s):^An annual lease preparation fee in the amount of^");
			System.out.println("Admin Fee = "+ adminFee);
			RunnerClass.setAdminFee(adminFee);
			
			residentBenefitsPackageAvailabilityCheck = dataExtractionClass.getFlags(text,"rent:^Resident Benefits Package (“RBP”) Program and Fee:@rent:^Resident Benefits Package (RBP) Lease Addendum@rent:^Resident Benefits Package Opt\\-Out Addendum");
			System.out.println("resident benefit package Availability Flag = "+ residentBenefitsPackageAvailabilityCheck); 
			RunnerClass.setresidentBenefitsPackageAvailabilityCheckFlag(residentBenefitsPackageAvailabilityCheck);
			if(residentBenefitsPackageAvailabilityCheck == true) {
				residentBenefitsPackage = dataExtractionClass.getValues(text, "Resident Benefits Package (“RBP”) Program and Fee:^Tenant agrees to pay a Resident Benefits Package Fee of^");
				System.out.println("Resident Benefit Package Fee = "+ residentBenefitsPackage);
				RunnerClass.setresidentBenefitsPackage(residentBenefitsPackage);
			}
			else {
				RunnerClass.setresidentBenefitsPackage("Error");
			}
			
			if(text.contains(("TOTAL CHARGE TO TENANT $").toLowerCase()))
		    {
		    	residentBenefitsPackageTaxAvailabilityCheck = true;
		    	RunnerClass.setResidentBenefitsPackageTaxAvailabilityCheck(residentBenefitsPackageTaxAvailabilityCheck);
		    	if(residentBenefitsPackageTaxAvailabilityCheck == true) {
		    		 residentBenefitsPackageTaxAmount  = dataExtractionClass.getValues(text, "Resident Benefits Package (“RBP”) Program and Fee:^(Inclusive of@TOTAL CHARGE TO TENANT^(Inclusive of");
		    		 RunnerClass.setResidentBenefitsPackageTaxAmount(residentBenefitsPackageTaxAmount);
		    	}
		    }
			else {
				residentBenefitsPackageTaxAvailabilityCheck = false;
		    	RunnerClass.setResidentBenefitsPackageTaxAvailabilityCheck(residentBenefitsPackageTaxAvailabilityCheck);
			}
			HVACFilterFlag = dataExtractionClass.getFlags(text, "rent:^HVAC FILTER MAINTENANCE PROGRAM OPT-OUT ADDENDUM@rent:^HVAC Filter Maintenance Program Fee of $");
			System.out.println("HVAC Filter Flag = "+ HVACFilterFlag);
			RunnerClass.setHVACFilterFlag(HVACFilterFlag);
			if(HVACFilterFlag == true) {
				airFilterFee = dataExtractionClass.getValues(text, "HVAC FILTER MAINTENANCE PROGRAM OPT-OUT ADDENDUM^HVAC Filter Maintenance Program Fee of@HVAC Filter Maintenance Program and Fee:^HVAC Filter Maintenance Program Fee of");
				System.out.println("HVAC Air Filter Fee = "+ airFilterFee);
				RunnerClass.setairFilterFee(airFilterFee);
			}
			
			 //HAVC Opt-Out Addendum check
		    try
		    {
		    	if(text.contains(("HVAC FILTER MAINTENANCE PROGRAM OPT-OUT ADDENDUM").toLowerCase()))
		    	{
		    		HVACFilterOptOutAddendum= true;
		    		
		    	}
		    	
		    }
		    catch(Exception e) {}
		    RunnerClass.setHVACFilterOptOutAddendum(HVACFilterOptOutAddendum);
		  //RBP Opt - Out Addendum Check
		    try
		    {
		    	 if(text.contains(("Resident Benefits Package Opt-Out Addendum").toLowerCase()))
		 	    {
		 	    	RBPOptOutAddendumCheck= true;
		 	    	
		 	    }
		    }
		    catch(Exception e)
		    {
		    	
		    }
		    RunnerClass.setRBPOptOutAddendumCheck(RBPOptOutAddendumCheck);
		    //Occupants
			occupants= dataExtractionClass.getTextWithStartandEndValue(text, "USE AND OCCUPANCY:^this Lease are:^Only two Tenants@USE AND OCCUPANCY:^this Lease are:^B. Phone Numbers@USE AND OCCUPANCY:^ages of all occupants):^NO OTHER OCCUPANTS SHALL RESIDE@USE AND OCCUPANCY:^ages of all occupants):^B. Phone Numbers:@USE AND OCCUPANCY:^listed as follows:^Property shall be used by Tenant@USE AND OCCUPANCY:^Name, Age ^The Tenant and the Minor Occupants listed above^@USE AND OCCUPANCY:^this Lease are^B. Phone Numbers@OCCUPANTS^Landlord/Landlord’s Broker:^11. MAINTENANCE@OCCUPANTS^Landlord/Landlord’s Broker:^10. MAINTENANCE@SUBLET AND ASSIGNMENT^persons listed as follows:^Property shall be used by Tenant");
			occupants = capitalizeFirstLetter(occupants);
			System.out.println("Occupants Name = "+ occupants);
			RunnerClass.setOccupants(occupants);
			proratedRent = dataExtractionClass.getValues(text, "Prorated Rent:^Tenant will pay Landlord@prorated rent,^Tenant will pay Landlord@Prorated Rent:^Tenant will pay Landlord");
			System.out.println("Prorated rent = "+ proratedRent);
			RunnerClass.setProrateRent(proratedRent);
			
		/*	if(RunnerClass.portfolioType.contains("MCH"))
	  		{
	  			if(proratedRent.equalsIgnoreCase("n/a")||proratedRent.equalsIgnoreCase("Error")||proratedRent.equalsIgnoreCase(""))
	  			{
	  				prepaymentCharge = "Error";
	  				RunnerClass.setprepaymentCharge(prepaymentCharge);
	  			}
	  			else
	  			{
		  		try
		  		{
		  			prepaymentCharge =String.valueOf(Double.parseDouble(monthlyRent.trim().replace(",", "")) - Double.parseDouble(proratedRent.trim().replace(",", ""))); 
		  			RunnerClass.setprepaymentCharge(prepaymentCharge);
		  		}
		  		catch(Exception e)
		  		{
		  			prepaymentCharge ="Error";
		  			RunnerClass.setprepaymentCharge(prepaymentCharge);
		  		}
		  		}
	  			System.out.println("Prepayment Charge = "+prepaymentCharge);
	  		 } */
			
	  		 if(text.contains(("SPECIAL PROVISIONS:").toLowerCase()))
	  		 {
	  			residentUtilityBillFlag = true;
	  			RunnerClass.setResidentUtilityBillFlag(residentUtilityBillFlag);
	  			RUBS = dataExtractionClass.getValues(text, "UTILITIES:^Tenant shall pay a");
	  			RunnerClass.setRUBS(RUBS);
	  			prorateRUBS = dataExtractionClass.getValues(text, "UTILITIES:^Tenant will pay Landlord@UTILITIES:^RUBS fee of");
	  			RunnerClass.setProrateRUBS(prorateRUBS);
	  		 }
	  		 else {
	  			residentUtilityBillFlag = false;
	  			RunnerClass.setResidentUtilityBillFlag(residentUtilityBillFlag);
	  			RunnerClass.setProrateRUBS("Error");
	  			RunnerClass.setRUBS("Error");
	  		 }
	  		
	  		if(text.contains("pet inspection fee"))
	    	{
	    		
	    		petInspectionFeeFlag = true;
	    	}
	  		PropertyWare_updateValues.setPetInspectionFeeFlag(petInspectionFeeFlag);
	  		
	  		
	  		petFlag = dataExtractionClass.getFlags(text, "rent:^THIS PET ADDENDUM (this@rent^PET AUTHORIZATION AND PET DESCRIPTION:");
			System.out.println("Pet Flag = "+ petFlag);
			RunnerClass.setpetFlag(petFlag);
			if(petFlag == true) {
				petSecurityDeposit = dataExtractionClass.getValues(text, "PET AUTHORIZATION AND PET DESCRIPTION:^On or before the date Tenant moves into the Property, Tenant will pay Landlord an additional deposit of@THIS PET ADDENDUM^Tenant will, upon execution of this agreement, pay Landlord");
				System.out.println("Pet Security Deposit = "+ petSecurityDeposit);
				RunnerClass.setPetSecurityDeposit(petSecurityDeposit);
				if(!petSecurityDeposit.equalsIgnoreCase("Error")) {
					petSecurityDepositFlag = true;
					PropertyWare_updateValues.setPetSecurityDepositFlag(petSecurityDepositFlag);
				}
				else {
					PropertyWare_updateValues.setPetSecurityDepositFlag(petSecurityDepositFlag);
				}
				proratedPetRent = dataExtractionClass.getValues(text, "Prorated Pet Rent:^Tenant will pay Landlord");
				System.out.println("Prorated Pet Rent = "+ proratedPetRent);
				RunnerClass.setproratedPetRent(proratedPetRent);
				petRent = dataExtractionClass.getValues(text, "THIS PET ADDENDUM^Tenant will pay Landlord monthly pet rent in the amount of@PET AUTHORIZATION AND PET DESCRIPTION:^Tenant will pay Landlord monthly pet rent in the amount of@PET AUTHORIZATION AND PET DESCRIPTION:^Tenant will pay Landlord a monthly pet inspection fee in the amount of@PET AUTHORIZATION AND PET DESCRIPTION:^The monthly rent in the lease is increased by");
				System.out.println("Pet Rent = "+ petRent);
				RunnerClass.setPetRent(petRent);
				petRentTaxAmount = dataExtractionClass.getValues(text, "THIS PET ADDENDUM^tax and administrative fees of@PET AUTHORIZATION AND PET DESCRIPTION:^tax and administrative fees of");
				System.out.println("Pet Rent Tax Amount = "+ petRentTaxAmount);
				if(petRentTaxAmount.trim().equalsIgnoreCase("0.00")||petRentTaxAmount.trim().equalsIgnoreCase("N/A")||petRentTaxAmount.trim().equalsIgnoreCase("n/a")||petRentTaxAmount.trim().equalsIgnoreCase("na")||petRentTaxAmount.trim().equalsIgnoreCase(""))
		 	    {
		 	    	petRentTaxFlag = false;
		 	    	RunnerClass.setPetRentTaxFlag(petRentTaxFlag);
		 	    }
		 	    else
		 	    {
		 	    	petRentTaxFlag = true;
		 	    	RunnerClass.setPetRentTaxFlag(petRentTaxFlag);
					totalPetRentWithTax = dataExtractionClass.getValues(text, "THIS PET ADDENDUM^for a total of@PET AUTHORIZATION AND PET DESCRIPTION:^for a total of");
					System.out.println("Total Pet Rent With Tax = "+ totalPetRentWithTax);
					RunnerClass.setTotalPetRentWithTax(totalPetRentWithTax);
				}
				petOneTimeNonRefundableFee = dataExtractionClass.getValues(text, "THIS PET ADDENDUM^Tenant will, upon execution of this agreement, pay Landlord@PET AUTHORIZATION AND PET DESCRIPTION:^Tenant will, upon execution of this agreement, pay Landlord");
				System.out.println("Pet One Time Non Refundable Fee = "+ petOneTimeNonRefundableFee);
				RunnerClass.setPetOneTimeNonRefundableFee(petOneTimeNonRefundableFee);
				
				String typeSubString = dataExtractionClass.getTextWithStartandEndValue(text, "THIS PET ADDENDUM^Tenant may keep the following pet(s) on the Property until the above-referenced lease ends.^B. CONSIDERATION:@PET AUTHORIZATION AND PET DESCRIPTION:^Tenant may keep the following pet(s) on the Property until the above-referenced lease ends.^B. CONSIDERATION:");
				String newText = typeSubString.replace("type:","");
			    int countOfTypeWordInText = ((typeSubString.length() - newText.length())/"type:".length());
			    System.out.println("Type: occurence = "+countOfTypeWordInText);
			    for(int i =0;i<countOfTypeWordInText;i++)
			    {
			    	String type = typeSubString.substring(nthOccurrence(typeSubString, "type:", i+1)+"type:".length(),nthOccurrence(typeSubString, "breed:", i+1)).trim();
			    	if(type.contains("N/A")||type.contains("n/a"))
			    		break;
			    	System.out.println(type);
			    	petType.add(type);
			    	int pet1Breedindex1 = nthOccurrence(typeSubString, "breed:", i+1)+"breed:".length()+1;
				    String subString = typeSubString.substring(pet1Breedindex1);
				    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
				   // System.out.println("Index 2 = "+(index2+index1));
				    String breed = subString.split("name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
				    System.out.println(breed);
				    petBreed.add(breed);
				    int pet1Weightindex1 = nthOccurrence(typeSubString, "weight:", i+1)+"weight:".length()+1;
				    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
				    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
				   // System.out.println("Index 2 = "+(index2+index1));
				    String weight = pet1WeightSubstring.split("age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
				    System.out.println(weight);
				    petWeight.add(weight);
			    }
			    RunnerClass.setPetTypes(petType);
			    RunnerClass.setPetBreeds(petBreed);
			    RunnerClass.setPetWeights(petWeight);
			}
			serviceAnimalFlag = dataExtractionClass.getFlags(text,"SERVICE/SUPPORT ANIMAL AGREEMENT^SERVICE/SUPPORT ANIMAL AUTHORIZATION");
			System.out.println("Service Animal Flag = "+ serviceAnimalFlag);
				
			RunnerClass.setserviceAnimalFlag(serviceAnimalFlag);
				
				if(serviceAnimalFlag == true) {
						System.out.println("Service Animal Addendum is available");
					 	String typeSubStrings = dataExtractionClass.getTextWithStartandEndValue(text, "THIS PET ADDENDUM^Tenant has the following Service/Support Animal(s) on the Property until the above-referenced lease ends.^B. SERVICE/SUPPORT ANIMAL RULES@PET AUTHORIZATION AND PET DESCRIPTION:^Tenant has the following Service/Support Animal(s) on the Property until the above-referenced lease ends.^B. SERVICE/SUPPORT ANIMAL RULES@SERVICE/SUPPORT ANIMAL AUTHORIZATION AND SERVICE/SUPPORT ANIMAL DESCRIPTION:^Tenant has the following Service/Support Animal(s) on the Property until the above-referenced lease ends.^B. SERVICE/SUPPORT ANIMAL RULES");
				    	
				    	String newTexts = typeSubStrings.replace("type:","");
					    int  countOftypeWords_ServiceAnimal = ((typeSubStrings.length() - newTexts.length())/"type:".length());
					    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
					    
					    serviceAnimalPetType = new ArrayList();
					    serviceAnimalPetBreed = new ArrayList();
					    serviceAnimalPetWeight = new ArrayList();
					    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
					    {
					    	String type = typeSubStrings.substring(RunnerClass.nthOccurrence(typeSubStrings, "type:", i+1)+"type:".length(),RunnerClass.nthOccurrence(typeSubStrings, "breed:", i+1)).trim();
					    	if(type.contains("N/A")||type.contains("n/a"))
					    		break;
					    	System.out.println(type);
					    	serviceAnimalPetType.add(type);
					    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubStrings, "breed:", i+1)+"breed:".length()+1;
						    String subString = typeSubStrings.substring(pet1Breedindex1);
						    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
						   // System.out.println("Index 2 = "+(index2+index1));
						    String breed = subString.split("name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
						    System.out.println(breed);
						    serviceAnimalPetBreed.add(breed);
						    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubStrings, "weight:", i+1)+"weight:".length()+1;
						    String pet1WeightSubstring = typeSubStrings.substring(pet1Weightindex1);
						    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
						   // System.out.println("Index 2 = "+(index2+index1));
						    String weight = pet1WeightSubstring.split("age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
						    System.out.println(weight);
						    serviceAnimalPetWeight.add(weight);
					    }
					    RunnerClass.setServiceAnimalPetType(serviceAnimalPetType);
					    RunnerClass.setServiceAnimalPetBreeds(serviceAnimalPetBreed);
					    RunnerClass.setServiceAnimalPetWeights(serviceAnimalPetWeight);
					    
				}
			    
			
			smartHomeAgreementCheck = dataExtractionClass.getFlags(text, "rent:^This Smart Home Agreement is subject");
			System.out.println("Smart Home Agreement Flag = "+ smartHomeAgreementCheck);
			RunnerClass.setSmartHomeAgreementCheck(smartHomeAgreementCheck);
			if(smartHomeAgreementCheck == true) {
				smartHomeAgreementFee = dataExtractionClass.getValues(text, "This Smart Home Agreement is subject^Smart Home Agreement shall be");
				System.out.println("Smart Home Agreement Fee = "+smartHomeAgreementFee);
				RunnerClass.setSmartHomeAgreementFee(smartHomeAgreementFee);
			}
			else {
				RunnerClass.setSmartHomeAgreementFee(smartHomeAgreementFee);
			}
			earlyTermination = dataExtractionClass.getTextWithStartandEndValue(text, "Early Termination:^Landlord of^month’s rent at the time the Notice is provided@EARLY TERMINATION BY TENANT^Landlord of^month’s rent at the time the Notice is provided");
    		System.out.println("Early Termination  = "+earlyTermination.trim());
    		RunnerClass.setEarlyTermination(earlyTermination);
			
    		
    		
    		
    		
    		
    		//RBP when Portfolio is ATX
    	    
    	    try
    	    {
    	    	if(RunnerClass.getPortfolioName().contains("ATX."))
    	    	{
    	    		if(text.contains(PDFAppConfig.Austin_Format1.residentBenefitsPackageAddendumCheck)&&!text.contains("Resident Benefits Package Opt-Out Addendum"))
    	    	    {
    	    	    	residentBenefitsPackageAvailabilityCheck = true;
    	    	    	 try
    	    	 	    {
    	    	 		    residentBenefitsPackage = text.substring(text.indexOf(PDFAppConfig.Austin_Format1.RBPWhenPortfolioIsATX)+PDFAppConfig.Austin_Format1.RBPWhenPortfolioIsATX.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
                            if(residentBenefitsPackage.contains("month"))
                            	residentBenefitsPackage = residentBenefitsPackage.substring(0,residentBenefitsPackage.indexOf("month")).trim();
    	    	 		    if(residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
    	    	 		    {
    	    	 		    	residentBenefitsPackage = "Error";
    	    	 		    }
    	    	 	    }
    	    	 	    catch(Exception e)
    	    	 	    {
    	    	 		    residentBenefitsPackage = "Error";
    	    	 		    e.printStackTrace();
    	    	 	    }
    	    	    	System.out.println("Resident Benefits Packages  = "+residentBenefitsPackage.trim());
    	    	    	RunnerClass.setresidentBenefitsPackageAvailabilityCheckFlag(residentBenefitsPackageAvailabilityCheck);
    	    	    	RunnerClass.setresidentBenefitsPackage(residentBenefitsPackage);
    	    	    	//PDFAppConfig.Austin_Format1.AB1_residentBenefitsPackage_Prior
    	    	}
    	    		// Check if Option 1 is selected in RBP Lease Agreement
    	    		
    	    		String optionValue = TessaractTest.pdfScreenShot(file,SNo);
    	    		if(optionValue.equals("Option 1"))
    	    		{
    	    			captiveInsurenceATXFlag = true;
    	    			RunnerClass.setCaptiveInsurenceATXFlag(captiveInsurenceATXFlag);
    	    			 try
    		    	 	    {
    	    				 	captiveInsurenceATXFee = text.substring(text.indexOf(PDFAppConfig.Austin_Format1.captiveInsurenceATXFee_Prior.toLowerCase())+PDFAppConfig.Austin_Format1.captiveInsurenceATXFee_Prior.toLowerCase().length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
    		    	 		   if(captiveInsurenceATXFee.contains("per")||captiveInsurenceATXFee.contains("Per"))
    		    	 			   	captiveInsurenceATXFee = captiveInsurenceATXFee.trim().replace("per", "");
    		    	 		    if(captiveInsurenceATXFee.matches(".*[a-zA-Z]+.*"))
    		    	 		    {
    		    	 		    	captiveInsurenceATXFee = "Error";
    		    	 		    }
    		    	 	    }
    		    	 	    catch(Exception e)
    		    	 	    {
    		    	 	    	captiveInsurenceATXFee = "Error";
    		    	 		    e.printStackTrace();
    		    	 	    }
    	    			 	RunnerClass.setCaptiveInsurenceATXFee(captiveInsurenceATXFee);
    		    	    	System.out.println("Captive Insurence ATX Fee  = "+captiveInsurenceATXFee.trim());
    	    		} 
    	    		else {
    	    			RunnerClass.setCaptiveInsurenceATXFlag(captiveInsurenceATXFlag);
    	    		} 
    	    		
    	    	}
    	    	else {
    	    		RunnerClass.setCaptiveInsurenceATXFlag(captiveInsurenceATXFlag);
    	    		RunnerClass.setCaptiveInsurenceATXFee(captiveInsurenceATXFee);
    	    	}
    	    } 
    	    catch(Exception e)
    	    {}
    		
    	
    		
    		if(company.equalsIgnoreCase("Florida")) {
    			 String optionValue1 = TessaractTest.floridaLiquidizedAddendumOptionCheck(file,SNo);
    			 if(optionValue1.equals("Option 1"))
    			    {
    			    	floridaLiquidizedAddendumOption1Check =  true;
    			    	RunnerClass.setEarlyTermination("two (2)");
    			    	
    			    }
    		}
			RunnerClass.setFloridaLiquidizedAddendumOption1Check(floridaLiquidizedAddendumOption1Check);
			
    	    
			//Late Fee Rule
    		LateFeeRuleTypeAssigner.lateFeeRule(text);
         
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
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
	
	
	

	 public static String capitalizeFirstLetter(String str) {
	    	// Convert the string to char array
	        char[] chars = str.toCharArray();
	        boolean capitalizeNext = true;

	        // Iterate through each character
	        for (int i = 0; i < chars.length; i++) {
	            // If the current character is a letter and we need to capitalize the next letter
	            if (Character.isLetter(chars[i]) && capitalizeNext) {
	                chars[i] = Character.toUpperCase(chars[i]);
	                capitalizeNext = false; // Set to false as we have capitalized the letter
	            }
	            // If the current character is a space, set capitalizeNext to true
	            else if (Character.isWhitespace(chars[i])) {
	                capitalizeNext = true;
	            }
	        }
	     // Convert the char array back to string and return
	        return new String(chars);
	    }

	
	}
