package com.java.java_proj.dto.response.fordetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DResponseSyllabusOutline {

    @Getter
    private String code;
    private List<DResponseTrainingUnit> topicOutline;

    public HashMap<Integer, List<DResponseTrainingUnit>> getTopicOutline() {
        HashMap<Integer, List<DResponseTrainingUnit>> map = new HashMap<>();
        this.topicOutline.forEach(unit -> {
            Integer key = unit.getDayNumber();
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            List<DResponseTrainingUnit> list = map.get(key);
            list.add(unit);
            map.put(key, list);
        });
        return map;
    }

}