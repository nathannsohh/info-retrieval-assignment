package com.example.backend.news.mapper;

import com.example.backend.news.dto.NewsTweetResponse;
import com.example.backend.news.model.NewsTweet;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewsTweetMapper {

  List<NewsTweetResponse> fromNewsTweetListToNewsTweetResponseList(List<NewsTweet> entity);

  NewsTweetResponse fromNewsTweetToNewsTweetResponse(NewsTweet entity);
}
