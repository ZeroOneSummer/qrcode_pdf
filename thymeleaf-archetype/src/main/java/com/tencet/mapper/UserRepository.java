package com.tencet.mapper;

import com.tencet.entity.UserEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends ElasticsearchRepository<UserEntity, Long> {

}