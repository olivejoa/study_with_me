package com.ksk.project.study_with_me.web;

import com.ksk.project.study_with_me.config.MatchNames;
import com.ksk.project.study_with_me.config.auth.LoginUser;
import com.ksk.project.study_with_me.config.auth.dto.SessionUser;
import com.ksk.project.study_with_me.service.LikeService;
import com.ksk.project.study_with_me.service.PlaceService;
import com.ksk.project.study_with_me.web.dto.place.PostsListResponseDto;
import com.ksk.project.study_with_me.web.dto.place.PostsReadResponseDto;
import com.ksk.project.study_with_me.web.dto.place.PostsSaveRequestDto;
import com.ksk.project.study_with_me.web.dto.place.PostsSaveResponseDto;
import com.ksk.project.study_with_me.web.file.TransferFiles;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/board/place")
@RestController
public class PlaceApiController {
    private final PlaceService placeService;
    private final LikeService likeService;

    @PostMapping("/posts")
    public PostsSaveResponseDto save(@RequestBody PostsSaveRequestDto requestDto) {
        Long savedPostNo = placeService.save(requestDto);

        TransferFiles.saveImagesByHtmlCode(requestDto.getContent(), MatchNames.Boards.BOARD_PLACE_RECOMMENDATION.getShortName(), savedPostNo);

        return PostsSaveResponseDto.builder()
                .postNo(savedPostNo)
                .thumbnailPath(requestDto.getThumbnailPath())
                .build();
    }

    @PostMapping("/posts/upload/{thumbnail}")
    public boolean upload(@RequestParam("thumbnail") MultipartFile uploadFile, @PathVariable String thumbnail) {
        //thumbnail 명 받아서 그대로 옮기면 될것같음

        return TransferFiles.savedThumbnail(uploadFile, thumbnail);
    }

    @GetMapping("/posts/list")
    public ModelAndView findPosts(@PageableDefault(size = 10, sort="createdDate", direction = Sort.Direction.DESC) Pageable pageRequest
            , HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("board/place/posts-list");

        Page<PostsListResponseDto> list = placeService.findPosts(pageRequest);

        mav.addObject("list", list);
//        TransferFiles.listThumbnails(request.getSession().getServletContext().getRealPath("/")
//                , MatchNames.Boards.BOARD_PLACE_RECOMMENDATION.getShortName(), list.getContent());

        return mav;
    }

    @GetMapping("/posts/write")
    public ModelAndView write(@LoginUser SessionUser user) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("board/place/posts-save");

        mav.addObject("user", user);

        return mav;
    }

    @GetMapping("/posts/read")
    public ModelAndView read(Long postNo, @LoginUser SessionUser user, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("board/place/posts-read");

        PostsReadResponseDto responseDto = likeService.findByUserAndPost(placeService.findById(postNo));

        mav.addObject("post", responseDto);
        mav.addObject("user", user);

        TransferFiles.readImagesByHtmlCode(responseDto.getContent(), request.getSession().getServletContext().getRealPath("/")
                , MatchNames.Boards.BOARD_PLACE_RECOMMENDATION.getShortName(), postNo);

        return mav;
    }
}
