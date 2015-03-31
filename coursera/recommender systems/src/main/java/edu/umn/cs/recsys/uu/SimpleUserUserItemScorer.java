package edu.umn.cs.recsys.uu;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.ItemEventDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.history.History;
import org.grouplens.lenskit.data.history.RatingVectorUserHistorySummarizer;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.symbols.Symbol;
import org.grouplens.lenskit.symbols.TypedSymbol;
import org.grouplens.lenskit.vectors.ImmutableSparseVector;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.*;

/**
 * User-user item scorer.
 * @author GroupLens">http://www.grouplens.org">GroupLens Research
 */
public class SimpleUserUserItemScorer extends AbstractItemScorer {
    private final UserEventDAO userDao;
    private final ItemEventDAO itemDao;

    @Inject
    public SimpleUserUserItemScorer(UserEventDAO udao, ItemEventDAO idao) {
        userDao = udao;
        itemDao = idao;
    }

    @Override
    public void score(long user, @Nonnull MutableSparseVector scores) {
        SparseVector userRating = getUserRatingVector(user);

        // TODO Score items for this user using user-user collaborative filtering

        // Get the users who have rated the items

        // This is the loop structure to iterate over items to score
        for (VectorEntry e: scores.fast(VectorEntry.State.EITHER)) {
            long itemId = e.getKey();
            double d = e.getValue();
            double userMeanRating = userRating.mean();

            LongSet possibleNeighbors = itemDao.getUsersForItem(itemId);

            // remove the user from the neighborhood
            possibleNeighbors.remove(user);

            double totalCosSim = 0.0;
            double wgtdScore = 0.0; // weighted score


            HashMap<Long, Double> cosSims = new HashMap<Long,Double>();

            // Calculate the cosine similarity between the current user and all its neighbors
            // And add to the HashMap
            for (long neighbor: possibleNeighbors)  {
                double cosSim = getCosineSimilarity(neighbor, user);
                cosSims.put(neighbor, cosSim);
            }

            // Find the top neighbors - currently set at 30.
            Map<Long, Double> topNeighbors = getTopNeighbors(cosSims);

            // Aggregate the Numerator (ie Weighted Score) and the Denominator
            for (Entry<Long, Double> entry : topNeighbors.entrySet())
            {
                long neighbor = entry.getKey();
                double cosSim = entry.getValue();

                // Get the users ratings.
                SparseVector ngbrRatings = getUserRatingVector(neighbor);

                // Get the neighbors rating for this single item.
                double itemRating = ngbrRatings.get(itemId);

                // Average Score for this neighbor for all the items.
                double avg = ngbrRatings.mean();

                // Weighted Score is calculated as (ItemRating - Average) * CosineSimilarity
                wgtdScore += (itemRating - avg) * cosSim;

                // Accumulate the denominator which is the sum of the absolute values of the cosine similarities.
                totalCosSim += Math.abs(cosSim);
            }

            // Calculate the item Score
            double itemScore = userMeanRating + ( wgtdScore / totalCosSim);

            // Assign the item Score to the Vector
            scores.set(itemId, itemScore);
        }
    }

    /**
     * Get a user's rating vector.
     * @param user The user ID.
     * @return The rating vector.
     */
    private SparseVector getUserRatingVector(long user) {
        UserHistory<Rating> history = userDao.getEventsForUser(user, Rating.class);
        if (history == null) {
            history = History.forUser(user);
        }
        return RatingVectorUserHistorySummarizer.makeRatingVector(history);
    }

    /**
     * Get a user's Mean Normalized rating vector.
     * @param user The user ID.
     * @return The rating vector.
     */
    private SparseVector getNormalizedUserRatingVector(long user) {
        SparseVector userVector = getUserRatingVector(user);
        double avg = userVector.mean();
        MutableSparseVector userNormRatings = userVector.mutableCopy();
        userNormRatings.add(-avg);
        return userNormRatings;
    }

    /**
     * Calcuate the Cosine Similarities between the two users.
     * @param user The user ID.
     * @param otherUser The ID of the other user
     * @return The Cosine Similarity
     */
    private double getCosineSimilarity(long user, long otherUser) {
        SparseVector ngbrNormRating = getNormalizedUserRatingVector(otherUser);
        SparseVector userNormRating = getNormalizedUserRatingVector(user);

        double cosSim =  new CosineVectorSimilarity().similarity(ngbrNormRating, userNormRating);
        return cosSim;
    }


    /**
     * Get the top 30 neigbbors based on the Cosine Similarities.
     * @param map The Map mapping userIds to Cosine Similarities
     * @return The Cosine Similarity
     */
    private static Map<Long, Double>  getTopNeighbors(HashMap<Long,Double> map) {
        int TOP = 30;

        List<Entry<Long, Double>> list = new LinkedList<Entry<Long, Double>>(map.entrySet());

        // Sort the Map based on the Values (Cosine Similarities).
        // By implementing a Custom Comparator.
        Collections.sort(list, new Comparator<Entry<Long, Double>>()
        {
            public int compare(Entry<Long, Double> o1,
                               Entry<Long, Double> o2)
            {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // After sorting
        // Add the Entries to a LinkedHashMap
        // To preserve the order of insertion
        Map<Long, Double> sortedMap = new LinkedHashMap<Long, Double>();
        int i = 0;
        for (Entry<Long, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
            i++;
            // Restrict the no. of entries to first to TOP (30) entries.
            if (i >= TOP){
                break;
            }
        }

        return sortedMap;
    }
}