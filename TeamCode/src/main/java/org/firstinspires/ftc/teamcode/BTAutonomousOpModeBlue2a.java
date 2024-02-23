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
public class BTAutonomousOpModeBlue2a extends BTAutonomousOpModeV2 {
    
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
        BTLocation blue2Home            = new BTLocation(106.0, 8.0, 90.0);
        BTLocation blue2PixelCenterPos  = new BTLocation(106.0, 39.0, 90.0);
        BTLocation blue2PixelBackPos    = new BTLocation(106.0, 35.0, 90.0);
        BTLocation blue2EscapePos       = new BTLocation(126.0, 35.0, 90.0);
        BTLocation blue2Foward          = new BTLocation(126.0, 76.0, 90.0);
        BTLocation blue2CornerPos       = new BTLocation(8.0, 62.0, 90.0);

        // construct the route we want to follow
        BTRoute r = new BTRoute();
        r.addStop(blue2PixelCenterPos);     // drive to push pixel
        r.addStop(blue2PixelBackPos);       // back away from pixel
        r.addStop(blue2EscapePos);          // clear pixel
        r.addStop(blue2Foward);             // move to x coord center of map
        r.addStop(blue2CornerPos);          // drive to end position
        
        // make robot
        BT2023SeasonRobotV4 robot = new BT2023SeasonRobotV4();
        BTIAutonomousController c = robot.getNavigation();
        c.setPosition(blue2Home);
        c.setRoute(r);
        
        return robot;
    }

}
  





