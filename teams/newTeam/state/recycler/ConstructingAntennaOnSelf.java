package newTeam.state.recycler;

import battlecode.common.*;

import newTeam.state.BaseState;
import newTeam.state.idle.Idling;
import newTeam.common.Prefab;
import newTeam.common.util.Logger;
import newTeam.state.idle.Idling;

public class ConstructingAntennaOnSelf extends BaseState {

    public ConstructingAntennaOnSelf(BaseState oldState) {
        super(oldState);

    }

    @Override
    public void senseAndUpdateKnowledge() {
    }

    @Override
    public BaseState getNextState() {

        if( myBH.finishedBuilding() )
        {
            return new UpgradeBase(this);
        }

        return this;
    }

    @Override
    public BaseState execute() {

        //add a sensor method that checks if the square is occupied
        if( !myBH.getCurrentlyBuilding() && myRC.getTeamResources() > Prefab.commRecycler.getComponentCost() - ComponentType.RECYCLER.cost + 10 )
        {
            Logger.debug_printSashko("building antenna");
            myBH.buildComponents( Prefab.commRecycler , myRC.getLocation());
        }

        myBH.step();
        
        return this;
    }

}
