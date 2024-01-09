package mainPackage;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PropertyWare_MoveInCharges 
{
	public static boolean addMoveInCharges()
	{
		try
		{
		 //Get All Auto Charges from Table
	    DataBase.getMoveInCharges();
		
		RunnerClass.driver.navigate().refresh();
		//Pop up after clicking Lease Name
		PropertyWare.intermittentPopUp();
		if(!RunnerClass.company.equals("Arizona")) //Screen is already in Ledger for Arizona while getting Monthly rent in "PropertyWare_UpdateValues" class
		{
		RunnerClass.js.executeScript("window.scrollBy(document.body.scrollHeight,0)");
		RunnerClass.driver.findElement(Locators.ledgerTab).click();
		}
		Thread.sleep(2000);
		RunnerClass.actions.sendKeys(Keys.ESCAPE).build().perform();
		List<WebElement> existingMoveInCharges_ChargeCodes = RunnerClass.driver.findElements(Locators.moveInCharges_List);
		List<WebElement> existingMoveInCharges_Amount = RunnerClass.driver.findElements(Locators.moveInCharge_List_Amount);
		
		for(int i=0;i<RunnerClass.moveInCharges.length;i++)
		{
			existingMoveInCharges_ChargeCodes = RunnerClass.driver.findElements(Locators.moveInCharges_List);
			existingMoveInCharges_Amount = RunnerClass.driver.findElements(Locators.moveInCharge_List_Amount);
				boolean availabilityCheck = false;
				String chargeCode = RunnerClass.moveInCharges[i][0];
				String amount = RunnerClass.moveInCharges[i][1];
				String startDate = RunnerClass.moveInCharges[i][2];
				String endDate = RunnerClass.moveInCharges[i][3];
				String description = RunnerClass.moveInCharges[i][4];
				
				if(amount.equalsIgnoreCase("Error")||amount.equals("0.00")||amount==null||amount.trim().equals("")||amount.trim().matches(".*[a-zA-Z]+.*"))
				{
					System.out.println("Issue in Adding Move in charge - "+description);
					RunnerClass.failedReason =  RunnerClass.failedReason+","+"Move in charge - "+description;
					System.out.println(description+ " is not updated");
					RunnerClass.statusID=1;
					continue;
				}
				try
				{
					/*
				for(int k=0;k<existingMoveInCharges_ChargeCodes.size();k++)
				{
					String autoChargeCodes = existingMoveInCharges_ChargeCodes.get(k).getText();
					String autoChargeAmount = existingMoveInCharges_Amount.get(k).getText();
					//Check if Prepayments Charge is already available even with different amount
					if(!autoChargeAmount.equals(""))
					{
					if(autoChargeCodes.equals(AppConfig.getPrepaymentChargeCode(RunnerClass.company))&&chargeCode.contains(autoChargeCodes))
					{
						availabilityCheck = true;
						System.out.println(description+" already available");
						break;
					}
					/*
					//If Amount doesn't have .00 at the end add it to check if it is already there
					if(!amount.endsWith(".00"))
						amount = amount+".00";
					if(amount.endsWith(".0"))
						amount = amount.replace(".0", "0.00");
						
					if(chargeCode.contains(autoChargeCodes)&&(autoChargeAmount.replaceAll("[^0-9]", "").contains(amount.replaceAll("[^0-9]", ""))))//||autoChargeAmount.split(".")[0].contains(amount.split(".")[0])))
					{
						availabilityCheck = true;
						System.out.println(description+" already available");
						break;
					}
					}
				}
				if(availabilityCheck==true)
					continue;
				*/
				//Add new Charge if it is not there
				if(availabilityCheck==false)
				{
						PropertyWare_MoveInCharges.addingMoveInCharge(chargeCode, amount, startDate, endDate, description);
				}
				
		        }
		        catch(Exception e)
		        {
		        	RunnerClass.statusID=1;
					e.printStackTrace();
					System.out.println("Issue in Adding Move in charges");
					RunnerClass.failedReason =  RunnerClass.failedReason+","+"Issue in Adding Move in charges";
					continue;
		        }
				 
		}
		return true;
		}
		catch(Exception e)
		{
			RunnerClass.statusID=1;
			e.printStackTrace();
			System.out.println("Issue in Adding Move in charges");
			RunnerClass.failedReason =  RunnerClass.failedReason+","+" Issue in Adding Move in charges";
			RunnerClass.driver.navigate().refresh();
			RunnerClass.js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			RunnerClass.driver.findElement(Locators.summaryTab).click();
			return true;
		}
		
	}

	public static boolean addingMoveInCharge(String accountCode, String amount, String startDate,String endDate,String description) throws Exception
	{
		try
		{
		RunnerClass.driver.findElement(Locators.newCharge).click();
		Thread.sleep(2000);
		//Account code
		Select AutoChargesDropdown = new Select(RunnerClass.driver.findElement(Locators.accountDropdown));
		try
		{
		AutoChargesDropdown.selectByVisibleText(accountCode);
		}
		catch(Exception e)
		{
			System.out.println("Issue - "+description);
			RunnerClass.driver.findElement(Locators.moveInChargeCancel).click();
			return false;
		}
		//Reference
		Thread.sleep(2000);
		RunnerClass.driver.findElement(Locators.referenceName).sendKeys(description);
		Thread.sleep(2000);
		//Amount
		RunnerClass.driver.findElement(Locators.moveInChargeAmount).click();
		RunnerClass.actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
		Thread.sleep(2000);
		RunnerClass.driver.findElement(Locators.moveInChargeAmount).sendKeys(amount); 
		Thread.sleep(2000);
		//Start Date
		RunnerClass.driver.findElement(Locators.moveInChargeDate).clear();
		Thread.sleep(2000);
		RunnerClass.driver.findElement(Locators.moveInChargeDate).sendKeys(startDate);
		//Save or Cancel button
		Thread.sleep(2000);
		if(AppConfig.saveButtonOnAndOff==false)
		RunnerClass.driver.findElement(Locators.moveInChargeCancel).click();
		else 
		RunnerClass.driver.findElement(Locators.moveInChargeSaveButton).click();
		Thread.sleep(3000);
		 RunnerClass.driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
	        RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(2));
		try
		{
			if(RunnerClass.driver.findElement(Locators.somethingWrongInSavingCharge).isDisplayed())
			{
				RunnerClass.driver.findElement(Locators.moveInChargeCancel).click();
			}
			
		}
		catch(Exception e)
		{}
		    RunnerClass.driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
	        RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(10));
	        RunnerClass.driver.navigate().refresh();
	      //Pop up after clicking Lease Name
			PropertyWare.intermittentPopUp();
		return true;
		}
		catch(Exception e)
		{
			RunnerClass.statusID=1;
			RunnerClass.driver.navigate().refresh();
			RunnerClass.js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			RunnerClass.driver.findElement(Locators.summaryTab).click();
			e.printStackTrace();
			System.out.println("Issue in adding Move in Charge"+description);
			RunnerClass.failedReason =  RunnerClass.failedReason+","+"Move in Charge - "+description+"In pop up";
			return false;	
		}
	}
	
}
