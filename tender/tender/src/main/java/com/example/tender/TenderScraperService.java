package com.example.tender;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenderScraperService {

    public List<Tender> scrapeTenders() {
        // System.setProperty("webdriver.chrome.driver",
        // "C:\\Windows\\chromedriver.exe"); // ✅ Update to your actual path
        System.setProperty("webdriver.chrome.driver", "C:\\Windows\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        List<Tender> tenders = new ArrayList<>();

        try {
            // Navigate to CPWD eTender portal
            driver.get("https://etender.cpwd.gov.in/");
            Thread.sleep(3000);

            // Click "New Tenders" > "All"
            driver.findElement(By.linkText("New Tenders")).click();
            Thread.sleep(2000);
            driver.findElement(By.linkText("All")).click();
            Thread.sleep(4000);

            // Get tender table rows
            List<WebElement> rows = driver.findElements(
                    By.xpath("//table[contains(@class,'table') and contains(@class,'table-bordered')]/tbody/tr"));
            System.out.println("Total rows found: " + rows.size());

            for (int i = 0; i < Math.min(20, rows.size()); i++) {
                List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));

                if (cells.size() >= 8) {
                    tenders.add(new Tender(
                            cells.get(1).getText().trim(), // NIT/RFP NO
                            cells.get(2).getText().trim(), // Name of Work
                            cells.get(4).getText().trim(), // Estimated Cost
                            cells.get(6).getText().trim(), // Bid Submission End Date
                            cells.get(5).getText().trim(), // EMD
                            cells.get(7).getText().trim() // Bid Opening Date
                    ));
                }
            }

            // Save extracted data to CSV
            writeToCSV(tenders);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return tenders;
    }

    private void writeToCSV(List<Tender> tenders) throws IOException {
        // File csvFile = new File("C:\\Users\\visha\\OneDrive\\Desktop\\tenders.csv");
        File csvFile = new File("C:\\Users\\visha\\OneDrive\\Desktop\\tenders.csv");

        FileWriter out = new FileWriter(csvFile);

        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader("ref_no", "title", "tender_value", "bid_submission_end_date", "emd", "bid_open_date"))) {
            for (Tender tender : tenders) {
                printer.printRecord(
                        tender.getRefNo(),
                        tender.getTitle(),
                        tender.getTenderValue(),
                        tender.getBidSubmissionEndDate(),
                        tender.getEmd(),
                        tender.getBidOpenDate());
            }
        }

        // Open the file automatically
        if (csvFile.exists() && Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(csvFile);
        }
    }
}