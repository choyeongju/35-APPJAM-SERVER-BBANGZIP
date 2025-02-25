package com.sopt.bbangzip.common.constants.entity;

public class UserTableConstants {
    // 테이블 이름
    public static final String TABLE_USER = "users";

    // 컬럼 이름
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PLATFORM_USER_ID = "platform_user_id";
    public static final String COLUMN_PLATFORM = "platform";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_IS_ONBOARDING_COMPLETE = "is_onboarding_complete";
    public static final String COLUMN_USER_POINT = "user_point";
    public static final String COLUMN_USER_LEVEL = "user_level";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    // 뱃지 관련 컬럼
    public static final String COLUMN_FIRST_STUDY_COMPLETED_AT = "first_study_completed_at";
    public static final String COLUMN_FIRST_TODAY_TASKS_COMPLETED_AT = "first_today_tasks_completed_at";
    public static final String COLUMN_TODAY_STUDY_COMPLETE_COUNT = "today_study_complete_count";
    public static final String COLUMN_LAST_STUDY_COMPLETED_DATE = "last_study_completed_date";
    public static final String COLUMN_FIRST_CREATE_STUDY_COUNT = "first_create_study_count";
    public static final String COLUMN_HAS_MASS_BAKING_BREAD_BADGE = "has_mass_baking_bread_badge";
    public static final String COLUMN_HAS_PREPARING_OPENING_BAKERY = "has_preparing_opening_bakery";

    /**
     * ONLY APPZAM
     */
    // 추가된 뱃지 관련 컬럼
    public static final String COLUMN_ESCAPE_BADGE_1 = "escape_badge_1";
    public static final String COLUMN_ESCAPE_BADGE_AAPJAM = "escape_badge_aapjam";
    public static final String COLUMN_ESCAPE_BARELY_BADGE_1 = "escape_barely_badge_1";
    public static final String COLUMN_ESCAPE_BARELY_BADGE_2 = "escape_barely_badge_2";
    public static final String COLUMN_ESCAPE_BARELY_BADGE_3 = "escape_barely_badge_3";
    public static final String COLUMN_INSSA_BOSS_BADGE_1 = "inssa_boss_badge_1";
    public static final String COLUMN_INSSA_BOSS_BADGE_2 = "inssa_boss_badge_2";
}