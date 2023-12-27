package mainPackage;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PropertyWare 
{
	public static boolean login()
	{
		try
		{
		RunnerClass.downloadFilePath = AppConfig.downloadFilePath;
		Map<String, Object> prefs = new HashMap<String, Object>();
	    // Use File.separator as it will work on any OS
	    prefs.put("download.default_directory",
	    		RunnerClass.downloadFilePath);
        // Adding cpabilities to ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--remote-allow-origins=*");
		//WebDriverManager.chromedriver().setup();
		WebDriverManager.chromedriver().clearDriverCache().setup();
        RunnerClass.driver= new ChromeDriver(options);
		RunnerClass.driver.manage().window().maximize();
        RunnerClass.driver.get(AppConfig.URL);
        RunnerClass.driver.findElement(Locators.userName).sendKeys(AppConfig.username); 
        RunnerClass.driver.findElement(Locators.password).sendKeys(AppConfig.password);
        RunnerClass.driver.findElement(Locators.signMeIn).click();
        RunnerClass.actions = new Actions(RunnerClass.driver);
        RunnerClass.js = (JavascriptExecutor)RunnerClass.driver;
        RunnerClass.driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(2));
        try
        {
        if(RunnerClass.driver.findElement(Locators.loginError).isDisplayed())
        {
        	System.out.println("Login failed");
		    RunnerClass.failedReason = RunnerClass.failedReason+","+ "Login failed";
			return false;
        }
        }
        catch(Exception e) {}
        RunnerClass.driver.manage().timeouts().implicitlyWait(100,TimeUnit.SECONDS);
        RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(100));
        return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Login failed");
		    RunnerClass.failedReason = RunnerClass.failedReason+","+ "Login failed";
			return false;
		}
	}
	
	public static boolean searchBuilding(String company, String building)
	{
		try
		{
	    //RunnerClass.driver.findElement(Locators.dashboardsTab).click();
		RunnerClass.driver.findElement(Locators.searchbox).clear();
		RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
			try
			{
			RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
			}
			catch(Exception e)
			{
				try
				{
				RunnerClass.driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
				RunnerClass.driver.navigate().refresh();
				RunnerClass.driver.findElement(Locators.dashboardsTab).click();
				RunnerClass.driver.findElement(Locators.searchbox).clear();
				RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
				RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
				}
				catch(Exception e2) {}
			}
			try
			{
			RunnerClass.driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
			if(RunnerClass.driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
			{
				long count = building.chars().filter(ch -> ch == '.').count();
				if(building.chars().filter(ch -> ch == '.').count()>=2)
				{
					building = building.substring(building.indexOf(".")+1,building.length());
					RunnerClass.driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
					RunnerClass.driver.navigate().refresh();
					RunnerClass.driver.findElement(Locators.dashboardsTab).click();
					RunnerClass.driver.findElement(Locators.searchbox).clear();
					RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
					RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
					try
					{
					RunnerClass.driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					if(RunnerClass.driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
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
					try
					{
					building = building.split("_")[1];
					RunnerClass.driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
					RunnerClass.driver.navigate().refresh();
					RunnerClass.driver.findElement(Locators.dashboardsTab).click();
					RunnerClass.driver.findElement(Locators.searchbox).clear();
					RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
					RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
					try
					{
					RunnerClass.driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					if(RunnerClass.driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
					{
						System.out.println("Building Not Found");
					    RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
						return false;
					}
					}
					catch(Exception e3) {}
					}
					catch(Exception e)
					{
				    System.out.println("Building Not Found");
			        RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
				    return false;
					}
				}
			}
			}
			catch(Exception e2)
			{
			}
			RunnerClass.driver.manage().timeouts().implicitlyWait(100,TimeUnit.SECONDS);
			Thread.sleep(1000);
			System.out.println(building);
		// Select Lease from multiple leases
			List<WebElement> displayedCompanies =null;
			try
			{
				displayedCompanies = RunnerClass.driver.findElements(Locators.searchedLeaseCompanyHeadings);
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
						
						List<WebElement> leaseList = RunnerClass.driver.findElements(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li/a"));
						//System.out.println(leaseList.size());
						//Check if displayed leases list has the building name completely first
						for(int j=0;j<leaseList.size();j++)
						{
							String lease = leaseList.get(j).getText();
							if(lease.toLowerCase().contains(RunnerClass.completeBuildingAbbreviation.toLowerCase()))
							{
								
								try
								{
								RunnerClass.portfolioType = RunnerClass.driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).getText().trim().split(":")[0];
								RunnerClass.portfolioName = RunnerClass.portfolioType;
								System.out.println("Portfolio type = "+RunnerClass.portfolioType);
								}
								catch(Exception e) 
								{}
								
								RunnerClass.driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).click();
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
								RunnerClass.portfolioType = RunnerClass.driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).getText().trim().split(":")[0];
								RunnerClass.portfolioName = RunnerClass.portfolioType;
								System.out.println("Portfolio type = "+RunnerClass.portfolioType);
								}
								catch(Exception e) 
								{}
								
								RunnerClass.driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).click();
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
				    RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
					return false;
				}
	         } catch(Exception e) 
		     {
	         RunnerClass.failedReason = RunnerClass.failedReason+","+  "Issue in selecting Building";
		     return false;
		     }
		return true;
	}
	
	public static boolean downloadLeaseAgreement(String building, String ownerName) throws Exception
	{
		//City from Building Address for Arizona rent code
		try
		{
			String buildingAddress = RunnerClass.driver.findElement(Locators.buildingAddress).getText();
			String[] lines = buildingAddress.split("\\n");
			String city = lines[1].split(" ")[0].trim();
			RunnerClass.arizonaCityFromBuildingAddress = city;
			System.out.println("Building Address = "+buildingAddress);
			System.out.println("Building City = "+city);
		}
		catch(Exception e)
		{
			
		}
		
		PropertyWare.intermittentPopUp();
		PDFReader.RCDetails = "";
		RunnerClass.driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(5));
		
		try
		{
			//RunnerClass.portfolioType = RunnerClass.driver.findElement(Locators.checkPortfolioType).getText();
			//System.out.println("Portfolio Type = "+RunnerClass.portfolioType);
		
		int portfolioFlag =0;
		for(int i=0;i<AppConfig.IAGClientList.length;i++)
		{
			if(RunnerClass.portfolioType.startsWith(mainPackage.AppConfig.IAGClientList[i]))
			{
				portfolioFlag =1;
				break;
			}
		}
		
		if(portfolioFlag==1)
			RunnerClass.portfolioType = "MCH";
		else RunnerClass.portfolioType = "Others";
	    System.out.println("Portfolio Type = "+RunnerClass.portfolioType);
		}
	
		catch(Exception e) 
		{
			System.out.println("Unable to fetch Portfolio Type");
			 RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Unable to fetch Portfolio Type";
		   // return false;  -- Commented this as we are not using Portfolio condition anywhere in the process
		}
		
		//RC details
		
		try
		{
		PDFReader.RCDetails = RunnerClass.driver.findElement(Locators.RCDetails).getText();
		}
		catch(Exception e)
		{
			PDFReader.RCDetails = "Error";
		}
		System.out.println("RC Details = "+PDFReader.RCDetails);
		
		try
		{
		RunnerClass.js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
		Thread.sleep(2000);
		if(RunnerClass.driver.findElement(Locators.leasesTab).getText().equals("Leases"))
		RunnerClass.driver.findElement(Locators.leasesTab).click();
		else 
			RunnerClass.driver.findElement(Locators.leasesTab2).click();
		RunnerClass.driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(5));
		try
		{
			RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(By.partialLinkText(ownerName.trim()))).build().perform();
		RunnerClass.driver.findElement(By.partialLinkText(ownerName.trim())).click();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Unable to Click Lease Owner Name");
		    RunnerClass.failedReason =  RunnerClass.failedReason+","+  "Unable to Click Lease Onwer Name";
			return false;
		}
		//Pop up after clicking Lease Name
		PropertyWare.intermittentPopUp();
		
		RunnerClass.driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
        RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(15));
        RunnerClass.js.executeScript("window.scrollBy(0,document.body.scrollHeight)");

        //Start and End Dates in Property Ware
        try
        {
        	RunnerClass.startDateInPW =RunnerClass.driver.findElement(Locators.leaseStartDate_PW).getText();
			System.out.println("Lease Start Date in PW = "+RunnerClass.startDateInPW);
			RunnerClass.endDateInPW =RunnerClass.driver.findElement(Locators.leaseEndDate_PW).getText();
			System.out.println("Lease End Date in PW = "+RunnerClass.endDateInPW);
        }
        catch(Exception e)
        {}
        
		
		RunnerClass.driver.findElement(Locators.notesAndDocs).click();
		
		List<WebElement> documents = RunnerClass.driver.findElements(Locators.documentsList);
		boolean checkLeaseAgreementAvailable = false;
		 
		for(int i =0;i<documents.size();i++)
		{
			for(int j=0;j<AppConfig.LeaseAgreementFileNames.length;j++)
			{
			 if(documents.get(i).getText().startsWith(AppConfig.LeaseAgreementFileNames[j])&&!documents.get(i).getText().contains("Termination")&&!documents.get(i).getText().contains("_Mod")&&!documents.get(i).getText().contains("_MOD"))//&&documents.get(i).getText().contains(AppConfig.getCompanyCode(RunnerClass.company)))
			 {
			 	documents.get(i).click();
				checkLeaseAgreementAvailable = true;
				PropertyWare.waitUntilFileIsDownloaded();
				break;
			 }
			}
			if(checkLeaseAgreementAvailable == true)
				break;
		}
		
		if(checkLeaseAgreementAvailable==false)
		{
			System.out.println("Unable to download Lease Agreement");
		    RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Unable to download Lease Agreement";
			return false;
		}
		Thread.sleep(15000);
		File file = RunnerClass.getLastModified();
		
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(RunnerClass.driver).withTimeout(Duration.ofSeconds(25)).pollingEvery(Duration.ofMillis(100));
		wait.until( x -> file.exists());
		Thread.sleep(10000);
		return true;
		}
		catch(Exception e)
		{
			System.out.println("Unable to download Lease Agreement");
		    RunnerClass.failedReason =  RunnerClass.failedReason+","+"Unable to download Lease Agreement";
			return false;
		}
	}
	
	public static void waitUntilFileIsDownloaded() throws Exception
	{
		try {
			Thread.sleep(10000);
			if(RunnerClass.getLastModified() !=null) {
				while (true) {
			 	  File  file = RunnerClass.getLastModified();
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
			File file = RunnerClass.getLastModified();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Thread.sleep(10000);
		}
	}
	
	public static boolean selectBuilding(String company,String building)
	{
		RunnerClass.failedReason ="";
		try
		{
			//Get BuildingEntityID from LeaseFact_Dashboard table
			String buildingEntityID = DataBase.getBuildingEntityID();
			if(buildingEntityID.equals("Error"))
			{
				System.out.println("Building Not Found");
			    RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
				return false;
			}
			else
			{
			RunnerClass.driver.manage().timeouts().implicitlyWait(100,TimeUnit.SECONDS);
	        RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(100));
	        RunnerClass.driver.navigate().refresh();
	        PropertyWare.intermittentPopUp();
	        //if(PropertyWare.checkIfBuildingIsDeactivated()==true)
	        	//return false;
	        RunnerClass.driver.findElement(Locators.marketDropdown).click();
	        String marketName = "HomeRiver Group - "+RunnerClass.company;
	        Select marketDropdownList = new Select(RunnerClass.driver.findElement(Locators.marketDropdown));
	        marketDropdownList.selectByVisibleText(marketName);
	        String buildingPageURL = AppConfig.buildingPageURL+buildingEntityID;
	        RunnerClass.driver.navigate().to(buildingPageURL);
	        PropertyWare.intermittentPopUp();
	        try
	        {
	        	RunnerClass.portfolioType = RunnerClass.driver.findElement(Locators.checkPortfolioType).getText().trim().split(":")[0];
	        	RunnerClass.portfolioName = RunnerClass.portfolioType;
				System.out.println("Portfolio type = "+RunnerClass.portfolioType);
	        }
	        catch(Exception e)
	        {
	        	
	        }
	        return true;
			}
	        /*
	        String buildingAddress = RunnerClass.driver.findElement(Locators.buildingTitle).getText();
	        if(buildingAddress.toLowerCase().contains(RunnerClass.address.substring(0,RunnerClass.address.lastIndexOf(" ")).toLowerCase()))
	        return true;
	        else
	        {
	        	System.out.println("Address it not matched");
	        	RunnerClass.failedReason = "Address is not matched";
	        	return false;
	        }*/
		}
		catch(Exception e)
		{
			System.out.println("Building Not Found");
		    RunnerClass.failedReason =  RunnerClass.failedReason+","+ "Building Not Found";
			return false;
		}
		
		
		/*
		RunnerClass.failedReason = "";
		RunnerClass.driver.navigate().refresh();
		boolean checkBuildingIsClicked = false;
		try
		{
	    RunnerClass.driver.findElement(Locators.dashboardsTab).click();
		RunnerClass.driver.findElement(Locators.searchbox).clear();
		RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
			try
			{
			RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
			}
			catch(Exception e)
			{
				try
				{
				RunnerClass.driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
				RunnerClass.driver.navigate().refresh();
				RunnerClass.driver.findElement(Locators.dashboardsTab).click();
				RunnerClass.driver.findElement(Locators.searchbox).clear();
				RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
				RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
				}
				catch(Exception e2) {}
			}
			try
			{
			RunnerClass.driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
			if(RunnerClass.driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
			{
				long count = building.chars().filter(ch -> ch == '.').count();
				if(building.chars().filter(ch -> ch == '.').count()>=2)
				{
					building = building.substring(building.indexOf(".")+1,building.length());
					RunnerClass.driver.manage().timeouts().implicitlyWait(200,TimeUnit.SECONDS);
					RunnerClass.driver.navigate().refresh();
					RunnerClass.driver.findElement(Locators.dashboardsTab).click();
					RunnerClass.driver.findElement(Locators.searchbox).clear();
					RunnerClass.driver.findElement(Locators.searchbox).sendKeys(building);
					RunnerClass.wait.until(ExpectedConditions.invisibilityOf(RunnerClass.driver.findElement(Locators.searchingLoader)));
					try
					{
					RunnerClass.driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					if(RunnerClass.driver.findElement(Locators.noItemsFoundMessagewhenLeaseNotFound).isDisplayed())
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
			RunnerClass.driver.manage().timeouts().implicitlyWait(150,TimeUnit.SECONDS);
			Thread.sleep(1000);
			System.out.println(building);
		// Select Lease from multiple leases
			List<WebElement> displayedCompanies =null;
			try
			{
				displayedCompanies = RunnerClass.driver.findElements(Locators.searchedLeaseCompanyHeadings);
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
		              RunnerClass.driver.findElement(Locators.advancedSearch).click();
		              RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.advancedSearch_buildingsSection)).build().perform();
		              List<WebElement> buildingAddresses =  RunnerClass.driver.findElements(Locators.advancedSearch_buildingAddresses);
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
	
	public static void intermittentPopUp()
	{
		//Pop up after clicking lease name
				try
				{
					RunnerClass.driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
			        RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(3));
			        try
			        {
					if(RunnerClass.driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed())
					{
						RunnerClass.driver.findElement(Locators.popupClose).click();
					}
			        }
			        catch(Exception e) {}
			        try
			        {
					if(RunnerClass.driver.findElement(Locators.scheduledMaintanancePopUp).isDisplayed())
					{
						RunnerClass.driver.findElement(Locators.scheduledMaintanancePopUpOkButton).click();
					}
			        }
			        catch(Exception e) {}
			        try
			        {
			        if(RunnerClass.driver.findElement(Locators.scheduledMaintanancePopUpOkButton).isDisplayed())
			        	RunnerClass.driver.findElement(Locators.scheduledMaintanancePopUpOkButton).click();
			        }
			        catch(Exception e) {}
					RunnerClass.driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
			        RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(5));
				}
				catch(Exception e) {}
				
	}


}
