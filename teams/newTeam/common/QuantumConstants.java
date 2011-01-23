package newTeam.common;

/**
 * Class for keeping adjustable constants for gameplay.  
 */
import battlecode.common.*;

public final class QuantumConstants {

        /* Number of messages a robot stores in its outgoing message queue. */
        public static final int MESSAGE_QUEUE_LENGTH = 10;
        /* How many rounds between recurring message broadcasts */
        public static final int PING_CYCLE_LENGTH = 4;
        /* How many rounds a unit waits for a command from the recycler before moving out */
        public static final int TIME_WAIT_FOR_COMMAND = 10;
        /* How many rounds a factory/armory will wait for a chassis to show up before aborting build process */
        public static final int BUILD_TIMEOUT = 5;
        /* How many rounds a flying sensor unit will wait when a mine becomes occupied */
        public static final int BUILD_RECYCLER_TIMEOUT = 3;
        /* How many rounds a flying sensor unit will wait when a mine becomes occupied */
        public static final int STATUS_TIMEOUT = 3;
	/** Total flux needed to explore for more mines */
	public static final int TOTAL_FLUX_EXPLORE = 120;
	/** Number of locations to remember for bug navigation */
	public static final int BUG_MEMORY_LENGTH = 15;
	/** Force bug to begin backtracking at this number */
	public static final int BUG_FORCE_BACKTRACK = 350;
	/** Maximum path weight before aborting bug navigation */
	public static final int BUG_ABORT = 500;
	/** Bug distance increase before trying different path */
	public static final int BUG_DISTANCE_SWITCH = 30;
	/** Maximum number of moves to remember doing tangent bug */
	public static final int TANGENT_BUG_PATH_LENGTH = 200;
	/** Maximum number of virtual bugs in the tangent bug algorithm */
	public static final int NUMBER_OF_VIRTUAL_BUGS = 10;
	/** Farthest sensor distance... assumed to be the satellite **/
	public static final int FARTHEST_SENSOR_DISTANCE = ComponentType.SATELLITE.range;
	/** Size of map location hashes used **/
	// Because robots can sense off the map in both directions, the hash must be big enough
	//   to handle that.
	public static final int LOCATION_HASH_SIZE = GameConstants.MAP_MAX_WIDTH +
	                                             2 * FARTHEST_SENSOR_DISTANCE;
	/** Number of rounds to navigate before setting a new exploration goal */
	public static final int EXPLORE_TIME = 50;
	/** Distance to exploration goal */
	public static final int EXLPORE_GOAL_DISTANCE = 15;

        /** Number of soldiers per constructor */
	public static final int SOLDIERS_PER_CONSTRUCTOR = 4;
	/** Big integer */
	public static final int BIG_INT = 65536;
	/** Whether or not we attack debris */
	public static final boolean ATTACK_DEBRIS = false;
	
	/** Max conceivable number of robots on the map */
	public static final int MAX_TOTAL_NUMBER_OF_ROBOTS = 2000;
	/** Max conceivable number of robots sensed */
	public static final int MAX_NUMBER_OF_SENSABLE_THINGS = 400;
	
	/** Cut-off for delta flux for building soldiers*/
	public static final int DELTA_FLUX_CUTOFF = 3;
	
	/** Planck's constant */
	public static final double H = 6.626069E-34;
	/** Reduced Planck's constant */
	public static final double H_BAR = 1.0545716E-34;
	
	private QuantumConstants() {	
	}
}
