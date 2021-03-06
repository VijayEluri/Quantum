package sprint.common.location;

public enum LocationType {
    
    OFF_MAP,
    VOID,
    LAND,
    UNMINED_MINE,
    OUR_MINE,
    ENEMY_MINE, // actually "enemy building on mine"
    OUR_RECYCLER,
    ENEMY_RECYClER,
    OUR_FACTORY,
    ENEMY_FACTORY,
    OUR_ARMORY,
    ENEMY_ARMORY,
    DEBRIS,
    DESTROYED_DEBRIS,
    // default "null" should be considered "UNKNOWN"

}
