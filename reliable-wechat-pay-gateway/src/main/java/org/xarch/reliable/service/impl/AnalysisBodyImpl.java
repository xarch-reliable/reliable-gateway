package org.xarch.reliable.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.xarch.reliable.service.AnalysisBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AnalysisBodyImpl implements AnalysisBody {

	private static ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> decodeBody_json(String body) {
		try {
			return mapper.readValue(body, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String encodeBody_json(Map<String, Object> map) {
		try {
			return mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String encodeBody_from(Map<String, Object> map) {
		try {
			return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, Object> decodeBody_from(String body) {
		try {
			return Arrays.stream(body.split("&")).map(s -> s.split("="))
					.collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
