package stats;

import midi.Note;

import java.util.ArrayList;
import java.util.Comparator;

public class JaccardSimilarity {

    public static Comparator<Note> noteComparator = Comparator.comparing(
            (Note note) -> note.getPitch().orElse(0),
            Comparator.nullsLast(Comparator.naturalOrder())
    ).thenComparing(Note::getLength);

    /**
     * NoteSimilarity will calculate the jaccard similarity coefficient for two melodies
     * where 0 is completely disjoint and 1 is completely similar. The note and rhythm
     * must be the same to be considered similar, though the order need not be the same.
     * Note this similarity looks without respect to order
     * @param m1 melody one
     * @param m2 melody two
     * @return J(m1, m2)
     */
    public static double NoteSimilarity(ArrayList<Note> m1, ArrayList<Note> m2) {
        m1.sort(noteComparator);
        m2.sort(noteComparator);

        int intersecting = 0;

        int shortestMelody = Math.min(m1.size(), m2.size());

        // we use a two pointer approach to traverse the lists and identify elements
        // which are similar at two points
        int m1Idx = 0;
        int m2Idx = 0;

        while (m1Idx < shortestMelody && m2Idx < shortestMelody) {
            Note one = m1.get(m1Idx);
            Note two = m2.get(m2Idx);

            if (one.getPitch() == two.getPitch()) {
                if (one.getLength() == two.getLength() && one.getLength() == two.getLength()) {
                    intersecting++;
                } else {
                    if (one.getLength() < two.getLength()) {
                        m1Idx++;
                    } else {
                        m2Idx++;
                    }
                }
            } else {
                if (one.getPitch().orElse(0) < two.getPitch().orElse(0)) {
                    m1Idx++;
                } else {
                    m2Idx++;
                }
            }
        }


        return ((double) intersecting / (m1.size() + m2.size() - (double) intersecting));
    }


    /**
     * FuzzyNoteSimilarity will consider two notes as similar if they have the same note
     * -or- rhythm, which is different from NoteSimilarity which requires both
     * Note this similarity looks without respect to order
     * @param m1 melody one
     * @param m2 melody two
     * @return J(m1, m2), but without requiring both note and rhythm to match
     */
    public static double FuzzyNoteSimilarity(ArrayList<Note> m1, ArrayList<Note> m2) {
        m1.sort(noteComparator);
        m2.sort(noteComparator);

        int intersecting = 0;
        int diverging = 0;

        int shortestMelody = Math.min(m1.size(), m2.size());

        // we use a two pointer approach to traverse the lists and identify elements
        // which are similar at two points
        int m1Idx = 0;
        int m2Idx = 0;

        while (m1Idx < shortestMelody && m2Idx < shortestMelody) {
            Note one = m1.get(m1Idx);
            Note two = m2.get(m2Idx);

            if (one.getPitch() == two.getPitch()) {
                if (one.getLength() == two.getLength() || one.getLength() == two.getLength()) {
                    intersecting++;
                } else {
                    if (one.getLength() < two.getLength()) {
                        m1Idx++;
                    } else {
                        m2Idx++;
                    }
                }
            } else {
                if (one.getPitch().orElse(0) < two.getPitch().orElse(0)) {
                    m1Idx++;
                } else {
                    m2Idx++;
                }
            }
        }

        return ((double) intersecting / (m1.size() + m2.size() - (double) intersecting));
    }

    /**
     * OrderedNoteSimilarity will calculate the jaccard similarity coefficient *with* respect to order.
     * That means for things to be similar in this sense, they must have the same ordered structure
     * not just a similar set of notes and rhythms
     * @param m1 melody one
     * @param m2 melody two
     * @return J(m1, m2), but without requiring both note and rhythm to match
     */
    public static double OrderedNoteSimilarity(ArrayList<Note> m1, ArrayList<Note> m2) {

        int intersecting = 0;

        int shortestMelody = Math.min(m1.size(), m2.size());

        for (int i = 0; i < shortestMelody; i++) {
            Note one = m1.get(i);
            Note two = m2.get(i);
            if (one.getPitch().orElse(0) == two.getPitch().orElse(0) && one.getLength() == two.getLength()) {
                intersecting++;
            }
        }

        return ((double) intersecting / (m1.size() + m2.size() - (double) intersecting));
    }
}
