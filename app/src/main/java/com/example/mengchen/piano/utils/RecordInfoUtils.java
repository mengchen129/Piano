package com.example.mengchen.piano.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordInfoUtils {

    public static String toJSON(Map<Long, List<RecordInfo>> map) {
        List<RecordInfoObj> list = new ArrayList<>();

        for (Map.Entry<Long, List<RecordInfo>> entry : map.entrySet()) {
            long time = entry.getKey();
            List<RecordInfo> infoList = entry.getValue();

            RecordInfoObj infoObj = new RecordInfoObj();
            infoObj.time = time;

            List<Integer> tunesList = new ArrayList<>();
            for (RecordInfo info : infoList) {
                tunesList.add(info.getTunes());
                //infoObj.tunes.add(info.getTunes());
            }
            infoObj.tunes = tunesList.toArray(new Integer[tunesList.size()]);

            list.add(infoObj);
        }

        return JSONHelper.toJSON(list);
    }
}

