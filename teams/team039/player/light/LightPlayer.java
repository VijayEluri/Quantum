package team039.player.light;

import team039.common.*;
import team039.common.util.Logger;
import team039.handler.ComponentsHandler;
import team039.player.SpecificPlayer;
import team039.player.SpecificPlayerImpl;
import battlecode.common.*;

public class LightPlayer extends SpecificPlayerImpl {
    
    private final RobotController   myRC;
    private final Knowledge         knowledge;
    private final ComponentsHandler compHandler;
        
    public LightPlayer(RobotController rc,
                       Knowledge know,
                       ComponentsHandler compHand) {
        
        super(rc, know, compHand);
        myRC        = rc;
        knowledge   = know;
        compHandler = compHand;
    }
    
    public void doSpecificActions() {
        super.doSpecificActions();
    }
    
    public void doSpecificFirstRoundActions() {
        super.doSpecificActions();
    }

    public void beginningStateSwitches()
    {
        if( knowledge.myState == RobotState.JUST_BUILT )
        {
            Logger.debug_print( "I called JUST_BUILT at round " + knowledge.roundNum  );
            knowledge.myState = RobotState.IDLE;
        }
    }
    
    public SpecificPlayer determineSpecificPlayer(ComponentType compType) {
        SpecificPlayer result = this;

        

        switch(compType) {
        case CONSTRUCTOR:
            result = new LightConstructorPlayer(myRC, knowledge, compHandler);
            break;
        case BLASTER:
            result = new LightSoldierPlayer(myRC, knowledge, compHandler);
            break;
        }
        
        return result;
    }

}