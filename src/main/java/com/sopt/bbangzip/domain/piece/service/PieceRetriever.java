package com.sopt.bbangzip.domain.piece.service;

import com.sopt.bbangzip.common.exception.base.NotFoundException;
import com.sopt.bbangzip.common.exception.code.ErrorCode;
import com.sopt.bbangzip.domain.piece.entity.Piece;
import com.sopt.bbangzip.domain.piece.repository.PieceRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PieceRetriever {

    private final PieceRepository pieceRepository;

    public Piece findByPieceIdAndUserId(final long pieceId, final long userId){
        return pieceRepository.findByPieceIdAndUserId(pieceId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PIECE));
    }

    public List<Piece> findByPiecesIdAndUserId(final long userId, final List<Long> pieceIds){
        return pieceRepository.findByPiecesIdAndUserId(userId, pieceIds);
    }

    public int countUnfinishedTodayPieces(final Long userId){
        return pieceRepository.countUnfinishedTodayPieces(userId);
    }

    public int countFinishedTodayPieces(final Long userId) {
        return pieceRepository.countFinishedTodayPieces(userId);
    }

    public int countPendingTodayPieces(final Long userId){
        return pieceRepository.countPendingTodayPieces(userId);
    }

    /*
    Todo
     */
    public List<Piece> findTodoPiecesByRecentOrder(final Long userId, final int year, final String semester){
        return  pieceRepository.findTodoPiecesByRecentOrder(userId, year, semester);
    }

    public List<Piece> findTodoPiecesByLeastVolumeOrder(final Long userId, final int year, final String semester){
        return pieceRepository.findTodoPiecesByLeastVolumeOrder(userId, year, semester);
    }

    public List<Piece> findTodoPiecesByNearestDeadlineOrder(final Long userId, final int year, final String semester){
        return pieceRepository.findTodoPiecesByNearestDeadlineOrder(userId, year, semester);
    }

    /*
    Pending
     */
    public List<Piece> findPendingPiecesByRecentOrder(final Long userId, final int year, final String semester){
        return pieceRepository.findPendingPiecesByRecentOrder(userId, year, semester);
    }

    public List<Piece> findPendingPiecesByLeastVolumeOrder(final Long userId, final int year, final String semester){
        return pieceRepository.findPendingPiecesByLeastVolumeOrder(userId, year, semester);
    }

    public List<Piece> findPendingPiecesByNearestDeadlineOrder(final Long userId, final int year, final String semester){
        return pieceRepository.findPendingPiecesByNearestDeadlineOrder(userId, year, semester);
    }

    /*
    Add Todo
     */

    public int findAddTodoPieceCount(final Long userId, final int year, final String semester){
        return pieceRepository.findAddTodoPieceCount(userId, year, semester);
    }

    public List<Piece> findAddTodoPieceListByRecentOrder(final Long userId, final int year,  final String semester){
        return pieceRepository.findAddTodoPieceListByRecentOrder(userId, year, semester);
    }

    public List<Piece> findAddTodoPieceListByLeastVolumeOrder(final Long userId, int year,  String semester){
        return pieceRepository.findAddTodoPieceListByLeastVolumeOrder(userId, year, semester);
    }

    public List<Piece> findAddTodoPieceListByNearestDeadlineOrder(final Long userId, final int year, final String semester){
        return pieceRepository.findAddTodoPieceListByNearestDeadlineOrder(userId, year, semester);
    }

    /*
    Add Pending
     */

    // 중간고사, 기말고사 필터링
    public List<Piece> findByStudyExamIdAndUserId(final Long userId, final Long examId) {
        return pieceRepository.findByStudyExamIdAndUserId(userId, examId);
    }

    // 남은 공부 가져오기
    public int findRemainingCount(final Long userId, final Long subjectId, final Long examId) {
        return pieceRepository.findRemainingCount(userId, subjectId, examId);
    }

    // 밀린 공부 가져오기
    public int findPendingCount(final Long userId, final Long subjectId, final Long examId) {
        return pieceRepository.findPendingCount(userId, subjectId, examId);
    }
}
