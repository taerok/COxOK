<template>
  <div class="wrapper">
    <div class="page-header page-header-mini">
      <parallax
        class="page-header-image"
        :style="{ backgroundImage: 'url(http://i3a104.p.ssafy.io/header/cook.jpg)' }"
      ></parallax>
      <div class="content-center">
        <h1>레시피 상세 정보 입력</h1>
      </div>
    </div>

    <div class="section make-recipe"  style="padding:0px;">
      <div class="container2" style="height:0px;">
        <div class="button-container" style="height:0px;">
          <button class="learn-more submit" @click="preTest"><i class="far fa-check-circle"></i><p>등록</p></button>
          <button class="learn-more" @click="goBackPage"><i class="fas fa-ban"></i><p>취소</p></button>
        </div>
      </div>
    </div>

    <div class="section section-about-us">
      <div class="container">
        <div class="row">
          <div class="col-md-8 ml-auto mr-auto text-center">
            <div class="row">
              <input
                ref="thumbnailInput"
                type="file"
                name="photo"
                id="recipe_thumbnail"
                hidden
                @change="onChangeThumbnail"
              />
              <!-- <div class="row"> -->
                <div class="col-5">
                  <img id="recipe-preview" :src="recipePreview" class="w-100" style="height:200px;"/>
                  <br><br>
                  <a @click="onClickThumbnailUpload" class="w-100">
                    <n-button type="primary" round block>메인 사진 업로드</n-button>
                  </a>
                </div>
              <!-- </div> -->
            <!-- </div> -->
            <br><br>
            <!-- <div class="row"> -->
              <div class="col-7 detail-input">
                <div class="versus-name">
                  <label for="name" class="inp">
                    <input type="text" id="name" placeholder=" " v-model="recipeName" required autocomplete="off">
                    <span class="label">레시피 제목</span>
                    <span class="focus-bg"></span>
                  </label>
                </div>
                <div class="versus-name">
                  <label for="msg" class="inp">
                    <input type="text" id="msg" placeholder=" " v-model="recipeDetail" required autocomplete="off">
                    <span class="label">레시피 내용</span>
                    <span class="focus-bg"></span>
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>

        <hr class="my-5" />
        
        <br><br><br><br><br><br><br><br> 

        <div class="row">
          <div class="col-12">
            <div id="slider">
              <div id="sliderContainer">
                <div class="tick-slider">
                  <div class="tick-slider-header">
                    <h5><label for="weightSlider">난이도</label></h5>
                    <h5>level</h5>
                  </div>
                  <div class="tick-slider-value-container">
                    <div id="weightLabelMin" class="tick-slider-label">1</div>
                    <div id="weightLabelMax" class="tick-slider-label">5</div>
                    <div id="weightValue" class="tick-slider-value"></div>
                  </div>
                  <div class="tick-slider-background"></div>
                  <div id="weightProgress" class="tick-slider-progress"></div>
                  <div id="weightTicks" class="tick-slider-tick-container"></div>
                  <input
                      id="weightSlider"
                      class="tick-slider-input"
                      type="range"
                      min="1"
                      max="5"
                      step="1"
                      value="3"
                      v-model="level"
                      data-tick-step="5"
                      data-tick-id="weightTicks"
                      data-value-id="weightValue"
                      data-progress-id="weightProgress"
                      data-handle-size="18"
                      data-min-label-id="weightLabelMin"
                      data-max-label-id="weightLabelMax"
                    />
                </div>
                <div class="tick-slider">
                  <div class="tick-slider-header">
                  <h5><label for="sizeSlider">소요 시간</label></h5>
                  <h5>분</h5>
                </div>
                <div class="tick-slider-value-container">
                  <div id="sizeLabelMin" class="tick-slider-label">0</div>
                  <div id="sizeLabelMax" class="tick-slider-label">120</div>
                  <div id="sizeValue" class="tick-slider-value"></div>
                </div>
                <div class="tick-slider-background"></div>
                <div id="sizeProgress" class="tick-slider-progress"></div>
                <div id="sizeTicks" class="tick-slider-tick-container"></div>
                <input
                    id="sizeSlider"
                    class="tick-slider-input"
                    type="range"
                    min="0"
                    max="120"
                    step="5"
                    value="0"
                    v-model="cookTime"
                    data-tick-step="5"
                    data-tick-id="sizeTicks"
                    data-value-id="sizeValue"
                    data-progress-id="sizeProgress"
                    data-handle-size="18"
                    data-min-label-id="sizeLabelMin"
                    data-max-label-id="sizeLabelMax"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>


        
        <br><br><br><br><br><br><br><br> 

        <hr class="my-5" />

        <div>
          <h3 class="text-left">카테고리 선택</h3>
          <div class="row">
            <div
              v-for="category in categories"
              :key="category.foodCategoryId"
              class="col-2 d-flex justify-content-start"
            > 
              <p @click="selectCategory(category.foodCategoryId)" class="custom-cursor">
                <input
                  v-model="checker[category.foodCategoryId]"
                  type="checkbox"
                  class="mr-2"
                />
                {{ category['foodCategoryName']}}
              </p>
            </div>
          </div>
        </div>

        <hr class="my-5" />

        <div>
          <h3 class="text-left" style="float:left">재료 선택</h3>
          <div class="add-btn">
            <n-button @click.native="showModal" type="primary" round class="btn">
                재료가 없어요!
            </n-button>
          </div>
          <div class="ingredient-select">
            <div class="row" v-for="(selectedIngredient, index) in selectedIngredients" :key="index">
              <b-form-input
                :list="getIngredientDatalistId(index)"
                :id="getIngredientInputId(index)"
                v-model="selectedIngredient.ingredient"
                @change="test(selectedIngredient)"
                class="col-4 text-center"
                style="margin-top: 24px"
              />
              <b-form-datalist :id="getIngredientDatalistId(index)" :options="ingredientsName"></b-form-datalist>
              <div v-if="selectedIngredient.unit == null" class="col-3" style="margin-top: 16px">
                <i class="fas fa-exclamation-triangle fa-2x" style="color: rgba(236, 240, 12, 0.959;" id="no-ingredient"></i>
              </div>
              
              <div v-else class="col-5 m-0 row">
                <fg-input type="number" v-model="selectedIngredient.amount" min="0" class="col-8 offset-1" />
                <p class="col-3">{{ selectedIngredient.unit }}</p>
              </div>
              <a @click="removeIngredient(index)">
                <b-button variant="danger" style="margin-top: 24px">재료 삭제</b-button>
              </a>
            </div>
          </div>

          <div class="row">
            <div class="col-3" />
            <n-button @click.native.prevent="addIngredient" class="col-6" type="primary">
              <i class="now-ui-icons ui-1_simple-add"></i>
            </n-button>
          </div>
          
          <b-modal hide-footer ref="ingredient-modal" title="재료 추가 요청">
            <p class="my-4">추가할 재료명과 단위를 올려주세요!</p>

            <b-form-input v-model="newIngredient" placeholder="재료명을 적어주세요"></b-form-input>
            <b-form-select v-model="newIngredientUnit" :options="units"></b-form-select>

            <n-button type="danger" round block @click.native="sendData">제출</n-button>
          </b-modal>
        </div>

        <hr class="my-5" />

        <h3 class="text-left">상세 과정 입력</h3>

        <div v-show="tempInputs.length > 0" class="upload-image">
          <draggable v-model="tempInputs" group="recipeDetail" @start="drag=true" @end="drag=false">
            <div v-for="(tempInput, index) in tempInputs" :key="index" class="my-3 row">
              <div class="col-10 row">
                <img :src="tempInput.imageSrc" class="col-3 offset-2">
                <textarea v-model="tempInput.content" rows="4" class="col-5 offset-1 border align-self-center" />
              </div>
              <button class="btn btn-danger col-1 align-self-start mt-5" @click="removeRecipeDetail(index)">-</button>
            </div>
          </draggable>
        </div>

        <div class="row">
          <div class="col-3">
          </div>
          <div class="input-group col-9">
            <input
              type="text"
              class="form-control-file detail-image-upload"
              placeholder="드래그 & 드랍하거나 오른쪽 버튼을 클릭하세요"
              v-model="filename"
              @dragover.prevent
              @drop.prevent="onDrop"
              multiple>
            <div class="input-group-append col-1">
              <span @click="onClickFile">
                <i class="fa fa-cloud" />
              </span>
            </div>
            <input hidden type=file class="file-input" ref="fileInput" multiple @change="onFileChange">
          </div>
        </div>
        
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router";
import SERVER from "@/api/api";

import { mapGetters, mapActions } from "vuex";

import axios from "axios";
import draggable from 'vuedraggable'
import { Button, FormGroupInput as FgInput } from "@/components/global";

const API_KEY = process.env.VUE_APP_YOUTUBE_API_KEY
const API_URL = 'https://www.googleapis.com/youtube/v3/search'

export default {
  name: "RecipeMakeView",
  data() {
    return {
      recipeName: null,
      recipeDetail: null,
      recipePreview: "https://www.sylff.org/wp-content/uploads/2016/04/noImage.jpg",
      level: 1,
      cookTime: 60,
      categories: [],
      ingredients: {},
      ingredientsName: [],
      selectedCategories: [],
      selectedIngredients: [{ ingredient: null, amount: null, unit: null }],
      filename: '',
      addingFiles: [],
      fileList: [],
      tempInputs: [],
      units: ['g', '큰술', '작은술', '꼬집', '봉', '개', 'ml', '장'],
      newIngredient: null,
      newIngredientUnit: null,
    };
  },
  components: {
    FgInput,
    [Button.name]: Button,
    draggable,
  },
  computed: {
    ...mapGetters(["config"]),
    checker() {
      let tempChecker = {};
      const self = this;
      this.categories.forEach(function (category) {
        if (self.selectedCategories.indexOf(category.foodCategoryId) >= 0) {
          tempChecker[category.foodCategoryId] = true;
        } else {
          tempChecker[category.foodCategoryId] = false;
        }
      });

      return tempChecker;
    },
  },
  created() {
    this.getCategory();
    window.onload = this.init
  },
  mounted:function(){
    window.onload(this.init);
    window.addEventListener('resize', this.onResize)
  },
  methods: {
    goBackPage() {
      this.$router.go(-1);
    },
    ...mapActions(['logout']),
    // 썸네일 변경 및 미리보기 관련 함수
    onClickThumbnailUpload() {
      this.$refs.thumbnailInput.click();
    },
    onChangeThumbnail() {
      const file = event.target.files[0];
      this.recipePreview = URL.createObjectURL(file);

      const previewImage = document.querySelectorAll("#recipe-preview");
      previewImage.src = this.recipePreview;
    },

    // 카테고리 선택 관련 함수
    selectCategory(id) {
      if (this.selectedCategories.indexOf(id) >= 0) {
        this.selectedCategories.splice(this.selectedCategories.indexOf(id), 1);
      } else {
        this.selectedCategories.push(id);
      }
    },

    // 재료 선택 관련 함수
    test(ingredient) {
      if (this.ingredientsName.indexOf(ingredient.ingredient) == -1) {
        ingredient.unit = null;
      } else {
        ingredient.unit = this.ingredients[ingredient.ingredient][1];
      }
    },
    addIngredient() {
      let tempfile = { ingredient: null, amount: null, unit: null };
      this.selectedIngredients.push(tempfile);
    },
    removeIngredient(index) {
      this.selectedIngredients.splice(index, 1);
    },

    // 레시피 상세 과정 관련 함수
    onDrop (event) {
      this.inputImageFile(event.dataTransfer.files)
    },
    onClickFile() {
      this.$refs.fileInput.click()
    },
    onFileChange(event) {
      this.inputImageFile(event.target.files)
    },
    inputImageFile (addingfiles) {
      this.addingFiles = addingfiles
      this.fileList = [...this.fileList, ...addingfiles]
      this.onClickUpload()
    },
    onClickUpload () {
      const self = this
      this.addingFiles.forEach(function(file) {
        self.tempInputs.push(
          {'imageSrc': URL.createObjectURL(file), 'content': null, 'rawFile': file}
          )
      })
    },
    removeRecipeDetail(index) {
      this.tempInputs.splice(index, 1);
    },

    // 제출 전 유효성 확인 함수
    preTest() {
      let problems = [];
      let photoFile = document.getElementById("recipe_thumbnail");
      if (photoFile.files.length == 0 || this.recipePreview === "https://www.sylff.org/wp-content/uploads/2016/04/noImage.jpg") {
        problems.push("레시피 썸네일");
      }
      if (this.recipeName == null) { problems.push("레시피 제목"); }
      if (this.recipeDetail == null) { problems.push("레시피 설명"); }
      if (this.level == 0) { problems.push("요리 난이도"); }
      if (this.cookTime == null || this.cookTime < 0) { problems.push("요리 소요 시간"); }
      if (this.selectedCategories.length == 0) { problems.push("카테고리");}
      this.selectedIngredients.forEach(function (selectedIngredient, index) {
        if (selectedIngredient.unit == null) {
          problems.push(index + 1 + "번째 재료");
        } else if (
          selectedIngredient.amount == null ||
          selectedIngredient.amount <= 0
        ) {
          problems.push(index + 1 + "번째 재료의 양");
        }
      });

      if (this.tempInputs.length == 0 ) { problems.push('상세 과정') }

      this.tempInputs.forEach(function (tempInput, index) {
        if (tempInput.rawFile == null) {
          problems.push(index + 1 + "번째 상세 과정의 사진");
        }
        if (tempInput.content == null) {
          problems.push(index + 1 + "번째 상세 과정의 설명");
        }
      });

      if (problems.length == 0) {
        this.makeRecipe();
      } else {
        alert("필수 항목이 누락되었습니다!\n\n누락된 항목: " + problems);
      }
    },
    makeRecipe() {
      // formData를 이용해서 여러 데이터와 파일을 string / blob 형식으로 보내줍니다.
      let frm = new FormData();
      let photoFile = document.getElementById("recipe_thumbnail");
      frm.append("recipeThumbnail", photoFile.files[0]);
      frm.append("recipeName", this.recipeName);
      frm.append("recipeDetail", this.recipeDetail);

      // 요리 난이도, 요리 소요 시간도 String으로 보내줍니다.
      frm.append("level", this.level);
      frm.append("cookTime", this.cookTime);

      // categories 배열에는 선택된 카테고리의 ID가 String으로 들어갑니다. / 숫자로 넣는건 테스트를 해봐야됩니다.abnf
      // 아래 반복문을 이렇게 고쳐보세요 frm.append('categories', selectedCategoryId * 1);
      this.selectedCategories.forEach(function (selectedCategory) {
        frm.append("categories", selectedCategory);
      });

      const self = this;
      // 재료의 경우 ingredientPk, ingredientAmount 2개의 배열로 나뉘어서 들어갑니다.
      this.selectedIngredients.forEach(function (ingredient) {
        frm.append("ingredientPk", self.ingredients[ingredient.ingredient][0]);
        frm.append("ingredientAmount", ingredient.amount);
      });

      // 상세 정보의 경우에도 description 배열에 String으로 들어갑니다.
      // 사진의 경우 photo배열에 Blob 파일로 들어갑니다.

      this.tempInputs.forEach(function (tempInput) {
        frm.append("description", tempInput.content);
        frm.append("photo", tempInput.rawFile);
      });

      let configs = {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: this.config,
        },
      };

      axios
        .post(SERVER.URL + SERVER.ROUTES.recipeRegister, frm, configs)
        .then((res) => {
          const curRecipeId = res.data.recipeId

          axios
            .get(API_URL, {
              params: {
                key: API_KEY,
                part: 'snippet',
                type: 'video',
                q: self.recipeName
              }
            })
            .then(response => {
              let videoIds = response.data.items.slice(0,3).map(video => video.id.videoId)
              let videoUrls = response.data.items.slice(0,3).map(video => video.snippet.thumbnails.medium.url)
              let body = {
                'videoId': videoIds,
                'thumbnailSrc': videoUrls,
                'recipeId': curRecipeId
              }
              axios
                .post(SERVER.URL + SERVER.ROUTES.addYoutube, body)
                .then(() => {
                  alert('레시피 등록에 성공했습니다!')
                })
                .catch(err => console.log(err))
            })
            .catch(() => {
              alert('레시피 등록에 성공했습니다.\n\n(관련 유튜브는 추후 추가해드리겠습니다.)')
            })
          router.push({ name: "PrevRecipeList" });
        })
        .catch((err) => {
          if (err.response.status == 401) {
            alert('로그인 정보가 만료되었습니다! 다시 로그인해주세요.')
            this.logout()
          }});
    },
    getIngredientInputId(index) {
      return "input-with-list" + index;
    },
    getIngredientDatalistId(index) {
      return "input-list" + index;
    },
    getCategory() {
      axios
        .get(SERVER.URL + SERVER.ROUTES.goRegister, {
        headers: {
          Authorization: this.config,
        }})
        .then((res) => {
          this.categories = res.data.catogories;
          const self = this;
          res.data.ingredients.forEach(function (ingredient) {
            self.ingredients[ingredient.name] = [
              ingredient.ingredientId,
              ingredient.unit,
            ];
          });
          this.ingredientsName = Object.keys(this.ingredients);
        })
        .catch((err) => {
          if (err.response.status == 401) {
            alert('로그인 정보가 만료되었습니다! 다시 로그인해주세요.')
            this.logout()
          }});
    },

    // 난이도, 소요시간
    init() {
      const sliders = document.getElementsByClassName("tick-slider-input");

      for (let slider of sliders) {
        slider.oninput = this.onSliderInput;

        this.updateValue(slider);
        this.updateValuePosition(slider);
        this.updateLabels(slider);
        this.updateProgress(slider);

        this.setTicks(slider);
      }
    },

    onSliderInput(event) {
        this.updateValue(event.target);
        this.updateValuePosition(event.target);
        this.updateLabels(event.target);
        this.updateProgress(event.target);
    },

    updateValue(slider) {
        let value = document.getElementById(slider.dataset.valueId);

        value.innerHTML = "<div>" + slider.value + "</div>";
    },

    updateValuePosition(slider) {
        let value = document.getElementById(slider.dataset.valueId);

        const percent = this.getSliderPercent(slider);
        const sliderWidth = slider.getBoundingClientRect().width;
        const valueWidth = value.getBoundingClientRect().width;
        const handleSize = slider.dataset.handleSize;

        let left = percent * (sliderWidth - handleSize) + handleSize / 2 - valueWidth / 2;

        left = Math.min(left, sliderWidth - valueWidth);
        left = slider.value === slider.min ? 0 : left;

        value.style.left = left + "px";
    },

    updateLabels(slider) {
        const value = document.getElementById(slider.dataset.valueId);
        const minLabel = document.getElementById(slider.dataset.minLabelId);
        const maxLabel = document.getElementById(slider.dataset.maxLabelId);

        const valueRect = value.getBoundingClientRect();
        const minLabelRect = minLabel.getBoundingClientRect();
        const maxLabelRect = maxLabel.getBoundingClientRect();

        const minLabelDelta = valueRect.left - (minLabelRect.left);
        const maxLabelDelta = maxLabelRect.left - valueRect.left;

        const deltaThreshold = 32;

        if (minLabelDelta < deltaThreshold) minLabel.classList.add("hidden");
        else minLabel.classList.remove("hidden");

        if (maxLabelDelta < deltaThreshold) maxLabel.classList.add("hidden");
        else maxLabel.classList.remove("hidden");
    },

    updateProgress(slider) {
        let progress = document.getElementById(slider.dataset.progressId);
        const percent = this.getSliderPercent(slider);

        progress.style.width = percent * 100 + "%";
    },

    getSliderPercent(slider) {
        const range = slider.max - slider.min;
        const absValue = slider.value - slider.min;

        return absValue / range;
    },

    setTicks(slider) {
        let container = document.getElementById(slider.dataset.tickId);
        const spacing = parseFloat(slider.dataset.tickStep);
        const sliderRange = slider.max - slider.min;
        const tickCount = sliderRange / spacing + 1; // +1 to account for 0

        for (let ii = 0; ii < tickCount; ii++) {
            let tick = document.createElement("span");

            tick.className = "tick-slider-tick";

            container.appendChild(tick);
        }
    },

    onResize() {
        const sliders = document.getElementsByClassName("tick-slider-input");

        for (let slider of sliders) {
            this.updateValuePosition(slider);
        }
    },
    showModal() {
      this.$refs['ingredient-modal'].show()
    },
    hideModal() {
      this.$refs['ingredient-modal'].hide()
    },
    sendData() {
      if (this.ingredientsName.indexOf(this.newIngredient) >= 0) {
        alert('이미 존재하는 재료입니다!!')
      } else {
        let body = {
          name: this.newIngredient,
          unit: this.newIngredientUnit
        }

        let configs = {
          headers: {
            Authorization: this.config,
          },
        }

        axios
          .post(SERVER.URL + SERVER.ROUTES.userAddIngredient, body, configs)
          .then(() => {
            alert('재료 추가 요청에 성공했습니다!')
            this.hideModal()
            this.getCategory()
            this.newIngredient = null
            this.newIngredientUnit = null
          })
          .catch((err) => {
            if (err.response.status == 401) {
              alert('로그인 정보가 만료되었습니다! 다시 로그인해주세요.')
              this.logout()
            }});
      }
    }
  },
};
</script>

<style scoped>
.custom-cursor {
  cursor: pointer;
}

.detail-image-upload{
  text-align: center;
  width: 60%;
  border: solid 1px;
}

/*
Basic input element using psuedo classes
*/


.detail-input input,
.detail-input span,
.detail-input label,
.detail-input textarea {
  font-family: "Ubuntu", sans-serif;
  display: block;
  margin: 10px;
  padding: 5px;
  border: none;
  font-size: 22px;
}

textarea:focus,
input:focus {
  outline: 0;
}

/* 제목, 내용 입력 */

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




/* 난이도, 소요시간 */
@import url("https://fonts.googleapis.com/css?family=Hind+Madurai:300,600|Poppins:300&display=swap");

.wrapper {
  --yellow: #ffd049;
  --light-yellow: #fdf2d2;
  --orange: #ffa929;
  --light-gray: #e3e4e8;
  --gray: #71738b;
  --light-blue: #7a7c93;
  --blue: #34385a;

  --slider-handle-size: 14px;
  --slider-handle-border-radius: 2px;
  --slider-handle-margin-top: -4px;
  --slider-track-height: 6px;
  --slider-track-border-radius: 4px;
}

#slider {
  position: absolute;
  width: 100%;
  height: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
}

#sliderContainer {
  width: 100%;
  max-width: 440px;

  padding: 56px 40px;

  border-radius: 40px;

  box-shadow: 0px 8px 40px rgba(128, 128, 128, 0.15);
}

#sliderContainer > div:first-child {
  margin-bottom: 48px;
}

.tick-slider-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 24px;
}

.tick-slider-header > h5 {
  margin: 0;

  font-family: "Poppins", sans-serif;
  font-size: 18px;
  font-weight: 300;
  color: var(--gray);
}

.tick-slider {
  position: relative;

  width: 100%;
}

.tick-slider-value-container {
  position: relative;
  width: 100%;

  display: flex;
  justify-content: space-between;
  align-items: center;

  margin-bottom: 12px;

  font-family: "Hind Madurai", sans-serif;
  font-size: 18px;
  color: var(--gray);
}

.tick-slider-value {
  position: absolute;
  top: 0;

  font-weight: bold;

  color: var(--blue);

  border-radius: var(--slider-handle-border-radius);
}

.tick-slider-value > div {
  animation: bulge 0.3s ease-out;
}

.tick-slider-background,
.tick-slider-progress,
.tick-slider-tick-container {
  position: absolute;
  bottom: 5px;
  left: 0;

  height: var(--slider-track-height);

  pointer-events: none;

  border-radius: var(--slider-track-border-radius);

  z-index: 0;
}

.tick-slider-background {
  width: 100%;
  background-color: var(--light-gray);
}

.tick-slider-progress {
  background-color: var(--yellow);
}

.tick-slider-tick-container {
  width: 100%;

  display: flex;
  justify-content: space-between;
  align-items: center;

  padding: 0 calc(var(--slider-handle-size) / 2);
}

.tick-slider-tick {
  width: 2px;
  height: 2px;

  border-radius: 50%;

  background-color: white;
}

.tick-slider-label {
  opacity: 0.85;
  transition: opacity 0.1s ease;
}

.tick-slider-label.hidden {
  opacity: 0;
}

@keyframes bulge {
  0% {
    transform: scale(1);
  }

  25% {
    transform: scale(1.1);
  }

  100% {
    transform: scale(1);
  }
}

/*

    REMOVE SLIDER STYLE DEFAULTS

*/
input[type="range"] {
  -webkit-appearance: none;

  width: 100%;
  height: 100%;

  background: transparent;
  outline: none;

  margin: 5px 0;
}

input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;

  border: none;
}

input[type="range"]:focus {
  outline: none;
}

input[type="range"]::-moz-focus-outer {
  border: 0;
}

/*

    HANDLE

*/
input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;

  width: var(--slider-handle-size);
  height: var(--slider-handle-size);

  background: var(--orange);

  border-radius: var(--slider-handle-border-radius);

  cursor: pointer;

  margin-top: var(--slider-handle-margin-top);

  -webkit-transform: scale(1);
  transform: scale(1);

  transition: transform 0.25s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

input[type="range"]:hover::-webkit-slider-thumb,
input[type="range"]:focus::-webkit-slider-thumb {
  transform: scale(1.2);
}

input[type="range"]::-moz-range-thumb {
  -webkit-appearance: none;

  width: var(--slider-handle-size);
  height: var(--slider-handle-size);

  background: var(--orange);

  border: none;
  border-radius: var(--slider-handle-border-radius);

  cursor: pointer;

  transition: transform 0.25s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

input[type="range"]:hover::-moz-range-thumb,
input[type="range"]:focus::-moz-range-thumb {
  transform: scale(1.2);
}

/*

    TRACK

*/

input[type="range"]::-webkit-slider-runnable-track {
  width: 100%;
  height: var(--slider-track-height);

  cursor: pointer;

  background: none;

  border-radius: var(--slider-track-border-radius);
}

input[type="range"]::-moz-range-track {
  width: 100%;
  height: var(--slider-track-height);

  cursor: pointer;

  background: none;

  border-radius: var(--slider-track-border-radius);
}

input[type="range"]:focus::-webkit-slider-runnable-track {
  background: none;
}
input[type="range"]:active::-webkit-slider-runnable-track {
  background: none;
}

.ingredient-select{
  padding-left: 200px;
}

.add-btn{
  padding-left: 365px;
  padding-bottom: 0px;
}


/* 등록, 취소 */
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
  bottom: 30px;
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
</style>