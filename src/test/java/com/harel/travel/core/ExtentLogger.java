package com.harel.travel.core;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ExtentLogger {
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private ExtentLogger() {}

    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public static void clear() {
        test.remove();
    }

    public static void info(String message) {
        ExtentTest t = test.get();
        if (t != null) {
            t.log(Status.INFO, message);
        }
    }

    public static void pass(String message) {
        ExtentTest t = test.get();
        if (t != null) {
            t.log(Status.PASS, message);
        }
    }
}
