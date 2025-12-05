package com.fitU.aiFitness.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "recommendation")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    private String id;
    private String userId;
    private String activityId;
    private String recommendationType;
    

}
