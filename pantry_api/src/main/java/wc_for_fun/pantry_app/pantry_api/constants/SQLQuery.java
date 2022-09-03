package wc_for_fun.pantry_app.pantry_api.constants;

import java.util.Iterator;
import java.util.Map;

public class SQLQuery {
	public static final String fromTableWhereID = "FROM :Table T WHERE T.id = :id";

	/**
	 * 
	 * @param attributesList a list, presumably of map keys.
	 * @return a concatenated string in the format "attribute = :attribute,"
	 */
	public static final String generateAttributeMappingString(Iterable<String> attributesKeySet) {
		if (attributesKeySet != null) {
			String queryStringSnippet = "";
			Iterator<String> traverseKeys = attributesKeySet.iterator();
			while (traverseKeys.hasNext()) {
				String key = traverseKeys.next();
				queryStringSnippet = queryStringSnippet + String.format("u.%1$s= :%1$s", key);
				//realize I'm not 100% sure if there's a performance hit I'm taking for doing it this way.
				if(traverseKeys.hasNext()) {
					queryStringSnippet= queryStringSnippet + ", ";
				}
			}
			return queryStringSnippet;
		}
		return null;
	}
	
	/**
	 * Not DRY but...
	 * Realized probably wanted the alias configurable.
	 * @param attributesKeySet
	 * @param rowAlias
	 * @return
	 */
	public static final String generateAttributeMappingString(Iterable<String> attributesKeySet, String rowAlias) {
		if (attributesKeySet != null) {
			String queryStringSnippet = "";
			Iterator<String> traverseKeys = attributesKeySet.iterator();
			while (traverseKeys.hasNext()) {
				String key = traverseKeys.next();
				queryStringSnippet = queryStringSnippet + String.format("%2$s.%1$s= :%1$s", key, rowAlias);
				//realize I'm not 100% sure if there's a performance hit I'm taking for doing it this way.
				if(traverseKeys.hasNext()) {
					queryStringSnippet= queryStringSnippet + ", ";
				}
			}
			return queryStringSnippet;
		}
		return null;
	}
}
