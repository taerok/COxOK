<template>
  <div class="container">
    <div class="row">
      <p class="col-3">레시피 사진</p>
      <p class="col-4">레시피 정보</p>
      <p class="col-3">신고 정보</p>
      <p class="col-2">버튼</p>
    </div>

    <div v-for="recipe in curRecipes" :key="recipe.reportRecipeDto.reportRecipeId" class="row" >
      <img :src="imageSrc(recipe.recipeResponseDto.recipeThumbnailSrc)" class="col-3">
      <div class="col-4">
        <h3>{{ recipe.recipeResponseDto.recipeName }}</h3>
        <p>{{ recipe.recipeResponseDto.recipeDetail }}</p>
      </div>
      <div class="col-3">
        <p>신고자 : {{ recipe.reportRecipeDto.nickname }}님</p>
        <p>신고사유 : {{ recipe.reportRecipeDto.reason }}</p>
      </div>
      <div class="col-2">
        <router-link :to="{name: 'RecipeDetailView', params: {recipe_id: recipe.reportRecipeDto.recipeId} }"><button class="btn">상세 보기</button></router-link>
        <button class="btn" @click="deleteReport(recipe)">신고 취소</button>
        <button class="btn" @click="deleteRecipe(recipe)">레시피 삭제</button>
      </div>
    </div>

    <PageButton
      class="d-flex justify-content-center"
      :curPage="curPage"
      :maxPage="maxPage"
      @move-page="movePage"
    />
  </div>
</template>

<script>
import PageButton from "@/components/common/PageButtons.vue";

import SERVER from '@/api/api'
import axios from 'axios'

import { mapGetters } from 'vuex'

export default {
  name: 'AdminReportedRecipeList',
  data() {
    return {
      curPage: 1,
      maxPage: null,
      reportedRecipes: [],
    }
  },
  components: {
    PageButton
  },
  computed: {
    ...mapGetters(['config']),
    configs() {
      return { headers: {
        Authorization: this.config,
      }}
    },
    curRecipes() {
      return this.reportedRecipes.slice((this.curPage-1) * 10, this.curPage * 10)
    }
  },
  mounted() {
    this.getRecipes();
  },
  methods: {
    imageSrc(link) {
      return SERVER.IMAGE_URL + link
    },
    getRecipes() {
      axios
        .get(SERVER.URL + SERVER.ROUTES.adminGetReportedRecipes, this.configs)
        .then(res => {
          this.reportedRecipes = res.data.list
          this.maxPage = parseInt((res.data.list.length - 1) / 10) + 1
        })
        .catch((err) => {
          if (err.response.status == 401) {
            alert('로그인 정보가 만료되었습니다! 다시 로그인해주세요.')
            this.logout()
          }});
    },
    movePage(page) {
      if (page == "«") {
        this.curPage = 1
      } else if (page == "»") {
        this.curPage = this.maxPage
      } else if (page != this.curPage) {
        this.curPage = parseInt(page)
      }
      scroll(0, 0);
    },

    deleteReport(recipe) {
      let response = confirm('신고를 취소하시겠습니까?\n\n레시피 이름: ' + recipe.recipeResponseDto.recipeName)
      if (response) {
        axios
          .delete(SERVER.URL + SERVER.ROUTES.adminReportCancel + recipe.reportRecipeDto.reportRecipeId, this.configs)
          .then(() => {
            alert('신고를 삭제했습니다.')
            this.getRecipes();
          })
          .catch((err) => {
            if (err.response.status == 401) {
              alert('로그인 정보가 만료되었습니다! 다시 로그인해주세요.')
              this.logout()
            }});
      }
    },

    deleteRecipe(recipe) {
      let response = confirm('레시피를 삭제하시겠습니까?\n\n레시피 이름: ' + recipe.recipeResponseDto.recipeName)
      if (response) {
        axios
          .delete(SERVER.URL + SERVER.ROUTES.adminDeleteRecipe + recipe.reportRecipeDto.recipeId, this.configs)
          .then(() => {
            alert('해당 레시피를 삭제했습니다.')
            this.getRecipes();
          })
          .catch((err) => {
            if (err.response.status == 401) {
              alert('로그인 정보가 만료되었습니다! 다시 로그인해주세요.')
              this.logout()
            }});
      }
    }

  }
}
</script>

<style>

</style>