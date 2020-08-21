package com.ksk.project.study_with_me.domain.boardPlaceRecommendation;

import com.ksk.project.study_with_me.domain.board.Board;
import com.ksk.project.study_with_me.domain.board.BoardRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardPlaceRecommendationTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardPlaceRecommendationRepository boardPlaceRecommendationRepository;

    @After
    public void cleanup() {
        boardPlaceRecommendationRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    public void 스터디장소추천게시글저장_불러오기() {
        //given
        String boardCode = "BPR";
        String boardName = "스터디장소 추천게시판";

        Board board = Board.builder()
                .boardCode(boardCode)
                .boardName(boardName)
                .build();

        boardRepository.save(board);

        Long userCode = 1L;
        String title = "스터디장소 추천 제목";
        String address = "서울특별시 강남구";
        int likeCount = 112;
        int dislikeCount = 34;
        int viewCount = 234;
        Long imageCodes = 123L;
        Long thumbnail_image_code = 12L;

        boardPlaceRecommendationRepository.save(
                BoardPlaceRecommendation.builder()
                .board(board)
                .userCode(userCode)
                .title(title)
                .address(address)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .viewCount(viewCount)
                .build());

        //when
        List<BoardPlaceRecommendation> boardPlaceRecommendationList = boardPlaceRecommendationRepository.findAll();

        //then
        BoardPlaceRecommendation boardPlaceRecommendation = boardPlaceRecommendationList.get(0);
        assertThat(boardPlaceRecommendation.getBoard().getBoardCode()).isEqualTo(boardCode);
        assertThat(boardPlaceRecommendation.getUserCode()).isEqualTo(userCode);
        assertThat(boardPlaceRecommendation.getAddress()).isEqualTo(address);
        assertThat(boardPlaceRecommendation.getLikeCount()).isEqualTo(likeCount);
        assertThat(boardPlaceRecommendation.getDislikeCount()).isEqualTo(dislikeCount);
        assertThat(boardPlaceRecommendation.getViewCount()).isEqualTo(viewCount);
    }
}