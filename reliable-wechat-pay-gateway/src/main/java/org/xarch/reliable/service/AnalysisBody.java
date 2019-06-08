package org.xarch.reliable.service;

import java.util.Map;

public interface AnalysisBody {
	/**
	 * [Json]String -> Map
	 */
	public Map<String, Object> decodeBody_json(String body);

	/**
	 * [Json]Map -> String
	 */
	public String encodeBody_json(Map<String, Object> map);

	/**
	 * [From]Map -> String
	 */
	public String encodeBody_from(Map<String, Object> map);

	/**
	 * [From]String -> Map
	 */
	public Map<String, Object> decodeBody_from(String body);
}
