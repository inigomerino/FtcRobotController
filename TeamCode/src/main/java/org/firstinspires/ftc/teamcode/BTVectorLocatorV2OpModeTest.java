package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.hardware.BTHardwareMapProvider;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTHardwareNotFoundException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTSubsystemInitializationException;
import org.firstinspires.ftc.teamcode.libs.math.BTLocation;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTHardwareFactory;
import org.firstinspires.ftc.teamcode.libs.subsystems.control.BTAutonomousControlModule;
import org.firstinspires.ftc.teamcode.libs.subsystems.drive.BTAutonomousDrive;
import org.firstinspires.ftc.teamcode.libs.subsystems.location.BTVectorLocatorV2;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTNavigation;
import org.firstinspires.ftc.teamcode.libs.subsystems.planning.BTRoute;

import java.util.List;


@TeleOp(name="BTVectorLocatorV2OpMode Test", group="Tests")
public class BTVectorLocatorV2OpModeTest extends LinearOpMode {

    private StringBuilder telemetryLog;
    private String currentReading;
    private BTVectorLocatorV2 locator;

    //////////////////////// Telemetry stuff -- TODO: move to separate class

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

    //////////////////////// End Telemetry

    private void testVectorLocatorV2(long seconds) {

        // notify
        this.printUpdate("Testing vector locator v2. Robot will NOT drive. Move robot manually and confirm values printed are as expected.");

        // run drive system
        long startTime = System.currentTimeMillis();
        long maxTime = seconds * 1000;

        // Loop until the current time - start time is less than maxTime
        while (opModeIsActive() && ( System.currentTimeMillis() - startTime < maxTime )) {            
            sleep(1000/50); // this is 20 ms
            BTLocation pos = this.locator.getPosition();
            this.updateReading("Odometers: \n" + this.locator.getTelemetry() + "\n" + 
                "Position: " + pos.toString());
        }

        // done
        this.printUpdate("VectorLocatorV2 test complete - take pics now. Sleeping for 10 mins - hit Stop to end earlier");
        sleep(10 * 60 * 1000);
    }

    private void initVectorLocatorV2() throws BTHardwareNotFoundException {

        ////////// load and configure a BTBTVectorLocatorV2
        
        // init hwMap factory -- has to be done b4 init'ing robot
        BTHardwareMapProvider prov = new BTHardwareMapProvider(this.hardwareMap, this.gamepad1, this.gamepad2 );
        BTHardwareFactory.getInstance().initProvider( prov );
        this.printUpdate("Initialized hardware provider");

        // init it
        this.locator = new BTVectorLocatorV2();
        this.printUpdate("Initialized BTBTVectorLocatorV2");

    }

    @Override
    public void runOpMode() {

        try {

            // init the log
            telemetryLog = new StringBuilder();
            currentReading = "";

            // done
            this.printUpdate("Initialization starting\n");
            this.printUpdate("*** NOTES:");
            this.printUpdate("***  1) Starts asuming position (0, 0, 0°)");
            this.printUpdate("***  2) Ensure there's space in other directions to conduct tests");

            // init
            this.initVectorLocatorV2();
            this.printUpdate("Initialization complete. Press 'start' to start\nProgram will run for a max time of 15.5 minutes before exiting.\nPress 'stop' at any time to abort.");

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // run tests
            this.testVectorLocatorV2(( ( 15 * 60 ) + 30 ) * 1000);  // 15 minutes on top of the autonomous runtime of 30s should be enough

        } catch (Exception e) {
            this.printUpdate("Unknown error in main loop: " + e);
            
        } finally {
            this.printUpdate("Suggested troubleshooting actions: \n" + 
                " - Run BTRawDriveTrainTestOpMode to see if all odometers are working properly");

            this.printUpdate("\n\nThis message will self-destruct in 10 minutes (so take a pic)");
            sleep(10*60*1000);
        }
    }

}
