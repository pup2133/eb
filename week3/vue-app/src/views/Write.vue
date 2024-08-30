<template>
  <form ref="writeForm">
    <table style="border: 1px">
      <tr>
        <th>카테고리</th>
        <td>
          <select name="categoryId" v-model="postFormState.categoryId">
            <option value="">카테고리 선택</option>
            <option v-for="category in postFormState.categories"
                    v-bind:value="category.categoryId">
              {{ category.categoryName }}
            </option>
          </select>
        </td>
      </tr>
      <tr>
        <th>작성자</th>
        <td>
          <input name="postWriter" type="text" v-model="postFormState.postWriter">
        </td>
      </tr>
      <tr>
        <th>비밀번호*</th>
        <td>
          <input name="postPassword" type="password" placeholder="비밀번호" v-model="postFormState.postPassword">
          <input name="confirmPassword" type="password" placeholder="비밀번호 확인" v-model="postFormState.confirmPassword">
        </td>
      </tr>
      <tr>
        <th>제목*</th>
        <td>
          <input name="postTitle" type="text" v-model="postFormState.postTitle">
        </td>
      </tr>
      <tr>
        <th>내용*</th>
        <td>
          <textarea name="postContent" v-model="postFormState.postContent"></textarea>
        </td>
      </tr>
      <tr>
        <th>파일첨부*</th>
        <td>
          <input v-for="file in 3" v-bind:key="file" name="file" type="file">
        </td>
      </tr>
      <tr>
        <td colspan="2">
          <button type="button" @click="goToList">취소</button>
          <button type="button" @click="savePost">저장</button>
        </td>
      </tr>
    </table>
  </form>
</template>

<script setup>
import {writeValid} from '@/modules/validation'
import {useRoute, useRouter} from 'vue-router';
import {ref, onMounted} from 'vue';
import boardService from "@/service/boardService";

const router = useRouter();
const route = useRoute();

/* <form> */
const writeForm = ref();

/**
 * 1. categoryId: 사용자가 입력한 카테고리 ID
 * 2. postWriter: 사용자가 입력한 작성자
 * 3. postPassword: 사용자가 입력한 비밀번호
 * 4. confirmPassword: 사용자가 입력한 확인용 비밀번호
 * 5. postTitle: 사용자가 입력한 제목
 * 6. postContent: 사용자가 입력한 내용
 * 7. categories: 전체 카테고리 목록
 * @type {Ref<UnwrapRef<{postContent: string, postWriter: string, confirmPassword: string, postTitle: string, categories: *[], postPassword: string, categoryId: string}>>}
 */
const postFormState = ref({
  categoryId: "",
  postWriter: "",
  postPassword: "",
  confirmPassword: "",
  postTitle: "",
  postContent: "",
  categories: []
});

/* 컴포넌트 마운트 된 후 실행 */
onMounted(() => {
  getWrite();
});

/**
 * 1. boardService.getCategories() 호출하여 데이터 가져옴
 * 2. 가져온 데이터로 postFormState 상태 변경
 * @returns {Promise<void>}
 */
const getWrite = async () => {
  try {
    const response = await boardService.getCategories();
    postFormState.value.categories = response.data;
  } catch (error) {
  }
};

/**
 * 1. 검증 실행
 * 2. boardService.savePost(writeForm) 호출 후 게시글이 저장되면
 * 3. 쿼리 파라미터를 초기화 하고 게시글 목록으로 이동
 * @returns {Promise<void>}
 */
const savePost = async () => {
  try {
    if (!writeValid(postFormState.value)) return;
    await boardService.savePost(writeForm);
    await router.push({name: "List", query: {}});
  }catch (error){
  }
};

/* router, route 넘겨서 게시글 목록 페이지로 이동*/
const goToList = () => {
  boardService.goToList(router, route);
};
</script>