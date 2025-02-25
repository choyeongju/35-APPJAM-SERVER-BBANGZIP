package com.sopt.bbangzip.domain.badge.service;

import com.sopt.bbangzip.common.exception.base.NotFoundException;
import com.sopt.bbangzip.common.exception.code.ErrorCode;
import com.sopt.bbangzip.domain.badge.entity.Badge;
import com.sopt.bbangzip.domain.badge.entity.BadgeCondition;
import com.sopt.bbangzip.domain.badge.api.dto.response.BadgeDetailResponse;
import com.sopt.bbangzip.domain.badge.api.dto.response.BadgeListResponse;
import com.sopt.bbangzip.domain.badge.api.dto.response.BadgeResponse;
import com.sopt.bbangzip.domain.piece.service.PieceRetriever;
import com.sopt.bbangzip.domain.user.entity.User;
import com.sopt.bbangzip.domain.user.repository.UserRepository;
import com.sopt.bbangzip.domain.user.service.LevelInfo;
import com.sopt.bbangzip.domain.user.service.UserLevelCalculator;
import com.sopt.bbangzip.domain.user.service.UserRetriever;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadgeService {

    /**
     * - 모든 뱃지의 목록 관리
     * - 사용자에게 조건에 맞는 뱃지 지급
     *
     * - 모든 뱃지는 한 번씩만 획득할 수 있다!!
     */

    private final UserRetriever userRetriever;
    private final List<Badge> badges;
    private final PieceRetriever pieceRetriever;
    private final UserLevelCalculator userLevelCalculator;
    private final UserRepository userRepository;

//    /**
//     * 조건에 맞는 모든 뱃지를 반환
//     */
//    public List<BadgeResponse> getAllEligibleBadges(User user) {
//        List<BadgeResponse> awardedBadges = new ArrayList<>();
//
//        for (Badge badge : badges) {
//            BadgeCondition condition = badge.getCondition();
//            if (condition.isEligible(user) && !isBadgeAlreadyAwarded(user, badge)) {
//                awardBadge(user, badge); // 뱃지를 부여
//                awardedBadges.add(new BadgeResponse(
//                        badge.getName(),
//                        badge.getImage(),
//                        badge.getHashTags()
//                ));
//            }
//        }
//        updateUserLevel(user);
//        return awardedBadges; // 조건에 맞는 모든 뱃지를 반환
//    }

    public List<BadgeResponse> getAllEligibleBadges(final User user) {
        List<BadgeResponse> awardedBadges = new ArrayList<>();

        for (Badge badge : badges) {
            if (badge.getCondition().isEligible(user) && !isBadgeAlreadyAwarded(user, badge)) {
                awardBadge(user, badge);
                awardedBadges.add(new BadgeResponse(
                        badge.getName(),
                        badge.getImage(),
                        badge.getHashTags()
                ));
            }
        }
        updateUserLevel(user);
        return awardedBadges;
    }

    private void updateUserLevel(final User user) {
        int currentPoints = user.getPoint();
        LevelInfo levelInfo = userLevelCalculator.calculateLevelInfo(currentPoints);
        user.updateUserLevel(levelInfo.getLevel());
    }

    /**
     * "빵집 오픈 준비 중" 뱃지 평가
     */
    public List<BadgeResponse> checkForPreparingOpenBakeryBadge(final User user) {
        Badge badge = badges.stream()
                .filter(b -> b.getName().equals("빵집 오픈 준비 중"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Badge not found: 빵집 오픈 준비 중"));

        if (badge.getCondition().isEligible(user) && !isBadgeAlreadyAwarded(user, badge)) {
            awardBadge(user, badge);
            return List.of(new BadgeResponse(badge.getName(), badge.getImage(), badge.getHashTags()));
        }
        return List.of();
    }

    /**
     * "빵 굽기 시작" 뱃지 평가
     */
    public List<BadgeResponse> checkForStartBakingBreadBadge(final User user) {
        Badge badge = badges.stream()
                .filter(b -> b.getName().equals("빵 굽기 시작"))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_BADGE));

        if (badge.getCondition().isEligible(user) && !isBadgeAlreadyAwarded(user, badge)) {
            awardBadge(user, badge);
            return List.of(new BadgeResponse(badge.getName(), badge.getImage(), badge.getHashTags()));
        }
        return List.of();
    }

    private boolean isBadgeAlreadyAwarded(final User user, final Badge badge) {
        int todayCounts = pieceRetriever.countUnfinishedTodayPieces(user.getId());
        return switch (badge.getName()) {
            case "빵집 오픈 준비 중" -> user.getHasPreparingOpeningBakery() != null;
            case "빵 굽기 시작" -> user.getFirstStudyCompletedAt() != null;
            case "오늘의 빵 완판" -> user.getAllTasksCompletedAt() != null;
            case "빵 대량 생산" -> user.getHasMassBakingBreadBadge() != null;
            case "미룬이탈출1" -> user.getEscapeBadge1() != null;
            case "미룬이탈출앱잼" -> user.getEscapeBadgeAapjam() != null;
            case "미룬이겨우탈출1" -> user.getEscapeBarelyBadge1() != null;
            case "미룬이겨우탈출2" -> user.getEscapeBarelyBadge2() != null;
            case "미룬이겨우탈출3" -> user.getEscapeBarelyBadge3() != null;
            case "인싸보스1" -> user.getInssaBossBadge1() != null;
            case "인싸보스2" -> user.getInssaBossBadge2() != null;
            default -> false;
        };
    }

    // 뱃지 부여
    private void awardBadge(final User user, final Badge badge) {
        if (!badge.getCondition().isEligible(user) || isBadgeAlreadyAwarded(user, badge)) {
            return; // 조건 불충족 또는 이미 부여된 경우
        }

        // 유저가 어떤 뱃지를 얻었는지와 포인트에 대한 특정 필드를 업데이트 시키자
        switch (badge.getName()) {
            case "빵집 오픈 준비 중" -> {
                user.markHasPreparingOpeningBakery(); // '빵집 오픈 준비 중' 뱃지 처리
                user.incrementFirstCreateStudyCount(); // Study 생성 횟수 증가 처리
            }
            case "빵 굽기 시작" -> user.markFirstStudyComplete();
            case "오늘의 빵 완판" -> user.markFirstTodayTasksCompletedAt();
            case "빵 대량 생산" -> user.markHasMassBakingBreadBadge();
            case "미룬이탈출1" -> user.markEscapeBadge1();
            case "미룬이탈출앱잼" -> user.markEscapeBadgeAapjam();
            case "미룬이겨우탈출1" -> user.markEscapeBarelyBadge1();
            case "미룬이겨우탈출2" -> user.markEscapeBarelyBadge2();
            case "미룬이겨우탈출3" -> user.markEscapeBarelyBadge3();
            case "인싸보스1" -> user.markInssaBossBadge1();
            case "인싸보스2" -> user.markInssaBossBadge2();
            default -> throw new NotFoundException(ErrorCode.NOT_FOUND_BADGE);
        }
        userRepository.save(user);
        log.info(badge.getName() + "뱃지를 획득하였습니다!");
    }

    /**
     * 유저가 획득한 뱃지 목록을 반환
     */
    public BadgeListResponse getBadgeList(final Long userId) {
        User user = userRetriever.findByUserId(userId);
        String nickname = user.getNickname();

        List<BadgeListResponse.Badge> badgeList = badges.stream()
                .map(badge -> new BadgeListResponse.Badge(
                        badge.getCategory(),
                        badge.getName(),
                        isBadgeLocked(user, badge),
                        badge.getImage()
                ))
                .toList();
        return new BadgeListResponse(nickname, badgeList);
    }

    /**
     * 유저 조건에 따라 뱃지 잠금 여부를 판별
     */
    private boolean isBadgeLocked(final User user, final Badge badge) {
        switch (badge.getName()) {
            case "빵집 오픈 준비 중":
                return user.getHasPreparingOpeningBakery() == null;
            case "빵 굽기 시작":
                return user.getFirstStudyCompletedAt() == null;
            case "오늘의 빵 완판":
                return user.getAllTasksCompletedAt() == null;
            case "빵 대량 생산":
                return user.getHasMassBakingBreadBadge() == null;
            case "미룬이탈출1" :
                return user.getEscapeBadge1() == null;
            case "미룬이탈출앱잼" :
                return user.getEscapeBadgeAapjam() == null;
            case "미룬이겨우탈출1" :
                return user.getEscapeBarelyBadge1() == null;
            case "미룬이겨우탈출2" :
                return user.getEscapeBarelyBadge2() == null;
            case "미룬이겨우탈출3" :
                return user.getEscapeBarelyBadge3() == null;
            case "인싸보스1" :
                return user.getInssaBossBadge1() == null;
            case "인싸보스2" :
                return user.getInssaBossBadge2() == null;
            default :
                return true;
        }
    }

    /**
     * 특정 뱃지의 상세 정보 조회
     */
    public BadgeDetailResponse getBadgeDetail(final Long userId, final String badgeName) {
        User user = userRetriever.findByUserId(userId);

        // 특정 이름의 뱃지 검색
        return badges.stream()
                .filter(badge -> badge.getName().equals(badgeName))
                .findFirst()
                .map(badge -> new BadgeDetailResponse(
                        badge.getName(),
                        badge.getImage(),
                        badge.getHashTags(),
                        badge.getAchievementCondition(),
                        badge.getReward(),
                        badge.isLocked(user)
                ))
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_BADGE));
    }
}