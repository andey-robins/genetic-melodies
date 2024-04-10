package genetics.fitness;

import genetics.Individual;
import genetics.interfaces.IFitnessFunction;
import midi.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class SmoothFitness implements IFitnessFunction {

    public SmoothFitness() {}

    @Override
    public double score(Individual i) {

        Note lastNote = null;
        ArrayList<Double> distances = new ArrayList<>();

        // approximate the first derivative of the melody by
        // measuring the distance between notes, ignoring rests
        for (Note note : i.getMelody()) {
            if (lastNote != null) {
                Optional<Integer> lastNotePitch = lastNote.getPitch();
                Optional<Integer> notePitch = note.getPitch();
                
                // Only calculate distance if both notes are not rests
                if (lastNotePitch.isPresent() && notePitch.isPresent()) {
                    distances.add((double) (lastNotePitch.get() - notePitch.get()));
                }
            }
            lastNote = note;
        } 

        // If no distances could be calculated (Ex. one note melody), return 0
        if (distances.size() == 0) 
            return 0;
        
        // calculate the variance of the distances array
        double mean = calculateMean(distances);
        double sumOfSquaredDifferences = 0.0;
        for (double distance : distances) {
            double difference = distance - mean;
            sumOfSquaredDifferences += difference * difference;
        }

        double variance = sumOfSquaredDifferences / distances.size();
        double max = Collections.max(distances);
        double min = Collections.min(distances);
        double maxPossibleVariance = (max - min) * (max - min) / 4;

        // normalize variance to fit between 0 and 1 for fitness evaluation
        return variance / maxPossibleVariance;
    }

    private double calculateMean(ArrayList<Double> distances) {
        double sum = 0;
        for (double distance : distances) {
            sum += distance;
        }
        return sum / distances.size();
    }
}
