package com.hoangvhh.services;

/**
 *
 * @author HoangVHH
 */
public interface RoutePlanService {
    
    /**
     * Get the cost which is applied for the completed route.
     * @param fromStopID
     * @param toStopID
     * @return the applied cost
     */
    float getCompletedRouteCost(String fromStopID,String toStopID);
    
    /**
     * Get the maximum cost which is applied for the uncompleted route
     * @param stopID
     * @return the applied cost
     */
    float getUncompletedRouteCost(String stopID);
}
