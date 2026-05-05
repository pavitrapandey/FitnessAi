package com.fitU.activityService.Repository;

import com.fitU.activityService.Model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepo extends MongoRepository<Activity,String>
{
    List<Activity> findByUserId(String userId);
}
