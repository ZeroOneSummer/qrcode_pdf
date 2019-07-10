package com.tencet.controller;

import com.google.common.collect.Lists;
import com.tencet.common.R;
import com.tencet.entity.UserEntity;
import com.tencet.mapper.UserRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userMapper;
    
    @GetMapping("save")
    public R save(){
        UserEntity user = new UserEntity();
        user.setId((short) 1001);
        user.setName("zhangsan");
        user.setAge(18);
        userMapper.save(user);
        return R.OK();
    }


    /*
     * @param title   搜索标题 http://localhost:8080/api/v1/user/search?title=zhangsan
     * @param pageable page = 第几页参数, value = 每页显示条数
     */
    @GetMapping("search")
    public R search(String title, @PageableDefault(page = 1, value = 10) Pageable pageable){

        //按标题进行搜索
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", title);

        //如果实体和数据的名称对应就会自动封装，pageable分页参数
        Iterable<UserEntity> listIt =  userMapper.search(queryBuilder, pageable);
        List<UserEntity> list= Lists.newArrayList(listIt);
        
        return R.OK(list);
    }
}