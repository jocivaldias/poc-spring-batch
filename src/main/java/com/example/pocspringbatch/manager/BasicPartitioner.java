package com.example.pocspringbatch.manager;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class BasicPartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        int total = 10000;
        int targetSize = total / gridSize;
        Map<String, ExecutionContext> result = new HashMap<>();
        int start;
        int end;
        for (int i = 0; i < gridSize; i++) {
            start = i * targetSize;
            end = (i == gridSize - 1) ? total : start + targetSize;
            ExecutionContext context = new ExecutionContext();
            context.putInt("start", start);
            context.putInt("end", end);
            result.put("partition" + i, context);
        }
        return result;
    }
}