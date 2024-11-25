package com.green.greengramver1.feed.model;

import com.green.greengramver1.common.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Schema(title="피드 정보")
public class FeedGetReq extends Paging {
    public FeedGetReq(Integer page, Integer size) {
        super(page, size);
    }
}
