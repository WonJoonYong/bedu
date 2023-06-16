<template>
    <div id="loginChangeContainer">
        <div id="loginChangeSection">
            <button @click="$router.push('/login')" id="loLoginChangeBtn">
                로그인
            </button>
            <button @click="$router.push('/regist')" id="loRegistChangeBtn" class="registbtn">
                회원가입
            </button>
        </div>
        <div id="loginContainer">
            <div id="loginLogoContainer">
                <img id="loginLogo" src="@/assets/imgs/Logo2.png" /><br />
                <p>강의는 역시 B:EDU</p>
            </div>
            <div id="loginSection">
                <form class="login-form" @submit.prevent="fnLogin">
                    <div class="form-group">
                        <input
                            id="emailInputSection"
                            v-model="email"
                            placeholder="사용자 이메일"
                        />
                    </div>
                    <div class="form-group">
                        <input
                            id="pwdInputSection"
                            v-model="password"
                            type="password" 
                            placeholder="비밀번호"
                        />
                    </div>
                    <!-- <div id="idRememberSection">
                        <input
                            class="form-check-input"
                            type="checkbox"
                            ref="saveIdCheckbox"
                        />
                        <span style="cursor: pointer" @click="toggleCheckbox"
                            >아이디 저장</span
                        >
                    </div> -->
                    <div>
                        <button id="loginSubmitBtn" type="submit">로그인</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import '@/assets/css/userStyle.css';
import {mapActions, mapGetters} from 'vuex'

export default {
    data() {
        return {
            email: '',
            password: '',
        };
    },
    methods: {
        ...mapActions(['login']),

        // 체크박스 토글
        toggleCheckbox() {
            const checkbox = this.$refs.saveIdCheckbox;
            checkbox.checked = !checkbox.checked;
        },
        async fnLogin() {
        if (this.email === '') {
            alert('ID를 입력하세요.')
            return
        }

        if (this.password === '') {
            alert('비밀번호를 입력하세요.')
            return
        }

        try {
            let loginResult = await this.login({email: this.email, password: this.password})
            if (loginResult) this.goToPages()
        } catch (err) {
            if (err.message.indexOf('Network Error') > -1) {
                    alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
                } else {
                    alert('로그인 정보를 확인할 수 없습니다.')
                }
            }
        },
        goToPages() {
            this.$router.push({
                name: 'main'
            })
        }
    },
    computed: {
        ...mapGetters({
            errorState: 'getErrorState'
        })
    }
}

</script>
