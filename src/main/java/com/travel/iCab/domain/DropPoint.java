/**
 * 
 */
package com.travel.iCab.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

@PlanningEntity
public interface DropPoint {
    /**
     * @return never null
     */
    GeoLocation getGeoLocation();

    /**
     * @return sometimes null
     */
    Cab getCab();

    /**
     * @return sometimes null
     */
    @InverseRelationShadowVariable(sourceVariableName = "previousDropPoint")
    Employee getNextDropEmp();
    void setNextDropEmp(Employee nextDropEmp);

}
