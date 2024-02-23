package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; 
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.libs.exceptions.BTHardwareNotFoundException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTSubsystemInitializationException;
import org.firstinspires.ftc.teamcode.libs.robots.BT2023SeasonRobotV4;
import org.firstinspires.ftc.teamcode.libs.robots.BTIRobot;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;    
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotNotInitializedException;    
import org.firstinspires.ftc.teamcode.libs.math.BTLocation;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTIAutonomousController;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTNavigation;
import org.firstinspires.ftc.teamcode.libs.subsystems.planning.BTRoute;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTNavigationV2;
import org.firstinspires.ftc.teamcode.processors.BTVisionProcessor;


@Autonomous()
public class BTAutonomousOpModeRed2a extends BTAutonomousOpModeV2 {
    
    // you have to define this
    @Override
    protected BTIRobot makeRobot(BTVisionProcessor.Selected pixLoc)
                                                throws BTSubsystemInitializationException,
                                                        BTRobotNotInitializedException, 
                                                        BTRobotInitializationException, 
                                                        BTHardwareNotFoundException {

        // pix orient mod
        double pixOrient;
        if (pixLoc == BTVisionProcessor.Selected.LEFT) {
            pixOrient = 180.0;
        } else if (pixLoc == BTVisionProcessor.Selected.MIDDLE) {
            pixOrient = 90.0;
        } else {
            pixOrient = 0.0;
        }
        
        // notable spots worth visiting
        BTLocation red2Home            = new BTLocation(33.25,  8.0, 90.0);
        BTLocation red2PixelCenterPos  = new BTLocation(33.25, 39.0, 90.0);
        BTLocation red2PixelBackPos    = new BTLocation(33.25, 33.0, 90.0);
        BTLocation red2EscapePos       = new BTLocation(17.75, 33.0, 90.0);
        BTLocation red2Foward          = new BTLocation(17.75, 62.5, 90.0);
        BTLocation red2CornerPos       = new BTLocation(127.5, 55.5, 90.0);

        // construct the route we want to follow
        BTRoute r = new BTRoute();
        r.addStop(red2PixelCenterPos);     // drive to push pixel
        r.addStop(red2PixelBackPos);       // back away from pixel
        r.addStop(red2EscapePos);          // clear pixel
        r.addStop(red2Foward);             // move to x coord center of map
        r.addStop(red2CornerPos);          // drive to end position
        
        // make robot
        BT2023SeasonRobotV4 robot = new BT2023SeasonRobotV4();
        BTIAutonomousController c = robot.getNavigation();
        c.setPosition(red2Home);
        c.setRoute(r);
        
        return robot;
    }

}
  





