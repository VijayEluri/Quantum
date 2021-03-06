package sprint.common;

import battlecode.common.*;

public class PathFinder {

    private final RobotController myRC;
    private final ComponentsHandler myCH;
    private final Knowledge knowledge;
    
    private        NavigationAlgorithm  navAlg = NavigationAlgorithm.NONE;
    private        boolean              navigating = false;
    private        MapLocation          goal;
    
    /*** Exploring info ***/
    private        int                  exploreRound;
    private        int                  savedExploreRound;
    private        MapLocation          savedExploreGoal;
    private        Direction            exploreDirection;
    private        boolean              exploringPaused = false;
    
     /*** Bug Navigation info ***/
    private        boolean              tracking;
    private        boolean              trackingCW;
    private        boolean              stopTracking;
    private        Direction            trackingDirection;
    private        Direction            trackingRefDirection;
    private        MapLocation          bugStart;
    private        MapLocation []       bugPrevLocations;
    private        Direction []         bugPrevDirections;
    private        int                  bugStep;
    private        enum                 BugState { ROTATE, MOVE_FORWARD, MOVE_BACKWARD };
    
    public PathFinder(RobotController rc, ComponentsHandler comp, Knowledge know) {
        myRC = rc;
        myCH = comp;
        knowledge = know;
        
        //set a pseudo-random direction to start exploring
        exploreDirection = Direction.values()[knowledge.myRobotID % 8];
        exploreRound = knowledge.roundNum - 1;
        goal = knowledge.myLocation.add(exploreDirection, QuantumConstants.EXLPORE_GOAL_DISTANCE);
    }
    
    public void explore () throws GameActionException {
        if(exploringPaused) {
            exploreRound = knowledge.roundNum - savedExploreRound;
            exploringPaused = false;
            goal = savedExploreGoal;
            initiateBugNavigation();
        }
        
        if(reachedGoal()) {
            //keep exploring in the same direction
            goal = knowledge.myLocation.add(exploreDirection, QuantumConstants.EXLPORE_GOAL_DISTANCE);
            initiateBugNavigation();
            exploreRound = knowledge.roundNum;
        } else if((knowledge.roundNum - exploreRound) % QuantumConstants.EXPLORE_TIME == 0) {
            // find a new exploration goal
            exploreDirection = exploreDirection.rotateRight().rotateRight().rotateRight();
            goal = knowledge.myLocation.add(exploreDirection, QuantumConstants.EXLPORE_GOAL_DISTANCE);
            
            initiateBugNavigation();
        } 
        
        step();
        
    }
    
    public void pauseExploration() {
        exploringPaused = true;
        savedExploreRound = (knowledge.roundNum - exploreRound) % QuantumConstants.EXPLORE_TIME;
        savedExploreGoal = goal;
    }
    
    public void setNavigationAlgorithm(NavigationAlgorithm alg) {
        navAlg = alg;
    }
    
    public void setGoal(MapLocation g) {
        goal = g;
    }
    
    public boolean isNavigating() {
        return navigating;
    }
    
    public void step() throws GameActionException {
        //do nothing if the motor is active or you are not navigating
        if(!navigating)
            return;
        if(myCH.motorActive())
            return;
        
        switch(navAlg) {
            case BUG:
                navigateBug();
                break;
            case A_STAR:
                break;
        }        
    }
    
    public boolean reachedGoal() {        
        return knowledge.myLocation.equals(goal);
    }
    
    public void initiateBugNavigation() {
//        bugNavigating = true;
        navAlg = NavigationAlgorithm.BUG;
        navigating = true;
        tracking = false;
        stopTracking = false;
//        bugGoal = goal;
        bugStart = myRC.getLocation();
        bugPrevLocations = new MapLocation [QuantumConstants.BUG_MEMORY_LENGTH];
        bugPrevDirections = new Direction [QuantumConstants.BUG_MEMORY_LENGTH];
        bugStep = 0;
        myRC.setIndicatorString(2, "you are using pathFinder");
    }
    
    public void navigateBug() throws GameActionException {    
     
        MapLocation location = knowledge.myLocation;
        Direction directionToGoal = location.directionTo(goal);
//        Direction myDirection = knowledge.myDirection;
        int bugPos = bugStep % QuantumConstants.BUG_MEMORY_LENGTH;
        
        //stop navigating once you have reached your goal
        if(reachedGoal()) {
            navigating = false;
            return;
        }
        
        myRC.yield();
        if(myCH.motorActive())
            return;
        
        BugState action = determineBugState();
        
        switch(action) {
            case ROTATE:
                if(tracking)
                    myCH.setDirection(trackingDirection);
                else
                    myCH.setDirection(directionToGoal);
                break;
            
            case MOVE_FORWARD:
                myCH.moveForward();
                
                if(stopTracking){
                    tracking = false;
                    stopTracking = false;
                }
                break;
                
            case MOVE_BACKWARD:
                myCH.moveBackward();
                
                if(stopTracking){
                    tracking = false;
                    stopTracking = false;
                }
                break; 
        }
       
    }

    private BugState determineBugState() {
        
        MapLocation location = knowledge.myLocation;
        Direction directionToGoal = location.directionTo(goal);
//        Direction myDirection = knowledge.myDirection;
        int bugPos = bugStep % QuantumConstants.BUG_MEMORY_LENGTH;
        BugState action;
        
        if(tracking) {
            //set the initial reference direction to begin testing from.
            Direction testDirection = trackingRefDirection;
            
            //necessary to track in the direction opposite the reference direction.
            if(trackingDirection == trackingRefDirection.opposite()){
                if(trackingCW)
                    testDirection = testDirection.rotateLeft();
                else
                    testDirection = testDirection.rotateRight();
            }
            
            //check directions beginning with the reference direction, in the order depending on 
            //if you are tracking clockwise or counterclockwise
            boolean pathBlocked = true;
            while(pathBlocked) {
                if(myCH.canMove(testDirection)){
                    trackingDirection = testDirection;
                    pathBlocked = false;
                }
                
                //increment direction
                if(trackingCW)
                    testDirection = testDirection.rotateLeft();
                else
                    testDirection = testDirection.rotateRight();
                
                //stop checking if you are surrounded
                if(testDirection == trackingRefDirection) {
                    trackingDirection = testDirection;
                    break;
                }
            }

            //check if you are finished tracking
            if(trackingDirection == trackingRefDirection) {
                stopTracking = true;
            }
            
            action = getBugAction(trackingDirection);
            
        } else {
            //not tracking, so check if you can move towards the goal
            if(myCH.canMove(directionToGoal)) {
                action = getBugAction(directionToGoal);
            } else {
                //the path is blocked, so begin tracking.
                tracking = true;
                bugPos = (bugPos + 1) % QuantumConstants.BUG_MEMORY_LENGTH;
                bugPrevLocations[bugPos] = location;
                trackingRefDirection = directionToGoal;
            
                //determine if you should track around the obstacle clockwise or counterclockwise
                Direction ccwDir = directionToGoal;
                Direction cwDir = directionToGoal;
                boolean searching = true;
                while(searching) {
                    ccwDir = ccwDir.rotateRight();
                    cwDir = cwDir.rotateLeft();
                    if(myCH.canMove(ccwDir)) {
                        bugPrevDirections[bugPos] = ccwDir;
                        trackingCW = false;
                        searching = false;
                        if(myCH.canMove(cwDir) && location.add(cwDir).distanceSquaredTo(goal) 
                                < location.add(ccwDir).distanceSquaredTo(goal)) {
                            bugPrevDirections[bugPos] = cwDir;
                            trackingCW = true;
                        }
                    } else if(myCH.canMove(cwDir)) {
                        bugPrevDirections[bugPos] = cwDir;
                        trackingCW = true;
                        searching = false;
                    }
                        
                    //stop searching if you are surrounded
                    if(ccwDir == cwDir) {
                        bugPrevDirections[bugPos] = ccwDir;
                        break;
                    }
                }
                
                trackingDirection = bugPrevDirections[bugPos];
                action = getBugAction(trackingDirection);
            }
        }
        
        myRC.setIndicatorString(0, "reference " + trackingRefDirection +
                " tracking " + trackingDirection);
        myRC.setIndicatorString(1,"tracking: " + tracking + 
                "; orientation is clockwise: " + trackingCW);
        myRC.setIndicatorString(2, "you are using pathFinder");
        return action;

    }
    
    private BugState getBugAction(Direction dir) {
        BugState action;
        if(knowledge.myDirection == dir)
            action = BugState.MOVE_FORWARD;
//        else if(knowledge.myDirection == dir.opposite())
//            action = BugState.MOVE_BACKWARD;
        else
            action = BugState.ROTATE;
        
        return action;
    }

    public void navigateToAdjacent() throws GameActionException{
        //must initiate bug navigation before calling.
        if(!navigating)
            return;
        
        MapLocation location = knowledge.myLocation;
        
        if(location.distanceSquaredTo(goal) <= 2) {
            if(myCH.motorActive()) {
                return;
            } else {
                navigating = false;
                myCH.setDirection(location.directionTo(goal));
                return;
            }
        }
    
        navigateBug();

    }
}

