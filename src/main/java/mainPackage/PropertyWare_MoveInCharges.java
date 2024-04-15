package mainPackage;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PropertyWare_MoveInCharges 
{
	public static boolean addMoveInCharges(WebDriver driver,String company,String buildingAbbreviation,String SNo)
	{
		String failedReason="";
		Actions actions = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		try
		{
		 //Get All Auto Charges from Table
	    DataBase.getMoveInCharges(buildingAbbreviation,SNo);
		
		driver.navigate().refresh();
		//Pop up after clicking Lease Name
		PropertyWare.intermittentPopUp(driver);
		if(!company.equals("Arizona")) //Screen is already in Ledger for Arizona while getting Monthly rent in "PropertyWare_UpdateValues" class
		{
			js.executeScript("window.scrollBy(document.body.scrollHeight,0)");
		driver.findElement(Locators.ledgerTab).click();
		}
		Thread.sleep(2000);
		actions.sendKeys(Keys.ESCAPE).build().perform();
		List<WebElement> existingMoveInCharges_ChargeCodes = driver.findElements(Locators.moveInCharges_List);
		List<WebElement> existingMoveInCharges_Amount = driver.findElements(Locators.moveInCharge_List_Amount);
		
		for(int i=0;i<RunnerClass.getMoveInCharges().length;i++)
		{
			existingMoveInCharges_ChargeCodes = driver.findElements(Locators.moveInCharges_List);
			existingMoveInCharges_Amount = driver.findElements(Locators.moveInCharge_List_Amount);
				boolean availabilityCheck = false;
				String chargeCode = RunnerClass.getMoveInCharges()[i][0];
				String amount =RunnerClass.getMoveInCharges()[i][1];
				String startDate = RunnerClass.getMoveInCharges()[i][2];
				String endDate = RunnerClass.getMoveInCharges()[i][3];
				String description = RunnerClass.getMoveInCharges()[i][4];
				
				if(amount.equalsIgnoreCase("Error")||amount.equals("0.00")||amount==null||amount.trim().equals("")||amount.trim().matches(".*[a-zA-Z]+.*"))
				{
					System.out.println("Issue in Adding Move in charge - "+description);
					failedReason =  failedReason+","+"Move in charge - "+description;
					RunnerClass.setFailedReason(failedReason);
					System.out.println(description+ " is not updated");
					RunnerClass.setStatusID(1);
					continue;
				}
				try
				{
					
				for(int k=0;k<existingMoveInCharges_ChargeCodes.size();k++)
				{
					String autoChargeCodes = existingMoveInCharges_ChargeCodes.get(k).getText();
					String autoChargeAmount = existingMoveInCharges_Amount.get(k).getText();
					//Check if Prepayments Charge is already available even with different amount
					if(!autoChargeAmount.equals(""))
					{
					if(autoChargeCodes.equals(AppConfig.getPrepaymentChargeCode(company))&&chargeCode.contains(autoChargeCodes))
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
						*/
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
				
				//Add new Charge if it is not there
				if(availabilityCheck==false)
				{
						PropertyWare_MoveInCharges.addingMoveInCharge(driver,chargeCode, amount, startDate, endDate, description);
				}
				
		        }
		        catch(Exception e)
		        {
		        	RunnerClass.setStatusID(1);
					e.printStackTrace();
					System.out.println("Issue in Adding Move in charges");
					failedReason =  failedReason+","+"Issue in Adding Move in charges";
					RunnerClass.setFailedReason(failedReason);
					continue;
		        }
				 
		}
		return true;
		}
		catch(Exception e)
		{
			RunnerClass.setStatusID(1);
			e.printStackTrace();
			System.out.println("Issue in Adding Move in charges");
			failedReason = failedReason+","+" Issue in Adding Move in charges";
			RunnerClass.setFailedReason(failedReason);
			driver.navigate().refresh();
			js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			driver.findElement(Locators.summaryTab).click();
			return true;
		}
		
	}

	public static boolean addingMoveInCharge(WebDriver driver,String accountCode, String amount, String startDate,String endDate,String description) throws Exception
	{
		String failedReason="";
		Actions actions = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		try
		{
		driver.findElement(Locators.newCharge).click();
		Thread.sleep(2000);
		//Account code
		Select AutoChargesDropdown = new Select(driver.findElement(Locators.accountDropdown));
		try
		{
		AutoChargesDropdown.selectByVisibleText(accountCode);
		}
		catch(Exception e)
		{
			System.out.println("Issue - "+description);
			driver.findElement(Locators.moveInChargeCancel).click();
			return false;
		}
		//Reference
		Thread.sleep(2000);
		driver.findElement(Locators.referenceName).sendKeys(description);
		Thread.sleep(2000);
		//Amount
		driver.findElement(Locators.moveInChargeAmount).click();
		actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
		Thread.sleep(2000);
		driver.findElement(Locators.moveInChargeAmount).sendKeys(amount); 
		Thread.sleep(2000);
		//Start Date
		driver.findElement(Locators.moveInChargeDate).clear();
		Thread.sleep(2000);
		driver.findElement(Locators.moveInChargeDate).sendKeys(startDate);
		//Save or Cancel button
		Thread.sleep(2000);
		if(AppConfig.saveButtonOnAndOff==false)
		driver.findElement(Locators.moveInChargeCancel).click();
		else 
		driver.findElement(Locators.moveInChargeSaveButton).click();
		Thread.sleep(3000);
		 driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		try
		{
			if(driver.findElement(Locators.somethingWrongInSavingCharge).isDisplayed())
			{
				driver.findElement(Locators.moveInChargeCancel).click();
			}
			
		}
		catch(Exception e)
		{}
		    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        driver.navigate().refresh();
	      //Pop up after clicking Lease Name
			PropertyWare.intermittentPopUp(driver);
		return true;
		}
		catch(Exception e)
		{
			RunnerClass.setStatusID(1);
			driver.navigate().refresh();
			js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			driver.findElement(Locators.summaryTab).click();
			e.printStackTrace();
			System.out.println("Issue in adding Move in Charge"+description);
			failedReason =  failedReason+","+"Move in Charge - "+description+"In pop up";
			RunnerClass.setFailedReason(failedReason);
			return false;	
		}
	}
	
}
