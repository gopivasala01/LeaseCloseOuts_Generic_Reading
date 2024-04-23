package DataReader;

import mainPackage.PDFReader;
import mainPackage.RunnerClass;

public class LateFeeRuleTypeAssigner {
	
	
	
	public static String lateFeeRuleText_Prior = "LATE CHARGES:";
	public static String lateFeeRuleText_After = "Returned Checks/NSF Fees:";
	public static String lateFeeRuleText_After2 = "RETURNED CHECKS:";
	public static String lateFeeRuleText_Prior3 = "Late Payment of Rent Fee";
	public static String lateFeeRuleText_After3 = "Deductions from Rent";
	
	
	public static String lateFeeRule_whicheverIsGreater= "whichever is greater";
	public static String lateFeeRule_tenantShallBeAssessed = "Tenant shall be assessed a late fee in the amount";
	public static String lateFeeRule_mayNotExceedMoreThan30Days = "may not exceed more than 30 days";
	public static String lateFeeRule_mayNotExceedAmount = "(initial and additional) may not exceed";
	public static String lateFeeRule_landlordTheLiquidatedSumOf = "landlord the liquidated sum of";
	public static String lateFeeRule_mayNotExceed375 = "Additional late charges may not exceed $375";
	public static String lateFeeRule_totalDelinquentRentDueToTheTenantAccount = "total delinquent rent due to the Tenant Account";
	public static String lateFeeRule_whicheverIsGreater_dueDay_Prior = "Page 3 of 13:";
	public static String lateFeeRule_whicheverIsGreater_dueDay_After = " day of the month in which it is due";
	public static String lateFeeRule_whicheverIsGreater_dueDay_prior2 = "11:59 p.m. on the";
	public static String lateFeeRule_whicheverIsGreater_lateFeePercentage = "Tenant will pay Landlord for each late payment a late fee of";
	public static String lateFeeRule_whicheverIsGreater_lateFeeAmount = "rent or $";
	public static String lateFeeRule_whicheverIsGreater_lateFeeAmount2 ="late fee in the amount of $";
	
	
	public static String AB_lateFee_Prior = " initial late charge equal to";
	public static String AB_lateFee_After = " of one month�s rent; and; ";
	
	public static String AB_additionalLateChargesPerDay_Prior = "charges of $";
	public static String AB_additionalLateChargesPerDay_After = " per day thereafter until rent \r\n"
			+ "and late charges are paid in full. ";
	
	public static String AB_additionalLateChargesLimit_Prior = "payment may not exceed more than";
	public static String AB_additionalLateChargesLimit_After = "B. For the purposes of paying rent and late charges, the mailbox is not the agent for receipt for Landlord and the";

	public static boolean lateFeeRule(String text)
	{
		String lateFeeRuleText ="";
		try
		{
		 lateFeeRuleText = text.substring(text.indexOf(lateFeeRuleText_Prior.toLowerCase())+lateFeeRuleText_Prior.toLowerCase().length(),text.indexOf(lateFeeRuleText_After.toLowerCase()));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(lateFeeRuleText_Prior.toLowerCase())+lateFeeRuleText_Prior.toLowerCase().length(),text.indexOf(lateFeeRuleText_After2.toLowerCase()));
			}
			catch(Exception e2)
			{
				try
				{
				lateFeeRuleText = text.substring(text.indexOf(lateFeeRuleText_Prior3.toLowerCase())+lateFeeRuleText_Prior3.toLowerCase().length(),text.indexOf(lateFeeRuleText_After3.toLowerCase()));
				}
				catch(Exception e3)
				{
				return false;
				}
			}
		}
		if(lateFeeRuleText.contains(lateFeeRule_whicheverIsGreater.toLowerCase()))
		{
			PDFReader.setLateFeeRuleType("GreaterOfFlatFeeOrPercentage");
			//RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
			
		//Late charge day
			try
			{
		   // PDFReader.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(.lateFeeRule_whicheverIsGreater_dueDay_Prior)+.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				PDFReader.setLateChargeDay(lateFeeRuleText.split(lateFeeRule_whicheverIsGreater_dueDay_After.toLowerCase())[0].trim());
				PDFReader.setLateChargeDay(PDFReader.getLateChargeDay().substring(PDFReader.getLateChargeDay().lastIndexOf(" ")+1));
				PDFReader.setLateChargeDay(PDFReader.getLateChargeDay().replaceAll("[^0-9]", ""));
			}
			catch(Exception e)
			{
				PDFReader.setLateChargeDay("Error");
			}
			System.out.println("Late Charge Day = "+PDFReader.getLateChargeDay());
			RunnerClass.setDueDay_GreaterOf(PDFReader.getLateChargeDay());
		//Late Fee Percentage
			try
			{
		    PDFReader.setLateFeePercentage(lateFeeRuleText.substring(lateFeeRuleText.indexOf(lateFeeRule_whicheverIsGreater_lateFeePercentage.toLowerCase())+lateFeeRule_whicheverIsGreater_lateFeePercentage.toLowerCase().length()).trim().split(" ")[0]);
		    PDFReader.setLateFeePercentage( PDFReader.getLateFeePercentage().replaceAll("[^0-9]", ""));
			}
			catch(Exception e)
			{
				 PDFReader.setLateFeePercentage("Error");
			}
         System.out.println("Late Fee Percentage = "+ PDFReader.getLateFeePercentage());
         RunnerClass.setPercentage(PDFReader.getLateFeePercentage());
         //Late Fee Amount
         String flatFeeAmount ="";
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(lateFeeRule_whicheverIsGreater_lateFeeAmount.toLowerCase())+lateFeeRule_whicheverIsGreater_lateFeeAmount.toLowerCase().length()).trim().split(" ")[0];
         flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+flatFeeAmount);
        RunnerClass.setFlatFee(flatFeeAmount);
         return true;
		}
		else 
		if(lateFeeRuleText.contains(lateFeeRule_mayNotExceedMoreThan30Days.toLowerCase()))
		{
			PDFReader.setLateFeeRuleType("initialFeePluPerDayFee");
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			PDFReader.setLateFeeType("initialFeePluPerDayFee"); 
	         try
	 	    {
	 		    PDFReader.setLateChargeFee(text.substring(text.indexOf(AB_lateFee_Prior.toLowerCase())+AB_lateFee_Prior.toLowerCase().length()).trim().split(" ")[0]);
	 		    //PDFReader.lateChargeFee =  PDFReader.lateChargeFee.substring(0,PDFReader.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	PDFReader.setLateChargeFee("Error");	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+PDFReader.getLateChargeFee().trim());
	 	    RunnerClass.setInitialFeeAmount(PDFReader.getLateChargeFee());
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	PDFReader.setLateFeeChargePerDay(text.substring(text.indexOf(AB_additionalLateChargesPerDay_Prior.toLowerCase())+AB_additionalLateChargesPerDay_Prior.toLowerCase().length()).split(" ")[0].trim());//,text.indexOf(.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	PDFReader.setLateFeeChargePerDay("Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+PDFReader.getLateFeeChargePerDay().trim());
	 	    RunnerClass.setPerDayFeeAmount(PDFReader.getLateFeeChargePerDay());
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	PDFReader.setAdditionalLateChargesLimit(text.substring(text.indexOf(AB_additionalLateChargesLimit_Prior.toLowerCase())+AB_additionalLateChargesLimit_Prior.toLowerCase().length()).trim().split(" ")[0]); //,text.indexOf(.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	PDFReader.setAdditionalLateChargesLimit("Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+PDFReader.getAdditionalLateChargesLimit().trim());
	 	    RunnerClass.setAdditionalLateChargesLimit(PDFReader.getAdditionalLateChargesLimit());
	 	 //Late Charge Day
			try
	 	    {
				PDFReader.setLateChargeDay( lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0]);
				PDFReader.setLateChargeDay(PDFReader.getLateChargeDay().replaceAll("[^0-9]", ""));
	 	    }
			catch(Exception e)
	 	    {
				PDFReader.setLateChargeDay("Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+PDFReader.getLateChargeDay().trim());
	 	    RunnerClass.setDueDay_initialFee(PDFReader.getLateChargeDay());
	 	   return true;
		}
		else if(lateFeeRuleText.contains(lateFeeRule_mayNotExceedAmount.toLowerCase())||lateFeeRuleText.contains(lateFeeRule_mayNotExceed375.toLowerCase()))
			{
			PDFReader.setLateFeeRuleType("initialFeePluPerDayFee");
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
				PDFReader.setLateChargeDay( lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0]);
				PDFReader.setLateChargeDay(PDFReader.getLateChargeDay().replaceAll("[^0-9]", ""));
	 	    }
			catch(Exception e)
	 	    {
				PDFReader.setLateChargeDay("Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+PDFReader.getLateChargeDay().trim());
	 	    RunnerClass.setDueDay_initialFee(PDFReader.getLateChargeDay());
	 	    // initial Late Charge
	 	   try
	 	    {
	 		  PDFReader.setLateChargeFee(lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0]);
	 		 PDFReader.setLateChargeFee(PDFReader.getLateChargeFee().replaceAll("[^0-9.]", "").substring(0, PDFReader.getLateChargeFee().length()-1));
	 	    }
			catch(Exception e)
	 	    {
				PDFReader.setLateChargeFee("Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+PDFReader.getLateChargeFee().trim());
	 	   RunnerClass.setInitialFeeAmount(PDFReader.getLateChargeFee());
	 	    // Additional Late Charges
	 	   try
	 	    {
			PDFReader.setAdditionalLateCharges(lateFeeRuleText.substring(lateFeeRuleText.indexOf("additional late charge of $")+"additional late charge of $".length()).trim().split(" ")[0]);
			System.out.println(PDFReader.getAdditionalLateCharges().trim());
			if(PDFReader.getAdditionalLateCharges().trim().equals("not"))
			{
				PDFReader.setAdditionalLateCharges(lateFeeRuleText.substring(lateFeeRuleText.indexOf("additional late charges of $")+"additional late charges of $".length()).trim().split(" ")[0]);
			}
			PDFReader.setAdditionalLateCharges(PDFReader.getAdditionalLateCharges().replaceAll("[^0-9.]", ""));
	 	    }
			catch(Exception e)
	 	    {
				PDFReader.setAdditionalLateCharges("Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+PDFReader.getAdditionalLateCharges().trim());
	 	    RunnerClass.setPerDayFeeAmount(PDFReader.getAdditionalLateCharges());
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
	 		  PDFReader.setAdditionalLateChargesLimit(lateFeeRuleText.substring(lateFeeRuleText.indexOf("may not exceed $")+"may not exceed $".length()).trim().split(" ")[0]);
	 		 PDFReader.setAdditionalLateChargesLimit(PDFReader.getAdditionalLateChargesLimit().replaceAll("[^0-9.]", ""));
	 	    }
			catch(Exception e)
	 	    {
				PDFReader.setAdditionalLateChargesLimit("Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+PDFReader.getAdditionalLateChargesLimit().trim());
	 	    RunnerClass.setAdditionalLateChargesLimit(PDFReader.getAdditionalLateChargesLimit());
			return true;
			}
		else 
			if(lateFeeRuleText.contains(lateFeeRule_totalDelinquentRentDueToTheTenantAccount.toLowerCase()))
			{
				PDFReader.setLateFeeRuleType("GreaterOfFlatFeeOrPercentage");
				//RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
				
			//Late Charge Day
			try
	 	    {
				PDFReader.setLateChargeDay(lateFeeRuleText.substring(lateFeeRuleText.indexOf("place of payment on the ")+"place of payment on the ".length()).trim().split(" ")[0]);
				PDFReader.setLateChargeDay(PDFReader.getLateChargeDay().replaceAll("[^0-9]", ""));
	 	    }
			catch(Exception e)
	 	    {
				PDFReader.setLateChargeDay("Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+PDFReader.getLateChargeDay().trim());
	 	   RunnerClass.setDueDay_GreaterOf(PDFReader.getLateChargeDay());
	 	    // initial Late Charge
	 	   try
	 	    {
	 		  PDFReader.setLateChargeFee(lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0]);
			//PDFReader.lateChargeFee = PDFReader.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
				PDFReader.setLateChargeFee("Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+PDFReader.getLateChargeFee().trim());
	 	   RunnerClass.setPercentage(PDFReader.getLateChargeFee());
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
			else if(lateFeeRuleText.contains(lateFeeRule_landlordTheLiquidatedSumOf.toLowerCase()))
			{
				PDFReader.setLateFeeRuleType("initialFeePluPerDayFee");
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
				PDFReader.setLateChargeDay(lateFeeRuleText.substring(lateFeeRuleText.indexOf("tenant is not received by landlord within ")+"tenant is not received by landlord within ".length()).trim().split(" ")[0]);
				PDFReader.setLateChargeDay(PDFReader.getLateChargeDay().replaceAll("[^0-9]", ""));
	 	    }
			catch(Exception e)
	 	    {
				PDFReader.setLateChargeDay( "Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+PDFReader.getLateChargeDay().trim());
	 	    RunnerClass.setDueDay_initialFee(PDFReader.getLateChargeDay());
	 	    // initial Late Charge
	 	   try
	 	    {
	 		  PDFReader.setLateChargeFee(lateFeeRuleText.substring(lateFeeRuleText.indexOf("landlord the liquidated sum of $")+"landlord the liquidated sum of $".length()).trim().split(" ")[0]);
	 		 PDFReader.setLateChargeFee(PDFReader.getLateChargeFee().replaceAll("[^0-9.]", "").substring(0, PDFReader.getLateChargeFee().length()-1));
	 	    }
			catch(Exception e)
	 	    {
				PDFReader.setLateChargeFee("Error");	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+PDFReader.getLateChargeFee().trim());
	 	    RunnerClass.setInitialFeeAmount(PDFReader.getLateChargeFee());
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
			else 
				if(lateFeeRuleText.contains(PDFAppConfig.Ohio_Format1.lateFeeRule_TenantshallBeAssessedALateFeeInTheAmount))
				{
					PDFReader.setLateFeeRuleType( "GreaterOfFlatFeeOrPercentage");
					
					
				//Late Charge Day
				try
		 	    {
				PDFReader.setLateChargeDay(lateFeeRuleText.substring(lateFeeRuleText.indexOf("11:59 p.m. on the ")+"11:59 p.m. on the ".length()).trim().split(" ")[0]);
				PDFReader.setLateChargeDay( PDFReader.getLateChargeDay().replaceAll("[^0-9]", ""));
		 	    }
				catch(Exception e)
		 	    {
		 	    	PDFReader.setLateChargeDay("Error");	
		 	    	e.printStackTrace();
		 	    }
		 	    System.out.println("Late Charge Due Day = "+PDFReader.getLateChargeDay().trim());
		 	   RunnerClass.setDueDay_GreaterOf(PDFReader.getLateChargeDay());
		 	    // initial Late Charge
		 	   try
		 	    {
				PDFReader.setLateChargeFee(lateFeeRuleText.substring(lateFeeRuleText.indexOf("assessed a late fee in the amount of $")+"assessed a late fee in the amount of $".length()).trim().split(" ")[0]);
				//PDFReader.lateChargeFee = PDFReader.lateChargeFee.replaceAll("[^0-9.]", "");
		 	    }
				catch(Exception e)
		 	    {
		 	    	PDFReader.setLateChargeFee( "Error");	
		 	    	e.printStackTrace();
		 	    }
		 	    System.out.println("Late Charge Fee = "+PDFReader.getLateChargeFee().trim());
		 	   RunnerClass.setFlatFee(PDFReader.getLateChargeFee());
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
				else if(lateFeeRuleText.contains(PDFAppConfig.Ohio_Format1.lateFeeRule_designatedPlaceOfPayment)&&lateFeeRuleText.contains(PDFAppConfig.Ohio_Format1.AB_lateFee_Prior))
				{
					PDFReader.setLateFeeRuleType("initialFeePluPerDayFee");
					//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
					
					PDFReader.setLateFeeType("initialFeePluPerDayFee"); 
			         try
			 	    {
			 		    PDFReader.setLateChargeFee(text.substring(text.indexOf(PDFAppConfig.Ohio_Format1.AB_lateFee_Prior)+PDFAppConfig.Ohio_Format1.AB_lateFee_Prior.length()).trim().split(" ")[0]);
			 		    //PDFReader.lateChargeFee =  PDFReader.lateChargeFee.substring(0,PDFReader.lateChargeFee.length()-1);
			 	    }
			 	    catch(Exception e)
			 	    {
			 		    PDFReader.setLateChargeFee("Error");	
			 		    e.printStackTrace();
			 	    }
			 	    System.out.println("Late Charge Fee = "+PDFReader.getLateChargeFee().trim());
			 	   RunnerClass.setInitialFeeAmount(PDFReader.getLateChargeFee());
			 	   /*
			 	    //Per Day Fee
			 	    try
			 	    {
			 	    	PDFReader.lateFeeChargePerDay = text.substring(text.indexOf(PDFAppConfig.Ohio_Format1.AB_additionalLateChargesPerDay_Prior)+PDFAppConfig.Ohio_Format1.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(PDFAppConfig.Ohio_Format1.AB_additionalLateChargesPerDay_After));
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
			 	    	PDFReader.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.Ohio_Format1.AB_additionalLateChargesLimit_Prior)+PDFAppConfig.Ohio_Format1.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(PDFAppConfig.Ohio_Format1.AB_additionalLateChargesLimit_After));
			 	    }
			 	    catch(Exception e)
			 	    {
			 	    	PDFReader.additionalLateChargesLimit =  "Error";	
			 	    	e.printStackTrace();
			 	    }
			 	    System.out.println("additional Late Charges Limit = "+PDFReader.additionalLateChargesLimit.trim());
			 	    RunnerClass.additionalLateChargesLimit = PDFReader.additionalLateChargesLimit;
			 	    */
			 	 //Late Charge Day
					try
			 	    {
					PDFReader.setLateChargeDay (lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0]);
					PDFReader.setLateChargeDay(PDFReader.getLateChargeDay().replaceAll("[^0-9]", ""));
			 	    }
					catch(Exception e)
			 	    {
			 	    	PDFReader.setLateChargeDay("Error");	
			 	    	e.printStackTrace();
			 	    }
			 	    System.out.println("Late Charge Due Day = "+PDFReader.getLateChargeDay().trim());
			 	    RunnerClass.setDueDay_initialFee(PDFReader.getLateChargeDay());
			 	   return true;
				} 
				else
				if(lateFeeRuleText.contains(PDFAppConfig.Ohio_Format1.lateFeeRule_designatedPlaceOfPayment)&&lateFeeRuleText.contains("a late charge equal to"))
				{
					PDFReader.setLateFeeRuleType("GreaterOfFlatFeeOrPercentage");
					PDFReader.setLateFeeType("GreaterOfFlatFeeOrPercentage");
					
				//Late Charge Day
				try
		 	    {
				PDFReader.setLateChargeDay(lateFeeRuleText.substring(lateFeeRuleText.indexOf("11:59 p.m. on the ")+"11:59 p.m. on the ".length()).trim().split(" ")[0]);
				PDFReader.setLateChargeDay(PDFReader.getLateChargeDay().replaceAll("[^0-9]", ""));
		 	    }
				catch(Exception e)
		 	    {
		 	    	PDFReader.setLateChargeDay("Error");	
		 	    	e.printStackTrace();
		 	    }
		 	    System.out.println("Late Charge Due Day = "+PDFReader.getLateChargeDay().trim());
		 	   RunnerClass.setDueDay_GreaterOf(PDFReader.getLateChargeDay());
		 	    // initial Late Charge
		 	   try
		 	    {
				PDFReader.setLateChargeFee(lateFeeRuleText.substring(lateFeeRuleText.indexOf("late charge equal to ")+"late charge equal to ".length()).trim().split(" ")[0]);
				//PDFReader.lateChargeFee = PDFReader.lateChargeFee.replaceAll("[^0-9.]", "");
		 	    }
				catch(Exception e)
		 	    {
		 	    	PDFReader.setLateChargeFee("Error");	
		 	    	e.printStackTrace();
		 	    }
		 	    System.out.println("Late Charge Fee = "+PDFReader.getLateChargeFee().trim());
		 	   RunnerClass.setPercentage(PDFReader.getLateChargeFee());
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
				else if(lateFeeRuleText.contains("two delinquent account billing "))
				{
					PDFReader.setLateFeeRuleType("initialFeePluPerDayFee");
					//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
					
					PDFReader.setLateFeeType("initialFeePluPerDayFee"); 
			         try
			 	    {
			 		    PDFReader.setLateChargeFee(text.substring(text.indexOf("late fee of $")+"late fee of $".length()).trim().split(" ")[0].replaceAll("[^0-9]", ""));
			 		    //PDFReader.lateChargeFee =  PDFReader.lateChargeFee.substring(0,PDFReader.lateChargeFee.length()-1);
			 	    }
			 	    catch(Exception e)
			 	    {
			 		    PDFReader.setLateChargeFee("Error");	
			 		    e.printStackTrace();
			 	    }
			 	    System.out.println("Late Charge Fee = "+PDFReader.getLateChargeFee().trim());
			 	   RunnerClass.setInitialFeeAmount(PDFReader.getLateChargeFee());
			 	   /*
			 	    //Per Day Fee
			 	    try
			 	    {
			 	    	PDFReader.lateFeeChargePerDay = text.substring(text.indexOf(PDFAppConfig.Ohio_Format1.AB_additionalLateChargesPerDay_Prior)+PDFAppConfig.Ohio_Format1.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(PDFAppConfig.Ohio_Format1.AB_additionalLateChargesPerDay_After));
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
			 	    	PDFReader.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.Ohio_Format1.AB_additionalLateChargesLimit_Prior)+PDFAppConfig.Ohio_Format1.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(PDFAppConfig.Ohio_Format1.AB_additionalLateChargesLimit_After));
			 	    }
			 	    catch(Exception e)
			 	    {
			 	    	PDFReader.additionalLateChargesLimit =  "Error";	
			 	    	e.printStackTrace();
			 	    }
			 	    System.out.println("additional Late Charges Limit = "+PDFReader.additionalLateChargesLimit.trim());
			 	    RunnerClass.additionalLateChargesLimit = PDFReader.additionalLateChargesLimit;
			 	    */
			 	 //Late Charge Day
					try
			 	    {
					PDFReader.setLateChargeDay (lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0]);
					PDFReader.setLateChargeDay(PDFReader.getLateChargeDay().replaceAll("[^0-9]", ""));
			 	    }
					catch(Exception e)
			 	    {
			 	    	PDFReader.setLateChargeDay("Error");	
			 	    	e.printStackTrace();
			 	    }
			 	    System.out.println("Late Charge Due Day = "+PDFReader.getLateChargeDay().trim());
			 	    RunnerClass.setDueDay_initialFee(PDFReader.getLateChargeDay());
			 	   return true;
				} 
		return true;	
	}
}
