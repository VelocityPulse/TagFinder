package com.cpulse.tagfinder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MetaParser {

    public static String[] getArticlesTag(String iHTML) {
        return getMetaAttributeValue(iHTML, "property", "article:tag", "content");
    }

    public static String[] getMetaAttributeValue(String iHTML, String iType,
                                                 String iTypeValue, String iAttribute) {
        List<String> oContentList = new ArrayList<>();

        Document lDoc = Jsoup.parse(iHTML);
        String lQuery = "meta[" + iType + "=" + iTypeValue + "]";
        Elements lElements = lDoc.select(lQuery);

        for (Element lItem : lElements) {
            oContentList.add(lItem.attr(iAttribute));
        }

        return oContentList.toArray(new String[0]);
    }
}