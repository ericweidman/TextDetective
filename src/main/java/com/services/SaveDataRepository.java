package com.services;

import com.entities.SaveData;
import org.springframework.data.repository.CrudRepository;



/**
 * Created by ericweidman on 2/6/17.
 */
public interface SaveDataRepository extends CrudRepository<SaveData, Integer> {
    SaveData findByUserId(int id);

}
