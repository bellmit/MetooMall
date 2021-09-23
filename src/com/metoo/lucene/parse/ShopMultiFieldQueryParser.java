package com.metoo.lucene.parse;

import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class ShopMultiFieldQueryParser extends MultiFieldQueryParser {

	public ShopMultiFieldQueryParser(Version matchVersion, String[] fields, Analyzer analyzer) {
		super(matchVersion, fields, analyzer);
		// TODO Auto-generated constructor stub
	}

	@Deprecated
	public static Query parse(Version matchVersion, String[] queries, String[] fields, BooleanClause.Occur[] flags,
			Analyzer analyzer) throws ParseException {
		if ((queries.length != fields.length) || (queries.length != flags.length))
			throw new IllegalArgumentException("queries, fields, and flags array have have different length");
		BooleanQuery bQuery = new BooleanQuery();
		for (int i = 0; i < fields.length; i++) {
			QueryParser qp = new QueryParser(matchVersion, fields[i], analyzer);
			Query q = qp.parse(queries[i]);
			if ((q != null) && ((!(q instanceof BooleanQuery)) || (((BooleanQuery) q).getClauses().length > 0))) {
				bQuery.add(q, flags[i]);
			}
		}
		return bQuery;
	}
	
	

}
