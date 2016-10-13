package dubbo_server;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class HighlightTest {
	public static void main(String[] args) throws SolrServerException {
		
		String urlString = "http://localhost:8085/solr/collection1";
		SolrServer solr = new HttpSolrServer(urlString);
		
		// 这里就是得到HttpSolrServer，你也可以自己封装
		SolrQuery query = new SolrQuery();
		query.setQuery("memo:测试");
		query.setHighlight(true); // 开启高亮
		query.setHighlightFragsize(10); // 返回的字符个数
		query.setHighlightRequireFieldMatch(true);
		query.setHighlightSimplePost("<aa>"); // 前缀
		query.setHighlightSimplePre("</aa>"); // 后缀
		query.setParam("hl.fl", "memo"); // 高亮字段
		QueryResponse req = solr.query(query);
		SolrDocumentList list = req.getResults();
		Map<String, Map<String, List<String>>> map = req.getHighlighting();
		for (SolrDocument doc : list) {
			System.out.println(map.get(doc.getFieldValue("id").toString()));
		}
	}
}
