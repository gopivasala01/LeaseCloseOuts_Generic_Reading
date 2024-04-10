package mainPackage;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import DataReader.ReadingLeaseAgreements;
import io.github.bonigarcia.wdm.WebDriverManager;

public class PropertyWare 
{
	
	public static boolean searchBuilding(WebDriver driver,String company, String building)
	{
		String failedReason = "";
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		Actions actions = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		try
		{
	    //driver.findElement(Locators.dashboardsTab).click();
		driver.findElement(Locators.searchbox).clear();
		driver.findElement(Locators.searchbox).sendKeys(building);
			try
			{
				wait.until(ExpectedConditions.invisibilityOf(driver.findElement(Locators.searchingLoader)));
			}
			catch(Exception e)
			{
				try
				{
				driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
				driver.navigate().refresh();
				actions.sendKeys(Keys.ESCAPE).build().perform();
				driver.findElement(Locators.dashboardsTab).click();
				driver.findElement(Locators.searchbox).clear();
				driver.findElement(Locators.searchbox).sendKeys(building);
				wait.until(ExpectedConditions.invisibilityOf(driver.findElement(Locators.searchingLoader)));
				}
				catch(Exception e2) {}
			}
			try
			{
			driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
			if(driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
			{
				long count = building.chars().filter(ch -> ch == '.').count();
				if(building.chars().filter(ch -> ch == '.').count()>=2)
				{
					building = building.substring(building.indexOf(".")+1,building.length());
					driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
					driver.navigate().refresh();
					actions.sendKeys(Keys.ESCAPE).build().perform();
					driver.findElement(Locators.dashboardsTab).click();
					driver.findElement(Locators.searchbox).clear();
					driver.findElement(Locators.searchbox).sendKeys(building);
					wait.until(ExpectedConditions.invisibilityOf(driver.findElement(Locators.searchingLoader)));
					try
					{
					driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					if(driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
					{
						System.out.println("Building Not Found");
					    failedReason = failedReason+","+ "Building Not Found";
						return false;
					}
					}
					catch(Exception e3) {}
				}
				else
				{
					try
					{
					building = building.split("_")[1];
					driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
					driver.navigate().refresh();
					actions.sendKeys(Keys.ESCAPE).build().perform();
					driver.findElement(Locators.dashboardsTab).click();
					driver.findElement(Locators.searchbox).clear();
					driver.findElement(Locators.searchbox).sendKeys(building);
					wait.until(ExpectedConditions.invisibilityOf(driver.findElement(Locators.searchingLoader)));
					try
					{
					driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					if(driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
					{
						System.out.println("Building Not Found");
					    failedReason =  failedReason+","+ "Building Not Found";
						return false;
					}
					}
					catch(Exception e3) {}
					}
					catch(Exception e)
					{
				    System.out.println("Building Not Found");
			        failedReason =  failedReason+","+ "Building Not Found";
				    return false;
					}
				}
			}
			}
			catch(Exception e2)
			{
			}
			driver.manage().timeouts().implicitlyWait(100,TimeUnit.SECONDS);
			Thread.sleep(1000);
			System.out.println(building);
		// Select Lease from multiple leases
			List<WebElement> displayedCompanies =null;
			try
			{
				displayedCompanies = driver.findElements(Locators.searchedLeaseCompanyHeadings);
			}
			catch(Exception e)
			{
				
			}
				boolean leaseSelected = false;
				for(int i =0;i<displayedCompanies.size();i++)
				{
					String companyName = displayedCompanies.get(i).getText();
					if(companyName.toLowerCase().contains(company.toLowerCase())&&!companyName.contains("Legacy"))
					{
						
						List<WebElement> leaseList = driver.findElements(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li/a"));
						//System.out.println(leaseList.size());
						//Check if displayed leases list has the building name completely first
						for(int j=0;j<leaseList.size();j++)
						{
							String lease = leaseList.get(j).getText();
							if(lease.toLowerCase().contains(RunnerClass.completeBuildingAbbreviation.toLowerCase()))
							{
								
								try
								{
									RunnerClass.setPortfolioType(driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).getText().trim().split(":")[0]);
								RunnerClass.setPortfolioName(RunnerClass.getPortfolioType());
								System.out.println("Portfolio type = "+RunnerClass.getPortfolioType());
								}
								catch(Exception e) 
								{}
								
								driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).click();
								leaseSelected = true;
								break;
							}
						}
						if(leaseSelected == false)
						{
						for(int j=0;j<leaseList.size();j++)
						{
							String lease = leaseList.get(j).getText();
							if(lease.toLowerCase().contains(building.toLowerCase())&&lease.contains(":"))
							{
								
								try
								{
									RunnerClass.setPortfolioType(driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).getText().trim().split(":")[0]);
								RunnerClass.setPortfolioName(RunnerClass.getPortfolioType());
								System.out.println("Portfolio type = "+RunnerClass.getPortfolioType());
								}
								catch(Exception e) 
								{}
								
								driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).click();
								leaseSelected = true;
								break;
							}
						}
						}
					}
					if(leaseSelected==true)
					{
						/*
						//Decide Portfolio Type
						int portfolioFlag =0;
						for(int k=0;k<mainPackage.AppConfig.IAGClientList.length;k++)
						{
							String portfolioStarting = mainPackage.AppConfig.IAGClientList[k].toLowerCase();
							if(RunnerClass.portfolioType.toLowerCase().startsWith(portfolioStarting))
							{
								portfolioFlag =1;
								break;
								//PDFReader.portfolioType = "MCH";
							}
						}
						
						if(portfolioFlag==1)
							RunnerClass.portfolioType = "MCH";
						else RunnerClass.portfolioType = "Others";
						*/
					     return true;
					}
				}
				if(leaseSelected==false)
				{
				    failedReason =  failedReason+","+ "Building Not Found";
					return false;
				}
	         } catch(Exception e) 
		     {
	         failedReason = failedReason+","+  "Issue in selecting Building";
		     return false;
		     }
		return true;
	}
	
	public static boolean downloadLeaseAgreement(WebDriver driver,String building, String ownerName) throws Exception
	{
		String failedReason = "";
		Actions actions = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		//City from Building Address for Arizona rent code
		try
		{
			String buildingAddress = driver.findElement(Locators.buildingAddress).getText();
			String[] lines = buildingAddress.split("\\n");
			String city = lines[1].split(" ")[0].trim();
			RunnerClass.arizonaCityFromBuildingAddress = city;
			System.out.println("Building Address = "+buildingAddress);
			System.out.println("Building City = "+city);
		}
		catch(Exception e)
		{
			
		}
		
		PropertyWare.intermittentPopUp(driver);
		PDFReader.setRCDetails("");
		driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
       
		
		try
		{
			//RunnerClass.portfolioType = driver.findElement(Locators.checkPortfolioType).getText();
			//System.out.println("Portfolio Type = "+RunnerClass.portfolioType);
		
		int portfolioFlag =0;
		for(int i=0;i<AppConfig.IAGClientList.length;i++)
		{
			if(RunnerClass.getPortfolioType().startsWith(mainPackage.AppConfig.IAGClientList[i]))
			{
				portfolioFlag =1;
				break;
			}
		}
		
		if(portfolioFlag==1)
			RunnerClass.setPortfolioType("MCH");
		else RunnerClass.setPortfolioType("Others");
	    System.out.println("Portfolio Type = "+RunnerClass.getPortfolioType());
		}
	
		catch(Exception e) 
		{
			System.out.println("Unable to fetch Portfolio Type");
			 failedReason =  failedReason+","+ "Unable to fetch Portfolio Type";
		   // return false;  -- Commented this as we are not using Portfolio condition anywhere in the process
		}
		
		//RC details
		
		try
		{
			actions.moveToElement(driver.findElement(Locators.RCDetails)).build().perform();
			PDFReader.setRCDetails(driver.findElement(Locators.RCDetails).getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			PDFReader.setRCDetails("Error");
		}
		System.out.println("RC Details = "+PDFReader.getRCDetails());
		
		try
		{
		js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
		Thread.sleep(2000);
		if(driver.findElement(Locators.leasesTab).getText().equals("Leases"))
		driver.findElement(Locators.leasesTab).click();
		else 
			driver.findElement(Locators.leasesTab2).click();
		driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		try
		{
			actions.moveToElement(driver.findElement(By.partialLinkText(ownerName.trim()))).build().perform();
		driver.findElement(By.partialLinkText(ownerName.trim())).click();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Unable to Click Lease Owner Name");
		    failedReason =  failedReason+","+  "Unable to Click Lease Onwer Name";
			return false;
		}
		//Pop up after clicking Lease Name
		PropertyWare.intermittentPopUp(driver);
		
		driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");

        //Start and End Dates in Property Ware
        try
        {
        	RunnerClass.setStartDateInPW(driver.findElement(Locators.leaseStartDate_PW).getText());
			System.out.println("Lease Start Date in PW = "+RunnerClass.getStartDateInPW());
			RunnerClass.setEndDateInPW(driver.findElement(Locators.leaseEndDate_PW).getText());
			System.out.println("Lease End Date in PW = "+RunnerClass.getEndDateInPW());
        }
        catch(Exception e)
        {}
        
		
		driver.findElement(Locators.notesAndDocs).click();
		int k=0;
		while(k<2)
		{
			try
			{
		List<WebElement> documents = driver.findElements(Locators.documentsList);
		boolean checkLeaseAgreementAvailable = false;
		String filename = null;
		for(int i =0;i<documents.size();i++)
		{
			for(int j=0;j<AppConfig.LeaseAgreementFileNames.length;j++)
			{
			 if(documents.get(i).getText().startsWith(AppConfig.LeaseAgreementFileNames[j])&&!documents.get(i).getText().contains("Termination")&&!documents.get(i).getText().contains("_Mod")&&!documents.get(i).getText().contains("_MOD"))//&&documents.get(i).getText().contains(AppConfig.getCompanyCode(RunnerClass.company)))
			 {
			 	documents.get(i).click();
			 	filename = documents.get(i).getText();
			 	RunnerClass.setFileName(filename);
				checkLeaseAgreementAvailable = true;
				PropertyWare.waitUntilFileIsDownloaded(filename);
				break;
			 }
			}
			if(checkLeaseAgreementAvailable == true)
				break;
		}
		
		if(checkLeaseAgreementAvailable==false)
		{
			System.out.println("Lease Agreement is not available");
		    failedReason =  failedReason+","+ "Lease Agreement is not available";
			return false;
		}
		Thread.sleep(2000);
		
		
		return true;
			}
		catch(Exception e)
			{
			driver.navigate().refresh();
			continue;
			}
		
		}
		}
		catch(Exception e)
		{
			System.out.println("Unable to download Lease Agreement");
		    failedReason =  failedReason+","+"Unable to download Lease Agreement";
			return false;
		}
		return true;
		
	}
	
	public static void waitUntilFileIsDownloaded(String filename) throws Exception
	{
		try {
			Thread.sleep(10000);
			if(RunnerClass.getLastModified(filename) !=null) {
				while (true) {
			 	  File  file = RunnerClass.getLastModified(filename);
			 	    if (file.getName().endsWith(".crdownload")) {
			 	        try {
			 	            Thread.sleep(5000);
			 	        } catch (InterruptedException e1) {
			 	            
			 	        	e1.printStackTrace();
			 	            // Handle the InterruptedException if needed
			 	        }
			 	    } else {
			 	        // Break the loop if the file name does not end with ".crdownload"
			 	        break;
			 	    }
			 	}
			}
			File file = RunnerClass.getLastModified(filename);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Thread.sleep(10000);
		}
	}
	
	public static boolean selectBuilding(WebDriver driver,String company,String ownerName)
	{
		String failedReason = "";
		Actions actions = new Actions(driver);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		try
		{
			//Get BuildingEntityID from LeaseFact_Dashboard table
			String buildingEntityID = DataBase.getBuildingEntityID(company,ownerName);
			if(buildingEntityID.equals("Error"))
			{
				System.out.println("Building Not Found");
				failedReason =  failedReason+","+ "Building Not Found";
				return false;
			}
			else
			{
			driver.manage().timeouts().implicitlyWait(100,TimeUnit.SECONDS);
	        wait = new WebDriverWait(driver, Duration.ofSeconds(100));
	        driver.navigate().refresh();
	        actions.sendKeys(Keys.ESCAPE).build().perform();
	        PropertyWare.intermittentPopUp(driver);
	        //if(PropertyWare.checkIfBuildingIsDeactivated()==true)
	        	//return false;
	        driver.findElement(Locators.marketDropdown).click();
	        String marketName = "HomeRiver Group - "+company;
	        Select marketDropdownList = new Select(driver.findElement(Locators.marketDropdown));
	        marketDropdownList.selectByVisibleText(marketName);
	        String buildingPageURL = AppConfig.buildingPageURL+buildingEntityID;
	        driver.navigate().to(buildingPageURL);
	        PropertyWare.intermittentPopUp(driver);
	        try
	        {
	        	RunnerClass.setPortfolioType(driver.findElement(Locators.checkPortfolioType).getText().trim().split(":")[0]);
	        	RunnerClass.setPortfolioName( RunnerClass.getPortfolioType());
				System.out.println("Portfolio type = "+RunnerClass.getPortfolioType());
	        }
	        catch(Exception e)
	        {
	        	
	        }
	        return true;
			}
		}
		catch(Exception e)
		{
			System.out.println("Building Not Found");
		    failedReason =  failedReason+","+ "Building Not Found";
			return false;
		}
		
		
		/*
		RunnerClass.failedReason = "";
		driver.navigate().refresh();
		boolean checkBuildingIsClicked = false;
		try
		{
	    driver.findElement(Locators.dashboardsTab).click();
		driver.findElement(Locators.searchbox).clear();
		driver.findElement(Locators.searchbox).sendKeys(building);
			try
			{
			RunnerClass.wait.until(ExpectedConditions.invisibilityOf(driver.findElement(Locators.searchingLoader)));
			}
			catch(Exception e)
			{
				try
				{
				driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
				driver.navigate().refresh();
				driver.findElement(Locators.dashboardsTab).click();
				driver.findElement(Locators.searchbox).clear();
				driver.findElement(Locators.searchbox).sendKeys(building);
				RunnerClass.wait.until(ExpectedConditions.invisibilityOf(driver.findElement(Locators.searchingLoader)));
				}
				catch(Exception e2) {}
			}
			try
			{
			driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
			if(driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
			{
				long count = building.chars().filter(ch -> ch == '.').count();
				if(building.chars().filter(ch -> ch == '.').count()>=2)
				{
					building = building.substring(building.indexOf(".")+1,building.length());
					driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
					driver.navigate().refresh();
					driver.findElement(Locators.dashboardsTab).click();
					driver.findElement(Locators.searchbox).clear();
					driver.findElement(Locators.searchbox).sendKeys(building);
					RunnerClass.wait.until(ExpectedConditions.invisibilityOf(driver.findElement(Locators.searchingLoader)));
					try
					{
					driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					if(driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
					{
						System.out.println("Building Not Found");
					    RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
						return false;
					}
					}
					catch(Exception e3) {}
				}
				else
				{
				System.out.println("Building Not Found");
			    RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
				return false;
				}
			}
			}
			catch(Exception e2)
			{
			}
			driver.manage().timeouts().implicitlyWait(150,TimeUnit.SECONDS);
			Thread.sleep(1000);
			System.out.println(building);
		// Select Lease from multiple leases
			List<WebElement> displayedCompanies =null;
			try
			{
				displayedCompanies = driver.findElements(Locators.searchedLeaseCompanyHeadings);
			}
			catch(Exception e)
			{
				
			}
				boolean leaseSelected = false;
				for(int i =0;i<displayedCompanies.size();i++)
				{
					String companyName = displayedCompanies.get(i).getText();
					if(companyName.toLowerCase().contains(company.toLowerCase())&&!companyName.contains("Legacy"))
					{
		              driver.findElement(Locators.advancedSearch).click();
		              RunnerClass.actions.moveToElement(driver.findElement(Locators.advancedSearch_buildingsSection)).build().perform();
		              List<WebElement> buildingAddresses =  driver.findElements(Locators.advancedSearch_buildingAddresses);
		              for(int j=0;j<buildingAddresses.size();j++)
		              {
		            	  String address = buildingAddresses.get(j).getText();
		            	  if(address.toLowerCase().endsWith(building.toLowerCase()))
		            	  {
		            		  buildingAddresses.get(j).click();
		            		  checkBuildingIsClicked = true;
		            		  break;
		            	  }
		              }
		              if(checkBuildingIsClicked==true)
		            	  break;
					}
				}
				if(checkBuildingIsClicked==false)
				{
					System.out.println("Building Not Found");
				    RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
					return false;
				}
		}
		catch(Exception e)
		{
			
		}
		return true;
	*/
	}
	
	public static void intermittentPopUp(WebDriver driver) throws Exception
	{
		Thread.sleep(2000);
		//Pop up after clicking lease name
				try
				{
					driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			       
			        boolean popupCheck = false;
			        try
			        {
					        	driver.switchTo().frame(driver.findElement(Locators.scheduleMaintananceIFrame));
					        	if(driver.findElement(Locators.scheduleMaintanancePopUp2).isDisplayed()) {
					        		driver.findElement(Locators.maintananceCloseButton).click();
					        	}
					        	driver.switchTo().defaultContent();
			        }
			        catch(Exception e)
			        {}
			        try
			        {
					if(driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed())
					{
						driver.findElement(Locators.popupClose).click();
					}
			        }
			        catch(Exception e) {}
			        try
			        {
					if(driver.findElement(Locators.scheduledMaintanancePopUp).isDisplayed())
					{
						driver.findElement(Locators.scheduledMaintanancePopUpOkButton).click();
					}
			        }
			        catch(Exception e) {}
			        try
			        {
			        if(driver.findElement(Locators.scheduledMaintanancePopUpOkButton).isDisplayed())
			        	driver.findElement(Locators.scheduledMaintanancePopUpOkButton).click();
			        }
			        catch(Exception e) {}
					driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
			        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
				}
				catch(Exception e) {}
				
	}


}
