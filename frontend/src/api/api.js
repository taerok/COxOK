export default {
  // URL: 'http://localhost:8080',
  URL: "http://i3a104.p.ssafy.io",
  IMAGE_URL: "http://i3a104.p.ssafy.io/img/",
  // IMAGE_URL: '/img/',
  ROUTES: {
    // Accounts ROUTES
    signup: "/user/signup/",
    login: "/user/login/",
    checkEmail: "/user/email/",
    checkNickname: "/user/nickname/",
    myPage: "/user/mypage/", // 회원 정보 리턴 페이지..?
    update: "/user/modify/", // 회원 정보 수정
    withdrawal: "/user/withdrawal/", // 회원 탈퇴
    userLikeRecipe: "/user/likeRecipe", // 좋아요 레시피 목록 받기
    userMyRecipe: "/user/myrecipe", // 작성한 레시피 목록 받기
    userTotal: "/user/total", // 회원 합계 정보 받기

    // Recipes ROUTES
    recipeList: "/recipe/all/", // 모든 레시피 리스트를 받기 위한 주소(뒤에 페이지 주소 0부터 시작 / 6개씩 전달)
    searchRecipe: "/recipe/search/", // 레시피 검색을 위한 주소입니다. (선택된 카테고리의 id와 재료의 id를 Array로 보냅니다.)

    recipeDetail: "/recipe/view/", // 레시피 상세 정보를 받기 위한 주소(뒤에 recipe id 붙여주기)

    goRegister: "/recipe/goRegister/", // categories와 ingredients 리스트를 받기 위해 요청을 보내는 주소
    getCategory: "/recipe/get/", // categories와 ingredients 리스트를 받기 위해 요청을 보내는 주소(미인증 사용자용)

    recipeRegister: "/recipe/register/", // 레시피 작성을 위해 데이터를 보내야 되는 곳
    photoRegister: "/photo/register/", // 사진 등록 및 URL 리턴

    updateInfo: "/recipe/modifyInfo/", // UPDATE 시 정보 획득
    recipeUpdate: "/recipe/modify/",

    recipeDelte: "/recipe/delete/", // 뒤에 recipe_id 붙여주기

    recipeLike: "/recipe/like/",
    recipeDislike: "/recipe/likeCancel/",

    // Review Routes
    recipeReview: "/review/view/", // GET / 레시피 아이디를 보내면 레시피의 리뷰들이 반환되는 것같아요.
    reviewRegister: "/review/", // POST / 리뷰 작성을 위한 데이터를 전달할 주소
    reviewDelete: "/review/", // DELETE / 뒤에 review_id 붙여주기
    reviewUpdate: "/review/", // PUT / 뒤에 review_id

    // Versus ROUTES
    versusRegister: "/versus/register/", // POST / 요리대전 생성
    versusList: "/versus/all/", // GET / 요리대전 목록 (6개씩)
    versusDetail: "/versus/view/", // GET / 요리대전 상세보기

    // Blog ROUTES
    blog: "/blog/",
    blogList: "/blog/all/", // 모든 블로그 게시글을 받기 위한 주소
  },
};
