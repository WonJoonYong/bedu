<!-- PageHeader.vue -->
<template>
    <b-navbar class="w-100 d-flex" toggleable="lg">

        <b-navbar-brand>
            <router-link to="/">
                <img id="bedu-logo" src="@/assets/imgs/Logo.png" width="150">
            </router-link>
        </b-navbar-brand>

        <b-navbar-toggle target="header-collapse"></b-navbar-toggle>
        
        <b-collapse id="header-collapse" is-nav>
            <b-navbar-nav id="nav1" class="me-auto">
                <b-nav-item class="dropdown fs-5" @mouseenter="openDropdown" @mouseleave="closeDropdown">
                    
                    <!-- 랜더링 되기전에 접근하면 에러가 발생하기 때문에 v-if로 조건 -->
                    <a v-if='categories.length' class="dropdown-toggle no-arrow m-0" type="button" id="dropdownMenuButton1" :aria-expanded="isDropdownOpen">
                        <router-link :to='"/lectureCategories/"+categories[0].cateCode+"?cnt_mid_cate="+categories[0].children[0].cateCode'>
                            분야별 강의
                        </router-link>
                    </a>    
                    <a v-else class="dropdown-toggle no-arrow m-0" type="button" id="dropdownMenuButton1" :aria-expanded="isDropdownOpen">
                        분야별 강의
                    </a>

                    <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1" :class="isDropdownOpen == true ? 'dropdown-menu-show':''">
                        <li v-for="(item,index) in categories" :key="index" class="dropdown-item my-2">
                            <router-link :to='"/lectureCategories/" + item.cateCode + "?cnt_mid_cate=" + item.children[0].cateCode'>
                                <a  style="cursor: pointer;" class="">{{ item.cateKor }}</a>
                            </router-link>
                        </li>
                    </ul>
                </b-nav-item>
                <b-nav-item class="fs-5">
                    <router-link to="/comm/qna">커뮤니티</router-link>
                </b-nav-item>
                <b-nav-item class="fs-5">
                    <router-link to="/review">수강후기</router-link>
                </b-nav-item>
                <b-nav-item class="fs-5">
                    <router-link to="/companyStudy">기업교육</router-link>
                </b-nav-item>
                <b-nav-item class="fs-5">
                    <router-link to="/fileUpload">영상업로드</router-link>
                </b-nav-item>
            </b-navbar-nav>
            <b-navbar-nav id="nav2" class="ms-auto">
                <div class="search-popup">
                    <div class="border-3 rounded-pill p-2 d-flex align-middle text-center m-auto border-bedu">
                        <font-awesome-icon class="m-auto mx-3" :icon="['fas', 'magnifying-glass']" />
                        <form @submit.prevent="lectSearch">
                            <b-form-input class="border-0 me-2" v-model="keyword"></b-form-input>
                        </form>
                    </div>
                </div>
                <b-nav-item class="fs-5">
                    <router-link v-if="!isLoggedIn" to="/login">로그인</router-link>
                </b-nav-item>
                <b-dropdown v-if="isLoggedIn" class="fs-5">
                    <template #button-content>
                        <span class="no-arrow">{{ getNickname }}</span>
                    </template>
                    <b-dropdown-item>
                        <router-link to="/mypage">마이페이지</router-link>
                    </b-dropdown-item>
                    <b-dropdown-item>
                        <router-link to="/csc">고객센터</router-link>
                    </b-dropdown-item>
                </b-dropdown>
                <b-nav-item class="fs-5">
                    <button v-if="isLoggedIn" @click="logout">로그아웃</button>
                </b-nav-item>
                <b-nav-item class="fs-5">
                    <router-link v-if="!isLoggedIn" to="/regist">회원가입</router-link>
                </b-nav-item>
            </b-navbar-nav>
      </b-collapse>   
        <div ref="scrollTop" class="position-fixed d-flex scrollTop rounded-circle" @click="scrollToTop"
        :class="{ 'show': showButton }">
            <font-awesome-icon class="text-white fs-3 position-relative" :icon="['fas', 'angles-up']" />
        </div>
    </b-navbar>
</template>

<script>
    export default {
        name: 'PageHeader',
        mounted() {
            window.addEventListener('scroll', this.scrollHandler)
        },
        created(){
            this.getCategory();
            this.isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
        },
        data() {
            return {
                isDropdownOpen: false, 
                categories: [], 
                showButton: false,
                keyword : '',
            };
        },
        computed: {
            isLoggedIn() {
                return this.$store.getters.getIsAuth
            },
            getNickname() {
                return this.$store.getters.getNickname;
            }
        },
        methods: {
            /** 검색 메서드 */
            lectSearch(){
                this.$routerPush(
                'lectureSearch',
                {
                    keyword : this.keyword
                },
                true
                )
            },
            /** 받은 카테고리를 트리 구조로 변경하는 함수 */
            convertToHierarchy(data) {
                const map = {}; // 부모-자식 관계를 저장할 맵
                // 맵에 카테고리 코드를 키로하여 카테고리 객체를 저장
                data.forEach(category => {
                    category.children = []; // 자식 카테고리를 저장할 배열 생성
                    map[category.cateCode] = category;
                });

                const hierarchy = []; // 최상위 부모 카테고리를 저장할 배열

                // 부모-자식 관계 설정
                data.forEach(category => {
                    const parentNum = category.parentNum;
                    if (parentNum != 0) {
                        const parent = map[parentNum];
                        if (parent) parent.children.push(category)
                    } else {
                        hierarchy.push(category);
                    }
                    this.categories = hierarchy;
                });
            },
            scrollHandler() {/** 스크롤이 내려감에 따라 showButton의 값 변경해주는 핸들러 */
                this.showButton = window.scrollY > 200;
            },
            scrollToTop() {/** 최상단으로 올리는 메서드 */
                window.scrollTo({top: 0})
            },
            navigateTo(route) {
                this.$router.push(route);
            },
            openDropdown() {
                this.isDropdownOpen = true;
            },
            closeDropdown() {
                this.isDropdownOpen = false;
            },
            getCategory() { /** 카테고리 조회 */
                let cateData = [];
                this.$axiosSend('get', '/api/lect/getCategory')
                .then((res) => {
                    console.log('res::: ', res)
                    if (this.$isNotEmpty(
                        res?.data
                    )) {
                        cateData = res?.data;
                    }
                    console.log('cateData ::: ', cateData)
                    this.convertToHierarchy(cateData)
                })
            },
            logout() {
                localStorage.removeItem("user_token")
                this.$store.commit('IS_AUTH', false);
                this.$store.commit('NICKNAME', null);
                this.$store.commit('USERNUM', null);
                this.$store.commit('EMAIL', null);
                location.reload()
            },
          },
          watch:{ /** url 변경 감지하여 헤더에 있는 검색 입력부분은 비우기 */
            '$route'(){
                this.keyword = '' 
            }
          }
    };
</script>
  
<style scoped>
    .scrollTop {
        background-color: var(--black);
        top : 85%;
        right : 6%;
        width: 80px;
        height: 80px;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transition: opacity 0.25s ease;
        cursor: default;
    }

    .scrollTop.show {
        opacity: 1;
        cursor: pointer;
    }

    .dropdown:hover .dropdown-menu {
        display: block;
    }

    #nav1 {
        display: flex;
        align-items: center;
    }

    #nav2 {
        display: flex;
        align-items: center;
    }

    #nav1 #bedu-logo {
        margin-right: 20px;
    }

    #nav1 a {
        margin-right: 30px;
        font-family: 'Noto Sans CJK KR', sans-serif;
        font-weight: bold;
        color: #2c3e50;
        text-decoration: none;
    }

    #nav2 a {
        margin-left: 20px;
        font-family: 'Noto Sans CJK KR', sans-serif;
        font-weight: bold;
        color: #2c3e50;
        text-decoration: none;
    }

    #nav2 .search-popup {
        align-items: center;
        display: inline-block;
        margin-left: -20px;
        margin-right: -50px;
    }

    #nav2 .search-popup input {
        width: 100%;
        height: 15px;
        padding: 5px 10px;
        border: none;
        background-color: #F6E0B3;
        border-radius: 30px;
    }

    .border-bedu {
        background-color: #F6E0B3;
        width: 70%;
        border-color: #EDC268;
    }

    .no-arrow::after {
        display: none;
    }

    .dropdown-menu-show{
        height: auto;
    }
</style>