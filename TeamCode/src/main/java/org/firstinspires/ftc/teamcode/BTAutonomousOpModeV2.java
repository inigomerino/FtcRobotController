package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; 
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.hardware.BTHardwareMapProvider;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTHardwareNotFoundException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTSubsystemInitializationException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotNotInitializedException;
import org.firstinspires.ftc.teamcode.libs.robots.BTIRobot;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTHardwareFactory;
import org.firstinspires.ftc.teamcode.processors.BTVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous()
public abstract class BTAutonomousOpModeV2 extends OpMode {
    
    // members
    private BTIRobot robot;
    private ElapsedTime runtime;
    private BTVisionProcessor visionProcessor;
    private VisionPortal visionPortal;

    // you have to define this
    protected abstract BTIRobot makeRobot(BTVisionProcessor.Selected pixLoc) 
            throws BTSubsystemInitializationException,
                    BTRobotNotInitializedException, 
                    BTRobotInitializationException, 
                    BTHardwareNotFoundException;

    // methods
    private void initVision() {

        // vision
        this.visionProcessor = new BTVisionProcessor();
        this.visionPortal = VisionPortal.easyCreateWithDefaults( hardwareMap.get(WebcamName.class, "Webcam 1"), visionProcessor );
        telemetry.addData("Vision", "Initialized Portal");

        // give service time to start
        telemetry.addData("Vision", "Initializing stream...");
        while ( visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING ) {
            telemetry.addData("Vision", "State: " + visionPortal.getCameraState());
            try {
                Thread.sleep(250); // give stuff time to load before we say we are done
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        telemetry.addData("Vision", "State: " + visionPortal.getCameraState());       
        // telemetry.addData("Vision", "Streaming");        
    }

    private void initRobot(BTVisionProcessor.Selected pixLoc) throws BTSubsystemInitializationException,
                                                                BTRobotNotInitializedException, 
                                                                BTRobotInitializationException, 
                                                                BTHardwareNotFoundException {
        
        // notify
        telemetry.addData("BTStat", "Initializing robot with pixel loc " + pixLoc);

        // make it
        this.robot = this.makeRobot(pixLoc);
    }

    @Override
    public void init_loop() {

        // telemetry.addData("init_loop()", "Starting");

        // read pixel position
        BTVisionProcessor.Selected selection = this.visionProcessor.getSelection();
        String sv = this.visionProcessor.getSaturationValues();

        // notify
        telemetry.addData("Vision", "Camera id'd pixel loc: " + selection);
        telemetry.addData("Vision", "Saturations: " + sv );

        // adjust for camera placement
        if (selection == BTVisionProcessor.Selected.LEFT) {
            // because of cammera placement, LEFT indicates left
            // do othing   
        } else if ( (selection == BTVisionProcessor.Selected.RIGHT) || 
                    (selection == BTVisionProcessor.Selected.MIDDLE)) {
            // because of cammera placement, RIGHT or LEFT indicates center
            selection = BTVisionProcessor.Selected.MIDDLE;
        } else {
            // because of cammera placement, NONE indicates right
            selection = BTVisionProcessor.Selected.RIGHT;
        }

        // notify
        telemetry.addData("Vision", "Corrected pixel loc: " + selection);
        telemetry.addData("Vision", "Saturations: " + sv );

        // init robot, route, etc
        try {
            this.initRobot(selection);

            // notify
            telemetry.addData("BTStat", "Robot built");
    
            // get the routes
            telemetry.addData("BTStat", "Routes:");
            String routeInfo = this.robot.getNavigation().getRoute().toString();
            telemetry.addData("BTStat", routeInfo);

        } catch (BTRobotInitializationException e) {
            telemetry.addData("BTStat", "Failed: Robot initialization error: " + e.getStackTraceAsString());
        } catch (BTRobotNotInitializedException e) {
            telemetry.addData("BTStat", "Failed: Robot could not be initialized successfully: " + e.getStackTraceAsString());
        } catch (Exception e) {
            telemetry.addData("BTStat", "Failed: Unidentified error: " + e);
        }

    }

    @Override
    public void init() {

        // init telemetry
        // We show the log in oldest-to-newest order, as that's better for poetry
        telemetry.log().setDisplayOrder(Telemetry.Log.DisplayOrder.OLDEST_FIRST);

        // We can control the number of lines shown in the log
        telemetry.log().setCapacity(6);

        // notify
        telemetry.addData("BTStat", "Initializing");

        // init hwMap factory -- has to be done b4 init'ing robot
        BTHardwareMapProvider prov = new BTHardwareMapProvider(this.hardwareMap, this.gamepad1, this.gamepad2 );
        BTHardwareFactory.getInstance().initProvider( prov );

        // init members
        this.initVision();

        // notify
        telemetry.addData("BTStat", "Initialized");
    }

    @Override
    public void start() {

        // start timer
        runtime = new ElapsedTime();

        // stop this to conserve CPU (or so we're told)
        visionPortal.stopStreaming();
    }

    @Override
    public void stop() {

        // stop the robot
        robot.stop();

        // log
        this.logNavStatus();
        
        // notify
        telemetry.addData("BTStat", "Run Time: " + runtime.toString());
        telemetry.update();
    }

    private void logNavStatus() {

        // add it
        telemetry.log().add("Nav Logs:");

        // get debug info
        StringBuilder logString = new StringBuilder();
        for (String entry : robot.getNavigation().getLog()) {
            telemetry.log().add(entry);
            //logString.append(entry);
            logString.append("\n");          
        }

    }

    @Override
    public void loop() {
        try {

            // do some stuff
            this.robot.loop();
      
            // log
            this.logNavStatus();

            // notify
            telemetry.addData("BTStat", "Run Time: " + runtime.toString());
      
        } catch (Exception e) {
        
            // over here, should we try to re-initialize and re-start?
            // error-recovery would be nice ...
            // for now, just report the error
        
            // notify
            telemetry.addData("BTStat", "Unidentified error: " + e);
    
        }

    }
}


