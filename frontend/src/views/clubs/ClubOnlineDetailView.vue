<template>
  <div class="wrapper">
    <section id="top">
      <card
        type="background"
        :style="{ backgroundImage: 'url(http://i3a104.p.ssafy.io/header/meet.jpg)' }"
      >
        <div class="card-title text-left">
          <h1 style>{{ online.title }}</h1>
        </div>
        <div class="card-footer text-left">
          <div class="stats">
            <div class="card-footer text-left">
              <div class="stats">
                <i class="fas fa-edit"></i>
                {{ author.nickname }}
              </div>
            </div>
          </div>
        </div>
      </card>
      
    </section>

    <router-link
      v-if="userId == author.id"
      :to="{ name: 'ClubOnlineUpdateView', params: { club_id: online.onlineId } }"
      style="height:1px;">
      <div class="section online-view" style=" padding: 0;">
        <div class="container">
          <div class="button-container" style="height:1px;">
            <button class="learn-more submit"><i class="far fa-edit"></i><p>수정</p></button>
            <button class="learn-more" @click="deleteClub" style="margin-left: 20px;"><i class="fas fa-ban"></i><p>삭제</p></button>
          </div>
        </div>
      </div>
    </router-link>
    

    <div class="container">
      <h3 class="meet-subtitle text-left col-12">[{{ online.type }}]</h3>
      <div v-if="online.video != null">
        <div class="embed-responsive embed-responsive-16by9">
          <iframe class="embed-responsive-item" :src="videoUrl" allowfullscreen></iframe>
        </div>
      </div>
      <div v-else>
        <div class="live-div col-12">
          <p class="meet-content text-left col-12">강의 링크: <a :href="online.link" target="_blank"> {{ online.link }}</a></p>
          <p class="meet-content text-left col-12">날짜: {{ simpleDate }}</p>
          <p class="meet-content text-left col-12">시간: {{ simpleTime }}</p>
        </div>
      </div>
      
            
      <div v-if="author.id != userId" class="join-btn-div">
        <button class="btn" style="width:100%" @click="changeJoinClub">{{ joinMent }}</button>
      </div>

      <div class="col-12 mt-5">
        <h3 class="text-left">상세 내용</h3>
        <div class="detail-main-content" v-html="online.content" />
      </div>

      <div class="col-12">
        <h3 class="text-left mt-5">찜한 사람 ({{ joinList.length }})</h3>
        
        <div class="row">
        <p
            v-for="(user, index) in joinList"
            :key="user.userId"
            class="col-2"
          ><img :id="getUserIndex(index)" class="join-member-profile img-raised mb-5" :src="userProfile(index)" />
            <b-tooltip :target="getUserIndex(index)" triggers="hover" placement="bottom">
              {{user.nickname}}
            </b-tooltip>
          </p>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import SERVER from "@/api/api";
import axios from "axios";
import { mapGetters } from "vuex";

import { Card } from "@/components/global";

export default {
  name: "ClubOnlineDetailView",
  data() {
    return {
      online: {
        onlineId: 0,
        title: null,
        content: null,
        type: null,
        date: "",
        link: null,
        thumbnailSrc: "dochi.png",
        video: null,
      },
      joinList: [],
      author: {
        id: null,
        nickname: null,
      },
      userId: null,
      isIn: false,
    };
  },
  components: {
    Card,
  },
  computed: {
    ...mapGetters(["config"]),
    videoUrl() {
      return `https://youtube.com/embed/${this.online.video}`;
    },
    joinMent() {
      return this.isIn ? "찜 취소하기" : "강의 찜하기";
    },
    simpleDate() {
      return this.online.date.slice(0,10);
    },
    simpleTime() {
      return this.online.date.slice(11,16);
    },
  },
  created() {},
  mounted() {
    this.getInfo();
  },
  methods: {
    getInfo() {
      axios
      .get(
        SERVER.URL + SERVER.ROUTES.onlineDetail + this.$route.params.club_id,
        {
          headers: {
            Authorization: this.config,
          },
        }
      )
      .then((res) => {
        this.online = res.data.online;
        this.author = {
          id: res.data.online.userId,
          nickname: res.data.writerNickname,
        };
        this.userId = res.data.userId;
        this.joinList = res.data.joinList;
        this.isIn =
            res.data.joinList
              .map((user) => user.userId)
              .indexOf(res.data.userId) >= 0
              ? true
              : false;
      })
      .catch((err) => {
        if (err.response.status == 401) {
          alert('로그인 정보가 만료되었습니다! 다시 로그인해주세요.')
          this.logout()
        }});

    },

    deleteClub() {
      let response = confirm("삭제하실 건가요?");
      if (response) {
        axios
          .delete(
            SERVER.URL +
              SERVER.ROUTES.onlineDelete +
              this.$route.params.club_id,
            {
              headers: {
                Authorization: this.config,
              },
            }
          )
          .then(() => {
            alert("삭제에 성공했습니다!");
            this.$router.push({ name: "ClubListView", params: { pageNum: 1 } });
          })
          .catch((err) => {
            if (err.response.status == 401) {
              alert('로그인 정보가 만료되었습니다! 다시 로그인해주세요.')
              this.logout()
            }});

      }
    },

    changeJoinClub() {
      if (this.isIn) {
        axios
          .delete(
            SERVER.URL +
              SERVER.ROUTES.cancelOnline +
              this.$route.params.club_id,
            {
              headers: {
                Authorization: this.config,
              },
            }
          )
          .then(() => {
            alert("찜 목록에서 삭제하셨습니다.");
            this.isIn = false;
            this.getInfo();
          })
          .catch((err) => console.log(err.response));
      } else {
        axios
          .post(
            SERVER.URL + SERVER.ROUTES.joinOnline,
            { onlineId: this.$route.params.club_id },
            {
              headers: {
                Authorization: this.config,
              },
            }
          )
          .then(() => {
            alert("강의를 찜 하셨습니다.");
            this.isIn = true;
            this.getInfo();
          })
          .catch((err) => {
            if (err.response.status == 401) {
              alert('로그인 정보가 만료되었습니다! 다시 로그인해주세요.')
              this.logout()
            }});

      }
      this.getInfo();
    },
    userProfile(index) {
      return SERVER.IMAGE_URL + this.joinList[index].profilePhoto;
    },
    getUserIndex(index){
      return "user-profile" + index;
    },
    
  },
};
</script>

<style scoped>
.stats {
  font-size: 25px;
}

.detail-main-content {
  min-height: 200px;
  margin-bottom: 10px;
}

button {
  position: relative;
  display: inline-block;
  cursor: pointer;
  outline: none;
  border: 0;
  vertical-align: middle;
  text-decoration: none;
}
button.learn-more {
  font-weight: 600;
  height: 60px;
  color: #382b22;
  text-transform: uppercase;
  padding: 0.3em 1.5em;
  background: #f2efe4;
  border: 2px solid #b69f81;
  border-left: 0;
  border-radius: 0;
  -webkit-transform-style: preserve-3d;
  transform-style: preserve-3d;
  -webkit-transition: background 150ms cubic-bezier(0, 0, 0.58, 1),
    -webkit-transform 150ms cubic-bezier(0, 0, 0.58, 1);
  transition: background 150ms cubic-bezier(0, 0, 0.58, 1),
    -webkit-transform 150ms cubic-bezier(0, 0, 0.58, 1);
  transition: transform 150ms cubic-bezier(0, 0, 0.58, 1),
    background 150ms cubic-bezier(0, 0, 0.58, 1);
  transition: transform 150ms cubic-bezier(0, 0, 0.58, 1),
    background 150ms cubic-bezier(0, 0, 0.58, 1),
    -webkit-transform 150ms cubic-bezier(0, 0, 0.58, 1);
  bottom: 70px;
  z-index: 2;
}
button.learn-more:first-child {
  border-top-left-radius: 0.75em;
  border-bottom-left-radius: 0.75em;
  padding-left: 2em;
  border-left: 2px solid #b69f81;
}
button.learn-more:last-child {
  border-top-right-radius: 0.75em;
  border-bottom-right-radius: 0.75em;
  padding-right: 2em;
  right: 1.5em;
}
button.learn-more::before {
  position: absolute;
  content: "";
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: #f2d4ae;
  border-radius: inherit;
  box-shadow: 0 0 0 2px #b69f81, 0 0.625em 0 0 #f2f0ce;
  -webkit-transform: translate3d(0, 0.75em, -1em);
  transform: translate3d(0, 0.75em, -1em);
  -webkit-transition: box-shadow 150ms cubic-bezier(0, 0, 0.58, 1),
    -webkit-transform 150ms cubic-bezier(0, 0, 0.58, 1);
  transition: box-shadow 150ms cubic-bezier(0, 0, 0.58, 1),
    -webkit-transform 150ms cubic-bezier(0, 0, 0.58, 1);
  transition: transform 150ms cubic-bezier(0, 0, 0.58, 1),
    box-shadow 150ms cubic-bezier(0, 0, 0.58, 1);
  transition: transform 150ms cubic-bezier(0, 0, 0.58, 1),
    box-shadow 150ms cubic-bezier(0, 0, 0.58, 1),
    -webkit-transform 150ms cubic-bezier(0, 0, 0.58, 1);
}
button.learn-more:hover {
  background: #f2efe4;
  -webkit-transform: translate(0, 0.25em);
  transform: translate(0, 0.25em);
}
button.learn-more:hover::before {
  box-shadow: 0 0 0 2px #b69f81, 0 0.5em 0 0 #f2f0ce;
  -webkit-transform: translate3d(0, 0.5em, -1em);
  transform: translate3d(0, 0.5em, -1em);
}
button.learn-more:active {
  background: #f2efe4;
  -webkit-transform: translate(0em, 0.75em);
  transform: translate(0em, 0.75em);
}
button.learn-more:active::before {
  box-shadow: 0 0 0 2px #b69f81, 0 0 #f2f0ce;
  -webkit-transform: translate3d(0, 0, -1em);
  transform: translate3d(0, 0, -1em);
}

.join-btn-div, .live-div {
  font-size:20px;
}

.join-member-profile {
  width:60px;
  height:60px;
  border-radius:100%;
}
</style>