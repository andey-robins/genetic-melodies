package genetics.fitness;

import genetics.Individual;
import genetics.interfaces.IFitnessFunction;
import midi.Note;

import java.util.HashMap;
import java.util.Map;

public class VarietyFitness implements IFitnessFunction {
    public VarietyFitness() {}

    @Override
    public double score(Individual i) {
        Map<Integer, Integer> map = new HashMap<>();
        int count;
        int noteCount = i.size();

        for (Note note : i.getMelody()) {
            Integer pitch = note.getPitch().hashCode();
            if (map.containsKey(pitch)) {
                count = map.get(pitch);
                map.put(pitch, count+1);
            }
            else{
                map.put(pitch, 1);
            }
        }
        double max = 0;
        for (Integer c : map.values()) {
            max = Math.max(((double) c), max);
        }
        return (1 - max/noteCount);
    }
}
