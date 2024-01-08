package PDFDataExtract;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.PDFReader;
import mainPackage.RunnerClass;
import mainPackage.TessaractTest;

public class NewMexico_Format2 
{
	public static String text ="";
	public static  boolean  format2() throws Exception
	//public static void main(String[] args) throws Exception 
	{
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Tennessee\\Format 2\\Lease_031.22_05.23_1327_Everwood_Dr_Ashland_C_(1).pdf");
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		PDDocument document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    
	    try
	    {
	    	String commensementRaw = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.commensementDate_Prior)+PDFAppConfig.NewMexico_Format2.commensementDate_Prior.length()+1).trim();//,text.indexOf(PDFAppConfig.NewMexico_Format2.commensementDate_After)).trim();
	    	 PDFReader.commencementDate = commensementRaw.substring(0, commensementRaw.indexOf('(')).trim();
	    	 PDFReader.commencementDate = PDFReader.commencementDate.trim().replaceAll(" +", " ");
		    System.out.println("Commensement Date = "+PDFReader.commencementDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	PDFReader.commencementDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	String expirationDateRaw  = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.expirationDate_Prior)+PDFAppConfig.NewMexico_Format2.expirationDate_Prior.length()).trim();//,text.indexOf(PDFAppConfig.NewMexico_Format2.expirationDate_After)).trim();
	    	PDFReader.expirationDate = expirationDateRaw.substring(0,expirationDateRaw.indexOf('(')).trim();
	    	PDFReader.expirationDate = PDFReader.expirationDate.trim().replaceAll(" +", " ");
	    	System.out.println("Expiration Date = "+PDFReader.expirationDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	PDFReader.expirationDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	PDFReader.proratedRentDate = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.proratedRentDate_Prior)+PDFAppConfig.NewMexico_Format2.proratedRentDate_Prior.length()+1,text.indexOf(PDFAppConfig.NewMexico_Format2.proratedRentDate_After)).trim();
		    System.out.println("prorated Rent Date = "+PDFReader.proratedRentDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	PDFReader.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	PDFReader.proratedRent = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.proratedRent_Prior)+PDFAppConfig.NewMexico_Format2.proratedRent_Prior.length()).split(" ")[0].trim();
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
	    System.out.println("prorated Rent = "+PDFReader.proratedRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    	PDFReader.monthlyRent = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.monthlyRent_Prior)+PDFAppConfig.NewMexico_Format2.monthlyRent_Prior.length()).split(" ")[0].trim();
	    	if(!PDFReader.monthlyRent.contains("."))
	    		PDFReader.monthlyRent = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.monthlyRent_Prior2)+PDFAppConfig.NewMexico_Format2.monthlyRent_Prior2.length()).split(" ")[0].trim();
	    	if(PDFReader.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	PDFReader.monthlyRent = "Error";
		    }
	    	if(PDFReader.monthlyRent.contains("*")||text.contains(PDFAppConfig.NewMexico_Format2.monthlyRentAvailabilityCheck)==true)
	    	{
	    		PDFReader.incrementRentFlag = true;
	    		PDFReader.monthlyRent = PDFReader.monthlyRent.replace("*", "");
	    		System.out.println("Monthly Rent has Asterick *");
	    		
	    		//PDFReader.increasedRent_amount = text.substring(text.indexOf(". $")+". $".length()).trim().split(" ")[0];
	    		String increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+PDFReader.commencementDate.trim()+" through ";
	    		 String endDateArray[] = text.substring(text.indexOf(". $")+". $".length()).split(" ");
	    		if(endDateArray[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
	    		 {
	    		  PDFReader.increasedRent_previousRentEndDate = endDateArray[0]+" "+endDateArray[1]+" "+endDateArray[2];
	    		  System.out.println("Increased Rent - Previous rent end date = "+PDFReader.increasedRent_previousRentEndDate);
	    		 
	    		  String newRentStartDate[] = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig.NewMexico_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    		  PDFReader.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    		  System.out.println("Increased Rent - New Rent Start date = "+PDFReader.increasedRent_newStartDate);
	    		  
	    		  String increasedRentRaw = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig.NewMexico_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    		  PDFReader.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
	    		  System.out.println("Increased Rent - Amount = "+PDFReader.increasedRent_amount); 
	    		}
	    		else 
	    		 {
	    			 String adding0toMonth = "0"+PDFReader.commencementDate.trim().split(" ")[1];
	    			 String commeseDate = PDFReader.commencementDate.trim().replace(PDFReader.commencementDate.trim().split(" ")[1], adding0toMonth);
	    			 increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+commeseDate+" through ";
		    		 String endDateArray2[] = text.substring(text.indexOf(increasedRent_ProviousRentEndDate)+increasedRent_ProviousRentEndDate.length()).split(" ");
		    		 if(endDateArray2[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
		    		 {
		    		  PDFReader.increasedRent_previousRentEndDate = endDateArray2[0]+" "+endDateArray2[1]+" "+endDateArray2[2];
		    		  System.out.println("Increased Rent - Previous rent end date = "+PDFReader.increasedRent_previousRentEndDate);
		    		 
		    		  String newRentStartDate[] = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig.NewMexico_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
		    		  PDFReader.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
		    		  System.out.println("Increased Rent - New Rent Start date = "+PDFReader.increasedRent_newStartDate);
		    		  
		    		  String increasedRentRaw = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig.NewMexico_Format2.increasedRent_newStartDate_Prior.length()).trim();
		    		  PDFReader.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
		    		  System.out.println("Increased Rent - Amount = "+PDFReader.increasedRent_amount); 
		    		 }
	    		 }
	    	}
	    }
	    catch(Exception e)
	    {
	    	PDFReader.monthlyRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Monthly Rent = "+PDFReader.monthlyRent);//.substring(commensementDate.lastIndexOf(":")+1));
	   
	    try
	    {
	    	PDFReader.adminFee = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.adminFee_prior)+PDFAppConfig.NewMexico_Format2.adminFee_prior.length()).split(" ")[0].trim();
	    	if(PDFReader.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
	    		PDFReader.adminFee = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.adminFee_prior2)+PDFAppConfig.NewMexico_Format2.adminFee_prior2.length()).split(" ")[0].trim();
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
	    System.out.println("Admin Fee = "+PDFReader.adminFee);//.substring(commensementDate.lastIndexOf(":")+1));
	  //Resident Benefits Package 
	    if(text.contains(PDFAppConfig.NewMexico_Format2.residentBenefitsPackageAddendumCheck)&&!text.contains("Resident Benefits Package Opt-Out Addendum"))
	    {
	    	PDFReader.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    PDFReader.residentBenefitsPackage = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.AB1_residentBenefitsPackage_Prior)+PDFAppConfig.NewMexico_Format2.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
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
	    	//OKC_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(PDFAppConfig.NewMexico_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	PDFReader.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
		    	PDFReader.HVACFilterFlag =true;
		    	PDFReader.airFilterFee = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.HVACAirFilter_prior)+PDFAppConfig.NewMexico_Format2.HVACAirFilter_prior.length()).split(" ")[0].trim();
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
		    System.out.println("HVAC Air Filter Fee = "+PDFReader.airFilterFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    try
	    {
	    	PDFReader.occupants = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.occupants_Prior)+PDFAppConfig.NewMexico_Format2.occupants_Prior.length(),text.indexOf(PDFAppConfig.NewMexico_Format2.occupants_After)).trim();
		    System.out.println("Occupants = "+PDFReader.occupants);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	PDFReader.occupants = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Fee Rule
	    NewMexico_Format2.lateFeeRule();
	    
	    
	  //Prepayment Charge
	    try
	    {
		if(RunnerClass.portfolioType.contains("MCH"))
		{
		try
		{
		PDFReader.prepaymentCharge =String.valueOf(Double.parseDouble(PDFReader.monthlyRent.replace(",", "")) - Double.parseDouble(PDFReader.proratedRent.replace(",", ""))); 
		}
		catch(Exception e)
		{
			PDFReader.prepaymentCharge ="Error";
		}
		System.out.println("Prepayment Charge = "+PDFReader.prepaymentCharge);
		}
	    }
	    catch(Exception e) {}
	    //Early Termination
		try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.earlyTermination_Prior)+PDFAppConfig.NewMexico_Format2.earlyTermination_Prior.length()).split(" ");
	    	
		   PDFReader.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(OKC_PDFAppConfig.AB_earlyTerminationFee_Prior)+OKC_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(OKC_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	PDFReader.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
		System.out.println("Early Termination = "+PDFReader.earlyTermination);
	    // Checking Pet Addendum is available or not
	    
	    if(text.contains(PDFAppConfig.NewMexico_Format2.petAgreementAvailabilityCheck)==true)
	    	PDFReader.petFlag =  true;
	    else if(PDFReader.petFlag = text.contains(PDFAppConfig.NewMexico_Format2.petAgreementAvailabilityCheck2)==true)
	    	PDFReader.petFlag =  true;
	    else PDFReader.petFlag =  false;
	    
	    System.out.println("Pet Addendum Available = "+PDFReader.petFlag);
	    if(PDFReader.petFlag ==true)
	    {
	    	PDFReader.petFlag = true;
			    	try
			    	{
			    		 String proratedPetRaw = "Prorated Pet Rent: On or before "+PDFReader.commencementDate.trim()+" Tenant will pay Landlord $";
				    		PDFReader.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0].trim();//.replaceAll("[a-ZA-Z,]", "");
				    		if(PDFReader.proratedPetRent.matches(".*[a-zA-Z]+.*")||PDFReader.proratedPetRent.trim().equals("1."))
						    {
						    	PDFReader.proratedPetRent = "Error";
						    }
				    		//PDFReader.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			        }
				    catch(Exception e)
				    {
				    PDFReader.proratedPetRent = "Error";	
				    e.printStackTrace();
				    }
	    	System.out.println("Prorated Pet rent= "+PDFReader.proratedPetRent.trim());
	    	try
    		{
    			PDFReader.petRent = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.petRent_Prior)+PDFAppConfig.NewMexico_Format2.petRent_Prior.length()).split(" ")[0].trim();
				 //System.out.println("Pet rent = "+PDFReader.petRent.trim());
				 if(RunnerClass.onlyDigits(PDFReader.petRent)==false)
				    {
				    	 PDFReader.petRent = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.petRent_Prior2)+PDFAppConfig.NewMexico_Format2.petRent_Prior2.length()).trim().split(" ")[0];
				    }
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
	    	System.out.println("Pet rent= "+PDFReader.petRent.trim());
	    	
	    	//Pet Security Deposit -- Need to find the text of Pet Security Deposit for the format PDF, until then, it is commented
	    	/*
	    	try
	    	{
	    	OKC_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.securityDeposit_Prior)+PDFAppConfig.NewMexico_Format2.securityDeposit_Prior.length()).trim().split(" ")[0];//,text.indexOf(PDFAppConfig.NewMexico_Format2.securityDeposit_After));
		    System.out.println("Security Deposit = "+OKC_PropertyWare.securityDeposit.trim());
		    if(RunnerClass.onlyDigits(OKC_PropertyWare.petSecurityDeposit)==true)
		    {
		    	OKC_PropertyWare.petSecurityDepositFlag = true;
		    	System.out.println("Security Deposit is checked and has value");
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	OKC_PropertyWare.securityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	*/
	    	String typeSubString = "";
	    	try
	    	{
	    	typeSubString = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.typeWord_Prior)+PDFAppConfig.NewMexico_Format2.typeWord_Prior.length(),text.indexOf(PDFAppConfig.NewMexico_Format2.typeWord_After));
	    	}
	    	catch(Exception e)
	    	{
	    		try
	    		{
	    		typeSubString = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.typeWord_Prior)+PDFAppConfig.NewMexico_Format2.typeWord_Prior.length(),text.indexOf(PDFAppConfig.NewMexico_Format2.typeWord_After2));
	    		}
	    		catch(Exception e2)
	    		{
	    			try
	    			{
	    				typeSubString = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.typeWord_Prior2)+PDFAppConfig.NewMexico_Format2.typeWord_Prior2.length(),text.indexOf(PDFAppConfig.NewMexico_Format2.typeWord_After3));
	    			}
	    			catch(Exception e3)
	    			{
	    		    typeSubString = "";
	    			}
	    		}
	    	}
	    	 String newText = typeSubString.replace("Type:","");
			    int countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
			    System.out.println("Type: occurences = "+countOfTypeWordInText);
	    	
	    	PDFReader.petType = new ArrayList();
		    PDFReader.petBreed = new ArrayList();
		    PDFReader.petWeight = new ArrayList();
		    for(int i =0;i<countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.NewMexico_Format2.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
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
		    	PDFReader.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.petOneTimeNonRefundable_Prior)+PDFAppConfig.NewMexico_Format2.petOneTimeNonRefundable_Prior.length(),text.indexOf(PDFAppConfig.NewMexico_Format2.petOneTimeNonRefundable_After)).trim();
		    	if(PDFReader.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	PDFReader.petOneTimeNonRefundableFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	try
		    	{
		    		PDFReader.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.petOneTimeNonRefundable_Prior2)+PDFAppConfig.NewMexico_Format2.petOneTimeNonRefundable_Prior2.length()).trim().split(",")[0];
				    //System.out.println("pet one time non refundable = "+PDFReader.petOneTimeNonRefundableFee.trim());
		    		if(PDFReader.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
				    {
				    	PDFReader.petOneTimeNonRefundableFee = "Error";
				    }
		    	}
		    	catch(Exception e2)
		    	{
		    	PDFReader.petOneTimeNonRefundableFee =  "Error";
		    	e2.printStackTrace();
		    	}
		    }  
		    System.out.println("pet one time non refundable = "+PDFReader.petOneTimeNonRefundableFee.trim());
		    
		  
		    
		    
	    }
	    
	  //Service Animal Addendum check
	    try
	    {
	    if(text.contains(PDFAppConfig.NewMexico_Format2.serviceAnimalText))
	    {
	    	PDFReader.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.AB_serviceAnimal_typeWord_Prior)+PDFAppConfig.NewMexico_Format2.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(PDFAppConfig.NewMexico_Format2.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		   PDFReader.serviceAnimalPetType = new ArrayList();
		    PDFReader.serviceAnimalPetBreed = new ArrayList();
		    PDFReader.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.NewMexico_Format2.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
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
	    	if(text.contains(PDFAppConfig.NewMexico_Format2.concessionAddendumText))
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
	    	if(text.contains(PDFAppConfig.NewMexico_Format2.smartHomeAgreementCheck))
	    	{
	    		PDFReader.smartHomeAgreementCheck = true;
	    		System.out.println("Smart Home Agreement is available");
	    		try
	    		{
	    			PDFReader.smartHomeAgreementFee=text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.smartHomeAgreementFee_Prior)+PDFAppConfig.NewMexico_Format2.smartHomeAgreementFee_Prior.length()).trim().split(" ")[0];
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
	    //document.close();
		return true;
	}
	
	public static boolean lateFeeRule()
	{
		String lateFeeRuleText ="";
		try
		{
		 lateFeeRuleText = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.lateFeeRuleText_Prior)+PDFAppConfig.NewMexico_Format2.lateFeeRuleText_Prior.length(),text.indexOf(PDFAppConfig.NewMexico_Format2.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.lateFeeRuleText_Prior)+PDFAppConfig.NewMexico_Format2.lateFeeRuleText_Prior.length(),text.indexOf(PDFAppConfig.NewMexico_Format2.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
			return false;
			}
		}
		if(lateFeeRuleText.contains(PDFAppConfig.NewMexico_Format2.lateFeeRule_whicheverIsGreater))
		{
			PDFReader.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
			RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
			//PDFReader.lateFeeType ="Greater of Flat Fee or Percentage"; 
		//Late charge day
			try
			{
		   // PDFReader.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(OKC_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+OKC_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				PDFReader.lateChargeDay =  lateFeeRuleText.split(PDFAppConfig.NewMexico_Format2.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				PDFReader.lateChargeDay =PDFReader.lateChargeDay.substring(PDFReader.lateChargeDay.lastIndexOf(" ")+1);
		    PDFReader.lateChargeDay =  PDFReader.lateChargeDay.replaceAll("[^0-9]", "");
		    if(PDFReader.lateChargeDay.trim().equals(""))
		    {
		    	PDFReader.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf("11:59 PM of the ")+"11:59 PM of the ".length()).trim().split(" ")[0];
		    	PDFReader.lateChargeDay =  PDFReader.lateChargeDay.replaceAll("[^0-9]", "");
		    }
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
		    PDFReader.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(PDFAppConfig.NewMexico_Format2.lateFeeRule_whicheverIsGreater_lateFeePercentage)+PDFAppConfig.NewMexico_Format2.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
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
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(PDFAppConfig.NewMexico_Format2.lateFeeRule_whicheverIsGreater_lateFeeAmount)+PDFAppConfig.NewMexico_Format2.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
        PDFReader.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	PDFReader.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+PDFReader.flatFeeAmount);
        RunnerClass.flatFee =PDFReader.flatFeeAmount;
         return true;
		}
		else 
		if(lateFeeRuleText.contains(PDFAppConfig.NewMexico_Format2.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			PDFReader.lateFeeRuleType = "initialFeePluPerDayFee";
			//PDFReader.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			PDFReader.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    PDFReader.lateChargeFee = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.AB_lateFee_Prior)+PDFAppConfig.NewMexico_Format2.AB_lateFee_Prior.length()).trim().split(" ")[0];
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
	 	    	PDFReader.lateFeeChargePerDay = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.AB_additionalLateChargesPerDay_Prior)+PDFAppConfig.NewMexico_Format2.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(OKC_PDFAppConfig.AB_additionalLateChargesPerDay_After));
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
	 	    	PDFReader.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.AB_additionalLateChargesLimit_Prior)+PDFAppConfig.NewMexico_Format2.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(OKC_PDFAppConfig.AB_additionalLateChargesLimit_After));
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
		else if(lateFeeRuleText.contains(PDFAppConfig.NewMexico_Format2.lateFeeRule_mayNotExceedAmount)||lateFeeRuleText.contains("and late charges are paid in full"))
			{
			PDFReader.lateFeeRuleType = "initialFeePluPerDayFee";
			//PDFReader.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
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
			PDFReader.lateChargeFee = PDFReader.lateChargeFee.replaceAll("[^0-9.]", "");
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
			if(lateFeeRuleText.contains(PDFAppConfig.NewMexico_Format2.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
				PDFReader.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
				RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
				
			//Late Charge Day
			try
	 	    {
			PDFReader.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("11:59 PM of the ")+"11:59 PM of the ".length()).trim().split(" ")[0];
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
			return false;
	 	    */
		   }
		return true;
		
	}
		
		
		/*
		//Late Charge Day
		try
	    {
	    	PDFReader.lateChargeDay = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.lateFeeDay_Prior)+PDFAppConfig.NewMexico_Format2.lateFeeDay_Prior.length()).trim().split(" ")[0].trim().replace("[^0-9]", "");
	    	PDFReader.lateChargeDay = PDFReader.lateChargeDay.replaceAll("[^\\d]", "");
	    	System.out.println("Late Charge Day = "+PDFReader.lateChargeDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	PDFReader.lateChargeDay = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Charge Fee
	    try
	    {
	    	PDFReader.lateChargeFee = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.lateChargePerDayFee)+PDFAppConfig.NewMexico_Format2.lateChargePerDayFee.length()).trim().split(" ")[0].trim();
	    	//PDFReader.lateChargeFee = PDFReader.lateChargeFee.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	PDFReader.lateChargeFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Late Fee = "+PDFReader.lateChargeFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    /*
	    //Per Day Fee
	    try
	    {
	    	PDFReader.additionalLateCharges = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.additionaLateCharge_Prior)+PDFAppConfig.NewMexico_Format2.additionaLateCharge_Prior.length()).trim().split(" ")[0].trim();
	    	PDFReader.additionalLateCharges = PDFReader.additionalLateCharges.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	PDFReader.additionalLateCharges = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Additional Late Charges  = "+PDFReader.additionalLateCharges);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    if(OKC_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
	    	PDFReader.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.NewMexico_Format2.additionaLateCharge_Prior)+PDFAppConfig.NewMexico_Format2.additionaLateCharge_Prior.length()).split(" ")[0].trim();
		    System.out.println("Additional Late Charges  = "+PDFReader.additionalLateChargesLimit);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    }
	    catch(Exception e)
	    {
	    	PDFReader.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    */




}
