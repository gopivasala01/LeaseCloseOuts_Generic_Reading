package PDFDataExtract;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.PDFReader;
import mainPackage.RunnerClass;
import mainPackage.TessaractTest;

public class Arkansas_Format1 
{
	public static boolean petFlag;
	public static String text="";
	public static boolean format1() throws Exception
	//public static void main(String args[]) throws Exception
	{
		
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\SantoshMurthyP\\Lease Audit Automation\\Lease_02.22_02.23_200_Doc_Johns_Dr_ATX_Smith (3).pdf");
		FileInputStream fis = new FileInputStream(file);
		PDDocument document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    try
	    {
	    	PDFReader.commencementDate = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_commencementDate_Prior)+PDFAppConfig.Arkansas_Format1.AB_commencementDate_Prior.length());
	    	PDFReader.commencementDate = PDFReader.commencementDate.substring(0, PDFReader.commencementDate.indexOf(PDFAppConfig.Arkansas_Format1.AB_commencementDate_After)).trim();
	    	PDFReader.commencementDate = PDFReader.commencementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	PDFReader.commencementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+PDFReader.commencementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_expirationDate_Prior)+PDFAppConfig.Arkansas_Format1.AB_expirationDate_Prior.length());
		   PDFReader.expirationDate =expirationDateWaw.substring(0,expirationDateWaw.indexOf(PDFAppConfig.Arkansas_Format1.AB_expirationDate_After)).trim();
		   PDFReader.expirationDate = PDFReader.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 PDFReader.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+PDFReader.expirationDate);
	   try
	    {
		    PDFReader.proratedRent = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_proratedRent_Prior,1)+PDFAppConfig.Arkansas_Format1.AB_proratedRent_Prior.length()).trim().split(" ")[0];
		    if(PDFReader.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	PDFReader.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	PDFReader.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+PDFReader.proratedRent);
	    try
	    {
		    PDFReader.proratedRentDate = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_proratedRentDate_Prior)+PDFAppConfig.Arkansas_Format1.AB_proratedRentDate_Prior.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	PDFReader.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+PDFReader.proratedRentDate.trim());
	    /*
	    try
	    {
		    PDFReader.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_fullRentDate_Prior)+PDFAppConfig.Arkansas_Format1.AB_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+PDFReader.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		PDFReader.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_fullRentDate_Prior)+PDFAppConfig.Arkansas_Format1.AB_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+PDFReader.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	PDFReader.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    PDFReader.monthlyRent = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_fullRent_Prior)+PDFAppConfig.Arkansas_Format1.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(PDFAppConfig.Arkansas_Format1.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(PDFReader.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	PDFReader.monthlyRent = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_fullRent2_Prior)+PDFAppConfig.Arkansas_Format1.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(PDFReader.monthlyRent.contains("*"))
		    {
		    	PDFReader.monthlyRent = PDFReader.monthlyRent.replace("*","");
		    }
		    if(PDFReader.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	PDFReader.monthlyRent = text.substring(text.indexOf("Monthly Rent due in the amount of $")+"Monthly Rent due in the amount of $".length()).trim().split(" ")[0].trim();//,text.indexOf(PDFAppConfig.DFW_Format1.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    	 if(PDFReader.monthlyRent.matches(".*[a-zA-Z]+.*"))
				    {
				    	PDFReader.monthlyRent = "Error";
				    }
		    }
		    if(PDFReader.monthlyRent.endsWith(","))
		    {
		    	PDFReader.monthlyRent = PDFReader.monthlyRent.substring(0,PDFReader.monthlyRent.length()-1);
		    }
	    }
	    catch(Exception e)
	    {
	    	 PDFReader.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+PDFReader.monthlyRent.trim());
	    
	  //Increased Rent Check
	    try
	    {
	    	if(PDFReader.monthlyRent.contains("*")||text.contains(PDFAppConfig.Arkansas_Format2.monthlyRentAvailabilityCheck)==true)
	    	{
	    		PDFReader.incrementRentFlag = true;
	    		PDFReader.monthlyRent = PDFReader.monthlyRent.replace("*", "");
	    		System.out.println("Monthly Rent has Asterick *");
	    		
	    		//PDFReader.increasedRent_amount = text.substring(text.indexOf(". $")+". $".length()).trim().split(" ")[0];
	    		String increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+PDFReader.commencementDate.trim()+", through ";
	    		 //String endDateArray[] = text.substring(text.indexOf(". $")+". $".length()).split(" ");
	    		//if(endDateArray[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
	    		 //{
	    		  PDFReader.increasedRent_previousRentEndDate = text.substring(text.indexOf(increasedRent_ProviousRentEndDate)+increasedRent_ProviousRentEndDate.length(),text.indexOf(" shall be $"));
	    				  //endDateArray[0]+" "+endDateArray[1]+" "+endDateArray[2];
	    		  System.out.println("Increased Rent - Previous rent end date = "+PDFReader.increasedRent_previousRentEndDate);
	    		 
	    		  String newRentStartDate[] = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig.Arkansas_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    		  PDFReader.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    		  System.out.println("Increased Rent - New Rent Start date = "+PDFReader.increasedRent_newStartDate);
	    		  
	    		  String increasedRentRaw = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig.Arkansas_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    		  PDFReader.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
	    		  System.out.println("Increased Rent - Amount = "+PDFReader.increasedRent_amount); 
	    	}
	    }
	    catch(Exception e)
	    {
	    	
	    }
	    
	    try
	    {
		    PDFReader.adminFee = text.toLowerCase().substring(text.toLowerCase().indexOf(PDFAppConfig.Arkansas_Format1.AB_adminFee_Prior.toLowerCase())+PDFAppConfig.Arkansas_Format1.AB_adminFee_Prior.length()).trim().split(" ")[0];
		    if(PDFReader.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	PDFReader.adminFee = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.adminFee_prior2)+PDFAppConfig.Arkansas_Format1.adminFee_prior2.length()).split(" ")[0].trim();
	    		if(PDFReader.adminFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	PDFReader.adminFee = "Error";
			    }
		    }
	    }
	    catch(Exception e)
	    {
		    PDFReader.adminFee = "Error";
		    e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+PDFReader.adminFee.trim());
	    
	  //Resident Benefits Package 
	    if(text.contains(PDFAppConfig.Arkansas_Format1.residentBenefitsPackageAddendumCheck)&&!text.contains("Resident Benefits Package Opt-Out Addendum"))
	    {
	    	PDFReader.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    PDFReader.residentBenefitsPackage = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB1_residentBenefitsPackage_Prior)+PDFAppConfig.Arkansas_Format1.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(PDFReader.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	PDFReader.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    PDFReader.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+PDFReader.residentBenefitsPackage.trim());
	    	//PDFAppConfig.Arkansas_Format1.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(PDFAppConfig.Arkansas_Format1.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	PDFReader.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
			   String[] airFilterFeeArray = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_airFilterFee_Prior)+PDFAppConfig.Arkansas_Format1.AB_airFilterFee_Prior.length()).split(" ");
			   PDFReader.airFilterFee = airFilterFeeArray[0];
			   PDFReader.HVACFilterFlag =true;
			   if(PDFReader.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	PDFReader.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    PDFReader.airFilterFee = "Error";
		    e.printStackTrace();
		    }
		    }
		    System.out.println("Air Filter Fee = "+PDFReader.airFilterFee.trim());
	    }
	    try
	    {
	    	String earlyTerminationSectionText = text.substring(text.indexOf("Early Termination:"));
	    	String[] earlyTerminationRaw = earlyTerminationSectionText.substring(earlyTerminationSectionText.indexOf(PDFAppConfig.Arkansas_Format1.AB_earlyTerminationFee_Prior)+PDFAppConfig.Arkansas_Format1.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    PDFReader.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_earlyTerminationFee_Prior)+PDFAppConfig.Arkansas_Format1.AB_earlyTerminationFee_Prior.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	PDFReader.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+PDFReader.earlyTermination.trim());
	    try
	    {
	    	
		    PDFReader.occupants = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_occupants_Prior)+PDFAppConfig.Arkansas_Format1.AB_occupants_Prior.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.AB_occupants_After));
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		 PDFReader.occupants = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_occupants_Prior2)+PDFAppConfig.Arkansas_Format1.AB_occupants_Prior2.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.AB_occupants_After2));
	    	}
	    	catch(Exception e2)
	    	{
	    		try
	    		{
	    			PDFReader.occupants = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_occupants_Prior3)+PDFAppConfig.Arkansas_Format1.AB_occupants_Prior3.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.AB_occupants_After3));
	    		}
	    		catch(Exception e3)
	    		{
		        PDFReader.occupants ="Error";	
		        e.printStackTrace();
	    		}
	    	}
	    }
	    System.out.println("Occupants = "+PDFReader.occupants.trim());
	    
	    //Late charges 
	    //Decide Late Fee Rule
	   Arkansas_Format1.lateFeeRule();
	    
	  //Prepayment Charge
  		if(PDFReader.portfolioType.contains("MCH"))
  		{
  			if(PDFReader.proratedRent.equalsIgnoreCase("n/a")||PDFReader.proratedRent.equalsIgnoreCase("Error")||PDFReader.proratedRent.equalsIgnoreCase(""))
  			{
  				PDFReader.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		PDFReader.prepaymentCharge =String.valueOf(Double.parseDouble(PDFReader.monthlyRent.trim().replace(",", "")) - Double.parseDouble(PDFReader.proratedRent.trim().replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			PDFReader.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+PDFReader.prepaymentCharge);
  		 }
	    petFlag = text.contains(PDFAppConfig.Arkansas_Format1.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	PDFReader.petFlag = true;
	    	try
	    	{
	    	PDFReader.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_securityDeposity_Prior)+PDFAppConfig.Arkansas_Format1.AB_securityDeposity_Prior.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.AB_securityDeposity_After));
	    	if(PDFReader.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	PDFReader.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	PDFReader.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Pet Security Deposit = "+PDFReader.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(PDFReader.petSecurityDeposit.replace(".", ""))==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+PDFReader.commencementDate.trim()+" Tenant will pay Landlord $";
	    		PDFReader.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(PDFReader.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	PDFReader.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    PDFReader.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+PDFReader.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 PDFReader.petRent = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_petRent_Prior)+PDFAppConfig.Arkansas_Format1.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(PDFReader.petRent.contains(",for"))
	    		 {
	    			 PDFReader.petRent = PDFReader.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(PDFReader.petRent.matches(".*[a-zA-Z]+.*")==true)
		    			 PDFReader.petRent = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_petRent1_Prior)+PDFAppConfig.Arkansas_Format1.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    		 else 
		    		 PDFReader.petRent = RunnerClass.extractNumber(PDFReader.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			PDFReader.petRent = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_petRent1_Prior)+PDFAppConfig.Arkansas_Format1.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+PDFReader.petRent.trim());
	    			if(PDFReader.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	PDFReader.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	PDFReader.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+PDFReader.petRent.trim());
		    	//PDFReader.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_petFee_Prior));
    			PDFReader.petFee = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_petFee_Prior)+PDFAppConfig.Arkansas_Format1.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//PDFReader.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(PDFReader.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//PDFReader.petFee = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_petFee2_Prior)+PDFAppConfig.Arkansas_Format1.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	PDFReader.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+PDFReader.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_typeWord_Prior)+PDFAppConfig.Arkansas_Format1.AB_typeWord_Prior.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+countOfTypeWordInText);
		    
		    
		    for(int i =0;i<countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.Arkansas_Format1.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	PDFReader.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    PDFReader.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    PDFReader.petWeight.add(weight);
		    }
		    
		    
		    
		    try
		    {
		    	PDFReader.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_petFeeOneTime_Prior)+PDFAppConfig.Arkansas_Format1.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(PDFAppConfig.Arkansas_Format1.AB_petFeeOneTime_After));
		    	if(PDFReader.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
    		    {
    		    	PDFReader.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	PDFReader.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+PDFReader.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    //Service Animal Addendum check
	    try
	    {
	    if(text.contains(PDFAppConfig.Arkansas_Format1.serviceAnimalText))
	    {
	    	PDFReader.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_serviceAnimal_typeWord_Prior)+PDFAppConfig.Arkansas_Format1.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    PDFReader.serviceAnimalPetType = new ArrayList();
		    PDFReader.serviceAnimalPetBreed = new ArrayList();
		    PDFReader.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.Arkansas_Format1.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	PDFReader.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    PDFReader.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    PDFReader.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	PDFReader.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(PDFAppConfig.Arkansas_Format1.concessionAddendumText))
	    	{
	    		PDFReader.concessionAddendumFlag = true;
	    		System.out.println("Concession Addendum is available");
	    	}
	    }
	    catch(Exception e)
	    {}
	  //Smart Home Agreement
	    try
	    {
	    	if(text.contains(PDFAppConfig.Arkansas_Format1.smartHomeAgreementCheck))
	    	{
	    		PDFReader.smartHomeAgreementCheck = true;
	    		System.out.println("Smart Home Agreement is available");
	    		try
	    		{
	    			PDFReader.smartHomeAgreementFee=text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.smartHomeAgreementFee_Prior)+PDFAppConfig.Arkansas_Format1.smartHomeAgreementFee_Prior.length()).trim().split(" ")[0];
	    		}
	    		catch(Exception e)
	    		{
	    			PDFReader.smartHomeAgreementFee= "Error";
	    		}
	    	}
	    }
	    catch(Exception e)
	    {}
//RBP when Portfolio is ATX
	    
	    try
	    {
	    	if(RunnerClass.portfolioName.contains("ATX."))
	    	{
	    		if(text.contains(PDFAppConfig.Austin_Format1.residentBenefitsPackageAddendumCheck)&&!text.contains("Resident Benefits Package Opt-Out Addendum"))
	    	    {
	    	    	PDFReader.residentBenefitsPackageAvailabilityCheck = true;
	    	    	 try
	    	 	    {
	    	 		    PDFReader.residentBenefitsPackage = text.substring(text.indexOf(PDFAppConfig.Austin_Format1.RBPWhenPortfolioIsATX)+PDFAppConfig.Austin_Format1.RBPWhenPortfolioIsATX.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
                        if(PDFReader.residentBenefitsPackage.contains("month"))
                        	PDFReader.residentBenefitsPackage = PDFReader.residentBenefitsPackage.substring(0,PDFReader.residentBenefitsPackage.indexOf("month")).trim();
	    	 		    if(PDFReader.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	    	 		    {
	    	 		    	PDFReader.residentBenefitsPackage = "Error";
	    	 		    }
	    	 	    }
	    	 	    catch(Exception e)
	    	 	    {
	    	 		    PDFReader.residentBenefitsPackage = "Error";
	    	 		    e.printStackTrace();
	    	 	    }
	    	    	 System.out.println("Resident Benefits Package  = "+PDFReader.residentBenefitsPackage.trim());
	    	    	//PDFAppConfig.Austin_Format1.AB1_residentBenefitsPackage_Prior
	    	}
// Check if Option 1 is selected in RBP Lease Agreement
	    		
	    		String optionValue = TessaractTest.pdfScreenShot(file);
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
	    		}
	    	}
	    }
	    catch(Exception e)
	    {}
	    
		return true;
	    
	 // document.close();
	  //return true;
    }
	public static boolean lateFeeRule()
	{
		String lateFeeRuleText ="";
		try
		{
		 lateFeeRuleText = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.lateFeeRuleText_Prior)+PDFAppConfig.Arkansas_Format1.lateFeeRuleText_Prior.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.lateFeeRuleText_Prior)+PDFAppConfig.Arkansas_Format1.lateFeeRuleText_Prior.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
				try
				{
				lateFeeRuleText = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.lateFeeRuleText_Prior3)+PDFAppConfig.Arkansas_Format1.lateFeeRuleText_Prior3.length(),text.indexOf(PDFAppConfig.Arkansas_Format1.lateFeeRuleText_After3));
				}
				catch(Exception e3)
				{
				return false;
				}
			}
		}
		if(lateFeeRuleText.contains(PDFAppConfig.Arkansas_Format1.lateFeeRule_whicheverIsGreater))
		{
			PDFReader.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
			RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
			//PDFReader.lateFeeType ="Greater of Flat Fee or Percentage"; 
		//Late charge day
			try
			{
		   // PDFReader.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(PDFAppConfig.Arkansas_Format1.lateFeeRule_whicheverIsGreater_dueDay_Prior)+PDFAppConfig.Arkansas_Format1.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				PDFReader.lateChargeDay =  lateFeeRuleText.split(PDFAppConfig.Arkansas_Format1.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				PDFReader.lateChargeDay =PDFReader.lateChargeDay.substring(PDFReader.lateChargeDay.lastIndexOf(" ")+1);
		    PDFReader.lateChargeDay =  PDFReader.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				PDFReader.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+PDFReader.lateChargeDay);
			RunnerClass.dueDay_GreaterOf = PDFReader.lateChargeDay;
		//Late Fee Percentage
			try
			{
		    PDFReader.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(PDFAppConfig.Arkansas_Format1.lateFeeRule_whicheverIsGreater_lateFeePercentage)+PDFAppConfig.Arkansas_Format1.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    PDFReader.lateFeePercentage = PDFReader.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				PDFReader.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+PDFReader.lateFeePercentage);
         RunnerClass.percentage = PDFReader.lateFeePercentage;
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(PDFAppConfig.Arkansas_Format1.lateFeeRule_whicheverIsGreater_lateFeeAmount)+PDFAppConfig.Arkansas_Format1.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         PDFReader.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 PDFReader.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+PDFReader.flatFeeAmount);
        RunnerClass.flatFee = PDFReader.flatFeeAmount;
         return true;
		}
		else 
		if(lateFeeRuleText.contains(PDFAppConfig.Arkansas_Format1.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			PDFReader.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			PDFReader.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    PDFReader.lateChargeFee = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_lateFee_Prior)+PDFAppConfig.Arkansas_Format1.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //PDFReader.lateChargeFee =  PDFReader.lateChargeFee.substring(0,PDFReader.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    PDFReader.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+PDFReader.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = PDFReader.lateChargeFee;
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	PDFReader.lateFeeChargePerDay = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_additionalLateChargesPerDay_Prior)+PDFAppConfig.Arkansas_Format1.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(PDFAppConfig.Arkansas_Format1.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	PDFReader.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+PDFReader.lateFeeChargePerDay.trim());
	 	    RunnerClass.perDayFeeAmount = PDFReader.lateFeeChargePerDay;
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	PDFReader.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.Arkansas_Format1.AB_additionalLateChargesLimit_Prior)+PDFAppConfig.Arkansas_Format1.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(PDFAppConfig.Arkansas_Format1.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	PDFReader.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+PDFReader.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = PDFReader.additionalLateChargesLimit;
	 	 //Late Charge Day
			try
	 	    {
			PDFReader.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0];
			PDFReader.lateChargeDay = PDFReader.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+PDFReader.lateChargeDay.trim());
	 	    RunnerClass.dueDay_initialFee = PDFReader.lateChargeDay;
	 	   return true;
		}
		else if(lateFeeRuleText.contains(PDFAppConfig.Arkansas_Format1.lateFeeRule_mayNotExceedAmount)||lateFeeRuleText.contains(PDFAppConfig.Arkansas_Format1.lateFeeRule_mayNotExceed375))
			{
			PDFReader.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
			PDFReader.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			PDFReader.lateChargeDay = PDFReader.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+PDFReader.lateChargeDay.trim());
	 	   RunnerClass.dueDay_initialFee = PDFReader.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			PDFReader.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			PDFReader.lateChargeFee = PDFReader.lateChargeFee.replaceAll("[^0-9.]", "").substring(0, PDFReader.lateChargeFee.length()-1);
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+PDFReader.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = PDFReader.lateChargeFee;
	 	    // Additional Late Charges
	 	   try
	 	    {
			PDFReader.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("additional late charge of $")+"additional late charge of $".length()).trim().split(" ")[0];
			PDFReader.additionalLateCharges = PDFReader.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+PDFReader.additionalLateCharges.trim());
	 	   RunnerClass.perDayFeeAmount = PDFReader.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			PDFReader.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("s (initial and additional) may not exceed $")+"s (initial and additional) may not exceed $".length()).trim().split(" ")[0];
			PDFReader.additionalLateChargesLimit = PDFReader.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+PDFReader.additionalLateChargesLimit.trim());
	 	   RunnerClass.additionalLateChargesLimit = PDFReader.additionalLateChargesLimit;
			return true;
			}
		else 
			if(lateFeeRuleText.contains(PDFAppConfig.Arkansas_Format1.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
				PDFReader.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
				RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
				
			//Late Charge Day
			try
	 	    {
			PDFReader.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("place of payment on the ")+"place of payment on the ".length()).trim().split(" ")[0];
			PDFReader.lateChargeDay = PDFReader.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+PDFReader.lateChargeDay.trim());
	 	   RunnerClass.dueDay_GreaterOf = PDFReader.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			PDFReader.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0];
			//PDFReader.lateChargeFee = PDFReader.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+PDFReader.lateChargeFee.trim());
	 	   RunnerClass.percentage = PDFReader.lateChargeFee;
	 	   /*
	 	    // Additional Late Charges
	 	   try
	 	    {
			PDFReader.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			PDFReader.additionalLateCharges = PDFReader.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+PDFReader.additionalLateCharges.trim());
	 	    RunnerClass.maximumAmount = PDFReader.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			PDFReader.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			PDFReader.additionalLateChargesLimit = PDFReader.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+PDFReader.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = PDFReader.additionalLateChargesLimit;
			return true;
			}
			else
		   {
			PDFReader.lateFeeType ="";
		   }
		   */
			}
			else if(lateFeeRuleText.contains(PDFAppConfig.Arkansas_Format1.lateFeeRule_landlordTheLiquidatedSumOf))
			{
			PDFReader.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
			PDFReader.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("tenant is not received by landlord within ")+"tenant is not received by landlord within ".length()).trim().split(" ")[0];
			PDFReader.lateChargeDay = PDFReader.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+PDFReader.lateChargeDay.trim());
	 	   RunnerClass.dueDay_initialFee = PDFReader.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			PDFReader.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("landlord the liquidated sum of $")+"landlord the liquidated sum of $".length()).trim().split(" ")[0];
			PDFReader.lateChargeFee = PDFReader.lateChargeFee.replaceAll("[^0-9.]", "").substring(0, PDFReader.lateChargeFee.length()-1);
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+PDFReader.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = PDFReader.lateChargeFee;
	 	   /*
	 	    // Additional Late Charges
	 	   try
	 	    {
			PDFReader.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("additional late charges of $")+"additional late charges of $".length()).trim().split(" ")[0];
			PDFReader.additionalLateCharges = PDFReader.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+PDFReader.additionalLateCharges.trim());
	 	   RunnerClass.perDayFeeAmount = PDFReader.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			PDFReader.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("Additional late charges may not exceed $")+"Additional late charges may not exceed $".length()).trim().split(" ")[0];
			PDFReader.additionalLateChargesLimit = PDFReader.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	PDFReader.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+PDFReader.additionalLateChargesLimit.trim());
	 	   RunnerClass.additionalLateChargesLimit = PDFReader.additionalLateChargesLimit;
			*/
			return true;
			}
		return true;		
	}




}
