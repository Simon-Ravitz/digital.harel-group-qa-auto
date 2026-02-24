package com.harel.travel.core;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Path;

public class ExtentManager {
    private static ExtentReports extent;

    public static synchronized ExtentReports getExtent() {
        if (extent == null) {
            Path reportPath = Path.of("target", "extent-report", "index.html");
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath.toString());
            reporter.config().setReportName("Harel Travel Policy Automation");
            reporter.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }
}
