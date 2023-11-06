package project.fin_the_pen.thirdparty.codefAPI.util;

public class CommonConstant {
    public static final String API_DOMAIN = "https://api.codef.io";                                        // API서버 도메인
    public static final String TEST_DOMAIN = "https://development.codef.io";                                // API서버 데모 도메인

    public static final String TOKEN_DOMAIN = "https://oauth.codef.io";                                        // OAUTH2.0 테스트 도메인
    public static final String GET_TOKEN = "/oauth/token";                                                // OAUTH2.0 토큰 발급 요청 URL
    public static final String CONNECTED_ID = "connectedId";                                                // 유저 식별 연결 아이디
    public static final String PAGE_NO = "pageNo";                                                        // 페이지 번호

    public static final String KR_BK_1_B_001 = "/v1/kr/bank/b/account/account-list";                     // 은행 기업 보유계좌
    public static final String KR_BK_1_B_002 = "/v1/kr/bank/b/account/transaction-list";                 // 은행 기업 수시입출 거래내역
    public static final String KR_BK_1_B_003 = "/v1/kr/bank/b/installment-savings/transaction-list";     // 은행 기업 적금 거래내역
    public static final String KR_BK_1_B_004 = "/v1/kr/bank/b/loan/transaction-list";                    // 은행 기업 대출 거래내역
    public static final String KR_BK_1_B_005 = "/v1/kr/bank/b/exchange/transaction-list";                // 은행 기업 외화 거래내역
    public static final String KR_BK_1_B_006 = "/v1/kr/bank/b/fund/transaction-list";                    // 은행 기업 펀드 거래내역
    public static final String KR_BK_1_B_007 = "/v1/kr/bank/b/fast-account/transaction-list";            // 은행 기업 빠른계좌조회

    public static final String KR_BK_1_P_001 = "/v1/kr/bank/p/account/account-list";                     // 은행 개인 보유계좌
    public static final String KR_BK_1_P_002 = "/v1/kr/bank/p/account/transaction-list";                 // 은행 개인 수시입출 거래내역
    public static final String KR_BK_1_P_003 = "/v1/kr/bank/p/installment-savings/transaction-list";     // 은행 개인 적금 거래내역
    public static final String KR_BK_1_P_004 = "/v1/kr/bank/p/loan/transaction-list";                    // 은행 개인 대출 거래내역
    public static final String KR_BK_1_P_005 = "/v1/kr/bank/p/fast-account/transaction-list";            // 은행 개인 빠른계좌조회

    public static final String KR_BK_2_P_001 = "/v1/kr/bank2/p/account/account-list";                    // 저축은행 개인 보유계좌 조회
    public static final String KR_BK_2_P_002 = "/v1/kr/bank2/p/account/transaction-list";                // 저축은행 개인 수시입출 거래내역

    public static final String KR_CD_B_001 = "/v1/kr/card/b/account/card-list";                            // 카드 법인 보유카드
    public static final String KR_CD_B_002 = "/v1/kr/card/b/account/approval-list";                        // 카드 법인 승인내역
    public static final String KR_CD_B_003 = "/v1/kr/card/b/account/billing-list";                         // 카드 법인 청구내역
    public static final String KR_CD_B_004 = "/v1/kr/card/b/account/limit";                                // 카드 법인 한도조회

    public static final String KR_CD_P_001 = "/v1/kr/card/p/account/card-list";                            // 카드 개인 보유카드
    public static final String KR_CD_P_002 = "/v1/kr/card/p/account/approval-list";                        // 카드 개인 승인내역
    public static final String KR_CD_P_003 = "/v1/kr/card/p/account/billing-list";                         // 카드 개인 청구내역
    public static final String KR_CD_P_004 = "/v1/kr/card/p/account/limit";                                // 카드 개인 한도조회

    public static final String KR_PB_NT_001 = "/v1/kr/public/nt/business/status";                            // 공공 사업자상태
    public static final String KR_PB_CK_001 = "/v1/kr/public/ck/real-estate-register/status";                // 공공 부동산등기
    public static final String KR_PB_EF_001 = "/v1/kr/public/ef/driver-license/status";                      // 공공 운전면허 진위여부
    public static final String KR_PB_MW_001 = "/v1/kr/public/mw/identity-card/status";                       // 공공 주민등록 진위여부

    public static final String KR_IS_0001_001 = "/v1/kr/insurance/0001/credit4u/contract-info";            // 보험다보여-계약정보조회
    public static final String KR_IS_0001_002 = "/v1/kr/insurance/0001/credit4u/register";                // 보험다보여-회원가입신청
    public static final String KR_IS_0001_003 = "/v1/kr/insurance/0001/credit4u/find-id";                // 보험다보여-아이디찾기
    public static final String KR_IS_0001_004 = "/v1/kr/insurance/0001/credit4u/change-pwd";                // 보험다보여-비밀번호변경
    public static final String KR_IS_0001_005 = "/v1/kr/insurance/0001/credit4u/unregister";                // 보험다보여-회원탈퇴신청


    public static final String GET_CONNECTED_IDS = "/v1/account/connectedId-list";                        // 커넥티드아이디 목록 조회
    public static final String GET_ACCOUNTS = "/v1/account/list";                                            // 계정 목록 조회
    public static final String CREATE_ACCOUNT = "/v1/account/create";                                        // 계정 등록(커넥티드아이디 발급)
    public static final String ADD_ACCOUNT = "/v1/account/add";                                                // 계정 추가
    public static final String UPDATE_ACCOUNT = "/v1/account/update";                                        // 계정 수정
    public static final String DELETE_ACCOUNT = "/v1/account/delete";                                        // 계정 삭제

    /**
     * API 요청 도메인 반환
     *
     * @return
     */
    public static String getRequestDomain() {
        return CommonConstant.TEST_DOMAIN;
    }


    /**
     * CODEF로부터 발급받은 클라이언트 아이디
     */
    public static final String CLIENT_ID = "81a8680d-bcb7-4f73-83f2-15bef1966761";

    /**
     * CODEF로부터 발급받은 시크릿 키
     */
    public static final String SECERET_KEY = "1f4ba394-abbf-451c-8f75-77ddd05965cf";

    /**
     * CODEF로부터 발급받은 퍼블릭 키
     */
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1ld5T8Q2IhUK+KbuPI2M4Vv3mdin6/RU4DHM5guWTBKAEawgLedFQzNo+CpSB77WsLs5S6+XEEuIFvRv9O6MOcTE/9YdW6DbkfcpYj/Gj4pn4lDqPUj+GgFugVAU9b4geLyoUurkFrfFVA+6DZFKSXqJlVJ7zoWOgCSNl2YaZvsXy7mkAAZdPGNkfpYSIxHX898/5t8covZ2zGeN1FLKl+VIn/IS3u28L5PKbJPr/RhYUkgwENykwz3zfzFg0KyutUC6D8CNxRzRlsqBXmK5xJbD+Gq6ysv3Qhlaj8kqgt3zcf/JvJEvUZlbpa6eGh34OvSmmeJYsdcBQhcr1H0kMQIDAQAB";

    /**
     * OAUTH2.0 토큰 샘플
     */
    public static String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZXJ2aWNlX3R5cGUiOiIxIiwic2NvcGUiOlsicmVhZCJdLCJzZXJ2aWNlX25vIjoiMDAwMDAyMTg4MDAyIiwiZXhwIjoxNjc5MjgxMzkyLCJhdXRob3JpdGllcyI6WyJJTlNVUkFOQ0UiLCJQVUJMSUMiLCJCQU5LIiwiRVRDIiwiU1RPQ0siLCJDQVJEIl0sImp0aSI6IjI5ZGU0Mzc4LWJlZDYtNDRhNC05YjhlLThlMDM2N2JmYTkxOSIsImNsaWVudF9pZCI6IjRlMDgzNTZjLTE5NDAtNGM2OS05ZWM5LTFkNTVmMTJjM2NmMiJ9.G1ug_aUvVCcvw2fsXP6Fiszb8L91Yuumxx0UNzN__fSmYEUGEWoD6XHInTqcBtGZQiQTgBuq9RF3hBT_A9zWLaFYHnAnU1UBimM6YCqsrF__uA0CiqX85z5dys9mWTY7uCr79qpU4kyMcRmYFcWKKobRBtRG6f0r3kMVTdwHzq4dXpexTDCDVI11mK2YG_o3-LMBAVnEf3ddY-m1cWWPzk7Op6HIn6jQ4BFpqwpu6neU_FRGKsx8fUVM3NMDfTYqbirc4nrNHqgnghLvnfRX6feM2Vxly8jscc2rAbRckApFVRauDQVhJ7wiANZKUg8ssMn-rpPz4-p5SYrbcUCuMw";


    /**
     * 샌드박스 테스트용 상수
     */
    public static final String SANDBOX_DOMAIN = "https://sandbox.codef.io";                                    // API서버 샌드박스 도메인
    public static final String SANDBOX_CLIENT_ID = "ef27cfaa-10c1-4470-adac-60ba476273f9";                // CODEF 샌드박스 클라이언트 아이디
    public static final String SANDBOX_SECERET_KEY = "83160c33-9045-4915-86d8-809473cdf5c3";                // CODEF 샌드박스 클라이언트 시크릿
}
