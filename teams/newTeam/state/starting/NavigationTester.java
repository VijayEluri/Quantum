package newTeam.state.starting;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import newTeam.handler.navigation.NavigatorType;
import newTeam.state.BaseState;

public class NavigationTester extends BaseState {
    
    public NavigationTester(BaseState oldState, MapLocation goal) {
        super(oldState);
        
        myMH.initializeNavigationTo(goal, NavigatorType.BUG_BACKWARD);
    }
    
    @Override
    public BaseState execute() {
        if(myMH.step()) {
            myMH.initializeNavigationTo(myK.myLocation.add(Direction.WEST, 30), 
                    NavigatorType.BUG_BACKWARD);
        }
        return this;
    }

}
