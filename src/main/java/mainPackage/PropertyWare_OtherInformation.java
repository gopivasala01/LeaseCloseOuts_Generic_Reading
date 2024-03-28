package mainPackage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Joiner;


public class PropertyWare_OtherInformation 
{
	 public  static String type1,type2,type3,weight1,weight2,weight3,breed1,breed2,breed3;
	 
	public static boolean addOtherInformation(WebDriver driver,String company,String buildingAbbreviation) throws Exception
	{
		String failedReason="";
		Actions actions = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		type1=type2=type3=weight1=weight2=weight3=breed1=breed2=breed3 ="";
		driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        RunnerClass.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		driver.navigate().refresh();
		//Pop up after clicking Lease Name
		PropertyWare.intermittentPopUp(driver);
		js.executeScript("window.scrollBy(0,-document.body.scrollHeight)");
		driver.findElement(Locators.summaryEditButton).click();
		
		try
		{
		//Other Fields
        Thread.sleep(2000);
	 
        //Base Rent
        try
        {
        	
        	if(RunnerClass.getMonthlyRent().equalsIgnoreCase("Error"))
			{
				failedReason = failedReason+",Base Rent";
				//DataBase.notAutomatedFields(buildingAbbreviation, "Intial Monthly Rent"+'\n');
				//temp=1;
			}
			else
			{
			actions.moveToElement(driver.findElement(Locators.baseRent)).build().perform();
			//driver.findElement(Locators.initialMonthlyRent).clear();
			driver.findElement(Locators.baseRent).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			driver.findElement(Locators.baseRent).sendKeys(RunnerClass.getMonthlyRent());
			
			}
		}
		catch(Exception e)
		{
			DataBase.notAutomatedFields(buildingAbbreviation, "Base Rent"+'\n');
			failedReason = failedReason+",Base Rent";
			//temp=1;
		}
        
		// RC Field
		try
		{
			if(PDFReader.RCDetails.equalsIgnoreCase("Error"))
			{
				failedReason = failedReason+",RC Details";
				//DataBase.notAutomatedFields(buildingAbbreviation, "RC Details"+'\n');
				//temp=1;
			}
			else
			{
			actions.moveToElement(driver.findElement(Locators.RCDetails)).build().perform();
			driver.findElement(Locators.rcField).clear();
			Thread.sleep(1000);
			driver.findElement(Locators.rcField).sendKeys(PDFReader.RCDetails);
			}
		}
		catch(Exception e)
		{
			try
			{
				actions.moveToElement(driver.findElement(Locators.APMField)).build().perform();
				driver.findElement(Locators.APMField).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.APMField).sendKeys(PDFReader.RCDetails);
			}
			catch(Exception e2)
			{
				try
				{
					actions.moveToElement(driver.findElement(Locators.RC)).build().perform();
					driver.findElement(Locators.RC).clear();
					Thread.sleep(1000);
					driver.findElement(Locators.RC).sendKeys(PDFReader.RCDetails);
				}
				catch(Exception e3)
				{
					failedReason = failedReason+",RC Details";
				//DataBase.notAutomatedFields(buildingAbbreviation, "RC Details"+'\n');
				//temp=1;
				}
			}
		}
		
        //Early Termination
		try
		{
			if(PDFReader.earlyTermination.equalsIgnoreCase("Error"))
			{
				failedReason = failedReason+",Early Termination";
				//DataBase.notAutomatedFields(buildingAbbreviation, "Early Termination"+'\n');
				//temp=1;
			}
			else
			{
			if(PDFReader.earlyTermination.contains("2")||PDFReader.floridaLiquidizedAddendumOption1Check==true)
			{
				if(company.equals("San Antonio"))
				{
					actions.moveToElement(driver.findElement(Locators.earlyTermFee2x_textbox1)).build().perform();
					driver.findElement(Locators.earlyTermFee2x_textbox1).clear();
					driver.findElement(Locators.earlyTermFee2x_textbox1).sendKeys(AppConfig.getEarlyTermination(company));
				}
				else
				{
				actions.moveToElement(driver.findElement(Locators.earlyTermFee2x)).build().perform();
				driver.findElement(Locators.earlyTermFee2x).click();
				Select earlyTermination_List = new Select(driver.findElement(Locators.earlyTermination_List));
				try
				{
				earlyTermination_List.selectByVisibleText(AppConfig.getEarlyTermination(company));
				}
				catch(Exception e)
				{
					try
					{
						earlyTermination_List.selectByVisibleText("YES");
					}
					catch(Exception e2)
					{
						try
						{
							earlyTermination_List.selectByVisibleText("Yes");
						}
						catch(Exception e3)
						{
							failedReason = failedReason+",Early Termination";
							//DataBase.notAutomatedFields(buildingAbbreviation, "Early Termination"+'\n');
							e2.printStackTrace();
							//temp=1;
						}
					}
				}
				}
			}
			else
			{
				failedReason = failedReason+",Early Termination";
				//DataBase.notAutomatedFields(buildingAbbreviation, "Early Termination"+'\n');
				//temp=1;
			}
			}
		}
		catch(Exception e)
		{
			try
			{
				if(PDFReader.earlyTermination.contains("2"))
				{
					if(company.equals("San Antonio"))
					{
						actions.moveToElement(driver.findElement(Locators.earlyTermFee2x_textbox2)).build().perform();
						driver.findElement(Locators.earlyTermFee2x_textbox2).clear();
						driver.findElement(Locators.earlyTermFee2x_textbox2).sendKeys(AppConfig.getEarlyTermination(company));
					}
					else
					{
					actions.moveToElement(driver.findElement(Locators.earlyTermFee2x_2)).build().perform();
					driver.findElement(Locators.earlyTermFee2x_2).click();
					Select earlyTermination_List = new Select(driver.findElement(Locators.earlyTermination_List_2));
					try
					{
					earlyTermination_List.selectByVisibleText(AppConfig.getEarlyTermination(company));
					}
					catch(Exception ee)
					{
						try
						{
							earlyTermination_List.selectByVisibleText("YES");
						}
						catch(Exception e2)
						{
							try
							{
								earlyTermination_List.selectByVisibleText("Yes");
							}
							catch(Exception e3)
							{
								failedReason = failedReason+",Early Termination";
								//DataBase.notAutomatedFields(buildingAbbreviation, "Early Termination"+'\n');
								e2.printStackTrace();
								//temp=1;
							}
						}
					}
					}
				}
				else
				{
					failedReason = failedReason+",Early Termination";
					//DataBase.notAutomatedFields(buildingAbbreviation, "Early Termination"+'\n');
					//temp=1;
				}
			}
			catch(Exception e2)
			{
			failedReason = failedReason+",Early Termination";
			//DataBase.notAutomatedFields(buildingAbbreviation, "Early Termination"+'\n');
			e2.printStackTrace();
			//temp=1;
			}
		}
		
		if(PDFReader.residentBenefitsPackageAvailabilityCheck==true)
		{
			if(PDFReader.residentBenefitsPackage!="Error")
			{
			//Thread.sleep(2000);
			try
			{
			actions.moveToElement(driver.findElement(Locators.residentBenefitsPackage)).build().perform();
			driver.findElement(Locators.residentBenefitsPackage).click();
			Select residentBenefitsPackageList = new Select(driver.findElement(Locators.residentBenefitsPackage));
			//if(OKC_PropertyWare.HVACFilterFlag==false)
			residentBenefitsPackageList.selectByVisibleText("YES");
			//else enrolledInFilterEasyList.selectByVisibleText("NO");
			}
			catch(Exception e)
			{
				failedReason = failedReason+",Resident Benefits Package";
				//DataBase.notAutomatedFields(buildingAbbreviation, "Resident Benefits Package"+'\n');
				//temp=1;
				e.printStackTrace();
			}
			}
		}
		else
		{
			if(company.equals("Chicago"))
			{
			//Enrolled in FilterEasy
			if(PDFReader.airFilterFee!="Error")
			{
			//Thread.sleep(2000);
			try
			{
			actions.moveToElement(driver.findElement(Locators.enrolledInFilterEasy)).build().perform();
			driver.findElement(Locators.enrolledInFilterEasy).click();
			Select enrolledInFilterEasyList = new Select(driver.findElement(Locators.enrolledInFilterEasy_List));
			if(PDFReader.HVACFilterFlag==false||PDFReader.HVACFilterOptOutAddendum==true)
			enrolledInFilterEasyList.selectByVisibleText("YES");
			else enrolledInFilterEasyList.selectByVisibleText("NO");
			}
			catch(Exception e)
			{
				try
				{
				actions.moveToElement(driver.findElement(Locators.enrolledInFilterEasy)).build().perform();
				driver.findElement(Locators.enrolledInFilterEasy).click();
				Select enrolledInFilterEasyList = new Select(driver.findElement(Locators.enrolledInFilterEasy_List));
				if(PDFReader.HVACFilterFlag==false||PDFReader.HVACFilterOptOutAddendum==true)
				enrolledInFilterEasyList.selectByVisibleText("Yes");
				else enrolledInFilterEasyList.selectByVisibleText("No");
				}
				catch(Exception e2)
				{
					failedReason = failedReason+",Enrolled in FilterEasy";
					//DataBase.notAutomatedFields(buildingAbbreviation, "Enrolled in FilterEasy"+'\n');
					//temp=1;
					e.printStackTrace();
				}
			}
		}
		}
		}
		//Client Type
		try
		{
			actions.moveToElement(driver.findElement(Locators.clientType)).build().perform();
			driver.findElement(Locators.clientType).click();
			Select clientType = new Select(driver.findElement(Locators.clientType));
			if(RunnerClass.portfolioType.equals("MCH"))
			clientType.selectByVisibleText("Institutional");
			else clientType.selectByVisibleText("Retail");
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Client Type";
			//DataBase.notAutomatedFields(buildingAbbreviation, "Enrolled in FilterEasy"+'\n');
			//temp=1;
			e.printStackTrace();
		}
		
		//Captive Insurence
		//if(PDFReader.captiveInsurenceATXFlag==true) 
		//{
			try
			{
			actions.moveToElement(driver.findElement(Locators.captiveInsurence)).build().perform();
			driver.findElement(Locators.captiveInsurence).click();
			Select captiveInsurenceList = new Select(driver.findElement(Locators.captiveInsurence));
			try
			{
				if(PDFReader.captiveInsurenceATXFlag==true)
				captiveInsurenceList.selectByVisibleText("Yes");
				else
					captiveInsurenceList.selectByVisibleText("No");
			}
			catch(Exception e)
			{
				try
				{
					if(PDFReader.captiveInsurenceATXFlag==true)
						captiveInsurenceList.selectByVisibleText("YES");
						else
							captiveInsurenceList.selectByVisibleText("NO");
				}
				catch(Exception e2)
				{
					failedReason = failedReason+",Captive Insurence";
					//DataBase.notAutomatedFields(buildingAbbreviation, "Enrolled in FilterEasy"+'\n');
					//temp=1;
					e.printStackTrace();
				}
			}
			}
			catch(Exception e)
			{
				failedReason = failedReason+",Captive Insurence";
				//DataBase.notAutomatedFields(buildingAbbreviation, "Enrolled in FilterEasy"+'\n');
				//temp=1;
				e.printStackTrace();
			}
		//}
		
		//Needs New Lease - No by default
		//Thread.sleep(2000);
		try
		{
		actions.moveToElement(driver.findElement(Locators.needsNewLease)).build().perform();
		driver.findElement(Locators.needsNewLease).click();
		Select needsNewLease_List = new Select(driver.findElement(Locators.needsNewLease_List));
		needsNewLease_List.selectByVisibleText(AppConfig.getNeedsNewLease(company));
		}
		catch(Exception e)
		{
			failedReason = failedReason+",Needs New Lease";
			//DataBase.notAutomatedFields(buildingAbbreviation, "Needs New Lease"+'\n');
			//temp=1;
		}
		//Lease Occupants
		//Thread.sleep(2000);
		try
		{
			if(RunnerClass.getOccupants().equalsIgnoreCase("Error"))
			{
				failedReason = failedReason+",Lease Occupants";
				//DataBase.notAutomatedFields(buildingAbbreviation, "Lease Occupants"+'\n');
				//temp=1;
			}
			else
			{
			actions.moveToElement(driver.findElement(Locators.leaseOccupants)).build().perform();
			driver.findElement(Locators.leaseOccupants).clear();
			Thread.sleep(1000);
			driver.findElement(Locators.leaseOccupants).sendKeys(RunnerClass.getOccupants().trim());
			Thread.sleep(1000);
			}
		}
		catch(Exception e)
		{
			try
			{
				actions.moveToElement(driver.findElement(Locators.otherOccupants)).build().perform();
				driver.findElement(Locators.otherOccupants).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.otherOccupants).sendKeys(RunnerClass.getOccupants());
			}
			catch(Exception e2)
			{
			//DataBase.notAutomatedFields(buildingAbbreviation, "Lease Occupants"+'\n');
			failedReason = failedReason+",Lease Occupants";
			}
			//temp=1;
		}
		if(PDFReader.petFlag==true||PDFReader.serviceAnimalFlag==true)
		{
		//pet information
			//Thread.sleep(2000);
			
			//Pet Type
			String petType = String.join(",", PDFReader.petType);
			String petBreed = String.join(",", PDFReader.petBreed);
			String petWeight = String.join(",", PDFReader.petWeight);
			petInfoDistribution(petType,petWeight,petBreed);
			
			try
			{
				actions.moveToElement(driver.findElement(Locators.pet1Type)).build().perform();
				driver.findElement(Locators.pet1Type).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.pet1Type).sendKeys(type1);
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Types"+'\n');
				failedReason = failedReason+",Pet Types";
				//temp=1;
			}
			//Thread.sleep(2000);
			//Pet Breed
			//String petBreed = String.join(",", PDFReader.petBreed);
			try
			{
				actions.moveToElement(driver.findElement(Locators.pet1Breed)).build().perform();
				driver.findElement(Locators.pet1Breed).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.pet1Breed).sendKeys(breed1);
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Breed"+'\n');
				failedReason = failedReason+",Pet Breed";
				//temp=1;
			}
			
			//Pet Weight
			//String petWeight = String.join(",", PDFReader.petWeight);
			try
			{
				actions.moveToElement(driver.findElement(Locators.pet1Weight)).build().perform();
				driver.findElement(Locators.pet1Weight).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.pet1Weight).sendKeys(weight1);
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Weight"+'\n');
				failedReason = failedReason+",Pet Weight";
				//temp=1;
			}
			//Pet 2 Info
			try
			{
				actions.moveToElement(driver.findElement(Locators.pet2Type)).build().perform();
				driver.findElement(Locators.pet2Type).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.pet2Type).sendKeys(type2);
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Types"+'\n');
				failedReason = failedReason+",Pet Types";
				//temp=1;
			}
			try
			{
				actions.moveToElement(driver.findElement(Locators.pet2Breed)).build().perform();
				driver.findElement(Locators.pet2Breed).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.pet2Breed).sendKeys(breed2);
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Breed"+'\n');
				failedReason = failedReason+",Pet Breed";
				//temp=1;
			}
			try
			{
				actions.moveToElement(driver.findElement(Locators.pet2Weight)).build().perform();
				driver.findElement(Locators.pet2Weight).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.pet2Weight).sendKeys(weight2);
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Weight"+'\n');
				failedReason = failedReason+",Pet Weight";
				//temp=1;
			}
			
			//Pet 3 Info
			try
			{
				actions.moveToElement(driver.findElement(Locators.pet3Type)).build().perform();
				driver.findElement(Locators.pet3Type).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.pet3Type).sendKeys(type3);
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Types"+'\n');
				failedReason = failedReason+",Pet Types";
				//temp=1;
			}
			try
			{
				actions.moveToElement(driver.findElement(Locators.pet3Breed)).build().perform();
				driver.findElement(Locators.pet3Breed).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.pet3Breed).sendKeys(breed3);
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Breed"+'\n');
				failedReason = failedReason+",Pet Breed";
				//temp=1;
			}
			try
			{
				actions.moveToElement(driver.findElement(Locators.pet3Weight)).build().perform();
				driver.findElement(Locators.pet3Weight).clear();
				Thread.sleep(1000);
				driver.findElement(Locators.pet3Weight).sendKeys(weight3);
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Weight"+'\n');
				failedReason = failedReason+",Pet Weight";
				//temp=1;
			}
			
			
			
			//Pet Rent
			//Thread.sleep(2000);
			try
			{
				if(PDFReader.petRent.equalsIgnoreCase("Error"))
				{
					//DataBase.notAutomatedFields(buildingAbbreviation, "pet Rent"+'\n');
					failedReason = failedReason+",Pet Rent";
					//temp=1;
				}
				else
				{
					try
					{
				actions.moveToElement(driver.findElement(Locators.petAmount)).build().perform();
				//driver.findElement(Locators.petAmount).clear();
				driver.findElement(Locators.petAmount).click();
				//OKC_PropertyWare.clearTextField();
				driver.findElement(Locators.petAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				Thread.sleep(1000);
				//actions.click(driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
				driver.findElement(Locators.petAmount).sendKeys(PDFReader.petRent);
					}
					catch(Exception e)
					{
						actions.moveToElement(driver.findElement(Locators.petAmount2)).build().perform();
						//driver.findElement(Locators.petAmount).clear();
						driver.findElement(Locators.petAmount2).click();
						//OKC_PropertyWare.clearTextField();
						driver.findElement(Locators.petAmount2).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
						Thread.sleep(1000);
						//actions.click(driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
						driver.findElement(Locators.petAmount2).sendKeys(PDFReader.petRent);
					}
				}
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "pet Rent"+'\n');
				failedReason = failedReason+",Pet Rent";
				//temp=1;
			}
			try
			{
				if(PDFReader.petOneTimeNonRefundableFee.equalsIgnoreCase("Error"))
				{
					//DataBase.notAutomatedFields(buildingAbbreviation, "Pet One Time Non-Refundable Fee"+'\n');
					failedReason = failedReason+",Pet One Time Non-Refundable Fee";
					//temp=1;
				}
				else
				{
					try
					{
				actions.moveToElement(driver.findElement(Locators.tenantOneTimePetFee)).build().perform();
				driver.findElement(Locators.tenantOneTimePetFee).click();
				Thread.sleep(1000);
				driver.findElement(Locators.tenantOneTimePetFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				//OKC_PropertyWare.clearTextField();
				//actions.click(driver.findElement(Locators.tenantOneTimePetFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
				driver.findElement(Locators.tenantOneTimePetFee).sendKeys(PDFReader.petOneTimeNonRefundableFee);
					}
					catch(Exception e)
					{
						actions.moveToElement(driver.findElement(Locators.petDepositAmount)).build().perform();
						driver.findElement(Locators.petDepositAmount).click();
						Thread.sleep(1000);
						driver.findElement(Locators.petDepositAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
						//OKC_PropertyWare.clearTextField();
						//actions.click(driver.findElement(Locators.tenantOneTimePetFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
						driver.findElement(Locators.petDepositAmount).sendKeys(PDFReader.petOneTimeNonRefundableFee);
					}
				}
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "pet One Time Non-Refundable Fee"+'\n');
				failedReason = failedReason+",Pet One Time Non-Refundable Fee";
				//temp=1;
			}
			
			//Initial Pet Rent Amount
			try
			{
				if(PDFReader.petRent.equalsIgnoreCase("Error"))
				{
					failedReason = failedReason+",Intial Pet Rent";
					//DataBase.notAutomatedFields(buildingAbbreviation, "Intial Monthly Rent"+'\n');
					//temp=1;
				}
				else
				{
				actions.moveToElement(driver.findElement(Locators.initialPetRentAmount)).build().perform();
				driver.findElement(Locators.initialPetRentAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				driver.findElement(Locators.initialPetRentAmount).sendKeys(PDFReader.petRent);
				
				}
			}
			catch(Exception e)
			{
				DataBase.notAutomatedFields(buildingAbbreviation, "Intial Pet Monthly Rent"+'\n');
				failedReason = failedReason+",Intial Pet Rent";
				//temp=1;
			}
			
			//Pet Rent Amount
			try
			{
				if(PDFReader.petRent.equalsIgnoreCase("Error"))
				{
					failedReason = failedReason+",Pet Rent";
					//DataBase.notAutomatedFields(buildingAbbreviation, "Intial Monthly Rent"+'\n');
					//temp=1;
				}
				else
				{
				actions.moveToElement(driver.findElement(Locators.petRentAmount)).build().perform();
				driver.findElement(Locators.petRentAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				driver.findElement(Locators.petRentAmount).sendKeys(PDFReader.petRent);
				
				}
			}
			catch(Exception e)
			{
				DataBase.notAutomatedFields(buildingAbbreviation, "Pet Rent Amount"+'\n');
				failedReason = failedReason+",Pet Rent";
				//temp=1;
			}
			
		}
			//Service Animal Information
			if(PDFReader.serviceAnimalFlag==true)
			{
				//Thread.sleep(2000);
				/*
				//Pet Type
				String ServiceAnimal_petType = String.join(",", PDFReader.serviceAnimalPetType);
				try
				{
					actions.moveToElement(driver.findElement(Locators.serviceAnimal_pet2Type)).build().perform();
					driver.findElement(Locators.serviceAnimal_pet2Type).clear();
					Thread.sleep(1000);
					driver.findElement(Locators.serviceAnimal_pet2Type).sendKeys("Service "+ServiceAnimal_petType);
				}
				catch(Exception e)
				{
					//DataBase.notAutomatedFields(buildingAbbreviation, "Pet 2 Types"+'\n');
					failedReason = failedReason+",Pet 2 Types";
					//temp=1;
				}
				//Thread.sleep(2000);
				//Pet Breed
				String serviceAnimal_petBreed = String.join(",", PDFReader.serviceAnimalPetBreed);
				try
				{
					actions.moveToElement(driver.findElement(Locators.serviceAnimal_pet2Breed)).build().perform();
					driver.findElement(Locators.serviceAnimal_pet2Breed).clear();
					Thread.sleep(1000);
					driver.findElement(Locators.serviceAnimal_pet2Breed).sendKeys(serviceAnimal_petBreed);
				}
				catch(Exception e)
				{
					//DataBase.notAutomatedFields(buildingAbbreviation, "Pet 2 Breed"+'\n');
					failedReason = failedReason+",Pet 2 Breed";
					//temp=1;
				}
				
				
				//Thread.sleep(2000);
				//Pet Weight
				String serviceAnimal_petWeight = String.join(",", PDFReader.serviceAnimalPetWeight);
				try
				{
					actions.moveToElement(driver.findElement(Locators.serviceAnimal_pet2Weight)).build().perform();
					driver.findElement(Locators.serviceAnimal_pet2Weight).clear();
					Thread.sleep(1000);
					driver.findElement(Locators.serviceAnimal_pet2Weight).sendKeys(serviceAnimal_petWeight);
				}
				catch(Exception e)
				{
					//DataBase.notAutomatedFields(buildingAbbreviation, "Pet 2 Weight"+'\n');
					failedReason = failedReason+",Pet 2 Weight";
					//temp=1;
				}
				*/
				//Pet Special Provisions
				try
				{
					actions.moveToElement(driver.findElement(Locators.petSpecialProvisions)).build().perform();
					driver.findElement(Locators.petSpecialProvisions).clear();
					Thread.sleep(1000);
					driver.findElement(Locators.petSpecialProvisions).sendKeys("Service animals, no deposit required");
				}
				catch(Exception e)
				{
					//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Special Provisions"+'\n');
					failedReason = failedReason+",Pet Special Provisions";
					//temp=1;
				}
				
			} //Service Animal block
			else
			{
			//Pet Security Deposit
			//Thread.sleep(2000);
			try
			{
				//if(!OKC_PropertyWare.petSecurityDeposit.equalsIgnoreCase("Error")||!OKC_PropertyWare.petSecurityDeposit.trim().equalsIgnoreCase(" ")||!OKC_PropertyWare.petSecurityDeposit.trim().equalsIgnoreCase(""))
				if(RunnerClass.onlyDigits(PDFReader.petSecurityDeposit.trim())==true)
				{
				actions.moveToElement(driver.findElement(Locators.petDepositAmount)).build().perform();
				//driver.findElement(Locators.petAmount).clear();
				driver.findElement(Locators.petDepositAmount).click();
				driver.findElement(Locators.petDepositAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				//OKC_PropertyWare.clearTextField();
				Thread.sleep(1000);
				//actions.click(driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
				driver.findElement(Locators.petDepositAmount).sendKeys(PDFReader.petSecurityDeposit);
				}
			}
			catch(Exception e)
			{
				//DataBase.notAutomatedFields(buildingAbbreviation, "Pet Security Deposit"+'\n');
				failedReason = failedReason+",Pet Security Deposit";
				//temp=1;
			}
			}
			
			
			

		//Initial Monthly Payment
		try
		{
			if(RunnerClass.getMonthlyRent().equalsIgnoreCase("Error"))
			{
				failedReason = failedReason+",Intial Monthly Rent";
				//DataBase.notAutomatedFields(buildingAbbreviation, "Intial Monthly Rent"+'\n');
				//temp=1;
			}
			else
			{
				if(company.equals("Boise")||company.equals("Idaho Falls")||company.equals("Utah"))
				{
					actions.moveToElement(driver.findElement(Locators.monthlyRentAmount)).build().perform();
					driver.findElement(Locators.monthlyRentAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					driver.findElement(Locators.monthlyRentAmount).sendKeys(RunnerClass.getMonthlyRent());
				}
				else
				{
			        actions.moveToElement(driver.findElement(Locators.initialMonthlyRent)).build().perform();
			        driver.findElement(Locators.initialMonthlyRent).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			        driver.findElement(Locators.initialMonthlyRent).sendKeys(RunnerClass.getMonthlyRent());
				}
			
			}
		}
		catch(Exception e)
		{
			DataBase.notAutomatedFields(buildingAbbreviation, "Intial Monthly Rent"+'\n');
			failedReason = failedReason+",Intial Monthly Rent";
			//temp=1;
		}
		
		//Current Monthly Rent
				try
				{
					if(RunnerClass.getMonthlyRent().equalsIgnoreCase("Error"))
					{
						failedReason = failedReason+",Current Monthly Rent";
						//DataBase.notAutomatedFields(buildingAbbreviation, "Intial Monthly Rent"+'\n');
						//temp=1;
					}
					else
					{
					actions.moveToElement(driver.findElement(Locators.currentMonthlyRent)).build().perform();
					//driver.findElement(Locators.initialMonthlyRent).clear();
					driver.findElement(Locators.currentMonthlyRent).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					driver.findElement(Locators.currentMonthlyRent).sendKeys(RunnerClass.getMonthlyRent());
					
					}
				}
				catch(Exception e)
				{
					DataBase.notAutomatedFields(buildingAbbreviation, "Current Monthly Rent"+'\n');
					failedReason = failedReason+",Current Monthly Rent";
					//temp=1;
				}
		
		//Late Fee Rule
		//OKC_InsertDataIntoPropertyWare.lateFeeRuleMethod(OKC_PropertyWare.lateFeeType);
		mainPackage.LateFeeRule.lateFeeRule(PDFReader.lateFeeRuleType,driver);
		
		//Thread.sleep(2000);
		js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
		try
		{
			Thread.sleep(2000);
			if(AppConfig.saveButtonOnAndOff==true)
			{
			actions.moveToElement(driver.findElement(Locators.saveLease)).click(driver.findElement(Locators.saveLease)).build().perform();
			Thread.sleep(2000);
			try
			{
				RunnerClass.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				RunnerClass.wait.until(ExpectedConditions.invisibilityOf(driver.findElement(Locators.saveLease)));
			}
			catch(Exception e) {}
			if(driver.findElement(Locators.saveLease).isDisplayed())
			{
				actions.moveToElement(driver.findElement(Locators.leaseOccupants)).build().perform();
				driver.findElement(Locators.leaseOccupants).clear();
				actions.moveToElement(driver.findElement(Locators.saveLease)).click(driver.findElement(Locators.saveLease)).build().perform();
				Thread.sleep(2000);
			}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//Thread.sleep(2000);
		/*
		if(temp==0)
		RunnerClass.leaseCompletedStatus = 1;
		else RunnerClass.leaseCompletedStatus = 3;
		*/
		return true;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		//RunnerClass.leaseCompletedStatus = 2;
		//Thread.sleep(2000);
		if(AppConfig.saveButtonOnAndOff==true)
		{
		actions.moveToElement(driver.findElement(Locators.saveLease)).click(driver.findElement(Locators.saveLease)).build().perform();
		if(driver.findElement(Locators.saveLease).isDisplayed())
		{
			actions.moveToElement(driver.findElement(Locators.leaseOccupants)).build().perform();
			driver.findElement(Locators.leaseOccupants).clear();
			actions.moveToElement(driver.findElement(Locators.saveLease)).click(driver.findElement(Locators.saveLease)).build().perform();
			//Thread.sleep(2000);
		}
		}
		return false;
	}
	}
	
	public static void petInfoDistribution(String petType, String petWeight, String petBreed)
	{
	 //String  type1,type2,type3,weight1,weight2,weight3,breed1,breed2,breed3;
	 //type1=type2=type3=weight1=weight2=weight3=breed1=breed2=breed3 ="";
		if(PDFReader.petFlag==false&&PDFReader.serviceAnimalFlag==true)
		{
			 if(String.join(",",PDFReader.serviceAnimalPetType).contains(","))
			 {
				 PDFReader.serviceAnimalPetType.replaceAll(s->"Service "+s);
				 type1 = String.join(",",PDFReader.serviceAnimalPetType).split(",",2)[0];
				 type2 = String.join(",",PDFReader.serviceAnimalPetType).split(",",2)[1];
				 
				 weight1 = String.join(",",PDFReader.serviceAnimalPetWeight).split(",",2)[0];
				 weight2 = String.join(",",PDFReader.serviceAnimalPetWeight).split(",",2)[1];
				 
				 breed1 = String.join(",",PDFReader.serviceAnimalPetBreed).split(",",2)[0];
				 breed2 = String.join(",",PDFReader.serviceAnimalPetBreed).split(",",2)[1];
			 }
			 else
			 {
				 PDFReader.serviceAnimalPetType.replaceAll(s->"Service "+s);
				 type1 = String.join(",",PDFReader.serviceAnimalPetType);
				 weight1 = String.join(",",PDFReader.serviceAnimalPetWeight);
				 breed1 = String.join(",",PDFReader.serviceAnimalPetBreed);
			 }
		}
		else
	 if(PDFReader.serviceAnimalFlag==true)
	 {
		 PDFReader.serviceAnimalPetType.replaceAll(s->"Service "+s);
		 type3=String.join(",",PDFReader.serviceAnimalPetType);
		 weight3=String.join(",",PDFReader.serviceAnimalPetWeight);
		 breed3 = String.join(",",PDFReader.serviceAnimalPetBreed);
		 
		 if(petType.contains(","))
		 {
			 type1 = petType.split(",",2)[0];
			 type2 = petType.split(",",2)[1];
			 
			 weight1 = petWeight.split(",",2)[0];
			 weight2 = petWeight.split(",",2)[1];
			 
			 breed1 = petBreed.split(",",2)[0];
			 breed2 = petBreed.split(",",2)[1];
		 }
		 else
		 {
			 type1 = petType;
			 weight1 = petWeight;
			 
			 breed1 = petBreed;
		 }
	 }
	 else
	 {
		 int count =(int)petType.chars().filter(ch -> ch == ',').count();
		 if(count==0)
		 {
			 type1 = petType;
			 
			 weight1 = petWeight;
			 
			 breed1 = petBreed;
		 }
		 else  if(count==1)
		 {
			 type1 = petType.split(",",2)[0];
			 type2 = petType.split(",",2)[1];
			 
			 weight1 = petWeight.split(",",2)[0];
			 weight2 = petWeight.split(",",2)[1];
			 
			 breed1 = petBreed.split(",",2)[0];
			 breed2 = petBreed.split(",",2)[1]; 
		 }
		 else if(count>1)
		 {
			 
			 type1 = petType.split(",",3)[0];
			 type2 = petType.split(",",3)[1];
			 type3 = petType.split(",",3)[2];
			 
			 weight1 = petWeight.split(",",3)[0];
			 weight2 = petWeight.split(",",3)[1];
			 weight3 = petWeight.split(",",3)[2];
			 
			 breed1 = petBreed.split(",",3)[0];
			 breed2 = petBreed.split(",",3)[1]; 
			 breed3 = petBreed.split(",",3)[2];
		 }			
		 }
		
	 
	 System.out.println("Pet Type = "+type1+ "  |  "+type2+"   |  "+type3);
	 System.out.println("Pet Weight = "+weight1+ "  |  "+weight2+"   |  "+weight3);
	 System.out.println("Pet Breed = "+breed1+ "  |  "+breed2+"   |  "+breed3);
	 }
	 
		
}
