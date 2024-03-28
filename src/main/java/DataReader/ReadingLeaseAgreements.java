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

import io.github.bonigarcia.wdm.WebDriverManager;
import mainPackage.AppConfig;
import mainPackage.PDFReader;
import mainPackage.RunnerClass;
import mainPackage.TessaractTest;

public class ReadingLeaseAgreements {
	
	public static String increasedRent_newStartDate="";
	
	public static boolean incrementRentFlag;
	public static boolean residentBenefitsPackageAvailabilityCheck;
	public static boolean HVACFilterFlag;
	public static boolean petFlag;
	public static boolean serviceAnimalFlag;
	public static boolean concessionAddendumFlag;
	public static boolean smartHomeAgreementCheck;
	public static boolean captiveInsurenceATXFlag;
	

	public static String residentBenefitsPackage="";
	public static String airFilterFee="";
	public static String petSecurityDeposit="";
	public static String proratedPetRent="";
	public static String petRent="";
	public static String petRentTaxAmount="";
	public static String totalPetRentWithTax="";
	public static String petOneTimeNonRefundableFee="";
	public static String smartHomeAgreementFee="";
	public static String captiveInsurenceATXFee = "";
	public static String earlyTermination="";
	public static String prepaymentCharge="";
	public static String increasedRent_amount="";
	public static List<String> allIncreasedRent_amounts=new ArrayList(); 
	
	
	
	public static ArrayList<String> petType = new ArrayList(); 
	public static ArrayList<String> petBreed = new ArrayList();
	public static ArrayList<String> petWeight = new ArrayList();
	public static ArrayList<String> serviceAnimalPetType;
    public static ArrayList<String> serviceAnimalPetBreed;
    public static ArrayList<String> serviceAnimalPetWeight;
	
	
	public static void dataRead(String fileName) throws Exception 
	{
		String text="";
		String commencementDate ="";
		String expirationDate="";
		String proratedRent="";
		String proratedRentDate="";
		String monthlyRent="";
		boolean monthlyRentTaxFlag;
		String monthlyRentTaxAmount="";
		String adminFee="";
		String occupants="";

		try {
			File file = RunnerClass.getLastModified(fileName);
			//File file = new File("C:\\SantoshMurthyP\\Lease Audit Automation\\Lease_02.22_02.23_200_Doc_Johns_Dr_ATX_Smith (3).pdf");
			FileInputStream fis = new FileInputStream(file);
			PDDocument document = PDDocument.load(fis);
		    text = new PDFTextStripper().getText(document);
		    text = text.replaceAll(System.lineSeparator(), " ");
		    text = text.trim().replaceAll(" +", " ");
		    text = text.toLowerCase();
		 
	
			
		       
		            
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
			RunnerClass.setProrateRentDate(RunnerClass.convertDate(proratedRentDate));
			//incrementRentFlag= dataExtractionClass.getFlags(text,"rent:^*Per the Landlord\\, Monthly Rent");
			
			PDFReader.concessionAddendumFlag = dataExtractionClass.getFlags(text, "rent:^This is a CONCESSION ADDENDUM to your Lease Agreement");
			System.out.println("Concession Addendum Flag = "+ PDFReader.concessionAddendumFlag);
			
			monthlyRent =dataExtractionClass.getValues(text,"Monthly Rent:^Monthly Rent due in the amount of^@Monthly Rent:^Tenant will pay Landlord monthly rent in the amount of^@monthly installments,^on or before the 1st day of each month, in the amount of^@monthly installments,^Tenant will pay Landlord monthly rent in the amount of^");
			System.out.println("Monthly Rent Amount = "+ monthlyRent);
			RunnerClass.setMonthlyRent(monthlyRent);
			
			allIncreasedRent_amounts =dataExtractionClass.getMultipleValues(text, "Monthly Rent:^Monthly Rent due in the amount of^@Monthly Rent:^Tenant will pay Landlord monthly rent in the amount of^@monthly installments,^on or before the 1st day of each month, in the amount of^@monthly installments,^Tenant will pay Landlord monthly rent in the amount of^") ;
			if (allIncreasedRent_amounts.size() > 1) {
	            double firstValue = Double.parseDouble(allIncreasedRent_amounts.get(0));
	            for (int i = 1; i < allIncreasedRent_amounts.size(); i++) {
	                double currentValue = Double.parseDouble(allIncreasedRent_amounts.get(i));
	                if (currentValue > firstValue) {
	                	PDFReader.incrementRentFlag = true;
	                	System.out.println("Increment Rent Flag = "+ PDFReader.incrementRentFlag);
	                	PDFReader.increasedRent_amount = String.valueOf(currentValue);
	                	System.out.println("Increment Rent Amount = "+ PDFReader.increasedRent_amount);
	                	PDFReader.increasedRent_newStartDate = dataExtractionClass.getSecondDate(text, "Monthly Rent:^Month");
	                	System.out.println("Increased Rent - New Rent Start date =  "+PDFReader.increasedRent_newStartDate);
	                    //System.out.println("Value " + increasedRent_amounts.get(i) + " is greater than the first value.");
	                    break;
	                }
	            }
	        }
			
			monthlyRentTaxFlag =dataExtractionClass.getFlags(text,"rent:^plus the additional amount of $@rent:^plus applicable sales tax and administrative fees of $");
			System.out.println("Monthly Rent Tax Flag = "+ monthlyRentTaxFlag);
			RunnerClass.setMonthlyRentTaxFlag(monthlyRentTaxFlag);
			if(monthlyRentTaxFlag == true) {
				monthlyRentTaxAmount= dataExtractionClass.getValues(text, "Monthly Rent:^plus applicable sales tax and administrative fees of^@Monthly Rent:^plus the additional amount of^@monthly installments,^plus the additional amount of^");
				System.out.println("Monthly Rent Tax Amount = "+ monthlyRentTaxAmount);
				RunnerClass.setMonthlyRentTaxAmount(monthlyRentTaxAmount);
			}
			
			adminFee = dataExtractionClass.getValues(text, "Lease Administrative Fee(s):^preparation fee in the amount of^@Lease Administrative Fee(s):^An annual lease preparation fee in the amount of^");
			System.out.println("Admin Fees = "+ adminFee);
			RunnerClass.setAdminFee(adminFee);
			
			PDFReader.residentBenefitsPackageAvailabilityCheck = dataExtractionClass.getFlags(text,"rent:^Resident Benefits Package (“RBP”) Program and Fee:@rent:^Resident Benefits Package (RBP) Lease Addendum@rent:^Resident Benefits Package Opt\\-Out Addendum");
			System.out.println("resident benefit package Availability Flag = "+ PDFReader.residentBenefitsPackageAvailabilityCheck); 
			if(PDFReader.residentBenefitsPackageAvailabilityCheck == true) {
				PDFReader.residentBenefitsPackage = dataExtractionClass.getValues(text, "Resident Benefits Package (“RBP”) Program and Fee:^Tenant agrees to pay a Resident Benefits Package Fee of^");
				System.out.println("Resident Benefit Package Fee = "+ PDFReader.residentBenefitsPackage);
			}
			PDFReader.HVACFilterFlag = dataExtractionClass.getFlags(text, "rent:^HVAC FILTER MAINTENANCE PROGRAM OPT-OUT ADDENDUM@rent:^HVAC Filter Maintenance Program Fee of $");
			System.out.println("HVAC Filter Flag = "+ PDFReader.HVACFilterFlag);
			if(PDFReader.HVACFilterFlag == true) {
				PDFReader.airFilterFee = dataExtractionClass.getValues(text, "HVAC FILTER MAINTENANCE PROGRAM OPT-OUT ADDENDUM^HVAC Filter Maintenance Program Fee of");
				System.out.println("HVAC Air Filter Fee = "+ PDFReader.airFilterFee);
			}
			
			occupants= dataExtractionClass.getTextWithStartandEndValue(text, "USE AND OCCUPANCY:^this Lease are:^Only two Tenants@USE AND OCCUPANCY:^this Lease are:^B. Phone Numbers@USE AND OCCUPANCY:^ages of all occupants):^NO OTHER OCCUPANTS SHALL RESIDE@USE AND OCCUPANCY:^ages of all occupants):^B. Phone Numbers:@USE AND OCCUPANCY:^listed as follows:^Property shall be used by Tenant@USE AND OCCUPANCY:^Name, Age ^The Tenant and the Minor Occupants listed above^@USE AND OCCUPANCY:^this Lease are^B. Phone Numbers@OCCUPANTS^Landlord/Landlord’s Broker:^11. MAINTENANCE@OCCUPANTS^Landlord/Landlord’s Broker:^10. MAINTENANCE");
			System.out.println("Occupants Name = "+ occupants);
			RunnerClass.setOccupants(occupants);
			proratedRent = dataExtractionClass.getValues(text, "Prorated Rent:^Tenant will pay Landlord@prorated rent,^Tenant will pay Landlord@Prorated Rent:^Tenant will pay Landlord");
			System.out.println("Prorated rent = "+ proratedRent);
			RunnerClass.setProrateRent(proratedRent);
			
			//if(RunnerClass.portfolioType.contains("MCH"))
	  		{
	  			if(proratedRent.equalsIgnoreCase("n/a")||proratedRent.equalsIgnoreCase("Error")||proratedRent.equalsIgnoreCase(""))
	  			{
	  				PDFReader.prepaymentCharge = "Error";
	  			}
	  			else
	  			{
		  		try
		  		{
		  			PDFReader.prepaymentCharge =String.valueOf(Double.parseDouble(monthlyRent.trim().replace(",", "")) - Double.parseDouble(proratedRent.trim().replace(",", ""))); 
		  		}
		  		catch(Exception e)
		  		{
		  			PDFReader.prepaymentCharge ="Error";
		  		}
		  		}
	  			System.out.println("Prepayment Charge = "+PDFReader.prepaymentCharge);
	  		 }
			
			
			
			if(PDFReader.petFlag == true) {
				//petSecurityDeposit = dataExtractionClass.getValues(text, "PET AUTHORIZATION AND PET DESCRIPTION:^On or before the date Tenant moves into the Property, Tenant will pay Landlord an additional deposit of@THIS PET ADDENDUM^Tenant will, upon execution of this agreement, pay Landlord");
				//System.out.println("Pet Security Deposit = "+ petSecurityDeposit);
				PDFReader.proratedPetRent = dataExtractionClass.getValues(text, "Prorated Pet Rent:^Tenant will pay Landlord");
				System.out.println("Prorated Pet Rent = "+ PDFReader.proratedPetRent);
				PDFReader.petRent = dataExtractionClass.getValues(text, "THIS PET ADDENDUM^Tenant will pay Landlord monthly pet rent in the amount of@PET AUTHORIZATION AND PET DESCRIPTION:^Tenant will pay Landlord monthly pet rent in the amount of@PET AUTHORIZATION AND PET DESCRIPTION:^Tenant will pay Landlord a monthly pet inspection fee in the amount of");
				System.out.println("Pet Rent = "+ PDFReader.petRent);
				PDFReader.petRentTaxAmount = dataExtractionClass.getValues(text, "THIS PET ADDENDUM^tax and administrative fees of@PET AUTHORIZATION AND PET DESCRIPTION:^tax and administrative fees of");
				System.out.println("Pet Rent Tax Amount = "+ PDFReader.petRentTaxAmount);
				if(PDFReader.petRentTaxAmount != "Eroor" || PDFReader.petRentTaxAmount != null) {
					PDFReader.totalPetRentWithTax = dataExtractionClass.getValues(text, "THIS PET ADDENDUM^for a total of@PET AUTHORIZATION AND PET DESCRIPTION:^for a total of");
					System.out.println("Total Pet Rent With Tax = "+ PDFReader.totalPetRentWithTax);
				}
				PDFReader.petOneTimeNonRefundableFee = dataExtractionClass.getValues(text, "THIS PET ADDENDUM^Tenant will, upon execution of this agreement, pay Landlord@PET AUTHORIZATION AND PET DESCRIPTION:^Tenant will, upon execution of this agreement, pay Landlord");
				System.out.println("Pet One Time Non Refundable Fee = "+ PDFReader.petOneTimeNonRefundableFee);
				
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
			    	PDFReader.petType.add(type);
			    	int pet1Breedindex1 = nthOccurrence(typeSubString, "breed:", i+1)+"breed:".length()+1;
				    String subString = typeSubString.substring(pet1Breedindex1);
				    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
				   // System.out.println("Index 2 = "+(index2+index1));
				    String breed = subString.split("name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
				    System.out.println(breed);
				    PDFReader.petBreed.add(breed);
				    int pet1Weightindex1 = nthOccurrence(typeSubString, "weight:", i+1)+"weight:".length()+1;
				    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
				    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
				   // System.out.println("Index 2 = "+(index2+index1));
				    String weight = pet1WeightSubstring.split("age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
				    System.out.println(weight);
				    PDFReader.petWeight.add(weight);
			    }
			    
			  
			    PDFReader.serviceAnimalFlag = dataExtractionClass.getFlags(text,"SERVICE/SUPPORT ANIMAL AGREEMENT^SERVICE/SUPPORT ANIMAL AUTHORIZATION");
				System.out.println("Service Animal Flag = "+ PDFReader.serviceAnimalFlag);
				if(PDFReader.serviceAnimalFlag == true) {
						System.out.println("Service Animal Addendum is available");
					 	String typeSubStrings = dataExtractionClass.getTextWithStartandEndValue(text, "THIS PET ADDENDUM^Tenant has the following Service/Support Animal(s) on the Property until the above-referenced lease ends.^B. SERVICE/SUPPORT ANIMAL RULES@PET AUTHORIZATION AND PET DESCRIPTION:^Tenant has the following Service/Support Animal(s) on the Property until the above-referenced lease ends.^B. SERVICE/SUPPORT ANIMAL RULES");
				    	
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
					    	PDFReader.serviceAnimalPetType.add(type);
					    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubStrings, "breed:", i+1)+"breed:".length()+1;
						    String subString = typeSubStrings.substring(pet1Breedindex1);
						    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
						   // System.out.println("Index 2 = "+(index2+index1));
						    String breed = subString.split("name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
						    System.out.println(breed);
						    PDFReader.serviceAnimalPetBreed.add(breed);
						    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubStrings, "weight:", i+1)+"weight:".length()+1;
						    String pet1WeightSubstring = typeSubStrings.substring(pet1Weightindex1);
						    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
						   // System.out.println("Index 2 = "+(index2+index1));
						    String weight = pet1WeightSubstring.split("age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
						    System.out.println(weight);
						    PDFReader.serviceAnimalPetWeight.add(weight);
					    }
				}
			    
			}
			PDFReader.smartHomeAgreementCheck = dataExtractionClass.getFlags(text, "rent:^This Smart Home Agreement is subject");
			System.out.println("Smart Home Agreement Flag = "+ PDFReader.smartHomeAgreementCheck);
			if(PDFReader.smartHomeAgreementCheck == true) {
				PDFReader.smartHomeAgreementFee = dataExtractionClass.getValues(text, "This Smart Home Agreement is subject^Smart Home Agreement shall be");
				System.out.println("Smart Home Agreement Fee = "+ PDFReader.smartHomeAgreementFee);
			}
			PDFReader.earlyTermination = dataExtractionClass.getTextWithStartandEndValue(text, "Early Termination:^Landlord of^month’s rent at the time the Notice is provided");
    		System.out.println("Early Termination  = "+PDFReader.earlyTermination.trim());
			
    		// Check if Option 1 is selected in RBP Lease Agreement
    		
    	/*	String optionValue = TessaractTest.pdfScreenShot(file);
    		if(optionValue.equals("Option 1"))
    		{
    			PDFReader.captiveInsurenceATXFlag = true;
    			 try
	    	 	    {
    				 PDFReader.captiveInsurenceATXFee = text.substring(text.indexOf(PDFAppConfig.Austin_Format1.captiveInsurenceATXFee_Prior)+PDFAppConfig.Austin_Format1.captiveInsurenceATXFee_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	    	 		   if(PDFReader.captiveInsurenceATXFee.contains("per")||PDFReader.captiveInsurenceATXFee.contains("Per"))
	    	 			  PDFReader.captiveInsurenceATXFee = PDFReader.captiveInsurenceATXFee.trim().replace("per", "");
	    	 		    if(PDFReader.captiveInsurenceATXFee.matches(".*[a-zA-Z]+.*"))
	    	 		    {
	    	 		    	PDFReader.captiveInsurenceATXFee = "Error";
	    	 		    }
	    	 	    }
	    	 	    catch(Exception e)
	    	 	    {
	    	 	    	PDFReader.captiveInsurenceATXFee = "Error";
	    	 		    e.printStackTrace();
	    	 	    }
	    	    	 System.out.println("Captive Insurence ATX Fee  = "+PDFReader.captiveInsurenceATXFee.trim());
    		} */
			
		       
			
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
	
	
	
	}
