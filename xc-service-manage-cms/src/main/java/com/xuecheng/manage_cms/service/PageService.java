package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:32
 **/
@Service
public class PageService {

    @Autowired
    CmsPageRepository cmsPageRepository;


    /**
     * 页面查询方法
     *
     * @param page             页码，从1开始记数
     * @param size             每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {

        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of (page, size);


        // ExampleMatcher exampleMatcher = ExampleMatcher.matching(); 精确匹配
        // 条件匹配器
        // 页面别名模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching ().withMatcher ("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains ());
        CmsPage cmsPage = new CmsPage ();
        if (!StringUtils.isEmpty (queryPageRequest.getSiteId ())) {
            cmsPage.setSiteId (queryPageRequest.getSiteId ());
        }
        Example<CmsPage> example = Example.of (cmsPage, exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll (example, pageable);
        QueryResult queryResult = new QueryResult ();
        queryResult.setList (all.getContent ());  //数据列表
        queryResult.setTotal (all.getTotalElements ());  //总条数
        QueryResponseResult queryResponseResult = new QueryResponseResult (CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 增加cms_page
     *
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage) {
        CmsPage cmsPage1 = cmsPageRepository.save (cmsPage);
        CmsPageResult cmsPageResult = new CmsPageResult (CommonCode.SUCCESS, cmsPage1);
        return cmsPageResult;
    }
}
