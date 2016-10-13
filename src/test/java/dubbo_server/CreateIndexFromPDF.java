package dubbo_server;

import java.io.File;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * 从PDF创建索引 <功能详细描述>
 * 
 * @author Administrator
 * @version [版本号, 2014年3月18日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CreateIndexFromPDF {

	public static void main(String[] args) {
		
		String fileName = "H:/lbw/B2B/_架构设计/备忘+资料/Mybatis/MyBatis3-用户指南(附JavaDB实例).pdf";
		String solrId = "MyBatis3-用户指南(附JavaDB实例).pdf";
		try {
			indexFilesSolrCell(fileName, solrId);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 从文件创建索引 <功能详细描述>
	 * 
	 * @param fileName
	 * @param solrId
	 * @see [类、类#方法、类#成员]
	 */
	public static void indexFilesSolrCell(String fileName, String solrId) throws IOException, SolrServerException {
		String urlString = "http://localhost:8085/solr/collection1";
		SolrServer solr = new HttpSolrServer(urlString);
		ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");

		String contentType = "application/pdf";
		up.addFile(new File(fileName), contentType);
		up.setParam("literal.id", solrId);
		up.setParam("uprefix", "attr_");
		up.setParam("fmap.content", "attr_content");
		up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);

		solr.request(up);

		QueryResponse rsp = solr.query(new SolrQuery("*:*"));
		System.out.println(rsp);
	}

}

