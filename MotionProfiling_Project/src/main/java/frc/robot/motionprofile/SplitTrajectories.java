package frc.robot.motionprofile;

import frc.robot.RobotConstants;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;

/**
 * This class splits the trajectories for each side of the robot.
 */
public class SplitTrajectories {

    private TankModifier modifier;
    private Path path;

    public SplitTrajectories(Path path) {
        this.path = path;
        this.modifier = new TankModifier(this.path.getTrajectory()).modify(RobotConstants.WHEEL_BASE_WIDTH);
    }

    public Trajectory getRightTrajectory() {
        return modifier.getRightTrajectory();
    }

    public Trajectory getLeftTrajectory() {
        return modifier.getLeftTrajectory();
    }
}
