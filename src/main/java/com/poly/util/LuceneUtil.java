package com.poly.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.poly.redis.RedisDao;

public class LuceneUtil {
	/**
	 * 
	 * @param map
	 * @param state
	 *            1-修改帖子，2-自增帖子 flag 1-全部帖子 2-外部帖子
	 * @throws IOException
	 */
	public static void insertorUpdateIndex(Map<String, String> map, int state,
			int flag) throws IOException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
		Directory directory = FSDirectory.open(new File("/lucene/index"));
		// 配置索引
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_9,
				analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);

		try {
			if (state == 1) {
				iwriter.deleteDocuments(new Term("id", map.get("id")));
			}
			Document doc = new Document();
			doc.add(new Field("title", map.get("title"), TextField.TYPE_STORED));
			doc.add(new Field("userid", map.get("userid"),
					TextField.TYPE_STORED));
			doc.add(new Field("plateid", map.get("plateid"),
					TextField.TYPE_STORED));
			doc.add(new Field("id", map.get("id"), TextField.TYPE_STORED));
			doc.add(new Field("addtime", map.get("addtime"),
					TextField.TYPE_STORED)); // addtime是时间戳
			doc.add(new Field("flag", String.valueOf(flag), Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
			iwriter.addDocument(doc);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			iwriter.close();
		}
	}

	/**
	 * 
	 * @param str
	 * @param page
	 * @param flag
	 *            1-全部帖子 2-外部帖子
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, String>> searchIndex(String str,
			Integer page, int flag) throws IOException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
		Directory directory = FSDirectory.open(new File("/lucene/index"));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		try {
			QueryParser parser = new QueryParser(Version.LUCENE_4_9, "flag",
					analyzer);
			QueryParser parser2 = new QueryParser(Version.LUCENE_4_9, "title",
					analyzer);
			BooleanQuery query = new BooleanQuery();
			Query query1 = parser.parse(String.valueOf(flag));
			Query query2 = parser2.parse(str);
			query.add(query1, Occur.MUST);
			query.add(query2, Occur.MUST);
			Sort sort = new Sort(new SortField("title", SortField.Type.SCORE),
					new SortField("addtime", SortField.Type.DOUBLE, true));
			TopDocs docs = isearcher.search(query, 100000, sort);// 查找
			ScoreDoc[] hits = docs.scoreDocs;
			int pages = 0;
			int p = hits.length;
			if (p > 0) {
				if (p % 10 == 0) {
					pages = p / 10;
				} else {
					pages = p / 10 + 1;
				}
				if (page >= pages) {
					page = pages;
				}
				int start = (page - 1) * 10;
				int end = start + 10 - 1;
				if (end >= p) {
					end = p - 1;
				}
				SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
						"<span style='color:red'>", "</span>");
				Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
						new QueryScorer(query));
				// 高亮htmlFormatter对象
				// 设置高亮附近的字数
				highlighter.setTextFragmenter(new SimpleFragmenter(100));

				// assertEquals(1, hits.length);
				System.out.println("Searched " + hits.length + " documents.");
				// Iteratethrough the results:
				for (int i = start; i <= end; i++) {
					Document hitDoc = isearcher.doc(hits[i].doc);
					String value = hitDoc.get("title");
					TokenStream tokenStream = analyzer.tokenStream(value,
							new StringReader(value));
					String str1 = highlighter.getBestFragment(tokenStream,
							value);
					Map<String, String> map = new HashMap<String, String>();
					System.out.println(hitDoc.get("id"));
					map.put("id", hitDoc.get("id"));
					map.put("title", str1);
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					
					double d1= Double.parseDouble(hitDoc.get("addtime"));
					Double a=d1;
					Long time=a.longValue();
					String d = format.format(time);
					map.put("addtime", d);
					map.put("userid", hitDoc.get("userid"));
					map.put("username",
							RedisDao.getusernamebyid(hitDoc.get("userid")));
					map.put("platename",
							RedisDao.getplatenamebyid(hitDoc.get("plateid")));
					list.add(map);
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("pages", String.valueOf(pages));
				list.add(map);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			ireader.close();
			directory.close();
		}
		return list;
	}

	public static void deleteIndex(String id) throws IOException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
		Directory directory = FSDirectory.open(new File("/lucene/index"));
		// 配置索引
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_9,
				analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);
		try {
			// 这里，将5篇文档filedname信息和content信息存入索引
			iwriter.deleteDocuments(new Term("id", id));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			iwriter.close();
		}
	}
}
