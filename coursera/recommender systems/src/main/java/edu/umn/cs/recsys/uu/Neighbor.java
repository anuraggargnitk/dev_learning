package edu.umn.cs.recsys.uu;
import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.ItemEventDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.history.History;
import org.grouplens.lenskit.data.history.RatingVectorUserHistorySummarizer;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
/**
 * Created with IntelliJ IDEA.
 * Neighbor: alejandrofernandez
 * Date: 16/10/13
 * Time: 23:44
 * This class helped me encapsulate everything related to each neighbor
 *
 */
public class Neighbor implements Comparable<Neighbor> {
    long id;
    private SparseVector userVector;
    private double similarity;
    private double mean; //To lazy initialize it


    public Neighbor(long id, SparseVector myVector, SparseVector theOtherUsersVector) {
        this.id = id;
        this.setUserVector(myVector);
        this.mean = myVector.mean();

        MutableSparseVector myMeanCenteredVector = myVector.mutableCopy();
        myMeanCenteredVector.add(- getMean());

        MutableSparseVector theOtherUsersMeanCenteredVector = theOtherUsersVector.mutableCopy();
        theOtherUsersMeanCenteredVector.add(- theOtherUsersMeanCenteredVector.mean());

        CosineVectorSimilarity vectorSimilarity = new CosineVectorSimilarity();
        similarity = vectorSimilarity.similarity(theOtherUsersMeanCenteredVector, myMeanCenteredVector);



    }


    /**
     * Helps sorting neighbors by cosine similarity (higher values come first)
     * @param neighbor
     * @return
     */
    @Override
    public int compareTo(Neighbor neighbor) {

        return new Double(neighbor.getSimilarity()).compareTo(new Double(this.getSimilarity()));

    }

    public SparseVector getUserVector() {
        return userVector;
    }

    public void setUserVector(SparseVector userVector) {
        this.userVector = userVector;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public double getMean() {

        return mean;
    }

    public double getRatingFor(long movie) {
        return  userVector.get(movie);
    }

    public double getMeanCenteredRatingFor(long movie) {
        return getRatingFor(movie) - getMean();
    }
}
