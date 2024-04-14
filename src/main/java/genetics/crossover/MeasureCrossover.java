

public class MeasureCrossover implements ICrossoverMechanism {
    private static final Random RNG = new Random();

    public MeasureCrossover() {}

    @Override
    public Individual[] crossover(Individual p1, Individual p2) {
        ArrayList<Note> m1 = new ArrayList<>();
        ArrayList<Note> m2 = new ArrayList<>();

        // determine bounds since there is no guarantee about melody lengths
        boolean p1Longer = false;
        int shortestIndividual = p1.size();
        if (p2.size() < shortestIndividual) {
            shortestIndividual = p2.size();
            p1Longer = true;
        }

        boolean fullMeasures = false;
        if (shortestIndividual % 4 == 0) {
            fullMeasures = true;
        } else {
            int tail = shortestIndividual % 4;
            shortestIndividual = shortestIndividual - tail;
        }

        // crossover the overlapping parts of the two melodies measure by measure
        for (int i = 0; i < shortestIndividual; i+=4) {
            for (int j = i; j < i+4; j++) {
                Note parentOneNote = p1.getMelody().get(j);
                Note parentTwoNote = p2.getMelody().get(j);

                if (RNG.nextBoolean()) {
                    m1.add(parentOneNote);
                    m2.add(parentTwoNote);
                } else {
                    m2.add(parentOneNote);
                    m1.add(parentTwoNote);
                }
            }   
        }

        ArrayList<Note> overflow = new ArrayList<>();
        ArrayList<Note> overflow2 = new ArrayList<>();
        if (p1.size() != p2.size() && p1Longer) {
            for (int i = shortestIndividual; i < p1.size(); i++) {
                overflow.add(p1.getMelody().get(i));
            }
            if (!fullMeasures) {
                for (int i = shortestIndividual; i < tail; i++) {
                    overflow2.add(p2.getMelody().get(i));
                }
            }
        } else if (p1.size() != p2.size()) {
            for (int i = shortestIndividual; i < p2.size(); i++) {
                overflow.add(p2.getMelody().get(i));
            }
            if (!fullMeasures) {
                for (int i = shortestIndividual; i < tail; i++) {
                    overflow2.add(p1.getMelody().get(i));
                }
            }
        }

        if (RNG.nextBoolean()) {
            m1.addAll(overflow);
            m2.addAll(overflow2);
        } else {
            m2.addAll(overflow);
            m1.addAll(overflow2);
        }
        
        return new Individual[]{new Individual(m1), new Individual(m2)};
    }
}