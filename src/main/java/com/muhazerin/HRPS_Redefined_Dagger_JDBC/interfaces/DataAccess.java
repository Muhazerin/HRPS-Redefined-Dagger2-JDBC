package com.muhazerin.HRPS_Redefined_Dagger_JDBC.interfaces;

import java.util.ArrayList;

import com.muhazerin.HRPS_Redefined_Dagger_JDBC.entity.Entity;

/**
 * 
 * @author muhazerin
 *
 */
public interface DataAccess {
	public ArrayList<Entity> retrieveAllObjects();	
	public void insertObject(Entity entity);
	public void updateObject(Entity oldEntity, Entity newEntity);
	//public void removeObject(Entity entity);
}
