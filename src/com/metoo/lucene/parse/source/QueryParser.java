package com.metoo.lucene.parse.source;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.CharStream;
import org.apache.lucene.queryparser.classic.FastCharStream;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.queryparser.classic.QueryParserConstants;
import org.apache.lucene.queryparser.classic.QueryParserTokenManager;
import org.apache.lucene.queryparser.classic.Token;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class QueryParser extends QueryParserBase implements QueryParserConstants {

	@Override
	public void ReInit(CharStream paramCharStream) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Query TopLevelQuery(String paramString) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}}
