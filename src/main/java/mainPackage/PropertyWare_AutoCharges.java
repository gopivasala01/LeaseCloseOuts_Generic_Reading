package mainPackage;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class PropertyWare_AutoCharges 
{
	public static boolean addingAutoCharges(WebDriver driver,String buildingAbbreviation,String SNo)
	{
		String failedReason="";
		Actions actions = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor)driver;
	      try
	      {
			DataBase.getAutoCharges(buildingAbbreviation,SNo);
			driver.navigate().refresh();
			//Pop up after clicking Lease Name
			PropertyWare.intermittentPopUp(driver);
			driver.findElement(Locators.summaryTab).click();
			driver.findElement(Locators.summaryEditButton).click();
			Thread.sleep(2000);
			js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			actions.moveToElement(driver.findElement(Locators.newAutoCharge)).build().perform();
			
			
			//List<WebElement> startDates = driver.findElements(Locators.autoCharge_List_startDates);
			//List<WebElement> endDates = driver.findElements(Locators.autoCharge_List_EndDates);
			for(int i=0;i<RunnerClass.autoCharges.length;i++)
			{
				boolean availabilityCheck = false;
				String chargeCode = RunnerClass.autoCharges[i][0];
				String amount = RunnerClass.autoCharges[i][1];
				String startDate = RunnerClass.autoCharges[i][2];
				String endDate = RunnerClass.autoCharges[i][3];
				String description = RunnerClass.autoCharges[i][4];
				
				//If amount is Captive Insurance, Need to add decimal values to the amount as it is not coming along with it
				if(!amount.contains("."))
					amount = amount+".00";
				
				if(amount.trim().equals("Error")||amount.trim().equals("0.00")||amount==null||amount.trim().equals("")||amount.trim().matches(".*[a-zA-Z]+.*"))
				{
					System.out.println(" issue in adding Auto Charge - "+description);
					failedReason = failedReason+","+" Auto Charge - "+description;
					RunnerClass.statusID=1;
					continue;
				}
				try
				{
				List<WebElement> existingAutoCharges = driver.findElements(Locators.autoCharge_List);
				List<WebElement> existingAutoChargeAmounts = driver.findElements(Locators.autoCharge_List_Amounts);
				for(int k=0;k<existingAutoCharges.size();k++)
				{
					existingAutoCharges = driver.findElements(Locators.autoCharge_List);
					existingAutoChargeAmounts = driver.findElements(Locators.autoCharge_List_Amounts);
					//startDates = driver.findElements(Locators.autoCharge_List_startDates);
					//endDates = driver.findElements(Locators.autoCharge_List_EndDates);
					
					String autoChargeCodes = existingAutoCharges.get(k).getText();
					String autoChargeAmount = existingAutoChargeAmounts.get(k).getText();
					//String autoChargeStartDate = startDates.get(k).getText();
					//String autoChargeEndDate = endDates.get(k).getText();
					
					/*try
					{
						autoChargeAmount = autoChargeAmount.substring(1,autoChargeAmount.indexOf("."));
						amount = amount.substring(0,amount.indexOf("."));
					}
					catch(Exception e)
					{}*/
					if(chargeCode.contains(autoChargeCodes.replaceAll(".", ""))&&autoChargeAmount.replaceAll("[^0-9]", "").equals(amount.replaceAll("[^0-9]", ""))||amount.trim().equals("0.00"))//&&(startDate.equals(autoChargeStartDate)||autoChargeEndDate.trim().equals("")))
					{
						availabilityCheck = true;
						System.out.println(description+" already available");
						break;
					}
				}
				}
				catch(Exception e)
				{}
				if(availabilityCheck==false)
				{
					PropertyWare_AutoCharges.addingAnAutoCharge(driver,chargeCode, amount, startDate,endDate, description);
				}
				
			}
			 if(AppConfig.saveButtonOnAndOff==true)
					actions.moveToElement(driver.findElement(Locators.saveLease)).click(driver.findElement(Locators.saveLease)).build().perform();
				  else
					actions.moveToElement(driver.findElement(Locators.cancelLease)).click(driver.findElement(Locators.cancelLease)).build().perform();
	  Thread.sleep(2000);
			return true;
	      }
	      catch(Exception e)
	      {
	    	  e.printStackTrace();
	    	  failedReason = failedReason+","+"Something went wrong in adding auto charges";
			  System.out.println("Something went wrong in adding auto charges");
			  driver.navigate().refresh();
			  return true;
	      }
			
		}
		
		
		
		public static boolean addingAnAutoCharge(WebDriver driver,String accountCode, String amount, String startDate,String endDate,String description) throws Exception
		{
			String failedReason="";
			Actions actions = new Actions(driver);
			try
			{
			driver.findElement(Locators.newAutoCharge).click();
			 
		    //Charge Code
			Select autoChargesDropdown = new Select(driver.findElement(Locators.accountDropdown));
			autoChargesDropdown.selectByVisibleText(accountCode); //
						
			//Start Date
			driver.findElement(Locators.autoCharge_StartDate).clear();
			Thread.sleep(500);
			driver.findElement(Locators.autoCharge_StartDate).sendKeys(startDate);
						
			//click this to hide calendar UI
			driver.findElement(Locators.autoCharge_refField).click();
			//Amount
			driver.findElement(Locators.autoCharge_Amount).click();
			actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
			driver.findElement(Locators.autoCharge_Amount).sendKeys(amount);
			Thread.sleep(500);
						
			//End Date
			driver.findElement(Locators.autoCharge_EndDate).clear();
			Thread.sleep(500);
			driver.findElement(Locators.autoCharge_EndDate).sendKeys(endDate);
			
			//Description
			driver.findElement(Locators.autoCharge_Description).sendKeys(description);
			
			//Save or Cancel
			if(AppConfig.saveButtonOnAndOff==false)
			driver.findElement(Locators.autoCharge_CancelButton).click();
			else 
			driver.findElement(Locators.autoCharge_SaveButton).click();
			Thread.sleep(2000);
			}
			catch(Exception e)
			{
				try
				{
				e.printStackTrace();
				RunnerClass.statusID=1;
				System.out.println("Issue in adding Move in Charge"+description);
				failedReason =  failedReason+","+"Issue in adding Auto Charge - "+description;
				driver.findElement(Locators.autoCharge_CancelButton).click();
				return false;	
				}
				catch(Exception e2)
				{
					driver.navigate().refresh();
				}
			}
			return true;
		}
		

}
