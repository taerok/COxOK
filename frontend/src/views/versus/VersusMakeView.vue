<template>
  <div class="wrapper">
    <div class="page-header page-header-mini">
      <parallax
        class="page-header-image"
        :style="{ backgroundImage: 'url(http://i3a104.p.ssafy.io/header/versus.jpg)' }"
      ></parallax>

      <div class="content-center">
        <h1 class="title">요리대전</h1>
      </div>
    </div>

    <div id="idx-btn" class="row mt-5 text-left Katuri"> 
      <h4><i class="fas fa-utensils ml-1 mr-2"></i>선택된 레시피</h4>
      <div>
        <span class="idx-obj ml-5">{{selectedRecipes.length}}개 / 16개</span>
      </div>
    </div>

    <div class="section make-versus">
      <div class="container">
        <div class="button-container">
          <button class="learn-more submit" @click="submitVersus"><i class="far fa-check-circle"></i><p>등록</p></button>
          <button class="learn-more" @click="goBackPage"><i class="fas fa-ban"></i><p>취소</p></button>
        </div>
      </div>
    </div>


    <div class="container">
      <!-- <p class="selected-recipe-list-title">선택한 레시피 목록</p> -->
      <div class="paragraph">
        <div class="RecipeVersus">
            <div v-if="versusTitle === ''">
              [대회 이름을 입력하세요]
            </div>
            <div v-else>
              [{{versusTitle}}]
            </div>
        </div> 
        <div class="VersusList"> 
          <span>선택된 레시피 </span>
        </div>
      </div>
      <br>
      <div class="selected-recipe-list row">
        <div class="ml-auto mr-auto align-self-center">
         


          <b-button variant="primary" class="btn-circle" pill @click="movePrev">
            <i class="now-ui-icons arrows-1_minimal-left"></i>
          </b-button>
        </div>

        <div class="select-zero" v-if="selectedRecipes.length == 0">
            레시피를 선택하세요.
        </div>
        <div v-for="(recipe, index) in curRecipes" :key="index" class="col-md-4 col-lg-2">
          <card
            type="pricing"
            class="card-background selected-recipe-card"
            :style="'background-image: url('+ imageSrc(recipe.recipeThumbnailSrc) +')'"
          >
            <a class="selected-recipe-remove" @click="removeSelectedRecipe(index)">
              <i class="now-ui-icons ui-1_simple-remove"></i>
            </a>
            <h6 class="card-title">{{ recipe.recipeName }}</h6>
            <p class="card-description">{{ recipe.recipeDetail }}</p>
          </card>
        </div>

        <div class="ml-auto mr-auto align-self-center">
          <b-button variant="primary" class="btn-circle" pill @click="moveNext">
            <i class="now-ui-icons arrows-1_minimal-right"></i>
          </b-button>
        </div>
      </div>
    </div>
    <hr />
    
    <div class="container">
      <tabs
        class="row"
        pills
        type="primary"
        icons
        tab-nav-wrapper-classes="col-md-2"
        tab-content-classes="col-md-10"
        vertical
      >
        <tab-pane label="Profile">
          <template slot="label">
            <i class="now-ui-icons sport_trophy"></i> 요리 대전
          </template>

          <div class="container">

            <div class="versus-name">
              <label for="name" class="inp">
                <input type="text" id="name" placeholder=" " v-model="versusTitle">
                <span class="label">대회 이름</span>
                <span class="focus-bg"></span>
              </label>
            </div>

            <br />
            <br />

            <div class="versus-name">
              <label for="detail" class="inp">
                <input type="text" id="detail" placeholder=" " v-model="versusContent">
                <span class="label">대회 개요</span>
                <span class="focus-bg"></span>
              </label>
            </div>
          </div>

          <div style="height:300px"></div>
        </tab-pane>
        <tab-pane label="Settings">
          <template slot="label">
            <i class="now-ui-icons sport_user-run"></i> 선수 선발
          </template>
          <CategorySelector @searchRecipe="categorySubmit" />
          <br><br>

          <div v-for="recipe in recipes" :key="recipe.id" class="row">
            <img :src="imageSrc(recipe.recipeThumbnailSrc)" class="col-4" />
            <div class="col-7">
              <h3 class="row">{{ recipe.recipeName }}</h3>
              <p class="row">{{ recipe.recipeDetail }}</p>
            </div>
            <label class="toggle-control">
              <input type="checkbox" checked="checked" v-model="checker[recipe.recipeId]" @click="checkRecipe(recipe)">
              <span class="control"></span>
            </label>

            <!-- <div class="checkbox-plus-minus">
              <input type="checkbox" class="plus-minus" v-model="checker[recipe.recipeId]" @click="checkRecipe(recipe)">
            </div> -->
          </div>
          <div id="bottomSensor"></div>
        </tab-pane>
      </tabs>
    </div>
  </div>
</template>

<script>
import CategorySelector from "@/components/recipes/CategorySelector";

import { Tabs, TabPane, Card } from "@/components/global";

import { mapGetters, mapActions, mapMutations } from "vuex";
import SERVER from "@/api/api";

import axios from "axios";
import scrollmonitor from "scrollmonitor";

export default {
  name: "VersusMakeView",
  data() {
    return {
      widthInterval:'',
      versusTitle: "",
      versusContent: "",
      selectedRecipes: [],
      recipes: [],
      selectedCurPage: null,
      curPage: 1,
      maxPage: 10,
    };
  },
  components: {
    CategorySelector,
    Tabs,
    Card,
    TabPane,
  },
  computed: {
    ...mapGetters(["config", "searchingData"]),
    selectedRecipesId() {
      return this.selectedRecipes.map((recipe) => recipe.recipeId);
    },
    selectedMaxPage() {
      return parseInt((this.selectedRecipes.length - 1) / 4);
    },
    curRecipes() {
      return this.selectedRecipes.slice(
        4 * this.selectedCurPage,
        4 * (this.selectedCurPage + 1)
      );
    },
    checker() {
      let tempChecker = {};
      const self = this;

      this.recipes.forEach(function (recipe) {
        if (self.selectedRecipesId.indexOf(recipe.recipeId) >= 0) {
          tempChecker[recipe.recipeId] = true;
        } else {
          tempChecker[recipe.recipeId] = false;
        }
      });

      return tempChecker;
    },
  },
  created() {
    this.getRecipes();
    this.setSearchData({
      'selectedCategory': [],
      'selectedIngredients': [],
      'selectedIngredientsName': [],
      'level': 5,
      'cookTime': 120,
  })},
  mounted() {
    window.addEventListener("scroll", this.indexScrollFuncion);
    this.addScrollWatcher();
    this.winWidth();
  },
  beforeDestory() {
    window.removeEventListener('scroll', this.indexScrollFuncion);
    clearInterval(this.widthInterval);
  },
  updated() {
    if (this.curPage < this.maxPage) {
      this.loadUntilViewportIsFull();
    }
  },
  methods: {
    ...mapMutations(["setSearchData"]),
    ...mapActions(["logout"]),
    removeSelectedRecipe(index) {
      this.selectedRecipes.splice(this.selectedCurPage * 4 + index, 1);
    },
    imageSrc(image) {
      return SERVER.URL + "/img/" + image;
    },
    categorySubmit() {
      this.recipes = [];
      this.curPage = 1;
      if (
        (this.searchingData.selectedCategory.length +
          this.searchingData.selectedIngredients.length ==
        0) && (this.searchingData.level == 5) && (this.searchingData.cookTime == 120)
      ) {
        this.allRecipe(this.curPage++);
      } else {
        this.searchRecipe(this.curPage++);
      }
    },
    allRecipe(page) {
      axios
        .get(SERVER.URL + SERVER.ROUTES.recipeList + (page - 1))
        .then((res) => {
          this.recipes = [...this.recipes, ...res.data.list];
          this.maxPage = parseInt((res.data.total - 1) / 6);
        })
        .catch((err) => console.log(err));
    },
    searchRecipe(page) {
      let frm = new FormData();

      // categories 배열에는 선택된 카테고리의 ID가 String으로 들어갑니다.
      this.searchingData.selectedCategory.forEach(function (selectedCategory) {
        frm.append("selectedCategory", selectedCategory);
      });
      this.searchingData.selectedIngredients.forEach(function (
        selectedIngredient
      ) {
        frm.append("selectedIngredients", selectedIngredient);
      });

      frm.append("level", this.searchingData.level)

      frm.append("cookTime", this.searchingData.cookTime)

      // recipe/search/{{page}} 라는 주소로 selectedCategory(선택된 카테고리의 id들) / selectedIngredients(선택된 재료들의 id)를 전달합니다.
      axios
        .post(SERVER.URL + SERVER.ROUTES.searchRecipe + (page - 1), frm)
        .then((res) => {
          this.recipes = [...this.recipes, ...res.data.list];
          this.maxPage = parseInt((res.data.total - 1) / 6);
        })
        .catch((err) => console.log(err));
    },
    checkRecipe(recipe) {
      if (this.selectedRecipesId.indexOf(recipe.recipeId) == -1) {
        this.selectedRecipes.push(recipe);
      } else {
        this.selectedRecipes.splice(
          this.selectedRecipesId.indexOf(recipe.recipeId),
          1
        );
      }
    },
    movePrev() {
      if (this.selectedCurPage != 0) {
        this.selectedCurPage -= 1;
      } else {
        this.selectedCurPage = this.selectedMaxPage;
      }
    },
    moveNext() {
      if (this.selectedCurPage < this.selectedMaxPage) {
        this.selectedCurPage += 1;
      } else {
        this.selectedCurPage = 0;
      }
    },
    addScrollWatcher() {
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollmonitor.create(bottomSensor);
      if (this.curPage < this.maxPage) {
        watcher.enterViewport(() => {
          setTimeout(() => {
            this.getRecipes();
          }, 1000);
        });
      }
    },
    loadUntilViewportIsFull() {
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollmonitor.create(bottomSensor);
      if (watcher.isFullyInViewport) {
        this.getRecipes();
      }
    },
    getRecipes() {
      if (
        (this.searchingData.selectedCategory.length +
          this.searchingData.selectedIngredients.length ==
        0) && (this.searchingData.level == 5) && (this.searchingData.cookTime == 120)
      ) {
        this.allRecipe(this.curPage++);
      } else {
        this.searchRecipe(this.curPage++);
      }
    },
    scrollToTop() {
      scroll(0, 0);
    },
    indexScrollFuncion() {
      if(window.innerWidth > 1024 && document.getElementById("idx-btn") != null) {
        if (
          document.body.scrollTop > 400 ||
          document.documentElement.scrollTop > 400
        ) {
          document.getElementById("idx-btn").style.display = "block";
        } else {
          document.getElementById("idx-btn").style.display = "none";
        }
      }
    },
    scrollDoc(id) {
      if (id != "top") {
        var location = document.querySelector("#" + id).offsetTop-100;

        window.scrollTo({ top: location, behavior: "smooth" });
      } else {
        window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
      }
    },
    winWidth: function () { 
        this.widthInterval = setInterval(() => {
            var w = window.innerWidth;
            if (w < 1024 && document.getElementById("idx-btn") != null) {
              document.getElementById("idx-btn").style.display = "none";
            }
        }, 100);
    },
    submitVersus() {
      let versusData = {
        title: this.versusTitle,
        content: this.versusContent,
        recipeIdList: this.selectedRecipesId,
      };
      if (this.versusTitle == "" || this.versusContent == "") {
        alert('요리대전 제목과 설명을 확인해주세요.')
      } else if (this.selectedRecipes.length != 16) {
        alert('16개의 레시피를 선택해주세요!\n\n현재 ' + this.selectedRecipes.length + '개 선택')
      } else {
        axios
          .post(SERVER.URL + SERVER.ROUTES.versusRegister, versusData, {
            headers: {
              Authorization: this.config,
            },
          })
          .then(() => {
            alert('작성에 성공했습니다!')
            this.$router.push({ name: "VersusHomeView" });
          })
          .catch((err) => {
            if (err.response.status == 401) {
              alert('로그인 정보가 만료되었습니다! 다시 로그인해주세요.')
              this.logout()
            }});
      }
    },
    goBackPage() {
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped>
.wrapper{
  font-family: 'Katuri';
}

.make-versus .button-container {
  margin-top: -112px;
}
.selected-recipe-list {
  height: 240px;
}
.selected-recipe-list .selected-recipe-remove i {
  color: #aaaaaa;
}
.selected-recipe-list .selected-recipe-remove:hover i {
  color: #ffffff;
}
.selected-recipe-list .selected-recipe-remove {
  position: relative;
  top: -10px;
  right: -50px;
  cursor: pointer;
}
.btn-circle {
  width: 30px;
  height: 30px;
  padding: 6px 0px;
  border-radius: 15px;
  text-align: center;
  font-size: 12px;
  line-height: 1.42857;
}
.selected-recipe-card {
  width: 8rem;
  height: 12rem;
}



/* 대회 이름, 대회 개요 */
.versus-name {
  -webkit-text-size-adjust: 100%;
  -webkit-font-smoothing: antialiased;
}

.inp {
  position: relative;
  margin: auto;
  width: 100%;
  max-width: 700px;
  border-radius: 3px;
  overflow: hidden;
  box-sizing: border-box;
}
.inp .label {
  position: absolute;
  top: 20px;
  left: 12px;
  font-size: 16px;
  color: rgba(0, 0, 0, 0.5);
  font-weight: 500;
  -webkit-transform-origin: 0 0;
          transform-origin: 0 0;
  -webkit-transform: translate3d(0, 0, 0);
          transform: translate3d(0, 0, 0);
  -webkit-transition: all 0.2s ease;
  transition: all 0.2s ease;
  pointer-events: none;
}
.inp .focus-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: white;
  z-index: -1;
  -webkit-transform: scaleX(0);
          transform: scaleX(0);
  -webkit-transform-origin: left;
          transform-origin: left;
}
.inp input {
  -webkit-appearance: none;
  -moz-appearance: none;
       appearance: none;
  width: 100%;
  border: 0;
  font-family: inherit;
  padding: 16px 12px 0 12px;
  height: 56px;
  font-size: 16px;
  font-weight: 400;
  background: white;
  box-shadow: inset 0 -1px 0 rgba(0, 0, 0, 0.3);
  color: #000;
  -webkit-transition: all 0.15s ease;
  transition: all 0.15s ease;
}
.inp input:hover {
  background: white;
  box-shadow: inset 0 -1px 0 rgba(0, 0, 0, 0.5);
}
.inp input:not(:placeholder-shown) + .label {
  color: rgba(0, 0, 0, 0.5);
  -webkit-transform: translate3d(0, -12px, 0) scale(0.75);
          transform: translate3d(0, -12px, 0) scale(0.75);
}
.inp input:focus {
  background: white;
  outline: none;
  box-shadow: inset 0 -2px 0 #0077FF;
}
.inp input:focus + .label {
  color: #0077FF;
  -webkit-transform: translate3d(0, -12px, 0) scale(0.75);
          transform: translate3d(0, -12px, 0) scale(0.75);
}
.inp input:focus + .label + .focus-bg {
  -webkit-transform: scaleX(1);
          transform: scaleX(1);
  -webkit-transition: all 0.1s ease;
  transition: all 0.1s ease;
}


/* 선택한 레시피 목록 */
@import url('https://fonts.googleapis.com/css?family=Roboto:300');

.paragraph {
  text-align:center;
  color:rgb(56, 9, 25);
  font-family:'Katuri';
  font-weight:500;
  font-size:30px;
  overflow:hidden;
  -webkit-backface-visibility: hidden;
  -webkit-perspective: 1000;
  -webkit-transform: translate3d(0,0,0);
}


.RecipeVersus .VersusList {
  display:inline-block;
  overflow:hidden;
  white-space:nowrap;
}

.RecipeVersus {    /* For increasing performance 
                       ID/Class should've been used. 
                       For a small demo 
                       it's okaish for now */
  animation: showup 7s infinite;
}

.VersusList {
  width:0px;
  animation: reveal 7s infinite;
}

.VersusList span {
  margin-left:-100px;
  animation: slidein 7s infinite;
}

@keyframes showup {
    0% {opacity:0;}
    20% {opacity:1;}
    80% {opacity:1;}
    100% {opacity:0;}
}

@keyframes slidein {
    0% { margin-left:-800px; }
    20% { margin-left:-800px; }
    35% { margin-left:0px; }
    100% { margin-left:0px; }
}

@keyframes reveal {
    0% {opacity:0;width:0px;}
    20% {opacity:1;width:0px;}
    30% {width:100%;}
    80% {opacity:1;}
    100% {opacity:0;width:100%;}
}



/* 등록 취소 버튼 */
@import url("https://fonts.googleapis.com/css?family=Rubik:700&display=swap");
* {
  box-sizing: border-box;
}
*::before, *::after {
  box-sizing: border-box;
}

body {
  font-family: 'Rubik', sans-serif;
  font-size: 1rem;
  line-height: 1.5;
  display: -webkit-box;
  display: flex;
  -webkit-box-align: center;
          align-items: center;
  -webkit-box-pack: center;
          justify-content: center;
  margin: 0;
  min-height: 100vh;
  background: #fff;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
          flex-direction: column;
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
  
/* button.submit {
  color: #382b22;
  background: #D7FFF1;
  border: 2px solid #77AF9C;
}


button.submit::before {
  background: #67D5B5;
  box-shadow: 0 0 0 2px #77AF9C, 0 0.625em 0 0 #cff0da;
}


button.submit:hover {
  background: #D7FFF1;
}


button.submit:hover::before {
  box-shadow: 0 0 0 2px #77AF9C, 0 0.5em 0 0 #cff0da;
}


button.submit:active {
  background: #D7FFF1;
}


button.submit:active::before {
  box-shadow: 0 0 0 2px #77AF9C, 0 0 #cff0da;
} */



/* 옆에 목차? */
#idx-btn {
  position: fixed;
  left: 50px;
  top: 90px;
  z-index: 2;
  display:none;  
  padding: 0px 20px 20px 30px;
  border-radius: 20px;
  border: solid 1px lightgray;
}

.select-zero{
  padding-top: 110px;
}




/* 체크박스 */
.toggle-control {
  display: block;
  position: relative;
  padding-left: 10px;
  margin-bottom: 12px;
  cursor: pointer;
  font-size: 22px;
  -webkit-user-select: none;
     -moz-user-select: none;
      -ms-user-select: none;
          user-select: none;
}
.toggle-control input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
}
.toggle-control input:checked ~ .control {
  background-color: dodgerblue;
}
.toggle-control input:checked ~ .control:after {
  left: 55px;
}
.toggle-control .control {
  position: absolute;
  top: 0;
  left: 0;
  height: 50px;
  width: 100px;
  border-radius: 25px;
  background-color: darkgray;
  -webkit-transition: background-color 0.15s ease-in;
  transition: background-color 0.15s ease-in;
}
.toggle-control .control:after {
  content: "";
  position: absolute;
  left: 5px;
  top: 5px;
  width: 40px;
  height: 40px;
  border-radius: 25px;
  background: white;
  -webkit-transition: left 0.15s ease-in;
  transition: left 0.15s ease-in;
}

/* Center the control */
body {
  display: -webkit-box;
  display: flex;
  -webkit-box-pack: center;
          justify-content: center;
  -webkit-box-align: center;
          align-items: center;
  color: white;
}

</style>