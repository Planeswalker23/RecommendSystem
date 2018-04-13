package recommend;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLBooleanPrefJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.AllSimilarItemsCandidateItemsStrategy;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.jdbc.MySQLJDBCInMemoryItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class Similarity {
    public static MysqlDataSource dataSource = new MysqlDataSource();
    public static MySQLBooleanPrefJDBCDataModel dataModelTemp;
    public static FastByIDMap<FastIDSet> userData;
    public static DataModel dataModel;
    public static ItemSimilarity similarity;

    public static void init() throws Exception
    {
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("admin");
        dataSource.setDatabaseName("recommend");
        dataModelTemp = new MySQLBooleanPrefJDBCDataModel(dataSource);
        userData = dataModelTemp.exportWithIDsOnly();
        dataModel = new GenericBooleanPrefDataModel(userData);
        similarity = new LogLikelihoodSimilarity(dataModel);
        //DB_io.deleteSimilarityIntoDb();
    }

    private static void computeSimilarityMatrix(ItemSimilarity similarity, final long[] newsArray) {
        for (final long leftId : newsArray) {
            try {
                final List<Map.Entry<Long, Double>> similarityList = Lists.newArrayList();
                for (long rightId : newsArray) {
                    if (leftId < rightId) {
                        double sim = similarity.itemSimilarity(leftId, rightId);
                        if (!Double.isNaN(sim) && sim > 0.01) {
                            similarityList.add(Maps.immutableEntry(rightId, sim));
                        }
                    }
                }
                Collections.sort(similarityList, new Comparator<Map.Entry<Long, Double>>() {
                    @Override
                    public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
                        return o1.getValue() < o2.getValue() ? 1 : 0;
                    }
                });
                int maxSize = (similarityList.size() < 500) ? similarityList.size() : 500;
                DB_io.saveSimilarityIntoDb(leftId, similarityList.subList(0, maxSize));

            } catch (TasteException e) {
                Throwables.propagate(e);
            }
        }
    }

    public static List<RecommendedItem> recommender(int userid,int howmany){
        long[] items = DB_io.getItemIdFromDB();
        List<RecommendedItem> rm = null;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
       // computeSimilarityMatrix(similarity,items);

        DataModel dataModel = new MySQLBooleanPrefJDBCDataModel(dataSource);
        ItemSimilarity similarity = new MySQLJDBCInMemoryItemSimilarity(dataSource);
        AllSimilarItemsCandidateItemsStrategy candidateItemsStrategy = new AllSimilarItemsCandidateItemsStrategy(similarity);
        ItemBasedRecommender recommender =  new GenericItemBasedRecommender(dataModel,similarity,candidateItemsStrategy,candidateItemsStrategy);

        try {
            rm = recommender.recommend(userid,howmany);
           // System.out.println(rm);
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return rm;
    }



}
