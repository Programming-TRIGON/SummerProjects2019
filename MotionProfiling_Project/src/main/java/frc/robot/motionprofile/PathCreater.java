package frc.robot.motionprofile;

import java.io.File;
import java.io.IOException;

import frc.robot.RobotConstants;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

/**
 * This class generates all the paths for the path follower command to use.
 */
public class PathCreater {

    public final Trajectory.Config config;

    /**
     * We configure the pathfinding and create new pathes, then we generate all the
     * pathes we created
     */
    public PathCreater() {
        this.config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH,
                RobotConstants.TIMEFRAME, RobotConstants.MAX_VELOCITY, RobotConstants.MAX_ACCELERATION,
                RobotConstants.MAX_JERK);

    }

    /** Writes all the paths to csv for quick generation of paths */
    public void writeToCSV_AllPaths() {
        for (Path path : Path.values()) {
            String s = "/home/lvuser/Paths " + path.name() + ".csv";
            Pathfinder.writeToCSV(new File(s), path.getTrajectory());
        }
    }
    

}
