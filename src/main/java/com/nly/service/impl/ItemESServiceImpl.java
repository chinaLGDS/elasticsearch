package com.nly.service.impl;

import com.nly.es.pojo.Stu;
import com.nly.es.pojo.Items;
import com.nly.service.ItemsESService;
import com.nly.utils.PagedGridResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemESServiceImpl implements ItemsESService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public PagedGridResult searhItems(String keywords, String sort, Integer page, Integer pageSize) {
//        String preTag = "<font color='red'>";
//        String postTag = "</font>";

        Pageable pageable = PageRequest.of(page, pageSize);

        SortBuilder sortBuilder = null;
        //c表示销量从高到低
        if (sort.equals("c")){
             sortBuilder = new FieldSortBuilder("sellCounts")
                    .order(SortOrder.DESC);
        }else if(sort.equals("p")){
            sortBuilder = new FieldSortBuilder("price")
                    .order(SortOrder.ASC);
        }else {
            sortBuilder = new FieldSortBuilder("itemName.keyword")
                    .order(SortOrder.ASC);
        }


        String itemNameField ="itemName";

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(itemNameField, keywords))
                .withHighlightFields(new HighlightBuilder.Field(itemNameField))
//                        .preTags(preTag)
//                        .postTags(postTag))
                .withSort(sortBuilder)
                .withPageable(pageable)
                .build();
        AggregatedPage<Items> pagedItems = esTemplate.queryForPage(query, Items.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

                List<Items> itemHighLightList = new ArrayList<>();

                SearchHits hits = response.getHits();
                for (SearchHit h : hits) {
                    HighlightField highlightField = h.getHighlightFields().get(itemNameField);
                    String itemName = highlightField.getFragments()[0].toString();

                    String itemId = (String) h.getSourceAsMap().get("itemId");
                    String imgUrl = (String) h.getSourceAsMap().get("imgUrl");
                    Integer price = (Integer) h.getSourceAsMap().get("price");
                    Integer sellCounts = (Integer) h.getSourceAsMap().get("sellCounts");


                    Items items = new Items();
                    items.setItemName(itemName);
                    items.setItemId(itemId);
                    items.setImgUrl(imgUrl);
                    items.setPrice(price);
                    items.setSellCounts(sellCounts);

                    itemHighLightList.add(items);
                }

                //传进去分页的pageable以及总的命中条数
                return new AggregatedPageImpl<>((List<T>) itemHighLightList,
                        pageable,
                        response.getHits().totalHits);

            }
        });

                //设计返回PagedGridResult
                PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(pagedItems.getContent());
        gridResult.setPage(page + 1);
        gridResult.setTotal(pagedItems.getTotalPages());
        gridResult.setRecords(pagedItems.getTotalElements());
        return gridResult;

}
    }
