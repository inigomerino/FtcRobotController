package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; 
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.libs.exceptions.BTHardwareNotFoundException;
import org.firstinspires.ftc.teamcode.libs.robots.BT2023SeasonRobotV2;
import org.firstinspires.ftc.teamcode.libs.robots.BTIRobot;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;    
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotNotInitializedException;    
import org.firstinspires.ftc.teamcode.libs.math.BTLocation;
import org.firstinspires.ftc.teamcode.libs.subsystems.planning.BTRoute;


@Autonomous()
public class BTAutonomousOpModeRed2 extends BTAutonomousOpMode {
    
    // you have to define this
    @Override
    protected BT2023SeasonRobotV2 makeRobot() throws BTRobotInitializationException, BTRobotNotInitializedException, BTHardwareNotFoundException {

        // notable spots worth visiting
        BTLocation red2Home            = new BTLocation(33.25, 8);
        BTLocation red2PixelCenterPos  = new BTLocation(33.25, 41);
        BTLocation red2PixelBackPos    = new BTLocation(33.25, 33);
        BTLocation red2EscapePos       = new BTLocation(17.75, 33);
        BTLocation red2Foward          = new BTLocation(17.75, 62.5);
        BTLocation red2CornerPos       = new BTLocation(127.5, 55.5);

        // construct the route we want to follow
        BTRoute r = new BTRoute();
        r.addStop(red2PixelCenterPos);     // drive to push pixel
        r.addStop(red2PixelBackPos);       // back away from pixel
        r.addStop(red2EscapePos);          // clear pixel
        r.addStop(red2Foward);             // move to x coord center of map
        r.addStop(red2CornerPos);          // drive to end position
        
        return new BT2023SeasonRobotV2(red2Home, r);
    }

}
  


