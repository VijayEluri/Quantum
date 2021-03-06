/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sprint.common;

import battlecode.common.*;

/**
 * The name is short for Prefabricated. These are the robot builds we have hard-coded.
 *
 * @author sashko
 */
public abstract class Prefab {

    private static final ComponentType[] commRecyclerComponents = { ComponentType.RECYCLER, ComponentType.ANTENNA };
    public static BuildInstructions commRecycler = new BuildInstructions(Chassis.BUILDING, commRecyclerComponents);

    private static final ComponentType[] lightSoldierComponents = {ComponentType.SIGHT, ComponentType.ANTENNA, ComponentType.BLASTER, ComponentType.BLASTER};
    private static final ComponentType[] lightContructorComponents = {ComponentType.SIGHT, ComponentType.ANTENNA, ComponentType.CONSTRUCTOR};
    public static BuildInstructions lightSoldier = new BuildInstructions(Chassis.LIGHT, lightSoldierComponents);
    public static BuildInstructions lightConstructor = new BuildInstructions(Chassis.LIGHT, lightContructorComponents);

    public static BuildInstructions[] startingRecyclerUnits = { Prefab.commRecycler, Prefab.lightSoldier };
    public static BuildOrder startingRecyclerBuildOrder = new BuildOrder( startingRecyclerUnits );
}
