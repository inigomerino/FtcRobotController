package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; 
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.BTHardwareMapProvider;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTHardwareNotFoundException;
import org.firstinspires.ftc.teamcode.libs.robots.BT2023SeasonRobotV2;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotNotInitializedException;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTHardwareFactory;

@Autonomous()
public abstract class BTAutonomousOpMode extends OpMode {
    
    // members
    private BT2023SeasonRobotV2 robot;    
    private ElapsedTime runtime;

    // you have to define this
    protected abstract BT2023SeasonRobotV2 makeRobot() throws BTRobotInitializationException, BTRobotNotInitializedException, BTHardwareNotFoundException;

    // methods
    @Override
    public void init() {
        try {

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
            this.runtime = new ElapsedTime();
            this.robot = this.makeRobot();

            // notify
            telemetry.addData("BTStat", "Robot built");

            // get the routes
            telemetry.addData("BTStat", "Routes:");
            String routeInfo = this.robot.getRoute().toString();
            telemetry.addData("BTStat", routeInfo);

            // notify
            telemetry.addData("BTStat", "Initialized");

        } catch (BTRobotInitializationException e) {
            telemetry.addData("BTStat", "Failed: Robot initialization error: " + e.getStackTraceAsString());
        } catch (BTRobotNotInitializedException e) {
            telemetry.addData("BTStat", "Failed: Robot could not be initialized successfully: " + e.getStackTraceAsString());
        } catch (Exception e) {
            telemetry.addData("BTStat", "Failed: Unidentified error: " + e);
        }
    }

    @Override
    public void start() {
        runtime.reset();
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


