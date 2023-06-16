<template>
    <div class="review-list">
        <div class="review-group">
            <div class="m-select">
                <ul class="mk-c-tab col8 tab-event">
                    <li><a class="review-a" href="#">전체</a></li>
                    <li><a class="review-a" href="#">기초강의</a></li>
                    <li><a class="review-a" href="#">데이터분석</a></li>
                    <li><a class="review-a" href="#">웹 개발</a></li>
                    <li><a class="review-a" href="#">프로그래밍 언어</a></li>
                    <li><a class="review-a" href="#">인공지능</a></li>
                    <li><a class="review-a" href="#">프로그래밍 교양</a></li>
                    <li><a class="review-a" href="#">개발도구</a></li>
                    <li><a class="review-a" href="#">컴퓨터 공학 전공 지식</a></li>
                    <li><a class="review-a" href="#">디자인</a></li>
                </ul>
            </div>
            <div class="review-search">
                <p id="fw-bold fs-3 text-start" class="fw-bold fs-3 text-start">수강후기</p>
                <div class="review-search-bar">
                    <div id="review-search-input">
                        <input
                        type="text"
                        placeholder="검색어를 입력하세요"
                        v-model="searchKeyword"
                        @keyup.enter="searchReviews"
                        />
                        <button @click="searchReviews">검색</button>
                    </div>
                </div>
            </div>
            <div id="review-sort">
                <select id="sortOption" v-model="sortOption" @change="sortReviews">
                    <option value="default">최신 순</option>
                    <option value="highRating">평점 높은 순</option>
                    <option value="lowRating">평점 낮은 순</option>
                </select>
            </div>  
        </div>
        <!-- <b-button type="button" :to="'/review/reviewWrite'">작성하기</b-button> -->
        <div id="scroll-container">
            <table id="review-table">
                <thead>
                    <tr>
                        <th>강좌</th>
                        <th>수강후기</th>
                        <th>별점</th>
                        <th>작성자</th>
                        <th>작성일</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(reviews, index) in displayedReviews" :key="index">
                        <td class="review-title">{{ reviews.TITLE }}</td>
                        <td>
                            <span class="review-content">{{ reviews.CONTENT }}</span>
                        </td>
                        <td>
                            <div class="review-star-rating">
                                <span class="review-star" v-for="star in reviews.STAR" :key="star"
                                >&#9733;</span
                                >
                            </div>
                        </td>
                        <td>{{ reviews.WRITER }}</td>
                        <td>{{ formatDateTime(reviews.REVIEWDATE) }}</td>
                    </tr>
                    <infinite-loading id="infiniteReview" @infinite="fetchMoreReviews" :force-use-infinite-wrapper="true">
                        <template #no-more>마지막 후기 입니다.</template>
                    </infinite-loading>
                </tbody>
                <tr v-if="isLoading">
                    <td colspan="5" class="loading-text">로딩 중...</td>
                </tr>
                <div class="infinite-loading-container">
                </div>
            </table>
        </div>
    </div>
</template>

<script>
    import InfiniteLoading from "infinite-loading-vue3-ts";
    import '@/assets/css/reviewStyle.css';

    export default {
        components: {
            InfiniteLoading,
        },
        data() {
            return {
                searchKeyword: "", // 검색어
                fetchedReviews: [], // 검색 전에 모든 후기를 저장하는 배열
                searchedReviews: [], // 검색 결과를 저장하는 배열
                sortOption: "default", // 정렬 옵션
                content: "",
                isLoading: false, // 로딩 중 여부
                currentPage: 1, // 현재 페이지 번호
                itemsPerPage: 20, // 한 페이지에 보여줄 아이템 수
                totalItems: 0, // 총 아이템 수
            };
        },
        computed: {
            displayedReviews() {
                return this.searchKeyword ? this.searchedReviews : this.fetchedReviews;
            },
        },
        created() {
            this.fetchReviews(); 
        },
        methods: {
            // 후기 가져오기
            fetchReviews() {
                this.isLoading = true; // 로딩 중 상태를 true로 설정합니다.
                this.fetchedReviews = []; // fetchedReviews 배열을 초기화합니다.
                this.$axiosSend("get", "/api/reviews", {
                    page: this.currentPage, // 현재 페이지 번호를 파라미터로 전달합니다.
                    size: this.itemsPerPage, // 한 페이지에 보여줄 아이템 수를 파라미터로 전달합니다.
                    keyword: this.searchKeyword, // 검색어를 파라미터로 전달합니다.
                })
                .then((response) => {
                    const { totalElements } = response.data;
                        if (response.data.length) {
                        // 가져온 후기를 fetchedReviews 배열에 추가
                        this.fetchedReviews.push(...response.data);
                        this.currentPage++;
                        }
                        
                        this.totalItems = totalElements; // 총 아이템 수를 업데이트합니다

                        this.isLoading = false; // 로딩 중 상태를 false로 설정합니다.
                        // 검색 및 정렬 수행
                        this.fetchSearchedReviews(); // 검색 수행
                        this.sortReviews(); // 정렬 수행
                })
                .catch((error) => {
                     console.error(error);
                    this.isLoading = false; // 에러 발생 시 로딩 중 상태를 false로 설정합니다.
                });
            },        
            fetchSearchedReviews(){
                const keyword = this.searchKeyword.trim().toLowerCase();
                    if (keyword === "") {
                        this.searchedReviews = []; // 키워드가 비어있을 경우 검색된 후기를 초기화합니다.
                    } else {
                    this.$axiosSend("get", "/api/reviews/search", {
                    keyword: this.searchKeyword, // 검색어를 파라미터로 전달합니다.
                })
                .then((response) => {
                    this.searchedReviews = response.data; 
                })
                .catch((error) => {
                    console.error(error);
                });
                   }
            },
            handleScroll() {
                const scrollHeight = document.documentElement.scrollHeight;
                const scrollTop = window.pageYOffset;
                const clientHeight = document.documentElement.clientHeight;
                const bottomOffset = 20;

                if (scrollHeight - scrollTop - clientHeight <= bottomOffset && !this.isFetching) {
                     // 맨 아래로 스크롤하고 데이터를 불러오는 중이 아닌 경우 fetchMoreReviews 함수를 호출합니다.
                    this.fetchMoreReviews();
                }
            },
            fetchMoreReviews() {
                this.isFetching = true;
                // 데이터를 불러오는 비동기 작업 수행
                this.$axiosSend("get", "/api/reviews", {
                    page: this.currentPage,
                    size: this.itemsPerPage,
                    keyword: this.searchKeyword,
                })
                .then((response) => {
                const { totalElements } = response.data;
                    if (response.data.length) {
                        // 가져온 후기를 fetchedReviews 배열에 추가
                        this.fetchedReviews.push(...response.data);
                        this.currentPage++;
                    }
                        this.totalItems = totalElements;
                        this.isFetching = false;

                        // 검색 및 정렬 수행
                        this.fetchSearchedReviews(); // 검색 수행
                        this.sortReviews(); // 정렬 수행
                })
                .catch((error) => {
                    console.error(error);
                    this.isFetching = false;
                });
            },
            //별점순, 최신순 정렬
            sortReviews() {
                switch (this.sortOption) {
                    case "highRating":
                        // 별점 높은 순으로 후기 정렬
                        this.fetchedReviews = [...this.fetchedReviews].sort(
                            (a, b) => b.STAR - a.STAR
                        );
                    break;
                    case "lowRating":
                        // 별점 낮은 순으로 후기 정렬
                        this.fetchedReviews = [...this.fetchedReviews].sort(
                            (a, b) => a.STAR - b.STAR
                        );
                    break;
                    default:
                        // 최신 순으로 후기 정렬
                        this.fetchedReviews = [...this.fetchedReviews].sort((a, b) => {
                            const dateA = new Date(a.REVIEWDATE);
                            const dateB = new Date(b.REVIEWDATE);
                        return dateB - dateA;
                        });
                    break;
                }
            },
            // 후기 검색
            searchReviews() {
                if (this.searchKeyword === "") {
                // 검색어가 비어있을 경우, 알림창을 표시합니다.
                    alert("검색어를 입력해주세요");
                return; // 메소드를 종료합니다.
                }
                this.searchedReviews = []; //검색된 후기를 초기화
                this.currentPage = 1; // 페이지 번호 초기화
                this.fetchReviews(); // 후기 가져오기 호출
            },
            formatDateTime(value) {
                // value는 날짜 값입니다
                const now = new Date();
                const date = new Date(value);

                const diffInMilliseconds = now - date;
                const diffInSeconds = Math.floor(diffInMilliseconds / 1000);
                const diffInMinutes = Math.floor(diffInSeconds / 60);
                const diffInHours = Math.floor(diffInMinutes / 60);
                const diffInDays = Math.floor(diffInHours / 24);

                if (diffInDays > 0) {
                    return `${diffInDays}일 전`;
                } else if (diffInHours > 0) {
                     return `${diffInHours}시간 전`;
                } else if (diffInMinutes > 0) {
                    return `${diffInMinutes}분 전`;
                } else {
                    return '방금 전';
                }
            },
        },
        mounted() {
            // 스크롤 이벤트를 감지하기 위해 window 객체에 이벤트 리스너를 추가합니다.
            window.addEventListener('scroll', this.handleScroll); 
        },
        beforeUnmount() {
            // 컴포넌트가 해제되기 전에 스크롤 이벤트 리스너를 제거합니다.
            window.removeEventListener('scroll', this.handleScroll);
        },
            
            
    };
</script>
