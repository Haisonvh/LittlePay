package com.hoangvhh.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author HoangVHH
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RoutePlanServiceTest {
    @Autowired
    private RoutePlanService routePlan;

    @Test
    public void shouldReturn0WhenGetSameStop() {
        float response = routePlan.getCompletedRouteCost("Stop1", "Stop1");
        Assert.assertEquals(0f, response,0.0f);
    }
    
    @Test
    public void shouldReturnCorrectWhenGetCorrectRoute() {
        float response = routePlan.getCompletedRouteCost("Stop1", "Stop2");
        Assert.assertEquals(3.25f, response,0.0f);
    }
    
    @Test
    public void shouldReturnSameValueWhenGetSameRoute() {
        float response1 = routePlan.getCompletedRouteCost("Stop1", "Stop2");
        float response2= routePlan.getCompletedRouteCost("Stop2", "Stop1");
        Assert.assertEquals(response2, response1,0.0f);
    }

    @Test
    public void shouldReturnCorrectWhenGetUncompleteWithStopInMid() {
        float response = routePlan.getUncompletedRouteCost("Stop2");
        Assert.assertEquals(5.5f, response,0.0f);
    }
    @Test
    public void shouldReturnCorrectWhenGetUncompleteWithStopInStart() {
        float response = routePlan.getUncompletedRouteCost("Stop1");
        Assert.assertEquals(7.3f, response,0.0f);
    }
    @Test
    public void shouldReturnCorrectWhenGetUncompleteWithStopInEnd() {
        float response = routePlan.getUncompletedRouteCost("Stop3");
        Assert.assertEquals(7.3f, response,0.0f);
    }
}
