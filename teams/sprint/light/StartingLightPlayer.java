package sprint.light;

import sprint.common.ComponentsHandler;
import sprint.common.Knowledge;
import sprint.common.SpecificPlayer;
import sprint.common.util.Logger;

import battlecode.common.*;

public class StartingLightPlayer extends LightConstructorPlayer {
    
    private final RobotController   myRC;
    private final Knowledge         knowledge;
    private final ComponentsHandler compHandler;
        
    public StartingLightPlayer(RobotController rc,
                               Knowledge know,
                               ComponentsHandler compHand) {
        
        super(rc, know, compHand);
        myRC        = rc;
        knowledge   = know;
        compHandler = compHand;
    }
    
    @Override
    public void doSpecificActions() {
        super.doSpecificActions();
    }
    
    @Override
    public void doSpecificFirstRoundActions() {
        try {
            super.doSpecificFirstRoundActions();
            
            Direction desiredDirection = compHandler.senseStartingLocation();
            if(desiredDirection == Direction.OMNI) {
                if(knowledge.myLocation.isAdjacentTo(knowledge.startingUnminedMineLocations[0])) {
                    // wait to build mine?
                }
                else {
                    // move to be adjacent to mine
                }
            }
            else if(desiredDirection != Direction.NONE) {
                compHandler.setDirection(desiredDirection);
            }
        }
        catch(Exception e) {
            Logger.debug_printExceptionMessage(e);
        }
    }
    
    @Override
    public SpecificPlayer determineSpecificPlayer(ComponentType compType) {
        
        SpecificPlayer result = this;
        return result;
    }

}
