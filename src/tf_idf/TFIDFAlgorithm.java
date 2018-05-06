package tf_idf;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class TFIDFAlgorithm {
    /**
     * 根据文件路径，文件中存放的100个网址的 url，获取 url 路径列表
     *
     * @param path 本地文件路径
     * @return 路径列表
     */
    public List<String> readUrlFromText(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        List<String> urls = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                urls.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }

    /**
     * 利用 Jsoup 工具，根据网址获取网页文本
     *
     * @param url 网址
     * @return 网页文本
     */
    public String getTextFromUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        String text = "";
        try {
            Document document = Jsoup.connect(url).get();
            text = document.text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.replace(" ", "");
    }

    /**
     * 运用 ansj 给文本分词
     *
     * @param text 文本内容
     * @return 分词结果
     */
    public List<Term> parse(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        List<Term> terms = FilterModifWord.modifResult(ToAnalysis.parse(text));
        if (terms == null || terms.size() == 0) {
            return null;
        }
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).getName().contains("．")||terms.get(i).getName().contains(" ")||"null".equals(terms.get(i).getNatrue().natureStr) || terms.get(i).getNatrue().natureStr.startsWith("w")) {
                terms.remove(i);
            }
        }
        return terms;
    }

    /**
     * 计算一篇文章分词后除去标点符号后词的总数
     *
     * @param terms 分词后的集合
     * @return 一篇文章分词后除去标点符号后词的总数
     */
    private Integer countWord(List<Term> terms) {
        if (terms == null || terms.size() == 0) {
            return null;
        }
        return terms.size();
    }

    /**
     * 计算词频 IF
     *
     * @param word  词
     * @param terms 分词结果集合
     * @return IF
     */
    public double computeTF(String word, List<Term> terms) {
        if (StringUtils.isBlank(word)) {
            return 0.0;
        }
        int count = 0;
        for (Term term : terms) {
            if (term.getName().equals(word)) {
                count += 1;
            }
        }
        return (double) count / countWord(terms);
    }

    /**
     * 统计词语的逆文档频率 IDF
     *
     * @param word IDF
     */
    public double computeIDF(String word,List<Article_train> ls) {
        if (StringUtils.isBlank(word)) {
            return 0.0;
        }
        int count = 1;
        for (Article_train at : ls) {
            String text = at.getContent();
            if (text.contains(word)) {
                count += 1;
            }
        }
        return Math.log10((double) ls.size() / count);
    }

    public List<KnnRow> getFeatureRowFromDb(int trainSetNumPerType){

        List<KnnRow> rowList = new ArrayList<>();
        Connection conn = null;
        Map<String,Integer> typeNumber = new HashMap<>();

        typeNumber.put("culture",0);
        typeNumber.put("education",0);
        typeNumber.put("entertainment",0);
        typeNumber.put("film",0);
        typeNumber.put("game",0);
        typeNumber.put("science",0);
        typeNumber.put("society",0);
        typeNumber.put("sports",0);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommend?useUnicode=true&amp;characterEncoding=utf-8",
                    "root","");
            String sql = "select id,type from article_train_matrix GROUP BY id";
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()){
                    KnnRow knnRow = new KnnRow();
                    knnRow.setId(rs.getInt("id"));
                    knnRow.setType(rs.getString("type"));
                    Map<String,Double> features = new HashMap<>();
                    String sql0 = "select feature,tf_idf from article_train_matrix where id = ?";
                    PreparedStatement ps0 = null;
                    ResultSet rs0 = null;
                    try {
                        ps0 = conn.prepareStatement(sql0);
                        ps0.setInt(1,knnRow.getId());
                        rs0 = ps0.executeQuery();
                        while(rs0.next()){
                            features.put(rs0.getString("feature"),rs0.getDouble("tf_idf"));
                        }
                        knnRow.setFeatures(features);
                    }catch(SQLException e){

                    }
                    typeNumber.put(knnRow.getType(),typeNumber.get(knnRow.getType())+1);
                    if(typeNumber.get(knnRow.getType())>trainSetNumPerType){
                        continue;
                    }
                    rowList.add(knnRow);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rowList;
    }


    public List<Article_train> getTrainSetList(int trainSetNumPerType){

        List<Article_train> rowList = new ArrayList<>();
        Connection conn = null;

        Map<String,Integer> typeNumber = new HashMap<>();

        typeNumber.put("culture",0);
        typeNumber.put("education",0);
        typeNumber.put("entertainment",0);
        typeNumber.put("film",0);
        typeNumber.put("game",0);
        typeNumber.put("science",0);
        typeNumber.put("society",0);
        typeNumber.put("sports",0);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommend?useUnicode=true&amp;characterEncoding=utf-8",
                    "root","");
            String sql = "select * from article_train;";
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()){
                    Article_train at = new Article_train();
                    at.setId(rs.getInt("id"));
                    at.setType(rs.getString("type"));
                    at.setContent(rs.getString("content"));

                    typeNumber.put(at.getType(),typeNumber.get(at.getType())+1);

                    if(typeNumber.get(at.getType())>trainSetNumPerType){
                        continue;
                    }
                    rowList.add(at);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rowList;
    }

    @Test
    public void test0() {
        /*InsertTextIntoDb("G:\\IdeaProjects\\TF_IDF\\web\\data_train\\culture.txt","culture");
        InsertTextIntoDb("G:\\IdeaProjects\\TF_IDF\\web\\data_train\\education.txt","education");
        InsertTextIntoDb("G:\\IdeaProjects\\TF_IDF\\web\\data_train\\entertainment.txt","entertainment");
        InsertTextIntoDb("G:\\IdeaProjects\\TF_IDF\\web\\data_train\\film.txt","film");
        InsertTextIntoDb("G:\\IdeaProjects\\TF_IDF\\web\\data_train\\game.txt","game");
        InsertTextIntoDb("G:\\IdeaProjects\\TF_IDF\\web\\data_train\\science.txt","science");
        InsertTextIntoDb("G:\\IdeaProjects\\TF_IDF\\web\\data_train\\society.txt","society");
        InsertTextIntoDb("G:\\IdeaProjects\\TF_IDF\\web\\data_train\\sports.txt","sports");*/

        //List<Article_train> trainsSet = getMinTextFromDb();
        // System.out.println(trainsSet.size());





        String content =  getTextFromUrl("http://edu.people.com.cn/GB//n1/2017/0802/c1053-29444101.html");
        List<Term> terms = parse(content);
        System.out.println(terms);

        //ClassifyByKnn(content,10,20,96987);

       /* List<Article_train> list = getTrainSetList(10);
        for(Article_train at:list){
            System.out.println(at.getId()+":"+at.getType());
        }*/

        /*HtmlCanvas html = new HtmlCanvas();
        try {
            html
                    .html()
                    .head()
                    .title()

                    ._title()
                    ._head()
                    .body()
                    .h1().content("renderSnake it")
                    ._body()
                    ._html();

            System.out.println( html.toHtml());
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }

    public void ClassifyByKnn(String content,int k,int trainSetNum,int featuresNum){
        Long start;
        Long end;
        start = System.currentTimeMillis();
        String type = "";
        List<Term> terms = parse(content);
        List<TrainData> ls = getTrainDataFromDb();
        Set<String> features = new HashSet<>();
        List<Article_train> trainsSet = getMinTextFromDb().subList(0,30);

        for(TrainData td:ls.subList(0,featuresNum)){
            features.add(td.getFeature());
        }
        ls.clear();
        //System.gc();
        int n=1;
        List<Article_train> rowList = getTrainSetList(trainSetNum);
        Map<Integer,Double> distances = new HashMap<>();
        Map<Integer,String> idToType = new HashMap<>();
        Map<String,Double>testSetTfidf = new HashMap<>();
        for(String fea:features){
            double tf_idf = computeTFIDF(terms,fea,trainsSet);
            testSetTfidf.put(fea,tf_idf);
        }
        for(Article_train at:rowList){
            //System.out.println(knnRow.getId()+":"+knnRow.getType());
            List<Term> trainSetTerms = parse(at.getContent());
            double dis = 0.0;
            double disTemp = 0.0;
            for(String fea:features){

                double trainSetTfidf = computeTFIDF(trainSetTerms,fea,trainsSet);
                disTemp = testSetTfidf.get(fea) - trainSetTfidf;
                dis+=Math.pow(disTemp,2.0);
                System.out.println(n+":"+fea+":"+disTemp);
            }
            n++;
            dis = Math.sqrt(dis);
            distances.put(at.getId(),dis);
            idToType.put(at.getId(),at.getType());
        }

        List<Map.Entry<Integer,Double>> list = new ArrayList<>(distances.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                double result = o2.getValue() - o1.getValue();
                if (result > 0) {
                    return -1;
                }
                else if (result == 0) {
                    return 0;
                }
                else{
                    return 1;
                }
            }
        });
       // System.out.println("list:"+list.size()+"all: "+rowList.size());
        List<Map.Entry<Integer,Double>> topK = list.subList(0,k);

        Map<String,Integer> typeNumber = new HashMap<>();
        typeNumber.put("culture",0);
        typeNumber.put("education",0);
        typeNumber.put("entertainment",0);
        typeNumber.put("film",0);
        typeNumber.put("game",0);
        typeNumber.put("science",0);
        typeNumber.put("society",0);
        typeNumber.put("sports",0);


        for(Map.Entry<Integer,Double> m:topK){
            typeNumber.put(idToType.get(m.getKey()),typeNumber.get(idToType.get(m.getKey()))+1);
        }
        int max = -1;
        for(Map.Entry<String,Integer> m : typeNumber.entrySet()){
            System.out.println(m.getKey()+":"+m.getValue());
            if(m.getValue()>max){
                max = m.getValue();
                type = m.getKey();
            }
        }

        System.out.println(type);

        end = System.currentTimeMillis();

        System.out.println("usetime: "+(end-start)/1000);

        return ;
    }

    //构造kNN矩阵

    public void insertTrainMatrixIntoDb(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommend?useUnicode=true&amp;characterEncoding=utf-8",
                    "root","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<TrainData> ls = getTrainDataFromDb();
        Set<String> features = new HashSet<>();
        Long start;
        Long end;
        //构造特征
        for(TrainData td:ls){
            features.add(td.getFeature());
        }
        ls.clear();
        //每一行为一个训练集
        List<Article_train> trainsSet = getMinTextFromDb();
        //插入数据
        start = System.currentTimeMillis();
        int n=0;
        for(Article_train at:trainsSet.subList(0,trainsSet.size()-1)){
            List<Term> terms = parse(at.getContent());
            for(String fea:features){
                String sql = "insert into article_train_matrix values(?,?,?,?)";
                PreparedStatement ps = null;
                try {
                    ps = conn.prepareStatement(sql);
                    double tf_idf = computeTFIDF(terms,fea,trainsSet);
                    ps.setInt(1,at.getId());
                    ps.setString(2,fea);
                    ps.setDouble(3,tf_idf);
                    ps.setString(4,at.getType());
                    if(tf_idf==0.0){
                        continue;
                    }
                    ps.execute();
                    System.out.println(n+":"+fea+":"+tf_idf+":"+terms.size());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            terms.clear();
            n++;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.println(end-start);
    }

    public void insertArticleFeaturesIntoDb()
    {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommend?useUnicode=true&amp;characterEncoding=utf-8",
                    "root","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Article_train> ls = getTextFromDb();
        for(Article_train item : ls.subList(8667,10673)){

            List<Map.Entry<String,Double>> features = Top10Keywords(item,ls.subList(0,2000));

            for(Map.Entry<String,Double> map : features){
                String sql = "insert into article_train_features values(?,?,?,?)";
                PreparedStatement ps = null;
                try {
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1,item.getId());
                    ps.setString(2,map.getKey());
                    ps.setDouble(3,map.getValue());
                    ps.setString(4,item.getType());
                    ps.execute();
                    System.out.println(item.getId()+":"+map.getKey()+":"+map.getValue()+":"+item.getType());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void InsertTextIntoDb(String path,String type){
        if (StringUtils.isBlank(path)) {
            return;
        }
        List<String> urls = readUrlFromText(path);
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommend?useUnicode=true&amp;characterEncoding=utf-8",
                    "root","");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String url : urls) {
            String text = getTextFromUrl(url);
            String sql = "insert into article_train (content, type) values(?,?)";
            PreparedStatement ps = null;
            System.out.println(text);
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1,text);
                ps.setString(2,type);
                ps.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(type+" : done");

    }

    public List<TrainData> getTrainDataFromDb(){
        List<TrainData> ls = new ArrayList<>();
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommend?useUnicode=true&amp;characterEncoding=utf-8",
                    "root","");
            String sql = "select * from article_train_features";
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()){
                    TrainData td = new TrainData();
                    td.setId(rs.getInt("id"));
                    td.setFeature(rs.getString("feature"));
                    td.setTf_idf(rs.getDouble("tf_idf"));
                    td.setType(rs.getString("type"));
                    ls.add(td);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ls;
    }


    public List<Article_train> getMinTextFromDb(){
        List<Article_train> ls = new ArrayList<>();
        Connection conn = null;
        int num = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommend?useUnicode=true&amp;characterEncoding=utf-8",
                    "root","");
            List<String> types = new ArrayList<>();
            types.add("culture");
            types.add("education");
            types.add("entertainment");
            types.add("film");
            types.add("game");
            types.add("science");
            types.add("society");
            types.add("sports");
            for(String type:types){
                try {
                    String sql = "select * from article_train where type = ?";
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    ps = conn.prepareStatement(sql);
                    ps.setString(1,type);
                    rs = ps.executeQuery();
                    while(rs.next()){
                        Article_train at = new Article_train();
                        at.setId(rs.getInt("id"));
                        at.setContent(rs.getString("content"));
                        at.setType(rs.getString("type"));
                        ls.add(at);
                        num++;
                        if(num>=100){
                            num = 0;
                            break;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ls;
    }


    public List<Article_train> getTextFromDb(){
        List<Article_train> ls = new ArrayList<>();
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommend?useUnicode=true&amp;characterEncoding=utf-8",
                    "root","");
            String sql = "select * from article_train";
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()){
                    Article_train at = new Article_train();
                    at.setId(rs.getInt("id"));
                    at.setContent(rs.getString("content"));
                    at.setType(rs.getString("type"));
                    ls.add(at);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ls;
    }

    /**
     * 计算词频-逆文档频率 TF—IDF
     *
     * @param terms    分词结果集合
     * @param word     词
     * @return TF—IDF
     */

    public Double computeTFIDF(List<Term> terms, String word,List<Article_train> ls) {
        return computeTF(word, terms) * computeIDF(word,ls);
    }


    public List<Map.Entry<String,Double>> Top10Keywords(Article_train item,List<Article_train> ls)
    {
        long begin = System.currentTimeMillis();
        TFIDFAlgorithm tfidfAlgorithm = new TFIDFAlgorithm();
        List<Term> terms = tfidfAlgorithm.parse(item.getContent());
        HashMap<String,Double> tf_idfs = new HashMap<String, Double>();
       // System.out.println(terms);
       // System.out.println("size:"+terms.size());
        for(int i=0;i<terms.size();i++)
        {
            double tf_idf = tfidfAlgorithm.computeTFIDF(terms,terms.get(i).getName(),ls);
            tf_idfs.put(terms.get(i).getName(),tf_idf);
           // System.out.println(i+" : "+terms.size()+" : "+terms.get(i).getName()+":"+tf_idf);
        }

        List<Map.Entry<String,Double>> list = new ArrayList<>(tf_idfs.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                double result = o2.getValue() - o1.getValue();
                if (result > 0) {
                    return 1;
                }
                else if (result == 0) {
                    return 0;
                }
                else{
                    return -1;
                }
            }
        });

       /* for(Map.Entry<String,Double> map:list.subList(0,10)){
            System.out.println(map.getKey()+":"+map.getValue());
        }
        long end = System.currentTimeMillis();
        System.out.println("usetime: "+(end-begin)/1000);*/

        return list.subList(0,10);
    }

    @Test
    public void Test()
    {
        TFIDFAlgorithm tfidfAlgorithm = new TFIDFAlgorithm();
        String filePath = "G:\\IdeaProjects\\TF_IDF\\web\\data_train\\culture.txt";
        String url = "http://news.sina.com.cn/c/xl/2018-04-28/doc-ifzvpatq7329841.shtml";
        String word = "语言";
        List<Term> terms = tfidfAlgorithm.parse(tfidfAlgorithm.getTextFromUrl(url));
        List<Article_train> ls = getTextFromDb();
        for(Term t:terms){
            System.out.println(t.getName());
        }
        System.out.println("[【" + word + "】词频 ] " + tfidfAlgorithm.computeTF(word, terms));
        System.out.println("[【" + word + "】逆文档频率 ] " + tfidfAlgorithm.computeIDF(word,ls));
        System.out.println("[【" + word + "】词频-逆文档频率 ] "+tfidfAlgorithm.computeTFIDF(terms,word,ls));
    }

}
