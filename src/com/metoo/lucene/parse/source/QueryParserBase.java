package com.metoo.lucene.parse.source;

import java.io.StringReader;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.queryparser.classic.CharStream;
import org.apache.lucene.queryparser.classic.FastCharStream;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.TokenMgrError;
import org.apache.lucene.queryparser.flexible.standard.CommonQueryParserConfiguration;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.MultiTermQuery.RewriteMethod;
import org.apache.lucene.util.QueryBuilder;

public abstract class QueryParserBase extends QueryBuilder
   implements CommonQueryParserConfiguration{
	
	public QueryParserBase(Analyzer analyzer) {
		super(null);
		// TODO Auto-generated constructor stub
	}

	protected String field;
	
	 public abstract void ReInit(CharStream paramCharStream);
	 
	 public abstract Query TopLevelQuery(String paramString)
		     throws ParseException;
	 
	  public Query parse(String query)
			  /*     */     throws ParseException
			  /*     */   {
			  /* 120 */     ReInit(new FastCharStream(new StringReader(query)));
			  /*     */     try
			  /*     */     {
			  /* 123 */       Query res = TopLevelQuery(field);
			  /* 124 */       return res != null ? res : newBooleanQuery(false);
			  /*     */     }
			  /*     */     catch (ParseException tme)
			  /*     */     {
			  /* 128 */       ParseException e = new ParseException("Cannot parse '" + query + "': " + tme.getMessage());
			  /* 129 */       e.initCause(tme);
			  /* 130 */       throw e;
			  /*     */     }
			  /*     */     catch (TokenMgrError tme) {
			  /* 133 */       ParseException e = new ParseException("Cannot parse '" + query + "': " + tme.getMessage());
			  /* 134 */       e.initCause(tme);
			  /* 135 */       throw e;
			  /*     */     }
			  /*     */     catch (BooleanQuery.TooManyClauses tmc) {
			  /* 138 */       ParseException e = new ParseException("Cannot parse '" + query + "': too many boolean clauses");
			  /* 139 */       e.initCause(tmc);
			  /* 140 */       throw e;
			  /*     */     }
			  /*     */   }
}
