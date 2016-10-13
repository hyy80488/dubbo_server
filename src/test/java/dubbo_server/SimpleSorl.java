package dubbo_server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SimpleSorl {
    private String solrUrl;
    private SolrServer client;
    private int num = 10;
//    private String zkUrl;
//    private String collectionName;

    private SolrServer createNewSolrClient() {
        try {
            System.out.println("server address:" + solrUrl);
            SolrServer client = new HttpSolrServer(solrUrl);
//            client.setConnectionTimeout(30000);
//            client.setDefaultMaxConnectionsPerHost(100);
//            client.setMaxTotalConnections(100);
//            client.setSoTimeout(30000);
            return client;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

//    private SolrServer createCouldSolrClient() {
//        CloudSolrClient client = new CloudSolrClient(zkUrl);
//        client.setZkClientTimeout(30000);
//        client.setZkConnectTimeout(50000);
//        client.setDefaultCollection(collectionName);
//        return client;
//    }

//    public void close() {
//        try {
//            client.close();
//        } catch (IOException e) {
//
//            e.printStackTrace();
//        }
//    }

    public SimpleSorl(String solrUrl, int num) {
        this.solrUrl = solrUrl;
        this.client = createNewSolrClient();
        this.num = num;
    }

//    public SimpleSorl(String zkUrl, int num, String collection) {
//        this.zkUrl = zkUrl;
//        this.num = num;
//        collectionName = collection;
//        this.client = createCouldSolrClient();
//    }

    public void createDocs() {
        System.out.println("======================add doc ===================");
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        for (int i = 1; i <= num; i++) {
            SolrInputDocument doc1 = new SolrInputDocument();
            doc1.addField("id", UUID.randomUUID().toString(), 1.0f);
            doc1.addField("name", "bean");
            doc1.addField("equIP_s", "192.168.2.104");
            doc1.addField("level_s", "4");
            doc1.addField("collectPro_s", "ffffffffffffffffffffjajajajajajdddddddddd");
            doc1.addField("sourceType_s", "txt");
            doc1.addField("filePath_s", "E:、apache-tomcat-7.0.69_solr");
            doc1.addField("filename_s", "RUNNING.txt");//            doc1.addField("_route_", "shard1");
            docs.add(doc1);
        }
        try {
            UpdateResponse rsp = client.add(docs);
            System.out.println("Add doc size" + docs.size() + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());

//            UpdateResponse rspcommit = client.commit();
//            System.out.println("commit doc to index" + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());

        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void queryDocs() {
        SolrQuery params = new SolrQuery();
        System.out.println("======================query===================");
        params.set("q", "addparam_s:*");
        params.set("start", 0);
        params.set("rows", 5);
        params.set("sort", "accesstime_s desc");

        try {
            QueryResponse rsp = client.query(params);
            SolrDocumentList docs = rsp.getResults();
            System.out.println("查询内容:" + params);
            System.out.println("文档数量：" + docs.getNumFound());
            System.out.println("查询花费时间:" + rsp.getQTime());

            System.out.println("------query data:------");
            for (SolrDocument doc : docs) {
                // 多值查询
                @SuppressWarnings("unchecked")
                List<String> collectTime = (List<String>) doc.getFieldValue("collectTime");
                String clientmac_s = (String) doc.getFieldValue("clientmac_s");
                System.out.println("collectTime:" + collectTime + "\t clientmac_s:" + clientmac_s);
            }
            System.out.println("-----------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteById(String id) {
        System.out.println("======================deleteById ===================");
        try {
            UpdateResponse rsp = client.deleteById(id);
            client.commit();
            System.out.println("delete id:" + id + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteByQuery(String queryCon) {
        System.out.println("======================deleteByQuery ===================");
//        UpdateResponse rsp;

        try {
            UpdateRequest commit = new UpdateRequest();
            commit.deleteByQuery(queryCon);
            commit.setCommitWithin(5000);
            commit.process(client);
            System.out.println("url:"+commit.getPath()+"\t xml:"+commit.getXML()+" method:"+commit.getMethod());
//            rsp = client.deleteByQuery(queryCon);
//            client.commit();
//            System.out.println("delete query:" + queryCon + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());
        } catch (SolrServerException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void updateField(String id,String fieldName, Object fieldValue) {
        System.out.println("======================updateField ===================");
        HashMap<String, Object> oper = new HashMap<String, Object>();
//        多值更新方法
//        List<String> mulitValues = new ArrayList<String>();
//        mulitValues.add(fieldName);
//        mulitValues.add((String)fieldValue);
        oper.put("set", fieldValue);

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", id);
        doc.addField(fieldName, oper);
        try {
            UpdateResponse rsp = client.add(doc);
            System.out.println("update doc id" + id + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());
            UpdateResponse rspCommit = client.commit();
            System.out.println("commit doc to index" + " result:" + rspCommit.getStatus() + " Qtime:" + rspCommit.getQTime());

        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        String url = "http://localhost:8085/solr/collection1";
//        String zkUrl = "127.0.0.1:2181";
//        PropertyConfigurator.configure("./etc/log4j.properties");
        SimpleSorl ss = new SimpleSorl(url, 2);
//        SimpleSorl sc = new SimpleSorl(zkUrl, 2, "collection1");
        // 添加文档
        ss.createDocs();

        // 删除文档
//        sc.deleteById("00cda454-bd3d-4945-814f-afa7110dcd21");
        ss.deleteByQuery("name:bean");
        
        //更新文档
        ss.updateField("bd67564f-4939-4de1-9a83-3483ebbbbbee", "name", "1233313131313");
        
//        sc.close();
        

        // 查询文档
        ss.queryDocs();
//        ss.close();

    }

}
