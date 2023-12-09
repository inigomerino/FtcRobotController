package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.libs.exceptions.BTException;

import java.util.List;


@TeleOp(name="Raw Hardware Test", group="Tests")
public class BTRawHardwareTestOpMode extends LinearOpMode {

    private StringBuilder telemetryLog;
    private String currentReading;
    private long testDurationSeconds = 15;

    private void resfreshDisplay() {
        // notify
        telemetry.addData("BTStat", telemetryLog.toString());
        telemetry.addData("Reading", currentReading);
        telemetry.update();
    }

    private void printUpdate(String message) {

        // add it
        telemetryLog.append(message).append("\n");

        // refresh
        this.resfreshDisplay();
    }

    private void updateReading(String reading) {

        // do it
        currentReading = reading;

        // refresh
        this.resfreshDisplay();
    }

    private void commitReading() {
        this.printUpdate(this.currentReading);
        this.updateReading("(none)");
    }

    private void runMotor(String name, double speed, long seconds) {

        try {

            // notify
            this.printUpdate("Testing Motor '" + name + "' with " + speed + "%power for " + seconds + "sec.");

            // get it
            DcMotor m = hardwareMap.get(DcMotor.class, name);

            // run
            m.setPower(0.5);

            // Run motor for a few seconds
            sleep(seconds * 1000);

            // Stop the motor
            m.setPower(0);

            // done
            this.printUpdate("Test complete for motor '" + name + ".");
        
//        } catch (BTException e) {
//            this.printUpdate("BT error while testing motor '" + name + "': " + e.getStackTrace());
        } catch (Exception e) {
            this.printUpdate("Unknown error while testing motor '" + name + "': " + e);
        }
    }

    private void runOdometer(String name, long seconds) {

        try {

            // notify
            this.printUpdate("Reading from Odometer '" + name + " for " + seconds + "sec.");

            // get it
            DcMotor m = hardwareMap.get(DcMotor.class, name);
            // reset
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            // set mode
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // apparently this refers to the motor's built in encoder; not for extenral encoders. See https://gm0.org/en/latest/docs/software/tutorials/encoders.html

            // Record start time
            long startTime = System.currentTimeMillis();
            long maxTime = seconds * 1000;

            // Loop until the current time - start time is less than maxTime
            int ticks = 0;
            while (opModeIsActive() && ( System.currentTimeMillis() - startTime < maxTime )) {

                // get number of revolutions
                ticks = m.getCurrentPosition();   // NOTE: TODO: WATCH FOR INT OVERFLOWs. This has info BUT appears to be outdated: https://docs.ftclib.org/ftclib/features/hardware/motors
                this.updateReading("Odometer '" + name + "' ticks: " + ticks);
            }
            
            // done
            this.printUpdate("Test complete for odometer '" + name + ".");
        
//        } catch (BTException e) {
//            this.printUpdate("BT error while testing motor '" + name + "': " + e.getStackTrace());
        } catch (Exception e) {
            this.printUpdate("Unknown error while testing odometer '" + name + "': " + e);
        }
    }

    private void testMotors() {

        // notify
        this.printUpdate("Testing motors. Each motor will run for " + this.testDurationSeconds + " seconds. Confirm behavior and values printed are as expected.");

        // Initialize and run motors one by one
        for (String name : new String[]{"dtFL", "dtFR", "dtBL", "dtBR"}) {
            this.runMotor(name, 0.5, this.testDurationSeconds);
            this.commitReading();
        }

        // Initialize and run motors one by one
        for (String name : new String[]{"armL", "armR"}) {
            this.runMotor(name, 0.5, 3);
            this.commitReading();
        }

        // done
        this.printUpdate("Motor test complete");
    }

    private void testOdometers() {

        // notify
        this.printUpdate("Testing odometers. Each odometer will be reading for " + this.testDurationSeconds + " seconds. Confirm behavior and values printed are as expected.");

        // Initialize and run motors one by one
        for (String name : new String[]{"dtFL", "dtBL"}) {
            //, "dtFR", , "dtBR"}) {
            this.runOdometer(name, testDurationSeconds);
            this.commitReading();
        }

        // done
        this.printUpdate("Odometer test complete");
    }


    @Override
    public void runOpMode() {

        try {

            // init the log
            telemetryLog = new StringBuilder();
            currentReading = new String();

            // done
            this.printUpdate("Initialization complete");
            this.printUpdate("*** NOTE: Place robot in a safe position ***");

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // run tests
            this.testOdometers();
            this.testMotors();

        } catch (Exception e) {
            this.printUpdate("Unknown error in main loop: " + e);
        } finally {
            this.printUpdate("Suggested troubleshooting actions: " +
                    "Check robot configuration in the driver station. " +
                    "Drivetrain motor placement on control hub:  'dtFL' -> 0, 'dtFR' -> 1, 'dtBL' -> 2, 'dtBR -> 3" +
                    "Check that the failed elements are correctly connecter to the appropriate ports in the control station. " +
                    "Check that the opMode initialized the hardware map properly. " +
                    "Check that motors, wheels and other mechanisms are rotating freely and not rubbing. ");
        }
    }

}
