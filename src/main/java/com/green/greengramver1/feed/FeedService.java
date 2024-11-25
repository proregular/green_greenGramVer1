package com.green.greengramver1.feed;

import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.feed.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedMapper mapper;
    private final MyFileUtils myFileUtils;

    public FeedPostRes postFeed(List<MultipartFile> pics, FeedPostReq p) {
        int result = mapper.insFeed(p);

        //파일 저장
        //위치 feed/${feedId}
        String middlePath = String.format("feed/%d", p.getFeedId());

        //폴더 만들기
        myFileUtils.makeFolders(middlePath);

        FeedPicDto feedPicDto = new FeedPicDto();

        //feedPicDto에 feedId값 넣어주세요
        feedPicDto.setFeedId(p.getFeedId());

        List<String> picStr = new ArrayList<String>();

        for(MultipartFile pic : pics) {
            String savedPicName = myFileUtils.makeRandomFileName(pic);
            String filePath = String.format("%s/%s", middlePath, savedPicName);

            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            feedPicDto.setPic(savedPicName);
            picStr.add(savedPicName);

            mapper.insFeedPic(feedPicDto);
        }

        FeedPostRes res = new FeedPostRes();

        res.setFeedId(p.getFeedId());
        res.setPics(picStr);

        return res;
    }

    public List<FeedGetRes> getFeedList(FeedGetReq p) {
        List<FeedGetRes> list = mapper.selFeedList(p);

        for(FeedGetRes res : list) {
            List<String> pics = mapper.selFeedPicList(res.getFeedId());
            res.setPics(pics);
        }

        return list;
    }
}
