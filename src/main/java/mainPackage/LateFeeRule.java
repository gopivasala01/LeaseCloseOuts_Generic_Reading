package mainPackage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class LateFeeRule 
{
	public static void lateFeeRule(String lateFeeRuleType,WebDriver driver) throws Exception
	{
		switch(lateFeeRuleType)
		{
		case "GreaterOfFlatFeeOrPercentage":
			LateFeeRule.greaterOfFlatFeeOrPercentage(driver);
			break;
		case "initialFeePluPerDayFee":
			LateFeeRule.initialFeePluPerDayFee(driver);
			break;
			
		}
	}
	
	public static boolean greaterOfFlatFeeOrPercentage(WebDriver driver) throws Exception
	{
		String failedReason="";
		Actions actions = new Actions(driver);
		try
		{
	    actions.moveToElement(driver.findElement(Locators.lateFeeType)).build().perform();
		Select feeType = new Select(driver.findElement(Locators.lateFeeType));
		feeType.selectByVisibleText("Greater of Flat Fee or Percentage");
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Late Charges - Late Fee Rule";
			//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Fee Rule"+'\n');
			return false;
		}
		Thread.sleep(500);
		try
		{
		actions.moveToElement(driver.findElement(Locators.lateFeeDueDay)).build().perform();
		Select dueDayList = new Select(driver.findElement(Locators.lateFeeDueDay)) ;
		dueDayList.selectByVisibleText(RunnerClass.dueDay_GreaterOf);
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Late Charges - Late Fee Due Day";
			//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Fee Due Day"+'\n');
		}
		Thread.sleep(500);
		try
		{
		actions.moveToElement(driver.findElement(Locators.flatFee)).build().perform();
		driver.findElement(Locators.flatFee).click();
		//driver.findElement(Locators.flatFee).clear();
		//RunnerClass.keyBoardActions();
		driver.findElement(Locators.flatFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		driver.findElement(Locators.flatFee).sendKeys(RunnerClass.flatFee);
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Late Charges - Flat Fee";
			//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Flat Fee"+'\n');
		}
		Thread.sleep(500);
		try
		{
		actions.moveToElement(driver.findElement(Locators.lateFeePercentage)).build().perform();
		driver.findElement(Locators.lateFeePercentage).click();
		//RunnerClass.keyBoardActions();
		driver.findElement(Locators.lateFeePercentage).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
		//actions.sendKeys(Keys.END).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
		driver.findElement(Locators.lateFeePercentage).sendKeys(RunnerClass.percentage);
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Late Charges - Percentage";
			//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Percentage"+'\n');
		}
		return true;
	}
	
	public static boolean initialFeePluPerDayFee(WebDriver driver) throws Exception
	{
		String failedReason="";
		Actions actions = new Actions(driver);
		try
		{
	    actions.moveToElement(driver.findElement(Locators.lateFeeType)).build().perform();
		Select feeType = new Select(driver.findElement(Locators.lateFeeType));
		feeType.selectByVisibleText("Initial Fee + Per Day Fee");
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Late Charges - Late Fee Rule";
			//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Fee Rule"+'\n');
			return false;
		}
		Thread.sleep(500);
		//Late Charges
//		Thread.sleep(2000);
		try
		{
		if(RunnerClass.additionalLateChargesLimit.contains("375"))
		{
			RunnerClass.dueDay_initialFee = "2";
		}
		else RunnerClass.dueDay_initialFee = String.valueOf(RunnerClass.dueDay_initialFee.trim().replaceAll("[^0-9]", ""));
		}
		catch(Exception e) {}
		try
		{
			if(RunnerClass.dueDay_initialFee.equalsIgnoreCase("Error"))
			{
				failedReason = failedReason+",Late Charges - Late Charge Day";
				//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Charge Day"+'\n');
				//temp=1;
			}
			else
			{
			actions.moveToElement(driver.findElement(Locators.lateFeeDueDay)).build().perform();
			Select dueDayList = new Select(driver.findElement(Locators.lateFeeDueDay)) ;
			dueDayList.selectByVisibleText(RunnerClass.dueDay_initialFee.trim());
			}
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Late Charges - Late Charge Day";
			//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Charge Day"+'\n');
			//temp=1;
		}
		// Initial Fee
		Thread.sleep(500);
		try
		{
			if(RunnerClass.initialFeeAmount.equalsIgnoreCase("Error"))
			{
				failedReason = failedReason+",Late Charges - Late Charge Fee";
				//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Charge Fee"+'\n');
				//temp=1;
			}
			else
			{
			actions.moveToElement(driver.findElement(Locators.initialFee)).build().perform();
			driver.findElement(Locators.initialFee).click();
			Thread.sleep(1000);
			driver.findElement(Locators.initialFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			//TN_PropertyWare.clearTextField();
			//actions.click(driver.findElement(Locators.initialFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
			driver.findElement(Locators.initialFee).sendKeys(RunnerClass.initialFeeAmount);
			}
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Late Charges - Late Charge Fee";
			//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Charge Fee"+'\n');
			//temp=1;
		}
		Thread.sleep(500);
		//Initial Fee Dropdown
		
		try
		{
			if(RunnerClass.initialFeeAmount.contains("%"))
			{
			Select initialDropdown = new Select(driver.findElement(Locators.initialFeeDropdown)) ;
			initialDropdown.selectByVisibleText("% of Rent Charges");
			}
			else 
			{
				Select initialDropdown = new Select(driver.findElement(Locators.initialFeeDropdown)) ;
				initialDropdown.selectByVisibleText("Fixed Amount");
			}
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Late Charges - Initial fee Dropdown";
			//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Initial fee Dropdown"+'\n');
			//temp=1;
		}
		
		//Per Day Fee
		Thread.sleep(500);
		try
		{
			if(RunnerClass.perDayFeeAmount.equalsIgnoreCase("Error"))
			{
				failedReason = failedReason+",Late Charges - Late Charge Fee Per Day";
				//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Charge Fee Per Day"+'\n');
				//temp=1;
			}
			else
			{
			actions.moveToElement(driver.findElement(Locators.perDayFee)).build().perform();
			driver.findElement(Locators.perDayFee).click();
			Thread.sleep(1000);
			driver.findElement(Locators.perDayFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			//TN_PropertyWare.clearTextField();
			//actions.click(driver.findElement(Locators.perDayFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
			driver.findElement(Locators.perDayFee).sendKeys(RunnerClass.perDayFeeAmount);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			failedReason = failedReason+",Late Charges - Late Charge Fee Per Day";
			//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Charge Fee Per Day"+'\n');
			//temp=1;
		}
        //Per Day Fee Dropdown
		Thread.sleep(500);
		try
		{
		Select perDayFeeDropdown = new Select(driver.findElement(Locators.perDayFeeDropdown)) ;
		perDayFeeDropdown.selectByVisibleText("Fixed Amount");
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Late Charges - Late Charge Fee Per Day Dropdown";
			//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Charge Fee Per Day Dropdown"+'\n');
			//temp=1;
		}
		if(RunnerClass.PDFFormatType.equalsIgnoreCase("Format1"))
	    {
		
			//Maximum
			Thread.sleep(500);
			try
			{
			Select maximumDropdown = new Select(driver.findElement(Locators.maximumYesNoDropdown)) ;
			if(RunnerClass.additionalLateChargesLimit.equals("Error")||RunnerClass.additionalLateChargesLimit.equals(""))
			maximumDropdown.selectByVisibleText("No");
			else maximumDropdown.selectByVisibleText("Yes");
			}
			catch(Exception e)
			{
				failedReason = failedReason+",Late Charges - Late Fee Maximum dropdown";
				//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Fee Maximum dropdown"+'\n');
				//temp=1;
			}
			// Additional Late charges Limit
			
			String maximumLimitDropdown = "";
			if(RunnerClass.additionalLateChargesLimit.contains("30"))
			{
				RunnerClass.additionalLateChargesLimit = "12";
			    maximumLimitDropdown = "% of Rent Charges";
			}
			else
				maximumLimitDropdown = "Fixed Amount";
			Thread.sleep(500);
			try
			{
				if(RunnerClass.additionalLateChargesLimit.equalsIgnoreCase("Error"))
				{
					failedReason = failedReason+",Late Charges - Late Charge Fee Limit";
					//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Charge Fee Limit"+'\n');
					//temp=1;
				}
				else
				{
					driver.findElement(Locators.maximumDatField).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					driver.findElement(Locators.maximumDatField).clear();
					driver.findElement(Locators.maximumDatField).click();
					Thread.sleep(1000);
					//TN_PropertyWare.clearTextField();
					//actions.click(driver.findElement(Locators.maximumDatField)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					driver.findElement(Locators.maximumDatField).sendKeys(RunnerClass.additionalLateChargesLimit);
				}
			}
			catch(Exception e)
			{
				failedReason = failedReason+",Late Charges - Late Charge Fee Limit";
				//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Late Charge Fee Limit"+'\n');
				//temp=1;
			}
			Thread.sleep(500);
			try
			{
			Select maximumDropdown2 = new Select(driver.findElement(Locators.maximumDropdown2)) ;
			maximumDropdown2.selectByVisibleText(maximumLimitDropdown);
			}
			catch(Exception e)
			{
				failedReason = failedReason+",Late Charges - Maximum Limit Dropdown 2";
				//DataBase.notAutomatedFields(RunnerClass.buildingAbbreviation, "Late Charges - Maximum Limit Dropdown 2"+'\n');
				//temp=1;
			}
	    }
		Thread.sleep(500);
		return true;
	}


}
