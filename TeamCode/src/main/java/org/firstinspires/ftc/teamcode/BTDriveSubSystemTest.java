package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.hardware.BTHardwareMapProvider;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTSubsystemInitializationException;
import org.firstinspires.ftc.teamcode.libs.math.BTLocation;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTHardwareFactory;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTIController;
import org.firstinspires.ftc.teamcode.libs.subsystems.control.BTAutonomousControlModule;
import org.firstinspires.ftc.teamcode.libs.subsystems.drive.BTAutonomousDrive;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTIAutonomousController;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTNavigation;
import org.firstinspires.ftc.teamcode.libs.subsystems.planning.BTRoute;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTNavigationV3;

import java.util.List;


@TeleOp(name="BTDriveSubSystem Test", group="Tests")
public class BTDriveSubSystemTest extends LinearOpMode {

    private StringBuilder telemetryLog;
    private String currentReading;
    private BTAutonomousDrive driveSubsystem;
    private boolean drive = true;
    private boolean turn = true;
    private double drivePwr = 0.5;
    private double rotPwr = 0.5;

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

    private void testDriveSystem(long seconds) {

        // notify
        this.printUpdate("Testing drive system. Robot will drive a simple test route. Confirm behavior and values printed are as expected.");

        // // print table 
        // String headerFormat = "%-7s  %7s  %7s  %7s  %7s";
        // String headerSep = "----------";
        // String heading = String.format(headerFormat, "motor", "% power", "μ", "𝜎", "𝜎/μ");
        // String separator = String.format(headerFormat, headerSep, headerSep, headerSep, headerSep, headerSep);
        // this.printUpdate( heading);
        // this.printUpdate( separator);

        // run drive system
        long startTime = System.currentTimeMillis();
        long maxTime = seconds * 1000;

        // Loop until the current time - start time is less than maxTime
        int i = 0;
        while (opModeIsActive() && ( System.currentTimeMillis() - startTime < maxTime )) {            

            // loop
            this.driveSubsystem.loop();
            sleep(1000/50); // this is 20 ms

            // // give chance to debug / see screen
            // if (++i % 10 == 0) {
            //     this.driveSubsystem.stop();
            //     sleep(1000); // this is 20 ms    
            // }

            String tele = this.driveSubsystem.readTelemetry();
            this.updateReading(tele);
        }

        // done
        this.printUpdate("Motor test complete - take pics now. Sleeping for 10 mins - hit Stop to end earlier");
        sleep(10 * 60 * 1000);
    }

    private BTAutonomousDrive makeDriveSS() throws BTSubsystemInitializationException {

        // We no longer support drive and turn toggles so commeting this out for now
        // // config it
        // this.ConfigDrive();

        // init it
        BTAutonomousDrive dSS = new BTAutonomousDrive();
        this.printUpdate("Initialized autonomous drive subsystem");

        // debugging
        if (!this.drive) {
            this.printUpdate("WARNING: Setting 'drive' is false");
        }
        dSS.setDrive(drive);

        if (!this.turn) {
            this.printUpdate("WARNING: Setting 'turn' is false");
        }
        dSS.setTurn(turn);

        return dSS;
    }

    private void ConfigDrive() {

        boolean configd = false;
        this.printUpdate("Config DSS");
        this.printUpdate("--------------");
        this.printUpdate("A: drive = false");
        this.printUpdate("B: drive = true");
        this.printUpdate("X: turn  = false");
        this.printUpdate("Y: turn  = true");
        this.printUpdate("Left trigger:  increase DT pwr");
        this.printUpdate("Right trigger: decrease DT pwr");
        this.printUpdate("\n ====> WHEN DONE: Left bumper => done");

        while(!configd) {

            if( gamepad1.left_bumper) {
                configd = true;
                this.printUpdate("DSS Settings configured!");
            }

            if( gamepad1.a ) {
                this.printUpdate("Setting drive = False");
                this.drive = false;
            }

            if( gamepad1.b ) {
                this.printUpdate("Setting drive = True");
                this.drive = true;
            }

            if( gamepad1.y ) {
                this.printUpdate("Setting turn = True");
                this.turn = true;
            }

            if( gamepad1.x ) {
                this.printUpdate("Setting turn = False");
                this.turn = false;
            }

            if ( gamepad1.left_trigger > 0.0 ) {
                this.drivePwr   = Math.min( this.drivePwr + 0.1, 1.0 );
                this.rotPwr     = Math.min( this.rotPwr   + 0.1, 1.0 ); ;
                this.printUpdate("Setting drive speed    = " + this.drivePwr);
                this.printUpdate("Setting rotation speed = " + this.rotPwr);
            }

            if ( gamepad1.right_trigger > 0.0 ) {
                this.drivePwr   = Math.max( this.drivePwr - 0.1, 0.1 );
                this.rotPwr     = Math.max( this.rotPwr   - 0.1, 0.1 ); ;
                this.printUpdate("Setting drive speed    = " + this.drivePwr);
                this.printUpdate("Setting rotation speed = " + this.rotPwr);
            }

            // give me a break
            sleep(250);

        }

    }


    private void initDriveSS() throws BTSubsystemInitializationException {

        ////////// load and configure an autonomous drive subsystem

        // init hwMap factory -- has to be done b4 init'ing robot
        BTHardwareMapProvider prov = new BTHardwareMapProvider(this.hardwareMap, this.gamepad1, this.gamepad2 );
        BTHardwareFactory.getInstance().initProvider( prov );
        this.printUpdate("Initialized hardware provider");

        // init it
        this.driveSubsystem = this.makeDriveSS();

        // get CM and nav
        BTAutonomousControlModule autoCM = (BTAutonomousControlModule) this.driveSubsystem.getControlModule();

        // get Nav
        BTIController ctrlr = autoCM.getController();
        BTIAutonomousController c = (BTNavigationV3) ctrlr;

        // set a low max speed so we can see what it's doing
        // c.setDriveSpeed(this.drivePwr);
        // c.setRotationSpeed(this.rotPwr);
        this.printUpdate(String.format("Initialized power adj factor to: %.2f (drive), %.2f (rotation)", this.drivePwr, this.rotPwr));

        // set the route
        BTLocation homeFacingN     = new BTLocation( 0.0,  0.0,  90.0);
        BTLocation homeFacingE     = new BTLocation( 0.0,  0.0,   0.0);
        BTLocation homeFacingS     = new BTLocation( 0.0,  0.0, -90.0);
        BTLocation homeFacingW     = new BTLocation( 0.0,  0.0, 180.0);
        BTLocation shortDistN      = new BTLocation( 0.0,  4.0,  90.0);
        BTLocation shortDistNE     = new BTLocation( 0.0,  4.0,   0.0);
        BTLocation shortDistE      = new BTLocation( 0.0,  4.0,   0.0);
        BTLocation shortDistW      = new BTLocation(-4.0,  0.0,  90.0);
        BTLocation longDist        = new BTLocation(-4*12.0, 0.0, 90.0);

        // start position
        BTRoute r = new BTRoute();

        // construct the route we want to follow

        // // this is just a rotation test
        // r.addStop(homeFacingE);
        // r.addStop(homeFacingN);
        // r.addStop(homeFacingW);
        // r.addStop(homeFacingN);

        // this is mini-Blue1 with pixel on the E(ast) position 
        r.addStop(shortDistN);
        r.addStop(shortDistNE);
        r.addStop(shortDistN);
        r.addStop(homeFacingN);
        r.addStop(shortDistW);
        r.addStop(homeFacingN);

        // r.addStop(homeFacingS);
        // r.addStop(homeFacingW);
        // r.addStop(homeFacingN);
        // r.addStop(homeFacingW); 
        // r.addStop(shortDistE);
        // r.addStop(shortDistN);
        // r.addStop(homeFacingN);               // back away from pixel (back home)
        // r.addStop(longDist);            // drive to push pixel
        // r.addStop(homeFacingN); 

        // make the route
        c.setPosition(homeFacingN);
        c.setRoute(r);

        // notify
        this.printUpdate("Set starting position to: " + homeFacingE);
        this.printUpdate("Set route to: \n" + c.getRoute());

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
            this.printUpdate("***  1) Requires min 6' forward");
            this.printUpdate("***  2) Requires min 8\" to the right");
            this.printUpdate("***  3) Ensure there's space in other directions");

            // init
            initDriveSS();
            this.printUpdate("Initialization complete. Press 'start' to start\nProgram will run for a max time of 15.5 minutes before exiting.\nPress 'stop' at any time to abort.");

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // run tests
            this.testDriveSystem(( ( 15 * 60 ) + 30 ) * 1000);  // 15 minutes on top of the autonomous runtime of 30s should be enough

        } catch (Exception e) {
            this.printUpdate("Unknown error in main loop: " + e);
            
        } finally {
            this.printUpdate("Suggested troubleshooting actions: \n" + 
                " - Run BTRawDriveTrainTestOpMode to see if all wheels are working properly");

            this.printUpdate("\n\nThis message will self-destruct in 10 minutes (so take a pic)");
            sleep(10*60*1000);
        }
    }

}
